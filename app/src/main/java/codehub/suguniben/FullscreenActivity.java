package codehub.suguniben;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class FullscreenActivity extends AppCompatActivity {
 FireClass   fireClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
        fireClass = new FireClass();
        if (fireClass.checkifExist(getApplicationContext())) ;
        finish();


    }


}
