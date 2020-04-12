package cafe.adriel.hal.sample.turnstile

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cafe.adriel.hal.collectState
import cafe.adriel.hal.plusAssign
import cafe.adriel.hal.sample.R
import kotlinx.android.synthetic.main.activity_turnstile.*

class TurnstileActivity : AppCompatActivity() {

    private val viewModel by viewModels<TurnstileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_turnstile)

        vInsertCoin.setOnClickListener {
            viewModel += TurnstileAction.InsertCoin
        }
        vPush.setOnClickListener {
            viewModel += TurnstileAction.Push
        }

        viewModel.collectState(lifecycleScope) { state ->
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
