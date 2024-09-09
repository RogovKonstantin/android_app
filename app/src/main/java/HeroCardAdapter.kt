package com.example.andr_dev_application

import HeroModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class HeroCardAdapter(private val heroes: List<HeroModel>) : RecyclerView.Adapter<HeroCardAdapter.HeroViewHolder>() {

    class HeroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView = view.findViewById(R.id.user_name)
        val userDescription: TextView = view.findViewById(R.id.user_description)
        val userImage: ImageView = view.findViewById(R.id.user_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hero_card, parent, false)
        return HeroViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        val hero = heroes[position]
        holder.userName.text = hero.firstName
        holder.userDescription.text = hero.lastName

        // Load the hero's image using Glide
        Glide.with(holder.itemView.context)
            .load(hero.imageUrl) // Assuming imageUrl contains the correct URL to the image
            .into(holder.userImage)
    }

    override fun getItemCount(): Int {
        return heroes.size
    }
}
