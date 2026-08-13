import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            DashboardSwing ventana = new DashboardSwing();
            ventana.setVisible(true);
        });
    }
}