package model;

public class Rating {

    private int movieId;
    private int rating;

    public Rating(int movieId, int rating) {

        this.movieId = movieId;
        this.rating = rating;
    }

    public int getMovieId() {
        return movieId;
    }

    public int getRating() {
        return rating;
    }
}