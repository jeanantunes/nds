package br.com.abril.nds.integracao.ems0133.processor;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.integracao.ems0133.outbound.EMS0133Output;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.service.integracao.DistribuidorService;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

@Component

public class EMS0133MessageProcessor extends AbstractRepository implements MessageProcessor  {


	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	private static SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");

	@Autowired
	private FixedFormatManager fixedFormatManager;

	@Autowired
	private DistribuidorService distribuidorService;

	public EMS0133MessageProcessor() {

	}

	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void processMessage(Message message) {	
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		Date data = new Date();
		
		StringBuilder sql = new StringBuilder()
			.append("SELECT l ")
			.append("FROM Lancamento l ")
			.append("WHERE l.id ")
			.append("IN (SELECT lc.id FROM Lancamento lc ")
			.append("JOIN l.produtoEdicao pe ")
			.append("JOIN pe.produto p ")
			.append("JOIN p.fornecedores f ")
			.append("WHERE l.dataRecolhimentoDistribuidor = :dataOperacao)");
		
		Query query = getSession().createQuery(sql.toString());

		query.setParameter("dataOperacao", distribuidor.getDataOperacao());
		

		
		try {

			@SuppressWarnings("unchecked")
			List<Lancamento> lancamentos = query.list();
			if (lancamentos.isEmpty()) {
				ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.RELACIONAMENTO, "Nenhum resultado encontrado para Data de Operação: "+ distribuidor.getDataOperacao());
			}
			
			PrintWriter print = new PrintWriter(new FileWriter(message.getHeader().get(MessageHeaderProperties.OUTBOUND_FOLDER.getValue())+"/RECOLHIMENTO.NEP"));	
			
			
			
			for (Lancamento lancamento : lancamentos){
				
				EMS0133Output output = new EMS0133Output();
				
				for(Fornecedor fornecedor : lancamento.getProdutoEdicao().getProduto().getFornecedores()){			
						output.setCodigoFornecedorProduto(fornecedor.getCodigoInterface());
						output.setCodigoProduto(lancamento.getProdutoEdicao().getProduto().getCodigo());
						output.setNumeroEdicao(lancamento.getProdutoEdicao().getNumeroEdicao());						
						output.setContextoProduto(lancamento.getProdutoEdicao().getProduto().getCodigoContexto());
						output.setDataGeracaoArquivo(data);
						output.setHoraGeracaoArquivo(data);
						output.setDataRecolhimento(lancamento.getDataRecolhimentoDistribuidor());
						output.setCodigoDistribuidor(distribuidor.getCodigoDistribuidorDinap());
						
						print.println(fixedFormatManager.export(output));
				}
				
				
			}
			
			print.flush();
			print.close();
			
		} catch (IOException e) {
			
			ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.GERACAO_DE_ARQUIVO, "Não foi possível gerar o arquivo");
			
		} 
		
		
	}
	
	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}
