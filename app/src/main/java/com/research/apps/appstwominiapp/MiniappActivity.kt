package com.research.apps.appstwominiapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.ktx.initialize
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.research.apps.appstwominiapp.databinding.ActivityMiniappBinding
import timber.log.Timber

class MiniappActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMiniappBinding

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Timber.w("Apps notification permission granted")
        } else {
            Timber.w("Apps notification permission not granted")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMiniappBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initTimber()
        initFirebaseMiniapp()
        askNotificationPermission()
        getCurrentFcmToken()
        initFirebaseDatabase()
        initFirebaseRemoteConfig()

        initView()
        initEvent()
    }

    override fun onDestroy() {
        super.onDestroy()

        FirebaseApp.getInstance(FB_PROJECT_ID).delete()
    }

    private fun initView() {

        val message = "Hello! This is miniapp!"
        val capsMessage = message.capitalizeEachWord()
        binding.tvHello.text = capsMessage
    }

    private fun initEvent() {
        val analytics = FirebaseAnalytics.getInstance(this@MiniappActivity)

        binding.apply {
            btnCrash.setOnClickListener {
                throw RuntimeException("Test Crash Miniapp")
            }

            btnAnalytics.setOnClickListener {
                val bundle = Bundle()

                bundle.putString("button_miniapp", "testing button analytics miniapp")
                analytics.logEvent("button_analytics_miniapp", bundle)
            }

            btnSendReport.setOnClickListener {
                FirebaseCrashlytics.getInstance().checkForUnsentReports()
            }
        }
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }
    private fun initFirebaseMiniapp() {
        val options = FirebaseOptions.Builder()
            .setProjectId(FB_PROJECT_ID)
            .setStorageBucket("apps-two-miniapp.appspot.com")
            .setApplicationId("1:163783144337:android:bbbe5465977e60e1832030")
            .setApiKey("AIzaSyA9bEDp-SGTqC7EYy0ZKlnKnorlypR35H0")
            .setDatabaseUrl("https://apps-two-miniapp-default-rtdb.asia-southeast1.firebasedatabase.app")
            .build()


        // Firebase Kotlin Docs Ways
        Firebase.initialize(this@MiniappActivity, options, FB_PROJECT_ID)
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // should display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun getCurrentFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.w("Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            val msg = "Current device token (miniapp) => $token"
            Timber.d(msg)
        })
    }

    private fun initFirebaseDatabase() {
        val app = Firebase.app(FB_PROJECT_ID)
        val database = Firebase.database(app)
        val mRootRef = database.getReference("miniapp_db")
        val admin = mRootRef.child("admin")

        admin.get()
            .addOnSuccessListener {
                val appData = it.child("app")
                val name = it.child("name")
                Timber.w("success miniapp data -> ${it.value}")
                Timber.w("success miniapp data -> ${appData.value}")
                Timber.w("success miniapp data -> ${name.value}")
            }.addOnCanceledListener {
                Timber.w("data cancelled miniapp")

            }.addOnFailureListener {
                Timber.w("failure miniapp-> ${it.message}")
            }
    }

    private fun initFirebaseRemoteConfig() {
        val app = Firebase.app(FB_PROJECT_ID)
        val remoteConfig = Firebase.remoteConfig(app)

        remoteConfig.fetchAndActivate()

        val remoteString = remoteConfig.getString("miniapp_param_string")
        Timber.w("remote string miniapp => $remoteString")

        val remoteBool = remoteConfig.getBoolean("miniapp_param_bool")
        Timber.w("remote bool miniapp => $remoteBool")
    }
}
