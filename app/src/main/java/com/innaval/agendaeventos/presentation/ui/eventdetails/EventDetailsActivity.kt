package com.innaval.agendaeventos.presentation.ui.eventdetails

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.innaval.agendaeventos.R
import com.innaval.agendaeventos.databinding.ActivityDetailsBinding
import com.innaval.agendaeventos.databinding.DialogCheckinBinding
import com.innaval.agendaeventos.domain.model.CheckinBO
import com.innaval.agendaeventos.domain.model.EventBO
import com.innaval.agendaeventos.presentation.ui.EventsRouter.EVENT_SELECTED_ID
import com.innaval.agendaeventos.presentation.ui.EventsRouter.shareLocation
import com.innaval.agendaeventos.presentation.utils.formatMonetary
import com.innaval.agendaeventos.presentation.utils.toDayMonthYear
import com.innaval.agendaeventos.state.EventResponse
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.concurrent.schedule

class EventDetailsActivity : AppCompatActivity() {

    private val viewModel: EventDetailsViewModel by viewModels()
    private val eventId by lazy { intent.getStringExtra(EVENT_SELECTED_ID) }

    private val binding by lazy { ActivityDetailsBinding.inflate(layoutInflater) }
    private lateinit var dialogCheckin: Dialog
    private var dialogLoading: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupViews()
        setupEventObserver()
        setupCheckinObserver()
        viewModel.fetchEventDetails()
    }

    private fun setupViews() {
        showLoading()
        setupToolbar()
        openCheckinDialog()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun openCheckinDialog() {
        binding.btnCheckin.setOnClickListener {
            val view = DialogCheckinBinding.inflate(layoutInflater)
            dialogCheckin = Dialog(this)
            dialogCheckin.setContentView(view.root)
            view.btnConfirmCheckin.setOnClickListener {
                if (view.etName.text.isNotEmpty() && view.etEmail.text.isNotEmpty()) {
                    viewModel.checkinEvent(
                        view.etName.text.toString(),
                        view.etEmail.text.toString()
                    )
                } else {
                    if (view.etName.text.isEmpty()) view.etName.error =
                        getString(R.string.fill_field)
                    if (view.etEmail.text.isEmpty()) view.etEmail.error =
                        getString(R.string.fill_field)
                }
            }
            dialogCheckin.show()
        }
    }

    private fun showLoading() {
        if (dialogLoading == null || !dialogLoading!!.isShowing) {
            dialogLoading = Dialog(this)
            (dialogLoading as Dialog).setContentView(R.layout.dialog_loading)
            (dialogLoading as Dialog).setCancelable(false)
            (dialogLoading as Dialog).show()
        }
    }

    private fun hideLoading() {
        Timer().schedule(1000) {
            dialogLoading?.dismiss()
        }
    }

    private fun setupEventObserver() {
        val observer = Observer<EventResponse<EventBO>> { state ->
            when (state) {
                is EventResponse.EventLoading -> showLoading()
                is EventResponse.EventSuccess -> {
                    hideLoading()
                    populateEventDetails(state.data)
                }
                is EventResponse.GenericError -> {
                    hideLoading()
                    showErrorMessage()
                }
                is EventResponse.ServerError -> {
                    hideLoading()
                    showErrorMessage()
                }
                is EventResponse.NetworkError -> {
                    hideLoading()
                    showErrorMessage()
                }
            }
        }
        viewModel.eventLiveData.observe(this, observer)
        viewModel.selectEvent(eventId)
    }

    private fun setupCheckinObserver() {
        val observer = Observer<EventResponse<CheckinBO>> { state ->
            when (state) {
                is EventResponse.EventLoading -> showLoading()
                is EventResponse.EventSuccess -> {
                    dialogCheckin.dismiss()
                    hideLoading()
                    showCheckinSuccessMessage()
                }
                is EventResponse.GenericError -> {
                    hideLoading()
                    showErrorMessage()
                }
                is EventResponse.ServerError -> {
                    hideLoading()
                    showErrorMessage()
                }
                is EventResponse.NetworkError -> {
                    hideLoading()
                    showErrorMessage()
                }
            }
        }
        viewModel.checkinLiveData.observe(this, observer)
    }

    private fun showCheckinSuccessMessage() {
        Toast.makeText(this, R.string.checkin_done, Toast.LENGTH_LONG).show()
    }

    private fun populateEventDetails(event: EventBO) {
        binding.let {
            Picasso.get()
                .load(event.imageUrl)
                .fit()
                .placeholder(R.drawable.bg_events)
                .error(R.drawable.bg_events)
                .into(ivImage)
            toolbarLayout.title = event.title
            toolbar.title = event.title
            tvPrice.text = event.price.formatMonetary()
            tvDate.text = event.date.toDayMonthYear()
            tvLatitude.text = getString(R.string.location_latitude, event.latitude)
            tvLongitude.text = getString(R.string.location_longitude, event.longitude)
            tvDescription.text = event.description
            contentLocation.contentDescription =
                getString(R.string.share_location_button, event.latitude, event.longitude)
            contentLocation.setOnClickListener {
                shareLocation(this, event.latitude, event.longitude)
            }
            toolbar.setOnMenuItemClickListener {
                shareLocation(this, event.latitude, event.longitude)
                true
            }
        }
    }

    private fun showErrorMessage() {
        Toast.makeText(this, R.string.error_generic, Toast.LENGTH_LONG).show()
    }
}
