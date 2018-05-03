package hdv.iky.nrftouch.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hdv.iky.nrftouch.R;
import hdv.iky.nrftouch.data.BusEvent;
import hdv.iky.nrftouch.data.DataManager;
import hdv.iky.nrftouch.data.Protocol;
import hdv.iky.nrftouch.data.model.IkyDevice;
import hdv.iky.nrftouch.ui.base.BaseActivity;
import hdv.iky.nrftouch.ui.main.MainActivity;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Ann on 2/28/16.
 */
public class ChangePINSmartkeyFragment extends Fragment {


    @Bind(R.id.etNewPINSmartkeyChangeFragment)
    EditText etNewPINSmartkeyChangeFragment;


    @Inject
    DataManager dataManager;
    @Inject
    Bus bus;

    private IkyDevice mIkyDevice;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ((BaseActivity) getActivity()).activityComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_change_pin_smartkey, container, false);
        ButterKnife.bind(this, fragmentView);
        ((MainActivity)getActivity()).showTabMenu(false);
        ((MainActivity)getActivity()).setTextTitle("Đổi mã PIN SMART key");
        bus.register(this);

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
                        mIkyDevice = ikyDevice;

                    }
                });
        return fragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bus.unregister(this);
    }

    @OnClick(R.id.ibSaveFragmentChangePINSmartkey)
    void OnClickedSavePINSmartkey(){

        String newPin = etNewPINSmartkeyChangeFragment.getText().toString();

        if(TextUtils.isEmpty(newPin) || (newPin.length() != (int)Protocol.PIN_SMARTKEY_LENGTH)){
            toast("PIN Smartkey mới không hợp lệ");
            return;
        }

        ((MainActivity)getActivity()).getMainPresenter().changePinSmartkey(newPin);

    }


    private void toast(final String ss){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), ss, Toast.LENGTH_SHORT).show();

            }
        });
    }



    @Subscribe
    public void eventUpdatePinSmartkey(BusEvent.UpdatePinSmartkey updatePinSmartkey){
        if(updatePinSmartkey.isSuccess){
            toast("Đổi mã PIN SMART key thành công ");
            mIkyDevice.setPINSmartkey(etNewPINSmartkeyChangeFragment.getText().toString());
            saveIkyDevice(mIkyDevice);

        }else{
            toast("Lỗi khi đổi mã PIN SMART key");
        }
    }

    public void saveIkyDevice(IkyDevice ikydevice){
        dataManager.setIkyDevice(ikydevice)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }
                });
    }



}
