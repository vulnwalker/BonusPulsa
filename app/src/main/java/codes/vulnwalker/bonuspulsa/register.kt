package codes.vulnwalker.bonuspulsa

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.KeyEvent
import codes.vulnwalker.bonuspulsa.volley.URL
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import io.vrinda.kotlinpermissions.DeviceInfo


import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.toast

import org.json.JSONObject


class register : AppCompatActivity() {
    private var from:String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            parentLayoutRegister.setBackgroundResource(R.drawable.bg)
        }else{
            parentLayoutRegister.setBackgroundColor(Color.DKGRAY)
        }
        this.from = intent.getStringExtra("from")

        buttonRegister.setOnClickListener {
            val queue = Volley.newRequestQueue(this@register)
//            val response: String? = null
            val postRequest = object : StringRequest(Request.Method.POST, URL.REGISTER,Response.Listener<String>{
                response ->
                val resp = JSONObject(response)
                val err = resp.getString("err")
                val cek = resp.getString("cek")
//                val content = JSONObject(resp.getString("content"))
                if(err.equals("")){
                    toast(cek)
                    pageLogin()
                }else{
                    toast(err.toString())
                }
//                toast(response.toString())

            },
                    Response.ErrorListener {
                       toast("error")
                    }
            ) {
                override fun getParams(): Map<String, String> {
                    val manufakture : String = Build.MANUFACTURER
                    val deviceModel : String = Build.DEVICE
                    val deviceImei : String = DeviceInfo.getIMEI(getApplicationContext())
                    val deviceName : String = manufakture+" "+deviceModel


                    val params = HashMap<String, String>()
                     params.put("nama", namaLengkap.text.toString())
                     params.put("email", emailRegister.text.toString())
                     params.put("password", passwordRegister.text.toString())
                     params.put("no_telepon", nomorTeleponRegister.text.toString())
                     params.put("deviceName", deviceName)
                     params.put("imei", deviceImei)



                    return params
                }
            }
            postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            queue.add(postRequest)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            when(this.from){
                "Login" -> pageLogin()

            }

            true
        } else super.onKeyDown(keyCode, event)
    }

    fun pageLogin(){
        val i = Intent(this@register, LoginActivity::class.java)
        i.putExtra("from","Login")
        startActivity(i)
        finish()
    }


}
