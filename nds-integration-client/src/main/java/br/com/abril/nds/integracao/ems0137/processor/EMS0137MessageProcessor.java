package br.com.abril.nds.integracao.ems0137.processor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import org.lightcouch.CouchDbClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0137Input;
import br.com.abril.nds.integracao.model.canonic.EMS0137InputItem;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;
import br.com.abril.nds.model.planejamento.fornecedor.ItemChamadaEncalheFornecedor;
import br.com.abril.nds.model.planejamento.fornecedor.RegimeRecolhimento;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Component
public class EMS0137MessageProcessor extends AbstractRepository implements MessageProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(EMS0137MessageProcessor.class);

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory; 
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {

	}

	@Override
	public void processMessage(Message message) {
		
		message.getHeader().put(MessageHeaderProperties.FILE_NAME.getValue(), "Oracle : Icd : TH152 : icd_user");

		CouchDbClient dbClient = null;

		Connection connection = null;
		
		EMS0137Input input = (EMS0137Input) message.getBody();

		try {	

			String codigoDistribuidor = distribuidorService.obter().getCodigoDistribuidorDinap();
			
			// Validar código do distribuidor:
			Distribuidor distribuidor = this.distribuidorService.obter();
			if(!distribuidor.getCodigoDistribuidorDinap().equals(
					input.getCodigoDistribuidor())){			
				this.ndsiLoggerFactory.getLogger().logWarning(message,
						EventoExecucaoEnum.RELACIONAMENTO, 
						"Código do distribuidor do arquivo não é o mesmo do Sistema.");
				return;
			}
			
			dbClient = getCouchDBClient(codigoDistribuidor);
			
			ChamadaEncalheFornecedor ce = montarChamadaEncalheFornecedor(message, input);
			
			getSession().merge(ce);
			getSession().flush();

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

	}

	private ChamadaEncalheFornecedor montarChamadaEncalheFornecedor(Message message, EMS0137Input input) {
		
		ChamadaEncalheFornecedor ce = new ChamadaEncalheFornecedor();
		
		ce.setNumeroChamadaEncalhe(input.getCePK().getNumeroChamadaEncalhe());
		ce.setAnoReferencia(input.getDataAnoReferencia());
		ce.setCodigoDistribuidor(Long.parseLong(input.getCodigoDistribuidor()));
		ce.setCodigoPreenchimento(input.getCodigoPreenchimento());
		ce.setDataEmissao(input.getDataEmissao());
		ce.setDataVencimento(input.getDataLimiteRecebimento()); //TODO: Sérgio: verificar se a data esta correta
		ce.setDataLimiteRecebimento(input.getDataLimiteRecebimento());
		ce.setNotaValoresDiversos(input.getValorNotaValoresDiversos());
		ce.setNumeroSemana(input.getNuemroSemanaReferencia());
		ce.setStatus(input.getTipoStatus());
		ce.setTipoChamadaEncalhe(input.getCodigoTipoChamadaEncalhe());
		ce.setTotalCreditoApurado(input.getValorTotalCreditoApurado());
		ce.setTotalCreditoInformado(input.getValorTotalCreditoInformado());
		ce.setTotalMargemApurado(input.getValorTotalMargemApurado());
		ce.setTotalMargemInformado(input.getValorTotalMargemInformado());
		ce.setTotalVendaApurada(input.getValorTotalVendaApurada());
		ce.setTotalVendaInformada(input.getValorTotalVendaInformada());
		
		if(ce.getItens() == null && input.getItems().size() > 0) {
			ce.setItens(new ArrayList<ItemChamadaEncalheFornecedor>());
		}

		montarItensChamadaEncalheFornecedor(message, input, ce);
		
		return ce;
		
	}

	private void montarItensChamadaEncalheFornecedor(Message message, EMS0137Input input, ChamadaEncalheFornecedor ce) {
		
		for (EMS0137InputItem item : input.getItems()) {
			
			if(item.getLancamentoEdicaoPublicacao() == null || (item.getLancamentoEdicaoPublicacao() != null && item.getLancamentoEdicaoPublicacao().getCodigoPublicacao() == null)) {
				
				this.ndsiLoggerFactory.getLogger().logError(message,
						EventoExecucaoEnum.SEM_DOMINIO,
						"Não foi possível incluir registro - Dados incompletos vindos do Icd - Codigo Produto: " +
						"Chamada Encalhe: "+ item.getCeItemPK().getNumeroChamadaEncalhe() +
						" Item Chamada Encalhe: "+ item.getCeItemPK().getNumeroItem());
				continue;
				
			}
			
			if(item.getLancamentoEdicaoPublicacao() != null && item.getLancamentoEdicaoPublicacao().getNumeroEdicao() == null) {
				
				this.ndsiLoggerFactory.getLogger().logError(message,
						EventoExecucaoEnum.SEM_DOMINIO,
						"Não foi possível incluir registro - Dados incompletos vindos do Icd - Numero edição: " +
						"Chamada Encalhe: "+ item.getCeItemPK().getNumeroChamadaEncalhe() +
						" Item Chamada Encalhe: "+ item.getCeItemPK().getNumeroItem());
				continue;
				
			}
			
			String codigoProduto = item.getLancamentoEdicaoPublicacao().getCodigoPublicacao();
			
			Long numeroEdicao  = item.getLancamentoEdicaoPublicacao().getNumeroEdicao().longValue();
			
			ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(
					codigoProduto
					, numeroEdicao);
			
			if (produtoEdicao == null) {
				this.ndsiLoggerFactory.getLogger().logError(message,
						EventoExecucaoEnum.RELACIONAMENTO,
						"Não foi possível incluir registro - Nenhum resultado encontrado para Produto/Edição: "
						+ codigoProduto + " e Edicao: " + numeroEdicao
						+ " no cadastro de edições do Novo Distrib");
				continue;
			}
			
			ItemChamadaEncalheFornecedor ice = new ItemChamadaEncalheFornecedor();
			
			ice.setChamadaEncalheFornecedor(ce);
			ice.setNumeroItem(item.getCeItemPK().getNumeroItem());
			ice.setProdutoEdicao(produtoEdicao);
			ice.setControle(item.getNumeroControle());
			ice.setDataRecolhimento(item.getDataRecolhimento());
			ice.setRegimeRecolhimento(RegimeRecolhimento.getByCodigo(item.getCodigoRegimeRecolhimento()));
			ice.setDataRecolhimento(item.getDataRecolhimento());
			ice.setControle(item.getNumeroControle());
			ice.setNumeroDocumento(item.getNumeroDocumento());
			ice.setNumeroNotaEnvio(item.getNumeroNotaEnvio());
			ice.setPrecoUnitario(item.getValorPrecoUnitario());
			ice.setQtdeDevolucaoApurada(item.getQuantidadeDevolucaoApurada());
			ice.setQtdeDevolucaoInformada(item.getQuantidadeDevolucaoInformada());
			ice.setQtdeDevolucaoParcial(item.getQuantidadeDevolucaoParcial());
			ice.setQtdeEnviada(item.getQuantidadeEnviada());
			ice.setQtdeVendaApurada(item.getQuantidadeVendaApurada());
			ice.setQtdeVendaInformada(item.getQuantidadeVendaInformada());
			ice.setTipoProduto(item.getTipoProduto());
			ice.setStatus(item.getTipoStatus());
			ice.setValorMargemApurado(item.getValorMargemApurado());
			ice.setValorMargemInformado(item.getValorMargemInformado());
			ice.setValorVendaApurado(item.getValorVendaApurada());
			ice.setValorVendaInformado(item.getValorVendaInformada());
			
			//item.getCodigoLancamentoEdicao()

			ce.getItens().add(ice);
			
		}
		
	}

	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub

	}

}
