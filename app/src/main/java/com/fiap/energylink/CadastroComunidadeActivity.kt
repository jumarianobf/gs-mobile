package com.fiap.energylink

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CadastroComunidadeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cadastro_comunidade_activity)

        // Inicializar o Firebase Auth e Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val acesse = findViewById<Button>(R.id.cadastro)
        acesse.setOnClickListener {
            val intent = Intent(this, LoginUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }

        val inputNomeComunidade: EditText = findViewById(R.id.nome_entidade_input)
        val inputCidade: EditText = findViewById(R.id.cidade_input)
        val inputEstado: EditText = findViewById(R.id.estado_input)
        val inputPais: EditText = findViewById(R.id.pais_input)
        val inputCep: EditText = findViewById(R.id.cep_input)
        val inputCapacidade: EditText = findViewById(R.id.capacidade_input)
        val inputTipoEnergia: EditText = findViewById(R.id.tipo_input)
        val buttonConcluir: Button = findViewById(R.id.login_button)

        buttonConcluir.setOnClickListener {
            val nomeComunidade = inputNomeComunidade.text.toString()
            val cidade = inputCidade.text.toString()
            val estado = inputEstado.text.toString()
            val pais = inputPais.text.toString()
            val cep = inputCep.text.toString()
            val capacidade = inputCapacidade.text.toString()
            val tipoEnergia = inputTipoEnergia.text.toString()

            if (nomeComunidade.isEmpty() || cidade.isEmpty() || estado.isEmpty() || pais.isEmpty() || cep.isEmpty() || capacidade.isEmpty() || tipoEnergia.isEmpty()) {
                showMessage("Preencha todos os campos.")
            } else {
                // Validação de dados
                if (!isValidCep(cep)) {
                    showMessage("CEP inválido.")
                    return@setOnClickListener
                }

                // Cadastro no Firestore
                val user = auth.currentUser
                val userId = user?.uid

                if (userId != null) {
                    saveCommunityDataToFirestore(userId, nomeComunidade, cidade, estado, pais, cep, capacidade, tipoEnergia)
                } else {
                    showMessage("Usuário não autenticado.")
                }
            }
        }
    }

    private fun saveCommunityDataToFirestore(userId: String, nomeComunidade: String, cidade: String, estado: String, pais: String, cep: String, capacidade: String, tipoEnergia: String) {
        // Salvar dados da comunidade no Firestore
        val comunidade = hashMapOf(
            "nomeComunidade" to nomeComunidade,
            "cidade" to cidade,
            "estado" to estado,
            "pais" to pais,
            "cep" to cep,
            "capacidade" to capacidade,
            "tipoEnergia" to tipoEnergia
        )

        db.collection("comunidades").document(userId).set(comunidade)
            .addOnSuccessListener {
                // Sucesso ao salvar dados
                val intent = Intent(this@CadastroComunidadeActivity, LoginComunidadeActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                showMessage("Erro ao salvar dados no Firestore: ${e.message}")
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
