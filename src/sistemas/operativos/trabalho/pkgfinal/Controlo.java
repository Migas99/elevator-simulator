package sistemas.operativos.trabalho.pkgfinal;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controlo implements Runnable {
    /* Semáforos inicializados com 0 recursos porque usámos
    um ciclo do{}while() em vez de um ciclo while() */

    protected boolean running = true;
    protected boolean STOP = false;
    protected boolean KEY = false;
    protected Semaphore semMotor = new Semaphore(0);
    protected Semaphore semPortas = new Semaphore(0);
    protected Semaphore semBotoneira = new Semaphore(0);
    private Elevador elevador;

    public Controlo(Elevador elevador) {
        this.elevador = elevador;
    }

    @Override
    public void run() {
        System.out.println("Mudando de piso ....");
        /* O elevador demora 2 segundos a mudar de piso */
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Motor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void esperar() throws InterruptedException {
        synchronized (elevador) {
            elevador.wait();
        }
    }

    public void notificarAll() {
        synchronized (elevador) {
            elevador.notifyAll();
        }
    }
}
