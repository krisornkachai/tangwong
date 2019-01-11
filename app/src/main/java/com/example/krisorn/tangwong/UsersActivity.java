package com.example.krisorn.tangwong;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.example.krisorn.tangwong.Model.Notification2;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.app.Notification;

import android.app.NotificationManager;

import android.app.PendingIntent;
//import android.widget.TextView;

import com.example.krisorn.tangwong.Model.MyResponse;
import com.example.krisorn.tangwong.Model.Sender;
import com.example.krisorn.tangwong.Remote.APIService;
import com.example.krisorn.tangwong.Service.Common;
import com.example.krisorn.tangwong.databinding.ActivityUsersBindingImpl;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import java.util.Map;
import com.example.krisorn.tangwong.Model.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Notification.*;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.toUnsignedString;

public class UsersActivity extends AppCompatActivity {
    public DatabaseReference nameCard;
    private UsersViewModel viewModel;
    private FirebaseAuth mAuth;
    // TODO Step 1: Declare binding instance instead view's (binding class is auto-generated)
    //private TextView textView;
    ActivityUsersBindingImpl binding;

    private EditText mtypeField;
    private EditText mdataField;
    private EditText nameField;


    private EditText jroomid;
    private long roomid;
    private int i=0;
    private int change=0;
    private long turnq=1;
    private String livenow;

    private boolean owner=false;


    private ProgressDialog mProgressDialog;

    private Button mselectImage;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    // [END declare_auth]
    private static final int GALLERY_INTENT =2;
    final int id =11;
    Builder test;

    Button btnShowNotification;
    Button btnSendData;
    EditText edtTitle,edtContent;

    APIService mService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d ("aaaa","can create");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        viewModel = new UsersViewModel(this);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        mProgressDialog= new ProgressDialog(this);
        mStorage=FirebaseStorage.getInstance().getReference();
        mselectImage=(Button) findViewById(R.id.btn_addImage);

        Common.currentToken= FirebaseInstanceId.getInstance ().getToken ();
        mService = Common.getFCMClient ();
        btnSendData = (Button)findViewById(R.id.btnSendData) ;
        //edtContent = (EditText)findViewById (R.id.edtContent);
        //edtTitle = (EditText)findViewById (R.id.edtTitle);

        btnSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d ("aaaa","can click");

                Notification2 notification = new Notification2("asd","asd");
                Sender sender = new Sender (Common.currentToken,notification);
                mService.sendNotification (sender)
                        .enqueue (new Callback <MyResponse> () {
                            @Override
                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                if(response.body().success == 1)
                                    Toast.makeText (UsersActivity.this, "true", Toast.LENGTH_SHORT).show ();
                                else
                                    Toast.makeText (UsersActivity.this, "false", Toast.LENGTH_SHORT).show ();
                            }

                            @Override
                            public void onFailure(Call <MyResponse> call, Throwable t) {
                                Log.e("ERROR",t.getMessage());
                            }
                        });
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
       /* mselectImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });*/

        new DownloadImageTask((ImageView)findViewById(R.id.profile)).execute("https://firebasestorage.googleapis.com/v0/b/tangwong-862c9.appspot.com/o/Photos%2Fstorage%2Femulated%2F0%2FDCIM%2FCamera%2FIMG_20181216_222350.jpg?alt=media&token=804a1f60-af35-4fe6-beb2-dabf51c3dd5a");
        initView();
        //get firebase


        nameCard = database.getReference();

        nameCard.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String uid = user.getUid();
                if(dataSnapshot.child("user").child(uid).child("notification").getValue(String.class)=="1")
                {
                    showNotification();
                    Log.d ("asd","zsdf");
                }
               /* Map map =(Map)dataSnapshot.getValue();
                String name = String.valueOf(map.get("name"));*/
              String name=dataSnapshot.child("user").child(uid).child("name").getValue(String.class);

              String pathPhoto=dataSnapshot.child(uid).child("pathPhoto").getValue(String.class);
              viewModel.setName(name);

             // viewModel.setPathPhoto(pathPhoto);


              binding.name.setText(viewModel.getName());

              //new DownloadImageTask((ImageView)findViewById(R.id.profile)).execute(pathPhoto);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initView(){

       // FirebaseUser currentUser = mAuth.getCurrentUser();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_users);
        binding.setViewmodel(viewModel);

    }

    public void click(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        nameCard = database.getReference();

            mDatabase.child("room").child("0").child("name").setValue("qwe");

            mDatabase.child("room").child("0").child("name").setValue("eiei");

        mDatabase.child("room").child("0").child("data").setValue("qwe");

        mDatabase.child("room").child("0").child("data").setValue("eiei");

        mDatabase.child("room").child("0").child("type").setValue("qwe");

        mDatabase.child("room").child("0").child("type").setValue("eiei");
        nameCard.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot dataSnapshot){
                roomid = dataSnapshot.child("room").getChildrenCount() + 1;
                FirebaseUser user = mAuth.getCurrentUser();
                String uid = user.getUid();
                if(dataSnapshot.child("user").child(uid).child("notification").getValue(String.class)=="1")
                {
                    showNotification();
                    Log.d ("asd","zsdf");
                }
                  while (true)
                  {


                      if(!dataSnapshot.child("room").hasChild(Long.toString(roomid)))
                      {
                          break;
                      }
                        roomid++;



                  }


                      livenow =dataSnapshot.child("user").child(user.getUid()).child("nowlive").getValue(String.class);

                if(dataSnapshot.child("user").child(user.getUid()).child("owner").hasChild(livenow))
                {
                        owner = true;
                }
                else owner = false;

                while (true)
                {


                    if(!dataSnapshot.child("room").child(livenow).child("q").hasChild(Long.toString(turnq)))
                    {
                        break;
                    }
                    turnq++;



                }







            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if(view.getId()==R.id.button){
        int current=parseInt(viewModel.getString(),10);
        current++;
        String strCurrent= String.valueOf(current);

        viewModel.setString(strCurrent);
        binding.textView2.setText(viewModel.getString());}
        else if(view.getId()==R.id.btn_addImage){
            Intent intent =new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,GALLERY_INTENT);
        }
        else if(view.getId()==R.id.addroom){

            showNotification();
            Log.d ("aaaa","can click");
            Notification2 notification = new Notification2("asd","asd");
            Sender sender = new Sender (Common.currentToken,notification);
            mService.sendNotification (sender)
                    .enqueue (new Callback <MyResponse> () {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if(response.body().success == 1)
                                Toast.makeText (UsersActivity.this, "true", Toast.LENGTH_SHORT).show ();
                            else
                                Toast.makeText (UsersActivity.this, "false", Toast.LENGTH_SHORT).show ();
                        }

                        @Override
                        public void onFailure(Call <MyResponse> call, Throwable t) {
                            Log.e("ERROR",t.getMessage());
                        }
                    });

        }

        else if(view.getId()==R.id.q){
            jroomid = findViewById(R.id.roomid);
            FirebaseUser user = mAuth.getCurrentUser();

                mDatabase.child("user").child(user.getUid()).child("live").child(jroomid.getText().toString()).setValue("1");

            jroomid = findViewById(R.id.roomid);

            mDatabase.child("user").child(user.getUid()).child("nowlive").setValue(jroomid.getText().toString());



        }
        else if(view.getId()==R.id.enter){



        }

        else if(view.getId()==R.id.addq){

            FirebaseUser user = mAuth.getCurrentUser();
            mDatabase.child("room").child(livenow).child("q").child(Long.toString(turnq)).setValue(user.getUid());

        }
        else if(view.getId()==R.id.leave){

            FirebaseUser user = mAuth.getCurrentUser();
            mDatabase.child("user").child(user.getUid()).child("nowlive").setValue("0");
            setContentView(R.layout.activity_users);

        }
        else if(view.getId()==R.id.createroom){




                    mtypeField = findViewById(R.id.type);
            mdataField = findViewById(R.id.data);
            nameField=findViewById(R.id.name);

            mDatabase.child("room").child(Long.toString(roomid)).child("name").setValue(nameField.getText().toString());
            mDatabase.child("room").child(Long.toString(roomid)).child("type").setValue(mtypeField.getText().toString());
            mDatabase.child("room").child(Long.toString(roomid)).child("data").setValue(mdataField.getText().toString());
            FirebaseUser user = mAuth.getCurrentUser();
            mDatabase.child("user").child(user.getUid()).child("owner").child(Long.toString(roomid)).setValue("1");

            setContentView(R.layout.activity_users);
        }
    }

    private void showNotification() {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://devahoy.com/posts/android-notification/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("DevAhoy News")
                        .setContentText("สวัสดีครับ ยินดีต้อนรับเข้าสู่บทความ Android Notification :)")
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setVibrate(new long[] {800,100,500})
                        .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1000, notification);

        Log.d("aasd","asdasda");
    }

    public void signOut(View view) {
        viewModel.setLogoutSatus();
        mAuth.signOut();
        Intent i = new Intent(this,EmailPasswordActivity.class);
        startActivity(i);
    }

    public void click2(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        nameCard = database.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        jroomid = findViewById(R.id.roomid);
        mDatabase.child("user").child(user.getUid()).child("nowlive").setValue(jroomid.getText().toString());
        mDatabase.child("room").child("0").child("type").setValue("qwe");

        mDatabase.child("room").child("0").child("type").setValue("eiei");
        mDatabase.child("room").child("0").child("data").setValue("qwe");

        mDatabase.child("room").child("0").child("data").setValue("eiei");
        nameCard.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot dataSnapshot){
                roomid = dataSnapshot.child("room").getChildrenCount() + 1;
                while (true)
                {


                    if(!dataSnapshot.child("room").hasChild(Long.toString(roomid)))
                    {
                        break;
                    }
                    roomid++;



                }
                FirebaseUser user = mAuth.getCurrentUser();

                livenow =dataSnapshot.child("user").child(user.getUid()).child("nowlive").getValue(String.class);

                if(dataSnapshot.child("user").child(user.getUid()).child("owner").child(livenow).hasChild("1"))
                {
                    owner = true;
                    mDatabase.child("room").child("0").child("data").setValue("asdasd");
                }
                else owner = false;

                while (true)
                {


                    if(!dataSnapshot.child("room").child(livenow).child("q").hasChild(Long.toString(turnq)))
                    {
                        break;
                    }
                    turnq++;



                }







            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

            setContentView(R.layout.activity_addqaddmin);


    }

    public void click3(View v) {

       /* Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://devahoy.com/posts/android-notification/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("DevAhoy News")
                        .setContentText("สวัสดีครับ ยินดีต้อนรับเข้าสู่บทความ Android Notification :)")
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setVibrate(new long[] {800,100,500})
                        .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1000, notification);

        Log.d("aasd","asdasda");
        setContentView(R.layout.activity_test);*/

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
/*
        tempData =data;
        tempRequestCode=requestCode;
        tempResultCode=resultCode;
        */

        if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK){

            Uri uri=data.getData();
            final StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment());
           // mProgressDialog.setMessage("Uploading....");
            mProgressDialog.setMessage("uploading....");
            mProgressDialog.show();

           /* filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(UsersActivity.this,"upload Done",Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();

                    }
            });

            */

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                           // Log.d(TAG, "onSuccess: uri= "+ uri.toString());
                            String url = uri.toString();
                            mDatabase.child(mAuth.getCurrentUser().getUid()).child("pathPhoto").setValue(url);
                            Toast.makeText(UsersActivity.this,"upload Done",Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();
                        }
                    });
                }
            });

            // Create a reference to the file we want to download


         //   mDatabase.child(mAuth.getCurrentUser().getUid()).child("pathPhoto1").setValue(ref);
//            mDatabase.child(mAuth.getCurrentUser().getUid()).child("pathPhoto2").setValue(filepath.getDownloadUrl().getResult().toString());
           // mDatabase.child(mAuth.getCurrentUser().getUid()).child("pathPhoto3").setValue(filepath.getDownloadUrl().getResult().toString());
       //     mDatabase.child(mAuth.getCurrentUser().getUid()).child("pathPhoto4").setValue(filepath.getPath());
         //   mDatabase.child(mAuth.getCurrentUser().getUid()).child("pathPhoto5").setValue(filepath.getName());

        }

    }


}
