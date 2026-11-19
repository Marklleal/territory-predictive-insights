// Encapsula e valida região, população e limiares de alerta protegidos por código de acesso.
package br.com.ubs;

public class DadosTerritorio {
    private static final String CODIGO_ACESSO = "segredo";
    private final String regiao;
    private final int populacao;
    private int limiteFila = 18;
    private int limiteEstoque = 20;
    private int limiteAtendimentos = 12;
    
    public DadosTerritorio(String regiao, int populacao) {
        this.regiao = regiao;
        this.populacao = populacao;
    }
    
    public int getLimiteFila() {
        return this.limiteFila;
    }

    public int getPopulacao() {
        return this.populacao;
    }

    public String getRegiao() {
        return this.regiao;
    }

    public int getLimiteEstoque() {
        return this.limiteEstoque;
    }

    public int getLimiteAtendimentos() {
        return this.limiteAtendimentos;
    }

    // Permite alterar limite da fila com valores maiores que zero e com codigo
    public boolean setAlterarLimiteFila(int novoLimite, String codigoInformado) {
        if (novoLimite <= 0 || !CODIGO_ACESSO.equals(codigoInformado)) {
            return true;
        } 
    
        this.limiteFila = novoLimite;
        return false;
    }
}