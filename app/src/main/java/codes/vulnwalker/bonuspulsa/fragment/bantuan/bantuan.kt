package codes.vulnwalker.bonuspulsa.fragment.bantuan

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.Window
import codes.vulnwalker.bonuspulsa.R
import kotlinx.android.synthetic.main.activity_bantuan.*
import kotlinx.android.synthetic.main.toolbar.*

class bantuan : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bantuan)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        if(intent.getStringExtra("jenis").toString().equals("Video")){
            titleBantuan.text = "Tonton Video"
            deskripsiBantuan.text = "Tonton video adalah fitur aplikasi ini untuk mendapatkan point. \n" +
                    "Point akan didapat setelah pengguna menonton video hingga selesai dan mengklik tautan video. \n" +
                    "Jika salah satu syarat di atas terlewat maka point tidak akan bertambah. \n" +
                    "Point yang didapat dalam fitur ini antara 20 - 65 point. \n" +
                    "Batas limit pemutaran video yaitu 10 menit sekali / chanel iklan."
        }else if(intent.getStringExtra("jenis").toString().equals("Klik")){
            titleBantuan.text = "Klik Iklan"
            deskripsiBantuan.text = "Klik dan ikuti arah iklan untuk mendapatkan point . \n" +
                    "Point akan bertambah setelah iklan ditutup serta tautan iklan terlah diikuti \n" +
                    "Jika salah satu syarat di atas terlewat maka point tidak akan bertambah. \n" +
                    "Point yang didapat dalam fitur ini antara 20 - 40 point. \n" +
                    "Batas limit pemutaran video yaitu 3 menit sekali"
        }else if(intent.getStringExtra("jenis").toString().equals("Absen")){
            titleBantuan.text = "Absen Harian"
            deskripsiBantuan.text = "Dapatkan point secara acak antara 100 - 500 point.\n" +
                    "Absen harian hanya dapat dilakukan 1 kali / hari \n"
        }else if(intent.getStringExtra("jenis").toString().equals("Penukaran")){
            titleBantuan.text = "Penukaran Point"
            deskripsiBantuan.text = "Anda dapat menukarkan point dengan pulsa sesuai nominal yang telah ditentukan.\n" +
                    "Dengan menu tukar point, anda dapat mengajukan penukaran kepada kami. \n" +
                    "Pulsa akan diperoleh bila penukaran telah diverifikasi oleh kami. \n"
        }else if(intent.getStringExtra("jenis").toString().equals("Suspend")){
            titleBantuan.text = "Suspend Account"
            deskripsiBantuan.text = "Dengan menggunakan aplikasi ini berarti anda haru patuh pada peraturan.\n" +
                    "Jika kami mendapati ada pelanggaran pada akun anda kami berhak mensuspend akun anda. \n" +
                    "Bila anda terkena suspend anda tidak dapat menggunakan aplikasi ini. \n" +
                    "Silakakan hubungi kami untuk membuka kembali akun anda."
        }



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
