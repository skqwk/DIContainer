import annotation.ConfigProperty;
import injectinator.Injectinator;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Properties;

public class Configurator {

    static private final Properties properties = new Properties();

    public static void configure(List<Class<?>> classes) throws IllegalAccessException, IOException {

        for (Class<?> c : classes) {
            System.out.println("Try configure: " + c.getSimpleName());
            for (Field field : c.getDeclaredFields()) {
                if (field.isAnnotationPresent(ConfigProperty.class)) {
                    System.out.println(c.getSimpleName() + " contains @ConfigProperty");
                    ConfigProperty property = field.getAnnotation(ConfigProperty.class);
                    field.setAccessible(true);

                    properties.load(new FileInputStream(property.configFileName()));
                    if (Modifier.isStatic(field.getModifiers())) {
                        System.out.println(c.getSimpleName() + "." + field.getName() + " is static");
                        field.set(null, properties.getProperty(property.propertyName()));
                    } else {
                        Object o = Injectinator.getInstance(c);

                        String propertyName = property.propertyName().equals("null")
                                ? String.format("%s.%s", c.getSimpleName(),field.getName())
                                : property.propertyName();
                        String value = properties.getProperty(propertyName);
                        field.set(o, property.type().parse.apply(value));
                    }
                }
            }
        }


    }

}
