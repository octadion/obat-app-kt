package com.octadion.tugas_akhir.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.FirebaseDatabase
import com.octadion.tugas_akhir.R
import com.octadion.tugas_akhir.model.Vitamin

class AddFragment2 : Fragment(), View.OnClickListener {
    private lateinit var etNamaVitamin: EditText
    private lateinit var etHarga2: EditText
    private lateinit var etKeterangan2: EditText
    private lateinit var btnSave2: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etNamaVitamin=view.findViewById(R.id.etNamaVitamin)
        etHarga2=view.findViewById(R.id.etHarga2)
        etKeterangan2=view.findViewById(R.id.etKeterangan2)
        btnSave2=view.findViewById(R.id.btnSave2)
        btnSave2.setOnClickListener ( this )
    }

    override fun onClick(v: View?) {
        saveData()
    }

    private fun saveData() {
        val namaVitamin = etNamaVitamin.text.toString().trim()
        val harga2 = etHarga2.text.toString().trim()
        val keterangan2 = etKeterangan2.text.toString().trim()
        if(namaVitamin.isEmpty()){
            etNamaVitamin.error="Isi nama Vitamin!"
            return
        }
        if(harga2.isEmpty()){
            etHarga2.error="Isi harga!"
            return
        }
        if(keterangan2.isEmpty()){
            etHarga2.error="Isi keterangan!"
            return
        }
        val ref = FirebaseDatabase.getInstance("https://tugas-akhir-fc254-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Vitamin")
        val obtId = ref.push().key
        val obt = Vitamin(obtId!!, namaVitamin, harga2, keterangan2)
        if(obtId!=null) {
            ref.child(obtId).setValue(obt).addOnCompleteListener {
                Toast.makeText(activity, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}