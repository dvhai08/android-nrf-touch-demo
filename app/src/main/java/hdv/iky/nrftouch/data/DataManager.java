package hdv.iky.nrftouch.data;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import hdv.iky.nrftouch.data.local.DatabaseHelper;
import hdv.iky.nrftouch.data.model.IkyDevice;
import hdv.iky.nrftouch.util.PreferencesHelper;
import rx.Observable;

/**
 * Created by Ann on 2/24/16.
 */
@Singleton
public class DataManager {
    private final DatabaseHelper mDatabaseHelper;
    private final PreferencesHelper mPreferencesHelper;


    @Inject
    public DataManager( DatabaseHelper databaseHelper,
                       PreferencesHelper preferencesHelper) {
        mDatabaseHelper = databaseHelper;
        mPreferencesHelper = preferencesHelper;
        
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public Observable<IkyDevice> findIkyDevices() {
        return mDatabaseHelper.findIkyDevice();
    }
    public Observable<Void> setIkyDevice(IkyDevice ikyDevice){
        List<IkyDevice> ikyDevices = new ArrayList<>();
        ikyDevices.add(ikyDevice);
        return mDatabaseHelper.setIkyDevices(ikyDevices);
    }
    public Observable<Void> deleteIkyDevice(){
        return mDatabaseHelper.deleteAllIkyDevices();
    }



}
