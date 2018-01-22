package codehub.suguniben;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class F_HOUSE extends Fragment {
    FireClass fireClass;
    //FloatingActionButton newSale;
    private DatabaseReference houseDatabase;//, offCampus, campusOthers;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    //private TextView campus;//, campus_others, off_campus;
    private CircleImageView search;


    public F_HOUSE() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.f_house, container, false);
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        fireClass = new FireClass();
        String UNI = fireClass.university();
        houseDatabase = FirebaseDatabase.getInstance().getReference().child(UNI).child("house");
    /*    campusDatabase = FirebaseDatabase.getInstance().getReference().child(UNI).child("house").child("campus");
        offCampus = FirebaseDatabase.getInstance().getReference().child(UNI).child("house").child("off_campus");
        campusOthers = FirebaseDatabase.getInstance().getReference().child(UNI).child("house").child("campus_others");
        campusDatabase.keepSynced(true);
        offCampus.keepSynced(true);
        campusOthers.keepSynced(true);
*/
        //campus = (TextView) getActivity().findViewById(R.id.house_campus);
        //  off_campus = (TextView) getActivity().findViewById(R.id.house_Off_campus);
        // campus_others = (TetView) getActivity().findViewById(R.id.house_campus_others);


        recyclerView = (RecyclerView) getActivity().findViewById(R.id.house_RecylerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        TextView renthostel = (TextView) getActivity().findViewById(R.id.house_main_rent_house);
        renthostel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(
                      getContext(),POST_RENT_HOSTEL.class));
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        final EditText houseName = (EditText) getActivity().findViewById(R.id.house_name);
        search = (CircleImageView) getActivity().findViewById(R.id.house_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load(houseName.getText().toString());
            }
        });
        load("");
    }

    private void load(String searchPara) {
        Query query;
        if (searchPara != "" && searchPara != null && searchPara.length() > 1) {
            query = houseDatabase.orderByChild("name").startAt(searchPara);
        } else {
            query = houseDatabase.orderByChild("time");
        }
        final FirebaseRecyclerAdapter<SALES, house_Holder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<SALES, house_Holder>(
                SALES.class,
                R.layout.template_buy_sell_and_accomodation,
                house_Holder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(F_HOUSE.house_Holder viewHolder, SALES model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setAmount(model.getAmount());
                //viewHolder.setDesc(model.getDesc());
                viewHolder.setTime(model.getTime());
                viewHolder.setTel(model.getTel());
                // try {
                viewHolder.setImg1(getContext(), model.getImg1());
                  viewHolder.setImg2(getContext(), model.getImg2());
                   viewHolder.setImg3(getContext(), model.getImg3());

                // }
                // catch (Exception ex){

                //}
            }
        };
       /*  final FirebaseRecyclerAdapter<SALES, house_Holder> firebaseRecyclerAdapterOff_Campus = new FirebaseRecyclerAdapter<SALES, house_Holder>(
                SALES.class,
                R.layout.template_buy_sell_and_accomodation,
                house_Holder.class,
                offCampus
        ) {
            @Override
            protected void populateViewHolder(F_HOUSE.house_Holder viewHolder, SALES model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setAmount(model.getAmount());
                viewHolder.setDesc(model.getDesc());
                // viewHolder.setTime(getContext() ,  Date.parse(model.getTime()));
                // try {
                if (model.getImg1() != null) {
                    viewHolder.setImg1(getContext(), model.getImg1());
                    //    Toast.makeText(getContext(),model.getImg1(),Toast.LENGTH_SHORT).show();
                }
                if (model.getImg2() != null) {
                    viewHolder.setImg2(getContext(), model.getImg2());
                }
                if (model.getImg3() != null) {
                    viewHolder.setImg3(getContext(), model.getImg3());
                }

                // }
                // catch (Exception ex){

                //}
            }
        };
       final FirebaseRecyclerAdapter<SALES, house_Holder> firebaseRecyclerAdapterCampus_Others = new FirebaseRecyclerAdapter<SALES, house_Holder>(
                SALES.class,
                R.layout.template_buy_sell_and_accomodation,
                house_Holder.class,
                campusOthers
        ) {
            @Override
            protected void populateViewHolder(F_HOUSE.house_Holder viewHolder, SALES model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setAmount(model.getAmount());
                viewHolder.setDesc(model.getDesc());
                // viewHolder.setTime(getContext() ,  Date.parse(model.getTime()));
                // try {
                if (model.getImg1() != null) {
                    viewHolder.setImg1(getContext(), model.getImg1());
                    Toast.makeText(getContext(), model.getImg1(), Toast.LENGTH_SHORT).show();
                }
                if (model.getImg2() != null) {
                    viewHolder.setImg2(getContext(), model.getImg2());
                }
                if (model.getImg3() != null) {
                    viewHolder.setImg3(getContext(), model.getImg3());
                }

                // }
                // catch (Exception ex){

                //}
            }
        };*/
       /*  campus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setAdapter(firebaseRecyclerAdapter);
            }
        });
       off_campus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setAdapter(firebaseRecyclerAdapterOff_Campus);
            }
        });
        campus_others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setAdapter(firebaseRecyclerAdapterCampus_Others);
            }
        });*/
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private static class house_Holder extends RecyclerView.ViewHolder {
        View view;
        FireClass fireClass;
        Context context;

        public house_Holder(View itemView) {
            super(itemView);
            view = itemView;
            context = itemView.getContext();
fireClass=new FireClass();
        }

        public void setName(String name) {
            TextView titile = (TextView) view.findViewById(R.id.buy_title);
            titile.setText(name);

        }

        public void setAmount(String Amount) {
            TextView amount = (TextView) view.findViewById(R.id.buy_amount);
            amount.setText(Amount);
        }

        public void setDesc(String desc) {
            TextView Desc = (TextView) view.findViewById(R.id.sell_desc);
            Desc.setText(desc);
        }

        public void setTime(String time) {
            TextView timee = (TextView) view.findViewById(R.id.buy_time_ago);
            String t = fireClass.timeago(Long.parseLong(time));
            timee.setText(t);
        }

        public void setTel(final String tel) {
            ImageButton buycon=(ImageButton)view.findViewById(R.id.buy_contact);
                   buycon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + tel));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    context.startActivity(intent);
                }
            });

        }

        public void setType(String type) {

        }

        public void setImg1(final Context ctx, final String img) {
            final ImageButton image = (ImageButton) view.findViewById(R.id.buy_img1);
            if (img.length() > 1) {
                Picasso.with(ctx).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(ctx).load(img).into(image);
                    }
                });
            }

        }

        public void setImg2(final Context ctx, final String img) {
            final ImageButton image= (ImageButton) view.findViewById(R.id.buy_img2);
            if (img.length() > 1) {
                Picasso.with(ctx).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(ctx).load(img).into(image);
                    }
                });
            }
        }


        public void setImg3(final Context ctx, final String img) {
            final ImageButton image=(ImageButton)view.findViewById(R.id.buy_img3);
            if (img.length() > 1) {
                Picasso.with(ctx).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(ctx).load(img).into(image);
                    }
                });
            }
        }

    }
}
