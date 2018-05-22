package codes.vulnwalker.bonuspulsa.fragment.tukar_point


import `in`.creativelizard.recyclerviewtest.tukarPointAdapter
import `in`.creativelizard.recyclerviewtest.tukarPointListItem
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar

import codes.vulnwalker.bonuspulsa.R
import codes.vulnwalker.bonuspulsa.volley.URL
import com.android.volley.*
import com.android.volley.Response.Listener
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.util.HashMap
import org.json.JSONArray
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class tukarPointFragment : Fragment() {
    private var tukarPointAdapter: tukarPointAdapter? = null
    private var arrayList: ArrayList<tukarPointListItem>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var rlItems : RecyclerView
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater!!.inflate(R.layout.fragment_tukar_point, container, false)
        rlItems = v.findViewById(R.id.rcc)

        initialize()
        setupList()
        loaddata()



        return v

    }
    private fun loaddata() {

        val queue = Volley.newRequestQueue(context)
        val response: String? = null
        var currentSaldo = "0"
        val postRequest = object : StringRequest(Method.POST, URL.GET_LIST_TUKAR, Listener<String>{
            response ->
            val resp = JSONObject(response)
            val err = resp.getString("err")
            val cek = resp.getString("cek")
            if(err.equals("")){
                val jsonContent = resp.getString("content").toString()
                val jsonArrayContent = JSONArray(jsonContent)
                for (i in 0..(jsonArrayContent.length() - 1)) {
                    val content = jsonArrayContent.getJSONObject(i)
                    val myItem = tukarPointListItem()
                    myItem.nama = content.getString("nama_tukar")
                    myItem.id_tukar = content.getString("id")
                    arrayList!!.add(myItem)

                }
                tukarPointAdapter!!.notifyDataSetChanged()


            }else{
            }

        },
                Response.ErrorListener {
                    //  toast("error")
                }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("email", "test")
                return params
            }
        }
        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(postRequest)



    }

    private fun setupList() {
        rlItems!!.layoutManager = layoutManager
        rlItems!!.adapter = tukarPointAdapter
    }

    private fun initialize() {
        arrayList = ArrayList<tukarPointListItem>()
        layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        tukarPointAdapter = tukarPointAdapter(arrayList!!, context, R.layout.item_tukar)

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

