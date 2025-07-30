package yusufs.turan.blogsite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import yusufs.turan.blogsite.databinding.GonderiItemBinding
import yusufs.turan.blogsite.model.Gonderi

class GonderiAdapter(private val gonderiListesi : ArrayList<Gonderi>) : RecyclerView.Adapter<GonderiAdapter.GonderiHolder>() {

    class GonderiHolder(val binding: GonderiItemBinding) :RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GonderiHolder {
        val binding = GonderiItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GonderiHolder(binding)
    }

    override fun getItemCount(): Int {
        return gonderiListesi.size
    }

    override fun onBindViewHolder(holder: GonderiHolder, position: Int) {
        holder.binding.recyclerEmailText.text =gonderiListesi[position].email
        holder.binding.recyclerBaslikText.text =gonderiListesi[position].baslik
        holder.binding.recyclerCommentText.text =gonderiListesi[position].yorum
        Picasso.get().load(gonderiListesi[position].gorselUrl).into(holder.binding.recyclerImageview)
    }
}