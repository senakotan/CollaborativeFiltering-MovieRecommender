package service;

import model.Rating;
import model.User;

public class SimilarityService {

    public double calculateCosineSimilarity(User targetUser, User otherUser) {
        double dotProduct = 0;
        double targetNorm = 0;
        double otherNorm = 0;

        for (Rating targetRating : targetUser.getRatings()) {
            int targetMovieId = targetRating.getMovieId();
            int targetValue = targetRating.getRating();

            targetNorm += targetValue * targetValue;

            int otherValue = otherUser.getRatingByMovieId(targetMovieId);

            if (otherValue != 0) {
                dotProduct += targetValue * otherValue;
            }
        }

        for (Rating otherRating : otherUser.getRatings()) {
            int value = otherRating.getRating();
            otherNorm += value * value;
        }

        if (targetNorm == 0 || otherNorm == 0) {
            return 0;
        }

        return dotProduct / (Math.sqrt(targetNorm) * Math.sqrt(otherNorm));
    }
}