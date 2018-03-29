# AndroidSupportLib
Library developed to assist in the development of Android applications, with several classes and helper methods.

How to use AndroidSupportLib in your Android Project:

Step 1. Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}

Step 2. Add the dependency

	dependencies {
	        compile 'com.github.helpdeveloper:AndroidSupportLib:2.0.0'
	}
