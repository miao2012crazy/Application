package com.xialan.app.view.mainfragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.awarmisland.android.popularrefreshlayout.RefreshLayout;
import com.awarmisland.android.popularrefreshlayout.RefreshListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.tamic.novate.BaseSubscriber;
import com.tamic.novate.Throwable;
import com.xialan.app.R;
import com.xialan.app.adapter.ProductAdapter2;
import com.xialan.app.base.BaseFragment;
import com.xialan.app.base.BasePresenter;
import com.xialan.app.bean.ProductBean;
import com.xialan.app.contract.ProductContract;
import com.xialan.app.presenter.ProductPresenter;
import com.xialan.app.utils.HttpUrl;
import com.xialan.app.utils.RxBus;
import com.xialan.app.utils.StringUtil;
import com.xialan.app.utils.UIUtils;
import com.xialan.app.view.JSinterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/9/6.
 */
public class ProductFragment2 extends BaseFragment implements ProductContract.View {
    @BindView(R.id.progressBarLoading)
    ProgressBar mProgressBarLoading;
    @BindView(R.id.progress)
    TextView tv_progress;
    @BindView(R.id.rl_progressbar)
    RelativeLayout rl_progressbar;
    @BindView(R.id.tab_product_2)
    TabLayout tabProduct2;
    @BindView(R.id.recycler_product_2)
    RecyclerViewPager recyclerProduct2;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.refresh)
    RefreshLayout refresh;
    @BindView(R.id.rl_webview)
    RelativeLayout rlWebview;
    private ProductPresenter productPresenter;
    private List<ProductBean> productBeanList;
    private ProductAdapter2 productAdapter2;
    private boolean isVideo = false;
    private int tab_length;
    private View rootView;
    private Intent intent;
    private boolean isBind;
    private Observable<String> netstate;

    @Override
    protected int getContentId() {
        return R.layout.fragment_product2;
    }

    @Override
    protected void loadData() {
        productBeanList = new ArrayList<>();
        //获取tab名称
        productPresenter.getDataForTabName();
        recyclerProduct2.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //判断是当前layoutManager是否为LinearLayoutManager
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取最后一个可见view的位置
                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    if (lastItemPosition == firstItemPosition) {
                        tabProduct2.setScrollPosition(lastItemPosition, 0, true);
                    }
                }
            }
        });

        RxBus.get().register("hide_web_view", String.class).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                if (rlWebview != null) {
                    webView.loadUrl("about:blank");
                    rlWebview.setVisibility(View.GONE);
                }
                if (isVideo) {
                    RxBus.get().post("vv_reset_soruce", "");
                    RxBus.get().post("update_product_tag", false);//设置产品视频标签不显示
                    isVideo = false;
                }
            }
        });

    }

    /**
     * tab 获取成功
     *
     * @param data
     */
    @Override
    public void tabSuccess(String data) {
        String[] split = StringUtil.split(data, '/');
        tab_length = split.length;
        setButtonName(split);
    }

    /**
     * tab 获取失败
     *
     * @param
     */
    @Override
    public void tabFail() {
        UIUtils.showToast(getActivity(), "请求失败");

    }

    /**
     * 获取产品成功
     */
    @Override
    public void productSuccess(List<ProductBean> data, int position) {
        productBeanList.addAll(position, data);
        productAdapter2.notifyDataSetChanged();
        position++;
        if (position == tab_length) {
            return;
        } else {
            productPresenter.getDataProductDataForPosition(position);
        }
    }

    /**
     * 获取产品失败
     */
    @Override
    public void productFail() {
        UIUtils.showToast(getActivity(), "请求失败");
    }

    @SuppressLint("NewApi")
    private void setButtonName(String[] split) {
        productAdapter2 = new ProductAdapter2(productBeanList, split);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerProduct2.setLayoutManager(linearLayoutManager);
        recyclerProduct2.setAdapter(productAdapter2);
//        TabLayoutSupport.setupWithViewPager(tabProduct2, recyclerProduct2, productAdapter2);
        for (int i = 0; i < split.length; i++) {
            tabProduct2.addTab(tabProduct2.newTab().setText(split[i]));
        }
        tabProduct2.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                recyclerProduct2.scrollToPosition(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        recyclerProduct2.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                rlWebview.setVisibility(View.VISIBLE);
                initWebView(view.getTag() + "");
                ProductBean productBean = (ProductBean) baseQuickAdapter.getData().get(i);
                String video_url = "";
                switch (view.getId()) {
                    case R.id.image2_view_0:
                        video_url = productBean.getProduct_video_url_0();
                        break;
                    case R.id.image2_view_1:
                        video_url = productBean.getProduct_video_url_1();
                        break;
                    case R.id.image2_view_2:
                        video_url = productBean.getProduct_video_url_2();
                        break;
                    case R.id.image2_view_3:
                        video_url = productBean.getProduct_video_url_3();
                        break;
                }
                RxBus.get().post("vv_soruce", HttpUrl.setGetUrl("IBSync/videos/product/" + video_url));
                RxBus.get().post("update_product_tag", true);
                isVideo = true;
            }
        });
        RxBus.get().register("update_isVideo", boolean.class).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                isVideo = aBoolean;
            }
        });
        productPresenter.getDataProductDataForPosition(0);
    }

    /**
     * 加载 webview
     *
     * @param index 索引
     */
    @TargetApi(Build.VERSION_CODES.M)
    @SuppressLint("JavascriptInterface")
    private void initWebView(String index) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JSinterface(getActivity()), "android");
        webView.loadUrl(HttpUrl.setGetUrl("IBSync/view_product.aspx?no=" + index));
        webView.setWebChromeClient(new WebChromeClientProgress() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (tv_progress == null) return;
                tv_progress.setText(progress + "");
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                //设置加载进度条
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                tv_progress.setText("0");
                rl_progressbar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (rl_progressbar != null)
                    rl_progressbar.setVisibility(View.GONE);
            }

        });
        rl_progressbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_progressbar.setVisibility(View.GONE);
            }
        });
        refresh.setAllowLoadMore(true);//设置支持上拉加载
        refresh.setRefreshListener(new RefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh.finishRefresh();
                        //刷新数据
                        webView.reload();
                    }
                }, 1000);
            }

            @Override
            public void onRefreshLoadMore(RefreshLayout refreshLayout) {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh.finishRefreshLoadMore();
                        //刷新数据
                        webView.reload();
                    }
                }, 1000);
            }
        });
    }

    /**
     * webView进度条
     */
    private class WebChromeClientProgress extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int progress) {
            if (mProgressBarLoading != null) {
                tv_progress.setText(progress + "");
                mProgressBarLoading.setProgress(progress);
                if (progress == 100) rl_progressbar.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, progress);
        }
    }

    @Override
    protected BasePresenter createPresenter() {
        productPresenter = new ProductPresenter(this);
        return productPresenter;
    }


    @Override
    public void onPause() {
        super.onPause();
        if (rl_progressbar != null) {
            rl_progressbar.setVisibility(View.GONE);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister("NETSTATE",netstate);
    }

    @Override
    public void onResume() {
        super.onResume();
        netstate = RxBus.get().register("NETSTATE", String.class);
        netstate.subscribe(new BaseSubscriber<String>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                productPresenter.getDataForTabName();
            }
        });

    }
}
