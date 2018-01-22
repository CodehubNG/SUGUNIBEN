package codehub.suguniben;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;



public class My_Main_Activity extends AppCompatActivity {

    FireClass fireClass = new FireClass();
    LinearLayout upload_layout;
    CircleImageView upload_progress;
    TextView upload_meaassge;
    ImageButton addnew, mainOptionButton;
    private DatabaseReference userDatabse;
    private FirebaseAuth firebaseAuth;
    private ImageView main_user_image;
    private TextView main_user_name, main_logout;
    private Activity activity;
    private ImageButton new_notification;
    private String getLastCat;
    public RelativeLayout network_Layout;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_sug_update:
                    disFrage(R.id.nav_sug_update);
                    return true;
                case R.id.nav_newsFeed:
                    disFrage(R.id.nav_newsFeed);
                    return true;
                case R.id.nav_Buy_Sell:
                    disFrage(R.id.nav_Buy_Sell);
                    return true;
                case R.id.nav_search:
                    disFrage(R.id.nav_search);
                    return true;
                case R.id.nav_Accomodation:
                    disFrage(R.id.nav_Accomodation);
                    return true;

            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my__main_);
        firebaseAuth=FirebaseAuth.getInstance();

        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        getLastCat=getIntent().getExtras().getString("cat","Beauty");
        try {
            String type = getIntent().getStringExtra("type");
            String message = getIntent().getStringExtra("msg");
            if (type != null && type != "") {
                if (type == "news_feed") {

                } else if (type == "sale") {

                } else if (type == "house") {

                } else if (type == "sug") {

                }
            }
        } catch (NullPointerException ex) {

        }
        userDatabse = FirebaseDatabase.getInstance().getReference().child("users");
        main_user_image = (ImageView) findViewById(R.id.main_user_Image);
        main_user_name = (TextView) findViewById(R.id.main_user_name);
        mainOptionButton = (ImageButton) findViewById(R.id.main_optionMenu_btn);

        mainOptionButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                ContextThemeWrapper ctx = new ContextThemeWrapper(getApplicationContext(), R.style.PopupMenu);
                PopupMenu popupMenu = new PopupMenu(ctx, view);
                popupMenu.inflate(R.menu.main_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                           /* case R.id.action_post:
                                Intent intent=new Intent(My_Main_Activity.this,PostActitvity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                return true;
                            case R.id.action_Sale_property:
                                Fragment fragment = new Sell_Itemas();
                                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.my_main_act_Frame, fragment);
                                ft.commit();
                                return true;*/
                            case R.id.action_logOut:
                                firebaseAuth.signOut();
                                if (fireClass.checkifExist(getApplicationContext())) ;
                                finish();
                                return true;
                            case R.id.action_exit:
                                Toast.makeText(getApplicationContext(), menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        main_user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(My_Main_Activity.this, Profile.class);
                intent.putExtra("acc_id", firebaseAuth.getCurrentUser().getUid().toString());
                startActivity(intent);
            }
        });
        main_user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(My_Main_Activity.this, Profile.class);
                intent.putExtra("acc_id", firebaseAuth.getCurrentUser().getUid().toString());
                startActivity(intent);
            }
        });
        new_notification = (ImageButton) findViewById(R.id.main_new_notification);
        new_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(My_Main_Activity.this, Notification_feed.class);
                startActivity(intent);
            }
        });
        userDatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String name = dataSnapshot.child(firebaseAuth.getCurrentUser().getUid().toString()).child("fname").getValue().toString();
                    final String img = dataSnapshot.child(firebaseAuth.getCurrentUser().getUid().toString()).child("image").getValue().toString();
                    if (firebaseAuth.getCurrentUser() != null) {
                        if (name != "") {
                            main_user_name.setText(name);
                        }
                        if (img.length() > 1) {
                            Picasso.with(getApplicationContext()).load(img).placeholder(R.drawable.user2).networkPolicy(NetworkPolicy.OFFLINE).into(main_user_image,
                                    new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError() {
                                            Picasso.with(getApplicationContext()).load(img).placeholder(R.drawable.user2).into(main_user_image, new Callback() {
                                                @Override
                                                public void onSuccess() {

                                                }

                                                @Override
                                                public void onError() {
main_user_image.setImageResource(R.drawable.user2);
                                                }
                                            });
                                        }
                                    });
                        } else {
                            main_user_image.setImageResource(R.drawable.user2);
                        }
                    }
                } catch (Exception ex) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        network_Layout = (RelativeLayout) findViewById(R.id.main_network_layout);

fireClass.network(getApplicationContext(),network_Layout);
        disFrage(R.id.nav_sug_update);

    }



    public void disFrage(int id) {
        Fragment fragment = null;
        switch (id) {
            case R.id.nav_sug_update:
              // fireClass.network(getApplicationContext());
                fragment = new Frag_Sug_Update();
                break;
            case R.id.nav_newsFeed:
                //fireClass.network(getApplicationContext());
                fragment = new F_NEWS_FEED();
                break;
            case R.id.nav_Buy_Sell:
                //fireClass.network(getApplicationContext());
                fragment = new F_BUY_AND_SELL();
                break;
            case R.id.nav_Accomodation:
               // fireClass.network(getApplicationContext());
                fragment = new F_HOUSE();
                break;
            case R.id.nav_search:
                //fireClass.network(getApplicationContext());
                fragment = new F_SEARCH();
                break;


        }
        if (fragment != null) {
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.my_main_act_Frame, fragment);
            trans.commit();
        }
    }


}
