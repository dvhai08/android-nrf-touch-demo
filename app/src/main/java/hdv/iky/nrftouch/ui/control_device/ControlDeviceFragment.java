package hdv.iky.nrftouch.ui.control_device;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hdv.iky.nrftouch.R;
import hdv.iky.nrftouch.ui.base.BaseActivity;
import hdv.iky.nrftouch.ui.main.MainActivity;

/**
 * Created by Ann on 2/27/16.
 */
public class ControlDeviceFragment extends Fragment implements ControlDeviceMvpView {

    @Bind(R.id.ibFragmentControlDev1)
    ImageButton ibFragmentControlDev1;

    @Bind(R.id.ibFragmentControlDev2)
    ImageButton ibFragmentControlDev2;

    @Bind(R.id.ibFragmentControlDev3)
    ImageButton ibFragmentControlDev3;

    @Bind(R.id.ivStatusConnect)
    ImageView ivStatusConnect;

    @Inject
    ControlDevicePresenter setupPresenter;

    private boolean bDev1Status = true;
    private boolean bDev2Status = true;
    private boolean bDev3Status = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ((BaseActivity) getActivity()).activityComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_control, container, false);
        ButterKnife.bind(this, fragmentView);

        setupPresenter.attachView(this);

        if (((MainActivity) getActivity()).getMainPresenter() != null) {
            ((MainActivity) getActivity()).getMainPresenter().sendCommandReadStatus();
        }

        return fragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        setupPresenter.detachView();
    }

    @OnClick(R.id.ibFragmentControlDev1)
    void onClickedControlDev1()
    {
        if(bDev1Status)
        {
            ((MainActivity)getActivity()).getMainPresenter().control_output((byte)0x01, (byte)0x00);
        }
        else
        {
            ((MainActivity) getActivity()).getMainPresenter().control_output((byte)0x01, (byte)0x01);
        }
    }

    @OnClick(R.id.ibFragmentControlDev2)
    void onClickedControlDev2()
    {
        if(bDev2Status)
        {
            ((MainActivity)getActivity()).getMainPresenter().control_output((byte)0x02, (byte)0x00);
        }
        else
        {
            ((MainActivity) getActivity()).getMainPresenter().control_output((byte)0x02, (byte)0x01);
        }
    }

    @OnClick(R.id.ibFragmentControlDev3)
    void onClickedControlDev3()
    {
        if(bDev3Status)
        {
            ((MainActivity)getActivity()).getMainPresenter().control_output((byte)0x03, (byte)0x00);
        }
        else
        {
            ((MainActivity) getActivity()).getMainPresenter().control_output((byte)0x03, (byte)0x01);
        }
    }

    @Override
    public void updateStatusDevice(boolean bDev1,boolean bDev2,boolean bDev3) {
        bDev1Status = bDev1;
        bDev2Status = bDev2;
        bDev3Status = bDev3;

        if (bDev1) {
            ibFragmentControlDev1.setImageResource(R.drawable.ic_status_on);
        } else {
            ibFragmentControlDev1.setImageResource(R.drawable.ic_status_off);
        }

        if (bDev2) {
            ibFragmentControlDev2.setImageResource(R.drawable.ic_status_on);
        } else {
            ibFragmentControlDev2.setImageResource(R.drawable.ic_status_off);
        }

        if (bDev3) {
            ibFragmentControlDev3.setImageResource(R.drawable.ic_status_on);
        } else {
            ibFragmentControlDev3.setImageResource(R.drawable.ic_status_off);
        }

        updateStatusConnecttion(true);
    }

    @Override
    public void updateStatusConnecttion(boolean value) {
        if(value){
            ivStatusConnect.setImageResource(R.drawable.ic_connect);
        }else {
            ivStatusConnect.setImageResource(R.drawable.ic_disconnect);
        }
    }
}
