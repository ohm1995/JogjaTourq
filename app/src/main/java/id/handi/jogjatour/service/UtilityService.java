package id.handi.jogjatour.service;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import id.handi.jogjatour.R;
import id.handi.jogjatour.activities.DetailActivity;
import id.handi.jogjatour.model.Utils;
import id.handi.jogjatour.model.atraksi;
import id.handi.jogjatour.model.konstantaGmaps;
import id.handi.jogjatour.provider.atraksiwisata;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;
import static com.google.android.gms.location.LocationServices.GeofencingApi;
import static id.handi.jogjatour.provider.atraksiwisata.ATTRACTIONS;

/**
 * Created by akang on 18/05/2017.
 */

public class UtilityService extends IntentService {
    private static final String TAG = UtilityService.class.getSimpleName();

    public  static final String ACTION_GEOFENCE_TRIGGERED = "geofence_triggered";
    private static final String ACTION_LOCATION_UPDATED = "location_updated";
    private static final String ACTION_REQUEST_LOCATION = "request_location";
    private static final String ACTION_ADD_GEOFENCES = "add_geofences";
    private static final String ACTION_CLEAR_NOTIFICATION = "clear_notification";
    private static final String ACTION_CLEAR_REMOTE_NOTIFICATIONS = "clear_remote_notifications";
    private static final String ACTION_FAKE_UPDATE = "fake_update";
    private static final String EXTRA_TEST_MICROAPP = "test_microapp";

    public static IntentFilter getLocationUpdatedIntentFilter() {
        return new IntentFilter(UtilityService.ACTION_LOCATION_UPDATED);
    }

    public static void triggerWearTest(Context context, boolean microApp) {
        Intent intent = new Intent(context, UtilityService.class);
        intent.setAction(UtilityService.ACTION_FAKE_UPDATE);
        intent.putExtra(EXTRA_TEST_MICROAPP, microApp);
        context.startService(intent);
    }

    public static void addGeofences(Context context) {
        Intent intent = new Intent(context, UtilityService.class);
        intent.setAction(UtilityService.ACTION_ADD_GEOFENCES);
        context.startService(intent);
    }

    public static void requestLocation(Context context) {
        Intent intent = new Intent(context, UtilityService.class);
        intent.setAction(UtilityService.ACTION_REQUEST_LOCATION);
        context.startService(intent);
    }

    public static void clearNotification(Context context) {
        Intent intent = new Intent(context, UtilityService.class);
        intent.setAction(UtilityService.ACTION_CLEAR_NOTIFICATION);
        context.startService(intent);
    }

    public static Intent getClearRemoteNotificationsIntent(Context context) {
        Intent intent = new Intent(context, UtilityService.class);
        intent.setAction(UtilityService.ACTION_CLEAR_REMOTE_NOTIFICATIONS);
        return intent;
    }

    public UtilityService() {
        super(TAG);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent != null ? intent.getAction() : null;
        if (ACTION_ADD_GEOFENCES.equals(action)) {
            addGeofencesInternal();
        } else if (ACTION_GEOFENCE_TRIGGERED.equals(action)) {
            geofenceTriggered(intent);
        } else if (ACTION_REQUEST_LOCATION.equals(action)) {
            requestLocationInternal();
        } else if (ACTION_LOCATION_UPDATED.equals(action)) {
            locationUpdated(intent);
        } else if (ACTION_CLEAR_NOTIFICATION.equals(action)) {
            clearNotificationInternal();
        } else if (ACTION_CLEAR_REMOTE_NOTIFICATIONS.equals(action)) {
            clearRemoteNotifications();
        } else if (ACTION_FAKE_UPDATE.equals(action)) {
            LatLng currentLocation = Utils.getLocation(this);

            // If location unknown use test city, otherwise use closest city
            String city = currentLocation == null ? atraksiwisata.teskot :
                    atraksiwisata.getClosestCity(currentLocation);

            showNotification(city,
                    intent.getBooleanExtra(EXTRA_TEST_MICROAPP, konstantaGmaps.USE_MICRO_APP));
        }
    }

    /**
     * Add geofences using Play Services
     */
    private void addGeofencesInternal() {
        Log.v(TAG, ACTION_ADD_GEOFENCES);

        if (!Utils.checkFineLocationPermission(this)) {
            return;
        }

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();

        // It's OK to use blockingConnect() here as we are running in an
        // IntentService that executes work on a separate (background) thread.
        ConnectionResult connectionResult = googleApiClient.blockingConnect(
                konstantaGmaps.GOOGLE_API_CLIENT_TIMEOUT_S, TimeUnit.SECONDS);

        if (connectionResult.isSuccess() && googleApiClient.isConnected()) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this, 0, new Intent(this, UtilityReceiver.class), 0);
            GeofencingApi.addGeofences(googleApiClient,
                    atraksiwisata.getGeofenceList(), pendingIntent);
            googleApiClient.disconnect();
        } else {
            Log.e(TAG, String.format(konstantaGmaps.GOOGLE_API_CLIENT_ERROR_MSG,
                    connectionResult.getErrorCode()));
        }
    }

    /**
     * Called when a geofence is triggered
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void geofenceTriggered(Intent intent) {
        Log.v(TAG, ACTION_GEOFENCE_TRIGGERED);

        // Check if geofences are enabled
        boolean geofenceEnabled = Utils.getGeofenceEnabled(this);

        // Extract the geofences from the intent
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        List<Geofence> geofences = event.getTriggeringGeofences();

        if (geofenceEnabled && geofences != null && geofences.size() > 0) {
            if (event.getGeofenceTransition() == Geofence.GEOFENCE_TRANSITION_ENTER) {
                // Trigger the notification based on the first geofence
                showNotification(geofences.get(0).getRequestId(), konstantaGmaps.USE_MICRO_APP);
            } else if (event.getGeofenceTransition() == Geofence.GEOFENCE_TRANSITION_EXIT) {
                // Clear notifications
                clearNotificationInternal();
                clearRemoteNotifications();
            }
        }
        UtilityReceiver.completeWakefulIntent(intent);
    }

    /**
     * Called when a location update is requested
     */
    private void requestLocationInternal() {
        Log.v(TAG, ACTION_REQUEST_LOCATION);

        if (!Utils.checkFineLocationPermission(this)) {
            return;
        }

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();

        // It's OK to use blockingConnect() here as we are running in an
        // IntentService that executes work on a separate (background) thread.
        ConnectionResult connectionResult = googleApiClient.blockingConnect(
                konstantaGmaps.GOOGLE_API_CLIENT_TIMEOUT_S, TimeUnit.SECONDS);

        if (connectionResult.isSuccess() && googleApiClient.isConnected()) {

            Intent locationUpdatedIntent = new Intent(this, UtilityService.class);
            locationUpdatedIntent.setAction(ACTION_LOCATION_UPDATED);

            // Send last known location out first if available
            Location location = FusedLocationApi.getLastLocation(googleApiClient);
            if (location != null) {
                Intent lastLocationIntent = new Intent(locationUpdatedIntent);
                lastLocationIntent.putExtra(
                        FusedLocationProviderApi.KEY_LOCATION_CHANGED, location);
                startService(lastLocationIntent);
            }

            // Request new location
            LocationRequest mLocationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            FusedLocationApi.requestLocationUpdates(
                    googleApiClient, mLocationRequest,
                    PendingIntent.getService(this, 0, locationUpdatedIntent, 0));

            googleApiClient.disconnect();
        } else {
            Log.e(TAG, String.format(konstantaGmaps.GOOGLE_API_CLIENT_ERROR_MSG,
                    connectionResult.getErrorCode()));
        }
    }

    /**
     * Called when the location has been updated
     */
    private void locationUpdated(Intent intent) {
        Log.v(TAG, ACTION_LOCATION_UPDATED);

        // Extra new location
        Location location =
                intent.getParcelableExtra(FusedLocationProviderApi.KEY_LOCATION_CHANGED);

        if (location != null) {
            LatLng latLngLocation = new LatLng(location.getLatitude(), location.getLongitude());

            // Store in a local preference as well
            Utils.storeLocation(this, latLngLocation);

            // Send a local broadcast so if an Activity is open it can respond
            // to the updated location
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    /**
     * Clears the local device notification
     */
    private void clearNotificationInternal() {
        Log.v(TAG, ACTION_CLEAR_NOTIFICATION);
        NotificationManagerCompat.from(this).cancel(konstantaGmaps.MOBILE_NOTIFICATION_ID);
    }

    /**
     * Clears remote device notifications using the Wearable message API
     */
    private void clearRemoteNotifications() {
        Log.v(TAG, ACTION_CLEAR_REMOTE_NOTIFICATIONS);
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        // It's OK to use blockingConnect() here as we are running in an
        // IntentService that executes work on a separate (background) thread.
        ConnectionResult connectionResult = googleApiClient.blockingConnect(
                konstantaGmaps.GOOGLE_API_CLIENT_TIMEOUT_S, TimeUnit.SECONDS);

        if (connectionResult.isSuccess() && googleApiClient.isConnected()) {

            // Loop through all nodes and send a clear notification message
            Iterator<String> itr = Utils.getNodes(googleApiClient).iterator();
            while (itr.hasNext()) {
                Wearable.MessageApi.sendMessage(
                        googleApiClient, itr.next(), konstantaGmaps.CLEAR_NOTIFICATIONS_PATH, null);
            }
            googleApiClient.disconnect();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showNotification(String cityId, boolean microApp) {

        List<atraksi> attractions = ATTRACTIONS.get(cityId);

        if (microApp) {
            // If micro app we first need to transfer some data over
            sendDataToWearable(attractions);
        }

        // The first (closest) tourist attraction
        atraksi attraction = attractions.get(0);

        // Limit attractions to send
        int count = attractions.size() > konstantaGmaps.MAX_ATTRACTIONS ?
                konstantaGmaps.MAX_ATTRACTIONS : attractions.size();

        // Pull down the tourist attraction images from the network and store
        HashMap<String, Bitmap> bitmaps = new HashMap<>();
        try {
            for (int i = 0; i < count; i++) {
                bitmaps.put(attractions.get(i).name,
                        Glide.with(this)
                                .load(attractions.get(i).imageUrl)
                                .asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(konstantaGmaps.WEAR_IMAGE_SIZE, konstantaGmaps.WEAR_IMAGE_SIZE)
                                .get());
            }
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, "Error fetching image from network: " + e);
        }

        // The intent to trigger when the notification is tapped
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                DetailActivity.getLaunchIntent(this, attraction.name),
                PendingIntent.FLAG_UPDATE_CURRENT);

        // The intent to trigger when the notification is dismissed, in this case
        // we want to clear remote notifications as well
        PendingIntent deletePendingIntent =
                PendingIntent.getService(this, 0, getClearRemoteNotificationsIntent(this), 0);

        // Construct the main notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmaps.get(attraction.name))
                        .setBigContentTitle(attraction.name)
                        .setSummaryText(getString(R.string.nearby_attraction))
                )
                .setLocalOnly(microApp)
                .setContentTitle(attraction.name)
                .setContentText(getString(R.string.nearby_attraction))
                .setSmallIcon(R.drawable.ic_stat_maps_pin_drop)
                .setContentIntent(pendingIntent)
                .setDeleteIntent(deletePendingIntent)
                .setColor(getResources().getColor(R.color.colorPrimary, getTheme()))
                .setCategory(Notification.CATEGORY_RECOMMENDATION)
                .setAutoCancel(true);

        if (!microApp) {
            // If not a micro app, create some wearable pages for
            // the other nearby tourist attractions.
            ArrayList<Notification> pages = new ArrayList<Notification>();
            for (int i = 1; i < count; i++) {

                // Calculate the distance from current location to tourist attraction
                String distance = Utils.formatDistanceBetween(
                        Utils.getLocation(this), attractions.get(i).location);

                // Construct the notification and add it as a page
                pages.add(new NotificationCompat.Builder(this)
                        .setContentTitle(attractions.get(i).name)
                        .setContentText(distance)
                        .setSmallIcon(R.drawable.ic_stat_maps_pin_drop)
                        .extend(new NotificationCompat.WearableExtender()
                                .setBackground(bitmaps.get(attractions.get(i).name))
                        )
                        .build());
            }
            builder.extend(new NotificationCompat.WearableExtender().addPages(pages));
        }

        // Trigger the notification
        NotificationManagerCompat.from(this).notify(
                konstantaGmaps.MOBILE_NOTIFICATION_ID, builder.build());
    }


    private void sendDataToWearable(List<atraksi> attractions) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        // It's OK to use blockingConnect() here as we are running in an
        // IntentService that executes work on a separate (background) thread.
        ConnectionResult connectionResult = googleApiClient.blockingConnect(
                konstantaGmaps.GOOGLE_API_CLIENT_TIMEOUT_S, TimeUnit.SECONDS);

        // Limit attractions to send
        int count = attractions.size() > konstantaGmaps.MAX_ATTRACTIONS ?
                konstantaGmaps.MAX_ATTRACTIONS : attractions.size();

        ArrayList<DataMap> attractionsData = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            atraksi attraction = attractions.get(i);

            Bitmap image = null;
            Bitmap secondaryImage = null;

            try {
                // Fetch and resize attraction image bitmap
                image = Glide.with(this)
                        .load(attraction.imageUrl)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(konstantaGmaps.WEAR_IMAGE_SIZE_PARALLAX_WIDTH, konstantaGmaps.WEAR_IMAGE_SIZE)
                        .get();

                secondaryImage = Glide.with(this)
                        .load(attraction.secondaryImageUrl)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(konstantaGmaps.WEAR_IMAGE_SIZE_PARALLAX_WIDTH, konstantaGmaps.WEAR_IMAGE_SIZE)
                        .get();
            } catch (InterruptedException | ExecutionException e) {
                Log.e(TAG, "Exception loading bitmap from network");
            }

            if (image != null && secondaryImage != null) {

                DataMap attractionData = new DataMap();

                String distance = Utils.formatDistanceBetween(
                        Utils.getLocation(this), attraction.location);

                attractionData.putString(konstantaGmaps.EXTRA_TITLE, attraction.name);
                attractionData.putString(konstantaGmaps.EXTRA_DESCRIPTION, attraction.description);
                attractionData.putDouble(
                        konstantaGmaps.EXTRA_LOCATION_LAT, attraction.location.latitude);
                attractionData.putDouble(
                        konstantaGmaps.EXTRA_LOCATION_LNG, attraction.location.longitude);
                attractionData.putString(konstantaGmaps.EXTRA_DISTANCE, distance);
                attractionData.putString(konstantaGmaps.EXTRA_CITY, attraction.city);
                attractionData.putAsset(konstantaGmaps.EXTRA_IMAGE,
                        Utils.createAssetFromBitmap(image));
                attractionData.putAsset(konstantaGmaps.EXTRA_IMAGE_SECONDARY,
                        Utils.createAssetFromBitmap(secondaryImage));

                attractionsData.add(attractionData);
            }
        }

        if (connectionResult.isSuccess() && googleApiClient.isConnected()
                && attractionsData.size() > 0) {

            PutDataMapRequest dataMap = PutDataMapRequest.create(konstantaGmaps.ATTRACTION_PATH);
            dataMap.getDataMap().putDataMapArrayList(konstantaGmaps.EXTRA_ATTRACTIONS, attractionsData);
            dataMap.getDataMap().putLong(konstantaGmaps.EXTRA_TIMESTAMP, new Date().getTime());
            PutDataRequest request = dataMap.asPutDataRequest();
            request.setUrgent();

            // Send the data over
            DataApi.DataItemResult result =
                    Wearable.DataApi.putDataItem(googleApiClient, request).await();

            if (!result.getStatus().isSuccess()) {
                Log.e(TAG, String.format("Error sending data using DataApi (error code = %d)",
                        result.getStatus().getStatusCode()));
            }

        } else {
            Log.e(TAG, String.format(konstantaGmaps.GOOGLE_API_CLIENT_ERROR_MSG,
                    connectionResult.getErrorCode()));
        }
        googleApiClient.disconnect();
    }

}
