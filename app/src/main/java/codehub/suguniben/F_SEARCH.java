package codehub.suguniben;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class F_SEARCH extends Fragment {
    private static Context context;
    //DatabaseReference footballDatabase;
    RecyclerView recyclerView;
    FireClass fireClass;
    private Boolean likeClike = false;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userDatabase;
   // private DatabaseReference likeDatabase;
    private String UNI;
    private CircleImageView search;


    public F_SEARCH() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.f_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = (RecyclerView) getActivity().findViewById(R.id.football_RecyclerView);
        firebaseAuth = FirebaseAuth.getInstance();
        fireClass = new FireClass();
        UNI = fireClass.university();
        userDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        userDatabase.keepSynced(true);

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.football_RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final EditText username = (EditText) getActivity().findViewById(R.id.search_name);
        search = (CircleImageView) getActivity().findViewById(R.id.search_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseSeacrch(username.getText().toString());
            }
        });
        username.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (username.length() < 1 && username.getText().toString() == "") {
                    firebaseSeacrch("");
                }
                return false;
            }
        });
        username.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (username.length() < 1 && username.getText().toString() == "") {
                    firebaseSeacrch("");
                }
                return false;
            }
        });
        firebaseSeacrch("");

    }

    private void firebaseSeacrch(String SearchString) {
        Query query;

        if (SearchString != null && SearchString != "" && SearchString.length() < 1) {
            query = userDatabase.orderByChild("fname");
        } else {
            query = userDatabase.orderByChild("fname").startAt(SearchString);//.endAt(SearchString+"\uf8ff");
        }
        FirebaseRecyclerAdapter<followersGetter, F_SEARCH.usersViewHolder> feedsFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<followersGetter, usersViewHolder>(
                followersGetter.class
                , R.layout.temp_followers
                , F_SEARCH.usersViewHolder.class
                , query
        ) {
            @Override
            protected void populateViewHolder(final F_SEARCH.usersViewHolder viewHolder, final followersGetter model, int position) {
                //  Toast.makeText(Profile.this, model.getUid() + "  Hi", Toast.LENGTH_SHORT).show();
                //   Toast.makeText(Profile.this, "hello", Toast.LENGTH_SHORT).show();
                final String uid = model.getUid();

                if (uid != null) {
                    viewHolder.txtname.setOnClickListener(new View.OnClickListener() {
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
                    //   Toast.makeText(getContext().getApplicationContext(), uid, Toast.LENGTH_SHORT).show();
                    userDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                if (uid.length() > 1) {
                                    viewHolder.setName(dataSnapshot.child(uid).child("fname").getValue().toString(), uid);
                                    viewHolder.setImage(getContext().getApplicationContext(), dataSnapshot.child(uid).child("image").getValue().toString(), uid);
                                }
                            } catch (NullPointerException n) {

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }


        };
        recyclerView.setAdapter(feedsFirebaseRecyclerAdapter);
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    public static class usersViewHolder extends RecyclerView.ViewHolder {
        View view;
        Button followMe;
        FirebaseAuth firebaseAuth;
        Boolean follow = false;
        FireClass fireClass;
        DatabaseReference follodatabse, followingdatabase;
        CircleImageView imageView;
        TextView txtname;
        String UNI;

        public usersViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            firebaseAuth = FirebaseAuth.getInstance();
            followMe = (Button) itemView.findViewById(R.id.temp_followers_follow_btn);
            imageView = (CircleImageView) view.findViewById(R.id.temp_followers_image);
            txtname = (TextView) view.findViewById(R.id.temp_followers_name);
            fireClass = new FireClass();
            UNI = fireClass.university();
            follodatabse = FirebaseDatabase.getInstance().getReference().child(UNI).child("followers");
            follodatabse.keepSynced(true);
        }

        public void setFollowMe(final String uid, String currentUser) {
            try {
                follodatabse = follodatabse.child(uid);
                if (uid.matches(currentUser)) {
                    followMe.setVisibility(View.GONE);
                    followMe.setEnabled(false);
                } else {
                    followMe.setVisibility(View.VISIBLE);
                    followMe.setEnabled(true);
                }
                followingdatabase = FirebaseDatabase.getInstance().getReference().child(UNI).child("following").child(firebaseAuth.getCurrentUser().getUid());
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
                                        followingdatabase.child(firebaseAuth.getCurrentUser().getUid()).removeValue();
                                        follow = false;
                                    } else {
                                        follodatabse.child(firebaseAuth.getCurrentUser().getUid()).child("uid").setValue(firebaseAuth.getCurrentUser().getUid());
                                        followingdatabase.child(firebaseAuth.getCurrentUser().getUid()).child("uid").setValue(firebaseAuth.getCurrentUser().getUid());
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
            } catch (NullPointerException ex) {

            }
        }

        public void setImage(final Context ctx, final String image, final String uid) {

            if (image.length() > 1) {
                Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(ctx).load(image).into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                imageView.setImageResource(R.drawable.user2);
                            }
                        });
                    }
                });
            } else {

            }
        }

        public void setName(String name, final String uid) {

            if (name.length() > 1) {
                txtname.setText(name);
            } else {
                txtname.setText("...");
            }
        }
    }
}
