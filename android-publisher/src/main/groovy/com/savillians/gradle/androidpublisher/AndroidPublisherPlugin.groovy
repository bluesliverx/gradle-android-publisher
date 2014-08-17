/*
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

package com.savillians.gradle.androidpublisher

import com.android.build.gradle.AppPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AndroidPublisherPlugin implements Plugin<Project> {
	private static final String TASK_GROUP = "Android"

	@Override
	public void apply(Project project) {
		if (!project.getPlugins().findPlugin(AppPlugin.class))
			throw new RuntimeException("The android publisher plugin may only be applied to android application projects (apply: 'android')")

		project.getExtensions().create("androidPublisher", AndroidPublisherExtension.class);

		AndroidPublishTask publishTask = project.getTasks().create("androidPublish", AndroidPublishTask.class);
		publishTask.setGroup(TASK_GROUP);
		publishTask.setDescription("Publishes a release APK to Google Play");
		publishTask.dependsOn(project.getTasksByName("assembleRelease", false));

		AndroidPromoteTask promoteTask = project.getTasks().create("androidPromote", AndroidPromoteTask.class);
		promoteTask.setGroup(TASK_GROUP);
		promoteTask.setDescription("Promotes an APK in Google Play from one track to another");

	}
}