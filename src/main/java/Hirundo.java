import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;


/**
 * Created by vkonov on 2/8/16.
 */
@Configuration
public class Hirundo {
    public static void main(String[] args){
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Hirundo.class);
        new HirundoService();
        ctx.registerShutdownHook();
    }
}
