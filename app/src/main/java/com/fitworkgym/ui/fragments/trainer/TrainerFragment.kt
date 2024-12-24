package com.fitworkgym.ui.fragments.trainer

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
import com.fitworkgym.data.repository.TrainerDataSource
import com.fitworkgym.data.model.Trainer
import com.fitworkgym.data.repository.TrainerRepository
import com.fitworkgym.databinding.FragmentTrainerBinding
import com.fitworkgym.ui.adapter.AdapterAdmin
import com.fitworkgym.ui.viewmodel.TrainerViewModel
import com.google.firebase.firestore.FirebaseFirestore

class TrainerFragment : Fragment() {

    private var _binding: FragmentTrainerBinding? = null

    private val binding get() = _binding!!

    private lateinit var trainerViewModel: TrainerViewModel


    private lateinit var trainerAdapter: AdapterAdmin

    private var db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val firestore = FirebaseFirestore.getInstance()
        val trainerDataSource = TrainerDataSource(firestore)
        val trainerRepository = TrainerRepository(trainerDataSource)

        trainerViewModel =
            ViewModelProvider(this, object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TrainerViewModel(trainerRepository) as T
                }
            })[TrainerViewModel::class.java]
        _binding = FragmentTrainerBinding.inflate(inflater, container, false)

        recyclerView()
        showTrainers()
        val root: View = binding.root
        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView()
        showTrainers()
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_trainer_to_addTrainerFragment)
        }

    }

    private fun recyclerView() {
        trainerAdapter = AdapterAdmin()
        binding.contentRecyclerAdmin.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
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