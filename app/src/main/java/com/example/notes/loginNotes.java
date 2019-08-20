package com.example.notes;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserIdStorageFactory;
import com.example.notes.ApplicationClass;
import com.example.notes.R;

public class loginNotes extends AppCompatActivity {


    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    EditText etemail ,etpass;
    Button btnregister, btnlogin;
    TextView tvreset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_notes);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        etemail = findViewById(R.id.etemail);
        etpass = findViewById(R.id.etpass);
        btnregister = findViewById(R.id.btnregister);
        btnlogin = findViewById(R.id.btnlogin);

        tvreset = findViewById(R.id.tvreset);

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginNotes.this,com.example.notes.Main2Activity.class));

            }

        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etemail.getText().toString().isEmpty() || etpass.getText().toString().isEmpty()){
                    Toast.makeText(loginNotes.this,"all fields are required",Toast.LENGTH_SHORT).show();

                }
                else {
                    String email = etemail.getText().toString().trim();
                    String password =etpass.getText().toString().trim();
                    showProgress(true);

                    Backendless.UserService.login(email, password, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {

                            ApplicationClass.user = response;
                            showProgress(false);

                            Toast.makeText(loginNotes.this,"Success",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(loginNotes.this,com.example.notes.MainActivity.class));
                            loginNotes.this.finish();

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(loginNotes.this,"error : "+fault.getMessage(),Toast.LENGTH_SHORT).show();
                            showProgress(false);


                        }
                    }, true);
                }
            }
        });

        tvreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = etemail.getText().toString().trim();
                showProgress(true);
                Backendless.UserService.restorePassword(email, new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void response) {
                        Toast.makeText(loginNotes.this,"success",Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }


                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(loginNotes.this,"error"+fault.getMessage(),Toast.LENGTH_SHORT).show();
                        showProgress(false);


                    }
                });

            }
        });


        Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
            @Override
            public void handleResponse(Boolean response) {
                if (response){
                    String userObjectid = UserIdStorageFactory.instance().getStorage().get();
                    Backendless.Data.of(BackendlessUser.class).findById(userObjectid, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {

                            ApplicationClass.user = response;
                            showProgress(false);

                            startActivity(new Intent(loginNotes.this,com.example.notes.MainActivity.class));
                            loginNotes.this.finish();


                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(loginNotes.this,"error"+fault.getMessage(),Toast.LENGTH_SHORT).show();
                            showProgress(false);


                        }
                    });

                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(loginNotes.this,"error"+fault.getMessage(),Toast.LENGTH_SHORT).show();
                showProgress(false);

            }
        });








    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


}
