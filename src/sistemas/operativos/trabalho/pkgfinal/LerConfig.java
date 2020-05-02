package sistemas.operativos.trabalho.pkgfinal;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Integer.parseInt;

public class LerConfig {

    private Elevador elevador;
    private String currentLine;

    public LerConfig(Elevador elevador) {
        this.elevador = elevador;
    }

    /* O que estiver na linha 2 no ficheiro Config é a carga máxima do elevador
    e o que estiver na linha 4 é o número de pisos do edifício */
    public void ler() throws FileNotFoundException, IOException {
        int i = 1;

        FileReader arq = new FileReader("src\\sistemas\\operativos\\trabalho\\pkgfinal\\Config.txt");
        BufferedReader lerArq = new BufferedReader(arq);
        
        while ((currentLine = lerArq.readLine()) != null) {
            if (i == 2) {
                elevador.setCargaMax(parseInt(currentLine));
            }
            if (i == 4) {
                elevador.setNpisos(parseInt(currentLine));
            }
            i++;
        }
    }
}