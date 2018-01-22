package codehub.suguniben;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText firstname, password, confirmPassword, email;
    private Button logIn, signUp, googleSignIn;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private DatabaseReference database;
    private FireClass fireClass;
    private AlertDialog.Builder alertDialog;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fireClass=new FireClass();
        firstname = (EditText) findViewById(R.id.xml_firstName);
        password = (EditText) findViewById(R.id.xml_password);
        confirmPassword = (EditText) findViewById(R.id.xml_confirmPassword);
        email = (EditText) findViewById(R.id.xml_email);
        logIn = (Button) findViewById(R.id.xml_login);
        signUp = (Button) findViewById(R.id.xml_Signup);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        alertDialog=new AlertDialog.Builder(this);
        alertDialog.setCancelable(true);

        database = FirebaseDatabase.getInstance().getReference().child("users");
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(password.getText().toString().equals(confirmPassword.getText().toString())) {
                   if(password.getText().toString().length()<=5){
                       alertDialog.setMessage("Password too short \n \n \t password mush be up too six characters");
                       alertDialog.show();
                   }else {
                       startRegister();
                   }
                }else {
                    alertDialog.setMessage("Password don't match");
                    alertDialog.show();
                }
            }
        });
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent mainIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
            }
        });

        auth = FirebaseAuth.getInstance();

    }

    private void startRegister() {
        final String fname = firstname.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String confpass = confirmPassword.getText().toString().trim();
        String emai = email.getText().toString().trim();

        if (!TextUtils.isEmpty(fname) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(confpass) && !TextUtils.isEmpty(emai)) {

                  progressDialog.setMessage("Signing Up ...");
                  progressDialog.show();
                  auth.createUserWithEmailAndPassword(emai, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull final Task<AuthResult> task) {
                          if (task.isSuccessful()) {
                              String user_id = auth.getCurrentUser().getUid();
                              DatabaseReference current_userId = database.child(user_id);
                              current_userId.child("uid").setValue(user_id);
                              current_userId.child("university").setValue(fireClass.university());
                              current_userId.child("fname").setValue(fname);
                              current_userId.child("image").setValue("");
                              current_userId.child("access").setValue("1");

                              if (fireClass.checkifExist(getApplicationContext())) {
                                  progressDialog.dismiss();
                                  finish();
                              }
                          }
                          else {
                              progressDialog.dismiss();
                              alertDialog.setMessage(task.getException().getMessage());
                          }
                      }
                  });

        }
        }
    }

