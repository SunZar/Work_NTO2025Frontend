package ru.myitschool.work.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.myitschool.work.R;
import ru.myitschool.work.databinding.FragmentInformationBinding;
import ru.myitschool.work.ui.qr.scan.QrScanDestination;
import ru.myitschool.work.ui.qr.scan.QrScanFragment;

public class InformationFragment extends Fragment {
    FragmentInformationBinding binding;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInformationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getContext().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);

        getInformation();

        binding.refresh.setOnClickListener(view1 -> {
            onClickRefresh();
        });

        binding.scan.setOnClickListener(view1 -> {
            onClickScan();
        });

        binding.logout.setOnClickListener(view2 -> {
            onClickLogout();
        });

        //getParentFragmentManager()
        requireActivity().getSupportFragmentManager().setFragmentResultListener(QrScanDestination.REQUEST_KEY, getViewLifecycleOwner(), (requestKey, result) -> {
            String res = "";
            if (result.get(requestKey) != null) {
                res = result.get(requestKey).toString();
            }
            //Toast.makeText(getContext(), res, Toast.LENGTH_LONG).show();



            ResultFragment resFragment = ResultFragment.newInstance(res);
            getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, resFragment).commit();
        });
    }

    private void onClickRefresh() {
        getInformation();
    }

    private void onClickScan() {
        getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new QrScanFragment()).commit();
    }

    private void onClickLogout() {
        sharedPreferences.edit().clear().apply();
        getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new LoginFragment()).commit();
    }

    private void getInformation() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ru.myitschool.work.core.Constants.SERVER_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        StoreAPI storeApi = retrofit.create(StoreAPI.class);

        String login = sharedPreferences.getString(Constants.KEY_LOGIN, "");

        Call<User> call = storeApi.informationUser(login);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        User user = response.body();
                        binding.fullname.setText(user.getName());
                        binding.position.setText(user.getPosition());
                        binding.lastEntry.setText(user.getLastVisit().substring(0, 10) + " " + user.getLastVisit().substring(11, 16));
                        Picasso.get().load(user.getPhoto()).into(binding.photo);
                    } else {
                        takeError();
                    }
                } else if (response.code() == 401) {
                    takeError();
                } else if (response.code() == 400) {
                    takeError();
                } else {
                    takeError();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                takeError();
            }
        });
    }

    private void takeError() {
        //binding.photo.setVisibility(View.GONE);
        binding.fullname.setVisibility(View.GONE);
        binding.lastEntry.setVisibility(View.GONE);
        binding.position.setVisibility(View.GONE);
        //binding.logout.setVisibility(View.GONE);
        binding.scan.setVisibility(View.GONE);

        binding.error.setVisibility(View.VISIBLE);
    }
}
