package id.alfarizi.babymonitoring;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RiwayatAktifitas extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference db = database.getReference();


    SwipeRefreshLayout mRefresh;
    RecyclerView mRv;
    private String path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_aktifitas);

        Toolbar mMyToolbar = (Toolbar) findViewById(R.id.tolbar);
        mMyToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                    return;
                }
                RiwayatAktifitas.super.onBackPressed();
            }
        });

        mRefresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mRv = (RecyclerView) findViewById(R.id.rv_riwayat);
        mRv.setLayoutManager(new LinearLayoutManager(RiwayatAktifitas.this));

        if (getIntent().getStringExtra("path") != null){
            path = getIntent().getStringExtra("path");
            mMyToolbar.setTitle("Riwayat");
            mMyToolbar.setSubtitle(path.equals("LogNangis") ? "Bayi Menangis" : (path.equals("LogNgompol") ? "Bayi Mengompol" : "Bayi Berbenturan"));
            mRefresh.setRefreshing(true);
            AmbilDataList(path);

            mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    AmbilDataList(path);
                }
            });

        }

    }

    private void AmbilDataList(String data) {
        Log.d("dataaIntent",data);
        final List<ModelLog> list = new ArrayList<>();
        list.clear();

        db.child(data).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("cccccc", new Gson().toJson(dataSnapshot.getValue()));
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Log.d("child1", new Gson().toJson(data.getValue()));

                    String v = new Gson().toJson(data.getValue());
                    ModelLog value = new Gson().fromJson(v, ModelLog.class);

                    list.add(value);

                }
                Log.d("child___", new Gson().toJson(list));

                Thread mySplash = new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                mySplash.start();

                final Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        Collections.reverse(list);
                        AdapterLog adapterLog = new AdapterLog(RiwayatAktifitas.this, list);
                        mRv.setAdapter(adapterLog);
                    }
                };
                mRv.post(r);

                mRefresh.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
