package cafe.adriel.hal.sample.turnstile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.hal.HAL

class TurnstileViewModel : ViewModel(), HAL.StateMachine<TurnstileAction, TurnstileState> {

    override val hal by HAL(viewModelScope, TurnstileState.Locked) { action, transitionTo ->
        when (action) {
            is TurnstileAction.InsertCoin -> transitionTo(TurnstileState.Unlocked)
            is TurnstileAction.Push -> transitionTo(TurnstileState.Locked)
        }
    }
}
