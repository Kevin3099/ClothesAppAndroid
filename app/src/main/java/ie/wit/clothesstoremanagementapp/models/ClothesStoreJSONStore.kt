package ie.wit.clothesstoremanagementapp.models

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.wit.clothesstoremanagementapp.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

//const val JSON_FILE = "clothess.json"
//val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
  //               .registerTypeAdapter(Uri::class.java, UriParser())
    //             .create()
//val listType: Type = object : TypeToken<ArrayList<ClothesStoreModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class ClothesStoreJSONStore(private val context: Context) : ClothesStoreStore {

    var clothess = mutableListOf<ClothesStoreModel>()
    var filteredTypeClothing = mutableListOf<ClothesStoreModel>()
    var filteredPriceClothing = mutableListOf<ClothesStoreModel>()
    val db = Firebase.firestore

    init {
     //   if (exists(context, JSON_FILE)) {
      //      deserialize()
      //  }
        loadFromFirebase()
    }

    override fun loadFromFirebase(): Int {
        var finished = false
        db.collection("Clothing Items")
            .get()
            .addOnSuccessListener { documents ->

                for (document in documents) {

                    var clothing = ClothesStoreModel()
                    Log.d(TAG, "${document.id} => ${document.data}")
                    if(document.getData() != null){
                    clothing.title = document.get("title").toString()
                    clothing.description = document.get("description").toString()
                    clothing.price = document.get("price").toString().toDouble()
                    clothing.clothingType = document.get("type").toString()
                        clothess.add(clothing)
                    }
                }
                finished = true
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
        if(finished){
            return 1
        }
        else{
            return 0
        }
    }

    override fun findAll(): MutableList<ClothesStoreModel> {
        logAll()
        return clothess
    }

    override fun create(clothes: ClothesStoreModel) {
        clothes.id = generateRandomId()
        clothess.add(clothes)

        val clothing = hashMapOf(
            "title" to clothes.title,
            "description" to clothes.description,
            "price" to clothes.price,
            "type" to clothes.clothingType
        )

        db.collection("Clothing Items").document(clothes.id.toString())
            .set(clothing)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

       // serialize()
    }


    override fun update(Clothing: ClothesStoreModel) {
        var foundClothing = findOne(Clothing.id!!)
        if (foundClothing != null) {
            foundClothing.title = Clothing.title
            foundClothing.description = Clothing.description
            foundClothing.price = Clothing.price
            foundClothing.clothingType = Clothing.clothingType
            foundClothing.image = Clothing.image
            foundClothing.lat = Clothing.lat
            foundClothing.lng = Clothing.lng
            foundClothing.zoom = Clothing.zoom

            val clothingItem = db.collection("Clothing Items").document(Clothing.id.toString())

            clothingItem
                .update("title", foundClothing.title,
                    "description", foundClothing.description,
                    "price", foundClothing.price,
                    "type", foundClothing.clothingType)
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
        }


     //   serialize()
    }

    override fun delete(Clothing: ClothesStoreModel) {
        clothess.remove(Clothing)

        db.collection("Clothing Items").document(Clothing.id.toString())
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }

     //   serialize()
    }

    override fun findOne(id: Long): ClothesStoreModel? {
        var foundClothing: ClothesStoreModel? = clothess.find { p -> p.id == id }
        return foundClothing
    }

    override fun findByTitle(title: String): ClothesStoreModel? {
        var foundClothing: ClothesStoreModel? = clothess.find { p -> p.title == title }
       // logger.info { foundClothing }
        return foundClothing
    }

    override fun filterByType(clothingType: String): MutableList<ClothesStoreModel> {
        clothess.forEach {
            if (it.clothingType.equals(clothingType, ignoreCase = true)) {
                filteredTypeClothing.add(it)
           //     logger.info { it }
            }
        }
        return filteredTypeClothing
    }

    override fun filterByPrice(lowPrice: Double, highPrice: Double): MutableList<ClothesStoreModel> {
        clothess.forEach {
            if (it.price in lowPrice..highPrice) {
                filteredPriceClothing.add(it)
            //    logger.info { it }
            }
        }
        return filteredPriceClothing
    }

//    private fun serialize() {
//        val jsonString = gsonBuilder.toJson(clothess, listType)
//        write(context, JSON_FILE, jsonString)
//    }
//
//    private fun deserialize() {
//        val jsonString = read(context, JSON_FILE)
//        clothess = gsonBuilder.fromJson(jsonString, listType)
//    }

    private fun logAll() {
        clothess.forEach { Timber.i("$it") }
    }

}

//class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
//    override fun deserialize(
//        json: JsonElement?,
//        typeOfT: Type?,
//        context: JsonDeserializationContext?
//    ): Uri {
//        return Uri.parse(json?.asString)
//    }
//
//    override fun serialize(
//        src: Uri?,
//        typeOfSrc: Type?,
//        context: JsonSerializationContext?
//    ): JsonElement {
//        return JsonPrimitive(src.toString())
//    }
//}