package service;

import heap.MaxHeap;
import model.Movie;
import model.Rating;
import model.SimilarUser;
import model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

public class RecommendationService {

    public ArrayList<String> getRecommendations(
            User targetUser,
            MaxHeap heap,
            HashMap<Integer, Movie> movieMap,
            int x,
            int k
    ) {
        ArrayList<String> recommendations = new ArrayList<>();
        
        for (int i = 0; i < k; i++) {

            SimilarUser similarUser = heap.removeMax();

            if (similarUser == null) {
                break;
            }

            User user = similarUser.getUser();

            recommendations.add("");
            recommendations.add("--------------------------------------------------");
            recommendations.add((i + 1) + ". BENZER KULLANICI");
            recommendations.add("Kullanıcı ID      : " + user.getUserId());
            recommendations.add("Benzerlik Değeri  : "
                    + String.format(Locale.US, "%.4f", similarUser.getSimilarity()));
            recommendations.add("");
            recommendations.add("Film Önerileri:");

            ArrayList<Rating> sortedRatings = sortRatingsDescending(user.getRatings());

            int addedForThisUser = 0;

            for (Rating rating : sortedRatings) {
                
                if (addedForThisUser == x) {
                    break;
                }

                int movieId = rating.getMovieId();

                if (targetUser.hasRatedMovie(movieId)) {
                    continue;
                }

                Movie movie = movieMap.get(movieId);

                if (movie == null) {
                    continue;
                }

                String recommendation =
                        "  " + (addedForThisUser + 1) + ") "
                                + movie.getTitle()
                                + " — Puan: "
                                + rating.getRating();

                recommendations.add(recommendation);
                addedForThisUser++;
            }

            if (addedForThisUser == 0) {
                recommendations.add("  Bu kullanıcı için uygun film önerisi bulunamadı.");
            }
        }

        return recommendations;
    }

    private ArrayList<Rating> sortRatingsDescending(LinkedList<Rating> ratings) {

        ArrayList<Rating> sortedRatings = new ArrayList<>(ratings);

        sortedRatings.sort((r1, r2) ->
                Integer.compare(r2.getRating(), r1.getRating())
        );

        return sortedRatings;
    }
}