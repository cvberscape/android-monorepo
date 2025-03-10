package com.example.android2023ej

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.android2023ej.classes.weatherstation.OunasvaaraData
import com.example.android2023ej.databinding.FragmentWeatherStationBinding
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.FoldingCube
import com.google.gson.GsonBuilder
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WeatherStationFragment : Fragment() {
    private var _binding: FragmentWeatherStationBinding? = null

    private val binding get() = _binding!!

    private lateinit var client: Mqtt3AsyncClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherStationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Sets up SpinKit view
        val foldingCube: Sprite = FoldingCube()
        binding.progressBar.setIndeterminateDrawable(foldingCube)

        // Builds config MQTT connection
        client = MqttClient.builder()
            .useMqttVersion3()
            .sslWithDefaultConfig()
            .identifier(BuildConfig.MQTT_CLIENT_ID)
            .serverHost(BuildConfig.MQTT_BROKER)
            .serverPort(8883)
            .buildAsync()

        // Connects to weather station
        client.connectWith()
            .simpleAuth()
            .username(BuildConfig.MQTT_USERNAME)
            .password(BuildConfig.MQTT_PASSWORD.toByteArray())
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

        return root
    }

    fun subscribeToTopic()
    {
        val gson = GsonBuilder().setPrettyPrinting().create()

        client.subscribeWith()
            .topicFilter(BuildConfig.MQTT_TOPIC)
            .callback { publish ->

                // Show raw data in logcat
                var result = String(publish.getPayloadAsBytes())
                Log.d("ADVTECH", result)

                try {
                    // Parse data to objects
                    var item : OunasvaaraData = gson.fromJson(result, OunasvaaraData::class.java)
                    var temperature = item.d.get1().v
                    var pressure = item.d.get2().v
                    var humidity = item.d.get3().v

                    // Parse recieved timestamp to a more readable format
                    val dateTime = LocalDateTime.parse(item.ts, DateTimeFormatter.ISO_DATE_TIME)
                    val outputFormat = DateTimeFormatter.ofPattern("dd.MM-HH.mm.ss")
                    val outputString = dateTime.format(outputFormat)

                    // Builds string from parsed timestamp, temperature, pressure and humidity values
                    var text = "${outputString} - T: ${temperature}C P: ${pressure}bar H: ${humidity}%"

                    // Adds data to custom view which is running on ui thread
                    activity?.runOnUiThread {
                        //
                        binding.latestDataViewTester.addData(text)
                        binding.progressBar.visibility = View.GONE

                    }
                }
                catch(e : Exception) {
                    Log.d("ADVTECH", e.message.toString())
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