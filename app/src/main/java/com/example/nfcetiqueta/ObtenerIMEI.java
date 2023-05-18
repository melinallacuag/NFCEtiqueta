package com.example.nfcetiqueta;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

public class ObtenerIMEI {

    public static String getDeviceId(Context context) {
        // Verificar si ya se ha generado un identificador único previamente
        SharedPreferences preferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        String deviceId = preferences.getString("device_id", null);

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

            // Guardar el identificador en las preferencias compartidas
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("device_id", deviceId);
            editor.apply();
        }

        return deviceId;
    }
}
