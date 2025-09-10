package otus.gpb.homework.activities.receiver

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class ReceiverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiver)

        val titleView = intent.getStringExtra("title").orEmpty()

        findViewById<TextView>(R.id.titleTextView).run {
            text = titleView
        }
        findViewById<TextView>(R.id.yearTextView).run {
            text = intent.getStringExtra("year").orEmpty()
        }
        findViewById<TextView>(R.id.descriptionTextView).run {
            text = intent.getStringExtra("desc").orEmpty()
        }
        findViewById<ImageView>(R.id.posterImageView).run {
            when (titleView) {
                "Славные парни" -> setImageResource(R.drawable.niceguys)
                "Интерстеллар" -> setImageResource(R.drawable.interstellar)
            }
        }
        if (!titleView.isEmpty()) {
            Toast.makeText(this, titleView, Toast.LENGTH_SHORT).show()
        }
    }

}
