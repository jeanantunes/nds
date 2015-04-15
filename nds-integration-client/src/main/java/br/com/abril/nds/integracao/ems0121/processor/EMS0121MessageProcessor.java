package br.com.abril.nds.integracao.ems0121.processor;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.integracao.ems0121.outbound.EMS0121Detalhe;
import br.com.abril.nds.integracao.ems0121.outbound.EMS0121Header;
import br.com.abril.nds.integracao.ems0121.outbound.EMS0121Trailer;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.service.integracao.DistribuidorService;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;


@Component

public class EMS0121MessageProcessor extends AbstractRepository implements MessageProcessor {
	
	@Autowired
	private FixedFormatManager fixedFormatManager; 
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	public EMS0121MessageProcessor() {

	}
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void processMessage(Message message) {
	
		StringBuilder sql = new  StringBuilder();
		sql.append("SELECT mec ");
		sql.append("FROM MovimentoEstoqueCota mec ");
		sql.append("JOIN FETCH mec.lancamentoProdutoEdicao lan ");
		sql.append("JOIN FETCH mec.cota c ");
		sql.append("JOIN FETCH mec.produtoEdicao pe ");
		sql.append("JOIN FETCH pe.produto p ");
		sql.append("JOIN FETCH p.fornecedores fs ");
		sql.append("WHERE lan.dataRecolhimentoDistribuidor = :dataOperacao ");
		sql.append("AND mec.tipoMovimento.grupoMovimentoEstoque = :envioEncalhe ");
		sql.append("ORDER BY c.numeroCota ");		
		
	
		Query query = getSession().createQuery(sql.toString());
		query.setParameter("dataOperacao", distribuidorService.obter().getDataOperacao());
		query.setParameter("envioEncalhe", GrupoMovimentoEstoque.ENVIO_ENCALHE);
		
		@SuppressWarnings("unchecked")
		List<MovimentoEstoqueCota> mecs = (List<MovimentoEstoqueCota>) query.list();

		Date data = new Date();
		
		try {
			
			PrintWriter print = new PrintWriter(new FileWriter(message.getHeader().get(MessageHeaderProperties.OUTBOUND_FOLDER.getValue())+"/ENCALHE.NEP"));	
			
			EMS0121Header outheader = new EMS0121Header();
			
			outheader.setDataMovimento(data);
			outheader.setHoraMovimento(data);
			outheader.setQtdeRegistrosDetalhe(mecs.size());

			print.println(fixedFormatManager.export(outheader));
			
			
			for (MovimentoEstoqueCota mec : mecs){
				
				EMS0121Detalhe outdetalhe = new EMS0121Detalhe();
				
				outdetalhe.setCodigoCota(mec.getCota().getNumeroCota());
				outdetalhe.setCodigoFornecedorProduto(mec.getProdutoEdicao().getProduto().getFornecedores().iterator().next().getCodigoInterface());
			    outdetalhe.setContextoProduto(mec.getProdutoEdicao().getProduto().getCodigoContexto());
				outdetalhe.setCodPublicacao(mec.getProdutoEdicao().getProduto().getCodigo());
				outdetalhe.setEdicao(mec.getProdutoEdicao().getNumeroEdicao());
				outdetalhe.setQuantidadeEncalhe(Long.valueOf(mec.getQtde().toString()));
				outdetalhe.setDataRecolhimento(mec.getData());
				
				 
				print.println(fixedFormatManager.export(outdetalhe));

			}
			EMS0121Trailer outtrailer = new EMS0121Trailer();
			print.println(fixedFormatManager.export(outtrailer));
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
