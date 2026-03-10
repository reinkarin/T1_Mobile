import kotlin.random.Random

interface Discountable { fun calculateDiscount(price: Double): Double }

data class Product(
    val id: Int,
    val nama: String,
    val harga: Double,
    val kategori: String,
    var stok: Int
) :Discountable {
    override fun calculateDiscount(price: Double): Double = if (price > 1000000) price * 0.25 else 0.0
}

data class Customer(val id: Int, val nama: String, val email: String, val alamat: String? = null)
data class CartItem(val product: Product, var jumlah: Int)

sealed class OrderStatus {
    object Pending : OrderStatus(); object Processing : OrderStatus()
    override fun toString(): String = this::class.simpleName ?: "Pending"
}

sealed class PaymentMethod {
    object Cash : PaymentMethod(); data class Transfer(val bank: String) : PaymentMethod()
    override fun toString(): String = this::class.simpleName ?: "Cash"
}
data class Order(val id: String, val customer: Customer, val items: List<CartItem>, var status: OrderStatus, val paymentMethod: PaymentMethod, val totalHarga: Double)

class ShoppingCart {
    private val _items = mutableListOf<CartItem>()
    val items: MutableList<CartItem> get() = _items

    fun addItem(product: Product, qty: Int) {
        if (product.stok >= qty) {
            val existing = _items.find { it.product.id == product.id }
            if (existing != null) existing.jumlah += qty else _items.add(CartItem(product, qty))
            product.stok -= qty
            println("Berhasil: ${product.nama} masuk keranjang.")
        } else println("!!Gagal!!: Stok tidak cukup!")
    }

    fun removeProductCompletely(productId: Int) {
        val item = _items.find { it.product.id == productId }
        item?.let { it.product.stok += it.jumlah; _items.remove(it); println("Produk sudah dihapus.") }
    }

    fun reduceQuantity(productId: Int, amount: Int) {
        val item = _items.find { it.product.id == productId }
        if (item != null) {
            if (amount >= item.jumlah) removeProductCompletely(productId)
            else { item.jumlah -= amount; item.product.stok += amount; println("Jumlah produk sudah dikurangi.") }
        }
    }
}

class TokoOnline {
    val products = mutableListOf<Product>()
    val orders = mutableListOf<Order>()

    fun initData() {
        products.addAll(listOf(
            Product(1, "Laptop Pro", 15000000.0, "Elektronik", 5),
            Product(2, "Monitor 4K", 4500000.0, "Elektronik", 3),
            Product(3, "Keyboard Mech", 1200000.0, "Aksesoris", 10),
            Product(4, "Mouse Wireless", 300000.0, "Aksesoris", 15)
        ))
    }

    fun printProductTable(items: List<Any>, labelStok: String) {
        println("---------------------------------------------------------------------------")
        println(String.format("| %-3s | %-15s | %-12s | %-12s | %-6s |", "ID", "Nama", "Harga", "Kategori", labelStok))
        println("---------------------------------------------------------------------------")
        items.forEach {
            if (it is Product) println(String.format("| %-3d | %-15s | %-12.0f | %-12s | %-6d |", it.id, it.nama, it.harga, it.kategori, it.stok))
            else if (it is CartItem) println(String.format("| %-3d | %-15s | %-12.0f | %-12s | %-6d |", it.product.id, it.product.nama, it.product.harga, it.product.kategori, it.jumlah))
        }
        println("---------------------------------------------------------------------------")
    }

    fun printReceiptTable(items: List<CartItem>) {
        println("----------------------------------------------------------------------------------")
        println(String.format("| %-10s | %-15s | %-12s | %-15s | %-10s |", "ID Produk", "Nama Produk", "Harga", "Jumlah Barang", "Total"))
        println("----------------------------------------------------------------------------------")
        items.forEach {
            val subTotal = it.product.harga * it.jumlah
            println(String.format("| %-10d | %-15s | %-12.0f | %-15d | %-10.0f |", it.product.id, it.product.nama, it.product.harga, it.jumlah, subTotal))
        }
        println("----------------------------------------------------------------------------------")
    }
}

fun main() {
    val toko = TokoOnline().apply { initData() }
    val cart = ShoppingCart()

    println("=== SELAMAT DATANG DI TOKO REIN ===")
    print("Nama Lengkap   : "); val name = readln()
    print("Alamat Email   : "); val email = readln()
    print("Alamat Rumah   : "); val address = readln()
    val user = Customer(101, name, email, address)

    while (true) {
        println("\n--- MENU UTAMA ---")
        println("1. Daftar Produk\n2. Tambah Keranjang\n3. Kelola Keranjang\n4. Checkout Pilihan\n5. Riwayat\n6. Keluar")
        print("Pilih Menu (1-6): ")
        val menu = readln().trim()

        when (menu) {
            "1" -> {
                println("\n--- DAFTAR PRODUK ---")
                toko.printProductTable(toko.products.sortedBy { it.id }, "Stok")
            }
            "2" -> {
                try {
                    print("ID Produk: "); val id = readln().toInt()
                    print("Jumlah: "); val qty = readln().toInt()
                    toko.products.find { it.id == id }?.let { cart.addItem(it, qty) } ?: println("!!Gagal!!: ID Produk tidak ditemukan.")
                } catch (e: Exception) { println("!!Kesalahan: Input harus berupa angka!!") }
            }
            "3" -> {
                if (cart.items.isEmpty()) println("!!Keranjang kosong!!") else {
                    println("\n--- ISI KERANJANG ---")
                    toko.printProductTable(cart.items.sortedBy { it.product.id }, "Qty")
                    println("1. Kurangi | 2. Hapus | 3. Kembali")
                    print("Pilih Opsi (1/2/3): ")
                    val opsi = readln().trim()
                    when (opsi) {
                        "1" -> try {
                            print("ID Produk: "); val id = readln().toInt()
                            print("Jumlah dikurangi: "); val sub = readln().toInt()
                            cart.reduceQuantity(id, sub)
                        } catch (e: Exception) { println("!Input harus angka!") }
                        "2" -> try {
                            print("ID Produk: "); val id = readln().toInt()
                            cart.removeProductCompletely(id)
                        } catch (e: Exception) { println("!Input harus angka!") }
                        "3" -> continue
                        else -> println("!Opsi '$opsi' tidak valid!")
                    }
                }
            }
            "4" -> {
                if (cart.items.isEmpty()) println("!!Keranjang kosong!!") else {
                    toko.printProductTable(cart.items.sortedBy { it.product.id }, "Qty")
                    println("Instruksi: Ketik ID (contoh: 1, 2), ketik 'all', atau 'back'")
                    print("Pilih ID: ")
                    val input = readln().trim().lowercase()
                    if (input == "back") continue

                    val cartIds = cart.items.map { it.product.id }
                    val selectedIds = if (input == "all") cartIds else {
                        val parsed = input.split(",").mapNotNull { it.trim().toIntOrNull() }
                        if (parsed.isEmpty()) { println("!Kesalahan: Input tidak dikenali."); continue }
                        parsed
                    }

                    val invalidIds = selectedIds.filter { it !in cartIds }
                    if (invalidIds.isNotEmpty()) { println("!!Gagal!!: ID $invalidIds tidak ada di keranjang!"); continue }

                    val finalItems = cart.items.filter { it.product.id in selectedIds }
                    val subtotal = finalItems.sumOf { it.product.harga * it.jumlah }
                    val total = subtotal - (if (subtotal > 1000000) subtotal * 0.1 else 0.0)

                    println("\n--- RINGKASAN PEMBAYARAN ---")
                    finalItems.forEach { println("- ${it.product.nama} x ${it.jumlah}") }
                    println(String.format("Total Bayar: Rp%.0f", total))
                    print("Konfirmasi Bayar? (y/n): ")
                    val confirm = readln().trim().lowercase()
                    if (confirm == "y") {
                        // Membuat ID unik manual tanpa UUID
                        val idOrder = "ORD-${Random.nextInt(1000, 9999)}"
                        toko.orders.add(Order(idOrder, user, finalItems.toList(), OrderStatus.Pending, PaymentMethod.Cash, total))
                        cart.items.removeAll(finalItems); println("🚀 Berhasil! ID: $idOrder")
                    } else if (confirm == "n") {
                        println("!Pembayaran dibatalkan!")
                    } else {
                        println("!Input '$confirm' tidak valid. Transaksi dibatalkan.")
                    }
                }
            }
            "5" -> {
                println("\n--- RIWAYAT TRANSAKSI ---")
                if (toko.orders.isEmpty()) println("Belum ada transaksi.") else {
                    toko.orders.forEach { order ->
                        println("\nuser: ${order.customer.nama}")
                        println("email: ${order.customer.email}")
                        println("alamat: ${order.customer.alamat ?: "-"}")
                        println("\nid user: ${order.customer.id}")
                        toko.printReceiptTable(order.items)
                        println(String.format("total bayar: Rp%.0f", order.totalHarga))
                        println("\nTerimakasih telah berbelanja di toko REIN")
                        println("Semoga harimu menyenangkan dan jangan lupa datang kembali.\n==================================================================================")
                    }
                    println("Tekan Enter untuk kembali..."); readln()
                }
            }
            "6" -> break
            else -> println("!Menu Utama '$menu' tidak valid! Masukkan angka 1-6.")
        }
        println("\n(Klik Enter untuk lanjut)")
        readln()
    }
}