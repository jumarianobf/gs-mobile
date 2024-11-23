package com.fiap.energylink

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class PerfilUsuarioActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var nomeEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var telefoneEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        nomeEditText = findViewById(R.id.textView9)
        emailEditText = findViewById(R.id.textView10)
        telefoneEditText = findViewById(R.id.textVie1)

        loadUserData()

        val logoutButton: Button = findViewById(R.id.logout_button)
        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginUsuarioActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        val atualizarButton: Button = findViewById(R.id.atualizar)
        atualizarButton.setOnClickListener {
            updateUserData()
        }

        // Botão para excluir o perfil inteiro
        val excluirPerfilButton: Button = findViewById(R.id.excluir_perfil_button)
        excluirPerfilButton.setOnClickListener {
            deleteProfile()
        }
    }

    private fun loadUserData() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            db.collection("usuarios").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val nome = document.getString("nome") ?: ""
                        val email = document.getString("email") ?: ""
                        val telefone = document.getString("telefone") ?: ""

                        nomeEditText.setText(nome)
                        emailEditText.setText(email)
                        telefoneEditText.setText(telefone)
                    } else {
                        Toast.makeText(this, "Usuário não encontrado.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Erro ao carregar dados: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Usuário não está logado.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUserData() {
        val nome = nomeEditText.text.toString()
        val email = emailEditText.text.toString()
        val telefone = telefoneEditText.text.toString()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val userMap = hashMapOf(
                "nome" to nome,
                "email" to email,
                "telefone" to telefone
            )

            db.collection("usuarios").document(userId)
                .set(userMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Erro ao atualizar dados: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }


    private fun deleteProfile() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val userRef = db.collection("usuarios").document(userId)

            // Exclui o perfil completo
            userRef.delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Perfil excluído com sucesso!", Toast.LENGTH_SHORT).show()

                    // Log out do usuário após excluir o perfil
                    auth.signOut()
                    val intent = Intent(this, LoginUsuarioActivity::class.java)
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
