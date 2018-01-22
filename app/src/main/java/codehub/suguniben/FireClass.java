package codehub.suguniben;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

/**
 * Created by IK on 2017-11-12.
 */

public class FireClass extends Application {
    public static String PACKAGE_NAME;
    private static int PERMISSION_REQUEST_CODE = 839;
    DatabaseReference userDatabase;
    Boolean re = false;
    // PermissionManager permissionManager;
    FirebaseAuth firebaseAuth;
    private Boolean run = false;
    private FragmentManager supportFragmentManager;
    private String val;
    String access;
    private  static  int GALLERY_INT=839;

    public FireClass() {

    }


    public boolean network(Context context, RelativeLayout networkLayout) {
        ConnectivityManager net = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (net.getActiveNetworkInfo() != null && net.getActiveNetworkInfo().isAvailable() && net.getActiveNetworkInfo().isConnected()) {
            try {
              if(networkLayout!=null){
                  networkLayout.setVisibility(View.GONE);
              }
            } catch (Exception ex) {

            }
            return true;
        } else {
            if (net.getActiveNetworkInfo() != null && net.getActiveNetworkInfo().isAvailable() && net.getActiveNetworkInfo().isConnected()) {
                try {
                   if(networkLayout!=null){
                       networkLayout.setVisibility(View.VISIBLE);
                   }
                } catch (Exception ex) {

                }
            }
            return false;
        }
    }

    public static long currentDate() {
        Calendar c = Calendar.getInstance();
        //   SimpleDateFormat sdf = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
        // String strDate = sdf.format(c.getTime());
        long timeInMillis = System.currentTimeMillis();
        return timeInMillis;
    }
public UCrop.Options cropImageOptions(Context context){


    UCrop.Options options=new UCrop.Options();
    options.withAspectRatio(1,1);
    options.withMaxResultSize(512,512);
    options.setToolbarWidgetColor(ContextCompat.getColor(context, R.color.whiet));
    options.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
    options.setStatusBarColor(ContextCompat.getColor(context, R.color.colorPrimary));
    options.setActiveWidgetColor(ContextCompat.getColor(context, R.color.whiet));
    options.setToolbarWidgetColor(ContextCompat.getColor(context, R.color.whiet));
    options.setCompressionFormat(Bitmap.CompressFormat.PNG);

    return options;
}
    public void pickFromGallery(Activity activity) {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
       activity.startActivityForResult(intent, GALLERY_INT);

    }

    private Context getContext() {
        return getContext();
    }

    public void onCreate() {
        super.onCreate();
        //  PACKAGE_NAME=getApplicationContext().getPackageName();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
        // getActivity=new Activity().getParent();
        //  permissionManager=new android

    }

    public String university() {
        return "UNIBEN";
    }

    @Override
    public int checkCallingOrSelfPermission(String permission) {
        return super.checkCallingOrSelfPermission(permission);

    }

    public boolean securityCheck(String permission, Activity avx, Context ctx) {
        boolean boo = false;
        if (ContextCompat.checkSelfPermission(ctx, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(avx, permission)) {

            } else {
                ActivityCompat.requestPermissions(avx, new String[]{permission}, PERMISSION_REQUEST_CODE);

            }

        } else {
            boo = true;
        }
        return boo;
    }

    public Boolean checkifExist(final Context ctx) {
        firebaseAuth = FirebaseAuth.getInstance();
        run = true;
        if (firebaseAuth.getCurrentUser() != null) {
            userDatabase = FirebaseDatabase.getInstance().getReference().child("users");
            final String uid = firebaseAuth.getCurrentUser().getUid();
            final String tokenKey = FirebaseInstanceId.getInstance().getToken();

            userDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // try {
                    if (run) {
                        if (dataSnapshot.hasChild(uid)) {
                            userDatabase.child(firebaseAuth.getCurrentUser().getUid()).child("token").setValue(tokenKey);
                            Intent mainIntent = new Intent(ctx, Continue.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            ctx.startActivity(mainIntent);
                            run = false;
                            re = true;
                        } else {
                            Intent mainIntent = new Intent(ctx, SetupActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            ctx.startActivity(mainIntent);
                            run = false;
                            re = true;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            Intent mainIntent = new Intent(ctx, LoginActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(mainIntent);
            re = true;
        }


        return re;
    }

    /*public String accessLevel(final String uid){
       DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("users");
        db.keepSynced(true);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                val = dataSnapshot.child(uid).child("access").getValue().toString();
                // Toast.makeText(getApplicationContext(),dataSnapshot.child(firebaseAuth.getCurrentUser().getUid()).child("access").getValue().toString(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        return val;
    }*/
    //Tue Dec 12 05:42:55 GMT+1:00 2017
    public void likeClick(Context ctx) {
        try {
            MediaPlayer mp = MediaPlayer.create(ctx, R.raw.like);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            });
            mp.start();
        } catch (Exception ex) {

        }
    }

    public String timeago(Long d) {
        Locale LocaleBylanguageTag = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            LocaleBylanguageTag = Locale.forLanguageTag("en");
        }
        TimeAgoMessages messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();

        String text = TimeAgo.using(d, messages);
        return text;
    }

    public File bitmapToShare(Context ctx, String Title, Bitmap bitmap) {
        Random random = new Random();
        int ram = random.nextInt(99999);
        File root = Environment.getExternalStorageDirectory();
        File tempLocation = new File(root.getAbsolutePath() + "/Downloads/" + Title + "_" + ram + ".png");
        try {
            tempLocation.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(tempLocation);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            //  byte[] bytes=new byte[] ;

            //   bytes=stream.toByteArray();
            //  outputStream.write(bytes);
            outputStream.close();
            bitmap.recycle();
            outputStream.flush();
        } catch (Exception e) {
            Toast.makeText(ctx, "null" + tempLocation.length(), Toast.LENGTH_LONG).show();
        }

        return tempLocation;
    }


}
