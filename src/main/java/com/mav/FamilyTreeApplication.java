package com.mav;

import com.mav.famtree.FamilyTreeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
		final InputStream fileStream = FamilyTreeApplication.class.getResourceAsStream("/initial-data.txt");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(fileStream))) {
			String line;
			while ((line = br.readLine()) != null) {
				familyTreeService.processEntry(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
