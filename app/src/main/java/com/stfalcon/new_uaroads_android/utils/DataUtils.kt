/*
 * Copyright (c) 2017 stfalcon.com
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.stfalcon.new_uaroads_android.utils

import android.util.Base64
import com.stfalcon.new_uaroads_android.features.record.managers.Point
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.security.NoSuchAlgorithmException
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

/*
 * Created by Anton Bevza on 4/28/17.
 */
class DataUtils {

    fun buildString(data: List<Point>): String {
        var s = ""
        try {
            val stringBuilder = StringBuilder()
            for (aData in data) {
                stringBuilder.append(aData.time).append(";")
                stringBuilder.append(aData.pit).append(";")
                stringBuilder.append(aData.lat).append(";")
                stringBuilder.append(aData.lng).append(";")
                stringBuilder.append(aData.type).append(";")
                stringBuilder.append(aData.accuracy).append(";")
                stringBuilder.append(aData.speed).append("#")
            }
            s = stringBuilder.toString()
            return compress(s)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return s
    }

    fun md5(s: String): String {
        try {
            // Create MD5 Hash
            val digest = java.security.MessageDigest.getInstance("MD5")
            digest.update(s.toByteArray())
            val messageDigest = digest.digest()

            // Create Hex String
            val hexString = StringBuilder()
            for (aMessageDigest in messageDigest) hexString.append(Integer.toHexString(0xFF and aMessageDigest.toInt()))
            return hexString.toString()

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return ""
    }


    @Throws(IOException::class)
    fun compress(str: String): String {
        val os = ByteArrayOutputStream(str.length)
        val gos = GZIPOutputStream(os)
        gos.write(str.toByteArray())
        gos.close()
        os.close()
        val compressed = ByteArray(os.toByteArray().size)
        System.arraycopy(os.toByteArray(), 0, compressed, 0,
                os.toByteArray().size)
        return Base64.encodeToString(compressed, Base64.DEFAULT)
    }

    @Throws(IOException::class)
    fun decompress(zipText: String): String {
        val compressed = Base64.decode(zipText, Base64.DEFAULT)
        if (compressed.isNotEmpty()) {
            val gzipInputStream = GZIPInputStream(
                    ByteArrayInputStream(compressed, 0,
                            compressed.size)
            )

            val baos = ByteArrayOutputStream()
            var value = 0
            while (value != -1) {
                value = gzipInputStream.read()
                if (value != -1) {
                    baos.write(value)
                }
            }
            gzipInputStream.close()
            baos.close()
            val sReturn = String(baos.toByteArray())
            return sReturn
        } else {
            return ""
        }
    }
}