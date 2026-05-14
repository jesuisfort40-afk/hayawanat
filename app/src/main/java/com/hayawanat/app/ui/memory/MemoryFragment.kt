package com.hayawanat.app.ui.memory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.hayawanat.app.databinding.FragmentMemoryBinding

class MemoryFragment : Fragment() {

    private var _binding: FragmentMemoryBinding? = null
    private val binding get() = _binding!!
    private val args: MemoryFragmentArgs by navArgs()
    private val viewModel: MemoryViewModel by viewModels {
        MemoryViewModelFactory(args.level)
    }

    private lateinit var adapter: MemoryCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MemoryCardAdapter { position ->
            viewModel.flipCard(position)
        }

        binding.rvCards.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.rvCards.adapter = adapter

        viewModel.cards.observe(viewLifecycleOwner) { cards ->
            adapter.submitList(cards.toMutableList())
        }

        viewModel.attempts.observe(viewLifecycleOwner) { attempts ->
            binding.tvAttempts.text = "Essais : $attempts"
        }

        viewModel.gameFinished.observe(viewLifecycleOwner) { finished ->
            if (finished) {
                findNavController().navigate(
                    MemoryFragmentDirections.actionMemoryToResult(
                        score = viewModel.matchedPairs,
                        total = viewModel.totalPairs,
                        gameMode = "MEMORY",
                        level = args.level
                    )
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
