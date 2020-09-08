package appinventor.ai_Odukabdulbasit.Mardinigeziyorum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_show_image.*

class ShowImage : AppCompatActivity() {

    lateinit var showImageIV : ImageView
    var  maxImageCount = 0

    val imageUrls : ArrayList<String> = ArrayList()
    val comments : ArrayList<String> = ArrayList()
    val isimler : ArrayList<String> = ArrayList()

    val mRef = FirebaseDatabase.getInstance()

    lateinit var isimTv : TextView
    lateinit var yorumTv : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_image)

        showImageIV  = findViewById(R.id.showImageIV)
        isimTv = findViewById(R.id.isimtv)
        yorumTv = findViewById(R.id.yorumTv)


        val imageIndex = intent.getIntExtra("imageindex",0)

        mRef.getReference().child("SizdenGelenler").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@ShowImage, "İşlem iptal edildi!", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {

                for (i in p0.children){

                    imageUrls.add(i.child("url").value.toString())
                    comments.add(i.child("comment").value.toString())
                    isimler.add(i.child("name").value.toString())
                }

                Glide.with(this@ShowImage).load(imageUrls[imageIndex]).into(showImageIV)
                isimTv.text = isimler[imageIndex]
                yorumTv.text = comments[imageIndex]
                maxImageCount = (imageUrls.count() - 1)
            }

        })

        var num = imageIndex

        showImageLayout.setOnTouchListener(object : OnSwipeTouchListener(){

            override fun onSwipeRight() {
                super.onSwipeRight()

                if (num == 0){
                    num = maxImageCount
                    fotoDegistir(num)
                }else{
                    num --
                    fotoDegistir(num)
                }
            }

            override fun onSwipeLeft() {

                super.onSwipeLeft()
                if (num == maxImageCount){
                    num = 0
                    fotoDegistir(num)
                }else{
                    num ++
                    fotoDegistir(num)
                }
            }
        })
    }

    fun fotoDegistir(num : Int){

        Glide.with(this).load(imageUrls[num]).into(showImageIV)
        isimTv.text = isimler[num]
        yorumTv.text = comments[num]
    }
}
