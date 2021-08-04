package com.lolhistory.retrofit

import com.lolhistory.datamodel.SummonerIDInfo
import com.lolhistory.datamodel.SummonerRankInfo
import com.lolhistory.datamodel.MatchHistory
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface RiotAPI {
    @Headers("Accept: Application/json","X-Riot-Token: "+BaseUrl.RIOT_API_KEY)
    @GET(BaseUrl.RIOT_API_GET_SUMMONER + "{userId}")
    fun getSummonerIdInfo(@Path("userId") userId:String): Single<SummonerIDInfo>

    @Headers("Accept: Application/json","X-Riot-Token: "+BaseUrl.RIOT_API_KEY)
    @GET(BaseUrl.RIOT_API_GET_RANK + "{Id}")
    fun getSummonerRankInfo(@Path("Id") userId:String): Single<List<SummonerRankInfo>>

    @Headers("Accept: Application/json","X-Riot-Token: "+BaseUrl.RIOT_API_KEY)
    @GET(BaseUrl.RIOT_API_GET_MATCH_LIST + "{puuid}/ids")
    fun getMatchHistoryList(@Path("puuid") puuid: String): Single<ArrayList<String>>

    @Headers("Accept: Application/json","X-Riot-Token: "+BaseUrl.RIOT_API_KEY)
    @GET(BaseUrl.RIOT_API_GET_MATCH+"{matchId}")
    fun getMatchHistory(@Path("matchId") matchId: String): Single<MatchHistory>

}