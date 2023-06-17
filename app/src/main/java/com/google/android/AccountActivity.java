package com.google.android;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.databinding.ActivityAccountBinding;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.ui.login.LoginFragment;
import com.google.android.ui.register.RegisterFragment;
import com.google.base.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class AccountActivity extends BaseActivity<ActivityAccountBinding> {
    private final String[] titles = new String[]{"登陆","注册"};
    private final List<Fragment> fragmentList = new ArrayList<>();
    @Override
    protected void initActivity() {
        fragmentList.add(new LoginFragment());
        fragmentList.add(new RegisterFragment());

        FragmentStateAdapter adapter = new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getItemCount() {
                return fragmentList.size();
            }
        };

        binding.viewPager.setAdapter(adapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, false,false,(tab, position) -> {
            tab.setText(titles[position]);
        }).attach();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        fragmentList.clear();
    }
}