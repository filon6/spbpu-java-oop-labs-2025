package utils;

import annotation.MyAnnotation;

public class OfficeWorker {
    @MyAnnotation(repeat = 1)
    public void writeReport(String topic) {
        System.out.println("Составлен отчет на тему: " + topic);
    }

    public void attendMeeting(String topic) {
        System.out.println("Совещание на тему: " + topic);
    }

    @MyAnnotation(repeat = 3)
    protected void makeCoffee(int cups){
        System.out.println("Приготовлено " + cups + " чашек кофе");
    }

    protected void checkEmails(int count){
        System.out.println("Проверено " + count + " писем");
    }

    @MyAnnotation(repeat = 4)
    private void organizeFiles(String folder) {
        System.out.println("Файлы в папке \"" + folder + "\" упорядочены.");
    }

    @MyAnnotation(repeat = 2)
    private static void print() {
        System.out.println("Привет");
    }
}
