package com.prohua.smurfs.ui.other;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.prohua.smurfs.R;
import com.prohua.smurfs.adapter.GridImageAdapter;
import com.prohua.smurfs.net.CallBackInterface;
import com.prohua.smurfs.net.CircleSendReleaseNet;
import com.prohua.smurfs.net.ReleaseImageFileNet;
import com.prohua.smurfs.ui.MainFragment;
import com.prohua.smurfs.ui.main.FourthTabFragment;
import com.prohua.smurfs.ui.main.SecondTabFragment;
import com.prohua.smurfs.ui.widgt.SweetAlertDialog;
import com.prohua.smurfs.util.UtilAMaps;
import com.prohua.smurfs.util.UtilTools;
import com.tapadoo.alerter.Alerter;
import com.yalantis.ucrop.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

import static com.prohua.smurfs.application.SmurfsApplication.studentInfo;

/**
 * ReleaseMsgFragment 朋友圈发布
 * Created by Deep on 2017/4/24 0024.
 */
public class ReleaseMsgFragment extends SupportFragment {

    @BindView(R.id.status_bar)
    LinearLayout statusLinearLayout;
    @BindView(R.id.l_lin)
    LinearLayout linLinearLayout;
    @BindView(R.id.t_location)
    TextView locationTextView;

    @BindView(R.id.t_chance)
    TextView chanceTextView;
    @BindView(R.id.t_send)
    TextView sendTextView;

    @BindView(R.id.content)
    EditText contentEditText;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    // 1 纯文 2 图文 3 纯图
    // data为依据
    private int type = 1;

    // 三种 0文字 1照相机 2相册
    public static int startType = 0;

    // 高德API定位
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = new AMapLocationClientOption();

    // 定位状态
    private int locationType = 0;

    // 定位位置
    private String locationAddress = "";

    private GridImageAdapter adapter;
    // 图片最大可选数量
    private int maxSelectNum = 9;

    // 选择的路径数据
    private List<LocalMedia> selectMedia = new ArrayList<>();

    // 提示窗
    private SweetAlertDialog dialog;

    /**
     * 懒加载
     */
    public static ReleaseMsgFragment newInstance() {

        Bundle args = new Bundle();

        ReleaseMsgFragment fragment = new ReleaseMsgFragment();
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * 创建新的Fragment视图
     *
     * @param inflater           加载布局
     * @param container          view组
     * @param savedInstanceState 通讯（HashMap）
     * @return 视图
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.release_msg_fragment, container, false);

        ButterKnife.bind(this, view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            initStatusView();

        //初始化定位
        initLocation();

        initView();

        return view;
    }


    /**
     * 初始化定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(getActivity().getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {

        AMapLocationClientOption mOption = new AMapLocationClientOption();

        //可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setGpsFirst(false);
        //可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setHttpTimeOut(30000);
        //可选，设置定位间隔。默认为2秒
        mOption.setInterval(2000);
        //可选，设置是否返回逆地理地址信息。默认是true
        mOption.setNeedAddress(true);
        //可选，设置是否单次定位。默认是false
        mOption.setOnceLocation(false);
        //可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        mOption.setOnceLocationLatest(false);
        //可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);
        //可选，设置是否使用传感器。默认是false
        mOption.setSensorEnable(false);
        //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setWifiScan(true);
        //可选，设置是否使用缓存定位，默认为true
        mOption.setLocationCacheEnable(true);

        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                //解析定位结果
                locationAddress = UtilAMaps.getLocationStr(loc);
                locationTextView.setText(locationAddress);

                stopLocation();
                destroyLocation();
                locationType = 2;
            } else {
                locationType = 3;
                locationTextView.setText(getString(R.string.location_failed));
            }
        }
    };

    /**
     * 根据控件的选择，重新设置定位参数
     */
    private void resetOption() {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(false);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(false);
        // 设置是否单次定位
        locationOption.setOnceLocation(true);
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        locationOption.setOnceLocationLatest(false);
        //设置是否使用传感器
        locationOption.setSensorEnable(false);
        //设置是否开启wifi扫描，如果设置为false时同时会停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
/*        String strInterval = etInterval.getText().toString();
        if (!TextUtils.isEmpty(strInterval)) {
            try{
                // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
                locationOption.setInterval(Long.valueOf(strInterval));
            }catch(Throwable e){
                e.printStackTrace();
            }
        }

        String strTimeout = etHttpTimeout.getText().toString();
        if(!TextUtils.isEmpty(strTimeout)){
            try{
                // 设置网络请求超时时间
                locationOption.setHttpTimeOut(Long.valueOf(strTimeout));
            }catch(Throwable e){
                e.printStackTrace();
            }
        }*/
    }

    /**
     * 开始定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void startLocation() {
        //根据控件的选择，重新设置定位参数
        resetOption();
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 停止定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void stopLocation() {
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    /**
     * 已上传图片数量
     */
    private static int fileCount;

    /**
     * 初始化视图逻辑
     */
    private void initView() {
        linLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locationType == 0) {
                    locationType = 1;
                    locationTextView.setText(getString(R.string.being_positioned));
                    startLocation();
                }
            }
        });

        contentEditText.setFocusable(true);
        contentEditText.setFocusableInTouchMode(true);
        contentEditText.requestFocus();

        // 200后执行
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) contentEditText.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }

        }, 200);

        sendTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new SweetAlertDialog(getActivity());
                dialog.setTitleText(getString(R.string.please_wait));
                dialog.show();

                // 判断类型
                if (adapter.getList().size() == 0) {
                    type = 1;
                } else if ("".equals(contentEditText.getText().toString())) {
                    type = 3;
                } else {
                    type = 2;
                }

                // 如果不是纯文, 或者不为空
                if (type != 1 || !"".equals(contentEditText.getText().toString())) {
                    new CircleSendReleaseNet(new CallBackInterface() {
                        @Override
                        public void complete() {

                            // 判断是否上传图片
                            if (selectMedia.size() > 0) {

                                // 循环建立上传图片任务线程
                                for (int i = 0; i < selectMedia.size(); i++) {

                                    new ReleaseImageFileNet(new CallBackInterface() {
                                        @Override
                                        public void complete() {
                                            fileCount++;
                                            _mActivity.runOnUiThread(() -> {
                                                dialog.setTitleText(getString(R.string.upload_picture) +" " +
                                                        fileCount + "/" + selectMedia.size());

                                                // 上传完成就退出
                                                if (fileCount == selectMedia.size()) {
                                                    dialog.cancel();

                                                    InputMethodManager imm = (InputMethodManager) _mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                                    imm.hideSoftInputFromWindow(contentEditText.getWindowToken(), 0);
                                                    Alerter.create(getActivity())
                                                            .setText(getString(R.string.send_success))
                                                            .showIcon(false)
                                                            .setBackgroundColor(R.color.showbg)
                                                            .show();

                                                    // 返回数据给主fragment
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString(FourthTabFragment.KEY_RESULT_NICKNAME, studentInfo.getNickName());
                                                    setFragmentResult(RESULT_OK, bundle);

                                                    pop();
                                                    MainFragment.tapPosition = 1;
                                                    SecondTabFragment.sodo = 1;
                                                    fileCount = 0;
                                                }
                                            });
                                        }

                                        @Override
                                        public void error() {
                                            _mActivity.runOnUiThread(() -> dialog.setTitleText(getString(R.string.failed_to_upload)));
                                        }
                                    }).pullUp(selectMedia.get(i).getCompressPath(), CircleSendReleaseNet.circleSendReleaseOutBean.getData());

                                }

                            } else {

                                _mActivity.runOnUiThread(() -> {
                                    if (fileCount == selectMedia.size()) {
                                        dialog.cancel();
                                        InputMethodManager imm = (InputMethodManager) _mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(contentEditText.getWindowToken(), 0);
                                        Alerter.create(getActivity())
                                                .setText(getString(R.string.send_success))
                                                .showIcon(false)
                                                .setBackgroundColor(R.color.showbg)
                                                .show();
                                        // 返回数据给主fragment
                                        Bundle bundle = new Bundle();
                                        bundle.putString(FourthTabFragment.KEY_RESULT_NICKNAME, studentInfo.getNickName());
                                        setFragmentResult(RESULT_OK, bundle);

                                        pop();
                                        MainFragment.tapPosition = 1;
                                        SecondTabFragment.sodo = 1;
                                    }
                                });
                            }
                        }

                        @Override
                        public void error() {
                            _mActivity.runOnUiThread(() -> dialog.setTitleText(CircleSendReleaseNet.circleSendReleaseOutBean.getMsg()));
                        }
                    }).pullDown(contentEditText.getText().toString(), locationAddress, "", type);

                } else {
                    dialog.cancel();
                    Alerter.create(getActivity())
                            .setText(getString(R.string.written_yet))
                            .showIcon(false)
                            .setBackgroundColor(R.color.showbg)
                            .show();
                }
            }
        });

        chanceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) _mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(contentEditText.getWindowToken(), 0);
                SecondTabFragment.sodo = 2;
                onBackPressedSupport();
            }
        });


        FullyGridLayoutManager manager = new FullyGridLayoutManager(_mActivity, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(_mActivity, onAddPicClickListener);
        adapter.setList(selectMedia);
        adapter.setSelectMax(maxSelectNum);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                PictureConfig.getInstance().externalPicturePreview(getContext(), position, selectMedia);
            }
        });

        if (startType == 1)
            PictureConfig.getInstance().init(initOption()).startOpenCamera(getContext(), resultCallback);
        else if (startType == 2)
            PictureConfig.getInstance().init(initOption()).openPhoto(getContext(), resultCallback);

    }

    /**
     * 删除图片回调接口
     */

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick(int type, int position) {
            switch (type) {
                case 0:
                    // 先初始化参数配置，在启动相册
                    PictureConfig.getInstance().init(initOption()).openPhoto(getContext(), resultCallback);
                    break;
                case 1:
                    // 删除图片
                    selectMedia.remove(position);
                    adapter.notifyItemRemoved(position);
                    break;
            }
        }
    };

    /**
     * 初始化参数
     * @return
     */
    private FunctionOptions initOption() {
        return new FunctionOptions.Builder()
                .setType(FunctionConfig.TYPE_IMAGE) // 图片or视频 FunctionConfig.TYPE_IMAGE  TYPE_VIDEO
                .setCropMode(FunctionConfig.CROP_MODEL_DEFAULT) // 裁剪模式 默认、1:1、3:4、3:2、16:9
                .setCompress(true) //是否压缩
                .setEnablePixelCompress(true) //是否启用像素压缩
                .setEnableQualityCompress(true) //是否启质量压缩
                .setMaxSelectNum(maxSelectNum) // 可选择图片的数量
                .setSelectMode(FunctionConfig.MODE_MULTIPLE) // 单选 or 多选
                .setShowCamera(true) //是否显示拍照选项 这里自动根据type 启动拍照或录视频
                .setEnablePreview(true) // 是否打开预览选项
                .setEnableCrop(false) // 是否打开剪切选项
                .setPreviewVideo(true) // 是否预览视频(播放) mode or 多选有效
                .setCheckedBoxDrawable(0)
                .setRecordVideoDefinition(FunctionConfig.HIGH) // 视频清晰度
                .setRecordVideoSecond(60) // 视频秒数
                .setGif(false)// 是否显示gif图片，默认不显示
                .setCropW(0) // cropW-->裁剪宽度 值不能小于100  如果值大于图片原始宽高 将返回原图大小
                .setCropH(0) // cropH-->裁剪高度 值不能小于100 如果值大于图片原始宽高 将返回原图大小
                .setMaxB(0) // 压缩最大值 例如:200kb  就设置202400，202400 / 1024 = 200kb
                .setPreviewColor(ContextCompat.getColor(getContext(), R.color.blue)) //预览字体颜色
                .setCompleteColor(ContextCompat.getColor(getContext(), R.color.blue)) //已完成字体颜色
                .setPreviewBottomBgColor(ContextCompat.getColor(getContext(), R.color.black)) //预览底部背景色
                .setBottomBgColor(ContextCompat.getColor(getContext(), R.color.white)) //图片列表底部背景色
                .setGrade(Luban.THIRD_GEAR) // 压缩档次 默认三档
                .setCheckNumMode(false)
                .setCompressQuality(100) // 图片裁剪质量,默认无损
                .setImageSpanCount(4) // 每行个数
                .setSelectMedia(selectMedia) // 已选图片，传入在次进去可选中，不能传入网络图片
                .setCompressFlag(1) // 1 系统自带压缩 2 luban压缩
                .setCompressW(0) // 压缩宽 如果值大于图片原始宽高无效
                .setCompressH(0) // 压缩高 如果值大于图片原始宽高无效
                .setThemeStyle(Color.parseColor("#6699ff")) // 设置主题样式
                .create();
    }

    /**
     * 图片回调方法
     */
    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> resultList) {
            selectMedia = resultList;
            Log.i("callBack_result", selectMedia.size() + "");
            LocalMedia media = resultList.get(0);
            if (media.isCut() && !media.isCompressed()) {
                // 裁剪过
                String path = media.getCutPath();
            } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                String path = media.getCompressPath();
            } else {
                // 原图地址
                String path = media.getPath();
            }
            if (selectMedia != null) {
                adapter.setList(selectMedia);
                adapter.notifyDataSetChanged();
            }
        }
    };


    /**
     * 状态栏
     */
    private void initStatusView() {
        ViewGroup.LayoutParams lp = statusLinearLayout.getLayoutParams();
        lp.height = UtilTools.getStatusBarHeight(this.getContext());
        statusLinearLayout.setLayoutParams(lp);
    }

    /**
     * 处理回退事件
     *
     * @return true
     */
    @Override
    public boolean onBackPressedSupport() {

        MainFragment.tapPosition = 1;
        pop();
        return true;
    }

}