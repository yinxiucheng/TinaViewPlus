package tina.com.tinaviewplus;

import android.app.Application;
import android.content.Context;

/**
 * @author yxc
 * @date 2018/7/18
 */
public class MyApplication extends Application {

    public static Context sContext;

    public MyApplication(){
        sContext = this;
    }

    public static Context getContext(){
        return sContext;
    }

}
