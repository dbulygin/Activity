package otus.gpb.homework.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActivityA : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_a)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /* По клику на кнопку “Open ActivityB” запустите **ActivityB** в отдельном стеке,
        * при этом предусмотрите возможность открывать другие Activity в том же стеке
        * где расположена ActivityA
        *
        * Я понял так, что мы не должны запускать больше одной копии Активити А в текущей Таске с Активити А.
        * И дальше по заданию все остальные Активити находятся в другой Таске...
        * Зачем предусматривать возможность открытия и других Активити в Таске с Активити А - не понятно.
        * */
        findViewById<Button>(R.id.buttonOpenB).setOnClickListener {
            startActivity(
                Intent(this, ActivityB::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }

    }
}