package com.example.tp3.BDD
import android.content.Context
import androidx.room.*
import com.example.tp3.Converters.DateConverter
import com.example.tp3.DAO.ReservatioDao
import com.example.tp3.Entites.Reservation

@Database(entities = [Reservation::class], version = 6)
@TypeConverters(DateConverter::class)
abstract class AppBD :RoomDatabase(){
    abstract fun getReservationDao():ReservatioDao
    companion object{
        @Volatile
        private var INSTANCE:AppBD?=null
        fun buildDatabase(context: Context):AppBD?{
            if(INSTANCE==null){
                synchronized(this){
                    INSTANCE=Room.databaseBuilder(context,AppBD::class.java,"app_bd").allowMainThreadQueries().fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }
    }
}