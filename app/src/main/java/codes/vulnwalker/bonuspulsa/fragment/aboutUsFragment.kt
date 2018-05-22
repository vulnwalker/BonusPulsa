package codes.vulnwalker.bonuspulsa.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import codes.vulnwalker.bonuspulsa.MainActivity

import codes.vulnwalker.bonuspulsa.R
import android.view.KeyEvent.KEYCODE_BACK
import android.widget.Toast


/**
 * A simple [Fragment] subclass.
 */
class aboutUsFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        (activity as MainActivity).refreshBalance()


        return inflater!!.inflate(R.layout.fragment_about_us, container, false)
    }

//    @SuppressLint("MissingSuperCall")
//    override fun onDetach() {
//      //  super.onDetach()
////        var frg: Fragment? = null
////        frg = activity.supportFragmentManager.findFragmentByTag(context.)
////        val ft = activity.supportFragmentManager.beginTransaction()
//////        ft.detach(frg)
////        ft.attach(frg)
////        ft.commit()
//    }




}// Required empty public constructor
