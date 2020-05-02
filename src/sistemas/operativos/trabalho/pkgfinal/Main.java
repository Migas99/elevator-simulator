package sistemas.operativos.trabalho.pkgfinal;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {

        Elevador elevador = new Elevador();
        Controlo control = new Controlo(elevador);
        GuardarLogs logs = new GuardarLogs(elevador);
        LerConfig config = new LerConfig(elevador);
        
        try {
            config.ler();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Thread botoneira = new Thread(new Botoneira(elevador, control, logs), "Botoneira");
        Thread portas = new Thread(new Portas(elevador, control), "Portas");
        Thread motor = new Thread(new Motor(elevador, control), "Motor");

        botoneira.start();
        portas.start();
        motor.start();

    }

}
