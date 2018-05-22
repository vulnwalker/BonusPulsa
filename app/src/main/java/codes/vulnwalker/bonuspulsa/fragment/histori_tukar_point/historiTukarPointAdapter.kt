package codes.vulnwalker.bonuspulsa.fragment.histori_tukar_point

import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import codes.vulnwalker.bonuspulsa.R
import codes.vulnwalker.bonuspulsa.database.Account
import codes.vulnwalker.bonuspulsa.database.Histori
import codes.vulnwalker.bonuspulsa.database.KotlinHelper
import codes.vulnwalker.bonuspulsa.volley.URL
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by siddhartha on 23/5/17.
 */

internal class historiTukarPointAdapter(private val arrayList: ArrayList<historiTukarPointListItem>,
                                        private val context: Context,
                                        private val layout: Int) : RecyclerView.Adapter<historiTukarPointAdapter.ViewHolder>() {
    private lateinit var nama : TextView
    private lateinit var id_tukar : TextView
    private lateinit var gambar : ImageView
    private  lateinit var myEmail : String



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): historiTukarPointAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        val helper = KotlinHelper(context)
        nama = v.findViewById(R.id.nama)
        id_tukar = v.findViewById(R.id.id_tukar)
        gambar = v.findViewById(R.id.gambar)
        val parentList = v.findViewById<RelativeLayout>(R.id.parentListHistori)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            parentList.setBackgroundResource(R.drawable.item_list)
        }else{
            parentList.setBackgroundColor(Color.DKGRAY)
        }
        for (data: Account in helper.getAccount()) {
            myEmail = data.email
        }
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: historiTukarPointAdapter.ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(items: historiTukarPointListItem) {
            nama.text = items.nama
            id_tukar.text = items.id_tukar
            val helper = KotlinHelper(context)
            for (data: Histori in helper.getDetailHistori(items.id_tukar.toString())) {
                if(data.status.equals("DONE")){
                    gambar.setImageResource(R.mipmap.done)
                }else{
                    gambar.setImageResource(R.mipmap.kacang)
                }

            }

            itemView.setOnClickListener {
                itemTukarClicked(items.id_tukar.toString())
            }
        }
    }

    fun itemTukarClicked(idTukarSelected : String){
        val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                }

                DialogInterface.BUTTON_NEGATIVE -> {
                }
            }
        }
        val builder = AlertDialog.Builder(context)
        val helper = KotlinHelper(context)
        var gblk : String = ""
        for (data: Histori in helper.getDetailHistori(idTukarSelected)) {
            if(data.status.equals("DONE")){
                gblk = data.tanggal_aksi
            }else{
             //   gblk = data.tanggal_aksi
            }
            builder.setMessage(data.nama_tukar +"\n" +
                                data.tanggal+"\n"+
                                data.status +"\n" +
                                gblk

            ).setPositiveButton("Tutup", dialogClickListener)
                    .setNegativeButton("", dialogClickListener).show()
        }

    }




}