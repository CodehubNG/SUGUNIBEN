package codehub.suguniben;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

public class public_post extends AppCompatActivity {


    private static final int GALLERY_INT = 203;
    ProgressDialog progressDialog;
    ImageButton imageButtonn;
    private Uri ImageUri;
    private EditText Tille, Desc;
    private Button btnPost;
    private StorageReference storageRef;
    private DatabaseReference databaseRef;
    private DatabaseReference usersDataRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FireClass fireClass;
    private String UNI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_post);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        fireClass = new FireClass();
        UNI = fireClass.university();
        // String UNI=fireClass.university();
        databaseRef = FirebaseDatabase.getInstance().getReference().child(UNI).child("public");
        storageRef = FirebaseStorage.getInstance().getReference();
        usersDataRef = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());


        imageButtonn = (ImageButton) findViewById(R.id.public_post_imageButton);
        Tille = (EditText) findViewById(R.id.public_post_title);
        Desc = (EditText) findViewById(R.id.public_post_Desc);
        progressDialog = new ProgressDialog(this);
        btnPost = (Button) findViewById(R.id.public_post_btn_post);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startposting();
            }
        });

        imageButtonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallaryintent = new Intent(Intent.ACTION_PICK);
                gallaryintent.setType("image/*");
                startActivityForResult(gallaryintent, GALLERY_INT);

                //CropImage.startPickImageActivity(public_post.this);
                //\.setGuidelines(CropImageView.Guidelines.ON)
                //.setAspectRatio(1,1)
                //.start();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        ImageUri = data.getData();

        if (resultCode == RESULT_OK) {
            imageButtonn.setImageURI(ImageUri);
        }
    }


    private void startposting() {

        progressDialog.setMessage("Uploading Post ...");
        progressDialog.show();
        final String Title = Tille.getText().toString();
        final String Desc = this.Desc.getText().toString();
        Tille.setEnabled(false);
        Tille.setEnabled(true);
        this.Desc.setEnabled(false);
        this.Desc.setEnabled(true);
        if (ImageUri == null) {

            Snackbar.make(getCurrentFocus(), "Uploading Post", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            postWithOutPicture(Title, Desc);


        } else {

            Snackbar.make(getCurrentFocus(), "Uploading Post", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            postWithPicture(Title, Desc);
        }
    }

    private void postWithOutPicture(final String Titlee, final String Descc) {
        final DatabaseReference post = databaseRef.push();
        usersDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                post.child("title").setValue(Titlee);
                post.child("desc").setValue(Descc);
                post.child("image").setValue("");
                post.child("uid").setValue(firebaseUser.getUid());
                post.child("time").setValue(fireClass.currentDate());
                Tille.setText("");
                Desc.setText("");


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        progressDialog.dismiss();
    }

    private void postWithPicture(final String Titlee, final String Descc) {
        StorageReference filePath = storageRef.child("Photos").child(ImageUri.getLastPathSegment());
        filePath.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final String downloadUri = taskSnapshot.getDownloadUrl().toString();
                final DatabaseReference post = databaseRef.push();
                usersDataRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        post.child("title").setValue(Titlee);
                        post.child("desc").setValue(Descc);
                        post.child("image").setValue(downloadUri);
                        post.child("uid").setValue(firebaseUser.getUid());
                        post.child("time").setValue(fireClass.currentDate());
                        post.child("username").setValue(dataSnapshot.child("fname").getValue())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Tille.setText("");
                                            Desc.setText("");
                                        }
                                    }
                                });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                progressDialog.dismiss();
            }

        });
    }
}
