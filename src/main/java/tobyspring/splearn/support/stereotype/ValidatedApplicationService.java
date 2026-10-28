package tobyspring.splearn.support.stereotype;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApplicationService
public @interface ValidatedApplicationService {
}
