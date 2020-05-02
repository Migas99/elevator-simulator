package sistemas.operativos.trabalho.pkgfinal;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GuardarLogs {

    private Elevador elevador;

    public GuardarLogs(Elevador elevador) {
        this.elevador = elevador;
    }

    /* Este método armazenará no ficheiro Logs o piso actual e o destino actual do elevador
    de quando este é chamado */
    public void guardar() throws IOException {
        FileWriter arq = new FileWriter("src\\sistemas\\operativos\\trabalho\\pkgfinal\\Logs.txt", true);
        PrintWriter gravarArq = new PrintWriter(arq);
        gravarArq.println("Piso actual: " + elevador.getActualFloor());
        gravarArq.println("Destino: " + elevador.destinatedFloor[0] + "\n");
        arq.close();
    }
}