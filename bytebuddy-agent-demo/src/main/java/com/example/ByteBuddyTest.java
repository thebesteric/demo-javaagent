package com.example;

/**
 * @author Fox
 */
public class ByteBuddyTest {

    // -javaagent:/Users/keisun/IdeaProjects/research/javaagent/bytebuddy-agent/target/bytebuddy-agent-1.0.jar

    public static void main(String[] args) {
        HelloService helloService = new HelloService();
        helloService.say();
        helloService.say2();
    }
}
