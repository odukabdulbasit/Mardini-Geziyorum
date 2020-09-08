package appinventor.ai_Odukabdulbasit.Mardinigeziyorum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class GenelBilgiListesiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genel_bilgi_listesi)
    }

    fun tarihiYerleriGorBtn( view : View ){

        val intent = Intent(this, YerListe::class.java)
        startActivity(intent)
    }

    fun sizdenGelenerBtn( view : View ){

        val intent = Intent(this, SizdenGelenler::class.java)
        startActivity(intent)
    }
}
