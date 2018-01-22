package codehub.suguniben;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import static codehub.suguniben.FireClass.currentDate;

public class POST_RENT_HOSTEL extends AppCompatActivity {
    private RewardedVideoAd rewardedVideoAd;
    private static final int GALLAREY_REQUEST_CODE = 1;
    private FirebaseAuth firebaseAuth;
    private AlertDialog.Builder alert;
    private FireClass fireClass;
    private StorageReference storageReference;
    private DatabaseReference sellDatabase, houseDatabase, faviDatabase;

    private ImageButton img1, img2, img3;
    private TextView txt1, txt2, txt3;
    //  private RadioButton rad_campus, rad_offCampus, rad_others;
    private EditText p_name, p_amount, p_desc, p_phone, p_specify, p_address;
    private String p_type, img1path, img2path, img3path;
    private Uri img1uri, img2uri, img3uri;
    private ProgressDialog progressDialog;
    private Button clear_img_btn, upload_btn;
    private int reciver_image = 0;
    private String UNI;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_rent_hostel);
        firebaseAuth = FirebaseAuth.getInstance();
        fireClass = new FireClass();
        UNI = fireClass.university();
        sellDatabase = FirebaseDatabase.getInstance().getReference().child(UNI).child("sell");
        houseDatabase = FirebaseDatabase.getInstance().getReference().child(UNI).child("house");
        faviDatabase = FirebaseDatabase.getInstance().getReference().child(UNI).child("favi");

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        loadVideoAds();
        alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {

            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {

            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                startUploading();
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                loadVideoAds();
            }
        });

        sellDatabase.keepSynced(true);
        houseDatabase.keepSynced(true);
        faviDatabase.keepSynced(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        storageReference = FirebaseStorage.getInstance().getReference();

        alert = new AlertDialog.Builder(this);
        p_name = (EditText) findViewById(R.id.rent_post_property_name);
        p_amount = (EditText) findViewById(R.id.rent_post_amount);
        p_desc = (EditText) findViewById(R.id.rent_post_property_decs);
        p_phone = (EditText) findViewById(R.id.rent_post_tel);
        p_address = (EditText) findViewById(R.id.rent_post_Address);
        progressBar=(ProgressBar)findViewById(R.id.post_rent_house_progress_bar);

        // p_specify = (EditText) getActivity().findViewById(R.id.sell_post_specify);
        //rad_campus = (RadioButton) getActivity().findViewById(R.id.sell_post_radBtn_Campus);
        //rad_offCampus = (RadioButton) getActivity().findViewById(R.id.sell_post_radBtn_Off_Campus);
        // rad_others = (RadioButton) getActivity().findViewById(R.id.sell_post_radBtn_others);
        img1 = (ImageButton) findViewById(R.id.rent_post_img1);
        img2 = (ImageButton) findViewById(R.id.rent_post_img2);
        img3 = (ImageButton) findViewById(R.id.rent_post_img3);
        txt1 = (TextView) findViewById(R.id.rent_post_text1);
        txt2 = (TextView) findViewById(R.id.rent_post_text2);
        txt3 = (TextView) findViewById(R.id.rent_post_text3);
        clear_img_btn = (Button) findViewById(R.id.sell_post_clearImages);
        upload_btn = (Button) findViewById(R.id.sell_UPLOAD);
        ClickEvent();
    }

    private void loadVideoAds() {
        rewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
    }

    @Override
    public void onStart() {
        super.onStart();
        upload_btn.setOnClickListener(new View.OnClickListener() {
RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.main_network_layout);
            @Override
            public void onClick(View view) {
                upload_btn.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                if (fireClass.network(getApplicationContext(),relativeLayout)) {
                    if (rewardedVideoAd.isLoaded()) {
                        try {

                            alert.setCancelable(false);
                            alert.setTitle("Rent Hostel");
                            alert.setMessage("To continue to will have to watch a ads \n\n\n Do you want to PROCEED");
                            alert.setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    rewardedVideoAd.show();
                                    dialogInterface.dismiss();
                                }
                            });
                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            alert.show();

                        } catch (Exception ex) {
                        }
                   } else {
                        alert.setCancelable(true);
                        alert.setTitle("ALERT");
                        alert.setMessage("Ads not ready yet Try again in little bit");
                        alert.show();
                    }
                    upload_btn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }else {
                    alert.setCancelable(true);
                    alert.setTitle("ALERT");
                    alert.setMessage("Internet Connection is required to continue");
                    alert.show();
                }
            }
        });

    }

    @Override
    public void onResume() {
        rewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        rewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        rewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {




            if (requestCode == GALLAREY_REQUEST_CODE && resultCode == RESULT_OK) {
                UCrop uCrop = UCrop.of(data.getData()
                        , Uri.fromFile(new File(getCacheDir()
                                , firebaseAuth.getCurrentUser().getUid()+ currentDate()+".png")));
                uCrop.withOptions(fireClass.cropImageOptions(this));
                uCrop.start(POST_RENT_HOSTEL.this);

                if (reciver_image == 1) {

                    if (img1path != null) {
                        img2.setVisibility(View.VISIBLE);
                    } else {
                        img2.setVisibility(View.INVISIBLE);
                    }
                    if (requestCode == UCrop.REQUEST_CROP) {
                        Uri uri = data.getData();
                        img1path = uri.getLastPathSegment();
                        img1uri = uri;
                        img1.setImageURI(uri);
                    }

                } else if (reciver_image == 2) {
                    Uri uri = data.getData();
                    img2path = uri.getLastPathSegment();
                    img2uri = uri;
                    img2.setImageURI(uri);
                    if (img1path != null && img2path != null) {
                        img3.setVisibility(View.VISIBLE);
                    } else {
                        img2.setVisibility(View.INVISIBLE);
                    }
                } else if (reciver_image == 3) {
                    Uri uri = data.getData();
                    img3uri = uri;
                    img3path = uri.getLastPathSegment();
                    img3.setImageURI(uri);
                }

            }

        } catch (OutOfMemoryError ex) {

        }
    }

    private void ClickEvent() {
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reciver_image = 1;
                fireClass.pickFromGallery(POST_RENT_HOSTEL.this);

            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reciver_image = 2;
                fireClass.pickFromGallery(POST_RENT_HOSTEL.this);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 reciver_image = 3;
                fireClass.pickFromGallery(POST_RENT_HOSTEL.this);
            }
        });
        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reciver_image = 1;
                fireClass.pickFromGallery(POST_RENT_HOSTEL.this);

            }
        });
        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reciver_image = 2;
                fireClass.pickFromGallery(POST_RENT_HOSTEL.this);

            }
        });
        txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reciver_image = 3;
                fireClass.pickFromGallery(POST_RENT_HOSTEL.this);

            }
        });
        clear_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img1path = "";
                img2path = "";
                img3path = "";
                img1.setImageResource(R.drawable.add_image);
                img2.setImageResource(R.drawable.add_image);
                img3.setImageResource(R.drawable.add_image);
            }
        });
       /* rad_campus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    p_type = "campus";
                    Toast.makeText(getContext(), p_type, Toast.LENGTH_SHORT).show();
                }
            }
        });
        rad_offCampus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    p_type = "off_campus";
                    Toast.makeText(getContext(), p_type, Toast.LENGTH_SHORT).show();
                }
            }
        });
        rad_others.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    p_type = "campus_others";
                }
            }
        });*/
    }

    // private Boolean validate() {
    //  return false;
    //  }

    private void startUploading() {
        final String time = String.valueOf(fireClass.currentDate());

        if (img1 != null&&img1path.length()>1&&img1path!=null&&img1path!="") {
            StorageReference path1 = storageReference.child("house").child(img1path);
            progressDialog.setMessage("Uploading 1/3 ...");
            progressDialog.show();
            path1.putFile(img1uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        {
                            final String down1 = task.getResult().getDownloadUrl().toString();
                            if (img2 != null&&img2path.length()>1&&img2path!=null&&img2path!="") {
                                StorageReference path2 = storageReference.child("house").child(img2path);
                                progressDialog.setMessage("Uploading 2/3 ...");
                                path2.putFile(img2uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            final String down2 = task.getResult().getDownloadUrl().toString();
                                            if (img3 != null&&img3path.length()>1&&img3path!=null&&img3path!="") {
                                                StorageReference path3 = storageReference.child("house").child(img3path);
                                                progressDialog.setMessage("Uploading 3/3 ...");
                                                path3.putFile(img3uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            String down3 = task.getResult().getDownloadUrl().toString();
                                                            if (img1uri != null && img2uri != null && img3uri != null) {
                                                                DatabaseReference post = FirebaseDatabase.getInstance().getReference().child(UNI).child("house").push();
                                                                post.child("name").setValue(p_name.getText().toString().trim());
                                                                post.child("amount").setValue(p_amount.getText().toString().trim());
                                                                post.child("tel").setValue(p_phone.getText().toString().trim());
                                                                //  post.child("specify").setValue(p_specify.getText().toString().trim());
                                                                post.child("time").setValue(String.valueOf(fireClass.currentDate()));
                                                                // Toast.makeText(getContext().getApplicationContext(),String.valueOf(fireClass.currentDate()),Toast.LENGTH_LONG).show();
                                                                post.child("img1").setValue(down1);
                                                                post.child("img2").setValue(down2);
                                                                post.child("img3").setValue(down3);
                                                                progressDialog.dismiss();
                                                            }
                                                        } else {
                                                            try{
                                                                progressDialog.dismiss();
                                                                alert.setCancelable(true);
                                                                alert.setTitle("Alert");
                                                                alert.setMessage(task.getException().getMessage());
                                                                alert.show();
                                                            }catch (Exception ex){}
                                                        }
                                                    }
                                                });
                                            }else {
                                                progressDialog.dismiss();
                                                alert.setCancelable(true);
                                                alert.setTitle("Alert");
                                                alert.setMessage("Sorry but you need to Upload at least three images for item confirmation...\nThank You!");
                                                alert.show();
                                            }
                                        } else {
                                            try{
                                                progressDialog.dismiss();
                                                alert.setCancelable(true);
                                                alert.setTitle("Alert");
                                                alert.setMessage(task.getException().getMessage());
                                                alert.show();
                                            }catch (Exception ex){}
                                        }
                                    }
                                });
                            }else {
                                progressDialog.dismiss();
                                alert.setCancelable(true);
                                alert.setTitle("Alert");
                                alert.setMessage("Sorry but you need to Upload at least three images for item confirmation...\nThank You!");
                                alert.show();
                            }
                        }
                    } else {
                       try{
                           progressDialog.dismiss();
                           alert.setCancelable(true);
                           alert.setTitle("Alert");
                           alert.setMessage(task.getException().getMessage());
                           alert.show();
                       }catch (Exception ex){}
                    }
                }
            });
        }else {
            progressDialog.dismiss();
            alert.setCancelable(true);
            alert.setTitle("Alert");
            alert.setMessage("Sorry but you need to Upload at least three images for item confirmation...\nThank You!");
            alert.show();
        }

    }

   /* private boolean check() {
        if (rad_campus.isChecked()) {
            p_type = "campus";
        } else if (rad_offCampus.isChecked()) {
            p_type = "off_campus";
        } else {
            if (p_specify.getText().toString().trim().length() != 0) {
                p_type = "campus_others";
            } else {
                alert.setMessage("Please select the type of property you are about too sale");
                alert.show();
                p_type = "null";
            }
        }
        return true;
    }*/

}