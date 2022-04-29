package injectinator;

import annotation.Component;
import annotation.Inject;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Injectinator {
    private static final Map<Class<?>, Object> pool = new HashMap<>();
    private static ClassGraph graph;

    public static <T> T getInstance(Class<T> c) {
        T instance = null;
        try {
            instance = inject(c);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return instance;
    }


    public static void init(String directory) throws Exception {
        // Получаем все классы в пакете
        List<Class<?>> loadedClasses = ClassesFinder.loadAllClassesByPath(directory);


        // Отбираем только те, у которых аннотация @Component
        List<Class<?>> componentClasses = loadedClasses.stream()
                .filter(cl -> cl.isAnnotationPresent(Component.class))
                .collect(Collectors.toList());

        // Из тех, у которых аннотация @Component инстанцируем классы без аннотации @Inject
        List<Class<?>> simpleClasses = componentClasses.stream()
                .filter(cl ->
                        Arrays.stream(cl.getConstructors())
                                .noneMatch(constructor
                                        -> constructor.isAnnotationPresent(Inject.class)))
                .collect(Collectors.toList());


        simpleClasses.forEach(cl -> {
            try {
                pool.put(cl, cl.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Создаем граф классов с аннотацией @Component и производим топологическую сортировку
        graph = new ClassGraph(componentClasses);
        List<Class<?>> sortedClasses = graph.sort();

        // Проходимся по отсортированным классам, получаем их конструктор, отмеченный @Inject
        for (Class<?> sortedClass : sortedClasses) {
            for (Constructor<?> constructor : sortedClass.getConstructors()) {
                if (constructor.isAnnotationPresent(Inject.class)) {
                    injectViaConstructor(constructor, sortedClass);
                    pool.put(sortedClass, injectViaConstructor(constructor, sortedClass));
                }
            }
        }
    }

    private static  <T> T inject(Class<T> classToInject) throws Exception {
        T instance;

        if (classToInject.isInterface()) {
            Class<?> implementingClass = pool.keySet()
                    .stream().filter(classToInject::isAssignableFrom)
                    .findFirst()
                    .orElseThrow(()
                            -> new IllegalStateException(
                                    String.format("Среди компонентов не найден класс реализующий интерфейс",
                                            classToInject)));
            instance = (T) pool.get(implementingClass);
        }

        else {
            if (pool.containsKey(classToInject)) {
                instance = (T) pool.get(classToInject);
            } else {
                throw new IllegalStateException(
                        String.format("Класс %s не найден в пуле. Проверьте наличие аннотации @Component", classToInject));
            }
        }
        return instance;
    }

    private static  <T> T injectViaConstructor(Constructor<?> constructor, Class<T> classToInject) throws Exception {

        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] dependencies = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; ++i) {
            dependencies[i] = inject(parameterTypes[i]);
        }

        return classToInject.getConstructor(parameterTypes).newInstance(dependencies);
    }


    public static ClassGraph getGraph() {
        return graph;
    }

    public static List<Class<?>> getComponentClasses() {
        return new ArrayList<>(pool.keySet());
    }
}
