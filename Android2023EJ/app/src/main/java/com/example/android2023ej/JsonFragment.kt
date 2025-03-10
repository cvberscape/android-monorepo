package com.example.android2023ej

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.android2023ej.classes.DividerItemDecoration
import com.example.android2023ej.classes.TodoAdapter
import com.example.android2023ej.classes.todoDataClass
import com.example.android2023ej.databinding.FragmentJsonBinding
import com.google.gson.GsonBuilder

class JsonFragment : Fragment() {
    private var _binding: FragmentJsonBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJsonBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Button listener, gets JSON data on click
        binding.buttonJsonData.setOnClickListener {
            fetchJsonFromUrl()
        }

        return root
    }


    private fun fetchJsonFromUrl() {
        // URL for JSON request
        val JSON_URL = "https://jsonplaceholder.typicode.com/todos"

        // GET request from provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET, JSON_URL,
            Response.Listener { response ->

                // Print the response as a whole to logcat
                Log.d("ADVTECH", response)

                // Parse JSON data to an array object using gson
                val gson = GsonBuilder().setPrettyPrinting().create()
                val todos : List<todoDataClass> = gson.fromJson(response, Array<todoDataClass>::class.java).toList()

                // Set up the RecyclerView
                val recyclerView = binding.todoRecyclerView
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = TodoAdapter(todos)
                // Set up divider for RecyclerView entries
                val dividerItemDecoration = DividerItemDecoration(requireContext(), R.drawable.divider_line)
                recyclerView.addItemDecoration(dividerItemDecoration)
            },
            // Logs for errors
            Response.ErrorListener {
                // typically this is a connection error
                Log.d("ADVTECH", it.toString())
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                // basic headers for the data
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json; charset=utf-8"
                return headers
            }
        }

        // Add the request to the RequestQueue.
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}