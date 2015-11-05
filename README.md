A simple gradle plugin that publishes android builds to any track in the Google Play store.  Flavors and variants
are supported as well.

To use, add the following to your ```build.gradle``` file:

```
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'com.savillians.gradle:android-publisher:0.5+'
        // May need to add guava if you have problems with com.google.common.hash.HashCode.fromString(Ljava/lang/String;)Lcom/google/common/hash/HashCode;
        //classpath 'com.google.guava:guava:18.0'
    }
}

apply plugin: com.savillians.gradle.androidpublisher.AndroidPublisherPlugin

android {
    ...
}
androidPublisher {
    applicationName = "Company-Name-Product-Name/1.0"
    packageName = "<package name>"
    serviceAccountEmail = "<service account email>"
    serviceAccountKeyFile = file('<p12 keyfile - NOT the json file>')
    track = "alpha" // default, don't need to specify
    variantName = "release" // default, don't need to specify
}
```

Make sure the service account you create has "release manager" permissions, download the p12 key file and put it in
the project's directory. Then run this command:

```gradle androidPublish```
OR
```./gradlew androidPublish```

That will send it to Google Play using the credentials you specified.

# Flavors

The `variantName` parameter is a combination of the flavor and build type.  If no flavors are used, it is comprised
entirely of the build type.  For example, if there are 2 flavors, "full" and "demo", and two build types, "debug" and
"release", then the potential variant names are:

* fullDebug
* fullRelease
* demoDebug
* demoRelease

# Changelog

### 0.5

* Fix issues with latest version of Gradle Android plugin (#6)

### 0.3

* Fix bug with not finding the correct APK file for publishing

### 0.2

* Compatible with android gradle plugin 0.14

### 0.1

* Initial release
