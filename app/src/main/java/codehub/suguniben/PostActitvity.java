package codehub.suguniben;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;

import static codehub.suguniben.FireClass.currentDate;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

public class PostActitvity extends AppCompatActivity {
    private static final int GALLERY_INT = 203;

    ImageButton imageButton;
    Button add_photo;
    Button selectFeed;
    RelativeLayout ui1, ui2;
    private Uri ImageUri;
    private EditText mTille, mDesc;
    private Button mbtnPostNews;
    private StorageReference storageReference;
    private DatabaseReference postDatabase;
    private DatabaseReference usersDatabase;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private FireClass fireClass;
    //   private String UNI;
    //  private String time;
    private RadioButton  rad_sug_Update,rad_Sports, rad_Gossip, rad_Fashion, rad_Inspiration, rad_beauty, rad_Events, rad_music, rad_comedy, rad_movies, rad_gospel, rad_romances;
    //  String accessType;
    private String val;
    private AlertDialog.Builder alert;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_actitvity);

        firebaseAuth = FirebaseAuth.getInstance();


        storageReference = FirebaseStorage.getInstance().getReference();
        usersDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        usersDatabase.keepSynced(true);

        fireClass = new FireClass();
        //  UNI=fireClass.university();
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        add_photo = (Button) findViewById(R.id.post_add_photo);
        mTille = (EditText) findViewById(R.id.xml_title);
        mDesc = (EditText) findViewById(R.id.xml_desc);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        selectFeed = (Button) findViewById(R.id.post_select_feed);
        ui1 = (RelativeLayout) findViewById(R.id.post_ui_one);
        ui2 = (RelativeLayout) findViewById(R.id.post_ui_two);
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && action != null && type != null) {
            if (type.startsWith("image/")) {
                String body = intent.getStringExtra(Intent.EXTRA_TEXT);
                Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                if (body != null && body.length() >= 1 && body != "") {
                    mDesc.setText(body);
                }
                if (uri != null) {
                    UCrop uCrop = UCrop.of(uri
                            , Uri.fromFile(new File(getCacheDir()
                                    , firebaseAuth.getCurrentUser().getUid()+ currentDate()+".png")));
                    uCrop.withOptions(fireClass.cropImageOptions(this));
                    uCrop.start(PostActitvity.this);
                }
            } else if ("text/plain".equals(type) && type != null) {
                String body = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (body != null && body.length() >= 1 && body != "") {
                    mTille.setText(body);
                }
            }
        }

        selectFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ui1.setEnabled(false);
                ui2.setEnabled(true);
                ui2.setVisibility(View.VISIBLE);
            }
        });
        ui2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ui1.setEnabled(true);
                ui2.setVisibility(View.GONE);
                ui2.setEnabled(false);
            }
        });

        rad_sug_Update = (RadioButton) findViewById(R.id.post_act_rad_sug_update);
        rad_Sports = (RadioButton) findViewById(R.id.post_act_rad_sports);
        rad_Gossip = (RadioButton) findViewById(R.id.post_act_rad_Gossip);
        rad_Fashion = (RadioButton) findViewById(R.id.post_act_rad_Fashion);
        rad_Inspiration = (RadioButton) findViewById(R.id.post_act_rad_Inspiration);
        rad_beauty = (RadioButton) findViewById(R.id.post_act_rad_Beauty);
        rad_Events = (RadioButton) findViewById(R.id.post_act_rad_Events);
        rad_music = (RadioButton) findViewById(R.id.post_act_rad_Music);
        rad_comedy = (RadioButton) findViewById(R.id.post_act_rad_Comedy);
        rad_movies = (RadioButton) findViewById(R.id.post_act_rad_Movies);
        rad_gospel = (RadioButton) findViewById(R.id.post_act_rad_Gospel);
        rad_romances = (RadioButton) findViewById(R.id.post_act_rad_Romances);

        alert = new AlertDialog.Builder(getApplicationContext());
        alert.setCancelable(false);

        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                val = dataSnapshot.child(firebaseAuth.getCurrentUser().getUid()).child("access").getValue().toString();
                // Toast.makeText(getApplicationContext(),dataSnapshot.child(firebaseAuth.getCurrentUser().getUid()).child("access").getValue().toString(),Toast.LENGTH_SHORT).show();
                ch(val);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        mbtnPostNews = (Button) findViewById(R.id.xml_btn_post);

        //postDatabase = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("public");
/*
        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("access").getValue().toString()=="1"){
                    postDatabase = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("public");
                }
                else if(dataSnapshot.child("access").getValue()=="2"){
                    postDatabase = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("blog");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        mbtnPostNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radClick() != null) {
                    startposting();
                }
                ;

            }
        });

        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    Uri i=data.getData();

fireClass.pickFromGallery(PostActitvity.this);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (ui2.getVisibility() == View.VISIBLE) {
            ui1.setEnabled(true);
            ui2.setVisibility(View.GONE);
            ui2.setEnabled(false);

        } else {
return;
        }

    }




    private String radClick() {
        String s = null;
        if (rad_sug_Update.isChecked()) {
            postDatabase = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("Sug");
            s = "Sug";
        } else if (rad_Sports.isChecked()) {
            postDatabase = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("Sports");
            s = "Sports";
        } else if (rad_Gossip.isChecked()) {
            postDatabase = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("Gossip");
            s = "Gossip";
        } else if (rad_Fashion.isChecked()) {
            postDatabase = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("Fashion");
            s = "Fashion";
        } else if (rad_Inspiration.isChecked()) {
            postDatabase = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("Inspiration");
            s = "Inspiration";
        } else if (rad_beauty.isChecked()) {
            postDatabase = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("Beauty");
            s = "Beauty";
        } else if (rad_Events.isChecked()) {
            postDatabase = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("Events");
            s = "Events";
        } else if (rad_music.isChecked()) {
            postDatabase = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("Music");
            s = "Music";
        } else if (rad_comedy.isChecked()) {
            postDatabase = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("Comedy");
            s = "Comedy";
        } else if (rad_movies.isChecked()) {
            postDatabase = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("Movies");
            s = "Movies";
        } else if (rad_gospel.isChecked()) {
            postDatabase = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("Gospel");
            s = "Gospel";
        } else if (rad_romances.isChecked()) {
            postDatabase = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("Roman");
            s = "Roman";
        } else {
            s = null;
        }


        return s;
    }

    private void ch(String valu) {
        if (valu.matches("1")) {
            rad_sug_Update.setVisibility(View.GONE);
        } else if (valu.matches("2")||valu.matches("3")) {
            rad_sug_Update.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if(resultCode==RESULT_OK){
           UCrop uCrop = UCrop.of(data.getData()
                   , Uri.fromFile(new File(getCacheDir()
                           , firebaseAuth.getCurrentUser().getUid()+ currentDate()+".png")));
           uCrop.withOptions(fireClass.cropImageOptions(this));
           uCrop.start(PostActitvity.this);
       }
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            ImageUri=resultUri;
            imageButton.setImageURI(ImageUri);
        }

    }

    private void startposting() {
        final String Title = mTille.getText().toString();
        final String Desc = mDesc.getText().toString();
        mTille.setEnabled(false);
        mTille.setEnabled(true);
        mDesc.setEnabled(false);
        mDesc.setEnabled(true);
     /*   if (ImageUri.toString().length() >= 1 && Title.length() > 1 && mDesc.length() > 1) {
            // Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_SHORT).show();
            progressDialog.setMessage("Uploading Post ...");
            progressDialog.show();
            postWithPicture(Title, Desc);
        } else if (ImageUri.toString().length() < 1 && Title.length() < 1 && Desc.length() < 1) {
            //  Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_SHORT).show();
            progressDialog.setMessage("Uploading Post ...");
            progressDialog.show();
            postWithOutPicture(Title, Desc);
        } else if (ImageUri.toString().length() > 1 && Title.length() > 1 && Desc.length() < 1) {
            //Toast.makeText(getApplicationContext(),"3",Toast.LENGTH_SHORT).show();
            progressDialog.setMessage("Uploading Post ...");
            progressDialog.show();
            postWithPicture(Title, Desc);
        } else {
            Toast.makeText(getApplication(), "select a valid image format, enter a valid Title and Description ...", Toast.LENGTH_LONG).show();
        }*/
        progressDialog.setMessage("Uploading Post ...");
        progressDialog.show();
        postWithPicture(Title, Desc);
    }

    private void postWithOutPicture(final String Titlee, final String Descc) {
        final DatabaseReference post = postDatabase.push();
        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                post.child("title").setValue(Titlee);
                post.child("desc").setValue(Descc);
                post.child("image").setValue("");
                post.child("image_name").setValue(ImageUri.getLastPathSegment());
                post.child("uid").setValue(firebaseAuth.getCurrentUser().getUid());
                post.child("time").setValue(String.valueOf(currentDate()));
                post.child("username").setValue(dataSnapshot.child("Fname").getValue())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Snackbar.make(getCurrentFocus(), "Complete", Snackbar.LENGTH_LONG).show();
                                    // Toast.makeText(getApplicationContext(), "Post Uploaded", Toast.LENGTH_LONG).show();

                                } else {
                                    Snackbar.make(getCurrentFocus(), "Error uploading...", Snackbar.LENGTH_LONG).show();

                                }
                            }
                        });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void postWithPicture(final String Titlee, final String Descc) {
        if (ImageUri != null && ImageUri.getLastPathSegment().length() > 1) {
            StorageReference filePath = storageReference.child("Photos").child(ImageUri.getLastPathSegment());
            filePath.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.setMessage("Finishing ...");
                    final String downloadUri = taskSnapshot.getDownloadUrl().toString();
                    final String imageName = taskSnapshot.getStorage().getName();
                    final DatabaseReference post = postDatabase.push();
                    usersDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            post.child("title").setValue(Titlee);
                            post.child("desc").setValue(Descc);
                            post.child("image").setValue(downloadUri);
                            post.child("image_name").setValue(imageName);
                            post.child("uid").setValue(firebaseAuth.getCurrentUser().getUid());
                            post.child("time").setValue(String.valueOf(currentDate()));
                            post.child("username").setValue(dataSnapshot.child("fname").getValue())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                               finish();
                                            } else {
                                                progressDialog.setMessage("Error Uploading ....");
                                                progressDialog.dismiss();
                                            }
                                        }
                                    });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                }

            });
        } else {
            progressDialog.setMessage("Finishing ...");
            final String downloadUri = "";
            final String imageName = "";
            final DatabaseReference post = postDatabase.push();
            usersDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    post.child("title").setValue(Titlee);
                    post.child("desc").setValue(Descc);
                    post.child("image").setValue(downloadUri);
                    post.child("image_name").setValue(imageName);
                    post.child("uid").setValue(firebaseAuth.getCurrentUser().getUid());
                    post.child("time").setValue(String.valueOf(currentDate()));
                    post.child("username").setValue(dataSnapshot.child("fname").getValue())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                    finish();
                                    } else {
                                        progressDialog.setMessage("Error Uploading ....");
                                        progressDialog.dismiss();
                                    }
                                }
                            });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        }


    }
}
