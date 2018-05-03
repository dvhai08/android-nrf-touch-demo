package hdv.iky.nrftouch.ui.login;

import android.os.Bundle;

import hdv.iky.nrftouch.R;
import hdv.iky.nrftouch.ui.base.BaseActivity;

/**
 * Created by Ann on 2/27/16.
 */
public class LoginActivity extends BaseActivity implements LoginMvpView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
