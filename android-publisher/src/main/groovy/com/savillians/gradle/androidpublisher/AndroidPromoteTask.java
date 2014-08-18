/*
 * Adapted from original Google samples code.
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.savillians.gradle.androidpublisher;

import com.google.api.client.repackaged.com.google.common.base.Preconditions;
import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.AndroidPublisher.Edits;
import com.google.api.services.androidpublisher.AndroidPublisher.Edits.Commit;
import com.google.api.services.androidpublisher.AndroidPublisher.Edits.Insert;
import com.google.api.services.androidpublisher.AndroidPublisher.Edits.Tracks;
import com.google.api.services.androidpublisher.AndroidPublisher.Edits.Tracks.Update;
import com.google.api.services.androidpublisher.model.AppEdit;
import com.google.api.services.androidpublisher.model.Track;

import org.gradle.api.DefaultTask;
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Promotes an APK from one track to another.
 */
public class AndroidPromoteTask extends DefaultTask {
	@TaskAction
	public void promote() {
		AndroidPublisherExtension publisherExtension = getAndVerifyExtension();
		promoteApk(publisherExtension);
	}

	private AndroidPublisherExtension getAndVerifyExtension() {
		AndroidPublisherExtension publisherExtension = getProject().getExtensions()
				.getByType(AndroidPublisherExtension.class);

		Preconditions.checkArgument(!Strings.isNullOrEmpty(
						publisherExtension.getApplicationName()),
				"Application name cannot be null or empty!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(
						publisherExtension.getTrack()),
				"Track cannot be null or empty!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(
						publisherExtension.getTrack()),
				"Promotion track cannot be null or empty!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(
						publisherExtension.getPackageName()),
				"Package name cannot be null or empty!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(
						publisherExtension.getServiceAccountEmail()),
				"Service account email cannot be null or empty!");
		Preconditions.checkArgument(publisherExtension.getServiceAccountKeyFile() != null,
				"Service account key file cannot be null or empty!");

		return publisherExtension;
	}

	private void promoteApk(AndroidPublisherExtension publisherExtension) {
		try {
			// Create the API service
			AndroidPublisher service = AndroidPublisherHelper.init(
					publisherExtension.getApplicationName(),
					publisherExtension.getServiceAccountEmail(),
					publisherExtension.getServiceAccountKeyFile()
			);
			final Edits edits = service.edits();

			// Create a new edit to make changes to your listing
			Insert editRequest = edits
					.insert(publisherExtension.getPackageName(), null /** no content */);
			AppEdit edit = editRequest.execute();
			final String editId = edit.getId();
			getLogger().info(String.format("Created edit with id: %s", editId));

			// List all tracks
			Tracks.List list = edits.tracks().list(publisherExtension.getPackageName(), editId);
			List<Track> tracks = list.execute().getTracks();

			// Identify the source and destination tracks
			Track sourceTrack = null;
			Track destinationTrack = null;
			for (Track track : tracks) {
				if (track.getTrack().equals(publisherExtension.getTrack()))
					sourceTrack = track;
				else if (track.getTrack().equals(publisherExtension.getPromotionTrack()))
					destinationTrack = track;
			}

			// Error checking
			if (sourceTrack==null || destinationTrack==null) {
				throw new InvalidUserDataException(String.format(
						"Cannot find the %s or %s track on Google Play, invalid track name?",
						publisherExtension.getTrack(), publisherExtension.getPromotionTrack()
				));
			}
			if (sourceTrack.getVersionCodes().size()==0) {
				throw new InvalidUserDataException(String.format(
						"Cannot find a valid APK version code for the %s track, does it have at least one APK already uploaded?",
						sourceTrack.getVersionCodes()
				));
			}
			getLogger().info("Using source track {} with version codes {}",
					sourceTrack.getTrack(), sourceTrack.getVersionCodes());
			getLogger().info("Using destination track {} with version codes {}",
					destinationTrack.getTrack(), destinationTrack.getVersionCodes());

			// Find version code to promote, remove from source track and add to destination track
			Integer versionCode = Collections.max(sourceTrack.getVersionCodes());
			List<Integer> sourceVersionCodes = sourceTrack.getVersionCodes();
			sourceVersionCodes.remove(versionCode);
			sourceTrack.setVersionCodes(sourceVersionCodes);
			List<Integer> destinationVersionCodes = new ArrayList<Integer>();
			destinationVersionCodes.add(versionCode);
			destinationTrack.setVersionCodes(destinationVersionCodes);
			getLogger().info("Promoting version code {}", versionCode);

			Update sourceUpdateRequest = edits
					.tracks()
					.update(publisherExtension.getPackageName(),
							editId,
							sourceTrack.getTrack(), sourceTrack);
			sourceUpdateRequest.execute();
			getLogger().info(String.format("Source track %s has been updated", sourceTrack.getTrack()));
			Update destinationUpdateRequest = edits
					.tracks()
					.update(publisherExtension.getPackageName(),
							editId,
							destinationTrack.getTrack(), destinationTrack);
			getLogger().info(String.format("Destination track %s has been updated",
					destinationTrack.getTrack()));
			destinationUpdateRequest.execute();

			// Commit changes for edit.
			Commit commitRequest = edits.commit(publisherExtension.getPackageName(), editId);
			AppEdit appEdit = commitRequest.execute();
			getLogger().info(String.format("App edit with id %s has been committed", appEdit.getId()));
			getLogger().lifecycle("Version code {} has been promoted from the {} to the {} track",
					versionCode, sourceTrack.getTrack(), destinationTrack.getTrack());
		} catch (IOException e) {
			throw new InvalidUserDataException(
					String.format("Exception was thrown while promoting APK from the %s track to the %s track: %s",
							publisherExtension.getTrack(),
							publisherExtension.getPromotionTrack(),
							e.getMessage()),
					e);
		} catch (GeneralSecurityException e) {
			throw new InvalidUserDataException(
					String.format("Exception was thrown while promoting APK from the %s track to the %s track: %s",
							publisherExtension.getTrack(),
							publisherExtension.getPromotionTrack(),
							e.getMessage()),
					e);
		}
	}
}