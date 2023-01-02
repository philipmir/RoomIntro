package com.example.roomintro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.room.Room
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job : Job
    private lateinit var db: AppDatabase
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        job = Job()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "shopping-items"
        )
            .fallbackToDestructiveMigration()
            .build()

        val item1 = Item(0, "banan", false, "frukt")
        val item2 = Item(0, "mjöl", false, "kyl")
        val item3 = Item(0, "ost", false, "kyl")

//        saveItem(item1)   //Read in this when code working
//        saveItem(item2)
//        saveItem(item3)


//        val list = loadAllItems()
        val list = loadByCategory("kyl")

        launch {
            val itemList = list.await()

            for ( item in itemList) {
                Log.d("!!!", "item: $item")

        }


    }


    fun saveItem(item: Item) {
        launch(Dispatchers.IO) {
            db.itemDao().insert(item)
        }
    }

    fun loadAllItems(): Deferred<List<Item>> =
        async(Dispatchers.IO){
            db.itemDao().getAll()
        }
    }


    fun loadByCategory(category: String) =
        async(Dispatchers.IO) {
            db.itemDao().findByCategory(category)
        }

    fun delete(item : Item) {
        launch(Dispatchers.IO) {
            db.itemDao().delete(item)
        }
    }



}