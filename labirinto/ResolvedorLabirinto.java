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
                    if ((i == 0 || i == this.labirinto.length-1 || j == 0 || j == this.labirinto[i].length-1) && this.labirinto[i][j] == 'E') this.atual = new Coordenada(i, j);
                }
            }

            if (this.atual == null) throw new Exception("Labirinto invalido, entra não encontrada");

            this.caminho = new Pilha<>(x * y);
            this.possibilidades = new Pilha<>(x * y);
            this.fila = new Fila<>(3);

            while (true){
                if (this.labirinto[this.atual.getX()][this.atual.getY()] == 'S') break;
                this.possibilidades();
                while (!this.fila.isVazia()){
                    this.progressivo();
                    this.possibilidades();
                }
                while (!this.possibilidades.getUmItem().isVazia()){
                    this.recessivo();
                }
                this.atual = this.possibilidades.getUmItem().getUmItem();
                this.possibilidades();
            }
        }
        catch(Exception error){
            System.out.println("Labirinto invalido" + "\n" + "Mensagem de erro: " + error.getMessage());
        }
    }

    private void possibilidades() throws Exception {
        int x = this.atual.getX();
        int y = this.atual.getY();

        if (x > 0){
            if (this.labirinto[x-1][y] == ' ' || this.labirinto[x-1][y] == 'S') fila.guardeUmItem(new Coordenada(x - 1, y)); 
        }
        if (x < this.labirinto.length-1){
            if (this.labirinto[x+1][y] == ' ' || this.labirinto[x+1][y] == 'S') fila.guardeUmItem(new Coordenada(x + 1, y));
        }
        if (y > 0){
            if (this.labirinto[x][y-1] == ' ' || this.labirinto[x][y-1] == 'S') fila.guardeUmItem(new Coordenada(x, y - 1)); 
        }
        if (y < this.labirinto[x].length-1){
            if (this.labirinto[x][y+1] == ' ' || this.labirinto[x][y+1] == 'S') fila.guardeUmItem(new Coordenada(x, y + 1)); 
        }
    }

    private void progressivo() throws Exception{
        this.atual = this.fila.getUmItem();
        this.fila.removaUmItem();

        if (this.labirinto[this.atual.getX()][this.atual.getY()] == 'S') return;

        this.labirinto[this.atual.getX()][this.atual.getY()] = '*';
        this.caminho.guardeUmItem(this.atual);
        this.possibilidades.guardeUmItem(new Fila<>(this.fila));
        this.fila = new Fila<>(3);
    }

    private void recessivo() throws Exception{
        this.labirinto[this.atual.getX()][this.atual.getY()] = ' ';
        this.caminho.removaUmItem();

        if (caminho.isVazia()) throw new Exception("Sem solução");

        this.atual = this.caminho.getUmItem();
        this.fila = this.possibilidades.getUmItem();
        this.possibilidades.removaUmItem();
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