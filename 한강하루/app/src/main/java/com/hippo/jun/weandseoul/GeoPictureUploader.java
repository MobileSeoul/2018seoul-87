package com.hippo.jun.weandseoul;

/**
 * Created by JUN on 2018-09-03.
 */

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class GeoPictureUploader
{
    static String serviceDomain = "http://tkstka0023.cafe24.com";
    static String postUrl = serviceDomain + "/UploadToServer.php";

    static String CRLF = "\r\n";
    static String twoHyphens = "--";
    static String boundary = "*****mgd*****";

    private String pictureFileName = null;
    private String user_id = null;
    private String user_text = null;
    private String user_image = null;
    private String user_num = null;
    private DataOutputStream dataStream = null;

    enum ReturnCode { noPicture, unknown, http201, http400, http401, http403, http404, http500};

    public GeoPictureUploader(String user_id, String user_text, String user_image, String user_num)
    {
        this.user_id = user_id;
        this.user_text = user_text;
        this.user_image = user_image;
        this.user_num = user_num;
    }

    public static void setServiceDomain(String domainName)
    {
        serviceDomain = domainName;
    }

    public static String getServiceDomain()
    {
        return serviceDomain;
    }

    public String uploadPicture(String pictureFileName) {

        this.pictureFileName = pictureFileName;
        File uploadFile = new File(pictureFileName);

        if (uploadFile.exists())
            try
            {
                FileInputStream fileInputStream = new FileInputStream(uploadFile);

                URL connectURL = new URL(postUrl);
                HttpURLConnection conn = (HttpURLConnection)connectURL.openConnection();
                conn.setConnectTimeout(8000);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");

                conn.setRequestProperty("Connection","Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type","multipart/form-data;boundary="+boundary);

                conn.connect();

                dataStream = new DataOutputStream(conn.getOutputStream());

                writeFormField("user_id", user_id);
                writeFormField("user_text", user_text);
                writeFormField("user_image", user_image);
                writeFormField("user_num", user_num);
                writeFileField("uploaded_file", pictureFileName, "image/jpg", fileInputStream);

                // final closing boundary line
                dataStream.writeBytes(twoHyphens + boundary + twoHyphens + CRLF);

                fileInputStream.close();
                dataStream.flush();
                dataStream.close();
                dataStream = null;

                String response = getResponse(conn);
                int responseCode = conn.getResponseCode();

                if (response.contains("uploaded successfully")){
                    Log.e("uploaded successfully", "uploaded successfully: ");
                    return "ReturnCode.http201";}
                else{
                    // for now assume bad name/password
                    return "ReturnCode.http401";
                }
            }
            catch (MalformedURLException mue) {
                Log.e("MalformedURLException", "error: " + mue.getMessage(), mue);
//                System.out.println("GeoPictureUploader.uploadPicture: Malformed URL: " + mue.getMessage());
                return "ReturnCode.http400";
            }
            catch (IOException ioe) {
                Log.e("IOException", "error: " + ioe.getMessage(), ioe);
//                System.out.println("GeoPictureUploader.uploadPicture: IOE: " + ioe.getMessage());
                return "ReturnCode.http500";
            }
            catch (Exception e) {
                Log.e("Exception", "error: " + e.getMessage());
//                System.out.println("GeoPictureUploader.uploadPicture: unknown: " + e.getMessage());
                return "ReturnCode.unknown";
            }
        else
        {
            Log.e("noPicture", "error: ");
            return "ReturnCode.noPicture";
        }
    }

    /**
     * @param conn
     * @return
     */
    private String getResponse(HttpURLConnection conn)
    {
        try
        {
            DataInputStream dis = new DataInputStream(conn.getInputStream());
            byte []        data = new byte[1024];
            int             len = dis.read(data, 0, 1024);

            dis.close();
            int responseCode = conn.getResponseCode();

            if (len > 0)
                return new String(data, 0, len);
            else
                return "";
        }
        catch(Exception e)
        {
            System.out.println("GeoPictureUploader: biffed it getting HTTPResponse");
            //Log.e(TAG, "GeoPictureUploader: biffed it getting HTTPResponse");
            return "";
        }
    }

    /**
     *  this mode of reading response no good either
     */
    private String getResponseOrig(HttpURLConnection conn)
    {
        InputStream is = null;
        try
        {
            is = conn.getInputStream();
            // scoop up the reply from the server
            int ch;
            StringBuffer sb = new StringBuffer();
            while( ( ch = is.read() ) != -1 ) {
                sb.append( (char)ch );
            }
            return sb.toString();   // TODO Auto-generated method stub
        }
        catch(Exception e)
        {
            System.out.println("GeoPictureUploader: biffed it getting HTTPResponse");
            //Log.e(TAG, "GeoPictureUploader: biffed it getting HTTPResponse");
        }
        finally
        {
            try {
                if (is != null)
                    is.close();
            } catch (Exception e) {}
        }

        return "";
    }

    /**
     * write one form field to dataSream
     * @param fieldName
     * @param fieldValue
     */
    private void writeFormField(String fieldName, String fieldValue)
    {
        try
        {
            dataStream.writeBytes(twoHyphens + boundary + CRLF);
            dataStream.writeBytes("Content-Disposition: form-data; name=\"" + fieldName + "\"" + CRLF);
            dataStream.writeBytes(CRLF);
            dataStream.writeBytes(URLEncoder.encode (fieldValue, "utf-8"));
            dataStream.writeBytes(CRLF);
        }
        catch(Exception e)
        {
            System.out.println("GeoPictureUploader.writeFormField: got: " + e.getMessage());
            //Log.e(TAG, "GeoPictureUploader.writeFormField: got: " + e.getMessage());
        }
    }

    /**
     * write one file field to dataStream
     * @param fieldName - name of file field
     * @param fieldValue - file name
     * @param type - mime type
//     * @param fileInputStream - stream of bytes that get sent up
     */
    private void writeFileField(
            String fieldName,
            String fieldValue,
            String type,
            FileInputStream fis)
    {
        try
        {
            // opening boundary line
            dataStream.writeBytes(twoHyphens + boundary + CRLF);
            dataStream.writeBytes("Content-Disposition: form-data; name=\""
                    + fieldName
                    + "\";filename=\""
                    + fieldValue
                    + "\""
                    + CRLF);
            dataStream.writeBytes("Content-Type: " + type +  CRLF);
            dataStream.writeBytes(CRLF);

            // create a buffer of maximum size
            int bytesAvailable = fis.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            // read file and write it into form...
            int bytesRead = fis.read(buffer, 0, bufferSize);
            while (bytesRead > 0)
            {
                dataStream.write(buffer, 0, bufferSize);
                bytesAvailable = fis.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fis.read(buffer, 0, bufferSize);
            }

            // closing CRLF
            dataStream.writeBytes(CRLF);
        }
        catch(Exception e)
        {
            System.out.println("GeoPictureUploader.writeFormField: got: " + e.getMessage());
            //Log.e(TAG, "GeoPictureUploader.writeFormField: got: " + e.getMessage());
        }
    }

    /**
     * @param args
//     */
//    public static void main(String[] args)
//    {
//        if (args.length >= 0)
//        {
//            GeoPictureUploader gpu = new GeoPictureUploader("john", "notmyrealpassword");
//            String picName = args[0];
//
//            ReturnCode rc = gpu.uploadPicture(picName);
//            System.out.printf("done");
//        }
//    }

}