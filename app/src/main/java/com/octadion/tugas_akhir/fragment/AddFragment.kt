package com.octadion.tugas_akhir.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.octadion.tugas_akhir.R
import com.octadion.tugas_akhir.model.Obat

class AddFragment : Fragment(), View.OnClickListener {
    private lateinit var etNamaObat: EditText
    private lateinit var etHarga: EditText
    private lateinit var etKeterangan: EditText
    private lateinit var btnSave: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etNamaObat=view.findViewById(R.id.etNamaObat)
        etHarga=view.findViewById(R.id.etHarga)
        etKeterangan=view.findViewById(R.id.etKeterangan)
        btnSave=view.findViewById(R.id.btnSave)
        btnSave.setOnClickListener ( this )
    }

    override fun onClick(v: View?) {
        saveData()
    }

    private fun saveData() {
        val namaObat = etNamaObat.text.toString().trim()
        val harga = etHarga.text.toString().trim()
        val keterangan = etKeterangan.text.toString().trim()
        if(namaObat.isEmpty()){
            etNamaObat.error="Isi nama obat!"
            return
        }
        if(harga.isEmpty()){
            etHarga.error="Isi harga!"
            return
        }
        if(keterangan.isEmpty()){
            etHarga.error="Isi keterangan!"
            return
        }
        val ref = FirebaseDatabase.getInstance("https://tugas-akhir-fc254-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Obat")
        val obtId = ref.push().key
        val obt = Obat(obtId!!, namaObat, harga, keterangan)
        if(obtId!=null) {
            ref.child(obtId).setValue(obt).addOnCompleteListener {
                Toast.makeText(activity, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}