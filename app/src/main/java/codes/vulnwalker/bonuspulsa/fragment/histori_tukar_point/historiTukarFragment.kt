package codes.vulnwalker.bonuspulsa.fragment.histori_tukar_point


import `in`.creativelizard.recyclerviewtest.tukarPointAdapter
import `in`.creativelizard.recyclerviewtest.tukarPointListItem
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout

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
 * A simple [Fragment] subclass.
 */
class historiTukarFragment : Fragment() {
    private var historiTukarPointAdapter: historiTukarPointAdapter? = null
    private var arrayList: ArrayList<historiTukarPointListItem>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var rlItems : RecyclerView
    private var myEmail : String? = null
    private var row : String = "0"
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater!!.inflate(R.layout.fragment_tukar_point, container, false)
        rlItems = v.findViewById(R.id.rcc)
//        val backGroundFragment = v.findViewById<FrameLayout>(R.id.frameHistoriTukar)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            backGroundFragment.setBackgroundResource(R.drawable.bg)
//        }
        val helper = KotlinHelper(context)
        for (data: Account in helper.getAccount()) {
            myEmail = data.email
        }

        for (data: Histori in helper.getHistori()) {
            row = "1"
        }
        if(row.equals("0")){
            val errorDialog = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {

                    }

                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Belum ada histori penukaran" ).setPositiveButton("Tutup", errorDialog)
                    .setNegativeButton("", errorDialog).show()
        }else{
            initialize()
            setupList()
            loaddata()
        }




        return v

    }
    private fun loaddata() {
        val helper = KotlinHelper(context)
        for (data: Histori in helper.getHistori()) {
            val myItem = historiTukarPointListItem()
            myItem.nama = data.nama_tukar
            myItem.id_tukar = data.id.toString()
            arrayList!!.add(myItem)
        }
        historiTukarPointAdapter!!.notifyDataSetChanged()


    }

    private fun setupList() {
        rlItems!!.layoutManager = layoutManager
        rlItems!!.adapter = historiTukarPointAdapter
    }

    private fun initialize() {
        arrayList = ArrayList<historiTukarPointListItem>()
        layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        historiTukarPointAdapter = historiTukarPointAdapter(arrayList!!, context, R.layout.item_histori)

    }
//    @SuppressLint("MissingSuperCall")
//    override fun onDetach() {
//        //  super.onDetach()
////        var frg: Fragment? = null
////        frg = activity.supportFragmentManager.findFragmentByTag(context.)
////        val ft = activity.supportFragmentManager.beginTransaction()
//////        ft.detach(frg)
////        ft.attach(frg)
////        ft.commit()
//    }


}// Required empty public constructor
