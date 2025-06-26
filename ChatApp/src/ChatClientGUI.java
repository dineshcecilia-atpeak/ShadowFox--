import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;

public class ChatClientGUI {
    private JFrame frame;
    private JPanel chatPanel;
    private JScrollPane scrollPane;
    private JTextField messageField;
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private JLabel profilePic;
    private String username;
    private JLabel title;


    public ChatClientGUI() {
        setupUI();
        connectToServer();
    }

    /* ───────────────────────── UI SETUP ───────────────────────── */

    private void setupUI() {
        frame = new JFrame("Java Chat Room");
        frame.setSize(650, 520);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        /* Header (WhatsApp-green bar) */
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(37, 211, 102));
        header.setPreferredSize(new Dimension(frame.getWidth(), 60));

        profilePic = new JLabel();
        profilePic.setPreferredSize(new Dimension(50, 50));
        profilePic.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        chooseProfileImage();

        title = new JLabel("  Java Chat Room", JLabel.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));

        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftHeader.setOpaque(false);
        leftHeader.add(profilePic);
        leftHeader.add(title);
        header.add(leftHeader, BorderLayout.WEST);
        frame.add(header, BorderLayout.NORTH);

        /* Chat area (vertical BoxLayout) */
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(Color.WHITE);

        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(null);
        frame.add(scrollPane, BorderLayout.CENTER);

        /* Input area */
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        messageField = new JTextField();

        JButton sendButton = new JButton("Send");
        sendButton.setBackground(new Color(37, 211, 102));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        sendButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        /* Hover colours */
        sendButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { sendButton.setBackground(new Color(30, 200, 90)); }
            public void mouseExited(MouseEvent e)  { sendButton.setBackground(new Color(37, 211, 102)); }
        });

        JPanel sendPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sendPanel.setOpaque(false);
        sendPanel.add(sendButton);

        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendPanel, BorderLayout.EAST);
        frame.add(inputPanel, BorderLayout.SOUTH);

        /* Listeners */
        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());

        frame.setVisible(true);
    }

    /* ─────────────────────── Profile Picture ─────────────────────── */

    private void chooseProfileImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose Profile Picture");
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            ImageIcon icon = new ImageIcon(fileChooser.getSelectedFile().getAbsolutePath());
            Image image = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            profilePic.setIcon(new ImageIcon(image));
        } else {
            String name = JOptionPane.showInputDialog(frame, "Enter your name for profile (initial will show):");
            if (name == null || name.isEmpty()) name = "U";
            BufferedImage img = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();
            g.setColor(new Color(30, 144, 255)); g.fillOval(0, 0, 50, 50);
            g.setFont(new Font("SansSerif", Font.BOLD, 25)); g.setColor(Color.WHITE);
            FontMetrics fm = g.getFontMetrics();
            int x = (50 - fm.charWidth(name.toUpperCase().charAt(0)))/2;
            int y = ((50 - fm.getHeight())/2) + fm.getAscent();
            g.drawString(name.substring(0,1).toUpperCase(), x, y); g.dispose();
            profilePic.setIcon(new ImageIcon(img));
        }
    }

    /* ───────────────────────── Networking ───────────────────────── */

    private void connectToServer() {
        username = JOptionPane.showInputDialog(frame, "Enter your name:");
        title.setText("  " + username);
        String room = JOptionPane.showInputDialog(frame, "Enter chat room name:");
        try {
            socket = new Socket("localhost", 12345);
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(username); out.println(room);

            new Thread(() -> {
                String line;
                try { while ((line = in.readLine()) != null) displayMessage(line); }
                catch (IOException e) { displaySystemMessage("Disconnected from server."); }
            }).start();
        } catch (IOException e) { JOptionPane.showMessageDialog(frame,"Could not connect to server."); }
    }

    private void sendMessage() {
        String msg = messageField.getText().trim();
        if (!msg.isEmpty()) {
            out.println(msg);
            messageField.setText("");
        }
    }

    /* ───────────────────────── Display Logic ───────────────────────── */

    private void displayMessage(String line) {
        /* Detect system notices starting  */
        if (line.startsWith("🔔") || line.startsWith("[ERROR]")) {
            displaySystemMessage(line);
            return;
        }

        boolean isSelf = line.startsWith(username + ":");
        String text   = isSelf ? line.substring(username.length() + 1).trim() : line;

        JPanel bubble = new JPanel(new BorderLayout());
        bubble.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));   // ← tighter gap (was 5)

        JLabel lbl = new JLabel("<html><p style='width:250px;'>" + text + "</p></html>");
        lbl.setOpaque(true);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lbl.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));      // ← slightly smaller padding
        lbl.setForeground(Color.BLACK);

        if (isSelf) {
            lbl.setBackground(new Color(220, 248, 198));
            bubble.setLayout(new FlowLayout(FlowLayout.RIGHT));
        } else {
            lbl.setBackground(new Color(240, 240, 240));
            bubble.setLayout(new FlowLayout(FlowLayout.LEFT));
        }
        bubble.setBackground(Color.WHITE);
        bubble.add(lbl);

        SwingUtilities.invokeLater(() -> {
            chatPanel.add(bubble);
            chatPanel.revalidate();   chatPanel.repaint();
            scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        });
    }

    /** Centered, small grey/red system message */
    private void displaySystemMessage(String msg) {
        JPanel sys = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        sys.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));        // tighter spacing
        JLabel lbl = new JLabel(msg);
        lbl.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lbl.setForeground(new Color(120, 120, 120));
        sys.add(lbl); sys.setBackground(Color.WHITE);

        SwingUtilities.invokeLater(() -> {
            chatPanel.add(sys);
            chatPanel.revalidate();   chatPanel.repaint();
            scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        });
    }

    /* ─────────────────────────── Main ─────────────────────────── */

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatClientGUI::new);
    }
}
