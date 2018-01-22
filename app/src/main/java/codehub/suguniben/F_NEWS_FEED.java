package codehub.suguniben;


import android.app.AlertDialog;
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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class F_NEWS_FEED extends Fragment {
    private static Context context;
    RecyclerView recyclerView;
    DatabaseReference sport_db, gossip_DB, fashion_db, music_DB, comedy_db, beauty_Db, event_Db, movies_DB, insipration_Db, gospel_DB, roman_Db;
    // sug_update_db,showSugPost,
    FirebaseRecyclerAdapter<News, F_NEWS_FEED.NewsFeeds> BeautyfeedsFirebaseRecyclerAdapter;
    private Boolean likeClike = false;
    private FirebaseAuth firebaseAuth;
    private FireClass fireClass;
    private DatabaseReference userDatabase;
    private DatabaseReference likeDatabase;
    private DatabaseReference notification;
    private DatabaseReference views;
    private String access;
    private ImageButton showBeautyPost, showComedyPost, showEventsPost, showMusicPost, showSportPost, showGossipPost, showFashionPost,
            showMoviePost, showInsipirationPost, showGospelPost, showRoman;
    private LinearLayout lshowBeautyPost, lshowComedyPost, lshowEventsPost, lshowMusicPost, lshowSportPost, lshowGossipPost, lshowFashionPost,
            lshowMoviePost, lshowInsipirationPost, lshowGospelPost, lshowRoman;
    private String UNI;

    public F_NEWS_FEED() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.f__news__feed, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = (RecyclerView) getActivity().findViewById(R.id.news_feed_RecyclerView);
        firebaseAuth = FirebaseAuth.getInstance();
        fireClass = new FireClass();
        UNI = fireClass.university();
        userDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseAuth.getCurrentUser().getUid());
        likeDatabase = FirebaseDatabase.getInstance().getReference().child(UNI).child("likes");
        views = FirebaseDatabase.getInstance().getReference().child(UNI).child("views");
        views.keepSynced(true);


        likeDatabase.keepSynced(true);
        userDatabase.keepSynced(true);

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.news_feed_RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        showBeautyPost = (ImageButton) getActivity().findViewById(R.id.main_show_beauty_post);
        showComedyPost = (ImageButton) getActivity().findViewById(R.id.main_show_comedy_post);
        showEventsPost = (ImageButton) getActivity().findViewById(R.id.main_show_events_post);
        showMusicPost = (ImageButton) getActivity().findViewById(R.id.main_show_music_post);
        // showSugPost = (ImageButton) getActivity().findViewById(R.id.main_show_sug_post);
        showSportPost = (ImageButton) getActivity().findViewById(R.id.main_show_sport_post);
        showGossipPost = (ImageButton) getActivity().findViewById(R.id.main_show_gossip_post);
        showFashionPost = (ImageButton) getActivity().findViewById(R.id.main_show_Fashion_post);
        showMoviePost = (ImageButton) getActivity().findViewById(R.id.main_show_movies_post);
        showInsipirationPost = (ImageButton) getActivity().findViewById(R.id.main_show_inspiration_post);
        showGospelPost = (ImageButton) getActivity().findViewById(R.id.main_show_gospel_post);
        showRoman = (ImageButton) getActivity().findViewById(R.id.main_show_roman_post);
        lshowRoman = (LinearLayout) getActivity().findViewById(R.id.main_show_roman_postL);
        lshowBeautyPost = (LinearLayout) getActivity().findViewById(R.id.main_show_beauty_postL);
        lshowComedyPost = (LinearLayout) getActivity().findViewById(R.id.main_show_comedy_postL);
        lshowEventsPost = (LinearLayout) getActivity().findViewById(R.id.main_show_events_postL);
        lshowMusicPost = (LinearLayout) getActivity().findViewById(R.id.main_show_music_postL);
        // showSugPost = (ImageButton) getActivity().findViewById(R.id.main_show_sug_post);
        lshowSportPost = (LinearLayout) getActivity().findViewById(R.id.main_show_sport_postL);
        lshowGossipPost = (LinearLayout) getActivity().findViewById(R.id.main_show_gossip_postL);
        lshowFashionPost = (LinearLayout) getActivity().findViewById(R.id.main_show_Fashion_postL);
        lshowMoviePost = (LinearLayout) getActivity().findViewById(R.id.main_show_movies_postL);
        lshowInsipirationPost = (LinearLayout) getActivity().findViewById(R.id.main_show_inspiration_postL);
        lshowGospelPost = (LinearLayout) getActivity().findViewById(R.id.main_show_gospel_postL);


        notification = FirebaseDatabase.getInstance().getReference().child("notification");
        notification.keepSynced(true);
        //  sug_update_db = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child(fireClass.university()).child(fireClass.university()).child("Sug");
        sport_db = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("Sports");
        gossip_DB = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("Gossip");
        fashion_db = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("Fashion");
        insipration_Db = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("Inspiration");
        beauty_Db = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("Beauty");
        event_Db = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("Events");
        music_DB = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("Music");
        comedy_db = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("Comedy");
        movies_DB = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("Movies");
        gospel_DB = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("Gospel");
        roman_Db = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("Roman");
        // sug_update_db.keepSynced(true);
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

        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                access = dataSnapshot.child("access").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }


    @Override
    public void onStart() {
        super.onStart();
        btnFilter(showBeautyPost);
        BeautyfeedsFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, F_NEWS_FEED.NewsFeeds>(
                News.class
                , R.layout.templatesug_and_public_post
                , F_NEWS_FEED.NewsFeeds.class
                , beauty_Db.orderByValue()

        ) {
            @Override
            protected void populateViewHolder(final F_NEWS_FEED.NewsFeeds viewHolder, final News model, int position) {
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
        recyclerView.setAdapter(BeautyfeedsFirebaseRecyclerAdapter);
        btnFilter(showBeautyPost);
        perforeCircleLoadData();

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
        } else if (btn.getId() == showRoman.getId()) {
            showRoman.setImageResource(R.drawable.love2);
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
        showRoman.setImageResource(R.drawable.gray_love);
    }

    private void perforeCircleLoadData() {
        //Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();


        final FirebaseRecyclerAdapter<News, F_NEWS_FEED.NewsFeeds> SPORTfeedsFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, F_NEWS_FEED.NewsFeeds>(
                News.class
                , R.layout.templatesug_and_public_post
                , F_NEWS_FEED.NewsFeeds.class
                , sport_db.orderByValue()

        ) {
            @Override
            protected void populateViewHolder(final F_NEWS_FEED.NewsFeeds viewHolder, final News model, int position) {
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

        final FirebaseRecyclerAdapter<News, F_NEWS_FEED.NewsFeeds> RomanfeedsFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, F_NEWS_FEED.NewsFeeds>(
                News.class
                , R.layout.templatesug_and_public_post
                , F_NEWS_FEED.NewsFeeds.class
                , roman_Db.orderByChild("time")

        ) {
            @Override
            protected void populateViewHolder(final F_NEWS_FEED.NewsFeeds viewHolder, final News model, int position) {
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


        final FirebaseRecyclerAdapter<News, F_NEWS_FEED.NewsFeeds> GOSSIPfeedsFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, F_NEWS_FEED.NewsFeeds>(
                News.class
                , R.layout.templatesug_and_public_post
                , F_NEWS_FEED.NewsFeeds.class
                , gossip_DB.orderByChild("time")

        ) {
            @Override
            protected void populateViewHolder(final F_NEWS_FEED.NewsFeeds viewHolder, final News model, int position) {
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

        final FirebaseRecyclerAdapter<News, F_NEWS_FEED.NewsFeeds> FASHIONfeedsFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, F_NEWS_FEED.NewsFeeds>(
                News.class
                , R.layout.templatesug_and_public_post
                , F_NEWS_FEED.NewsFeeds.class
                , fashion_db.orderByChild("time")
        ) {
            @Override
            protected void populateViewHolder(final F_NEWS_FEED.NewsFeeds viewHolder, final News model, int position) {
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


        final FirebaseRecyclerAdapter<News, F_NEWS_FEED.NewsFeeds> MusicfeedsFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, F_NEWS_FEED.NewsFeeds>(
                News.class
                , R.layout.templatesug_and_public_post
                , F_NEWS_FEED.NewsFeeds.class
                , music_DB.orderByChild("time")
        ) {
            @Override
            protected void populateViewHolder(final F_NEWS_FEED.NewsFeeds viewHolder, final News model, int position) {
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

        final FirebaseRecyclerAdapter<News, F_NEWS_FEED.NewsFeeds> ComedyFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, F_NEWS_FEED.NewsFeeds>(
                News.class
                , R.layout.templatesug_and_public_post
                , F_NEWS_FEED.NewsFeeds.class
                , comedy_db.orderByValue()
        ) {
            @Override
            protected void populateViewHolder(final F_NEWS_FEED.NewsFeeds viewHolder, final News model, int position) {
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


        final FirebaseRecyclerAdapter<News, F_NEWS_FEED.NewsFeeds> EventfeedsFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, F_NEWS_FEED.NewsFeeds>(
                News.class
                , R.layout.templatesug_and_public_post
                , F_NEWS_FEED.NewsFeeds.class
                , event_Db.orderByChild("time")
        ) {
            @Override
            protected void populateViewHolder(final F_NEWS_FEED.NewsFeeds viewHolder, final News model, int position) {
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

        final FirebaseRecyclerAdapter<News, F_NEWS_FEED.NewsFeeds> MoviefeedsFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, F_NEWS_FEED.NewsFeeds>(
                News.class
                , R.layout.templatesug_and_public_post
                , F_NEWS_FEED.NewsFeeds.class
                , movies_DB.orderByChild("time")
        ) {
            @Override
            protected void populateViewHolder(final F_NEWS_FEED.NewsFeeds viewHolder, final News model, int position) {
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

        final FirebaseRecyclerAdapter<News, F_NEWS_FEED.NewsFeeds> InsipirationfeedsFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, F_NEWS_FEED.NewsFeeds>(
                News.class
                , R.layout.templatesug_and_public_post
                , F_NEWS_FEED.NewsFeeds.class
                , insipration_Db.orderByChild("time")
        ) {
            @Override
            protected void populateViewHolder(final F_NEWS_FEED.NewsFeeds viewHolder, final News model, int position) {
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

        final FirebaseRecyclerAdapter<News, F_NEWS_FEED.NewsFeeds> GospelfeedsFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, F_NEWS_FEED.NewsFeeds>(
                News.class
                , R.layout.templatesug_and_public_post
                , F_NEWS_FEED.NewsFeeds.class
                , gospel_DB.orderByChild("time")
        ) {
            @Override
            protected void populateViewHolder(final F_NEWS_FEED.NewsFeeds viewHolder, final News model, int position) {
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
        lshowRoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRecyleradpter(RomanfeedsFirebaseRecyclerAdapter, "Roman");
                btnFilter(showRoman);

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

    }

    private void setRecyleradpter(FirebaseRecyclerAdapter firebaseRAD, String headerText) {
        // selected_post_header.setText(headerText);
        // if (firebaseRAD.getItemCount() >= 1) {
        //  noPost.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.animate().setDuration(1);
        recyclerView.setAnimation(new AlphaAnimation(0, 10));
        recyclerView.setAdapter(firebaseRAD);
      /*  } else {
            noPost.setText("NO POST");
            recyclerView.setVisibility(View.INVISIBLE);
            noPost.setVisibility(View.VISIBLE);
        }*/

    }


    @Override
    public Context getContext() {
        return super.getContext();
    }

    public static class NewsFeeds extends RecyclerView.ViewHolder {
        View mView;
        ImageButton btn_like;
        DatabaseReference databaseUser, likeDatabase, notification;
        FirebaseAuth firebaseAuth;
        TextView numbers_of_like, post_User_name;
        ImageView post_user_image;
        ImageView post_image;
        FireClass fireClass;
        ImageButton deletePost;
        AlertDialog.Builder alert;
        RelativeLayout progressBar;
        Boolean likeClike = false;
        Context ctx;
        Button yesAmSure, noNotYet;
        RelativeLayout doYouWantToDeletePost,networkLayout;

        public NewsFeeds(View itemView) {
            super(itemView);
            mView = itemView;
            ctx = itemView.getContext();
            btn_like = (ImageButton) mView.findViewById(R.id.xml_btn_like);
            numbers_of_like = (TextView) mView.findViewById(R.id.xml_like_num);
            post_image = (ImageView) mView.findViewById(R.id.post_image);
            post_user_image = (ImageView) mView.findViewById(R.id.news_Image_postUser);
            post_User_name = (TextView) mView.findViewById(R.id.news_postUser);
            firebaseAuth = FirebaseAuth.getInstance();
            fireClass = new FireClass();
            String UNI = fireClass.university();
            deletePost = (ImageButton) mView.findViewById(R.id.news_feed_delete);

            likeDatabase = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("likes");

            likeDatabase.keepSynced(true);
            databaseUser = FirebaseDatabase.getInstance().getReference().child("users");
            databaseUser.keepSynced(true);
            notification = FirebaseDatabase.getInstance().getReference().child("notification");
            notification.keepSynced(true);
            progressBar = (RelativeLayout) mView.findViewById(R.id.public_progress_bar);
           networkLayout=(RelativeLayout)mView.findViewById(R.id.main_network_layout);
            doYouWantToDeletePost = (RelativeLayout) mView.findViewById(R.id.temp_public_areYouSureYouWantToDeletePost);
            yesAmSure = (Button) mView.findViewById(R.id.temp_public_yesAmSureDelete);
            noNotYet = (Button) mView.findViewById(R.id.temp_public_anNotSure);
            //  Glide.with(mView).asGif().load(R.drawable.loading).into(post_image);

        }

        public void likebtn(final String post_id, final String uid) {

            likeDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    numbers_of_like.setText(String.valueOf(dataSnapshot.child(post_id).getChildrenCount()));

                    if (dataSnapshot.child(post_id).hasChild(firebaseAuth.getCurrentUser().getUid())) {
                        btn_like.setImageResource(R.drawable.redlike);
                    } else {
                        btn_like.setImageResource(R.drawable.like);
                    }
                    btn_like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            likeClike = true;

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
                                    fireClass.likeClick(ctx);
                                    btn_like.setImageResource(R.drawable.redlike);
                                    likeClike = false;
                                }


                            }


                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        public void setTime(final String postTime) {
            TextView time = (TextView) mView.findViewById(R.id.news_time_Posted);
            String t = fireClass.timeago(Long.parseLong(postTime));
            time.setText(t);
        }

        public void sharebtn(final Context ctx, final String title, final String desc,
                             final String accessLevel, final String postId, final String imageUrl,
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
                    if (postUser.matches(firebaseAuth.getCurrentUser().getUid().toString()) || accessLevel.matches("3") || accessLevel.equals("3")) {
                        deletePost.setVisibility(View.VISIBLE);
                        deletePost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
             doYouWantToDeletePost.setVisibility(View.VISIBLE);
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
            yesAmSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if(fireClass.network(ctx,networkLayout)){
                       doYouWantToDeletePost.setVisibility(View.GONE);
                       progressBar.setVisibility(View.VISIBLE);
                       if (imageName != null && imageName != "" && imageName.length() > 1) {
                           final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Photos/" + imageName);

                           storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful()) {
                                       db.child(postId).removeValue();
                                       progressBar.setVisibility(View.GONE);
                                       Picasso.with(context).invalidate(imageUrl);
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
                }
            });
            noNotYet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doYouWantToDeletePost.setVisibility(View.GONE);
                }
            });
        }

        public void setviews(Long views) {
            TextView viewss = (TextView) mView.findViewById(R.id.xml_view_num);
            viewss.setText(String.valueOf(views));
        }


        public void setTitle(String title, String postTime) {
            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            if (title.length() > 1 || title != "") {
                post_title.setText(title);

            } else {
                post_title.setVisibility(View.GONE);
            }
            setTime(postTime);
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

        //  public void setviews(String views, DatabaseReference database) {
        //   TextView viewss = (TextView) mView.findViewById(R.id.xml_view_num);
        //    viewss.setText(views);
        // }

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

  /*      public void deletePost(final String accessLevel, final Context ctx, final String postId, String imageUrl, final String imageName, final String postUser, final DatabaseReference db) {
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Photos/" + imageName);
            //    Toast.makeText(ctx,"Access Level= "+accessLevel+" current User "+firebaseAuth.getCurrentUser().getUid()+" post user "+postUser,
            //          Toast.LENGTH_LONG).show();
            Toast.makeText(ctx,"Access Level= "+accessLevel+" current User "+firebaseAuth.getCurrentUser().getUid()+" post user "+postUser,Toast.LENGTH_LONG).show();

            if (accessLevel != null) {
                try {


                    // (.)-(.)

                    if (postUser.matches(firebaseAuth.getCurrentUser().getUid().toString()) || accessLevel.matches("3")||accessLevel.equals("3")) {
                        deletePost.setVisibility(View.VISIBLE);

                        deletePost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                progressBar.setVisibility(View.VISIBLE);
                                if (imageName!=null&&imageName!=""&&imageName.length()>1){
                                    final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Photos/" + imageName);

                                    storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                db.child(postId).removeValue();
                                                progressBar.setVisibility(View.GONE);
                                            } else {

                                            }
                                        }
                                    });
                                }else {

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
            }
        }
*/
    }
}
