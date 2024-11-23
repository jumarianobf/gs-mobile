package com.fiap.energylink

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PerfilFabricaActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var nomeEditText: EditText
    private lateinit var cepEditText: EditText
    private lateinit var paisEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_fabrica)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        nomeEditText = findViewById(R.id.nome)
        cepEditText = findViewById(R.id.cep)
        paisEditText = findViewById(R.id.pais)

        loadFactoryData()

        val logoutButton: Button = findViewById(R.id.logout_fabrica)
        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginFabricaActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        val atualizarButton: Button = findViewById(R.id.atualizar_fabrica)
        atualizarButton.setOnClickListener {
            updateFactoryData()
        }

        val excluirPerfilButton: Button = findViewById(R.id.excluir_fabrica_button)
        excluirPerfilButton.setOnClickListener {
            deleteFactoryProfile()
        }
    }
    private fun loadFactoryData() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            // Verifique o caminho correto para o seu documento
           db.collection("fabricas").document(userId)
               .get()
                .addOnSuccessListener { document ->
                    if (document != null ) {
                        val nome = document.getString("nome") ?: ""
                        val cep = document.getString("cep") ?: ""
                        val pais = document.getString("pais") ?: ""

                        nomeEditText.setText(nome)
                        cepEditText.setText(cep)
                        paisEditText.setText(pais)

                    } else {
                        Toast.makeText(this, "Fábrica não encontrada ou dados não encontrados.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Erro ao carregar dados: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Usuário não autenticado.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateFactoryData() {
        val nome = nomeEditText.text.toString()
        val cep = cepEditText.text.toString()
        val pais = paisEditText.text.toString()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val factoryMap = hashMapOf(
                "nome" to nome,
                "cep" to cep,
                "pais" to pais
            )

            db.collection("fabricas").document(userId)
                .set(factoryMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Erro ao atualizar dados: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun deleteFactoryProfile() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            db.collection("fabricas").document(userId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Perfil da fábrica excluído com sucesso!", Toast.LENGTH_SHORT).show()

                    // Redireciona para a tela de login após excluir o perfil
                    auth.signOut()
                    val intent = Intent(this, LoginFabricaActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Erro ao excluir perfil: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
