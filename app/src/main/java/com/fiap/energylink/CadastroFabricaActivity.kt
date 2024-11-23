package com.fiap.energylink

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CadastroFabricaActivity: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cadastro_fabrica_activity)

        // Inicializar o Firebase Auth e Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val acesse = findViewById<Button>(R.id.cadastro)
        acesse.setOnClickListener {
            val intent = Intent(this, LoginFabricaActivity::class.java)
            startActivity(intent)
            finish()
        }

        val inputNomeEntidade: EditText = findViewById(R.id.nome_input)
        val inputDemandaEnergia: EditText = findViewById(R.id.demanda_input)
        val inputCep: EditText = findViewById(R.id.cep_input)
        val inputCidade: EditText = findViewById(R.id.cidade_input)
        val inputPais: EditText = findViewById(R.id.pais_input)
        val inputEstado: EditText = findViewById(R.id.estado_input)
        val buttonConcluir: Button = findViewById(R.id.login_button)

        buttonConcluir.setOnClickListener {
            val nome = inputNomeEntidade.text.toString()
            val demanda = inputDemandaEnergia.text.toString()
            val cep = inputCep.text.toString()
            val cidade = inputCidade.text.toString()
            val pais = inputPais.text.toString()
            val estado = inputEstado.text.toString()

            if (nome.isEmpty() || demanda.isEmpty() || cep.isEmpty() || cidade.isEmpty() || pais.isEmpty() || estado.isEmpty()) {
                showMessage("Preencha todos os campos.")
            } else {
                // Validação de CEP
                if (!isValidCep(cep)) {
                    showMessage("CEP inválido.")
                    return@setOnClickListener
                }

                // Criar a fábrica sem usar email e senha, agora com nome e cep
                val nomeFabrica = nome
                val cepFabrica = cep

                // Salvar os dados da fábrica no Firestore
                saveFabricaDataToFirestore(nomeFabrica, demanda, cepFabrica, cidade, pais, estado)
            }
        }
    }

    private fun saveFabricaDataToFirestore(nome: String, demanda: String, cep: String, cidade: String, pais: String, estado: String) {
        // Pegar o UID do usuário autenticado
        val userId = auth.currentUser?.uid

        if (userId != null) {
            // Criar o mapa de dados da fábrica
            val fabrica = hashMapOf(
                "nome" to nome,
                "demanda" to demanda,
                "cep" to cep,
                "cidade" to cidade,
                "pais" to pais,
                "estado" to estado
            )

            // Salvar os dados da fábrica no Firestore com o UID do usuário como ID
            db.collection("fabricas").document(userId)
                .set(fabrica)
                .addOnSuccessListener {
                    // Sucesso ao salvar dados
                    val intent = Intent(this@CadastroFabricaActivity, LoginFabricaActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    showMessage("Erro ao salvar dados no Firestore: ${e.message}")
                }
        } else {
            showMessage("Usuário não autenticado.")
        }
    }


    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isValidCep(cep: String): Boolean {
        // Função simples para validar o CEP
        return cep.length == 8 && cep.all { it.isDigit() }
    }
}
