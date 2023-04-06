## LifeCycle Aware Components

* Most of the code is written inside Activity LifeCycle Methods - onCreate, onResume,
onPause etc. Due to this, Activity has multiple responsibilities.
* But there are scenarios where we want to take actions based on the activity lifecyle.
* For e.g.
	= Access the User's Location.
	= Playing Video.
	= Downloading Images.


It mainly consists of two Components :- <br>
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

 ## View Model Factory

* While View Models are responsible for managing the state and behavior of a specific view, the responsibility of creating instances of those View Models can be delegated to a separate component known as the View Model Factory. Here are a few reasons why we might use a View Model Factory:

* Decoupling View Model creation from usage: By delegating the responsibility of creating View Models to a factory, we can decouple the creation of the View Model from its usage. This allows us to easily change the way View Models are created or configure View Models differently depending on context.

* Enabling Dependency Injection: View Model factories are often used in combination with Dependency Injection (DI) frameworks, which allow us to easily manage the dependencies of our View Models. By using a factory, we can make sure that View Models are created with the correct dependencies injected, without having to manage those dependencies directly in our View Model classes.

* Supporting View Model lifetime management: In some cases, we may need to manage the lifetime of View Models, for example, by reusing View Models across multiple views or destroying them when they are no longer needed. A View Model Factory can be used to manage the creation and destruction of View Models, ensuring that they are created and disposed of appropriately.

* Promoting Testability: By using a View Model Factory, we can easily create mock or test versions of our View Models in order to unit test our code. This allows us to isolate the behavior of our View Models from the rest of our code and test them in a controlled environment.

* Overall, a View Model Factory helps to promote modularity, testability, and flexibility in our code by separating the concerns of creating View Models from their usage.

<br>
In our Projecct to pass Parameters in our View Model Class for ex: let suppose we want to start count from a specific value and not just from 0 for that to pass parameters in our ViewModel Class we make use of ViewModel factory.

Recall this code :-
```
class MainViewModel: ViewModel() {

    var count:Int=0

    fun increment(){
        count++
    }
}
```

Following Changes were Made :- <br>
Step 1: Creation of MainViewModelFactory.kt Class, using parameters and extending with ViewModelProvider.Factory + Implmenting all compulsory Methods  :- <br>
```
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

class MainViewModelFactory(val counter:Int):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(counter) as T
    }

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return super.create(modelClass, extras)
    }
}
```

Step 2: Make Changes in our MainActivity which is responsible for updating Views:-
```
lateinit var mainViewModel:MainViewModel

mainViewModel=ViewModelProvider(this,MainViewModelFactory(10)).get(MainViewModel::class.java)
```
That's it ;)

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