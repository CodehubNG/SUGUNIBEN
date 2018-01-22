package codehub.suguniben;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class viewPagerFragmentTwo extends Fragment {

    final static int GALLARY_REQUEST_CODE = 18472;
    DatabaseReference user_database;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    Uri image_uri = null;
    EditText fname;
    de.hdodenhof.circleimageview.CircleImageView userImage;
    Button submit;
    ProgressDialog progressDialog;
    AlertDialog.Builder alert;

    public viewPagerFragmentTwo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        user_database = FirebaseDatabase.getInstance().getReference().child("users");
        firebaseAuth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference().child("profile");

        progressDialog = new ProgressDialog(getContext().getApplicationContext());

        alert = new AlertDialog.Builder(getContext().getApplicationContext());
        alert.setCancelable(false);
        fname = (EditText) getActivity().findViewById(R.id.setup_fname);
        userImage = (de.hdodenhof.circleimageview.CircleImageView) getActivity().findViewById(R.id.setup_image_select);
        submit = (Button) getActivity().findViewById(R.id.setup_Submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fname.getText().toString().isEmpty()) {
                    setupAccount();
                }
            }
        });

        return inflater.inflate(R.layout.fragment_view_pager_fragment_two, container, false);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (GALLARY_REQUEST_CODE == requestCode) {
            image_uri = data.getData();
            userImage.setImageURI(image_uri);
        }
    }

    private void setupAccount() {
        if (image_uri != null) {
            if (fname != null || fname.length() < 3) {
                final String uid = firebaseAuth.getCurrentUser().getUid().toString();
                final String fnam = fname.getText().toString();

                progressDialog.setMessage("Setting Up Account ...");
                progressDialog.show();

                StorageReference filepath = storageReference.child(image_uri.getLastPathSegment());
                filepath.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String imgpath = taskSnapshot.getDownloadUrl().toString();
                        user_database.child(uid).child("fname").setValue(fnam);
                        user_database.child(uid).child("image").setValue(imgpath);
                        user_database.child(uid).child("access").setValue("1");
                        Intent mainIntent = new Intent(getContext().getApplicationContext(), My_Main_Activity.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                        getActivity().finish();
                        progressDialog.dismiss();
                    }
                });

            } else {
                alert.setMessage("invalid Nickname");
                alert.show();
            }

        } else {
            alert.setMessage("Select an image to continue");
            alert.show();
        }
    }

}
