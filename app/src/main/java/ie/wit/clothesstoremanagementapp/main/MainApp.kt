package ie.wit.clothesstoremanagementapp.main

import android.app.Application
import ie.wit.clothesstoremanagementapp.models.ClothesStoreJSONStore
import ie.wit.clothesstoremanagementapp.models.ClothesStoreStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var clothess: ClothesStoreStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        clothess = ClothesStoreJSONStore(applicationContext)
        i("ClothesStore started")
    }
}