package com.airshiplay.framework.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FWUtils
{
  public static final String LOG_CAT = "WLDroidGap";
  public static final int ANDROID_BUFFER_8K = 8192;
  public static final String WL_PREFS = "WLPrefs";
  public static final String WL_CHALLENGE_DATA = "WL-Challenge-Data";
  public static final String WL_CHALLENGE_RESPONSE_DATA = "WL-Challenge-Response-Data";
  public static final String WL_INSTANCE_AUTH_ID = "WL-Instance-Id";

  public static Drawable scaleImage(Drawable drawable, float scaleWidth, float scaleHeight) {
    Drawable resizedDrawable = null;
    if (drawable != null) {
      Bitmap bitmapOrg = ((BitmapDrawable)drawable).getBitmap();
      int width = bitmapOrg.getWidth();
      int height = bitmapOrg.getHeight();

      Matrix matrix = new Matrix();

      matrix.postScale(scaleWidth, scaleHeight);
      Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width, height, matrix, true);

      resizedDrawable = new BitmapDrawable(resizedBitmap);
    }
    return resizedDrawable;
  }


  public static String getResourceString(String recourceName, Activity context)
  {
    Class rStringClass = null;
    try
    {
      if (rStringClass == null) {
        rStringClass = Class.forName(new StringBuilder().append(context.getPackageName()).append(".R$string").toString());
      }
      return context.getResources().getString(((Integer)rStringClass.getDeclaredField(recourceName).get(null)).intValue());
    } catch (Exception e) {
      Log.e(context.getPackageName(), e.getMessage(), e);
      context.finish();
    }return "";
  }

  public static long getFreeSpaceOnDevice()
  {
    File path = Environment.getDataDirectory();
    StatFs stat = new StatFs(path.getPath());
    long blockSize = stat.getBlockSize();
    long availableBlocks = stat.getAvailableBlocks();
    long availableStorageInBytes = blockSize * availableBlocks;
    return availableStorageInBytes;
  }

  public static void copyFile(File in, File out)
    throws IOException
  {
    FileInputStream fis = new FileInputStream(in);
    if (!out.exists()) {
      if (in.isDirectory()) {
        out.mkdirs();
      } else {
        File parentDir = new File(out.getParent());
        if (!parentDir.exists()) {
          parentDir.mkdirs();
        }
        out.createNewFile();
      }
    }
    FileOutputStream fos = new FileOutputStream(out);
    try {
      byte[] buf = new byte[8192];
      int i = 0;
      while ((i = fis.read(buf)) != -1)
        fos.write(buf, 0, i);
    }
    catch (IOException e) {
      throw e;
    } finally {
      fis.close();
      fos.close();
    }
  }

  public static void copyFile(InputStream in, OutputStream out)
    throws IOException
  {
    byte[] buffer = new byte[8192];
    int read;
    while ((read = in.read(buffer)) != -1) {
      out.write(buffer, 0, read);
    }
    out.flush();
  }

  public static boolean deleteDirectory(File directory)
    throws Exception
  {
    try
    {
      if (directory.exists()) {
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++)
          if (files[i].isDirectory())
            deleteDirectory(files[i]);
          else
            files[i].delete();
      }
    }
    catch (Exception e)
    {
      throw new Exception(new StringBuilder().append("Error occurred while trying to delete directory ").append(directory).toString());
    }
    return directory.delete();
  }

  public static String convertStreamToString(InputStream is)
  {
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuilder sb = new StringBuilder();

    String line = null;
    try {
      while ((line = reader.readLine()) != null)
        sb.append(new StringBuilder().append(line).append("\n").toString());
    }
    catch (IOException e) {
      throw new RuntimeException(new StringBuilder().append("Error reding input stream (").append(is).append(").").toString(), e);
    } finally {
      try {
        is.close();
      } catch (IOException e) {
        debug(new StringBuilder().append("Failed to close input stream because ").append(e.getMessage()).toString(), e);
      }
    }
    return sb.toString();
  }

  public static void showDialog(Context context, String title, String message, String buttonText) {
    showDialog(context, title, message, buttonText, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
  }

  public static void showDialog(Context context, String title, String message, String buttonText, DialogInterface.OnClickListener buttonClickListener) {
    AlertDialog.Builder dlg = new AlertDialog.Builder(context);
    dlg.setTitle(title);
    dlg.setMessage(message);
    dlg.setCancelable(false);
    dlg.setPositiveButton(buttonText, buttonClickListener);
    dlg.create();
    dlg.show();
  }

  public static void debug(String msg) {
    Log.d("WLDroidGap", getMsgNotNull(msg));
  }

  public static void error(String msg) {
    Log.e("WLDroidGap", getMsgNotNull(msg));
  }

  public static void warning(String msg) {
    Log.w("WLDroidGap", getMsgNotNull(msg));
  }

  public static void info(String msg) {
    Log.i("WLDroidGap", getMsgNotNull(msg));
  }

  private static String getMsgNotNull(String msg)
  {
    if (msg == null) {
      return "null";
    }
    return msg;
  }

  public static void debug(String msg, Throwable throwable) {
    Log.d("WLDroidGap", getMsgNotNull(msg), throwable);
  }

  public static void error(String msg, Throwable throwable) {
    Log.e("WLDroidGap", getMsgNotNull(msg), throwable);
  }

  public static void warning(String msg, Throwable throwable) {
    Log.w("WLDroidGap", getMsgNotNull(msg), throwable);
  }

  public static void info(String msg, Throwable throwable) {
    Log.i("WLDroidGap", getMsgNotNull(msg), throwable);
  }

  public static void log(String msg, int logLevel) {
    Log.println(logLevel, "WLDroidGap", getMsgNotNull(msg));
  }

  public static void writeWLPref(Context context, String prefName, String PrefValue) {
    SharedPreferences prefs = context.getSharedPreferences("WLPrefs", 0);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putString(prefName, PrefValue);
    editor.commit();
  }

  public static String readWLPref(Context context, String prefName) {
    SharedPreferences prefs = context.getSharedPreferences("WLPrefs", 0);
    return prefs.getString(prefName, null);
  }

  public static void clearWLPref(Context context) {
    SharedPreferences prefs = context.getSharedPreferences("WLPrefs", 0);
    SharedPreferences.Editor editor = prefs.edit();
    editor.clear();
    editor.commit();
  }

  public static void unpack(InputStream in, File targetDir) throws IOException {
    ZipInputStream zin = new ZipInputStream(in);
    ZipEntry entry;
    while ((entry = zin.getNextEntry()) != null) {
      String extractFilePath = entry.getName();
      if ((extractFilePath.startsWith("/")) || (extractFilePath.startsWith("\\"))) {
        extractFilePath = extractFilePath.substring(1);
      }
      File extractFile = new File(new StringBuilder().append(targetDir.getPath()).append(File.separator).append(extractFilePath).toString());
      if (entry.isDirectory()) {
        if (!extractFile.exists())
          extractFile.mkdirs();
      }
      else
      {
        File parent = extractFile.getParentFile();
        if (!parent.exists()) {
          parent.mkdirs();
        }

        FileOutputStream os = new FileOutputStream(extractFile);
        copyFile(zin, os);
        os.flush();
        os.close();
      }
    }
  }




  public static List<File> getTree(File rootDir) {
    List files = new ArrayList();
    return getTree(rootDir, files);
  }

  private static List<File> getTree(File rootDir, List<File> files) {
    File[] filesToIterate = rootDir.listFiles();
    for (File file : filesToIterate) {
      if (file.isDirectory())
        getTree(file, files);
      else {
        files.add(file);
      }
    }

    return files;
  }

  public static byte[] read(File file) throws IOException
  {
    byte[] buffer = new byte[(int)file.length()];
    InputStream ios = null;
    try {
      ios = new FileInputStream(file);
      if (ios.read(buffer) == -1)
        throw new IOException("EOF reached while trying to read the whole file");
    }
    finally {
      try {
        if (ios != null)
          ios.close();
      }
      catch (IOException e)
      {
      }
    }
    return buffer;
  }


  public static JSONObject convertStringToJSON(String jsonString)
    throws JSONException
  {
    String secureJSONString = jsonString.substring(jsonString.indexOf("{"), jsonString.lastIndexOf("}") + 1);
    JSONObject jsonObject = new JSONObject(secureJSONString);
    return jsonObject;
  }

  public static List<String> convertJSONArrayToList(JSONArray jsonArray)
  {
    List listToReturn = new ArrayList();
    for (int i = 0; i < jsonArray.length(); i++) {
      try {
        listToReturn.add((String)jsonArray.get(i));
      } catch (JSONException e) {
        throw new RuntimeException(e);
      }
    }
    return listToReturn;
  }

  public static boolean isStringEmpty(String s) {
    return (s == null) || (s.length() == 0);
  }

  public static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      data[(i / 2)] = (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
    }

    return data;
  }

  public static String byteArrayToHexString(byte[] bytes) {
    StringBuilder sb = new StringBuilder(bytes.length * 2);

    Formatter formatter = new Formatter(sb);
    for (byte b : bytes) {
      formatter.format("%02x", new Object[] { Byte.valueOf(b) });
    }

    return sb.toString();
  }
}
