package codes.vulnwalker.bonuspulsa

import android.annotation.TargetApi
import android.app.*
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.provider.Settings
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import android.widget.TextView
import android.widget.Toast
import codes.vulnwalker.bonuspulsa.database.Account
import codes.vulnwalker.bonuspulsa.database.KotlinHelper
import codes.vulnwalker.bonuspulsa.volley.URL
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.ads.*
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener


import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.toast
import org.json.JSONObject
import java.security.NoSuchAlgorithmException
import java.util.HashMap
import java.util.Random

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.*
import android.support.v7.app.AlertDialog
import android.support.v7.app.NotificationCompat
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import codes.vulnwalker.bonuspulsa.database.Histori
import codes.vulnwalker.bonuspulsa.fragment.aboutUsFragment
import codes.vulnwalker.bonuspulsa.fragment.bantuan.bantuanFragment
import codes.vulnwalker.bonuspulsa.fragment.histori_tukar_point.historiTukarFragment
import codes.vulnwalker.bonuspulsa.fragment.tukar_point.tukarPointFragment
import io.vrinda.kotlinpermissions.DeviceInfo
import kotlinx.android.synthetic.main.nav_header_main.*
import org.json.JSONArray
import kotlin.coroutines.experimental.EmptyCoroutineContext.plus
import kotlin.text.Typography.tm

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var mInterstitialAd: InterstitialAd
    lateinit var mRewardedVideoAd: RewardedVideoAd
    lateinit var mRewardedVideoAd2: RewardedVideoAd
    lateinit var mRewardedVideoAd3: RewardedVideoAd
    lateinit var android_id : String
    lateinit var deviceId : String
    lateinit var publicEmail : String
    lateinit var myPoint : TextView
    lateinit var labelNama : TextView
    lateinit var labelEmail : TextView
    lateinit var random : Random
    val helper = KotlinHelper(this)
    lateinit var  spinner : ProgressBar
    lateinit var  messenger : Messenger
    var idVideoAds : String = "ca-app-pub-3417587124775040/1847447418"
    var idVideoAds2 : String = "ca-app-pub-3417587124775040/4249600070"
    var idVideoAds3 : String = "ca-app-pub-3417587124775040/8394349902"
    var idPopupAds : String = "ca-app-pub-3417587124775040/6435549751"
    var publisherID : String = "ca-app-pub-3417587124775040~2286431111"
    var apkVersion : String = "2.4.3"



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tm = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val operatorName = tm.simOperatorName
        spinner = findViewById<ProgressBar>(R.id.progressBar1)
        spinner.setVisibility(View.GONE)

        setSupportActionBar(toolbar)
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        val random = Random()
        val textNama = nav_view.getHeaderView(0).findViewById<TextView>(R.id.labelNama)
        textNama.text = intent.getStringExtra("namaLengkap")
        val textEmail = nav_view.getHeaderView(0).findViewById<TextView>(R.id.labelEmail)
        textEmail.text = intent.getStringExtra("email")
        publicEmail = intent.getStringExtra("email").toString()
        myPoint = nav_view.getHeaderView(0).findViewById<TextView>(R.id.mySaldo)
        labelNama = nav_view.getHeaderView(0).findViewById<TextView>(R.id.labelNama)
        labelEmail = nav_view.getHeaderView(0).findViewById<TextView>(R.id.labelEmail)
        syncManualOnLoad()

        absen.setOnClickListener{
            absenHarian()
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mainLayout.setBackgroundResource(R.drawable.bg)
//        }else{
            mainLayout.setBackgroundColor(Color.DKGRAY)
//        }



        //Iklan
        android_id = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
        deviceId = md5(android_id).toUpperCase()
        MobileAds.initialize(this,publisherID)

        playVideoAds.setOnClickListener{
            if(!operatorName.toString().equals("Android")){
                requestVideo()
                settingIklan()
            }else{
                val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }
                val errNotice = AlertDialog.Builder(this)
                errNotice.setMessage("IMEI TIDAK VALID !").setPositiveButton("Tutup", pesanResponse)
                        .setNegativeButton("", pesanResponse).show()
            }

        }

        playVideoAds2.setOnClickListener{
            if(!operatorName.toString().equals("Android")){
                requestVideo2()
                settingIklan2()
            }else{
                val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }
                val errNotice = AlertDialog.Builder(this)
                errNotice.setMessage("IMEI TIDAK VALID !").setPositiveButton("Tutup", pesanResponse)
                        .setNegativeButton("", pesanResponse).show()
            }
        }

        playVideoAds3.setOnClickListener{
            if(!operatorName.toString().equals("Android")){
                requestVideo3()
                settingIklan3()
            }else{
                val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }
                val errNotice = AlertDialog.Builder(this)
                errNotice.setMessage("IMEI TIDAK VALID !").setPositiveButton("Tutup", pesanResponse)
                        .setNegativeButton("", pesanResponse).show()
            }

        }
        clickAds.setOnClickListener{
            if(!operatorName.toString().equals("Android")){
                requestKlik()
            }else{
                val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }
                val errNotice = AlertDialog.Builder(this)
                errNotice.setMessage("IMEI TIDAK VALID !").setPositiveButton("Tutup", pesanResponse)
                        .setNegativeButton("", pesanResponse).show()
            }

        }
        clickAds2.setOnClickListener{
            if(!operatorName.toString().equals("Android")){
                requestKlik2()
            }else{
                val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }
                val errNotice = AlertDialog.Builder(this)
                errNotice.setMessage("IMEI TIDAK VALID !").setPositiveButton("Tutup", pesanResponse)
                        .setNegativeButton("", pesanResponse).show()
            }

        }



        // popupRequest()


    }


    fun popupRequestAbsen(){
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-3417587124775040/6435549751"
        mInterstitialAd.loadAd(AdRequest.Builder()
                .build())


        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {

                mInterstitialAd.show()
            }

            override fun onAdFailedToLoad(p0: Int) {
                mInterstitialAd.loadAd(AdRequest.Builder()
                        .build())
            }

            override fun onAdLeftApplication() {
                super.onAdLeftApplication()
            }

            override fun onAdClosed() {

                super.onAdClosed()
            }


        }

    }

    fun popupRequestBonusIklan(){
        var iklanTerklik :Int  = 0
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-3417587124775040/4375479115"
        mInterstitialAd.loadAd(AdRequest.Builder()
                .build())


        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                postRequestAds("BONUS IKLAN")
                mInterstitialAd.show()
                iklanTerklik = 0
            }

            override fun onAdFailedToLoad(p0: Int) {
                mInterstitialAd.loadAd(AdRequest.Builder()
                        .build())
            }

            override fun onAdLeftApplication() {
                super.onAdLeftApplication()
                iklanTerklik = 1



            }

            override fun onAdClosed() {
                if(iklanTerklik == 1){
                    getReward("BONUS IKLAN")
                }else{
                    toast("Iklan belum di klik point tidak bertambah")
                }
                super.onAdClosed()
                parentMainMenu.visibility = View.VISIBLE
                spinner.setVisibility(View.GONE);
            }


        }

    }

    fun popupRequestBonusIklan2(){
        var iklanTerklik :Int  = 0
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-3417587124775040/7076251925"
        mInterstitialAd.loadAd(AdRequest.Builder()
                .build())


        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                postRequestAds("BONUS IKLAN 2")
                mInterstitialAd.show()
                iklanTerklik = 0
            }

            override fun onAdFailedToLoad(p0: Int) {
                mInterstitialAd.loadAd(AdRequest.Builder()
                        .build())
            }

            override fun onAdLeftApplication() {
                super.onAdLeftApplication()
                iklanTerklik = 1



            }

            override fun onAdClosed() {
                if(iklanTerklik == 1){
                    getReward("BONUS IKLAN 2")
                }else{
                    toast("Iklan belum di klik point tidak bertambah")
                }
                super.onAdClosed()
                parentMainMenu.visibility = View.VISIBLE
                spinner.setVisibility(View.GONE);
            }


        }

    }


    fun requestVideo(){
        val queue = Volley.newRequestQueue(this@MainActivity)
        val response: String? = null
        var currentSaldo: String? = null
        val postRequest = object : StringRequest(Method.POST, URL.CEK_IKLAN, Response.Listener<String>{
            response ->
            val resp = JSONObject(response)
            val err = resp.getString("err")

            if(err.toString().equals("")){
                mRewardedVideoAd.loadAd((idVideoAds), AdRequest.Builder()
                        //  .addTestDevice(deviceId)
                        .build())
                parentMainMenu.visibility = View.GONE
                spinner.setVisibility(View.VISIBLE)
            }else{
                val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }
                val builder = AlertDialog.Builder(this)
                builder.setMessage(err.toString()).setPositiveButton("Tutup", pesanResponse)
                        .setNegativeButton("", pesanResponse).show()


            }

        },
                Response.ErrorListener {
                    toast("error")
                }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("email", publicEmail)
                params.put("jenis_iklan", "TONTON VIDEO")
                params.put("versiAPK", apkVersion)
                return params
            }
        }
        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(postRequest)





    }


    fun requestVideo2(){
        val queue = Volley.newRequestQueue(this@MainActivity)
        val response: String? = null
        var currentSaldo: String? = null
        val postRequest = object : StringRequest(Method.POST, URL.CEK_IKLAN, Response.Listener<String>{
            response ->
            val resp = JSONObject(response)
            val err = resp.getString("err")

            if(err.toString().equals("")){
                mRewardedVideoAd2.loadAd((idVideoAds2), AdRequest.Builder()
                        //  .addTestDevice(deviceId)
                        .build())
                parentMainMenu.visibility = View.GONE
                spinner.setVisibility(View.VISIBLE)
            }else{
                val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }
                val builder = AlertDialog.Builder(this)
                builder.setMessage(err.toString()).setPositiveButton("Tutup", pesanResponse)
                        .setNegativeButton("", pesanResponse).show()


            }

        },
                Response.ErrorListener {
                    toast("error")
                }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("email", publicEmail)
                params.put("jenis_iklan", "TONTON VIDEO 2")
                params.put("versiAPK", apkVersion)
                return params
            }
        }
        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(postRequest)





    }



    fun requestVideo3(){
        val queue = Volley.newRequestQueue(this@MainActivity)
        val response: String? = null
        var currentSaldo: String? = null
        val postRequest = object : StringRequest(Method.POST, URL.CEK_IKLAN, Response.Listener<String>{
            response ->
            val resp = JSONObject(response)
            val err = resp.getString("err")

            if(err.toString().equals("")){
                mRewardedVideoAd3.loadAd((idVideoAds3), AdRequest.Builder()
                        //  .addTestDevice(deviceId)
                        .build())
                parentMainMenu.visibility = View.GONE
                spinner.setVisibility(View.VISIBLE)
            }else{
                val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }
                val builder = AlertDialog.Builder(this)
                builder.setMessage(err.toString()).setPositiveButton("Tutup", pesanResponse)
                        .setNegativeButton("", pesanResponse).show()


            }

        },
                Response.ErrorListener {
                    toast("error")
                }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("email", publicEmail)
                params.put("jenis_iklan", "TONTON VIDEO 3")
                params.put("versiAPK", apkVersion)
                return params
            }
        }
        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(postRequest)





    }





    fun requestKlik(){
        val queue = Volley.newRequestQueue(this@MainActivity)
        val response: String? = null
        var currentSaldo: String? = null
        val postRequest = object : StringRequest(Method.POST, URL.CEK_IKLAN, Response.Listener<String>{
            response ->
            val resp = JSONObject(response)
            val err = resp.getString("err")

            if(err.toString().equals("")){
                popupRequestBonusIklan()
                parentMainMenu.visibility = View.GONE
                spinner.setVisibility(View.VISIBLE)
            }else{
                val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }
                val builder = AlertDialog.Builder(this)
                builder.setMessage(err.toString()).setPositiveButton("Tutup", pesanResponse)
                        .setNegativeButton("", pesanResponse).show()


            }

        },
                Response.ErrorListener {
                    toast("error")
                }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("email", publicEmail)
                params.put("jenis_iklan", "BONUS IKLAN")
                params.put("versiAPK", apkVersion)
                return params
            }
        }
        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(postRequest)





    }

    fun requestKlik2(){
        val queue = Volley.newRequestQueue(this@MainActivity)
        val response: String? = null
        var currentSaldo: String? = null
        val postRequest = object : StringRequest(Method.POST, URL.CEK_IKLAN, Response.Listener<String>{
            response ->
            val resp = JSONObject(response)
            val err = resp.getString("err")

            if(err.toString().equals("")){
                popupRequestBonusIklan2()
                parentMainMenu.visibility = View.GONE
                spinner.setVisibility(View.VISIBLE)
            }else{
                val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }
                val builder = AlertDialog.Builder(this)
                builder.setMessage(err.toString()).setPositiveButton("Tutup", pesanResponse)
                        .setNegativeButton("", pesanResponse).show()


            }

        },
                Response.ErrorListener {
                    toast("error")
                }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("email", publicEmail)
                params.put("jenis_iklan", "BONUS IKLAN 2")
                params.put("versiAPK", apkVersion)
                return params
            }
        }
        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(postRequest)





    }



    fun refreshBalance(){
        var dbAvailable : String = "0"
        for (data: Account in helper.getAccount()) {
            myPoint.text = rupiahFormat(data.saldo)
            labelNama.text = data.nama_lengkap
            labelEmail.text = data.email
            publicEmail = data.email
            dbAvailable = "1"
        }
        if(dbAvailable.equals("0")){
            logout()
        }
    }

    private fun rupiahFormat(s: String): String {
        if (s.length <= 3)
            return s
        val first = (s.length - 1) % 3 + 1
        val buf = StringBuilder(s.substring(0, first))
        var i = first
        while (i < s.length) {
            buf.append('.').append(s.substring(i, i + 3))
            i += 3
        }
        return buf.toString()
    }

    fun settingIklan(){
//        //=========== Native Express Ads
//        val adview2 = findViewById<NativeExpressAdView>(R.id.adView2)
//        val adRequest2 = AdRequest.Builder()
//                //.addTestDevice(deviceId)
//                .build()
//        adview2.loadAd(adRequest2)
//
//        adview2.adListener = object : AdListener() {
//            override fun onAdFailedToLoad(p0: Int) {
//                val adRequest2 = AdRequest.Builder()
//                    //    .addTestDevice(deviceId)
//                        .build()
//
//                adview2.loadAd(adRequest2)
//            }
//
//        }
//
//        //=========== Banner Ads
//        val adView = findViewById<AdView>(R.id.adview)
//        val adRequest = AdRequest.Builder()
//              //  .addTestDevice(deviceId)
//                .build()
//        adView.loadAd(adRequest)
//
//        adView.adListener = object : AdListener() {
//            override fun onAdFailedToLoad(p0: Int) {
//                val adRequest = AdRequest.Builder()
//                      //  .addTestDevice(deviceId)
//                        .build()
//                adView.loadAd(adRequest)
//            }
//
//        }

        //============================> PopUp Ads
//        var terklik : String
//        terklik = "0"
//
//        mInterstitialAd = InterstitialAd(this)
//        mInterstitialAd.adUnitId = idPopupAds
//        mInterstitialAd.adListener = object : AdListener() {
//            override fun onAdLoaded() {
//                postRequestAds("KLIK IKLAN")
//                mInterstitialAd.show()
//                parentMainMenu.visibility = View.VISIBLE
//                spinner.setVisibility(View.GONE)
//            }
//
//            override fun onAdFailedToLoad(p0: Int) {
//                mInterstitialAd.loadAd(AdRequest.Builder()
//                        .addTestDevice(deviceId)
//                        .build())
//            }
//
//            override fun onAdLeftApplication() {
//                terklik = "1"
//                super.onAdLeftApplication()
//            }
//
//            override fun onAdClosed() {
//                if(terklik.equals("0")){
//                    val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
//                        when (which) {
//                            DialogInterface.BUTTON_POSITIVE -> {
//                            }
//                            DialogInterface.BUTTON_NEGATIVE -> {
//                            }
//                        }
//                    }
//                    val builder = AlertDialog.Builder(this@MainActivity)
//                    builder.setMessage("Iklan belum diklik point tidak bertambah").setPositiveButton("Tutup", pesanResponse)
//                            .setNegativeButton("", pesanResponse).show()
//                }else{
//                    val rewardPoint = (20..40).random()
//                    Toast.makeText(baseContext, "Anda Mendapatkan "+rewardPoint.toString()+" point", Toast.LENGTH_SHORT).show()
//                    getReward(rewardPoint,"KLIK IKLAN")
//                    terklik = "0"
//                    super.onAdClosed()
//                }
//
//            }
//
//
//        }

        //=========== Video Ads
        var videoTerklik : String = "0"
        mRewardedVideoAd= MobileAds.getRewardedVideoAdInstance(this)
        mRewardedVideoAd.rewardedVideoAdListener=object : RewardedVideoAdListener {

            override fun onRewarded(rewardItem: RewardItem) {
                videoTerklik = "1"

            }

            override fun onRewardedVideoAdLoaded() {
                postRequestAds("TONTON VIDEO")
                parentMainMenu.visibility = View.VISIBLE
                spinner.setVisibility(View.GONE);
                mRewardedVideoAd.show()
                // Toast.makeText(baseContext, "Ad loaded.", Toast.LENGTH_SHORT).show()
                //  toast("Video1")

            }

            override fun onRewardedVideoAdOpened() {
                //  Toast.makeText(baseContext, "Ad opened.", Toast.LENGTH_SHORT).show()
            }

            override fun onRewardedVideoStarted() {
                //  Toast.makeText(baseContext, "Ad started.", Toast.LENGTH_SHORT).show()
            }

            override fun onRewardedVideoAdClosed() {
                //Toast.makeText(baseContext, "Ad closed.", Toast.LENGTH_SHORT).show()
                if(videoTerklik.equals("1")){
                    val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                            }
                        }
                    }
                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setMessage("Iklan belum diklik point tidak bertambah").setPositiveButton("Tutup", pesanResponse)
                            .setNegativeButton("", pesanResponse).show()
                }else if(videoTerklik.equals("2")){
                    getReward("TONTON VIDEO")
                }else{
                    val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                            }
                        }
                    }
                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setMessage("Iklan belum selesai point tidak bertambah").setPositiveButton("Tutup", pesanResponse)
                            .setNegativeButton("", pesanResponse).show()
                }
            }

            override fun onRewardedVideoAdLeftApplication() {
                //Toast.makeText(baseContext, "Ad left application.", Toast.LENGTH_SHORT).show()
                if(videoTerklik.equals("1")){
                    videoTerklik = "2"
                }


            }

            override fun onRewardedVideoAdFailedToLoad(i: Int) {
                //Toast.makeText(baseContext, "Ad failed to load. kode ="+i.toString(), Toast.LENGTH_SHORT).show()
//                val randomNumber = (1..3).random()
//                if(randomNumber.toString().equals("1")){
//                    idVideoAds  = "ca-app-pub-7139939522364878/3593977653"
//
//                }else{
//
//                    idVideoAds = "ca-app-pub-7139939522364878/4875768141"
//                }
//                mRewardedVideoAd.loadAd((idVideoAds), AdRequest.Builder()
//                     //  .addTestDevice(deviceId)
//                        .build())
                val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }
                val errNotice = AlertDialog.Builder(this@MainActivity)
                errNotice.setMessage("GAGAL LOAD IKLAN !").setPositiveButton("Tutup", pesanResponse)
                        .setNegativeButton("", pesanResponse).show()
                parentMainMenu.visibility = View.VISIBLE
                spinner.setVisibility(View.GONE);


            }
        }





    }


    fun settingIklan2(){
//        //=========== Native Express Ads
//        val adview2 = findViewById<NativeExpressAdView>(R.id.adView2)
//        val adRequest2 = AdRequest.Builder()
//                //.addTestDevice(deviceId)
//                .build()
//        adview2.loadAd(adRequest2)
//
//        adview2.adListener = object : AdListener() {
//            override fun onAdFailedToLoad(p0: Int) {
//                val adRequest2 = AdRequest.Builder()
//                    //    .addTestDevice(deviceId)
//                        .build()
//
//                adview2.loadAd(adRequest2)
//            }
//
//        }
//
//        //=========== Banner Ads
//        val adView = findViewById<AdView>(R.id.adview)
//        val adRequest = AdRequest.Builder()
//              //  .addTestDevice(deviceId)
//                .build()
//        adView.loadAd(adRequest)
//
//        adView.adListener = object : AdListener() {
//            override fun onAdFailedToLoad(p0: Int) {
//                val adRequest = AdRequest.Builder()
//                      //  .addTestDevice(deviceId)
//                        .build()
//                adView.loadAd(adRequest)
//            }
//
//        }

        //============================> PopUp Ads
//        var terklik : String
//        terklik = "0"
//
//        mInterstitialAd = InterstitialAd(this)
//        mInterstitialAd.adUnitId = idPopupAds
//        mInterstitialAd.adListener = object : AdListener() {
//            override fun onAdLoaded() {
//                postRequestAds("KLIK IKLAN")
//                mInterstitialAd.show()
//                parentMainMenu.visibility = View.VISIBLE
//                spinner.setVisibility(View.GONE)
//            }
//
//            override fun onAdFailedToLoad(p0: Int) {
//                mInterstitialAd.loadAd(AdRequest.Builder()
//                        .addTestDevice(deviceId)
//                        .build())
//            }
//
//            override fun onAdLeftApplication() {
//                terklik = "1"
//                super.onAdLeftApplication()
//            }
//
//            override fun onAdClosed() {
//                if(terklik.equals("0")){
//                    val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
//                        when (which) {
//                            DialogInterface.BUTTON_POSITIVE -> {
//                            }
//                            DialogInterface.BUTTON_NEGATIVE -> {
//                            }
//                        }
//                    }
//                    val builder = AlertDialog.Builder(this@MainActivity)
//                    builder.setMessage("Iklan belum diklik point tidak bertambah").setPositiveButton("Tutup", pesanResponse)
//                            .setNegativeButton("", pesanResponse).show()
//                }else{
//                    val rewardPoint = (20..40).random()
//                    Toast.makeText(baseContext, "Anda Mendapatkan "+rewardPoint.toString()+" point", Toast.LENGTH_SHORT).show()
//                    getReward(rewardPoint,"KLIK IKLAN")
//                    terklik = "0"
//                    super.onAdClosed()
//                }
//
//            }
//
//
//        }




        //=========== Video Ads 2
        var videoTerklik2 : String = "0"
        mRewardedVideoAd2= MobileAds.getRewardedVideoAdInstance(this)
        mRewardedVideoAd2.rewardedVideoAdListener=object : RewardedVideoAdListener {

            override fun onRewarded(rewardItem: RewardItem) {
                videoTerklik2 = "1"

            }

            override fun onRewardedVideoAdLoaded() {
                postRequestAds("TONTON VIDEO 2")
                parentMainMenu.visibility = View.VISIBLE
                spinner.setVisibility(View.GONE);
                // Toast.makeText(baseContext, "Ad loaded.", Toast.LENGTH_SHORT).show()
                // toast("Video2")
                mRewardedVideoAd2.show()
            }

            override fun onRewardedVideoAdOpened() {
                //  Toast.makeText(baseContext, "Ad opened.", Toast.LENGTH_SHORT).show()
            }

            override fun onRewardedVideoStarted() {
                //  Toast.makeText(baseContext, "Ad started.", Toast.LENGTH_SHORT).show()
            }

            override fun onRewardedVideoAdClosed() {
                //Toast.makeText(baseContext, "Ad closed.", Toast.LENGTH_SHORT).show()
                if(videoTerklik2.equals("1")){
                    val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                            }
                        }
                    }
                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setMessage("Iklan belum diklik point tidak bertambah").setPositiveButton("Tutup", pesanResponse)
                            .setNegativeButton("", pesanResponse).show()
                }else if(videoTerklik2.equals("2")){
//                    val rewardPoint = (20..50).random()
//                    Toast.makeText(baseContext, "Anda Mendapatkan "+rewardPoint.toString()+" point", Toast.LENGTH_SHORT).show()
                    getReward("TONTON VIDEO 2")
                }else{
                    val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                            }
                        }
                    }
                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setMessage("Iklan belum selesai point tidak bertambah").setPositiveButton("Tutup", pesanResponse)
                            .setNegativeButton("", pesanResponse).show()
                }
            }

            override fun onRewardedVideoAdLeftApplication() {
                //Toast.makeText(baseContext, "Ad left application.", Toast.LENGTH_SHORT).show()
                if(videoTerklik2.equals("1")){
                    videoTerklik2 = "2"
                }


            }

            override fun onRewardedVideoAdFailedToLoad(i: Int) {
                //Toast.makeText(baseContext, "Ad failed to load. kode ="+i.toString(), Toast.LENGTH_SHORT).show()
//                val randomNumber = (1..3).random()
//                if(randomNumber.toString().equals("1")){
//                    idVideoAds2  = "ca-app-pub-7139939522364878/3593977653"
//
//                }else{
//
//                    idVideoAds2 = "ca-app-pub-7139939522364878/4875768141"
//                }
//                mRewardedVideoAd2.loadAd((idVideoAds2), AdRequest.Builder()
//                        //  .addTestDevice(deviceId)
//                        .build())
                val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }
                val errNotice = AlertDialog.Builder(this@MainActivity)
                errNotice.setMessage("GAGAL LOAD IKLAN !").setPositiveButton("Tutup", pesanResponse)
                        .setNegativeButton("", pesanResponse).show()
                parentMainMenu.visibility = View.VISIBLE
                spinner.setVisibility(View.GONE);


            }
        }



    }

    fun settingIklan3(){
//        //=========== Native Express Ads
//        val adview2 = findViewById<NativeExpressAdView>(R.id.adView2)
//        val adRequest2 = AdRequest.Builder()
//                //.addTestDevice(deviceId)
//                .build()
//        adview2.loadAd(adRequest2)
//
//        adview2.adListener = object : AdListener() {
//            override fun onAdFailedToLoad(p0: Int) {
//                val adRequest2 = AdRequest.Builder()
//                    //    .addTestDevice(deviceId)
//                        .build()
//
//                adview2.loadAd(adRequest2)
//            }
//
//        }
//
//        //=========== Banner Ads
//        val adView = findViewById<AdView>(R.id.adview)
//        val adRequest = AdRequest.Builder()
//              //  .addTestDevice(deviceId)
//                .build()
//        adView.loadAd(adRequest)
//
//        adView.adListener = object : AdListener() {
//            override fun onAdFailedToLoad(p0: Int) {
//                val adRequest = AdRequest.Builder()
//                      //  .addTestDevice(deviceId)
//                        .build()
//                adView.loadAd(adRequest)
//            }
//
//        }

        //============================> PopUp Ads
//        var terklik : String
//        terklik = "0"
//
//        mInterstitialAd = InterstitialAd(this)
//        mInterstitialAd.adUnitId = idPopupAds
//        mInterstitialAd.adListener = object : AdListener() {
//            override fun onAdLoaded() {
//                postRequestAds("KLIK IKLAN")
//                mInterstitialAd.show()
//                parentMainMenu.visibility = View.VISIBLE
//                spinner.setVisibility(View.GONE)
//            }
//
//            override fun onAdFailedToLoad(p0: Int) {
//                mInterstitialAd.loadAd(AdRequest.Builder()
//                        .addTestDevice(deviceId)
//                        .build())
//            }
//
//            override fun onAdLeftApplication() {
//                terklik = "1"
//                super.onAdLeftApplication()
//            }
//
//            override fun onAdClosed() {
//                if(terklik.equals("0")){
//                    val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
//                        when (which) {
//                            DialogInterface.BUTTON_POSITIVE -> {
//                            }
//                            DialogInterface.BUTTON_NEGATIVE -> {
//                            }
//                        }
//                    }
//                    val builder = AlertDialog.Builder(this@MainActivity)
//                    builder.setMessage("Iklan belum diklik point tidak bertambah").setPositiveButton("Tutup", pesanResponse)
//                            .setNegativeButton("", pesanResponse).show()
//                }else{
//                    val rewardPoint = (20..40).random()
//                    Toast.makeText(baseContext, "Anda Mendapatkan "+rewardPoint.toString()+" point", Toast.LENGTH_SHORT).show()
//                    getReward(rewardPoint,"KLIK IKLAN")
//                    terklik = "0"
//                    super.onAdClosed()
//                }
//
//            }
//
//
//        }





        //=========== Video Ads 3
        var videoTerklik3 : String = "0"
        mRewardedVideoAd3= MobileAds.getRewardedVideoAdInstance(this)
        mRewardedVideoAd3.rewardedVideoAdListener=object : RewardedVideoAdListener {

            override fun onRewarded(rewardItem: RewardItem) {
                videoTerklik3 = "1"

            }

            override fun onRewardedVideoAdLoaded() {
                postRequestAds("TONTON VIDEO 3")
                parentMainMenu.visibility = View.VISIBLE
                spinner.setVisibility(View.GONE);
                // Toast.makeText(baseContext, "Ad loaded.", Toast.LENGTH_SHORT).show
                //toast("Video3")
                mRewardedVideoAd3.show()
            }

            override fun onRewardedVideoAdOpened() {
                //  Toast.makeText(baseContext, "Ad opened.", Toast.LENGTH_SHORT).show()
            }

            override fun onRewardedVideoStarted() {
                //  Toast.makeText(baseContext, "Ad started.", Toast.LENGTH_SHORT).show()
            }

            override fun onRewardedVideoAdClosed() {
                //Toast.makeText(baseContext, "Ad closed.", Toast.LENGTH_SHORT).show()
                if(videoTerklik3.equals("1")){
                    val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                            }
                        }
                    }
                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setMessage("Iklan belum diklik point tidak bertambah").setPositiveButton("Tutup", pesanResponse)
                            .setNegativeButton("", pesanResponse).show()
                }else if(videoTerklik3.equals("3")){
//                    val rewardPoint = (20..50).random()
//                    Toast.makeText(baseContext, "Anda Mendapatkan "+rewardPoint.toString()+" point", Toast.LENGTH_SHORT).show()
                    getReward("TONTON VIDEO 3")
                }else{
                    val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                            }
                        }
                    }
                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setMessage("Iklan belum selesai point tidak bertambah").setPositiveButton("Tutup", pesanResponse)
                            .setNegativeButton("", pesanResponse).show()
                }
            }

            override fun onRewardedVideoAdLeftApplication() {
                //Toast.makeText(baseContext, "Ad left application.", Toast.LENGTH_SHORT).show()
                if(videoTerklik3.equals("1")){
                    videoTerklik3 = "3"
                }


            }

            override fun onRewardedVideoAdFailedToLoad(i: Int) {
                //Toast.makeText(baseContext, "Ad failed to load. kode ="+i.toString(), Toast.LENGTH_SHORT).show()
//                val randomNumber = (1..3).random()
//                if(randomNumber.toString().equals("1")){
//                    idVideoAds3  = "ca-app-pub-7139939533364878/3593977653"
//
//                }else{
//
//                    idVideoAds3 = "ca-app-pub-7139939533364878/4875768141"
//                }
//                mRewardedVideoAd3.loadAd((idVideoAds3), AdRequest.Builder()
//                        //  .addTestDevice(deviceId)
//                        .build())
                val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }
                val errNotice = AlertDialog.Builder(this@MainActivity)
                errNotice.setMessage("GAGAL LOAD IKLAN !").setPositiveButton("Tutup", pesanResponse)
                        .setNegativeButton("", pesanResponse).show()
                parentMainMenu.visibility = View.VISIBLE
                spinner.setVisibility(View.GONE);

            }
        }


    }
    fun md5(s: String): String {
        try {
            // Create MD5 Hash
            val digest=java.security.MessageDigest.getInstance("MD5")
            digest.update(s.toByteArray())
            val messageDigest=digest.digest()

            // Create Hex String
            val hexString=StringBuffer()
            for (i in messageDigest.indices)
                hexString.append(Integer.toHexString(0xFF and messageDigest[i].toInt()))
            return hexString.toString()

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return ""
    }
    fun logout(){
        //   toast("Logout")
        helper.destroyApp()
        val i = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(i)
        finish()
    }
    fun tambahSaldoLocal(jumlah : Int) {
        val db = helper.writableDatabase
        var values = ContentValues()
        val calculatedSaldo = jumlah.toString()
        values.put("saldo", calculatedSaldo)
        val retVal = db.update("ACCOUNT", values, "userID = 1 ", null)
        if (retVal >= 1) {
            Log.v("@@@WWe", " Record updated")
        } else {
            Log.v("@@@WWe", " Not updated")
        }
        db.close()
    }
    private fun getReward( jenisIklan : String) {
        val queue = Volley.newRequestQueue(this@MainActivity)
        val response: String? = null
        var currentSaldo = "0"
        var hadiah = "0"
        var rewardMessage = ""
        val postRequest = object : StringRequest(Method.POST, URL.GET_SALDO, Response.Listener<String>{
            response ->
            val resp = JSONObject(response)
            val err = resp.getString("err")
            val cek = resp.getString("cek")
            if(err.equals("")){
                val jsonContent = resp.getString("content").toString()
                val content = JSONObject(jsonContent)
                currentSaldo = content.getString("saldo").toString()
                hadiah = content.getString("hadiah").toString()
                val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }
                if(jenisIklan.equals("ABSEN HARIAN")){
                    rewardMessage = "Terimakasih telah melakukan absen, point bertambah " + hadiah
                }else{
                    rewardMessage  = "Anda Mendapatkan "+hadiah+" point"
                }
                val builder = AlertDialog.Builder(this)
                builder.setMessage(rewardMessage).setPositiveButton("Tutup", pesanResponse)
                        .setNegativeButton("", pesanResponse).show()
                val jumlah = hadiah.toInt() + currentSaldo.toInt()
                tambahSaldoLocal(jumlah)
                tambahSaldoServer(jumlah,jenisIklan,hadiah.toInt())
                refreshBalance()

            }else{
                toast(err)
            }

        },
                Response.ErrorListener {
                    toast("error")
                }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("email", publicEmail)
                params.put("jenisIklanPOST", jenisIklan)
                return params
            }
        }
        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(postRequest)
    }

    private fun tambahSaldoServer(jumlah : Int, jenisIklan : String, saldoDapat : Int) {
        val queue = Volley.newRequestQueue(this@MainActivity)
        val response: String? = null
        var currentSaldo: String? = null
        val postRequest = object : StringRequest(Method.POST, URL.ADD_SALDO, Response.Listener<String>{
            response ->

        },
                Response.ErrorListener {
                    toast("error")
                }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("email", publicEmail)
                params.put("saldo", jumlah.toString())
                params.put("jenis_iklan",jenisIklan)
                params.put("saldo_dapat",saldoDapat.toString())
                return params
            }
        }
        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(postRequest)
    }


    fun ClosedRange<Int>.random() = Random().nextInt(endInclusive - start) +  start

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if(parentMainMenu.visibility == View.VISIBLE){
                super.onBackPressed()
            }

        }
    }

    override fun onResume() {
        refreshBalance()
        // mRewardedVideoAd?.resume(this)
        super.onResume()
    }
    public override fun onPause() {
        // mRewardedVideoAd?.pause(this)
        super.onPause()
    }
    public override fun onDestroy() {
        //  mRewardedVideoAd?.destroy(this)
        super.onDestroy()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.logoutButton -> {
                val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            logout()
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Logout ?").setPositiveButton("Ya", pesanResponse)
                        .setNegativeButton("Batal", pesanResponse).show()

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        spinner.visibility = View.GONE
        when (item.itemId) {
            R.id.mainMenu -> {
                this.title = "Bonus Pulsa"

                if(parentMainMenu.visibility == View.GONE){
                    parentMainMenu.visibility = View.VISIBLE

                }
                refreshBalance()
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            }
            R.id.tukarPoint -> {
                addFragment(tukarPointFragment(), "TUKAR POINT")

                if(parentMainMenu.visibility == View.VISIBLE){
                    parentMainMenu.visibility = View.GONE
                }
                this.title = "Tukar Point"

            }
            R.id.historiTukar -> {
                addFragment(historiTukarFragment(), "HISTORI TUKAR POINT")

                if(parentMainMenu.visibility == View.VISIBLE){
                    parentMainMenu.visibility = View.GONE
                }
                this.title = "Histori Tukar Point"


            }
            R.id.sync -> {
                if(parentMainMenu.visibility == View.VISIBLE){
                    parentMainMenu.visibility = View.GONE
                }
                spinner.setVisibility(View.VISIBLE)
                syncManual()

            }

            R.id.share -> {
                try {
                    val i = Intent(Intent.ACTION_SEND)
                    i.type = "text/plain"
                    i.putExtra(Intent.EXTRA_SUBJECT, "Bonus Pulsa")
                    var sAux = "\nAda aplikasi bagus nih, dari pada bengong mending cari pulsa di Aplikasi Bonus Pulsa\n\n"
                    sAux = sAux + "https://play.google.com/store/apps/details?id=codes.vulnwalker.bonuspulsa \n\n"
                    i.putExtra(Intent.EXTRA_TEXT, sAux)
                    startActivity(Intent.createChooser(i, "choose one"))
                } catch (e: Exception) {
                    //e.toString();
                }

            }
            R.id.aboutUs -> {
                addFragment(aboutUsFragment(), "ABOUT US")

                if(parentMainMenu.visibility == View.VISIBLE){
                    parentMainMenu.visibility = View.GONE
                }
                this.title = "Tentang Kami"

            }
            R.id.bantuan -> {
                addFragment(bantuanFragment(), "BANTUAN")
                if(parentMainMenu.visibility == View.VISIBLE){
                    parentMainMenu.visibility = View.GONE
                }
                this.title = "Bantuan"

            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)


        return true
    }


    fun addFragment(fragment: Fragment, tag: String) {
        val manager = supportFragmentManager
        val ft = manager.beginTransaction()
        ft.addToBackStack(tag)
        ft.replace(R.id.mainLayout, fragment, tag)
        ft.commitAllowingStateLoss()

    }

    fun removeFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.remove(fragment)
        fragmentTransaction.commit()
    }

    fun absenHarian(){
        parentMainMenu.visibility = View.GONE
        spinner.setVisibility(View.VISIBLE);
        val queue = Volley.newRequestQueue(this@MainActivity)
        val response: String? = null
        var currentSaldo: String? = null
        val postRequest = object : StringRequest(Method.POST, URL.ABSEN, Response.Listener<String>{
            response ->
            parentMainMenu.visibility = View.VISIBLE
            spinner.setVisibility(View.GONE);

            val resp = JSONObject(response)
            val err = resp.getString("err")
            if(err.toString().equals("")){

                getReward("ABSEN HARIAN")
                popupRequestAbsen()


            }else{
                val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }
                val builder = AlertDialog.Builder(this)

                builder.setMessage(err.toString()
                ).setPositiveButton("Tutup", pesanResponse)
                        .setNegativeButton("", pesanResponse).show()


            }

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
                params.put("email", publicEmail)
                params.put("deviceName", deviceName)
                params.put("imei", deviceImei)
                params.put("versiAPK", apkVersion)

                return params
            }
        }
        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(postRequest)

    }

    fun syncManual(){
        val queue = Volley.newRequestQueue(this@MainActivity)
        val response: String? = null
        var currentSaldo: String? = null
        val postRequest = object : StringRequest(Method.POST, URL.SYNC, Response.Listener<String>{
            response ->
            clearHistoriPenukaran()
            val resp = JSONObject(response)
            val err = resp.getString("err")
            val jsonContent = resp.getString("content").toString()
            val content = JSONObject(jsonContent)
            val jsonPenukaran = content.getString("penukaran").toString()
            val jsonArrayPenukaran = JSONArray(jsonPenukaran)
            for (i in 0..(jsonArrayPenukaran.length() - 1)) {
                val penukaran = jsonArrayPenukaran.getJSONObject(i)
                val dataHistori = Histori(
                        penukaran.getString("id").toString(),
                        penukaran.getString("nama_tukar").toString(),
                        penukaran.getString("tanggal").toString(),
                        penukaran.getString("status").toString(),
                        penukaran.getString("tanggal_aksi").toString()
                )
                helper.addHistori(dataHistori)
            }
            val jsonAkun = content.getString("akun").toString()
            val jsonArrayAkun = JSONArray(jsonAkun)
            for (i in 0..(jsonArrayAkun.length() - 1)) {
                val akun = jsonArrayAkun.getJSONObject(i)
                updateAccount(akun.getString("email").toString(), akun.getString("nama_lengkap"), akun.getString("no_telepon").toString(), akun.getString("saldo").toString())
            }

            val pesanResponse = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        refreshBalance()
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Data telah di sinkronkan").setPositiveButton("Tutup", pesanResponse)
                    .setNegativeButton("", pesanResponse).show()
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            this.title = "Bonus Pulsa"

            if(parentMainMenu.visibility == View.GONE){
                parentMainMenu.visibility = View.VISIBLE
            }
            spinner.setVisibility(View.GONE)


        },
                Response.ErrorListener {
                    toast("error")
                }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("email", publicEmail)
                return params
            }
        }
        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(postRequest)

    }

    fun syncManualOnLoad(){
        val queue = Volley.newRequestQueue(this@MainActivity)
        val response: String? = null
        var currentSaldo: String? = null
        val postRequest = object : StringRequest(Method.POST, URL.SYNC, Response.Listener<String>{
            response ->
            clearHistoriPenukaran()
            val resp = JSONObject(response)
            val err = resp.getString("err")
            val jsonContent = resp.getString("content").toString()
            val content = JSONObject(jsonContent)
            val jsonPenukaran = content.getString("penukaran").toString()
            val jsonArrayPenukaran = JSONArray(jsonPenukaran)
            for (i in 0..(jsonArrayPenukaran.length() - 1)) {
                val penukaran = jsonArrayPenukaran.getJSONObject(i)
                val dataHistori = Histori(
                        penukaran.getString("id").toString(),
                        penukaran.getString("nama_tukar").toString(),
                        penukaran.getString("tanggal").toString(),
                        penukaran.getString("status").toString(),
                        penukaran.getString("tanggal_aksi").toString()
                )
                helper.addHistori(dataHistori)
            }
            val jsonAkun = content.getString("akun").toString()
            val jsonArrayAkun = JSONArray(jsonAkun)
            for (i in 0..(jsonArrayAkun.length() - 1)) {
                val akun = jsonArrayAkun.getJSONObject(i)
                updateAccount(akun.getString("email").toString(), akun.getString("nama_lengkap"), akun.getString("no_telepon").toString(), akun.getString("saldo").toString())
            }

            refreshBalance()
            if(parentMainMenu.visibility == View.GONE){
                parentMainMenu.visibility = View.VISIBLE
            }
            spinner.setVisibility(View.GONE)


        },
                Response.ErrorListener {
                    toast("error")
                }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("email", publicEmail)
                return params
            }
        }
        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(postRequest)

    }

    fun clearHistoriPenukaran(){
        val query = "DELETE FROM HISTORI_TUKAR"
        val db = helper.writableDatabase
        db.execSQL(query)
    }

    fun updateAccount(email : String, nama_lengkap : String, no_telepon : String, saldo : String){
        val query = "UPDATE ACCOUNT set email = '"+email+"',nama_lengkap = '"+nama_lengkap+"', no_telepon = '"+no_telepon+"', saldo = '"+saldo+"'"
        val db = helper.writableDatabase
        db.execSQL(query)
    }

    fun postRequestAds(jenisIklan : String){
        val queue = Volley.newRequestQueue(this@MainActivity)
        val response: String? = null

        val postRequest = object : StringRequest(Method.POST, URL.ADS_REQUEST, Response.Listener<String>{
            response ->

        },
                Response.ErrorListener {
                    toast("error")
                }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("email", publicEmail)
                params.put("jenis_iklan", jenisIklan)
                return params
            }
        }
        postRequest.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(postRequest)

    }



}


