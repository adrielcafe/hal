package cafe.adriel.hal.sample.turnstile

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cafe.adriel.hal.livedata.observer.observeState
import cafe.adriel.hal.observeState
import cafe.adriel.hal.plusAssign
import cafe.adriel.hal.sample.R
import kotlinx.android.synthetic.main.activity_turnstile.*

class TurnstileActivity : AppCompatActivity(R.layout.activity_turnstile) {

    private val viewModel by viewModels<TurnstileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vInsertCoin.setOnClickListener {
            viewModel += TurnstileAction.InsertCoin
        }
        vPush.setOnClickListener {
            viewModel += TurnstileAction.Push
        }

        viewModel.observeState(this) { state ->
            when (state) {
                is TurnstileState.Locked -> updateState(locked = true)
                is TurnstileState.Unlocked -> updateState(locked = false)
            }
        }
    }

    private fun updateState(locked: Boolean) {
        vTurnstileState.setImageResource(
            if (locked) R.drawable.ic_lock_close
            else R.drawable.ic_lock_open
        )

        vInsertCoin.isEnabled = locked
        vPush.isEnabled = locked.not()
    }
}
