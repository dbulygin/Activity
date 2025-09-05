package otus.gpb.homework.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActivityC : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_c)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /* По клику на кнопку “Open ActivityA” запустите **ActivityA**,
        * таким образом, чтобы мы попали на существующий экземпляр  **ActivityA**
        * и у него был вызван метод `onNewIntent`, независимо от того находится **ActivityA**
        * наверху своего стека или нет
        * */
        findViewById<Button>(R.id.buttonOpenA).setOnClickListener {
            startActivity(
                Intent(this, ActivityA::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }

        /* По клику на кнопку “Open ActivityD” запустите **ActivityD** в том же стеке,
        * где расположены **ActivityB** и **ActivityC**, при этом завершите все предыдущие Activity,
        * которые находятся в текущем стеке
        * */
        findViewById<Button>(R.id.buttonOpenD).setOnClickListener {
            startActivity(
                Intent(this, ActivityD::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        }

        /* По клику на кнопку “CloseActivityC”, завершите **ActivityC**,
        * и перейдите на предыдущий экран в стеке
        * */
        findViewById<Button>(R.id.buttonCloseC).setOnClickListener { finish() }

        /* По клику на кнопку “Close Stack” завершите текущий стек, в котором находятся **ActivityB**
        * и **ActivityC**, и перейдите на **ActivityA**
        * */
        findViewById<Button>(R.id.buttonCloseStack).setOnClickListener {
            startActivity(
                Intent(this, ActivityA::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            finishAffinity()
        }

    }
}