package ie.wit.clothesstoremanagementapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.clothesstoremanagementapp.databinding.CardClothesBinding
import ie.wit.clothesstoremanagementapp.models.ClothesStoreModel

interface ClothesStoreListener {
    fun onClothesStoreClick(clothes: ClothesStoreModel)
}

class ClothesStoreAdapter constructor(
    private var clothess: List<ClothesStoreModel>,
    private val listener: ClothesStoreListener
) :
        RecyclerView.Adapter<ClothesStoreAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardClothesBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val clothes = clothess[holder.adapterPosition]
        holder.bind(clothes, listener)
    }

    override fun getItemCount(): Int = clothess.size

    class MainHolder(private val binding: CardClothesBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(clothes: ClothesStoreModel, listener: ClothesStoreListener) {
            binding.clothesTitle.text = clothes.title
            binding.description.text = clothes.description
            binding.price.text = clothes.price.toString()
            binding.type.text = clothes.clothingType
            Picasso.get().load(clothes.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onClothesStoreClick(clothes) }
        }
    }
}
