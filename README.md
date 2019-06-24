[![JitPack](https://img.shields.io/jitpack/v/github/adrielcafe/hal.svg?style=for-the-badge)](https://jitpack.io/#adrielcafe/hal) 
[![Android API](https://img.shields.io/badge/api-16%2B-brightgreen.svg?style=for-the-badge)](https://android-arsenal.com/api?level=16) 
[![Bitrise](https://img.shields.io/bitrise/29bfee3f365ee4b9/master.svg?style=for-the-badge&token=AWE1QrlM0cgnpevpS1Tmrw)](https://app.bitrise.io/app/29bfee3f365ee4b9) 
[![Codacy](https://img.shields.io/codacy/grade/590119aba1d14ea38908d6c1c8c11f07.svg?style=for-the-badge)](https://www.codacy.com/app/adriel_cafe/hal) 
[![kotlin](https://img.shields.io/github/languages/top/adrielcafe/hal.svg?style=for-the-badge)](https://kotlinlang.org/) 
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg?style=for-the-badge)](https://ktlint.github.io/) 
[![License MIT](https://img.shields.io/github/license/adrielcafe/hal.svg?style=for-the-badge&color=yellow)](https://opensource.org/licenses/MIT) 

# ![logo](https://github.com/adrielcafe/hal/blob/master/hal-logo.png?raw=true) HAL

🔴 **HAL** is a minimalistic [finite-state machine](https://en.wikipedia.org/wiki/Finite-state_machine) for Android &amp; JVM built with [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) and [LiveData](https://developer.android.com/topic/libraries/architecture/livedata).

This project started as a library module in one of my personal projects, but I decided to open source it and add more features for general use. Hope you like!

[![turnstile diagram](https://github.com/adrielcafe/hal/blob/master/turnstile-diagram.jpg?raw=true)](https://www.smashingmagazine.com/2018/01/rise-state-machines/)

## Usage

First, declare your `Action`s and `State`s. They **must** implement `HAL.Action` and `HAL.State` respectively.

```kotlin
sealed class MyAction : HAL.Action {

    object LoadPosts : MyAction()
    
    data class AddPost(val post: Post) : MyAction()
}

sealed class MyState : HAL.State {

    object Init : MyState()
    
    object Loading : MyState()
    
    data class PostsLoaded(val posts: List<Post>) : MyState()
    
    data class Error(val message: String) : MyState()
}
```

Next, implement the `HAL.StateMachine<YourAction, YourState>` interface in your `ViewModel`, `Presenter`, `Controller` or similar.

The `HAL` class receives the following parameters:
* A [`CoroutineScope`](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-coroutine-scope/) (tip: use the [built in viewModelScope](https://developer.android.com/topic/libraries/architecture/coroutines#viewmodelscope))
* A initial state
* A reducer function, `suspend (action: A, transitionTo: (S) -> Unit) -> Unit`, where:
    - `suspend`: the reducer runs inside a coroutine scope on a [default dispatcher](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-coroutine-dispatcher/), so you can run IO and other complex tasks without worrying about block the Main Thread
    - `action: A`: the action emitted to the state machine 
    - `transitionTo: (S) -> Unit`: the function responsible for changing the state

You should handle all actions inside the reducer function. Call `transitionTo()` whenever you need to change the state (it can be called multiple times).

```kotlin
class MyViewModel(private val postRepository: PostRepository) 
    : ViewModel(), HAL.StateMachine<MyAction, MyState> {

    override val hal by HAL(viewModelScope, MyState.Init) { action, transitionTo ->
        when (action) {
            is MyAction.LoadPosts -> {
                transitionTo(MyState.Loading)
                
                try {
                    // You can run suspend functions
                    val posts = postRepository.getPosts()
                    // And emit multiple states per action
                    transitionTo(MyState.PostsLoaded(posts))
                } catch(e: Exception) {
                    transitionTo(MyState.Error("Ops, something went wrong."))
                }
            }
            
            is MyAction.AddPost -> {
                /* Handle action */
            }
        }
    }
}
```

Finally, inside your view class (`Activity`, `Fragment` or similar) you can emit actions to your state machine and observe state changes.

If you want to use a [LiveData-based state observer](https://github.com/adrielcafe/HAL/blob/master/hal-livedata/src/main/kotlin/cafe/adriel/hal/livedata/observer/LiveDataStateObserver.kt), just pass your `LifecycleOwner` to `observeState()`, otherwise HAL will use a default [Callback-based state observer](https://github.com/adrielcafe/HAL/blob/master/hal-core/src/main/kotlin/cafe/adriel/hal/observer/CallbackStateObserver.kt) (which is best suited for JVM-only applications).

```kotlin
class MyActivity : AppCompatActivity() {

    private val viewModel by viewModels<MyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
    
        // Easily emit actions to your State Machine
        loadPostsBt.setOnClickListener {
            viewModel + MyState.LoadPosts
        }
        
        // Observe and handle state changes backed by a LiveData
        viewModel.observeState(this) { state ->
            when (state) {
                is MyState.Init -> showWelcomeMessage()
                
                is MyState.Loading -> showLoading()
                
                is MyState.PostsLoaded -> showPosts(state.posts)
                
                is MyState.Error -> showError(state.message)
            }
        }
    }
}
```

### Custom StateObserver

If needed, you can easily create your custom state observers by implementing the `StateObserver<State>` interface:

```kotlin
class MyCustomStateObserver<S : HAL.State>(
    private val myAwesomeParam: MyAwesomeClass,
    override val observer: (S) -> Unit
) : StateObserver<S> {

    override fun transitionTo(newState: S) {
        // Do any kind of operation and call `observer(newState)` in the end
        // IMPORTANT: this method runs on the Main Thread!
    }
}
``` 

And to use, just create an instance of it and pass to `observeState()` function: 

```kotlin
viewModel.observeState(MyCustomStateObserver(myAwesomeParam) { state ->
    // Handle state
})
``` 

## Import to your project
1. Add the JitPack repository in your root build.gradle at the end of repositories:
```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

2. Next, add the desired dependencies to your module:
```gradle
dependencies {
    // Core with default state observer
    implementation "com.github.adrielcafe.hal:hal-core:$currentVersion"

    // LiveData state observer
    implementation "com.github.adrielcafe.hal:hal-livedata:$currentVersion"
}
```
Current version: [![JitPack](https://img.shields.io/jitpack/v/github/adrielcafe/hal.svg?style=flat-square)](https://jitpack.io/#adrielcafe/hal)

### Platform compatibility

|         | `hal-core` | `hal-livedata` |
|---------|------------|----------------|
| Android | ✓          | ✓              |
| JVM     | ✓          |                |
