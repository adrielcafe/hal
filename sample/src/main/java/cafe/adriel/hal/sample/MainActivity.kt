package cafe.adriel.hal.sample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cafe.adriel.hal.sample.network.NetworkActivity
import cafe.adriel.hal.sample.turnstile.TurnstileActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vTurnstileExample.setOnClickListener {
            startActivity(Intent(this, TurnstileActivity::class.java))
        }
        vNetworkExample.setOnClickListener {
            startActivity(Intent(this, NetworkActivity::class.java))
        }
    }
}
