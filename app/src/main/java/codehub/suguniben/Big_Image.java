package codehub.suguniben;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class Big_Image extends AppCompatActivity {
    ImageView image;
private String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.big__image);

        img= getIntent().getExtras().getString("img");

        image=(ImageView)findViewById(R.id.bigimg_img);
        if(img!=null&&img!="default"){
            Picasso.with(getApplicationContext()).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(getApplicationContext()).load(img).into(image);
                }
            });
        }
    }
}
