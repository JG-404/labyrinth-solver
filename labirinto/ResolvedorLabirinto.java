package labirinto;

import estruturas.Fila;
import estruturas.Pilha;

public class ResolvedorLabirinto{
    
    private Pilha<Coordenada> caminho;
    private Pilha<Fila<Coordenada>> possibilidades;
}