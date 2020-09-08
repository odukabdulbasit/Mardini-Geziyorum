package appinventor.ai_Odukabdulbasit.Mardinigeziyorum

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.tarihi_yerler_list_item.view.*


class TarihiYerRecycelerViewListAdapter(val context : Context, val isimler : ArrayList<String>, val imageUrls : ArrayList<String>) : RecyclerView.Adapter<TarihiYerlerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarihiYerlerViewHolder {
        return TarihiYerlerViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.tarihi_yerler_list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }

    override fun onBindViewHolder(holder: TarihiYerlerViewHolder, position: Int) {

        Glide.with(context).load(imageUrls[position]).into(holder?.tarhiyerlerIV)
        holder?.tarihiYerListItemAdiTv.text = isimler[position]

    }


}
class TarihiYerlerViewHolder (view : View): RecyclerView.ViewHolder(view){

    val tarhiyerlerIV = view.tarihiYerlerListItemIV
    val tarihiYerListItemAdiTv = view.tarihiYerListItemAdiTv
}