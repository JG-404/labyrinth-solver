import labirinto.ResolvedorLabirinto;

public class Main {
    public static void main(String[] args) {
        try{
            ResolvedorLabirinto labirinto = new ResolvedorLabirinto("teste1.txt");
            System.out.println(labirinto);
        }
        catch(Exception e)
        {}
    }
}
