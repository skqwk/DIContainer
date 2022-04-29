package injectinator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ClassesFinder {

    public static List<String> getAllClassPaths(String directory) throws IOException {
        return Files.walk(Paths.get(directory))
                .filter(Files::isRegularFile)
                .map(Path::toString)
                .filter(p -> p.endsWith(".class"))
                .map(p -> p.substring(directory.length() + 1))
                .collect(Collectors.toList());
    }

    public static List<Class<?>> loadClasses(List<String> classPaths) {
        return classPaths.stream()
                .map(c -> c.replaceAll("\\\\", "."))
                .map(c -> c.replace(".class", ""))
                .map(p -> {
                        try {
                            return Class.forName(p);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        return null;
                }
                )
                .collect(Collectors.toList());

    }

    public static List<Class<?>> loadAllClassesByPath(String directory) {
        List<Class<?>> loadedClasses = null;
        try {
            List<String> allClassPaths = getAllClassPaths(directory);
            loadedClasses = loadClasses(allClassPaths);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return loadedClasses;
    }

}
