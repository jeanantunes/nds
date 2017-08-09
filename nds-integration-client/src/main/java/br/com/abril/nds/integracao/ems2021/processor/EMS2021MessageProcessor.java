package br.com.abril.nds.integracao.ems2021.processor;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
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
import br.com.abril.nds.service.integracao.DistribuidorService;
import org.lightcouch.CouchDbClient;
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
    private DistribuidorService distribuidorService;

    @Autowired
    private ProdutoEdicaoRepository produtoEdicaoRepository;

    @Override
    public void preProcess(AtomicReference<Object> tempVar) {
        LOGGER.info("EMS2021 preProcesss Client: {}", tempVar);
    }

    @Override
    public void processMessage(Message message) {

        LOGGER.info(":: Carregando estrategia {}", message.getBody());
        message.getHeader().put(MessageHeaderProperties.FILE_NAME.getValue(), "Oracle : Icd : TH152 : icd_user");
        CouchDbClient dbClient = null;
        EMS2021Input input = (EMS2021Input) message.getBody();
        try {
            // Validar código do distribuidor:
            String codigoDistribuidorDinap = distribuidorService.codigoDistribuidorDinap();
            if (!codigoDistribuidorDinap.equals(input.getCodigoDistribuidor().toString())) {
                this.ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.RELACIONAMENTO,
                        "Código do distribuidor do arquivo não é o mesmo do Sistema.");
                return;
            }
            dbClient = getCouchDBClient(codigoDistribuidorDinap, true);
            Estrategia estrategia = this.montarEstrategia(message, input);
            getSession().flush();
            // TODO - Confirmar se esse flush está sendo chamado duas vezes - pois tbm existe um no processMessage da classe CouchDBImportDataRouter
            getSession().merge(estrategia);
            // FIXME - O que acontece se já existir uma estrategia p/ o produto edicao ? Devo incluir valições antes? Não seria muito custoso?

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            message.getHeader().put(MessageHeaderProperties.ERRO_PROCESSAMENTO.getValue(), "Erro ao processar registro. " + e.getMessage());
        } finally {
            if (dbClient != null) {
                dbClient.shutdown();
            }
        }
    }

    private Estrategia montarEstrategia(Message message, EMS2021Input input) {

        ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(
                input.getCodigoProduto(), input.getNumeroEdicao());

        if (produtoEdicao == null) {
            this.ndsiLoggerFactory.getLogger().logError(
                    message, EventoExecucaoEnum.RELACIONAMENTO,
                    "Não foi possível incluir registro - Produto " + input.getCodigoProduto() + " Edição "
                            + input.getNumeroEdicao() + " não encontrado.");
        }

        Estrategia estrategia = new Estrategia();
        estrategia.setAbrangencia(BigDecimal.valueOf(input.getAbrangenciaDistribuicao()));
        estrategia.setCesta(input.getCesta());
        estrategia.setOportunidadeVenda(input.getOportunidadeVenda());
        estrategia.setReparteMinimo(input.getReparteMinimo());
        estrategia.setProdutoEdicao(produtoEdicao);
        estrategia.setPeriodo(input.getPeriodo());

        if (estrategia.getBasesEstrategia() == null && input.getItens().size() > 0) {
            estrategia.setBasesEstrategia(new ArrayList<EdicaoBaseEstrategia>());
            this.montarEdicoesBaseEstrategia(message, input, estrategia);
        }

        return estrategia;
    }

    private void montarEdicoesBaseEstrategia(Message message, EMS2021Input input, Estrategia estrategia) {

        for (EMS2021InputItem item : input.getItens()) {

            ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(
                    item.getCodigoProduto(), item.getNumeroEdicao());

            if (produtoEdicao == null) {
                this.ndsiLoggerFactory.getLogger().logError(
                        message,
                        EventoExecucaoEnum.RELACIONAMENTO,
                        "Não foi possível incluir registro - Produto " + item.getCodigoProduto()
                                + " Edição " + item.getNumeroEdicao()
                                + " não encontrado.");
                continue;
            }

            EdicaoBaseEstrategia edicao = new EdicaoBaseEstrategia();
            edicao.setEstrategia(estrategia);
            edicao.setPeso(item.getPeso());
            edicao.setProdutoEdicao(produtoEdicao);
            edicao.setPeriodoEdicao(item.getPeriodo());
            estrategia.getBasesEstrategia().add(edicao);
        }
    }

    @Override
    public void posProcess(Object tempVar) {
        LOGGER.info("EMS2021 posProcess Client: {}", tempVar);
    }
}