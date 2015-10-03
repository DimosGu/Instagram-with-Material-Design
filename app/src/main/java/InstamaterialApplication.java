import android.app.Application;

import timber.log.Timber;


/**
 * Created by 10000_hours on 2015/10/3.
 */
public class InstamaterialApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // TODO: 2015/10/3  去百度了解学习一下timber
        Timber.plant(new Timber.DebugTree());
    }
}
