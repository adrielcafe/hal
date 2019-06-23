package cafe.adriel.hal.sample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.hal.HAL

class SampleViewModel : ViewModel(), HAL.StateMachine<SampleAction, SampleState> {

    override val hal by HAL(viewModelScope, SampleState.Locked, ::reducer)

    private suspend fun reducer(action: SampleAction, transitionTo: suspend (SampleState) -> Unit) =
        when (action) {
            is SampleAction.InsertCoin -> transitionTo(SampleState.Unlocked)
            is SampleAction.Push -> transitionTo(SampleState.Locked)
        }
}
