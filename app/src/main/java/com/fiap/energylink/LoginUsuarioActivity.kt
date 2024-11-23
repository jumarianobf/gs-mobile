package com.fiap.energylink

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth

class LoginUsuarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_usuario_activity)

        val buttonback = findViewById<AppCompatButton>(R.id.back_button)
        buttonback.setOnClickListener {
            val intent = Intent(this, SelecaoActivity::class.java)
            startActivity(intent)
            finish()
        }

        val cadastro = findViewById<Button>(R.id.cadastro)
        cadastro.setOnClickListener {
            val intent = Intent(this, CadastroUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }

        val login = findViewById<AppCompatButton>(R.id.login_button)
        login.setOnClickListener{
            val intent = Intent(this, MenuUsuarioActivity::class.java)
            startActivity(intent)
            finish()
        }

        val emailInput: EditText = findViewById(R.id.email_input)
        val senhaInput: EditText = findViewById(R.id.password_input)



        login.setOnClickListener {
            val email = emailInput.text.toString()
            val senha = senhaInput.text.toString()

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()
            } else {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Login bem-sucedido
                            val intent = Intent(this, MenuUsuarioActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // Falha no login
                            Toast.makeText(this, "Falha no login: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }


    }


}
