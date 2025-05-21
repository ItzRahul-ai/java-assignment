import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ToDoTaskGUI extends JFrame {
    private DefaultListModel<String> taskModel;
    private JList<String> taskList;
    private static final String FILE_NAME = "tasks.txt";

    public ToDoTaskGUI() {
        setTitle("ToDo Task System");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        taskModel = new DefaultListModel<>();
        taskList = new JList<>(taskModel);
        taskList.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Tasks"));
        scrollPane.setPreferredSize(new Dimension(460, 200));
        add(scrollPane, BorderLayout.NORTH);

        // Buttons
        JButton addBtn = createButton("Add");
        JButton deleteBtn = createButton("Delete");
        JButton editBtn = createButton("Edit Task");
        JButton showAllBtn = createButton("Show All");
        JButton saveBtn = createButton("Save Tasks");

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(showAllBtn);
        buttonPanel.add(editBtn);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(buttonPanel, BorderLayout.CENTER);

        // Bottom save button panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(saveBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Add Task
        addBtn.addActionListener(e -> {
            String task = JOptionPane.showInputDialog(this, "Enter new task:");
            if (task != null && !task.trim().isEmpty()) {
                taskModel.addElement(task.trim());
            }
        });

        // Delete Task with Confirmation
        deleteBtn.addActionListener(e -> {
            int index = taskList.getSelectedIndex();
            if (index != -1) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this task?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    taskModel.remove(index);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to delete.");
            }
        });

        // Edit Task
        editBtn.addActionListener(e -> {
            int index = taskList.getSelectedIndex();
            if (index != -1) {
                String currentTask = taskModel.getElementAt(index);
                String newTask = JOptionPane.showInputDialog(this, "Edit task:", currentTask);
                if (newTask != null && !newTask.trim().isEmpty()) {
                    taskModel.set(index, newTask.trim());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to edit.");
            }
        });

        // Show All Tasks (from file)
        showAllBtn.addActionListener(e -> loadTasks());

        // Save Tasks
        saveBtn.addActionListener(e -> saveTasks());

        // Load tasks on startup
        loadTasks();

        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setPreferredSize(new Dimension(150, 40));
        return button;
    }

    private void loadTasks() {
        taskModel.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                taskModel.addElement(line);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No saved tasks found.");
        }
    }

    private void saveTasks() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < taskModel.size(); i++) {
                writer.println(taskModel.getElementAt(i));
            }
            JOptionPane.showMessageDialog(this, "Tasks saved successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving tasks.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ToDoTaskGUI::new);
    }
}
