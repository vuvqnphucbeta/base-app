# Android File Transfer

## CI/CD with GitHub Actions

The `Android CI/CD` workflow builds and runs unit tests for pull requests and pushes to `main` or `master`, then stores the debug APK as a workflow artifact. Push a version tag such as `v1.2.3` to build a signed release AAB, publish it to the Google Play internal track, and attach it to a GitHub Release.

Add these repository Actions secrets before using the workflow:

- `KEY_PACKAGE_GIT`: GitHub Packages read token for the private Android library Maven repository.
- `APP_ID`: AdMob app ID. Gradle exposes it as the `admob_app_id` string resource for both debug and release builds.
- `SIGNING_KEYSTORE_BASE64`: Base64 encoded release keystore (`base64 -i release-key.jks | tr -d '\n'`).
- `SIGNING_KEYSTORE_PASSWORD`, `SIGNING_KEY_ALIAS`, `SIGNING_KEY_PASSWORD`: release signing credentials.
- `SERVICE_ACCOUNT_JSON`: Google Play service account JSON with permission to publish the app.

Create a release by pushing a tag, for example:

```sh
git tag v1.2.3
git push origin v1.2.3
```

-- Build APK release
```sh
./gradlew clean assembleRelease
```
-- Build Release AAB
```sh
./gradlew clean bundleRelease

