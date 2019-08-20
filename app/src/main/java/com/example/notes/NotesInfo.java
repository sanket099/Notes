package com.example.notes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class NotesInfo extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    ImageView ivedit,ivdelete,share;
    Button btnsubmit;
    TextView tvtext,tvname;
    EditText ettext,ethead;
    boolean edit = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_info);

        mLoginFormView = findViewById(R.id.login_form);

        ivedit = findViewById(R.id.ivedit);
        ivdelete = findViewById(R.id.ivdelete);

        tvtext = findViewById(R.id.tvtext);
        tvname = findViewById(R.id.tvname);
        btnsubmit = findViewById(R.id.btnsubmit);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
        ettext = findViewById(R.id.ettext);
        ethead = findViewById(R.id.ethead);
        share = findViewById(R.id.share);



        ethead.setVisibility(View.GONE);

        btnsubmit.setVisibility(View.GONE);
        ettext.setVisibility(View.GONE);

        final int index = getIntent().getIntExtra("index",0);
        ethead.setText(ApplicationClass.datas.get(index).getHeading());
        ettext.setText(ApplicationClass.datas.get(index).getTextbody());

        tvtext.setText(ApplicationClass.datas.get(index).getTextbody());
        tvname.setText(ApplicationClass.datas.get(index).getHeading());


        ivedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit = !edit;
                if (edit){
                    ettext.setVisibility(View.VISIBLE);
                    ethead.setVisibility(View.VISIBLE);

                    btnsubmit.setVisibility(View.VISIBLE);



                }
                else{
                    ethead.setVisibility(View.GONE);

                    btnsubmit.setVisibility(View.GONE);
                    ettext.setVisibility(View.GONE);


                }
            }
        });


        ivdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogue = new AlertDialog.Builder(NotesInfo.this);
                dialogue.setMessage("Are you sure");
                dialogue.setPositiveButton("Yes,Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showProgress(true);
                        Backendless.Persistence.of(Data.class).remove(ApplicationClass.datas.get(index), new AsyncCallback<Long>() {
                            @Override
                            public void handleResponse(Long response) {
                                ApplicationClass.datas.remove(index);
                                Toast.makeText(NotesInfo.this,"Contact Deleted",Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                showProgress(false);
                                NotesInfo.this.finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {

                                Toast.makeText(NotesInfo.this,"error "+ fault.getMessage(),Toast.LENGTH_SHORT).show();
                                showProgress(false);


                            }
                        });

                    }
                });
                dialogue.setNegativeButton("No,Go Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialogue.show();
            }
        });


        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ethead.getText().toString().isEmpty()|| ettext.getText().toString().isEmpty()){
                    Toast.makeText(NotesInfo.this,"all fields are required",Toast.LENGTH_SHORT).show();
                }
                else{
                    ApplicationClass.datas.get(index).setHeading(ethead.getText().toString().trim());

                    ApplicationClass.datas.get(index).setTextbody(ettext.getText().toString().trim());

                    showProgress(true);
                    Backendless.Persistence.save(ApplicationClass.datas.get(index), new AsyncCallback<Data>() {
                        @Override
                        public void handleResponse(Data response) {
                            tvname.setText(ApplicationClass.datas.get(index).getHeading());
                            tvtext.setText(ApplicationClass.datas.get(index).getTextbody());
                            showProgress(false);




                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(NotesInfo.this,"error "+ fault.getMessage(),Toast.LENGTH_SHORT).show();
                            showProgress(false);


                        }
                    });

                }
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = tvtext.getText().toString();


                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share Note Text using"));

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
