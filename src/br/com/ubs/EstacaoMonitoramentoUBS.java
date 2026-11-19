// Estende ComponenteTerritorial, reúne os três sensores e verifica o status da estação.
package br.com.ubs;

import java.util.List;

public class EstacaoMonitoramentoUBS extends ComponenteTerritorial {
    private final Sensor sensorFila = new SensorFila();
    private final Sensor sensorAtendimentos = new SensorAtendimentos();
    private final Sensor sensorEstoque = new SensorEstoque();

    private final List<Sensor> sensores = List.of(
            sensorFila, sensorAtendimentos, sensorEstoque
    );

    public EstacaoMonitoramentoUBS(String identificacao, DadosTerritorio dadosTerritorio) {
        // Acesso de superclasse (pais)
        super(identificacao, dadosTerritorio);
    }

    public Sensor getSensorFila() {
        return sensorFila;
    }

    public Sensor getSensorAtendimentos() {
        return sensorAtendimentos;
    }

    public Sensor getSensorEstoque() {
        return sensorEstoque;
    }

    public List<Sensor> getSensores() {
        return sensores;
    }

    @Override
    public String verificarStatus() {
        int sensoresOperacionais = 0;

        // Percorre o status de funcionamento de todos os sensores e incrementa
        for (Sensor sensor : sensores) {
            if (sensor.verificarFuncionamento()) {
                sensoresOperacionais++;
            }
        }

        if (sensoresOperacionais == sensores.size()) {
            return "OPERACIONAL";
        }
        if (sensoresOperacionais == 0) {
            return "INDISPONÍVEL";
        }
        return "OPERAÇÃO PARCIAL";
    }
}