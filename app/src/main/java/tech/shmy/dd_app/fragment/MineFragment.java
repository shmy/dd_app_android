package tech.shmy.dd_app.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tendcloud.tenddata.TCAgent;
import com.tendcloud.tenddata.TDAccount;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import tech.shmy.dd_app.R;
import tech.shmy.dd_app.activity.AboutActivity;
import tech.shmy.dd_app.activity.HistoryActivity;
import tech.shmy.dd_app.activity.LoginActivity;
import tech.shmy.dd_app.activity.WebViewActivity;
import tech.shmy.dd_app.defs.AfterResponse;
import tech.shmy.dd_app.defs.BaseFragment;
import tech.shmy.dd_app.entity.UserEntity;
import tech.shmy.dd_app.event.UserLoggedEvent;
import tech.shmy.dd_app.event.UserLogoutEvent;
import tech.shmy.dd_app.event.UserToLogin;
import tech.shmy.dd_app.util.HttpClient;
import tech.shmy.dd_app.util.Util;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class MineFragment extends BaseFragment {
    private boolean logged = false;
    @BindView(R.id.refresh)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.login)
    Button loginButton;
    @BindView(R.id.logout)
    Button logoutButton;
//    @BindView(R.id.info)
//    FlexboxLayout infoContainer;
//    @BindView(R.id.username)
//    TextView username;
//    @BindView(R.id.avatar)
//    ImageView avatar;
@BindView(R.id.profile)
    SuperTextView profile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            new Thread(() -> {
                AfterResponse<UserEntity> afterResponse = HttpClient.userInfo();
                if (afterResponse.error != null) {
                    refreshLayout.finishRefresh(false);
                    return;
                }
                getActivity().runOnUiThread(() -> {
                    Glide.with(this)
                            .load("https://avatars2.githubusercontent.com/u/3004225?s=180&v=4")
                            .circleCrop()
                            .transition(withCrossFade())
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    profile.setLeftIcon(resource);
                                }
                            });

                    profile.setRightString(afterResponse.data.username.toUpperCase());
                    TCAgent.onLogin(afterResponse.data.id + "", TDAccount.AccountType.REGISTERED, afterResponse.data.username);

                    refreshLayout.finishRefresh(true);
                });
            }).start();
        });
        init();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void init() {
        String token = Util.mmkv.getString("token", "");
        logged = !token.isEmpty();
        initUI();
    }

    private void initUI() {
        // 未登录
        if (!logged) {
            smartRefreshLayout.setEnableRefresh(false);
            profile.setVisibility(View.GONE);
            logoutButton.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);

        } else {
            smartRefreshLayout.setEnableRefresh(true);
            smartRefreshLayout.autoRefresh();
            profile.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);

        }
    }

    @OnClick(R.id.login)
    void onLoginClick() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        pushActivity(intent);
    }
//    @OnClick(R.id.history)
//    void onHistoryClick() {
//        Intent intent = new Intent(getContext(), HistoryActivity.class);
//        pushActivity(intent);
//    }
    @OnClick(R.id.subject)
    void onSubjectClick() {
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra("url", "https://dd.shmy.tech/dd_app/subject");
        pushActivity(intent);
    }
    @OnClick(R.id.group)
    void onGroupClick() {
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra("url", "https://dd.shmy.tech/dd_app/join_group");
        pushActivity(intent);
    }
    @OnClick(R.id.about)
    void onAboutClick() {
        Intent intent = new Intent(getContext(), AboutActivity.class);
        pushActivity(intent);
    }
    @OnClick(R.id.logout)
    void onLogoutClick() {
        new AlertDialog.Builder(getContext())
                .setTitle("提示")
                .setMessage("确认退出登录吗?")
                .setPositiveButton("取消", (dialogInterface, i) -> {
                })
                .setNegativeButton("退出", (dialogInterface, i) -> {
                    Util.mmkv.clear();
                    init();
                })
                .show();
    }
    @OnClick(R.id.profile)
    public void OnProfileClick() {
        Toasty.info(getActivity(), "敬请期待", Toasty.LENGTH_LONG).show();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserLoggedEvent(UserLoggedEvent event) {
        init();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserLogoutEvent(UserLogoutEvent event) {
        Util.mmkv.clear();
        init();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserToLoginEvent(UserToLogin event) {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        pushActivity(intent);
    }

}
