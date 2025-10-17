package model;

public class Registro {
    private String codigo;
    
    public Registro(String codigo) {
        // Garante que o código sempre tenha 9 dígitos com zeros à esquerda
        this.codigo = String.format("%09d", Long.parseLong(codigo));
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Registro registro = (Registro) obj;
        return codigo.equals(registro.codigo);
    }
    
    @Override
    public int hashCode() {
        return codigo.hashCode();
    }
    
    @Override
    public String toString() {
        return codigo;
    }
}