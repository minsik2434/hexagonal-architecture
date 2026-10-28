package tobyspring.splearn.support.stereotype;

import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Adapter
@Documented
@RestController
public @interface WebApiAdapter {
}

