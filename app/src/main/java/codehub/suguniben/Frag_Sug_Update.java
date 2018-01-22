package codehub.suguniben;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;



/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_Sug_Update extends Fragment {
    static String username;
    private static Context context;
    Context contextt;
    AdView mAdView;

    private AlertDialog.Builder alert;
    private RecyclerView NewsList;
    private FireClass fireClass;
    private DatabaseReference blogDatabase;
    private DatabaseReference userDatabase;
    //  private DatabaseReference likeDatabase;
    private DatabaseReference notification;
    private String access;
    private FirebaseAuth firebaseAuth;
private  int adsCount=0;
    //private static String UNI;
    private Boolean likeClike = false;
    private FirebaseRecyclerAdapter<News, Frag_Sug_Update.NewsViewHolder> firebaseRecyclerAdapter = null;
    private RewardedVideoAd mAd;
    private Button retryNetwork;
   // private RelativeLayout network_Layout;

    public Frag_Sug_Update() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag__sug__update, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MobileAds.initialize(getContext().getApplicationContext(),
                getString(R.string.admob_app_id));
        fireClass = new FireClass();
        // UNI=fireClass.university();
        blogDatabase = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("Sug");
        userDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        blogDatabase.keepSynced(true);
        userDatabase.keepSynced(true);

        notification = FirebaseDatabase.getInstance().getReference().child("notification");
        notification.keepSynced(true);
        alert = new AlertDialog.Builder(this.getContext());
        alert.setCancelable(true);
        NewsList = (RecyclerView) getActivity().findViewById(R.id.list_of_news);
        //   LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext().getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            NewsList.setHasFixedSize(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            NewsList.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        //mAdView = (AdView) getActivity().findViewById(R.id.adView);
        // AdRequest adRequest = new AdRequest.Builder().build();

        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //   String name = dataSnapshot.child(firebaseAuth.getCurrentUser().getUid()).child("fname").getValue().toString();
                //  String imagUri = dataSnapshot.child(firebaseAuth.getCurrentUser().getUid()).child("image").getValue().toString();
                try {
                    access = dataSnapshot.child(firebaseAuth.getCurrentUser().getUid()).child("access").getValue().toString();
                } catch (NullPointerException ex) {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            Intent loginIntent = new Intent(getContext(), LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);

        } else {
            username = firebaseAuth.getCurrentUser().getUid();
        }

        TextView newSugPost = (TextView) getActivity().findViewById(R.id.sug_update_new_post);
        newSugPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext().getApplicationContext(), PostActitvity.class);
                startActivity(intent);
            }
        });
       // network_Layout = (RelativeLayout) getActivity().findViewById(R.id.main_network_layout);
       // retryNetwork = (Button) getActivity().findViewById(R.id.main_network);

       /* retryNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               fireClass.network(getContext().getApplicationContext(),network_Layout);
            }
        });*/

    }


    @Override
    public Context getContext() {
        return super.getContext();
    }
    private void firebaseSeacrch(String SearchString) {
        Query query;
        query = userDatabase.orderByChild("time").startAt(fireClass.currentDate());
        if (SearchString != null && SearchString != "" && SearchString.length() < 1) {
            query = userDatabase.orderByChild("time");
        } else {
            query = userDatabase.orderByChild("time").startAt(fireClass.currentDate());//.endAt(SearchString+"\uf8ff");
        }

    }
    @Override
    public void onStart() {
        super.onStart();
        final DatabaseReference views = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("views");
        views.keepSynced(true);
        Query query;
        query = blogDatabase.orderByChild("time");
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, Frag_Sug_Update.NewsViewHolder>(
                News.class,
                R.layout.templatesug_and_public_post,
                Frag_Sug_Update.NewsViewHolder.class,
               query
        ) {
            /*   @Override
               public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                   switch (viewType) {
                       case NATIVE_APP_INSTALL_AD_VIEW_TYPE:
                           View nativeAppInstallLayoutView = LayoutInflater.from(
                                   viewGroup.getContext()).inflate(R.layout.ad_app_install,
                                   viewGroup, false);
                           return new NativeAppInstallAdViewHolder(nativeAppInstallLayoutView);
                       case NATIVE_CONTENT_AD_VIEW_TYPE:
                           View nativeContentLayoutView = LayoutInflater.from(
                                   viewGroup.getContext()).inflate(R.layout.ad_content,
                                   viewGroup, false);
                           return new NativeContentAdViewHolder(nativeContentLayoutView);
                       case MENU_ITEM_VIEW_TYPE:
                           // Fall through.
                       default:
                           View menuItemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                                   R.layout.menu_item_container, viewGroup, false);
                           return new MenuItemViewHolder(menuItemLayoutView);
                   }
               }

               @Override
               public int getItemViewType(int position) {

                   Object recyclerViewItem = mRecyclerViewItems.get(position);
                   if (recyclerViewItem instanceof NativeAppInstallAd) {
                       return NATIVE_APP_INSTALL_AD_VIEW_TYPE;
                   } else if (recyclerViewItem instanceof NativeContentAd) {
                       return NATIVE_CONTENT_AD_VIEW_TYPE;
                   }
                   return MENU_ITEM_VIEW_TYPE;
               }*/

            @Override
            public News getItem(int position) {
                return super.getItem(getItemCount() - 1 - position);
            }

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @Override
            public long getItemId(int position) {
                return super.getItemId(position);
            }

            @Override
            public DatabaseReference getRef(int position) {
                return super.getRef(position);
            }

            @Override
            protected void populateViewHolder(final Frag_Sug_Update.NewsViewHolder viewHolder, final News model, int position) {

                final String post_id = getRef(position).getKey().toString();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setdesc(model.getDesc());
                //  viewHolder.setviews(String.valueOf(model.getViews()), getRef(position));

                viewHolder.setimage(getContext(), model.getImage());
                viewHolder.sharebtn(getActivity(), getContext(), model.getTitle(), model.getDesc());
                // viewHolder.setUsers(model.getUsers(), getRef(position));
                viewHolder.likebtn(post_id, model.getUid());
                viewHolder.setUsersImg(getContext().getApplicationContext(), model.getUid());
               /* if (network(getApplicationContext()) == false) {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
*/
              //  fireClass.network(getContext().getApplicationContext());
                viewHolder.setTime(model.getTime());
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
                viewHolder.deletePost(access, getContext().getApplicationContext(), post_id, model.getImage(), model.getImage_name(), model.getUid(), blogDatabase);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            NewsList.setAdapter(firebaseRecyclerAdapter);
        }


    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {

        Context ctx;
        Activity activity;
        // AdView mAdView;
        View mView;
        ImageButton btn_like;
        DatabaseReference databaseUser;
        FirebaseAuth firebaseAuth;
        TextView numbers_of_like, post_User_name;
        ImageView post_user_image;
        ImageView post_image;
        FireClass fireClass;
        //  private Context appContext;
        RelativeLayout progressBar;
        ImageButton deletePost;
        Boolean likeClike = false;
        DatabaseReference notification, likeDatabase;
        Button yesAmSure, noNotYet;
        RelativeLayout doYouWantToDeletePost,networkLayout;

        public NewsViewHolder(View itemView) {
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
            deletePost = (ImageButton) mView.findViewById(R.id.news_feed_delete);


            notification = FirebaseDatabase.getInstance().getReference().child("notification");
            notification.keepSynced(true);

            databaseUser = FirebaseDatabase.getInstance().getReference().child("users");
            databaseUser.keepSynced(true);
            progressBar = (RelativeLayout) mView.findViewById(R.id.public_progress_bar);
            likeDatabase = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("likes");
            likeDatabase.keepSynced(true);
            networkLayout=(RelativeLayout)mView.findViewById(R.id.main_network_layout);
            doYouWantToDeletePost = (RelativeLayout) mView.findViewById(R.id.temp_public_areYouSureYouWantToDeletePost);
            yesAmSure = (Button) mView.findViewById(R.id.temp_public_yesAmSureDelete);
            noNotYet = (Button) mView.findViewById(R.id.temp_public_anNotSure);
          //   ads=(RelativeLayout)mView.findViewById(R.id.temp_public_adsBanner);

            //   fireClass.network(ctx,n);
        }

      /*  public  void adsBanner(int count){
            if(count==7){
                ads.setVisibility(View.VISIBLE);
            }

        }*/

        public void likebtn(final String post_id, final String uid) {

            likeDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    numbers_of_like.setText(String.valueOf(dataSnapshot.child(post_id).getChildrenCount()));
                    if (dataSnapshot.child(post_id).hasChild(firebaseAuth.getCurrentUser().getUid())) {
                        btn_like.setImageResource(R.drawable.redlike);
                    } else {
                        btn_like.setImageResource(R.drawable.like);

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
                            if (likeClike) {
                                if (dataSnapshot.child(post_id).hasChild(firebaseAuth.getCurrentUser().getUid())) {
                                    likeDatabase.child(post_id).
                                            child(firebaseAuth.getCurrentUser().getUid())
                                            .child("post_userid").removeValue();
                                    DatabaseReference push = notification.child(uid).push();
                                    push.child("type").setValue("like");
                                    push.child("from").setValue(firebaseAuth.getCurrentUser().getUid());
                                    likeClike = false;
                                } else {
                                    likeDatabase.child(post_id).
                                            child(firebaseAuth.getCurrentUser().getUid()).
                                            child("post_userid").setValue(uid);
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


        public void setTime(final String postTime) {
            TextView time = (TextView) mView.findViewById(R.id.news_time_Posted);
            try {
                String t = fireClass.timeago(Long.parseLong(postTime));
                time.setText(t);
            } catch (Exception ex) {

            }
        }

        public void sharebtn(final Activity atv, final Context ctx, final String title, final String desc) {
            ImageButton btn_share = (ImageButton) mView.findViewById(R.id.xml_btn_sharePost);

            btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (fireClass.securityCheck(Manifest.permission.WRITE_EXTERNAL_STORAGE, atv, ctx)) {

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        String msg = title + "\n \n " + desc;
                        post_image.setDrawingCacheEnabled(true);
                        Drawable drawable = post_image.getDrawable();
                        if (drawable != null ) {
                            intent.setType("image/*");
                            Bitmap bitmap1 = ((BitmapDrawable) drawable).getBitmap();
                            String path = MediaStore.Images.Media.insertImage(ctx.getContentResolver(), bitmap1, "img", null);
                            try {
                                final Uri uri = Uri.parse(path);
                                intent.putExtra(Intent.EXTRA_STREAM, uri);
                            }catch (Exception ex){

                            }

                        }else {
                            intent.setType("text/plain");
                        }
                        if (msg != "" && msg != null && msg.length() > 1) {

                            intent.putExtra(Intent.EXTRA_TEXT, msg);
                        }

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ctx.startActivity(intent);

                    }
                }
            });

        }

        public void views(DatabaseReference database, int value) {
            int val = value + 1;
            database.child("views").setValue(val);
        }

        public void setTitle(String title) {
            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
           try {
               if (title.length() > 1 || title != "") {
                   post_title.setText(title);

               } else {
                   post_title.setVisibility(View.GONE);
               }
           }catch (NullPointerException n){}
          //  setTime(postTime);
        }

        public void setdesc(String desc) {
            TextView post_desc = (TextView) mView.findViewById(R.id.post_description);
          try {
              if (desc.length() > 1 && !desc.matches("")) {
                  post_desc.setText(desc);
                  post_desc.setVisibility(View.VISIBLE);
              } else {
                  post_desc.setVisibility(View.GONE);
              }
          }catch (NullPointerException n){}
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
                    //mAdView = (AdView)mView.findViewById(R.id.adView);
                    // AdRequest adRequest = new AdRequest.Builder().build();
                    // mAdView.loadAd(adRequest);

            }
        }

        public void deletePost(final String accessLevel, final Context ctx, final String postId, final String imageUrl, final String imageName, final String postUser, final DatabaseReference db) {
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Photos/" + imageName);
            //    Toast.makeText(ctx,"Access Level= "+accessLevel+" current User "+firebaseAuth.getCurrentUser().getUid()+" post user "+postUser,
            //          Toast.LENGTH_LONG).show();
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
            }
            yesAmSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(fireClass.network(ctx,networkLayout)) {
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

        public void setUsersImg(final Context ctx, final String uid) {
            databaseUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                     try {
                    final String img = dataSnapshot.child(uid).child("image").getValue().toString();
                    post_User_name.setText(dataSnapshot.child(uid).child("fname").getValue().toString());
                    if (img != "default" && img != null) {
                        Picasso.with(ctx).load(img).networkPolicy(NetworkPolicy.OFFLINE).noFade().placeholder(R.drawable.user2).into(post_user_image, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(ctx).load(img).noFade().placeholder(R.drawable.user2).into(post_user_image);
                            }
                        });
                    } else {
                        post_user_image.setImageResource(R.drawable.user2);
                    }
                  }catch (NullPointerException ex){

                  }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


    }

}
