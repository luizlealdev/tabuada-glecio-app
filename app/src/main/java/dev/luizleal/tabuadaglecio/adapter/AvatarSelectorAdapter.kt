package dev.luizleal.tabuadaglecio.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dev.luizleal.tabuadaglecio.R
import dev.luizleal.tabuadaglecio.content.SecurityPreferences
import dev.luizleal.tabuadaglecio.model.AvatarItem

class AvatarSelectorAdapter(private val context: Context, var progressBar: ProgressBar) :
    RecyclerView.Adapter<AvatarSelectorAdapter.AvatarSelectorViewHolder>() {

    private var avatarIdList = mutableListOf<AvatarItem>()
    private var selectedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarSelectorViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.avatar_item_layout, parent, false)
        return AvatarSelectorViewHolder(itemView, progressBar)
    }

    override fun onBindViewHolder(holder: AvatarSelectorViewHolder, position: Int) {
        val avatarId = avatarIdList[position]
        Log.d("avatar pos", avatarId.toString())
        holder.setItem(avatarId, position == selectedPosition)
        holder.itemView.setOnClickListener {
            // Atualize a posição selecionada e notifique a mudança
            val previousPosition = selectedPosition
            selectedPosition = position

            SecurityPreferences(context).storeString("avatarId", avatarId.id.toString())
            Log.d("click", avatarId.id.toString())

            // Notifique o adapter para atualizar a exibição dos itens antigos e novos
            notifyItemChanged(previousPosition)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return avatarIdList.size
    }

    fun setItems(list: MutableList<AvatarItem>) {
        this.avatarIdList = list
        notifyDataSetChanged()
    }

    class AvatarSelectorViewHolder(itemView: View, private val progressBar: ProgressBar) : RecyclerView.ViewHolder(itemView) {
        private var avatarImage: ImageView? = null

        fun setItem(data: AvatarItem, isSelected: Boolean) {
            avatarImage = itemView.findViewById(R.id.image_avatar)
            Picasso.get()
                .load("https://tabuada-de-glecio-admin.vercel.app/img/avatars/${data.id}.png")
                .into(avatarImage, object : Callback {
                    override fun onSuccess() {
                        progressBar.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        Toast.makeText(
                            itemView.context,
                            "Falha ao carregar os avatares, verifique sua conexão com a internet",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                })

            // Atualize o background de acordo com o estado selecionado
            if (isSelected) {
                avatarImage?.setBackgroundResource(R.drawable.avatar_selected_indicator_style)
            } else {
                avatarImage?.setBackgroundResource(0) // Remove o background se não estiver selecionado
            }
        }
    }
}
