package estruturas;
public class Fila<X> implements Cloneable{
    private X[] elementos;
    private final int tamanhoOriginal;
    private int inicio = 0;
    private int ultimo = 0;
    private int qtd = 0;

    public Fila(){
        this.elementos = (X[]) new Object[10];
        this.tamanhoOriginal = 10;
    }

    public Fila(int tamanho) throws Exception{
        if (tamanho <= 0) throw new Exception("Tamanho invalido");

        this.elementos = (X[]) new Object[tamanho];
        this.tamanhoOriginal = tamanho;
    }

    private void redimensionar(int novoTamanho) {
        X[] novo = (X[]) new Object[novoTamanho];

        /*
        int indice = 0;
        for (int i = this.inicio; i != this.ultimo; i++, indice++){
            novo[indice] = this.elementos[i];

            if (this.inicio == this.elementos.length-1) i = -1;
        }
        */

        for (int i = 0, atual = this.inicio; i < this.qtd; i++, atual=atual==this.elementos.length-1?0:atual+1){
            novo[i] = this.elementos[atual];
        }

        this.ultimo = this.qtd;
        this.inicio = 0;
        this.elementos = novo;
    }

    public void guardeUmItem(X item) throws Exception{
        if (item == null) throw new Exception("Item está null");

        if (this.qtd == this.elementos.length) this.redimensionar(2 * this.elementos.length);
        if (this.ultimo >= this.elementos.length) this.ultimo = 0;

        if (item instanceof Cloneable) this.elementos[ultimo] = (X) new Clonador<>().clone(item);
        else this.elementos[ultimo] = item;

        this.ultimo++;
        this.qtd++;
    }

    public void removaUmItem() throws Exception{
        if (this.qtd == 0) throw new Exception("fila vazia, não da pra remover nada");

        if (this.qtd <= (this.elementos.length / 2) && (this.elementos.length / 2) >= this.tamanhoOriginal) redimensionar(this.elementos.length / 2);

        this.elementos[this.inicio] = null;

        this.inicio++;
        if (this.inicio == this.elementos.length) this.inicio = 0;

        this.qtd--;
    }

    public X getUmItem() throws Exception{
        if (this.qtd == 0) throw new Exception("não tem oq resgatar");

        if (this.elementos[this.inicio] instanceof Cloneable) return (X) new Clonador<>().clone(this.elementos[this.inicio]);
        return this.elementos[this.inicio];
    }

    public boolean isCheia(){
        return this.qtd == this.elementos.length;
    }

    public boolean isVazia(){
        return this.qtd == 0;
    }

    @Override
    public String toString(){
        String texto = "";
        for (int i = 0; i < this.elementos.length; i++){
            texto += this.elementos[i] + "\n";
        }

        texto += "\n";

        texto += "Inicio da fila: " + this.inicio + "\n" +
                 "Final da fila: " + this.ultimo + "\n" +
                 "Quantidade: " + this.qtd;

        return texto;
    }

    @Override
    public int hashCode(){
        int ret = 1;

        ret = ret * 3 + ((Integer)this.qtd).hashCode();

        for(int i = this.inicio; i < this.qtd; i=i==this.elementos.length-1?0:i + 1){
            ret = ret * 3 + this.elementos[i].hashCode();
        }

        if (ret < 0) ret = -ret;
        return ret;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != this.getClass()) return false;
        Fila<X> outro = (Fila<X>) obj;
        if (outro.qtd != this.qtd) return false;

        //Aqui vem um for derramatico
        for (int i = 0, atualThis = this.inicio, atualOutro = outro.inicio;

            i < this.qtd;

            i++,
            atualThis = atualThis==this.elementos.length-1?0:atualThis+1,
            atualOutro = atualOutro==outro.elementos.length-1?0:atualOutro+1){

            if (!this.elementos[atualThis].equals(outro.elementos[atualOutro])) return false;
        }

        return true;
    }

    public Fila(Fila<X> modelo) throws Exception{
        if (modelo == null) throw new Exception("Modelo vazio");

        this.elementos = (X[]) new Object[modelo.elementos.length];
        this.tamanhoOriginal = modelo.tamanhoOriginal;
        this.ultimo = modelo.ultimo;
        this.inicio = modelo.inicio;
        this.qtd = modelo.qtd;

        for (int i = 0; i < this.elementos.length; i++){
            this.elementos[i] = modelo.elementos[i];
        }
    }

    @Override
    public Object clone(){
        Fila<X> ret = null;
        try{
            ret = new Fila<X>(this);
        }
        catch(Exception error)
        {}

        return ret;
    }
}