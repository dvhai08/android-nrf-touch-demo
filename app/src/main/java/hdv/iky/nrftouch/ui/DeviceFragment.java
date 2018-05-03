package hdv.iky.nrftouch.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
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
import hdv.iky.nrftouch.data.model.IkyDevice;
import hdv.iky.nrftouch.ui.base.BaseActivity;
import hdv.iky.nrftouch.ui.main.MainActivity;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Ann on 2/28/16.
 */
public class DeviceFragment extends Fragment {

    @Bind(R.id.tvDeviceNameFragmentDevice)
    TextView tvDeviceNameFragmentDevice;
    @Bind(R.id.tvUserFragment)
    TextView tvUserFragment;
    @Bind(R.id.tvModelBike)
    TextView tvModelBike;
    @Bind(R.id.tvNumberBike)
    TextView tvNumberBike;
    @Bind(R.id.tvPINSmartkey)
    TextView tvPINSmartkey;


    @Inject
    DataManager dataManager;
    private IkyDevice mIkyDevice;
    @Inject
    Bus bus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ((BaseActivity) getActivity()).activityComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_user_device, container, false);
        ButterKnife.bind(this, fragmentView);
        ((MainActivity)getActivity()).showTabMenu(false);
        ((MainActivity)getActivity()).setTextTitle("Thông tin thiết bị");
        bus.register(this);
        ((MainActivity)getActivity()).getMainPresenter().getPinSmartkey();

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

                        if(ikyDevice != null && ikyDevice.getName() != null){
                            mIkyDevice = ikyDevice;
                            updateUI();
                            setNameDevice(ikyDevice.getName());
                        }
                    }
                });
        return fragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bus.unregister(this);
    }

    @OnClick(R.id.tvChangeDevice)
    void OnClickedChangeDevice(){
        dataManager.deleteIkyDevice()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        Intent ii = new Intent(getActivity(),SplashActivity.class);
                        getActivity().finish();
                        startActivity(ii);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }

                });
    }

    @OnClick(R.id.tvUserFragment)
    void OnClickedUser(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_devicefragment_changeuser, null);
        final EditText etNew = (EditText)view.findViewById(R.id.etNewNameChangeFragment);
        etNew.setText(mIkyDevice.getUsername());
        etNew.setSelection(etNew.getText().length());

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setTitle(getActivity().getString(R.string.change_user))
                .setView(view)
                // Add action buttons
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        mIkyDevice.setUsername(etNew.getText().toString());
                        saveIkyDevice(mIkyDevice);

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create().show();

    }

    @OnClick(R.id.tvModelBike)
    void OnClickedModel(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_devicefragment_changemodel, null);
        final EditText etNew = (EditText)view.findViewById(R.id.etNewNameChangeFragment);
        etNew.setText(mIkyDevice.getModelBike());
        etNew.setSelection(etNew.getText().length());


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setTitle(getActivity().getString(R.string.change_model))
                .setView(view)
                // Add action buttons
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        mIkyDevice.setModelBike(etNew.getText().toString());
                        saveIkyDevice(mIkyDevice);

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create().show();

    }

    @OnClick(R.id.tvNumberBike)
    void OnClickedNumber(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_devicefragment_changenumberbike, null);
        final EditText etNew = (EditText)view.findViewById(R.id.etNewNameChangeFragment);
        etNew.setText(mIkyDevice.getNumberBike());
        etNew.setSelection(etNew.getText().length());

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setTitle(getActivity().getString(R.string.change_number_bike))
                .setView(view)
                // Add action buttons
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        mIkyDevice.setNumberBike(etNew.getText().toString());
                        saveIkyDevice(mIkyDevice);

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create().show();

    }

    private void setNameDevice(String ss){
        tvDeviceNameFragmentDevice.setText(ss);
    }

    private void toast(final String ss){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), ss, Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void updateUI(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvUserFragment.setText(mIkyDevice.getUsername());
                tvModelBike.setText(mIkyDevice.getModelBike());
                tvNumberBike.setText(mIkyDevice.getNumberBike());
                String pinSmartkey = mIkyDevice.getPINSmartkey();
                if (TextUtils.isEmpty(pinSmartkey)) {
                    tvPINSmartkey.setText("Chưa xác định");
                }
                else {
                    tvPINSmartkey.setText(mIkyDevice.getPINSmartkey());
                }

            }
        });
    }

    public void saveIkyDevice(IkyDevice ikydevice){
        dataManager.setIkyDevice(ikydevice)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        toast("Thay đổi thành công.");
                        updateUI();

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }
                });
    }

    @Subscribe
    public void eventGetPinSmartkey(BusEvent.GetPinSmartkey getPinSmartkey){
        if(getPinSmartkey.isSuccess){
            boolean correct = mIkyDevice.getPINSmartkey().toString().equals(getPinSmartkey.pin);
            if(correct == false){
                mIkyDevice.setPINSmartkey(getPinSmartkey.pin);
                saveIkyDevice(mIkyDevice);
            }
        }
    }




}
