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
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class UserRecommendationFrame extends JFrame {

    private JComboBox<String> comboTargetUsers;

    private JTextField txtX;
    private JTextField txtK;

    private JButton btnGetRecommendations;
    private JButton btnMovieBasedPage;

    private JTextArea txtRecommendations;

    private LinkedList<User> targetUsers;

    private LinkedList<User> mainUsers;

    private HashMap<Integer, Movie> movieMap;

    private final Color backgroundColor = new Color(253, 246, 238);
    private final Color cardColor = new Color(255, 255, 255);
    private final Color primaryColor = new Color(232, 131, 74);
    private final Color borderColor = new Color(237, 219, 202);
    private final Color textColor = new Color(44, 26, 14);
    private final Color mutedColor = new Color(156, 123, 90);

    public UserRecommendationFrame() {

        CSVService csvService = new CSVService();
        targetUsers = csvService.readUsers("target_user.csv");
        mainUsers = csvService.readUsers("main_data.csv");
        movieMap = csvService.readMovies("movies.csv");

        initializeFrame();
        initializeComponents();
        loadTargetUsers();
        addActions();
    }

    private void initializeFrame() {
        setTitle("Sena Kotan - Target User Recommendation");
        setSize(1100, 700);
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

        JPanel inputCard = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 12));
        inputCard.setBackground(cardColor);
        inputCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(borderColor, 1, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        inputCard.add(makeHeaderLabel("Target User:"));
        comboTargetUsers = new JComboBox<>();
        comboTargetUsers.setPreferredSize(new Dimension(150, 34));
        comboTargetUsers.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboTargetUsers.setBackground(new Color(255, 252, 248));
        styleComboBox(comboTargetUsers);
        inputCard.add(comboTargetUsers);

        inputCard.add(Box.createHorizontalStrut(6));
        inputCard.add(makeHeaderLabel("X:"));
        txtX = createStyledField(80);
        inputCard.add(txtX);

        inputCard.add(makeHeaderLabel("K:"));
        txtK = createStyledField(80);
        inputCard.add(txtK);

        inputCard.add(Box.createHorizontalStrut(6));
        btnGetRecommendations = createCinemaButton(
                " Get Recommendations",
                new Color(232, 244, 255),
                new Color(60, 100, 180)
        );
        btnMovieBasedPage = createCinemaButton(
                "← Movie Based Page",
                new Color(254, 240, 230),
                primaryColor
        );
        inputCard.add(btnGetRecommendations);
        inputCard.add(btnMovieBasedPage);

        txtRecommendations = new JTextArea();
        txtRecommendations.setEditable(false);
        txtRecommendations.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtRecommendations.setForeground(textColor);
        txtRecommendations.setBackground(cardColor);
        txtRecommendations.setMargin(new Insets(14, 16, 14, 16));
        txtRecommendations.setLineWrap(false);

        JScrollPane scrollPane = new JScrollPane(txtRecommendations);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(borderColor, 1, true),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        JPanel resultWrapper = new JPanel(new BorderLayout(0, 6));
        resultWrapper.setOpaque(false);
        JLabel resultHeader = new JLabel(" Recommendations");
        resultHeader.setFont(new Font("Georgia", Font.BOLD, 15));
        resultHeader.setForeground(mutedColor);
        resultWrapper.add(resultHeader, BorderLayout.NORTH);
        resultWrapper.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(inputCard, BorderLayout.NORTH);
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

    private void styleComboBox(JComboBox<?> combo) {
        combo.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setFont(new Font("Segoe UI", Font.PLAIN, 13));
                if (isSelected) {
                    setBackground(new Color(255, 235, 215));
                    setForeground(textColor);
                } else {
                    setBackground(new Color(255, 252, 248));
                    setForeground(textColor);
                }
                return this;
            }
        });
    }

    private JButton createCinemaButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bg.darker() : bg);
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

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.repaint();
            }
        });

        return btn;
    }

    private void loadTargetUsers() {

        for (User user : targetUsers) {
            comboTargetUsers.addItem("User " + user.getUserId());
        }
    }

    private void addActions() {

        btnMovieBasedPage.addActionListener(e -> {
            new MovieRecommendationFrame().setVisible(true);
            dispose();
        });

        btnGetRecommendations.addActionListener(e -> {
            try {
                int selectedIndex = comboTargetUsers.getSelectedIndex();

                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(this, "Please select a target user.");
                    return;
                }

                int x = Integer.parseInt(txtX.getText().trim());
                int k = Integer.parseInt(txtK.getText().trim());

                if (x <= 0 || k <= 0) {
                    JOptionPane.showMessageDialog(this, "X and K must be positive values.");
                    return;
                }

                User targetUser = targetUsers.get(selectedIndex);

                SimilarityService similarityService = new SimilarityService();

                MaxHeap heap = new MaxHeap();

                for (User user : mainUsers) {
                    double sim = similarityService.calculateCosineSimilarity(targetUser, user);

                    if (sim > 0) {
                        heap.insert(new SimilarUser(user, sim));
                    }
                }

                RecommendationService recService = new RecommendationService();
                ArrayList<String> recommendations
                        = recService.getRecommendations(targetUser, heap, movieMap, x, k);

                txtRecommendations.setText("");
                txtRecommendations.append("Hedef Kullanıcı: " + targetUser.getUserId() + "\n");
                txtRecommendations.append("X: " + x + "  |  K: " + k + "\n");
                txtRecommendations.append("Not: Hedef kullanıcının daha önce puanladığı filmler öneri listesinden çıkarılmıştır.\n");

                for (String rec : recommendations) {
                    txtRecommendations.append(rec + "\n");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric values for X and K.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage());
            }
        });
    }
}