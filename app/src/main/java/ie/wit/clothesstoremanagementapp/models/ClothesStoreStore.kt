package ie.wit.clothesstoremanagementapp.models

import android.content.Intent

interface ClothesStoreStore {
    fun loadFromFirebase()
    fun findAll(): List<ClothesStoreModel>
    fun findOne(id: Long): ClothesStoreModel?
    fun findByTitle(title: String): ClothesStoreModel?
    fun filterByType(clothingType: String): List<ClothesStoreModel>
    fun filterByPrice(lowPrice: Double, highPrice: Double): List<ClothesStoreModel>
    fun create(clothing: ClothesStoreModel)
    fun update(clothing: ClothesStoreModel)
    fun delete(clothing: ClothesStoreModel)
}