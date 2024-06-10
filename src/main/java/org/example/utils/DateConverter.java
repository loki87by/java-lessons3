package org.example.utils;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateConverter {

    public static Instant convertToInstant(String dateStr) {
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("dd-MMM-yyyy"),
                DateTimeFormatter.ofPattern("dd MMMM yyyy"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("d.MM.yyyy"),
                DateTimeFormatter.ofPattern("yyyy.MM.dd")
        };

        for (DateTimeFormatter formatter : formatters) {
            try {
                LocalDate date = LocalDate.parse(dateStr, formatter);
                return date.atStartOfDay(ZoneId.systemDefault()).toInstant();
            } catch (DateTimeParseException e) {
                // Continue to the next format
            }
        }

        throw new IllegalArgumentException("Unparseable date: " + dateStr);
    }
/*
    public static void main(String[] args) {
        String inputDate = "12-11-2023";
        Instant result = convertToInstant(inputDate);
        System.out.println(result);
    }*/
}
