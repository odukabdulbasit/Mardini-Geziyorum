package appinventor.ai_Odukabdulbasit.Mardinigeziyorum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var button: Button
    lateinit var genelBilgiTV : TextView
    lateinit var girisImageView: ImageView
    lateinit var sliderDotsPanel : LinearLayout

    val mardinGenelFotoUrl : ArrayList<String> = ArrayList()
    val dots : ArrayList<ImageView> = ArrayList()

    //for creating dots I need images count to make it dynamic
    var fotosCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        button = findViewById(R.id.listeAcButton)
        genelBilgiTV = findViewById(R.id.genelBilgiTV)
        girisImageView = findViewById(R.id.girisImageView)
        sliderDotsPanel = findViewById(R.id.SliderDots)

        var imageNo = 0
        var maxImageSwipeCount = 0

        val mRef = FirebaseDatabase.getInstance().getReference()

        mRef.child("genelBilgi").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@MainActivity, p0.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(p0: DataSnapshot) {

                val bilgiText = p0.child("bilgi").value as String
                genelBilgiTV.text = bilgiText
                for (i in p0.child("images").children){

                    val uri = i.value as String
                    mardinGenelFotoUrl.add(uri)
                }

                fotosCount = mardinGenelFotoUrl.count()
                maxImageSwipeCount = (fotosCount - 1)

                createDots()
                fotoDegistir(imageNo)
            }
        })



        //girisImageView.setImageResource(mardinFotolari[imageNo])

        girisScrollview.setOnTouchListener(object : OnSwipeTouchListener(){

            override fun onSwipeRight() {
                super.onSwipeRight()

                if (imageNo == 0){
                    imageNo = maxImageSwipeCount
                    fotoDegistir(imageNo)
                }else{
                    imageNo --
                    fotoDegistir(imageNo)
                }
            }

            override fun onSwipeLeft() {

                super.onSwipeLeft()
                if (imageNo == maxImageSwipeCount){
                    imageNo = 0
                    fotoDegistir(imageNo)
                }else{
                    imageNo ++
                    fotoDegistir(imageNo)
                }
            }
        })


        //Tarihi yerler listesini acmak o activityyi acmak icin
        button.setOnClickListener(View.OnClickListener {

            val intent = Intent(this, GenelBilgiListesiActivity ::class.java)
            startActivity(intent)
        })
    }

    fun fotoDegistir(imageNo : Int){
        //girisImageView.setImageResource(mardinFotolari[imageNo])

        Glide.with(this).load(mardinGenelFotoUrl[imageNo]).into(girisImageView)
        setDotImage(imageNo)
    }

    fun setDotImage(num : Int){
        for (i in 0 until  fotosCount){
            if (i == num){
                dots[num].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.active_dot))
            }else{
                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.non_active_dot))
            }
        }
    }

    fun createDots(){

        for (i in 0 until fotosCount){

            var newView: ImageView

            newView = ImageView(this)

            dots.add(newView)

            dots[i].setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.non_active_dot))

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            params.setMargins(8, 0, 8, 0)

            sliderDotsPanel.addView(dots[i], params)
        }
    }
}
