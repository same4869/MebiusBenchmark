package com.pokemon.mebius.benchmark

import android.content.Context
import com.pokemon.mebius.benchmark.MebiusBenchMarkUtils.LEVEL_HIGH
import com.pokemon.mebius.benchmark.MebiusBenchMarkUtils.LEVEL_HIGH_MIDDLE
import com.pokemon.mebius.benchmark.MebiusBenchMarkUtils.LEVEL_LOW
import com.pokemon.mebius.benchmark.MebiusBenchMarkUtils.LEVEL_LOW_VERY
import com.pokemon.mebius.benchmark.MebiusBenchMarkUtils.LEVEL_MIDDLE

class PhoneYearUtils {
    /**
     *  2013,// 超低端机
     *  2014,// 超低端机
     *  2015,// 低端机
     *  2016,// 低端机
     *  2017,// 中端
     *  2018,// 中-高端机
     *  2019,// 中-高端机
     *  2020,// 高端机
     *  2021,// 高端机
     * */
    fun getPhoneLevel(context: Context): Int {
        var phoneYear = getPhoneYear(context)
        return when (true) {
            (phoneYear >= 2020) -> {
                LEVEL_HIGH
            }
            (phoneYear >= 2018) -> {
                LEVEL_HIGH_MIDDLE
            }

            (phoneYear >= 2017) -> {
                LEVEL_MIDDLE
            }

            (phoneYear >= 2015) -> {
                LEVEL_LOW
            }

            else -> {
                LEVEL_LOW_VERY
            }
        }
    }

    private fun getPhoneYear(context: Context): Int {
        var cpuYear = CpuYearUtil().cpuYear
        var ramYear = RamYearUtils().getRamYear(context)
        // 因为有些手机可能是高CPU+低内存，或者低CPU+高内存，所以要做个加权平均
        // 加权以CPU为主，内存为辅
        return (cpuYear * 0.6 + ramYear * 0.4).toInt()
    }
}