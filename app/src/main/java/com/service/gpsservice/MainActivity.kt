package com.service.gpsservice

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.location.LocationManager
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionRequest


class MainActivity : AppCompatActivity() {

    private lateinit var btnStart: Button
    private lateinit var btnStop: Button

    private var permissionsGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnStart = findViewById(R.id.btn_start)
        btnStop = findViewById(R.id.btn_stop)

        btnStart.setOnClickListener(btnStartOnClick)
        btnStop.setOnClickListener(btnStopOnClick)

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()){
                            permissionsGranted = true
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {

                    }
                }).check()
    }

    private val btnStartOnClick = View.OnClickListener {
        val manager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (permissionsGranted){
            if (manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                val gps = Intent(this,GpsService::class.java)
                startService(gps)
                Toast.makeText(this,"Start service.",Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        }
    }

    private val btnStopOnClick = View.OnClickListener {
        val gps = Intent(this,GpsService::class.java)
        stopService(gps)
    }
}
