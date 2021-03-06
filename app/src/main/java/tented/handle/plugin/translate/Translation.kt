package tented.handle.plugin.translate

import org.json.JSONObject
import tented.internet.Request
import java.net.URLEncoder

/**
 * Created by Hoshino Tented on 2017/12/25.
 *
 * 估计是百度的接口加了个验证。。。
 * 现在post过去的话
 * 会error
 */
object Translation
{
    private val translateApi : String = "http://fanyi.baidu.com/v2transapi"
    private val languageApi : String = "http://fanyi.baidu.com/langdetect"

    fun translate( query : String ) : String
    {
        val request : Request = Request(translateApi)
        val getQueryLanguage : String = getLanguage(query)
        val to : String = if(getQueryLanguage == "zh") "en" else "zh"

        val result : String = request.doPost("from=$getQueryLanguage&to=$to&query=${URLEncoder.encode(query, "UTF-8")}")
        val jsonObj : JSONObject = JSONObject(result)

        val data : JSONObject = jsonObj.getJSONObject("trans_result").getJSONArray("data").getJSONObject(0)

        return data.getString("dst")
    }

    fun getLanguage( query : String ) : String
    {
        val request : Request = Request(languageApi)
        val result : String = request.doPost("query=${URLEncoder.encode(query, "UTF-8")}")
        val jsonObj : JSONObject = JSONObject(result)

        if( jsonObj["error"].toString() == "-1" ) throw Exception("[TentedPlugin]Getting Language Error")

        return jsonObj.getString("lan")
    }
}