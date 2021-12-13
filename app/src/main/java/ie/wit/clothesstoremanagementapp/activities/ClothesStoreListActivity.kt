package ie.wit.clothesstoremanagementapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.clothesstoremanagementapp.R
import ie.wit.clothesstoremanagementapp.adapters.ClothesStoreAdapter
import ie.wit.clothesstoremanagementapp.adapters.ClothesStoreListener
import ie.wit.clothesstoremanagementapp.databinding.ActivityClothesListBinding
import ie.wit.clothesstoremanagementapp.main.MainApp
import ie.wit.clothesstoremanagementapp.models.ClothesStoreModel

class ClothesStoreListActivity : AppCompatActivity(), ClothesStoreListener/*, MultiplePermissionsListener*/ {

    lateinit var app: MainApp
    private lateinit var binding: ActivityClothesListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClothesListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        loadClothesStores()
        registerRefreshCallback()
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, ClothesStoreActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
            R.id.item_map -> {
                val launcherIntent = Intent(this, ClothesStoreMapsActivity::class.java)
                mapIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClothesStoreClick(clothes: ClothesStoreModel) {
        val launcherIntent = Intent(this, ClothesStoreActivity::class.java)
        launcherIntent.putExtra("clothes_edit", clothes)
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadClothesStores() }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }

    private fun loadClothesStores() {
        showClothesStores(app.clothess.findAll())
    }

    fun showClothesStores (clothess: List<ClothesStoreModel>) {
        binding.recyclerView.adapter = ClothesStoreAdapter(clothess, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }


}