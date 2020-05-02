package sistemas.operativos.trabalho.pkgfinal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Botoneira implements Runnable {

    private final String[] buttonNames = {"Abrir Portas", "Fechar Portas", "START", "KEY", "STOP", "Encerrar Programa"};
    private Random pesoRandom = new Random();
    private String[] Spisos;
    private int[] Ipisos;
    private int nbutton;
    private Elevador elevador;
    private Controlo control;
    private GuardarLogs logs;

    public Botoneira(Elevador elevador, Controlo control, GuardarLogs logs) {
        this.elevador = elevador;
        this.control = control;
        this.logs = logs;
        this.Spisos = new String[elevador.getNpisos()];
        this.Ipisos = new int[elevador.getNpisos()];
        this.nbutton = 1;
    }

    /* Método que reseta a cor do botão */
    public void resetButtonColor(JPanel panel) {
        for (int i = 0; i < panel.getComponentCount(); i++) {
            panel.getComponent(i).setForeground(Color.BLACK);
            panel.getComponent(i).repaint();
        }
    }

    @Override
    public void run() {
        JFrame f = new JFrame(Thread.currentThread().getName());
        JPanel panel = new JPanel();
        JLabel aFloor = new JLabel();
        JLabel dFloor = new JLabel();
        JLabel carga = new JLabel();

        /* Método usado para a criação dos botões 
        São usados dois arrays em que um armazena o formato de texto do botão
        e o outro o número do botão */
        for (int i = 0; i < elevador.getNpisos(); i++) {
            Spisos[i] = "Piso " + String.valueOf(nbutton);
            Ipisos[i] = nbutton;
            nbutton++;
        }

        /* Criação de um número de botões consoante o número de pisos do edíficio */
        for (String array : this.Spisos) {
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(200, 75));
            panel.add(b);
            b.setText(array);
            b.addActionListener((ActionEvent e) -> {
                for (int i = 0; i < Spisos.length; i++) {
                    if (Spisos[i].equals(b.getText())) {
                        int contador = 0;
                        /* O contador é utilizado para prevenir vários cliques num mesmo botão
                        fazendo com que este não seja armazenado mais do que uma vez no array destinatedFloor */
                        for (int y = 0; y < elevador.destinatedFloor.length; y++) {
                            if (elevador.destinatedFloor[y] == Ipisos[i]) {
                                contador++;
                            }
                        }
                        if (contador == 0) {
                            if (elevador.getActualFloor() != Ipisos[i]) {
                                for (int z = 0; z < elevador.destinatedFloor.length; z++) {
                                    if (elevador.destinatedFloor[z] == -1) {
                                        elevador.destinatedFloor[z] = Ipisos[i];
                                        break;
                                    }
                                }
                            }
                        }
                        /* Faz se um release para que na botoneira seja atualizado o destino do elevador */
                        control.semBotoneira.release();
                    }
                }
            });
        }

        /* Criação de outros botões */
        for (String array : this.buttonNames) {
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(200, 75));
            panel.add(b);
            b.setText(array);
            b.addActionListener((ActionEvent e) -> {
                if ("Abrir Portas".equals(b.getText())) {
                    if (elevador.getEstadoMotor() == "Parado" && control.KEY == false) {
                        /* Abre as portas e faz release para que a Thread responsábel
                        pelas portas, actualize o seu estado */
                        elevador.openDoors();
                        control.semPortas.release();
                    }
                }
                if ("Fechar Portas".equals(b.getText())) {
                    if (elevador.getEstadoMotor() == "Parado" && control.KEY == false) {
                        /* Fecha as portas e faz release para que a Thread responsábel
                        pelas portas, actualize o seu estado */
                        elevador.closeDoors();
                        control.semPortas.release();
                    }
                }
                if ("START".equals(b.getText())) {
                    /* O peso actual do elevador só pode ser alterado caso as portas
                    estejam abertas, permitindo a entrada e saída de utilizadores */
                    if (elevador.getDoorsclosed() == false) {
                        elevador.setCargaAtual(pesoRandom.nextInt(elevador.getCargaMax() - 50) + 50);
                    }
                    if (elevador.destinatedFloor[0] != -1 && this.control.KEY == false) {
                        if (elevador.getCargaAtual() > elevador.getCargaMax()) {
                            b.setForeground(Color.RED);
                            carga.setText("\nCarga Atual:" + elevador.getCargaAtual() + ">" + "Máx:" + elevador.getCargaMax());
                        } else {
                            carga.setText("\nCarga Atual:" + elevador.getCargaAtual() + "<=" + "Máx:" + elevador.getCargaMax());

                            /* Release utilizado para caso a carga actual seja maior que a carga máxima,
                            quando o START for clicado novamente, este actualize a sua cor seja actualizada
                            mal o elevador */
                            control.semBotoneira.release();
                            try {
                                /* Regista o piso actual do elevador e o destino */
                                logs.guardar();
                            } catch (IOException ex) {
                                Logger.getLogger(Botoneira.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (control.STOP == false) {
                                /* Fecha as portas e actualiza o estado do motor e das portas */
                                elevador.closeDoors();
                                control.semPortas.release();
                                control.semMotor.release();
                            } else {
                                if (elevador.getDoorsclosed() == false) {
                                    /* Redefine a ordem do array destinatedFloor */
                                    elevador.defineOrder(elevador);
                                    elevador.closeDoors();
                                    /* Release utilizador para actualizar o estado das portas */
                                    control.semPortas.release();
                                }
                                control.STOP = false;
                                /* Método que notifica o wait no motor para este retornar ao movimento */
                                control.notificarAll();
                            }
                        }
                    }
                }
                if ("KEY".equals(b.getText())) {
                    if (this.control.KEY == false) {
                        this.control.KEY = true;
                    } else {
                        this.control.KEY = false;
                    }
                }
                if ("STOP".equals(b.getText())) {
                    if (elevador.getEstadoMotor() != "Parado") {
                        control.STOP = true;
                    }
                }
                if ("Encerrar Programa".equals(b.getText())) {
                    f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
                    System.exit(0);
                }
            });
        }

        do {
            aFloor.setText("Andar Atual: " + elevador.getActualFloor() + " ||");
            if (elevador.destinatedFloor[0] == -1) {
                dFloor.setText("Destino: Não definido ||");
            } else {
                dFloor.setText("Destino: " + elevador.destinatedFloor[0] + " ||");
            }

            panel.add(aFloor);
            panel.add(dFloor);
            panel.add(carga);
            f.add(panel);
            f.setSize(500, 675);
            f.setLocation(700, 100);
            f.setVisible(true);
            resetButtonColor(panel);

            /* Fica à espera que haja um release para actualizar as informações na botoneira */
            try {
                control.semBotoneira.acquire();
            } catch (InterruptedException ex) {
                Logger.getLogger(Botoneira.class.getName()).log(Level.SEVERE, null, ex);
            }

        } while (control.running == true);
    }
}
