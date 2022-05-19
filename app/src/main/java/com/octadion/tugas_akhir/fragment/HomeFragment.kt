package com.octadion.tugas_akhir.fragment

import android.app.AlertDialog
import android.content.ContentValues.TAG
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
import com.octadion.tugas_akhir.adapter.ObatAdapter
import com.octadion.tugas_akhir.model.Obat
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    private lateinit var ref : DatabaseReference
    private lateinit var obatRecyclerview : RecyclerView
    private lateinit var obatArrayList : ArrayList<Obat>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabAdd.setOnClickListener {
            val actionAdd = HomeFragmentDirections.actionAdd()
            Navigation.findNavController(it).navigate(actionAdd)
        }
        obatRecyclerview = view.findViewById(R.id.recycler_view)
        obatRecyclerview.layoutManager = LinearLayoutManager(activity)
        obatRecyclerview.setHasFixedSize(true)
        obatArrayList = arrayListOf<Obat>()
        getObatData()

    }

    private fun getObatData() {
        val ref = FirebaseDatabase.getInstance("https://tugas-akhir-fc254-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Obat")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                obatArrayList.clear()
                if(snapshot.exists()){
                    for(obatSnapshot in snapshot.children){
                        val obat = obatSnapshot.getValue(Obat::class.java)
                        obatArrayList.add(obat!!)
                    }
                    obatRecyclerview.adapter = ObatAdapter(obatArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
    private fun createButtonClickObservable(): Observable<String> {
        return Observable.create { emitter ->
            searchButton.setOnClickListener {
                emitter.onNext(searchText.text.toString())
            }
            emitter.setCancellable {
                searchButton.setOnClickListener(null)
            }
        }
    }
    override fun onStart() {
        super.onStart()
        val searchTextObservable = createButtonClickObservable()
        searchTextObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { search(it)  }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

    }
    fun search(query: String){
            Log.d(TAG, "search: $query")
        if(query.isEmpty()){
            Toast.makeText(activity, "Harap isi kolom pencarian!", Toast.LENGTH_SHORT).show()
        } else {
            val firebaseSearchQuery =
                FirebaseDatabase.getInstance("https://tugas-akhir-fc254-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("Obat").orderByChild("namaObat").startAt(query)
                    .endAt(query + "\uf8ff")
            firebaseSearchQuery.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    obatArrayList.clear()
                    if (snapshot.exists()) {
                        for (obatSnapshot in snapshot.children) {
                            val obat = obatSnapshot.getValue(Obat::class.java)
                            obatArrayList.add(obat!!)
                        }
                        obatRecyclerview.getAdapter()?.notifyDataSetChanged()
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

