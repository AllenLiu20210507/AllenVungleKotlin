package com.allen.allenvunglekotlin

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.*
import com.vungle.warren.*
import com.vungle.warren.error.VungleException
import okhttp3.internal.Internal.instance


class MainActivity : Activity() {
    private var titleTextView: TextView? = null
    private var btn_load_interstitial_legacy: Button? = null
    private var btn_load_rewarded_video: Button? = null
    private var btn_load_mrec: Button? = null
    private var btn_play_interstitial_legacy: Button? = null
    private var btn_play_rewarded_video: Button? = null
    private var btn_play_mrec: Button? = null
    private var btn_load_banner: Button? = null
    private var btn_show_banner: Button? = null
    private var btn_close_mrec: Button? = null
    private var btn_close_banner: Button? = null
    private var container_mrec: LinearLayout? = null
    private var container_banner: RelativeLayout? = null
    var appId: String? = null
    var iv: String? = null
    var rv: String? = null
    var mrec: String? = null
    var banner: String? = null
    val TAG = "allentest"
    private var vungleBanner: VungleBanner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.ce
        setContentView(R.layout.activity_main)
        appId = getString(R.string.app_id)
        iv = getString(R.string.placement_id_interstitial)
        rv = getString(R.string.placement_id_rewarded_video)
        mrec = getString(R.string.placement_id_mrec)
        banner=getString(R.string.placement_id_banner)
        initView()
        Vungle.init(appId!!, applicationContext, object : InitCallback {
            override fun onSuccess() {
                // Initialization has succeeded and SDK is ready to load an ad or play one if there
                // is one pre-cached already
                //load banner test
                titleTextView!!.setText(R.string.app_id)
            }

            override fun onError(throwable: VungleException) {
                titleTextView!!.text = throwable.localizedMessage
                // Initialization error occurred - throwable.getLocalizedMessage() contains error message
            }

            override fun onAutoCacheAdAvailable(placementId: String) {
                // Callback to notify when an ad becomes available for the cache optimized placement
                // NOTE: This callback works only for the cache optimized placement. Otherwise, please use
                // LoadAdCallback with loadAd API for loading placements.
                titleTextView!!.text = "onAutoCacheAdAvailable has $placementId"
            }
        })
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
    }

    private fun initView() {
        titleTextView = findViewById(R.id.text_app_id)
        titleTextView!!.setOnClickListener { makeToast(titleTextView!!.text.toString()) }
        //load button
        btn_load_interstitial_legacy = findViewById(R.id.btn_load_interstitial_legacy)
        btn_load_rewarded_video = findViewById(R.id.btn_load_rewarded_video)
        btn_load_mrec = findViewById(R.id.btn_load_mrec)
        btn_load_banner =findViewById(R.id.btn_load_banner)

        //playbutton
        btn_play_interstitial_legacy = findViewById(R.id.btn_play_interstitial_legacy)
        btn_play_rewarded_video = findViewById(R.id.btn_play_rewarded_video)
        btn_play_mrec = findViewById(R.id.btn_play_mrec)
        btn_show_banner = findViewById(R.id.btn_play_banner)
        btn_close_mrec = findViewById(R.id.btn_close_mrec)
        btn_close_banner=findViewById(R.id.btn_close_banner)
        container_mrec = findViewById(R.id.container_mrec)
        container_banner = findViewById(R.id.container_banner)
        setLoadClick(btn_load_interstitial_legacy!!, iv)
        setLoadClick(btn_load_rewarded_video!!, rv)
        setLoadMrecClick(btn_load_mrec!!, mrec)
        setLoadBannerClick(btn_load_banner!!,banner )
        setPlayClick(btn_play_interstitial_legacy!!, iv)
        setPlayClick(btn_play_rewarded_video!!, rv)
        setPlayMrecClick(btn_play_mrec!!, mrec)
        setPlayBannerClick(btn_show_banner!!,banner)
        setCloseClick(btn_close_mrec)
        setCloseClick(btn_close_banner)

    }

    private fun loadAd(placementReferenceId: String?) {
        if (Vungle.isInitialized()) {
            Vungle.loadAd(placementReferenceId!!, object : LoadAdCallback {
                override fun onAdLoad(placementReferenceId: String) {
                    makeToast(placementReferenceId + "onAdLoad")
                }

                override fun onError(placementReferenceId: String, throwable: VungleException) {
                    // Load ad error occurred - throwable.getLocalizedMessage() contains error message
                    makeToast(placementReferenceId + "onError==" + throwable.localizedMessage)
                }
            })
        }
    }

    private fun playAd(placementReferenceId: String?) {
        if (Vungle.canPlayAd(placementReferenceId!!)) {
            Vungle.playAd(placementReferenceId,null, vunglePlayAdCallback
             )
        } else {
            makeToast(placementReferenceId + "can not play")
        }
    }

    private fun setLoadClick(button: Button, placementReferenceId: String?) {
        button.setOnClickListener { //                Intent intent=new Intent(MainActivity.this,SecondActivity.class);
//                startActivity(intent);
            loadAd(placementReferenceId)
        }
    }

    private fun setPlayClick(button: Button, placementReferenceId: String?) {
        button.setOnClickListener { playAd(placementReferenceId) }
    }

    private fun setLoadMrecClick(button: Button, placementReferenceId: String?) {
        button.setOnClickListener {
            val bannerAdConfig = BannerAdConfig()
            bannerAdConfig.adSize = AdConfig.AdSize.VUNGLE_MREC
            Banners.loadBanner(mrec!!, bannerAdConfig, object : LoadAdCallback{
                override fun onAdLoad(placementReferenceId: String) {
                    makeToast(placementReferenceId + "onAdLoad")
                }

                override fun onError(placementReferenceId: String, throwable: VungleException) {
                    // Load ad error occurred - throwable.getLocalizedMessage() contains error message
                    makeToast(placementReferenceId + "onError==" + throwable.localizedMessage)
                }
            })
        }
    }


    private fun setPlayMrecClick(button: Button, placementReferenceId: String?) {
        button.setOnClickListener {
            val bannerAdConfig = BannerAdConfig()
            bannerAdConfig.adSize = AdConfig.AdSize.VUNGLE_MREC
            if(Banners.canPlayAd(mrec!!, bannerAdConfig.adSize)){
                container_mrec = findViewById(R.id.container_mrec)

                var bannerAdConfig =  BannerAdConfig();
                bannerAdConfig.setAdSize(AdConfig.AdSize.VUNGLE_MREC);

                var vungleBanner = Banners.getBanner(mrec!!, bannerAdConfig, vunglePlayAdCallback);
                container_mrec!!.addView(vungleBanner);
            }

        }
    }

    private fun setCloseClick(button: Button?) {
        button!!.setOnClickListener {
            if (container_banner!=null){
                container_banner!!.removeAllViews()
            }
            if (container_mrec!=null){
                container_mrec!!.removeAllViews()
            }
            if (vungleBanner!=null){
                vungleBanner!!.destroyAd()
            }
        }
    }





    private fun setLoadBannerClick(button: Button, placementReferenceId: String?) {
        button.setOnClickListener {
            val bannerAdConfig = BannerAdConfig()
            bannerAdConfig.adSize = AdConfig.AdSize.BANNER
            Banners.loadBanner(banner!!, bannerAdConfig, object : LoadAdCallback{
                override fun onAdLoad(placementId: String?) {
                    makeToast("onAdLoad"+placementId)
                }

                override fun onError(placementId: String?, exception: VungleException?) {
                    makeToast("onError"+placementId+"VungleException"+exception)
                }

            })
        }
    }

    private fun setPlayBannerClick(button: Button, placementReferenceId: String?) {
        button.setOnClickListener {
            val bannerAdConfig = BannerAdConfig()
            bannerAdConfig.adSize = AdConfig.AdSize.BANNER
            if (Banners.canPlayAd(banner!!, bannerAdConfig.adSize)){
                container_banner = findViewById(R.id.container_banner);

                var bannerAdConfig =  BannerAdConfig();
                bannerAdConfig.setAdSize(AdConfig.AdSize.BANNER);

                var vungleBanner = Banners.getBanner(banner!!, bannerAdConfig, vunglePlayAdCallback);
                container_banner!!.addView(vungleBanner);
            }
        }
    }




    private fun makeToast(message: String) {
        Log.e(TAG, message)
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }


    // Implement PlayAdCallback
    private val vunglePlayAdCallback: PlayAdCallback = object : PlayAdCallback {
        override fun onAdStart(placementReferenceId: String) {
            // Placement reference ID for the placement to be played
            makeToast("onAdStart=$placementReferenceId")
        }

        override fun onAdEnd(
                placementReferenceId: String,
                completed: Boolean,
                isCTAClicked: Boolean
        ) {
            // Placement reference ID for the placement that has completed ad experience
            // completed has value of true or false to notify whether video was
            // watched for 80% or more
            // isCTAClkcked has value of true or false to indicate whether download button
            // of an ad has been clicked by the user
            makeToast("onAdEnd=$placementReferenceId")
            //            releaseNatveAd();
        }

        override fun onAdEnd(id: String) {
            makeToast("onAdEnd=$id")
        }

        override fun onAdClick(id: String) {
            makeToast("onAdClick=$id")
        }

        override fun onAdRewarded(id: String) {
            makeToast("onAdRewarded=$id")
        }

        override fun onAdLeftApplication(id: String) {
            makeToast("        public void onAdLeftApplication(String id) {\n=$id")
        }

        override fun onError(placementReferenceId: String, exception: VungleException) {
            // Placement reference ID for the placement that failed to play an ad
            // Throwable contains error message
            makeToast("onError=" + placementReferenceId + "==" + exception.localizedMessage)
        }

        override fun onAdViewed(id: String) {
            makeToast("onAdViewed=$id")
        }

        override fun creativeId(creativeId: String?) {

            makeToast("creativeId=$creativeId")

        }
    }
}