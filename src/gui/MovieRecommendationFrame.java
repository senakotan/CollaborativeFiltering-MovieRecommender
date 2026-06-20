package gui;

import heap.MaxHeap;
import model.Movie;
import model.SimilarUser;
import model.User;
import service.CSVService;
import service.RecommendationService;
import service.SimilarityService;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

public class MovieRecommendationFrame extends JFrame {

    private JComboBox<Movie>[] movieCombos;
    private JRadioButton[][] ratingButtons;
    private ButtonGroup[] ratingGroups;

    private JTextField txtX;
    private JTextField txtK;

    private JButton btnGetRecommendations;
    private JButton btnRefreshMovies;
    private JButton btnTargetUserPage;

    private JTextArea txtRecommendations;

    private LinkedList<User> mainUsers;
    private HashMap<Integer, Movie> movieMap;

    private ArrayList<Movie> randomMovies;

    private final Color backgroundColor = new Color(253, 246, 238);
    private final Color cardColor = new Color(255, 255, 255);
    private final Color primaryColor = new Color(232, 131, 74);
    private final Color borderColor = new Color(237, 219, 202);
    private final Color textColor = new Color(44, 26, 14);
    private final Color mutedColor = new Color(156, 123, 90);

    public MovieRecommendationFrame() {
        CSVService csvService = new CSVService();
        mainUsers = csvService.readUsers("main_data.csv");
        movieMap = csvService.readMovies("movies.csv");

        initializeFrame();
        initializeComponents();

        refreshMovieLists();

        addActions();
    }

    private void initializeFrame() {
        setTitle("Sena Kotan - Movie Based Recommendation");
        setSize(1100, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(backgroundColor);
    }

    private void initializeComponents() {
        JPanel topStripe = new JPanel();
        topStripe.setBackground(primaryColor);
        topStripe.setPreferredSize(new Dimension(getWidth(), 4));
        add(topStripe, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 18));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(24, 40, 24, 40));
        mainPanel.setBackground(backgroundColor);

        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(cardColor);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(borderColor, 1, true),
                BorderFactory.createEmptyBorder(22, 28, 18, 28)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        formCard.add(makeHeaderLabel("Movie Title (Random 10 Movies)"), gbc);

        for (int rating = 1; rating <= 5; rating++) {
            gbc.gridx = rating;
            gbc.weightx = 0.0;
            formCard.add(makeHeaderLabel(String.valueOf(rating)), gbc);
        }

        movieCombos = new JComboBox[5];
        ratingButtons = new JRadioButton[5][5];
        ratingGroups = new ButtonGroup[5];

        for (int row = 0; row < 5; row++) {
            gbc.gridy = row + 1;

            movieCombos[row] = new JComboBox<>();
            movieCombos[row].setPreferredSize(new Dimension(420, 34));
            movieCombos[row].setFont(new Font("Segoe UI", Font.PLAIN, 13));
            movieCombos[row].setBackground(new Color(255, 252, 248));

            gbc.gridx = 0;
            gbc.weightx = 1.0;
            formCard.add(movieCombos[row], gbc);

            ratingGroups[row] = new ButtonGroup();

            for (int col = 0; col < 5; col++) {
                ratingButtons[row][col] = new JRadioButton();
                ratingButtons[row][col].setOpaque(false);
                ratingButtons[row][col].setHorizontalAlignment(SwingConstants.CENTER);
                ratingGroups[row].add(ratingButtons[row][col]);

                gbc.gridx = col + 1;
                gbc.weightx = 0.0;
                formCard.add(ratingButtons[row][col], gbc);
            }
        }

        JPanel paramPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        paramPanel.setOpaque(false);

        paramPanel.add(makeHeaderLabel("X (Movies per User):"));
        txtX = createStyledField(60);
        paramPanel.add(txtX);

        paramPanel.add(makeHeaderLabel("K (Similar Users Count):"));
        txtK = createStyledField(60);
        paramPanel.add(txtK);

        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 6;
        gbc.insets = new Insets(14, 4, 4, 4);
        formCard.add(paramPanel, gbc);

        btnRefreshMovies = createCinemaButton(" Refresh Movies", new Color(254, 240, 230), primaryColor);
        btnGetRecommendations = createCinemaButton(" Get Recommendations", new Color(232, 244, 255), new Color(60, 100, 180));
        btnTargetUserPage = createCinemaButton("← Target User Page", new Color(240, 236, 252), new Color(100, 70, 180));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnRefreshMovies);
        buttonPanel.add(btnGetRecommendations);
        buttonPanel.add(btnTargetUserPage);

        gbc.gridy = 7;
        gbc.insets = new Insets(14, 4, 4, 4);
        formCard.add(buttonPanel, gbc);

        txtRecommendations = new JTextArea();
        txtRecommendations.setEditable(false);
        txtRecommendations.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtRecommendations.setForeground(textColor);
        txtRecommendations.setBackground(cardColor);
        txtRecommendations.setMargin(new Insets(14, 16, 14, 16));

        JScrollPane scrollPane = new JScrollPane(txtRecommendations);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(borderColor, 1, true),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        JPanel resultWrapper = new JPanel(new BorderLayout(0, 6));
        resultWrapper.setOpaque(false);

        JLabel resultHeader = new JLabel("Recommendations");
        resultHeader.setFont(new Font("Georgia", Font.BOLD, 15));
        resultHeader.setForeground(mutedColor);

        resultWrapper.add(resultHeader, BorderLayout.NORTH);
        resultWrapper.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(formCard, BorderLayout.NORTH);
        mainPanel.add(resultWrapper, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JLabel makeHeaderLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(mutedColor);
        return lbl;
    }

    private JTextField createStyledField(int width) {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(width, 34));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setForeground(textColor);
        field.setBackground(new Color(255, 252, 248));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(borderColor, 1, true),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        return field;
    }

    private JButton createCinemaButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color fillColor = getModel().isRollover() ? bg.darker() : bg;
                g2.setColor(fillColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2.setColor(fg.brighter());
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(fg);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));

        return btn;
    }

    private void refreshMovieLists() {
        randomMovies = getRandomMovies(10);

        for (int row = 0; row < 5; row++) {
            movieCombos[row].removeAllItems();

            for (Movie movie : randomMovies) {
                movieCombos[row].addItem(movie);
            }

            movieCombos[row].setSelectedIndex(-1);
            ratingGroups[row].clearSelection();
        }
    }

    private ArrayList<Movie> getRandomMovies(int count) {
        HashMap<Integer, Integer> ratingCounts = new HashMap<>();

        for (User user : mainUsers) {
            for (var rating : user.getRatings()) {
                int movieId = rating.getMovieId();

                if (movieMap.containsKey(movieId)) {
                    ratingCounts.put(movieId, ratingCounts.getOrDefault(movieId, 0) + 1);
                }
            }
        }

        ArrayList<Integer> movieIds = new ArrayList<>(ratingCounts.keySet());

        movieIds.sort((id1, id2)
                -> Integer.compare(ratingCounts.get(id2), ratingCounts.get(id1))
        );

        ArrayList<Movie> mostRatedMovies = new ArrayList<>();

        int limit = Math.min(100, movieIds.size());

        for (int i = 0; i < limit; i++) {
            Movie movie = movieMap.get(movieIds.get(i));

            if (movie != null) {
                mostRatedMovies.add(movie);
            }
        }

        Collections.shuffle(mostRatedMovies);

        ArrayList<Movie> selected = new ArrayList<>();

        for (int i = 0; i < count && i < mostRatedMovies.size(); i++) {
            selected.add(mostRatedMovies.get(i));
        }

        return selected;
    }

    private void addActions() {
        btnTargetUserPage.addActionListener(e -> {
            new UserRecommendationFrame().setVisible(true);
            dispose();
        });

        btnRefreshMovies.addActionListener(e -> {
            refreshMovieLists();
            txtRecommendations.setText("");
            txtX.setText("");
            txtK.setText("");
        });

        btnGetRecommendations.addActionListener(e -> {
            try {
                int x = Integer.parseInt(txtX.getText().trim());
                int k = Integer.parseInt(txtK.getText().trim());

                if (x <= 0 || k <= 0) {
                    JOptionPane.showMessageDialog(this, "X ve K pozitif olmalıdır.");
                    return;
                }

                User tempUser = new User(-1);

                txtRecommendations.setText("");
                txtRecommendations.append("Kullanıcının Seçtiği ve Puanladığı Filmler:\n\n");

                for (int row = 0; row < 5; row++) {
                    Movie selectedMovie = (Movie) movieCombos[row].getSelectedItem();

                    if (selectedMovie == null) {
                        JOptionPane.showMessageDialog(this, "Her satırda bir film seçmelisiniz.");
                        return;
                    }

                    int rating = getSelectedRating(row);

                    if (rating == 0) {
                        JOptionPane.showMessageDialog(this, "Her film için 1-5 arasında bir puan seçmelisiniz.");
                        return;
                    }

                    if (isMovieSelectedBefore(row, selectedMovie)) {
                        JOptionPane.showMessageDialog(this, "Aynı filmi birden fazla kez seçemezsiniz.");
                        return;
                    }

                    tempUser.addRating(selectedMovie.getMovieId(), rating);

                    txtRecommendations.append(
                            "  " + (row + 1) + ") "
                            + selectedMovie.getTitle()
                            + " -> Puan: "
                            + rating
                            + "\n"
                    );
                }

                SimilarityService similarityService = new SimilarityService();
                MaxHeap heap = new MaxHeap();

                for (User user : mainUsers) {
                    double sim = similarityService.calculateCosineSimilarity(tempUser, user);

                    if (sim > 0) {
                        heap.insert(new SimilarUser(user, sim));
                    }
                }

                RecommendationService recService = new RecommendationService();

                ArrayList<String> recommendations
                        = recService.getRecommendations(tempUser, heap, movieMap, x, k);

                txtRecommendations.append("\nX (Film Sayısı): " + x + "  |  K (Benzer Kullanıcı Sayısı): " + k + "\n");
                txtRecommendations.append("Not: Seçtiğiniz filmler öneri listesinden otomatik olarak elenmiştir.\n");

                for (String rec : recommendations) {
                    txtRecommendations.append(rec + "\n");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Lütfen geçerli sayısal değerler giriniz.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Bir hata oluştu: " + ex.getMessage());
            }
        });
    }

    private int getSelectedRating(int row) {
        for (int col = 0; col < 5; col++) {
            if (ratingButtons[row][col].isSelected()) {
                return col + 1;
            }
        }

        return 0;
    }

    private boolean isMovieSelectedBefore(int currentRow, Movie selectedMovie) {
        for (int i = 0; i < currentRow; i++) {
            Movie previousMovie = (Movie) movieCombos[i].getSelectedItem();

            if (previousMovie != null && previousMovie.getMovieId() == selectedMovie.getMovieId()) {
                return true;
            }
        }

        return false;
    }
}