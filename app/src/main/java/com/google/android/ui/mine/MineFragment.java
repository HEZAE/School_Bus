package com.google.android.ui.mine;

import android.annotation.SuppressLint;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.AccountActivity;
import com.google.android.GlobalConfig;
import com.google.android.UserInfoActivity;
import com.google.android.api.Api;
import com.google.android.ui.mine.bean.DriverBean;
import com.google.android.ui.mine.bean.UserBean;
import com.google.base.fragment.BaseFragment;
import com.google.android.databinding.FragmentMineBinding;
import com.google.base.mmkv.KVConfigImpl;
import com.google.base.utils.BaseUtils;

import org.litepal.LitePal;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class MineFragment extends BaseFragment<FragmentMineBinding> {
    @SuppressLint("SetTextI18n")
    @Override
    protected void initFragment() {
        initData();

        binding.logout.setOnClickListener(v -> {
            KVConfigImpl.getKVConfigImpl().setInt(GlobalConfig.ID, -1);
            LitePal.deleteAll(UserBean.class);
            LitePal.deleteAll(DriverBean.class);
            BaseUtils.startActivity(context, AccountActivity.class);
            activity.finish();
        });

        binding.userInfo.setOnClickListener(v -> BaseUtils.startActivity(context, UserInfoActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        if (LitePal.findLast(UserBean.class) == null && LitePal.findLast(DriverBean.class) == null) {
            if (KVConfigImpl.getKVConfigImpl().getInt(GlobalConfig.STATUS, -1) == 0) {
                Api.INSTANCE.selectOne(String.valueOf(KVConfigImpl.getKVConfigImpl().getInt(GlobalConfig.ID, -1)), (success, errCode, data) -> {
                    if (data != null) {
                        data.getData().save();
                        activity.runOnUiThread(() -> {
                            binding.info.setText(data.getData().getUsername() + "|" + data.getData().getSex() + "|" + data.getData().getAge());
                            Glide.with(context).load(data.getData().getHead())
                                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 4)))
                                    .into(binding.mineBlur);

                            Glide.with(context).load(data.getData().getHead())
                                    .circleCrop()
                                    .into(binding.mineHead);
                        });
                    }
                });
            } else {
                Api.INSTANCE.selectDriver(String.valueOf(KVConfigImpl.getKVConfigImpl().getInt(GlobalConfig.ID, -1)), (success, errCode, data) -> {
                    if (data != null) {
                        data.getData().save();
                        activity.runOnUiThread(() -> {
                            binding.info.setText(data.getData().getUsername() + "|" + data.getData().getSex() + "|" + data.getData().getAge());

                            Glide.with(context).load(data.getData().getHead())
                                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 4)))
                                    .into(binding.mineBlur);

                            Glide.with(context).load(data.getData().getHead())
                                    .circleCrop()
                                    .into(binding.mineHead);
                        });
                    }
                });
            }
        } else {
            if (KVConfigImpl.getKVConfigImpl().getInt(GlobalConfig.STATUS, -1) == 0) {
                UserBean userBean = LitePal.findLast(UserBean.class);
                binding.info.setText(userBean.getUsername() + "|" + userBean.getSex() + "|" + userBean.getAge());

                Glide.with(context).load(userBean.getHead())
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 4)))
                        .into(binding.mineBlur);

                Glide.with(context).load(userBean.getHead())
                        .circleCrop()
                        .into(binding.mineHead);
            } else {
                DriverBean driverBean = LitePal.findLast(DriverBean.class);
                binding.info.setText(driverBean.getUsername() + "|" + driverBean.getSex() + "|" + driverBean.getAge());

                Glide.with(context).load(driverBean.getHead())
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 4)))
                        .into(binding.mineBlur);

                Glide.with(context).load(driverBean.getHead())
                        .circleCrop()
                        .into(binding.mineHead);
            }
        }
    }
}