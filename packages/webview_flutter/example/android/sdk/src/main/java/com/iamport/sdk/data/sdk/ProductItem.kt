package com.iamport.sdk.data.sdk

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * for naverpay
 */

@Parcelize
@Serializable
data class ProductItem(
    val categoryType: String,
    val categoryId: String,
    val uid: String,
    val name: String,
    val payReferrer: String?,
    val startDate: String?,
    val endDate: String?,
    val sellerId: String?,
    val count: Int?,
) : Parcelable
