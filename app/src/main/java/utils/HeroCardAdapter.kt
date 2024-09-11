package utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.andr_dev_application.R

class HeroCardAdapter(
    private var heroes: MutableList<HeroModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_HERO = 1
    private val VIEW_TYPE_NO_MORE_CARDS = 2

    override fun getItemViewType(position: Int): Int {
        return if (heroes[position].isPlaceholder) VIEW_TYPE_NO_MORE_CARDS else VIEW_TYPE_HERO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HERO) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.hero_card, parent, false)
            HeroViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.no_more_card, parent, false)
            NoMoreCardsViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeroViewHolder) {
            val hero = heroes[position]
            holder.userName.text = hero.firstName
            holder.userDescription.text = hero.lastName
            Glide.with(holder.itemView.context).load(hero.imageUrl).into(holder.userImage)
        }

    }

    override fun getItemCount(): Int = heroes.size

    fun removeHeroAt(position: Int) {
        if (position < heroes.size) {
            heroes.removeAt(position)
            notifyItemRemoved(position)
            if (heroes.isEmpty()) {

                heroes.add(HeroModel(0, "", "", "", "", "", "", "", true))
                notifyItemInserted(0)
            }
        }
    }

    fun getHeroAt(position: Int): HeroModel {
        return heroes[position]
    }

    class HeroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView = view.findViewById(R.id.hero_name)
        val userDescription: TextView = view.findViewById(R.id.hero_last_name)
        val userImage: ImageView = view.findViewById(R.id.hero_image)
    }

    class NoMoreCardsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }
}