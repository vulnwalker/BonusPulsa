package codes.vulnwalker.bonuspulsa.fragment.bantuan

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import codes.vulnwalker.bonuspulsa.R
import codes.vulnwalker.bonuspulsa.database.Account
import codes.vulnwalker.bonuspulsa.database.KotlinHelper
import java.util.ArrayList
import org.jetbrains.anko.*
/**
 * Created by siddhartha on 23/5/17.
 */

internal class bantuanAdapter(private val arrayList: ArrayList<bantuanList>,
                              private val context: Context,
                              private val layout: Int) : RecyclerView.Adapter<bantuanAdapter.ViewHolder>() {
    private lateinit var namaBantuan : TextView
    private lateinit var idBantuan : TextView
    private lateinit var gambar : ImageView
    private  lateinit var myEmail : String



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): bantuanAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        val helper = KotlinHelper(context)
        namaBantuan = v.findViewById(R.id.namaBantuan)
        idBantuan = v.findViewById(R.id.idBantuan)
        gambar = v.findViewById(R.id.gambar)
        val parentList = v.findViewById<RelativeLayout>(R.id.parentListHelp)
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

    override fun onBindViewHolder(holder: bantuanAdapter.ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(items: bantuanList) {
            namaBantuan.text = items.namaBantuan
            idBantuan.text = items.idBantuan


            itemView.setOnClickListener {
                itemTukarClicked(items.idBantuan.toString())
            }
        }
    }

    fun itemTukarClicked(itemSelected : String){
         val i = Intent(context, bantuan::class.java)
            if(itemSelected.equals("1")){
                i.putExtra("jenis","Video")
            }else if(itemSelected.equals("2")){
                i.putExtra("jenis","Klik")
            }else if(itemSelected.equals("5")){
                i.putExtra("jenis","Absen")
            }else if(itemSelected.equals("3")){
                i.putExtra("jenis","Penukaran")
            }else if(itemSelected.equals("4")){
                i.putExtra("jenis","Suspend")
            }
        context.startActivity(i)


    }




}