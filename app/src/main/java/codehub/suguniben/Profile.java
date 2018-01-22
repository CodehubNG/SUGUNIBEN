package codehub.suguniben;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    private static Context context;
    // private int count;
    private static int GALLERY_REQUEST_CODE = 405;
    DatabaseReference sug_update_db, sport_db, gossip_DB, fashion_db, music_DB, comedy_db, beauty_Db, event_Db, movies_DB, insipration_Db, gospel_DB, notification, roman_Db;
    private String acc_id_colleter;
    private RecyclerView recyclerView;
    private FireClass fireClass;
    private DatabaseReference userDatabse, follodatabse, followingdatabase, views, likeDatabase;
    private FirebaseAuth firebaseAuth;
    private CollapsingToolbarLayout profileDisplay;
    private String accName, accImage, accPost;
    private ImageView profile_image;
    private TextView num_of_views, num_of_followers, num_of_following, num_of_post, selected_post_header;
    private Boolean likeClike = false;
    private String access;
    private Boolean follow = false;
    private Boolean follow_f = false;
    private TextView acc_id;
    private String UNI;
    //DatabaseReference sug_update_db,sport_db,gossip_DB,fashion_db,music_DB,comedy_db,beauty_Db,event_Db,movies_DB,insipration_Db,gospel_DB;
    private ImageButton showBeautyPost, showComedyPost, showEventsPost, showMusicPost, showSugPost, showSportPost, showGossipPost, showFashionPost,
            showMoviePost, showInsipirationPost, showGospelPost, showRoman;
    private LinearLayout lshowBeautyPost, lshowComedyPost, lshowEventsPost, lshowMusicPost, lshowSportPost, lshowGossipPost, lshowFashionPost,
            lshowMoviePost, lshowInsipirationPost, lshowGospelPost, lshowRoman;
    private Button folow_folow;
    private ProgressBar progressBar;
    // private String get_nickname,get_image;
    private LinearLayout showFollowers, showFollowing;
    private TextView noPost;
    private FirebaseRecyclerAdapter<followersGetter, Profile.followersViewHolder> followersfollowersViewHolderFirebaseRecyclerAdapter;
    private FirebaseRecyclerAdapter<followersGetter, Profile.followingViewHolder> folloingViewHolderFirebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        acc_id = (TextView) findViewById(R.id.profile_post_id_holder);
        acc_id_colleter = getIntent().getExtras().getString("acc_id");
        if (acc_id_colleter.length() > 1) {
            acc_id.setText(acc_id_colleter);
        }
        recyclerView = (RecyclerView) findViewById(R.id.profile_Recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        firebaseAuth = FirebaseAuth.getInstance();
        userDatabse = FirebaseDatabase.getInstance().getReference().child("users");
        fireClass = new FireClass();
        UNI = fireClass.university();
        // views=FirebaseDatabase.getInstance().getReference().child("views");
        //views.keepSynced(true);
        likeDatabase = FirebaseDatabase.getInstance().getReference().child(UNI).child("likes");
        //num_of_views = (TextView) findViewById(R.id.profile_numbers_of_views);
        num_of_followers = (TextView) findViewById(R.id.profile_numbers_of_folowers);
        num_of_following = (TextView) findViewById(R.id.profile_numbers_of_folowing);
        folow_folow = (Button) findViewById(R.id.temp_followers_follow_btn);
        selected_post_header = (TextView) findViewById(R.id.profile_currentHeader);
        noPost = (TextView) findViewById(R.id.profile_NO_POST);
        showFollowers = (LinearLayout) findViewById(R.id.profile_show_followers_btn);
        showFollowing = (LinearLayout) findViewById(R.id.profile_show_following_btn);
        showBeautyPost = (ImageButton) findViewById(R.id.profile_show_beauty_post);
        showComedyPost = (ImageButton) findViewById(R.id.profile_show_comedy_post);
        showEventsPost = (ImageButton) findViewById(R.id.profile_show_events_post);
        showMusicPost = (ImageButton) findViewById(R.id.profile_show_music_post);
        // showSugPost = (ImageButton) findViewById(R.id.profile_show_sug_post);
        showSportPost = (ImageButton) findViewById(R.id.profile_show_sport_post);
        showGossipPost = (ImageButton) findViewById(R.id.profile_show_gossip_post);
        showFashionPost = (ImageButton) findViewById(R.id.profile_show_Fashion_post);
        showMoviePost = (ImageButton) findViewById(R.id.profile_show_movies_post);
        showInsipirationPost = (ImageButton) findViewById(R.id.profile_show_inspiration_post);
        showGospelPost = (ImageButton) findViewById(R.id.profile_show_gospel_post);
        showRoman = (ImageButton) findViewById(R.id.main_show_roman_post);
        lshowRoman = (LinearLayout) findViewById(R.id.main_show_roman_postL);
        lshowBeautyPost = (LinearLayout) findViewById(R.id.profile_show_beauty_postL);
        lshowComedyPost = (LinearLayout) findViewById(R.id.profile_show_comedy_postL);
        lshowEventsPost = (LinearLayout) findViewById(R.id.profile_show_events_postL);
        lshowMusicPost = (LinearLayout) findViewById(R.id.profile_show_music_postL);
        // showSugPost = (ImageButton) findViewById(R.id.profile_show_sug_post);
        lshowSportPost = (LinearLayout) findViewById(R.id.profile_show_sport_postL);
        lshowGossipPost = (LinearLayout) findViewById(R.id.profile_show_gossip_postL);
        lshowFashionPost = (LinearLayout) findViewById(R.id.profile_show_Fashion_postL);
        lshowMoviePost = (LinearLayout) findViewById(R.id.profile_show_movies_postL);
        lshowInsipirationPost = (LinearLayout) findViewById(R.id.profile_show_inspiration_postL);
        lshowGospelPost = (LinearLayout) findViewById(R.id.profile_show_gospel_postL);

        profileDisplay = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        profile_image = (ImageView) findViewById(R.id.toolbar_layout_image);

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);

        userDatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                accName = dataSnapshot.child(acc_id.getText().toString()).child("fname").getValue().toString();
                accImage = dataSnapshot.child(acc_id.getText().toString()).child("image").getValue().toString();
                access = dataSnapshot.child(acc_id.getText().toString()).child("access").getValue().toString();
                if (accImage != null && accImage != "default") {
                    Picasso.with(getApplicationContext()).load(accImage).networkPolicy(NetworkPolicy.OFFLINE).into(profile_image, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(getApplicationContext()).load(accImage).into(profile_image);
                        }
                    });
                } else {
                    profile_image.setImageResource(R.color.colorPrimary);
                }
                profileDisplay.setTitle(accName);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        if (firebaseAuth.getCurrentUser().getUid().matches(acc_id.getText().toString())) {

            follodatabse = FirebaseDatabase.getInstance().getReference().child(UNI).child("followers").child(acc_id.getText().toString());
            followingdatabase = FirebaseDatabase.getInstance().getReference().child(UNI).child("following").child(firebaseAuth.getCurrentUser().getUid());

        } else {
            follodatabse = FirebaseDatabase.getInstance().getReference().child(UNI).child("followers").child(acc_id.getText().toString());
            followingdatabase = FirebaseDatabase.getInstance().getReference().child(UNI).child("following").child(acc_id.getText().toString());

        }
        final DatabaseReference following = FirebaseDatabase.getInstance().getReference().child(UNI).child("following").child(firebaseAuth.getCurrentUser().getUid());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                follow = true;
                String loginUser = firebaseAuth.getCurrentUser().getUid();
                if (!loginUser.matches(acc_id.getText().toString())) {
                    follodatabse.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (follow) {
                                if (dataSnapshot.hasChild(firebaseAuth.getCurrentUser().getUid())) {
                                    follodatabse.child(firebaseAuth.getCurrentUser().getUid()).removeValue();
                                    following.child(acc_id.getText().toString()).removeValue();
                                    follow = false;
                                } else {
                                    follodatabse.child(acc_id.getText().toString()).child("uid").setValue(firebaseAuth.getCurrentUser().getUid());
                                    following.child(acc_id.getText().toString()).child("uid").setValue(acc_id.getText().toString());
                                    follow = false;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    Intent intent = new Intent(Profile.this, SetupActivity.class);
                    intent.putExtra("name", accName);
                    intent.putExtra("access", access);
                    intent.putExtra("image", accImage);
                    startActivity(intent);
                }

            }
        });

        notification = FirebaseDatabase.getInstance().getReference().child("notification");
        notification.keepSynced(true);


        sug_update_db = FirebaseDatabase.getInstance().getReference().child(UNI).child("Sug");
        sport_db = FirebaseDatabase.getInstance().getReference().child(UNI).child("Sports");
        gossip_DB = FirebaseDatabase.getInstance().getReference().child(UNI).child("Gossip");
        fashion_db = FirebaseDatabase.getInstance().getReference().child(UNI).child("Fashion");
        insipration_Db = FirebaseDatabase.getInstance().getReference().child(UNI).child("Inspiration");
        beauty_Db = FirebaseDatabase.getInstance().getReference().child(UNI).child("Beauty");
        event_Db = FirebaseDatabase.getInstance().getReference().child(UNI).child("Events");
        music_DB = FirebaseDatabase.getInstance().getReference().child(UNI).child("Music");
        comedy_db = FirebaseDatabase.getInstance().getReference().child(UNI).child("Comedy");
        movies_DB = FirebaseDatabase.getInstance().getReference().child(UNI).child("Movies");
        gospel_DB = FirebaseDatabase.getInstance().getReference().child(UNI).child("Gospel");
        roman_Db = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("Roman");

        sug_update_db.keepSynced(true);
        sport_db.keepSynced(true);
        gossip_DB.keepSynced(true);
        fashion_db.keepSynced(true);
        music_DB.keepSynced(true);
        comedy_db.keepSynced(true);
        beauty_Db.keepSynced(true);
        event_Db.keepSynced(true);
        movies_DB.keepSynced(true);
        insipration_Db.keepSynced(true);
        gospel_DB.keepSynced(true);
        roman_Db.keepSynced(true);
        //database=FirebaseDatabase.getInstance().getReference().child("Comedy");
        // database.keepSynced(true);
        // Toast.makeText(getApplicationContext(),firebaseAuth.getCurrentUser().getUid(),Toast.LENGTH_LONG).show();
        //   loadData("Beauty");

        follodatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                num_of_followers.setText(String.valueOf(dataSnapshot.getChildrenCount()));

//                followersfollowersViewHolderFirebaseRecyclerAdapter.notifyAll();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        followingdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                num_of_following.setText(String.valueOf(dataSnapshot.getChildrenCount()));

//                followersfollowersViewHolderFirebaseRecyclerAdapter.notifyAll();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        followersfollowersViewHolderFirebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<followersGetter, Profile.followersViewHolder>(
                        followersGetter.class,
                        R.layout.temp_followers,
                        Profile.followersViewHolder.class,
                        follodatabse
                ) {
                    @Override
                    protected void populateViewHolder(final Profile.followersViewHolder viewHolder, final followersGetter model, int position) {
                        //  Toast.makeText(Profile.this, model.getUid() + "  Hi", Toast.LENGTH_SHORT).show();
                        //   Toast.makeText(Profile.this, "hello", Toast.LENGTH_SHORT).show();
                     /*      final String uid = model.getUid();

                        viewHolder.txtname.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getContext(), Profile.class);
                                intent.putExtra("acc_id", model.getUid());
                                startActivity(intent);
                            }
                        });
                        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getContext(), Profile.class);
                                intent.putExtra("acc_id", model.getUid());
                                startActivity(intent);
                            }
                        });

                      //  viewHolder.setFollowMe(uid, firebaseAuth.getCurrentUser().getUid());
                        Toast.makeText(Profile.this, uid, Toast.LENGTH_SHORT).show();
                    userDatabse.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (uid != null && uid != "") {
                                    viewHolder.setName(dataSnapshot.child(uid).child("fname").getValue().toString(), acc_id, uid);
                                    viewHolder.setImage(getApplicationContext(), dataSnapshot.child(uid).child("image").getValue().toString(), uid, acc_id);
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
*/
                    }
                };
        folloingViewHolderFirebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<followersGetter, Profile.followingViewHolder>(
                        followersGetter.class,
                        R.layout.temp_followers,
                        Profile.followingViewHolder.class,
                        followingdatabase
                ) {
                    @Override
                    protected void populateViewHolder(final Profile.followingViewHolder viewHolder, final followersGetter model, int position) {
                        //  Toast.makeText(Profile.this, model.getUid() + "  Hi", Toast.LENGTH_SHORT).show();
                        //   Toast.makeText(Profile.this, "hello", Toast.LENGTH_SHORT).show();
                        final String uid = model.getUid();

                    /*    viewHolder.txtname.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getContext(), Profile.class);
                                intent.putExtra("acc_id", model.getUid());
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        });
                        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getContext(), Profile.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("acc_id", model.getUid());
                                startActivity(intent);
                            }
                        });

                        viewHolder.setFollowMe(uid, firebaseAuth.getCurrentUser().getUid());
                        Toast.makeText(Profile.this, uid, Toast.LENGTH_SHORT).show();
                        userDatabse.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (uid.length() > 1) {
                                    viewHolder.setName(dataSnapshot.child(uid).child("fname").getValue().toString(), acc_id, uid);
                                    viewHolder.setimage(getApplicationContext(), dataSnapshot.child(uid).child("image").getValue().toString());
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
*/
                    }
                };
     //   recyclerView.setAdapter(followersfollowersViewHolderFirebaseRecyclerAdapter);
//Query allView=views;
//count=0;
/*allView.orderByChild("uid").addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
              class Message {
                  private String uid;
                  private Message() {}

                  public Message(String uid, String text) {
                      this.uid = uid;
                  }

                  public String getUid() {
                      return uid;
                  }
              }

          //    Message m = dataSnapshot.getValue(Message.class);
          if(dataSnapshot.getValue().toString()==acc_id.getText().toString())
          {
              count=count+1;
              num_of_views.setText(String.valueOf(count));

          }
         // Toast.makeText(getApplicationContext(),m.uid +" hi its me "+String.valueOf(count),Toast.LENGTH_LONG).show();
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
      });
*/
        showFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                loadFollowers(followersfollowersViewHolderFirebaseRecyclerAdapter);
                progressBar.setVisibility(View.INVISIBLE);
                //followersfollowersViewHolderFirebaseRecyclerAdapter.

            }
        });
        showFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                loadFollowers(folloingViewHolderFirebaseRecyclerAdapter);
                progressBar.setVisibility(View.INVISIBLE);
                //followersfollowersViewHolderFirebaseRecyclerAdapter.

            }
        });


    }

    private void currentUser_VS_acc_id() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
        }
    }

    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    protected void onStart() {
        super.onStart();
        selected_post_header.setText("Followers");

        perforeCircleLoadData();

    }

    private void loadFollowers(FirebaseRecyclerAdapter folowRecylerBase) {
        if (folowRecylerBase == folloingViewHolderFirebaseRecyclerAdapter) {
            selected_post_header.setText("Following");
            if (folowRecylerBase.getItemCount() >= 1) {
                noPost.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(folowRecylerBase);
            } else {
                recyclerView.setVisibility(View.INVISIBLE);
                noPost.setText("");
                noPost.setVisibility(View.VISIBLE);
            }
        } else if (folowRecylerBase == followersfollowersViewHolderFirebaseRecyclerAdapter) {
            selected_post_header.setText("Followers");
            if (folowRecylerBase.getItemCount() >= 1) {
                noPost.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(folowRecylerBase);
            } else {
                recyclerView.setVisibility(View.INVISIBLE);
                noPost.setText("NO FOLLOWERS");
                noPost.setVisibility(View.VISIBLE);
            }
        }
    }

    private void perforeCircleLoadData() {
        //Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();
        final DatabaseReference views = FirebaseDatabase.getInstance().getReference().child(UNI).child("views");
        views.keepSynced(true);
        progressBar.setVisibility(View.VISIBLE);

        final FirebaseRecyclerAdapter<News, Profile.NewsFeeds> BeautyfeedsFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, Profile.NewsFeeds>(
                News.class
                , R.layout.templatesug_and_public_post
                , Profile.NewsFeeds.class
                , beauty_Db.orderByChild("uid").equalTo(acc_id.getText().toString())

        ) {
            @Override
            protected void populateViewHolder(final Profile.NewsFeeds viewHolder, final News model, int position) {
                final String post_id = getRef(position).getKey().toString();

                viewHolder.setTitle(model.getTitle(), model.getTime());
                viewHolder.setdesc(model.getDesc());
                //  viewHolder.setviews(String.valueOf(model.getViews()), getRef(position));

                viewHolder.setimage(getContext(), model.getImage());
                viewHolder.sharebtn(getContext(), model.getTitle(), model.getDesc(),
                        access, post_id, model.getImage(), model.getImage_name(), model.getUid(), beauty_Db);
                // viewHolder.setUsers(model.getUsers(), getRef(position));
                viewHolder.likebtn(post_id, model.getUid());
                viewHolder.setUsersImg(getContext(), model.getUid());
               /* if (network(getApplicationContext()) == false) {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }


                if (model.getUsers().contains(username)) {
                } else {
                    viewHolder.views(getRef(position), model.getViews());
                }*/
                viewHolder.post_User_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), F_NEWS_FEED.class);
                        intent.putExtra("acc_id", model.getUid());
                        startActivity(intent);
                    }
                });
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), SingleActivity.class);
                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("desc", model.getDesc());
                        intent.putExtra("postUser", model.getUid());
                        intent.putExtra("image", model.getImage());
                        intent.putExtra("post_id", post_id);
                        startActivity(intent);
                    }
                });

                views.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        viewHolder.setviews(dataSnapshot.child(post_id).getChildrenCount());
                        if (dataSnapshot.child(post_id).hasChild(firebaseAuth.getCurrentUser().getUid())) {
                        } else {
                            views.child(post_id).child(firebaseAuth.getCurrentUser().getUid()).setValue(String.valueOf(fireClass.currentDate()));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        };


        final FirebaseRecyclerAdapter<News, Profile.NewsFeeds> SPORTfeedsFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, Profile.NewsFeeds>(
                News.class
                , R.layout.templatesug_and_public_post
                , Profile.NewsFeeds.class
                , sport_db.orderByChild("uid").equalTo(acc_id.getText().toString())

        ) {
            @Override
            protected void populateViewHolder(final Profile.NewsFeeds viewHolder, final News model, int position) {
                final String post_id = getRef(position).getKey();

                viewHolder.setTitle(model.getTitle(), model.getTime());
                viewHolder.setdesc(model.getDesc());
                //  viewHolder.setviews(String.valueOf(model.getViews()), getRef(position));

                viewHolder.setimage(getContext(), model.getImage());
                viewHolder.sharebtn(getContext(), model.getTitle(), model.getDesc(),
                        access, post_id, model.getImage(), model.getImage_name(), model.getUid(), sport_db);
                // viewHolder.setUsers(model.getUsers(), getRef(position));
                viewHolder.likebtn(post_id, model.getUid());
                viewHolder.setUsersImg(getContext(), model.getUid());
               /* if (network(getApplicationContext()) == false) {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }

                if (model.getUsers().contains(username)) {
                } else {
                    viewHolder.views(getRef(position), model.getViews());
                }*/
                viewHolder.post_User_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), Profile.class);
                        intent.putExtra("acc_id", model.getUid());
                        startActivity(intent);
                    }
                });
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), SingleActivity.class);
                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("desc", model.getDesc());
                        intent.putExtra("postUser", model.getUid());
                        intent.putExtra("image", model.getImage());
                        intent.putExtra("post_id", post_id);
                        startActivity(intent);
                    }
                });
                final String muid = firebaseAuth.getCurrentUser().getUid();
                views.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        viewHolder.setviews(dataSnapshot.child(post_id).getChildrenCount());
                        if (dataSnapshot.child(post_id).hasChild(muid)) {
                        } else {
                            views.child(post_id).child(firebaseAuth.getCurrentUser().getUid()).setValue(String.valueOf(fireClass.currentDate()));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        };

        final FirebaseRecyclerAdapter<News, Profile.NewsFeeds> RomanfeedsFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, Profile.NewsFeeds>(
                News.class
                , R.layout.templatesug_and_public_post
                , Profile.NewsFeeds.class
                , roman_Db.orderByChild("uid").equalTo(acc_id.getText().toString())

        ) {
            @Override
            protected void populateViewHolder(final Profile.NewsFeeds viewHolder, final News model, int position) {
                final String post_id = getRef(position).getKey().toString();

                viewHolder.setTitle(model.getTitle(), model.getTime());
                viewHolder.setdesc(model.getDesc());
                //  viewHolder.setviews(String.valueOf(model.getViews()), getRef(position));

                viewHolder.setimage(getContext(), model.getImage());
                viewHolder.sharebtn(getContext(), model.getTitle(), model.getDesc(),
                        access, post_id, model.getImage(), model.getImage_name(), model.getUid(), roman_Db);
                // viewHolder.setUsers(model.getUsers(), getRef(position));
                viewHolder.likebtn(post_id, model.getUid());
                viewHolder.setUsersImg(getContext(), model.getUid());
               /* if (network(getApplicationContext()) == false) {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }

                if (model.getUsers().contains(username)) {
                } else {
                    viewHolder.views(getRef(position), model.getViews());
                }*/
                viewHolder.post_User_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), Profile.class);
                        intent.putExtra("acc_id", model.getUid());
                        startActivity(intent);
                    }
                });
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), SingleActivity.class);
                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("desc", model.getDesc());
                        intent.putExtra("postUser", model.getUid());
                        intent.putExtra("image", model.getImage());
                        intent.putExtra("post_id", post_id);
                        startActivity(intent);
                    }
                });

                views.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        viewHolder.setviews(dataSnapshot.child(post_id).getChildrenCount());
                        if (dataSnapshot.child(post_id).hasChild(firebaseAuth.getCurrentUser().getUid())) {
                        } else {
                            views.child(post_id).child(firebaseAuth.getCurrentUser().getUid()).setValue(String.valueOf(fireClass.currentDate()));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        };


        final FirebaseRecyclerAdapter<News, Profile.NewsFeeds> GOSSIPfeedsFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, Profile.NewsFeeds>(
                News.class
                , R.layout.templatesug_and_public_post
                , Profile.NewsFeeds.class
                , gossip_DB.orderByChild("uid").equalTo(acc_id.getText().toString())

        ) {
            @Override
            protected void populateViewHolder(final Profile.NewsFeeds viewHolder, final News model, int position) {
                final String post_id = getRef(position).getKey().toString();

                viewHolder.setTitle(model.getTitle(), model.getTime());
                viewHolder.setdesc(model.getDesc());
                //  viewHolder.setviews(String.valueOf(model.getViews()), getRef(position));
                viewHolder.setimage(getContext(), model.getImage());
                viewHolder.sharebtn(getContext(), model.getTitle(), model.getDesc(),
                        access, post_id, model.getImage(), model.getImage_name(), model.getUid(), gossip_DB);
                // viewHolder.setUsers(model.getUsers(), getRef(position));
                viewHolder.likebtn(post_id, model.getUid());
                viewHolder.setUsersImg(getContext(), model.getUid());
               /* if (network(getApplicationContext()) == false) {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }

                if (model.getUsers().contains(username)) {
                } else {
                    viewHolder.views(getRef(position), model.getViews());
                }*/
                viewHolder.post_User_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), Profile.class);
                        intent.putExtra("acc_id", model.getUid());
                        startActivity(intent);
                    }
                });
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), SingleActivity.class);
                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("desc", model.getDesc());
                        intent.putExtra("postUser", model.getUid());
                        intent.putExtra("image", model.getImage());
                        intent.putExtra("post_id", post_id);
                        startActivity(intent);
                    }
                });

                views.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        viewHolder.setviews(dataSnapshot.child(post_id).getChildrenCount());
                        if (dataSnapshot.child(post_id).hasChild(firebaseAuth.getCurrentUser().getUid())) {
                        } else {
                            views.child(post_id).child(firebaseAuth.getCurrentUser().getUid()).setValue(String.valueOf(fireClass.currentDate()));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        };

        final FirebaseRecyclerAdapter<News, Profile.NewsFeeds> FASHIONfeedsFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, Profile.NewsFeeds>(
                News.class
                , R.layout.templatesug_and_public_post
                , Profile.NewsFeeds.class
                , fashion_db.orderByChild("uid").equalTo(acc_id.getText().toString())
        ) {
            @Override
            protected void populateViewHolder(final Profile.NewsFeeds viewHolder, final News model, int position) {
                final String post_id = getRef(position).getKey().toString();

                viewHolder.setTitle(model.getTitle(), model.getTime());
                viewHolder.setdesc(model.getDesc());
                //  viewHolder.setviews(String.valueOf(model.getViews()), getRef(position));
                // viewHolder.deletePost(access, getContext().getApplicationContext(), post_id, model.getImage(), model.getImage_name(), model.getUid(), fashion_db);
                viewHolder.setimage(getContext(), model.getImage());
                viewHolder.sharebtn(getContext(), model.getTitle(), model.getDesc(),
                        access, post_id, model.getImage(), model.getImage_name(), model.getUid(), fashion_db);
                // viewHolder.setUsers(model.getUsers(), getRef(position));
                viewHolder.likebtn(post_id, model.getUid());
                viewHolder.setUsersImg(getContext(), model.getUid());
               /* if (network(getApplicationContext()) == false) {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }

                if (model.getUsers().contains(username)) {
                } else {
                    viewHolder.views(getRef(position), model.getViews());
                }*/
                views.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        viewHolder.setviews(dataSnapshot.child(post_id).getChildrenCount());
                        if (dataSnapshot.child(post_id).hasChild(firebaseAuth.getCurrentUser().getUid())) {
                        } else {
                            views.child(post_id).child(firebaseAuth.getCurrentUser().getUid()).setValue(String.valueOf(fireClass.currentDate()));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                viewHolder.post_User_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), Profile.class);
                        intent.putExtra("acc_id", model.getUid());
                        startActivity(intent);
                    }
                });
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), SingleActivity.class);
                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("desc", model.getDesc());
                        intent.putExtra("postUser", model.getUid());
                        intent.putExtra("image", model.getImage());
                        intent.putExtra("post_id", post_id);
                        startActivity(intent);
                    }
                });


            }
        };


        final FirebaseRecyclerAdapter<News, Profile.NewsFeeds> MusicfeedsFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, Profile.NewsFeeds>(
                News.class
                , R.layout.templatesug_and_public_post
                , Profile.NewsFeeds.class
                , music_DB.orderByChild("uid").equalTo(acc_id.getText().toString())
        ) {
            @Override
            protected void populateViewHolder(final Profile.NewsFeeds viewHolder, final News model, int position) {
                final String post_id = getRef(position).getKey().toString();

                viewHolder.setTitle(model.getTitle(), model.getTime());
                viewHolder.setdesc(model.getDesc());
                //  viewHolder.setviews(String.valueOf(model.getViews()), getRef(position));
                // viewHolder.deletePost(access, getContext().getApplicationContext(), post_id, model.getImage(), model.getImage_name(), model.getUid(), music_DB);

                viewHolder.setimage(getContext(), model.getImage());
                viewHolder.sharebtn(getContext(), model.getTitle(), model.getDesc(),
                        access, post_id, model.getImage(), model.getImage_name(), model.getUid(), music_DB);
                // viewHolder.setUsers(model.getUsers(), getRef(position));
                viewHolder.likebtn(post_id, model.getUid());
                viewHolder.setUsersImg(getContext(), model.getUid());
               /* if (network(getApplicationContext()) == false) {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }

                if (model.getUsers().contains(username)) {
                } else {
                    viewHolder.views(getRef(position), model.getViews());
                }*/

                views.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        viewHolder.setviews(dataSnapshot.child(post_id).getChildrenCount());
                        if (dataSnapshot.child(post_id).hasChild(firebaseAuth.getCurrentUser().getUid())) {
                        } else {
                            views.child(post_id).child(firebaseAuth.getCurrentUser().getUid()).child("uid").setValue(model.getUid());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                viewHolder.post_User_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), Profile.class);
                        intent.putExtra("acc_id", model.getUid());
                        startActivity(intent);
                    }
                });
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), SingleActivity.class);
                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("desc", model.getDesc());
                        intent.putExtra("postUser", model.getUid());
                        intent.putExtra("image", model.getImage());
                        intent.putExtra("post_id", post_id);
                        startActivity(intent);
                    }
                });


            }
        };

        final FirebaseRecyclerAdapter<News, Profile.NewsFeeds> ComedyFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, Profile.NewsFeeds>(
                News.class
                , R.layout.templatesug_and_public_post
                , Profile.NewsFeeds.class
                , comedy_db.orderByChild("uid").equalTo(acc_id.getText().toString())
        ) {
            @Override
            protected void populateViewHolder(final Profile.NewsFeeds viewHolder, final News model, int position) {
                final String post_id = getRef(position).getKey().toString();

                viewHolder.setTitle(model.getTitle(), model.getTime());
                viewHolder.setdesc(model.getDesc());
                //  viewHolder.setviews(String.valueOf(model.getViews()), getRef(position));

                viewHolder.setimage(getContext(), model.getImage());
                viewHolder.sharebtn(getContext(), model.getTitle(), model.getDesc(),
                        access, post_id, model.getImage(), model.getImage_name(), model.getUid(), comedy_db);
                // viewHolder.setUsers(model.getUsers(), getRef(position));
                viewHolder.likebtn(post_id, model.getUid());
                viewHolder.setUsersImg(getContext(), model.getUid());
                // viewHolder.deletePost(access, getContext().getApplicationContext(), post_id, model.getImage(), model.getImage_name(), model.getUid(), comedy_db);

               /* if (network(getApplicationContext()) == false) {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }


                if (model.getUsers().contains(username)) {
                } else {
                    viewHolder.views(getRef(position), model.getViews());
                }*/
                viewHolder.post_User_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), Profile.class);
                        intent.putExtra("acc_id", model.getUid());
                        startActivity(intent);
                    }
                });
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), SingleActivity.class);
                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("desc", model.getDesc());
                        intent.putExtra("postUser", model.getUid());
                        intent.putExtra("image", model.getImage());
                        intent.putExtra("post_id", post_id);
                        startActivity(intent);
                    }
                });

                views.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        viewHolder.setviews(dataSnapshot.child(post_id).getChildrenCount());
                        if (dataSnapshot.child(post_id).hasChild(firebaseAuth.getCurrentUser().getUid())) {
                        } else {
                            views.child(post_id).child(firebaseAuth.getCurrentUser().getUid()).setValue(String.valueOf(fireClass.currentDate()));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        };


        final FirebaseRecyclerAdapter<News, Profile.NewsFeeds> EventfeedsFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, Profile.NewsFeeds>(
                News.class
                , R.layout.templatesug_and_public_post
                , Profile.NewsFeeds.class
                , event_Db.orderByChild("uid").equalTo(acc_id.getText().toString())
        ) {
            @Override
            protected void populateViewHolder(final Profile.NewsFeeds viewHolder, final News model, int position) {
                final String post_id = getRef(position).getKey().toString();

                viewHolder.setTitle(model.getTitle(), model.getTime());
                viewHolder.setdesc(model.getDesc());
                //  viewHolder.setviews(String.valueOf(model.getViews()), getRef(position));

                viewHolder.setimage(getContext(), model.getImage());
                viewHolder.sharebtn(getContext(), model.getTitle(), model.getDesc(),
                        access, post_id, model.getImage(), model.getImage_name(), model.getUid(), event_Db);
                // viewHolder.setUsers(model.getUsers(), getRef(position));
                viewHolder.likebtn(post_id, model.getUid());
                viewHolder.setUsersImg(getContext(), model.getUid());
                // viewHolder.deletePost(access, getContext().getApplicationContext(), post_id, model.getImage(), model.getImage_name(), model.getUid(), event_Db);

               /* if (network(getApplicationContext()) == false) {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }

                if (model.getUsers().contains(username)) {
                } else {
                    viewHolder.views(getRef(position), model.getViews());
                }*/
                viewHolder.post_User_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), Profile.class);
                        intent.putExtra("acc_id", model.getUid());
                        startActivity(intent);
                    }
                });
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), SingleActivity.class);
                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("desc", model.getDesc());
                        intent.putExtra("postUser", model.getUid());
                        intent.putExtra("image", model.getImage());
                        intent.putExtra("post_id", post_id);
                        startActivity(intent);
                    }
                });

                views.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        viewHolder.setviews(dataSnapshot.child(post_id).getChildrenCount());
                        if (dataSnapshot.child(post_id).hasChild(firebaseAuth.getCurrentUser().getUid())) {
                        } else {
                            views.child(post_id).child(firebaseAuth.getCurrentUser().getUid()).setValue(String.valueOf(fireClass.currentDate()));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        };

        final FirebaseRecyclerAdapter<News, Profile.NewsFeeds> MoviefeedsFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, Profile.NewsFeeds>(
                News.class
                , R.layout.templatesug_and_public_post
                , Profile.NewsFeeds.class
                , movies_DB.orderByChild("uid").equalTo(acc_id.getText().toString())
        ) {
            @Override
            protected void populateViewHolder(final Profile.NewsFeeds viewHolder, final News model, int position) {
                final String post_id = getRef(position).getKey().toString();

                viewHolder.setTitle(model.getTitle(), model.getTime());
                viewHolder.setdesc(model.getDesc());
                //  viewHolder.setviews(String.valueOf(model.getViews()), getRef(position));

                viewHolder.setimage(getContext(), model.getImage());
                viewHolder.sharebtn(getContext(), model.getTitle(), model.getDesc(),
                        access, post_id, model.getImage(), model.getImage_name(), model.getUid(), movies_DB);
                // viewHolder.setUsers(model.getUsers(), getRef(position));
                viewHolder.likebtn(post_id, model.getUid());
                viewHolder.setUsersImg(getContext(), model.getUid());
                // viewHolder.deletePost(access, getContext().getApplicationContext(), post_id, model.getImage(), model.getImage_name(), model.getUid(), movies_DB);

               /* if (network(getApplicationContext()) == false) {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }

                if (model.getUsers().contains(username)) {
                } else {
                    viewHolder.views(getRef(position), model.getViews());
                }*/
                viewHolder.post_User_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), Profile.class);
                        intent.putExtra("acc_id", model.getUid());
                        startActivity(intent);
                    }
                });
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), SingleActivity.class);
                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("desc", model.getDesc());
                        intent.putExtra("postUser", model.getUid());
                        intent.putExtra("image", model.getImage());
                        intent.putExtra("post_id", post_id);
                        startActivity(intent);
                    }
                });
                views.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        viewHolder.setviews(dataSnapshot.child(post_id).getChildrenCount());
                        if (dataSnapshot.child(post_id).hasChild(firebaseAuth.getCurrentUser().getUid())) {
                        } else {
                            views.child(post_id).child(firebaseAuth.getCurrentUser().getUid()).setValue(String.valueOf(fireClass.currentDate()));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        };

        final FirebaseRecyclerAdapter<News, Profile.NewsFeeds> InsipirationfeedsFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, Profile.NewsFeeds>(
                News.class
                , R.layout.templatesug_and_public_post
                , Profile.NewsFeeds.class
                , insipration_Db.orderByChild("uid").equalTo(acc_id.getText().toString())
        ) {
            @Override
            protected void populateViewHolder(final Profile.NewsFeeds viewHolder, final News model, int position) {
                final String post_id = getRef(position).getKey().toString();

                viewHolder.setTitle(model.getTitle(), model.getTime());
                viewHolder.setdesc(model.getDesc());
                //  viewHolder.setviews(String.valueOf(model.getViews()), getRef(position));
                // viewHolder.deletePost(access, getContext().getApplicationContext(), post_id, model.getImage(), model.getImage_name(), model.getUid(), insipration_Db);

                viewHolder.setimage(getContext(), model.getImage());
                viewHolder.sharebtn(getContext(), model.getTitle(), model.getDesc(),
                        access, post_id, model.getImage(), model.getImage_name(), model.getUid(), insipration_Db);
                // viewHolder.setUsers(model.getUsers(), getRef(position));
                viewHolder.likebtn(post_id, model.getUid());
                viewHolder.setUsersImg(getContext(), model.getUid());
               /* if (network(getApplicationContext()) == false) {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }

                if (model.getUsers().contains(username)) {
                } else {
                    viewHolder.views(getRef(position), model.getViews());
                }*/
                viewHolder.post_User_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), Profile.class);
                        intent.putExtra("acc_id", model.getUid());
                        startActivity(intent);
                    }
                });
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), SingleActivity.class);
                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("desc", model.getDesc());
                        intent.putExtra("postUser", model.getUid());
                        intent.putExtra("image", model.getImage());
                        intent.putExtra("post_id", post_id);
                        startActivity(intent);
                    }
                });

                views.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        viewHolder.setviews(dataSnapshot.child(post_id).getChildrenCount());
                        if (dataSnapshot.child(post_id).hasChild(firebaseAuth.getCurrentUser().getUid())) {
                        } else {
                            views.child(post_id).child(firebaseAuth.getCurrentUser().getUid()).setValue(String.valueOf(fireClass.currentDate()));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        };

        final FirebaseRecyclerAdapter<News, Profile.NewsFeeds> GospelfeedsFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, Profile.NewsFeeds>(
                News.class
                , R.layout.templatesug_and_public_post
                , Profile.NewsFeeds.class
                , gospel_DB.orderByChild("uid").equalTo(acc_id.getText().toString())
        ) {
            @Override
            protected void populateViewHolder(final Profile.NewsFeeds viewHolder, final News model, int position) {
                final String post_id = getRef(position).getKey().toString();

                viewHolder.setTitle(model.getTitle(), model.getTime());
                viewHolder.setdesc(model.getDesc());
                //  viewHolder.setviews(String.valueOf(model.getViews()), getRef(position));
                //  viewHolder.deletePost(access, post_id, model.getImage(), model.getImage_name(), model.getUid(), gospel_DB);

                viewHolder.setimage(getContext(), model.getImage());
                viewHolder.sharebtn(getContext(), model.getTitle(), model.getDesc(),
                        access, post_id, model.getImage(), model.getImage_name(), model.getUid(), gospel_DB);
                // viewHolder.setUsers(model.getUsers(), getRef(position));
                viewHolder.likebtn(post_id, model.getUid());
                viewHolder.setUsersImg(getContext(), model.getUid());
               /* if (network(getApplicationContext()) == false) {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }

                if (model.getUsers().contains(username)) {
                } else {
                    viewHolder.views(getRef(position), model.getViews());
                }*/
                views.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        viewHolder.setviews(dataSnapshot.child(post_id).getChildrenCount());
                        if (dataSnapshot.child(post_id).hasChild(firebaseAuth.getCurrentUser().getUid())) {
                        } else {
                            views.child(post_id).child(firebaseAuth.getCurrentUser().getUid()).setValue(String.valueOf(fireClass.currentDate()));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                viewHolder.post_User_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), Profile.class);
                        intent.putExtra("acc_id", model.getUid());
                        startActivity(intent);
                    }
                });
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), SingleActivity.class);
                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("desc", model.getDesc());
                        intent.putExtra("postUser", model.getUid());
                        intent.putExtra("image", model.getImage());
                        intent.putExtra("post_id", post_id);
                        startActivity(intent);
                    }
                });


            }
        };


        lshowBeautyPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRecyleradpter(BeautyfeedsFirebaseRecyclerAdapter, "Beauty");
                btnFilter(showBeautyPost);

            }
        });
        lshowComedyPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRecyleradpter(ComedyFirebaseRecyclerAdapter, "Comedy");
                btnFilter(showComedyPost);
            }
        });
        lshowEventsPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRecyleradpter(EventfeedsFirebaseRecyclerAdapter, "Events");
                btnFilter(showEventsPost);
            }
        });

        lshowFashionPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRecyleradpter(FASHIONfeedsFirebaseRecyclerAdapter, "Fashion");
                btnFilter(showFashionPost);
            }
        });
        lshowGospelPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRecyleradpter(GospelfeedsFirebaseRecyclerAdapter, "Gospel");
                btnFilter(showGospelPost);
            }
        });
        lshowGossipPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRecyleradpter(GOSSIPfeedsFirebaseRecyclerAdapter, "Gossip");
                btnFilter(showGossipPost);
            }
        });

        lshowInsipirationPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRecyleradpter(InsipirationfeedsFirebaseRecyclerAdapter, "Insipiration");
                btnFilter(showInsipirationPost);
            }
        });

        lshowMoviePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRecyleradpter(MoviefeedsFirebaseRecyclerAdapter, "Movies");
                btnFilter(showMoviePost);
            }
        });

        lshowMusicPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRecyleradpter(MusicfeedsFirebaseRecyclerAdapter, "Music");
                btnFilter(showMusicPost);
            }
        });
     /*   showSugPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "nice", Toast.LENGTH_SHORT).show();
                setRecyleradpter(SUGfeedsFirebaseRecyclerAdapter, "SUG");
                btnFilter(showSugPost);
            }
        });*/
        lshowSportPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRecyleradpter(SPORTfeedsFirebaseRecyclerAdapter, "Sport");
                btnFilter(showSportPost);
            }
        });
        progressBar.setVisibility(View.INVISIBLE);
    }


    private void btnFilter(ImageButton btn) {
        grayButton();
        if (btn.getId() == showBeautyPost.getId()) {
            showBeautyPost.setImageResource(R.drawable.main_beauty);
        } else if (btn.getId() == showComedyPost.getId()) {
            showComedyPost.setImageResource(R.drawable.main_comedy);

        } else if (btn.getId() == showEventsPost.getId()) {
            showEventsPost.setImageResource(R.drawable.main_event);

        } else if (btn.getId() == showMusicPost.getId()) {
            showMusicPost.setImageResource(R.drawable.main_music);
        } else if (btn.getId() == showSportPost.getId()) {
            showSportPost.setImageResource(R.drawable.main_sport);
        } else if (btn.getId() == showGossipPost.getId()) {
            showGossipPost.setImageResource(R.drawable.main_gossip);
        } else if (btn.getId() == showFashionPost.getId()) {
            showFashionPost.setImageResource(R.drawable.main_fashion);

        } else if (btn.getId() == showMoviePost.getId()) {
            showMoviePost.setImageResource(R.drawable.main_movies);

        } else if (btn.getId() == showInsipirationPost.getId()) {
            showInsipirationPost.setImageResource(R.drawable.main_insipiration);

        } else if (btn.getId() == showGospelPost.getId()) {
            showGospelPost.setImageResource(R.drawable.main_gospel);
        }


    }

    private void grayButton() {
        showBeautyPost.setImageResource(R.drawable.main_beauty_gray);
        showComedyPost.setImageResource(R.drawable.main_comedy_gray);
        showEventsPost.setImageResource(R.drawable.main_event_gray);
        showMusicPost.setImageResource(R.drawable.main_music_gray);
        //  showSugPost.setImageResource(R.drawable.main_sug_gray);
        showSportPost.setImageResource(R.drawable.main_sport_gray);
        showGossipPost.setImageResource(R.drawable.main_gossip_gray);
        showFashionPost.setImageResource(R.drawable.main_fashion_gray);
        showMoviePost.setImageResource(R.drawable.main_movies_gray);
        showInsipirationPost.setImageResource(R.drawable.main_insipiration_gray);
        showGospelPost.setImageResource(R.drawable.main_gospel_gray);
    }

    private void setRecyleradpter(FirebaseRecyclerAdapter firebaseRAD, String headerText) {
        selected_post_header.setText(headerText);
        if (firebaseRAD.getItemCount() >= 1) {
            noPost.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(firebaseRAD);
        } else {
            noPost.setText("NO POST");
            recyclerView.setVisibility(View.INVISIBLE);
            noPost.setVisibility(View.VISIBLE);
        }

    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (acc_id_colleter != "") {
            acc_id.setText(acc_id_colleter);
        } else if (acc_id.getText().toString() == "" && acc_id_colleter != "") {
            acc_id.setText(acc_id_colleter);
        } else if (acc_id.getText().toString() == "" && acc_id_colleter == "") {
            acc_id.setText(firebaseAuth.getCurrentUser().getUid().toString());
        }
    }

    public static class NewsFeeds extends RecyclerView.ViewHolder {
        View mView;
        ImageButton btn_like;
        FirebaseAuth firebaseAuth;
        TextView numbers_of_like, post_User_name;
        ImageView post_user_image;
        FireClass fireClass;
        ImageView post_image;
        DatabaseReference databaseUser, likeDatabase, notification;
        RelativeLayout progressBar;
        Boolean likeClike = false;
        String UNI;
        ImageButton deletePost;
        Context ctx;

        public NewsFeeds(View itemView) {
            super(itemView);
            mView = itemView;
            ctx = itemView.getContext();
            fireClass = new FireClass();
            btn_like = (ImageButton) mView.findViewById(R.id.xml_btn_like);
            numbers_of_like = (TextView) mView.findViewById(R.id.xml_like_num);
            post_image = (ImageView) mView.findViewById(R.id.post_image);
            post_user_image = (ImageView) mView.findViewById(R.id.news_Image_postUser);
            post_User_name = (TextView) mView.findViewById(R.id.news_postUser);
            firebaseAuth = FirebaseAuth.getInstance();
            UNI = fireClass.university();
            deletePost = (ImageButton) mView.findViewById(R.id.news_feed_delete);

            databaseUser = FirebaseDatabase.getInstance().getReference().child("users");
            databaseUser.keepSynced(true);
            likeDatabase = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("likes");

            likeDatabase.keepSynced(true);
            notification = FirebaseDatabase.getInstance().getReference().child("notification");
            notification.keepSynced(true);

            progressBar = (RelativeLayout) mView.findViewById(R.id.public_progress_bar);

        }

        public void likebtn(final String post_id, final String uid) {


            likeDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (likeClike) {
                        if (dataSnapshot.child(post_id).hasChild(firebaseAuth.getCurrentUser().getUid())) {
                            btn_like.setImageResource(R.drawable.redlike);
                        } else {
                            btn_like.setImageResource(R.drawable.like);
                        }


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            btn_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    likeClike = true;
                    likeDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            numbers_of_like.setText(String.valueOf(dataSnapshot.child(post_id).getChildrenCount()));
                            if (likeClike) {
                                if (dataSnapshot.child(post_id).hasChild(firebaseAuth.getCurrentUser().getUid())) {
                                    likeDatabase.child(post_id).
                                            child(firebaseAuth.getCurrentUser().getUid())
                                            .child("post_userid").removeValue();
                                    DatabaseReference push = notification.child(uid).push();
                                    push.child("type").setValue("like");
                                    push.child("from").setValue(firebaseAuth.getCurrentUser().getUid());
                                    btn_like.setImageResource(R.drawable.like);
                                    likeClike = false;
                                } else {
                                    likeDatabase.child(post_id).
                                            child(firebaseAuth.getCurrentUser().getUid()).
                                            child("post_userid").setValue(uid);
                                    btn_like.setImageResource(R.drawable.redlike);
                                    fireClass.likeClick(ctx);
                                    likeClike = false;
                                }


                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            });


        }


        public void sharebtn(final Context ctx, final String title, final String desc,
                             final String accessLevel, final String postId, String imageUrl,
                             final String imageName, final String postUser, final DatabaseReference db) {

            try {
                ImageButton btn_share = (ImageButton) mView.findViewById(R.id.xml_btn_sharePost);

                btn_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        String msg = title + "\n \n " + desc;
                        post_image.setDrawingCacheEnabled(true);
                        Drawable drawable = post_image.getDrawable();
                        if (drawable != null && !drawable.equals(null)) {
                            Bitmap bitmap1 = ((BitmapDrawable) drawable).getBitmap();
                            String path = MediaStore.Images.Media.insertImage(ctx.getContentResolver(), bitmap1, "img", null);
                            try {
                                final Uri uri = Uri.parse(path);
                                intent.putExtra(Intent.EXTRA_STREAM, uri);
                            } catch (Exception ex) {

                            }

                        }
                        if (msg != "" && msg != null && msg.length() > 1) {

                            intent.putExtra(Intent.EXTRA_TEXT, msg);
                        }
                        intent.setType("image/*");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ctx.startActivity(intent);
                    }
                });
            } catch (Exception ex) {
                Toast.makeText(ctx, "Unable to share post", Toast.LENGTH_SHORT).show();
            }

            if (accessLevel != null) {
                try {


                    // (.)-(.)

                    if (postUser.matches(firebaseAuth.getCurrentUser().getUid().toString()) || accessLevel.matches("3") || accessLevel.equals("3")) {
                        deletePost.setVisibility(View.VISIBLE);

                        deletePost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                progressBar.setVisibility(View.VISIBLE);
                                if (imageName != null && imageName != "" && imageName.length() > 1) {
                                    final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Photos/" + imageName);

                                    storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                db.child(postId).removeValue();
                                                progressBar.setVisibility(View.GONE);
                                            } else {
                                                Toast.makeText(ctx, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                } else {

                                    db.child(postId).removeValue();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });


                    } else {
                        deletePost.setVisibility(View.GONE);
                    }

                } catch (NullPointerException ex) {

                }
            } else {
                deletePost.setVisibility(View.VISIBLE);
            }


        }

        public void views(DatabaseReference database, int value) {
            int val = value + 1;
            database.child("views").setValue(val);
        }

        public void setTitle(String title, String postTime) {
            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            if (title.length() > 1 || title != "") {
                post_title.setText(title);

            } else {
                post_title.setVisibility(View.GONE);
            }

        }

        public void setdesc(String desc) {
            TextView post_desc = (TextView) mView.findViewById(R.id.post_description);
            if (desc.length() > 1 && !desc.matches("")) {
                post_desc.setText(desc);
                post_desc.setVisibility(View.VISIBLE);
            } else {
                post_desc.setVisibility(View.GONE);
            }
        }


        public void setimage(final Context ctx, final String image) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                if (image != null && image != "" && image.length() > 1) {
                    //Picasso.with(ctx).load(image).into(post_image);
                    Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).fit().centerCrop().noFade().into(post_image, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            Picasso.with(ctx).load(image).fit().centerCrop().centerCrop().noFade().into(post_image, new Callback() {
                                @Override
                                public void onSuccess() {
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {

                                }
                            });
                        }
                    });
                    //mAdView = (AdView)mView.findViewById(R.id.adView);
                    // AdRequest adRequest = new AdRequest.Builder().build();
                    // mAdView.loadAd(adRequest);
                } else {
                    post_image.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        }

        public void setviews(Long views) {
            TextView viewss = (TextView) mView.findViewById(R.id.xml_view_num);
            viewss.setText(String.valueOf(views));
        }

        public void setUsersImg(final Context ctx, final String uid) {
            databaseUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        String img = dataSnapshot.child(uid).child("image").getValue().toString();
                        post_User_name.setText(dataSnapshot.child(uid).child("fname").getValue().toString());
                        if (img != "default" && img != null) {
                            Picasso.with(ctx).load(img).into(post_user_image);
                        } else {
                            post_user_image.setImageResource(R.drawable.user2);
                        }
                    } catch (NullPointerException ex) {

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


    public static class followersViewHolder extends RecyclerView.ViewHolder {
        View view;
        Button followMe;
        FirebaseAuth firebaseAuth;
        Boolean follow = false;
        FireClass fireClass;
        DatabaseReference follodatabse, followingdatabase;
        CircleImageView imageView;
        TextView txtname;
        String UNI;

        public followersViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            firebaseAuth = FirebaseAuth.getInstance();
            followMe = (Button) itemView.findViewById(R.id.temp_followers_follow_btn);
              fireClass = new FireClass();
            UNI = fireClass.university();
            follodatabse = FirebaseDatabase.getInstance().getReference().child(UNI).child("followers");
            follodatabse.keepSynced(true);
            imageView = (CircleImageView) view.findViewById(R.id.temp_followers_image);
            txtname = (TextView) view.findViewById(R.id.temp_followers_name);


        }

        public void setFollowMe(final String uid, String currentUser) {
            if (uid != null && uid != "" && currentUser != null & currentUser != "") {
                if (firebaseAuth.getCurrentUser().getUid().matches(uid)) {

                    follodatabse = FirebaseDatabase.getInstance().getReference().child(UNI).child("followers").child(uid);
                    followingdatabase = FirebaseDatabase.getInstance().getReference().child(UNI).child("following").child(firebaseAuth.getCurrentUser().getUid());

                } else {
                    follodatabse = FirebaseDatabase.getInstance().getReference().child(UNI).child("followers").child(uid);
                    followingdatabase = FirebaseDatabase.getInstance().getReference().child(UNI).child("following").child(uid);

                }
                follodatabse = follodatabse.child(uid);
                if (uid.matches(currentUser)) {
                    followMe.setVisibility(View.GONE);
                    followMe.setEnabled(false);
                } else {
                    followMe.setVisibility(View.VISIBLE);
                    followMe.setEnabled(true);
                }
                follodatabse.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(firebaseAuth.getCurrentUser().getUid())) {
                            followMe.setText("Unfollow");
                        } else {
                            followMe.setText("Follow");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                followMe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        follow = true;
                        follodatabse.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (follow) {
                                    final DatabaseReference followingDB = FirebaseDatabase.getInstance().getReference().child(UNI).child("following").child(firebaseAuth.getCurrentUser().getUid());
                                    if (dataSnapshot.hasChild(firebaseAuth.getCurrentUser().getUid())) {
                                        follodatabse.child(firebaseAuth.getCurrentUser().getUid()).removeValue();
                                        followingDB.child(uid).removeValue();
                                        follow = false;
                                    } else {
                                        follodatabse.child(firebaseAuth.getCurrentUser().getUid()).child("uid").setValue(firebaseAuth.getCurrentUser().getUid());
                                        followingDB.child(uid).child("uid").setValue(uid);
                                        follow = false;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });
            }
        }

        public void setImage(final Context ctx, final String image, final String uid, final TextView acc_id) {

            if (image.length() > 1) {
                Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(ctx).load(image).into(imageView);
                    }
                });
            } else {

            }
        }

        public void setName(String name, final TextView acc_id, final String uid) {

            if (name.length() > 1) {
                txtname.setText(name);
            } else {
                txtname.setText("...");
            }
        }
    }

    public static class followingViewHolder extends RecyclerView.ViewHolder {
        View view;
        Button followMe;
        FirebaseAuth firebaseAuth;
        Boolean follow = false;
        FireClass fireClass;
        DatabaseReference follodatabse;
        ImageView imageView;
        TextView txtname;
        RelativeLayout progressBar;
        String UNI;

        public followingViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            firebaseAuth = FirebaseAuth.getInstance();
            followMe = (Button) itemView.findViewById(R.id.temp_followers_follow_btn);
            fireClass = new FireClass();
            UNI = fireClass.university();
            follodatabse = FirebaseDatabase.getInstance().getReference().child(UNI).child("followers");
            follodatabse.keepSynced(true);
            imageView = (ImageView) view.findViewById(R.id.temp_followers_image);
            txtname = (TextView) view.findViewById(R.id.temp_followers_name);
            progressBar = (RelativeLayout) view.findViewById(R.id.public_progress_bar);
        }

        public void setFollowMe(final String uid, String currentUser) {
            follodatabse = follodatabse.child(uid);
            if (uid.matches(currentUser)) {
                followMe.setVisibility(View.GONE);
                followMe.setEnabled(false);
            } else {
                followMe.setVisibility(View.VISIBLE);
                followMe.setEnabled(true);
            }
            follodatabse.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(firebaseAuth.getCurrentUser().getUid())) {
                        followMe.setText("Unfollow");
                    } else {
                        followMe.setText("Follow");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            followMe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    follow = true;
                    follodatabse.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (follow) {
                                if (dataSnapshot.hasChild(firebaseAuth.getCurrentUser().getUid())) {
                                    follodatabse.child(firebaseAuth.getCurrentUser().getUid()).removeValue();
                                    follow = false;
                                } else {
                                    follodatabse.child(firebaseAuth.getCurrentUser().getUid()).child("uid").setValue(firebaseAuth.getCurrentUser().getUid());
                                    follow = false;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            });
        }

        public void setimage(final Context ctx, final String image) {
            progressBar = (RelativeLayout) view.findViewById(R.id.public_progress_bar);
            try {
                if (image != null && image != "" && image.length() > 1) {
                    //Picasso.with(ctx).load(image).into(post_image);
                    Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).fit().centerCrop().noFade().into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            Picasso.with(ctx).load(image).fit().centerCrop().centerCrop().noFade().into(imageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {

                                }
                            });
                        }
                    });
                    //mAdView = (AdView)mView.findViewById(R.id.adView);
                    // AdRequest adRequest = new AdRequest.Builder().build();
                    // mAdView.loadAd(adRequest);
                } else {
                    imageView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }

            } catch (NullPointerException n) {


            }
        }

        public void setName(String name, final TextView acc_id, final String uid) {

            if (name.length() > 1) {
                txtname.setText(name);
            } else {
                txtname.setText("...");
            }
        }
    }

    public class ViewGetter {
        private String uid;

        public ViewGetter() {

        }

        public ViewGetter(String uid, String text) {
            this.uid = uid;
        }

        public String getUid() {
            return uid;

        }


    }

}
