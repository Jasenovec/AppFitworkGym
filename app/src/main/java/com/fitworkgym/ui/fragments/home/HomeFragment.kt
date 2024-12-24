package com.fitworkgym.ui.fragments.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitworkgym.data.model.Trainer
import com.fitworkgym.databinding.FragmentHomeBinding
import com.fitworkgym.ui.adapter.Adapter
import com.fitworkgym.ui.viewmodel.HomeViewModel
import com.fitworkgym.ui.viewmodel.TipoMembresia
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var recyclerviewActivities: RecyclerView

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var trainerAdapter: Adapter

    private var db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        recyclerView()
        showTrainers()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        homeViewModel.estadoApertura.observe(viewLifecycleOwner) { estadoApertura ->
            binding.includedCard.scheduleSchedule.text = estadoApertura.texto
            if (!estadoApertura.estaAbierto) {
                binding.includedCard.scheduleSchedule.setTextColor(Color.RED)
            } else {
                binding.includedCard.scheduleSchedule.setTextColor(Color.GREEN)
            }

        }


        homeViewModel.estadoMembresia.observe(viewLifecycleOwner) { estadoMembresia ->
            binding.includedSchedule.trainerTime.text = estadoMembresia
        }
        homeViewModel.verificarMembresia(LocalDate.now().minusDays(15), TipoMembresia.NOVENTA_DIAS)

        homeViewModel.verificarHorario()

        recyclerviewActivities = binding.contentRecyclerActivities

        recyclerviewActivities.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Observa los cambios en activities
       /* homeViewModel.activities.observe(viewLifecycleOwner) { activities ->
            recyclerviewActivities.adapter = ActivitiesAdapter(activities)
            recyclerviewActivities.setHasFixedSize(true)
        }*/


    }


    private fun recyclerView() {
        trainerAdapter = Adapter()
        binding.contentRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = trainerAdapter
        }
    }

    private fun showTrainers() {
        db.collection("trainers").addSnapshotListener { snapshot, e ->
            if (snapshot != null) {
                val dataList = mutableListOf<Trainer>()
                for (documents in snapshot.documents) {
                    val data = documents.toObject(Trainer::class.java)
                    dataList.add(data!!)
                }
                trainerAdapter.differ.submitList(dataList)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}