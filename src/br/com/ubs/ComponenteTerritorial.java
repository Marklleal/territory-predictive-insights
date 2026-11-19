// Classe abstrata com identificação, região, status, ativação e desativação.
package br.com.ubs;

public abstract class ComponenteTerritorial {
    private final String identificacao;
    private final DadosTerritorio dadosTerritorio;

    public ComponenteTerritorial(String identificacao, DadosTerritorio dadosTerritorio) {
        this.identificacao = identificacao;
        this.dadosTerritorio = dadosTerritorio;
    }

    public String getIdentificacao() {
        return identificacao;
    }

    public DadosTerritorio getDadosTerritorio() {
        return dadosTerritorio;
    }

    public abstract String verificarStatus();
}