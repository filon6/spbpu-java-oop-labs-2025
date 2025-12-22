package main;

import characters.Hero;
import strategy.FlyStrategy;
import strategy.HorseStrategy;
import strategy.WalkStrategy;
import utils.Point;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static final String WALK_CHOICE = "1";
    private static final String HORSE_CHOICE = "2";
    private static final String FLY_CHOICE = "3";
    private static final String EXIT_CHOICE = "4";

    private static void printMenu() {
        System.out.println("\nВыберите стратегию: ");
        System.out.println("1. Пешком");
        System.out.println("2. Верхом на лошади");
        System.out.println("3. Полет");
        System.out.println("4. Выход");
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Введите имя героя: ");
            String name = scanner.nextLine().trim();

            System.out.print("Задайте начальное положение героя (x, y): ");
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            scanner.nextLine();

            Hero hero = new Hero(name, new Point(x, y));
            boolean exit = false;

            while (!exit) {
                printMenu();
                System.out.print("Выбор: ");
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case WALK_CHOICE -> {
                        hero.setStrategy(new WalkStrategy());
                        System.out.println("Выбрана стратегия - Пешком");
                    }
                    case HORSE_CHOICE -> {
                        hero.setStrategy(new HorseStrategy());
                        System.out.println("Выбрана стратегия - Верхом на лошади");
                    }
                    case FLY_CHOICE -> {
                        hero.setStrategy(new FlyStrategy());
                        System.out.println("Выбрана стратегия - Полет");
                    }
                    case EXIT_CHOICE -> {
                        exit = true;
                        System.out.println("Выход из программы");
                        continue;
                    }
                    default -> {
                        System.out.println("Некорректный ввод");
                        continue;
                    }
                }

                System.out.print("Введите целевую точку (x, y): ");
                int newX = scanner.nextInt();
                int newY = scanner.nextInt();
                scanner.nextLine();

                hero.move(new Point(newX, newY));
            }
        }
        catch (InputMismatchException e) {
            System.out.println("Ошибка: " + e.getMessage());
            //e.printStackTrace(System.out);
        }
    }
}
