package com.google.android;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.api.Api;
import com.google.android.api.ApiCar;
import com.google.android.databinding.ActivityUserInfoBinding;
import com.google.android.ui.mine.bean.DriverBean;
import com.google.android.ui.mine.bean.UserBean;
import com.google.android.widget.GlideEngine;
import com.google.base.activity.BaseActivity;
import com.google.base.mmkv.KVConfigImpl;
import com.google.base.utils.LogUtils;
import com.google.base.utils.ToastUtils;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.engine.CompressFileEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.luck.picture.lib.style.PictureSelectorStyle;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;

import top.zibin.luban.Luban;
import top.zibin.luban.OnNewCompressListener;

/**
 * 用户修改信息的Activity
 */
@SuppressLint("SetTextI18n")
public class UserInfoActivity extends BaseActivity<ActivityUserInfoBinding> {

    private final PictureSelectorStyle pictureSelectorStyle = new PictureSelectorStyle();
    private File file;

    @Override
    protected void initActivity() {
        initInfo();

        switch (KVConfigImpl.getKVConfigImpl().getInt(GlobalConfig.STATUS, -1)) {
            case 0:
                binding.myCar.setVisibility(View.GONE);
                break;
            case 1:
                binding.myCar.setVisibility(View.VISIBLE);
                binding.myCar.setOnClickListener(v -> BottomDialog.build()
                        .setMaskColor(0xFF2A9A53)
                        .setCustomView(new OnBindView<>(R.layout.item_car) {
                            @Override
                            public void onBind(BottomDialog dialog, View v) {
                                Button submit = v.findViewById(R.id.submit);
                                Button cancel = v.findViewById(R.id.cancel);
                                TextView textView = v.findViewById(R.id.title);
                                EditText title = v.findViewById(R.id.carTitle);
                                EditText content = v.findViewById(R.id.carContent);
                                DriverBean driverBean = LitePal.findLast(DriverBean.class);
                                if (driverBean.getCar_id().equals("0")){
                                    textView.setText("您还没有车辆，输入车辆信息以绑定");
                                    submit.setText("绑定");
                                    submit.setOnClickListener(view -> ApiCar.INSTANCE.addInfo(String.valueOf(driverBean.getDriver_id()), String.valueOf(System.currentTimeMillis()), title.getText().toString(), content.getText().toString(), (success, errCode, data) -> {
                                        if (data != null) {
                                            data.getData().save();
                                            ToastUtils.showToast(context,"绑定成功");
                                            dialog.dismiss();
                                        } else {
                                            ToastUtils.showToast(context, "请选择一个封面");
                                        }
                                    }));
                                }else {
                                    textView.setText("查看或修改您的车辆信息");
                                    submit.setText("修改");
                                    ApiCar.INSTANCE.getBy(driverBean.getCar_id(), (success, errCode, data) -> {
                                        if (data != null && data.getData() != null){
                                            runOnUiThread(() -> {
                                                title.setText(data.getData().getCar_name());
                                                content.setText(data.getData().getCar_info());
                                            });
                                        }
                                    });

                                    submit.setOnClickListener(view -> ApiCar.INSTANCE.addInfo(String.valueOf(driverBean.getDriver_id()), driverBean.getCar_id(), title.getText().toString(), content.getText().toString(), (success, errCode, data) -> {
                                        if (data != null) {
                                            ToastUtils.showToast(context,"修改成功");
                                            dialog.dismiss();
                                        } else {
                                            ToastUtils.showToast(context, "绑定失败");
                                        }
                                    }));
                                }

                                cancel.setOnClickListener(view -> dialog.dismiss());
                            }
                        }).show(this));
                break;
        }

        binding.back.setOnClickListener(v -> finish());
        binding.cancel.setOnClickListener(v -> finish());

        binding.changeSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.headChange.setOnClickListener(v -> PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .setImageEngine(GlideEngine.createGlideEngine())
                .setMaxSelectNum(1)
                .setSelectorUIStyle(pictureSelectorStyle)
                //这里用鲁班算法对选择的图片进行压缩，避免图片过大而浪费资源
                .setCompressEngine((CompressFileEngine) (context, source, call) -> Luban.with(context).load(source).ignoreBy(100)
                        .setCompressListener(new OnNewCompressListener() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(String source, File compressFile) {
                                if (call != null) {
                                    file = compressFile;
                                    switch (KVConfigImpl.getKVConfigImpl().getInt(GlobalConfig.STATUS, -1)) {
                                        case 0:
                                            Api.INSTANCE.upload(file, String.valueOf(KVConfigImpl.getKVConfigImpl().getInt(GlobalConfig.ID, -1)), (success, errCode, data) -> {
                                                if (data != null) {
                                                    ToastUtils.showToast(context, data.getMessage());
                                                    ContentValues values = new ContentValues();
                                                    values.put("head", data.getData());
                                                    LitePal.update(UserBean.class, values, LitePal.findLast(UserBean.class).getId());
                                                    runOnUiThread(() -> initInfo());
                                                } else {
                                                    ToastUtils.showToast(context, "头像上传失败:" + errCode);
                                                }
                                            });
                                            break;
                                        case 1:
                                            Api.INSTANCE.uploadD(file, String.valueOf(KVConfigImpl.getKVConfigImpl().getInt(GlobalConfig.ID, -1)), (success, errCode, data) -> {
                                                if (data != null) {
                                                    ToastUtils.showToast(context, data.getMessage());
                                                    ContentValues values = new ContentValues();
                                                    values.put("head", data.getData());
                                                    LitePal.update(DriverBean.class, values, LitePal.findLast(DriverBean.class).getDriver_id());
                                                    runOnUiThread(() -> initInfo());
                                                } else {
                                                    ToastUtils.showToast(context, "头像上传失败:" + errCode);
                                                }
                                            });
                                            break;
                                    }

                                    call.onCallback(source, compressFile.getAbsolutePath());
                                }
                            }

                            @Override
                            public void onError(String source, Throwable e) {
                                if (call != null) {
                                    call.onCallback(source, null);
                                }
                            }
                        }).launch())
                .forResult(new OnResultCallbackListener<>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {

                    }

                    @Override
                    public void onCancel() {
                        ToastUtils.showToast(context, "取消选择");
                    }
                }));

        binding.submit.setOnClickListener(v -> {
            switch (KVConfigImpl.getKVConfigImpl().getInt(GlobalConfig.STATUS, -1)) {
                case 0:
                    Api.INSTANCE.update(String.valueOf(KVConfigImpl.getKVConfigImpl().getInt(GlobalConfig.ID, -1)), binding.changeUsername.getText().toString(),
                            binding.changeSex.getSelectedItem().toString(), binding.changeAge.getText().toString(), (success, errCode, data) -> {
                                if (data != null) {
                                    if (data.getData() != null) {
                                        if (data.getData().save()) {
                                            ToastUtils.showToast(context, data.getMessage());
                                            runOnUiThread(this::initInfo);
                                        }
                                    }
                                } else {
                                    ToastUtils.showToast(context, "出现异常:" + errCode);
                                }
                            });
                    break;
                case 1:
                    Api.INSTANCE.updateD(String.valueOf(KVConfigImpl.getKVConfigImpl().getInt(GlobalConfig.ID, -1)), binding.changeUsername.getText().toString(),
                            binding.changeSex.getSelectedItem().toString(), binding.changeAge.getText().toString(), (success, errCode, data) -> {
                                if (data != null) {
                                    if (data.getData() != null) {
                                        if (data.getData().save()) {
                                            ToastUtils.showToast(context, data.getMessage());
                                            runOnUiThread(this::initInfo);
                                        }
                                    }
                                } else {
                                    ToastUtils.showToast(context, "出现异常:" + errCode);
                                }
                            });
                    break;
            }
        });
    }

    private void initInfo() {
        switch (KVConfigImpl.getKVConfigImpl().getInt(GlobalConfig.STATUS, -1)) {
            case 0:
                Glide.with(context)
                        .load(LitePal.findLast(UserBean.class).getHead())
                        .into(binding.head);
                LogUtils.d("返回的头像" + LitePal.findLast(UserBean.class).getHead());
                binding.changeUsername.setText(LitePal.findLast(UserBean.class).getUsername());
                binding.changeAge.setText(LitePal.findLast(UserBean.class).getAge() + "");
                switch (LitePal.findLast(UserBean.class).getSex()) {
                    case "未设置":
                        binding.changeSex.setSelection(0);
                        break;
                    case "男":
                        binding.changeSex.setSelection(1);
                        break;
                    case "女":
                        binding.changeSex.setSelection(2);
                        break;
                }
                break;
            case 1:
                Glide.with(context)
                        .load(LitePal.findLast(DriverBean.class).getHead())
                        .into(binding.head);

                binding.changeUsername.setText(LitePal.findLast(DriverBean.class).getUsername());
                binding.changeAge.setText(LitePal.findLast(DriverBean.class).getAge() + "");
                switch (LitePal.findLast(DriverBean.class).getSex()) {
                    case "未设置":
                        binding.changeSex.setSelection(0);
                        break;
                    case "男":
                        binding.changeSex.setSelection(1);
                        break;
                    case "女":
                        binding.changeSex.setSelection(2);
                        break;
                }
                break;
        }

    }
}