package otus.gpb.homework.activities

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class EditProfileActivity : AppCompatActivity() {

    private val launchPermissionCamera = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        when (isGranted) {
            // Разрешение получено - показываем кота
            true -> findViewById<ImageView>(R.id.imageview_photo).setImageResource(R.drawable.cat)
            // Разрешение не получено
            false -> if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                // Отображаем диалог с обоснованием перед запросом разрешения
                showRationaleDialog()
            } else {
                showOpenSettingsDialog()
            }
        }
    }

    private val launcherGetPicture = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { image ->
        if (image != null) {
            //findViewById<ImageView>(R.id.imageview_photo).setImageURI(image)
            populateImage(image)
        }
    }

    private val launcherFillFormActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                val data = result.data
                if (data != null) {
                    val name = data.getStringExtra("name") ?: ""
                    val surname = data.getStringExtra("surname") ?: ""
                    val age = data.getIntExtra("age", 0)

                    findViewById<TextView>(R.id.textview_name).text = name
                    findViewById<TextView>(R.id.textview_surname).text = surname
                    findViewById<TextView>(R.id.textview_age).text = age.toString()
                }
            }

            RESULT_CANCELED -> {
                Toast.makeText(this, "Галя, у нас отмена!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showRationaleDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Нужен доступ к камере")
            .setMessage("Для создания фотографии профиля необходимо разрешение на использование камеры")
            .setPositiveButton("Дать доступ") { dialog, which ->
                // Повторно запрашиваем разрешение
                launchPermissionCamera.launch(Manifest.permission.CAMERA)
            }
            .setNegativeButton("Отмена") { dialog, which ->
                dialog.dismiss()
            }.show()
    }

    private fun showOpenSettingsDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Доступ к камере запрещен")
            .setMessage("Для использования камеры необходимо предоставить разрешение в настройках приложения")
            .setPositiveButton("Открыть настройки") { dialog, which ->
                startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        "package:$packageName".toUri()
                    )
                )
            }
            .setCancelable(false)
            .show()
    }

    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        imageView = findViewById(R.id.imageview_photo)

        findViewById<Toolbar>(R.id.toolbar).apply {
            inflateMenu(R.menu.menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.send_item -> {
                        openSenderApp()
                        true
                    }

                    else -> false
                }
            }
        }

        imageView.setOnClickListener {
            val items = arrayOf("Сделать фото", "Выбрать фото")
            MaterialAlertDialogBuilder(this)
                .setTitle("Выберите действие")
                .setItems(items) { dialog, which ->
                    when (which) {
                        0 -> { // Сделать фото
                            launchPermissionCamera.launch(Manifest.permission.CAMERA)
                        }

                        1 -> { // Выбрать фото из галереи
                            launcherGetPicture.launch("image/*")
                        }
                    }
                }
                .show()
        }

        findViewById<Button>(R.id.button4).setOnClickListener {
            val intent = Intent(this, FillFormActivity::class.java)
            launcherFillFormActivity.launch(intent)
        }

    }

    /**
     * Используйте этот метод чтобы отобразить картинку полученную из медиатеки в ImageView
     */
    private fun populateImage(uri: Uri) {
        val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
        imageView.setImageBitmap(bitmap)
    }

    private fun openSenderApp() {
        val name = findViewById<TextView>(R.id.textview_name).text
        val surname = findViewById<TextView>(R.id.textview_surname).text
        val age = findViewById<TextView>(R.id.textview_age).text.toString()

        val shareText = "Имя: $name\nФамилия: $surname\nВозраст: $age"
        val bitmap = (imageView.drawable as? BitmapDrawable)?.bitmap

        try {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "image/*"
                putExtra(Intent.EXTRA_TEXT, shareText)

                // Добавляем изображение
                bitmap?.let {
                    val imageUri = getImageUri(this@EditProfileActivity, it)
                    putExtra(Intent.EXTRA_STREAM, imageUri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
            }

            startActivity(Intent.createChooser(intent, "Поделиться профилем"))

        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Нет приложений для отправки", Toast.LENGTH_SHORT)
                .show()
        }
    }

    // Используем FileProvider, т.к. "MediaStore.Images.Media.insertImage" - deprecated.
    private fun getImageUri(context: Context, bitmap: Bitmap): Uri {
        // Создаем временный файл
        val imagesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile("share_image_", ".jpg", imagesDir)

        // Сохраняем bitmap в файл
        FileOutputStream(imageFile).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }

        // Получаем URI через FileProvider
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            imageFile
        )
    }

}