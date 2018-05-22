package codes.vulnwalker.bonuspulsa.firebase

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import java.nio.file.Files.size
import com.google.firebase.messaging.RemoteMessage
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.app.NotificationCompat
import codes.vulnwalker.bonuspulsa.MainActivity
import codes.vulnwalker.bonuspulsa.R
import codes.vulnwalker.bonuspulsa.database.KotlinHelper
import com.google.firebase.iid.FirebaseInstanceId
import org.jetbrains.anko.toast
import org.json.JSONObject
import android.os.Messenger
import codes.vulnwalker.bonuspulsa.LoginActivity


/**
 * Created by root on 05/09/17.
 */
class FirebaseListener : FirebaseMessagingService() {
    internal lateinit var soundNotif: Uri
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        Log.d(TAG, "From: " + remoteMessage!!.from)

        Log.d(TAG, "Message data payload: " + remoteMessage.data)

        var idNotif : Int = 1

        var notifTitle = ""
        var notifBody = ""
        var eventPush = ""
        val content = JSONObject(remoteMessage.data.toString())
        val itemPush = JSONObject(content.getString("itemPush").toString())
        notifTitle = itemPush.getString("title").toString()
        eventPush = itemPush.getString("event").toString()
        if(eventPush.equals("penukaran")){
            val dataJson = JSONObject(itemPush.getString("body").toString())
            val idPenukaran = dataJson.getString("id").toString()
            val namaPenukaran = dataJson.getString("nama_tukar")
            val tanggalAksi = dataJson.getString("tanggal_aksi")
            idNotif = 2
            notifBody = namaPenukaran
            penukaranDiterima(idPenukaran,tanggalAksi)
        }else if(eventPush.equals("hadiah")){
            val dataJson = JSONObject(itemPush.getString("body").toString())
            val saldoTambah = dataJson.getString("saldo").toString()
            notifBody = rupiahFormat(saldoTambah)
            idNotif = 3
            addPoint(saldoTambah)
        }

        if(!eventPush.equals("suspend") && !eventPush.equals("double")){
            soundNotif = Uri.parse("android.resource://codes.vulnwalker.bonuspulsa/"
                    + R.raw.sound2)

            val stackBuilder = TaskStackBuilder.create(this)
            stackBuilder.addParentStack(MainActivity::class.java)
            val mBuilder = NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setColor(1)
                    .setContentTitle(notifTitle)
                    .setContentText(notifBody)
                    .setSound(soundNotif)
                    .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                    .setAutoCancel(true) as NotificationCompat.Builder
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBuilder.color = applicationContext.resources.getColor(R.color.colorPrimaryDark)
                mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                mBuilder.setSound(soundNotif)
                mBuilder.setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                mBuilder.setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.mipmap.ic_launcher))
            }

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(Integer.parseInt(idNotif.toString()), mBuilder.build())
        }else if(eventPush.equals("suspend")){
            val dataJson = JSONObject(itemPush.getString("body").toString())
            val reason = dataJson.getString("alasan")
            notifBody = reason
            suspend()
            soundNotif = Uri.parse("android.resource://codes.vulnwalker.bonuspulsa/"
                    + R.raw.sound2)
            val resultIntent = Intent(this, LoginActivity::class.java)
            val stackBuilder = TaskStackBuilder.create(this)

            stackBuilder.addNextIntent(resultIntent)
            val resultPending = stackBuilder
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            stackBuilder.addParentStack(MainActivity::class.java)
            val mBuilder = NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setColor(1)
                    .setContentTitle(notifTitle)
                    .setContentText(notifBody)
                    .setSound(soundNotif)
                    .setContentIntent(resultPending)
                    .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                    .setAutoCancel(true) as NotificationCompat.Builder
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBuilder.color = applicationContext.resources.getColor(R.color.colorPrimaryDark)
                mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                mBuilder.setSound(soundNotif)
                mBuilder.setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                mBuilder.setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.mipmap.ic_launcher))
            }

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(Integer.parseInt(idNotif.toString()), mBuilder.build())
        }else if(eventPush.equals("double")){

            suspend()
            soundNotif = Uri.parse("android.resource://codes.vulnwalker.bonuspulsa/"
                    + R.raw.sound2)
            val resultIntent = Intent(this, LoginActivity::class.java)
            val stackBuilder = TaskStackBuilder.create(this)

            stackBuilder.addNextIntent(resultIntent)
            val resultPending = stackBuilder
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            stackBuilder.addParentStack(MainActivity::class.java)
            val mBuilder = NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setColor(1)
                    .setContentTitle("Double Login")
                    .setContentText("Akun Anda Telah Login D")
                    .setSound(soundNotif)
                    .setContentIntent(resultPending)
                    .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                    .setAutoCancel(true) as NotificationCompat.Builder
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBuilder.color = applicationContext.resources.getColor(R.color.colorPrimaryDark)
                mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                mBuilder.setSound(soundNotif)
                mBuilder.setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                mBuilder.setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.mipmap.ic_launcher))
            }

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(Integer.parseInt(idNotif.toString()), mBuilder.build())
        }




//        if (remoteMessage.notification != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification.body!!)
//        }
    }

    fun penukaranDiterima(idPenukaran : String, tanggalAksi : String){
        val helper = KotlinHelper(applicationContext)
        val query = "UPDATE HISTORI_TUKAR set status = 'DONE',tanggal_aksi = '"+tanggalAksi+"' where id = '"+idPenukaran+"' "
        val db = helper.writableDatabase
        db.execSQL(query)

    }
    fun addPoint(saldo : String){
        val helper = KotlinHelper(applicationContext)
        val query = "UPDATE ACCOUNT set saldo = saldo + "+saldo+""
        val db = helper.writableDatabase
        db.execSQL(query)

    }

    fun suspend(){
        val helper = KotlinHelper(applicationContext)
        helper.destroyApp()

    }

     fun rupiahFormat(s: String): String {
        if (s.length <= 3)
            return s
        val first = (s.length - 1) % 3 + 1
        val buf = StringBuilder(s.substring(0, first))
        var i = first
        while (i < s.length) {
            buf.append('.').append(s.substring(i, i + 3))
            i += 3
        }
        return buf.toString()
    }
}


