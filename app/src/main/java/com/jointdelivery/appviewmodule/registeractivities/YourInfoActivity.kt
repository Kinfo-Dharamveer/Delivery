package com.jointdelivery.appviewmodule.registeractivities

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.bruce.pickerview.popwindow.DatePickerPopWin
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jointdelivery.MyApplication
import com.jointdelivery.R
import com.jointdelivery.auth.AuthManager
import com.jointdelivery.utilities.*
import com.scyhealth.basemodule.interfaces.GetAPIResultCallback
import com.scyhealth.basemodule.interfaces.PostAPIResultCallback
import com.scyhealth.loginmodule.LoginApiMethods
import kotlinx.android.synthetic.main.activity_your_info_layout.*
import kotlinx.android.synthetic.main.tool_bar_layout.*
import retrofit2.Response
import java.io.File


open class YourInfoActivity : AppCompatActivity(), PostAPIResultCallback<CommonResponseModel>,
    GetAPIResultCallback<ZipCodeData> {

    lateinit var dialog: AlertDialog
    lateinit var imageUri: Uri

    lateinit var picturePath: String
    lateinit var compressedPath: String
    lateinit var imageKey: String
    lateinit var profilePic: String

    lateinit var compressImage: CompressImage

    lateinit var transferUtility: TransferUtility

    var state_Selected = ""
    var city_Selected = ""
    var zip_Code = ""
    var strDate = ""
    lateinit var mauthmanager: AuthManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_your_info_layout)
        (application as MyApplication).getAppComponent()?.inject(this)
        toolbar_title.text = resources.getString(R.string.your_info_heading)
        spinnerInitValues()
        compressImage = CompressImage()
        profilePic = ""

        tv_mobile.text = intent.getStringExtra(Constants.KEY_PHONE)

        back_button.setOnClickListener { finish() }

        edit_text_zip_code.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val zipTextAvaliable = !CommonUtil.checkEmptyString(edit_text_zip_code)

                if (zipTextAvaliable) {
                    btn_verify.isEnabled = true
                }else{
                    btn_verify.isEnabled = false
                    continueBt.isEnabled = false
                }
            }
        })
    }


    override fun onResponse(response: Response<CommonResponseModel>) {
        CommonUtil.hideProgress()
        if (response.body()!!.success) {
            CommonUtil.showShortToast(applicationContext, response.body()!!.message)
        } else {
            CommonUtil.showSnackbar(main_layout, response.body()!!.message)
        }
    }

    fun ProfilePicClick(view: View) {
        if (CommonUtil.isConnectingToInternet(this)) {

            if (!CommonUtil.hasPermissions(this, Constants.CAMERA_PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, Constants.CAMERA_PERMISSIONS, Constants.PERMISSION_ALL)
            } else {
                openCustomDialog()
            }

        } else {
            CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_connect_internet))
        }
    }

    fun openCustomDialog() {
        val inflater = layoutInflater
        val alertLayout = inflater.inflate(R.layout.dialog_choose_file_type, null)

        val alert = AlertDialog.Builder(this)
        alert.setView(alertLayout)

        dialog = alert.create()
        dialog.show()
    }

    override fun onFailure() {
        CommonUtil.showSnackbar(main_layout, resources.getString(R.string.something_wrong))
        CommonUtil.hideProgress()
    }

    fun onCameraClick(view: View) {
        dialog.dismiss()
        val sesionvalues1 = ContentValues()
        sesionvalues1.put(MediaStore.Images.Media.TITLE, "New Picture")
        sesionvalues1.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, sesionvalues1)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intent, 1)
    }

    fun verifyZipCode(view: View) {
        continueBt.isEnabled = true
        if (CommonUtil.checkEmptyString(edit_text_zip_code)) {
            CommonUtil.showSnackbar(main_layout, "please enter zip code")
            return
        } else {
            CommonUtil.showProgress(this)
            LoginApiMethods.getZipCodedata(edit_text_zip_code.text.toString(), this)
        }
    }

    fun onGalleryClick(view: View) {
        dialog.dismiss()
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhotoIntent, 2)
    }

    private fun spinnerInitValues() {

        edit_dob.setOnClickListener {
            CommonUtil.hideKeyboard(main_layout, this)
            val pickerPopWin =
                DatePickerPopWin.Builder(this, object : DatePickerPopWin.OnDatePickedListener {
                    override fun onDatePickCompleted(year: Int, month: Int, day: Int, dateDesc: String) {

                        var setMonth = month.toString()
                        var setDay = day.toString()

                        if (setMonth.length == 1) {
                            setMonth = "0$setMonth"
                        }

                        if (setDay.length == 1) {
                            setDay = "0$setDay"
                        }

                        strDate = "$year-$setMonth-$setDay"
                        edit_dob.text = strDate
                    }
                }).textConfirm("CONFIRM") //text of confirm button
                    .textCancel("CANCEL") //text of cancel button
                    .btnTextSize(16) // button text size
                    .colorCancel(Color.parseColor("#999999")) //color of cancel button
                    .colorConfirm(Color.parseColor("#27AE60"))//color of confirm button
                    .minYear(1970) //min year in loop
                    .maxYear(2018) // max year in loop
                    .dateChose(strDate)// date chose when init popwindow
                    .build()

            pickerPopWin.showPopWin(this)
        }

        /*spinner_select_City.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0)
                    city_Selected = position.toString()
            }

        }

//        spinner_select_State.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, array_states)
        spinner_select_State.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0)
                    state_Selected = position.toString()
            }
        }*/
    }

    fun continueClick(view: View) {
        if (CommonUtil.isConnectingToInternet(applicationContext)) {

            if (profilePic.equals("")) {
                CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_upload_profilePic))
                return
            }
            if (CommonUtil.checkEmptyString(edit_text_name)) {
                CommonUtil.showSnackbar(main_layout, getString(R.string.please_enter_name))
                return
            }
            if (CommonUtil.checkEmptyString(edit_text_email)) {
                CommonUtil.showSnackbar(main_layout, getString(R.string.please_enter_email))
                return
            }
            if (CommonUtil.checkEmptyString(edit_text_zip_code)) {
                CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_add_zip_code))
                return
            }
            if (state_Selected.equals("")) {
                CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_selete_valid_zipcode_state))
                return
            }
            if (city_Selected.equals("")) {
                CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_enter_valid_zipcode))
                return
            }
            if (edit_dob.text.toString().equals("")) {
                CommonUtil.showSnackbar(main_layout, getString(R.string.please_enter_dob))
                return
            }
            if (CommonUtil.checkEmptyString(edit_social_security)) {
                CommonUtil.showSnackbar(main_layout, getString(R.string.please_enter_social_security))
                return
            }
            if (CommonUtil.checkEmptyString(edit_licence_no)) {
                CommonUtil.showSnackbar(main_layout, getString(R.string.please_enter_license_number))
                return
            }
            if (CommonUtil.checkEmptyString(edit_text_SetPassword)) {
                CommonUtil.showSnackbar(main_layout, getString(R.string.please_enter_password))
                return
            }
            if (!CommonUtil.checkValidPassword(edit_text_SetPassword)) {
                CommonUtil.showShortToast(this, getString(R.string.please_enter_valid_password))
                return
            }
            if (CommonUtil.checkEmptyString(edit_text_ConfirmPassword)) {
                CommonUtil.showSnackbar(main_layout, getString(R.string.please_enter_confirm_password))
                return
            }

            if (!CommonUtil.fieldValue(edit_text_SetPassword).equals(CommonUtil.fieldValue(edit_text_ConfirmPassword))) {
                CommonUtil.showSnackbar(main_layout, "Passwords don't match")
                return
            }

            val hashMap: HashMap<String, String> = HashMap<String, String>() //define empty hashmap

            hashMap.put("FullName", edit_text_name.text.toString())
            hashMap.put("Email", edit_text_email.text.toString())
            hashMap.put("Password", edit_text_SetPassword.text.toString())
            hashMap.put("ConfirmPassword", edit_text_ConfirmPassword.text.toString())
            hashMap.put("PhoneNumber", tv_mobile.text.toString())
            hashMap.put("Address", edit_text_address.text.toString())
            hashMap.put("ProfilePictureUrl", profilePic)
            hashMap.put("ZipCode", zip_Code)
            hashMap.put("DateOfBirth", edit_dob.text.toString())
            hashMap.put("SocialSecurityNumber", edit_social_security.text.toString())
            hashMap.put("DriverLicenseNumber", edit_licence_no.text.toString())

            if (hashMap != null) {
                intent = Intent(this, VehicleDetailActivity::class.java)
                intent.putExtra("hashDriverInfo", hashMap)
                startActivity(intent)
            }
        } else {
            CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_connect_internet))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uri = data.data
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(uri, projection, null, null, null)
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(projection[0])
            picturePath = cursor.getString(columnIndex)
            compressedPath = compressImage.compressImage(this, picturePath)
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
                picturePath = CommonUtil.getRealPathFromURI(imageUri, applicationContext)
                compressedPath = compressImage.compressImage(this, picturePath)
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

    private fun beginUpload(filePath: String?) {
        CommonUtil.showProgress(this)

        if (filePath == null) {
            CommonUtil.showSnackbar(main_layout, "Could not find the filepath of the selected file")
            return
        }

        val file = File(filePath)

        transferUtility = AWSUtils.getTransferUtility(this)

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

    override fun onGetResponse(response: Response<ZipCodeData>) {
        CommonUtil.hideProgress()
        if (response.body()?.success!!) {
            text_select_State.text = response.body()?.data?.state
            text_select_City.text = response.body()?.data?.city
            state_Selected = response.body()?.data?.state!!
            city_Selected = response.body()?.data?.city!!
            zip_Code = response.body()?.data?.zipCode!!
        } else {
            CommonUtil.showSnackbar(main_layout, response.body()?.message!!)
        }
    }

    override fun onGetFailure() {
        CommonUtil.hideProgress()
        CommonUtil.showSnackbar(main_layout, "something went wrong, please try again.")
    }

}