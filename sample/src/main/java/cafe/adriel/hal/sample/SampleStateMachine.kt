package cafe.adriel.hal.sample

import cafe.adriel.hal.HAL

sealed class SampleAction : HAL.Action {
    object InsertCoin : SampleAction()
    object Push : SampleAction()
}

sealed class SampleState : HAL.State {
    object Locked : SampleState()
    object Unlocked : SampleState()
}
