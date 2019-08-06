package sg.edu.rp.c346.smsapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText toET,contentET;
    BroadcastReceiver br;
    Button btn,btnMes;
    String to,content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toET = findViewById(R.id.to);
        contentET = findViewById(R.id.content);
        btn = findViewById(R.id.button);
        btnMes = findViewById(R.id.buttonMes);

        br = new MessageReceiver();

        checkPermission();

        IntentFilter filter = new IntentFilter(ConnectivityManager.EXTRA_NO_CONNECTIVITY);
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(br, filter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String to = toET.getText().toString();
                String content = contentET.getText().toString();

                String[] numSplit = to.split(",");

                for(String i:numSplit){
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(i, null, content, null, null);
                    Toast.makeText(MainActivity.this,"Message Sent", Toast.LENGTH_LONG).show();

                }

                contentET.setText("");
            }
        });

        btnMes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                to = toET.getText().toString();
                content = contentET.getText().toString();

                Uri smsUri = Uri.parse("smsto:" + to);
                Intent intent = new Intent(Intent.ACTION_SENDTO, smsUri);
                intent.putExtra("sms_body", content);
                startActivity(intent);
            }
        });
    }

    private void checkPermission() {
        int permissionSendSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        int permissionRecvSMS = ContextCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_SMS);

        if (permissionSendSMS != PackageManager.PERMISSION_GRANTED &&permissionRecvSMS != PackageManager.PERMISSION_GRANTED) {
            String[] permissionNeeded = new String[]{Manifest.permission.SEND_SMS,Manifest.permission.RECEIVE_SMS};
            ActivityCompat.requestPermissions(this, permissionNeeded, 1);
        }
    }
}
