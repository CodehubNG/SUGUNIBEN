package codehub.suguniben;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 818;
    private static final String TAG = "LoginActivity";
    public GoogleSignInClient mGoogleSignInClient;
    ProgressDialog progressdia;
    //GoogleSignInOptions gso;
    private EditText email, password;
    private Button login, newAcc;
    private SignInButton signInButton;
    // private FirebaseAuth auth;
    private DatabaseReference userDatabase;
    private FireClass fireClass;
    // GoogleSignInResult g
    private GoogleApiClient googleApiClient;
    private FirebaseAuth mAuth;
    private AlertDialog.Builder alertdialog;



    public Activity getActivity() {
        return getActivity();
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.xml_login_email);
        password = (EditText) findViewById(R.id.xml_login_password);
        login = (Button) findViewById(R.id.xml_login_loginBtn);
        newAcc = (Button) findViewById(R.id.xml_log_needAccount);
        signInButton = (SignInButton) findViewById(R.id.xml_googleSignIn);
        alertdialog = new AlertDialog.Builder(this);
        alertdialog.setCancelable(true);
        progressdia = new ProgressDialog(this);
        progressdia.setCancelable(false);
        fireClass=new FireClass();


        userDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startlogin();
            }
        });
        mAuth = FirebaseAuth.getInstance();

        newAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newacc = new Intent(LoginActivity.this, RegisterActivity.class);
                newacc.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(newacc);
            }
        });
        // Configure Google Sign In

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // mGoogleSignInClient=new GoogleSignInClient()
               signIn();
               // Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                // Intent signInIntent =Auth.GoogleSignInApi.getSignInIntent(googleApiClient);

              //  startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                         //   updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                       //     Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                          //  updateUI(null);
                        }

                        // ...
                    }
                });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
     //   updateUI(currentUser);
    }

      private void startlogin() {
        String emai = email.getText().toString().trim();
        String passw = password.getText().toString().trim();
        if (!TextUtils.isEmpty(emai) && !TextUtils.isEmpty(passw)) {
            email.setEnabled(false);
            email.setEnabled(true);
            password.setEnabled(false);
            password.setEnabled(true);
            progressdia.setMessage("Logging In ...");
            progressdia.show();
            mAuth.signInWithEmailAndPassword(emai, passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                       FireClass fireClass=new FireClass();
                        if (fireClass.checkifExist(getApplicationContext())) ;
                        {
                            progressdia.dismiss();
                            finish();
                        }
                        // finish();
                    } else {
                        progressdia.dismiss();
                        alertdialog.setMessage(task.getException().getMessage());
                        alertdialog.show();
                    }
                }
            });

        }
    }



}