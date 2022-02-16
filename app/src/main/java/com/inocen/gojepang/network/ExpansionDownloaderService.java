package com.inocen.gojepang.network;


import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.google.android.vending.expansion.downloader.impl.DownloaderService;

import java.io.File;
import java.util.Vector;

public class ExpansionDownloaderService  extends DownloaderService {
    public static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw/JMncBXfo34DTSH8FF0bhAvN1FdEYZ1J8b6/PjH55OPoL7p6Xjmg/rdFs9d5aAj+4SPz9yGrshOvE5WuV8dOxv8+yMxGdjOb0R5ctLtnX/7LR4DTf/ZtAbSgIxhcwIR02kghdirTg8IAYaxPisE3ZS7yNLN5TCO0uJpdabm1AMoszAtcVe5QryyCiUHyJ/s0WgmqZU5hFQkjwFcrJbpzIkJEGcVrzDxAoEvi7OHedXQtY00CsL75YNtYs3PjPCzKTIUR9M4fnxLW3/ksfsC3k7v4lAjcYy6quwlavDhhsXoulJp3iIGGyOB/SChK/BawDQSO8bZQTMt4XToK2xniQIDAQAB";
    // You should also modify this salt
    public static final byte[] SALT = new byte[] { 2, -42, 12, -15, 24, 9,
            -10, -2, 4, 20, -80, -42, 94, 4 -10, -17, -3, 5, -1, 84
    };
    private final static String EXP_PATH_API_23 = "/Android/data/";
    private final static String EXP_PATH = "/Android/obb/";

    @Override
    public String getPublicKey() {
        return BASE64_PUBLIC_KEY;
    }

    @Override
    public byte[] getSALT() {
        return SALT;
    }

    @Override
    public String getAlarmReceiverClassName() {
        return SampleAlarmReceiver.class.getName();
    }

    static String[] getAPKExpansionFiles(Context ctx, int mainVersion,
                                         int patchVersion) {
        String packageName = ctx.getPackageName();
        Vector<String> ret = new Vector<String>();
        if (Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED)) {
            // Build the full path to the app's expansion files
            File root = Environment.getExternalStorageDirectory();
            String path = Build.VERSION.SDK_INT >=23 ? EXP_PATH_API_23 : EXP_PATH;
            File expPath = new File(root.toString() + path + packageName);

            // Check that expansion file path exists
            if (expPath.exists()) {
                if ( mainVersion > 0 ) {
                    String strMainPath = expPath + File.separator + "main." +
                            mainVersion + "." + packageName + ".obb";
                    File main = new File(strMainPath);
                    if ( main.isFile() ) {
                        ret.add(strMainPath);
                    }
                }
                if ( patchVersion > 0 ) {
                    String strPatchPath = expPath + File.separator + "patch." +
                            mainVersion + "." + packageName + ".obb";
                    File main = new File(strPatchPath);
                    if ( main.isFile() ) {
                        ret.add(strPatchPath);
                    }
                }
            }
        }
        String[] retArray = new String[ret.size()];
        ret.toArray(retArray);
        return retArray;
    }
}
