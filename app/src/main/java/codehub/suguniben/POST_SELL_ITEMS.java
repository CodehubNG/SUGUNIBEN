package codehub.suguniben;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class POST_SELL_ITEMS extends AppCompatActivity {


    private static final int GALLAREY_REQUEST_CODE = 1;
    private FirebaseAuth firebaseAuth;
    private AlertDialog.Builder alert;
    private FireClass fireClass;
    private StorageReference storageReference;
    private DatabaseReference sellDatabase, houseDatabase, faviDatabase;
    private ImageButton img1, img2, img3;
    private TextView txt1, txt2, txt3;
    // private RadioButton rad_deivice, rad_housei, rad_others;
    private EditText p_name, p_amount, p_desc, p_phone, p_specify;
    private String p_type, img1path, img2path, img3path;
    private Uri img1uri,img2uri,img3uri;
    private ProgressDialog progressDialog;
    private Button clear_img_btn, upload_btn;
    private int reciver_image = 0;
    private String UNI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post__sell__items);
        firebaseAuth = FirebaseAuth.getInstance();
        fireClass = new FireClass();
        UNI = fireClass.university();
        sellDatabase = FirebaseDatabase.getInstance().getReference().child(UNI).child("sell");
        houseDatabase = FirebaseDatabase.getInstance().getReference().child(UNI).child("house");
        faviDatabase = FirebaseDatabase.getInstance().getReference().child(UNI).child("favi");

        sellDatabase.keepSynced(true);
        houseDatabase.keepSynced(true);
        faviDatabase.keepSynced(true);

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

        storageReference = FirebaseStorage.getInstance().getReference();

        alert=new AlertDialog.Builder(this);
        p_name = (EditText)findViewById(R.id.sell_post_property_name);
        p_amount = (EditText)findViewById(R.id.sell_post_amount);
        p_desc = (EditText)findViewById(R.id.sell_post_Desc);
        p_phone = (EditText) findViewById(R.id.sell_post_tel);
        // p_specify = (EditText) getActivity().findViewById(R.id.sell_post_specify);
        //rad_deivice = (RadioButton) getActivity().findViewById(R.id.sell_post_radBtn_Devices);
        //rad_housei = (RadioButton) getActivity().findViewById(R.id.sell_post_radBtn_House);
        // rad_others = (RadioButton) getActivity().findViewById(R.id.sell_post_radBtn_others);
        img1 = (ImageButton)findViewById(R.id.sell_post_img1);
        img2 = (ImageButton)findViewById(R.id.sell_post_img2);
        img3 = (ImageButton)findViewById(R.id.sell_post_img3);
        txt1 = (TextView)findViewById(R.id.sell_post_text1);
        txt2 = (TextView)findViewById(R.id.sell_post_text2);
        txt3 = (TextView)findViewById(R.id.sell_post_text3);
        clear_img_btn = (Button)findViewById(R.id.sell_post_clearImages);
        upload_btn = (Button)findViewById(R.id.sell_UPLOAD);
        ClickEvent();
    }
    @Override
    public void onStart() {
        super.onStart();
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  if(check()){
                startUploading();
                // }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == GALLAREY_REQUEST_CODE && resultCode == RESULT_OK) {
                if (reciver_image == 1) {
                    Uri uri = data.getData();
                    img1path = uri.getLastPathSegment();
                    img1uri=uri;
                    img1.setImageURI(uri);
                    if (img1path != null) {
                        img2.setVisibility(View.VISIBLE);
                    } else {
                        img2.setVisibility(View.INVISIBLE);
                    }
                } else if (reciver_image == 2) {
                    Uri uri = data.getData();
                    img2path = uri.getLastPathSegment();
                    img2uri=uri;
                    img2.setImageURI(uri);
                    if (img1path != null && img2path != null) {
                        img3.setVisibility(View.VISIBLE);
                    } else {
                        img2.setVisibility(View.INVISIBLE);
                    }
                } else if (reciver_image == 3) {
                    Uri uri = data.getData();
                    img3uri=uri;
                    img3path = uri.getLastPathSegment();
                    img3.setImageURI(uri);
                }

            }

        }
        catch (OutOfMemoryError ex){

        }
    }

    private void ClickEvent() {

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                reciver_image = 1;
                startActivityForResult(intent, GALLAREY_REQUEST_CODE);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                reciver_image = 2;
                startActivityForResult(intent, GALLAREY_REQUEST_CODE);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                reciver_image = 3;
                startActivityForResult(intent, GALLAREY_REQUEST_CODE);
            }
        });
        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                reciver_image = 1;
                startActivityForResult(intent, GALLAREY_REQUEST_CODE);
            }
        });
        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                reciver_image = 2;
                startActivityForResult(intent, GALLAREY_REQUEST_CODE);
            }
        });
        txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                reciver_image = 3;
                startActivityForResult(intent, GALLAREY_REQUEST_CODE);
            }
        });
        clear_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img1path = "";
                img2path = "";
                img3path = "";
                img1.setImageResource(R.drawable.sell_add_image);
                img2.setImageResource(R.drawable.sell_add_image);
                img3.setImageResource(R.drawable.sell_add_image);
            }
        });
        /*rad_deivice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    p_type = "Devices";
                    Toast.makeText(getContext(), p_type, Toast.LENGTH_SHORT).show();
                }
            }
        });
        rad_housei.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    p_type = "House";
                    Toast.makeText(getContext(), p_type, Toast.LENGTH_SHORT).show();
                }
            }
        });
        rad_others.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    p_specify.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), p_type, Toast.LENGTH_SHORT).show();
                } else {
                    p_specify.setVisibility(View.GONE);
                    Toast.makeText(getContext(), p_type, Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }

    //   private Boolean validate() {
    //    return false;
    // }

    private void startUploading() {
        final String time = String.valueOf(fireClass.currentDate());

        if(img1!=null){
            StorageReference path1=storageReference.child(p_type).child(img1path);
            progressDialog.setMessage("Uploading 1/3 ...");
            progressDialog.show();
            path1.putFile(img1uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final String down1=taskSnapshot.getDownloadUrl().toString();
                    if(img2!=null){
                        StorageReference path2=storageReference.child(p_type).child(img2path);
                        progressDialog.setMessage( "Uploading 2/3 ...");
                        path2.putFile(img2uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                final String  down2=taskSnapshot.getDownloadUrl().toString();
                                if(img3!=null){
                                    StorageReference path3=storageReference.child(p_type).child(img3path);
                                    progressDialog.setMessage("Uploading 3/3 ...");
                                    path3.putFile(img3uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            String   down3=taskSnapshot.getDownloadUrl().toString();
                                            if(img1uri!=null && img1uri!=null && img3uri!=null){
                                                DatabaseReference post = FirebaseDatabase.getInstance().getReference().child(UNI).child(p_type).push();
                                                post.child("name").setValue(p_name.getText().toString().trim());
                                                post.child("amount").setValue(p_amount.getText().toString().trim());
                                                post.child("desc").setValue(p_desc.getText().toString().trim());
                                                post.child("tel").setValue(p_phone.getText().toString().trim());
                                                post.child("img1").setValue(down1);
                                                post.child("img2").setValue(down2);
                                                post.child("img3").setValue(down3);
                                                post.child("time").setValue(String.valueOf(time));
                                                // Toast.makeText(getContext().getApplicationContext(),String.valueOf(fireClass.currentDate()),Toast.LENGTH_LONG).show();

                                                progressDialog.dismiss();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            });
        }

    }
  /*  private boolean check() {
        if (rad_deivice.isChecked()) {
            p_type = "devices";
        } else if (rad_housei.isChecked()) {
            p_type = "house";
        } else {
            if (p_specify.getText().toString().trim().length() != 0) {
                p_type = "others";
            } else {
                alert.setMessage("Please select the type of property you are about too sale");
                alert.show();
                p_type = "null";
            }
           }
           return true;
    }*/
}


