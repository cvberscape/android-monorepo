package com.example.a2023k.ui.home

import android.content.Context
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.a2023k.BuildConfig
import com.example.a2023k.R
import com.example.a2023k.databinding.FragmentHomeBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.*
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class HomeFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentHomeBinding? = null
    private var myString: String? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var firebaseDataBase : FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null
    private var launchTime: Long = 0



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        launchTime = System.currentTimeMillis()


        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment!!.getMapAsync { mMap ->
            mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE

            myString = arguments?.getString("myString")

            Log.d("locatest","$myString")

            val latLngString = myString.toString()
            var lat = 0.0
            var lng = 0.0
            if (latLngString.length > 10) {
                val latLngParts = latLngString.split(",")
                lat = latLngParts[0].toDouble()
                lng = latLngParts[1].toDouble()

            } else {
                Log.d("error123","something went wrong")
            }

            val latLng = LatLng(lat, lng)
            val zoomLevel = 15f
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Marker")
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))

        }


          firebaseDataBase = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_DATABASE_URL)


        val circularProgressBar2 = binding.circularProgressBar2
        circularProgressBar2.apply {
            // Set Progress

            // Set Progress Max
            progressMax = 120f

            // Set ProgressBar Color
            progressBarColor = Color.RED
            // or with gradient
            progressBarColorStart = Color.RED
            progressBarColorEnd = Color.WHITE
            progressBarColorDirection = CircularProgressBar.GradientDirection.BOTTOM_TO_END

            // Set background ProgressBar Color
            backgroundProgressBarColor = Color.GRAY
            // or with gradient
            backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set Width
            progressBarWidth = 10f // in DP
            backgroundProgressBarWidth = 3f // in DP

            // Other
            roundBorder = true
            startAngle = 180f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }


        val circularProgressBar = binding.circularProgressBar
        circularProgressBar.apply {
            progressMax = 40f

            // Set ProgressBar Color
            progressBarColor = Color.RED
            // or with gradient
            progressBarColorStart = Color.RED
            progressBarColorEnd = Color.WHITE
            progressBarColorDirection = CircularProgressBar.GradientDirection.BOTTOM_TO_END

            // Set background ProgressBar Color
            backgroundProgressBarColor = Color.GRAY
            // or with gradient
            backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set Width
            progressBarWidth = 10f // in DP
            backgroundProgressBarWidth = 3f // in DP

            // Other
            roundBorder = true
            startAngle = 180f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }


        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val textView2: TextView = binding.textHomebpm
        homeViewModel.textbpm.observe(viewLifecycleOwner) {
            textView2.text = it
        }

        val textView3: TextView = binding.textHomebpmstatic
        homeViewModel.textbpmstatic.observe(viewLifecycleOwner) {
            textView3.text = it
        }

        val textView4: TextView = binding.textHome2
        homeViewModel.text2.observe(viewLifecycleOwner) {
            textView4.text = it
        }

        val textView5: TextView = binding.textHomerr
        homeViewModel.textrr.observe(viewLifecycleOwner) {
            textView5.text = it
        }

        val textView6: TextView = binding.textHomerrstatic
        homeViewModel.textrrstatic.observe(viewLifecycleOwner) {
            textView6.text = it
        }
        val textView7: TextView = binding.textHomemapstatic
        homeViewModel.textmapstatic.observe(viewLifecycleOwner) {
            textView7.text = it
        }

        getData(textView2, textView5, circularProgressBar, circularProgressBar2)


        return root
    }
    override fun onMapReady(googleMap: GoogleMap) {
    }
    private fun getData(textView2: TextView, textView5: TextView, circularProgressBar : CircularProgressBar, circularProgressBar2 : CircularProgressBar ) {
        val c = Calendar.getInstance()

        val month = c.get(Calendar.MONTH) + 1
        val day = c.get(Calendar.DAY_OF_MONTH)

        val monthstr = month.toString()
        val datestr = day.toString()

        fun monthFunct(monthstr: String): String {
            return if (monthstr.length < 2) {
                "0$monthstr"
            } else {
                monthstr
            }
        }

        fun dateFunct(datestr: String): String {
            return if (datestr.length < 2) {
                "0$datestr"
            } else {
                datestr
            }
        }

        Log.d("DDMM", "${monthFunct(monthstr)}, ${dateFunct(datestr)}")

        databaseReference = firebaseDataBase?.getReference("huh")?.child(monthFunct(monthstr))?.child(dateFunct(datestr)) //?.child("04")?.child("24")

        databaseReference?.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val mostRecentChildValue = snapshot.child("data").value

                val hrRegex = """"HR": "(\d+)"""".toRegex()
                val rrRegex = """"RR": "(\d+)"""".toRegex()
                val sRegex = """"S": "(\d+)"""".toRegex()

                val hrValue = hrRegex.find("$mostRecentChildValue")?.groupValues?.get(1)
                val rrValue = rrRegex.find("$mostRecentChildValue")?.groupValues?.get(1)
                val sValue = sRegex.find("$mostRecentChildValue")?.groupValues?.get(1)

                Log.d("valuelog", "$hrValue:$rrValue:$sValue")

                textView2.text = hrValue
                textView5.text = " $rrValue"


                val progressnumberhr = hrValue.toString()
                val progressnumberhrf = progressnumberhr.toFloat()

                val progressnumberrr = rrValue.toString()
                val progressnumberrrf = progressnumberrr.toFloat()

                val currentTime = System.currentTimeMillis()
                val timeDifference = currentTime - launchTime

                if (timeDifference > (30 * 1000)) { // ignore if launched within 30 seconds
                    if (progressnumberhrf >= 130) {
                        showAlertDialog(context)
                    }

                }

                circularProgressBar2.apply {
                    progress = progressnumberhrf
                }

                circularProgressBar.apply {
                    progress = progressnumberrrf
                }
            }

override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
// This method will be called if any of the child values are changed
}

override fun onChildRemoved(dataSnapshot: DataSnapshot) {
// This method will be called if any of the child values are removed
}

override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
// This method will be called if any of the child values are moved
}

override fun onCancelled(databaseError: DatabaseError) {
// Handle errors
Log.d("errortest", "error")
}
})
}

private fun showAlertDialog(context: Context?) {
val builder = context?.let { MaterialAlertDialogBuilder(it, R.style.AlertDialogTheme) }
builder?.setTitle("High Heartrate Alert")
builder?.setMessage("Your heartrate has exceeded the threshold of 130 bpm.")
builder?.setPositiveButton("Dismiss", null)
builder?.show()
}


override fun onDestroyView() {
super.onDestroyView()
_binding = null
}
}