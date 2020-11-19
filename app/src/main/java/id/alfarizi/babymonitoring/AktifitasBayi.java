package id.alfarizi.babymonitoring;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class AktifitasBayi extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference db = database.getReference();

    Switch mStatusAktif;
    TextView mWaktuMenangis;
    TextView mWaktuMengompol;
    TextView mWaktuBergerak;
    SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktifitas_bayi);

        Toolbar mMyToolbar = (Toolbar) findViewById(R.id.tolbar);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);

        mMyToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                    return;
                }
                AktifitasBayi.super.onBackPressed();
            }
        });

        mStatusAktif = (Switch) findViewById(R.id.status_aktif);
        mWaktuMenangis = (TextView) findViewById(R.id.waktu_menangis);
        mWaktuMengompol = (TextView) findViewById(R.id.waktu_mengompol);
        mWaktuBergerak = (TextView) findViewById(R.id.waktu_bergerak);

        refreshLayout.setRefreshing(true);
        AmbilData();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AmbilData();
            }
        });


    }

    private void AmbilData() {
        db.child("sensor").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("nilaiSensor", new Gson().toJson(dataSnapshot.getValue()));
                int sensor = dataSnapshot.getValue(Integer.class);
//                valueRangeKostum = model.getNilai_kostum();

                mStatusAktif.setChecked(sensor == 1 ? true : false);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mStatusAktif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.child("sensor").setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AktifitasBayi.this, "Berhasil menghidupkan sensor", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    db.child("sensor").setValue(0).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AktifitasBayi.this, "Berhasil mematikan sensor", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        Query qNangis = db.child("LogNangis").orderByKey().limitToLast(1);
        qNangis.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    String data = new Gson().toJson(child.getValue());
                    ModelLog model = new Gson().fromJson(data, ModelLog.class);
                    String hari = new WaktuTanggalJam().HanyaTanggal(model.getTanggal());

                    mWaktuMenangis.setText(model.getHari() + ", " +hari +"\nJam "+model.getJam());
                    mWaktuMenangis.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(AktifitasBayi.this, RiwayatAktifitas.class);
                            i.putExtra("path", "LogNangis");
                            startActivity(i);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query qNgompol = db.child("LogNgompol").orderByKey().limitToLast(1);
        qNgompol.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    String data = new Gson().toJson(child.getValue());
                    ModelLog model = new Gson().fromJson(data, ModelLog.class);
                    String hari = new WaktuTanggalJam().HanyaTanggal(model.getTanggal());

                    mWaktuMengompol.setText(model.getHari() + ", " +hari +"\nJam "+model.getJam());
                    mWaktuMengompol.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(AktifitasBayi.this, RiwayatAktifitas.class);
                            i.putExtra("path", "LogNgompol");
                            startActivity(i);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query qGetar = db.child("LogGetar").orderByKey().limitToLast(1);
        qGetar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    String data = new Gson().toJson(child.getValue());
                    ModelLog model = new Gson().fromJson(data, ModelLog.class);
                    String hari = new WaktuTanggalJam().HanyaTanggal(model.getTanggal());

                    mWaktuBergerak.setText(model.getHari() + ", " +hari +"\nJam "+model.getJam());
                    mWaktuBergerak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(AktifitasBayi.this, RiwayatAktifitas.class);
                            i.putExtra("path", "LogGetar");
                            startActivity(i);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
