import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class EventRegistrationApp extends JFrame {
    private static final String URL =
        "jdbc:mysql://localhost:3306/campus_events";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password";

    private final RegistrationDAO dao = new RegistrationDAO();
    private final RegistrationService service = new RegistrationService(dao);
    private final JTextField studentId = new JTextField(15);
    private final JTextField studentName = new JTextField(15);
    private final JTextField email = new JTextField(15);
    private final JComboBox<Event> eventBox = new JComboBox<>();
    private final JComboBox<Event> filterBox = new JComboBox<>();

    private final DefaultTableModel model = new DefaultTableModel(
        new String[]{"ID","Student ID","Name","Email","Event","Date"}, 0) {
        @Override public boolean isCellEditable(int row, int col) {
            return false;
        }
    };
    private final JTable table = new JTable(model);
    private final JLabel status = new JLabel("Ready");
    private int currentPage = 0;
    private static final int PAGE_SIZE = 20;

    public EventRegistrationApp() {
        super("Campus Event Registration");
        setLayout(new BorderLayout(10, 10));
        createMenu();

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Student Registration"));
        JButton registerButton = new JButton("Register");
        form.add(new JLabel("Student ID")); form.add(studentId);
        form.add(new JLabel("Student Name")); form.add(studentName);
        form.add(new JLabel("Email")); form.add(email);
        form.add(new JLabel("Select Event")); form.add(eventBox);
        form.add(new JLabel("")); form.add(registerButton);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        JPanel controls = new JPanel(new FlowLayout());
        JButton filterButton = new JButton("Filter");
        JButton previousButton = new JButton("Previous");
        JButton nextButton = new JButton("Next");
        JButton exportButton = new JButton("Export CSV");
        controls.add(new JLabel("Filter Event")); controls.add(filterBox);
        controls.add(filterButton); controls.add(previousButton);
        controls.add(nextButton); controls.add(exportButton);
        top.add(controls, BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);

        registerButton.addActionListener(e -> registerStudent());
        filterButton.addActionListener(e -> {
            currentPage = 0; loadRegistrations();
        });
        previousButton.addActionListener(e -> {
            if (currentPage > 0) { currentPage--; loadRegistrations(); }
        });
        nextButton.addActionListener(e -> {
            currentPage++; loadRegistrations();
        });
        exportButton.addActionListener(e -> exportCSV());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        loadEvents();
        loadRegistrations();
    }

    private void createMenu() {
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem refresh = new JMenuItem("Refresh");
        JMenuItem exit = new JMenuItem("Exit");
        refresh.addActionListener(e -> {
            currentPage = 0; loadEvents(); loadRegistrations();
        });
        exit.addActionListener(e -> dispose());
        file.add(refresh); file.add(exit); bar.add(file);
        setJMenuBar(bar);
    }

    private void loadEvents() {
        new SwingWorker<List<Event>, Void>() {
            @Override protected List<Event> doInBackground() throws Exception {
                return dao.getEvents();
            }
            @Override protected void done() {
                try {
                    List<Event> events = get();
                    eventBox.removeAllItems();
                    filterBox.removeAllItems();
                    filterBox.addItem(new Event(0, "All Events"));
                    for (Event event : events) {
                        eventBox.addItem(event);
                        filterBox.addItem(event);
                    }
                    status.setText("Events loaded");
                } catch (Exception e) { showError(e); }
            }
        }.execute();
    }

    private void registerStudent() {
        String id = studentId.getText().trim();
        String name = studentName.getText().trim();
        String mail = email.getText().trim();
        Event selected = (Event) eventBox.getSelectedItem();

        if (id.isEmpty() || name.isEmpty() || mail.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required");
            return;
        }
        if (!mail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(this, "Enter a valid email address");
            return;
        }
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Select an event");
            return;
        }

        int eventId = selected.id;
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                service.register(id, name, mail, eventId);
                return null;
            }
            @Override protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(
                        EventRegistrationApp.this, "Registration successful");
                    studentId.setText(""); studentName.setText(""); email.setText("");
                    currentPage = 0; loadRegistrations();
                } catch (Exception e) { showError(e); }
            }
        }.execute();
        status.setText("Registering...");
    }

    private void loadRegistrations() {
        Event selected = (Event) filterBox.getSelectedItem();
        int eventId = selected == null ? 0 : selected.id;
        int offset = currentPage * PAGE_SIZE;

        new SwingWorker<List<Registration>, Void>() {
            @Override protected List<Registration> doInBackground()
                    throws Exception {
                return dao.getRegistrations(eventId, PAGE_SIZE, offset);
            }
            @Override protected void done() {
                try {
                    List<Registration> records = get();
                    model.setRowCount(0);
                    for (Registration r : records) {
                        model.addRow(new Object[]{r.id, r.studentId,
                            r.studentName, r.email, r.eventName, r.date});
                    }
                    status.setText("Page " + (currentPage + 1)
                        + " | Records displayed: " + records.size());
                } catch (Exception e) { showError(e); }
            }
        }.execute();
    }

    private void exportCSV() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File("registrations.csv"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                Event selected = (Event) filterBox.getSelectedItem();
                int eventId = selected == null ? 0 : selected.id;
                List<Registration> records = dao.getAllRegistrations(eventId);
                try (FileWriter writer = new FileWriter(chooser.getSelectedFile())) {
                    writer.write("ID,Student ID,Name,Email,Event,Date\n");
                    for (Registration r : records) {
                        writer.write(csv(r.id) + "," + csv(r.studentId) + ","
                            + csv(r.studentName) + "," + csv(r.email) + ","
                            + csv(r.eventName) + "," + csv(r.date) + "\n");
                    }
                }
                return null;
            }
            @Override protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(
                        EventRegistrationApp.this, "CSV exported successfully");
                } catch (Exception e) { showError(e); }
            }
        }.execute();
    }

    private String csv(Object value) {
        String text = String.valueOf(value).replace("\"", "\"\"");
        return "\"" + text + "\"";
    }

    private void showError(Exception e) {
        Throwable cause = e.getCause();
        String message = cause == null ? e.getMessage() : cause.getMessage();
        JOptionPane.showMessageDialog(this, message, "Error",
            JOptionPane.ERROR_MESSAGE);
        status.setText("Operation failed");
    }

    static class Event {
        int id; String name;
        Event(int id, String name) { this.id = id; this.name = name; }
        @Override public String toString() { return name; }
    }

    static class Registration {
        int id; String studentId; String studentName;
        String email; String eventName; String date;
    }

    static class RegistrationService {
        private final RegistrationDAO dao;
        RegistrationService(RegistrationDAO dao) { this.dao = dao; }

        void register(String studentId, String name,
                      String email, int eventId) throws Exception {
            if (studentId.isBlank() || name.isBlank() || email.isBlank())
                throw new IllegalArgumentException("All fields are required");
            if (eventId <= 0)
                throw new IllegalArgumentException("Invalid event");
            dao.insertRegistration(studentId, name, email, eventId);
        }
    }

    static class RegistrationDAO {
        private Connection connect() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }

        List<Event> getEvents() throws SQLException {
            List<Event> list = new ArrayList<>();
            String sql = "SELECT event_id, event_name FROM events";
            try (Connection c = connect();
                 PreparedStatement ps = c.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    list.add(new Event(rs.getInt("event_id"),
                                       rs.getString("event_name")));
            }
            return list;
        }

        void insertRegistration(String studentId, String name,
                                String email, int eventId) throws SQLException {
            String sql = "INSERT INTO registrations "
                + "(student_id, student_name, email, event_id) VALUES (?, ?, ?, ?)";
            try (Connection c = connect();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, studentId); ps.setString(2, name);
                ps.setString(3, email); ps.setInt(4, eventId);
                ps.executeUpdate();
            } catch (SQLException e) {
                if (e.getErrorCode() == 1062)
                    throw new SQLException(
                        "Student is already registered for this event", e);
                throw e;
            }
        }

        List<Registration> getRegistrations(
                int eventId, int limit, int offset) throws SQLException {
            List<Registration> list = new ArrayList<>();
            String sql = "SELECT r.registration_id, r.student_id, "
                + "r.student_name, r.email, e.event_name, r.registration_date "
                + "FROM registrations r JOIN events e ON r.event_id=e.event_id "
                + (eventId == 0 ? "" : "WHERE r.event_id=? ")
                + "ORDER BY r.registration_id LIMIT ? OFFSET ?";
            try (Connection c = connect();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                int index = 1;
                if (eventId != 0) ps.setInt(index++, eventId);
                ps.setInt(index++, limit); ps.setInt(index, offset);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Registration r = new Registration();
                        r.id = rs.getInt("registration_id");
                        r.studentId = rs.getString("student_id");
                        r.studentName = rs.getString("student_name");
                        r.email = rs.getString("email");
                        r.eventName = rs.getString("event_name");
                        r.date = rs.getTimestamp("registration_date").toString();
                        list.add(r);
                    }
                }
            }
            return list;
        }

        List<Registration> getAllRegistrations(int eventId)
                throws SQLException {
            List<Registration> list = new ArrayList<>();
            String sql = "SELECT r.registration_id, r.student_id, "
                + "r.student_name, r.email, e.event_name, r.registration_date "
                + "FROM registrations r JOIN events e ON r.event_id=e.event_id "
                + (eventId == 0 ? "" : "WHERE r.event_id=? ")
                + "ORDER BY r.registration_id";
            try (Connection c = connect();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                if (eventId != 0) ps.setInt(1, eventId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Registration r = new Registration();
                        r.id = rs.getInt("registration_id");
                        r.studentId = rs.getString("student_id");
                        r.studentName = rs.getString("student_name");
                        r.email = rs.getString("email");
                        r.eventName = rs.getString("event_name");
                        r.date = rs.getTimestamp("registration_date").toString();
                        list.add(r);
                    }
                }
            }
            return list;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new EventRegistrationApp().setVisible(true));
    }
}
