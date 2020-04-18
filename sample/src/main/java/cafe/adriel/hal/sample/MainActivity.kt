package cafe.adriel.hal.sample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cafe.adriel.hal.sample.network.NetworkActivity
import cafe.adriel.hal.sample.turnstile.TurnstileActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vTurnstileExample.setOnClickListener {
            start<TurnstileActivity>()
        }
        vNetworkExample.setOnClickListener {
            start<NetworkActivity>()
        }
    }

    private inline fun <reified A : Activity> start() =
        startActivity(Intent(this, A::class.java))
}
