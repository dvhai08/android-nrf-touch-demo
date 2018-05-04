package hdv.iky.nrftouch.ui.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hdv.iky.nrftouch.R;
import hdv.iky.nrftouch.data.DataManager;
import hdv.iky.nrftouch.data.model.IkyDevice;
import hdv.iky.nrftouch.ui.ChangeNameFragment;
import hdv.iky.nrftouch.ui.ChangePassFragment;
import hdv.iky.nrftouch.ui.UserFragment;
import hdv.iky.nrftouch.ui.base.BaseActivity;
import hdv.iky.nrftouch.ui.main.MainActivity;
import hdv.iky.nrftouch.util.PreferencesHelper;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Ann on 2/27/16.
 */
public class AccountFragment extends Fragment implements AccountMvpView {
    @Bind(R.id.tvDeviceFragmentUser)
    TextView tvDeviceFragmentUser;
    @Bind(R.id.tvChangeNameDeviceFragmentUser)
    TextView tvChangeNameDeviceFragmentUser;
    @Bind(R.id.tvPassFragmentUser)
    TextView tvPassFragmentUser;


    @Inject
    AccountPresenter statusPresenter;

    @Inject
    DataManager dataManager;

    @Inject
    PreferencesHelper preferencesHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ((BaseActivity) getActivity()).activityComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, fragmentView);
        statusPresenter.attachView(this);
        ((MainActivity)getActivity()).showTabMenu(true);
        ((MainActivity)getActivity()).showTitleUser(true);
        ((MainActivity)getActivity()).setTextTitle("Tài khoản");

        dataManager.findIkyDevices()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<IkyDevice>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(IkyDevice ikyDevice) {

                        if(ikyDevice != null ){
                            setNameDevice(ikyDevice.getName());
                        }
                    }
                });
        return fragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        statusPresenter.detachView();
    }


    @Override
    public void showDialogInforUser() {
        ((MainActivity)getActivity()).showTitleUser(false);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.fragment, new UserFragment())
                .addToBackStack(null)
                .commit();

    }

    @OnClick(R.id.tvChangeNameDeviceFragmentUser)
    void OnClickChangeName(){

        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.fragment, new ChangeNameFragment())
                .addToBackStack(null)
                .commit();


    }

    @OnClick(R.id.tvPassFragmentUser)
    void OnClickChangePass(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.fragment, new ChangePassFragment())
                .addToBackStack(null)
                .commit();

    }

    private void setNameDevice(String ss){
        tvDeviceFragmentUser.setText(ss);
    }

}
