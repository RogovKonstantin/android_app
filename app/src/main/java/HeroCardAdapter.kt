package com.example.andr_dev_application

import HeroModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class HeroCardAdapter(
    private var heroes: MutableList<HeroModel>
) : RecyclerView.Adapter<HeroCardAdapter.HeroViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hero_card, parent, false)
        return HeroViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        val hero = heroes[position]
        holder.userName.text = hero.firstName
        holder.userDescription.text = hero.lastName
        Glide.with(holder.itemView.context).load(hero.imageUrl).into(holder.userImage)
    }

    override fun getItemCount(): Int = heroes.size

    fun removeHeroAt(position: Int) {
        if (position < heroes.size) {
            heroes.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getHeroAt(position: Int): HeroModel {
        return heroes[position]
    }

    class HeroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView = view.findViewById(R.id.user_name)
        val userDescription: TextView = view.findViewById(R.id.user_description)
        val userImage: ImageView = view.findViewById(R.id.user_image)
    }
}
