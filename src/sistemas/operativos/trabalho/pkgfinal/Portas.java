package sistemas.operativos.trabalho.pkgfinal;

import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Portas implements Runnable {

    private Elevador elevador;
    private Controlo control;

    public Portas(Elevador elevador, Controlo control) {
        this.elevador = elevador;
        this.control = control;
    }

    @Override
    public void run() {
        JFrame f = new JFrame(Thread.currentThread().getName());
        JLabel l = new JLabel();

        do {
            if (elevador.getDoorsclosed() == false) {
                l.setText("Estado das portas: Abertas");
            } else {
                l.setText("Estado das portas: Fechadas");
            }

            l.setFont(new Font("Arial", Font.PLAIN, 35));

            f.add(l);
            f.setSize(500, 675);
            f.setLocation(1300, 100);
            f.setVisible(true);

            /* Fica à espera que haja um release para actualizar as informações do estado das portas */
            try {
                control.semPortas.acquire();
            } catch (InterruptedException ex) {
                Logger.getLogger(Portas.class.getName()).log(Level.SEVERE, null, ex);
            }

        } while (control.running == true);
    }
}
