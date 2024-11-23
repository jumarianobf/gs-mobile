package com.fiap.energylink

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CadastroUsuarioActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cadastro_usuario_activity)

        // Inicializar o Firebase Auth e Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val acesse = findViewById<Button>(R.id.cadastro)
        acesse.setOnClickListener {
            val intent = Intent(this, LoginUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }

        val inputNome: EditText = findViewById(R.id.nome_input)
        val inputSobrenome: EditText = findViewById(R.id.sobrenome_input)
        val inputEmail: EditText = findViewById(R.id.email_input)
        val inputCpf: EditText = findViewById(R.id.cpf_input)
        val inputTelefone: EditText = findViewById(R.id.telefone_input)
        val inputTipoUsuario: EditText = findViewById(R.id.pais_input)
        val inputSenha: EditText = findViewById(R.id.senha_input)
        val buttonConcluir: Button = findViewById(R.id.login_button)

        buttonConcluir.setOnClickListener {
            val nome = inputNome.text.toString()
            val sobrenome = inputSobrenome.text.toString()
            val email = inputEmail.text.toString()
            val cpf = inputCpf.text.toString()
            val telefone = inputTelefone.text.toString()
            val tipoUsuario = inputTipoUsuario.text.toString()
            val senha = inputSenha.text.toString()

            if (nome.isEmpty() || sobrenome.isEmpty() || email.isEmpty() || cpf.isEmpty() || telefone.isEmpty() || tipoUsuario.isEmpty() || senha.isEmpty()) {
                showMessage("Preencha todos os campos.")
            } else {
                // Validação de e-mail
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    showMessage("E-mail inválido.")
                    return@setOnClickListener
                }

                // Validação de CPF
                if (!isValidCpf(cpf)) {
                    showMessage("CPF inválido.")
                    return@setOnClickListener
                }

                // Cadastro no Firebase Authentication
                auth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            // Agora você pode adicionar dados no Firestore ou em outro lugar
                            saveUserDataToFirestore(user?.uid, nome, sobrenome, email, cpf, telefone, tipoUsuario, senha)
                        } else {
                            showMessage("Falha no cadastro: ${task.exception?.message}")
                        }
                    }
            }
        }
    }

    private fun saveUserDataToFirestore(userId: String?, nome: String, sobrenome: String, email: String, cpf: String, telefone: String, tipoUsuario: String, senha: String) {
        // Salvar dados do usuário no Firestore
        val user = hashMapOf(
            "nome" to nome,
            "sobrenome" to sobrenome,
            "email" to email,
            "cpf" to cpf,
            "telefone" to telefone,
            "tipoUsuario" to tipoUsuario,
            "senha" to senha
        )

        userId?.let {
            db.collection("usuarios").document(it).set(user)
                .addOnSuccessListener {
                    // Sucesso ao salvar dados
                    val intent = Intent(this@CadastroUsuarioActivity, LoginUsuarioActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    showMessage("Erro ao salvar dados no Firestore: ${e.message}")
                }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isValidCpf(cpf: String): Boolean {
        // Função simples para validar o CPF
        return cpf.length == 11 && cpf.all { it.isDigit() }
    }
}
