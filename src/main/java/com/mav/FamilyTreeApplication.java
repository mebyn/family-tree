package com.mav;

import com.mav.famtree.FamilyTreeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

@SpringBootApplication
public class FamilyTreeApplication {

	private static final String EXIT_COMMAND = "-1";
	private static FamilyTreeService familyTreeService;

	public static void main(String[] args) {
		final ConfigurableApplicationContext context = SpringApplication.run(FamilyTreeApplication.class, args);
		familyTreeService = context.getBean(FamilyTreeService.class);
		System.out.println("Welcome to the Family Tree application!");
		String entry = "";

		populateInitialData();

		while (!entry.equals(EXIT_COMMAND)) {
			Scanner systemInput = new Scanner(System.in);
			System.out.print("Input: ");
			entry = systemInput.nextLine();
			if (entry.equals(EXIT_COMMAND)) {
				systemInput.close();
				break;
			}
			familyTreeService.processEntry(entry);
		}

	}

	private static void populateInitialData() {
		ClassLoader classLoader = FamilyTreeApplication.class.getClassLoader();
		File csvFile = new File(Objects.requireNonNull(classLoader.getResource("initial-data.txt")).getFile());
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				familyTreeService.processEntry(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
