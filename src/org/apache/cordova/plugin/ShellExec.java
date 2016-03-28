package org.apache.cordova.ShellExec;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class ShellExec extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (action.equals("exec")) {
            Process p;
            StringBuffer output = new StringBuffer();
            int exitStatus = 100;
            try {
//                        p = Runtime.getRuntime().exec((String) args.get(0));
//                        BufferedReader reader = new BufferedReader(
//                                new InputStreamReader(p.getInputStream()));
//                        String line = "";
//                        while ((line = reader.readLine()) != null) {
//                                output.append(line + "\n");
//                                p.waitFor();
//                        }

                ActivityManager actvityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
                List<RunningAppProcessInfo> procInfos = actvityManager.getRunningAppProcesses();
                for(RunningAppProcessInfo runningProInfo:procInfos){
                    output.append("Running Processes", "()()" + runningProInfo.processName + "\n");
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
