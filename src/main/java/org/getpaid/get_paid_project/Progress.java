package org.getpaid.get_paid_project;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;

class Progress {

    @FXML
    private ProgressBar pr_br;
    private Runnable onProgressFinishedCallBack;
    @FXML
    Label load_status;


    public void setOnProgressFinishedCallBack(Runnable callBack) {
        this.onProgressFinishedCallBack = callBack;
    }

    public void run() {
        new Thread(() -> {
            for (int i = 0; i <= 100; i++) {
                try {
                    Thread.sleep(40);
                    updateUI(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Platform.runLater(() -> {
                if (onProgressFinishedCallBack != null) {
                    onProgressFinishedCallBack.run();
                }

                load_status.setText("Progress finished");
            });
        }).start();
    }

    private void updateUI(int value) {
        Platform.runLater(() -> {
            if (pr_br != null) {
                pr_br.setProgress(value / 100.0);
            }
            if (load_status != null) {
                load_status.setText(value + "%");
            }
        });

    }
}

