package mycompany.com.deneme6;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignIn;
    private Button buttonRegister;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonRegister.setOnClickListener(this);
        buttonSignIn.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);

    }




    @Override
    public void onClick(View view) {
        if (view == buttonRegister){
            registerUser();
        }
        if(view == buttonSignIn){
            //will open Login activity
            userLogin();
        }
    }



    private void registerUser(){

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            //emailis empty
            Toast.makeText(this,"Please enter your e-mail address...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this,"Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Registering User....");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"Registered successfully...", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"Could not Register. Please try again...", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                });

    }


    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            //emailis empty
            Toast.makeText(this,"Please enter your e-mail address...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this,"Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Signing in....");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {


                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    finish();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));                        }
                else
                {
                    Toast.makeText(MainActivity.this,"Could not Sign In. Please try again...", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }
            });


        }
}



        /*
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onStateChanged:Signed_in:" + user.getUid());
                    toastMessage("Successfully signed in with;" + user.getEmail());
                } else {
                    Log.d(TAG, "onStateChanged:Signed_out:");
                    toastMessage("Successfully signed out." );
                }
            }
        };

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password =editTextPassword.getText().toString();
                if(!email.equals("")&& !password.equals("")){
                    firebaseAuth.signInWithEmailAndPassword(email, password);
                }else{
                    toastMessage("You didn't fill all the fields.");
                }

            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                toastMessage("Signing out...");
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null){
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }

    }

    private void toastMessage(String message){

        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }*/