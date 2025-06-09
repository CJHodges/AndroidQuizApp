package uk.ac.aber.dcs.cs31620.quizapp.datasource

import android.content.Context

object Injection {

    fun getDatabase(context: Context): RoomDatabaseI =
        FaaPersistentRoomDatabase.getDatabase(context)!!
}