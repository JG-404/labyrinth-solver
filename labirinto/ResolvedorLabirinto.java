package labirinto;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import estruturas.Fila;
import estruturas.Pilha;

public class ResolvedorLabirinto implements Cloneable{
    private Pilha<Coordenada> caminho;
    private Pilha<Fila<Coordenada>> possibilidades;
    private Fila<Coordenada> fila;
    private Pilha<Coordenada> inverso;

    private int largura;
    private int altura;

    private char[][] labirinto;

    private Coordenada atual;

    private boolean completo = false;

    public ResolvedorLabirinto(String path) throws Exception{

        if (!Files.exists(Paths.get("./caminhos/" + path))) throw new Exception("Arquivo não encontrado, verifique se você digitou corretamente e se ele está na pasta caminhos");

        List<String> arquivo = Files.readAllLines(Paths.get("./caminhos/" + path), StandardCharsets.UTF_8);

        try{
            this.altura = Integer.parseInt(arquivo.get(0));
            this.largura = Integer.parseInt(arquivo.get(1));
        }
        catch(Exception e){
            throw new Exception("Dimensões invalidas");
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

        this.saidaOuEntradaNoCaminho();
        if (!this.acharSaida()) throw new Exception("Saída não econtrada");
        if (this.labirintoAberto()) throw new Exception("As paredes do labirinto estão abertas");
        this.atual = this.acharEntrada();

        this.caminho = new Pilha<>(this.altura * this.largura);
        this.possibilidades = new Pilha<>(this.altura * this.largura);

        this.inverso = new Pilha<>(this.altura * this.largura);
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

    public void rodar() throws Exception{

        if (this.completo) throw new Exception("Labirinto já solucionado");

        while (this.labirinto[this.atual.getY()][this.atual.getX()] != 'S') {

            this.andar();

        }
        System.out.println(this);

        this.completo = true;

        this.mostrarCaminho();
    }

    private Coordenada acharEntrada() throws Exception{
        Coordenada coordenadaEncontrada = null;

        for (int i = 0; i < this.labirinto.length; i++) {
            for (int j = 0; j < this.labirinto[i].length; j++) {
                if ((i == 0 || i == this.labirinto.length - 1 || j == 0 || j == this.labirinto[i].length - 1) && this.labirinto[i][j] == 'E'){ 
                    if (coordenadaEncontrada == null) coordenadaEncontrada = new Coordenada(i, j);
                    else throw new Exception("Mais de uma entrada encontrada, só pode ter uma");
                }
            }
        }

        if (coordenadaEncontrada == null) throw new Exception("Entrada não encontrada");

        return coordenadaEncontrada;        
    }

    private boolean acharSaida() throws Exception{
        boolean encontrou = false;

        for (int i = 0; i < this.labirinto.length; i++) {
            for (int j = 0; j < this.labirinto[i].length; j++) {
                if ((i == 0 || i == this.labirinto.length - 1 || j == 0 || j == this.labirinto[i].length - 1) && this.labirinto[i][j] == 'S'){ 
                    if (encontrou == false) encontrou = true;
                    else throw new Exception("Mais de uma saida encontrada, só pode ter uma");
                }
            }
        }

        return encontrou;
    }

    private boolean labirintoAberto(){
        boolean estaAberto = false;

        for (int i = 0; i < this.labirinto.length; i++) {
            for (int j = 0; j < this.labirinto[i].length; j++) {
                if ((i == 0 || i == this.labirinto.length - 1 || j == 0 || j == this.labirinto[i].length - 1) && this.labirinto[i][j] != 'S' && this.labirinto[i][j] != 'E' && this.labirinto[i][j] != '#'){ 
                    estaAberto = true;
                }
            }
        }

        return estaAberto;
    }

    private void saidaOuEntradaNoCaminho() throws Exception{
        for (int i = 1; i < this.labirinto.length-1; i++) {
            for (int j = 1; j < this.labirinto[i].length-1; j++) {
                if(this.labirinto[i][j] == 'E') throw new Exception("A entrada tem que estar nas bordas do labirinto");
                if(this.labirinto[i][j] == 'S') throw new Exception("A saida tem que estar nas bordas do labirinto");
            }
        }
    }

    private void mostrarCaminho() throws Exception{

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
        ret = ret * 3 + ((Boolean)this.completo).hashCode();

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
        if (this.completo != outro.completo) return false;
        for (int i = 0; i < this.labirinto.length; i++) {
            for (int j = 0; j < this.labirinto[i].length; j++) {
                if (this.labirinto[i][j] != outro.labirinto[i][j]) return false;
            }
        }

        return true;
    }

    public ResolvedorLabirinto(ResolvedorLabirinto modelo) throws Exception{
        if (modelo == null) throw new Exception("Modelo para copia vazio");

        this.caminho = (Pilha<Coordenada>)modelo.caminho.clone();
        this.possibilidades = (Pilha<Fila<Coordenada>>)modelo.possibilidades.clone();
        this.fila = (modelo.fila != null) ? (Fila<Coordenada>)modelo.fila.clone() : null; 
        this.inverso = (Pilha<Coordenada>)modelo.inverso.clone();
        this.atual = modelo.atual;
        this.largura = modelo.largura;
        this.altura = modelo.altura;
        this.completo = modelo.completo;

        this.labirinto = new char[this.altura][this.largura];

        for (int i = 0; i < this.labirinto.length; i++){
            for (int j = 0; j < this.labirinto[i].length; j++){
                this.labirinto[i][j] = modelo.labirinto[i][j];
            }
        }
    }

    @Override
    public Object clone(){
        ResolvedorLabirinto ret = null;
        
        try{
            ret = new ResolvedorLabirinto(this);
        }
        catch(Exception e)
        {}

        return ret;
    }
}