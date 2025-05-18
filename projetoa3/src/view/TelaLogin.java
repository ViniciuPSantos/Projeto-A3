package view;

import dao.BazarDAO;
import dao.UsuarioDAO;
import javax.swing.*;
import java.awt.event.*;
import java.util.regex.*;

public class TelaLogin extends JFrame{
    public TelaLogin(){
        setTitle("Login");
        setSize(300,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(10, 10, 80, 25);
        JTextField emailField = new JTextField();
        emailField.setBounds(100, 10, 160, 25);

        JLabel senhaLabel = new JLabel("Senha:");
        senhaLabel.setBounds(10, 40, 80, 25);
        JPasswordField senhaField = new JPasswordField();
        senhaField.setBounds(100, 40, 160, 25);

        JLabel tipoLabel = new JLabel("Tipo:");
        tipoLabel.setBounds(10, 70, 80, 25);
        String[] tipos = {"Usuário", "Bazar"};
        JComboBox<String> tipoComboBox = new JComboBox<>(tipos);
        tipoComboBox.setBounds(100, 70, 160, 25);

        JCheckBox mostrarSenhaCheckBox = new JCheckBox("Mostrar Senha");
        mostrarSenhaCheckBox.setBounds(100, 70 + 30, 160, 25);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(10, 130, 250, 25);

        JButton esqueceuSenhaButton = new JButton("Esqueci minha senha");
        esqueceuSenhaButton.setBounds(10, 160, 250, 25);

        JButton naoTemCadastrButton = new JButton("Não tem cadastro? Clique aqui!");
        naoTemCadastrButton.setBounds(10, 190, 250, 25);

        loginBtn.addActionListener(e ->{
            String email = emailField.getText().trim();
            String senha = new String(senhaField.getPassword()).trim();
            String tipo = (String) tipoComboBox.getSelectedItem();

            if (email.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
                return;
            }

            if (tipo.equals("Usuário")) {
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                if (usuarioDAO.loginUsuario(email, senha)) {
                    JOptionPane.showMessageDialog(this, "Login realizado com sucesso!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Email ou senha incorretos.");
                }
            } else if (tipo.equals("Bazar")) {
                BazarDAO bazarDAO = new BazarDAO();
                if (bazarDAO.login(email, senha)) {
                    JOptionPane.showMessageDialog(this, "Login realizado com sucesso!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Email ou senha incorretos.");
                }
            }
        });

        naoTemCadastrButton.addActionListener(e -> {
            TelaCadastro telaCadastro = new TelaCadastro();
            telaCadastro.setVisible(true);
            dispose();
        });

        mostrarSenhaCheckBox.addActionListener(e -> {
            if (mostrarSenhaCheckBox.isSelected()) {
                senhaField.setEchoChar((char) 0);
            } else {
                senhaField.setEchoChar('•');
            }
        });

        esqueceuSenhaButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha o campo de email!");
                return;
            }
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            if (usuarioDAO.verificarEmail(email)) {
                JOptionPane.showMessageDialog(this, "Um email de recuperação foi enviado para " + email);
            } else {
                JOptionPane.showMessageDialog(this, "Email não cadastrado.");
            }
        });

        senhaField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginBtn.doClick();
                }
            }
        });

        add(emailLabel);
        add(emailField);
        add(senhaLabel);
        add(senhaField);
        add(loginBtn);
        add(naoTemCadastrButton);
        add(tipoLabel);
        add(tipoComboBox);
        add(mostrarSenhaCheckBox);
        add(esqueceuSenhaButton);
        setLocationRelativeTo(null); // Centraliza a tela
    }
}