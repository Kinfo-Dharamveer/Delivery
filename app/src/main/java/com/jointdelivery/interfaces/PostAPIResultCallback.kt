package com.scyhealth.basemodule.interfaces

import retrofit2.Response

interface PostAPIResultCallback<T> {

    fun onResponse(response: Response<T>)
    fun onFailure()

}