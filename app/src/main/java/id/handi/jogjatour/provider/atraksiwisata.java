package id.handi.jogjatour.provider;

import android.net.Uri;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.handi.jogjatour.model.atraksi;

/**
 * Created by akang on 18/05/2017.
 */

public class atraksiwisata
{
    public static final String kota = "jogja";

    public static final String teskot = kota;

    private static final float areajogja = 2000; // Radius 2 KM sekitar jogja

    private static final int gerak_transisimaps = Geofence.GEOFENCE_TRANSITION_ENTER |
            Geofence.GEOFENCE_TRANSITION_EXIT;

    private static final long EXPIRATION_DURATION = Geofence.NEVER_EXPIRE;

    public static final Map<String, LatLng> lokasi_kota = new HashMap<String, LatLng>() {{
        put(kota, new LatLng(-7.801389, 110.364770));
    }};

    public static final HashMap<String, List<atraksi>> ATTRACTIONS =
            new HashMap<String, List<atraksi>>() {{

                put(kota, new ArrayList<atraksi>() {{
                    add(new atraksi(
                            "Wisata Malioboro",
                            "Malioboro merupakan kawasan perbelanjaan yang legendaris yang menjadi salah satu kebanggaan kota Yogyakarta. Penamaan Malioboro berasal dari nama seorang anggota kolonial Inggris yang dahulu pernah menduduki Jogja pada tahun 1811 – 1816 M yang bernama Marlborough",
                            "Malioboro merupakan kawasan perbelanjaan yang legendaris yang menjadi salah satu kebanggaan kota Yogyakarta. Penamaan Malioboro berasal dari nama seorang anggota kolonial Inggris yang dahulu pernah menduduki Jogja pada tahun 1811 – 1816 M yang bernama Marlborough\n\nMalioboro menyajikan berbagai aktivitas belanja, mulai dari bentuk aktivitas tradisional sampai dengan aktivitas belanja modern. Salah satu cara berbelanja di Malioboro adalah dengan proses tawar-menawar terutama untuk komoditi barang barang yang berupa souvenir dan cenderamata yang dijajakan oleh pedagang kaki lima yang berjajar di sepanjang trotoar jalan Malioboro. Berbagai macam cederamata dan kerajinan dapat anda dapatkan disini seperti kerajinan dari perak, kulit, kayu, kain batik, gerabah dan sebagainya\n\nMalioboro menyajikan berbagai aktivitas belanja, mulai dari bentuk aktivitas tradisional sampai dengan aktivitas belanja modern. Salah satu cara berbelanja di Malioboro adalah dengan proses tawar-menawar terutama untuk komoditi barang barang yang berupa souvenir dan cenderamata yang dijajakan oleh pedagang kaki lima yang berjajar di sepanjang trotoar jalan Malioboro. Berbagai macam cederamata dan kerajinan dapat anda dapatkan disini seperti kerajinan dari perak, kulit, kayu, kain batik, gerabah dan sebagainya",
                            Uri.parse("http://media.viva.co.id/thumbs2/2017/01/17/587db11251e12-jalur-pedestrian-di-malioboro-bikin-betah_663_382.jpg"),
                            Uri.parse("http://jokowarino.id/wp-content/uploads/2015/12/fi-86.png"),
                            new LatLng(-7.7931698,110.3635643),
                            kota
                    ));

                    add(new atraksi(
                            "Candi Prambanan",
                            "Candi Prambanan atau Candi Roro Jonggrang adalah kompleks candi Hindu terbesar di Indonesia yang dibangun pada abad ke-9 masehi. Candi ini dipersembahkan untuk Trimurti, tiga dewa utama Hindu yaitu Brahma sebagai dewa pencipta, Wishnu sebagai dewa pemelihara, dan Siwa sebagai dewa pemusnah. Berdasarkan prasasti Siwagrha nama asli kompleks candi ini adalah Siwagrha (bahasa Sanskerta yang bermakna 'Rumah Siwa'), dan memang di garbagriha (ruang utama) candi ini bersemayam arca Siwa Mahadewa setinggi tiga meter yang menujukkan bahwa di candi ini dewa Siwa lebih diutamakan",
                            "Candi Prambanan atau Candi Roro Jonggrang adalah kompleks candi Hindu terbesar di Indonesia yang dibangun pada abad ke-9 masehi. Candi ini dipersembahkan untuk Trimurti, tiga dewa utama Hindu yaitu Brahma sebagai dewa pencipta, Wishnu sebagai dewa pemelihara, dan Siwa sebagai dewa pemusnah. Berdasarkan prasasti Siwagrha nama asli kompleks candi ini adalah Siwagrha (bahasa Sanskerta yang bermakna 'Rumah Siwa'), dan memang di garbagriha (ruang utama) candi ini bersemayam arca Siwa Mahadewa setinggi tiga meter yang menujukkan bahwa di candi ini dewa Siwa lebih diutamakan.",
                            Uri.parse("https://upload.wikimedia.org/wikipedia/commons/6/66/Yogyakarta_Indonesia_Prambanan-temple-complex-02.jpg"),
                            Uri.parse("https://1.bp.blogspot.com/-JS2-hIjupVA/VcQz9pllStI/AAAAAAAADO4/q5M6aO_-34o/s1600/6699220511_ecd946e249_z.jpg"),
                            new LatLng(-7.7520153 ,110.4892787),
                            kota
                    ));

                    add(new atraksi(
                            "Taman sari Yogyakarta",
                            "Taman sari Yogyakarta merupakan cagar budaya warisan Keraton Yogyakarta yang masih dapat kita lihat berdiri gagah . Taman sari dibangun pada masa pemerintahan Sri Sultan HB I, pada tahun 1758. Sampai saat ini istana  Taman sari sudah mengalami beberapa kali renovasi sehingga terllihat menarik tanpa menghilangkan nilai historisnya. Taman sari terletak sekitar 300 meter sebelah barat dari Keraton Yogyakarta.",
                            "Taman sari Yogyakarta merupakan cagar budaya warisan Keraton Yogyakarta yang masih dapat kita lihat berdiri gagah . Taman sari dibangun pada masa pemerintahan Sri Sultan HB I, pada tahun 1758. Sampai saat ini istana  Taman sari sudah mengalami beberapa kali renovasi sehingga terllihat menarik tanpa menghilangkan nilai historisnya. Taman sari terletak sekitar 300 meter sebelah barat dari Keraton Yogyakarta.",
                            Uri.parse("http://anekatempatwisata.com/wp-content/uploads/2014/10/Istana-Air-Taman-Sari-Jogja.jpg"),
                            Uri.parse("http://jpswisata.com/wp-content/uploads/2016/06/taman-sari-2.jpg"),
                            new LatLng(-7.8099596 , 110.3568106),
                            kota
                    ));

                    add(new atraksi(
                            "Wisata Kalibiru",
                            "Deskripsi pendek",
                            "Deskripsi pendek",
                            Uri.parse("http://jpswisata.com/wp-content/uploads/2016/03/wisata-kalibiru.jpg"),
                            Uri.parse("http://kotajogja.com/wp-content/uploads/2016/09/insta1-7.jpg"),
                            new LatLng(-7.8058835, 110.1280435),
                            kota
                    ));

                    add(new atraksi(
                            "Hutan Pinus Mangunan",
                            "Deskripsi pendek",
                            "Deskripsi pendek",
                            Uri.parse("http://kotajogja.com/wp-content/uploads/2016/12/hutan-pinus.jpg"),
                            Uri.parse("http://sobatinfo.com/wp-content/uploads/2016/07/gambar-hutan-pinus-dlingo-mangunan-bantul-jogja-jos.jpg"),
                            new LatLng(-7.9209156,110.4333764 ),
                            kota
                    ));
                }});
            }};


    public static List<Geofence> getGeofenceList() {
        List<Geofence> geofenceList = new ArrayList<Geofence>();
        for (String city : lokasi_kota.keySet()) {
            LatLng cityLatLng = lokasi_kota.get(city);
            geofenceList.add(new Geofence.Builder()
                    .setCircularRegion(cityLatLng.latitude, cityLatLng.longitude, areajogja)
                    .setRequestId(city)
                    .setTransitionTypes(gerak_transisimaps)
                    .setExpirationDuration(EXPIRATION_DURATION)
                    .build());
        }
        return geofenceList;
    }

    public static String getClosestCity(LatLng curLatLng) {
        if (curLatLng == null) {
            // If location is unknown return test city so some data is shown
            return teskot;
        }

        double minDistance = 0;
        String closestCity = null;
        for (Map.Entry<String, LatLng> entry: lokasi_kota.entrySet()) {
            double distance = SphericalUtil.computeDistanceBetween(curLatLng, entry.getValue());
            if (minDistance == 0 || distance < minDistance) {
                minDistance = distance;
                closestCity = entry.getKey();
            }
        }
        return closestCity;
    }



}
