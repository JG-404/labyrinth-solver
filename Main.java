import labirinto.ResolvedorLabirinto;
import teclado.Teclado;

public class Main {
    public static void main(String[] args) {
        while (true) {
            System.out.println();
            String caminho = Teclado.inserirTexto("Arquivo: ", "Caminho vazio");

            try{
                ResolvedorLabirinto labirinto = new ResolvedorLabirinto(caminho);
                labirinto.rodar();
            }
            catch(Exception e)
            {
                System.err.println(e.getMessage());
            }
        }
    }
}
