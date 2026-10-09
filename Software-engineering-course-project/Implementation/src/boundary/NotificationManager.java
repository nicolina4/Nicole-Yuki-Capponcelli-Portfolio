package boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NotificationManager {
    private static final int NOTIFICATION_DURATION = 5000; // 5 secondi
    private static JFrame mainFrame;
    
    public static void setMainFrame(JFrame frame) {
        mainFrame = frame;
    }
    
    public static void showLikeNotification(String nomeAutore, String titoloPoesia) {
        if (mainFrame != null) {
            SwingUtilities.invokeLater(() -> {
                createNotification("❤️ Nuovo Like!", 
                    nomeAutore + " ha messo like alla tua poesia: \"" + titoloPoesia + "\"");
            });
        }
    }
    
    public static void showCommentNotification(String nomeAutore, String titoloPoesia) {
        if (mainFrame != null) {
            SwingUtilities.invokeLater(() -> {
                createNotification("💬 Nuovo Commento!", 
                    nomeAutore + " ha commentato la tua poesia: \"" + titoloPoesia + "\"");
            });
        }
    }
    
    private static void createNotification(String title, String message) {
        // Crea un JDialog non modale
        JDialog notificationDialog = new JDialog(mainFrame, title, false);
        notificationDialog.setLayout(new BorderLayout());
        notificationDialog.setSize(350, 100);
        notificationDialog.setLocationRelativeTo(mainFrame);
        notificationDialog.setUndecorated(true);
        notificationDialog.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        
        // Pannello principale
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(70, 130, 180));
        panel.setBorder(BorderFactory.createLineBorder(new Color(50, 100, 150), 2));
        
        // Testo della notifica
        JLabel messageLabel = new JLabel("<html><div style='width:300px;'>" + message + "</div></html>");
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        
        // Pulsante di chiusura
        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(new Color(100, 150, 200));
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        closeButton.setPreferredSize(new Dimension(30, 30));
        
        closeButton.addActionListener(e -> notificationDialog.dispose());
        
        // Pannello superiore con titolo e pulsante chiusura
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(60, 120, 170));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setBackground(new Color(60, 120, 170));
        buttonPanel.add(closeButton);
        
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Assembla il dialog
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(messageLabel, BorderLayout.CENTER);
        notificationDialog.add(panel);
        
        // Posiziona in alto a destra
        Point mainFrameLocation = mainFrame.getLocation();
        Dimension mainFrameSize = mainFrame.getSize();
        int x = mainFrameLocation.x + mainFrameSize.width - notificationDialog.getWidth() - 20;
        int y = mainFrameLocation.y + 50;
        notificationDialog.setLocation(x, y);
        
        // Mostra la notifica
        notificationDialog.setVisible(true);
        
        // Timer per chiudere automaticamente
        Timer timer = new Timer(NOTIFICATION_DURATION, e -> notificationDialog.dispose());
        timer.setRepeats(false);
        timer.start();
        
        // Effetto di entrata
        notificationDialog.setOpacity(0f);
        Timer fadeInTimer = new Timer(50, new ActionListener() {
            float opacity = 0f;
            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += 0.1f;
                if (opacity >= 1f) {
                    ((Timer)e.getSource()).stop();
                    notificationDialog.setOpacity(1f);
                } else {
                    notificationDialog.setOpacity(opacity);
                }
            }
        });
        fadeInTimer.start();
    }
}