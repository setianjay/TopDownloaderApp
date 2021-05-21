package com.setianjay.topdownloaderapp

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class DownloadData: AsyncTask<String,Void,String>() {
    private val TAG = "DownloadData"
    override fun doInBackground(vararg params: String?): String {
        Log.d(TAG, "doInBackground: Start with ${params[0]}")
        val rsFeed = downloadData(params[0])
        if (rsFeed.isEmpty()){
            Log.d(TAG,"doInBackground: Error downloading")
        }
        return rsFeed
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        Log.d(TAG,"onPostExecute: $result")
    }

    private fun downloadData(urlPath: String?): String{
        val xmlResult = StringBuilder()

        try {
            val url = URL(urlPath)
            val connection = url.openConnection() as HttpURLConnection
            val response = connection.responseCode
            Log.d(TAG,"downloadData: Response code is $response")

            val reader: BufferedReader = BufferedReader(InputStreamReader(connection.inputStream))

            val inputBuffer = CharArray(500)
            var charsRead = 0
            while (charsRead >= 0){
                charsRead = reader.read(inputBuffer)
                if (charsRead > 0){
                    xmlResult.append(String(inputBuffer,0,charsRead))
                }
            }
            reader.close()
            Log.d(TAG,"Received ${xmlResult.length} bytes")
           return xmlResult.toString()

        }catch (e: MalformedURLException){
            Log.d(TAG,"downloadData: Invalid URL ${e.message}")
        }catch (e: IOException){
            Log.d(TAG,"downloadData: IO Exception ${e.message}")
        } catch (e: SecurityException){
            Log.d(TAG,"downloadData: Need permission ? ${e.message}")
        } catch (e: Exception){
            Log.d(TAG, "downloadData: Unknown error ${e.message}")
        }

        return ""
    }

}