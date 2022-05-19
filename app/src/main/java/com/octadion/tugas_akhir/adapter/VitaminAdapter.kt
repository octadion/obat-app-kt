package com.octadion.tugas_akhir.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.octadion.tugas_akhir.R
import com.octadion.tugas_akhir.model.Vitamin


class VitaminAdapter(private val vitaminList : ArrayList<Vitamin>) : RecyclerView.Adapter<VitaminAdapter.MyViewHolder>() {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.itemvitamin,
            parent, false
        )
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = vitaminList[position]

        holder.namaVitamin.text = currentitem.namaVitamin
        holder.harga2.text = currentitem.harga2
        holder.keterangan2.text = currentitem.keterangan2
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

    private fun showUpdateDialog(currentitem: Vitamin) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Edit Data")
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.update_dialog2, null)
        val etNamaVitamin = view.findViewById<EditText>(R.id.etUpdateNamaVitamin)
        val etHarga2 = view.findViewById<EditText>(R.id.etUpdateHarga2)
        val etKeterangan2 = view.findViewById<EditText>(R.id.etUpdateKeterangan2)
        etNamaVitamin.setText(currentitem.namaVitamin)
        etHarga2.setText(currentitem.harga2)
        etKeterangan2.setText(currentitem.keterangan2)

        builder.setView(view)
        builder.setPositiveButton("Update"){p0,p1 ->
            val dbVitamin = FirebaseDatabase.getInstance("https://tugas-akhir-fc254-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Vitamin")
            val namavitamin = etNamaVitamin.text.toString().trim()
            val harga2 = etHarga2.text.toString().trim()
            val keterangan2 = etKeterangan2.text.toString().trim()
            if(namavitamin.isEmpty()){
                etNamaVitamin.error="Nama vitamin harus diisi"
                etNamaVitamin.requestFocus()
                return@setPositiveButton
            }
            if(harga2.isEmpty()){
                etHarga2.error="Harga harus diisi"
                etHarga2.requestFocus()
                return@setPositiveButton
            }
            if(keterangan2.isEmpty()){
                etKeterangan2.requestFocus()
                return@setPositiveButton
            }
            val currentitem = Vitamin(currentitem.id, namavitamin, harga2, keterangan2)
            dbVitamin.child(currentitem.id!!).setValue(currentitem)
            Toast.makeText(context,"Data berhasil diupdate", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){ p0,p1 ->
        }
        val alert = builder.create()
        alert.show()
    }
    private fun showDetailDialog(currentitem: Vitamin) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Detail Data")
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.detail_dialog2, null)
        val etNamaVitamin = view.findViewById<TextView>(R.id.etNamaVitamin)
        val etHarga2 = view.findViewById<TextView>(R.id.etHarga2)
        val etKeterangan2 = view.findViewById<TextView>(R.id.etKeterangan2)
        etNamaVitamin.setText(currentitem.namaVitamin)
        etHarga2.setText(currentitem.harga2)
        etKeterangan2.setText(currentitem.keterangan2)

        builder.setView(view)

        builder.setNegativeButton("Back"){ p0,p1 ->
        }
        val alert = builder.create()
        alert.show()
    }
    private fun showDeleteDialog(currentitem: Vitamin) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete Data")
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.delete_dialog2, null)
        val etNamaVitamin = view.findViewById<TextView>(R.id.etNamaVitamin)
        val etHarga2 = view.findViewById<TextView>(R.id.etHarga2)
        val etKeterangan2 = view.findViewById<TextView>(R.id.etKeterangan2)
        etNamaVitamin.setText(currentitem.namaVitamin)


        builder.setView(view)
        builder.setPositiveButton("Delete"){p0,p1 ->
            val dbVitamin = FirebaseDatabase.getInstance("https://tugas-akhir-fc254-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Vitamin").child(currentitem.id)
            dbVitamin.removeValue()

            Toast.makeText(context,"Data berhasil dihapus", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Cancel"){ p0,p1 ->
        }
        val alert = builder.create()
        alert.show()
    }
    override fun getItemCount(): Int {

        return vitaminList.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val namaVitamin: TextView = itemView.findViewById(R.id.tvNamaVitamin)
        val harga2: TextView = itemView.findViewById(R.id.tvHarga2)
        val keterangan2: TextView = itemView.findViewById(R.id.tvKeterangan2)
        val edit: ImageButton = itemView.findViewById(R.id.ibEdit2)
        val detail: ImageButton = itemView.findViewById(R.id.ibDetail2)
        val delete: ImageButton = itemView.findViewById(R.id.ibDelete2)
    }
}