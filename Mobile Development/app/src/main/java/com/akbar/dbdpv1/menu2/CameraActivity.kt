package com.akbar.dbdpv1.menu2

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.akbar.dbdpv1.ResultScanActivity
import com.akbar.dbdpv1.apinya.ApiConfig
import com.akbar.dbdpv1.databinding.ActivityCameraBinding
import com.akbar.dbdpv1.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*


class CameraActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCameraBinding
    private var getFile : File? = null
    private lateinit var currentPhotoPath: String

    companion object {
        const val CAMERA_X_RESULT = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val dialogAle = AlertDialog.Builder(this)
        dialogAle.setTitle("Cara Mengambil Gambar!")
            .setMessage("1. Pastikan objek menghadap ke kamera \n" +
                    "2. Penerangan yang cukup \n" +
                    "3. Objek tidak terlalu jauh \n" +
                    "4. Jika foto blur/tidak jelas, mohon diulang")
            .setPositiveButton("Oke", DialogInterface.OnClickListener { dia, _ -> dia.cancel() })

        binding.ivAlertDia.setOnClickListener {
            val clicked = dialogAle.create()
            clicked.show()
        }

        binding.btnGaleri.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startTakePhoto() } //startCameraX()
        binding.btnScan.setOnClickListener { startUploadFoto() }
    }


    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            binding.ivUploadFoto.setImageURI(selectedImg)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {

            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = BitmapFactory.decodeFile(myFile.path)

            binding.ivUploadFoto.setImageBitmap(result)
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        com.akbar.dbdpv1.createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this, "com.akbar.dbdpv1.menu2", it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    /*
    private fun reduceImageToBase64(file: File): String {
        val bitmap = BitmapFactory.decodeFile(file.path)
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bmpStream)
        val image = bmpStream.toByteArray()

        val imageData: ByteArray = Base64.encodeBase64(bitmap.getBytes())

        return Base64.getEncoder().encodeToString(image, Base64.DEFAULT)
    }
     */

    private fun startUploadFoto(){
        if(getFile != null){
            val token = "//belom ada tokennya atau gausah pake token"
            val file = reduceFileImage(getFile as File)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            ApiConfig.getApiService().uploadImage(imageMultipart).enqueue(object : Callback<UploadResponse>{
                override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            Toast.makeText(this@CameraActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                        }
                        //diisi akan dipindahkan ke activity baru atau hanya memunculkan data
                        Intent(this@CameraActivity, ResultScanActivity::class.java).also { startActivity(it) }
                        //showLoading(false)
                        finish()
                    } else {
                        //showLoading(false)
                        Toast.makeText(this@CameraActivity, response.message(), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                    //showLoading(false)
                    Toast.makeText(this@CameraActivity, "Retrofit instance failed", Toast.LENGTH_SHORT).show()
                }
            })
        }else{
            //showLoading(false)
            Toast.makeText(this, "Please add a picture first.", Toast.LENGTH_SHORT).show()
        }
    }

    /*
    private fun startCameraX() {
        val intent = Intent(this, CameraXActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )
            binding.ivUploadFoto.setImageBitmap(result)
        }
    }
    */
}