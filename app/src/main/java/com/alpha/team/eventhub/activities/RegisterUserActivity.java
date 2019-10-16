package com.alpha.team.eventhub.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alpha.team.eventhub.R;
import com.alpha.team.eventhub.entities.User;
import com.alpha.team.eventhub.network.CheckInternet;
import com.alpha.team.eventhub.network.RetrofitClientInstance;
import com.alpha.team.eventhub.network.RetrofitService;
import com.alpha.team.eventhub.sharedprefrence.ServicePreferences;
import com.alpha.team.eventhub.sharedprefrence.UserPreference;
import com.alpha.team.eventhub.utils.CategoryPicker;
import com.alpha.team.eventhub.utils.CountryPicker;
import com.alpha.team.eventhub.utils.GeneralHelpers;
import com.alpha.team.eventhub.utils.Validation;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterUserActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassord;
    private EditText etCity;
    private Button btnRegister;
    private Spinner spCountry;
    private Spinner spCity;
    private Spinner spCategory;

    private Validation validation;
    private UserPreference userPreference;
    private ServicePreferences servicePreferences;
    private CountryPicker countryPicker;
    private CategoryPicker categoryPicker;

    private int countryId;
    private int cityId;
    private String city;
    private ArrayList<Integer> selectedCatIds;
    private ArrayList<String> selectedCatNames;

    private TextView tvCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        validation = new Validation(getApplicationContext());
        userPreference = new UserPreference(getApplicationContext());
        servicePreferences = new ServicePreferences(getApplicationContext());
        selectedCatIds = new ArrayList<>();
        selectedCatNames = new ArrayList<>();


        bindViews();
        populateSpinners();
    }

    public void populateSpinners() {

        countryPicker = new CountryPicker(this, this, spCountry);
        countryPicker.startLoad();

        categoryPicker = new CategoryPicker(this, this, spCategory);
        categoryPicker.startLoad();
    }


    /**
     * Bind all views to  corresponding objects
     */
    private void bindViews() {
        etName = findViewById(R.id.et_name);
        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etPassord = findViewById(R.id.et_password);
        etCity = findViewById(R.id.et_city);

        spCity = findViewById(R.id.sp_categories);
        spCountry = findViewById(R.id.sp_country);
        spCategory = findViewById(R.id.sp_categories);
        btnRegister = findViewById(R.id.btn_register);

        tvCategories = findViewById(R.id.tv_categories);

        spCountry.setOnItemSelectedListener(new SpinnerClickListerner());
        spCategory.setOnItemSelectedListener(new SpinnerClickListerner());
        btnRegister.setOnClickListener(new ButtonClick());
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

    private void updateCatTextview() {
        StringBuilder sb = new StringBuilder();
        for (String cat : selectedCatNames) {
            sb.append(cat);
            sb.append(",");
        }
        tvCategories.setText(sb.toString());
    }

    private class SpinnerClickListerner implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            switch (parent.getId()) {
                case R.id.sp_country: {
                    countryId = countryPicker.getCountryList().get(position).getId();
                    break;
                }
                case R.id.sp_categories: {
                    int categoryId = categoryPicker.getCategoryList().get(position).getCategoryId();
                    String catName = categoryPicker.getCategoryList().get(position).getName();

                    if (!selectedCatIds.contains(categoryId)) {
                        selectedCatIds.add(categoryId);
                        selectedCatNames.add(catName);
                    } else {
                        selectedCatIds.remove(new Integer(categoryId));
                        selectedCatNames.remove(catName);
                    }
                    updateCatTextview();

                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


    /**
     * Class handles the register button click
     */
    private class ButtonClick implements View.OnClickListener {
        ProgressDialog progressDoalog;

        public ButtonClick() {
            progressDoalog = new ProgressDialog(RegisterUserActivity.this);
            progressDoalog.setTitle(getString(R.string.email));

        }

        @Override
        public void onClick(View v) {
            checkAndRegisterUser();
        }


        /**
         * Generate categories string from arraylist
         *
         * @param list
         * @return
         */
        private String generateCategories(ArrayList<Integer> list) {
            StringBuilder sb = new StringBuilder();
            for (int cat : list) {
                sb.append(cat);
                sb.append(",");
            }
            return sb.toString();
        }


        /**
         * Check validity of inputs and register user
         */
        private void checkAndRegisterUser() {
            if (validation.hasText(etName) && validation.hasText(etUsername)
                    && validation.hasText(etCity) && validation.isEmailAddress(etEmail) &&
                    validation.isPasswordValid(etPassord)) {
                String name = GeneralHelpers.getEditTextText(etName);
                String username = GeneralHelpers.getEditTextText(etUsername);
                String city = GeneralHelpers.getEditTextText(etCity);
                String email = GeneralHelpers.getEditTextText(etEmail);
                String password = GeneralHelpers.getEditTextText(etPassord);
                String categories = generateCategories(selectedCatIds);
                servicePreferences.insertCatogories(categories);

                HashMap<String, Object> map = new HashMap<>();
                map.put("name", name);
                map.put("username", username);
                map.put("email", email);
                map.put("password", password);
                map.put("cityName", city);
                map.put("categories", categories);
                map.put("status", 0);
                map.put("countryId", countryId);

                registerUser(map);
                if (CheckInternet.isNetworkAvailable(getApplicationContext())) {
                    registerUser(map);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.noint), Toast.LENGTH_SHORT).show();

                }
            }
        }

        /**
         * Register user
         *
         * @param map
         */
        private void registerUser(HashMap<String, Object> map) {
            //create retrofit interface
            progressDoalog.show();
            RetrofitService retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
            Call<HashMap<String, Object>> call = retrofitService.registerUser(map);
            call.enqueue(new Callback<HashMap<String, Object>>() {
                @Override
                public void onResponse(Call<HashMap<String, Object>> call, Response<HashMap<String, Object>> response) {
                    Log.e("response", response.body().toString());
                    progressDoalog.dismiss();
                    if (response.code() == 200) {
                        Double code = (Double) response.body().get("status");
                        if (code.equals(200.0)) {
                            Log.e("good", response.body().get("msg").toString());
                            User user = GeneralHelpers.serializeToUser(response.body().get("msg"));
                            if (user != null) {
                                user.setPassword(GeneralHelpers.getEditTextText(etPassord));
                                if (userPreference.insert(user)) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.user_registered), Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        } else if (code.equals(201.0)) {
                            etEmail.setError(getString(R.string.email_exist));
                            etEmail.requestFocus();
                            Toast.makeText(getApplicationContext(), getString(R.string.email_exist), Toast.LENGTH_SHORT).show();

                        } else if (code.equals(202.0)) {
                            etUsername.setError(getString(R.string.username_exist));
                            etUsername.requestFocus();
                            Toast.makeText(getApplicationContext(), getString(R.string.username_exist), Toast.LENGTH_SHORT).show();

                        } else if (code.equals(203.0)) {
                            Toast.makeText(getApplicationContext(), getString(R.string.unable_reach_server), Toast.LENGTH_SHORT).show();

                        }

                    }

                }

                @Override
                public void onFailure(Call<HashMap<String, Object>> call, Throwable t) {
                    progressDoalog.dismiss();
                    Log.e("GetCountriesService", t.toString());
                }
            });
        }


    }


}