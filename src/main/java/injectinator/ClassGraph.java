package injectinator;

import annotation.Inject;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassGraph {
    private final List<Class<?>> sorted = new ArrayList<>();
    private final Set<Class<?>> addedNodes = new HashSet<>();
    private final Map<Class<?>, List<Class<?>>> graph = new HashMap<>();

    public ClassGraph(List<Class<?>> classes) {
        fillGraph(classes);
    }

    private void fillGraph(List<Class<?>> classes) {
        for (Class<?> cl : classes) {
            for (Constructor<?> constructor : cl.getConstructors()) {
                if (constructor.isAnnotationPresent(Inject.class)) {
                    graph.put(cl, List.of(constructor.getParameterTypes()));
                }
            }
        }
    }

    public void showDependencies() {
        for (Map.Entry<Class<?>, List<Class<?>>> classDeps : graph.entrySet()) {
            System.out.println(classDeps.getKey().getSimpleName());
            for (Class<?> node : classDeps.getValue()) {
                System.out.println("\t" + node.getSimpleName());
            }
        }
    }

    public List<Class<?>> sort() {
        while (sorted.size() != graph.size()) {
            for (Class<?> node : graph.keySet()) {
                if (!addedNodes.contains(node)) {
                    addedNodes.add(node);
                    checkChildren(graph.get(node), node);
                }
            }
        }
        return sorted;
    }

    private void checkChildren(List<Class<?>> tree, Class<?> head) {
        addedNodes.add(head);
        if (!sorted.contains(head)) {
            for (Class<?> node : tree) {
                if (graph.containsKey(node)) {
                    checkChildren(graph.get(node), node);
                }
            }
            sorted.add(head);
        }
    }
}
