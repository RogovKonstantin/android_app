package utils

import android.content.Context
import android.os.Environment
import okio.IOException
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object FileUtils {


    fun saveHeroesToFile(context: Context, heroes: List<HeroModel>): File? {
        val fileName = "heroes.txt"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

        try {
            FileOutputStream(file).use { fos ->
                heroes.forEach { hero ->
                    fos.write("${hero.firstName} ${hero.lastName}\n".toByteArray())
                }
            }
            return file
        } catch (e: java.io.IOException) {
            e.printStackTrace()
            return null
        }
    }

    fun copyFile(source: File, destination: File): Boolean {
        return try {
            FileInputStream(source).use { input ->
                FileOutputStream(destination).use { output ->
                    input.copyTo(output)
                }
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    fun deleteFile(file: File): Boolean {
        return file.delete()
    }

    fun fileExists(file: File): Boolean {
        return file.exists()
    }
}
