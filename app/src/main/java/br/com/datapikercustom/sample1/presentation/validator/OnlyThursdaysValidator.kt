package br.com.datapikercustom.sample1.presentation.validator

import android.os.Parcel
import android.os.Parcelable
import com.google.android.material.datepicker.CalendarConstraints
import java.util.Calendar

class OnlyThursdaysValidator() : CalendarConstraints.DateValidator {
    constructor(parcel: Parcel) : this() {}

    override fun isValid(date: Long): Boolean {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {}

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<OnlyThursdaysValidator> {
        override fun createFromParcel(parcel: Parcel): OnlyThursdaysValidator {
            return OnlyThursdaysValidator(parcel)
        }

        override fun newArray(size: Int): Array<OnlyThursdaysValidator?> {
            return arrayOfNulls(size)
        }
    }
}