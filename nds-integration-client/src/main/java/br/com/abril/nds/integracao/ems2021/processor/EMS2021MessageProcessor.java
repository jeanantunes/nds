package br.com.abril.nds.integracao.ems2021.processor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.integracao.model.canonic.EMS2021Input;
import br.com.abril.nds.integracao.model.canonic.EMS2021InputItem;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.planejamento.EdicaoBaseEstrategia;
import br.com.abril.nds.model.planejamento.Estrategia;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.EstrategiaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;

@Component
public class EMS2021MessageProcessor extends AbstractRepository implements MessageProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(EMS2021MessageProcessor.class);

    @Autowired
    private NdsiLoggerFactory ndsiLoggerFactory;

    @Autowired
    private EstrategiaService estrategiaService;

    @Autowired
    private ProdutoEdicaoRepository produtoEdicaoRepository;

    @Override
    public void preProcess(AtomicReference<Object> tempVar) {
        LOGGER.info("EMS2021 preProcesss Client: {}", tempVar);
    }

    @Override
    public void processMessage(Message message) {

        LOGGER.info(":: Carregando estrategia {}", message.getBody());
        EMS2021Input input = (EMS2021Input) message.getBody();
        try {
            // Valida o código do distribuidor:
            String codigoDistribuidorDinap = message.getHeader().get(MessageHeaderProperties.CODIGO_DISTRIBUIDOR.getValue()).toString();
            if (!codigoDistribuidorDinap.equals(input.getCodigoDistribuidor().toString())) {
                this.ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.RELACIONAMENTO,
                        "Código do distribuidor do arquivo não é o mesmo do Sistema.");
                return;
            }

            Estrategia estrategia = this.montarEstrategia(message, input);

            if (estrategia != null) {
                getSession().merge(estrategia);
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            message.getHeader().put(MessageHeaderProperties.ERRO_PROCESSAMENTO.getValue(), "Erro ao processar registro. " + e.getMessage());
        }

    }

    private Estrategia montarEstrategia(Message message, EMS2021Input input) {

        Estrategia estrategia = estrategiaService.buscarPorCodigoProdutoNumeroEdicao(input.getCodigoProduto(), input.getNumeroEdicao());

        if (estrategia == null) {

            ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(
                    input.getCodigoProduto(), input.getNumeroEdicao());

            if (produtoEdicao == null) {
                this.ndsiLoggerFactory.getLogger().logError(
                        message, EventoExecucaoEnum.RELACIONAMENTO,
                        "Não foi possível incluir registro - Produto " + input.getCodigoProduto() + " Edição "
                                + input.getNumeroEdicao() + " não encontrado.");
                return null;
            }

            estrategia = new Estrategia();
            estrategia.setProdutoEdicao(produtoEdicao);

        }

        estrategia.setAbrangencia(BigDecimal.valueOf(input.getAbrangenciaDistribuicao()));
        estrategia.setCesta(input.getCesta());
        estrategia.setOportunidadeVenda(input.getOportunidadeVenda());
        estrategia.setReparteMinimo(input.getReparteMinimo());
        estrategia.setPeriodo(input.getPeriodo());

        if (estrategia.getBasesEstrategia() == null && input.getItens().size() > 0) {
            estrategia.setBasesEstrategia(new ArrayList<EdicaoBaseEstrategia>());
            this.montarEdicoesBaseEstrategia(message, input, estrategia);
        }

        return estrategia;
    }

    private void montarEdicoesBaseEstrategia(Message message, EMS2021Input input, Estrategia estrategia) {

        for (EMS2021InputItem item : input.getItens()) {

            EdicaoBaseEstrategia edicaoBaseEstrategia = null;

            if (estrategia.getId() != null) {
                edicaoBaseEstrategia = estrategiaService.buscarBasePorCodigoProdutoNumEdicao(estrategia.getId(), item.getCodigoProduto(), item.getNumeroEdicao());

            }

            if (edicaoBaseEstrategia == null) {

                ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(
                        item.getCodigoProduto(), item.getNumeroEdicao());

                if (produtoEdicao == null) {
                    this.ndsiLoggerFactory.getLogger().logError(
                            message,
                            EventoExecucaoEnum.RELACIONAMENTO,
                            "Não foi possível incluir registro - Produto " + item.getCodigoProduto()
                                    + " Edição " + item.getNumeroEdicao() + " não encontrado.");
                    continue;
                }

                edicaoBaseEstrategia = new EdicaoBaseEstrategia();
                edicaoBaseEstrategia.setProdutoEdicao(produtoEdicao);
            }

            edicaoBaseEstrategia.setEstrategia(estrategia);
            edicaoBaseEstrategia.setPeso(item.getPeso());
            edicaoBaseEstrategia.setPeriodoEdicao(item.getPeriodo());
            estrategia.getBasesEstrategia().add(edicaoBaseEstrategia);
        }
    }

    @Override
    public void posProcess(Object tempVar) {
        LOGGER.info("EMS2021 posProcess Client: {}", tempVar);
    }
}