package com.hayawanat.app.ui.quiz

import android.os.Bundle
import android.os.CountDownTimer
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
import com.hayawanat.app.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private val args: QuizFragmentArgs by navArgs()
    private val viewModel: QuizViewModel by viewModels {
        QuizViewModelFactory(args.level)
    }

    private var timer: CountDownTimer? = null
    private val answerButtons get() = listOf(
        binding.btnAnswer0, binding.btnAnswer1,
        binding.btnAnswer2, binding.btnAnswer3
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentQuestion.observe(viewLifecycleOwner) { q ->
            if (q == null) return@observe
            binding.tvEmoji.text = q.animal.emoji
            binding.tvArabic.text = q.animal.arabic
            binding.tvTranslit.text = q.animal.transliteration
            binding.tvProgress.text = "${viewModel.questionIndex + 1} / ${viewModel.totalQuestions}"
            binding.progressBar.progress = ((viewModel.questionIndex.toFloat() / viewModel.totalQuestions) * 100).toInt()

            answerButtons.forEachIndexed { i, btn ->
                btn.text = q.choices[i]
                btn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.btn_default))
                btn.isEnabled = true
            }
            startTimer()
        }

        viewModel.gameFinished.observe(viewLifecycleOwner) { finished ->
            if (finished) {
                timer?.cancel()
                val action = QuizFragmentDirections.actionQuizToResult(
                    score = viewModel.score,
                    total = viewModel.totalQuestions,
                    gameMode = "QUIZ",
                    level = args.level
                )
                findNavController().navigate(action)
            }
        }

        answerButtons.forEachIndexed { i, btn ->
            btn.setOnClickListener { onAnswerSelected(i, btn) }
        }
    }

    private fun onAnswerSelected(index: Int, btn: Button) {
        timer?.cancel()
        answerButtons.forEach { it.isEnabled = false }

        val correct = viewModel.checkAnswer(index)
        val correctIdx = viewModel.currentQuestion.value?.correctIndex ?: 0

        btn.setBackgroundColor(
            ContextCompat.getColor(requireContext(),
                if (correct) R.color.correct else R.color.wrong)
        )
        if (!correct) {
            answerButtons[correctIdx].setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.correct)
            )
        }

        binding.root.postDelayed({ viewModel.nextQuestion() }, 1200)
    }

    private fun startTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(10_000, 100) {
            override fun onTick(ms: Long) {
                binding.timerBar.progress = (ms / 100).toInt()
            }
            override fun onFinish() {
                answerButtons.forEach { it.isEnabled = false }
                val correctIdx = viewModel.currentQuestion.value?.correctIndex ?: 0
                answerButtons[correctIdx].setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.correct)
                )
                binding.root.postDelayed({ viewModel.nextQuestion() }, 1000)
            }
        }.start()
    }

    override fun onDestroyView() {
        timer?.cancel()
        super.onDestroyView()
        _binding = null
    }
}
