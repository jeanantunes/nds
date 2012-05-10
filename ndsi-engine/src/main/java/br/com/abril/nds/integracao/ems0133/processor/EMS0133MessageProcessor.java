package br.com.abril.nds.integracao.ems0133.processor;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0133.outbound.EMS0133Output;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.model.planejamento.Lancamento;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

@Component
public class EMS0133MessageProcessor implements MessageProcessor{

	@PersistenceContext
	private EntityManager entityManager;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
	
	@Autowired
	private FixedFormatManager fixedFormatManager; 
	
	public EMS0133MessageProcessor() {
		
	}

	@Override
	public void processMessage(Message message) {	
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT l ");
		sql.append("FROM Lancamento l ");
		sql.append("JOIN FETCH l.produtoEdicao pe ");
		sql.append("JOIN FETCH pe.produto p ");
		//sql.append("JOIN FETCH p.fornecedores f");
		//sql.append("JOIN FETCH f.distribuicaoFornecedor df ");
		//sql.append("JOIN FETCH df.distribuidor d");
		
		//StringBuilder sqlDistribuidor = new StringBuilder();
		//sqlDistribuidor.append("SELECT df ");
		//sqlDistribuidor.append("FROM DistribuicaoFornecedor df JOIN FETCH df.distribuidor d");
		
		
		
		Query queryLancamento = entityManager.createQuery(sql.toString());
		
		
		//Query queryDistribuidor = entityManager.createQuery(sqlDistribuidor.toString());
		//queryDistribuidor.setParameter("fornecedor
		//Distribuidor distribuidor =  (Distribuidor) queryDistribuidor.getSingleResult();
		
		
		Date data = new Date();
		
		@SuppressWarnings("unchecked")
		List<Lancamento> lancamentos = queryLancamento.getResultList();
	
		//@SuppressWarnings("unchecked")
		//List<DistribuicaoFornecedor> dfs = queryDistribuidor.getResultList();
		try {
			
			PrintWriter print = new PrintWriter(new FileWriter(message.getHeader().get("NDSI_EMS0133_OUTBOUND")+"/"+sdf.format(data)+".drr"));	
			
			for (Lancamento lancamento : lancamentos){
				
				EMS0133Output output = new EMS0133Output();
				
						
				output.setCodigoFornecedorProduto("0000001");
				output.setCodigoProduto(lancamento.getProdutoEdicao().getProduto().getCodigo());
				output.setContextoProduto("1");
				output.setDataGeracaoArquivo(data);
				output.setHoraGeracaoArquivo(data);
				output.setDataRecolhimento(lancamento.getDataRecolhimentoDistribuidor());
				output.setCodigoDistribuidor("1234567");
				
				/*for (DistribuicaoFornecedor df : dfs){
					if(df.getFornecedor().getId() == lancamento.getProdutoEdicao().getProduto().getFornecedor().getId()){}
						//output.setCodigoDistribuidor(df.getDistribuidor().getCodigo());

				}*/
				print.println(fixedFormatManager.export(output));
			}
			
			print.flush();
			print.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
