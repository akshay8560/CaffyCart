package com.example.caffycart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import soup.neumorphism.NeumorphButton;
import soup.neumorphism.NeumorphFloatingActionButton;

public class LoginActivity extends AppCompatActivity {


    EditText lgnEmail, lgnPassword;
    NeumorphFloatingActionButton lgnGoogle, lgnFacebook, lgnGithub;
    NeumorphButton lgnLogin, lgnMoveToSignUpPage;
    TextView lgnForgetPassword;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 367;
    CallbackManager callbackManager;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, OrderCreatingActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_activity);

        // This will remove the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        lgnEmail = findViewById(R.id.enterLoginEmail);
        lgnPassword = findViewById(R.id.enterLoginPassword);
        lgnGoogle = findViewById(R.id.loginWithGoogleBtn);
        lgnFacebook = findViewById(R.id.loginWithFacebookBtn);
        lgnGithub = findViewById(R.id.loginWithGithubBtn);
        lgnLogin = findViewById(R.id.loginBtn);
        lgnMoveToSignUpPage = findViewById(R.id.redirectToSignUpBtn);
        lgnForgetPassword = findViewById(R.id.forgotPasswordButton);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("LoggingIn, Please Wait....");
        progressDialog.setCanceledOnTouchOutside(false);

        // Moving to SignUp Page after clicking "Does not have an Account? SignUp"
        lgnMoveToSignUpPage.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });

        // Moving to the order creating activity
        lgnLogin.setOnClickListener(view -> {

            String strLgnEmail = lgnEmail.getText().toString();
            String strLgnPassword = lgnPassword.getText().toString();

            // checking the email
            if (TextUtils.isEmpty(strLgnEmail)) {
                lgnEmail.setError("Invalid Email!");
                lgnEmail.requestFocus();
                return;
            }

            //checking the password
            if (TextUtils.isEmpty(strLgnPassword)) {
                lgnPassword.setError("Invalid Password!");
                lgnPassword.requestFocus();
                return;
            }

            progressDialog.setMessage("LoggingIn...");
            progressDialog.show();

            // Authenticating the User
            firebaseAuth.signInWithEmailAndPassword(strLgnEmail, strLgnPassword).addOnSuccessListener(authResult -> {
                progressDialog.dismiss();
                //going to order create activity
                Intent intent = new Intent(LoginActivity.this, OrderCreatingActivity.class);
                Toast.makeText(LoginActivity.this, "Successfully LoggedIn", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Login Failed! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });

        // SigningIn with Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.myDefault_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        lgnGoogle.setOnClickListener(view -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        // SigningIn with Facebook
        lgnFacebook.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, fbAuth.class);
            startActivity(intent);
            finish();
        });

        // SigningIn with GitHub
        lgnGithub.setOnClickListener(view -> {
            String emailForGitHub = lgnEmail.getText().toString();
            if (TextUtils.isEmpty(emailForGitHub)) {
                lgnEmail.setError("Invalid Email!");
                lgnEmail.requestFocus();
            } else {
                OAuthProvider.Builder provider = OAuthProvider.newBuilder("github.com");
                provider.addCustomParameter("login", emailForGitHub);

                List<String> scopes =
                        new ArrayList<String>() {
                            {
                                add("user:email");
                            }
                        };
                provider.setScopes(scopes);

                Task<AuthResult> pendingResultTask = firebaseAuth.getPendingAuthResult();
                if (pendingResultTask != null) {
                    // There's something already here! Finish the sign-in for your user.
                    pendingResultTask
                            .addOnSuccessListener(
                                    authResult -> {
                                    })
                            .addOnFailureListener(
                                    e -> Toast.makeText(LoginActivity.this, "signIn Failed!! " + e.getMessage(), Toast.LENGTH_SHORT).show());
                } else {
                    firebaseAuth
                            .startActivityForSignInWithProvider(/* activity= */ LoginActivity.this, provider.build())
                            .addOnSuccessListener(
                                    authResult -> {
                                        Intent intent = new Intent(LoginActivity.this, OrderCreatingActivity.class);
                                        Toast.makeText(LoginActivity.this, "LoggedIn with Github", Toast.LENGTH_SHORT).show();
                                        startActivity(intent);
                                        finish();
                                    })
                            .addOnFailureListener(
                                    e -> Toast.makeText(LoginActivity.this, "signIn Failed!! " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }
        });

        // adding forget password functionality
        lgnForgetPassword.setOnClickListener(view -> {
            String emailForForgetPassword = lgnEmail.getText().toString();
            if (TextUtils.isEmpty(emailForForgetPassword)) {
                lgnEmail.setError("Invalid Email!");
                lgnEmail.requestFocus();
            } else {
                firebaseAuth.sendPasswordResetEmail(emailForForgetPassword).addOnSuccessListener(unused -> Toast.makeText(LoginActivity.this, "Reset Link has been sent to your email account.", Toast.LENGTH_LONG).show())
                        .addOnFailureListener(e -> Toast.makeText(LoginActivity.this, "Error! Reset Link failed to send." + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(this, "SignIn with Google Successful", Toast.LENGTH_SHORT).show();
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "Google Auth Failed with " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Intent intent = new Intent(LoginActivity.this, OrderCreatingActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "SignIn Failed!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}