package com.octadion.tugas_akhir.model

data class Vitamin(
    var id: String,
    var namaVitamin: String,
    var harga2: String,
    var keterangan2: String
){constructor():this("", "", "","")}