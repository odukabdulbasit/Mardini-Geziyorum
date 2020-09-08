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
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.util.*
import kotlin.collections.ArrayList

class SizdenGelenler : AppCompatActivity() {



    lateinit var sizdenGelenlerGV : GridView

    val mRef = FirebaseDatabase.getInstance()
    val mAuth = FirebaseAuth.getInstance()

    var imageList = ArrayList<IsimResimAciklama>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sizden_gelenler)


        sizdenGelenlerGV = findViewById(R.id.sizdenGelenlerGV)
        var adapter: GvAdapter?
        //img url toplanilacak arrayist


        //fab yani (ekle) butonu tanimlanip islem yapiliyor
        val fab: View = findViewById(R.id.sizdenGelenlerFab)
        fab.setOnClickListener { view ->

            if (mAuth.currentUser?.uid == null){
                val intent = Intent(this, GoogleSignInFbActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this, FotoEklemeActivity::class.java)
                startActivity(intent)

            }


        }

        adapter = GvAdapter(this, imageList)

        sizdenGelenlerGV.adapter = adapter

        mRef.getReference().child("SizdenGelenler").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@SizdenGelenler, "İşlem iptal edildi!", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {

                for (i in p0.children){

                    val url = i.child("url").value.toString()
                    val name = i.child("name").value.toString()
                    val comment = i.child("comment").value.toString()
                    //burda bilgileri firebaseden isimresimaciklama sinifina gire alip arraylistime ekleycem
                    imageList.add(IsimResimAciklama(url,name, comment))
                }
                 adapter!!.notifyDataSetChanged()
            }

        })


        sizdenGelenlerGV.setOnItemClickListener( object : AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

               val intent = Intent(this@SizdenGelenler, ShowImage ::class.java)

                intent.putExtra("imageindex", position)
                startActivity(intent)
            }
        })
    }

}
