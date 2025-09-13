package otus.gpb.homework.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class FillFormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_fill_form)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        findViewById<Button>(R.id.button_apply).setOnClickListener {
            try {
                val name = findViewById<EditText>(R.id.edittext_name).text.toString()
                val surname = findViewById<EditText>(R.id.edittext_surname).text.toString()
                val age = findViewById<EditText>(R.id.edittext_age).text.toString().toInt()

                if (name.isNotBlank() || surname.isNotBlank() || age <= 0) {
                    val intent = Intent()
                        .putExtra("name", name)
                        .putExtra("surname", surname)
                        .putExtra("age", age)
                    setResult(RESULT_OK, intent)
                    Toast.makeText(this, "Данные сохранены", Toast.LENGTH_LONG).show()
                } else {
                    setResult(RESULT_CANCELED)
                    Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()

                }
            } catch (e: Exception) {
                setResult(RESULT_CANCELED)
            }
            finish()
        }

    }
}