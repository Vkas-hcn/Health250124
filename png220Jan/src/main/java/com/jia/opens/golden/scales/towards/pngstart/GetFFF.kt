package com.jia.opens.golden.scales.towards.pngstart

import android.content.Context
import android.util.Log
import com.jia.opens.golden.scales.towards.jgri6sd.SanShow.getAssetName
import com.jia.opens.golden.scales.towards.jgri6sd.SanShow.getSupportedAbi
import com.jia.opens.golden.scales.towards.jgri6sd.SanZong
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object GetFFF {
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

    //    public static byte[] encrypt(byte[] data, String key) throws Exception {
    //        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
    //        Cipher cipher = Cipher.getInstance("AES");
    //        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    //        return cipher.doFinal(data);
    //    }
    // 将加密后的数据写入文件
    @Throws(IOException::class)
    fun writeFile(data: ByteArray?, filePath: String?) {
        val fos = FileOutputStream(filePath)
        fos.write(data)
        fos.close()
    }

    //    public static void generateEncryptedPng(Context context) {
    //        try {
    //
    //            byte[] soBytes = readAssetToBytes(context, "sanv8a.so");
    //            // 对文件内容进行AES加密
    //            byte[] encryptedData = encrypt(soBytes, SanZong.keykeyFFF);
    //            // 将加密后的内容保存为新文件
    //            String fileName = context.getDataDir() + "/san8.png";
    //            writeFile(encryptedData, fileName);
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //    }
    // AES解密
    @Throws(Exception::class)
    fun decrypt(encryptedData: ByteArray?, key: String): ByteArray {
        val secretKey = SecretKeySpec(key.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        return cipher.doFinal(encryptedData)
    }

    fun loadEncryptedSo(context: Context) {
        try {
            val abi = getSupportedAbi()
            val soAssetName = getAssetName(abi, false)
            Log.e("TAG", "loadEncryptedSo: $soAssetName")
            val encryptedData = readAssetToBytes(context, soAssetName)
            val decryptedData = decrypt(encryptedData, SanZong.keykeyFFF)
            val fileName = context.dataDir.toString() + "/san.so"
            writeFile(decryptedData, fileName)
            System.load(fileName)
            File(fileName).delete()
        } catch (e: Exception) {
            Log.e("TAG", "解密so=$e")
        }
    }

    fun loadEncryptedH5So(context: Context) {
        try {
            // 1. 获取设备架构信息
            val abi = getSupportedAbi()
            // 2. 从assets读取加密的so文件
            val soAssetName = getAssetName(abi, true)
            // 读取加密后的test.png文件
            val encryptedData = readAssetToBytes(context, soAssetName)

            // 对加密数据进行解密
            val decryptedData = decrypt(encryptedData, SanZong.keykeyFFF)
            val fileName = context.dataDir.toString() + "/san.so"

            // 将解密后的数据恢复为原始的dev8a.so文件
            writeFile(decryptedData, fileName)
            System.load(fileName)
            //删除文件
            File(fileName).delete()
        } catch (e: Exception) {
            Log.e("TAG", "解密h5so=$e")
        }
    }
}
