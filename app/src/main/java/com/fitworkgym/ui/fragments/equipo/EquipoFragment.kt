package com.fitworkgym.ui.fragments.equipo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitworkgym.data.repository.EquipoDataSource
import com.fitworkgym.data.model.Equipo
import com.fitworkgym.data.repository.EquipoRepository
import com.fitworkgym.databinding.FragmentEquipoBinding
import com.fitworkgym.ui.adapter.EquipoAdapter
import com.fitworkgym.ui.viewmodel.EquipoAdminViewModel
import com.google.firebase.firestore.FirebaseFirestore

class EquipoFragment : Fragment() {

    private var _binding: FragmentEquipoBinding? = null

    private val binding get() = _binding!!

    private lateinit var dashboardViewModel: EquipoAdminViewModel

    private lateinit var equipoAdapter: EquipoAdapter

    private var db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val firestore = FirebaseFirestore.getInstance()
        val equipoDataSource = EquipoDataSource(firestore)
        val equipoRepository = EquipoRepository(equipoDataSource)
        dashboardViewModel =
            ViewModelProvider(this, object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return EquipoAdminViewModel(equipoRepository) as T
                }
            })[EquipoAdminViewModel::class.java]

        _binding = FragmentEquipoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        recyclerView()
        showTrainers()
        return root
    }

    private fun recyclerView() {
        equipoAdapter = EquipoAdapter()
        binding.contentRecyclerEquipo.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = equipoAdapter
        }
    }

    private fun showTrainers() {
        db.collection("equipos").addSnapshotListener { snapshot, e ->
            if (snapshot != null) {
                val dataList = mutableListOf<Equipo>()
                for (documents in snapshot.documents) {
                    val data = documents.toObject(Equipo::class.java)
                    dataList.add(data!!)
                }
                equipoAdapter.differ.submitList(dataList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}