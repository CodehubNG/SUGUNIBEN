package codehub.suguniben;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class SingleActivity extends AppCompatActivity {

    Boolean likebtnClike = false;
    private AlertDialog.Builder alert;
    private String post_title, post_Desc, post_image, post_id,post_userId;
    private TextView title, desc;
    private EditText mComment;
    private ImageButton mSendComment;
    private ImageView imageView;
    private DatabaseReference commentDatabse;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userDatabase, databaselike, views, notification;
    private ImageButton btn_like, btn_share;
    private FireClass fireClass;
    private TextView number_of_likes, numbers_of_views,post_user_name ,numbers_of_comment;
    private RecyclerView commentList;
    private ImageView post_user_image;
    private String uid;
    private String UNI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        firebaseAuth = FirebaseAuth.getInstance();
        commentList = (RecyclerView) findViewById(R.id.singel_commentList);
        commentList.setHasFixedSize(true);
        commentList.setLayoutManager(new LinearLayoutManager(this));
        fireClass = new FireClass();
        UNI = fireClass.university();
        title = (TextView) findViewById(R.id.singel_post_title);
        desc = (TextView) findViewById(R.id.single_post_desc);
        imageView = (ImageView) findViewById(R.id.singel_post_image);
        mComment = (EditText) findViewById(R.id.singel_comment);
        mSendComment = (ImageButton) findViewById(R.id.single_btnComment);
        btn_like = (ImageButton) findViewById(R.id.single_btn_like);
        btn_share = (ImageButton) findViewById(R.id.single_btn_sharePost);
        numbers_of_views = (TextView) findViewById(R.id.single_view_num);

        alert=new AlertDialog.Builder(this);
        alert.setCancelable(true);

        post_title = getIntent().getExtras().getString("title");
        post_Desc = getIntent().getExtras().getString("desc");
        post_userId=getIntent().getExtras().getString("postUser");
        post_image = getIntent().getExtras().getString("image");

        post_id = getIntent().getExtras().getString("post_id");

        setTitle(post_title);
        notification = FirebaseDatabase.getInstance().getReference().child("notification");
        notification.keepSynced(true);
        commentDatabse = FirebaseDatabase.getInstance().getReference().child(UNI).child("comment").child(post_id);
        commentDatabse.keepSynced(true);
        userDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        userDatabase.keepSynced(true);
        views = FirebaseDatabase.getInstance().getReference().child(UNI).child("views");
        databaselike = FirebaseDatabase.getInstance().getReference().child(UNI).child("likes");

        number_of_likes = (TextView) findViewById(R.id.single_like_num);
        numbers_of_views = (TextView) findViewById(R.id.single_view_num);
        post_user_image=(ImageView)findViewById(R.id.single_post_user_image);
        post_user_name=(TextView) findViewById(R.id.single_post_user_name);
        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uid = dataSnapshot.child(firebaseAuth.getCurrentUser().getUid()).child("image").getValue().toString();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       post_user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleActivity.this, Profile.class);
                intent.putExtra("acc_id", post_userId);
                startActivity(intent);

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bigIn=new Intent(SingleActivity.this,Big_Image.class);
                bigIn.putExtra("img",post_image);
                startActivity(bigIn);
            }
        });

        title.setText(post_title);
        desc.setText(post_Desc);
        if (post_image.length()> 1) {
            Picasso.with(this).load(post_image).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(getApplicationContext()).load(post_image).into(imageView);
                }
            });
        }

        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String img=dataSnapshot.child(post_userId).child("image").getValue().toString();
                post_user_name.setText(dataSnapshot.child(post_userId).child("fname").getValue().toString());
                if(img!="default"||img!=null){
                    Picasso.with(getApplicationContext()).load(img).into(post_user_image);
                }else {
                    post_user_image.setImageResource(R.drawable.user2);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Comments = mComment.getText().toString().trim();
             if(Comments.length()>=1&& Comments!=""){
                 userDatabase.addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         {
                             if(Comments!=null ){
                                 String fname = dataSnapshot.child(firebaseAuth.getCurrentUser().getUid()).child("fname").getValue().toString();
                                 String image = dataSnapshot.child(firebaseAuth.getCurrentUser().getUid()).child("image").getValue().toString();
                                 DatabaseReference post = commentDatabse.push();
                                 post.child("comment").setValue(Comments);
                                 post.child("user").setValue(fname);
                                 post.child("image").setValue(post_image);
                                 post.child("uid").setValue(firebaseAuth.getCurrentUser().getUid());
                                 post.child("post_author").setValue(post_userId);
                                 post.child("time").setValue(String.valueOf(fireClass.currentDate()));
                                 DatabaseReference push = notification.child(post_userId).push();
                                 push.child("type").setValue("comment");
                                 push.child("comment").setValue(Comments);
                                 push.child("from").setValue(firebaseAuth.getCurrentUser().getUid());
                                 push.child("time").setValue(String.valueOf(fireClass.currentDate()));
                                 push.child("post_id").setValue(post_id);
                                 push.child("seen").setValue(false);
                                 // InputMethodManager inputMethodManager(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                 //inputMethodManager.hideSoftInputFromWindow(this.getWindowsToke(),
                                 //   inputMethodManager.HIDE_NOT_ALWAYS);
                                 mComment.setText("");
                                 mComment.setEnabled(false);
                                 mComment.setEnabled(true);
                             }
                             else{
                                 alert.setMessage("Invalid Comment");
                                 alert.show();
                             }
                         }


                     }


                     @Override
                     public void onCancelled(DatabaseError databaseError) {

                     }
                 });
             }else {
                 alert.setMessage("Invalid Comment");
                 alert.show();
             }
                //   if(!Comments.isEmpty()){}
            }
        });
        views.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numbers_of_views.setText(String.valueOf(dataSnapshot.child(post_id).getChildrenCount()));
                if (dataSnapshot.child(post_id).hasChild(firebaseAuth.getCurrentUser().getUid())) {
                } else {
                    views.child(post_id).child(firebaseAuth.getCurrentUser().getUid()).setValue(String.valueOf(fireClass.currentDate()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likebtnClike = true;
                databaselike.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (likebtnClike) {
                            if (dataSnapshot.child(post_id).hasChild(firebaseAuth.getCurrentUser().getUid())) {
                                databaselike.child(post_id).
                                        child(firebaseAuth.getCurrentUser().getUid())
                                        .child("post_userid").removeValue();
                                likebtnClike = false;
                            } else {
                                databaselike.child(post_id).
                                        child(firebaseAuth.getCurrentUser().getUid()).
                                        child("post_userid").setValue(post_userId);
                                fireClass.likeClick(getApplicationContext());
                                likebtnClike = false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fireClass.securityCheck(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,SingleActivity.this, getApplicationContext())) {

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    String msg = title + "\n \n " + desc;
                    imageView.setDrawingCacheEnabled(true);
                    Drawable drawable = imageView.getDrawable();
                    if (drawable != null ) {
                        intent.setType("image/*");
                        Bitmap bitmap1 = ((BitmapDrawable) drawable).getBitmap();
                        String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), bitmap1, "img", null);
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
                    startActivity(intent);

                }
            }

        });

        databaselike.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                number_of_likes.setText(String.valueOf(dataSnapshot.child(post_id).getChildrenCount()));
                if (dataSnapshot.child(post_id).hasChild(firebaseAuth.getCurrentUser().getUid())) {
                    btn_like.setImageResource(R.drawable.single_like);

                } else {
                    btn_like.setImageResource(R.drawable.single_unlike);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        FirebaseRecyclerAdapter<comment, CommentViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<comment, CommentViewHolder>(
                comment.class,
                R.layout.comment,
                CommentViewHolder.class,
                commentDatabse
        ) {
            @Override
            protected void populateViewHolder(final CommentViewHolder viewHolder, final comment model, int position) {
                viewHolder.setuser(model.getUser());
                viewHolder.setcomment(model.getComment());
                /// Toast.makeText(getApplicationContext(), model.getComment(), Toast.LENGTH_LONG).show();
                    viewHolder.muser.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(SingleActivity.this, Profile.class);
                            intent.putExtra("acc_id", model.getUid());
                            startActivity(intent);
                        }
                    });
                viewHolder.mimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent bigIn=new Intent(SingleActivity.this,Big_Image.class);
                        bigIn.putExtra("img",model.getImage());
                        startActivity(bigIn);
                    }
                });
                if (uid != null) {
                    viewHolder.setImage(getApplicationContext(), uid);
                }


            }
        };
        commentList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();


    }

    @Override
    protected void onStart() {
        super.onStart();
title.isFocused();
        title.setFocusable(true);
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView mimage;
        TextView muser, mcomment;
        View mView;

        public CommentViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            muser = (TextView) mView.findViewById(R.id.comment_title);
            mcomment = (TextView) mView.findViewById(R.id.comment_comment);
            mimage = (ImageView) mView.findViewById(R.id.comment_image);

        }

        public void setuser(String user) {
            muser.setText(user);
        }

        public void setcomment(String comment) {
            mcomment.setText(comment);
        }

        public void setImage(final Context ctx, final String image) {
     if(image!=null){
         if(image.length()>1){
             Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(mimage, new Callback() {
                 @Override
                 public void onSuccess() {

                 }

                 @Override
                 public void onError() {
                     Picasso.with(ctx).load(image).into(mimage);
                 }
             });
         }
     }
     else {
         mimage.setVisibility(View.GONE);
     }
        }

    }
}
