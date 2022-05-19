package com.octadion.tugas_akhir.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.octadion.tugas_akhir.R
import com.octadion.tugas_akhir.model.Obat

class ObatAdapter(private val obatList : ArrayList<Obat>) : RecyclerView.Adapter<ObatAdapter.MyViewHolder>() {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item,
            parent, false
        )
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = obatList[position]

        holder.namaObat.text = currentitem.namaObat
        holder.harga.text = currentitem.harga
        holder.keterangan.text = currentitem.keterangan
        holder.edit.setOnClickListener {
            showUpdateDialog(currentitem)
        }
        holder.detail.setOnClickListener {
            showDetailDialog(currentitem)
        }
        holder.delete.setOnClickListener {
            showDeleteDialog(currentitem)
        }

    }

    private fun showUpdateDialog(currentitem: Obat) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Edit Data")
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.update_dialog, null)
        val etNamaObat = view.findViewById<EditText>(R.id.etUpdateNamaObat)
        val etHarga = view.findViewById<EditText>(R.id.etUpdateHarga)
        val etKeterangan = view.findViewById<EditText>(R.id.etUpdateKeterangan)
        etNamaObat.setText(currentitem.namaObat)
        etHarga.setText(currentitem.harga)
        etKeterangan.setText(currentitem.keterangan)

        builder.setView(view)
        builder.setPositiveButton("Update"){p0,p1 ->
            val dbObat = FirebaseDatabase.getInstance("https://tugas-akhir-fc254-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Obat")
            val namaobat = etNamaObat.text.toString().trim()
            val harga = etHarga.text.toString().trim()
            val keterangan = etKeterangan.text.toString().trim()
            if(namaobat.isEmpty()){
                etNamaObat.error="Nama obat harus diisi"
                etNamaObat.requestFocus()
                return@setPositiveButton
            }
            if(harga.isEmpty()){
                etHarga.error="Harga harus diisi"
                etHarga.requestFocus()
                return@setPositiveButton
            }
            if(keterangan.isEmpty()){
                etKeterangan.requestFocus()
                return@setPositiveButton
            }
            val currentitem = Obat(currentitem.id, namaobat, harga, keterangan)
            dbObat.child(currentitem.id!!).setValue(currentitem)
            Toast.makeText(context,"Data berhasil diupdate", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){ p0,p1 ->
        }
        val alert = builder.create()
        alert.show()
    }
    private fun showDetailDialog(currentitem: Obat) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Detail Data")
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.detail_dialog, null)
        val etNamaObat = view.findViewById<TextView>(R.id.etNamaObat)
        val etHarga = view.findViewById<TextView>(R.id.etHarga)
        val etKeterangan = view.findViewById<TextView>(R.id.etKeterangan)
        etNamaObat.setText(currentitem.namaObat)
        etHarga.setText(currentitem.harga)
        etKeterangan.setText(currentitem.keterangan)

        builder.setView(view)

        builder.setNegativeButton("Back"){ p0,p1 ->
        }
        val alert = builder.create()
        alert.show()
    }
    private fun showDeleteDialog(currentitem: Obat) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete Data")
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.delete_dialog, null)
        val etNamaObat = view.findViewById<TextView>(R.id.etNamaObat)
        val etHarga = view.findViewById<TextView>(R.id.etHarga)
        val etKeterangan = view.findViewById<TextView>(R.id.etKeterangan)
        etNamaObat.setText(currentitem.namaObat)


        builder.setView(view)
        builder.setPositiveButton("Delete"){p0,p1 ->
            val dbObat = FirebaseDatabase.getInstance("https://tugas-akhir-fc254-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Obat").child(currentitem.id)
            dbObat.removeValue()

            Toast.makeText(context,"Data berhasil dihapus", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Cancel"){ p0,p1 ->
        }
        val alert = builder.create()
        alert.show()
    }
    override fun getItemCount(): Int {

        return obatList.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val namaObat: TextView = itemView.findViewById(R.id.tvNamaObat)
        val harga: TextView = itemView.findViewById(R.id.tvHarga)
        val keterangan: TextView = itemView.findViewById(R.id.tvKeterangan)
        val edit: ImageButton = itemView.findViewById(R.id.ibEdit)
        val detail: ImageButton = itemView.findViewById(R.id.ibDetail)
        val delete: ImageButton = itemView.findViewById(R.id.ibDelete)
    }

}