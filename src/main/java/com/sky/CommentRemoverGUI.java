package com.sky;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public class CommentRemoverGUI extends JFrame {

    private JTextArea logArea; // 日志显示区域
    private JButton selectButton, startButton; // 按钮
    private JTextField pathField; // 路径显示
    private JCheckBox skipUrlsCheckbox; // 跳过删除网址选项
    private JCheckBox removeSingleLineCheckbox; // 删除单行注释
    private JCheckBox removeMultiLineCheckbox; // 删除多行注释
    private File selectedDirectory; // 用户选择的目录

    public CommentRemoverGUI() {
        // 初始化窗口
        setTitle("Java 注释清理工具");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 初始化界面组件
        initComponents();
    }

    private void initComponents() {
        // 顶部路径选择面板
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        pathField = new JTextField();
        pathField.setEditable(false);
        selectButton = new JButton("选择目录");
        selectButton.addActionListener(e -> selectDirectory());
        topPanel.add(pathField, BorderLayout.CENTER);
        topPanel.add(selectButton, BorderLayout.EAST);

        // 中间日志显示区域
        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        // 底部操作面板
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        // 勾选框选项
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        removeSingleLineCheckbox = new JCheckBox("删除单行注释", true);
        removeMultiLineCheckbox = new JCheckBox("删除多行注释", true);
        skipUrlsCheckbox = new JCheckBox("跳过删除网址", false);
        optionsPanel.add(removeSingleLineCheckbox);
        optionsPanel.add(removeMultiLineCheckbox);
        optionsPanel.add(skipUrlsCheckbox);

        // 操作按钮
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        startButton = new JButton("开始处理");
        startButton.setEnabled(false);
        startButton.addActionListener(e -> processDirectory());
        actionPanel.add(startButton);

        bottomPanel.add(optionsPanel);
        bottomPanel.add(actionPanel);

        // 添加组件到窗口
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void selectDirectory() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("选择 Java 项目的根目录");

        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedDirectory = chooser.getSelectedFile();
            pathField.setText(selectedDirectory.getAbsolutePath());
            startButton.setEnabled(true);
            logArea.append("选择的目录: " + selectedDirectory.getAbsolutePath() + "\n");
        }
    }

    private void processDirectory() {
        if (selectedDirectory == null) {
            JOptionPane.showMessageDialog(this, "请先选择一个目录！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        logArea.append("开始处理目录...\n");

        try {
            processDirectory(selectedDirectory);
            logArea.append("处理完成！\n");
        } catch (IOException e) {
            logArea.append("处理过程中发生错误: " + e.getMessage() + "\n");
        }
    }

    private void processDirectory(File directory) throws IOException {
        if (directory.isDirectory()) {
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                if (file.isDirectory()) {
                    processDirectory(file);
                } else if (file.getName().endsWith(".java")) {
                    processJavaFile(file.toPath());
                }
            }
        }
    }

    private void processJavaFile(Path javaFilePath) throws IOException {
        String code = Files.readString(javaFilePath);
        try {
            CompilationUnit cu = StaticJavaParser.parse(code);

            // 删除多行注释
            if (removeMultiLineCheckbox.isSelected()) {
                cu.getAllContainedComments()
                        .stream()
                        .filter(comment -> comment.isBlockComment() || comment.isJavadocComment())
                        .filter(comment -> !skipUrlsCheckbox.isSelected() || !containsUrl(comment.getContent()))
                        .forEach(comment -> comment.remove());
            }

            // 删除单行注释
            if (removeSingleLineCheckbox.isSelected()) {
                cu.getAllContainedComments()
                        .stream()
                        .filter(comment -> comment.isLineComment())
                        .filter(comment -> !skipUrlsCheckbox.isSelected() || !containsUrl(comment.getContent()))
                        .forEach(comment -> comment.remove());
            }

            Files.writeString(javaFilePath, cu.toString(), StandardOpenOption.TRUNCATE_EXISTING);
            logArea.append("处理成功: " + javaFilePath + "\n");
        } catch (Exception e) {
            logArea.append("处理失败: " + javaFilePath + "\n");
            e.printStackTrace();
        }
    }

    private boolean containsUrl(String text) {
        return text.matches(".*(http|https)://.*");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("Launching GUI..."); // 添加调试日志
            System.out.println("程序已启动！"); // 添加调试日志
            CommentRemoverGUI app = new CommentRemoverGUI();
            app.setVisible(true);
        });
    }
}
