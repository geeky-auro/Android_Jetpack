## LifeCycle Aware Components

* Most of the code is written inside Activity LifeCycle Methods - onCreate, onResume,
onPause etc. Due to this, Activity has multiple responsibilities.
* But there are scenarios where we want to take actions based on the activity lifecyle.
* For e.g.
	= Access the User's Location.
	= Playing Video.
	= Downloading Images.


It mainly consists of two Components :-
	* LifeCycle Owner
	* LifeCycle Observer 

## View Model

* Model for your views such as Activity or Fragment.

* View Models are lifecycle aware.

* Data required for your screen is stored at one place i.e. ViewModel. It may involve
formatting that data in a particular format, accumulating data, any logic in displaying
this data in your Ul.

* The Data we were using in the MainActivity, we shall be using in our ViewModel Class ;)
 they only get destroyed when our activity get destroyed ;)


### Plugins Required:-
```
id 'kotlin-kapt'
```

### buildFeatures
```
    buildFeatures{
        dataBinding true
    }
```

### Dependency Required:-
```
    def room_version = "2.4.3"
    //noinspection GradleDependency
    implementation "androidx.room:room-runtime:$room_version"
    // To use Kotlin annotation processing tool (kapt)
    kapt "androidx.room:room-compiler:$room_version"

    // optional - Kotlin Extensions and Coroutines support for Room
    //noinspection GradleDependency
    implementation "androidx.room:room-ktx:$room_version"
    // Fot Core Dependencies for Core COroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    // For Android Specific Libraries ;)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
```