package codehub.suguniben;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    final static int GALLARY_REQUEST_CODE = 18472;
    DatabaseReference user_database;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    Uri image_uri = null;
    EditText fname;
    CircleImageView userImage;
    FireClass fireClass;
    Button submit;
    ProgressBar progressBar;
    Button cancel;
    AlertDialog.Builder alert;
    private String image, name, access;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        user_database = FirebaseDatabase.getInstance().getReference().child("users");
        firebaseAuth = FirebaseAuth.getInstance();
        fireClass = new FireClass();
        storageReference = FirebaseStorage.getInstance().getReference().child("profile");

        progressBar = (ProgressBar) findViewById(R.id.setup_progress_bar);


        cancel = (Button) findViewById(R.id.setup_floatingActionButton_close);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        alert=new AlertDialog.Builder(this);
        alert.setCancelable(true);
        fname = (EditText) findViewById(R.id.setup_fname);
        userImage = (CircleImageView) findViewById(R.id.setup_image_select);

        try {
            name = getIntent().getStringExtra("name");
            image = getIntent().getStringExtra("image");
            access = getIntent().getStringExtra("access");
            if (name != null && name != "" && name.length() >= 1) {
                fname.setText(name);
            }
            if (image != null && image != "" && image.length() >= 1) {
                Picasso.with(getApplicationContext()).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(userImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(getApplicationContext()).load(image).into(userImage);
                    }
                });
            }


        } catch (NullPointerException nullP) {

        }

        submit = (Button) findViewById(R.id.setup_Submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fname.getText().toString().isEmpty()) {
                    setupAccount();
                }
            }
        });
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLARY_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void setupAccount() {
        cancel.setEnabled(false);
        submit.setEnabled(false);
        userImage.setEnabled(false);
        fname.setEnabled(false);
            if(fname!=null || fname.length()<3){
                final String uid = firebaseAuth.getCurrentUser().getUid().toString();
                final String fnam = fname.getText().toString();

                progressBar.setVisibility(View.VISIBLE);
                submit.setVisibility(View.GONE);

                if (image_uri != null) {

                    StorageReference filepath = storageReference.child(image_uri.getLastPathSegment());
                    filepath.putFile(image_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                String imgpath = task.getResult().getDownloadUrl().toString();
                                user_database.child(uid).child("fname").setValue(fnam);
                                user_database.child(uid).child("image").setValue(imgpath);
                                if (access != null && access != "" && access.length() == 1) {
                                    user_database.child(uid).child("access").setValue(access);
                                } else {
                                    user_database.child(uid).child("access").setValue("1");
                                }
                                user_database.child(uid).child("uid").setValue(uid);
                                user_database.child(uid).child("university").setValue(fireClass.university());
                                //   Intent mainIntent = new Intent(SetupActivity.this, My_Main_Activity.class);
                                cancel.setEnabled(true);
                                submit.setEnabled(true);
                                userImage.setEnabled(true);
                                fname.setEnabled(true);
                                progressBar.setVisibility(View.GONE);
                                submit.setVisibility(View.VISIBLE);
                                finish();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                submit.setVisibility(View.VISIBLE);
                                userImage.setEnabled(true);
                                fname.setEnabled(true);
                                cancel.setEnabled(true);
                                submit.setEnabled(true);
                                alert.setMessage(task.getException().getMessage().toString());
                                alert.show();
                            }
                        }
                    });
                } else if (image != null && image != "" && image.length() >= 1) {

                    progressBar.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.GONE);


                    String imgpath = image;
                    user_database.child(uid).child("fname").setValue(fnam);
                    user_database.child(uid).child("uid").setValue(uid);
                    user_database.child(uid).child("image").setValue(imgpath);
                    if (access != null && access != "" && access.length() == 1) {
                        user_database.child(uid).child("access").setValue(access);
                    } else {
                        user_database.child(uid).child("access").setValue("1");
                    }
                    user_database.child(uid).child("university").setValue(fireClass.university());
                    //    Intent mainIntent = new Intent(SetupActivity.this, My_Main_Activity.class);
                    progressBar.setVisibility(View.GONE);
                    submit.setVisibility(View.VISIBLE);
                    userImage.setEnabled(true);
                    fname.setEnabled(true);
                    cancel.setEnabled(true);
                    submit.setEnabled(true);
                    finish();


                } else {
                    progressBar.setVisibility(View.GONE);
                    submit.setVisibility(View.VISIBLE);
                    userImage.setEnabled(true);
                    fname.setEnabled(true);
                    cancel.setEnabled(true);
                    submit.setEnabled(true);
                    alert.setMessage("Select an image to continue");
                    alert.show();
                }
            } else {
                progressBar.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
                userImage.setEnabled(true);
                fname.setEnabled(true);
                cancel.setEnabled(true);
                submit.setEnabled(true);
                alert.setMessage("invalid Nickname");
                alert.show();
            }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (GALLARY_REQUEST_CODE == requestCode && resultCode == RESULT_OK) {
            image_uri = data.getData();
            userImage.setImageURI(image_uri);
        }
    }
}
