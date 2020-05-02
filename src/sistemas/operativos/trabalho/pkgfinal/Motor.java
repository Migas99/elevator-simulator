package sistemas.operativos.trabalho.pkgfinal;

import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Motor implements Runnable {

    private Elevador elevador;
    private Controlo control;

    public Motor(Elevador elevador, Controlo control) {
        this.elevador = elevador;
        this.control = control;
    }

    @Override
    public void run() {
        JFrame f = new JFrame(Thread.currentThread().getName());
        JLabel l = new JLabel();

        do {
            /* Caso o elevador não tenha um destino definido ou o destino for igual ao piso actual 
            o estado do motor será "Parado" */
            if (elevador.destinatedFloor[0] == -1 || elevador.destinatedFloor[0] == elevador.getActualFloor()) {
                elevador.setEstadoMotor("Parado");
                l.setText("Estado do motor: " + elevador.getEstadoMotor());
                System.out.println("Piso actual: " + elevador.getActualFloor());
            } else {
                /* Caso o valor do destino do elevador for maior que o do valor do piso actual
                o estado do motor será "Subir" */
                if (elevador.getActualFloor() < elevador.destinatedFloor[0]) {
                    elevador.setEstadoMotor("Subir");
                    l.setText("Estado do motor: " + elevador.getEstadoMotor());

                    /* Neste cliclo for() incrementa-se o piso actual gradualmente simulando o movimento do
                    elevador entre os pisos */
                    for (int i = elevador.getActualFloor() + 1; i <= elevador.destinatedFloor[0]; i++) {

                        Thread Controlo = new Thread(new Controlo(elevador));
                        Controlo.start();
                        try {
                            Controlo.join();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Motor.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        /* Actualiza-se o piso actual e dá se release para que
                        a botoneira actualize as suas informações */
                        elevador.setActualFloor(i);
                        control.semBotoneira.release();
                        System.out.println("Piso actual: " + elevador.getActualFloor());

                        /* Caso o botão STOP seja premido, o elevador irá parar o seu movimento
                        alterando assim o estado do motor para "Parado" e depois o método esperar
                        fará o elevador aguardar um notificar (aguarda até ser premido o botão START) 
                        para retornar ao movimento */
                        if (control.STOP == true) {
                            elevador.setEstadoMotor("Parado");
                            l.setText("Estado do motor: " + elevador.getEstadoMotor());

                            try {
                                control.esperar();
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Controlo.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            elevador.setEstadoMotor("Subir");
                            l.setText("Estado do motor: " + elevador.getEstadoMotor());
                        }
                    }
                } else {
                    /* Caso o valor do destino do elevador não for maior que o do valor do piso actual
                    o estado do motor será "Descer" */
                    elevador.setEstadoMotor("Descer");
                    l.setText("Estado do motor: " + elevador.getEstadoMotor());

                    /* Neste cliclo for() decrementa-se o piso actual gradualmente simulando o movimento do
                    elevador entre os pisos */
                    for (int i = elevador.getActualFloor() - 1; i >= elevador.destinatedFloor[0]; i--) {

                        Thread Controlo = new Thread(new Controlo(elevador));
                        Controlo.start();
                        try {
                            Controlo.join();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Motor.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        /* Actualiza-se o piso actual e dá se release para que
                        a botoneira actualize as suas informações */
                        elevador.setActualFloor(i);
                        control.semBotoneira.release();
                        System.out.println("Piso actual: " + elevador.getActualFloor());

                        /* Caso o botão STOP seja premido, o elevador irá parar o seu movimento
                        alterando assim o estado do motor para "Parado" e depois o método esperar
                        fará o elevador aguardar um notificar (aguarda até ser premido o botão START) 
                        para retornar ao movimento */
                        if (control.STOP == true) {
                            elevador.setEstadoMotor("Parado");
                            l.setText("Estado do motor: " + elevador.getEstadoMotor());

                            try {
                                control.esperar();
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Controlo.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            elevador.setEstadoMotor("Descer");
                            l.setText("Estado do motor: " + elevador.getEstadoMotor());
                        }
                    }
                }

                /* Quando o elevador chega ao seu destino, o motor fica "Parado" e este abre automaticamente as portas
                e é invocado o método defineOrder para definir o próximo destino
                É realizado um release para a botoneira e as portas para estes atualizarem as suas informações */
                elevador.openDoors();
                elevador.defineOrder(elevador);
                elevador.setEstadoMotor("Parado");
                l.setText("Estado do motor: " + elevador.getEstadoMotor());
                control.semPortas.release();
                control.semBotoneira.release();
            }
            l.setFont(new Font("Arial", Font.PLAIN, 40));
            f.add(l);
            f.setSize(500, 675);
            f.setLocation(100, 100);
            f.setVisible(true);

            /* Fica à espera que haja um release para actualizar as informações do estado do motor */
            try {
                control.semMotor.acquire();
            } catch (InterruptedException ex) {
                Logger.getLogger(Motor.class.getName()).log(Level.SEVERE, null, ex);
            }

        } while (control.running == true);
    }
}
