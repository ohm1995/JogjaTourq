package id.handi.jogjatour.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import id.handi.jogjatour.R;


public class DetailActivity extends AppCompatActivity {


    private static final String EXTRA_ATTRACTION = "attraction";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void launch(Activity activity, String attraction, View heroView) {
        Intent intent = getLaunchIntent(activity, attraction);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity, heroView, heroView.getTransitionName());
            ActivityCompat.startActivity(activity, intent, options.toBundle());
        } else {
            activity.startActivity(intent);
        }
    }

    public static Intent getLaunchIntent(Context context, String attraction) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_ATTRACTION, attraction);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wisata);

        String attraction = getIntent().getStringExtra(EXTRA_ATTRACTION);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, DetailFragment.createInstance(attraction))
                    .commit();
        }
    }
}
