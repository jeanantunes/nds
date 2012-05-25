package br.com.abril.nds.integracao.ems0124.processor;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0124.outbound.EMS0124Detalhe;
import br.com.abril.nds.integracao.ems0124.outbound.EMS0124Header;
import br.com.abril.nds.integracao.ems0124.outbound.EMS0124Trailer;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;


@Component
public class EMS0124MessageProcessor implements MessageProcessor{

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private FixedFormatManager fixedFormatManager; 
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	public EMS0124MessageProcessor() {

	}
	
	@Override
	public void processMessage(Message message) {
	
		StringBuilder sql = new  StringBuilder();
		sql.append("SELECT mec ");
		sql.append("FROM MovimentoEstoqueCota mec ");
		sql.append("JOIN FETCH mec.cota c ");
		sql.append("JOIN FETCH mec.produtoEdicao pe ");
		sql.append("JOIN FETCH pe.produto p ");
		sql.append("JOIN FETCH p.fornecedores fs ");
		sql.append("WHERE mec.data = :dataOperacao ");
		sql.append("AND mec.tipoMovimento.grupoMovimentoEstoque = :nivelamentoEntrada ");
		sql.append("OR mec.tipoMovimento.grupoMovimentoEstoque = :nivelamentoSaida");

		
		
	
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("dataOperacao", distribuidorService.findDistribuidor().getDataOperacao());
		//query.setParameter("nivelamentoEntrada", GrupoMovimentoEstoque.NIVELAMENTO_ENTRADA);
		//query.setParameter("nivelamentoSaida", GrupoMovimentoEstoque.NIVELAMENTO_SAIDA);
		
		@SuppressWarnings("unchecked")
		List<MovimentoEstoqueCota> mecs = (List<MovimentoEstoqueCota>) query.getResultList();

		Date data = new Date();
		
		try {
			
			PrintWriter print = new PrintWriter(new FileWriter(message.getHeader().get("NDSI_EMS0124_OUTBOUND")+"/NIVELTO.NEW"));	
			
			EMS0124Header outheader = new EMS0124Header();
			
			outheader.setDataMovimento(data);
			outheader.setHoraMovimento(data);
			outheader.setQtdeRegistrosDetalhe(mecs.size());

			print.println(fixedFormatManager.export(outheader));
			
			
			for (MovimentoEstoqueCota mec : mecs){
				
				EMS0124Detalhe outdetalhe = new EMS0124Detalhe();
				
				outdetalhe.setCodigoCota(mec.getCota().getNumeroCota());
				outdetalhe.setCodigoFornecedorProduto(mec.getProdutoEdicao().getProduto().getFornecedores().iterator().next().getCodigoInterface());
			    outdetalhe.setContextoProduto(mec.getProdutoEdicao().getProduto().getCodigoContexto());
				outdetalhe.setCodPublicacao(mec.getProdutoEdicao().getProduto().getCodigo());
				outdetalhe.setEdicao(mec.getProdutoEdicao().getNumeroEdicao());
				outdetalhe.setPrecoCapa(mec.getProdutoEdicao().getPrecoVenda());
				outdetalhe.setQuantidadeNivelamento(mec.getQtde());
				//outdetalhe.setTipoNivelamento();
				outdetalhe.setDataLancamento(mec.getData());
				
				 
				print.println(fixedFormatManager.export(outdetalhe));

			}
			EMS0124Trailer outtrailer = new EMS0124Trailer();
			print.println(fixedFormatManager.export(outtrailer));
			print.flush();
			print.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		
		
	}

}
