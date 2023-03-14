package controller;

import ai.*;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class AiLearningOverviewController {
    @FXML
    private Label messageLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button startButton;

    @FXML
    public void onStartButtonClick() {
        try {
            int size = 9;
            messageLabel.setText("START TRAINING ...");
            //
            //			int[] layers = new int[]{ size, 128, 128, size };

            ConfigFileLoader cfl = new ConfigFileLoader();
            cfl.loadConfigFile("./resources/config.txt");
            Config config = cfl.get("F");
            System.out.println("Test.main() : " + config);
            int l = config.numberOfhiddenLayers;
            int h = config.hiddenLayerSize;
            double lr = config.learningRate;
            double epochs = 10000 ;
            HashMap<Integer, Coup> mapTrain = loadCoupsFromFile("./resources/train_dev_test/train.txt");

            int[] layers = new int[l + 2];
            layers[0] = size;
            for (int i = 0; i < l; i++) {
                layers[i + 1] = h;
            }
            layers[layers.length - 1] = size;
            //
            MultiLayerPerceptron net = new MultiLayerPerceptron(layers, lr, new SigmoidalTransferFunction());

            messageLabel.setText("Load data ...");

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    double error = 0.0;
                    //TRAINING ...
                    for (int i = 0; i < epochs; i++) {

                        Coup c = null;
                        while (c == null)
                            c = mapTrain.get((int) (Math.round(Math.random() * mapTrain.size())));

                        error += net.backPropagate(c.in, c.out);
                        updateMessage(i+", "+epochs);
                        if (i % 10000 == 0) {
                            updateMessage("Error at step " + i + " is " + (error / (double) i));
                        }
                        updateProgress(i, epochs);
                    }
                    updateMessage("Learning completed!");
                    return null;
                }
            };
            progressBar.progressProperty().bind(task.progressProperty());
            task.messageProperty().addListener((obs, oldMsg, newMsg) -> {
                messageLabel.setText(newMsg);
            });
            Thread thread = new Thread(task);
            thread.start();
        } catch (Exception e) {
            System.out.println("Test.main()");
            e.printStackTrace();
            System.exit(-1);
        }

    }

    public static HashMap<Integer, Coup> loadCoupsFromFile(String file){
        System.out.println("loadCoupsFromFile from "+file+" ...");
        HashMap<Integer, Coup> map = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file))));
            String s = "";
            while ((s = br.readLine()) != null) {
                String[] sIn = s.split("\t")[0].split(" ");
                String[] sOut = s.split("\t")[1].split(" ");

                double[] in = new double[sIn.length];
                double[] out = new double[sOut.length];

                for (int i = 0; i < sIn.length; i++) {
                    in[i] = Double.parseDouble(sIn[i]);
                }

                for (int i = 0; i < sOut.length; i++) {
                    out[i] = Double.parseDouble(sOut[i]);
                }

                Coup c = new Coup(9, "");
                c.in = in ;
                c.out = out ;
                map.put(map.size(), c);
            }
            br.close();
        }
        catch (Exception e) {
            System.out.println("Test.loadCoupsFromFile()");
            e.printStackTrace();
            System.exit(-1);
        }
        return map ;
    }
}