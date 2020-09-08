package appinventor.ai_Odukabdulbasit.Mardinigeziyorum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class YerListe : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


    val liste: ArrayList<String> = ArrayList()
    val yerListImages : ArrayList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yer_liste)

        title = "Tarihi Yerler"
        recyclerView = findViewById(R.id.tarihiYerlerRv)


        val mRef = FirebaseDatabase.getInstance().getReference()

        mRef.child("tarihiYerler").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@YerListe, p0.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(p0: DataSnapshot) {

                for (i in p0.children){
                    val yerAdi = i.child("yerAdi").value as String
                    val imageUrl = i.child("images").child("1").value as String

                    liste.add(yerAdi)
                    yerListImages.add(imageUrl)
                }
                setAdaptertoRecyclerView()

            }

        })



        recyclerView.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                // Your logic

                val intent = Intent(this@YerListe, BilgiActivity ::class.java)
                intent.putExtra("yerismi",  liste[position])
                intent.putExtra("position", position)
                startActivity(intent)
            }
        })

    }

    fun setAdaptertoRecyclerView(){

        viewManager = LinearLayoutManager(this)
        viewAdapter = TarihiYerRecycelerViewListAdapter(this, liste, yerListImages)

        recyclerView = findViewById<RecyclerView>(R.id.tarihiYerlerRv).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
    }

    interface OnItemClickListener {
        fun onItemClicked(position: Int, view: View)
    }

    fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
        this.addOnChildAttachStateChangeListener(object: RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
                view?.setOnClickListener(null)
            }

            override fun onChildViewAttachedToWindow(view: View) {

                view?.setOnClickListener({
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClicked(holder.adapterPosition, view)
                })
            }

        })
    }
}
