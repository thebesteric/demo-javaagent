package com.example;

/**
 * @author Fox
 */

public class HelloService {


    public String say() {
        System.out.println("===say====");
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello world";
    }

    public String say2() {
        System.out.println("===say2====");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello world";
    }

}
