package id.alfarizi.babymonitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference db = database.getReference();
    Button mAktifitasBayi, mLihatBayi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db.child("fcm").setValue(0);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        String token = task.getResult().getToken();
                        ((TextView) findViewById(R.id.fcm_kode)).setText(token);
                        Log.d("tokenfirebase", token);
                        db.child("fcm").setValue(token);
                         ((EditText) findViewById(R.id.fcm_kode)).setText(token);
                    }
                });


        mAktifitasBayi = (Button) findViewById(R.id.aktifitas_bayi);
        mLihatBayi = (Button) findViewById(R.id.lihat_bayi);
        mAktifitasBayi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AktifitasBayi.class);
                startActivity(i);
            }
        });
        mLihatBayi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LihatBayi.class);
                startActivity(i);
            }
        });
    }
}
