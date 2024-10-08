package com;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;

public class CurrencyConverter {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Type currency to convert from:");
        String convertFrom = scanner.nextLine();
        System.out.println("Type currency to convert to:");
        String convertTo = scanner.nextLine();
        System.out.println("Type quantity to convert:");
        BigDecimal quantity = scanner.nextBigDecimal();

        // Updated URL to use the correct Frankfurter API base URL
        String urlString = "https://api.frankfurter.app/latest?from=" + convertFrom.toUpperCase();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlString)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String stringResponse = response.body().string();
            JSONObject jsonObject = new JSONObject(stringResponse);
            JSONObject ratesObject = jsonObject.getJSONObject("rates");

            // Ensure the currency to convert to exists
            if (!ratesObject.has(convertTo.toUpperCase())) {
                System.out.println("Invalid currency code: " + convertTo.toUpperCase());
                return;
            }

            BigDecimal rate = ratesObject.getBigDecimal(convertTo.toUpperCase());
            BigDecimal result = rate.multiply(quantity);
            System.out.println("Conversion result: " + result);

        } catch (IOException e) {
            System.out.println("Error occurred during conversion: " + e.getMessage());
        }
    }
}
