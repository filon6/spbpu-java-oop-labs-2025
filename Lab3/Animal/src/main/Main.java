package main;

import animal.*;
import java.util.*;

import static utils.ZooUtils.segregate;

public class Main {
    private static void print(Collection<Animal> c1, Collection<Animal> c2, Collection<Animal> c3) {
        System.out.println("Collection1: " + c1);
        System.out.println("Collection2: " + c2);
        System.out.println("Collection3: " + c3);
    }

    public static void main(String[] args) {
        List<Animal> src = List.of(
                new Hedgehogs("Еж1"),
                new Manul("Манул1"),
                new Lynx("Рысь1"),
                new Hedgehogs("Еж2"),
                new Lynx("Рысь2")
        );

        List<Animal> c1 = new ArrayList<>();
        List<Animal> c2 = new ArrayList<>();
        List<Animal> c3 = new ArrayList<>();

        System.out.println("Вызов 1:");
        segregate(src,
                Hedgehogs.class, c1,
                Manul.class, c2,
                Lynx.class, c3
        );
        print(c1, c2, c3);
        c1.clear(); c2.clear(); c3.clear();

        System.out.println("\nВызов 2:");
        segregate(src,
                Predatory.class, c1,
                Manul.class, c2,
                Feline.class, c3
        );
        print(c1, c2, c3);
        c1.clear(); c2.clear(); c3.clear();

        System.out.println("\nВызов 3:");
        segregate(src,
                Hedgehogs.class, c1,
                Insectivores.class, c2,
                Predatory.class, c3
        );
        print(c1, c2, c3);
    }
}
