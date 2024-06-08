package com.zandroid.mycalculator.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.zandroid.mycalculator.databinding.ActivityHistoryBinding
import com.zandroid.mycalculator.repository.HistoryRepository
import com.zandroid.mycalculator.room.CalcDao
import com.zandroid.mycalculator.room.CalcEntity
import com.zandroid.mycalculator.ui.HistoryAdapter
import com.zandroid.mycalculator.viewModel.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration

@AndroidEntryPoint
class HistoryActivity : AppCompatActivity() {

    //Binding
    private lateinit var binding: ActivityHistoryBinding

    private val viewModel:HistoryViewModel by viewModels()

    @Inject
    lateinit var historyAdapter: HistoryAdapter2

    @Inject
    lateinit var dao: CalcDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            //get data
            viewModel.loadHistory()
            viewModel.historyList.observe(this@HistoryActivity){
                historyAdapter.setData(it)
                Log.e( "list ", it.toString())
            }

            //delete item
            historyAdapter.setOnItemClickListener {
                val entity=CalcEntity(it.id,it.expression,it.result)
                lifecycle.coroutineScope.launch {
                    dao.deleteExpression(entity)
                    historyAdapter.setData(dao.getAllHistory())
                }
            }


            //clear All
            btnClear.setOnClickListener {
          viewModel.clearHistory()
                    viewModel.emptyList.observe(this@HistoryActivity) {
                        historyAdapter.setData(it)
                        Log.e( "emptylist ", it.toString())
                    }

             //   fadeOutRecyclerView()
            }


            recyclerHistory.apply {
                layoutManager=LinearLayoutManager(this@HistoryActivity)
                adapter=historyAdapter
            }

        }


    }

/*    private fun fadeOutRecyclerView() {
        val animator = ObjectAnimator.ofFloat(binding.recyclerHistory, "alpha", 1.5f, 0f)
        animator.duration = 1000 // 1 second
        animator.start()

    }*/
}