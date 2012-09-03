package br.com.abril.nds.integracao.ems0133.processor;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0133.outbound.EMS0133Output;
import br.com.abril.nds.integracao.engine.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.impl.AbstractRepository;

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
	public void preProcess() {
		// TODO Auto-generated method stub
	}

	@Override
	public void processMessage(Message message) {	
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		Date data = new Date();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT l ");
		sql.append("FROM Lancamento l ");
		sql.append("WHERE l.id ");
		sql.append("IN (SELECT lc.id FROM Lancamento lc ");
		sql.append("JOIN l.produtoEdicao pe ");
		sql.append("JOIN pe.produto p ");
		sql.append("JOIN p.fornecedores f ");
		sql.append("WHERE l.dataRecolhimentoDistribuidor = :dataOperacao)");
		
		Query query = getSession().createQuery(sql.toString());

		query.setParameter("dataOperacao", distribuidor.getDataOperacao());
		

		
		try {

			@SuppressWarnings("unchecked")
			List<Lancamento> lancamentos = query.list();
			if (lancamentos.isEmpty()) {
				ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.RELACIONAMENTO, "Nenhum resultado encontrado para Data de Operação: "+ distribuidor.getDataOperacao());
			}
			
			PrintWriter print = new PrintWriter(new FileWriter(message.getHeader().get(MessageHeaderProperties.OUTBOUND_FOLDER.getValue())+"/"+sdf.format(data)+".drr"));	
			
			
			
			for (Lancamento lancamento : lancamentos){
				
				EMS0133Output output = new EMS0133Output();
				
				for(Fornecedor fornecedor : lancamento.getProdutoEdicao().getProduto().getFornecedores()){			
						output.setCodigoFornecedorProduto(fornecedor.getCodigoInterface());
						output.setCodigoProduto(lancamento.getProdutoEdicao().getProduto().getCodigo());
						output.setContextoProduto(lancamento.getProdutoEdicao().getProduto().getCodigoContexto());
						output.setDataGeracaoArquivo(data);
						output.setHoraGeracaoArquivo(data);
						output.setDataRecolhimento(lancamento.getDataRecolhimentoDistribuidor());
						output.setCodigoDistribuidor(distribuidor.getCodigo());
						
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
	public void posProcess() {
		// TODO Auto-generated method stub
	}
	
}
