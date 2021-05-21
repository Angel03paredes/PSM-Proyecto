package com.example.linehome

import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.linehome.adapters.NotifyAdapter
import com.example.linehome.adapters.SaveAdapter
import com.example.linehome.models.Notification
import com.example.linehome.models.NotificationPreview
import com.example.linehome.models.NotifyPreview
import com.example.linehome.models.PostPreview
import com.example.linehome.services.NotificationService
import com.example.linehome.services.RestEngine
import kotlinx.android.synthetic.main.fragment_notification.view.*
import kotlinx.android.synthetic.main.fragment_save.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class NotificationFragment : Fragment() {

    private var notfAdapter: NotifyAdapter? = null
    val listNot = mutableListOf<NotifyPreview>()
    var user: String? = null

    var INTERNET_AVAILABLE : Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notification, container, false)

        val sharedPreferences : SharedPreferences = requireContext().getSharedPreferences("SharedP", Context.MODE_PRIVATE)
        user = sharedPreferences.getString("id", "")

        notfAdapter = NotifyAdapter(requireContext(), listNot )

        val llm = LinearLayoutManager(requireContext())
        llm.orientation = LinearLayoutManager.VERTICAL
        view.rvNot.setLayoutManager(llm)
        view.rvNot.setAdapter(notfAdapter)

        INTERNET_AVAILABLE = isNetDisponible()

        if(INTERNET_AVAILABLE) {
            getNotification()
        }
        else {
            showNotificationDisconected()
        }

        return view
    }

    private fun getNotification() {
        val notificationService: NotificationService = RestEngine.getRestEngine().create(NotificationService::class.java)
        val result: Call<List<NotificationPreview>> = notificationService.getNotificationsByReceiver(user!!.toInt())
        DBApplication.dataDBHelper.truncateNotification()

        result.enqueue(object :  Callback<List<NotificationPreview>> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<List<NotificationPreview>>,
                response: Response<List<NotificationPreview>>
            ) {
                val resp = response.body();

                if(resp != null) {
                    for(notification in resp) {
                        val imageBytes = Base64.getDecoder().decode(notification.imageUrl)
                        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                        val not = NotifyPreview(notification.id, notification.userName, decodedImage, notification.createdAt)

                        DBApplication.dataDBHelper.insertNotification(not)

                        listNot.add(not)
                        notfAdapter?.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<List<NotificationPreview>>, t: Throwable) {
                println(t.toString())
            }

        })
    }

    private fun showNotificationDisconected() {
        var listNotification:List<NotifyPreview>
        listNotification = DBApplication.dataDBHelper.getNotification(user!!.toInt())
        for(notification in listNotification){
            listNot.add(notification)
            notfAdapter?.notifyDataSetChanged()
        }
    }

    private fun isNetDisponible(): Boolean {

        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val actNetInfo = connectivityManager.activeNetworkInfo
        return actNetInfo != null && actNetInfo.isConnected
    }

}