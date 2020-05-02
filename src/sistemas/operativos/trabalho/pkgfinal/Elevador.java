package sistemas.operativos.trabalho.pkgfinal;

import java.util.Random;

public class Elevador {

    private boolean doorsclosed;
    protected int[] destinatedFloor;
    private int actualFloor;
    private int cargaMax;
    private int cargaAtual;
    private int npisos;
    private String estadoMotor;
    private Random randomFloor = new Random();
    private Random randomPeso = new Random();

    public Elevador() {
        this.doorsclosed = false;
        this.npisos = 4;
        this.cargaMax = 500;
        this.destinatedFloor = new int[npisos - 1];
        this.estadoMotor = "Parado";
        this.cargaAtual = randomPeso.nextInt(cargaMax + 100) + 50;
        this.actualFloor = randomFloor.nextInt(npisos) + 1;

        /* Preencher o array destinatedFloor com -1 */
        for (int p = 0; p < destinatedFloor.length; p++) {
            destinatedFloor[p] = -1;
        }
    }

    /* Método que redefine o destinatedFloor consoante o piso actual */
    public int[] defineOrder(Elevador elevador) {
        int j = 0;
        int k = destinatedFloor.length;
        int i;
        int order[] = new int[destinatedFloor.length];

        for (int x = 0; x < destinatedFloor.length; x++) {
            order[x] = -1;
        }

        for (i = 0; i < destinatedFloor.length; i++) {
            if (getActualFloor() == destinatedFloor[i]) {
                destinatedFloor[i] = -1;
                k--;
            } else {
                order[j] = destinatedFloor[i];
                j++;
            }
        }

        for (; k < destinatedFloor.length; k++) {
            order[k] = -1;
        }

        for (int h = 0; h < destinatedFloor.length; h++) {
            elevador.destinatedFloor[h] = order[h];
        }

        return elevador.destinatedFloor;
    }

    public void changeFloor(Elevador elevador) {
        elevador.setActualFloor(elevador.destinatedFloor[0]);
    }

    public void closeDoors() {
        setDoorsclosed(true);
    }

    public void openDoors() {
        setDoorsclosed(false);
    }

    public String getEstadoMotor() {
        return estadoMotor;
    }

    public void setEstadoMotor(String estadoMotor) {
        this.estadoMotor = estadoMotor;
    }

    public boolean getDoorsclosed() {
        return doorsclosed;
    }

    public void setDoorsclosed(boolean doorsclosed) {
        this.doorsclosed = doorsclosed;
    }

    public int getActualFloor() {
        return actualFloor;
    }

    public final void setActualFloor(int actualFloor) {
        this.actualFloor = actualFloor;
    }

    public int getCargaMax() {
        return cargaMax;
    }

    public void setCargaMax(int cargaMax) {
        this.cargaMax = cargaMax;
    }

    public int getNpisos() {
        return npisos;
    }

    public void setNpisos(int npisos) {
        this.npisos = npisos;
    }

    public int getCargaAtual() {
        return cargaAtual;
    }

    public void setCargaAtual(int cargaAtual) {
        this.cargaAtual = cargaAtual;
    }

}