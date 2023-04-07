package com.innaval.agendaeventos.presentation.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.innaval.agendaeventos.R
import com.innaval.agendaeventos.presentation.ui.eventdetails.EventDetailsActivity

object EventsRouter {

    fun openEventDatailsActivity(activity: Activity, eventId: String) {
        val intent = Intent(activity, EventDetailsActivity::class.java)
        intent.putExtra(EVENT_SELECTED_ID, eventId)
        activity.startActivityForResult(intent, EVENTS_REFRESH_REQUEST_CODE)
    }

    fun shareLocation(activity: Activity, latitude: Double, longitude: Double) {
        val uri = ("geo:" + latitude.toString() + ","
                + longitude.toString() + "?q=" + latitude
                .toString() + "," + longitude)
        activity.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        )
    }

    fun shareEvent(activity: Activity, title: String, latitude: Double, longitude: Double){
        val uri = activity.getString(R.string.share_location_url, latitude, longitude)
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val ShareSub = activity.getString(R.string.share_event_text, title)
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, ShareSub)
        sharingIntent.putExtra(Intent.EXTRA_TEXT, uri)
        activity.startActivity(Intent.createChooser(sharingIntent, activity.getString(R.string.share_event_title)))
    }

    const val EVENT_SELECTED_ID = "EventSelectedId"
    const val EVENTS_NEEDS_REFRESH = "EventsNeedsRefresh"
    const val EVENTS_REFRESH_REQUEST_CODE = 33
}

