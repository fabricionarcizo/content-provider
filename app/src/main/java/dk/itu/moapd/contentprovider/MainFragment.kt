package dk.itu.moapd.contentprovider

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    companion object {
        const val REQUEST_CODE = 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        load_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK)
            return

        if (requestCode == REQUEST_CODE && data != null) {

            val contactUri = data.data
            val id = showContactInfo(contactUri)
            if (id != -1)
                showContactPhone(id)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun showContactInfo(uri: Uri?): Int {
        val queries = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.HAS_PHONE_NUMBER
        )
        val cursor = requireActivity().contentResolver.query(
            uri!!, queries, null, null, null
        )

        cursor.use {
            if (it?.count == 0)
                return -1

            it?.moveToFirst()
            name_text.setText(it?.getString(1))

            if (it?.getString(2) == "1")
                return it.getString(0).toInt()
        }

        return -1
    }

    /** To access the phone number, you need the user's permission */
    private fun showContactPhone(id: Int) {
        val queries = arrayOf(
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        val cursor = requireActivity().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, queries,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
            null, null
        )

        cursor.use {
            if (it?.count == 0)
                return

            it?.moveToFirst()
            phone_number_text.setText(it?.getString(0))
        }
    }

}
