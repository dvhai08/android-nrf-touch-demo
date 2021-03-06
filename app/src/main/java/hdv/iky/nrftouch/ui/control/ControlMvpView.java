package hdv.iky.nrftouch.ui.control;

import hdv.iky.nrftouch.ui.base.MvpView;

/**
 * Created by Ann on 1/29/16.
 */
public interface ControlMvpView extends MvpView{

    void showLoading();

    void hideLoading();

    void showError(String error);

    void setNameDevice(String device);

    void setSendData(String sendData);

    void setReceiveData(String rssi);

    void toast(String ss);

    void updateImageForStatus(byte lock, byte vibrate);

    void showDialogAddDevice();

    void showDialogSettings();

    void showDialogRename();

    void showDialogChangePin();

}
