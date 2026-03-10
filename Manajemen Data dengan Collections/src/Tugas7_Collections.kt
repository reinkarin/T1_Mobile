data class NilaiMahasiswa(
    val nim: String,
    val nama: String,
    val mataKuliah: String,
    val nilai: Int
)

fun getGrade(nilaiInt: Int): String {
    return when (nilaiInt) {
        in 85..100 -> "A"
        in 70..84 -> "B"
        in 60..69 -> "C"
        in 50..59 -> "D"
        in 0..49 -> "E"
        else -> "tidak valid"
    }
}

fun main() {

    val listMahasiswa = listOf(
        NilaiMahasiswa("F1D0231001", "Kim Yeonchae", "Pemrograman", 76),
        NilaiMahasiswa("F1D0231002", "Lee Mark", "Pemrograman", 85),
        NilaiMahasiswa("F1D0231003", "Kim Jongin", "Pemrograman", 79),
        NilaiMahasiswa("F1D0231004", "Choi Beomgyu", "Pemrograman", 45),
        NilaiMahasiswa("F1D0231005", "Park Chanyeol", "Pemrograman", 75),
        NilaiMahasiswa("F1D0231006", "Lee Hyuna", "Pemrograman", 88),
        NilaiMahasiswa("F1D0231007", "Lee Yeongji", "Pemrograman", 55),
        NilaiMahasiswa("F1D0231008", "Lisa Manoban", "Pemrograman", 72),
        NilaiMahasiswa("F1D0231009", "Shin Ryujin", "Pemrograman", 62),
        NilaiMahasiswa("F1D0231010", "Kim Minjeong", "Pemrograman", 60)
    )

    println("===== DATA NILAI MAHASISWA =====")
    println("\nNo  NIM           Nama             MataKuliah      Nilai")

    listMahasiswa.forEachIndexed { index, mhs ->
        println("${(index + 1).toString().padEnd(3)} ${mhs.nim.padEnd(13)} " +
                "${mhs.nama.padEnd(16)} ${mhs.mataKuliah.padEnd(15)} ${mhs.nilai}")
    }

    val rataRata = listMahasiswa.map { it.nilai }.average()
    val tertinggi = listMahasiswa.maxByOrNull { it.nilai }
    val terendah = listMahasiswa.minByOrNull { it.nilai }

    println("\n===== STATISTIK =====")
    println("Total Mahasiswa : ${listMahasiswa.size}")
    println("Rata-rata Nilai : $rataRata")
    println("Nilai Tertinggi : ${tertinggi?.nilai} (${tertinggi?.nama})")
    println("Nilai Terendah  : ${terendah?.nilai} (${terendah?.nama})")

    println("\n===== MAHASISWA LULUS =====")
    val lulus = listMahasiswa.filter { it.nilai >= 70 }
    lulus.forEachIndexed { i, mhs ->
        println("${i + 1}. ${mhs.nama} - ${mhs.nilai} (${getGrade(mhs.nilai)})")
    }

    val groupGrade = listMahasiswa.groupBy { getGrade(it.nilai) }

    println("\n===== JUMLAH PER GRADE =====")
    listOf("A", "B", "C", "D", "E").forEach { g ->
        val jumlah = groupGrade[g]?.size ?: 0
        println("Grade $g: $jumlah mahasiswa")
    }

    // MEMBUAT RANKING BERDASARKAN NILAI TERTINGGI
    val ranking = listMahasiswa.sortedByDescending { it.nilai }

    println("\n===== SISTEM PENCARIAN MAHASISWA =====")
    println("Ketik nama mahasiswa untuk mencari data")
    println("Ketik 'selesai' untuk keluar\n")

    while (true) {

        print("Masukkan nama: ")
        val input = readLine()

        if (input.equals("selesai", ignoreCase = true)) {
            println("Program selesai.")
            break
        }

        val hasil = ranking.find { it.nama.equals(input, ignoreCase = true) }

        if (hasil != null) {

            val peringkat = ranking.indexOf(hasil) + 1

            println("\nHasil Pencarian")
            println("Nama      : ${hasil.nama}")
            println("Peringkat : $peringkat")
            println("Nilai     : ${hasil.nilai}")
            println("Grade     : ${getGrade(hasil.nilai)}\n")

        } else {
            println("Mahasiswa tidak ditemukan\n")
        }
    }
}