# Movie Recommendation System

A Java-based movie recommendation system that generates personalized movie suggestions using **User-Based Collaborative Filtering** and **Cosine Similarity**.

## Overview

This project recommends movies to users based on the preferences of similar users. A custom **Max Heap** data structure is used to efficiently select the most similar users and generate recommendations.

## Features

- User-Based Collaborative Filtering
- Cosine Similarity Calculation
- Custom Max Heap Implementation
- Personalized Movie Recommendations
- Movie-Based Recommendation Support
- Java Swing Graphical User Interface
- CSV-Based Dataset Management


## Project Structure

```text
src/
├── model/
│   ├── Movie.java
│   ├── Rating.java
│   ├── SimilarUser.java
│   └── User.java
│
├── heap/
│   ├── Node.java
│   └── MaxHeap.java
│
├── service/
│   ├── CSVService.java
│   ├── SimilarityService.java
│   └── RecommendationService.java
│
├── gui/
│   ├── MainFrame.java
│   ├── MovieRecommendationFrame.java
│   └── UserRecommendationFrame.java
│
└── main/
    └── Main.java
```

## Recommendation Process

1. The target user selects and rates movies.
2. Cosine similarity is calculated between the target user and all existing users.
3. Similar users are stored in a Max Heap.
4. The top-K most similar users are selected.
5. Unseen movies from similar users are collected.
6. The system recommends the highest-rated movies.

## Dataset

The project uses three CSV files:

- `movies.csv`
- `users.csv`
- `ratings.csv`

## Algorithms Used

- User-Based Collaborative Filtering
- Cosine Similarity
- Max Heap Priority Selection

