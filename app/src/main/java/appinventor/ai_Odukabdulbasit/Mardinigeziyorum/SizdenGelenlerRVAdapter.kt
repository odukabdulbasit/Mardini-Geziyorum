package appinventor.ai_Odukabdulbasit.Mardinigeziyorum

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.sizden_gelenler_list_item.view.*
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class SizdenGelenlerRVAdapter(val context : Context, val imageUrls : ArrayList<String>) : RecyclerView.Adapter<SizdenGelenlerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizdenGelenlerViewHolder {
        return SizdenGelenlerViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.sizden_gelenler_list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }

    override fun onBindViewHolder(holder: SizdenGelenlerViewHolder, position: Int) {

        Glide.with(context).load(imageUrls[position]).into(holder?.sizdenGelenlerIV)
        //holder?.sizdenGelenlerIV.setImageURI(imageUrls[position])
    }

}
class SizdenGelenlerViewHolder (view : View): RecyclerView.ViewHolder(view){

    val sizdenGelenlerIV = view.sizdenGelenlerIV
}