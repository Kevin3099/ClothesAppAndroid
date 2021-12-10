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

    override fun findOne(id: Long): ClothesStoreModel? {
        TODO("Not yet implemented")
    }

    override fun findByTitle(title: String): ClothesStoreModel? {
        TODO("Not yet implemented")
    }

    override fun filterByType(clothingType: String): List<ClothesStoreModel> {
        TODO("Not yet implemented")
    }

    override fun filterByPrice(lowPrice: Double, highPrice: Double): List<ClothesStoreModel> {
        TODO("Not yet implemented")
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
            foundClothesStore.price = clothes.price
            foundClothesStore.clothingType = clothes.clothingType
            foundClothesStore.image = clothes.image
            foundClothesStore.lat = clothes.lat
            foundClothesStore.lng = clothes.lng
            foundClothesStore.zoom = clothes.zoom
            logAll()
        }
    }

    override fun delete(clothing: ClothesStoreModel) {
        clothess.remove(clothing)
    }

    private fun logAll() {
        clothess.forEach { i("$it") }
    }
}