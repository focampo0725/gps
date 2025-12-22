package com.kloubit.gps.data.room

import android.content.Context
import android.os.Environment
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kloubit.gps.data.room.dao.CompanyDao
import com.kloubit.gps.data.room.dao.ScheduledRoadmapDao
import com.kloubit.gps.domain.entities.Company
import com.kloubit.gps.domain.entities.ScheduledRoadmap
import com.kloubit.gps.infrastructure.extensions.logi
import java.io.File
import java.io.FileOutputStream


@Database(
    entities = [
        Company::class,
        ScheduledRoadmap::class
    ], version = 1, exportSchema = false
)
abstract class AppDB : RoomDatabase(

) {
    abstract fun companyDao() : CompanyDao
    abstract fun scheduledRoadmapDao() : ScheduledRoadmapDao

    companion object {
        const val DATABASE_NAME = "app.db"


//        private var todoDefaultList : MutableList<Todo> = mutableListOf(
//            Todo(alias = "one"), Todo(alias = "two"))

//        private var compnayDefaultList : MutableList<Company> = mutableListOf(
//            Company(companyCode = "1", companyName = "Empresa1"))

        // tipos de click : usb permission, apagado pantalla, reinicio equipo,
        @Volatile
        private var INSTANCE: AppDB? = null
        fun getInstance(context: Context): AppDB? {
            INSTANCE ?: synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDB::class.java,
                    DATABASE_NAME
                )
//                    .addCallback(seedDatabaseCallback(context))
                    .fallbackToDestructiveMigration()
//                        .openHelperFactory(factory)
                    .build()
            }
            return INSTANCE
        }

        /**
         * poblamos la tabla. El mètodo addCallback se ejecuta una sola vez en la construcciòn de la bd..
         */
//        private fun seedDatabaseCallback(context: Context): Callback {
//            return object : Callback() {
//                override fun onCreate(db: SupportSQLiteDatabase) {
//                    super.onCreate(db)
//                    doAsynTask({
//                        getInstance(context)?.todoDao()?.let {
//                            it.insertAllOrReplace(todoDefaultList)
//                        }
//                    }, {
////                        context.logi("${it?.size} is the size..")
//                    })
//                }
//            }
//        }

        /**
         * remueve las viejas bases de datos de la sdcard..
         * change to listof()
         */
        fun removeOldDtabaseFile(path: String) {
            try {
                val deleted = File(path).delete()
                this.logi("deleted $deleted")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun deleteDatabaseFile() {
            removeOldDtabaseFile(Environment.getExternalStorageDirectory().path + "/Download/" + "backup_" + DATABASE_NAME)
            removeOldDtabaseFile(Environment.getExternalStorageDirectory().path + "/Download/" + "backup_" + DATABASE_NAME + "-shm")
            removeOldDtabaseFile(Environment.getExternalStorageDirectory().path + "/Download/" + "backup_" + DATABASE_NAME + "-wal")
            this.logi("success")
        }

        /**
         * exporta y retorna el path del fichero exportado
         */
        fun exportDatabaseFile(context: Context): List<String> {
            try {
                deleteDatabaseFile()
                Thread.sleep(1000)

                copyDataFromOneToAnother(
                    context.getDatabasePath(DATABASE_NAME).path,
                    Environment.getExternalStorageDirectory().path + "/Download/" + "backup_" + DATABASE_NAME
                )
                copyDataFromOneToAnother(
                    context.getDatabasePath(DATABASE_NAME + "-shm").path,
                    Environment.getExternalStorageDirectory().path + "/Download/" + "backup_" + DATABASE_NAME + "-shm"
                )
                copyDataFromOneToAnother(
                    context.getDatabasePath(DATABASE_NAME + "-wal").path,
                    Environment.getExternalStorageDirectory().path + "/Download/" + "backup_" + DATABASE_NAME + "-wal"
                )
                return listOf(
                    Environment.getExternalStorageDirectory().path + "/Download/" + "backup_" + DATABASE_NAME,
                    Environment.getExternalStorageDirectory().path + "/Download/" + "backup_" + DATABASE_NAME + "-shm",
                    Environment.getExternalStorageDirectory().path + "/Download/" + "backup_" + DATABASE_NAME + "-wal"
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return listOf()
        }

        fun listAllDatabasesFromDownload(context: Context? = null) : List<String>{
            try {
                val pathDownload = Environment.getExternalStorageDirectory().path + "/Download"
                val directory = File(pathDownload)
                val pathFiles = directory.listFiles().filter { it.name.contains("backup") }.map { it.path }
                return pathFiles
            }catch (e : java.lang.Exception){
                e.printStackTrace()
            }

            return listOf()
        }

        fun importDatabaseFile(context: Context) {
            try {
                copyDataFromOneToAnother(
                    Environment.getExternalStorageDirectory().path + "/Download/" + "backup_" + DATABASE_NAME,
                    context.getDatabasePath(DATABASE_NAME).path
                )
                copyDataFromOneToAnother(
                    Environment.getExternalStorageDirectory().path + "/Download/" + "backup_" + DATABASE_NAME + "-shm",
                    context.getDatabasePath(DATABASE_NAME + "-shm").path
                )
                copyDataFromOneToAnother(
                    Environment.getExternalStorageDirectory().path + "/Download/" + "backup_" + DATABASE_NAME + "-wal",
                    context.getDatabasePath(DATABASE_NAME + "-wal").path
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun copyDataFromOneToAnother(fromPath: String, toPath: String) {
            val inStream = File(fromPath).inputStream()
            val outStream = FileOutputStream(toPath)

            inStream.use { input ->
                outStream.use { output ->
                    input.copyTo(output)
                }
            }
        }

    }
}
