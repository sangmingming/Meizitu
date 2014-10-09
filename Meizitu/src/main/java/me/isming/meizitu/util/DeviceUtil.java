package me.isming.meizitu.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import me.isming.meizitu.App;

/**
 * Created by sam on 14-9-13.
 */
public class DeviceUtil {

    private static String sModel;
    private static String sOsVersion;
    private static String sSimOperator;
    private static String sIMEI;
    private static String sMAC;

    private static final String PREFS_DEVICE_ID = "device_id";
    private static final String PREFS_FILE = "pre_device.xml";
    protected static UUID uuid;


    public static String getModel() {
        if (TextUtils.isEmpty(sModel)) {
            sModel = android.os.Build.MODEL;
        }
        return sModel;
    }

    public static String getOsVersion() {
        if (TextUtils.isEmpty(sOsVersion)) {
            sOsVersion = android.os.Build.VERSION.RELEASE;
        }
        return sOsVersion;
    }

    public static String getSimOperator() {
        if (TextUtils.isEmpty(sSimOperator)) {
            TelephonyManager telManager = (TelephonyManager) App.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            sSimOperator = telManager.getSimOperator();
        }
        return sSimOperator;
    }

    /***
     * 获取设别信息
     * @param context
     * @return
     */
    public static String getDeviceInfo(Context context) {
        try{
            org.json.JSONObject json = new org.json.JSONObject();
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();
            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            sMAC = mac;
            json.put("mac", mac);

            if( TextUtils.isEmpty(device_id) ){
                device_id = mac;
            }

            if( TextUtils.isEmpty(device_id) ){
                device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getDeviceId() {
        Context context = App.getContext();
        if (context == null) {
            return "";
        }
        if( uuid ==null ) {
            synchronized (DeviceUtil.class) {
                if( uuid == null) {
                    final SharedPreferences prefs = context.getSharedPreferences( PREFS_FILE, 0);
                    final String id = prefs.getString(PREFS_DEVICE_ID, null );
                    if (id != null) {
                        // Use the ids previously computed and stored in the prefs file
                        uuid = UUID.fromString(id);
                    } else {
                        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                        // Use the Android ID unless it's broken, in which case fallback on deviceId,
                        // unless it's not available, then fallback on a random number which we store
                        // to a prefs file
                        try {
                            if (!"9774d56d682e549c".equals(androidId)) {
                                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                            } else {
                                final String deviceId = ((TelephonyManager) context.getSystemService( Context.TELEPHONY_SERVICE )).getDeviceId();
                                uuid = deviceId!=null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
                            }
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                        // Write the value out to the prefs file
                        prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString() ).commit();
                    }
                }
            }
        }
        return uuid.toString();
    }


}
