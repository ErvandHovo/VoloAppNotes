package com.example.ervand.voloappnotes.view.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.ervand.voloappnotes.R;
import com.example.ervand.voloappnotes.model.User;
import com.example.ervand.voloappnotes.view.fragments.LoginFragment;

public class MainActivity extends AppCompatActivity {

    public static User user;
    private static final String USER_KEY = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.mainContainer,
                LoginFragment.newInstance(), "LoginFragment")
                .addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 1) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }
}
