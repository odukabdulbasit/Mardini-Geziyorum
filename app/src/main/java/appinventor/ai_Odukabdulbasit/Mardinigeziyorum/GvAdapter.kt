package appinventor.ai_Odukabdulbasit.Mardinigeziyorum

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.sizden_gelenler_list_item.view.*

class GvAdapter(val context : Context, val IsimResimAciklama : ArrayList<IsimResimAciklama>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val url = IsimResimAciklama[position].url
        val name = IsimResimAciklama[position].name
        val comment = IsimResimAciklama[position].comment

        var infaltor = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var newView = infaltor.inflate(R.layout.sizden_gelenler_list_item, null)


        //fotoEkledesign upload etme yeri
        val img = newView.sizdenGelenlerIV
        newView.userNameTv.text = name
        newView.userCommentTv.text = comment


        Glide.with(context).load(url).into(img)

        return newView
    }

    override fun getItem(position: Int): Any {
        return IsimResimAciklama[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return IsimResimAciklama.count()
    }
}