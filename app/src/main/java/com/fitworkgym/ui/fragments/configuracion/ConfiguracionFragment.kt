package com.fitworkgym.ui.fragments.configuracion

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fitworkgym.ui.viewmodel.MainActivity
import com.fitworkgym.data.repository.AuthDataSource
import com.fitworkgym.data.repository.AuthRepository
import com.fitworkgym.databinding.FragmentConfiguracionBinding
import com.fitworkgym.ui.viewmodel.ConfiguracionViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ConfiguracionFragment : Fragment() {

    private  lateinit var  notificationsViewModel: ConfiguracionViewModel

    private var _binding: FragmentConfiguracionBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val firebaseAuth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        val authDataSource = AuthDataSource(firebaseAuth, firestore)
        val authRepository = AuthRepository(authDataSource)

        notificationsViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ConfiguracionViewModel(authRepository) as T
            }
        })[ConfiguracionViewModel::class.java]

        _binding = FragmentConfiguracionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notificationsViewModel.name.observe(viewLifecycleOwner) { name ->
            binding.textViewName.text = name
        }

        binding.logoutButton.setOnClickListener{
            notificationsViewModel.logout()
           val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}