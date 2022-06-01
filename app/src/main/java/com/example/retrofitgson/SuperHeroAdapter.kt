package com.example.retrofitgson

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

interface HeroActionListener {
    fun onHeroDetails(heroModel: HeroModel)
}

class SuperHeroAdapter(
    private val actionListener: HeroActionListener,
    private var categories: List<HeroModel>
) : RecyclerView.Adapter<SuperHeroAdapter.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setListData(categories: List<HeroModel>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(category: HeroModel) {
            val imageHero = itemView.findViewById<ImageView>(R.id.imageViewPhotoHero)
            val nameHero = itemView.findViewById<TextView>(R.id.textViewHeroName)

            nameHero.text = category.name

            Picasso.with(itemView.context).load(category.image.url).fit().centerCrop()
                .into(imageHero)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.item_hero, parent, false
        )
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categories[position])
        val heroIdModel = categories[position]
        holder.itemView.setOnClickListener {
            actionListener.onHeroDetails(heroIdModel)
        }
    }
}
