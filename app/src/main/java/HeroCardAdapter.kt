package com.example.andr_dev_application

import HeroModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HeroCardAdapter(private val heroes: List<HeroModel>) : RecyclerView.Adapter<HeroCardAdapter.HeroViewHolder>() {

    class HeroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView = view.findViewById(R.id.user_name)
        val userDescription: TextView = view.findViewById(R.id.user_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hero_card, parent, false)
        return HeroViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        val hero = heroes[position]
        holder.userName.text = hero.firstName
        holder.userDescription.text = hero.lastName
    }

    override fun getItemCount(): Int {
        return heroes.size
    }
}

