package com.example.groceryappxl.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestHeaders
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.groceryappxl.databinding.FragmentHomeBinding
import okhttp3.Headers
import org.json.JSONArray

class HomeFragment : Fragment() {

    private lateinit var recipeNameList: MutableList<String>
    private lateinit var recipeImageList: MutableList<String>
    private lateinit var preferencesList: MutableList<String>
    private lateinit var ingredientStorage: MutableList<List<String>>
    private var _binding: FragmentHomeBinding? = null
    private val apiKey = "?apiKey=f46430a89c5f4a9ba1a2dd27290b9765"

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val searchView: SearchView = binding.searchBar
        recipeImageList = mutableListOf()
        recipeNameList = mutableListOf()
        preferencesList = mutableListOf()
        ingredientStorage = mutableListOf()
        val url = "https://api.spoonacular.com/recipes/complexSearch"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                getSpoonAcular(query, url)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle search query text change
                return true
            }
        })

        return root



    }

    private fun getSpoonAcular(query: String?, url: String){
        val client = AsyncHttpClient()
        val fullUrl = "$url$apiKey&query=$query&instructionsRequired=true&number=20&addRecipeNutrition=false&fillIngredients=true"
        val params =  RequestParams();

        val requestHeaders = RequestHeaders()
        requestHeaders["Content-Type"] = "application/json"

        //requestHeaders, params
        client[fullUrl, requestHeaders, params, object:
            JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.d("JSON", "Request Failed $response")

            }

            override fun onSuccess(
                statusCode: Int,
                headers: Headers?,
                json: JsonHttpResponseHandler.JSON
            ) {

                Log.d("Status Code", "$statusCode")
                if (recipeNameList.isNotEmpty()) {
                    recipeNameList.clear()
                    recipeImageList.clear()
                    ingredientStorage.clear()
                }

                Log.d("JSON Response", "$json")
                var ingredientsArray: JSONArray
                val recipeJSON = json.jsonObject.getJSONArray("results")


                //get all the ingredients
                ingredientsArray = recipeJSON.getJSONObject(0).getJSONArray("missedIngredients")

                //seperate all the ingredients
                seperateIngredients(recipeJSON)

                //get name of recipe
                getName(recipeJSON)

                //get image of recipe
                getImage(recipeJSON)

            }
        }]
    }

    fun seperateIngredients(ingredientsArray : JSONArray){
        val ingredientNames = mutableListOf<String>()

        for(i in 0 until ingredientsArray.length()) {
            val currentRecipeIngredients = ingredientsArray.getJSONObject(i).getJSONArray("missedIngredients")
            ingredientNames.clear()
            for (i in 0 until currentRecipeIngredients.length()) {
                val ingredient = currentRecipeIngredients.getJSONObject(i)
                val name = ingredient.getString("original")
                ingredientNames.add(name)
            }
            ingredientStorage.add(ingredientNames)

        }



        Log.d("Ingredients", "$ingredientStorage")


    }

    fun getName(recipeArray: JSONArray){

        for (i in 0 until recipeArray.length()) {
            val ingredient = recipeArray.getJSONObject(i)
            val name = ingredient.getString("title")
            recipeNameList.add(name)
        }

        Log.d("Recipe Names", recipeNameList.toString())
    }

    fun getImage(recipeArray: JSONArray){
        for (i in 0 until recipeArray.length()) {
            val ingredient = recipeArray.getJSONObject(i)
            val image = ingredient.getString("image")
            recipeImageList.add(image)
        }

        Log.d("Image List", recipeImageList.toString())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

