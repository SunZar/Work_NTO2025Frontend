package ru.myitschool.work.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.myitschool.work.R;
import ru.myitschool.work.databinding.FragmentResultBinding;

public class ResultFragment extends Fragment {
    FragmentResultBinding binding;
    SharedPreferences sharedPreferences;
    Door door;

    public static ResultFragment newInstance(String data) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_RESULT, data);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentResultBinding.inflate(inflater, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getContext().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);

        if (getArguments() != null) {
            door = new Door(getArguments().getString(Constants.KEY_RESULT, ""));
        } else {
            door = new Door("");
        }
        if (Objects.equals(door.getCode(), "")) {
            binding.result.setText(getText(R.string.cancelled));
        } else {
            patchDoor();
        }

        binding.close.setOnClickListener(view1 -> {
            onClickClose();
        });
    }

    private void onClickClose() {
        getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new InformationFragment()).commit();
    }

    private void patchDoor() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ru.myitschool.work.core.Constants.SERVER_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        StoreAPI storeApi = retrofit.create(StoreAPI.class);

        String login = sharedPreferences.getString(Constants.KEY_LOGIN, "");

        Call<Void> call = storeApi.openDoor(login, door);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    binding.result.setText(getText(R.string.success));
                } else if (response.code() == 401) {
                    binding.result.setText(getText(R.string.wrong));
                } else if (response.code() == 400) {
                    binding.result.setText(getText(R.string.wrong));
                } else {
                    binding.result.setText(getText(R.string.wrong));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                binding.result.setText(getText(R.string.wrong));
            }
        });
    }
}
