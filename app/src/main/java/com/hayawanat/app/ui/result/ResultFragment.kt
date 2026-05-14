package com.hayawanat.app.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hayawanat.app.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val args: ResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val score = args.score
        val total = args.total
        val percent = if (total > 0) (score * 100 / total) else 0

        binding.tvScore.text = "$score / $total"
        binding.tvPercent.text = "$percent%"

        val stars = when {
            percent >= 90 -> "⭐⭐⭐"
            percent >= 60 -> "⭐⭐"
            percent >= 30 -> "⭐"
            else -> "💪 Continue !"
        }
        binding.tvStars.text = stars

        val message = when {
            percent == 100 -> "Parfait ! مبروك 🎉"
            percent >= 80  -> "Excellent ! أحسنت 👏"
            percent >= 60  -> "Bien joué ! جيد 😊"
            percent >= 40  -> "Continue ! واصل 💪"
            else           -> "Réessaye ! حاول مجدداً 🔄"
        }
        binding.tvMessage.text = message

        binding.btnReplay.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnHome.setOnClickListener {
            findNavController().popBackStack(
                com.hayawanat.app.R.id.homeFragment, false
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
