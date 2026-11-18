// Encapsula e valida região, população e limiares de alerta protegidos por código de acesso.
package br.com.ubs;

public class DadosTerritorio {
    private int limiteFila = 15;
    private String codigo = "segredo";

    public int getLimiteFila() {
        return this.limiteFila;
    }
    
    public boolean setAlterarLimiteFila(int novoLimite, String codigoInformado) {
        if (novoLimite > 0 && codigoInformado.equals(this.codigo)) {
            this.limiteFila = novoLimite;
            return true;
        } else {
            return false;
        }
    }
}