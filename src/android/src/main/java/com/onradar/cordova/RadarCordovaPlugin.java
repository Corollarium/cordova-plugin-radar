package com.onradar.cordova;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.onradar.sdk.Radar;
import com.onradar.sdk.RadarReceiver;
import com.onradar.sdk.RadarCallback;
import com.onradar.sdk.model.RadarEvent;
import com.onradar.sdk.model.RadarUser;

public class RadarCordovaPlugin extends CordovaPlugin {

    private static CallbackContext eventsCallbackContext;
    private static CallbackContext errorCallbackContext;

    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (action.equals("initialize"))
            init(args, callbackContext);
        else if (action.equals("setUserId"))
            setUserId(args, callbackContext);
        else if (action.equals("setDescription"))
            setDescription(args, callbackContext);
        else if (action.equals("setPlacesProvider"))
            setPlacesProvider(args, callbackContext);
        else if (action.equals("getPermissionsStatus"))
            getPermissionsStatus(args, callbackContext);
        else if (action.equals("requestPermissions"))
            requestPermissions(args, callbackContext);
        else if (action.equals("startTracking"))
            startTracking(args, callbackContext);
        else if (action.equals("stopTracking"))
            stopTracking(args, callbackContext);
        else if (action.equals("acceptEvent"))
            startTracking(args, callbackContext);
        else if (action.equals("rejectEvent"))
            stopTracking(args, callbackContext);
        else if (action.equals("trackOnce"))
            trackOnce(args, callbackContext);
        else if (action.equals("updateLocation"))
            updateLocation(args, callbackContext);
        else if (action.equals("onEvents"))
            onEvents(args, callbackContext);
        else if (action.equals("onError"))
            onError(args, callbackContext);
        else if (action.equals("offEvents"))
            offEvents(args, callbackContext);
        else if (action.equals("offError"))
            offError(args, callbackContext);
        else
            return false;

        return true;
    }

    public void init(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        final String key = args.getString(0);

        final Activity activity = this.cordova.getActivity();
        Radar.initialize(activity, key);

        if (!Radar.checkSelfPermissions()) {
            Radar.requestPermissions(activity);
        }

        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
    }

    public void setUserId(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
          final String userId = args.getString(0);

          Radar.setUserId(userId);

          callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
    }

    public void setDescription(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        final String description = args.getString(0);
        Radar.setDescription(description);

        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
    }

    public void setPlacesProvider(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        final String provider = args.getString(0);

        Radar.RadarPlacesProvider p;
        if ("facebook".equals(provider)) {
            p = Radar.RadarPlacesProvider.FACEBOOK;
        }
        else {
            p = Radar.RadarPlacesProvider.NONE;
        }
        Radar.setPlacesProvider(p);

        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
    }

    public void getPermissionsStatus(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        boolean hasPermissions = Radar.checkSelfPermissions();

        String str = RadarCordovaUtils.stringForPermissionsStatus(hasPermissions);

        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, str));
    }

    public void requestPermissions(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        Radar.requestPermissions(this.cordova.getActivity());

        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
    }

    public void startTracking(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        Radar.startTracking();

        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
    }

    public void stopTracking(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        Radar.stopTracking();

        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
    }

    public void acceptEvent(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        final String eventId = args.getString(0);
        final String placeId = args.getString(1);

        Radar.acceptEvent(eventId, placeId);

        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
    }

    public void rejectEvent(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        final String eventId = args.getString(0);

        Radar.rejectEvent(eventId);

        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
    }


    public void trackOnce(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
      Radar.trackOnce(new RadarCallback() {
          @Override
          public void onCallback(Radar.RadarStatus status, Location location, RadarEvent[] events, RadarUser user) {
              try {
                  JSONObject obj = new JSONObject();
                  obj.put("status", RadarCordovaUtils.stringForStatus(status));
                  if (location != null)
                      obj.put("location", RadarCordovaUtils.jsonObjectForLocation(location));
                  if (events != null)
                      obj.put("events", RadarCordovaUtils.jsonArrayForEvents(events));
                  if (user != null)
                      obj.put("user", RadarCordovaUtils.jsonObjectForUser(user));

                  callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, obj));
              } catch (JSONException e) {
                  callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION));
              }
          }
      });
    }

    public void updateLocation(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        final JSONObject locationObj = args.getJSONObject(0);

        double latitude = locationObj.getDouble("latitude");
        double longitude = locationObj.getDouble("longitude");
        float accuracy = (float)locationObj.getDouble("accuracy");

        Location location = new Location("RadarCordovaPlugin");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setAccuracy(accuracy);

        Radar.updateLocation(location, new RadarCallback() {
            @Override
            public void onCallback(Radar.RadarStatus status, Location location, RadarEvent[] events, RadarUser user) {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("status", RadarCordovaUtils.stringForStatus(status));
                    if (location != null)
                        obj.put("location", RadarCordovaUtils.jsonObjectForLocation(location));
                    if (events != null)
                        obj.put("events", RadarCordovaUtils.jsonArrayForEvents(events));
                    if (user != null)
                        obj.put("user", RadarCordovaUtils.jsonObjectForUser(user));

                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, obj));
                } catch (JSONException e) {
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION));
                }
            }
        });
    }

    public void onEvents(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        RadarCordovaPlugin.eventsCallbackContext = callbackContext;
    }

    public void onError(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        RadarCordovaPlugin.errorCallbackContext = callbackContext;
    }

    public void offEvents(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        RadarCordovaPlugin.eventsCallbackContext = null;
    }

    public void offError(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        RadarCordovaPlugin.errorCallbackContext = null;
    }

    public static class RadarCordovaReceiver extends RadarReceiver {

        @Override
        public void onEventsReceived(@NonNull Context context, @NonNull RadarEvent[] events, @NonNull RadarUser user) {
            if (RadarCordovaPlugin.eventsCallbackContext == null)
                return;

            try {
                JSONObject obj = new JSONObject();
                obj.put("events", RadarCordovaUtils.jsonArrayForEvents(events));
                obj.put("user", RadarCordovaUtils.jsonObjectForUser(user));

                PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, obj);
                pluginResult.setKeepCallback(true);
                RadarCordovaPlugin.eventsCallbackContext.sendPluginResult(pluginResult);
            } catch (JSONException e) {
                RadarCordovaPlugin.eventsCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION));
            }
        }

        @Override
        public void onError(@NonNull Context context, @NonNull Radar.RadarStatus status) {
            if (RadarCordovaPlugin.errorCallbackContext == null)
                return;

            try {
                JSONObject obj = new JSONObject();
                obj.put("status", RadarCordovaUtils.stringForStatus(status));

                PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, obj);
                pluginResult.setKeepCallback(true);
                RadarCordovaPlugin.errorCallbackContext.sendPluginResult(pluginResult);
            } catch (JSONException e) {
                RadarCordovaPlugin.errorCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION));
            }
        }

    }

}
