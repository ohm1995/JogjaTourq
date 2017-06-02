package id.handi.jogjatour.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import id.handi.jogjatour.R;
import id.handi.jogjatour.model.User;
import id.handi.jogjatour.sql.DatabaseHelper;

public class MenuActivity extends AppCompatActivity {

    private AppCompatActivity activity = MenuActivity.this;
    private AppCompatTextView textViewName2;
    private List<User> listUsers;
    private DatabaseHelper databaseHelper;

    Button tentang;
    Button Berita;
    Button harga;
    Button wisata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initView();
        initObject();

        tentang = (Button) findViewById(R.id.btnTentang);
        Berita = (Button) findViewById(R.id.btnBerita);
        wisata = (Button) findViewById(R.id.btnWisata);
        harga = (Button) findViewById(R.id.btnHarga);


        tentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this,tentangActivity.class);
                startActivity(i);
            }
        });

        wisata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(MenuActivity.this,WisataActivity.class);
                startActivity(a);
            }
        });

        harga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b = new Intent(MenuActivity.this,HargaActivity.class);
                startActivity(b);
            }
        });

        Berita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(MenuActivity.this, BeritaActivity.class);
                startActivity(j);
            }
        });

    }

    private void initObject() {
        listUsers = new ArrayList<>();
        databaseHelper = new DatabaseHelper(activity);
        String emailFromIntent = getIntent().getStringExtra("Email");
        textViewName2.setText(emailFromIntent);
        getDataFromSQLite();

    }

    private void getDataFromSQLite() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listUsers.clear();
                listUsers.addAll(databaseHelper.getAllUser());

                return null;
            }
        }.execute();
    }

    private void initView() {
        textViewName2 = (AppCompatTextView) findViewById(R.id.textViewName2);
    }
}
