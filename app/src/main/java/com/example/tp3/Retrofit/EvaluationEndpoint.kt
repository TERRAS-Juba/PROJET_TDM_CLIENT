package com.example.tp3.Retrofit

import com.example.tp3.Entites.Evaluation
import com.example.tp3.url
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EvaluationEndpoint {
    @POST("/evaluation/ajouter_evaluation/")
    suspend fun addEvaluation(@Body data: List<Evaluation>): Response<Void>
    @GET("/evaluation/liste_evaluation/{id}")
    suspend fun getEvaluationParking(@Path("id") id: Int?):Response<List<Evaluation>>
    companion object {
        @Volatile
        var evaluationEndpoint: EvaluationEndpoint? = null
        fun createInstance(): EvaluationEndpoint {
            if (evaluationEndpoint == null) {
                synchronized(this) {
                    evaluationEndpoint =
                        Retrofit.Builder().baseUrl(url)
                            .addConverterFactory(GsonConverterFactory.create()).build()
                            .create(EvaluationEndpoint::class.java)
                }
            }
            return evaluationEndpoint!!
        }
    }
}