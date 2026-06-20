package model;

public class SimilarUser {

    private User user;
    private double similarity;

    public SimilarUser(User user, double similarity) {

        this.user = user;
        this.similarity = similarity;
    }

    public User getUser() {
        return user;
    }

    public double getSimilarity() {
        return similarity;
    }

    @Override
    public String toString() {

        return "User " + user.getUserId() + " - Similarity: " + similarity;
    }
}