package com.fiap.energylink

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ContratoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contrato)

        val menu = findViewById<ImageButton>(R.id.imageButton4)
        menu.setOnClickListener {
            val intent = Intent(this, MenuUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }

        val perfil = findViewById<ImageButton>(R.id.imageButton5)
        perfil.setOnClickListener {
            val intent = Intent(this, PerfilUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}