package com.example.android2023ej

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import com.example.android2023ej.databinding.FragmentThirdPartyComponentBinding
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.WanderingCubes
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class ThirdPartyComponentFragment : Fragment() {
    private var _binding: FragmentThirdPartyComponentBinding? = null

    private val binding get() = _binding!!

    private lateinit var wanderingCubes: Sprite


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentThirdPartyComponentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Sets up SpinKit View
        wanderingCubes = WanderingCubes()
        binding.progressBar.setIndeterminateDrawable(wanderingCubes)

        // Button on click listener, calls toggleProgressBarVisbility method when clicked
        binding.buttonSpinkit.setOnClickListener {
            toggleProgressBarVisibility()
        }

        // Button on click listener, launches intro made using AppIntro on click
        binding.buttonIntro.setOnClickListener {
           val action = ThirdPartyComponentFragmentDirections.actionThirdPartyComponentFragmentToMyCustomAppIntro()
            it.findNavController().navigate(action)
        }

        // Button on click listener, shows success toast on click
        binding.buttonSuccess.setOnClickListener {
            val context = requireContext()
            MotionToast.createToast(
                context as Activity,
                "Success",
                "This is a success toast",
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helveticabold))
        }
        // Button on click listener, shows error toast on click
        binding.buttonError.setOnClickListener {
            val context = requireContext()
            MotionToast.createToast(
                context as Activity,
                "Failed",
                "This is a failure toast",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helveticabold))
        }
        // Button on click listener, shows warning toast on click
        binding.buttonWarning.setOnClickListener {
            val context = requireContext()
            MotionToast.createToast(
                context as Activity,
                "Warning",
                "Please fill all the details!",
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helveticabold))
        }
        // Button on click listener, shows info toast on click
        binding.buttonInfo.setOnClickListener {
            val context = requireContext()
            MotionToast.createToast(
                context as Activity,
                "Note",
                "This is an information toast!",
                MotionToastStyle.INFO,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helveticabold))
        }
        // Button on click listener, shows delete toast on click
        binding.buttonDelete.setOnClickListener {
            val context = requireContext()
            MotionToast.createToast(
                context as Activity,
                "Deleted",
                "This is a deletion toast",
                MotionToastStyle.DELETE,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helveticabold))
        }
        return root
    }

    private fun toggleProgressBarVisibility() {
         if (binding.progressBar.visibility == View.VISIBLE) {
            // If progressBar is visible, sets visibility to View.GONE and changes text to "SHOW"
            binding.progressBar.visibility = View.GONE
            binding.buttonSpinkit.text = "SHOW"       }
         else {
             // If progressBar is not visible, sets visibility to View.VISIBLE and changes text to "HIDE"
            binding.progressBar.visibility = View.VISIBLE
             binding.buttonSpinkit.text = "HIDE"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}