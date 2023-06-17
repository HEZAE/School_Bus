package com.google.android.ui.forum;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.GlobalConfig;
import com.google.android.R;
import com.google.android.api.Api;
import com.google.android.api.ApiUpdate;
import com.google.android.databinding.FragmentForumBinding;
import com.google.android.ui.forum.adapter.ForumAdapter;
import com.google.android.ui.mine.bean.DriverBean;
import com.google.android.widget.GlideEngine;
import com.google.base.fragment.BaseFragment;
import com.google.base.mmkv.KVConfigImpl;
import com.google.base.utils.DateUtils;
import com.google.base.utils.ToastUtils;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.engine.CompressFileEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.luck.picture.lib.style.PictureSelectorStyle;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;

import top.zibin.luban.Luban;
import top.zibin.luban.OnNewCompressListener;

public class ForumFragment extends BaseFragment<FragmentForumBinding> {
    private final PictureSelectorStyle pictureSelectorStyle = new PictureSelectorStyle();
    private File file;
    @Override
    protected void initFragment() {
        binding.refreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        }).setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        });
        switch (KVConfigImpl.getKVConfigImpl().getInt(GlobalConfig.STATUS, -1)) {
            case 0:
                binding.addNew.setVisibility(View.GONE);
                break;
            case 1:
                binding.addNew.setVisibility(View.VISIBLE);
                break;
        }

        initData();

        binding.addNew.setOnClickListener(v -> BottomDialog.build()
                .setMaskColor(0xFF2A9A53)
                .setCustomView(new OnBindView<>(R.layout.item_update) {
                    @Override
                    public void onBind(BottomDialog dialog, View v) {
                        Button submit = v.findViewById(R.id.submit);
                        Button cancel = v.findViewById(R.id.cancel);
                        ImageView imageView = v.findViewById(R.id.select);
                        EditText title = v.findViewById(R.id.updateTitle);
                        EditText content = v.findViewById(R.id.updateContent);
                        imageView.setOnClickListener(view -> {
                            PictureSelector.create(context)
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
//                                                        activity.runOnUiThread(() -> Glide.with(context).load(file).into(imageView));
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
                                    .forResult(new OnResultCallbackListener<LocalMedia>() {
                                        @Override
                                        public void onResult(ArrayList<LocalMedia> result) {
                                            Glide.with(context).load(file).into(imageView);
                                        }

                                        @Override
                                        public void onCancel() {
                                            ToastUtils.showToast(context, "取消选择");
                                        }
                                    });
                        });

                        submit.setOnClickListener(view -> {
                            DriverBean driverBean = LitePal.findLast(DriverBean.class);
                            if (driverBean != null){
                                ApiUpdate.INSTANCE.addInfo(String.valueOf(driverBean.getDriver_id()), driverBean.getUsername(), title.getText().toString(), content.getText().toString(), DateUtils.getCurrentDateTime(), (success, errCode, data) -> {
                                    if (data != null && file != null) {
                                        ApiUpdate.INSTANCE.upload(file, String.valueOf(data.getData()), (success1, errCode1, data1) -> {
                                            if (data1 != null) {
                                                initData();
                                                ToastUtils.showToast(context, "发布成功");
                                                dialog.dismiss();
                                            } else {
                                                ToastUtils.showToast(context, "发布失败");
                                            }
                                        });
                                    } else {
                                        ToastUtils.showToast(context, "请选择一个封面");
                                    }
                                });
                            }else {
                                Api.INSTANCE.selectDriver(String.valueOf(KVConfigImpl.getKVConfigImpl().getInt(GlobalConfig.ID, -1)), (success, errCode, data) -> {
                                    if (data != null) {
                                        data.getData().save();
                                        ApiUpdate.INSTANCE.addInfo(String.valueOf(data.getData().getDriver_id()), data.getData().getUsername(), title.getText().toString(), content.getText().toString(), DateUtils.getCurrentDateTime(), (success12, errCode12, data12) -> {
                                            if (data12 != null && file != null) {
                                                ApiUpdate.INSTANCE.upload(file, String.valueOf(data12.getData()), (success1, errCode1, data1) -> {
                                                    if (data1 != null) {
                                                        initData();
                                                        ToastUtils.showToast(context, "发布成功");
                                                        dialog.dismiss();
                                                    } else {
                                                        ToastUtils.showToast(context, "发布失败");
                                                    }
                                                });
                                            } else {
                                                ToastUtils.showToast(context, "请选择一个封面");
                                            }
                                        });
                                    }
                                });
                            }
                        });

                        cancel.setOnClickListener(view -> dialog.dismiss());
                    }
                }).show(activity));
    }

    private void initData(){
        ApiUpdate.INSTANCE.getAll((success, errCode, data) -> {
            if (data != null) {
                activity.runOnUiThread(() -> {
                    binding.refreshView.finishRefresh();
                    binding.refreshView.finishLoadMore();
                    ForumAdapter forumAdapter = new ForumAdapter(context, getViewLifecycleOwner(), data.getData());
                    binding.forumListView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                    binding.forumListView.setAdapter(forumAdapter);
                });
            }
        });
    }
}