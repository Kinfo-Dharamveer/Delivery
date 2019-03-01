package com.jointdelivery.fragments.pagerFragments

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.bumptech.glide.Glide
import com.jointdelivery.MyApplication
import com.jointdelivery.R
import com.jointdelivery.appviewmodule.loginactivities.ProfileEditActivity
import com.jointdelivery.appviewmodule.profileupdateApi.ProfileUpdateMethods
import com.jointdelivery.auth.AuthManager
import com.jointdelivery.utilities.*
import com.scyhealth.basemodule.interfaces.PostAPIResultCallback
import kotlinx.android.synthetic.main.dialog_choose_file_type.*
import kotlinx.android.synthetic.main.fragment_vehicle_info_layout.*
import retrofit2.Response
import java.io.File
import javax.inject.Inject

open class VehicleInfoFragment : Fragment(), View.OnClickListener, PostAPIResultCallback<CommonResponseModel> {

    @Inject
    lateinit var authManager: AuthManager

    override fun onResponse(response: Response<CommonResponseModel>) {
        CommonUtil.hideProgress()

        CommonUtil.showShortToast(context!!, response.body()!!.message)
        activity!!.finish()

    }

    override fun onFailure() {
        CommonUtil.showSnackbar(main_layout, resources.getString(R.string.something_wrong))
        CommonUtil.hideProgress()
    }

    lateinit var compressedPath: String
    lateinit var imageKey: String

    lateinit var imageUri: Uri

    var selected_vehicleType = "";
    var selected_vehicle_Brand = "";
    var selected_vehicle_year = "";
    var selected_vehicle_color = "";

    lateinit var frontPhotoUrl: String
    lateinit var backPhotoUrl: String

    lateinit var compressImage: CompressImage

    lateinit var transferUtility: TransferUtility
    lateinit var dialog: AlertDialog

    var isFrontImage: Boolean = false
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.choose_vehicle_frontPic -> {
                if (CommonUtil.isConnectingToInternet(activity!!)) {

                    if (!CommonUtil.hasPermissions(activity!!, Constants.CAMERA_PERMISSIONS)) {
                        ActivityCompat.requestPermissions(
                            activity!!,
                            Constants.CAMERA_PERMISSIONS,
                            Constants.PERMISSION_ALL
                        );
                    } else {
                        isFrontImage = true
                        openCustomDialog()
                    }

                } else {
                    CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_connect_internet))
                }
            }
            R.id.choose_vehicle_back_photo -> {
                if (CommonUtil.isConnectingToInternet(activity!!)) {

                    if (!CommonUtil.hasPermissions(activity!!, Constants.CAMERA_PERMISSIONS)) {
                        ActivityCompat.requestPermissions(
                            activity!!,
                            Constants.CAMERA_PERMISSIONS,
                            Constants.PERMISSION_ALL
                        );
                    } else {
                        isFrontImage = false
                        openCustomDialog()
                    }

                } else {
                    CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_connect_internet))
                }
            }
            R.id.save_button -> {
                if (CommonUtil.checkEmptyString(edt_vehicle_plate_no)) {
                    CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_enter_plate_no))
                    return
                }
                if (CommonUtil.checkEmptyString(edt_vehicle_licence_no)) {
                    CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_enter_licenece_no))
                    return
                }
                if (selected_vehicleType.equals("")) {
                    CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_enter_vehicle_type))
                    return
                }
                if (selected_vehicle_Brand.equals("")) {
                    CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_enter_vehicle_brand))
                    return
                }
                if (selected_vehicle_year.equals("")) {
                    CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_enter_vehicle_making_year))
                    return
                }
                if (selected_vehicle_color.equals("")) {
                    CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_enter_vehicle_color))
                    return
                }

                if (frontPhotoUrl.equals("")) {
                    CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_upload_front_pic))
                    return
                }

                if (backPhotoUrl.equals("")) {
                    CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_upload_back_pic))
                    return
                }

                if (CommonUtil.isConnectingToInternet(activity!!)) {
                    val hashVehicleDetails: HashMap<String, String> = HashMap<String, String>() //define empty hashmap
                    hashVehicleDetails.put("VehiclePlateNumber", edt_vehicle_plate_no.text.toString())
                    hashVehicleDetails.put("Id", authManager.getUserId().toString())
                    hashVehicleDetails.put("VehicleRegistrationNumber", edt_vehicle_licence_no.text.toString())
                    hashVehicleDetails.put("VehicleBrand", selected_vehicle_Brand)
                    hashVehicleDetails.put("VehicleMakingYear", selected_vehicle_year)
                    hashVehicleDetails.put("VehicleColour", selected_vehicle_color)
                    hashVehicleDetails.put("VehicleType", selected_vehicleType)
                    hashVehicleDetails.put("VehicleBackPhoto", backPhotoUrl)
                    hashVehicleDetails.put("VehicleFrontPhoto", frontPhotoUrl)
                    hashVehicleDetails.put("AccountId", "2")
                    hashVehicleDetails.put("Id", authManager.getUserId().toString())

                    CommonUtil.showProgress(context!!)
                    ProfileUpdateMethods.updateVehicleDetails(this, authManager.getAccessToken(), hashVehicleDetails)

                } else {
                    CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_connect_internet))
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
            else -> {

            }

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity!!.application as MyApplication).getAppComponent()?.inject(this)

        return inflater.inflate(R.layout.fragment_vehicle_info_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        initData()
        initClicks()
        spinnerSelectionInit()
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

    private fun initClicks() {

        save_button.setOnClickListener(this)
        choose_vehicle_frontPic.setOnClickListener(this)
        choose_vehicle_back_photo.setOnClickListener(this)

//        main_layout.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                when (event?.action) {
//                    MotionEvent.ACTION_DOWN ->
//                        CommonUtil.hideKeyboard(v!!, activity!!)
//                }
//
//                return v?.onTouchEvent(event) ?: true
//            }
//        })
    }

    var profileDetail: HashMap<String, String>? = null
    private fun initData() {
        compressImage = CompressImage()
        frontPhotoUrl = ""
        backPhotoUrl = ""
        isFrontImage = false

        /* vehicleType
         vehiclePlateNumber
         vehicleBrand
         vehicleColour
         vehicleMakingYear*/
        profileDetail = arguments?.getSerializable("profileDetail") as HashMap<String, String>

        edt_vehicle_plate_no.setText(profileDetail?.get("vehiclePlateNumber"))
        edt_vehicle_licence_no.setText(profileDetail?.get("licenseNumber"))
        frontPhotoUrl = profileDetail?.get("vehicleFrontPhoto")!!
        backPhotoUrl = profileDetail?.get("vehicleBackPhoto")!!

        Glide.with(this)
            .load(backPhotoUrl)
            .into(image_back)


        Glide.with(this)
            .load(frontPhotoUrl)
            .into(image_front)

    }

    private fun spinnerSelectionInit() {

        spinner_vehicle_Type.adapter =
            ArrayAdapter<String>(context, R.layout.spinner_item, resources.getStringArray(R.array.spinner_vehicle_Type))
        spinner_brand.adapter =
            ArrayAdapter<String>(
                context,
                R.layout.spinner_item,
                resources.getStringArray(R.array.spinner_vehicle_Brand)
            )
        spinner_making_year.adapter =
            ArrayAdapter<String>(context, R.layout.spinner_item, resources.getStringArray(R.array.spinner_vehicle_year))
        spinner_color.adapter =
            ArrayAdapter<String>(
                context,
                R.layout.spinner_item,
                resources.getStringArray(R.array.spinner_vehicle_color)
            )

        for (i in 0 until resources.getStringArray(R.array.spinner_vehicle_Type).size) {
            if (resources.getStringArray(R.array.spinner_vehicle_Type).get(i).equals(profileDetail?.get("vehicleType")))
                spinner_vehicle_Type.setSelection(i)
        }

        for (i in 0 until resources.getStringArray(R.array.spinner_vehicle_Brand).size) {
            if (resources.getStringArray(R.array.spinner_vehicle_Brand).get(i).equals(profileDetail?.get("vehicleBrand")))
                spinner_brand.setSelection(i)
        }

        for (i in 0 until resources.getStringArray(R.array.spinner_vehicle_color).size) {
            if (resources.getStringArray(R.array.spinner_vehicle_color).get(i).equals(profileDetail?.get("vehicleColour")))
                spinner_color.setSelection(i)
        }

        for (i in 0 until resources.getStringArray(R.array.spinner_vehicle_year).size) {
            if (resources.getStringArray(R.array.spinner_vehicle_year).get(i).equals(profileDetail?.get("vehicleMakingYear")))
                spinner_making_year.setSelection(i)
        }

        spinner_vehicle_Type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0)
                    selected_vehicleType = spinner_vehicle_Type.selectedItem.toString();
            }

        }

        spinner_brand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0)
                    selected_vehicle_Brand = spinner_brand.selectedItem.toString();
            }
        }

        spinner_making_year.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0)

                    selected_vehicle_year = spinner_making_year.selectedItem.toString()
            }

        }

        spinner_color.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0)
                    selected_vehicle_color = spinner_color.selectedItem.toString();
            }

        }
    }

    var picturePath = ""
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

                if (isFrontImage) {
                    image_front.setImageBitmap(bmp)
                } else {

                    image_back.setImageBitmap(bmp)
                }


                beginUpload(compressedPath)
                try {
                    imageKey = compressedPath.substring(compressedPath.lastIndexOf("/") + 1)
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (requestCode == 1) {
            try {
                picturePath = CommonUtil.getRealPathFromURI(imageUri, activity!!);
                compressedPath = compressImage.compressImage(activity!!, picturePath)
                val bmp = BitmapFactory.decodeFile(compressedPath)

                if (isFrontImage) {
                    image_front.setImageBitmap(bmp)
                } else {
                    image_back.setImageBitmap(bmp)
                }
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
        CommonUtil.showProgress(activity!!)

        if (filePath == null) {
            CommonUtil.showSnackbar(main_layout, "Could not find the filepath of the selected file")
            return
        }

        val file = File(filePath)

        transferUtility = AWSUtils.getTransferUtility(activity!!)

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
                if (isFrontImage) {
                    frontPhotoUrl = "http://s3-eu-west-1.amazonaws.com/scyphirhealth/$imageKey"
                } else {
                    backPhotoUrl = "http://s3-eu-west-1.amazonaws.com/scyphirhealth/$imageKey"
                }
            }
        }
    }
}