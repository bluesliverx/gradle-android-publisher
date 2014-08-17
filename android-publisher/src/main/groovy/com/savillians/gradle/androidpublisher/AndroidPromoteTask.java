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
import com.google.api.services.androidpublisher.AndroidPublisher.Edits.Get;
import com.google.api.services.androidpublisher.AndroidPublisher.Edits.Tracks.Update;
import com.google.api.services.androidpublisher.model.AppEdit;
import com.google.api.services.androidpublisher.model.Track;

import org.gradle.api.DefaultTask;
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

/**
 * Uploads an apk to the alpha track.
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
//		try {
//			// Create the API service
//			AndroidPublisher service = AndroidPublisherHelper.init(
//					publisherExtension.getApplicationName(),
//					publisherExtension.getServiceAccountEmail(),
//					publisherExtension.getServiceAccountKeyFile()
//			);
//			final Edits getEdits = service.edits();
//			final Edits promoteEdits = service.edits();
//
//			// Create a get edit to get the latest APK version code
//			Insert getRequest = getEdits.insert(publisherExtension.getPackageName(), null);
//			AppEdit get = getRequest.execute();
//			final String getId = get.getId();
//			getLogger().info(String.format("Created get edit with id: %s", getId));
//			Get getRequest = getEdits.apklistings().
//
//			// Create a new edit to make changes to your listing
//			Insert editRequest = edits
//					.insert(publisherExtension.getPackageName(), null /** no content */);
//			AppEdit edit = editRequest.execute();
//			final String editId = edit.getId();
//			getLogger().info(String.format("Created edit with id: %s", editId));
//
//			// Assign apk to alpha track.
//			List<Integer> apkVersionCodes = new ArrayList<Integer>();
//			apkVersionCodes.add(apk.getVersionCode());
//			Update updateTrackRequest = edits
//					.tracks()
//					.patch()
//					.update(publisherExtension.getPackageName(),
//							editId,
//							publisherExtension.getTrack(),
//							new Track().setVersionCodes(apkVersionCodes));
//			Track updatedTrack = updateTrackRequest.execute();
//			getLogger().info(String.format("Track %s has been updated", updatedTrack.getTrack()));
//
//			// Commit changes for edit.
//			Commit commitRequest = edits.commit(publisherExtension.getPackageName(), editId);
//			AppEdit appEdit = commitRequest.execute();
//			getLogger().info(String.format("App edit with id %s has been committed", appEdit.getId()));
//
//		} catch (IOException e) {
//			throw new InvalidUserDataException(
//					String.format("Exception was thrown while uploading APK to the %s track: %s",
//							publisherExtension.getTrack(), e.getMessage()),
//					e);
//		} catch (GeneralSecurityException e) {
//			throw new InvalidUserDataException(
//					String.format("Exception was thrown while uploading APK to the %s track: %s",
//							publisherExtension.getTrack(), e.getMessage()),
//					e);
//		}
	}
}