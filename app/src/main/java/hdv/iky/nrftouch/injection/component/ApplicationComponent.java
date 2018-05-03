package hdv.iky.nrftouch.injection.component;

import android.app.Application;
import android.content.Context;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Component;
import hdv.iky.nrftouch.IKYSmartKeyApplication;
import hdv.iky.nrftouch.data.DataManager;
import hdv.iky.nrftouch.injection.ApplicationContext;
import hdv.iky.nrftouch.injection.module.ApplicationModule;
import hdv.iky.nrftouch.service.BluetoothLeService;
import hdv.iky.nrftouch.ui.control.ControlPresenter;
import hdv.iky.nrftouch.ui.main.MainPresenter;
import hdv.iky.nrftouch.ui.account.AccountPresenter;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(IKYSmartKeyApplication ikySmartKeyApplication);
    void inject(BluetoothLeService bluetoothLeService);
    void inject(ControlPresenter controlPresenter);
    void inject(MainPresenter mainPresenter);
    void inject(AccountPresenter statusPresenter);

    @ApplicationContext
    Context context();
    Application application();
//    EventPosterHelper eventPosterHelper();
    Bus eventBus();
    DataManager dataManager();

}
