package com.octadion.tugas_akhir.fragment
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.octadion.tugas_akhir.R

import com.octadion.tugas_akhir.adapter.VitaminAdapter
import com.octadion.tugas_akhir.model.Obat

import com.octadion.tugas_akhir.model.Vitamin
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.fragment_home.*


class CategoryFragment : Fragment() {
    private lateinit var ref : DatabaseReference
    private lateinit var vitaminRecyclerview : RecyclerView
    private lateinit var vitaminArrayList : ArrayList<Vitamin>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabAdd2.setOnClickListener {
            val actionAdd2 = CategoryFragmentDirections.actionAdd2()
            Navigation.findNavController(it).navigate(actionAdd2)
        }
        vitaminRecyclerview = view.findViewById(R.id.recycler_view2)
        vitaminRecyclerview.layoutManager = LinearLayoutManager(activity)
        vitaminRecyclerview.setHasFixedSize(true)
        vitaminArrayList = arrayListOf<Vitamin>()
        getVitaminData()

    }


    private fun getVitaminData() {
        val ref = FirebaseDatabase.getInstance("https://tugas-akhir-fc254-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Vitamin")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                vitaminArrayList.clear()
                if(snapshot.exists()){
                    for(vitaminSnapshot in snapshot.children){
                        val vitamin = vitaminSnapshot.getValue(Vitamin::class.java)
                        vitaminArrayList.add(vitamin!!)
                    }
                    vitaminRecyclerview.adapter = VitaminAdapter(vitaminArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
    private fun createButtonClickObservable2(): Observable<String> {
        return Observable.create { emitter ->
            searchButton2.setOnClickListener {
                emitter.onNext(searchText2.text.toString())
            }
            emitter.setCancellable {
                searchButton2.setOnClickListener(null)
            }
        }
    }
    override fun onStart() {
        super.onStart()
        val searchTextObservable = createButtonClickObservable2()
        searchTextObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { search2(it)  }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

    }
    fun search2(query: String){
        Log.d(ContentValues.TAG, "search: $query")
        if(query.isEmpty()){
            Toast.makeText(activity, "Harap isi kolom pencarian!", Toast.LENGTH_SHORT).show()
        } else {
            val firebaseSearchQuery =
                FirebaseDatabase.getInstance("https://tugas-akhir-fc254-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("Vitamin").orderByChild("namaVitamin").startAt(query).endAt(query + "\uf8ff")
            firebaseSearchQuery.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    vitaminArrayList.clear()
                    if (snapshot.exists()) {
                        for (vitaminSnapshot in snapshot.children) {
                            val vitamin = vitaminSnapshot.getValue(Vitamin::class.java)
                            vitaminArrayList.add(vitamin!!)
                        }
                        vitaminRecyclerview.getAdapter()?.notifyDataSetChanged()
                    } else {
                        Toast.makeText(activity, "Data tidak ditemukan!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

    }
}