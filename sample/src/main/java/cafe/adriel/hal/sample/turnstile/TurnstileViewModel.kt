package cafe.adriel.hal.sample.turnstile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.hal.HAL

class TurnstileViewModel : ViewModel(), HAL.StateMachine<TurnstileAction, TurnstileState> {

    override val stateMachine by HAL(TurnstileState.Locked, viewModelScope) { action ->
        when (action) {
            is TurnstileAction.InsertCoin -> +TurnstileState.Unlocked
            is TurnstileAction.Push -> +TurnstileState.Locked
        }
    }
}
