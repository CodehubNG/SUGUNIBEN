package codehub.suguniben;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

public class Notification_feed extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_feed);
        firebaseAuth = FirebaseAuth.getInstance();
        final DatabaseReference notificationDatabse = FirebaseDatabase.getInstance().getReference().child("notification").child(firebaseAuth.getCurrentUser().getUid())
                .child(firebaseAuth.getCurrentUser().getUid());
        recyclerView = (RecyclerView) findViewById(R.id.notification_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerAdapter<notificationGetter, notificationViewHolder> notificationGetternotificationViewHolderFirebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<notificationGetter, notificationViewHolder>(
                notificationGetter.class,
                R.layout.temp_notification,
                notificationViewHolder.class,
                notificationDatabse

        ) {
            @Override
            protected void populateViewHolder(final notificationViewHolder viewHolder, final notificationGetter model, int position) {
                notificationDatabse.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(model.getNotificationId())) {
                            viewHolder.setImage(dataSnapshot.child("image").getValue().toString());
                            if (dataSnapshot.child("type").getValue().toString() == "comment") {
                                viewHolder.setTitle_Desc_TimeAgo("commented on your post",
                                        dataSnapshot.child("comment").getValue().toString(),
                                        dataSnapshot.child("time").getValue().toString());
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
    }

    public static class notificationViewHolder extends RecyclerView.ViewHolder {
        View view;
        Context context;
        ImageView imageView;
        TextView title, desc, timeago;

        public notificationViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            context = view.getContext();
            imageView = (ImageView) view.findViewById(R.id.temp_notifi_image);
            title = (TextView) view.findViewById(R.id.temp_notifi_title);
            desc = (TextView) view.findViewById(R.id.temp_notifi_desc);
            timeago = (TextView) view.findViewById(R.id.temp_notifi_time_ago);
        }

        public void setTitle_Desc_TimeAgo(String mtitle, String mdesc, String mtimeago) {
            title.setText(mtitle);
            desc.setText(mdesc);
            timeago.setText(mtimeago);
        }

        public void setImage(final String Image) {
            Picasso.with(context).load(Image).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(Image).into(imageView);
                }
            });
        }
    }

    public class notificationGetter {
        private String notificationId, from;

        public notificationGetter() {

        }

        public notificationGetter(String notificationId, String from) {
            this.notificationId = notificationId;
            this.from = from;
        }

        public String getNotificationId() {
            return notificationId;
        }

        public void setNotificationId(String notificationId) {
            this.notificationId = notificationId;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }
    }
}
