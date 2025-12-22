1. Можем ли мы в интерфейсе объявить статический класс, если да то как?
```
public interface MoveStrategy {
   void move(Point from, Point to);
   
   public static class User {
   
   }
}
```
Почему `public static` горит серым в IDE?

2. Класс Hero имеет поле:
   `private Point position;`

Что такое final? Что будет если мы добавим final. Сможем ли мы изменить position, если у нас будет `private final Point position;`
Если да, то почему и как (продемонстрируйте). Если нет, то почему?

3. Какие исключения есть в Java? Почему выбрасывается именно `InputMismatchException`?

4. Как в одном классе вызвать один конструктор в другом? (тяжело сформулировать, но ниже ответ)

У нас есть конструктор:
```
 public Hero(String name, Point position) {
     this.name = name;
     this.position = position;
 }
```

Мы прописываем так:
```
public Hero() {
     this("Bob", new Point(5,5));
 }
```
Будет вызван конструктор с параметрами, объявленный выше