package view;

import javax.swing.*; //Classes para criar a interface gráfica (botões, janelas, etc.).
import javax.swing.border.EmptyBorder; // Para adicionar margens vazias em componentes.
import java.awt.*; // Classes básicas de layout e cores (BorderLayout, GridBagLayout, etc.).
import java.text.DecimalFormat; // Formata números em formato monetário (ex: R$ 20,00).
import java.awt.event.ActionEvent; // Para lidar com eventos de botões.
import java.awt.event.ActionListener; // Para lidar com eventos de botões.
import java.util.List; // Para trabalhar com listas de produtos.
import dao.ProdutoDAO; // Importa a classe ProdutoDAO para acessar o banco de dados.
import model.Produto; // Importa a classe Produto para representar os produtos.


// Classe principal que representa o carrinho de compras.
public class CarrinhoApp {
    // Atributos da classe
    private JFrame frame; // Janela principal da aplicação.
    private JPanel cartItemsPanel; // Painel que contém os itens do carrinho.
    private JLabel totalLabel; // Rótulo que exibe o total do carrinho.
    private DecimalFormat currencyFormat = new DecimalFormat("R$ #,##0.00"); // Formata valores em moeda
    private double currentTotal = 0.0; // Armazena o total acumulado dos itens
    private ProdutoDAO produtoDAO; // Acesso ao banco de dados para produtos

    // Construtor da classe CarrinhoApp
    public CarrinhoApp() {
        frame = new JFrame("Carrinho de Compras"); // Cria a janela principal com o título "Carrinho de Compras".
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Define o comportamento ao fechar a janela ao clicar no "X".
        frame.setSize(900, 700); // Define o tamanho da janela.
        frame.setLocationRelativeTo(null); // Centraliza a janela na tela.

        // Painel principal que contém todos os componentes da interface.
        // Define o layout do painel principal como BorderLayout com espaçamento de 10 pixels.
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // EmptyBorder adiciona margem de 10 pixels em todos os lados
        mainPanel.add(createTopBar(), BorderLayout.NORTH); // createTopBar adiciona o cabeçalho no topo
        mainPanel.add(createCenterContainer(), BorderLayout.CENTER); // createCenterContainer adiciona o painel central
        mainPanel.add(createFooter(), BorderLayout.SOUTH); // createFooter adiciona o rodapé com o total e botões

        // Exibição da janela
        // Adiciona o mainPanel ao frame e torna a janela visível
        frame.add(mainPanel);
        frame.setVisible(true);

        // Inicializa o ProdutoDAO para a conexão com o banco de dados
        produtoDAO = new ProdutoDAO();
        // Chama o método para carregar os produtos do banco de dados ao iniciar o aplicativo
        loadProductsAndAddToList();

        // As linhas de adição manual de produtos (ex: "PRODUTO 01", "PRODUTO 02", etc.)
        // foram removidas pois agora os produtos são carregados dinamicamente do banco de dados.
    }

    // Método loadProductsAndAddToList carrega os produtos do banco de dados e adiciona ao carrinho
    private void loadProductsAndAddToList() {
        // Limpa o painel de itens do carrinho antes de adicionar novos produtos.
        // Isso é importante para evitar duplicação se o método for chamado novamente.
        cartItemsPanel.removeAll();
        currentTotal = 0.0; // Reseta o total acumulado ao recarregar os itens.
        updateTotalLabel(); // Atualiza o rótulo do total para mostrar 0 antes de carregar os novos itens.

        List<Produto> produtosDoBanco = produtoDAO.getAllProdutos(); // Pega a lista de produtos do banco de dados.

        if (produtosDoBanco.isEmpty()) {
            showMsg("Nenhum produto encontrado no banco de dados. Verifique a conexão e a tabela 'produtos'.");
        } else {
            for (Produto p : produtosDoBanco) {
                // Adiciona cada produto encontrado no banco ao carrinho com uma quantidade inicial de 1.
                // O valor unitário e outros dados do produto vêm diretamente do objeto Produto.
                addCartItem(p.getNome(), p.getTamanho(), 1, p.getValorUnitario());
            }
        }
        // Repinta e revalida o painel para garantir que os novos itens apareçam na interface.
        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
    }

    // Métodos createTopBar para criar os componentes da interface gráfica (Cabeçalho)
    private JPanel createTopBar() {
        // topBar é o painel com botão Voltar (esquerda), título (centro) e botão Continuar Compra (direita)
        JPanel topBar = new JPanel(new BorderLayout());

        JLabel title = new JLabel("MEU CARRINHO", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        topBar.add(title, BorderLayout.CENTER);

        JButton continuarBtn = new JButton("Continuar Compra");
        // showMsg exibe uma mensagem quando o botão é clicado
        continuarBtn.addActionListener(e -> showMsg("Botão de Continuar Compra Clicado"));
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.add(continuarBtn);
        topBar.add(right, BorderLayout.EAST);

        return topBar;
    }

    // Método createCenterContainer cria o painel central que contém os detalhes do pedido e os itens do carrinho
    private JPanel createCenterContainer() {
        JPanel center = new JPanel(new BorderLayout());
        // createOrderDetailsPanel adiciona os detalhes do pedido no topo "Endereço" e "Telefone"
        center.add(createOrderDetailsPanel(), BorderLayout.NORTH);

        // cartItemsPanel é o painel que contém os itens individuais do carrinho.
        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        cartItemsPanel.setBackground(new Color(240, 240, 240));
        cartItemsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JScrollPane scroll = new JScrollPane(cartItemsPanel);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        center.add(scroll, BorderLayout.CENTER);

        return center;
    }

    // Método createOrderDetailsPanel cria o painel com os detalhes do pedido (Endereço e Telefone)
    private JPanel createOrderDetailsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addLabelField(panel, gbc, 0, "Endereço:", 20);
        addLabelField(panel, gbc, 2, "Telefone:", 15);

        return panel;
    }

    // Método addLabelField adiciona um rótulo e um campo de texto ao painel
    private void addLabelField(JPanel panel, GridBagConstraints gbc, int x, String label, int size) {
        gbc.gridx = x;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = x + 1;
        gbc.weightx = 1.0;
        panel.add(new JTextField(size), gbc);
        gbc.weightx = 0;
    }

    // Método createFooter cria o rodapé com o total do carrinho e botões "Adicionar Item" e "Finalizar Pedido"
    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(new EmptyBorder(10, 0, 0, 0));

        totalLabel = new JLabel("Total do Carrinho: " + currencyFormat.format(currentTotal), SwingConstants.RIGHT);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        footer.add(totalLabel, BorderLayout.EAST);

        // Painel com botões "Adicionar Item" e "Finalizar Pedido"
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        JButton finalizar = new JButton("Finalizar Pedido");
        finalizar.addActionListener(e -> showMsg("Botão Finalizar Pedido clicado!"));
        buttons.add(finalizar);

        footer.add(buttons, BorderLayout.SOUTH);
        return footer;
    }

    // Método addCartItem adiciona um item ao carrinho com nome, tamanho, quantidade e valor unitário e um painel para cada item
    private void addCartItem(String name, String size, int quantity, double unitValue) {
        JPanel itemPanel = new JPanel(new GridBagLayout());
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(10, 10, 10, 10)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridheight = 2;
        itemPanel.add(new JCheckBox(), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.gridheight = 1; gbc.anchor = GridBagConstraints.WEST;
        itemPanel.add(new JLabel(name), gbc);
        gbc.gridy = 1;
        itemPanel.add(new JLabel("Tamanho: " + size), gbc);

        // --- Adição dos botões de quantidade e do rótulo de quantidade ---
        JPanel quantityControls = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        quantityControls.setBackground(Color.WHITE);

        JButton minusButton = new JButton("-");
        JLabel quantityLabel = new JLabel(String.valueOf(quantity));
        JButton plusButton = new JButton("+");

        minusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentQuantity = Integer.parseInt(quantityLabel.getText());
                if (currentQuantity > 1) {
                    currentQuantity--;
                    quantityLabel.setText(String.valueOf(currentQuantity));
                    // Atualiza o valor total do item e o total geral
                    currentTotal -= unitValue;
                    updateItemAndTotalLabels(itemPanel, currentQuantity, unitValue);
                } else if (currentQuantity == 1) {
                    // Caixa de diálogo de confirmação de remoção do item quando a quantidade chega a 1
                    int resposta = JOptionPane.showConfirmDialog(frame, "Deseja remover o item " + name + " do carrinho?", "Remover Item", JOptionPane.YES_NO_OPTION);
                    // Se o usuário clicar em Sim (YES_OPTION), remove o item do carrinho
                    if (resposta == JOptionPane.YES_OPTION) {
                        currentTotal -= unitValue; // Subtrai o valor unitário do total
                        updateTotalLabel();
                        cartItemsPanel.remove(itemPanel); // Remove o painel do item da interface
                        cartItemsPanel.revalidate(); // Revalida o layout do painel de itens
                        cartItemsPanel.repaint(); // Repinta o painel para refletir a mudança
                        showMsg(name + " removido com sucesso");
                    }
                }
            }
        });

        plusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentQuantity = Integer.parseInt(quantityLabel.getText());
                currentQuantity++;
                quantityLabel.setText(String.valueOf(currentQuantity));
                // Atualiza o valor total do item e o total geral
                currentTotal += unitValue; // Adiciona o valor unitário ao total
                updateItemAndTotalLabels(itemPanel, currentQuantity, unitValue);
            }
        });

        quantityControls.add(minusButton);
        quantityControls.add(quantityLabel);
        quantityControls.add(plusButton);

        gbc.gridx = 2; gbc.gridy = 0; gbc.gridheight = 2; gbc.anchor = GridBagConstraints.CENTER;
        itemPanel.add(quantityControls, gbc);
        // --- Fim da adição dos botões de quantidade ---

        gbc.gridx = 3; gbc.gridy = 0; gbc.gridheight = 2; gbc.anchor = GridBagConstraints.EAST;
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        actions.setBackground(Color.WHITE);
        //Botão remove o item do carrinho e atualiza o total
        JButton del = new JButton("Remover");
        del.addActionListener(e -> {
            // Caixa de diálogo de confirmação de remoção do item
            int resposta = JOptionPane.showConfirmDialog(frame, "Deseja remover o item " + name + " do carrinho?", "Remover Item", JOptionPane.YES_NO_OPTION);
            // Se o usuário clicar em Sim (YES_OPTION), remove o item do carrinho
            if (resposta == JOptionPane.YES_OPTION) {
                // Ao remover, pegamos a quantidade atual do item para subtrair corretamente do total
                int currentQuantity = Integer.parseInt(quantityLabel.getText());
                currentTotal -= currentQuantity * unitValue;
                updateTotalLabel();
                cartItemsPanel.remove(itemPanel);
                cartItemsPanel.revalidate();
                cartItemsPanel.repaint();
                showMsg(name + " removido com sucesso");
            }
        });
        actions.add(del);
        itemPanel.add(actions, gbc);

        // Modificando a exibição de preços para ser atualizável
        JPanel prices = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        prices.setBackground(Color.WHITE);
        JLabel quantityDisplayLabel = new JLabel("Quantidade: " + quantity); // Rótulo que exibe a quantidade do item
        JLabel itemValueLabel = new JLabel("Valor: " + currencyFormat.format(unitValue)); // Rótulo que exibe o valor unitário
        JLabel itemTotalPriceLabel = new JLabel("Valor Total: " + currencyFormat.format(quantity * unitValue)); // Rótulo que exibe o valor total do item (quantidade * valor unitário)

        prices.add(quantityDisplayLabel);
        prices.add(itemValueLabel);
        prices.add(itemTotalPriceLabel);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4; // Ajuste o gridwidth para cobrir todos os itens
        gbc.anchor = GridBagConstraints.EAST;
        itemPanel.add(prices, gbc);

        cartItemsPanel.add(itemPanel);
        currentTotal += quantity * unitValue; // Adiciona o valor total do item ao total geral do carrinho.
        updateTotalLabel(); // Atualiza o rótulo que exibe o total geral do carrinho.
    }

    // --- Método para atualizar o rótulo de quantidade e o valor total do item ---
    // Este método é chamado quando a quantidade de um item é alterada (aumentada/diminuída).
    private void updateItemAndTotalLabels(JPanel itemPanel, int newQuantity, double unitValue) {
        // Percorre os componentes do painel do item para encontrar os rótulos de quantidade e valor total.
        Component[] components = itemPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel subPanel = (JPanel) comp;
                // Procura o painel que contém os labels de quantidade e valor total.
                // Assumimos que ele tem FlowLayout e contém JLabels com texto específico.
                if (subPanel.getLayout() instanceof FlowLayout && subPanel.getComponentCount() == 3) {
                    for (Component priceComp : subPanel.getComponents()) {
                        if (priceComp instanceof JLabel) {
                            JLabel label = (JLabel) priceComp;
                            // Atualiza o texto do rótulo de "Quantidade:"
                            if (label.getText().startsWith("Quantidade:")) {
                                label.setText("Quantidade: " + newQuantity);
                            }
                            // Atualiza o texto do rótulo de "Valor Total:"
                            else if (label.getText().startsWith("Valor Total:")) {
                                label.setText("Valor Total: " + currencyFormat.format(newQuantity * unitValue));
                            }
                        }
                    }
                }
            }
        }
        updateTotalLabel(); // Garante que o total geral do carrinho seja atualizado após a mudança.
    }

    // Métodos auxiliares
    private void updateTotalLabel() { // Atualiza o valor total exibido no rótulo do rodapé.
        totalLabel.setText("Total do Carrinho: " + currencyFormat.format(currentTotal));
    }

    private void showMsg(String msg) { // Exibe uma mensagem em uma janela de diálogo (pop-up).
        JOptionPane.showMessageDialog(frame, msg);
    }

    // Método main para executar a aplicação
    public static void main(String[] args) {
        SwingUtilities.invokeLater(CarrinhoApp::new); // Inicia a aplicação Swing na Thread de Despacho de Eventos (EDT).
    }
}
