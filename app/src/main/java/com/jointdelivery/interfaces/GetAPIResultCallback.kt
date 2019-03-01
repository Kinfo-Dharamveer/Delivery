package com.scyhealth.basemodule.interfaces

import retrofit2.Response

interface GetAPIResultCallback<T> {

    fun onGetResponse(response: Response<T>)
    fun onGetFailure()

}