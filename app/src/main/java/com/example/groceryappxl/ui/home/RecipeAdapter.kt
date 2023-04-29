package com.example.groceryappxl.ui.home

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryappxl.R
import org.w3c.dom.NameList
import org.w3c.dom.Text
import com.bumptech.glide.Glide


class RecipeAdapter (private val recipeNameList: List<String>, private val recipeImageList: List<String>, private val ingredientsList: List<List<String>>)
    :RecyclerView.Adapter<RecipeAdapter.ViewHolder>()
{
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recipeImage: ImageView
        val recipeTitle: TextView
        val recipeText: TextView
//        val adapter = RecipeAdapter(recipeNameList =  )

        init {
            recipeImage = view.rootView.findViewById(R.id.recipeImage)
            recipeTitle = view.rootView.findViewById(R.id.recipeTitle)
            recipeText = view.rootView.findViewById(R.id.recipeIngredients)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recipeImageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(recipeImageList[position])
            .centerCrop()
            .into(holder.recipeImage)
            holder.recipeTitle.text = recipeNameList[position]
        holder.recipeText.text = ingredients(position)
    }

    fun ingredients(position: Int):String{
        var ingredientString = ""
        for (i in 0 until ingredientsList [position].size) {
            if(i == 0){
                ingredientString += ingredientsList[position][i]
            }
            else {
                ingredientString += "\n" + ingredientsList[position][i]

            }
        }
        return ingredientString
    }


}