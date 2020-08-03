package lilin.com.login;

import lilin.com.arouter.ARouter;
import lilin.com.arouter.IRouter;

/**
 * 这种方法可以实现将login模块所有activity添加map中，但是类多了，每次都要添加，删除，非常繁琐，麻烦
 * 而作为一个框架存在的意义就是让调用者少写代码，减少工作量，因此引入编译时技术
 * 编译时技术：编译时运行的代码，在编译时生成此ActivityUtil类
 * 1、注解：标记类
 * 2、注解处理器:识别、处理注解
 *
 */
public class ActivityUtil implements IRouter {
    @Override
    public void putActivity() {
        ARouter.getInstance().addActivity("login/login",LoginActivity.class);
    }
}
