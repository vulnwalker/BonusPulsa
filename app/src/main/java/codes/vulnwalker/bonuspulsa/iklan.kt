package codes.vulnwalker.bonuspulsa

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.ads.*


class iklan : AppCompatActivity() {
    lateinit var mInterstitialAd: InterstitialAd
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iklan)
        MobileAds.initialize(this,"ca-app-pub-7139939522364878~7784504105")
        val mNativeExpressAdView = findViewById<NativeExpressAdView>(R.id.adView2)

        val request = AdRequest.Builder().build()
        mNativeExpressAdView.loadAd(request)


        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-7139939522364878/5632073350"
        mInterstitialAd.loadAd(AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("5DD3F07F839D23BB232AF58AA9665118")
                .build())
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                showInterstitialAd()
            }
        }

        val adView = findViewById<AdView>(R.id.adview)
        val adRequest = AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("5DD3F07F839D23BB232AF58AA9665118")
                .build()
        adView.loadAd(adRequest)

    }

    fun showInterstitialAd() {
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        }
    }

}