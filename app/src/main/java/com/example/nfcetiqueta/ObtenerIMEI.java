package com.example.nfcetiqueta;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class ObtenerIMEI {

    public static String getDeviceId(Context context) {
        String deviceId = readDeviceIdFromFile(context);

        if (deviceId == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            } else {
                final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (mTelephony.getDeviceId() != null) {
                    deviceId = mTelephony.getDeviceId();
                } else {
                    deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                }
            }

            if (deviceId != null) {
                saveDeviceIdToFile(context, deviceId);
            }
        }

        return deviceId;
    }

    private static String readDeviceIdFromFile(Context context) {
        String deviceId = null;
        FileInputStream fis = null;
        BufferedReader reader = null;

        try {
            File file = new File(context.getExternalFilesDir(null), "device_id.txt");
            fis = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(fis));
            deviceId = reader.readLine();
        } catch (Exception e) {
            Log.e("ObtenerIMEI", "Error al leer el archivo de identificación del dispositivo", e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                Log.e("ObtenerIMEI", "Error al cerrar los flujos de lectura del archivo", e);
            }
        }

        return deviceId;
    }

    private static void saveDeviceIdToFile(Context context, String deviceId) {
        FileOutputStream fos = null;

        try {
            File file = new File(context.getExternalFilesDir(null), "device_id.txt");
            fos = new FileOutputStream(file);
            fos.write(deviceId.getBytes());
        } catch (Exception e) {
            Log.e("ObtenerIMEI", "Error al guardar el identificador del dispositivo en el archivo", e);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                Log.e("ObtenerIMEI", "Error al cerrar el flujo de escritura del archivo", e);
            }
        }
    }
}
