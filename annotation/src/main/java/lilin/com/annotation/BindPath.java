package lilin.com.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//声明注解的作用域：注解放在什么地方
@Target(ElementType.TYPE)
//声明注解的生命周期
@Retention(RetentionPolicy.CLASS)
public @interface BindPath {
    String value();
}