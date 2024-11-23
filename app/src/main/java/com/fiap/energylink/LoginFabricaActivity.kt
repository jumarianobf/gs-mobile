package com.fiap.energylink

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.firestore.FirebaseFirestore

class LoginFabricaActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_fabrica_activity)

        db = FirebaseFirestore.getInstance()

        val buttonback = findViewById<AppCompatButton>(R.id.back_button)
        buttonback.setOnClickListener {
            val intent = Intent(this, SelecaoActivity::class.java)
            startActivity(intent)
            finish()
        }

        val fabrica = findViewById<Button>(R.id.cadastroFabrica)
        fabrica.setOnClickListener {
            val intent = Intent(this, CadastroFabricaActivity::class.java)
            startActivity(intent)
            finish()
        }

        val loginButton = findViewById<Button>(R.id.login_button)
        val nomeEntidadeInput: EditText = findViewById(R.id.nome_input)
        val cepInput: EditText = findViewById(R.id.cep_input)

        loginButton.setOnClickListener {
            val nomeEntidade = nomeEntidadeInput.text.toString()
            val cep = cepInput.text.toString()

            if (nomeEntidade.isEmpty() || cep.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()
            } else {
                // Procurar a fábrica pelo nome e CEP no Firestore
                db.collection("fabricas")
                    .whereEqualTo("nome", nomeEntidade)
                    .whereEqualTo("cep", cep)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            // Fábrica encontrada, login bem-sucedido
                            val intent = Intent(this, MenuFabricaActivity::class.java) // Alterado para MenuFabricaActivity
                            startActivity(intent)
                            finish()
                        } else {
                            // Fábrica não encontrada
                            Toast.makeText(this, "Fábrica não encontrada ou dados incorretos.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Erro ao realizar o login: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
