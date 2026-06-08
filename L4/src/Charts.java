import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class Charts extends JFrame {
    private JList<Function> functionList;
    private DefaultListModel<Function> listModel;
    private JTextField xminField, xmaxField, kField;
    private JPanel chartPanel;
    private Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.CYAN, Color.ORANGE, Color.PINK};
    private boolean itsFine = true;

    public Charts() {
        setTitle("Drawing charts");
        setSize(1024, 640);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        DocumentListener listener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSidePanel();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSidePanel();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSidePanel();
            }
        };

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setPreferredSize(new Dimension(256, 0));

        listModel = new DefaultListModel<>();
        functionList = new JList<>(listModel);
        functionList.setBackground(Color.LIGHT_GRAY);
        sidePanel.add(new JScrollPane(functionList));

        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        JButton addButton = new JButton("Add");
        buttons.add(addButton);
        JButton editButton = new JButton("Edit");
        buttons.add(editButton);
        editButton.setEnabled(false);
        JButton deleteButton = new JButton("Delete");
        buttons.add(deleteButton);
        deleteButton.setEnabled(false);
        sidePanel.add(buttons);

        functionList.addListSelectionListener(e -> {
            boolean isFunctionSelected = !functionList.isSelectionEmpty();
            editButton.setEnabled(isFunctionSelected);
            deleteButton.setEnabled(isFunctionSelected);
        });

        xminField = new JTextField("-10");
        xminField.setMaximumSize(new Dimension(1024, 64));
        sidePanel.add(new JLabel("xmin:"));
        sidePanel.add(xminField);
        xminField.getDocument().addDocumentListener(listener);
        xmaxField = new JTextField("10");
        xmaxField.setMaximumSize(new Dimension(1024, 64));
        sidePanel.add(new JLabel("xmax:"));
        sidePanel.add(xmaxField);
        xmaxField.getDocument().addDocumentListener(listener);
        kField = new JTextField("100");
        kField.setMaximumSize(new Dimension(1024, 64));
        sidePanel.add(new JLabel("k:"));
        sidePanel.add(kField);
        kField.getDocument().addDocumentListener(listener);

        add(sidePanel, BorderLayout.WEST);

        chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (itsFine) {
                    drawChart(g);
                }
            }
        };
        chartPanel.setBackground(Color.WHITE);
        add(chartPanel, BorderLayout.CENTER);

        addButton.addActionListener(e -> addFunction());
        editButton.addActionListener(e -> editFunction());
        deleteButton.addActionListener(e -> deleteFunction());
    }

    private void updateSidePanel()
    {
        itsFine = true;
        boolean xminFine = true, xmaxFine = true, kFine = true;
        double xmin = 0, xmax = 0;
        int k = 0;
        try {
            xmin = Double.parseDouble(xminField.getText());
            xminField.setBackground(Color.LIGHT_GRAY);
        } catch (NumberFormatException e) {
            itsFine = false;
            xminFine = false;
            xminField.setBackground(Color.RED);
        }
        try {
            xmax = Double.parseDouble(xmaxField.getText());
            xmaxField.setBackground(Color.LIGHT_GRAY);
        } catch (NumberFormatException e) {
            itsFine = false;
            xmaxFine = false;
            xmaxField.setBackground(Color.RED);
        }
        try {
            k = Integer.parseInt(kField.getText());
            kField.setBackground(Color.LIGHT_GRAY);
        } catch (NumberFormatException e) {
            itsFine = false;
            kFine = false;
            kField.setBackground(Color.RED);
        }

        if (xminFine && xmaxFine && xmin == xmax)
        {
            itsFine = false;
            xminField.setBackground(Color.RED);
            xmaxField.setBackground(Color.RED);
        }
        if (xminFine && xmin > 0)
        {
            itsFine = false;
            xminField.setBackground(Color.RED);
        }
        if (xmaxFine && xmax < 0)
        {
            itsFine = false;
            xmaxField.setBackground(Color.RED);
        }
        if (kFine && k < 2)
        {
            itsFine = false;
            kField.setBackground(Color.RED);
        }
        chartPanel.repaint();
    }

    private void setFunction(boolean add) {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));

        panel.add(new JLabel("Select function type:"));
        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Linear", "Quadratic", "Sine"});
        typeComboBox.setBackground(Color.LIGHT_GRAY);
        panel.add(typeComboBox);
        panel.add(new JLabel("Enter a:"));
        JTextField aField = new JTextField();
        panel.add(aField);
        panel.add(new JLabel("Enter b:"));
        JTextField bField = new JTextField();
        panel.add(bField);
        JLabel cLabel = new JLabel("Enter c:");
        panel.add(cLabel);
        JTextField cField = new JTextField();
        panel.add(cField);

        if (!add) {
            Function f = listModel.get(functionList.getSelectedIndex());
            String type = switch(f.type) {
                case LINEAR -> "Linear";
                case QUADRATIC -> "Quadratic";
                case SINE -> "Sine";
            };
            typeComboBox.setSelectedItem(type);
            aField.setText(String.valueOf(f.a));
            bField.setText(String.valueOf(f.b));
            cField.setText(String.valueOf(f.c));
        }

        cField.setVisible(!typeComboBox.getSelectedItem().equals("Linear"));
        cLabel.setVisible(!typeComboBox.getSelectedItem().equals("Linear"));
        typeComboBox.addActionListener(e -> {
            cField.setVisible(!typeComboBox.getSelectedItem().equals("Linear"));
            cLabel.setVisible(!typeComboBox.getSelectedItem().equals("Linear"));
            panel.revalidate();
            panel.repaint();
        });

        int result = JOptionPane.showConfirmDialog(this, panel, "New Function", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String type = (String) typeComboBox.getSelectedItem();
                double a = Double.parseDouble(aField.getText());
                double b = Double.parseDouble(bField.getText());
                double c = (cField.isVisible()) ? Double.parseDouble(cField.getText()) : 0;

                Function.FType fType = switch (type) {
                    case "Linear" -> Function.FType.LINEAR;
                    case "Quadratic" -> Function.FType.QUADRATIC;
                    case "Sine" -> Function.FType.SINE;
                    default -> throw new IllegalStateException("Unknown function type: " + type);
                };

                if (add) {
                    Function f = new Function(fType, a, b, c);
                    listModel.addElement(f);
                }
                else {
                    Function f = listModel.get(functionList.getSelectedIndex());
                    f.setFunction(fType, a, b, c);
                }
                chartPanel.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter numerical values.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addFunction() {
        setFunction(true);
    }

    private void editFunction() {
        setFunction(false);
    }

    private void deleteFunction() {
        listModel.remove(functionList.getSelectedIndex());
        chartPanel.repaint();
    }

    private void drawChart(Graphics g) {
        int width = chartPanel.getWidth();
        int height = chartPanel.getHeight();
        double xmin = Double.parseDouble(xminField.getText());
        double xmax = Double.parseDouble(xmaxField.getText());
        int k = Integer.parseInt(kField.getText());
        double ymin = 0, ymax = 0;

        for (int i = 0; i < listModel.size(); i++) {
            Function f = listModel.getElementAt(i);
            f.calculateValues(xmin, xmax, k);
        }
        for (int i = 0; i < listModel.size(); i++) {
            Function f = listModel.getElementAt(i);
            if (f.ymin < ymin) {
                ymin = f.ymin;
            }
            if (f.ymax > ymax) {
                ymax = f.ymax;
            }
        }
        if (ymin == 0 && ymax == 0) {
            ymin = -1;
            ymax = 1;
        }

        g.setColor(Color.BLACK);
        int OXy = (int) (ymax / (ymax - ymin) * (height - 96) + 48);
        int OYx = (int) (-xmin / (xmax - xmin) * (width - 96) + 48);

        g.drawLine(48, OXy, width - 48, OXy);
        g.drawLine(OYx, 48, OYx, height - 48);

        g.drawString("0", OYx + 5, OXy - 5);
        g.drawString(String.format("%.2f", xmin), 50, OXy - 5);
        g.drawString(String.format("%.2f", xmax), width - 70, OXy - 5);
        g.drawString(String.format("%.2f", ymin), OYx + 5, height - 50);
        g.drawString(String.format("%.2f", ymax), OYx + 5, 60);

        for (int i = 0; i < listModel.size(); i++) {
            Function f = listModel.getElementAt(i);
            g.setColor(colors[i % colors.length]);

            for (int j = 1; j < f.xValues.size(); j++) {
                int x1 = (int) ((f.xValues.get(j - 1) - xmin) / (xmax - xmin) * (width - 96) + 48);
                int y1 = (int) ((ymax - f.yValues.get(j - 1)) / (ymax - ymin) * (height - 96) + 48);
                int x2 = (int) ((f.xValues.get(j) - xmin) / (xmax - xmin) * (width - 96) + 48);
                int y2 = (int) ((ymax - f.yValues.get(j)) / (ymax - ymin) * (height - 96) + 48);
                g.drawLine(x1, y1, x2, y2);
            }
        }
    }

    public static void main(String[] args) {
        UIManager.put("Button.background", Color.LIGHT_GRAY);
        UIManager.put("TextField.background", Color.LIGHT_GRAY);
        SwingUtilities.invokeLater(() -> new Charts().setVisible(true));
    }
}