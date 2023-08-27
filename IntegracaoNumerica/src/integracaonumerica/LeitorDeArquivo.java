package integracaonumerica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LeitorDeArquivo extends JFrame {

    private JTextField filePathTextField;
    private JTextField numberTextField;
    private JButton openButton;
    private JButton executeButton;
    private JCheckBox foldersOnlyCheckbox;
    private ArrayList<Double> xn = new ArrayList<>();
    private ArrayList<Double> yn = new ArrayList<>();
    private Trapezio trapezio1;
    private TresOitavosSimpson tresOitavosSimpson;
    private UmTercoSimpson umTercoSimpson;

    public LeitorDeArquivo(String metodoSelecionado) {
        
        setTitle("Seletor de Arquivos e Pastas");
        setSize(500, 150);
        setResizable(false); // Desabilita o redimensionamento da janela
        setLayout(new FlowLayout());
        
        Dimension tamanhoTela = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (tamanhoTela.width - 500) / 2;
        int y = (tamanhoTela.height - 150) / 2;
        setLocation(x, y);

        // Adicione um rótulo explicativo
        JLabel label = new JLabel("Caminho do arquivo/pasta selecionado:");
        JLabel numberLabel = new JLabel("Digite a quantidade de subintervalos:");

        // Crie o botão "Abrir" e o campo de texto para exibir o caminho do arquivo selecionado.
        filePathTextField = new JTextField(30);
        numberTextField = new JTextField(5);
        openButton = new JButton("Abrir");
        executeButton = new JButton("Executar");

        // Defina o tamanho da fonte dos botões para torná-los mais proeminentes
        openButton.setFont(new Font("Arial", Font.BOLD, 14));
        executeButton.setFont(new Font("Arial", Font.BOLD, 14));

        // Adicione uma caixa de seleção para permitir que o usuário escolha arquivos ou pastas
        foldersOnlyCheckbox = new JCheckBox("Selecionar apenas pastas");
        foldersOnlyCheckbox.setSelected(false); // Padrão é selecionar arquivos e pastas

        // Adicione um ActionListener para o botão "Abrir".
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Crie um objeto JFileChooser.
                JFileChooser fileChooser = new JFileChooser();

                // Configurar para selecionar apenas pastas, se a caixa de seleção estiver marcada
                fileChooser.setFileSelectionMode(foldersOnlyCheckbox.isSelected() ? JFileChooser.DIRECTORIES_ONLY : JFileChooser.FILES_AND_DIRECTORIES);

                // Exiba o diálogo de seleção de arquivo e aguarde até que o usuário selecione um arquivo ou cancele a ação.
                int result = fileChooser.showOpenDialog(null);

                // Se o usuário selecionar um arquivo, obtenha o caminho e exiba no campo de texto.
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    filePathTextField.setText(selectedFile.getAbsolutePath());
                }
            }
        });

        // Adicione um ActionListener para o botão "Executar".
        executeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String path = filePathTextField.getText();
                if (!path.isEmpty()) {
                    File fileToExecute = new File(path);
                    if (fileToExecute.exists() && fileToExecute.isFile() && fileToExecute.getName().endsWith(".dat")) {
                        try {
                            int subIntervalos = Integer.parseInt(numberTextField.getText())+1;
                            int counter = 0;

                            try(BufferedReader br = new BufferedReader(new FileReader(path))) {
                                String linha = "";

                                while((linha = br.readLine()) != null) {
                                    String x = linha.split("\\s+")[0];
                                    String y = linha.split("\\s+")[1];
                                    counter++;

                                    if(counter <= subIntervalos) {
                                        xn.add(Double.valueOf(x));
                                        yn.add(Double.valueOf(y));
                                    }
                                }
                            } 
                            catch (IOException ex) {
                                JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo:"+ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                                dispose();
                            }

                            Derivada derivada = new Derivada(subIntervalos, xn, yn);

                            if(metodoSelecionado == "Trapezio"){
                                trapezio1 = new Trapezio(xn, yn);

                                new Resultado(subIntervalos, trapezio1.calculo(), trapezio1.erro(derivada.DerivadaSegunda()), xn, yn).setVisible(true);
                                xn.clear();
                                yn.clear();
                            }
                            
                            if(metodoSelecionado == "SimpsonTresOitavos"){                                
                                tresOitavosSimpson = new TresOitavosSimpson(xn, yn);

                                new Resultado(subIntervalos, tresOitavosSimpson.calculo(), tresOitavosSimpson.erro(derivada.DerivadaSegunda()), xn, yn).setVisible(true);
                                xn.clear();
                                yn.clear();
                            }
                            
                            if(metodoSelecionado == "SimpsonUmTerco"){                             
                                umTercoSimpson = new UmTercoSimpson(xn, yn);

                                new Resultado(subIntervalos, umTercoSimpson.calculo(), umTercoSimpson.erro(derivada.DerivadaSegunda()), xn, yn).setVisible(true);
                                xn.clear();
                                yn.clear();
                            }

                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Não foi possível abrir o arquivo.", "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Por favor, selecione um arquivo de texto válido.", "Arquivo Inválido", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

        // Adicione os componentes à janela.
        add(label);
        add(filePathTextField);
        add(numberLabel);
        add(numberTextField);
        add(openButton);
        add(executeButton);
        add(foldersOnlyCheckbox);
    }
}