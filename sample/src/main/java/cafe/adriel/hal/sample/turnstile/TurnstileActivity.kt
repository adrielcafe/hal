package cafe.adriel.hal.sample.turnstile

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cafe.adriel.hal.observeState
import cafe.adriel.hal.observer.LiveDataStateObserver
import cafe.adriel.hal.plus
import cafe.adriel.hal.sample.R
import kotlinx.android.synthetic.main.activity_turnstile.*

class TurnstileActivity : AppCompatActivity() {

    private val viewModel by viewModels<TurnstileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_turnstile)

        vInsertCoin.setOnClickListener {
            viewModel + TurnstileAction.InsertCoin
        }
        vPush.setOnClickListener {
            viewModel + TurnstileAction.Push
        }

        viewModel.observeState(LiveDataStateObserver(this) { state ->
            when (state) {
                is TurnstileState.Locked -> updateState(true)
                is TurnstileState.Unlocked -> updateState(false)
            }
        })
    }

    private fun updateState(locked: Boolean) {
        vTurnstileState.setImageResource(
            if (locked) R.drawable.ic_lock_close else R.drawable.ic_lock_open
        )

        vInsertCoin.isEnabled = locked
        vPush.isEnabled = !locked
    }
}
