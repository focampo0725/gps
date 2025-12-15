package com.kloubit.gps.infrastructure.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ZipUtils {

    /**
     * Clase que comprime multiples archivos en un solo archivo ZIP
     * (ZipOutputStream) para escribir en el archivo ZIP
     * (FileInputStream) para leer su contenido
     */
    companion object {
        fun compressFiles(
            routesFiles: List<String>,
            fileNameZip: String,
            context: Context
        ): String? {
            val buffer = ByteArray(1024)
            try {
                val zipFile = Environment.getExternalStorageDirectory().path + "/Download/" + fileNameZip
                val zipOutputStream = ZipOutputStream(FileOutputStream(zipFile))

                for (filePath in routesFiles) {
                    val fileInputStream = FileInputStream(File(filePath))
                    zipOutputStream.putNextEntry(ZipEntry(File(filePath).name))
                    var length: Int
                    while (fileInputStream.read(buffer).also { length = it } > 0) {
                        zipOutputStream.write(buffer, 0, length)
                    }
                    fileInputStream.close()
                }
                zipOutputStream.closeEntry()
                zipOutputStream.close()
                return zipFile
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }
    }

}