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
    private Pilha<Coordenada> inverso;

    private int largura;
    private int altura;

    private char[][] labirinto;

    private Coordenada atual = null;

    public ResolvedorLabirinto(String path) throws Exception{

        if (!Files.exists(Paths.get("./caminhos/" + path))) throw new Exception("Arquivo não encontrado, verifique se você digitou corretamente e se ele está na pasta caminhos");

        List<String> arquivo = Files.readAllLines(Paths.get("./caminhos/" + path), StandardCharsets.UTF_8);

        try{
            this.altura = Integer.parseInt(arquivo.get(0));
            this.largura = Integer.parseInt(arquivo.get(1));
        }
        catch(Exception e){
            throw new Exception("Dimensoões invalidas");
        }

        if (this.altura < 3 || this.largura < 3)
            throw new Exception("Labirinto invalido");
        if (arquivo.size() - 2 != this.altura)
            throw new Exception("As dimensões do labirinto não bate com as dimensões informadas");
        for (int i = 0; i < this.altura; i++) {
            if (arquivo.get(i + 2).length() != this.largura)
                throw new Exception("As dimensões do labirinto não bate com as dimensões informadas");
        }

        this.labirinto = new char[this.altura][this.largura];

        for (int i = 0; i < this.altura; i++) {
            for (int j = 0; j < this.largura; j++) {
                this.labirinto[i][j] = arquivo.get(i + 2).charAt(j);
            }
        }

        for (int i = 0; i < this.labirinto.length; i++) {
            for (int j = 0; j < this.labirinto[i].length; j++) {
                if ((i == 0 || i == this.labirinto.length - 1 || j == 0 || j == this.labirinto[i].length - 1)
                        && this.labirinto[i][j] == 'E')
                    this.atual = new Coordenada(i, j);
            }
        }

        if (this.atual == null)
            throw new Exception("Entrada não encontrada");

        this.caminho = new Pilha<>(this.altura * this.largura);
        this.possibilidades = new Pilha<>(this.altura * this.largura);

        while (this.labirinto[this.atual.getY()][this.atual.getX()] != 'S') {

            andar();

        }
        System.out.println(this);

        this.inverso = new Pilha<>(this.altura * this.largura);

        while (!this.caminho.isVazia()) {
            this.inverso.guardeUmItem(this.caminho.getUmItem());
            this.caminho.removaUmItem();
        }

        System.out.println("Caminho para solucionar o labirinto a seguir:");

        while (!this.inverso.isVazia()) {
            System.out.print(this.inverso.getUmItem() + " ");
            this.inverso.removaUmItem();
        }
    }

    private Fila<Coordenada> caminhosPossiveis() throws Exception {
        int y = this.atual.getY();
        int x = this.atual.getX();

        Fila<Coordenada> fila = new Fila<>(3);

        if (y > 0){
            if (this.labirinto[y-1][x] == ' ' || this.labirinto[y-1][x] == 'S') fila.guardeUmItem(new Coordenada(y - 1, x)); 
        }
        if (y < this.labirinto.length-1){
            if (this.labirinto[y+1][x] == ' ' || this.labirinto[y+1][x] == 'S') fila.guardeUmItem(new Coordenada(y + 1, x));
        }
        if (x > 0){
            if (this.labirinto[y][x-1] == ' ' || this.labirinto[y][x-1] == 'S') fila.guardeUmItem(new Coordenada(y, x - 1)); 
        }
        if (x < this.labirinto[y].length-1){
            if (this.labirinto[y][x+1] == ' ' || this.labirinto[y][x+1] == 'S') fila.guardeUmItem(new Coordenada(y, x + 1)); 
        }

        return fila;
    }


    private void andar() throws Exception{
        this.fila = caminhosPossiveis();

        if (this.fila.isVazia() && this.possibilidades.isVazia() && this.caminho.isVazia()) throw new Exception("Labirinto sem saida");

        while (this.fila.isVazia()){
            this.fila = this.possibilidades.getUmItem();
            this.recessivo();
        }

        this.progressivo();
    }

    private void progressivo() throws Exception{
        this.atual = this.fila.getUmItem();
        this.fila.removaUmItem();

        if (this.labirinto[this.atual.getY()][this.atual.getX()] == 'S') return;

        this.labirinto[this.atual.getY()][this.atual.getX()] = '*';

        this.caminho.guardeUmItem(this.atual);
        this.possibilidades.guardeUmItem(fila);
    }

    private void recessivo() throws Exception{

        this.atual = this.caminho.getUmItem();
        this.caminho.removaUmItem();

        this.labirinto[this.atual.getY()][this.atual.getX()] = ' ';
        this.possibilidades.removaUmItem();

        if (this.possibilidades.isVazia() && this.caminho.isVazia()) throw new Exception("Labirinto sem saida");

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

    @Override
    public int hashCode() {
        int ret = 1;

        ret = ret * 3 + ((Integer)this.largura).hashCode();
        ret = ret * 3 + ((Integer)this.altura).hashCode();

        for (int i = 0; i < this.labirinto.length; i++) {
            for (int j = 0; j < this.labirinto[i].length; j++) {
                ret = ret * 3 + ((Character)this.labirinto[i][j]).hashCode();
            }
        }

        if (ret < 0) ret = -ret;

        return ret;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) return false;
        if (obj == this) return true;
        if (this.getClass() != obj.getClass()) return false;
        ResolvedorLabirinto outro = (ResolvedorLabirinto)obj;
        if (this.altura != outro.altura) return false;
        if (this.largura != outro.largura) return false;
        for (int i = 0; i < this.labirinto.length; i++) {
            for (int j = 0; j < this.labirinto[i].length; j++) {
                if (this.labirinto[i][j] != outro.labirinto[i][j]) return false;
            }
        }

        return true;
    }
}