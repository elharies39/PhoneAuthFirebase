package com.elharies.autheticationphonefirebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_verifikasi_kode.*

class VerifikasiKodeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var verifikasiID: String
    private lateinit var token: String
    private var timer: Int = 0
    private lateinit var auth: FirebaseAuth
    private lateinit var stateListener: FirebaseAuth.AuthStateListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verifikasi_kode)

        auth = FirebaseAuth.getInstance()
        stateListener = FirebaseAuth.AuthStateListener {
            val currentUser = it.currentUser
            if (currentUser != null){
                startActivity(Intent(this@VerifikasiKodeActivity, HomeActivity::class.java))
                finish()
            }
        }

        verifikasiID = intent.getStringExtra("VERIFIKASI_ID")
        //token = intent.getStringExtra("RESEND_TOKEN")
        timer = intent.getIntExtra("TIMER",0)

        btn_login.setOnClickListener(this)
        tv_resend.setOnClickListener(this)

    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(stateListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(stateListener)
    }

    private fun signWithPhoneAuthCredential(credential: PhoneAuthCredential){
        auth.signInWithCredential(credential).addOnCompleteListener(this){
            if (it.isSuccessful){
                Log.d("VERIFIKASI ACTIVITY","SUkses")
                Toast.makeText(this, "Berhasil Login", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }else{
                if (it.exception is FirebaseAuthInvalidCredentialsException){
                    Log.e("VERIFIKASI ACTIVITY",""+it.exception)
                }
            }
        }
    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.btn_login -> {
                    val verifiCode = et_kode_verifikasi.text.toString()
                    if (TextUtils.isEmpty(verifikasiID)){
                        Toast.makeText(this, "Masukkan Kode verifikasi", Toast.LENGTH_LONG).show()
                    }else{
                        val phoneAuthCredential = PhoneAuthProvider.getCredential(verifikasiID, verifiCode)
                        signWithPhoneAuthCredential(phoneAuthCredential)
                    }
                }
            }
        }
    }

}
