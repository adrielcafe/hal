[![JitPack](https://img.shields.io/jitpack/v/github/adrielcafe/hal.svg?style=for-the-badge)](https://jitpack.io/#adrielcafe/hal) 
[![Android API](https://img.shields.io/badge/api-16%2B-brightgreen.svg?style=for-the-badge)](https://android-arsenal.com/api?level=16) 
[![Bitrise](https://img.shields.io/bitrise/29bfee3f365ee4b9/master.svg?style=for-the-badge&token=AWE1QrlM0cgnpevpS1Tmrw)](https://app.bitrise.io/app/29bfee3f365ee4b9) 
[![Codacy](https://img.shields.io/codacy/grade/590119aba1d14ea38908d6c1c8c11f07.svg?style=for-the-badge)](https://www.codacy.com/app/adriel_cafe/hal) 
[![Codecov](https://img.shields.io/codecov/c/github/adrielcafe/hal/master.svg?style=for-the-badge)](https://codecov.io/gh/adrielcafe/hal) 
[![kotlin](https://img.shields.io/github/languages/top/adrielcafe/hal.svg?style=for-the-badge)](https://kotlinlang.org/) 
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg?style=for-the-badge)](https://ktlint.github.io/) 
[![License MIT](https://img.shields.io/github/license/adrielcafe/hal.svg?style=for-the-badge&color=yellow)](https://opensource.org/licenses/MIT) 

<p align="center">
    <img width="200px" height="200px" src="https://github.com/adrielcafe/hal/blob/master/hal-logo.png?raw=true">
</p>

### **HAL** is a non-deterministic [finite-state machine](https://en.wikipedia.org/wiki/Finite-state_machine) for Android &amp; JVM built with [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) and [LiveData](https://developer.android.com/topic/libraries/architecture/livedata).

#### Why non-deterministic?

Because in a [non-deterministic](https://www.tutorialspoint.com/automata_theory/non_deterministic_finite_automaton.htm) finite-state machine, an action can lead to *one*, *more than one*, or *no transition* for a given state. That way we have more flexibility to handle any kind of scenario.

Use cases:
* InsertCoin `transition to` Unlocked
* LoadPosts `transition to` Loading then `transition to` Success or Error
* LogMessage `don't transition` 

[![turnstile diagram](https://github.com/adrielcafe/hal/blob/master/turnstile-diagram.jpg?raw=true)](https://www.smashingmagazine.com/2018/01/rise-state-machines/)
    
#### Why HAL?

It's a tribute to [HAL 9000](https://en.wikipedia.org/wiki/HAL_9000) (**H**euristically programmed **AL**gorithmic computer), the sentient computer that controls the systems of the [Discovery One](https://en.wikipedia.org/wiki/Discovery_One) spacecraft. 

<p align="center">
    <i>"I'm sorry, Dave. I'm afraid I can't do that." (HAL 9000)</i>
</p>

---

This project started as a library module in one of my personal projects, but I decided to open source it and add more features for general use. Hope you like!

## Usage

First, declare your `Action`s and `State`s. They *must* implement `HAL.Action` and `HAL.State` respectively.

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
* An initial state
* An *optional* capacity to specify the [Channel buffer size](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.channels/-channel/index.html) (default is [Channel.UNLIMITED](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.channels/-channel/-u-n-l-i-m-i-t-e-d.html))
* An *optional* [CoroutineDispatcher](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-coroutine-dispatcher/index.html) to run the reducer function (default is [Dispatcher.DEFAULT](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-dispatchers/-default.html))
* A reducer function, `suspend (action: A, transitionTo: (S) -> Unit) -> Unit`, where:
    - `suspend`: the reducer runs inside a `CoroutineScope`, so you can run IO and other complex tasks without worrying about block the Main Thread
    - `action: A`: the action emitted to the state machine 
    - `transitionTo: (S) -> Unit`: the function responsible for changing the state

You should handle all actions inside the reducer function. Call `transitionTo()` whenever you need to change the state (it can be called multiple times).

```kotlin
class MyViewModel(private val postRepository: PostRepository) : ViewModel(), HAL.StateMachine<MyAction, MyState> {

    override val hal by HAL(viewModelScope, MyState.Init) { action, transitionTo ->
        when (action) {
            is MyAction.LoadPosts -> {
                transitionTo(MyState.Loading)
                
                try {
                    // You can run suspend functions without blocking the Main Thread
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

Finally, choose a class to emit actions to your state machine and observe state changes, it can be an `Activity`, `Fragment` or any other class.

If you want to use a [LiveData-based state observer](https://github.com/adrielcafe/HAL/blob/master/hal-livedata/src/main/kotlin/cafe/adriel/hal/livedata/observer/LiveDataStateObserver.kt) (highly recommended if you're on Android), just pass your `LifecycleOwner` to `observeState()`, otherwise HAL will use a default [Callback-based state observer](https://github.com/adrielcafe/HAL/blob/master/hal-core/src/main/kotlin/cafe/adriel/hal/observer/CallbackStateObserver.kt) (which is best suited for JVM-only applications).

```kotlin
class MyActivity : AppCompatActivity() {

    private val viewModel by viewModels<MyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
    
        // Easily emit actions to your State Machine
        loadPostsBt.setOnClickListener {
            viewModel + MyAction.LoadPosts
        }
        
        // Observe and handle state changes backed by a LiveData
        viewModel.observeState(lifecycleOwner = this) { state ->
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

You can easily create your custom state observer by implementing the `StateObserver<State>` interface:

```kotlin
class MyCustomStateObserver<S : HAL.State>(
    private val myAwesomeParam: MyAwesomeClass,
    override val observer: (S) -> Unit
) : StateObserver<S> {

    override fun transitionTo(newState: S) {
        // Do any kind of operation here and call `observer(newState)` in the end
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
    // Core with callback state observer
    implementation "com.github.adrielcafe.hal:hal-core:$currentVersion"

    // LiveData state observer only
    implementation "com.github.adrielcafe.hal:hal-livedata:$currentVersion"
}
```
Current version: [![JitPack](https://img.shields.io/jitpack/v/github/adrielcafe/hal.svg?style=flat-square)](https://jitpack.io/#adrielcafe/hal)

### Platform compatibility

|         | `hal-core` | `hal-livedata` |
|---------|------------|----------------|
| Android | ✓          | ✓              |
| JVM     | ✓          |                |
