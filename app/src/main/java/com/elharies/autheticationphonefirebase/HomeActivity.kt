package com.elharies.autheticationphonefirebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btn_logout.setOnClickListener(this)
    }

    private fun logout(){
        FirebaseAuth.getInstance().signOut()
        if (FirebaseAuth.getInstance().currentUser == null){
            finish()
            startActivity(Intent(this@HomeActivity, MainActivity::class.java))
        }
    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.btn_logout -> {
                    Toast.makeText(this, "Berhasil Keluar", Toast.LENGTH_LONG).show()
                    logout()
                }
            }
        }
    }
}
