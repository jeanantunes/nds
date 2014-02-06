package br.com.abril.nds.integracao.ems2021.processor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang.StringUtils;
import org.lightcouch.CouchDbClient;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;import br.com.abril.nds.integracao.model.canonic.EMS2021Input;
import br.com.abril.nds.integracao.model.canonic.EMS2021InputItem;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.planejamento.EdicaoBaseEstrategia;
import br.com.abril.nds.model.planejamento.Estrategia;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.EstrategiaService;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Component
public class EMS2021MessageProcessor extends AbstractRepository implements MessageProcessor {

    private static final Logger LOGGER = Logger.getLogger(EMS2021MessageProcessor.class);

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
    }

    @Override
    public void processMessage(Message message) {

	message.getHeader().put(MessageHeaderProperties.FILE_NAME.getValue(), "Oracle : Icd : TH152 : icd_user");
	CouchDbClient dbClient = null;
	Connection connection = null;
	EMS2021Input input = (EMS2021Input) message.getBody();
	try {
	    // Validar código do distribuidor:
	    Distribuidor distribuidor = distribuidorService.obter();
	    if (!distribuidor.getCodigoDistribuidorDinap().equals(input.getCodigoDistribuidor())) {
		this.ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.RELACIONAMENTO,
			"Código do distribuidor do arquivo não é o mesmo do Sistema.");
		return;
	    }
	    dbClient = getCouchDBClient(distribuidor.getCodigoDistribuidorDinap());
	    Estrategia estrategia = montarEstrategia(message, input);
	    getSession().merge(estrategia);
	    getSession().flush();
	} catch (Exception e) {
	    LOGGER.error(e.getMessage(), e);
	    message.getHeader().put(MessageHeaderProperties.ERRO_PROCESSAMENTO.getValue(), "Erro ao processar registro. " + e.getMessage());
	} finally {
	    if (connection != null) {
		try {
		    connection.close();
		} catch (SQLException e) {
		}
	    }
	    if (dbClient != null) {
		dbClient.shutdown();
	    }
	}
    }

    private Estrategia montarEstrategia(Message message, EMS2021Input input) {

	ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(
		    StringUtils.leftPad(input.getCodigoProduto(), 8, "0"), input.getNumeroEdicao());

	if (produtoEdicao == null) {
	    this.ndsiLoggerFactory.getLogger().logError(
		    message, EventoExecucaoEnum.RELACIONAMENTO,
		    "Não foi possível incluir registro - Nenhum resultado encontrado para Produto/Edição: " + input.getCodigoProduto() + " e Edicao: "
		    + input.getNumeroEdicao() + " no cadastro de edições do Novo Distrib");
	}
	
	Estrategia estrategia = new Estrategia();
	estrategia.setAbrangencia(BigDecimal.valueOf(input.getAbrangencia()));
	estrategia.setCesta(input.getCesta());
	estrategia.setOportunidadeVenda(input.getOportunidadeVenda());
	estrategia.setReparteMinimo(BigInteger.valueOf(input.getReparteMinimo()));
	estrategia.setProdutoEdicao(produtoEdicao);
	estrategia.setPeriodo(input.getPeriodo());
	
	if (estrategia.getBasesEstrategia() == null && input.getItems().size() > 0) {
	    estrategia.setBasesEstrategia(new ArrayList<EdicaoBaseEstrategia>());
	}
	montarEdicoesBaseEstrategia(message, input, estrategia);
	return estrategia;
    }

    private void montarEdicoesBaseEstrategia(Message message, EMS2021Input input, Estrategia estrategia) {

	for (EMS2021InputItem item : input.getItems()) {

	    ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(
		    StringUtils.leftPad(item.getCodigoProduto(), 8, "0"), item.getNumeroEdicao());

	    if (produtoEdicao == null) {
		this.ndsiLoggerFactory.getLogger().logError(
			message,
			EventoExecucaoEnum.RELACIONAMENTO,
			"Não foi possível incluir registro - Nenhum resultado encontrado para Produto/Edição: " + item.getCodigoProduto()
				+ " e Edicao: " + item.getNumeroEdicao() + " no cadastro de edições do Novo Distrib");
		continue;
	    }

	    EdicaoBaseEstrategia edicao = new EdicaoBaseEstrategia();
	    edicao.setEstrategia(estrategia);
	    edicao.setPeso(1);
	    edicao.setProdutoEdicao(produtoEdicao);
	    estrategia.getBasesEstrategia().add(edicao);
	}
    }

    @Override
    public void posProcess(Object tempVar) {
    }
}
