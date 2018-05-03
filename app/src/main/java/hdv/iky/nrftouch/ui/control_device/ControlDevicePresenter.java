package hdv.iky.nrftouch.ui.control_device;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import hdv.iky.nrftouch.data.BusEvent;
import hdv.iky.nrftouch.data.DataManager;
import hdv.iky.nrftouch.service.BluetoothLeService;
import hdv.iky.nrftouch.ui.base.BasePresenter;

/**
 * Created by Ann on 2/27/16.
 */
public class ControlDevicePresenter extends BasePresenter<ControlDeviceMvpView> {

    DataManager dataManager;

    @Inject
    Bus mEventBus;

    @Inject
    public ControlDevicePresenter(DataManager dataManager) {

        this.dataManager = dataManager;

    }

    @Override
    public void attachView(ControlDeviceMvpView mvpView) {
        super.attachView(mvpView);
        mEventBus.register(this);
    }

    @Override
    public void detachView() {
        super.detachView();
        mEventBus.unregister(this);
    }

    @Subscribe
    public void eventUpdateStatus(BusEvent.UpdateStatus event){
        if(isViewAttached()) {
            getMvpView().updateStatusLock(event.bLock);
        }
    }

    @Subscribe
    public void eventConnect(BusEvent.Connect event){
        if(isViewAttached()) {
            if (event.state == BluetoothLeService.STATE_DISCONVERED) {
                getMvpView().updateStatusConnecttion(true);
            } else if (event.state == BluetoothLeService.STATE_DISCONNECTED) {
                getMvpView().updateStatusConnecttion(false);
            }
        }
    }

}
