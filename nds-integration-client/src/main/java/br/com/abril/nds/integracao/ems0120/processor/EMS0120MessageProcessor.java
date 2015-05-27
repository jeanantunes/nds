package br.com.abril.nds.integracao.ems0120.processor;

import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;


@Component
public class EMS0120MessageProcessor extends AbstractRepository implements MessageProcessor {

	
	@Autowired
	private FixedFormatManager fixedFormatManager; 
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	//Autowired
	//private DistribuidorService distribuidorService;
	
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
		
	}
		
	@Override
	@SuppressWarnings("unchecked")
	public void processMessage(Message message) {
	
		/*
		Date dataOperacao = distribuidorService.obter().getDataOperacao();
		
		StringBuilder sql = new  StringBuilder();
		sql.append("SELECT mec ");
		sql.append("FROM MovimentoEstoqueCota mec ");		
		sql.append("JOIN FETCH mec.lancamento lan ");
		sql.append("JOIN FETCH mec.cota c ");
		sql.append("JOIN FETCH mec.produtoEdicao pe ");
		sql.append("JOIN FETCH pe.produto p ");
		sql.append("JOIN FETCH p.fornecedores fs ");
		sql.append("WHERE lan.dataLancamentoDistribuidor = :dataOperacao ");
		sql.append("AND mec.tipoMovimento.grupoMovimentoEstoque = :recebimentoReparte ");
		sql.append("ORDER BY c.numeroCota ");
	
		Query query = this.getSession().createQuery(sql.toString());		
		query.setParameter("dataOperacao", dataOperacao);
		query.setParameter("recebimentoReparte", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		
		List<MovimentoEstoqueCota> mecs = (List<MovimentoEstoqueCota>) query.list();

		Date data = new Date();
		
		try {
			
			PrintWriter print = new PrintWriter(new FileWriter(message.getHeader().get(MessageHeaderProperties.OUTBOUND_FOLDER.getValue())+"/REPARTE.NEP"));	
			
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
				outdetalhe.setNumeroBoxCota(mec.getCota().getBox().getCodigo());
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
			message.getHeader().put(MessageHeaderProperties.FILE_NAME.getValue(), "REPARTE.NEW" );
			ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.GERACAO_DE_ARQUIVO, "Não foi possível gerar o arquivo");
		}
				
		
		*/
	}

	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}
