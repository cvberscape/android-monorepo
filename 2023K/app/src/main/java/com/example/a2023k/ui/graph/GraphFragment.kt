package com.example.a2023k.ui.graph

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.a2023k.BuildConfig
import com.example.a2023k.databinding.FragmentGraphBinding
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartAnimationType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GraphFragment : Fragment() {

    private var _binding: FragmentGraphBinding? = null
    private val binding get() = _binding!!

    private val firebaseDatabase by lazy {
        Firebase.database(BuildConfig.FIREBASE_DATABASE_URL)
    }

    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM")
    private val hrRegex = """"HR": "(\d+)"""".toRegex()
    private val rrRegex = """"RR": "(\d+)"""".toRegex()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGraphBinding.inflate(inflater, container, false)
        setupViewModel()
        setupChartData()
        return binding.root
    }

    private fun setupViewModel() {
        ViewModelProvider(this).get(GraphViewModel::class.java).apply {
            text.observe(viewLifecycleOwner) {
                binding.textGraph.text = it
            }
        }
    }

    private fun setupChartData() {
        val dates = List(4) { index ->
            LocalDate.now().minusDays((index + 1).toLong())
        }.reversed()

        val databaseReferences = dates.map { date ->
            val month = date.format(DateTimeFormatter.ofPattern("MM"))
            val day = date.format(DateTimeFormatter.ofPattern("dd"))
            firebaseDatabase.getReference("huh/$month/dailyAverages/$day")
        }

        var completedQueries = 0
        val chartData = Array(4) { Pair(0f, 0f) }

        databaseReferences.forEachIndexed { index, reference ->
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val dataString = snapshot.getValue(String::class.java) ?: ""
                    chartData[index] = parseHealthMetrics(dataString)

                    if (++completedQueries == 4) {
                        updateChart(dates, chartData)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Database error: ${error.toException()}")
                    if (++completedQueries == 4) {
                        updateChart(dates, chartData)
                    }
                }
            })
        }
    }

    private fun parseHealthMetrics(data: String): Pair<Float, Float> {
        val hr = hrRegex.find(data)?.groupValues?.get(1)?.toFloatOrNull() ?: 0f
        val rr = rrRegex.find(data)?.groupValues?.get(1)?.toFloatOrNull() ?: 0f
        return Pair(hr, rr)
    }

    private fun updateChart(dates: List<LocalDate>, data: Array<Pair<Float, Float>>) {
        val categories = dates.map { it.format(dateFormatter) }
        val (hrValues, rrValues) = data.toList().let { pairs ->
            Pair(pairs.map { it.first }, pairs.map { it.second })
        }

        AAChartModel().apply {
            chartType(AAChartType.Column)
            backgroundColor("#000624")
            categories(categories.toTypedArray())
            dataLabelsEnabled(false)
            yAxisMax(140)
            axesTextColor("#FFFFFF")
            animationType(AAChartAnimationType.EaseInSine)
            animationDuration(1000)
            colorsTheme(arrayOf("#FF0000", "#0000FF"))
            series(arrayOf(
                AASeriesElement()
                    .name("HEART RATE")
                    .data(hrValues.toTypedArray()),
                AASeriesElement()
                    .name("RESPIRATORY RATE")
                    .data(rrValues.toTypedArray())
            ))
            binding.aaChartView.aa_drawChartWithChartModel(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}