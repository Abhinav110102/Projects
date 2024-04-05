# **RXpress**

RXpress is a comprehensive pharmaceutical mobile application that will allow users to conveniently track information related to recurring prescriptions and potential medications for themselves and their family. The application is designed to make medication use simpler, easier, and safer for users through the use of various design functionalities (further detailed here: https://github.com/SCCapstone/RXpress/wiki/Design).  
Due to advancements in the field of pharmacology, the variety of medications available can be too intricate and numerous for consumers to safely and effectively utilize all the medicine they need. There is a rising need for a user-friendly application to cater to information regarding drug interactions, dosage frequency, drug usage, and other general knowledge.

## External Requirements

There are various ways to build the project. We recommend using Android Studio. It only requires:

- [Android Studio] (https://developer.android.com/studio?gclid=Cj0KCQjwk7ugBhDIARIsAGuvgPbU2eqrMuRblcyewjeNa5L-J7fLv0qtBT9PlIBo95HyxpGsJetnGUMaAj_aEALw_wcB&gclsrc=aw.ds)
- [Java Development Kit] (https://techpassmaster.com/install-setup-android-studio-java-jdk-sdk/)
- [Android SDK] (https://techpassmaster.com/install-setup-android-studio-java-jdk-sdk/)

## Downloading Android Studio Linux:
1. In order to download android studio visit this site here https://developer.android.com/studio/?gclid=CjwKCAjwzNOaBhAcEiwAD7Tb6M0YFFxHoxSksK9TvKzpjvj7BLae3METQdU2saXCccq9tC-Xako3CxoCttoQAvD_BwE&gclsrc=aw.ds click the button that says Download Andriod Studio    
2. When download is complete you will click next  
3. Then you will click standard installation or custom optional  
4. check the Download components such as 'Android Emulator' 'Android SDK Build-Tools' 'Android SDK Platform' 'Android SDK Platform-Tools' 'Emulator Accelerator' 'SDK Patch Applier' and 'Sources for Android' , then click finish.
5. If on linux or windows you will edit your
``` 
vim ~./bash_profile
```
6. add the line 
```
export ANDROID_SDK=/Users/myuser/Library/Android/sdk
```
To get the path in the instruction above open the SDK Manager and the path is at the top of the window. Copy this path and paste it in the ./~bash_profile
7. Also you will need to add platform-tools to the 
```
vim ./~bash_profile
```
```
PATH=/Users/myuser/Library/Android/sdk/platform-tools:$PATH
```
note: the path is the same as your SDK in step 6.
8. run in terminal the command below. if there is an error the path was put in incorrectly  
``` 
adb
```  

## Setup

To run the app on the android studio emulator you will open up android studio and:  

1. Open Up SDK Manager  
2. SDK Platform should have the most up to date version of android i.e. Tiramisu.
3. SDK Tools 'Android Emulator' 'Android SDK Platform-Tools' and the Emulator Accelorator should all be selected. If not selected select them and android studio will download them for you 
4. Go to ADV Manager in android studio
5. Click Create Virtual Device  
6. Select the Hardware that you want to emulate
7. click next
8. Select the latest stable operating system.
9. Click next.
10. You Can now give it a name and click finish
## Running

Once the repo is cloned, the app can be easily run within Android Studio by selecting the green build and run button with the repository selected. 

# Deployment

# Testing

## Testing Technology
Android Studio is the testing technology used to test our app. 
We need to add to these dependencies to build.gradle app to be able to use testing feature provided by android studios: 
```
androidTestImplementation 'androidx.test.ext:junit:1.1.5'  
androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
testImplementation("junit:junit:4.13.2")
androidTestImplementation("io.github.kakaocup:kakao:3.0.4")
androidTestImplementation("androidx.navigation:navigation-testing:2.5.3")
testImplementation 'org.mockito:mockito-core:2.24.5'
debugImplementation("androidx.fragment:fragment-testing:1.6.0-alpha04")
testImplementation 'org.json:json:20180813'
androidTestImplementation 'org.mockito:mockito-android:2.24.5'
implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
implementation "androidx.navigation:navigation-ui-ktx:$nav_version"
implementation("androidx.multidex:multidex:2.0.1")
testImplementation "org.mockito:mockito-inline:3.11.2"

```


## Running Tests
Change the sdk.dir owner section in the local properties to local computer name to to be able to run the test: sdk.dir=C\:\\Users\\OWNER\\AppData\\Local\\Android\\Sdk
Next, ensure that the emulator or device is in developer mode. This can be done on most android devices/emulators by navigating to:
Settings > About emulated device (may be different on physical device) > scroll down to build number > click on build number until prompted that the device is in developer mode.
Once this is done, we need to turn off 3 settings to prevent a bug with some instrumental tests. This can be done by navigating to:
Settings > System > Developer Options > scrolls down to Window animation scale, Transition animation scale, and animator duration scale. Turn all 3 of these off.
### To run the Unit Test: 
Locate the folder under the path 
```
C:\Users\OWNER\AndroidStudioProjects\RXpress\app\src\test\java\com\example\rxpress10\ui\
```
Then Right-Click on the folder and select 'Run Tests in "com.example.rxpress10.ui"'.  
### To run the Behavioral Test:   
Locate the folder under the path
```
C:\Users\OWNER\AndroidStudioProjects\RXpress\app\src\androidTest\java\com\example\rxpress10\
```
Then Right-Click on the folder and select 'Run Tests in "com.example.rxpress10"'.
This one may take a while to complete.
**NOTE:** Sometimes the tests may fail due to pop ups from google or other services asking if you would like to save a password or email. The best way to avoid this is not being signed in to google on your device, and running through the application prior to running tests.

### Behavioral Testing 
**Calendar Instrumented Test**  
• You have to already have a personal event on the calendar in order for the edit screen test will pass.  
• testEditScreen : Test checks If user can navigate on the calendar screen and checks all buttons on calendar.  
• testAddPersonalEventButton: Test checks If user can add personal events to the calendar screen.  
**Drug Interaction Instrumented Test**  
• searchInteractionTest : Test checks if user can search certain medications.  
• testViews: Test checks if search shows a valid description for medication.  
**Edit Profile Instrumented Test**  
• testEditScreen: Test checks if user the is  able to change their profile information.  
**FAQ Instrumented Test**  
• testRecyclerView : Test checks if user can click on the FAQ dropdown Questions.  
• testFAQNavigation: Test checks if user can navigate to the prescription screen from the FAQ Screen.  
**Forgot Password Instrumented Test**  
• loginCheckForgotPassword :Test checks if user is able navigate to Forgot Password Screen from the login Screen.  
• forgotPasswordFieldWork: Test checks if user can use the text Field and buttons on forgot Password Screen.  
**Login Instrumented Test**  
• testLoginScreen: Test checks if user is able enter email and password to login into the account.  
• checkNavigationToRegister: Test checks if user is able navigate to Register Screen from the login Screen.  
• loginWithGoogle: Test checks if user is able navigate to Welcome Screen from the login Screen.  
**Prescription Instrumented Test**  
• testCreatingUser: Test checks if user can create a new user to add prescriptions for.  
**Profile Instrumented Test**  
• testEditProfileButton: Test checks if the user can navigate to edit Profile Fragment Screen  from Profile Fragment Screen.  
**Register Instrumented Test**  
• testRegisterScreenField: Test checks if all field and button are working on Register Screen.  
• checkNavigationToLogin: Test checks if user is able navigate to Login Screen from the register Screen.  
• RegisterWithGoogle: Test checks if user is able navigate to Welcome Screen from the Register Screen.  
**Setting Instrumented Test**  
• signOut: Test checks if user can sign out of their account.  
• goToProfileFromSettings: Test checks if user can navigate from Setting Fragment to Profile Fragment.  
**Welcome Instrumented Test**  
• navigateToLogin: Test checks if user can navigate from Welcome Screen to Login Screen.  
• navigateToRegister: Test checks if user can navigate from Welcome Screen to Register Screen.  
• signUpWithGoogle: Test checks if user is able sign up with google.  

### Unit Testing  
**Add Personal Event Test**  
• Null: Test checks if a personal event is null  
• UserInfoCreated: Test checks if user info was properly added.  
**Calendar Fragment Test**  
• testFormatTime: Test checks if time is formatted properly.  
• testFormatDate: Test checks if the date is formatted properly.  
• testFormatMonth: Test checks if the month if formatted properly.  
• testFormatDay: Test checks if the day is formatted properly.  
**Edit Personal Event Test**  
• Null: This test checks if a null personal event can be added.  
• UserInfoCreated: This test checks if the user info was created properly and the values match.  
**Drug Interaction Test**  
• replaceSpacesTest: Test checks that whitespace before and after is removed.  
• fetchSuggestionTest: Test checks if autocomplete suggestions are working.  
**Prescription Fragment Test**  
• medicationNull: Test checks if an added medication is null.  
• medicationCreated: Test checks if a medication is created properly.  
**Edit Profile Fragment Test**  
• Null: Test checks if UserInfo is null.  
• UserInfoCreated: Test checks if prescription user info is added properly.  
**Register Activity Unit**  
• UserInfoCreated: Test checks if a registered users info is added properly.  


# Authors

Alexis Anderson aka4@email.sc.edu  
Amjad Omer - amomer@email.sc.edu  
Abhinav Myadala - myadala@email.sc.edu  
Brendan Reeder - br17@email.sc.edu
