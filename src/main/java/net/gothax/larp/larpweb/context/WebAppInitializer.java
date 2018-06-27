package net.gothax.larp.larpweb.context;

import org.springframework.context.annotation.Import;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.EnumSet;

@Import({WebSecurityConfig.class, WebMvcConfig.class})
public class WebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(WebMvcConfig.class);

        servletContext.addListener(new ContextLoaderListener(ctx));

        FilterRegistration encodingFilter = servletContext.addFilter("encodingFilter", CharacterEncodingFilter.class);
        encodingFilter.setInitParameter("encoding", "UTF-8");
        encodingFilter.addMappingForUrlPatterns(null, true, "/*");

        FilterRegistration springSecureFilterChain = servletContext.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class);
        springSecureFilterChain.addMappingForUrlPatterns(null, true, "/*");
        springSecureFilterChain.addMappingForUrlPatterns(EnumSet.of(DispatcherType.FORWARD), true, "/login/auth");

        ServletRegistration.Dynamic larp = servletContext.addServlet("larp", new DispatcherServlet(ctx));
        larp.setLoadOnStartup(1);
        larp.addMapping("/");
    }
}
