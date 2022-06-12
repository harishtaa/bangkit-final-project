package com.akbar.dbdpv1.menu2

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.akbar.dbdpv1.result.ResultScanActivity
import com.akbar.dbdpv1.apinya.ApiConfig
import com.akbar.dbdpv1.databinding.ActivityCameraBinding
import com.akbar.dbdpv1.uriToFile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class CameraActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCameraBinding
    private var getFile : File? = null
    private lateinit var currentPhotoPath: String

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
            .setPositiveButton("Oke") { dia, _ -> dia.cancel() }

        binding.ivAlertDia.setOnClickListener {
            val clicked = dialogAle.create()
            clicked.show()
        }

        binding.btnGaleri.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startTakePhoto() }
        binding.btnScan.setOnClickListener { startUploadFoto() }
    }

    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile
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

    private fun reduceImageToBase64(file: File): String {
        val fileCompress = reduceFileImage(file)
        return Base64.encodeToString(fileCompress.readBytes(), Base64.DEFAULT)
    }

    private fun startUploadFoto(){
        showLoading(true)
        if(getFile != null){

            val covertImageToBase64 = reduceImageToBase64(getFile as File)
            val fileRequest = UploadRequest(data64 = covertImageToBase64)

            ApiConfig.getApiService().uploadImage(fileRequest).enqueue(object : Callback<UploadResponse>{
                override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        val intent = Intent(this@CameraActivity, ResultScanActivity::class.java)
                        showLoading(false)
                        startActivity(intent.putExtra("EXTRA_DATA", responseBody?.dogdata))
                        finish()
                    } else {
                        showLoading(false)
                        Toast.makeText(this@CameraActivity, response.message(), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                    showLoading(false)
                    Toast.makeText(this@CameraActivity, "Retrofit instance failed", Toast.LENGTH_SHORT).show()
                    Log.e("apa errornya: ", t.message.toString())
                }
            })
        }else{
            showLoading(false)
            Toast.makeText(this, "Please add a picture first.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }
}