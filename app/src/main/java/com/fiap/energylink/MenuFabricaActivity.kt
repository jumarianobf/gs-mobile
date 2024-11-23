package com.fiap.energylink

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView

class MenuFabricaActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_fabrica_menu)

        // Definindo o DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout)

        // Configuração da Toolbar (caso queira ter um ícone de menu)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Ação de clicar no ícone de menu (abrir o Drawer)
        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Ação dos botões na tela principal
        val botaoContrato = findViewById<ImageButton>(R.id.contrato)
        botaoContrato.setOnClickListener {
            val intent = Intent(this, ContratoActivity::class.java)
            startActivity(intent)
        }

        val botaoPerfil = findViewById<ImageButton>(R.id.perfil_fabrica)
        botaoPerfil.setOnClickListener {
            val intent = Intent(this, PerfilFabricaActivity::class.java)
            startActivity(intent)
        }

        // Configuração do RecyclerView para o menu lateral
        val navRecyclerView: RecyclerView = findViewById(R.id.nav_view)
    }

    // Método para lidar com o clique nos itens do menu lateral
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Método para fechar o Drawer ao pressionar o botão de voltar
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
