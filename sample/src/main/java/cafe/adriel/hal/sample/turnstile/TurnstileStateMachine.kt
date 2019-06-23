package cafe.adriel.hal.sample.turnstile

import cafe.adriel.hal.HAL

sealed class TurnstileAction : HAL.Action {
    object InsertCoin : TurnstileAction()
    object Push : TurnstileAction()
}

sealed class TurnstileState : HAL.State {
    object Locked : TurnstileState()
    object Unlocked : TurnstileState()
}
