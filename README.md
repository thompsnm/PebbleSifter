# Pebble Sifter

Pebble / Android app to interface with site scrapers in order to sift out important strings.

## Android Companion App Installation

To install the companion app to your Android device:

* Download and install [Android Studio](https://developer.android.com/sdk/installing/studio.html)
* Import the Pebble Sifter project
* Run the Android configuration [on your device](http://developer.android.com/tools/device.html)

## Pebble App Installation

To deploy the Pebble app to your smart watch:

* Install the [Pebble SDK](https://developer.getpebble.com/2/getting-started/macosx/)
* Use the [Pebble Command Line Tool](https://developer.getpebble.com/2/getting-started/pebble-tool/) to install the app from the pebble_sifter directory

## Creating Your Own Sifter

To create and use your own sifter:

* Create a new class that extends PebbleSifter in the personalSifters directory
  * **Note:** The contents of the personalSifters directory has been added to the .gitignore file. It is highly recommended that you store your sifters in a separate repository to prevent them from being lost when working with PebbleSifter.
* Override the sift(), getFullName(), getPebbleName(), and refresh() methods
* Instantiate your sifter in the DrawApp class under the line that says "As new sifters are implemented, add them here:"
* Re-install the Android Companion App

For examples on how to create a sifter, checkout the exampleSifters directory. All examples use [jsoup](http://jsoup.org) for parsing HTML, but you can use whatever library you would like.

## Contributing

Pull requests to improve this project are more than welcome. Checkout the roadmap.txt file for ideas of what I'd like to do next. No coding conventions have been set; just try to follow the existing conventions you see in the code.