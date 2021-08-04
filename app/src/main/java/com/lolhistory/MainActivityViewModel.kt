package com.lolhistory

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lolhistory.datamodel.MatchHistory
import com.lolhistory.datamodel.SummonerIDInfo
import com.lolhistory.datamodel.SummonerRankInfo
import com.lolhistory.repository.RiotRepository
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

class MainActivityViewModel :ViewModel() {

    private val repo = RiotRepository()

    private val summonerIdInfoLiveData = MutableLiveData<SummonerIDInfo>()
    private val summonerRankInfoLiveData = MutableLiveData<SummonerRankInfo>()
    private val historyAdapterLiveData = MutableLiveData<HistoryAdapter>()

    private var summonerName = ""

    private var matchHistories: ArrayList<MatchHistory> = ArrayList()

    fun getSummonerIDInfoLiveData(): MutableLiveData<SummonerIDInfo>{

        return summonerIdInfoLiveData
    }
    fun getSummonerRankInfoLiveData(): MutableLiveData<SummonerRankInfo>{
        return summonerRankInfoLiveData
    }
    fun getHistoryAdapterLiveData():MutableLiveData<HistoryAdapter>{
        return historyAdapterLiveData
    }

    fun searchSummoner(name: String){
        summonerName = name
        matchHistories.clear()
        /**
         * repo.getSummonerIdInfo(name).subscribe(object : SingleObserver<SummonerIDInfo>){
        override fun onSubscribs(d: Disposable){

        }

        override fun onSuccess(summonerIDInfo: SummonerIDInfo){
        var summonerName = summonerIDInfo.name
        }
        }
         */
        repo.getSummonerIdInfo(name).subscribe(object : SingleObserver<SummonerIDInfo> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onSuccess(summonerIDInfo: SummonerIDInfo) {
                getSummonerRankInfo(summonerIDInfo.id)
                getMatchList(summonerIDInfo.puuid)
            }

            override fun onError(e: Throwable) {
                Log.e("TESTLOG", "[getSummonerIdInfo] error: $e")
            }
        })
    }

    fun getSummonerRankInfo(summonerId: String){
        repo.getSummonerRankInfo(summonerId)
            .subscribe(object :SingleObserver<List<SummonerRankInfo>> {
                override fun onSubscribe(d: Disposable) {
                }


                override fun onSuccess(t: List<SummonerRankInfo>) {
                    setSummonerRankInfo(t)
                }

                override fun onError(e: Throwable) {
                    Log.e("TESTLOG","[getSummonerRankInfo] error: $e")
                }
            })


    }

    private fun getMatchList(puuid: String) {
        repo.getMatchList(puuid).subscribe(object : SingleObserver<ArrayList<String>> {
            override fun onSubscribe(d: Disposable) {
            }

            override fun onSuccess(array: ArrayList<String>) {
                var count = 0
                for (match in array) {

                    if (count < 15) {

                        count++
                        getMatchHistory(match,puuid)

                    } else {
                        break
                    }
                }
            }

            override fun onError(e: Throwable) {
                Log.e("TESTLOG","[getMatchList] error: $e")
            }

        })
    }

    private fun getMatchHistory(match:String, puuid: String){
        repo.getMatchHistory(match).subscribe(object: SingleObserver<MatchHistory>{
            override fun onSubscribe(d: Disposable) {

            }

            override fun onSuccess(t: MatchHistory) {
                matchHistories.add(t)
                if (matchHistories.size >14){
                    val historyAdapter = HistoryAdapter(matchHistories, puuid)
                    historyAdapterLiveData.value = historyAdapter
                }
            }

            override fun onError(e: Throwable) {
                Log.e("TESTLOG", "[getMatchHistory] error: $e")
                historyAdapterLiveData.value = null
            }
        })

    }

    private fun setSummonerRankInfo(summonerRankInfos: List<SummonerRankInfo>){

        var soloRankInfo: SummonerRankInfo? = null
        var flexRankInfo: SummonerRankInfo? =null
        var soloRankTier = 0
        var flexRankTier = 0

        if (summonerRankInfos.isEmpty()){
            val unRankInfo = SummonerRankInfo(summonerName, "","UNRANKED","",0,0,0)
            summonerRankInfoLiveData.value = unRankInfo

        } else {
            for (info in summonerRankInfos) {
                if (info.queueType == "RANKED_SOLO_5x5"){
                    soloRankInfo = info
                    soloRankTier = calcTier(info.tier, info.rank, info.leaguePonits)
                    Log.d("TESTLOG", "soloRankTier: $soloRankTier")
                } else if(info.queueType == "RANKED_FLEX_SR"){
                    flexRankInfo = info
                    flexRankTier =calcTier(info.tier, info.rank, info.leaguePonits)
                    Log.d("TESTLOG", "flexRankTier: $flexRankTier")

                }
            }

            if (soloRankTier < flexRankTier){
                summonerRankInfoLiveData.value = flexRankInfo
            } else {
                summonerRankInfoLiveData.value = soloRankInfo
            }
        }

    }
    private fun calcTier(tier: String, rank: String, lp:Int): Int {

        var tierPoint = 0
        when (tier){
            "BRONZE" -> tierPoint = 1000
            "SILVER" -> tierPoint = 2000
            "GOLD" -> tierPoint = 3000
            "PLATINUM" -> tierPoint = 4000
            "DIAMOND" -> tierPoint = 5000
            "MASTER" -> tierPoint = 6000
            "GRANDMASTER" -> tierPoint = 7000
            "CHALLENGER" -> tierPoint = 8000

        }
        when(rank){
            "IV" -> tierPoint += 100
            "III" -> tierPoint+= 300
            "II" -> tierPoint+= 500
            "I" ->tierPoint += 700

        }
        tierPoint += lp
        return tierPoint
    }




    }
