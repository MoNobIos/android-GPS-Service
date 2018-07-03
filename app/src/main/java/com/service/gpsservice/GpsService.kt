package com.service.gpsservice

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*

class GpsService : Service(){

    private var mLocationRequest: LocationRequest? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        init()
        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
    }

    private fun init(){
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 1500
        mLocationRequest!!.fastestInterval = 5000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient!!.requestLocationUpdates(mLocationRequest, locCallback, null)
        }
    }

    private val locCallback = object : LocationCallback() {
        override fun onLocationResult(locRes: LocationResult?) {
            super.onLocationResult(locRes)
            val str = "Location " + locRes!!.lastLocation.latitude + "," + locRes.lastLocation.longitude
            Log.e("onLocationResult", str)
            Toast.makeText(this@GpsService,str,
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopLocationUpdates(){
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locCallback)
    }
}