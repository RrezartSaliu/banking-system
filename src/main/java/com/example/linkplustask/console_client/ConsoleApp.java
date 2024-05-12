package com.example.linkplustask.console_client;

import com.google.gson.*;

import javax.script.ScriptContext;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class ConsoleApp {

    public static void showListOfBanks(String url) throws IOException {
        URL apiUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader= new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null){
            response.append(line);
        }

        String jsonResponse = response.toString();
        JsonArray jsonArray = JsonParser.parseString(String.valueOf(jsonResponse)).getAsJsonArray();

        for(JsonElement jsonElement: jsonArray){
            JsonObject bank = jsonElement.getAsJsonObject();
            int bankId = bank.get("bankId").getAsInt();
            String bankName = bank.get("name").getAsString();
            System.out.println(bankId +") "+ bankName);
        }

        reader.close();
        connection.disconnect();
    }

    public static void createBank(Scanner scanner, String url) throws IOException {
        System.out.print("Enter Bank Name: ");
        scanner.nextLine();
        String bankName = scanner.nextLine();
        System.out.print("Enter banks flat fee value: ");
        BigDecimal flatFeeValue = scanner.nextBigDecimal().setScale(2, RoundingMode.HALF_UP);
        System.out.print("Enter banks percent fee amount: ");
        BigDecimal percentFeeValue = scanner.nextBigDecimal().setScale(2,RoundingMode.HALF_UP);

        URL apiUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        JsonObject reqBodyJson = new JsonObject();
        reqBodyJson.addProperty("name", bankName);
        reqBodyJson.add("accounts", JsonNull.INSTANCE);
        reqBodyJson.addProperty("totalTransactionFeeAmount", 0.00);
        reqBodyJson.addProperty("totalTransferAmount", 0.00);
        reqBodyJson.addProperty("transactionFlatFeeAmount", flatFeeValue);
        reqBodyJson.addProperty("percentFeeValue", percentFeeValue);

        printResponseBody(connection, reqBodyJson);

        connection.disconnect();
    }

    public static void printResponseBody(HttpURLConnection connection, JsonObject reqBodyJson) throws IOException {
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        byte[] requestBodyBytes = reqBodyJson.toString().getBytes(StandardCharsets.UTF_8);
        outputStream.write(requestBodyBytes);

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        String responseBody = response.toString();
        System.out.println(responseBody);
    }

    public static void createAccount(Scanner scanner, String url) throws IOException {
        System.out.println("Create account");
        System.out.print("Enter user's name: ");
        scanner.nextLine();
        String userName = scanner.nextLine();
        System.out.print("Enter user's balance: ");
        BigDecimal balance = scanner.nextBigDecimal().setScale(2, RoundingMode.HALF_UP);

        URL apiUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        JsonObject reqBodyJson = new JsonObject();
        reqBodyJson.addProperty("userName", userName);
        reqBodyJson.addProperty("balance", balance);

        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        byte[] requestBodyBytes = reqBodyJson.toString().getBytes(StandardCharsets.UTF_8);
        outputStream.write(requestBodyBytes);

        connection.disconnect();
    }

    public static void makeTransaction(Scanner scanner, String url) throws IOException {
        System.out.print("Enter source account id: ");
        Long srcAccountId = (long) scanner.nextInt();
        System.out.print("Enter destination account id: ");
        Long destAccountId = (long) scanner.nextInt();
        System.out.print("Enter amount: ");
        BigDecimal amount = scanner.nextBigDecimal().setScale(2, RoundingMode.HALF_UP);
        System.out.print("Enter description: ");
        scanner.nextLine();
        String description = scanner.nextLine();

        url = url+ "&srcAccId="+srcAccountId+"&destAccId="+destAccountId;

        URL apiUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        JsonObject reqBodyJson = new JsonObject();
        reqBodyJson.addProperty("amount", amount);
        reqBodyJson.addProperty("description", description);

        printResponseBody(connection, reqBodyJson);

        connection.disconnect();
    }

    public static void makeDepositWithdraw(Scanner scanner, String url) throws IOException {
        System.out.print("Enter account id: ");
        Long srcAccountId = (long) scanner.nextInt();
        System.out.print("Enter amount: ");
        BigDecimal amount = scanner.nextBigDecimal().setScale(2, RoundingMode.HALF_UP);
        System.out.print("Enter description: ");
        scanner.nextLine();
        String description = scanner.nextLine();

        url = url + "&srcId="+srcAccountId;

        URL apiUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        JsonObject reqBodyJson = new JsonObject();
        reqBodyJson.addProperty("amount", amount);
        reqBodyJson.addProperty("description", description);

        printResponseBody(connection, reqBodyJson);

        connection.disconnect();
    }

    public static void checkListOfTransactionsForAccount(Scanner scanner, String url) throws IOException {
        System.out.print("Enter account id: ");
        Long srcAccountId = (long) scanner.nextInt();

        url = url + "accountId="+srcAccountId;

        URL apiUrl = null;
        HttpURLConnection connection = null;

        apiUrl = new URL(url);
        connection = (HttpURLConnection) apiUrl.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null){
            response.append(line);
        }

        String jsonResponse = response.toString();
        JsonArray jsonArray = null;
        try {
            jsonArray = JsonParser.parseString(String.valueOf(jsonResponse)).getAsJsonArray();
        }
        catch (Exception e){
            System.out.println("invalid account id!");
            return;
        }

        for(JsonElement jsonElement: jsonArray){
            JsonObject transacation = jsonElement.getAsJsonObject();
            int transactionId = transacation.get("transactionId").getAsInt();
            BigDecimal amount = transacation.get("amount").getAsBigDecimal();
            String description = transacation.get("description").getAsString();
            System.out.println(transactionId +"- "+ description+" "+amount);
        }

        reader.close();
        connection.disconnect();
    }

    public static void checkBalanceForAccount(Scanner scanner, String url) throws IOException {
        System.out.print("Enter account id: ");
        Long srcAccountId = (long) scanner.nextInt();

        url = url +"&accountId="+srcAccountId;

        URL apiUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader= new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null){
            response.append(line);
        }

        String jsonResponse = response.toString();
        System.out.println(jsonResponse);

        connection.disconnect();
    }

    public static void seeListOfBankAccounts(Scanner scanner, String url) throws IOException {
        URL apiUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader= new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null){
            response.append(line);
        }

        String jsonResponse = response.toString();
        JsonArray jsonArray = JsonParser.parseString(String.valueOf(jsonResponse)).getAsJsonArray();

        for(JsonElement jsonElement: jsonArray){
            JsonObject bank_account = jsonElement.getAsJsonObject();
            System.out.println(bank_account.get("accountId") + " - "+ bank_account.get("userName"));
        }

        reader.close();
        connection.disconnect();
    }

    public static void checkBankDetails(Scanner scanner, String url) throws IOException {
        URL apiUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader= new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null){
            response.append(line);
        }

        String jsonResponse = response.toString();
        JsonArray jsonArray = JsonParser.parseString(String.valueOf(jsonResponse)).getAsJsonArray();

        System.out.println(jsonArray.get(0)+"$ bank total transaction fee amount");
        System.out.println(jsonArray.get(1)+"$ bank total transfer amount");

        reader.close();
        connection.disconnect();
    }

    public static void operateInBank(Long bankId, Scanner scanner) throws IOException {
        System.out.println("Bank menu!!!");
        List<String> menuOperations = new ArrayList<>(List.of("Create Account", "Flat fee transaction", "Percent fee transaction", "Deposit", "Withdraw", "See list of transactions for account", "Check account balance for account", "See List of Bank accounts", "Check Bank Details"));
        while (true){
            int i = 1;
            for(String operation: menuOperations){
                System.out.println(i+") "+operation);
                i++;
            }
            System.out.println("0) Go BACK");
            System.out.print("Enter number of operation: ");
            int operationNumber = scanner.nextInt();
            switch (operationNumber){
                case 0:
                    return;
                case 1:
                    createAccount(scanner, "http://localhost:8080/api/account/create?bankId="+bankId);
                    break;
                case 2:
                    makeTransaction(scanner,"http://localhost:8080/api/transaction/flat_fee?bankId="+bankId);
                    break;
                case 3:
                    makeTransaction(scanner, "http://localhost:8080/api/transaction/percent_fee?bankId="+bankId);
                    break;
                case 4:
                    makeDepositWithdraw(scanner, "http://localhost:8080/api/transaction/deposit?bankId="+bankId);
                    break;
                case 5:
                    makeDepositWithdraw(scanner, "http://localhost:8080/api/transaction/withdraw?bankId="+bankId);
                    break;
                case 6:
                    checkListOfTransactionsForAccount(scanner, "http://localhost:8080/api/transaction/transactions_for_account?");
                    break;
                case 7:
                    checkBalanceForAccount(scanner, "http://localhost:8080/api/account/account_balance?bankId="+bankId);
                    break;
                case 8:
                    seeListOfBankAccounts(scanner, "http://localhost:8080/api/bank/bank_accounts?bankId="+bankId);
                    break;
                case 9:
                    checkBankDetails(scanner, "http://localhost:8080/api/bank/bank_details?bankId="+bankId);
                    break;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("WELCOME to our banking system!!!!");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            showListOfBanks("http://localhost:8080/api/bank/get_all");
            System.out.print("0) Create New Bank\n\nEnter number of bank you want to operate in or create bank: ");
            int operatingNumber = scanner.nextInt();

            switch (operatingNumber) {
                case 0:
                    createBank(scanner, "http://localhost:8080/api/bank/create");
                    break;
                default:
                    operateInBank((long) operatingNumber, scanner);
                    break;
            }
        }
    }
}
