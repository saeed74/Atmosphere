package com.arianmanesh.atmospher.data.remote

import okhttp3.ResponseBody

sealed class ResponseResult<T> {
    data class Success<T>(val data: T?) : ResponseResult<T>()
    data class Error<T>(val errorCode: Int?=null ,val errorResponseBody: ResponseBody?= null) : ResponseResult<T>()
    data class DataBaseError<T>(val error: String) : ResponseResult<T>()
    class Loading<T> : ResponseResult<T>()
}