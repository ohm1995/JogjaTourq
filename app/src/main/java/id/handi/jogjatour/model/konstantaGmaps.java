package id.handi.jogjatour.model;

/**
 * Created by akang on 18/05/2017.
 */

public class konstantaGmaps
{
    private konstantaGmaps() {};

    public static final boolean USE_MICRO_APP = true;

    public static final int GOOGLE_API_CLIENT_TIMEOUT_S = 10; // 10 seconds
    public static final String GOOGLE_API_CLIENT_ERROR_MSG =
            "Failed to connect to GoogleApiClient (error code = %d)";

    public static final int IMAGE_ANIM_MULTIPLIER = 2;

    public static final int WEAR_IMAGE_SIZE = 400;

    public static final int WEAR_IMAGE_SIZE_PARALLAX_WIDTH = 640;

    // The minimum bottom inset percent to use on a round screen device
    public static final float WEAR_ROUND_MIN_INSET_PERCENT = 0.08f;


    public static final int MAX_ATTRACTIONS = 4;

    //  ID
    public static final int MOBILE_NOTIFICATION_ID = 100;
    public static final int WEAR_NOTIFICATION_ID = 200;


    public static final String EXTRA_ATTRACTIONS = "extra_attractions";
    public static final String EXTRA_ATTRACTIONS_URI = "extra_attractions_uri";
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_DESCRIPTION = "extra_description";
    public static final String EXTRA_LOCATION_LAT = "extra_location_lat";
    public static final String EXTRA_LOCATION_LNG = "extra_location_lng";
    public static final String EXTRA_DISTANCE = "extra_distance";
    public static final String EXTRA_CITY = "extra_city";
    public static final String EXTRA_IMAGE = "extra_image";
    public static final String EXTRA_IMAGE_SECONDARY = "extra_image_secondary";
    public static final String EXTRA_TIMESTAMP = "extra_timestamp";


    public static final String ATTRACTION_PATH = "/attraction";
    public static final String START_PATH = "/start";
    public static final String START_ATTRACTION_PATH = START_PATH + "/attraction";
    public static final String START_NAVIGATION_PATH = START_PATH + "/navigation";
    public static final String CLEAR_NOTIFICATIONS_PATH = "/clear";

    // Maps values
    public static final String MAPS_INTENT_URI = "geo:0,0?q=";
    public static final String MAPS_NAVIGATION_INTENT_URI = "google.navigation:mode=w&q=";
}
