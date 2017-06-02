package id.handi.jogjatour.model;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by akang on 18/05/2017.
 */

public class atraksi

{
    public String name;
    public String description;
    public String longDescription;
    public Uri imageUrl;
    public Uri secondaryImageUrl;
    public LatLng location;
    public String city;

    public atraksi
            (String name,
             String description,
             String longDescription,
             Uri imageUrl,
             Uri secondaryImageUrl,
             LatLng location,
             String city
            ) {

        this.name = name;
        this.description = description;
        this.longDescription = longDescription;
        this.imageUrl = imageUrl;
        this.secondaryImageUrl = secondaryImageUrl;
        this.location = location;
        this.city = city;

    }


}
