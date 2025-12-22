1. Можем ли мы через invoke вызывать статические методы? Если да, то как и почему. Если нет, то почему?

```
if (Modifier.isStatic(modifiers)) {
   method.invoke(null, generatedParams);
} else {
   method.invoke(obj, generatedParams);
}
```
Я добавила такую проверку. Маслакову понравилось 

2. Что такое```@Retention(RetentionPolicy.RUNTIME)```. Какие ещё есть варианты?
3. Что такое```@Target(ElementType.METHOD)```. Как добавать аннотацию для метода и пакета сразу?
4. ВОПРОС ДЛЯ СЕНЬОРОВ!!! 10 ЛЕТ НИКТО НЕ ОТВЕЧАЕТ:
```КАК АННОТИРОВАТЬ ПАКЕТ``` (НЕ ВСЁ ТАК ПРОСТО, МОЙ ДРУГ)

