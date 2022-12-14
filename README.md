
# Get Contacts

If you want to reach the phone numbers in the phone book on your phone from your application, it will be enough to follow the steps below.
## Permission Request code



```kotlin
private val PERMISSIONS_REQUEST_READ_CONTACTS = 100

```

## Is there permission or not?

Function that checks whether the directory access permission is granted.
 If the user has given permission before, it will fetch direct phone numbers.
 If permission is not granted, permission is asked.

```kotlin
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
```
## Displaying a permission message to the user

If the user has not given permission, the relevant message is displayed to get permission. If the user gives permission, the phone numbers in the directory are retrieved.

```kotlin
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
```

## Get Contacts
Code block that pulls phone numbers in the directory into the application
```kotlin
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
```






## ???? Links
[![linkedin](https://img.shields.io/badge/linkedin-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/muhammed-furkan-kucuk-40897111a/)
[![google](https://img.shields.io/badge/google-0A66C2?style=for-the-badge&logo=google&logoColor=orange)](https://g.dev/mfurkankck)
