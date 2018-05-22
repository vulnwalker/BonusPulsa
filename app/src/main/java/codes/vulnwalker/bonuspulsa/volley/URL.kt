package codes.vulnwalker.bonuspulsa.volley


/**
 * Created by root on 01/09/17.
 */
object URL {
    private val URL_ROOT = "http://api.bonus-pulsa.com/pages.php?Pg=api&tipe="
    val REGISTER = URL_ROOT + "register"
    val AUTH = URL_ROOT + "auth"
    val SET_TOKEN = URL_ROOT + "setToken"
    val ADD_SALDO = URL_ROOT + "addSaldo"
    val GET_SALDO = URL_ROOT + "getSaldo"
    val GET_LIST_TUKAR = URL_ROOT + "getListTukar"
    val GET_DETAIL_LIST_TUKAR = URL_ROOT + "getDetailListTukar"
    val REQUEST_TUKAR = URL_ROOT + "requestTukar"
    val GET_LIST_HISTORI_TUKAR = URL_ROOT + "getHistoriPenukaran"
    val ABSEN = URL_ROOT + "absen"
    val SYNC = URL_ROOT + "sync"
    val CEK_IKLAN = URL_ROOT + "cek_iklan"
    val ADS_REQUEST = URL_ROOT + "adsRequest"

}