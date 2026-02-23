package utils;
import animal.*;
import java.util.Collection;

public class ZooUtils {
    public static void segregate(
            Collection<? extends Animal> src,
            Class<?> c1, Collection<Animal> col1,
            Class<?> c2, Collection<Animal> col2,
            Class<?> c3, Collection<Animal> col3
    ) {
        for (Animal a : src) {
            Class<?> t = a.getClass();
            if (c1.isAssignableFrom(t)) {
                col1.add(a);
            }
            if (c2.isAssignableFrom(t)) {
                col2.add(a);
            }
            if (c3.isAssignableFrom(t)) {
                col3.add(a);
            }
        }
    }
}