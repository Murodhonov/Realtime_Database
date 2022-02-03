package uz.umarxon.realtimedatabase.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.umarxon.realtimedatabase.databinding.ItemRvBinding
import uz.umarxon.realtimedatabase.models.User

class RvAdapter(private val list: List<User>) :
    RecyclerView.Adapter<RvAdapter.Vh>() {
    inner class Vh(var itemRv: ItemRvBinding) : RecyclerView.ViewHolder(itemRv.root) {
        fun onBind(user: User, position: Int) {

            itemRv.tvId.text = user.idToken
            itemRv.tvName.text = user.name
            itemRv.tvNumber.text = user.number
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size
}