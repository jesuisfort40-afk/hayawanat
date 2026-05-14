package com.hayawanat.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hayawanat.app.R
import com.hayawanat.app.databinding.FragmentHomeBinding
import com.hayawanat.app.model.Difficulty
import com.hayawanat.app.model.GameMode

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var selectedDifficulty = Difficulty.ALL

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Difficulty chips
        binding.chipAll.setOnClickListener { selectedDifficulty = Difficulty.ALL; updateChips() }
        binding.chipBeginner.setOnClickListener { selectedDifficulty = Difficulty.BEGINNER; updateChips() }
        binding.chipIntermediate.setOnClickListener { selectedDifficulty = Difficulty.INTERMEDIATE; updateChips() }
        binding.chipExpert.setOnClickListener { selectedDifficulty = Difficulty.EXPERT; updateChips() }

        updateChips()

        // Game mode buttons
        binding.btnMatch.setOnClickListener { navigate(GameMode.MATCH) }
        binding.btnMemory.setOnClickListener { navigate(GameMode.MEMORY) }
        binding.btnPuzzle.setOnClickListener { navigate(GameMode.PUZZLE) }
        binding.btnListen.setOnClickListener { navigate(GameMode.LISTEN) }
        binding.btnQuiz.setOnClickListener { navigate(GameMode.QUIZ) }
    }

    private fun updateChips() {
        binding.chipAll.isChecked = selectedDifficulty == Difficulty.ALL
        binding.chipBeginner.isChecked = selectedDifficulty == Difficulty.BEGINNER
        binding.chipIntermediate.isChecked = selectedDifficulty == Difficulty.INTERMEDIATE
        binding.chipExpert.isChecked = selectedDifficulty == Difficulty.EXPERT
    }

    private fun navigate(mode: GameMode) {
        val action = when (mode) {
            GameMode.MATCH  -> HomeFragmentDirections.actionHomeToMatch(selectedDifficulty.level)
            GameMode.MEMORY -> HomeFragmentDirections.actionHomeToMemory(selectedDifficulty.level)
            GameMode.PUZZLE -> HomeFragmentDirections.actionHomeToPuzzle(selectedDifficulty.level)
            GameMode.LISTEN -> HomeFragmentDirections.actionHomeToListen(selectedDifficulty.level)
            GameMode.QUIZ   -> HomeFragmentDirections.actionHomeToQuiz(selectedDifficulty.level)
        }
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
