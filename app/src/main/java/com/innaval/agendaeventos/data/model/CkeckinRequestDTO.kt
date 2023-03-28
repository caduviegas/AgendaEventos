package com.innaval.agendaeventos.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class CkeckinRequestDTO(
    @SerializedName("eventId") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String
): Parcelable