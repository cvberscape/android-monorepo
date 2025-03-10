package com.example.android2023ej

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.android2023ej.classes.DividerItemDecoration
import com.example.android2023ej.classes.Feedback
import com.example.android2023ej.classes.FeedbackAdapter
import com.example.android2023ej.databinding.FragmentFeedbackReadBinding
import com.google.gson.GsonBuilder
import org.json.JSONObject

class FeedbackReadFragment : Fragment() {
    private var _binding: FragmentFeedbackReadBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedbackReadBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Get all feedbacks on fragment launch
        getFeedbacks()

        // Button click listener, opens Feedback Send Fragment on click
        binding.buttonSendFeedback.setOnClickListener {
            val action = FeedbackReadFragmentDirections.actionFeedbackReadFragmentToFeedbackSendFragment()
            it.findNavController().navigate(action)
        }


        // Swipe refresh listener, calls getFeedbacks() to refresh data
        binding.swiperefresh.setOnRefreshListener {
            getFeedbacks()
            binding.swiperefresh.isRefreshing = false
        }

        return root
    }

    var feedbacks : List<Feedback> = emptyList();

    fun getFeedbacks(){

        // Using http://10.0.2.2 for localhost. this is a virtual address for Android emulators, since
        // localhost refers to the Android device instead of your computer
        val JSON_URL = "http://10.0.2.2:1337/api/feedbacks"
        val gson = GsonBuilder().setPrettyPrinting().create()

        // GET request from provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.GET, JSON_URL,
            Response.Listener { response ->
                val jObject = JSONObject(response)

                val jArray = jObject.getJSONArray("data")

                // Parses JSON object
                feedbacks = gson.fromJson(jArray.toString() , Array<Feedback>::class.java).toList()

                // Display parsed data in RecyclerView
                val layoutManager = LinearLayoutManager(activity)
                binding.recyclerViewFeedbacks.layoutManager = layoutManager
                val adapter = FeedbackAdapter(feedbacks as MutableList<Feedback>)
                binding.recyclerViewFeedbacks.adapter = adapter
                // Sets divider for each RecyclerView entry
                val dividerItemDecoration = DividerItemDecoration(requireContext(), R.drawable.divider_line)
                binding.recyclerViewFeedbacks.addItemDecoration(dividerItemDecoration)
            },

            // Logs errors
            Response.ErrorListener {
                Log.d("ADVTECH", it.toString())
            })
        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                // we have to specify a proper header, otherwise Apigility will block our queries!
                // define we are after JSON data!
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json; charset=utf-8"
                // Define bearer token for Strapi
                headers["Authorization"] = "Bearer ${BuildConfig.STRAPI_TOKEN}"

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