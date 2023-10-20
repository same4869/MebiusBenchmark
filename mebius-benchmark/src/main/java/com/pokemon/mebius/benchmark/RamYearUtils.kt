package com.pokemon.mebius.benchmark

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import java.io.FileInputStream
import java.io.IOException

class RamYearUtils {
    val MB = 1000 * 1000L
    val BASE_YEAR = 2012
    private val ramSize = longArrayOf(
        500 * MB,  //2012
        1500 * MB,  //2013
        2500 * MB,  //2014
        3500 * MB,  //2015   低端
        3500 * MB,  //2016   低端
        5500 * MB,  //2017   中端
        7500 * MB,  //2018   中-高端
        8000 * MB,  //2019   高端
        12000 * MB
    )

    fun getRamYear(context: Context): Int {
        var totalRam = getTotalRamMemory(context)
        for (i in ramSize.indices.reversed()) {
            if (ramSize[i] < totalRam) {
                return i + BASE_YEAR
            }
        }
        return BASE_YEAR
    }

    /**
     * 获取手机内存信息，和官方文档一致
     */
    private fun getTotalRamMemory(context: Context): Long {
        return if (Build.VERSION.SDK_INT >= 16) {
            val totalMem1 = ActivityManager.MemoryInfo()
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            am.getMemoryInfo(totalMem1)
            totalMem1?.totalMem ?: -1L
        } else {
            var totalMem = -1L
            try {
                val memInfo = FileInputStream("/proc/meminfo")
                memInfo.use { memInfo ->
                    totalMem = parseFileForValue("MemTotal", memInfo).toLong()
                    totalMem *= 1024L
                }
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
            totalMem
        }
    }
}