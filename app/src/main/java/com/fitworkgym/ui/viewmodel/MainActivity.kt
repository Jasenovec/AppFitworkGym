package com.fitworkgym.ui.viewmodel

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.fitworkgym.R
import com.fitworkgym.data.repository.AuthDataSource
import com.fitworkgym.data.model.User
import com.fitworkgym.data.repository.AuthRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var loginViewModel: AuthViewModel

    private lateinit var btoLogin: Button
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var btoCRegister: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets


        }
        val firebaseAuth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        val authDataSource = AuthDataSource(firebaseAuth, firestore)
        val loginRepository = AuthRepository(authDataSource)

        loginViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(loginRepository) as T
            }
        })[AuthViewModel::class.java]

        btoLogin = findViewById(R.id.btoLogin)
        email = findViewById(R.id.username)
        password = findViewById(R.id.password)
        btoCRegister = findViewById(R.id.btoCRegister)

        val inflater = LayoutInflater.from(this)
        val registerLayout =
            inflater.inflate(R.layout.activity_register, this.findViewById(R.id.main), false)

        val name = registerLayout.findViewById<EditText>(R.id.nameRegister)
        val usernameRegister = registerLayout.findViewById<EditText>(R.id.usernameRegister)
        val phoneNumber = registerLayout.findViewById<EditText>(R.id.phoneRegister)
        val passwordRegister = registerLayout.findViewById<EditText>(R.id.passwordRegister)
        val btoRegister = registerLayout.findViewById<Button>(R.id.btoRegister)

        btoCRegister.setOnClickListener {
            setContentView(registerLayout)
        }

        btoRegister.setOnClickListener {
            val nameUser = name.text.toString()
            val email = usernameRegister.text.toString()
            val password = passwordRegister.text.toString()
            val phone = phoneNumber.text.toString().toInt()
            val createdAt = Timestamp.now()

            // Validar campos
            if (nameUser.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            loginViewModel.register(
                user = User(
                    name = nameUser,
                    email = email,
                    password = password,
                    phoneNumber = phone,
                    createdAt = createdAt
                )
            )
        }


        btoLogin.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()


            loginViewModel.login(email, password)

        }

        lifecycleScope.launch {
            loginViewModel.authState.observe(this@MainActivity) { authState ->
                when (authState) {
                    is AuthState.Loading -> {
                        // Show loading
                    }

                    is AuthState.Success -> {
                        val currentEmail = FirebaseAuth.getInstance().currentUser?.email

                        val isAdmin = currentEmail == "admin@admin.com"

                        if (isAdmin) {
                            val intent = Intent(this@MainActivity, AdminActivity::class.java)
                            startActivity(intent)
                        } else {
                            val intent = Intent(this@MainActivity, NavigationActivity::class.java)
                            Toast.makeText(this@MainActivity, authState.message, Toast.LENGTH_SHORT)
                                .show()
                            startActivity(intent)
                        }
                    }

                    is AuthState.Error -> {
                        Toast.makeText(this@MainActivity, authState.error, Toast.LENGTH_SHORT)
                            .show()
                    }

                    AuthState.Idle -> {

                    }
                }
            }
        }
    }
}