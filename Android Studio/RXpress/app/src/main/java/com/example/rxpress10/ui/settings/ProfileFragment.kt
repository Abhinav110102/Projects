package com.example.rxpress10.ui.settings

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Messenger
import android.os.ParcelFileDescriptor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.rxpress10.R
import com.example.rxpress10.databinding.ActivityProfileFragmentBinding
import com.example.rxpress10.model.Picture
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_edit_profile_fragment.*
import kotlinx.android.synthetic.main.activity_profile_fragment.*
import java.io.FileDescriptor
import java.io.IOException


class ProfileFragment : Fragment(com.example.rxpress10.R.layout.activity_profile_fragment) {

    private lateinit var binding: ActivityProfileFragmentBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    val contentResolver = context?.contentResolver
    var SELECT_PICTURE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileFragmentBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Get uId from firebase authentication
        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid
        //Get database instance of pic info
        database = FirebaseDatabase.getInstance().getReference("users/" + userId + "/PicInfo")

        database.get().addOnSuccessListener {
            //If there is not any info do nothing
            //Otherwise get the uri and set the profile picture and catch any errors
            if (it.childrenCount.toInt() == 0) {

            } else {
                val uri = Uri.parse(it.child("picture").value.toString())
                try {
                    val inputStream = activity?.contentResolver?.openInputStream(uri)
                    val drawable = Drawable.createFromStream(inputStream, uri.toString())
                    binding.profilePicture.background = drawable
                } catch(e: Exception) {

                }
            }
        }

        //Setting the information from the database
        setInfo()

        //Listen for the user clicking the edit profile button
        binding.editProfile.setOnClickListener { editProfileClicked() }
        //Listen for the user clicking the change profile picture button
        binding.changeProfilePicture.setOnClickListener { editPicture() }

        return binding.root
    }

    @Throws(IOException::class)
    fun setPic(uri: Uri): Bitmap {
        val parcelFileDescriptor: ParcelFileDescriptor? =
            contentResolver?.openFileDescriptor(uri, "r")
        val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
        val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        if (parcelFileDescriptor != null) {
            parcelFileDescriptor.close()
        }
        return image
    }

    //Sets the information that the users has in the database
    fun setInfo(){
        auth= FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("users/"+userId+"/UserInfo")

        database.get().addOnSuccessListener {
            //Get the name, email, date of birth, gender, and pronouns from the database and set the texts on the screen
            val info = it.childrenCount.toInt()
            binding.profileName.text = getString(com.example.rxpress10.R.string.userName, it.child("firstName").value.toString(), it.child("lastName").value.toString())
            binding.emailAddress.text = it.child("email").value.toString()
            binding.dob.text = getString(com.example.rxpress10.R.string.dob, it.child("dob").value.toString())
            binding.gender.text = getString(com.example.rxpress10.R.string.gender, it.child("gender").value.toString())
            binding.pronouns.text = getString(com.example.rxpress10.R.string.pronouns, it.child("pronouns").value.toString())
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

    //Navigate to edit profile fragment
    fun editProfileClicked() {
        val action = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment()
        binding.editProfile.findNavController().navigate(action)
    }

    //Creates a picture instance
    fun createPicture(
        picture: String
    ): Picture {
        // reads the data input on each text input and assigns it to a variable

        // creates an object named medication with the user input
        return Picture(
            picture
        )
    }

    //Shows a chooser for images for the user to select from their device
    fun editPicture() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            //action = Intent.ACTION_GET_CONTENT
            type = "image/*"
        }
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE)
    }

    //Sets the picture selected by the user as the profile picture
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid
        database = FirebaseDatabase.getInstance().getReference("users/" + userId + "/PicInfo")
        if (resultCode == RESULT_OK) {
            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                val selectedImageUri: Uri? = data?.data
                if (null != selectedImageUri) {
                    val contentResolver = context?.contentResolver

                    //Persists permissions to image through phone restarts but does not persist through
                    //Uninstalling the app
                    val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    // Check for the freshest data.
                    contentResolver?.takePersistableUriPermission(selectedImageUri, takeFlags)
                    val inputStream = activity?.contentResolver?.openInputStream(selectedImageUri)
                    val drawable = Drawable.createFromStream(inputStream, selectedImageUri.toString())
                    //Set the uri into the database under Pic Info
                    database.get().addOnSuccessListener {
                        val picInfo = createPicture(
                            selectedImageUri.toString()
                        )
                        database.setValue(picInfo).addOnSuccessListener {

                            binding.profilePicture.background = drawable

                            Toast.makeText(
                                context,
                                "Successfully Saved",
                                Toast.LENGTH_SHORT
                            ).show()
                        }.addOnFailureListener {
                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                        // update the preview image in the layout
                }
            }
        }
    }
}