package estruturas;

public class Pilha<X> implements Cloneable{
    private int ultima = -1;
    private final int tamanhoOriginal;
    private X[] elementos;

    public Pilha(){
        this.tamanhoOriginal = 10;
        this.elementos = (X[]) new Object[tamanhoOriginal];
    }

    public Pilha(int tamanho) throws Exception{
        if (tamanho <= 0) throw new Exception("Tamanho invalido");

        this.tamanhoOriginal = tamanho;
        this.elementos = (X[]) new Object[tamanho];
    }

    private void redimensionar(){
        X[] novo;
        if (this.ultima == this.elementos.length-1) novo = (X[]) new Object[this.elementos.length*2];
        else novo = (X[]) new Object[this.elementos.length / 2];

        for (int i = 0; i <= this.ultima; i++){
            novo[i] = this.elementos[i];
        }

        this.elementos = novo;
    }

    public void guardeUmItem(X i) throws Exception{
        if (i == null) throw new Exception("Valor é null");
        
        if (this.ultima == this.elementos.length-1){
            this.redimensionar();
        }

        this.ultima++;
        if (i instanceof Cloneable) this.elementos[ultima] = (X) new Clonador<>().clone(i);
        else this.elementos[ultima] = i; 
    }

    public X getUmItem() throws Exception{
        if (this.ultima == -1) throw new Exception("Pilha vazia");

        if (this.elementos[ultima] instanceof Cloneable) return (X) new Clonador<>().clone(this.elementos[ultima]);
        return this.elementos[ultima];
    }

    public void removaUmItem() throws Exception{
        if (this.ultima == -1) throw new Exception("ta vazia");

        this.elementos[ultima] = null;
        this.ultima--;

        if (this.ultima <= this.elementos.length / 2 && this.elementos.length / 2 >= this.tamanhoOriginal){
            this.redimensionar();
        }
    }

    public boolean isCheia(){
        return this.ultima == this.elementos.length-1;
    }

    public boolean isVazia(){
        return this.ultima == -1;
    }

    @Override
    public String toString(){
        String texto = "";

        for (int i = 0; i < this.elementos.length; i++){
            if (this.elementos[i] != null) texto += this.elementos[i] + "\n";
        }

        return texto;
    }

    @Override
    public int hashCode(){
        int ret = 1;

        ret += ret * 3 + ((Integer)this.tamanhoOriginal).hashCode();
        ret += ret * 3 + ((Integer)this.ultima).hashCode();

        for (int i = 0; i <= this.ultima; i++){
            ret += ret * 3 + this.elementos[i].hashCode();
        }

        if (ret < 0) ret = -ret;
        return ret;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != this.getClass()) return false;
        Pilha<X> novo = (Pilha<X>) obj;
        if (this.tamanhoOriginal != novo.tamanhoOriginal) return false;
        if (this.ultima != novo.ultima) return false;
        for (int i = 0; i <= novo.ultima; i++){
            if (!this.elementos[i].equals(novo.elementos[i])) return false;
        }
        return true;
    }

    @Override
    public Object clone(){
        Pilha<X> ret = null;
        try{
            ret = new Pilha<>(this);
        }
        catch(Exception error)
        {}

        return ret;
    }

    public Pilha(Pilha<X> modelo) throws Exception{
        if (modelo == null) throw new Exception("é null");

        this.tamanhoOriginal = modelo.tamanhoOriginal;
        this.ultima = modelo.ultima;
        this.elementos = (X[]) new Object[modelo.elementos.length];

        for (int i = 0; i <= modelo.ultima; i++){
            this.elementos[i] = modelo.elementos[i];
        }
    }
}