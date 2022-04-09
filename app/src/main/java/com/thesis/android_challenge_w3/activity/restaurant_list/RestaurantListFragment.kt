package com.thesis.android_challenge_w3.activity.restaurant_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.thesis.android_challenge_w3.R
import com.thesis.android_challenge_w3.databinding.FragmentRestaurantListBinding
import com.thesis.android_challenge_w3.model.Restaurant



class RestaurantListFragment : AppCompatActivity(){
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var menu: Menu
    private lateinit var viewModel: RestaurantListViewModel
    private lateinit var binding: ActivityRestaurantListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModelBinding()
        setupToolbar()
        setupRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()
        restaurantAdapter.listener = null
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_restaurant,menu)
        this.menu = menu!!
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_list_switch -> {
                val isLinearSwitched: Boolean = restaurantAdapter.toggleItemViewType()

                if(isLinearSwitched){
                    binding.rvRestaurantList.layoutManager = LinearLayoutManager(this)
                    menu[0].icon = ContextCompat.getDrawable(this,R.drawable.ic_grid)
                } else {
                    binding.rvRestaurantList.layoutManager = GridLayoutManager(this,2)
                    menu[0].icon = ContextCompat.getDrawable(this,R.drawable.ic_linear)

                }
            }
        }
        return true
    }

    private fun setupViewModelBinding() {
        viewModel = ViewModelProvider(this).get(RestaurantListViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_restaurant_list)
        binding.lifecycleOwner = this
        binding.restaurantListViewModel = viewModel

        viewModel.fetchRestaurantList().observe(this, Observer {
            if(it.isNotEmpty()){
                restaurantAdapter.submitList(it)
            } else {
                showToastMessage("Can't find any restaurant")
            }
        })

    }

    private fun setupToolbar(){
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Restaurants"
    }

    private fun setupRecyclerView(){
        restaurantAdapter = RestaurantAdapter()
        restaurantAdapter.listener = object : RestaurantAdapter.RestaurantAdapterListener {
            override fun onItemClicked(restaurant: Restaurant) {
                showToastMessage(restaurant.name)
            }
        }
        binding.rvRestaurantList.adapter = restaurantAdapter
    }

    private fun showToastMessage(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
}