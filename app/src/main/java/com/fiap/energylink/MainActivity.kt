package com.fiap.energylink

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val loginButton = findViewById<AppCompatButton>(R.id.my_button)
        loginButton.setOnClickListener {
            val intent = Intent(this, SelecaoActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}