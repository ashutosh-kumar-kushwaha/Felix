package `in`.silive.felix.recyclerview

import `in`.silive.felix.R
import `in`.silive.felix.module.CategoryResponse
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView

class RecyclerCategoryAdapter(val context : Context, val categories: List<CategoryResponse>, val categoryClickListener: CategoryClickListener) : RecyclerView.Adapter<RecyclerCategoryAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val categoryBtn : AppCompatButton = itemView.findViewById(R.id.categoryBtn)

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                categoryClickListener.onItemClick(position, categories[position].allCategoryName)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.categoryBtn.text = categories[position].allCategoryName
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}