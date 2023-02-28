package com.meghamlabs.ecomadminapp

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.meghamlabs.ecomadminapp.databinding.DetailsPageBinding

class DetailsActivity : AppCompatActivity(){


    private lateinit var binding: DetailsPageBinding
    private  var imageUri: Uri = Uri.EMPTY
    private lateinit var pickMedia: ActivityResultLauncher<String>
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailsPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pickMedia = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri
                Glide.with(this).load(imageUri).into(binding.imageBtn)

            }
        }

        binding.imageBtn.setOnClickListener {
                selectImageFromGallery()
        }
        binding.saveBtn.setOnClickListener {
//            println("sdsjdsbjds"+imageUri)
//            Log.d("imageUri value",imageUri.toString())
            val productImage = imageUri
            val productName = binding.prdName.text.toString()
            val productPrice = binding.prdPrice.text.toString()
            uploadDataToFirebase(productImage,productName,productPrice)
        }


    }

    private fun selectImageFromGallery(){

        pickMedia.launch("image/*")


    }



   private fun uploadDataToFirebase(
       productImage: Uri,
       productName: String,
       productPrice: String
   ) {

       database = Firebase.database.reference
       val productId = database.child("products").push().key

       val storageRef = Firebase.storage.reference.child("product_images").child(productId!!)
       storageRef.putFile(productImage)

           .addOnSuccessListener { taskSnapshot ->
               // Get the download URL of the uploaded image
               storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                   // Create a Product object with the download URL and other details
                   val product = Product(downloadUri.toString(), productName, productPrice)
                   // Save the product to the Firebase Realtime Database
                   database.child("products").child(productId).setValue(product)

                       .addOnSuccessListener {
                           Toast.makeText(
                               this,
                               "uploaded successfully to db check db",
                               Toast.LENGTH_LONG
                           ).show()
                           finish()
                       }
                       .addOnFailureListener {
                           Log.e("Error uploading", "failed to upload to firebase", it)
                       }


               }


           }
   }
}


