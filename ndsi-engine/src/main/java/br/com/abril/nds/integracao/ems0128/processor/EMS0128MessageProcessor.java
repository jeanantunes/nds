package br.com.abril.nds.integracao.ems0128.processor;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.lightcouch.CouchDbClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0112Input;
import br.com.abril.nds.integracao.model.canonic.EMS0128Input;
import br.com.abril.nds.integracao.model.canonic.EMS0128InputItem;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoEditor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneEditor;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoTelefone;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.repository.impl.AbstractRepository;

@Component

public class EMS0128MessageProcessor extends AbstractRepository implements MessageProcessor  {



	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	private int itens=1;
	private EMS0128Input input;
	
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT me ");
		sql.append("FROM MovimentoEstoque me ");
		sql.append("JOIN FETCH me.tipoMovimento tm ");
		sql.append("JOIN FETCH me.produtoEdicao pe ");
		sql.append("JOIN FETCH pe.produto pr ");
		sql.append("WHERE tm.grupoMovimentoEstoque in (:grupoMovimentoEstoque) ");
		
		Query query = getSession().createQuery(sql.toString());
/*		
		query.setParameterList("grupoMovimentoEstoque", (new GrupoMovimentoEstoque[]{ 
				GrupoMovimentoEstoque.ENVIO_JORNALEIRO
				, GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE
		}) );
*/
		query.setParameterList("grupoMovimentoEstoque", (new GrupoMovimentoEstoque[]{ 
				GrupoMovimentoEstoque.SOBRA_EM
				, GrupoMovimentoEstoque.SOBRA_DE
				, GrupoMovimentoEstoque.FALTA_EM
				, GrupoMovimentoEstoque.FALTA_DE
		}) );

		tempVar.set( query.list() );		
		
		input = new EMS0128Input();
		
		input.setCodigoDistribuidor(distribuidorService.obter().getCodigoDistribuidorDinap());
		input.setDataSolicitacao(new Date());
		input.setFormaSolicitacao("CARGA-NDISTRIB");
		input.setSituacaoSolicitacao("SOLICITADO");
			
	}

	@Override
	public void processMessage(Message message) {
		
		EMS0128InputItem item = new EMS0128InputItem();
		
		MovimentoEstoque me = (MovimentoEstoque)message.getBody();
		
		
		item.setNumSequenciaDetalhe(itens++);
		
		GrupoMovimentoEstoque gme = ((TipoMovimentoEstoque)(me).getTipoMovimento()).getGrupoMovimentoEstoque();
		
		if ( gme.equals( GrupoMovimentoEstoque.FALTA_EM ) ) {
			item.setTipoAcerto( 3 );
		} else if ( gme.equals( GrupoMovimentoEstoque.FALTA_DE ) ) {			
			item.setTipoAcerto( 4 );
		} else if ( gme.equals( GrupoMovimentoEstoque.SOBRA_EM ) ) {
			item.setTipoAcerto( 5 );
		} else if ( gme.equals( GrupoMovimentoEstoque.SOBRA_DE ) ) {
			item.setTipoAcerto( 6 );
		}		
		
		item.setCodigoProduto(me.getProdutoEdicao().getProduto().getCodigo());
		item.setNumeroEdicao(me.getProdutoEdicao().getNumeroEdicao());
		item.setQtd(me.getQtde());
		item.setPrecoCapa(me.getProdutoEdicao().getPrecoVenda());
		if (null != me.getProdutoEdicao().getProduto().getDescontoLogistica()) {
			item.setPercentualDesconto(me.getProdutoEdicao().getProduto().getDescontoLogistica().getPercentualDesconto());
		}
		item.setSituacaoAcerto("SOLICITADO");
		
		input.getItems().add(item);
		
	}

	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
		if (!input.getItem().isEmpty()) {
			CouchDbClient cdbc = this.getCouchDBClient(input.getCodigoDistribuidor());		
			cdbc.save(input);
			cdbc.shutdown();
		}		
	}
	
	
	
}
