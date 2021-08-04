package com.lolhistory.datamodel

import android.accounts.Account
import com.google.gson.annotations.SerializedName

/**
 * "id": "g4KCgGnX1lXMfF1qODItFvsi4H-G1OaMNbhksphtagasaks",
"accountId": "L7_pljPmVjeCxUTwsaHAfA_8OsvTx0KEUgo7n9u74Z-RdGs",
"puuid": "Bc7DHcGSFq8jlPNmmFa0sEXIBXanwDtxHzJ7Akl9IAwBQolQYdJoDDmmzh1D-Hrg4oc-VBPPlYCdRg",
"name": "연신내사나이",
"profileIconId": 3868,
"revisionDate": 1627391600068,
"summonerLevel": 285
 */
data class SummonerIDInfo (
    @SerializedName("id")
    var id: String,

    @SerializedName("accountId")
    var account: String,

    @SerializedName("puuid")
    var puuid: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("summonerLevel")
    var summonerLevel: Int

        ) {

}

