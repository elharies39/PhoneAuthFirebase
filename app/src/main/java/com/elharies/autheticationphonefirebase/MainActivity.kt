package com.elharies.autheticationphonefirebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var stateListener: FirebaseAuth.AuthStateListener
    private lateinit var callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var noTelp: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_submit.setOnClickListener(this)

        connectToFirebaseAuth()
    }

    private fun connectToFirebaseAuth(){
        auth = FirebaseAuth.getInstance()
        stateListener = FirebaseAuth.AuthStateListener {
            val currentUser = it.currentUser
            if (currentUser != null){
                startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(stateListener)
    }

    override fun onStop() {
        super.onStop()
        if (stateListener != null){
            auth.removeAuthStateListener(stateListener)
        }
    }

    private fun setupVerificationCallback(){
        callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationFailed(p0: FirebaseException?) {

            }

            override fun onVerificationCompleted(p0: PhoneAuthCredential?) {

            }

            override fun onCodeSent(p0: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
                super.onCodeSent(p0, p1)
                val intent = Intent(this@MainActivity, VerifikasiKodeActivity::class.java)
                intent.putExtra("VERIFIKASI_ID", p0)
                intent.putExtra("RESEND_TOKEN", p1)
                intent.putExtra("TIMER", 60)
                startActivity(intent)
            }
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.btn_submit ->{
                    noTelp = et_nomor.text.toString()
                    if (!TextUtils.isEmpty(noTelp)){
                        setupVerificationCallback()
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+62"+noTelp,
                            60,
                            TimeUnit.SECONDS,
                            this,
                            callback
                        )
                        Toast.makeText(this, "Telah Mengirim Kode Verifikasi", Toast.LENGTH_SHORT).show()
                        noTelp = ""
                        et_nomor.text = null
                    }

                }
            }
        }
    }
}
