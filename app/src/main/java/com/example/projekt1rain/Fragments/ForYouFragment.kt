package com.example.projekt1rain.Fragments

//import com.example.projekt1rain.Adapter.ForYouAdapter
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.projekt1rain.Adapter.ForYouAdapter
import com.example.projekt1rain.DataStorag.DataService
import com.example.projekt1rain.InterFaces.CallBack
import com.example.projekt1rain.InterFaces.FragmentCallBack
import com.example.projekt1rain.InterFaces.RemoveCallBack
import com.example.projekt1rain.MainActivity
import com.example.projekt1rain.MyApp
import com.example.projekt1rain.R
import com.example.projekt1rain.RetrofitApi.retrofitOneCallResponse
import com.example.projekt1rain.RetrofitApi.retrofitOneCallrefreshResponse
import com.example.projekt1rain.Room.Favorites
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.DecimalFormat
import java.util.concurrent.Executors

class ForYouFragment() : Fragment(), CallBack, FragmentCallBack, RemoveCallBack {
    var recyclerViewAdapter: ForYouAdapter? = null
    private lateinit var forYouAdapter: ForYouAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currentLocation: Location
    private var favorites: List<Favorites> = ArrayList()
    val FINE_LOCATION_REQUEST = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.recylerviewforyou1, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        setToolbar()
        getLocation()
        val dataService: DataService = (requireActivity().application as MyApp).dataService

        val swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        swipeRefresh.setOnRefreshListener {
            refreshForYou()
            Toast.makeText(requireContext(), getString(R.string.refresh), Toast.LENGTH_LONG)
                .show()
            forYouAdapter.notifyDataSetChanged()
            swipeRefresh.isRefreshing = false
        }

        forYouAdapter = ForYouAdapter(

            forYouConstructorList = ArrayList(), requireContext(),
            this, this
        )

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerviewforyou)
        recyclerView.apply {
            adapter = forYouAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)

        }
        dataService.getFavorites(this)

        forYouAdapter.notifyDataSetChanged()

        val floatingButton: FloatingActionButton = view.findViewById(R.id.flab)
        floatingButton.setOnClickListener() {
            startMapViewFragment()
        }

    }

    private fun setToolbar() {
        val actionBar: androidx.appcompat.app.ActionBar? =
            (requireActivity() as MainActivity).supportActionBar
        actionBar?.apply {
            title = (getString(R.string.foryou))
            setDisplayShowTitleEnabled(true)
            setHomeButtonEnabled(false)
            setDisplayHomeAsUpEnabled(false)
        }
    }

    private fun startMapViewFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, MapViewFragment())
            .addToBackStack(null)
            .commit()
    }

    //get the list for swipeToRefresh

    private fun refreshForYou() {
        val dataService: DataService = (requireActivity().application as MyApp).dataService
        dataService.getFavorites(this)
        favorites.forEach { favorite ->
            val lat = favorite.currentWeatherResponse?.lat
            val lon = favorite.currentWeatherResponse?.lon
            val adress = favorite.address
            retrofitOneCallrefreshResponse(lat!!, lon!!, adress)
            forYouAdapter.notifyDataSetChanged()

        }
    }

    //get the list from the database
    override fun getFavoritesList(favorites: List<Favorites>) {
        requireActivity().runOnUiThread(java.lang.Runnable {
            forYouAdapter.updateFavList(favorites)
            forYouAdapter.notifyDataSetChanged()

        })

    }


    override fun onDetailPage(favorites: Favorites) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        val blankFragmentDetailPage = DetailFragment()
        val bundle = Bundle()
        bundle.putSerializable("weatherkey", favorites)
        blankFragmentDetailPage.setArguments(bundle)
        transaction.replace(R.id.container, blankFragmentDetailPage)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    override fun onRemove(favorites: Favorites) {
        val dataService: DataService = (requireActivity().application as MyApp).dataService
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.entfernen))
            .setNeutralButton(R.string.cancelButton) { dialogInterface, i ->
            }
            .setNegativeButton(R.string.delete) { dialogInterface, i ->
                Executors.newSingleThreadExecutor()
                    .execute {
                        dataService.deleteFavorites(favorites, this)
                        dataService.getFavorites(this)
                    }
            }
            .create().show()
        forYouAdapter.notifyDataSetChanged()
    }


    // ask for permisson when is guranteed get the currentlocation and make a call from the api
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ): Unit {
        if (requestCode == MapViewFragment.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val dataService: DataService = (requireActivity().application as MyApp).dataService

                getLocation();

            }
        }
    }

    private fun getLocation() {
        val dataService: DataService = (requireActivity().application as MyApp).dataService

        if (ActivityCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                MapViewFragment.LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {

        }

        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            val dataService: DataService = (requireActivity().application as MyApp).dataService

            if (location != null) {
                currentLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                val currentAddress = getAddress(location.latitude, location.longitude)
                Log.d("TAG", "klagenfurt : ${currentAddress}")
                retrofitOneCallResponse(location.latitude, location.longitude, currentAddress)
                dataService.getFavorites(this)

            }
        }

    }


    private fun getAddress(lat: Double, lng: Double): String {
        val df = DecimalFormat()

        df.maximumFractionDigits = 3
        val geocoder = Geocoder(requireContext())
        val list = geocoder.getFromLocation(lat, lng, 1)
        if (list != null && list.size > 0) {
            list.forEach {
                if (it.locality != null && it.locality.isNotEmpty()) {
                    return it.locality;
                }
            }

        }
        return ""

    }


}
