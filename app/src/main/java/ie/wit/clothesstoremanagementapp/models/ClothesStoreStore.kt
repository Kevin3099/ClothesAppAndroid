package ie.wit.clothesstoremanagementapp.models

interface ClothesStoreStore {
    fun findAll(): List<ClothesStoreModel>
    fun create(clothes: ClothesStoreModel)
    fun update(clothes: ClothesStoreModel)
}