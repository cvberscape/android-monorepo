package com.example.android2023ej

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android2023ej.databinding.FragmentCloudMqttBinding
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck

class CloudMqttFragment : Fragment() {
    private var _binding: FragmentCloudMqttBinding? = null

    private val binding get() = _binding!!

    private lateinit var client: Mqtt3AsyncClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCloudMqttBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Builds config MQTT connection
        client = MqttClient.builder()
            .useMqttVersion3()
            .sslWithDefaultConfig()
            .identifier(BuildConfig.HIVEMQ_ID)
            .serverHost(BuildConfig.HIVEMQ_BROKER)
            .serverPort(8883)
            .buildAsync()

        // Connects to HiveMQ cluster
        client.connectWith()
            .simpleAuth()
            .username(BuildConfig.HIVEMQ_USERNAME)
            .password(BuildConfig.HIVEMQ_PASSWORD.toByteArray())
            .applySimpleAuth()
            .send()
            .whenComplete { connAck: Mqtt3ConnAck?, throwable: Throwable? ->
                if (throwable != null) {
                    Log.d("ADVTECH", "Connection failure.")
                } else {
                    // Setup subscribes or start publishing
                    subscribeToTopic()
                }
            }

        // Button on click listener for button press
        // When pressed empties editText collects its value
        binding.buttonSendCloudMessage.setOnClickListener {
            var stringPayload = binding.editTextMsg.text.toString()
            binding.editTextMsg.setText("")
            binding.editTextMsg.hint = "Your Message"

            // Publishes editText value to HiveMQ
            client.publishWith()
                .topic(BuildConfig.HIVEMQ_TOPIC)
                .payload(stringPayload.toByteArray())
                .send()
        }

        return root
    }

    fun subscribeToTopic()
    {
        client.subscribeWith()
            .topicFilter(BuildConfig.HIVEMQ_TOPIC)
            .callback { publish ->

                // This callback runs everytime your code receives new data payload
                var result = String(publish.getPayloadAsBytes())
                Log.d("ADVTECH", result)

                // Adds latest message to custom view which is running on ui thread
                activity?.runOnUiThread {
                    binding.latestDataViewTester.addData(result)

                }


            }
            .send()
            .whenComplete { subAck, throwable ->
                if (throwable != null) {
                    // Handle failure to subscribe
                    Log.d("ADVTECH", "Subscribe failed.")
                } else {
                    // Handle successful subscription, e.g. logging or incrementing a metric
                    Log.d("ADVTECH", "Subscribed!")
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        client.disconnect()
    }
}