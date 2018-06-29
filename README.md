# JournalApp
Google Africa Challenge Project

Requirements To Install Peach Notes - Journal App
1. Android 6.0 Marshmallow
2. Existing Google account
3. Android Studio - Version 3.1.3 or Above

Additional Dependencies
1. Google Play Services
2. Firebase Authentication
3. Firebase Core
4. Firebase Database

Installing
1. On Loading the project on Android Studio, open the Firebase Assistant, On Authentication, Click Connect to Firebase, proceed to add a project folder or place the app on any other project folder on Firebase.
2. Go to http://console.firebase.google.com, find the project you added and enable Authentication via Google and via Email.
3. On Android Studio, Navigate to build.gradle(Project) and build.gradle(Module) to ensure all dependencies are up-to-date. Sync your project if any changes are made.

Running the Application
Launch An AVD or Connect an Android Device on Android 6.0 Marshmallow with debugging enabled to the Computer and proceed to run the Application on it. Make sure You Need to test that Login Functions are working because changes in the library dependency cause the Application to crash. Make sure to enable;
a. Permission to Storage from the Device’s Settings.
b. Internet Connection via Wifi or Cellular Data.

Testing the Application
To run the test;
1. Run the application on the device - make sure the application is launched
2. Create A new account with and proceed - Make sure that on continuing, you will get to the introduction page.
3. Tap the proceed to write button and make sure that you see the Toast message below about Backing and confirm that they are all successful - if you you don’t see a success message, it means you haven’t given the app permission  to storage from settings
4. On the action bar, tap on the avatar and sign out so you can login with a google account.
5. Login via google - If you are already logged in on the device with a google account, you will be directed straight ahead to the notes page.
6. Tap on the icon at the bottom centre to create a new note, write a title and a note then save.
7. You’ll Head back to see the note item listed. Open the note and edit it, make sure to see a message confirming you have edited the message.

Authors
Daniel Kioko Nzioka - Android Developer

Inspirations
Thanks to Google, Udacity & Andela for making this project possible through top notch education from the Google Africa Challenge.

Acknowledgments
Flaticon.com - For providing icons and avatars
