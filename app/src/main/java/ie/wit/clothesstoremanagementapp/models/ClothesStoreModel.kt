package ie.wit.clothesstoremanagementapp.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClothesStoreModel(var id: Long = 0,
                             var clothingType: String = "",
                             var price: Double = 0.00,
                             var title: String = "",
                             var description: String = "",
                             var image: Uri = Uri.EMPTY,
                             var lat : Double = 0.0,
                             var lng: Double = 0.0,
                             var zoom: Float = 0f) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable