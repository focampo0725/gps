package com.kloubit.gps.data.network


import io.reactivex.Single
import retrofit2.http.*

interface CompanyListAPI {
    @GET("/api/Empresa/listaEmpresa")
    fun requestCompanyList(
    ): Single<String>
}