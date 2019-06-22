package cafe.adriel.hal.sample

import cafe.adriel.hal.HAL

sealed class SampleAction : HAL.Action {
    object OnButtonClicked : SampleAction()
}

sealed class SampleState : HAL.State {
    data class MessageLoaded(val message: String) : SampleState()
}
