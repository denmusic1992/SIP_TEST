package ru.meteor.sip_test

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.xuchongyang.easyphone.EasyLinphone
import com.xuchongyang.easyphone.callback.PhoneCallback
import com.xuchongyang.easyphone.callback.RegistrationCallback

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        callPermissions()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_BEFORE_REGISTER) {
            var isPermissionGranted = true
            if (grantResults.isNotEmpty()) {
                for (result in grantResults) {
                    isPermissionGranted =
                        isPermissionGranted && (result == PackageManager.PERMISSION_GRANTED)
                }
            } else {
                isPermissionGranted = false
            }
            if (isPermissionGranted)
                makeUsingEasyLinPhone()
        }
    }

    private fun makeUsingEasyLinPhone() {
        // Стартуем Сервис
        EasyLinphone.startService(this)
        // Добавляем Callback
        EasyLinphone.addCallback(object : RegistrationCallback() {
            override fun registrationProgress() {
                super.registrationProgress()
                Log.i("Register", "Registration in Progress!")
            }

            override fun registrationOk() {
                super.registrationOk()
                Log.i("Register", "Registered!")
            }

            override fun registrationFailed() {
                super.registrationFailed()
                Log.i("Register", "Registration failed")
            }

        }, object : PhoneCallback() {
            override fun incomingCall(linphoneCall: org.linphone.core.LinphoneCall?) {
                super.incomingCall(linphoneCall)
            }

            override fun callConnected() {
                super.callConnected()
            }

            override fun callEnd() {
                super.callEnd()
            }
        })

        // Логинемся
        EasyLinphone.setAccount(USER, PASS, DOMAIN)
        EasyLinphone.login()

    }

    /**
     * Проверка разрешений
     */
    private fun callPermissions() {

        val permissions = ArrayList<String>()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.USE_SIP
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.USE_SIP)
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.RECORD_AUDIO)
        }

        if (permissions.size > 0) {
            ActivityCompat.requestPermissions(
                this, permissions.toTypedArray(), PERMISSIONS_BEFORE_REGISTER
            )
        } else {
            makeUsingEasyLinPhone()
        }
    }

    companion object {
        private const val PERMISSIONS_BEFORE_REGISTER: Int = 1
        //private const val PACKAGE_INTENT = "ru.meteor.test_sip_krepost.INCOMING_CALL"
//        private const val DOMAIN = "sip.linphone.org"
//        private const val USER = "denmusic1992"
//        private const val PASS = "black1992"

        private const val DOMAIN = "ec0009.directphone.net"
        private const val USER = "10070101450"
        private const val PASS = "Y43id5NnaXvT"
//        private const val DOMAIN = "sip2sip.info"
//        private const val USER = "2233576149"
//        private const val PASS = "black1992"

        //private const val CALL_TIMEOUT = 30
    }
}
