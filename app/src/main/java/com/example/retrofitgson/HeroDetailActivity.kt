package com.example.retrofitgson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_hero_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class HeroDetailActivity : AppCompatActivity() {

    private var heroId = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hero_detail)

        textViewNameHero.text = intent.getStringExtra(NAME_HERO_KEY)
        val imageUrl = intent.getStringExtra(IMAGE_HERO_KEY)


        Picasso.with(this)
            .load(imageUrl)
            .into(imageViewHero)

        heroId = intent.getStringExtra(ID_HERO_KEY)?.toInt()!!

        getSuperHeroFeatures(heroId)
    }

    private fun getSuperHeroFeatures(Id: Int?) {
        val retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response =
                    retroInstance.getHeroStats(RetroInstance.BASEURL + Id + getString(R.string.symbol_text) + TYPE_KEY)
                        .awaitResponse()

                if (response.isSuccessful) {
                    val jsonObject = response.body()!!.string()

                    val gson = GsonBuilder().create()

                    val powerstats = gson.fromJson(jsonObject, StatsModel::class.java)

                    withContext(Dispatchers.Main) {
                        textViewIntelligence.text = powerstats.intelligence.toString()
                        textViewStrength.text = powerstats.strength.toString()
                        textViewSpeed.text = powerstats.speed.toString()
                        textViewDurability.text = powerstats.durability.toString()
                        textViewPower.text = powerstats.power.toString()
                        textViewCombat.text = powerstats.combat.toString()
                    }

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.error_text),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    companion object {
        const val TYPE_KEY = "powerstats"
        const val NAME_HERO_KEY = "nameHero"
        const val ID_HERO_KEY = "idHero"
        const val IMAGE_HERO_KEY = "imageHero"
    }


}
