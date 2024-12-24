package com.fitworkgym.ui.fragments.login

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fitworkgym.R
import com.fitworkgym.data.repository.EquipoDataSource
import com.fitworkgym.data.model.Equipo
import com.fitworkgym.data.repository.EquipoRepository
import com.fitworkgym.databinding.FragmentAddEquipoBinding
import com.fitworkgym.ui.viewmodel.EquipoAdminViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class AddEquipoFragment : Fragment() {
    private var _binding: FragmentAddEquipoBinding? = null
    private val binding get() = _binding!!

    private lateinit var equipoViewModel: EquipoAdminViewModel
    private var fileUri: Uri? = null
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

        _binding = FragmentAddEquipoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btoEquipo.setOnClickListener {
            uploadImage()
        }

        binding.btoImgEquipo.setOnClickListener {
            abrirGaleria()
        }


        binding.btoVolverEquipo.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }
    override fun onResume() {
        super.onResume()
        // Accede al BottomNavigationView desde la actividad
        val bottomNavigationView = requireActivity().findViewById<View>(R.id.nav_view_admin)
        bottomNavigationView?.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()
        // Vuelve a mostrar el BottomNavigationView al salir del fragmento
        val bottomNavigationView = requireActivity().findViewById<View>(R.id.nav_view_admin)
        bottomNavigationView?.visibility = View.VISIBLE
    }

    private fun uploadImage() {
        if (fileUri != null) {
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("equipo_images/${System.currentTimeMillis()}.jpg")

            imageRef.putFile(fileUri!!).continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result.toString()

                    // Now you can use this downloadUri when creating the Trainer
                    val equipo = Equipo(
                        name = binding.nameEquipo.text.toString(),
                        description = binding.descriptionEquipo.text.toString(),
                        image = downloadUri, // Store the image URL
                    )

                    equipoViewModel.registerEquipo(equipo)

                    Toast.makeText(
                        context,
                        "Image uploaded successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Image upload failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Toast.makeText(
                context,
                "Please select an image first",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            data?.data?.let { uri ->
                fileUri = uri
                binding.imgTrainer.setImageURI(fileUri)
                Toast.makeText(
                    context,
                    "Imagen seleccionada",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}