package com.google.android.ui.register;

import com.google.android.api.Api;
import com.google.android.databinding.FragmentRegisterBinding;
import com.google.base.fragment.BaseFragment;
import com.google.base.utils.RegularUtils;
import com.google.base.utils.ToastUtils;

public class RegisterFragment extends BaseFragment<FragmentRegisterBinding> {
    @Override
    protected void initFragment() {
        binding.register.setOnClickListener(v -> {
            if (!binding.phone.getText().toString().isEmpty()){
                if (RegularUtils.isMobileSimple(binding.phone.getText().toString())){
                    if (binding.password.getText().toString().equals(binding.confirmPassword.getText().toString())){
                        switch (binding.identity.getSelectedItem().toString()){
                            case "学生注册":
                                Api.INSTANCE.register(binding.phone.getText().toString(), binding.password.getText().toString(),(success, errCode, data) -> {
                                    if (data != null){
                                        ToastUtils.showToast(context,data.getMessage());
                                    }else {
                                        ToastUtils.showToast(context,"注册失败");
                                    }
                                });
                                break;
                            case "司机注册":
                                Api.INSTANCE.registerDriver(binding.phone.getText().toString(), binding.password.getText().toString(),(success, errCode, data) -> {
                                    if (data != null){
                                        ToastUtils.showToast(context,data.getMessage());
                                    }else {
                                        ToastUtils.showToast(context,"注册失败");
                                    }
                                });
                                break;
                        }
                    }else {
                        ToastUtils.showToast(context,"两次输入密码不一致");
                    }
                }else {
                    ToastUtils.showToast(context,"请输入正确的手机号");
                }
            }else {
                ToastUtils.showToast(context,"手机号不能为空");
            }
        });
    }
}