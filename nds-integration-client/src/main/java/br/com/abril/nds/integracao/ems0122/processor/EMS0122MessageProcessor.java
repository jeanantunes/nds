package br.com.abril.nds.integracao.ems0122.processor;

//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.Date;
//import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

//import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
//import br.com.abril.nds.integracao.ems0122.outbound.EMS0122Detalhe;
//import br.com.abril.nds.integracao.ems0122.outbound.EMS0122Header;
//import br.com.abril.nds.integracao.ems0122.outbound.EMS0122Trailer;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
//import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
//import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;


@Component
public class EMS0122MessageProcessor extends AbstractRepository implements MessageProcessor {

	
	//Autowired
	//private FixedFormatManager fixedFormatManager;
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	//Autowired
	//private DistribuidorService distribuidorService;
	
	public EMS0122MessageProcessor() {

	}
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void processMessage(Message message) {
		
		/*
	
		StringBuilder sql = new  StringBuilder();
		sql.append("SELECT mec ");
		sql.append("FROM MovimentoEstoqueCota mec ");
		sql.append("JOIN FETCH mec.cota c ");
		sql.append("JOIN FETCH mec.produtoEdicao pe ");
		sql.append("JOIN FETCH pe.produto p ");
		sql.append("JOIN FETCH p.fornecedores fs ");
		sql.append("WHERE mec.data = :dataOperacao ");
		sql.append("AND mec.tipoMovimento.grupoMovimentoEstoque = :compraSuplementar ");

		
		
	
		Query query = getSession().createQuery(sql.toString());
		query.setParameter("dataOperacao", distribuidorService.obter().getDataOperacao());
		query.setParameter("compraSuplementar", GrupoMovimentoEstoque.COMPRA_SUPLEMENTAR);
		
		@SuppressWarnings("unchecked")
		List<MovimentoEstoqueCota> mecs = (List<MovimentoEstoqueCota>) query.list();

		Date data = new Date();
		
		try {
			
			PrintWriter print = new PrintWriter(new FileWriter(message.getHeader().get(MessageHeaderProperties.OUTBOUND_FOLDER.getValue())+"/SUPLEMEN.NEP"));	
			
			EMS0122Header outheader = new EMS0122Header();
			
			outheader.setDataMovimento(data);
			outheader.setHoraMovimento(data);
			outheader.setQtdeRegistrosDetalhe(mecs.size());

			print.println(fixedFormatManager.export(outheader));
			
			
			for (MovimentoEstoqueCota mec : mecs){
				
				EMS0122Detalhe outdetalhe = new EMS0122Detalhe();
				
				outdetalhe.setCodigoCota(mec.getCota().getNumeroCota());
				outdetalhe.setCodigoFornecedorProduto(mec.getProdutoEdicao().getProduto().getFornecedores().iterator().next().getCodigoInterface());
			    outdetalhe.setContextoProduto(mec.getProdutoEdicao().getProduto().getCodigoContexto());
				outdetalhe.setCodPublicacao(mec.getProdutoEdicao().getProduto().getCodigo());
				outdetalhe.setEdicao(mec.getProdutoEdicao().getNumeroEdicao());
				outdetalhe.setQuantidadeSuplementar( Long.valueOf( mec.getQtde().toString() ) );
				outdetalhe.setPrecoCapa(mec.getProdutoEdicao().getPrecoVenda());
				
				 
				print.println(fixedFormatManager.export(outdetalhe));

			}
			EMS0122Trailer outtrailer = new EMS0122Trailer();
			print.println(fixedFormatManager.export(outtrailer));
			print.flush();
			print.close();
			
		} catch (IOException e) {
			ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.GERACAO_DE_ARQUIVO, "Não foi possível gerar o arquivo");
		}
				
		*/
		
	}

	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}
