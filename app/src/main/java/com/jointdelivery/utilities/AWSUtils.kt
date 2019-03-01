package com.jointdelivery.utilities

import android.content.Context
import android.util.Log
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client

class AWSUtils {

    companion object {

        // We only need one instance of the clients and credentials provider
        private var sS3Client: AmazonS3Client? = null
        private var sCredProvider: CognitoCachingCredentialsProvider? = null
        private var sTransferUtility: TransferUtility? = null
        private val TAG = "AWSUtil"


        private fun getCredProvider(context: Context): CognitoCachingCredentialsProvider {
            if (sCredProvider == null) {
                sCredProvider = CognitoCachingCredentialsProvider(
                    context.applicationContext,
                    Constants.COGNITO_POOL_ID,
                    Regions.fromName(Constants.BUCKET_REGION)
                )
            }
            return sCredProvider as CognitoCachingCredentialsProvider
        }
        /**
         * Gets an instance of a S3 client which is constructed using the given
         * Context.
         *
         * @param context An Context instance.
         * @return A default S3 client.
         */
        fun getS3Client(context: Context): AmazonS3Client {
            if (sS3Client == null) {
                sS3Client = AmazonS3Client(getCredProvider(context.applicationContext))
                sS3Client!!.setRegion(Region.getRegion(Regions.fromName(Constants.BUCKET_REGION)))
            }
            return sS3Client as AmazonS3Client
        }

        fun getTransferUtility(context: Context): TransferUtility {
            if (sTransferUtility == null) {
                sTransferUtility = TransferUtility(
                    getS3Client(context.applicationContext),
                    context.applicationContext
                )
            }

            return sTransferUtility as TransferUtility
        }

        /**
         * Converts number of bytes into proper scale.
         *
         * @param bytes number of bytes to be converted.
         * @return A string that represents the bytes in a proper scale.
         */
        fun getBytesString(bytes: Long): String {
            val quantifiers = arrayOf("KB", "MB", "GB", "TB")
            var speedNum = bytes.toDouble()
            var i = 0
            while (true) {
                if (i >= quantifiers.size) {
                    return ""
                }
                speedNum /= 1024.0
                if (speedNum < 512) {
                    return String.format("%.2f", speedNum) + " " + quantifiers[i]
                }
                i++
            }
        }

        /*
     * Fills in the map with information in the observer so that it can be used
     * with a SimpleAdapter to populate the UI
     */
        fun fillMap(map: MutableMap<String, Any>, observer: TransferObserver, image: Int) {
            val progress = (observer.bytesTransferred.toDouble() * 100 / observer.bytesTotal).toInt()
            Log.d(TAG, "fillMap: " + observer.id)
            map["id"] = observer.id
            map["image"] = image
            map["fileName"] = observer.absoluteFilePath
            map["progress"] = progress
            map["bytes"] = getBytesString(observer.bytesTransferred) + "/" + getBytesString(observer.bytesTotal)
            map["state"] = observer.state
            map["percentage"] = progress.toString() + "%"
        }
    }

}