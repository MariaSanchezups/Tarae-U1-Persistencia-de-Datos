package com.mycompany.ormestudiantes;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.out.println(
                        "Se utilizará el estilo predeterminado.");
            }

            new ProductoForm().setVisible(true);
        });
    }
}
