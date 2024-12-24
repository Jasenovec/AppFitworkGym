package com.fitworkgym.ui.fragments.sede

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fitworkgym.R
import com.fitworkgym.databinding.FragmentMapsBinding
import com.fitworkgym.ui.viewmodel.MapsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null

    private val binding get() = _binding!!

    private val defaultLocation = LatLng(-12.0464, -77.0428)

    private val locations = listOf(
        LocationData("Sede: El porvenir  ",LatLng(-9.059216726516588, -78.5783001174114)),//-9.059216726516588, -78.5783001174114
        LocationData("Sede: Centro   ",LatLng(-9.074286973971105, -78.59075578193476)),//-9.074286973971105, -78.59075578193476
        LocationData("Sede: Ovalo   ",LatLng(-9.128543560479134, -78.51766761059338)),//-9.128543560479134, -78.51766761059338
        LocationData("Sede: La marina   ",LatLng(-9.126840458619258, -78.53264833959467))//-9.126840458619258, -78.53264833959467
    )

    private lateinit var googleMap: GoogleMap

    private val mapsViewModel: MapsViewModel by viewModels()


    private val callback = OnMapReadyCallback { googleMap ->
        this.googleMap = googleMap
        mapsViewModel.currentLocation.observe(viewLifecycleOwner) { _ ->
            googleMap.clear()
            locations.forEach { locationMap ->
                googleMap.addMarker(MarkerOptions().position(locationMap.latLng).title(locationMap.name))

            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations[0].latLng,15f))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMapsBinding.inflate(inflater, container, false)

        binding.rb1.text = locations[0].name
        binding.rb2.text = locations[1].name
        binding.rb3.text = locations[2].name
        binding.rb4.text = locations[3].name

        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        mapsViewModel.updateLocation(defaultLocation)

        setupRadioGroup()
    }

    private fun setupRadioGroup() {
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_1 -> moveToLocation(locations[0])
                R.id.rb_2 -> moveToLocation(locations[1])
                R.id.rb_3 -> moveToLocation(locations[2])
                R.id.rb_4 -> moveToLocation(locations[3])
            }
        }
    }

    private fun moveToLocation(location: LocationData) {
        mapsViewModel.updateLocation(location.latLng)
        googleMap.clear()
        googleMap.addMarker(
            MarkerOptions()
                .position(location.latLng)
                .title(location.name)
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location.latLng, 15f))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class LocationData(val name: String, val latLng: LatLng)