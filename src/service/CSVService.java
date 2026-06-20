package service;

import model.Movie;
import model.User;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;

public class CSVService {
    public HashMap<Integer, Movie> readMovies(String filePath) {
        HashMap<Integer, Movie> movieMap = new HashMap<>();

        String resourcePath = filePath.startsWith("/") ? filePath : "/" + filePath;

        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            
            if (is == null) {
                System.err.println("HATA: " + resourcePath + " dosyası bulunamadı! Lütfen dosyanın resources klasöründe olduğundan emin olun.");
                return movieMap;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line = br.readLine();

                while ((line = br.readLine()) != null) {
                    try {
                        int firstComma = line.indexOf(",");
                        int lastComma = line.lastIndexOf(",");

                        if (firstComma != -1 && lastComma != -1 && firstComma < lastComma) {
                            String idStr = line.substring(0, firstComma).trim();
                            String title = line.substring(firstComma + 1, lastComma).trim();
                            String genres = line.substring(lastComma + 1).trim();

                            if (title.startsWith("\"") && title.endsWith("\"")) {
                                title = title.substring(1, title.length() - 1).trim();
                            }

                            title = title.replace("\"\"", "\"");

                            int movieId = Integer.parseInt(idStr);

                            movieMap.put(movieId, new Movie(movieId, title, genres));
                        }

                    } catch (NumberFormatException e) {
                        System.err.println("movies.csv içinde hatalı satır atlandı: " + line + " -> Hata: " + e.getMessage());
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("movies.csv okunurken dosya hatası oluştu: " + e.getMessage());
        }

        return movieMap;
    }

    public LinkedList<User> readUsers(String filePath) {
        LinkedList<User> users = new LinkedList<>();

        String resourcePath = filePath.startsWith("/") ? filePath : "/" + filePath;

        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            
            if (is == null) {
                System.err.println("HATA: " + resourcePath + " dosyası bulunamadı!");
                return users;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String headerLine = br.readLine();

                if (headerLine == null) {
                    return users;
                }

                String[] movieIds = headerLine.split(",");
                String line;

                while ((line = br.readLine()) != null) {
                    try {
                        String[] values = line.split(",");

                        if (values.length == 0) {
                            continue;
                        }

                        int userId = Integer.parseInt(values[0].trim());
                        User user = new User(userId);

                        for (int i = 1; i < values.length && i < movieIds.length; i++) {
                            String ratingText = values[i].trim();

                            if (!ratingText.isEmpty()) {
                                int movieId = Integer.parseInt(movieIds[i].trim());
                                int rating = Integer.parseInt(ratingText);

                                if (rating != 0) {
                                    user.addRating(movieId, rating);
                                }
                            }
                        }

                        users.add(user);

                    } catch (NumberFormatException e) {
                        System.err.println(filePath + " içinde hatalı kullanıcı satırı atlandı. Hata: " + e.getMessage());
                    }
                }
            }

        } catch (IOException e) {
            System.err.println(filePath + " okunurken dosya hatası oluştu: " + e.getMessage());
        }

        return users;
    }
}