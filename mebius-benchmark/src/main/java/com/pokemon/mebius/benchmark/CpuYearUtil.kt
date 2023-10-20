package com.pokemon.mebius.benchmark

import java.io.File
import java.io.FileFilter
import java.io.FileInputStream
import java.io.IOException
import java.util.regex.Pattern

class CpuYearUtil {
    val KHZ = 1000L
    val BASE_YEAR = 2013
    private val cpuFreq = longArrayOf(
        1520 * KHZ,  //2013,1.52GHZ
        1620 * KHZ,  //2014,
        1780 * KHZ,  //810，   1.98GHZ,2015   低端机
        2000 * KHZ,  //820,   2.2HGZ,4,2016      低端机
        2200 * KHZ,  //835,   2.45HGZ,2017       中端
        2600 * KHZ,  //845,   2.8HGZ,2018        中端
        2830 * KHZ,  //855,   2.84HGZ,2019       高端机
        2900 * KHZ,  //860,   2.96HGZ,2020       高端机
        3000 * KHZ
    )

    // 获取最大主频对应的年份
    // 一般0号CPU拥有最大主频
    val cpuYear: Int
        get() {
            val cpuZeroClock = getMaxCpuFreq()
            for (i in cpuFreq.indices.reversed()) {
                if (cpuFreq[i] < cpuZeroClock) {
                    return i + BASE_YEAR
                }
            }
            return BASE_YEAR
        }


    //手机CPU主频大小
    private fun getMaxCpuFreq(): Long {
        var maxCpuFreq: Long = 0
        try {
            for (cpu in 0 until cpuCounts) {
                val freqBound = "/sys/devices/system/cpu/cpu$cpu/cpufreq/cpuinfo_max_freq"
                val cpuInfoMaxFreqFile = File(freqBound)
                if (cpuInfoMaxFreqFile.exists()) {
                    val buffer = ByteArray(128)
                    val stream = FileInputStream(cpuInfoMaxFreqFile)
                    try {
                        stream.read(buffer)
                        var e1 = 0
                        while (buffer[e1] in 48..57 && e1 < buffer.size) {
                            ++e1
                        }
                        val str = String(buffer, 0, e1)
                        val freqBound1 = Integer.valueOf(str.toInt())
                        if (freqBound1.toInt() > maxCpuFreq) {
                            maxCpuFreq = freqBound1.toInt().toLong()
                        }
                    } catch (var20: NumberFormatException) {
                    } finally {
                        stream.close()
                    }
                }
            }
            if (maxCpuFreq == -1L) {
                val cpuInfo = FileInputStream("/proc/cpuinfo")
                cpuInfo.use { cpuInfo ->
                    var cpuMHz = parseFileForValue("cpu MHz", cpuInfo)
                    cpuMHz *= 1000
                    if (cpuMHz > maxCpuFreq) {
                        maxCpuFreq = cpuMHz.toLong()
                    }
                }
            }
        } catch (exception: IOException) {
            maxCpuFreq = -1
        }
        return maxCpuFreq
    }

    //Filter to only list the devices we care about
    //Return the number of cores (virtual CPU devices)
    //Check if filename is "cpu", followed by a single digit number//Private Class to display only CPU devices in the directory listing
    /**
     * Gets the number of cores available in this device, across all processors.
     * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpu"
     *
     * @return The number of cores, or 1 if failed to get result
     */
    private val cpuCounts: Int
        get() {
            var cpuCount = 0//Get directory containing CPU info

            //Private Class to display only CPU devices in the directory listing
            class CpuFilter : FileFilter {
                override fun accept(pathname: File): Boolean {
                    //Check if filename is "cpu", followed by a single digit number
                    return Pattern.matches("cpu[0-9]", pathname.name)
                }
            }
            cpuCount = try {
                //Get directory containing CPU info
                val dir = File("/sys/devices/system/cpu/")
                //Filter to only list the devices we care about
                val files = dir.listFiles(CpuFilter())
                //Return the number of cores (virtual CPU devices)
                files?.size ?: 1
            } catch (e: Throwable) {
                1
            }
            return cpuCount
        }


}

fun parseFileForValue(textToMatch: String, stream: FileInputStream): Int {
    val buffer = ByteArray(1024)
    try {
        val e = stream.read(buffer)
        var i = 0
        while (i < e) {
            if (buffer[i] == 10.toByte() || i == 0) {
                if (buffer[i] == 10.toByte()) {
                    ++i
                }
                for (j in i until e) {
                    val textIndex = j - i
                    if (buffer[j] != textToMatch[textIndex].toByte()) {
                        break
                    }
                    if (textIndex == textToMatch.length - 1) {
                        return extractValue(buffer, j)
                    }
                }
            }
            ++i
        }
    } catch (var7: IOException) {
    } catch (var8: NumberFormatException) {
    }
    return -1
}

private fun extractValue(buffer: ByteArray, index: Int): Int {
    var index = index
    while (index < buffer.size && buffer[index] != 10.toByte()) {
        if (buffer[index] in 48..57) {
            val start: Int = index++
            while (index < buffer.size && buffer[index] >= 48 && buffer[index] <= 57) {
                ++index
            }
            val str = java.lang.String(buffer, 0, start, index - start)
            return Integer.parseInt(str.toString())
        }
        ++index
    }
    return -1
}