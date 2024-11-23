package com.fiap.energylink

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class SelecaoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selecao_main)

        // Configurando os botões
        val comunidadeButton = findViewById<AppCompatButton>(R.id.comunidade)
        comunidadeButton.setOnClickListener {
            val comunidade = Intent(this, LoginComunidadeActivity::class.java)
            startActivity(comunidade)
            finish()
        }
        val fabricaButton = findViewById<AppCompatButton>(R.id.registre_fabrica)
        fabricaButton.setOnClickListener {
            val fabrica = Intent(this, LoginFabricaActivity::class.java)
            startActivity(fabrica)
            finish()
        }

        val usuarioButton = findViewById<AppCompatButton>(R.id.registre_usuario)
        usuarioButton.setOnClickListener {
            val usuario = Intent(this, LoginUsuarioActivity::class.java)
            startActivity(usuario)
            finish()
        }


    }

    private fun salvarTipoUsuario(tipo: String) {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("tipoUsuario", tipo)  // Salva o tipo de perfil escolhido
        editor.apply()
    }
}
