package com.hayawanat.app.ui.match

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hayawanat.app.R
import com.hayawanat.app.databinding.FragmentMatchBinding

class MatchFragment : Fragment() {

    private var _binding: FragmentMatchBinding? = null
    private val binding get() = _binding!!
    private val args: MatchFragmentArgs by navArgs()
    private val viewModel: MatchViewModel by viewModels {
        MatchViewModelFactory(args.level)
    }

    private var selectedArabicIndex: Int? = null
    private val arabicButtons = mutableListOf<Button>()
    private val frenchButtons = mutableListOf<Button>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.pairs.observe(viewLifecycleOwner) { pairs ->
            setupButtons(pairs)
        }

        viewModel.matchResult.observe(viewLifecycleOwner) { result ->
            result ?: return@observe
            val (arabicIdx, frenchIdx, correct) = result
            val color = if (correct) R.color.correct else R.color.wrong
            arabicButtons.getOrNull(arabicIdx)?.setBackgroundColor(
                ContextCompat.getColor(requireContext(), color)
            )
            frenchButtons.getOrNull(frenchIdx)?.setBackgroundColor(
                ContextCompat.getColor(requireContext(), color)
            )
            if (correct) {
                arabicButtons.getOrNull(arabicIdx)?.isEnabled = false
                frenchButtons.getOrNull(frenchIdx)?.isEnabled = false
            } else {
                binding.root.postDelayed({
                    arabicButtons.getOrNull(arabicIdx)?.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.btn_default)
                    )
                    frenchButtons.getOrNull(frenchIdx)?.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.btn_default)
                    )
                    selectedArabicIndex = null
                }, 800)
            }
        }

        viewModel.gameFinished.observe(viewLifecycleOwner) { finished ->
            if (finished) {
                findNavController().navigate(
                    MatchFragmentDirections.actionMatchToResult(
                        score = viewModel.score,
                        total = viewModel.totalPairs,
                        gameMode = "MATCH",
                        level = args.level
                    )
                )
            }
        }
    }

    private fun setupButtons(pairs: List<Pair<String, String>>) {
        arabicButtons.clear()
        frenchButtons.clear()
        binding.containerArabic.removeAllViews()
        binding.containerFrench.removeAllViews()

        val emojiFrench = viewModel.emojiFrenchPairs

        pairs.forEachIndexed { i, (arabic, _) ->
            val btn = Button(requireContext()).apply {
                text = arabic
                tag = i
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.btn_default))
                setOnClickListener { onArabicSelected(i, this) }
            }
            arabicButtons.add(btn)
            binding.containerArabic.addView(btn)
        }

        emojiFrench.forEachIndexed { i, (emoji, french) ->
            val btn = Button(requireContext()).apply {
                text = "$emoji $french"
                tag = i
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.btn_default))
                setOnClickListener { onFrenchSelected(i) }
            }
            frenchButtons.add(btn)
            binding.containerFrench.addView(btn)
        }
    }

    private fun onArabicSelected(index: Int, btn: Button) {
        arabicButtons.forEach {
            it.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.btn_default))
        }
        selectedArabicIndex = index
        btn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.selected))
    }

    private fun onFrenchSelected(frenchIndex: Int) {
        val arabicIdx = selectedArabicIndex ?: return
        selectedArabicIndex = null
        arabicButtons.forEach {
            if (it.isEnabled)
                it.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.btn_default))
        }
        viewModel.tryMatch(arabicIdx, frenchIndex)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
