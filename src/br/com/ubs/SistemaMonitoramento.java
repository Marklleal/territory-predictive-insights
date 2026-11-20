// Classe principal com menu, histórico em ArrayList, previsões e alertas automáticos.
package br.com.ubs;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import br.com.ubs.DadosTerritorio.Indicador;
import br.com.ubs.DadosTerritorio.Nivel;

public class SistemaMonitoramento {

    // Unidade da UBS
    private final EstacaoMonitoramentoUBS estacao;

    // Guarda as leituras de cada sensor ao longo das horas simuladas.
    private final List<Integer> historicoFila = new ArrayList<>();
    private final List<Integer> historicoAtendimentos = new ArrayList<>();
    private final List<Integer> historicoEstoque = new ArrayList<>();

    // Duas formas de calcular previsões usando o histórico de leituras.
    private final EstrategiaPrevisao mediaMovel = new PrevisaoMediaMovel();
    private final EstrategiaPrevisao comparacao = new PrevisaoComparacao();

    // Conta quantas horas já foram simuladas.
    private int horaSimulada;

    public SistemaMonitoramento(EstacaoMonitoramentoUBS estacao) {
        this.estacao = estacao;
    }

    // Cria a UBS e inicia o menu com entrada pelo teclado.
    public static void main(String[] args) {
        DadosTerritorio dados = new DadosTerritorio("Xavier", 1_300);
        EstacaoMonitoramentoUBS estacao =
                new EstacaoMonitoramentoUBS("UBS Xavier", dados);

        try (Scanner entrada = new Scanner(System.in)) {
            new SistemaMonitoramento(estacao).executarMenu(entrada);
        }
    }

    // Repete o menu até o usuário sair ou a entrada de dados terminar.
    private void executarMenu(Scanner entrada) {
        while (true) {
            System.out.println("\n=== Monitoramento da UBS ===");
            System.out.println("1 - Simular próxima hora");
            System.out.println("2 - Rodar previsões");
            System.out.println("3 - Verificar sensores");
            System.out.println("4 - Gerenciar dados do território");
            System.out.println("5 - Exibir status completo");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            if (!entrada.hasNextLine()) {
                return;
            }

            switch (entrada.nextLine().trim()) {
                case "1":
                    simularProximaHora();
                    break;
                case "2":
                    rodarPrevisoes();
                    break;
                case "3":
                    verificarSensores();
                    break;
                case "4":
                    gerenciarDadosTerritorio(entrada);
                    break;
                case "5":
                    exibirStatusCompleto();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    // Avança uma hora, coleta novas leituras e atualiza as previsões.
    private void simularProximaHora() {
        horaSimulada++;
        System.out.printf("%n=== Hora simulada %d ===%n", horaSimulada);

        registrarLeitura(estacao.getSensorFila(), historicoFila);
        registrarLeitura(estacao.getSensorAtendimentos(), historicoAtendimentos);
        registrarLeitura(estacao.getSensorEstoque(), historicoEstoque);

        rodarPrevisoes();
    }

    // Adiciona uma leitura ao histórico apenas se o sensor estiver funcionando.
    private void registrarLeitura(Sensor sensor, List<Integer> historico) {
        if (!sensor.verificarFuncionamento()) {
            System.out.printf("%s — sensor indisponível.%n", sensor.getTipo());
            return;
        }

        int leitura = sensor.lerValor();
        historico.add(leitura);
        System.out.printf("%s — leitura de %d.%n", sensor.getTipo(), leitura);
    }


    // Executa cada previsão quando houver leituras suficientes no histórico.
    private void preverIndicador(
        Sensor sensor, 
        List<Integer> historico, 
        String unidade,
        int limiteAtencao, 
        int limiteAlerta, 
        int limiteCritico,
        boolean alertaQuandoBaixo
    ) {
        String indicador = sensor.getTipo();
        int total = historico.size();

        // A média móvel precisa de pelo menos três leituras.
        if (total < 3) {
            System.out.printf(
                "%s — histórico insuficiente para média móvel (%d/3).%n",
                indicador, total
            );
        } else {
            exibirResultado(
                indicador, "Média móvel", mediaMovel.prever(historico), unidade,
                limiteAtencao, limiteAlerta, limiteCritico, alertaQuandoBaixo
            );
        }

        // A comparação precisa de pelo menos seis leituras.
        if (total < 6) {
            System.out.printf(
                "%s — histórico insuficiente para comparação (%d/6).%n",
                indicador, total
            );
        } else {
            exibirResultado(
                indicador, "Comparação", comparacao.prever(historico), unidade,
                limiteAtencao, limiteAlerta, limiteCritico, alertaQuandoBaixo
            );
        }
    }

    // Mostra a previsão e o nível de alerta correspondente.
    private void exibirResultado(
            String indicador, 
            String estrategia, 
            double previsao, 
            String unidade,
            int limiteAtencao, 
            int limiteAlerta, 
            int limiteCritico,
            boolean alertaQuandoBaixo
    ) {
        String nivel;
        int limiteAtingido;

        // Verifica primeiro o nível mais grave para exibir apenas um resultado.
        if (atingiuLimite(previsao, limiteCritico, alertaQuandoBaixo)) {
            nivel = "CRÍTICO";
            limiteAtingido = limiteCritico;
        } else if (atingiuLimite(previsao, limiteAlerta, alertaQuandoBaixo)) {
            nivel = "ALERTA";
            limiteAtingido = limiteAlerta;
        } else if (atingiuLimite(previsao, limiteAtencao, alertaQuandoBaixo)) {
            nivel = "ATENÇÃO";
            limiteAtingido = limiteAtencao;
        } else {
            System.out.printf(
                "%s — %s — OK. Previsão de %.2f %s.%n",
                indicador, estrategia, previsao, unidade
            );
            return;
        }

        System.out.printf(
            "%s — %s — %s. Previsão de %.2f %s. Limiar atingido: %d.%n",
            nivel, indicador, estrategia, previsao, unidade, limiteAtingido
        );
    }

    // Estoque gera alerta ao cair; fila e atendimentos geram alerta ao subir.
    private boolean atingiuLimite(
            double previsao, int limite, boolean alertaQuandoBaixo
    ) {
        return alertaQuandoBaixo ? previsao <= limite : previsao >= limite;
    }

    // Exibe se cada sensor está disponível para realizar leituras.
    private void verificarSensores() {
        for (Sensor sensor : estacao.getSensores()) {
            String estado = sensor.verificarFuncionamento()
                ? "Operacional" : "Indisponível";
            System.out.printf("%s — %s%n", sensor.getTipo(), estado);
        }
    }

    // Calcula as previsões dos três indicadores monitorados.
    private void rodarPrevisoes() {
        System.out.println("\n=== Previsões ===");
    
        preverIndicadorConfigurado(
                Indicador.FILA, estacao.getSensorFila(), historicoFila, "pessoas");
        preverIndicadorConfigurado(
                Indicador.ATENDIMENTOS, estacao.getSensorAtendimentos(),
                historicoAtendimentos, "atendimentos por hora");
        preverIndicadorConfigurado(
                Indicador.ESTOQUE, estacao.getSensorEstoque(),
                historicoEstoque, "unidades");
    }
    
    // Usa os limites atuais do território para avaliar a previsão do indicador.
    private void preverIndicadorConfigurado(
            Indicador indicador, Sensor sensor, List<Integer> historico, String unidade
    ) {
        DadosTerritorio dados = estacao.getDadosTerritorio();
    
        preverIndicador(
                sensor, historico, unidade,
                dados.getLimiar(indicador, Nivel.ATENCAO),
                dados.getLimiar(indicador, Nivel.ALERTA),
                dados.getLimiar(indicador, Nivel.CRITICO),
                indicador.isAlertaQuandoBaixo());
    }
    
    // Permite escolher um indicador e alterar um de seus limites com código de acesso.
    private void gerenciarDadosTerritorio(Scanner entrada) {
        DadosTerritorio dados = estacao.getDadosTerritorio();
        exibirDadosTerritorio(dados);
    
        System.out.println("\n1 - Fila | 2 - Atendimentos | 3 - Estoque | 0 - Voltar");
        System.out.print("Indicador: ");
        Integer opcaoIndicador = lerInteiro(entrada);
        if (opcaoIndicador == null || opcaoIndicador == 0) {
            return;
        }
    
        Indicador[] indicadores = Indicador.values();
        if (opcaoIndicador < 1 || opcaoIndicador > indicadores.length) {
            System.out.println("Indicador inválido.");
            return;
        }
        // As opções começam em 1, mas as posições do array começam em 0.
        Indicador indicador = indicadores[opcaoIndicador - 1];
    
        System.out.println("1 - Atenção | 2 - Alerta | 3 - Crítico | 0 - Voltar");
        System.out.print("Nível: ");
        Integer opcaoNivel = lerInteiro(entrada);
        if (opcaoNivel == null || opcaoNivel == 0) {
            return;
        }
    
        Nivel[] niveis = Nivel.values();
        if (opcaoNivel < 1 || opcaoNivel > niveis.length) {
            System.out.println("Nível inválido.");
            return;
        }
        Nivel nivel = niveis[opcaoNivel - 1];
    
        System.out.printf("Valor atual: %d. Novo valor: ", dados.getLimiar(indicador, nivel));
        Integer novoValor = lerInteiro(entrada);
        if (novoValor == null) {
            return;
        }
    
        System.out.print("Código de acesso: ");
        if (!entrada.hasNextLine()) {
            return;
        }
    
        String codigoInformado = entrada.nextLine();
        if (dados.alterarLimiar(indicador, nivel, novoValor, codigoInformado)) {
            System.out.printf("Limiar de %s atualizado para %d.%n",
                    indicador.getNome(), novoValor);
        } else {
            System.out.println(
                    "Alteração recusada. Confira o código, o valor positivo e a ordem dos limiares.");
        }
    }
    
    // Retorna null se a entrada terminar ou se o texto não for um número inteiro.
    private Integer lerInteiro(Scanner entrada) {
        if (!entrada.hasNextLine()) {
            return null;
        }
    
        try {
            return Integer.parseInt(entrada.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Digite um número inteiro.");
            return null;
        }
    }
    
    // Mostra a região, a população e os limites configurados de cada indicador.
    private void exibirDadosTerritorio(DadosTerritorio dados) {
        System.out.printf("Região: %s%n", dados.getRegiao());
        System.out.printf("População: %d%n", dados.getPopulacao());
    
        for (Indicador indicador : Indicador.values()) {
            System.out.printf("%s — atenção: %d | alerta: %d | crítico: %d%n",
                    indicador.getNome(),
                    dados.getLimiar(indicador, Nivel.ATENCAO),
                    dados.getLimiar(indicador, Nivel.ALERTA),
                    dados.getLimiar(indicador, Nivel.CRITICO));
        }
    }
    
    // Reúne os dados da UBS, o estado dos sensores e os históricos de leituras.
    private void exibirStatusCompleto() {
        System.out.printf("Estação: %s%n", estacao.getIdentificacao());
        System.out.printf("Status: %s%n", estacao.verificarStatus());
        System.out.printf("Horas simuladas: %d%n", horaSimulada);

        exibirDadosTerritorio(estacao.getDadosTerritorio());
        verificarSensores();

        System.out.printf("Histórico da fila: %s%n", historicoFila);
        System.out.printf("Histórico dos atendimentos: %s%n", historicoAtendimentos);
        System.out.printf("Histórico do estoque: %s%n", historicoEstoque);
    }
}
