package com.jointdelivery.fragments.pagerFragments

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jointdelivery.MyApplication
import com.jointdelivery.R
import com.jointdelivery.appviewmodule.profileupdateApi.ProfileUpdateMethods
import com.jointdelivery.appviewmodule.registeractivities.ZipCodeData
import com.jointdelivery.auth.AuthManager
import com.jointdelivery.utilities.*
import com.scyhealth.basemodule.interfaces.GetAPIResultCallback
import com.scyhealth.basemodule.interfaces.PostAPIResultCallback
import com.scyhealth.loginmodule.LoginApiMethods
import kotlinx.android.synthetic.main.dialog_choose_file_type.*
import kotlinx.android.synthetic.main.fragment_yourinfo_layout.*
import retrofit2.Response
import java.io.File
import javax.inject.Inject

open class YourInfoFragment : Fragment(), View.OnClickListener, PostAPIResultCallback<CommonResponseModel>,
    GetAPIResultCallback<ZipCodeData> {

    override fun onGetResponse(response: Response<ZipCodeData>) {
        CommonUtil.hideProgress()
        if (response.body()?.success!!) {
            text_state.text = response.body()?.data?.state
            text_city.text = response.body()?.data?.city
            state_Selected = response.body()?.data?.state!!
            city_Selected = response.body()?.data?.city!!
            zip_Code = response.body()?.data?.zipCode!!
        } else {
            CommonUtil.showSnackbar(main_scroll, response.body()?.message!!)
        }
    }

    override fun onGetFailure() {
        CommonUtil.hideProgress()
        CommonUtil.showSnackbar(main_scroll, "something went wrong, please try again later")
    }

    @Inject
    lateinit var authManager: AuthManager

    override fun onResponse(response: Response<CommonResponseModel>) {

        CommonUtil.hideProgress()
        CommonUtil.showShortToast(context!!, response.body()!!.message)
        activity!!.finish()
    }

    override fun onFailure() {

        CommonUtil.showSnackbar(main_scroll, resources.getString(R.string.something_wrong))
        CommonUtil.hideProgress()
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.varify_zip_btn -> {
                if (CommonUtil.checkEmptyString(edit_zip_code)) {
                    CommonUtil.showSnackbar(main_scroll, "please enter zip code")
                    return
                } else {
                    CommonUtil.showProgress(context!!)
                    LoginApiMethods.getZipCodedata(edit_zip_code.text.toString(), this)

                }

            }

            R.id.edit_profile_pic -> {
                if (CommonUtil.isConnectingToInternet(activity!!)) {

                    if (!CommonUtil.hasPermissions(activity!!, Constants.CAMERA_PERMISSIONS)) {
                        ActivityCompat.requestPermissions(
                            activity!!,
                            Constants.CAMERA_PERMISSIONS,
                            Constants.PERMISSION_ALL
                        )
                    } else {
                        openCustomDialog()
                    }

                } else {
                    CommonUtil.showSnackbar(main_scroll, resources.getString(R.string.please_connect_internet))
                }
            }
            R.id.camera -> {
                dialog.dismiss()
                val sesionvalues1 = ContentValues()
                sesionvalues1.put(MediaStore.Images.Media.TITLE, "New Picture")
                sesionvalues1.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
                imageUri =
                    activity!!.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, sesionvalues1)
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(intent, 1)
            }

            R.id.gallery -> {
                dialog.dismiss()
                val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhotoIntent, 2)
            }

            R.id.save_button -> {

                city_Selected = text_city.text.toString()
                state_Selected = text_state.text.toString()

                zip_Code = edit_zip_code.text.toString()


                if (CommonUtil.checkEmptyString(edt_driver_name)) {
                    CommonUtil.showSnackbar(main_scroll, getString(R.string.please_enter_name))
                    return
                }

                if (CommonUtil.checkEmptyString(edt_address)) {
                    CommonUtil.showSnackbar(main_scroll, getString(R.string.please_enter_address))
                    return
                }

                if (city_Selected.equals("")) {
                    CommonUtil.showSnackbar(main_scroll, resources.getString(R.string.please_selete_city))
                    return
                }
                if (state_Selected.equals("")) {
                    CommonUtil.showSnackbar(main_scroll, resources.getString(R.string.please_selete_state))
                    return
                }

                if (profilePic.equals("")) {
                    CommonUtil.showSnackbar(main_scroll, resources.getString(R.string.please_upload_profilePic))
                    return
                }

                if (CommonUtil.checkEmptyString(edit_zip_code)) {
                    CommonUtil.showSnackbar(main_scroll, resources.getString(R.string.please_add_zip_code))
                    return
                }
                if (CommonUtil.isConnectingToInternet(activity!!)) {


                    val hashMap: HashMap<String, String> = HashMap<String, String>() //define empty hashmap

                    hashMap.put("Name", edt_driver_name.text.toString())
                    hashMap.put("Id", authManager.getUserId().toString())
                    hashMap.put("Email", edt_driver_email.text.toString())
                    hashMap.put("PhoneNumber", edt_driver_phone_no.text.toString())
                    hashMap.put("Address", edt_address.text.toString())
                    /*       hashMap.put("StateId", state_Selected)
                           hashMap.put("CityId", city_Selected)*/
                    hashMap.put("ProfilePictureUrl", profilePic)
                    hashMap.put("ZipCode", zip_Code)
                    hashMap.put("Id", authManager.getUserId().toString())


                    city_Selected = text_city.text.toString()
                    state_Selected = text_state.text.toString()
                    CommonUtil.showProgress(context!!)
                    ProfileUpdateMethods.updateDriverProfile(this, authManager.getAccessToken(), hashMap)
                } else {
                    CommonUtil.showSnackbar(main_scroll, resources.getString(R.string.please_connect_internet))
                }

            }
        }
    }

    lateinit var dialog: AlertDialog
    lateinit var imageUri: Uri

    lateinit var picturePath: String
    lateinit var compressedPath: String
    lateinit var imageKey: String
    var profilePic: String = ""

    lateinit var compressImage: CompressImage

    lateinit var transferUtility: TransferUtility
    var state_Selected = ""
    var city_Selected = ""
    var zip_Code = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_yourinfo_layout, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
        (activity!!.application as MyApplication).getAppComponent()?.inject(this)
        initClicks()
    }

    var profileDetail: HashMap<String, String>? = null
    private fun initData() {
        compressImage = CompressImage()
        profilePic = ""

        profileDetail = arguments?.getSerializable("profileDetail") as HashMap<String, String>

        edt_driver_name.setText(profileDetail!!.get("name"))
        edt_driver_email.setText(profileDetail!!.get("email"))
        edt_driver_phone_no.setText(profileDetail!!.get("phoneNumber"))
        edt_address.setText(profileDetail!!.get("address"))
        profilePic = profileDetail!!.get("profilePictureUrl")!!


        Glide.with(this)
            .load(profilePic)
            .apply(RequestOptions.circleCropTransform())
            .into(profile_pic)

        text_state.text = profileDetail!!.get("state")
        text_city.text = profileDetail!!.get("city")
        edit_zip_code.setText(profileDetail!!.get("zipcode"))
    }

    private fun initClicks() {
        save_button.setOnClickListener(this)
        edit_profile_pic.setOnClickListener(this)
        varify_zip_btn.setOnClickListener(this)


        edit_profile_pic.setOnClickListener {
            if (CommonUtil.isConnectingToInternet(activity!!.applicationContext)) {

                if (!CommonUtil.hasPermissions(activity, Constants.CAMERA_PERMISSIONS)) {
                    ActivityCompat.requestPermissions(
                        activity!!,
                        Constants.CAMERA_PERMISSIONS,
                        Constants.PERMISSION_ALL
                    )
                } else {
                    openCustomDialog()
                }

            } else {
                CommonUtil.showSnackbar(main_scroll, resources.getString(R.string.please_connect_internet))
            }
        }
    }


    fun openCustomDialog() {
        val inflater = layoutInflater
        val alertLayout = inflater.inflate(R.layout.dialog_choose_file_type, null)

        val alert = AlertDialog.Builder(activity!!)
        alert.setView(alertLayout)

        dialog = alert.create()
        dialog.show()
        dialog.camera.setOnClickListener(this)
        dialog.gallery.setOnClickListener(this)
    }


    private fun beginUpload(filePath: String?) {
        CommonUtil.showProgress(activity!!)

        if (filePath == null) {
            CommonUtil.showSnackbar(main_scroll, "Could not find the filepath of the selected file")
            return
        }

        val file = File(filePath)

        transferUtility = AWSUtils.getTransferUtility(activity!!)

        val observer = transferUtility.upload(Constants.BUCKET_NAME, file.name, file)

        observer.setTransferListener(UploadListener())
    }

    private inner class UploadListener : TransferListener {

        // Simply updates the UI list when notified.
        override fun onError(id: Int, e: Exception) {
            CommonUtil.hideProgress()
        }

        override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
        }

        override fun onStateChanged(id: Int, newState: TransferState) {
            if (newState == TransferState.COMPLETED) {
                CommonUtil.hideProgress()

                profilePic = "http://s3-eu-west-1.amazonaws.com/scyphirhealth/$imageKey"


            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uri = data.data
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = activity!!.contentResolver.query(uri, projection, null, null, null)
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(projection[0])
            picturePath = cursor.getString(columnIndex)
            compressedPath = compressImage.compressImage(activity!!, picturePath)
            cursor.close()

            try {
                val bmp = BitmapFactory.decodeFile(compressedPath)
                Glide.with(this)
                    .load(bmp)
                    .apply(RequestOptions.circleCropTransform())
                    .into(profile_pic)
                if (true) {
                    beginUpload(compressedPath)
                    try {
                        imageKey = compressedPath.substring(compressedPath.lastIndexOf("/") + 1)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (requestCode == 1) {
            try {
                picturePath = CommonUtil.getRealPathFromURI(imageUri, activity!!)
                compressedPath = compressImage.compressImage(activity!!, picturePath)
                val bmp = BitmapFactory.decodeFile(compressedPath)

                Glide.with(this)
                    .load(bmp)
                    .apply(RequestOptions.circleCropTransform())
                    .into(profile_pic)
                if (compressedPath != null) {
                    beginUpload(compressedPath)
                    try {
                        imageKey = compressedPath.substring(compressedPath.lastIndexOf("/") + 1)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /* private fun spinnerInitValues() {

         spinner_city.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
             override fun onNothingSelected(parent: AdapterView<*>?) {

             }

             override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                 if (position > 0)
                     city_Selected = position.toString()
             }

         }

 //        spinner_select_State.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, array_states)
         spinner_state.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
             override fun onNothingSelected(parent: AdapterView<*>?) {


             }

             override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                 if (position > 0)
                     state_Selected = position.toString()
             }

         }
     }*/
}