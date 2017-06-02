package id.handi.jogjatour.activities;

import android.support.v4.app.Fragment;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Map;

import id.handi.jogjatour.R;
import id.handi.jogjatour.model.Utils;
import id.handi.jogjatour.model.atraksi;
import id.handi.jogjatour.model.konstantaGmaps;

import static id.handi.jogjatour.provider.atraksiwisata.ATTRACTIONS;

public class DetailFragment extends Fragment
{
    private static final String EXTRA_ATTRACTION = "attraction";
    private atraksi mAttraction;

    public static DetailFragment createInstance(String attractionName) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ATTRACTION, attractionName);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    public DetailFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        String attractionName = getArguments().getString(EXTRA_ATTRACTION);
        mAttraction = findAttraction(attractionName);

        if (mAttraction == null) {
            getActivity().finish();
            return null;
        }

        TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        TextView descTextView = (TextView) view.findViewById(R.id.descriptionTextView);
        TextView distanceTextView = (TextView) view.findViewById(R.id.distanceTextView);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        FloatingActionButton mapFab = (FloatingActionButton) view.findViewById(R.id.mapFab);

        LatLng location = Utils.getLocation(getActivity());
        String distance = Utils.formatDistanceBetween(location, mAttraction.location);
        if (TextUtils.isEmpty(distance)) {
            distanceTextView.setVisibility(View.GONE);
        }

        nameTextView.setText(attractionName);
        distanceTextView.setText(distance);
        descTextView.setText(mAttraction.longDescription);

        int imageSize = getResources().getDimensionPixelSize(Integer.parseInt("120dp"))
                * konstantaGmaps.IMAGE_ANIM_MULTIPLIER;
        Glide.with(getActivity())
                .load(mAttraction.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.color.colorPrimary)
                .override(imageSize, imageSize)
                .into(imageView);

        mapFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(konstantaGmaps.MAPS_INTENT_URI +
                        Uri.encode(mAttraction.name + ", " + mAttraction.city)));
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent upIntent = NavUtils.getParentActivityIntent(getActivity());
                upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);


                if (NavUtils.shouldUpRecreateTask(getActivity(), upIntent)
                        || getActivity().isTaskRoot()) {


                    TaskStackBuilder.create(getActivity())
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    getActivity().finishAfterTransition();
                    return true;
                }

                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    private atraksi findAttraction(String attractionName) {
        for (Map.Entry<String, List<atraksi>> attractionsList : ATTRACTIONS.entrySet()) {
            List<atraksi> attractions = attractionsList.getValue();
            for (atraksi attraction : attractions) {
                if (attractionName.equals(attraction.name)) {
                    return attraction;
                }
            }
        }
        return null;
    }

}
