package com.pokemon.mebius.benchmark

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Serializable
import com.pokemon.mebius.benchmark.MebiusBenchMarkUtils.LEVEL_HIGH
import com.pokemon.mebius.benchmark.MebiusBenchMarkUtils.LEVEL_HIGH_MIDDLE
import com.pokemon.mebius.benchmark.MebiusBenchMarkUtils.LEVEL_LOW
import com.pokemon.mebius.benchmark.MebiusBenchMarkUtils.LEVEL_LOW_VERY
import com.pokemon.mebius.benchmark.MebiusBenchMarkUtils.LEVEL_MIDDLE
import com.pokemon.mebius.benchmark.MebiusBenchMarkUtils.LEVEL_UNKNOWN

class JsonBenchUtils {
    // 设备品牌，如xiaomi、huawei、samsung
    private val JSON_MANUFACTURER = "${Build.MANUFACTURER.toLowerCase()}.json"

    // 杂牌手机列表，没有命中xiaomi之类的，就去这个json下面找
    val JSON_OTHERS = "others.json"

    val ASSETS_PARENT_PATH = "benchmark"

    fun getLevelFromJson(context: Context): Int {
        try {
            val phoneBrand = Build.BRAND
            val benchMarkBeanCache = getBrandJson(context)
            return when (true) {
                benchMarkBeanCache?.high?.contains(phoneBrand) -> LEVEL_HIGH
                benchMarkBeanCache?.high_middle?.contains(phoneBrand) -> LEVEL_HIGH_MIDDLE
                benchMarkBeanCache?.middle?.contains(phoneBrand) -> LEVEL_MIDDLE
                benchMarkBeanCache?.low?.contains(phoneBrand) -> LEVEL_LOW
                benchMarkBeanCache?.low_very?.contains(phoneBrand) -> LEVEL_LOW_VERY
                else -> LEVEL_UNKNOWN
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return LEVEL_UNKNOWN
    }

    /**
     * 根据手机品牌，查找对应的json，并找到在json对应的手机等级
     * */
    @SuppressLint("CheckResult")
    fun getBrandJson(context: Context): BenchMarkBean? {
        try {
            readAssets(context, getBenchMarkJson(context)).let { fileContent ->
                Gson().fromJson<BenchMarkBean>(
                    fileContent,
                    object : TypeToken<BenchMarkBean>() {}.type
                ).let {
                    return it
                }
            }
        } catch (e: Exception) {
            Log.d("BenchMarkUtils", "fatal getLocalJson:$e")
        }
        return null
    }


    private fun readAssets(context: Context, fileName: String): String {
        context.assets.open(
            fileName
        ).getFileContent().let {
            return it
        }
        return ""
    }

    private fun InputStream.getFileContent(): String {
        val stringBuilder = StringBuilder()
        BufferedReader(
            InputStreamReader(this)
        ).use { reader ->
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        }
        return stringBuilder.toString()
    }


    private fun getBenchMarkJson(context: Context): String {
        // 首先根据设备品牌去找对应的json，如小米手机找xiaomi.json
        // 如果是杂牌手机，则去找others.json
        return if (context.checkAssetsExit(ASSETS_PARENT_PATH, JSON_MANUFACTURER)) {
            "$ASSETS_PARENT_PATH/$JSON_MANUFACTURER"
        } else {
            "$ASSETS_PARENT_PATH/$JSON_OTHERS"
        }
    }

    private fun Context.checkAssetsExit(
        assertParentPath: String,
        targetFileName: String
    ): Boolean {
        return assets.list(
            assertParentPath
        )?.find {
            it == targetFileName
        } != null
    }

}

@androidx.annotation.Keep
data class BenchMarkBean(
    val high: List<String?>?,
    val high_middle: List<String?>?,
    val middle: List<String?>?,
    val low: List<String?>?,
    val low_very: List<String?>?,
) : Serializable