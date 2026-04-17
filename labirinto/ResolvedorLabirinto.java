package labirinto;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import estruturas.Fila;
import estruturas.Pilha;

public class ResolvedorLabirinto{

    private Pilha<Coordenada> caminho;
    private Pilha<Fila<Coordenada>> possibilidades;
    private Fila<Coordenada> fila;

    private char[][] labirinto;

    private Coordenada atual = null;

    public ResolvedorLabirinto(String path) throws Exception{
        if (path == null || path.isBlank()) throw new Exception("Caminho vazio");

        try{
            List<String> arquivo = Files.readAllLines(Paths.get("./caminhos/" + path), StandardCharsets.UTF_8);
            
            int x = Integer.parseInt(arquivo.get(0));
            int y = Integer.parseInt(arquivo.get(1));

            if (x < 3 || y < 3) throw new Exception("Labirinto invalido");
            
            this.labirinto = new char[x][y];

            for(int i = 0; i < x; i++){
                for (int j = 0; j < y; j++){
                    this.labirinto[i][j] = arquivo.get(i + 2).charAt(j);
                }
            }

            for (int i = 0; i < this.labirinto.length; i++){
                for (int j = 0; j < this.labirinto[i].length; j++){
                    if ((i == 0 || i == this.labirinto.length-1 || j == 0 || j == this.labirinto[i].length-1) && this.labirinto[i][j] == 'E') atual = new Coordenada(i, j);
                }
            }

            if (atual == null) throw new Exception("Labirinto invalido, entra não encontrada");

            this.caminho = new Pilha<>(x * y);
            this.possibilidades = new Pilha<>(x * y);
            this.fila = new Fila<>(3);
        }
        catch(Exception error){
            System.out.println("Labirinto invalido" + "\n" + "Mensagem de erro: " + error.getMessage());
        }
    }

    private void progressivo() throws Exception{
        if (atual.getX() > 0){
            if (this.labirinto[atual.getX()-1][atual.getY()] == ' ' || this.labirinto[atual.getX()-1][atual.getY()] == 'S') fila.guardeUmItem(new Coordenada(atual.getX() - 1, atual.getY())); 
        }
        if (atual.getX() < this.labirinto.length-1){
            if (this.labirinto[atual.getX()+1][atual.getY()] == ' ' || this.labirinto[atual.getX()+1][atual.getY()] == 'S') fila.guardeUmItem(new Coordenada(atual.getX() + 1, atual.getY()));
        }   
    }

    private void recessivo(){

    }

    @Override
    public String toString(){
        String texto = "";

        for (int i = 0; i < this.labirinto.length; i++) {
            for (int j = 0; j < this.labirinto[i].length; j++) {
                texto += this.labirinto[i][j];
            }
            texto += "\n";
        }

        return texto;
    }
}