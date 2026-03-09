fun main(){
    println("===== Sistem Penilaian =====")

    print("\nMasukkan Nama Mahasiswa: ")
    val nama = readLine()

    print("Masukkan nilai UTS (0-100): ")
    val UTS = readLine()!!.toDouble()

    print("Masukkan nilai UAS (0-100): ")
    val UAS = readLine()!!.toDouble()

    print("Masukkan nilai tugas (0-100): ")
    val tugas = readLine()!!.toDouble()

    if(UTS !in 0.0..100.0 || UAS !in 0.0..100.0 || tugas !in 0.0..100.0){
        println("\nerror: nilai harus 0-100)")
        return
    }

    val nilaiAkhir = (UTS*0.3) + (UAS*0.4) + (tugas*0.3)
    val nilaiInt = nilaiAkhir.toInt()
    val grade = when (nilaiInt){
        in 85..100 -> "A"
        in 70..84 -> "B"
        in 60..69 -> "C"
        in 50..59 -> "D"
        in 0..49 -> "E"
        else -> "tidak valid"
    }

    val keterangan = when (grade){
        in "A" -> "Sangat Baik"
        in "B" -> "Baik"
        in "C" -> "Cukup"
        in "D" -> "Kurang"
        in "E" -> "Sangat Kurang"
        else -> "-"
    }

    val status = if(nilaiAkhir >= 60) "LULUS" else "TIDAK LULUS"

    println("\n===== Hasil Penilaian =====")
    println("Nama       : $nama")
    println("Nilai UTS  : ${UTS.toInt()} (Bobot 30%)")
    println("Nilai UAS  : ${UAS.toInt()} (Bobot 40%)")
    println("Nilai Tugas: ${tugas.toInt()} (Bobot 30%)")
    println("--------------------------------")
    println("Nilai Akhir: $nilaiAkhir")
    println("Grade      : $grade")
    println("Keterangan : $keterangan")
    println("Status     : $status")

    if (status == "LULUS"){
        println("\nSelamat! Anda dinyatakan LULUS!")
    } else {
        println("\nMaaf Anda TIDAK LULUS. Coba Lagi Tahun Depan.")
    }
}