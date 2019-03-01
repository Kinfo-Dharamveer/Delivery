package com.jointdelivery.appviewmodule.registeractivities

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.jointdelivery.MyApplication
import com.jointdelivery.R
import com.jointdelivery.appviewmodule.loginactivities.LoginActivity
import com.jointdelivery.basemodule.activities.BaseActivity
import com.jointdelivery.utilities.*
import com.scyhealth.basemodule.interfaces.PostAPIResultCallback
import com.scyhealth.loginmodule.LoginApiMethods
import kotlinx.android.synthetic.main.activity_vehicle_detail_layout.*
import kotlinx.android.synthetic.main.dialog_choose_file_type.*
import kotlinx.android.synthetic.main.tool_bar_layout.*
import retrofit2.Response
import java.io.File

open class VehicleDetailActivity : AppCompatActivity(), PostAPIResultCallback<CommonResponseModel> {
    override fun onResponse(response: Response<CommonResponseModel>) {
        CommonUtil.hideProgress()
        if (response.body()!!.success) {
            CommonUtil.showShortToast(this, response.body()!!.message)
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finishAffinity()
        } else {
            CommonUtil.showSnackbar(main_layout, response.body()!!.message)
        }
    }

    override fun onFailure() {
        CommonUtil.hideProgress()
        CommonUtil.showSnackbar(main_layout, resources.getString(R.string.something_wrong))
    }

    var selected_vehicleType = "";
    var selected_vehicle_Brand = "";
    var selected_vehicle_year = "";
    var selected_vehicle_color = "";
    lateinit var compressedPath: String
    lateinit var imageKey: String

    lateinit var imageUri: Uri


    lateinit var frontPhotoUrl: String
    lateinit var backPhotoUrl: String

    lateinit var compressImage: CompressImage

    lateinit var transferUtility: TransferUtility
    lateinit var dialog: AlertDialog

    var isFrontImage: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_detail_layout)
        (application as MyApplication).getAppComponent()?.inject(this)
        spinnerSelectionInit()
        toolbar_title.text = resources.getString(R.string.vehicle_detail)
        compressImage = CompressImage()
        frontPhotoUrl = ""
        backPhotoUrl = ""
        isFrontImage = false

        back_button.setOnClickListener { finish() }
//        main_layout.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                when (event?.action) {
//                    MotionEvent.ACTION_DOWN ->
//                        CommonUtil.hideKeyboard(v!!, applicationContext)
//                }
//
//                return v?.onTouchEvent(event) ?: true
//            }
//        })
    }

    fun FrontPhotoPic(view: View) {
        if (CommonUtil.isConnectingToInternet(this)) {

            if (!CommonUtil.hasPermissions(this, Constants.CAMERA_PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, Constants.CAMERA_PERMISSIONS, Constants.PERMISSION_ALL);
            } else {
                isFrontImage = true
                openCustomDialog()
            }

        } else {
            CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_connect_internet))
        }
    }

    fun BackPhotoPic(view: View) {
        if (CommonUtil.isConnectingToInternet(this)) {

            if (!CommonUtil.hasPermissions(this, Constants.CAMERA_PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, Constants.CAMERA_PERMISSIONS, Constants.PERMISSION_ALL);
            } else {
                isFrontImage = false
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


    fun onGalleryClick(view: View) {

        dialog.dismiss()
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhotoIntent, 2)

    }


    fun continueVechiclRegisteration(view: View) {
        if (CommonUtil.isConnectingToInternet(applicationContext)) {
            if (CommonUtil.checkEmptyString(edit_text_vehiclePlateNo)) {
                CommonUtil.showSnackbar(root_main, resources.getString(R.string.please_enter_plate_no))
                return
            }
            if (CommonUtil.checkEmptyString(edit_text_vehicle_LicenceNo)) {
                CommonUtil.showSnackbar(root_main, resources.getString(R.string.please_enter_licenece_no))
                return
            }
            if (selected_vehicleType.equals("")) {
                CommonUtil.showSnackbar(root_main, resources.getString(R.string.please_enter_vehicle_type))
                return
            }
            if (selected_vehicle_Brand.equals("")) {
                CommonUtil.showSnackbar(root_main, resources.getString(R.string.please_enter_vehicle_brand))
                return
            }
            if (selected_vehicle_year.equals("")) {
                CommonUtil.showSnackbar(root_main, resources.getString(R.string.please_enter_vehicle_making_year))
                return
            }
            if (selected_vehicle_color.equals("")) {
                CommonUtil.showSnackbar(root_main, resources.getString(R.string.please_enter_vehicle_color))
                return
            }

            if (frontPhotoUrl.equals("")) {
                CommonUtil.showSnackbar(root_main, resources.getString(R.string.please_upload_front_pic))
                return
            }

            if (backPhotoUrl.equals("")) {
                CommonUtil.showSnackbar(root_main, resources.getString(R.string.please_upload_back_pic))
                return
            }


            if (intent != null && intent.hasExtra("hashDriverInfo")) {
                val hashDriverInfo = intent.getSerializableExtra("hashDriverInfo") as HashMap<String, String>

                val hashVehicleDetails: HashMap<String, String> = HashMap() //define empty hashmap
                hashVehicleDetails.putAll(hashDriverInfo)
                hashVehicleDetails.put("VehiclePlateNumber", edit_text_vehiclePlateNo.text.toString())
                hashVehicleDetails.put("VehicleRegistrationNumber", edit_text_vehicle_LicenceNo.text.toString())
                hashVehicleDetails.put("VehicleBrand", selected_vehicle_Brand)
                hashVehicleDetails.put("VehicleMakingYear", selected_vehicle_year)
                hashVehicleDetails.put("VehicleColour", selected_vehicle_color)
                hashVehicleDetails.put("VehicleType", selected_vehicleType)
                hashVehicleDetails.put("BackPhotoUrl", backPhotoUrl)
                hashVehicleDetails.put("FrontPhotoUrl", frontPhotoUrl)
                hashVehicleDetails.put("AccountId", "2")

                CommonUtil.showProgress(this)
                LoginApiMethods.registerUserInfo(this, hashVehicleDetails)
            }
        } else {
            CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_connect_internet))
        }


    }

    var picturePath = ""
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
                if (isFrontImage)
                    image_fromPhoto.setImageBitmap(bmp)
                else
                    image_backPhoto.setImageBitmap(bmp)
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
                picturePath = CommonUtil.getRealPathFromURI(imageUri, applicationContext);
                compressedPath = compressImage.compressImage(this, picturePath)
                val bmp = BitmapFactory.decodeFile(compressedPath)

                if (isFrontImage)
                    image_fromPhoto.setImageBitmap(bmp)
                else
                    image_backPhoto.setImageBitmap(bmp)

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

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun beginUpload(filePath: String?) {
        CommonUtil.showProgress(this)

        if (filePath == null) {
            CommonUtil.showSnackbar(main_layout, "Could not find the filepath of the selected file")
            return
        }

        val file = File(filePath)

        transferUtility = AWSUtils.getTransferUtility(this)

        val observer = transferUtility.upload(Constants.BUCKET_NAME, file.getName(), file)

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
                if (isFrontImage)
                    frontPhotoUrl = "http://s3-eu-west-1.amazonaws.com/scyphirhealth/$imageKey"
                else
                    backPhotoUrl = "http://s3-eu-west-1.amazonaws.com/scyphirhealth/$imageKey"
            }
        }
    }

    private fun spinnerSelectionInit() {

        spinner_VehicleType.adapter =
            ArrayAdapter<String>(this, R.layout.spinner_item, resources.getStringArray(R.array.spinner_vehicle_Type))
        spinner_select_vehicle_Brand.adapter =
            ArrayAdapter<String>(this, R.layout.spinner_item, resources.getStringArray(R.array.spinner_vehicle_Brand))
        spinner_vehicle_year.adapter =
            ArrayAdapter<String>(this, R.layout.spinner_item, resources.getStringArray(R.array.spinner_vehicle_year))
        spinner_vehicle_color.adapter =
            ArrayAdapter<String>(this, R.layout.spinner_item, resources.getStringArray(R.array.spinner_vehicle_color))

        spinner_VehicleType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0)
                    selected_vehicleType = spinner_VehicleType.selectedItem.toString();
            }

        }




        spinner_select_vehicle_Brand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0)
                    selected_vehicle_Brand = spinner_select_vehicle_Brand.selectedItem.toString();
            }

        }



        spinner_vehicle_year.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0)

                    selected_vehicle_year = spinner_vehicle_year.selectedItem.toString()
            }

        }

        spinner_vehicle_color.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0)
                    selected_vehicle_color = spinner_vehicle_color.selectedItem.toString();
            }

        }
    }
}