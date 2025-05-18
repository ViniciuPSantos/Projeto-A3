package view;
import javax.swing.*;
import dao.BazarDAO;
import java.awt.event.*;
import java.util.regex.*;

public class TelaCadastroBazar extends JFrame {
    public TelaCadastroBazar() {
        setTitle("Cadastro de Bazar");
        setSize(400, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel nomeLabel = new JLabel("Nome:");
        nomeLabel.setBounds(10, 10, 80, 25);
        JTextField nomeField = new JTextField();
        nomeField.setBounds(100, 10, 240, 25);

        JLabel cnpjLabel = new JLabel("CNPJ:");
        cnpjLabel.setBounds(10, 40, 80, 25);
        JTextField cnpjField = new JTextField();
        cnpjField.setBounds(100, 40, 240, 25);

        JLabel enderecoLabel = new JLabel("Endereço:");
        enderecoLabel.setBounds(10, 70, 80, 25);
        JTextField enderecoField = new JTextField();
        enderecoField.setBounds(100, 70, 240, 25);

        JLabel emailLabel = new JLabel("Email de Contato:");
        emailLabel.setBounds(10, 100, 120, 25);
        JTextField emailField = new JTextField();
        emailField.setBounds(130, 100, 210, 25);

        JLabel senhaLabel = new JLabel("Senha:");
        senhaLabel.setBounds(10, 130, 80, 25);
        JPasswordField senhaField = new JPasswordField();
        senhaField.setBounds(100, 130, 240, 25);

        JLabel confirmarSenhaLabel = new JLabel("Confirmar Senha:");
        confirmarSenhaLabel.setBounds(10, 160, 120, 25);
        JPasswordField confirmarSenhaField = new JPasswordField();
        confirmarSenhaField.setBounds(130, 160, 210, 25);

        JProgressBar forcaSenhaBar = new JProgressBar();
        forcaSenhaBar.setString("Força da Senha");
        forcaSenhaBar.setStringPainted(true);
        forcaSenhaBar.setBounds(10, 190, 350, 25);

        JCheckBox mostrarSenhaCheckBox = new JCheckBox("Mostrar Senha");
        mostrarSenhaCheckBox.setBounds(100, 220, 150, 25);

        JButton cadastrarBtn = new JButton("Cadastrar");
        cadastrarBtn.setBounds(10, 250, 350, 25);

        JButton loginButton = new JButton("Já tem cadastro? Faça login");
        loginButton.setBounds(10, 280, 350, 25);

        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.setBounds(10, 310, 350, 25);

        cadastrarBtn.addActionListener(e -> {
            String nome = nomeField.getText().trim();
            String cnpj = cnpjField.getText().trim();
            String endereco = enderecoField.getText().trim();
            String email = emailField.getText().trim();
            String senha = new String(senhaField.getPassword()).trim();
            String confirmarSenha = new String(confirmarSenhaField.getPassword()).trim();
            if (nome.isEmpty() || cnpj.isEmpty() || endereco.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
                return;
            }
            if (!senha.equals(confirmarSenha)) {
                JOptionPane.showMessageDialog(this, "As senhas não coincidem!");
                return;
            }

            if(!cnpj.matches("\\d{14}")){
                JOptionPane.showMessageDialog(this, "CNPJ inválido! O CNPJ deve conter 14 dígitos.");
                return;
            }

            if (!email.matches("^[\\w-\\.]+@(gmail\\.com|hotmail\\.com|outlook\\.com)$")) {
                JOptionPane.showMessageDialog(this, "Email inválido!");
                return;
            }

            if (senha.length() < 6) {
                JOptionPane.showMessageDialog(this, "A senha deve ter pelo menos 6 caracteres!");
                return;
            }
        
            BazarDAO dao = new BazarDAO();

            if (dao.verificarEmail(email)) {
                int opcao = JOptionPane.showOptionDialog(this, "Este email já está cadastrado. Deseja fazer login?", "Email já cadastrado", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,null, new String[]{"Fazer Login", "Cancelar"}, "Fazer Login");
                if (opcao == JOptionPane.YES_OPTION) {
                    new TelaLogin().setVisible(true);
                    dispose();
                }
                return;
            }

            if(dao.verificarCNPJ(cnpj)) {
                int opcao = JOptionPane.showOptionDialog(this, "Este CNPJ já está cadastrado. Deseja fazer login?", "CNPJ já cadastrado", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,null, new String[]{"Fazer Login", "Cancelar"}, "Fazer Login");
                if (opcao == JOptionPane.YES_OPTION) {
                    new TelaLogin().setVisible(true);
                    dispose();
                }
                return;
            }

            if(dao.verificarNomeBazar(nome)) {
                int opcao = JOptionPane.showOptionDialog(this, "Este nome de bazar já está cadastrado. Deseja fazer login?", "Nome de bazar já cadastrado", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,null, new String[]{"Fazer Login", "Cancelar"}, "Fazer Login");
                if (opcao == JOptionPane.YES_OPTION) {
                    new TelaLogin().setVisible(true);
                    dispose();
                }
                return;
            }

            if (dao.cadastrarBazar(nome, cnpj, endereco, email, senha)) {
                JOptionPane.showMessageDialog(this, "Cadastro realizado!");
                new TelaLogin().setVisible(true);
                dispose(); // Fecha a tela de cadastro
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar bazar.");
            }
        });

        cancelarButton.addActionListener(e -> {
            dispose(); // Fecha a tela de cadastro
        });

        loginButton.addActionListener(e -> {
            TelaLogin telaLogin = new TelaLogin();
            telaLogin.setVisible(true);
            dispose(); // Fecha a tela de cadastro
        });

        mostrarSenhaCheckBox.addActionListener(e -> {
            if (mostrarSenhaCheckBox.isSelected()) {
                senhaField.setEchoChar((char) 0);
                confirmarSenhaField.setEchoChar((char) 0);
            } else {
                senhaField.setEchoChar('•');
                confirmarSenhaField.setEchoChar('•');
            }
        });

        confirmarSenhaField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    cadastrarBtn.doClick();
                }
            }
        });

        senhaField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                atualizarForcaSenha(senhaField, forcaSenhaBar);
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                atualizarForcaSenha(senhaField, forcaSenhaBar);
            }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                atualizarForcaSenha(senhaField, forcaSenhaBar);
            }
        });

        add(nomeLabel);
        add(nomeField);
        add(cnpjLabel);
        add(cnpjField);
        add(enderecoLabel);
        add(enderecoField);
        add(emailLabel);
        add(emailField);
        add(cadastrarBtn);
        add(cancelarButton);
        add(loginButton);
        add(senhaLabel);
        add(senhaField);
        add(confirmarSenhaLabel);
        add(confirmarSenhaField);
        add(mostrarSenhaCheckBox);
        add(forcaSenhaBar);
        setLocationRelativeTo(null); // Centraliza a tela
    }

    // Método para calcular a força da senha
    private int calcularForcaSenha(String senha) {
        int forca = 0;
        if (senha.length() >= 6) forca += 20;
        if (senha.matches(".*[a-z].*")) forca += 20;
        if (senha.matches(".*[A-Z].*")) forca += 20;
        if (senha.matches(".*\\d.*")) forca += 20;
        if (senha.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) forca += 20;
        return forca;
    }

    // Método para atualizar a barra de força da senha
    private void atualizarForcaSenha(JPasswordField senhaField, JProgressBar forcaSenhaBar) {
        String senha = new String(senhaField.getPassword());
        int forca = calcularForcaSenha(senha);
        forcaSenhaBar.setValue(forca);
        if (forca < 40) {
            forcaSenhaBar.setString("Fraca");
        } else if (forca < 80) {
            forcaSenhaBar.setString("Média");
        } else {
            forcaSenhaBar.setString("Forte");
        }
    }
}
