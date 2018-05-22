package codes.vulnwalker.bonuspulsa.fragment.bantuan


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import codes.vulnwalker.bonuspulsa.R
import codes.vulnwalker.bonuspulsa.database.Histori
import codes.vulnwalker.bonuspulsa.database.KotlinHelper
import java.util.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class bantuanFragment : Fragment() {
    private var bantuanAdapter: bantuanAdapter? = null
    private var arrayList: ArrayList<bantuanList>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var rlItems : RecyclerView
    private var myEmail : String? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.fragment_bantuan, container, false)
        rlItems = v.findViewById(R.id.rcc)
        initialize()
        setupList()
        loaddata()

        return v
    }

    private fun loaddata() {

            val myItem = bantuanList()
            myItem.namaBantuan = "Tonton Video"
            myItem.idBantuan = "1"
            arrayList!!.add(myItem)

//            val myItem2 = bantuanList()
//            myItem2.namaBantuan = "Klik Iklan"
//            myItem2.idBantuan = "2"
//            arrayList!!.add(myItem2)

            val myItem5 = bantuanList()
            myItem5.namaBantuan = "Absen Harian"
            myItem5.idBantuan = "5"
            arrayList!!.add(myItem5)

            val myItem3 = bantuanList()
            myItem3.namaBantuan = "Penukaran"
            myItem3.idBantuan = "3"
            arrayList!!.add(myItem3)

            val myItem4 = bantuanList()
            myItem4.namaBantuan = "Suspend"
            myItem4.idBantuan = "4"
            arrayList!!.add(myItem4)

        bantuanAdapter!!.notifyDataSetChanged()


    }

    private fun setupList() {
        rlItems!!.layoutManager = layoutManager
        rlItems!!.adapter = bantuanAdapter
    }

    private fun initialize() {
        arrayList = ArrayList<bantuanList>()
        layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        bantuanAdapter = bantuanAdapter(arrayList!!, context, R.layout.item_bantuan)

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
