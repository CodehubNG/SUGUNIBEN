package codehub.suguniben;


import android.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class F_BUY_AND_SELL extends Fragment {
    FireClass fireClass;
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
   // FloatingActionButton newSale;
    private DatabaseReference deviceDatabase;

    public F_BUY_AND_SELL() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.f__buy__and__sell, container, false);
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }




    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        fireClass=new FireClass();
        //String UNI=fireClass.university();
        deviceDatabase = FirebaseDatabase.getInstance().getReference().child(fireClass.university()).child("devices");
        deviceDatabase.keepSynced(true);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.sell_RecylerVieww);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        TextView sell = (TextView) getActivity().findViewById(R.id.sell_main_garget);
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(
                        getContext(),POST_SELL_ITEMS.class));
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        CircleImageView search;
        final EditText name = (EditText) getActivity().findViewById(R.id.buy_sell_search_name);
        search = (CircleImageView) getActivity().findViewById(R.id.buy_sell_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load(name.getText().toString());
            }
        });
    }

    private void load(String SearchString) {
        Query query;

        if (SearchString != null && SearchString != "" && SearchString.length() < 1) {
            query = deviceDatabase.orderByChild("name");
        } else {
            query = deviceDatabase.orderByChild("name").startAt(SearchString);//.endAt(SearchString+"\uf8ff");
        }
        FirebaseRecyclerAdapter<SALES, F_BUY_AND_SELL.Sell_Holder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<SALES, F_BUY_AND_SELL.Sell_Holder>(
                SALES.class,
                R.layout.template_buy_sell_and_accomodation,
                F_BUY_AND_SELL.Sell_Holder.class,
                query

        ) {
            @Override
            protected void populateViewHolder(F_BUY_AND_SELL.Sell_Holder viewHolder, final SALES model, int position) {
                try {     //  Toast.makeText(getContext(), deviceDatabase.getRef().toString(),Toast.LENGTH_SHORT).show();
                    viewHolder.setName(model.getName());
                    viewHolder.setAmount(getContext().getApplicationContext(), model.getAmount());
                viewHolder.setDesc(model.getDesc());
                    viewHolder.sharebtn(getContext().getApplicationContext(), model.getName(), model.getDesc());
                    viewHolder.contactAutor(getContext().getApplicationContext(), getActivity(), model.getTel());
                    viewHolder.setTime(model.getTime());
                    Toast.makeText(getContext().getApplicationContext(), model.getTime(), Toast.LENGTH_LONG).show();
                    if (model.getImg1().toString() != "") {
                        viewHolder.setImg1(getContext(), model.getImg1());
                        Toast.makeText(getContext(), model.getImg1(), Toast.LENGTH_SHORT).show();
                    }
                    if (model.getImg2() != "") {
                        Toast.makeText(getContext(), model.getImg2(), Toast.LENGTH_SHORT).show();
                        viewHolder.setImg2(getContext(), model.getImg2());
                    }
                    if (model.getImg3().toString() != "") {
                        Toast.makeText(getContext(), model.getImg3(), Toast.LENGTH_SHORT).show();
                        viewHolder.setImg3(getContext(), model.getImg3());
                    }


                } catch (Exception ex) {

                }
            }
        };
        try {
            recyclerView.setAdapter(firebaseRecyclerAdapter);
        } catch (Exception e) {
            Toast.makeText(getContext().getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }

    }
    private static class Sell_Holder extends RecyclerView.ViewHolder {
        View view;
        FireClass fireClass;
        ImageView image1, image2, image3;
        Context context;
        public Sell_Holder(View itemView) {
            super(itemView);
            view = itemView;
            context=itemView.getContext();
            fireClass=new FireClass();
            image1 = (ImageView) view.findViewById(R.id.buy_img1);
            image2 = (ImageView) view.findViewById(R.id.buy_img2);
            image3 = (ImageView) view.findViewById(R.id.buy_img3);
        }

        public void setName(String name) {
            TextView titile = (TextView) view.findViewById(R.id.buy_title);
            titile.setText(name);
        }

        public void setAmount(Context ctx, String Amount) {
            TextView amount = (TextView) view.findViewById(R.id.buy_amount);
            amount.setText(Amount);
        }

        public void setDesc(String desc) {
TextView Desc=(TextView)view.findViewById(R.id.sell_desc);
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
                    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    context.startActivity(intent);
                }
            });

        }


        public void setType(String type) {

        }

        public void contactAutor(final Context ctx, final Activity atv, final String number) {
            ImageButton btn_contact = (ImageButton) view.findViewById(R.id.buy_contact);

            btn_contact.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("MissingPermission")
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                 /*  post_image.setDrawingCacheEnabled(true);
                    Drawable drawable = post_image.getDrawable();
                    if (drawable != null && !drawable.equals(null)) {
                        Bitmap bitmap1 = ((BitmapDrawable) drawable).getBitmap();
                        String path = MediaStore.Images.Media.insertImage(ctx.getContentResolver(), bitmap1, "img", null);
                        final Uri uri = Uri.parse(path);
                        intent.putExtra(Intent.EXTRA_STREAM, uri);
                    }*/
                    if (number != "" && number != null && number.length() > 10) {

                        intent.putExtra(Intent.EXTRA_TEXT, number);
                    }
                    intent.setType("image/*");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    if (fireClass.securityCheck(android.Manifest.permission.CALL_PHONE, atv, ctx))
                        ctx.startActivity(intent);
                }
            });

        }

        public void sharebtn(final Context ctx, final String title, final String desc) {
            ImageButton btn_share = (ImageButton) view.findViewById(R.id.buy_share);
            image1.setDrawingCacheEnabled(true);
            Drawable drawable1 = image1.getDrawable();
            Bitmap bitmap1 = ((BitmapDrawable) drawable1).getBitmap();
            String path1 = MediaStore.Images.Media.insertImage(ctx.getContentResolver(), bitmap1, "img", null);
            final Uri uri1 = Uri.parse(path1);


            Drawable drawable2 = image2.getDrawable();
            Bitmap bitmap2 = ((BitmapDrawable) drawable2).getBitmap();
            String path2 = MediaStore.Images.Media.insertImage(ctx.getContentResolver(), bitmap2, "img", null);
            final Uri uri2 = Uri.parse(path2);

            Drawable drawable3 = image3.getDrawable();
            Bitmap bitmap3 = ((BitmapDrawable) drawable3).getBitmap();
            String path3 = MediaStore.Images.Media.insertImage(ctx.getContentResolver(), bitmap3, "img", null);
            final Uri uri3 = Uri.parse(path3);

            String msg = title + "\n \n" + desc;
            btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String msg = title + "\n \n " + desc;
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, msg);
                    intent.putExtra(Intent.EXTRA_ORIGINATING_URI, uri1);
                    intent.putExtra(Intent.EXTRA_ORIGINATING_URI, uri2);
                    intent.putExtra(Intent.EXTRA_ORIGINATING_URI, uri3);
                    intent.setType("text/image");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(intent);
                }
            });


        }

        public void setImg1(final Context ctx, final String img1) {
            final ImageView image1 = (ImageView) view.findViewById(R.id.buy_img1);
            if (img1 != null && img1.length() > 1) {
                Picasso.with(ctx).load(img1).networkPolicy(NetworkPolicy.OFFLINE).into(image1, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(ctx).load(img1).into(image1);
                    }
                });
            }
        }

        public void setImg2(final Context ctx, final String img2) {
            final ImageView image2 = (ImageView) view.findViewById(R.id.buy_img2);
            if (img2.length() > 1) {
                Picasso.with(ctx).load(img2).networkPolicy(NetworkPolicy.OFFLINE).into(image2, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(ctx).load(img2).into(image2);
                    }
                });
            }
        }


        public void setImg3(final Context ctx, final String img) {
            final ImageView image3 = (ImageView) view.findViewById(R.id.buy_img3);
            if (img.length() > 1) {
                Picasso.with(ctx).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(image3, new Callback() {
                    @Override
                    public void onSuccess() {

                    }
                    @Override
                    public void onError() {
                        Picasso.with(ctx).load(img).into(image3);
                    }
                });
            }
        }


    }
}
