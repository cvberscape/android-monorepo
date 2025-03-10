package com.example.android2023ej

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.navArgs
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.android2023ej.classes.FeedbackSend
import com.example.android2023ej.databinding.FragmentFeedbackSendBinding
import com.google.gson.GsonBuilder
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.io.UnsupportedEncodingException


class FeedbackSendFragment : Fragment() {
    private var _binding: FragmentFeedbackSendBinding? = null

    private val binding get() = _binding!!

    val args: FeedbackSendFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedbackSendBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Button listener, passes values to sendFeedback method when clicked.
        binding.buttonSendFeedbackApi.setOnClickListener {
            val name = binding.editTextFeedbackName.text.toString()
            val location = binding.editTextFeedbackLocation.text.toString()
            val value = binding.editTextFeedbackValue.text.toString()

            sendFeedback(name, location, value)
        }

        return root
    }

    fun sendFeedback(name : String, location : String, value : String) {
        // Using http://10.0.2.2 for localhost. this is a virtual address for Android emulators, since
        // localhost refers to the Android device instead of your computer
        val JSON_URL = "http://10.0.2.2:1337/api/feedbacks"
        var gson = GsonBuilder().create();

        // POST request from provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.POST, JSON_URL,
            Response.Listener { response ->
                Log.d("TESTI", "Lähetys Directusiin ok!")

                // Empties editText fields
                binding.editTextFeedbackName.setText("")
                binding.editTextFeedbackLocation.setText("")
                binding.editTextFeedbackValue.setText("")

                // Shows success toast, using MotionToast
                val context = requireContext()
                MotionToast.createColorToast(
                    context as Activity,
                    "Feedback Sent",
                    "Your feedback and information has been sent for review",
                    MotionToastStyle.SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helveticabold))
                // Returns back to previous fragment
                activity?.onBackPressed()
            },
            // Logs for errors
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

            // Builds new data
            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                // this function is only needed when sending data
                var body = ByteArray(0)
                try {
                    // Create newData variable
                    var newData = ""
                    // Builds a new Feedback object around the values of editText fields
                    var f : FeedbackSend = FeedbackSend()
                    f.data!!.name = name
                    f.data!!.location = location
                    f.data!!.value = value

                    // Parses Feedback object to JSON using gson
                    newData = gson.toJson(f)

                    // JSON to bytes
                    body = newData.toByteArray(Charsets.UTF_8)
                } catch (e: UnsupportedEncodingException) {
                    // problems with converting our data into UTF-8 bytes
                }
                return body
            }
        }

        // Add the request to the RequestQueue. This has to be done in both getting and sending new data.
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}