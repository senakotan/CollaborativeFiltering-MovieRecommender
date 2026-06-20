package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {

    private JButton btnTargetUser;
    private JButton btnMovieBased;

    private final Color backgroundColor = new Color(253, 246, 238);
    private final Color cardColor = new Color(255, 255, 255);
    private final Color primaryColor = new Color(232, 131, 74);
    private final Color borderColor = new Color(237, 219, 202);
    private final Color textColor = new Color(44, 26, 14);
    private final Color mutedColor = new Color(156, 123, 90);
    private final Color btnHoverBg = new Color(255, 248, 243);

    public MainFrame() {
        initializeFrame();
        initializeComponents();
        addActions();
    }

    private void initializeFrame() {
        setTitle("Sena Kotan - Movie Recommendation System");
        setSize(860, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(backgroundColor);
    }

    private void initializeComponents() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(52, 80, 44, 80));

        JPanel topStripe = new JPanel();
        topStripe.setBackground(primaryColor);
        topStripe.setPreferredSize(new Dimension(getWidth(), 4));
        add(topStripe, BorderLayout.NORTH);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Movie Recommendation System", SwingConstants.CENTER);
        titleLabel.setFont(loadPlayfairFont(32f));
        titleLabel.setForeground(textColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Heap-Based Collaborative Filtering", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(mutedColor);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(8));
        titlePanel.add(subtitleLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 16));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(38, 40, 38, 40));

        btnTargetUser = createMenuButton(
                "Recommendations by Target User",
                "Find films based on a specific user's taste"
        );

        btnMovieBased = createMenuButton(
                "Recommendations by Movie Ratings",
                "Get recommendations from selected movie ratings"
        );

        buttonPanel.add(btnTargetUser);
        buttonPanel.add(btnMovieBased);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JButton createMenuButton(String label, String description) {

        JButton button = new JButton() {

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                Color bg = getModel().isRollover()
                        ? btnHoverBg
                        : cardColor;

                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);

                Color border = getModel().isRollover()
                        ? primaryColor
                        : borderColor;

                g2.setStroke(
                        new BasicStroke(
                                getModel().isRollover() ? 1.8f : 1.2f
                        )
                );

                g2.setColor(border);
                g2.drawRoundRect(
                        0,
                        0,
                        getWidth() - 1,
                        getHeight() - 1,
                        14,
                        14
                );

                if (getModel().isRollover()) {

                    g2.setColor(primaryColor);

                    g2.setStroke(
                            new BasicStroke(
                                    3f,
                                    BasicStroke.CAP_ROUND,
                                    BasicStroke.JOIN_ROUND
                            )
                    );

                    int lx = 18;
                    int ly = getHeight() / 2 - 14;

                    g2.drawLine(lx, ly, lx, ly + 28);
                }

                int textX = 32;

                g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
                g2.setColor(textColor);

                FontMetrics fm = g2.getFontMetrics();

                g2.drawString(
                        label,
                        textX,
                        getHeight() / 2 - 2
                );

                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                g2.setColor(mutedColor);

                g2.drawString(
                        description,
                        textX,
                        getHeight() / 2 + fm.getHeight() - 4
                );

                g2.setFont(new Font("Segoe UI", Font.PLAIN, 18));

                Color arrowColor = getModel().isRollover()
                        ? primaryColor
                        : new Color(208, 168, 130);

                g2.setColor(arrowColor);

                int arrowOffset = getModel().isRollover() ? 3 : 0;

                g2.drawString(
                        "→",
                        getWidth() - 42 + arrowOffset,
                        getHeight() / 2 + 7
                );

                g2.dispose();
            }
        };

        button.setPreferredSize(new Dimension(0, 80));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.repaint();
            }
        });

        return button;
    }

    private Font loadPlayfairFont(float size) {
        try {
            var stream = getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf");
            if (stream == null) {
                throw new java.io.FileNotFoundException("Font dosyası belirtilen kaynak yolunda bulunamadı.");
            }

            Font f = Font.createFont(Font.TRUETYPE_FONT, stream);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(f);
            return f.deriveFont(Font.BOLD, size);

        } catch (Exception e) {
            return new Font("Segoe UI", Font.BOLD, (int) size);
        }
    }

    private void addActions() {

        btnTargetUser.addActionListener(e -> {
            UserRecommendationFrame userFrame = new UserRecommendationFrame();

            userFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            userFrame.setVisible(true);
        });

        btnMovieBased.addActionListener(e -> {
            MovieRecommendationFrame movieFrame = new MovieRecommendationFrame();
            movieFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            movieFrame.setVisible(true);
        });
    }
}