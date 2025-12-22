package utils;

import annotation.MyAnnotation;

public class Book {
    private String tittle;
    private int pages;

    @MyAnnotation(repeat = 1)
    public void showTitle() {
        System.out.println("Название книги: " + tittle);
    }

    @MyAnnotation(repeat = 2)
    protected void setTitle(String tittle) {
        this.tittle = tittle;
        System.out.println("Установлено название: " + tittle);
    }

    @MyAnnotation(repeat = 3)
    private void setPages(int pages) {
        this.pages = pages;
        System.out.println("Установлено количество страниц: " + pages);
    }
}
