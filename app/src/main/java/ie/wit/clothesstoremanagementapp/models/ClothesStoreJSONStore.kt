package ie.wit.clothesstoremanagementapp.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.wit.clothesstoremanagementapp.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "clothess.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
                 .registerTypeAdapter(Uri::class.java, UriParser())
                 .create()
val listType: Type = object : TypeToken<ArrayList<ClothesStoreModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class ClothesStoreJSONStore(private val context: Context) : ClothesStoreStore {

    var clothess = mutableListOf<ClothesStoreModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<ClothesStoreModel> {
        logAll()
        return clothess
    }

    override fun create(clothes: ClothesStoreModel) {
        clothes.id = generateRandomId()
        clothess.add(clothes)
        serialize()
    }


    override fun update(clothes: ClothesStoreModel) {
        // todo
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(clothess, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        clothess = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        clothess.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}