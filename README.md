# Bernie App for Android

This is the Bernie App for Android

Tracker Backlog: https://www.pivotaltracker.com/n/projects/1414762

Slack channel (you can request access [here](https://docs.google.com/forms/d/1pmxGTX17qPkZV49iuLh3rN-Mj_Z6w6M_XtUJMZCMIP4/viewform)): https://codersforsanders.slack.com/messages/bernie-app/details/

# Setting up your build environment

This project compiles using Android Studio and gradle. You should be able to import from
VCS in the Android Studio start-up panel. After importing, you'll need a couple of api keys.
 First off you'll need a Google Maps API key. After following the instructions to get an
 Android api key [here](https://developers.google.com/maps/documentation/android/signup),
 create a new file called key.xml under app/src/main/res/values. Your file should look this

 ```xml
 <?xml version="1.0" encoding="utf-8"?>
 <resources>
     <string name="google_maps_api_key">YOUR_API_KEY_HERE</string>
 </resources>
 ```

 Make sure that this file is not under version control!

 For the time being, you'll also want to get hold of a Firesize api key [here](http://firesize.com/) and once
 you do, place it in the key.xml file. The api key in this case is going to be the top level domain.
 Your final file should look like this:

 ```xml
 <?xml version="1.0" encoding="utf-8"?>
 <resources>
     <string name="google_maps_api_key">GOOGLE_MAPS_API_KEY</string>
     <string name="firesizeacc">FIRE_SIZE_API_KEY</string>
 </resources>
 ```
