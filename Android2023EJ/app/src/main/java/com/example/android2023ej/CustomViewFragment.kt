package com.example.android2023ej

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android2023ej.databinding.FragmentCustomViewBinding
import java.util.*
import kotlin.random.Random


class CustomViewFragment : Fragment() {
    private var _binding: FragmentCustomViewBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomViewBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Button click listener for temperature custom view
        binding.buttonChangeTemperature.setOnClickListener {
            // Rolls random number between -50 and 50 and passes the value to view
            val temp = Random.nextInt(-50, 50)
            binding.customtemperatureviewTester.changeTemperature(temp)
        }

        // Button click listener for compound custom view
        binding.buttonAddDataTest.setOnClickListener {
            // Rolls random UUID and passes the value to view
            var text = UUID.randomUUID().toString()
            binding.latestDataViewTester.addData(text)
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}