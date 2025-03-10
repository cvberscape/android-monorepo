package com.example.android2023ej

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.android2023ej.databinding.FragmentTodoDetailBinding

class TodoDetailFragment : Fragment() {
    private var _binding: FragmentTodoDetailBinding? = null

    private val binding get() = _binding!!

    // Gets values from MapsFragment using safe args
    val args: TodoDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Sets textview as values from safe args
        binding.textView2.text = args.userId.toString()
        binding.textView4.text = args.id.toString()
        binding.textView6.text = args.title
        binding.textView8.text = args.completed.toString()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}