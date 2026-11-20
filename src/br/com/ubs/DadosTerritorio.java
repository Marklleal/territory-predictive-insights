// Encapsula e valida região, população e limiares de alerta protegidos por código de acesso.
package br.com.ubs;

import java.util.EnumMap;
import java.util.Map;

public class DadosTerritorio {
    private static final String CODIGO_ACESSO = "segredo";
        
    // Tipos de dados monitorados; no estoque, valores baixos geram alertas.
    public enum Indicador {
        FILA("Fila", false),
        ATENDIMENTOS("Atendimentos", false),
        ESTOQUE("Estoque", true);

        private final String nome;
        private final boolean alertaQaundoBaixo;

        Indicador(String nome, boolean alertaQaundoBaixo) {
            this.nome = nome;
            this.alertaQaundoBaixo = alertaQaundoBaixo;
        }

        public String getNome() {
            return nome;
        }

        public boolean isAlertaQuandoBaixo() {
            return alertaQaundoBaixo;
        }
    }

    // Graus de gravidade usados para classificar os alertas.
    public enum Nivel {
        ATENCAO, ALERTA, CRITICO
    }
    
    private final String regiao;
    private final int populacao;
    // Guarda os valores de atenção, alerta e crítico de cada indicador.
    private final Map<Indicador, Map<Nivel, Integer>> limiares = new EnumMap<>(Indicador.class);

    public DadosTerritorio(String regiao, int populacao) {
        this.regiao = regiao;
        this.populacao = populacao;

        // Define os limites iniciais usados nas previsões.
        limiares.put(Indicador.FILA, criarLimiares(15,18,25));
        limiares.put(Indicador.ATENDIMENTOS, criarLimiares(10,12,18));
        limiares.put(Indicador.ESTOQUE, criarLimiares(20,10,5));
    }

    // Organiza os três limites de um indicador por nível de gravidade.
    private static Map<Nivel, Integer> criarLimiares(int atencao, int alerta, int critico) {
        Map<Nivel, Integer> valores = new EnumMap<>(Nivel.class);
        valores.put(Nivel.ATENCAO, atencao);
        valores.put(Nivel.ALERTA, alerta);
        valores.put(Nivel.CRITICO, critico);
        return valores;
    }

    public int getPopulacao() {
        return this.populacao;
    }

    public String getRegiao() {
        return this.regiao;
    }

    // Consulta o limite de um indicador em um nível específico.
    public int getLimiar(Indicador indicador, Nivel nivel) {
        return limiares.get(indicador).get(nivel);
    }

    public boolean alterarLimiar(Indicador indicador, Nivel nivel, int novoValor, String codigoInformado) {
        // Recusa dados inválidos ou alterações sem o código correto.
        if (indicador == null || nivel == null || novoValor <= 0 || !CODIGO_ACESSO.equals(codigoInformado)) {
            return false;
        }

        // Testa a mudança em uma cópia para preservar os valores atuais se ela for inválida.
        Map<Nivel, Integer> atuais = limiares.get(indicador); 
        Map<Nivel, Integer> propostos = new EnumMap<>(atuais);
        propostos.put(nivel, novoValor);


        int atencao = propostos.get(Nivel.ATENCAO);
        int alerta = propostos.get(Nivel.ALERTA);
        int critico = propostos.get(Nivel.CRITICO);

        // No estoque, quanto menor o valor, mais grave; nos demais, é o contrário.
        boolean ordemValida = indicador.isAlertaQuandoBaixo()
            ? critico < alerta && alerta < atencao 
            : atencao < alerta && alerta < critico;

        if (!ordemValida) {
            return false;
        }

        // Salva a alteração somente depois de validar a ordem dos limites.
        limiares.put(indicador, propostos);
        return true;
    }
    
    public int getLimiteFila() {
        return getLimiar(Indicador.FILA, Nivel.ALERTA);
    }

    public int getLimiteAtendimentos() {
        return getLimiar(Indicador.ATENDIMENTOS, Nivel.ALERTA);
    }

    public int getLimiteEstoque() {
        return getLimiar(Indicador.ESTOQUE, Nivel.ATENCAO);
    }

    // Permite alterar limite da fila com valores maiores que zero e com codigo
    public boolean setAlterarLimiteFila(int novoLimite, String codigoInformado) {
        return alterarLimiar(Indicador.FILA, Nivel.ALERTA, novoLimite, codigoInformado);
    }
}
