package com.hayawanat.app.ui.puzzle

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
import com.hayawanat.app.databinding.FragmentPuzzleBinding

class PuzzleFragment : Fragment() {

    private var _binding: FragmentPuzzleBinding? = null
    private val binding get() = _binding!!
    private val args: PuzzleFragmentArgs by navArgs()
    private val viewModel: PuzzleViewModel by viewModels {
        PuzzleViewModelFactory(args.level)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPuzzleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentPuzzle.observe(viewLifecycleOwner) { puzzle ->
            if (puzzle == null) return@observe
            binding.tvEmoji.text = puzzle.animal.emoji
            binding.tvFrench.text = puzzle.animal.french
            binding.tvHint.text = "Translittération : ${puzzle.animal.transliteration}"
            binding.tvMasked.text = puzzle.maskedWord
            binding.tvProgress.text = "${viewModel.questionIndex + 1} / ${viewModel.total}"
            setupLetterButtons(puzzle.choices)
            binding.tvFeedback.visibility = View.GONE
        }

        viewModel.gameFinished.observe(viewLifecycleOwner) { finished ->
            if (finished) {
                findNavController().navigate(
                    PuzzleFragmentDirections.actionPuzzleToResult(
                        score = viewModel.score,
                        total = viewModel.total,
                        gameMode = "PUZZLE",
                        level = args.level
                    )
                )
            }
        }
    }

    private fun setupLetterButtons(choices: List<String>) {
        binding.containerLetters.removeAllViews()
        choices.forEach { letter ->
            val btn = Button(requireContext()).apply {
                text = letter
                setOnClickListener { onLetterSelected(letter, this) }
            }
            binding.containerLetters.addView(btn)
        }
    }

    private fun onLetterSelected(letter: String, btn: Button) {
        val correct = viewModel.tryLetter(letter)
        btn.setBackgroundColor(
            ContextCompat.getColor(requireContext(),
                if (correct) R.color.correct else R.color.wrong)
        )
        if (!correct) btn.isEnabled = false

        binding.tvMasked.text = viewModel.currentPuzzle.value?.maskedWord

        if (viewModel.isWordComplete()) {
            binding.tvFeedback.visibility = View.VISIBLE
            binding.tvFeedback.text = "✅ ${viewModel.currentPuzzle.value?.animal?.arabic}"
            binding.root.postDelayed({ viewModel.nextQuestion() }, 1200)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
