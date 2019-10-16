package com.alpha.team.eventhub.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alpha.team.eventhub.R;
import com.alpha.team.eventhub.entities.User;
import com.alpha.team.eventhub.network.CheckInternet;
import com.alpha.team.eventhub.network.RetrofitClientInstance;
import com.alpha.team.eventhub.network.RetrofitService;
import com.alpha.team.eventhub.sharedprefrence.ServicePreferences;
import com.alpha.team.eventhub.sharedprefrence.UserPreference;
import com.alpha.team.eventhub.utils.GeneralHelpers;
import com.alpha.team.eventhub.utils.Validation;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by clasence on 03,December,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 * Activity handles event user login
 */
public class LoginActivity extends AppCompatActivity {
    private Validation validation;
    private UserPreference userPreference;
    private ServicePreferences servicePreferences;


    private EditText etUsername;
    private EditText etPassword;
    private CheckBox checkBox;
    private TextView tvRegister;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        validation = new Validation(getApplicationContext());
        userPreference = new UserPreference(getApplicationContext());
        servicePreferences = new ServicePreferences(getApplicationContext());


        bindViews();
        handleRemeberMe();

    }

    /**
     * Bind all views from foreground to java equivalents
     */
    private void bindViews() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        checkBox = findViewById(R.id.checkBox);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);
        ButtonClickListerner buttonClickListerner = new ButtonClickListerner();
        btnLogin.setOnClickListener(buttonClickListerner);
        tvRegister.setOnClickListener(buttonClickListerner);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
            }
        }
        return true;
    }


    /**
     * Save username and password to sharedPreferences if remember me is checked
     */
    private void handleRemeberMe() {
        if (userPreference.remeberMe()) {
            etUsername.setText(userPreference.getUsername());
            etPassword.setText(userPreference.getPassword());
        }
    }


    /**
     * Class handles button clicks for the main activity
     */
    private class ButtonClickListerner implements View.OnClickListener {
        private ProgressDialog progressDialog;

        public ButtonClickListerner() {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setTitle(getString(R.string.please_wait));
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.btn_login: {
                    checkAndLoginUser();
                    break;
                }
                case R.id.tv_register: {
                    Intent intent = new Intent(getApplicationContext(), RegisterUserActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }


        /**
         * Check validity of inputs and login user
         */
        private void checkAndLoginUser() {
            if (validation.hasText(etUsername) && validation.isPasswordValid(etPassword)) {
                String username = GeneralHelpers.getEditTextText(etUsername);
                String password = GeneralHelpers.getEditTextText(etPassword);

                HashMap<String, Object> map = new HashMap<>();
                map.put("usernameOrEmail", username);
                map.put("password", password);

                if (CheckInternet.isNetworkAvailable(getApplicationContext())) {
                    loginUser(map);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.noint), Toast.LENGTH_SHORT).show();

                }
            }
        }

        /**
         * Login user
         *
         * @param map
         */
        private void loginUser(HashMap<String, Object> map) {
            //create retrofit interface
            progressDialog.show();
            RetrofitService retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            Call<HashMap<String, Object>> call = retrofitService.loginUser(map);
            call.enqueue(new Callback<HashMap<String, Object>>() {
                @Override
                public void onResponse(Call<HashMap<String, Object>> call, Response<HashMap<String, Object>> response) {
                    Log.e("response", response.body().toString());
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        Double code = (Double) response.body().get("status");
                        if (code.equals(200.0)) {
                            Log.e("good", response.body().get("msg").toString());
                            User user = GeneralHelpers.serializeToUser(response.body().get("msg"));
                            if (user != null) {
                                user.setPassword(GeneralHelpers.getEditTextText(etPassword));
                                if (checkBox.isChecked()) {
                                    userPreference.setRememberMe(true);
                                } else {
                                    userPreference.setRememberMe(false);
                                }
                                if (userPreference.insert(user)) {
                                    servicePreferences.insertCatogories(user.getFavouriteCategories());

                                    Toast.makeText(getApplicationContext(), getString(R.string.login_successful), Toast.LENGTH_SHORT).show();

                                    finish();
                                }
                            }
                        } else if (code.equals(201.0)) {
                            etUsername.setError(getString(R.string.username_invalid));
                            etUsername.requestFocus();
                            Toast.makeText(getApplicationContext(), getString(R.string.email_exist), Toast.LENGTH_SHORT).show();

                        } else if (code.equals(203.0)) {
                            etPassword.setError(getString(R.string.password_invlid));
                            etPassword.requestFocus();
                            Toast.makeText(getApplicationContext(), getString(R.string.password_invlid), Toast.LENGTH_SHORT).show();

                        }

                    }

                }

                @Override
                public void onFailure(Call<HashMap<String, Object>> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e("GetCountriesService", t.toString());
                }
            });
        }
    }


}
