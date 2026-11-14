
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class LibrarySearch extends JFrame {

    private final JTextField searchField = new JTextField(22);

    private final JComboBox<String> categoryBox =
            new JComboBox<>(new String[]{
                    "All Categories",
                    "Programming",
                    "Science",
                    "Mathematics"
            });

    private final JLabel statusLabel =
            new JLabel("Welcome! Search for your favourite books.");

    private final DefaultTableModel model =
            new DefaultTableModel(
                    new String[]{"Book ID", "Book Title", "Category"}, 0) {

                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    private final JTable bookTable = new JTable(model);

    public LibrarySearch() {

        setTitle("Campus Library Management System");
        setLayout(new BorderLayout(10, 10));

        // ---------- HEADER ----------
        JLabel title = new JLabel(
                "CAMPUS LIBRARY SEARCH",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel subtitle = new JLabel(
                "Search and explore available books",
                SwingConstants.CENTER
        );

        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));

        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.add(title);
        headerPanel.add(subtitle);

        add(headerPanel, BorderLayout.NORTH);

        // ---------- SEARCH PANEL ----------
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear");

        searchPanel.add(new JLabel("Book Name:"));
        searchPanel.add(searchField);
        searchPanel.add(categoryBox);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);

        // ---------- TABLE ----------
        bookTable.setRowHeight(30);
        bookTable.setFont(new Font("Arial", Font.PLAIN, 14));
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.setAutoCreateRowSorter(true);

        JTableHeader tableHeader = bookTable.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(bookTable);

        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // ---------- BOTTOM PANEL ----------
        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 15, 10)
        );

        JButton previousButton = new JButton("Previous");
        JButton nextButton = new JButton("Next");
        JButton reserveButton = new JButton("Reserve Book");

        buttonPanel.add(previousButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(reserveButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        statusLabel.setBorder(
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        );

        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // ---------- SAMPLE BOOK DATA ----------
        loadBooks();

        // ---------- EVENT HANDLING ----------
        searchButton.addActionListener(e -> searchBooks());

        searchField.addActionListener(e -> searchBooks());

        clearButton.addActionListener(e -> {
            searchField.setText("");
            categoryBox.setSelectedIndex(0);
            loadBooks();
            statusLabel.setText("Search cleared. All books displayed.");
        });

        reserveButton.addActionListener(e -> reserveBook());

        previousButton.addActionListener(e ->
                statusLabel.setText("You are on the first page.")
        );

        nextButton.addActionListener(e ->
                statusLabel.setText("All available books are displayed.")
        );

        // ---------- FRAME SETTINGS ----------
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(650, 400));
        setSize(900, 550);
        setLocationRelativeTo(null);
    }

    // ---------- LOAD BOOKS ----------
    private void loadBooks() {

        model.setRowCount(0);

        Object[][] books = {
                {"B101", "Java Programming", "Programming"},
                {"B102", "Python Basics", "Programming"},
                {"B103", "Physics Fundamentals", "Science"},
                {"B104", "Discrete Mathematics", "Mathematics"},
                {"B105", "Advanced Java", "Programming"},
                {"B106", "Computer Networks", "Science"},
                {"B107", "Data Structures", "Programming"},
                {"B108", "Applied Mathematics", "Mathematics"}
        };

        for (Object[] book : books) {
            model.addRow(book);
        }

        statusLabel.setText(model.getRowCount() + " books available.");
    }

    // ---------- SEARCH BOOKS ----------
    private void searchBooks() {

        String keyword = searchField.getText()
                .trim()
                .toLowerCase();

        String selectedCategory =
                categoryBox.getSelectedItem().toString();

        model.setRowCount(0);

        Object[][] books = {
                {"B101", "Java Programming", "Programming"},
                {"B102", "Python Basics", "Programming"},
                {"B103", "Physics Fundamentals", "Science"},
                {"B104", "Discrete Mathematics", "Mathematics"},
                {"B105", "Advanced Java", "Programming"},
                {"B106", "Computer Networks", "Science"},
                {"B107", "Data Structures", "Programming"},
                {"B108", "Applied Mathematics", "Mathematics"}
        };

        int count = 0;

        for (Object[] book : books) {

            String bookName = book[1].toString().toLowerCase();

            boolean nameMatches =
                    bookName.contains(keyword);

            boolean categoryMatches =
                    selectedCategory.equals("All Categories")
                    || book[2].equals(selectedCategory);

            if (nameMatches && categoryMatches) {
                model.addRow(book);
                count++;
            }
        }

        statusLabel.setText(count + " book(s) found.");
    }

    // ---------- RESERVE BOOK ----------
    private void reserveBook() {

        int selectedRow = bookTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a book before reserving.",
                    "No Book Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Convert view row to model row because sorting may be enabled.
        int modelRow = bookTable.convertRowIndexToModel(selectedRow);

        String bookId = model.getValueAt(modelRow, 0).toString();
        String bookName = model.getValueAt(modelRow, 1).toString();

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Do you want to reserve " + bookName + "?",
                "Confirm Reservation",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            JOptionPane.showMessageDialog(
                    this,
                    "Book Reserved Successfully!\nBook ID: " + bookId,
                    "Reservation Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

            statusLabel.setText(
                    "Reservation completed for " + bookName
            );
        }
    }

    // ---------- MAIN METHOD ----------
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            LibrarySearch window = new LibrarySearch();
            window.setVisible(true);
        });
    }
}
