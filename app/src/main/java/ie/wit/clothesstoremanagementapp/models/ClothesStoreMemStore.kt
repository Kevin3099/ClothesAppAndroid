package ie.wit.clothesstoremanagementapp.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class ClothesStoreMemStore : ClothesStoreStore {

    val clothess = ArrayList<ClothesStoreModel>()

    override fun findAll(): List<ClothesStoreModel> {
        return clothess
    }

    override fun create(clothes: ClothesStoreModel) {
        clothes.id = getId()
        clothess.add(clothes)
        logAll()
    }

    override fun update(clothes: ClothesStoreModel) {
        val foundClothesStore: ClothesStoreModel? = clothess.find { p -> p.id == clothes.id }
        if (foundClothesStore != null) {
            foundClothesStore.title = clothes.title
            foundClothesStore.description = clothes.description
            foundClothesStore.image = clothes.image
            foundClothesStore.lat = clothes.lat
            foundClothesStore.lng = clothes.lng
            foundClothesStore.zoom = clothes.zoom
            logAll()
        }
    }

    private fun logAll() {
        clothess.forEach { i("$it") }
    }
}