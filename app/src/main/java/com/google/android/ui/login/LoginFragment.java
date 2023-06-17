package com.google.android.ui.login;

import com.google.android.GlobalConfig;
import com.google.android.MainActivity;
import com.google.android.api.Api;
import com.google.android.databinding.FragmentLoginBinding;
import com.google.base.fragment.BaseFragment;
import com.google.base.mmkv.KVConfigImpl;
import com.google.base.utils.BaseUtils;
import com.google.base.utils.ToastUtils;

public class LoginFragment extends BaseFragment<FragmentLoginBinding> {
    @Override
    protected void initFragment() {
        //登录按钮点击事件
        binding.login.setOnClickListener(v -> {
            //判断是否输入用户名以及密码
            if (binding.phone.getText() != null && binding.phone.getText().toString().isEmpty()) {
                ToastUtils.showToast(context, "请输入用户名");
            } else if (binding.password.getText() != null && binding.password.getText().toString().isEmpty()) {
                ToastUtils.showToast(context, "请输入密码");
            } else {
                //判断是否选中管理员登录
                if (binding.isDriver.isChecked()){
                    //binding.isDriver.isChecked() = true 调用司机登录接口
                    Api.INSTANCE.driverLogin(binding.phone.getText().toString(), binding.password.getText().toString(), (success, errCode, data) -> {
                        if (data != null) {
                            if (data.getCode() == 200){
                                //MMKV储存部分用户关键信息
                                KVConfigImpl.getKVConfigImpl().setInt(GlobalConfig.ID, data.getData());
                                KVConfigImpl.getKVConfigImpl().setInt(GlobalConfig.STATUS, 1);
                                BaseUtils.startActivity(context, MainActivity.class);
                                Api.INSTANCE.addInfod(String.valueOf(data.getData()), (success1, errCode1, data1) -> {
                                    if (data1 != null){
                                        ToastUtils.showToast(context,data.getMessage());
                                    }
                                });
                            }else {
                                ToastUtils.showToast(context,data.getMessage());
                            }
                        }
                    });
                }else {
                    //binding.isDriver.isChecked() = false 调用用户登录接口
                    Api.INSTANCE.login(binding.phone.getText().toString(), binding.password.getText().toString(), (success, errCode, data) -> {
                        if (data != null) {
                            if (data.getCode() == 200){
                                //MMKV储存部分用户关键信息
                                KVConfigImpl.getKVConfigImpl().setInt(GlobalConfig.ID, data.getData());
                                KVConfigImpl.getKVConfigImpl().setInt(GlobalConfig.STATUS, 0);
                                BaseUtils.startActivity(context, MainActivity.class);
                                Api.INSTANCE.addInfo(String.valueOf(data.getData()), (success1, errCode1, data1) -> {
                                    if (data1 != null){
                                        ToastUtils.showToast(context,data.getMessage());
                                    }
                                });
                            }else {
                                ToastUtils.showToast(context,data.getMessage());
                            }
                        }
                    });
                }
            }
        });
    }
}