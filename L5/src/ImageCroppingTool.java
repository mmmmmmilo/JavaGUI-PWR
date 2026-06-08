import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageCroppingTool {

    private JFrame frame;
    private JPanel drawPanel;
    private JLabel mousePositionLabel;
    private JLabel colorLabel;
    private JLabel shortcutsLabel;
    private JPopupMenu popupMenu;
    private BufferedImage image;
    private BufferedImage resizedImage;
    private File imageFile;

    private enum Mode { NONE, RECTANGLE, LINES }
    private Mode currentMode = Mode.NONE;
    private boolean changes = false;

    private Zaznaczenie selection;

    private RectangleSelection rectangleSelection;
    private LineSelection lineSelection;

    public ImageCroppingTool() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Image Cropping Tool");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setResizable(false);

        drawPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (resizedImage != null) {
                    g.drawImage(resizedImage, 0, 0, this);
                }
                if (selection != null) {
                    g.setColor(Color.CYAN);
                    ((Graphics2D) g).draw(selection.selection);
                }
                if (currentMode == Mode.RECTANGLE) {
                    rectangleSelection.draw((Graphics2D) g);
                }
                if (currentMode == Mode.LINES) {
                    lineSelection.draw((Graphics2D) g, drawPanel);
                }
            }
        };

        drawPanel.setBackground(Color.WHITE);
        drawPanel.setPreferredSize(new Dimension(600, 400));
        drawPanel.addMouseListener(new CustomMouseListener());
        drawPanel.addMouseMotionListener(new CustomMouseMotionListener());

        drawPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeImageToFitPanel();
            }
        });

        rectangleSelection = new RectangleSelection();
        lineSelection = new LineSelection();

        mousePositionLabel = new JLabel("Mouse Position: (0, 0)");
        colorLabel = new JLabel("Color: N/A");

        shortcutsLabel = new JLabel("Shortcuts:   W - Load   Z - Save   K - Rectangle   L - Lines   C - Clear   Q - Quit");

        popupMenu = new JPopupMenu();
        addPopupMenuItems();

        drawPanel.setComponentPopupMenu(popupMenu);
        drawPanel.setFocusable(true);
        drawPanel.addKeyListener(new CustomKeyListener());

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        bottomPanel.add(mousePositionLabel);
        bottomPanel.add(colorLabel);

        frame.getContentPane().add(drawPanel, BorderLayout.CENTER);
        frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        frame.getContentPane().add(shortcutsLabel, BorderLayout.NORTH);
        frame.setVisible(true);
    }

    private void addPopupMenuItems() {
        JMenuItem loadItem = new JMenuItem("Load");
        loadItem.addActionListener(e -> loadImage());
        popupMenu.add(loadItem);

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(e -> saveSelection());
        popupMenu.add(saveItem);

        JMenuItem rectangleItem = new JMenuItem("Rectangle");
        rectangleItem.addActionListener(e -> setMode(Mode.RECTANGLE));
        popupMenu.add(rectangleItem);

        JMenuItem linesItem = new JMenuItem("Lines");
        linesItem.addActionListener(e -> setMode(Mode.LINES));
        popupMenu.add(linesItem);

        JMenuItem clearItem = new JMenuItem("Clear");
        clearItem.addActionListener(e -> setMode(Mode.NONE));
        popupMenu.add(clearItem);

        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(e -> quitApplication());
        popupMenu.add(quitItem);
    }

    private void loadImage() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            imageFile = fileChooser.getSelectedFile();
            if (!imageFile.getName().toLowerCase().endsWith(".png")) {
                JOptionPane.showMessageDialog(frame, "Please select a PNG file.", "Invalid File Type", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                image = ImageIO.read(imageFile);
                resizeImageToFitPanel();
                drawPanel.repaint();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Error loading image.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void resizeImageToFitPanel() {
        if (image != null && drawPanel.getWidth() > 0 && drawPanel.getHeight() > 0) {
            int panelWidth = drawPanel.getWidth();
            int panelHeight = drawPanel.getHeight();
            double imageAspect = (double) image.getWidth() / image.getHeight();
            double panelAspect = (double) panelWidth / panelHeight;

            int newWidth, newHeight;
            if (imageAspect > panelAspect) {
                newWidth = panelWidth;
                newHeight = (int) (panelWidth / imageAspect);
            } else {
                newHeight = panelHeight;
                newWidth = (int) (panelHeight * imageAspect);
            }

            resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = resizedImage.createGraphics();
            g2d.drawImage(image, 0, 0, newWidth, newHeight, null);
            g2d.dispose();

            drawPanel.repaint();
        }
    }

    private void saveSelection() {
        if (selection != null && image != null) {
            try {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save Cropped Image");
                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG Images", "png"));

                if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    File outputFile = fileChooser.getSelectedFile();

                    if (!outputFile.getName().toLowerCase().endsWith(".png")) {
                        outputFile = new File(outputFile.getAbsolutePath() + ".png");
                    }

                    double scaleX = (double) image.getWidth() / resizedImage.getWidth();
                    double scaleY = (double) image.getHeight() / resizedImage.getHeight();

                    int originalX = (int) (selection.X * scaleX);
                    int originalY = (int) (selection.Y * scaleY);
                    int originalWidth = (int) (selection.W * scaleX);
                    int originalHeight = (int) (selection.H * scaleY);

                    BufferedImage croppedImage = image.getSubimage(originalX, originalY, originalWidth, originalHeight);
                    ImageIO.write(croppedImage, "png", outputFile);
                    JOptionPane.showMessageDialog(frame, "Selection saved to " + outputFile.getAbsolutePath() + ".");
                    changes = false;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Error saving selection.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearSelection() {
        selection = null;
        drawPanel.repaint();
    }

    private void setMode(Mode mode) {
        if (image == null) {
            return;
        }
        currentMode = mode;
        clearSelection();
        if (mode == Mode.LINES) {
            selection = lineSelection.mouseReleased();
        }
    }

    private void quitApplication() {
        if (selection != null && changes) {
            int choice = JOptionPane.showConfirmDialog(frame, "Save selection before exiting?", "Exit", JOptionPane.YES_NO_CANCEL_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                saveSelection();
            } else if (choice == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }
        System.exit(0);
    }

    private class CustomMouseListener extends MouseAdapter {
        private Point start;

        @Override
        public void mousePressed(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) {
                return;
            }
            switch (currentMode) {
                case Mode.RECTANGLE:
                    rectangleSelection.mousePressed(e.getPoint());
                    selection = null;
                    break;
                case Mode.LINES:
                    lineSelection.mousePressed(e.getPoint());
                    selection = null;
            }
            drawPanel.repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) {
                return;
            }
            switch (currentMode) {
                case Mode.RECTANGLE:
                    selection = rectangleSelection.mouseReleased(drawPanel);
                    break;
                case Mode.LINES:
                    selection = lineSelection.mouseReleased();
            }
            if (selection != null) {
                changes = true;
            }
            drawPanel.repaint();
        }
    }

    private class CustomMouseMotionListener extends MouseMotionAdapter {
        @Override
        public void mouseMoved(MouseEvent e) {
            updateMousePosition(e);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            updateMousePosition(e);
            switch (currentMode) {
                case Mode.RECTANGLE:
                    rectangleSelection.mouseMoved(e.getPoint(), drawPanel);
                    break;
                case Mode.LINES:
                    lineSelection.mouseMoved(e.getPoint(), drawPanel);
            }
        }

        private void updateMousePosition(MouseEvent e) {
            Point p = e.getPoint();
            mousePositionLabel.setText(String.format("Mouse Position: (%d, %d)", p.x, p.y));
            if (image != null && resizedImage != null && p.x < resizedImage.getWidth() && p.y < resizedImage.getHeight()) {
                double scaleX = (double) image.getWidth() / resizedImage.getWidth();
                double scaleY = (double) image.getHeight() / resizedImage.getHeight();
                int originalX = (int) (p.x * scaleX);
                int originalY = (int) (p.y * scaleY);
                if (originalX < image.getWidth() && originalY < image.getHeight()) {
                    int color = image.getRGB(originalX, originalY);
                    colorLabel.setText(String.format("Color: #%06X", (color & 0xFFFFFF)));
                }
            }
        }
    }

    private class CustomKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W -> loadImage();
                case KeyEvent.VK_Z -> saveSelection();
                case KeyEvent.VK_K -> setMode(Mode.RECTANGLE);
                case KeyEvent.VK_L -> setMode(Mode.LINES);
                case KeyEvent.VK_C -> setMode(Mode.NONE);
                case KeyEvent.VK_Q -> quitApplication();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ImageCroppingTool::new);
    }
}
