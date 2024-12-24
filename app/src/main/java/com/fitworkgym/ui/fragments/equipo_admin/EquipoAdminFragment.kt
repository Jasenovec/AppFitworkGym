package com.fitworkgym.ui.fragments.equipo_admin


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitworkgym.R
import com.fitworkgym.data.repository.EquipoDataSource
import com.fitworkgym.data.model.Equipo
import com.fitworkgym.data.repository.EquipoRepository
import com.fitworkgym.databinding.FragmentEquipoAdminBinding
import com.fitworkgym.ui.adapter.EquipoAdapter
import com.fitworkgym.ui.viewmodel.EquipoAdminViewModel
import com.google.firebase.firestore.FirebaseFirestore

class EquipoAdminFragment : Fragment() {

    private var _binding: FragmentEquipoAdminBinding? = null

    private val binding get() = _binding!!

    private  lateinit var equipoAdapter: EquipoAdapter

    private lateinit var  equipoViewModel: EquipoAdminViewModel

    private var db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val firestore = FirebaseFirestore.getInstance()
        val equipoDataSource = EquipoDataSource(firestore)
        val equipoRepository = EquipoRepository(equipoDataSource)
        equipoViewModel =
            ViewModelProvider(this, object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return EquipoAdminViewModel(equipoRepository) as T
                }
            })[EquipoAdminViewModel::class.java]
        _binding = FragmentEquipoAdminBinding.inflate(inflater, container, false)

        recyclerView()
        showEquipos()
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView()
        showEquipos()
        binding.floatingActionButtonEquipo.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_equipo_to_addEquipoFragment)
        }

    }



    private fun recyclerView() {
        equipoAdapter = EquipoAdapter()
        binding.contentRecyclerEquipo.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = equipoAdapter
        }
    }

    private fun showEquipos() {
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