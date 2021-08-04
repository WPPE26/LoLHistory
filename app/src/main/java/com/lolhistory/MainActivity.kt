package com.lolhistory

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lolhistory.databinding.ActivityMainBinding
import com.lolhistory.datamodel.SummonerIDInfo
import com.lolhistory.datamodel.SummonerRankInfo
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainActivityViewModel

    lateinit var inputMethodManager: InputMethodManager


    private var isVisibleInfoLayout = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)


        viewModel.getSummonerRankInfoLiveData().observe(this,{summonerRankInfo ->

            viewModel.getSummonerIDInfoLiveData().observe(this,{summonerIdInfo ->
                if (summonerIdInfo == null){
                    val notExistToast = Toast.makeText(
                        applicationContext,
                        R.string.not_exist_summoner,Toast.LENGTH_SHORT
                    )
                    notExistToast.show()
                }


            })


        if (summonerRankInfo != null){
            setRankInfoView(summonerRankInfo)
            isVisibleInfoLayout = true
            binding.inputLayout.visibility = View.GONE
        }


        })



        viewModel.getHistoryAdapterLiveData().observe(this,{historyAdapter ->
            if (historyAdapter != null){
                binding.rvHistory.adapter = historyAdapter
                binding.swipeLayut.isRefreshing= false
            }else{
                val historyErrorToast = Toast.makeText(
                    applicationContext,
                    R.string.history_error, Toast.LENGTH_SHORT

                )
                historyErrorToast.show()
            }






        })
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        binding.rvHistory.setHasFixedSize(true)


        binding.swipeLayut.setOnRefreshListener { ->
            viewModel.searchSummoner(binding.tvSummonerName.text.toString())
        }
        //viewModel.getSummonerRankInfoLiveData().observe(this, { summonerRankInfo ->
        //    setRankInfoView(summonerRankInfo)
        //    binding.inputLayout.visibility = View.GONE
        //})


        binding.btnInputSummoner.setOnClickListener{ v ->
            inputMethodManager.hideSoftInputFromWindow(binding.etInputSummoner.windowToken, 0)
            Log.d("TESTLOG", "setOnClickListener: binding.etInputSummoner.text.toString()")
            viewModel.searchSummoner(binding.etInputSummoner.text.toString())


        }
    }
    override fun onBackPressed(){
        if(isVisibleInfoLayout){
            binding.infoLayout.visibility = View.GONE
            binding.inputLayout.visibility = View.VISIBLE
            isVisibleInfoLayout = !isVisibleInfoLayout

        }else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    private fun setRankInfoView(rankInfo: SummonerRankInfo){
        setTierEmblem(rankInfo.tier)
        binding.tvSummonerName.text = rankInfo.summonerName

        var tierRank = rankInfo.tier + " " + rankInfo.rank
        binding.tvTier.text = tierRank

        if (rankInfo.tier == "UNRANKED" ){
            binding.tvRankType.text =""
            binding.tvLp.text= ""
            binding.tvTotalWinRate.text = ""
            binding.tvTotalWinLose.text = ""
        }else{
            binding.tvRankType.text = rankInfo.queueType
            val point = rankInfo.leaguePonits.toString() +"LP"
            binding.tvLp.text = point

            val rate = rankInfo.wins.toDouble()/(rankInfo.wins + rankInfo.losses).toDouble() *100
            binding.tvTotalWinRate.text = String.format(Locale.getDefault(),"%.2f%%", rate)

            val winAndLosses = rankInfo.wins.toString() + " 승"+ rankInfo.losses.toString() + "패"
            binding.tvTotalWinLose.text = winAndLosses
        }
        binding.infoLayout.visibility = View.VISIBLE
    }
    private fun setTierEmblem(tier: String){
        when(tier){
            "UNRANKED" -> binding.ivTierEmblem.setImageResource(R.drawable.emblem_unranked)
            "IRON" -> binding.ivTierEmblem.setImageResource(R.drawable.emblem_iron)
            "BRONZE" -> binding.ivTierEmblem.setImageResource(R.drawable.emblem_bronze)
            "SILVER" -> binding.ivTierEmblem.setImageResource(R.drawable.emblem_silver)
            "GOLD" -> binding.ivTierEmblem.setImageResource(R.drawable.emblem_gold)
            "PLATINUM" -> binding.ivTierEmblem.setImageResource(R.drawable.emblem_platinum)
            "DIAMOND" -> binding.ivTierEmblem.setImageResource(R.drawable.emblem_diamond)
            "MASTER" -> binding.ivTierEmblem.setImageResource(R.drawable.emblem_master)
            "GRANDMASTER" -> binding.ivTierEmblem.setImageResource(R.drawable.emblem_grandmaster)
            "CHALLENGER" -> binding.ivTierEmblem.setImageResource(R.drawable.emblem_challenger)





        }

}
}



