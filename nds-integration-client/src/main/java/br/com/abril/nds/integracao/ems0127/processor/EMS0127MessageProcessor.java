package br.com.abril.nds.integracao.ems0127.processor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang.StringUtils;
import org.lightcouch.CouchDbClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0127Input;
import br.com.abril.nds.integracao.model.canonic.EMS0127InputItem;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;
import br.com.abril.nds.model.planejamento.fornecedor.FormaDevolucao;
import br.com.abril.nds.model.planejamento.fornecedor.ItemChamadaEncalheFornecedor;
import br.com.abril.nds.model.planejamento.fornecedor.RegimeRecolhimento;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Component
public class EMS0127MessageProcessor extends AbstractRepository implements MessageProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(EMS0127MessageProcessor.class);

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private FornecedorRepository fornecedorRepository;
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {

	}

	@Override
	public void processMessage(Message message) {
		
		CouchDbClient dbClient = null;

		Connection connection = null;
		
		EMS0127Input input = (EMS0127Input) message.getBody();
		
		message.getHeader().put(MessageHeaderProperties.FILE_NAME.getValue(), "Oracle : Icd : "+ input.getBaseDeDados() +" : "+ input.getUsuarioBaseDeDados());

		try {	

			// Validar código do distribuidor:
			Distribuidor distribuidor = this.distribuidorService.obter();
			
			if(input.getCodigoDistribuidor()!= null){
				input.setCodigoDistribuidor(input.getCodigoDistribuidor().trim()); 
			}
			
			final boolean isCodigoDistribuidorDinap = (distribuidor.getCodigoDistribuidorDinap() != null 
					&& distribuidor.getCodigoDistribuidorDinap().equals(input.getCodigoDistribuidor()));
			
			final boolean isCodigoDistribuidorFc = (distribuidor.getCodigoDistribuidorFC() != null 
					&& distribuidor.getCodigoDistribuidorFC().equals(input.getCodigoDistribuidor()));
			
			if(!isCodigoDistribuidorDinap && !isCodigoDistribuidorFc) {
				
				this.ndsiLoggerFactory.getLogger().logWarning(message,
						EventoExecucaoEnum.RELACIONAMENTO, 
						"Código do distribuidor [" + input.getCodigoDistribuidor() + "] do arquivo não é o mesmo do Sistema.");
				return;
				
			}
			
			dbClient = getCouchDBClient(input.getCodigoDistribuidor(), true);
			
			ChamadaEncalheFornecedor ce = montarChamadaEncalheFornecedor(message, input);
			
			getSession().merge(ce);
			getSession().flush();
			
			this.ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.RELACIONAMENTO, 
					"Chamada Encalhe Fornecedor inserida com sucesso: "+ input.getCePK().getNumeroChamadaEncalhe());
			return;

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			message.getHeader().put(MessageHeaderProperties.ERRO_PROCESSAMENTO.getValue(), "Erro ao processar registro. "+ e.getMessage());
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

	private ChamadaEncalheFornecedor montarChamadaEncalheFornecedor(Message message, EMS0127Input input) {
		
		ChamadaEncalheFornecedor ce = new ChamadaEncalheFornecedor();
		
		ce.setNumeroChamadaEncalhe(input.getCePK().getNumeroChamadaEncalhe());
		ce.setAnoReferencia(input.getDataAnoReferencia());
		ce.setCodigoDistribuidor(Long.parseLong(input.getCodigoDistribuidor()));
		ce.setCodigoPreenchimento(input.getCodigoPreenchimento());
		ce.setDataEmissao(input.getDataEmissao());
		ce.setDataVencimento(input.getDataLimiteRecebimento()); //TODO: Sérgio: verificar se a data esta correta
		ce.setDataLimiteRecebimento(input.getDataLimiteRecebimento());
		ce.setNotaValoresDiversos(input.getValorNotaValoresDiversos());
		ce.setNumeroSemana(input.getNumeroSemanaReferencia());
		ce.setStatus(input.getTipoStatus());
		ce.setTipoChamadaEncalhe(input.getCodigoTipoChamadaEncalhe());
		ce.setTotalCreditoApurado(input.getValorTotalCreditoApurado());
		ce.setTotalCreditoInformado(input.getValorTotalCreditoInformado());
		ce.setTotalMargemApurado(input.getValorTotalMargemApurado());
		ce.setTotalMargemInformado(input.getValorTotalMargemInformado());
		ce.setTotalVendaApurada(input.getValorTotalVendaApurada());
		ce.setTotalVendaInformada(input.getValorTotalVendaInformada());
		ce.setControle(input.getNumeroControle());
		
		if(input.getItems() != null && !input.getItems().isEmpty()) {
			ce.setItens(new ArrayList<ItemChamadaEncalheFornecedor>());
			
			//orientação de Cesar - item 5, planilha sprint 5, as informações faltantes dizem respeito a fornecedor
			//que nunca foi inserido
			String codigoProduto = input.getItems().get(0).getLancamentoEdicaoPublicacao().getCodigoPublicacao();
			
			List<Fornecedor> fornecedores = this.fornecedorRepository.obterFornecedoresDeProduto(codigoProduto, null);
			
			if (fornecedores != null && !fornecedores.isEmpty()){
				ce.setFornecedor(fornecedores.get(0));
			}
		}

		montarItensChamadaEncalheFornecedor(message, input, ce);
		
		return ce;
		
	}

	private void montarItensChamadaEncalheFornecedor(Message message, EMS0127Input input, ChamadaEncalheFornecedor ce) {
		
		for (EMS0127InputItem item : input.getItems()) {
			
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
					StringUtils.leftPad(codigoProduto, 8, "0") 
					, numeroEdicao);
			
			if (produtoEdicao == null) {
				this.ndsiLoggerFactory.getLogger().logError(message,
						EventoExecucaoEnum.RELACIONAMENTO,
						"Não foi possível incluir registro - Chamada de Encalhe/Item "+ input.getCePK().getNumeroChamadaEncalhe() 
						+ "/"+ item.getCeItemPK().getNumeroItem() 
						+ " Produto "+codigoProduto + " Edição " + numeroEdicao
						+" não encontrado.");
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
			
			if(item.getCodigoFormaDevolucao()!= null){
				ice.setFormaDevolucao(FormaDevolucao.getByCodigo(item.getCodigoFormaDevolucao().intValue()));
			}
			
			//item.getCodigoLancamentoEdicao()

			ce.getItens().add(ice);
			
		}
		
	}

	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub

	}

}
