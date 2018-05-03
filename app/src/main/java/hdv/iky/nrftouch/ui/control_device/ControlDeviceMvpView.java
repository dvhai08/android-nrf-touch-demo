package hdv.iky.nrftouch.ui.control_device;

import hdv.iky.nrftouch.ui.base.MvpView;

/**
 * Created by Ann on 2/27/16.
 */
public interface ControlDeviceMvpView extends MvpView {

    void updateStatusLock(boolean value);

    void updateStatusConnecttion(boolean value);
}
