package cafe.adriel.hal.sample

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cafe.adriel.hal.observe
import cafe.adriel.hal.observer.LiveDataStateObserver
import cafe.adriel.hal.plus
import kotlinx.android.synthetic.main.activity_sample.*

class SampleActivity : AppCompatActivity() {

    private val viewModel by viewModels<SampleViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        vInsertCoin.setOnClickListener {
            viewModel + SampleAction.InsertCoin
        }
        vPush.setOnClickListener {
            viewModel + SampleAction.Push
        }

        viewModel.observe(LiveDataStateObserver(this) { state ->
            when (state) {
                is SampleState.Locked -> updateState(true)
                is SampleState.Unlocked -> updateState(false)
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
