package ie.wit.clothesstoremanagementapp.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import ie.wit.clothesstoremanagementapp.R
import ie.wit.clothesstoremanagementapp.databinding.ActivityClothesBinding
import ie.wit.clothesstoremanagementapp.helpers.showImagePicker
import ie.wit.clothesstoremanagementapp.main.MainApp
import ie.wit.clothesstoremanagementapp.models.Location
import ie.wit.clothesstoremanagementapp.models.ClothesStoreModel
import timber.log.Timber
import timber.log.Timber.i

class ClothesStoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClothesBinding
    var clothes = ClothesStoreModel()
    lateinit var app: MainApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClothesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        i("ClothesStore Activity started...")

        if (intent.hasExtra("clothes_edit")) {
            edit = true
            clothes = intent.extras?.getParcelable("clothes_edit")!!
            binding.clothesTitle.setText(clothes.title)
            binding.description.setText(clothes.description)
            binding.type.setText(clothes.clothingType)
            binding.price.setText(clothes.price.toString())
            binding.btnAdd.setText(R.string.save_clothes)
            Picasso.get()
                .load(clothes.image)
                .into(binding.clothesImage)
            if (clothes.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_clothes_image)
            }
        }

        binding.btnAdd.setOnClickListener() {
            clothes.title = binding.clothesTitle.text.toString()
            clothes.description = binding.description.text.toString()
            clothes.clothingType = binding.type.text.toString()
            clothes.price = binding.price.text.toString().toDouble()
            if (clothes.title.isEmpty()) {
                Snackbar.make(it,R.string.enter_clothes_title, Snackbar.LENGTH_LONG)
                        .show()
            } else {
                if (edit) {
                    app.clothess.update(clothes.copy())
                } else {
                    app.clothess.create(clothes.copy())
                }
            }
            i("add Button Pressed: $clothes")
            setResult(RESULT_OK)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        binding.clothesLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (clothes.zoom != 0f) {
                location.lat =  clothes.lat
                location.lng = clothes.lng
                location.zoom = clothes.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        registerImagePickerCallback()
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_clothes, menu)
        if (edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                app.clothess.delete(clothes)
                finish()
            }
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            clothes.image = result.data!!.data!!
                            Picasso.get()
                                   .load(clothes.image)
                                   .into(binding.clothesImage)
                            binding.chooseImage.setText(R.string.change_clothes_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            clothes.lat = location.lat
                            clothes.lng = location.lng
                            clothes.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}