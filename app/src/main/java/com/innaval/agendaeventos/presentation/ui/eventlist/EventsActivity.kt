package com.innaval.agendaeventos.presentation.ui.eventlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.innaval.agendaeventos.R
import com.innaval.agendaeventos.databinding.ActivityListBinding
import com.innaval.agendaeventos.domain.model.EventBO
import com.innaval.agendaeventos.presentation.ui.EventsRouter
import com.innaval.agendaeventos.presentation.ui.EventsRouter.EVENTS_REFRESH_REQUEST_CODE
import com.innaval.agendaeventos.state.EventResponse
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.processor.internal.definecomponent.codegen._dagger_hilt_android_components_ViewModelComponent

@AndroidEntryPoint
class EventsActivity : AppCompatActivity() {

    private val viewModel: EventsViewModel by viewModels()

    private val binding by lazy { ActivityListBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupViews()
        setupObserver()
    }

    private fun hideProgressBar() {
        binding.swipeLayout.isRefreshing = false
    }

    private fun showProgressBar() {
        binding.swipeLayout.isRefreshing = true
    }

    private fun setupViews() {
        setupRecylcerview()
        setupSwipeToRefresh()
        setupToolbar()
        viewModel.listEvents()
    }

    private fun setupRecylcerview() {
        binding.rvUsers.setHasFixedSize(true)
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
    }

    private fun setupSwipeToRefresh() {
        binding.swipeLayout.setOnRefreshListener {
            viewModel.listEvents()
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.itemRefresh -> viewModel.listEvents()
            }
            true
        }
    }

    private fun showEventsList(events: List<EventBO>) {
        binding.rvUsers.adapter = EventsAdapter(this, events) { event ->
            EventsRouter.openEventDatailsActivity(this, event.id)
        }
    }

    private fun setupObserver() {
        val observer = Observer<EventResponse<List<EventBO>>> { state ->
            when (state) {
                is EventResponse.EventLoading -> showProgressBar()
                is EventResponse.EventSuccess -> {
                    showEventsList(state.data)
                    hideProgressBar()
                }
                is EventResponse.NetworkError -> {
                    Toast.makeText(this, getString(R.string.error_network), Toast.LENGTH_LONG)
                        .show()
                    hideProgressBar()
                }

                is EventResponse.GenericError -> {
                    Toast.makeText(this, getString(R.string.error_network), Toast.LENGTH_LONG)
                        .show()
                    hideProgressBar()
                }
            }
        }
        viewModel.eventsLiveData.observe(this, observer)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            val camesFromEventDetails = requestCode == EVENTS_REFRESH_REQUEST_CODE
            val needsRefresh = it.getBooleanExtra(EventsRouter.EVENTS_NEEDS_REFRESH, false)
            if (camesFromEventDetails && needsRefresh) {
                viewModel.listEvents()

            }
        }


    }