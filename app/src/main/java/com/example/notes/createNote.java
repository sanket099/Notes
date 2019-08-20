package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class createNote extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    Button btnnewcontact;

    EditText etheading, ettextbody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        etheading = findViewById(R.id.etheading);
        ettextbody = findViewById(R.id.ettextbody);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        btnnewcontact = findViewById(R.id.btnnewcontact);

        btnnewcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etheading.getText().toString().isEmpty() || ettextbody.getText().toString().isEmpty())
                {
                    Toast.makeText(createNote.this,"all fields required",Toast.LENGTH_SHORT).show();
                }
                else{
                    String heading = etheading.getText().toString().trim();
                    String textbody = ettextbody.getText().toString().trim();


                    Data data = new Data();
                    data.setHeading(heading);

                    data.setTextbody(textbody);

                    data.setUserEmail(ApplicationClass.user.getEmail());

                    showProgress(true);
                    Backendless.Persistence.save(data, new AsyncCallback<Data>() {
                        @Override
                        public void handleResponse(Data response) {

                            Toast.makeText(createNote.this,"success",Toast.LENGTH_SHORT).show();
                            showProgress(false);
                            etheading.setText("");

                            ettextbody.setText("");
                            startActivity(new Intent(createNote.this,com.example.notes.MainActivity.class));

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            showProgress(false);

                            Toast.makeText(createNote.this,"error : "+fault.getMessage(),Toast.LENGTH_SHORT).show();



                        }
                    });
                }
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
