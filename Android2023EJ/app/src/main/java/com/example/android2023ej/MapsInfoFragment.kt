package com.example.android2023ej

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.android2023ej.databinding.FragmentMapsInfoBinding

class MapsInfoFragment : Fragment() {
    private var _binding: FragmentMapsInfoBinding? = null

    private val binding get() = _binding!!

    // Gets values from MapsFragment using safe args
    val args: MapsInfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Sets textview as values from safe args
        binding.textViewtem.text = args.tem + " C"
        binding.textViewhum.text = args.hum + " %"
        binding.textViewwin.text = args.win + " M/S"

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}