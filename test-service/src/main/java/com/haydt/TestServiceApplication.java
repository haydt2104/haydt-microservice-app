package com.haydt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.haydt")
public class TestServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestServiceApplication.class, args);
		int a = 0;
		int b = 0;
		for (int i = 0; i < 10; i++) {
			a += i;
			b += i * 5;
			System.out.println("a: " + a + ", b: " + b);
		}
	}

}
