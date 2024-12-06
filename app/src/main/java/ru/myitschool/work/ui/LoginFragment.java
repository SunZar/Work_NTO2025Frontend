package ru.myitschool.work.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.myitschool.work.R;
import ru.myitschool.work.core.Constants;
import ru.myitschool.work.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //binding.log.setText("created");
        sharedPreferences = getContext().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        //sharedPreferences.edit().putString(ru.myitschool.work.ui.Constants.KEY_LOGIN, "5user").apply();
        binding.login.setEnabled(false);
        binding.username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.username.getText().length() >= 3 && binding.username.getText().toString().matches("[0-9a-zA-Z]+") && (binding.username.getText().toString().charAt(0) + "").matches("[0-9]+")) {
                    binding.login.setEnabled(true);
                    //binding.log.setText("changed true");
                } else {
                    binding.login.setEnabled(false);
                    //binding.log.setText("changed false");
                }
                //Log.d("tagg", "changed");
                if (binding.error.getVisibility() == View.VISIBLE) {
                    binding.error.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.login.setOnClickListener(view1 -> {
            if (binding.login.isEnabled()) {
                if (binding.error.getVisibility() == View.VISIBLE) {
                    binding.error.setVisibility(View.GONE);
                    //binding.log.setText("click enabled");
                }
                //binding.log.setText("click");
                onClickLogin();
            }
        });
    }

    private void onClickLogin() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        StoreAPI storeApi = retrofit.create(StoreAPI.class);

        String login = binding.username.getText().toString();

        Call<Boolean> call = storeApi.authenticateUser(login);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body()) {
                        sharedPreferences.edit().putString(ru.myitschool.work.ui.Constants.KEY_LOGIN, binding.username.getText().toString()).apply();
                        getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new InformationFragment()).commit();
                    } else {
                        binding.error.setVisibility(View.VISIBLE);
                    }
                } else if (response.code() == 401) {
                    binding.error.setVisibility(View.VISIBLE);
                } else if (response.code() == 400) {
                    binding.error.setVisibility(View.VISIBLE);
                } else {
                    binding.error.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                binding.error.setVisibility(View.VISIBLE);
            }
        });
    }
}
