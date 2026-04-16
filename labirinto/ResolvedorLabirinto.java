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

    private Coordenada atual;

    public ResolvedorLabirinto(String path){
        try{
            List<String> arquivo = Files.readAllLines(Paths.get("./teste1.txt"), StandardCharsets.UTF_8);
                
            int x = Integer.parseInt(arquivo.get(0));
            int y = Integer.parseInt(arquivo.get(1));
            
            this.labirinto = new char[x][y];

            for(int i = 0; i < x; i++){
                for (int j = 0; j < y; j++){
                    this.labirinto[i][j] = arquivo.get(i + 2).charAt(j);
                }
            }

            this.caminho = new Pilha<>(x * y);
            this.possibilidades = new Pilha<>(x * y);
            this.fila = new Fila<>(3);
        }
        catch(Exception error){
            System.err.println(error.getMessage());
        }
    }
}