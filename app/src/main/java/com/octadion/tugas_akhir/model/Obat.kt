package com.octadion.tugas_akhir.model

data class Obat(
    var id: String,
    var namaObat: String,
    var harga: String,
    var keterangan: String
){constructor():this("", "", "","")}