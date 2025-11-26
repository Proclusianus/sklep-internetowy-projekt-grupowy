package wat.grupa.trzy.wielkieakcjeitransakcje.other.security; // lub inny pakiet konfiguracyjny

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final NoCacheInterceptor noCacheInterceptor;

    @Autowired
    public WebConfig(NoCacheInterceptor noCacheInterceptor) {
        this.noCacheInterceptor = noCacheInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Stosuj nasz interceptor tylko do stron logowania i rejestracji
        registry.addInterceptor(noCacheInterceptor).addPathPatterns("/login", "/register");
    }
}