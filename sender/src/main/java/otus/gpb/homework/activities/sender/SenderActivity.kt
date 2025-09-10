package otus.gpb.homework.activities.sender

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import otus.gpb.homework.activities.receiver.R

class SenderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sender)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /* To Google Maps button */
        findViewById<Button>(R.id.buttonGoogleMaps).setOnClickListener {
            openGoogleMapsWithCategory(this, 55.7558, 37.6173, "Рестораны")
        }

        /* Send Email button */
        findViewById<Button>(R.id.buttonSendEmail).setOnClickListener {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_SENDTO,
                        "mailto:android@otus.ru".toUri()
                    )
                )
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    this,
                    "Ни одного почтового клиента не найдено",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        /* Open Receiver button */
        findViewById<Button>(R.id.buttonOpenReciever).setOnClickListener {
            val payLoad1 = Payload(
                "Славные парни",
                "2016",
                "Что бывает, когда напарником брутального костолома становится субтильный лопух? Наемный охранник Джексон Хили и частный детектив Холланд Марч вынуждены работать в паре, чтобы распутать плевое дело о пропавшей девушке, которое оборачивается преступлением века. Смогут ли парни разгадать сложный ребус, если у каждого из них – свои, весьма индивидуальные методы."
            )
            val payLoad2 = Payload(
                "Интерстеллар",
                "2014",
                "Когда засуха, пыльные бури и вымирание растений приводят человечество к продовольственному кризису, коллектив исследователей и учёных отправляется сквозь червоточину (которая предположительно соединяет области пространства-времени через большое расстояние) в путешествие, чтобы превзойти прежние ограничения для космических путешествий человека и найти планету с подходящими для человечества условиями."
            )

            openReceiver(payLoad1)
        }
    }

    private fun openGoogleMapsWithCategory(
        context: Context,
        latitude: Double,
        longitude: Double,
        query: String
    ) {
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "geo:$latitude,$longitude?q=${Uri.encode(query)}".toUri()
                ).setPackage("com.google.android.apps.maps")
            )
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                context,
                "Google Maps не установлен",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openReceiver(payLoad: Payload) {
        try {
            startActivity(
                Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "Hello from Sender!")
                    putExtra("title", payLoad.title)
                    putExtra("year", payLoad.year)
                    putExtra("desc", payLoad.description)
                }
            )
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Activity not found", Toast.LENGTH_SHORT).show()
            Log.e("TAG", "Activity not found: ${e.message}")
        }
    }
}