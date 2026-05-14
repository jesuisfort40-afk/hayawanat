package com.hayawanat.app.ui.listen

import android.os.Bundle
import android.speech.tts.TextToSpeech
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
import com.hayawanat.app.databinding.FragmentListenBinding
import java.util.Locale

class ListenFragment : Fragment(), TextToSpeech.OnInitListener {

    private var _binding: FragmentListenBinding? = null
    private val binding get() = _binding!!
    private val args: ListenFragmentArgs by navArgs()
    private val viewModel: ListenViewModel by viewModels {
        ListenViewModelFactory(args.level)
    }

    private var tts: TextToSpeech? = null
    private var ttsReady = false

    private val answerButtons get() = listOf(
        binding.btnAnswer0, binding.btnAnswer1,
        binding.btnAnswer2, binding.btnAnswer3
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tts = TextToSpeech(requireContext(), this)

        viewModel.currentQuestion.observe(viewLifecycleOwner) { q ->
            if (q == null) return@observe
            binding.tvProgress.text = "${viewModel.questionIndex + 1} / ${viewModel.total}"
            answerButtons.forEachIndexed { i, btn ->
                btn.text = "${q.choices[i].emoji} ${q.choices[i].french}"
                btn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.btn_default))
                btn.isEnabled = true
            }
            binding.tvInstruction.text = "Écoute et trouve l'animal !"
            // Auto-speak after a short delay
            binding.root.postDelayed({ speakCurrent() }, 400)
        }

        viewModel.gameFinished.observe(viewLifecycleOwner) { finished ->
            if (finished) {
                tts?.stop()
                findNavController().navigate(
                    ListenFragmentDirections.actionListenToResult(
                        score = viewModel.score,
                        total = viewModel.total,
                        gameMode = "LISTEN",
                        level = args.level
                    )
                )
            }
        }

        binding.btnSpeak.setOnClickListener { speakCurrent() }

        answerButtons.forEachIndexed { i, btn ->
            btn.setOnClickListener { onAnswer(i, btn) }
        }
    }

    private fun speakCurrent() {
        if (!ttsReady) return
        val arabic = viewModel.currentQuestion.value?.animal?.arabic ?: return
        tts?.speak(arabic, TextToSpeech.QUEUE_FLUSH, null, "hayawanat_speak")
    }

    private fun onAnswer(index: Int, btn: Button) {
        answerButtons.forEach { it.isEnabled = false }
        val correct = viewModel.checkAnswer(index)
        val correctIdx = viewModel.currentQuestion.value?.correctIndex ?: 0
        btn.setBackgroundColor(
            ContextCompat.getColor(requireContext(), if (correct) R.color.correct else R.color.wrong)
        )
        if (!correct) {
            answerButtons[correctIdx].setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.correct)
            )
        }
        binding.root.postDelayed({ viewModel.nextQuestion() }, 1200)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("ar"))
            ttsReady = result != TextToSpeech.LANG_MISSING_DATA &&
                    result != TextToSpeech.LANG_NOT_SUPPORTED
            if (ttsReady) speakCurrent()
        }
    }

    override fun onDestroyView() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroyView()
        _binding = null
    }
}
