package dev.luizleal.tabuadaglecio.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dev.luizleal.tabuadaglecio.R
import dev.luizleal.tabuadaglecio.model.LeaderboardUser

class LeaderboardUserAdapter :
    RecyclerView.Adapter<LeaderboardUserAdapter.LeaderBoardUserViewHolder>() {
    private var leaderboardUserList = mutableListOf<LeaderboardUser>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderBoardUserViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.leaderboard_item_layout, parent, false)

        return LeaderBoardUserViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return leaderboardUserList.size
    }

    override fun onBindViewHolder(holder: LeaderBoardUserViewHolder, position: Int) {
        val leaderboardUser = leaderboardUserList[position]

        holder.setItem(leaderboardUser, position)
    }

    fun setItems(list: MutableList<LeaderboardUser>) {
        this.leaderboardUserList = list
        notifyDataSetChanged()
    }

    class LeaderBoardUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var username: TextView? = null
        private var userClass: TextView? = null
        private var userPosition: TextView? = null
        private var score: TextView? = null
        private var imageAvatar: ImageView? = null

        fun setItem(data: LeaderboardUser, position: Int) {
            username = itemView.findViewById(R.id.text_username)
            userClass = itemView.findViewById(R.id.text_class)
            userPosition = itemView.findViewById(R.id.text_position_index)
            score = itemView.findViewById(R.id.text_score)
            imageAvatar = itemView.findViewById(R.id.image_user_avatar)

            username?.text = data.username
            userClass?.text = data.userClass
            userPosition?.text = (position + 1).toString()
            score?.text = data.score.toString()

            Picasso.get()
                .load("https://tabuada-de-glecio-admin.vercel.app/img/avatars/${data.avatarId}.png")
                .into(imageAvatar)
        }
    }
}