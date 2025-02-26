package com.jia.opens.golden.scales.towards.pngstart

import android.content.Context
import android.util.Log
import com.jia.opens.golden.scales.towards.vjire.PngCanGo.getAssetName
import com.jia.opens.golden.scales.towards.vjire.PngCanGo.getSupportedAbi
import com.jia.opens.golden.scales.towards.vjire.PngAllData
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object ChangingSo {

    // 读取assets中的文件到字节数组
    @Throws(IOException::class)
    private fun readAssetToBytes(context: Context, assetName: String): ByteArray {
        context.assets.open(assetName).use { inputStream ->
            return inputStream.readBytes()  // 更简洁的读取方法
        }
    }

    // AES解密
    @Throws(Exception::class)
    private fun decrypt(encryptedData: ByteArray, key: String): ByteArray {
        val secretKey = SecretKeySpec(key.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES").apply {
            init(Cipher.DECRYPT_MODE, secretKey)
        }
        return cipher.doFinal(encryptedData)
    }

    // 加载并解密SO文件
    private fun loadEncryptedSoFile(context: Context, isH5: Boolean) {
        try {
            val abi = getSupportedAbi()
            val soAssetName = getAssetName(abi, isH5)
            Log.e("TAG", "Loading encrypted SO file: $soAssetName")

            val encryptedData = readAssetToBytes(context, soAssetName)
            val decryptedData = decrypt(encryptedData, PngAllData.aeskey)

            val fileName = context.dataDir.toString() + if (isH5) "/hJia.so" else "/pngJia.so"

            // 将解密后的数据写入文件
            writeFile(decryptedData, fileName)
            System.load(fileName)
            File(fileName).delete()  // 加载后删除文件

        } catch (e: Exception) {
            Log.e("TAG", "Error loading encrypted SO file: $e")
        }
    }

    // 加载普通加密SO文件
    fun loadEncryptedSo(context: Context) {
        loadEncryptedSoFile(context, false)
    }

    // 加载H5加密SO文件
    fun loadEncryptedH5So(context: Context) {
        loadEncryptedSoFile(context, true)
    }

    // 将解密后的数据写入文件
    @Throws(IOException::class)
    private fun writeFile(data: ByteArray, filePath: String) {
        FileOutputStream(filePath).use { fos ->
            fos.write(data)
        }
    }
}

