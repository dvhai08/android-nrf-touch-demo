package hdv.iky.nrftouch.ui.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import hdv.iky.nrftouch.IKYSmartKeyApplication;
import hdv.iky.nrftouch.data.BusEvent;
import hdv.iky.nrftouch.data.DataManager;
import hdv.iky.nrftouch.data.Protocol;
import hdv.iky.nrftouch.data.model.IkyDevice;
import hdv.iky.nrftouch.injection.ApplicationContext;
import hdv.iky.nrftouch.service.BluetoothLeService;
import hdv.iky.nrftouch.ui.base.BasePresenter;
import hdv.iky.nrftouch.util.AndroidComponentUtil;
import hdv.iky.nrftouch.util.CommonUtils;
import hdv.iky.nrftouch.util.EventPosterHelper;
import hdv.iky.nrftouch.util.NotificationHelper;
import hdv.iky.nrftouch.util.PreferencesHelper;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Ann on 2/26/16.
 */
public class MainPresenter extends BasePresenter<MainMvpView> {
    private static final String TAG = "MainPresenter";


    @Inject
    PreferencesHelper preferencesHelper;
    @Inject
    Bus mEventBus;
    @Inject
    NotificationHelper notificationHelper;

    @Inject
    EventPosterHelper eventPosterHelper;

    private final DataManager dataManager;
    private Context context;
    private BluetoothLeService mBluetoothLeService;
    private IkyDevice mIkyDevice;

    @Inject
    public MainPresenter(@ApplicationContext Context context, DataManager dataManager){
        IKYSmartKeyApplication.get(context).getComponent().inject(this);
        this.dataManager = dataManager;
        this.context = context;

    }


    @Override
    public void attachView(MainMvpView mvpView) {
        mEventBus.register(this);
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        mEventBus.unregister(this);
        if(AndroidComponentUtil.isServiceRunning(context,BluetoothLeService.class)) {
            context.unbindService(mServiceConnection);
        }
        super.detachView();
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                if(isViewAttached()){
                    getMvpView().showError("Unable to initialize Bluetooth");
                }
            }
            getIkyDevice();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    public void bindService() {
        Intent gattServiceIntent = new Intent(context, BluetoothLeService.class);
        context.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }


    private void deleteIkyDevice(){
        dataManager.deleteIkyDevice()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        if(isViewAttached()){
                            getMvpView().errorPin();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }

                });


    }
    private void getIkyDevice(){
        dataManager.findIkyDevices()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<IkyDevice>() {
                    @Override
                    public void onCompleted() {
                        if(mIkyDevice == null && isViewAttached()){
                            getMvpView().toast("Error");
                        }else {
                            connect();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().toast("IkyDevice Error");

                    }

                    @Override
                    public void onNext(IkyDevice ikyDevice) {

                        mIkyDevice = ikyDevice;

                    }
                });


    }

    public void connect(){
        if(mIkyDevice != null && mIkyDevice.getAddress() != null) {
            mBluetoothLeService.connect(mIkyDevice.getAddress());
        }else{
            if(isViewAttached()){
                getMvpView().showError("Cannot connect Iky device");
            }
        }
    }

    public void logon(){

        mBluetoothLeService.writeRXCharacteristic(Protocol.logOn(mIkyDevice.getPin()));

    }
    public void readVersion(){

        mBluetoothLeService.writeRXCharacteristic(Protocol.version(mIkyDevice.getPin()));
    }

    public void openTrunk(){

        mBluetoothLeService.writeRXCharacteristic(Protocol.openTrunk(mIkyDevice.getPin()));
    }


    public void configureRSSI(byte valueTurnOn, byte valueTurnOff){

        mBluetoothLeService.writeRXCharacteristic(Protocol.configureRSSI(mIkyDevice.getPin(), valueTurnOn, valueTurnOff));
    }

    public void startEmergency(byte valuue){

        mBluetoothLeService.writeRXCharacteristic(Protocol.startEmergency(mIkyDevice.getPin(), valuue));
    }
    public void findBike(){

        mBluetoothLeService.writeRXCharacteristic(Protocol.findbyke(mIkyDevice.getPin()));
    }

    public void rename(String newName){
        mBluetoothLeService.writeRXCharacteristic(Protocol.rename(mIkyDevice.getPin(), newName));
    }

    public void changePin(String newPin){
        mBluetoothLeService.writeRXCharacteristic(Protocol.changePin(mIkyDevice.getPin(), newPin));
    }

    public void changePinSmartkey(String newPin){
        mBluetoothLeService.writeRXCharacteristic(Protocol.changePinSmartkey(mIkyDevice.getPin(), newPin));
    }

    public void getPinSmartkey(){
        mBluetoothLeService.writeRXCharacteristic(Protocol.getPinSmartkey(mIkyDevice.getPin()));
    }

    public void lock(boolean lockOn){
        if(lockOn){
            mBluetoothLeService.writeRXCharacteristic(Protocol.setLockOn(mIkyDevice.getPin()));
        }else{
            mBluetoothLeService.writeRXCharacteristic(Protocol.setLockOff(mIkyDevice.getPin()));
        }
    }

    public void control_output(byte deviceNum, byte status){
            mBluetoothLeService.writeRXCharacteristic(Protocol.control__output(mIkyDevice.getPin(), deviceNum, status));
    }

    public void sendCommandReadStatus(){
        if(mIkyDevice != null) {
            mBluetoothLeService.writeRXCharacteristic(Protocol.readStatus(mIkyDevice.getPin()));
        }
    }


    //Subscribe event bus, what is sent from Ble BluetoohLeService.class
    @Subscribe
    public void eventReceived(BusEvent.ReceiveData event){

        byte[] bytes = event.values;
        Log.d(TAG, "eventReceived " + CommonUtils.convertByteToString(bytes));

        Log.d(TAG, "bytes[1] " + String.format("%X", bytes[1]));

        if(bytes[Protocol.OPCODE_OFFSET] == Protocol.OPCODE_READSTATUS){
            //CA 84 7 F0 DE C7 81 0 0 63 79
            if(bytes.length > 9) {
                try {
                    BusEvent.UpdateStatus eventUpdateStatus = new BusEvent.UpdateStatus();
                    eventUpdateStatus.bDev1Status = bytes[7] != 0;

                    eventUpdateStatus.bDev2Status = bytes[8] != 0;

                    eventUpdateStatus.bDev3Status = bytes[9] != 0;

                    eventPosterHelper.postEventSafely(eventUpdateStatus);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }else if(bytes[Protocol.OPCODE_OFFSET] == Protocol.APP_OPCODE_LOGON){
            try {
                byte statusCode = bytes[3];
                if (statusCode != Protocol.STATUS_CODE_SUCCESS) {
                    deleteIkyDevice();
                }else{
                    sendCommandReadStatus();
                }
            }catch (Exception e){
                e.printStackTrace();
                deleteIkyDevice();
            }

        }else if(bytes[Protocol.OPCODE_OFFSET] == Protocol.APP_OPCODE_RENAME){
            BusEvent.UpdateName updateName = new BusEvent.UpdateName();
            try {
                byte statusCode = bytes[3];
                updateName.isSuccess = statusCode == Protocol.STATUS_CODE_SUCCESS;
            }catch (Exception e){
                e.printStackTrace();
                updateName.isSuccess = false;
            }
            eventPosterHelper.postEventSafely(updateName);

        }else if(bytes[Protocol.OPCODE_OFFSET] == Protocol.APP_OPCODE_CHANGEPIN){
            BusEvent.UpdatePin updatePin = new BusEvent.UpdatePin();
            try {
                byte statusCode = bytes[3];
                updatePin.isSuccess = statusCode == Protocol.STATUS_CODE_SUCCESS;
            }catch (Exception e){
                e.printStackTrace();
                updatePin.isSuccess = false;
            }
            eventPosterHelper.postEventSafely(updatePin);
        }else if(bytes[Protocol.OPCODE_OFFSET] == Protocol.OPCODE_VERSION){

            //0xCA 0x8F 0x05 0x31 0x2E 0x30 0x2E 0x38 0xF5
            if(bytes.length > 5 ){
                byte[] version = new byte[bytes.length - 4];
                for (int i = 3; i < bytes.length - 1; i++) {
                    version[i-3] = bytes[i];

                }
                String sVersion = new String(version);
                eventPosterHelper.postEventSafely(new BusEvent.EventVersion(sVersion));
            }
        }

        if(isViewAttached()){
            getMvpView().setReceiveData(CommonUtils.convertByteToString(bytes));
        }
    }

    @Subscribe
    public void eventConnect(BusEvent.Connect event){
        if(isViewAttached()){
            getMvpView().hideLoading();
            if(event.state == BluetoothLeService.STATE_CONNECTED){
                getMvpView().setNameDevice("Connected");
            }else if(event.state == BluetoothLeService.STATE_CONNECTING){
                getMvpView().setNameDevice("Connecting");
            }else if(event.state == BluetoothLeService.STATE_DISCONNECTED){
                getMvpView().setNameDevice("Disconnect");
                notificationHelper.show("Disconnected");
            }else if(event.state == BluetoothLeService.STATE_DISCONVERED){
//
                notificationHelper.show("Connected");
                logon();
            }
        }
    }


    @Subscribe
    public void eventSendData(BusEvent.SendData event){
        if(isViewAttached()){
            getMvpView().hideLoading();
            getMvpView().setSendData(CommonUtils.convertByteToString(event.msg));
        }
    }

    @Subscribe
    public void eventError(BusEvent.ShowError event){
        if(isViewAttached()){
            getMvpView().hideLoading();
            getMvpView().showError(event.message);
        }
    }






}
