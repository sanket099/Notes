package com.example.notes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class NotesList extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    NotesAdapter adapter;
    ListView lvlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        lvlist = findViewById(R.id.lvlist);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        lvlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(NotesList.this,com.example.notes.NotesInfo.class);
                intent.putExtra("index", i);
                startActivityForResult(intent,1);
            }
        });



        String whereClause = "userEmail='"+ApplicationClass.user.getEmail()+"'";

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();

        queryBuilder.setWhereClause(whereClause);
        queryBuilder.setGroupBy("heading");



        showProgress(true);
        Backendless.Persistence.of(Data.class).find(queryBuilder, new AsyncCallback<List<Data>>() {
            @Override
            public void handleResponse(List<Data> response) {
                ApplicationClass.datas = response;
                adapter = new NotesAdapter(NotesList.this,ApplicationClass.datas);
                lvlist.setAdapter(adapter);
                showProgress(false);

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                showProgress(false);
                Toast.makeText(NotesList.this,"error"+fault.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            adapter.notifyDataSetChanged();
        }
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


