package com.service.gpsservice

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class GpsService : Service()
        , GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener {

    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocationRequest: LocationRequest? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        init()
        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        disconnect()
    }

    private fun init(){
        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build()
        }
        if (mGoogleApiClient != null) {
            mGoogleApiClient!!.connect()
        }

        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 1500
        mLocationRequest!!.fastestInterval = 5000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    override fun onConnected(p0: Bundle?) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, locCallback, null)
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

    private fun disconnect(){
        mGoogleApiClient!!.disconnect()
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locCallback)
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}