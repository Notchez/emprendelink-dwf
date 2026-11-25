package emprendelink.spring.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** GlassFish owns the WAR lifecycle; Spring owns only the explicitly registered beans. */
public class SpringRuntime implements ServletContextListener {
    private static final String KEY = SpringRuntime.class.getName();

    @Override public void contextInitialized(ServletContextEvent event) {
        var context = new AnnotationConfigApplicationContext();
        try {
            context.register(SpringConfig.class);
            context.refresh();
            event.getServletContext().setAttribute(KEY, context);
        } catch (RuntimeException ex) {
            context.close();
            throw ex;
        }
    }

    @Override public void contextDestroyed(ServletContextEvent event) {
        var servlet = event.getServletContext();
        Object value = servlet.getAttribute(KEY);
        servlet.removeAttribute(KEY);
        if (value instanceof AnnotationConfigApplicationContext context) context.close();
    }

    public static <T> T bean(ServletContext servlet, Class<T> type) {
        Object value = servlet.getAttribute(KEY);
        if (!(value instanceof AnnotationConfigApplicationContext context) || !context.isActive()) {
            throw new IllegalStateException("Spring context unavailable");
        }
        return context.getBean(type);
    }
}
