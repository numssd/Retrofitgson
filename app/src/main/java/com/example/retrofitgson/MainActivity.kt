package com.example.retrofitgson

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.retrofitgson.RetroInstance.Companion.BASEURL
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class MainActivity : AppCompatActivity() {
    private lateinit var adapterHero: SuperHeroAdapter
    private var heroList = ArrayList<HeroModel>()
    private var post: Int? = 1
    private var amount: Int? = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerViewHero()
        recyclerViewHero.adapter = adapterHero
        getSuperHeroGeneral(post, amount)

        recyclerViewHero!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    getSuperHeroGeneral(post, amount)
                }
            }
        })
    }

    private fun initRecyclerViewHero() {
        heroList = ArrayList()
        recyclerViewHero?.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)

            adapterHero = SuperHeroAdapter(object : HeroActionListener {
                override fun onHeroDetails(heroModel: HeroModel) {
                    val intent = Intent(this@MainActivity, HeroDetailActivity::class.java)
                    intent.putExtra(ID_HERO_KEY, heroModel.id.toString())
                    intent.putExtra(NAME_HERO_KEY, heroModel.name)
                    intent.putExtra(IMAGE_HERO_KEY, heroModel.image.url)
                    startActivity(intent)
                }
            }, heroList)
            adapter = adapterHero

            val decoration = DividerItemDecoration(
                applicationContext,
                StaggeredGridLayoutManager.VERTICAL
            )
            addItemDecoration(decoration)
        }
    }

    private fun getSuperHeroGeneral(post: Int?, amount: Int?) {
        var count = 1
        for (id_superhero in post!!..amount!!) {
            getSuperHeroId(id_superhero, count)
            count++
        }
        this.post = this.post?.plus(5)
        this.amount = this.amount?.plus(5)
    }

    private fun getSuperHeroId(id: Int?, count: Int) {
        progressBar!!.visibility = View.VISIBLE
        val retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)


        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = retroInstance.getHero(BASEURL + id).awaitResponse()
                if (response.isSuccessful) {
                    val dataHero = response.body()!!

                    withContext(Dispatchers.Main) {

                        if (dataHero.response == getString(R.string.success_text)) {
                            heroList.add(
                                HeroModel(
                                    dataHero.response, dataHero.id, dataHero.name,
                                    HeroImage(dataHero.image.url)
                                )
                            )
                            updateList(count)
                        }
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

    private fun updateList(count: Int) {
        if (count == 5) {
            adapterHero.setListData(heroList)
            progressBar!!.visibility = View.GONE
        }
    }

    companion object {
        const val NAME_HERO_KEY = "nameHero"
        const val ID_HERO_KEY = "idHero"
        const val IMAGE_HERO_KEY = "imageHero"
    }
}