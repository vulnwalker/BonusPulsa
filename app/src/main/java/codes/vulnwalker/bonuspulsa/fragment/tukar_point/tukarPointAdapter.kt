package `in`.creativelizard.recyclerviewtest

import android.content.ContentValues
import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import codes.vulnwalker.bonuspulsa.R

import java.util.ArrayList

import android.widget.Toast
import codes.vulnwalker.bonuspulsa.database.Account
import codes.vulnwalker.bonuspulsa.database.KotlinHelper
import codes.vulnwalker.bonuspulsa.volley.URL
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.widget.RelativeLayout
import codes.vulnwalker.bonuspulsa.MainActivity
import codes.vulnwalker.bonuspulsa.database.Histori




/**
 * Created by siddhartha on 23/5/17.
 */

internal class tukarPointAdapter(private val arrayList: ArrayList<tukarPointListItem>,
                                 private val context: Context,
                                 private val layout: Int) : RecyclerView.Adapter<tukarPointAdapter.ViewHolder>() {
    private lateinit var nama : TextView
    private lateinit var id_tukar : TextView
    private  lateinit var myEmail : String



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): tukarPointAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        val helper = KotlinHelper(context)
        val parentList = v.findViewById<RelativeLayout>(R.id.parentListTrade)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            parentList.setBackgroundResource(R.drawable.item_list)
        }else{
            parentList.setBackgroundColor(Color.DKGRAY)
        }
        nama = v.findViewById(R.id.nama)
        id_tukar = v.findViewById(R.id.id_tukar)
        for (data: Account in helper.getAccount()) {
            myEmail = data.email
        }
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: tukarPointAdapter.ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(items: tukarPointListItem) {
            nama.text = items.nama
            id_tukar.text = items.id_tukar
            itemView.setOnClickListener {
                itemTukarClicked(items.id_tukar.toString())
            }
        }
    }

    fun itemTukarClicked(idTukarSelected : String){
        val queue = Volley.newRequestQueue(context)
        val response: String? = null
        var currentSaldo = "0"
        val postRequest = object : StringRequest(Method.POST, URL.GET_DETAIL_LIST_TUKAR, Response.Listener<String> { response ->
            val resp = JSONObject(response)
            val jsonContent = resp.getString("content").toString()
            val jsonArrayContent = JSONArray(jsonContent)
            for (i in 0..(jsonArrayContent.length() - 1)) {
                val content = jsonArrayContent.getJSONObject(i)
//                Toast.makeText(context, "dapetnya : " + content.getString("jumlah_dapat").toString(), Toast.LENGTH_SHORT).show()
                val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            requestTukar(idTukarSelected)
                        }

                        DialogInterface.BUTTON_NEGATIVE -> {
                            //Toast.makeText(context, "Batal", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Saldo akan terpotong "+content.getString("jumlah_point").toString() + " Point , untuk menukar dengan " + content.getString("nama_tukar").toString() )
                        .setNegativeButton("Batal", dialogClickListener).setPositiveButton("Setuju", dialogClickListener).show()

            }




        },
                Response.ErrorListener {
                    //  toast("error")
                }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("email", myEmail)
                params.put("id_tukar",idTukarSelected)
                return params
            }
        }
        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(postRequest)
    }
    fun requestTukar(idTukarSelected: String){
        val queue = Volley.newRequestQueue(context)
        val response: String? = null
        var currentSaldo: String? = null
        val postRequest = object : StringRequest(Method.POST, URL.REQUEST_TUKAR, Response.Listener<String>{
            response ->
            val resp = JSONObject(response)
            val err = resp.getString("err")
            val cek = resp.getString("cek")
            if(err.equals("")){
                val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {

                        }

                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }
                val builder = AlertDialog.Builder(context)
                builder.setMessage(cek.toString() ).setPositiveButton("Tutup", pesanResponse)
                        .setNegativeButton("", pesanResponse).show()
                val jsonContent = resp.getString("content")
                val content = JSONObject(jsonContent)
                val helper = KotlinHelper(context)
                val db = helper.writableDatabase
                var values = ContentValues()
                val calculatedSaldo = content.getString("saldo").toString()
                values.put("saldo", calculatedSaldo)
                val retVal = db.update("ACCOUNT", values, "userID = 1 ", null)
                if (retVal >= 1) {
                    Log.v("@@@WWe", " Record updated")
                } else {
                    Log.v("@@@WWe", " Not updated")
                }
                db.close()

                val dataHistori = Histori(
                        content.getString("id_histori").toString(),
                        content.getString("nama_tukar").toString(),
                        content.getString("tanggal").toString(),
                        content.getString("status").toString(),
                        content.getString("tanggal_aksi").toString()
                )
                helper.addHistori(dataHistori)
                (context as MainActivity).refreshBalance()

            }else{

                val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {


                        }

                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }
                val builder = AlertDialog.Builder(context)
                builder.setMessage(err.toString() ).setPositiveButton("Tutup", pesanResponse)
                        .setNegativeButton("", pesanResponse).show()
            }

        },
                Response.ErrorListener {
                    val errorDialog = DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {

                            }

                            DialogInterface.BUTTON_NEGATIVE -> {
                            }
                        }
                    }
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("Error, periksa koneksi internet anda" ).setPositiveButton("Tutup", errorDialog)
                            .setNegativeButton("", errorDialog).show()

                }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("email", myEmail)
                params.put("id",idTukarSelected )
                return params
            }
        }
        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(postRequest)


    }



}
