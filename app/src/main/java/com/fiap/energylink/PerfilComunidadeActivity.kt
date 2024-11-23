package com.fiap.energylink

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class PerfilComunidadeActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var nomeComunidadeEditText: EditText
    private lateinit var cepEditText: EditText
    private lateinit var tipoEnergiaEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_comunidade)

        db = FirebaseFirestore.getInstance()

        nomeComunidadeEditText = findViewById(R.id.textView9)
        cepEditText = findViewById(R.id.textView10)
        tipoEnergiaEditText = findViewById(R.id.textVie1)

        loadComunidadeData()

        val logoutButton: Button = findViewById(R.id.logout_comunidade)
        logoutButton.setOnClickListener {
            // Fazer logout e redirecionar
            val intent = Intent(this, LoginComunidadeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        val atualizarButton: Button = findViewById(R.id.atualizar_comunidade_button)
        atualizarButton.setOnClickListener {
            updateComunidadeData()
        }

        val excluirPerfilButton: Button = findViewById(R.id.excluir_comunidade_button)
        excluirPerfilButton.setOnClickListener {
            deleteComunidadeProfile()
        }
    }

    private fun loadComunidadeData() {
        // Supondo que o ID da comunidade foi passado como extra
        val comunidadeId = intent.getStringExtra("COMUNIDADE_ID")
        if (comunidadeId != null) {
            db.collection("comunidades").document(comunidadeId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val nomeComunidade = document.getString("nomeComunidade") ?: ""
                        val tipoEnergia = document.getString("tipoEnergia") ?: ""
                        val cep = document.getString("cep") ?: ""

                        nomeComunidadeEditText.setText(nomeComunidade)
                        tipoEnergiaEditText.setText(tipoEnergia)
                        cepEditText.setText(cep)
                    } else {
                        Toast.makeText(this, "Comunidade não encontrada.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Erro ao carregar dados: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "ID da comunidade não fornecido.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateComunidadeData() {
        val nomeComunidade = nomeComunidadeEditText.text.toString()
        val tipoEnergia = tipoEnergiaEditText.text.toString()
        val cep = cepEditText.text.toString()

        val comunidadeId = intent.getStringExtra("COMUNIDADE_ID")
        if (comunidadeId != null) {
            val comunidadeMap = hashMapOf(
                "nomeComunidade" to nomeComunidade,
                "tipoEnergia" to tipoEnergia,
                "cep" to cep
            )

            db.collection("comunidades").document(comunidadeId)
                .set(comunidadeMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Dados da comunidade atualizados com sucesso!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Erro ao atualizar dados: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun deleteComunidadeProfile() {
        val comunidadeId = intent.getStringExtra("COMUNIDADE_ID")
        if (comunidadeId != null) {
            db.collection("comunidades").document(comunidadeId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Perfil da comunidade excluído com sucesso!", Toast.LENGTH_SHORT).show()

                    // Redireciona para a tela de login após excluir o perfil
                    val intent = Intent(this, LoginComunidadeActivity::class.java)
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
