package com.fiap.energylink

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.firestore.FirebaseFirestore

class LoginComunidadeActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_comunidade_activity)

        db = FirebaseFirestore.getInstance()

        val buttonback = findViewById<AppCompatButton>(R.id.back_button)
        buttonback.setOnClickListener {
            val intent = Intent(this, SelecaoActivity::class.java)
            startActivity(intent)
            finish()
        }

        val cadastro = findViewById<Button>(R.id.cadastroComunidade)
        cadastro.setOnClickListener {
            val intent = Intent(this, CadastroComunidadeActivity::class.java)
            startActivity(intent)
            finish()
        }

        val loginButton = findViewById<AppCompatButton>(R.id.login_button)
        val nomeInput: EditText = findViewById(R.id.entidade_input)
        val tipoEnergiaInput: EditText = findViewById(R.id.energia_input)

        loginButton.setOnClickListener {
            val nome = nomeInput.text.toString()
            val tipoEnergia = tipoEnergiaInput.text.toString()

            if (nome.isEmpty() || tipoEnergia.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()
            } else {
                // Verificar se a comunidade existe no Firestore
                db.collection("comunidades")
                    .whereEqualTo("nomeComunidade", nome)
                    .whereEqualTo("tipoEnergia", tipoEnergia)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents.isEmpty) {
                            Toast.makeText(this, "Comunidade não encontrada.", Toast.LENGTH_SHORT).show()
                        } else {
                            // Login bem-sucedido, a comunidade foi encontrada
                            val intent = Intent(this, MenuComunidadeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Erro ao verificar a comunidade: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
