package dk.itu.moapd.contentprovider

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    companion object {
        private const val ALL_PERMISSIONS_RESULT = 1011
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val permissions: ArrayList<String> = ArrayList()
        permissions.add(Manifest.permission.READ_CONTACTS)

        val permissionsToRequest = permissionsToRequest(permissions)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (permissionsToRequest.size > 0)
                requestPermissions(
                    permissionsToRequest.toTypedArray(),
                    ALL_PERMISSIONS_RESULT
                )

        val fragment = supportFragmentManager
            .findFragmentById(R.id.fragment_main)
        if (fragment == null)
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_main, MainFragment())
                .commit()
    }

    private fun permissionsToRequest(
        permissions: ArrayList<String>): ArrayList<String> {

        val result: ArrayList<String> = ArrayList()
        for (permission in permissions)
            if (!hasPermission(permission))
                result.add(permission)

        return result
    }

    private fun hasPermission(permission: String) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        else
            true

}
