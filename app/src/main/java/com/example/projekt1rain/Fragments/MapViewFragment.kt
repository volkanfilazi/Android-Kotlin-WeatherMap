package  com.example.projekt1rain.Fragments

import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.projekt1rain.DataStorag.DataService
import com.example.projekt1rain.InterFaces.CallBack
import com.example.projekt1rain.InterFaces.GetName
import com.example.projekt1rain.MainActivity
import com.example.projekt1rain.MyApp
import com.example.projekt1rain.R
import com.example.projekt1rain.RetrofitApi.retrofitResponse
import com.example.projekt1rain.RetrofitApi.retrofitOneCallResponse
import com.example.projekt1rain.Room.City
import com.example.projekt1rain.Room.Favorites
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.mapviewfragment.*
import java.text.DecimalFormat

private const val TAG = "MapViewFragment"

class MapViewFragment : Fragment(), OnMapReadyCallback, CallBack, GetName {
    private var favorites: List<Favorites> = emptyList()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currentLocation: Location

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
        lateinit var nMap: GoogleMap
        private var markers: MutableList<Marker> = mutableListOf<Marker>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        map_view.onCreate(savedInstanceState)
        map_view.onResume()
        map_view.getMapAsync(this)
        setToolbar()
    }

    //when the permisson is granted the map zooms on the Currentlocation
    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        // 1
        nMap.isMyLocationEnabled = true

// 2
        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->

            if (location != null) {
                currentLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                val currentAddress = getAddress(location.latitude, location.longitude)
                Log.d("TAG", "klagenfurt : ${currentAddress}")
                nMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 9f))

            }
        }
    }

    override fun onMapReady(map: GoogleMap?) {

        if (map != null) {
            nMap = map
        }
        nMap.uiSettings.isZoomControlsEnabled = true
        map?.let {
            nMap = it
            nMap.setOnInfoWindowClickListener { markerToDelete ->
                markers.remove(markerToDelete)
                markerToDelete.remove()
                Log.i(TAG, "onWindowsClickListener - Delete Thismarker")
            }
            favorites.forEach { favorite ->
                val lat = favorite.currentWeatherResponse?.lat
                val lon = favorite.currentWeatherResponse?.lon

                if (lat != null && lon != null)
                    nMap.addMarker(
                        MarkerOptions().position(LatLng(lat, lon))
                            .title(favorite.address)
                            .snippet(
                                getString(R.string.temperature) + ": " + favorite.currentWeatherResponse?.current?.temp?.toInt()
                                    ?.minus(
                                        273.15.toInt()
                                    ).toString() + "°C"
                            )

                    )
                setUpMap()
            }


            nMap.setOnMapLongClickListener { latlng ->
                Log.i(TAG, "onMapLongClickListener" + latlng)
                showAlertDialog(latlng)
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


    private fun showAlertDialog(latlng: LatLng) {
        val address = getAddress(latlng.latitude, latlng.longitude)

        val dialog =
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.wouldyou) + address + getString(R.string.tothefavorites) ).setMessage("")
                .setNegativeButton(getString(R.string.abbrechen), null)
                .setPositiveButton(getString(R.string.ok), null)
                .show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val marker = nMap.addMarker(
                MarkerOptions().position(latlng)
            )
            retrofitResponse(address)
            //get the call from RetrofitSetup.class and insert it directly in the Database
            retrofitOneCallResponse(latlng.latitude, latlng.longitude, address)
            markers.add(marker)
            dialog.dismiss()
            Toast.makeText(
                requireContext(),
                getString(R.string.City) + address + getString(R.string.add),
                LENGTH_LONG
            ).show()

        }
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener {
            dialog.dismiss()
        }

    }


    private fun setToolbar() {
        val actionBar: ActionBar? = (requireActivity() as MainActivity).supportActionBar
        actionBar?.apply {
            title = getString(R.string.Kartenansicht)
            setDisplayShowTitleEnabled(true)
            setHomeButtonEnabled(true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.mapviewfragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataService: DataService = (requireActivity().application as MyApp).dataService

        dataService.getFavorites(this)

        val searchView = view.findViewById<SearchView>(R.id.sv_location)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(location: String?): Boolean {
                searchKey(view)
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                searchView.query.isNullOrEmpty()
                return false
            }
        })


    }

    fun searchKey(view: View) {

        lateinit var location: String
        val dataService: DataService = (requireActivity().application as MyApp).dataService
        val searchView = view.findViewById<SearchView>(R.id.sv_location)
        location = searchView?.query.toString()
        dataService.getCitiesFindbyName(location, this)
    }

    override fun getFavoritesList(favorites: List<Favorites>) {
        this.favorites = favorites
    }

    //get the cities from the citylist.json asset
    override fun getCities(city: City?) {

        if (city?.name != null) {
            val latLng = LatLng(city.coord.lat!!, city.coord.lon!!)
            val lat = (city.coord.lat)
            val long = (city.coord.lon)

            nMap.addMarker(MarkerOptions().position(latLng).title(city.name))
            retrofitOneCallResponse(lat, long, city.name)
            Toast.makeText(requireContext(), getString(R.string.hinzufügen), LENGTH_LONG).show()
            nMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 9f))

        } else {
            Toast.makeText(requireContext(), getString(R.string.error), LENGTH_LONG)
                .show()
        }

    }


}
