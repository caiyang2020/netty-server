package org.example;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimpleHttpPostRequest {

    public static void main(String[] args) {
        try {
            URL url = new URL("http://127.0.0.1:8080");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Custom-Header", "header_value");
            connection.setDoOutput(true);

            // 获取输出流并发送请求体
            try(OutputStream os = connection.getOutputStream()) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write("{\"key1\":\"value1\", \"key2\":\"value2\"}");
                writer.flush();
            }

            // 读取响应
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                System.out.println(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
