package rs;

import org.eclipse.persistence.jaxb.rs.MOXyJsonProvider;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


// @ApplicationPath("/rs") will make it fail. Do not use slash here.
@ApplicationPath("rs")
public class ApplicationConfig extends Application {

    // ======================================
    // =             Attributes             =
    // ======================================

    private final Set<Class<?>> classes;

    // ======================================
    // =            Constructors            =
    // ======================================

    public ApplicationConfig() {
        HashSet<Class<?>> c = new HashSet<>();
        c.add(ConversionDTO.class);
        c.add(MOXyJsonProvider.class);

        classes = Collections.unmodifiableSet(c);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }

}