package lilin.com.arouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dalvik.system.DexFile;

/**
 * 1、写框架的目的就是为了少写一些代码
 * 路由/中间人/代理
 * 需要持有所有模块activity/fragment的引用
 */
public class ARouter {
    private static ARouter aRouter = new ARouter();

    //存放Activity
    private Map<String,Class<? extends Activity>> map;

    private Context context;

    private ARouter(){
        map =new HashMap<>();
    }

    public static ARouter getInstance() {
        return aRouter;
    }

    public void init(Context context){
        this.context = context;
        List<String> classNames = getClassNames("lilin.com.util");
        for (String className : classNames) {
            try {
                Class<?> aClass = Class.forName(className);
                //判断这个类是否是IRouter的子类
                if (IRouter.class.isAssignableFrom(aClass)){
                    IRouter iRouter = (IRouter) aClass.newInstance();
                    iRouter.putActivity();
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private List<String> getClassNames(String packageName) {
        List<String> classList =new ArrayList<>();
        String path;
        try {
            path = context.getPackageManager().getApplicationInfo(context.getPackageName(),0).sourceDir;

            //根据apk的完整路径获取编译后的dex文件目录
            DexFile dexFile = new DexFile(path);

            //获得编译后的dex文件中的所有的class
            Enumeration<String> entries = dexFile.entries();

            //遍历所有class的包名
            while (entries.hasMoreElements()){
                String name = entries.nextElement();
                if (name.contains(packageName)){
                    classList.add(name);
                }
            }
        } catch (PackageManager.NameNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return classList;
    }


    public void addActivity(String key,Class<? extends Activity> clazz){
        if (key!=null && clazz!=null && !map.containsKey(key)){
            map.put(key, clazz);
        }
    }

    /**
     * 跳转到指定activity
     */
    public void jumpActivity(String key, Bundle bundle){
        Class<? extends Activity> aClass = map.get(key);
        if (aClass !=null){
            Intent intent = new Intent().setClass(context, aClass);
            if (bundle!=null){
                intent.putExtras(bundle);
            }
            context.startActivity(intent);
        }
    }
}
