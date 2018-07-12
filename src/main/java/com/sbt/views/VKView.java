package com.sbt.views;

import com.sbt.controllers.IBaseController;
import com.sbt.controllers.VKRequestController;
import com.sbt.exceptions.IdFormatException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class VKView implements IBaseView {
    private JFrame mainFrame;
    private Font myFont;
    private JTextField idVKField;
    private JTextField codeField;
    private JPanel mainPanel;
    private JTextArea idVKTextArea;
    private IBaseController controller;
    private JLabel idVKLabel;
    private JLabel codeLabel;
    private JLabel urlForCodeLabel;
    private JButton goButtn;
    private JScrollPane scrollPane0;
    private JComboBox choiceMethdComboBox;
    private boolean isAuthorize;
    private final String[] choiceStrings = { "getInfoById", "getIdByMaxLikesOnWall"};

    public VKView(String titleOfFrame, int x, int y, IBaseController controller) {
        this.controller = controller;
        mainFrame = new JFrame(titleOfFrame);
        myFont = new Font("sanserif", Font.BOLD, 17);
        mainFrame.setFont(myFont);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(x, y);
        init();
        addListeners();
        addTopanel();
    }

    public void init() {
        idVKField = new JTextField("90830190", 15);
        codeField = new JTextField("",15);
        idVKLabel = new JLabel("ID VK: ");
        codeLabel = new JLabel("Code for Auth 2.0: ");
        urlForCodeLabel = new JLabel("URL for CODE: https://oauth.vk.com/authorize?client_id=6623093&display=page&redirect_uri=https://oauth.vk.com/getItForMe.html&scope=offline&response_type=code&v=5.80");
        urlForCodeLabel.setFont(new Font("sanserif",Font.BOLD,15));
        idVKTextArea = new JTextArea("idVKTextArea",20,50);
        scrollPane0 = new JScrollPane(idVKTextArea);
        scrollPane0.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane0.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane0.setBorder(new EmptyBorder(10,5,10,5));
        idVKTextArea.setLineWrap(true);
        idVKTextArea.setFont(myFont);
        choiceMethdComboBox = new JComboBox(choiceStrings);
        goButtn = new JButton("GO!");
    }

    public void addListeners(){
        urlForCodeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                launchBrowser(urlForCodeLabel.getText().substring(14));
            }
        });

        goButtn.addActionListener(this::doQuery);
    }

    public void addTopanel(){
        JPanel south = new JPanel();
        south.add(urlForCodeLabel);
        mainPanel = new JPanel();
        mainPanel.add(choiceMethdComboBox);
        mainPanel.add(idVKLabel);
        mainPanel.add(idVKField);
        mainPanel.add(codeLabel);
        mainPanel.add(codeField);
        mainPanel.add(goButtn);

        mainFrame.getContentPane().add(BorderLayout.NORTH, mainPanel);
        mainFrame.getContentPane().add(BorderLayout.CENTER,scrollPane0);
        mainFrame.getContentPane().add(BorderLayout.SOUTH, south);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.pack();
        mainFrame.setVisible(true);
        idVKField.requestFocus();
    }

    private void launchBrowser(String uriStr) {
        Desktop desktop;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                // launch browser
                URI uri;
                try {
                    uri = new URI(uriStr);
                    desktop.browse(uri);
                }
                catch(IOException | URISyntaxException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }

    private void doQuery(ActionEvent e) {
        try {
            if (!idVKField.getText().equals("") && !codeField.getText().equals("")) {
                if (!isAuthorize) isAuthorize = controller.auth(codeField.getText().trim());
                switch (choiceMethdComboBox.getSelectedItem().toString()) {
                    case "getInfoById":
                        idVKTextArea.setText(controller.getInfoById(idVKField.getText().trim()));
                        break;
                    case "getIdByMaxLikesOnWall":
                        idVKTextArea.setText(controller.getIdByMaxLikesOnWall(idVKField.getText().trim()));
                        break;
                    default:
                        break;
                }
            }
        } catch (IdFormatException e1) {
            JOptionPane.showMessageDialog(null, "Your id isn't correct!\n" + e1.getMessage());
        }
    }
}
