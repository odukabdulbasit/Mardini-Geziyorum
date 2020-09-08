package appinventor.ai_Odukabdulbasit.Mardinigeziyorum

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_foto_ekleme.*
import java.util.*

class FotoEklemeActivity : AppCompatActivity() {

    //Our variables
    var mUri: Uri? = null

    //Our constants
    private val OPERATION_CHOOSE_PHOTO = 2

    //sonradan yaptiklarim
    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001

    lateinit var fotoEkleIv : ImageView
    //Our widgets
    private lateinit var btnCapture: Button
    private lateinit var btnChoose : Button

    lateinit var commentEt : EditText

    val mRef = FirebaseDatabase.getInstance()
    val mAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foto_ekleme)

        fotoEkleIv = findViewById(R.id.fotoEkleIV)
        commentEt = findViewById(R.id.yorumEt)

        initializeWidgets()


        //kamera acma butonu
        btnCapture.setOnClickListener{

           //******************* galeryCameraLayout.visibility = View.INVISIBLE

            //if system os is Marshmallow or Above, we need to request runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    //permission was not enabled
                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    openCamera()
                }
            }
            else{
                //system os is < marshmallow
                openCamera()
            }
        }

        //galeriden resim secme butonu
        btnChoose.setOnClickListener{

           // **************888galeryCameraLayout.visibility = View.INVISIBLE

            //check permission at runtime
            val checkSelfPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED){
                //Requests permissions to be granted to this application at runtime
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), OPERATION_CHOOSE_PHOTO)
            }
            else{
                openGallery()
            }
        }
    }

    private fun initializeWidgets() {
        title = "Sizden Gelenler"
        btnCapture = findViewById(R.id.cameraBtn)
        btnChoose = findViewById(R.id.galeryBtn)
    }

    //buton layout backround color = android:background="#F8EDED"

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        mUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    private fun show(message: String) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }

    private fun openGallery(){
        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*"
        startActivityForResult(intent, OPERATION_CHOOSE_PHOTO)
    }

    //izinler alindiktan sonra yapilanlar
    //**********************************************************************************************************************************
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //called when user presses ALLOW or DENY from Permission Request Popup
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted
                    openCamera()
                }
                else{
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            OPERATION_CHOOSE_PHOTO ->{
                if (grantResults.isNotEmpty() && grantResults.get(0) ==
                    PackageManager.PERMISSION_GRANTED){
                    openGallery()
                }else {
                    show("Unfortunately You are Denied Permission to Perform this Operataion.")
                }
            }
        }
    }

    // islem yapildiktan sonra yapilacaklar
//**********************************************************************************************************************************
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            IMAGE_CAPTURE_CODE ->
                if (resultCode == Activity.RESULT_OK) {

                    //uploadImage()

                    setImageOnImageView()
                }
            OPERATION_CHOOSE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        mUri= data!!.data!!

                        //uploadImage()
                        setImageOnImageView()
                    }
                }
        }
    }

    fun setImageOnImageView(){

        //set image to imageview
        fotoEkleIv.setImageURI(mUri!!)
    }


    // UploadImage method
     fun ekleClick(view : View) {
        if (isImgOrEtEmpty()) {

            // Defining the child of storageReference
            val storageImageName = UUID.randomUUID().toString() + ".jpg"
            val strRef = FirebaseStorage.getInstance().reference.child("SizdenGelenler/"+ storageImageName)
            // adding listeners on upload
            // or failure of image


            var uploadTask = strRef.putFile(mUri!!)

            uploadTask.addOnSuccessListener {

                Toast.makeText(this, " Yukleme basrili", Toast.LENGTH_SHORT).show()

                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation strRef.downloadUrl
                }).addOnCompleteListener { task ->
                    if (task.isSuccessful) {



                        val downloadUri = task.result.toString()

                        val url = downloadUri
                        val name = mAuth.currentUser!!.displayName
                        val comment = commentEt.text.toString()
                        // mRef.getReference()!!.child("SizdenGelenler").push().setValue(downloadUri)
                        mRef.reference.child("SizdenGelenler").push().setValue(IsimResimAciklama(url,name!!, comment))

                        commentEt.text.clear()
                        mUri= null
                        //foto ekle ekranindan cikip sizden gelenler ekranina gidiyor
                        activityIntent()

                    } else {
                        // Handle failures
                    }
                }
            }.addOnFailureListener {

                Toast.makeText(this, " Fotoğraf yüklenemedi. Tekrar deneyin", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this,"Lütfen eklemek için fotoğraf seçip yorum yazınız..",Toast.LENGTH_LONG).show()
        }
    }



    fun isImgOrEtEmpty(): Boolean{
        return mUri != null && commentEt.text.toString() != null
    }

    fun activityIntent(){

        val intent = Intent(this, SizdenGelenler ::class.java)
        startActivity(intent)
    }

}
