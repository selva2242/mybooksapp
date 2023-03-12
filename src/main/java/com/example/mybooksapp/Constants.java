package com.example.mybooksapp;

import org.json.JSONObject;

public class Constants {
  public static final String REQUESTS_JSON =
      "{\n" +
          "  \"accounting\" : [\n" +
          "    {\n" +
          "      \"name\": \"Review Sales\",\n" +
          "      \"id\": \"reviewSales\",\n" +
          "      \"deadline\": 2,\n" +
          "      \"requiredHours\": 2\n" +
          "    },\n" +
          "    {\n" +
          "      \"name\": \"Review Purchases\",\n" +
          "      \"id\": \"reviewPurchases\",\n" +
          "      \"deadline\": 2,\n" +
          "      \"requiredHours\": 2\n" +
          "    },\n" +
          "    {\n" +
          "      \"name\": \"Compute Tax\",\n" +
          "      \"id\": \"computeTax\",\n" +
          "      \"deadline\": 2,\n" +
          "      \"requiredHours\": 3\n" +
          "    },\n" +
          "    {\n" +
          "      \"name\": \"Add Tax to Books\",\n" +
          "      \"id\": \"addTax\",\n" +
          "      \"deadline\": 2,\n" +
          "      \"requiredHours\": 1\n" +
          "    }\n" +
          "  ]\n" +
          "}";

  public static final JSONObject REQUEST_TYPES = getRequestTypes(REQUESTS_JSON);

  private static JSONObject getRequestTypes(String json) {
    JSONObject jsonObject = new JSONObject(json);
    return jsonObject;
  }
}
