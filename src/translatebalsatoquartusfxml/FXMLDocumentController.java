/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package translatebalsatoquartusfxml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

/**
 *
 * @author fccheng 2103/10/24
 *
 */
public class FXMLDocumentController implements Initializable {

    File file;
    @FXML
    private Label label;

    @FXML
    private Button openFileButton;

    @FXML
    private Button translateButton;

    @FXML
    private TextArea mainTextArea;

    @FXML
    private void handleOpenFileButtonAction(ActionEvent event) {
        System.out.println("handleOpenFileButtonAction");
        //label.setText("Hello World!");
        file = openFile();

        if (file != null) {
            label.setText(file.getName());
            mainTextArea.setText(readFile(file));
        } else {
            mainTextArea.setText(" No File selected! ");
        }

    }

    @FXML
    private void handleTranslateButtonAction(ActionEvent event) {
        if (mainTextArea.getText().length() == 0) {
            handleOpenFileButtonAction(event);
        }
        System.out.println("start translation ...");
        String st = this.appendQII(mainTextArea.getText());

        mainTextArea.setText(st);

    }

    @FXML
    private void handleClearButtonAction(ActionEvent event) {
        mainTextArea.setText("");
        label.setText("");
        System.out.println("clear ...");

    }

    @FXML
    private void handleSaveFileButtonAction(ActionEvent event) {

        System.out.println("save file ...");
        this.saveFile(file);

    }

    @FXML
    private void handleSaveAsFileButtonAction(ActionEvent event) {
        System.out.println("save as file ...");
        this.saveAsFile();
    }

    
    private File openFile() {
        System.out.println("Open file!");
        label.setText("reading file ....");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = fileChooser.showOpenDialog(null);
        return file;
    }

        
    private File openSaveFile() {
        System.out.println("Save file!");
        label.setText("saveing file ....");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = fileChooser.showSaveDialog(null);
        label.setText(file.getName());
        return file;
    }

    
    private String readFile(File file) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String text;
            while ((text = br.readLine()) != null) {
                sb.append(text + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    
    private boolean saveFile(File file) {
        if (file == null) {
            file = openSaveFile();
        }
        if (file == null) return false;
        
        String fileName = file.getParent() + File.separator + "Balsa_" + file.getName();
        label.setText(fileName);
        boolean flag = true;
        try {
            FileWriter fw = new FileWriter(fileName);
            fw.write(mainTextArea.getText());
            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    
    private boolean saveAsFile() {
        file = openSaveFile();
        return saveFile(file);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    final String[] gates = {"AND2", "AND3", "AND4", "OR2", "OR3", "OR4", "NAND2", "NAND3", "NAND4", "NOR2", "NOR3", "NOR4", "XOR2", "XNOR2",
        "MUTEX", "BUF", "BUFF", "INV", "LATCH", "MUX2", "NMUX2", "DEMUX2", "NKEEP", "TRIBUF", "TRIINV", "C2", "C3",
        "NC2P", "C2N", "VDD", "GND", "UDP_NKEEP", "UDP_demux2_top_half", "UDP_demux2_bottom_half", "UDP_MUX2",
        "UDP_NMUX2", "UDP_C2", "UDP_C3", "UDP_NC2P", "UDP_C2N", "UDP_mutex_top_half", "UDP_mutex_bottom_half", "UDP_LATCH"
    };

    final String[] problemGates = {"NQII_", "DRQII_", "DRXQII_", "QII_QII_", "XQII_"};
    final String[] fixedGates = {"QII_N", "DR", "DRX", "QII_", "QII_X"};

    private String appendQII(String source) {
        //StringBuilder sb = new StringBuilder(source);
        int start, end;
        String newSource;
        for (String gate : gates) {
            newSource = source.replaceAll(gate, "QII_" + gate); // note that NAND2 will also change to NQII_AND
            source = newSource;
        }

        // final fix
        for (int i = 0; i < problemGates.length; i++) {
            newSource = source.replaceAll(problemGates[i], fixedGates[i]); // note that NAND2 will also change to NQII_AND
            source = newSource;
        }

        return source;
    }

    private String addClearSignals(String source) {

        return source;
    }
}
