package lilin.com.componented_based_architecture;

import android.app.Application;

import lilin.com.arouter.ARouter;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ARouter.getInstance().init(this);
    }
}
