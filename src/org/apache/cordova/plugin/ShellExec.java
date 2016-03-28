package org.apache.cordova.plugin;

import android.content.Context;

import java.util.List;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;

import android.os.Build;
import android.util.Log;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;

public class ShellExec extends CordovaPlugin {
    private ActivityManager activityManager;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        Activity activity = cordova.getActivity();
        activityManager = (ActivityManager) activity.getSystemService(Activity.ACTIVITY_SERVICE);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (action.equals("exec")) {
            Process p;
            StringBuffer output = new StringBuffer();
            int exitStatus = 100;
            try {
                p = Runtime.getRuntime().exec((String) args.get(0));
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(p.getInputStream()));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    output.append(line + "\n");
                    p.waitFor();
                }

//                MemoryInfo memoryInfo = new MemoryInfo();
//                activityManager.getMemoryInfo(memoryInfo);
//                ActivityManager actvityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
//                List<RunningAppProcessInfo> procInfos = actvityManager.getRunningAppProcesses();
//                for(RunningAppProcessInfo runningProInfo:procInfos) {
//                    output.append("Running Processes", "()()" + runningProInfo.processName + "\n");
//                }


                List<ActivityManager.RunningAppProcessInfo> runningTasks = activityManager.getRunningAppProcesses();
                for(ActivityManager.RunningAppProcessInfo runningProInfo:runningTasks) {
                    output.append(runningProInfo.processName + "\n");
                }


                exitStatus = p.exitValue();
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            JSONObject json = new JSONObject();
            json.put("exitStatus", exitStatus);
            json.put("output", output.toString());
            callbackContext.success(json);
            return true;
        }
        return false;
    }

}


//package org.apache.cordova.ShellExec;
//
//import android.app.ActivityManager;
//import android.app.ActivityManager.RunningAppProcessInfo;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.util.List;
//
//public class ShellExec extends CordovaPlugin {
//
//    @Override
//    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
//
//        if (action.equals("exec")) {
//            Process p;
//            StringBuffer output = new StringBuffer();
//            int exitStatus = 100;
//            try {
////                        p = Runtime.getRuntime().exec((String) args.get(0));
////                        BufferedReader reader = new BufferedReader(
////                                new InputStreamReader(p.getInputStream()));
////                        String line = "";
////                        while ((line = reader.readLine()) != null) {
////                                output.append(line + "\n");
////                                p.waitFor();
////                        }
//
//                ActivityManager actvityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
//                List<RunningAppProcessInfo> procInfos = actvityManager.getRunningAppProcesses();
//                for(RunningAppProcessInfo runningProInfo:procInfos){
//                    output.append("Running Processes", "()()" + runningProInfo.processName + "\n");
//                }
//
//                exitStatus = p.exitValue();
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            JSONObject json = new JSONObject();
//            json.put("exitStatus", exitStatus);
//            json.put("output", output.toString());
//            callbackContext.success(json);
//            return true;
//        }
//        return false;
//    }
//
//}
