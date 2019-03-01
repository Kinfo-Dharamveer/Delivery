package com.jointdelivery.utilities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore

import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.jointdelivery.auth.AuthManager
import com.jointdelivery.appviewmodule.model.LoginModel
import com.jointdelivery.progressutils.SpotsDialog
import retrofit2.Response
import java.util.logging.Logger
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class CommonUtil {
    companion object {
        var progress: AlertDialog? = null
        private val REQUEST_ID_MULTIPLE_PERMISSIONS = 1001

        fun showSnackbar(view: View, message: String) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        }

        fun showShortToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        fun showLog(tag: String, message: String) {
            val Log = Logger.getLogger(tag)
            Log.warning(message)
        }

        fun checkValidEmail(value: EditText): Boolean {
            return Constants.EMAIL_ADDRESS_PATTERN.matcher(value.text.toString().trim()).matches()
        }

        fun checkValidPassword(value: EditText): Boolean {
            return Constants.PASSWORD_PATTERN.matcher(value.text.toString().trim()).matches()
        }

        fun fieldValue(field: EditText): String {
            return field.text.trim().toString()
        }

        fun checkEmptyString(value: EditText): Boolean {
            return value.text.toString().trim().equals("")
        }

        fun checkPasswordLength(value: EditText): Boolean {
            return value.text.toString().trim().length < 6
        }

        fun isConnectingToInternet(context: Context): Boolean {
            val connectionDetector = ConnectionDetector(context)
            return connectionDetector.isConnectingToInternet()
        }

        fun showProgress(context: Context) {
            progress = SpotsDialog.Builder()
                .setContext(context)
                .setMessage("Please wait")
                .build()

            progress?.show()
        }

        fun hideProgress() {
            progress?.dismiss()
        }

        fun saveLoginSession(authManager: AuthManager, response: Response<LoginModel>) {
            authManager.setAccessToken(response.body()!!.accessToken.toString())
            authManager.setRefreshToken(response.body()!!.refreshToken.toString())
            authManager.setUserId(response.body()!!.userId!!.toInt())
            authManager.setFirstName(response.body()!!.firstName.toString())
            authManager.setLastName(response.body()!!.lastName.toString())
            authManager.setEmail(response.body()!!.userName.toString())
            authManager.setRole(response.body()!!.role.toString())

        }
        fun hideKeyboard(view: View, context: Context) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }
        fun showKeyboard(view: View, context: Context) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }
        fun hasPermissions(context: Context?,  permissions: Array<String>): Boolean {
            if (context != null && permissions != null) {
                for (permission in permissions) {
                    if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                        return false
                    }
                }
            }
            return true
        }
        fun getRealPathFromURI(contentUri: Uri,context: Context): String {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val cursor =context.contentResolver.query(contentUri, proj, null, null, null)
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        }


        fun checkPermissions(activity: Activity): Boolean {
            val cameraPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
            val writePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val readPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)

            val listPermissionsNeeded = ArrayList<String>()

            if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA)
            }
            if (writePermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            if (readPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toTypedArray(), REQUEST_ID_MULTIPLE_PERMISSIONS)
                return false
            }
            return true
        }
    }






    fun checkEmptyString(value: EditText): Boolean {
        return value.text.toString().trim().equals("")
    }

}
