package hdv.iky.nrftouch.injection.component;

import dagger.Component;
import hdv.iky.nrftouch.injection.PerActivity;
import hdv.iky.nrftouch.injection.module.ActivityModule;
import hdv.iky.nrftouch.ui.ChangeNameFragment;
import hdv.iky.nrftouch.ui.ChangePINSmartkeyFragment;
import hdv.iky.nrftouch.ui.ChangePassFragment;
import hdv.iky.nrftouch.ui.DeviceFragment;
import hdv.iky.nrftouch.ui.InforFragment;
import hdv.iky.nrftouch.ui.SplashActivity;
import hdv.iky.nrftouch.ui.UserFragment;
import hdv.iky.nrftouch.ui.account.AccountFragment;
import hdv.iky.nrftouch.ui.control.ControlActivity;
import hdv.iky.nrftouch.ui.main.MainActivity;
import hdv.iky.nrftouch.ui.main_old.MainOldActivity;
import hdv.iky.nrftouch.ui.control_device.ControlDeviceFragment;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(ControlActivity controlActivity);
    
    void inject(MainActivity mainActivity);

    void inject(ControlDeviceFragment setupFragment);

    void inject(AccountFragment statusFragment);

    void inject(SplashActivity splashActivity);

    void inject(UserFragment userFragment);

    void inject(DeviceFragment deviceFragment);

    void inject(ChangePassFragment changePassFragment);

    void inject(MainOldActivity mainOldActivity);

    void inject(ChangeNameFragment changeNameFragment);

    void inject(InforFragment inforFragment);

    void inject(ChangePINSmartkeyFragment changePINSmartkeyFragment);
}

