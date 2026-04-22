package labirinto;

public class Coordenada{
    private int x;
    private int y;

    public Coordenada(int y, int x) throws Exception{
        if (x < 0 || y < 0) throw new Exception("Coordenadas invalidas, numeros negativos não permitidos"); 
        this.y = y;
        this.x = x;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public String toString(){
        return "(" + this.x + "," + this.y + ")";
    }

    @Override
    public int hashCode(){
        int ret = 1;

        ret = ret * 3 + ((Integer)this.x).hashCode();
        ret = ret * 3 + ((Integer)this.y).hashCode();

        if (ret < 0) ret = -ret;

        return ret;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) return false;
        if (obj == this) return true;
        if (this.getClass() != obj.getClass()) return false;
        Coordenada outra = (Coordenada)obj;
        if (this.x != outra.x) return false;
        if (this.y != outra.y) return false;
        return true;
    }
}
