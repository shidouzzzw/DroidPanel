# Release signing

Debug APKs from CI are signed with the default debug key and are for testing only.

For a release build:
1. Generate a keystore locally (`keytool -genkeypair ...`). Never commit it.
2. Store the keystore (base64) and passwords as GitHub Actions **secrets**.
3. Add a separate release workflow that decodes the secrets at build time and signs the release APK/AAB.

This repository contains no keys, credentials or secrets, and `.gitignore` excludes `*.jks`, `*.keystore` and `keystore.properties`.
