package com.jia.opens.golden.scales.towards.zsdk5f

import android.content.Context
import android.os.Build
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


object HealthCreon {
    var keySo: String = "edr78ccfdfvvgggt"

    @Throws(IOException::class)
    private fun readAssetToBytes(context: Context, assetName: String): ByteArray {
        context.assets.open(assetName).use { `is` ->
            val buffer = ByteArrayOutputStream()
            val data = ByteArray(16384)
            var nRead: Int
            while ((`is`.read(data).also { nRead = it }) != -1) {
                buffer.write(data, 0, nRead)
            }
            return buffer.toByteArray()
        }
    }

    @Throws(java.lang.Exception::class)
    fun encrypt(data: ByteArray?, key: String): ByteArray {
        val secretKey = SecretKeySpec(key.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return cipher.doFinal(data)
    }
    // 将加密后的数据写入文件
    @Throws(IOException::class)
    fun writeFile(data: ByteArray?, filePath: String?) {
        val fos = FileOutputStream(filePath)
        fos.write(data)
        fos.close()
    }


    // AES解密
    @Throws(Exception::class)
    fun decrypt(encryptedData: ByteArray?, key: String): ByteArray {
        val secretKey = SecretKeySpec(key.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        return cipher.doFinal(encryptedData)
    }
//    fun generateEncryptedPng(context: Context) {
//        try {
//            val soBytes = readAssetToBytes(context, "88.so")
//            // 对文件内容进行AES加密
//            val encryptedData = encrypt(soBytes, keySo)
//            // 将加密后的内容保存为新文件
//            val fileName = context.dataDir.toString() + "/88.mp3"
//            writeFile(encryptedData, fileName)
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }
//    }
//    fun generateEncryptedPng2(context: Context) {
//        try {
//            val soBytes = readAssetToBytes(context, "77.so")
//            // 对文件内容进行AES加密
//            val encryptedData = encrypt(soBytes, keySo)
//            // 将加密后的内容保存为新文件
//            val fileName = context.dataDir.toString() + "/77.mp3"
//            writeFile(encryptedData, fileName)
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }
//    }
    fun loadEncryptedSo(context: Context) {
        try {
            val abi = getSupportedAbi()
            val soAssetName = getAssetName(abi, false)
            Log.e("TAG", "loadEncryptedSo: $soAssetName")
            val encryptedData = readAssetToBytes(context, soAssetName)
            val decryptedData = decrypt(encryptedData, keySo)
            val fileName = context.dataDir.toString() + "/health.so"
            writeFile(decryptedData, fileName)
            System.load(fileName)
            File(fileName).delete()
        } catch (e: Exception) {
            Log.e("TAG", "解密san-so=$e")
        }
    }

//    fun loadEncryptedH5So(context: Context) {
//        try {
//            // 1. 获取设备架构信息
//            val abi = getSupportedAbi()
//            // 2. 从assets读取加密的so文件
//            val soAssetName = getAssetName(abi, true)
//            // 读取加密后的test.png文件
//            val encryptedData = readAssetToBytes(context, soAssetName)
//
//            // 对加密数据进行解密
//            val decryptedData = decrypt(encryptedData, keySo)
//            val fileName = context.dataDir.toString() + "/han.so"
//
//            // 将解密后的数据恢复为原始的dev8a.so文件
//            writeFile(decryptedData, fileName)
//            System.load(fileName)
//            //删除文件
//            File(fileName).delete()
//        } catch (e: Exception) {
//            Log.e("TAG", "解密han-so=$e")
//        }
//    }
    fun getSupportedAbi(): String {
        // 优先检测64位架构
        for (abi in Build.SUPPORTED_64_BIT_ABIS) {
            if (abi.startsWith("arm64") || abi.startsWith("x86_64")) {
                return abi
            }
        }
        for (abi in Build.SUPPORTED_32_BIT_ABIS) {
            if (abi.startsWith("armeabi") || abi.startsWith("x86")) {
                return abi
            }
        }
        return Build.CPU_ABI
    }

    fun getAssetName(abi: String,isH5:Boolean): String {
        // 根据架构选择加密文件名
        if (abi.contains("64")) {
            val assetName =if(isH5){
                ""
            }else{
                "88.mp3"
            }
            return assetName // 64位加密文件
        }
        val assetName =if(isH5){
            ""
        }else{
            "77.mp3"
        }
        return assetName // 32位加密文件
    }
}
