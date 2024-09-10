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
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_HERO = 1
    private val VIEW_TYPE_NO_MORE = 2

    override fun getItemViewType(position: Int): Int {
        return if (heroes[position].isPlaceholder) VIEW_TYPE_NO_MORE else VIEW_TYPE_HERO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HERO) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.hero_card, parent, false)
            HeroViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.no_more_card, parent, false)
            NoMoreViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeroViewHolder) {
            val hero = heroes[position]
            holder.userName.text = hero.firstName
            holder.userDescription.text = hero.lastName
            Glide.with(holder.itemView.context).load(hero.imageUrl).into(holder.userImage)
        } else if (holder is NoMoreViewHolder) {
            holder.placeholderText.text = "No more heroes nearby"
        }
    }

    override fun getItemCount(): Int = heroes.size

    fun removeHeroAt(position: Int) {
        if (position < heroes.size) {
            heroes.removeAt(position)
            notifyItemRemoved(position)
            if (heroes.isEmpty()) {
                heroes.add(HeroModel(0, "", "", "No more heroes nearby", "", "", "", "", true))
                notifyItemInserted(0)
            }
        }
    }

    fun getHeroAt(position: Int): HeroModel {
        return heroes[position]
    }

    class HeroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView = view.findViewById(R.id.hero_name)
        val userDescription: TextView = view.findViewById(R.id.user_description)
        val userImage: ImageView = view.findViewById(R.id.user_image)
    }

    class NoMoreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeholderText: TextView = view.findViewById(R.id.placeholder_text)
    }
}