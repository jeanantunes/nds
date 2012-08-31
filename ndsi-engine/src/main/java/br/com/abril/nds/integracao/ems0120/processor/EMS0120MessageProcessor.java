package br.com.abril.nds.integracao.ems0120.processor;

import java.io.FileWriter; 
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.ems0120.outbound.EMS0120Detalhe;
import br.com.abril.nds.integracao.ems0120.outbound.EMS0120Header;
import br.com.abril.nds.integracao.ems0120.outbound.EMS0120Trailer;
import br.com.abril.nds.integracao.engine.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.repository.impl.AbstractRepository;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;


@Component
public class EMS0120MessageProcessor extends AbstractRepository implements MessageProcessor {

	
	@Autowired
	private FixedFormatManager fixedFormatManager; 
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	
	@Override	
	@SuppressWarnings("unchecked")
	public void processMessage(Message message) {
	
		Date dataOperacao = distribuidorService.obter().getDataOperacao();
		
		StringBuilder sql = new  StringBuilder();
		sql.append("SELECT mec ");
		sql.append("FROM MovimentoEstoqueCota mec ");
		sql.append("JOIN FETCH mec.cota c ");
		sql.append("JOIN FETCH mec.produtoEdicao pe ");
		sql.append("JOIN FETCH pe.produto p ");
		sql.append("JOIN FETCH p.fornecedores fs ");
		sql.append("WHERE mec.data = :dataOperacao ");
		sql.append("AND mec.tipoMovimento.grupoMovimentoEstoque = :recebimentoReparte ");
	
		Query query = this.getSession().createQuery(sql.toString());
		query.setParameter("dataOperacao", dataOperacao);
		query.setParameter("recebimentoReparte", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		
		List<MovimentoEstoqueCota> mecs = (List<MovimentoEstoqueCota>) query.list();

		Date data = new Date();
		
		try {
			
			PrintWriter print = new PrintWriter(new FileWriter(message.getHeader().get(MessageHeaderProperties.OUTBOUND_FOLDER.getValue())+"/REPARTE.NEW"));	
			
			EMS0120Header outheader = new EMS0120Header();
			
			outheader.setDataMovimento(data);
			outheader.setHoraMovimento(data);
			outheader.setQtdeRegistrosDetalhe(mecs.size());

			print.println(fixedFormatManager.export(outheader));
			
			
			for (MovimentoEstoqueCota mec : mecs){
				
				EMS0120Detalhe outdetalhe = new EMS0120Detalhe();
				
				outdetalhe.setCodigoCota(mec.getCota().getNumeroCota());
				outdetalhe.setCodigoFornecedorProduto(mec.getProdutoEdicao().getProduto().getFornecedores().iterator().next().getCodigoInterface());
			    outdetalhe.setContextoProduto(mec.getProdutoEdicao().getProduto().getCodigoContexto());//cod_publ
				outdetalhe.setCodPublicacao(mec.getProdutoEdicao().getProduto().getCodigo());
				outdetalhe.setEdicao(mec.getProdutoEdicao().getNumeroEdicao());
				outdetalhe.setNumeroBoxCota(mec.getCota().getBox().getCodigo() + " - "+mec.getCota().getBox().getNome());
				outdetalhe.setPrecoCapa(mec.getProdutoEdicao().getPrecoVenda());
				outdetalhe.setQuantidadeReparte(Long.valueOf( mec.getQtde().toString() ));
				outdetalhe.setDataLancamento(mec.getData());
				
				 
				print.println(fixedFormatManager.export(outdetalhe));

			}
			EMS0120Trailer outtrailer = new EMS0120Trailer();
			print.println(fixedFormatManager.export(outtrailer));
			print.flush();
			print.close();
			
		} catch (IOException e) {
			ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.GERACAO_DE_ARQUIVO, "Não foi possível gerar o arquivo");
		}
				
		
		
	}

}
