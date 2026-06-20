package model;

import java.util.LinkedList;

public class User {

    private int userId;
    private LinkedList<Rating> ratings;

    public User(int userId) {

        this.userId = userId;
        this.ratings = new LinkedList<>();
    }

    public int getUserId() {
        return userId;
    }

    public LinkedList<Rating> getRatings() {
        return ratings;
    }

    public void addRating(int movieId, int rating) {

        if (rating != 0) {
            ratings.add(new Rating(movieId, rating));
        }
    }

    public boolean hasRatedMovie(int movieId) {

        for (Rating rating : ratings) {
            if (rating.getMovieId() == movieId) {
                return true;
            }
        }
        return false;
    }

    public int getRatingByMovieId(int movieId) {

        for (Rating rating : ratings) {
            if (rating.getMovieId() == movieId) {
                return rating.getRating();
            }
        }

        return 0;
    }

    @Override
    public String toString() {

        return "User " + userId;
    }
}