package lilin.com.annotation_processor;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import lilin.com.annotation.BindPath;

/**
 * 自定义注解处理器继承自AbstractProcessor
 * 目的：自动生成ActivityUtil工具类
 */


//重写方法和使用注解都可以
//@SupportedAnnotationTypes({"lilin.com.anotation.BindPath.class"})
//@SupportedSourceVersion(SourceVersion.RELEASE_8)

//注册注解处理器
@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor {

    //生成文件对象
    Filer filer;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.filer = processingEnvironment.getFiler();
    }

    /**
     * 声明注解处理器支持的java版本
     * @return SourceVersion
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 声明处理器须支持的类型
     * @return Set<String>
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> types = new HashSet<String>();
        types.add(BindPath.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //获取当前模块用到BindPath节点（类节点，方法节点、成员变量节点）
//        TypeElement 类节点
//        ExecutableElement  方法节点
//        VariableElement   成员变量节点
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(BindPath.class);
        HashMap<String, String> hashMap = new HashMap<>();
        for (Element element : elementsAnnotatedWith) {
            TypeElement typeElement = (TypeElement) element;

            //获取Activity的BindPath注解
            BindPath annotation = typeElement.getAnnotation(BindPath.class);
            String key = annotation.value();

            //获取activity的包名+类名
            Name activityName = typeElement.getQualifiedName();

            hashMap.put(key,activityName+".class");
        }

        if (hashMap.size()>0){

            System.out.println("----------开始生成代码----------");

            Writer writer = null;
            //需要生成的类名,各模块下类名不能重复
            String activityName = "ActivityUtil"+System.currentTimeMillis();

            //生成文件
            try {
                JavaFileObject sourceFile = filer.createSourceFile("lilin.com.util." + activityName);
                writer = sourceFile.openWriter();
                StringBuffer sb = new StringBuffer();
                sb.append("package lilin.com.util;\n");
                sb.append("import lilin.com.arouter.ARouter;\n" +
                        "import lilin.com.arouter.IRouter;\n" +
                        "\n" +
                        "public class "+activityName+" implements IRouter {\n" +
                        "\n" +
                        "    @Override\n" +
                        "    public void putActivity() {\n");

                Iterator<String> iterator = hashMap.keySet().iterator();
                while (iterator.hasNext()){
                    String key = iterator.next();
                    String className = hashMap.get(key);
                    sb.append("\t\tARouter.getInstance().addActivity(\""+key+"\","+className+");\n");
                }
                sb.append("\n\t}\n}");


                writer.write(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (writer !=null){
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("---------代码生成完毕------------");
                }
            }
        }

        return false;
    }
}