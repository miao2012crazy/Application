package com.xialan.app.view.mainfragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.SearchView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.tamic.novate.BaseSubscriber;
import com.tamic.novate.Throwable;
import com.xialan.app.R;
import com.xialan.app.adapter.TrainingVideoAdapter;
import com.xialan.app.application.XLApplication;
import com.xialan.app.base.BaseFragment;
import com.xialan.app.base.BasePresenter;
import com.xialan.app.bean.TrainingVideoBean;
import com.xialan.app.contract.TrainingVideoContract;
import com.xialan.app.presenter.TrainingVideoPresenter;
import com.xialan.app.utils.HttpUrl;
import com.xialan.app.utils.RxBus;
import com.xialan.app.utils.UIUtils;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/7/18.
 */
public class TrainingVideoFragment extends BaseFragment implements TrainingVideoContract.View {
    @BindView(R.id.recycler_view_shopping)
    RecyclerView recyclerViewShopping;
    @BindView(R.id.searchView)
    SearchView searchView;
    private TrainingVideoAdapter adapter;
    private int lastSelectPosition=-1;
    private boolean is_video=false;
    private TrainingVideoPresenter trainingVideoPresenter;
    private List<TrainingVideoBean> mlist = new ArrayList<>();
    private Observable<String> netstate;

    @Override
    protected int getContentId() {
        return R.layout.fragment_traning_video;
    }

    @Override
    protected void loadData() {
        recyclerViewShopping.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TrainingVideoAdapter(mlist);
        recyclerViewShopping.setAdapter(adapter);
        recyclerViewShopping.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                if (lastSelectPosition == position && is_video) {
                    return;
                } else {
                    List data = baseQuickAdapter.getData();
                    TrainingVideoBean trainingVideoBean = (TrainingVideoBean) data.get(position);
                    String o = HttpUrl.setGetUrl("IBSync/lecture/videos/") + trainingVideoBean.getVideo_name();
                    RxBus.get().post("vv_soruce", o);
                    is_video=true;
                    XLApplication.trainVideoPosition=position;
                    XLApplication.IS_VIDEO=true;
                    trainingVideoBean.setIsSelected("1");
                    if (lastSelectPosition!=-1){
                        TrainingVideoBean trainingVideoBean1 = (TrainingVideoBean) data.get(lastSelectPosition);
                        trainingVideoBean1.setIsSelected("0");
                    }
                    adapter.notifyItemChanged(position);
                    adapter.notifyItemChanged(lastSelectPosition);
                    lastSelectPosition = position;
                }
            }
        });
        recyclerViewShopping.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                TrainingVideoBean trainingVideoBean = (TrainingVideoBean) baseQuickAdapter.getData().get(position);
                trainingVideoBean.setIsSelected("0");
                adapter.notifyItemChanged(position);
                //停止播放
                RxBus.get().post("vv_reset_soruce","");
                lastSelectPosition=-1;
                is_video=false;
                XLApplication.IS_VIDEO=is_video;
                XLApplication.trainVideoPosition=-1;
            }
        });
        initSerchView();
        trainingVideoPresenter.getDataForVideo("");
    }



    /**
     * 初始化列表数据
     * @param trainingVideoBeanList
     */
    private void initRecyclerViewData(List<TrainingVideoBean> trainingVideoBeanList) {
        mlist.clear();
        mlist.addAll(trainingVideoBeanList);
        adapter.notifyDataSetChanged();
        int playingVideoPosition = XLApplication.getPlayingVideoPosition();
        if (playingVideoPosition!=-1){
            mlist.get(playingVideoPosition).setIsSelected("1");
            adapter.notifyItemChanged(playingVideoPosition);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (is_video){
            RxBus.get().post("vv_reset_soruce","");
        }
        if (lastSelectPosition!=-1){
            TrainingVideoBean trainingVideoBean1 = (TrainingVideoBean) mlist.get(lastSelectPosition);
            trainingVideoBean1.setIsSelected("0");
        adapter.notifyDataSetChanged();
        }



    }

    /**
     * 初始化serchview
     */
    private void initSerchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String s1="^[\u4e00-\u9fa5]+$";
                String s2="^[0-9]+$";
                if (s.matches(s1)||s.matches(s2)){
                    if (!TextUtils.isEmpty(s)){
                        trainingVideoPresenter.getDataForVideo(s);
                    }
                }else{
                    UIUtils.showToast(getActivity(),"仅支持文字和数字的搜索!不支持特殊字符");
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)){
                    trainingVideoPresenter.getDataForVideo("");
                }
                return false;
            }
        });
    }
    @Override
    protected BasePresenter createPresenter() {
        trainingVideoPresenter = new TrainingVideoPresenter(this);
        return trainingVideoPresenter;
    }

    @Override
    public void onVideoDataSuccess(List<TrainingVideoBean> trainingVideoBeanList) {
        if (trainingVideoBeanList.size()==0){
            UIUtils.showToast(getActivity(),"未查询到结果!");
        }
        initRecyclerViewData(trainingVideoBeanList);
    }

    @Override
    public void onVideoDataFailed() {
        UIUtils.showToast(getActivity(),"请求失败,请检查网络!");
    }


    @Override
    public void onResume() {
        super.onResume();
        //注册培训视频播放完成状态监听
        Observable<String> video_complete = RxBus.get().register("VIDEO_COMPLETE", String.class);
        video_complete.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(java.lang.Throwable e) {

            }

            @Override
            public void onNext(String s) {
                if(adapter!=null&&mlist.size()!=0&&lastSelectPosition!=-1){
                    mlist.get(lastSelectPosition).setIsSelected("0");
//                    adapter.notifyDataSetChanged();
                    adapter.notifyItemChanged(lastSelectPosition);
                    //重置状态
                    lastSelectPosition=-1;
                    is_video=false;
                }
            }
        });


        netstate = RxBus.get().register("NETSTATE", String.class);
        netstate.subscribe(new BaseSubscriber<String>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                //网络已连接
                trainingVideoPresenter.getDataForVideo("");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}
