# Territory Predictive Insights — Monitoramento de uma UBS

Projeto de Programação Orientada a Objetos da Global Solution 2026.2. O sistema simula leituras de uma Unidade Básica de Saúde fictícia e apresenta previsões e alertas no terminal.

## Público beneficiado

A equipe da UBS pode usar os indicadores para identificar quando vale investigar o crescimento da fila, revisar a capacidade de atendimento ou repor kits. Quem aguarda atendimento também pode se beneficiar de uma resposta mais rápida da equipe a esses problemas.

## Como compilar e executar

Tenha um JDK instalado e disponível no terminal. Na raiz do repositório, onde fica a pasta `src`, execute:

```bash
javac -encoding UTF-8 -d out src/br/com/ubs/*.java &&
  java -cp out br.com.ubs.SistemaMonitoramento
```

| Opção | Ação |
|---|---|
| 1 | Simular a próxima hora, registrar as leituras disponíveis e verificar as previsões automaticamente. |
| 2 | Rodar as previsões com o histórico existente. |
| 3 | Verificar o funcionamento de cada sensor. |
| 4 | Consultar os dados do território e alterar um limiar. |
| 5 | Exibir o status da estação, os dados do território e os históricos. |
| 0 | Sair. |

Na opção 4, escolha o indicador e o nível, informe o novo valor e use o código de demonstração `segredo`. O código é uma trava do exercício. Os históricos e os ajustes são mantidos em memória durante a execução.

## Decisões da equipe

- A interface `Sensor` define o contrato comum para fila, atendimentos e estoque. `EstacaoMonitoramentoUBS` herda de `ComponenteTerritorial` e reúne os três sensores.
- Cada indicador tem seu próprio histórico em `ArrayList`. A média móvel usa as três leituras mais recentes. A comparação usa as seis mais recentes, em dois grupos de três, e soma à média recente a diferença entre as duas médias.
- `DadosTerritorio` concentra os limiares e controla suas alterações. Fila e atendimentos acionam alertas ao atingir ou superar o limiar. Estoque aciona alertas ao atingir ou cair abaixo dele. Para cada previsão, o console exibe o nível mais grave atingido, o indicador, o valor previsto e o limiar.
- Os limiares iniciais são valores de demonstração para a unidade fictícia.

| Indicador | Atenção | Alerta | Crítico |
|---|---:|---:|---:|
| Fila (pessoas) | 15 | 18 | 25 |
| Atendimentos (por hora) | 10 | 12 | 18 |
| Estoque (kits) | 20 | 10 | 5 |

Para demonstrar um alerta pelo próprio menu, simule três horas com os sensores operacionais. Na opção 4, ajuste um limiar usando a média móvel exibida como referência e respeitando a ordem dos níveis. Por exemplo, se a média da fila for 5, altere a atenção da fila para 5 e rode a opção 2. A média móvel da fila deve indicar **ATENÇÃO**. Após seis leituras por sensor, a opção 2 também exibe a previsão por comparação.

## Reflexão crítica

O sistema deve se abster de emitir uma previsão e seu alerta quando faltar histórico para a estratégia escolhida. Com menos de três leituras, a média móvel fica indisponível. A comparação precisa de seis leituras para formar dois grupos completos. Esses mínimos permitem executar o cálculo, mas uma amostra pequena ainda pode refletir um pico passageiro, uma reposição pontual ou uma falha de coleta. A equipe deve interpretar a previsão levando em conta o contexto do turno e a qualidade das leituras.

Cada indicador representa apenas uma parte da operação. O tamanho da fila informa quantas pessoas estão aguardando, mas deixa de fora a gravidade dos casos e o tempo de espera. Atendimentos por hora medem o volume realizado. Uma hora com poucos atendimentos pode coincidir com uma fila grande se os casos forem demorados ou se a equipe estiver reduzida. O total de kits também não informa, por si só, a distribuição por tipo de material ou quando chega a próxima reposição. Como as leituras do projeto são simuladas, os resultados demonstram o funcionamento das regras.

O alerta dá à equipe um motivo para verificar a situação. Uma previsão de estoque baixo pode motivar a conferência do inventário e do prazo de reposição. A decisão de solicitar material, redistribuir profissionais ou reorganizar a fila depende de quem conhece a operação. Mostrar o valor previsto e o limiar permite que essa pessoa entenda o motivo do alerta antes de agir.

## Uso de IA

*By ChatGPT*: O ChatGPT foi usado para esclarecer conceitos de Java e POO, diagnosticar e sugerir trechos de implementação para a estação. Também apoiou a redação deste README. Durante o desenvolvimento, também foi usado para testar e validar os cenários de histórico, previsões, níveis de alerta e funcionamento do menu.

## Integrantes

| Integrante                    | RM     |
| ----------------------------- | ------ |
| Mark Lima Leal                | 566760 |
| Pedro Paulo Ferreira Agnelo   | 567564 |
| Gabriel Guimarães de Oliveira | 567835 |
