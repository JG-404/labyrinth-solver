package labirinto;

public class Coordenada implements Cloneable{
    private int x;
    private int y;

    public Coordenada(int x, int y) throws Exception{
        if (x < 0 || y < 0) throw new Exception("Coordenadas invalidas, numeros negativos não permitidos"); 
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString(){
        return "(" + this.x + "," + this.y + ")";
    }
}
