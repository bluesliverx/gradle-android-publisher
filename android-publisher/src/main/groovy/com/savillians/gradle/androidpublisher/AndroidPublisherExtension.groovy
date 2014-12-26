package com.savillians.gradle.androidpublisher;

/**
 * Created by bsaville on 8/14/2014.
 */
public class AndroidPublisherExtension {

	/**
	 * Specify the name of your application. If the application name is
	 * {@code null} or blank, the application will log a warning. Suggested
	 * format is "MyCompany-Application/1.0".
	 */
	String applicationName

	/**
	 * Specify the package name of the app.
	 */
	String packageName

	/**
	 * Authentication.
	 * <p>
	 * Installed application: Leave this string empty and copy or
	 * edit resources/client_secrets.json.
	 * </p>
	 * <p>
	 * Service accounts: Enter the service
	 * account email and add your key.p12 file to the resources directory.
	 * </p>
	 */
	String serviceAccountEmail

	/**
	 * The file that contains the service account key information.
	 */
	File serviceAccountKeyFile

	/**
	 * The name of the variant to use, defaults to "release".  The variant is a combination of the flavor and build
	 * type and has the format "flavorBuildType", such as "fullRelease".
	 */
	String variantName = "release"

	/**
	 * The track to deploy to, defaults to "alpha".
	 */
	String track = "alpha"

	/**
	 * The track to promote to, default to "beta".
	 */
	String promotionTrack = "beta"
}
