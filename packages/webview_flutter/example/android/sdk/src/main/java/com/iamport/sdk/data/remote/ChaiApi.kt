package com.iamport.sdk.data.remote

import com.iamport.sdk.data.chai.CHAI
import com.iamport.sdk.data.chai.response.ChaiPayment
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface ChaiApi {

    @Headers("Content-Type:application/json")
    @GET("/v1/payment/{${CHAI.PAYMENT_ID}}")
    suspend fun getChaiPayment(
        @Header("Idempotency-Key") idempotencyKey: String,
        @Header("public-API-Key") publicApiKey: String,
        @Path(CHAI.PAYMENT_ID) chaiPaymentId: String,
    ): ChaiPayment

}