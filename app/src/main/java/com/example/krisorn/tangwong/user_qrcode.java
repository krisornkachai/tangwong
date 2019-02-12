package com.example.krisorn.tangwong;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class user_qrcode extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final static int QRcodeWidth = 500 ;
    private static final String IMAGE_DIRECTORY = "/QRcodeDemonuts";
    Bitmap bitmap;
    private EditText String_QR;
    private ImageView Image_QR;
    private TextView textScanner;
    private Button Gen_QR;
    private IntentIntegrator qrscan;
    private DatabaseReference searchRoom ;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        setContentView(R.layout.activity_qrcode);
        searchRoom = FirebaseDatabase.getInstance().getReference();
        textScanner = (TextView)findViewById(R.id.textScanner);
        qrscan = new IntentIntegrator(this);



        //QR_code
        //Image_QR =(ImageView)findViewById(R.id.imageQR);
        //String_QR = (EditText)findViewById(R.id.string_QR);
        //Gen_QR =(Button)findViewById(R.id.gen_QR);

        /*Gen_QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(String_QR.getText().toString().trim().length()==0){
                    Toast.makeText(user_qrcode.this,"Enter nameQR",Toast.LENGTH_SHORT).show();
                }else{
                    try{
                        bitmap = TextToImageEncode(String_QR.getText().toString());
                        Image_QR.setImageBitmap(bitmap);
                        String path = saveImage(bitmap);
                        Toast.makeText(user_qrcode.this,"QR Code save in "+ path,Toast.LENGTH_SHORT).show();
                    }catch (WriterException e){
                        e.printStackTrace();
                    }
                }
            }
        });*/
        /*Scan_QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onActivityResult();
            }
        });*/

        //QR_code

        //side bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_user);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_dash_board);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                user_qrcode.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_dash_board);
        Log.d("testSideNav","---------------");
        navigationView.setNavigationItemSelectedListener(user_qrcode.this);
        navigationView.bringToFront();
        //end side bar

    }


    // decare function bitmap
    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black):getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.

        if (!wallpaperDirectory.exists()) {
            Log.d("dirrrrrr", "" + wallpaperDirectory.mkdirs());
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();   //give read write permission
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }
    // decare function bitmap

    public void scanQRCode(View v){
        qrscan.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null){
            Log.d ("kkkk",String.valueOf (result.getContents ()));
            if(result.getContents()== null){
                Toast.makeText(this,"Not Found",Toast.LENGTH_SHORT);
            }else{
                try{
                    JSONObject obj = new JSONObject(result.getContents());
                    Toast.makeText(this,"Scan Failed !!!",Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                    textScanner.setText(result.getContents()); //ค่า id ที่เราscanได้ เอาไปใช้ต่อ
                    Log.d(result.getContents(),"GGTOZO");
                    searchRoom.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                            mAuth=FirebaseAuth.getInstance();
                            FirebaseUser user = mAuth.getCurrentUser();
                             user = mAuth.getCurrentUser();
                            if (dataSnapshot.child("room").hasChild(result.getContents())) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(user_qrcode.this);
                                builder1.setMessage("You want Tungwong this");
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(

                                        "Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                mAuth=FirebaseAuth.getInstance();
                                                final FirebaseUser user = mAuth.getCurrentUser();
                                                searchRoom.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        Long noOfRoom = dataSnapshot.child("user").child(user.getUid()).child("live").getChildrenCount();
                                                        long numberOfpeople_live = dataSnapshot.child("room").child(result.getContents()).child("people_live").getChildrenCount();
                                                        Log.d("Taxx",String.valueOf(numberOfpeople_live));

                                                        searchRoom.child("user").child(user.getUid()).child("live").child(String.valueOf(noOfRoom)).setValue(result.getContents());
                                                        searchRoom.child("user").child(user.getUid()).child("livenow").setValue(result.getContents());
                                                        searchRoom.child("room").child(result.getContents()).child("people_live").child(Long.toString(numberOfpeople_live += 1)).child("uid").setValue (user.getUid ());

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

                                                Intent i = new Intent(user_qrcode.this,user_roomActivity.class);
                                                startActivity(i);

                                            }
                                        });

                                builder1.setNegativeButton(
                                        "No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        }else {
            super.onActivityResult(requestCode,resultCode,data);
            Toast.makeText(this,"Failed!!!",Toast.LENGTH_SHORT).show();

        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d("can select nav","can select nav");
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_room) {
            Intent i = new Intent(this,user_roomActivity.class);
            startActivity(i);
            return true;
        } else if (id == R.id.nav_add_room) {
            Intent i = new Intent(this,create_roomActiviity.class);
            startActivity(i);
            return true;

        } else if (id == R.id.nav_profile) {
            Intent i = new Intent(this,UsersActivity.class);
            startActivity(i);
            return true;

        } else if (id == R.id.nav_cart) {
            Intent i = new Intent(this,Cart.class);
            startActivity(i);
            return true;
        } else if (id == R.id.nav_qr) {
            Intent i = new Intent(this,user_qrcode.class);
            startActivity(i);
            return true;
        } else if (id == R.id.nav_share) {
            Intent i = new Intent(this,Status.class);
            startActivity(i);
            return true;

        }else if(id==R.id.nav_myroom){
            Intent i = new Intent(this,own_room.class);
            startActivity(i);
            return true;
        }else if(id == R.id.nav_logout){
            mAuth.signOut();
            Intent i = new Intent(this,EmailPasswordActivity.class);
            startActivity(i);
            return true;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_user);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}