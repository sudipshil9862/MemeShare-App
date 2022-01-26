package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.memeshare.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var currentImageUrl: String? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_main)


        loadMeme()
    }

    private fun loadMeme(){
        // progress bar to show loading
        binding.progressBar.visibility = View.VISIBLE;
        // Instantiate the RequestQueue.
        //val queue = Volley.newRequestQueue(this)  // we made singleton class for that

        val url = "https://meme-api.herokuapp.com/gimme"

        // Request a image from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                //Log.d("success request", response.substring(0,500))
                currentImageUrl = response.getString("url")

                //glide is taking time
                Glide.with(this).load(currentImageUrl).listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar.visibility = View.GONE
                        return false;
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar.visibility = View.GONE
                        return false;
                    }
                }).into(binding.memeImage)
            },
            {
                Toast.makeText(this,"something went wrong",Toast.LENGTH_LONG).show()
            })

        // Add the request to the RequestQueue.
        //queue.add(jsonObjectRequest)
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain" //it will show the apps to share .. if u write jpg than it will share only image apps
        intent.putExtra(Intent.EXTRA_TEXT,"hey ! checkout this cool meme i got from meme $currentImageUrl")
        //where to send(chooser)
        val chooser = Intent.createChooser(intent,"share this meme using...")
        startActivity(chooser)
    }
    fun nextMeme(view: View) {
        loadMeme();
    }
}