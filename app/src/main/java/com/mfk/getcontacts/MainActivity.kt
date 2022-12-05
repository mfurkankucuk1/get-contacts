package com.mfk.getcontacts

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView


class MainActivity : AppCompatActivity() {

    private var btnGetContact: MaterialButton? = null
    private var tvContacts: MaterialTextView? = null
    private val PERMISSIONS_REQUEST_READ_CONTACTS = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnGetContact = findViewById(R.id.btnGetContacts)
        tvContacts = findViewById(R.id.tvContacts)

        btnGetContact?.let { btn ->
           btn.setOnClickListener {
               showContacts()
           }
        }
    }

    private fun showContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )

        } else {
          getContacts()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts()
            } else {
                Toast.makeText(
                    this,
                    "Until you grant the permission, we canot display the names",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    @SuppressLint("Range")
    private fun getContacts() {
        val stringBuilder = StringBuilder()
        val cursor =
            contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

        cursor?.let { crs ->
            while (crs.moveToNext()) {
                if (crs.count > 0) {
                    val id = crs.getString(crs.getColumnIndex(ContactsContract.Contacts._ID))
                    val name =
                        crs.getString(crs.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val hasPhoneNumber =
                        crs.getString(crs.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                    if (hasPhoneNumber.toInt() > 0) {
                        val cursor1 = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?",
                            arrayOf(id), null
                        )

                        cursor1?.let { crs1 ->
                            while (crs1.moveToNext()) {
                                val phoneNumber =
                                    crs1.getString(crs1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                stringBuilder.append("Contact : ").append(name)
                                    .append(", Phone Number : ").append(phoneNumber).append("\n\n")
                            }
                            crs1.close()
                        }

                    }
                }
            }
            crs.close()
            tvContacts?.let { tv->
                tv.text = stringBuilder
            }
        }
    }
}