package appinventor.ai_Odukabdulbasit.Mardinigeziyorum

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.FirebaseException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_bilgi.*

class BilgiActivity : AppCompatActivity() {

    lateinit var bilgiTextView : TextView
    lateinit var bilgiImageView : ImageView
    lateinit var bilgiSliderdot : LinearLayout

    val bilgiDots : ArrayList<ImageView> = ArrayList()
    val urls : ArrayList<String> = ArrayList()

     var position : Int = 0
    var imagesCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bilgi)

        title = intent.getStringExtra("yerismi")
        position = (intent.getIntExtra("position", 0)) + 1

        bilgiTextView = findViewById(R.id.bilgiTextView)
        bilgiImageView = findViewById(R.id.bilgiImageView)
        bilgiSliderdot = findViewById(R.id.bilgiSliderdot)


       val mRef = FirebaseDatabase.getInstance().getReference()

        mRef.child("tarihiYerler").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@BilgiActivity, p0.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(p0: DataSnapshot) {

                val bilgiText = p0.child(position.toString()).child("bilgi").value as String
                bilgiTextView.text = bilgiText

                for (i in p0.child(position.toString()).child("images").children){

                    val gelenUrl = i.value as String
                    urls.add(gelenUrl)
                }

                imagesCount = urls.count()

                createDots(imagesCount)
                fotoDegistir(0)
            }
        })

        var imageNo = 0

        bilgiScrollView.setOnTouchListener(object : OnSwipeTouchListener(){

            override fun onSwipeRight() {

                if (imageNo == 0){
                    imageNo = (imagesCount - 1)
                    fotoDegistir(imageNo)
                }else{
                    imageNo --
                    fotoDegistir(imageNo)
                }

                super.onSwipeRight()
            }

            override fun onSwipeLeft() {

                if (imageNo == (imagesCount - 1)){
                    imageNo = 0
                    fotoDegistir(imageNo)
                }else{
                    imageNo ++
                    fotoDegistir(imageNo)
                }

                super.onSwipeLeft()
            }
        })
    }


    fun fotoDegistir(imageNo : Int){

        Glide.with(this@BilgiActivity).load(urls[imageNo]).into(bilgiImageView)
        changeDotImage(imageNo)
    }

    fun changeDotImage(imageNo : Int){
        for (i in 0 until  imagesCount){
            if (i == imageNo){
                bilgiDots[imageNo].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.active_dot))
            }else{
                bilgiDots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.non_active_dot))
            }
        }
    }

    fun createDots(count : Int){

        for (i in 0 until  count){

            val elemanImage = ImageView(this@BilgiActivity)
            bilgiDots.add(elemanImage)

            bilgiDots[i].setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.non_active_dot))

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            params.setMargins(8, 0 , 8 , 0)

            bilgiSliderdot.addView(bilgiDots[i], params)
        }
    }
}
