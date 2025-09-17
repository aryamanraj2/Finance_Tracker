# Firebase Setup for Google Sign-In

## Steps to configure Firebase for Google Sign-In:

### 1. Create Firebase Project
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or use existing one
3. Add an Android app with package name: `com.nsutrack.financetracker`

### 2. Download Configuration File
1. Download the `google-services.json` file from Firebase Console
2. Replace the placeholder `google-services.json` file in the `app/` directory
3. Make sure the package name matches: `com.nsutrack.financetracker`

### 3. Enable Authentication
1. In Firebase Console, go to Authentication > Sign-in method
2. Enable Google sign-in provider
3. Add your support email

### 4. Get Web Client ID
1. In Firebase Console, go to Project Settings
2. Scroll down to "Your apps" section
3. Click on your Android app
4. In the "Web API Key" section, copy the Web Client ID
5. Replace `YOUR_WEB_CLIENT_ID` in `GoogleAuthUiClient.kt` with this value

### 5. Add SHA-1 Fingerprint
For debug builds, you need to add your debug SHA-1 fingerprint:

```bash
# Get debug keystore fingerprint
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```

1. Copy the SHA-1 fingerprint from the output
2. In Firebase Console, go to Project Settings > Your apps
3. Click "Add fingerprint" and paste the SHA-1

### 6. For Release Builds
When building release APKs, you'll need to:
1. Generate a release keystore
2. Get the SHA-1 fingerprint of the release keystore
3. Add that fingerprint to Firebase Console

## Current Issues Fixed:

1. ✅ Added Firebase Authentication dependencies
2. ✅ Added Google Services plugin 
3. ✅ Created proper GoogleAuthUiClient with Firebase integration
4. ✅ Added error handling for authentication flow
5. ✅ Created Profile screen with user data display
6. ✅ Made profile icon clickable in dashboard
7. ✅ Added sign out functionality

## Required Manual Steps:

1. ⚠️ Replace placeholder `google-services.json` with real file from Firebase Console
2. ⚠️ Update `YOUR_WEB_CLIENT_ID` in `GoogleAuthUiClient.kt` with actual Web Client ID
3. ⚠️ Add SHA-1 fingerprint to Firebase Console

Without these steps, Google Sign-In will still crash. The placeholder configuration is provided to allow the project to build.