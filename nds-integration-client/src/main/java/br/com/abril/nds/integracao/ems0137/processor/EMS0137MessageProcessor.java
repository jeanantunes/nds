package br.com.abril.nds.integracao.ems0137.processor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

import org.lightcouch.CouchDbClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.model.canonic.EMS0137Input;
import br.com.abril.nds.integracao.model.canonic.EMS0137InputItem;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Component
public class EMS0137MessageProcessor extends AbstractRepository implements MessageProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(EMS0137MessageProcessor.class);

	@Autowired
	private DistribuidorService distribuidorService;

	@Override
	public void preProcess(AtomicReference<Object> tempVar) {

	}

	@Override
	public void processMessage(Message message) {

		CouchDbClient dbClient = null;

		Connection connection = null;

		try {	

			String codigoDistribuidor = distribuidorService.obter().getCodigoDistribuidorDinap();
			
			EMS0137Input input = (EMS0137Input) message.getBody();
			
			dbClient = getCouchDBClient(codigoDistribuidor);

			for (EMS0137InputItem item : input.getItems()) {						

				getSession().merge(item);
				getSession().flush();

			}
			
			dbClient.remove(input);

		}
		catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		finally {
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


		/*EMS0136Input input = (EMS0136Input) message.getBody();

		// Validar código do distribuidor:
		Distribuidor distribuidor = this.distribuidorService.obter();
		if(!distribuidor.getCodigoDistribuidorDinap().equals(
				input.getCodigoDistribuidor())){			
			this.ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.RELACIONAMENTO, 
					"Código do distribuidor do arquivo não é o mesmo do Sistema.");
			return;
		}

		// Validar Produto/Edicao
		final String codigoProduto = input.getCodigoProduto();
		final Long numeroEdicao = input.getEdicaoCapa();
		ProdutoEdicao produtoEdicao = this.obterProdutoEdicao(codigoProduto,
				numeroEdicao);
		if (produtoEdicao == null) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"Impossivel realizar Insert/update - Nenhum resultado encontrado para Produto: "
							+ codigoProduto + " e Edicao: " + numeroEdicao
							+ " na tabela produto_edicao");
			return;
		}

		LancamentoParcial lancamentoParcial = this.obterLancalmentoParcial(
				input, produtoEdicao);

		this.gerarPeriodoLancamentoParcial(input, lancamentoParcial);*/
	}

	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub

	}

}
