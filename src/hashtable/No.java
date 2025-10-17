package hashtable;

import model.Registro;

public class No {
    private Registro registro;
    private No proximo;
    
    public No(Registro registro) {
        this.registro = registro;
        this.proximo = null;
    }
    
    public Registro getRegistro() {
        return registro;
    }
    
    public No getProximo() {
        return proximo;
    }
    
    public void setProximo(No proximo) {
        this.proximo = proximo;
    }
}