package br.com.abril.nds.integracao.ems0110.processor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0109Input;
import br.com.abril.nds.integracao.model.canonic.EMS0110Input;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Brinde;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DescontoLogistica;
import br.com.abril.nds.model.cadastro.Dimensao;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.cadastro.desconto.DescontoProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.repository.DescontoProdutoEdicaoRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.repository.impl.AbstractRepository;
import br.com.abril.nds.service.DescontoService;

@Component
public class EMS0110MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private DescontoProdutoEdicaoRepository descontoProdutoEdicaoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private FornecedorRepository fornecedorRepository;
	
	@Autowired
	private DescontoService descontoService;
	
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void processMessage(Message message) {

		if (verificarDistribuidor(message)) {

			ProdutoEdicao edicao = this.findProdutoEdicao(message);

			if (edicao == null) {

				Produto produto = this.findProduto(message);
				
				if (produto == null) {
					produto = this.criarProdutoComInputDeEdicao(message);
				}

				this.criarProdutoEdicaoConformeInput(produto, message);

			} else {

				this.atualizaProdutoEdicaoConformeInput(edicao, message);
			}

		} else {

			this.ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"Distribuidor nao encontrato.");
			throw new RuntimeException("Distribuidor nao encontrado.");
		}
	}
	
	private DescontoLogistica findDescontoLogisticaByTipoDesconto(
			Integer codigoTipoDesconto) {
		StringBuilder sql = new StringBuilder();

		sql.append("select d from DescontoLogistica d ");
		sql.append(" where d.tipoDesconto = :codigoTipoDesconto ");

		Query query = this.getSession().createQuery(sql.toString());

		query.setParameter("codigoTipoDesconto", codigoTipoDesconto);

		return (DescontoLogistica) query.uniqueResult();

	}

	private Fornecedor findFornecedor(Integer codigoInterface) {
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT f FROM Fornecedor f ");
		sql.append("WHERE  f.codigoInterface = :codigoInterface ");

		Query query = this.getSession().createQuery(sql.toString());

		query.setParameter("codigoInterface", codigoInterface);

		return (Fornecedor) query.uniqueResult();

	}
	
	private Editor findEditorByID(Long codigoEditor) {

		StringBuilder sql = new StringBuilder();

		sql.append("select editor from Editor editor ");
		sql.append(" where editor.codigo = :codigoEditor ");

		Query query = this.getSession().createQuery(sql.toString());

		query.setParameter("codigoEditor", codigoEditor);

		return (Editor) query.uniqueResult();		
	}
	
	private TipoProduto findTipoProduto(Long codigoCategoria) {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT tp FROM TipoProduto tp ");
		sql.append("WHERE  tp.codigo = :codigo ");

		Query query = this.getSession().createQuery(sql.toString());
		query.setParameter("codigo", codigoCategoria);

		@SuppressWarnings("unchecked")
		List<TipoProduto> tiposProduto = (List<TipoProduto>) query.list();

		TipoProduto tipoProduto = null;

		if (!tiposProduto.isEmpty()) {

			tipoProduto = tiposProduto.get(0);

			

		} 
		return tipoProduto;
	}
	
	/**
	 * Retorna o enum TributacaoFiscal (codigoSituacaoTributaria) baseado na posição 220 retornada na EMS0109Input.java
	 * @return
	 */
	private TributacaoFiscal getTributacaoFiscal(String codigoSituacaoTributaria) {
		if ("A".equalsIgnoreCase(codigoSituacaoTributaria)) {
			return TributacaoFiscal.TRIBUTADO;
		} else if ("B".equalsIgnoreCase(codigoSituacaoTributaria)) {
			return TributacaoFiscal.ISENTO;
		} else {
			return TributacaoFiscal.OUTROS;
		}
	}
	
	private Produto criarProdutoComInputDeEdicao(Message message) {
		EMS0110Input input = (EMS0110Input) message.getBody();

		Produto produto = new Produto();

		Fornecedor fornecedor = this
				.findFornecedor(input.getCodFornecPublicacao());
		DescontoLogistica descontoLogistica = this
				.findDescontoLogisticaByTipoDesconto( Integer.parseInt( input.getTipoDesconto()) );
		
		Editor editor = this.findEditorByID(input.getCodEditor());

		if (null == editor) {
			this.ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.SEM_DOMINIO,
					"Editor " + input.getCodEditor() + " nao encontrado.");

			throw new RuntimeException("Editor " + input.getCodEditor() + " nao encontrado.");
		}
		
		TipoProduto tipoProduto = this.findTipoProduto(input.getCodCategoria());

		if (null == tipoProduto) {
			this.ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.SEM_DOMINIO,
					"Tipo Produto REVISTA nao encontrado.");

			throw new RuntimeException("Tipo Produto nao encontrado.");
		}
						
		produto.setTipoProduto(tipoProduto);
		produto.setNome(input.getNomeProd());
		produto.setCodigoContexto(input.getContextoPublicacao());
		produto.setNomeComercial(input.getNomeComercial());
		produto.setEditor(editor);
		produto.setPeriodicidade(PeriodicidadeProduto.INDEFINIDO); 	// default definito por Eduardo e Paulo em 13/12/2012
		produto.setSlogan("");										// default definito por Eduardo e Paulo em 13/12/2012
		produto.setPeb(input.getPeb());
		produto.setPeso(input.getPesoUni());
		produto.setPacotePadrao(input.getPactPadrao());
		produto.setCodigo(input.getCodProd());
		produto.setAtivo(input.getStatusProd());
		produto.setDataDesativacao(input.getDataDesativacao());
		produto.setFormaComercializacao(
				(input.getFormaComercializacao().equals("CON") 
						? FormaComercializacao.CONSIGNADO 
						: FormaComercializacao.CONTA_FIRME
				) 
		);

		String codigoSituacaoTributaria = input.getCodSitTributaria();
		produto.setTributacaoFiscal(this.getTributacaoFiscal(codigoSituacaoTributaria));

		produto.setOrigem(Origem.INTERFACE);
		
		if (fornecedor != null) {

			produto.addFornecedor(fornecedor);
		}

		if (descontoLogistica != null) {

			produto.setDescontoLogistica(descontoLogistica);

		}

		this.getSession().persist(produto);
		
		this.ndsiLoggerFactory.getLogger().logWarning(message,
				EventoExecucaoEnum.SEM_DOMINIO,
				"Produto "+ produto.getCodigo() +" Inserido com Periodicidade INDEFINIDA .");	
		return this.findProduto(message);
	}

	private boolean verificarDistribuidor(Message message) {
		EMS0110Input input = (EMS0110Input) message.getBody();

		Long codigoDistribuidorSistema = (Long) message.getHeader().get(
				MessageHeaderProperties.CODIGO_DISTRIBUIDOR.toString());
		Long codigoDistribuidorArquivo = Long.parseLong(input.getCodDistrib());

		if (codigoDistribuidorSistema.equals(codigoDistribuidorArquivo)) {

			return true;
		}

		return false;
	}

	private ProdutoEdicao findProdutoEdicao(Message message) {
		EMS0110Input input = (EMS0110Input) message.getBody();

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT pe FROM ProdutoEdicao pe JOIN FETCH pe.produto p ");
		sql.append("WHERE pe.numeroEdicao = :numeroEdicao ");
		sql.append(" AND  p.codigo = :codigo ");

		Query query = this.getSession().createQuery(sql.toString());

		query.setParameter("numeroEdicao", input.getEdicaoProd());
		query.setParameter("codigo", input.getCodProd());

		return (ProdutoEdicao) query.uniqueResult();
	}

	private Produto findProduto(Message message) {
		EMS0110Input input = (EMS0110Input) message.getBody();

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT p FROM Produto p ");
		sql.append("WHERE p.codigo = :codigo ");

		Query query = this.getSession().createQuery(sql.toString());

		query.setParameter("codigo", input.getCodProd());

		return (Produto) query.uniqueResult();
	}
	
	
	
	private void criarProdutoEdicaoConformeInput(Produto produto,
			Message message) {
		EMS0110Input input = (EMS0110Input) message.getBody();

		ProdutoEdicao edicao = new ProdutoEdicao();
		Dimensao dimensao = new Dimensao();
		Brinde brinde = new Brinde();

		if (!produto.getCodigoContexto().equals(input.getContextoProd())) {

			produto.setCodigoContexto(input.getContextoProd());
			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do Codigo Contexto Produto para: "
							+ input.getContextoProd());
		}
		if (!produto.getTipoProduto().getCodigoNBM().equals(input.getCodNBM())) {

			produto.getTipoProduto().setCodigoNBM(input.getCodNBM());
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do Codigo NBM para: " + input.getCodNBM());
		}

		if (!produto.getNomeComercial().equals(input.getNomeComercial())) {

			produto.setNomeComercial(input.getNomeComercial());
			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do Nome Comercial para: "
							+ input.getNomeComercial());
		}
		
		edicao.setProduto(produto);

		dimensao.setLargura(input.getLargura());
		dimensao.setComprimento(input.getComprimento());
		dimensao.setEspessura(input.getExpessura());
		edicao.setDimensao(dimensao);

		brinde.setDescricao(input.getDescBrinde());
		brinde.setPermiteVendaSeparada(input.getCondVendeSeparado());
		edicao.setBrinde(brinde);

		edicao.setCodigoDeBarras(input.getCodBarra());
		edicao.setNumeroEdicao(input.getEdicaoProd());
		edicao.setPacotePadrao(input.getPactPadrao());
		edicao.setPeb(input.getPeb());
		edicao.setPeso(input.getPesoUni());
		edicao.setPossuiBrinde(input.isContemBrinde());
		edicao.setDataDesativacao(input.getDataDesativacao());
		edicao.setChamadaCapa(input.getChamadaCapa());
		edicao.setOrigem(Origem.INTERFACE);
		edicao.setNomeComercial(input.getNomeComercial());

		this.getSession().persist(edicao);
		
		inserirDescontoProdutoEdicao(edicao, produto);
		
	}

	/**
	 * Insere os dados de desconto relativos ao produto edição em questão.
	 * 
	 * @param produtoEdicao
	 * @param indNovoProdutoEdicao
	 */
	private void inserirDescontoProdutoEdicao(ProdutoEdicao produtoEdicao, Produto produto) {
		
		GrupoProduto grupoProduto = produto.getTipoProduto().getGrupoProduto() ;
		
		if(GrupoProduto.OUTROS.equals(grupoProduto)) {
			return;
		}
		
		List<Fornecedor> fornecedores = fornecedorRepository.obterFornecedoresDeProduto(produto.getCodigo(), null);
		
		if(fornecedores == null || fornecedores.isEmpty()) {
			throw new IllegalStateException("Não há fornecedor associado ao produto.");
		}

		if(fornecedores.size()!=1) {
			throw new IllegalStateException("Mais de um fornecedor associado ao produto.");
		}
		
		Set<Fornecedor> conjuntoFornecedor = new HashSet<Fornecedor>();
		
		Fornecedor fornecedor = fornecedores.get(0);
		
		conjuntoFornecedor.add(fornecedor);
		
		Set<DescontoProdutoEdicao> conjuntoDescontoProdutoEdicaoEspecifico = 
				descontoProdutoEdicaoRepository.obterDescontoProdutoEdicao(TipoDesconto.ESPECIFICO, fornecedor, null);
		
		if(conjuntoDescontoProdutoEdicaoEspecifico!=null && !conjuntoDescontoProdutoEdicaoEspecifico.isEmpty()) {
			
			
			for(DescontoProdutoEdicao descontoEspecifico : conjuntoDescontoProdutoEdicaoEspecifico) {
				
				Cota cota = descontoEspecifico.getCota();
				
				descontoService.processarDescontoCota(cota, conjuntoFornecedor, descontoEspecifico.getDesconto());
				
			}
			
			
		}
		
		Set<DescontoProdutoEdicao> conjuntoDescontoProdutoEdicaoGeral = 
				descontoProdutoEdicaoRepository.obterDescontoProdutoEdicao(TipoDesconto.GERAL, fornecedor, null);
		
		if(conjuntoDescontoProdutoEdicaoGeral!=null && !conjuntoDescontoProdutoEdicaoGeral.isEmpty()) {
			
			
			for(DescontoProdutoEdicao descontoGeral : conjuntoDescontoProdutoEdicaoGeral) {
				
				descontoService.processarDescontoDistribuidor(conjuntoFornecedor, descontoGeral.getDesconto());
				
			}
			
			
		}
		
	}

	
	private void atualizaProdutoEdicaoConformeInput(ProdutoEdicao edicao,
			Message message) {
		EMS0110Input input = (EMS0110Input) message.getBody();

		edicao.setOrigem(Origem.INTERFACE);

		if (null != edicao.getProduto() && null != edicao.getProduto().getCodigoContexto() && !edicao.getProduto().getCodigoContexto()
				.equals(input.getContextoProd())) {

			edicao.getProduto().setCodigoContexto(input.getContextoProd());
			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do Codigo Contexto Produto para: "
							+ input.getContextoProd());
		}
		
		if (null != edicao.getProduto() && null != edicao.getProduto().getNomeComercial() && !edicao.getProduto().getNomeComercial()
				.equals(input.getNomeComercial())) {

			edicao.getProduto().setNomeComercial(input.getNomeComercial());
			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do nome Comercial do Produto para: "
							+ input.getNomeComercial());
		}
		
		if (null != edicao.getProduto() && null != edicao.getProduto().getTipoProduto() && null != edicao.getProduto().getTipoProduto().getCodigoNBM() && !edicao.getProduto().getTipoProduto().getCodigoNBM()
				.equals(input.getCodNBM())) {

			edicao.getProduto().getTipoProduto()
					.setCodigoNBM(input.getCodNBM());
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do Codigo NBM para: " + input.getCodNBM());
		}

		if (null != edicao.getNomeComercial() && !edicao.getNomeComercial().equals(input.getNomeComercial())) {

			edicao.setNomeComercial(input.getNomeComercial());
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao Nome Comercial para: " + input.getNomeComercial());
		}
		if (null != edicao.getDimensao() && null != edicao.getDimensao().getLargura() && !edicao.getDimensao().getLargura().equals(input.getLargura())) {

			edicao.getDimensao().setLargura(input.getLargura());
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao Largura para: " + input.getLargura());
		}
		if (null != edicao.getDimensao() && null != edicao.getDimensao().getComprimento() && !edicao.getDimensao().getComprimento()
				.equals(input.getComprimento())) {

			edicao.getDimensao().setComprimento(input.getComprimento());
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao Comprimento para: " + input.getComprimento());
		}
		if (null != edicao.getDimensao() && null != edicao.getDimensao().getEspessura() && !edicao.getDimensao().getEspessura().equals(input.getExpessura())) {

			edicao.getDimensao().setEspessura(input.getExpessura());
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao Espessura para: " + input.getExpessura());
		}

		if (null != edicao.getBrinde() && null != edicao.getBrinde().getPermiteVendaSeparada() && edicao.getBrinde().getPermiteVendaSeparada() != input
				.getCondVendeSeparado()) {

			edicao.getBrinde().setPermiteVendaSeparada(
					input.getCondVendeSeparado());
			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao Permite Venda Separada para: "
							+ input.getCondVendeSeparado());
		}
		if (null != edicao.getBrinde() && null != edicao.getBrinde().getDescricao() && !edicao.getBrinde().getDescricao().equals(input.getDescBrinde())) {

			edicao.getBrinde().setDescricao(input.getDescBrinde());
			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao Descricao Brinde para: "
							+ input.getDescBrinde());
		}

		if (null != edicao.getChamadaCapa() && !edicao.getChamadaCapa().equals(input.getChamadaCapa())) {

			edicao.setChamadaCapa(input.getChamadaCapa());
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao Chamada Capa para: " + input.getChamadaCapa());
		}
		if (edicao.isPossuiBrinde() != input.isContemBrinde()) {

			edicao.setPossuiBrinde(input.isContemBrinde());
			this.ndsiLoggerFactory.getLogger()
					.logInfo(
							message,
							EventoExecucaoEnum.INF_DADO_ALTERADO,
							"Atualizacao Possui Brinde para: "
									+ input.isContemBrinde());
		}
		if (null != edicao.getCodigoDeBarras() && !edicao.getCodigoDeBarras().equals(input.getCodBarra())) {

			edicao.setCodigoDeBarras(input.getCodBarra());
			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do Codigo de Barra para: "
							+ input.getCodBarra());
		}
		if (null != edicao.getDataDesativacao() && !edicao.getDataDesativacao().equals(input.getDataDesativacao())) {

			edicao.setDataDesativacao(input.getDataDesativacao());
			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao da Data de Desativacao para: "
							+ input.getDataDesativacao());
		}
		if (edicao.getPeb() != input.getPeb()) {

			edicao.setPeb(input.getPeb());
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do PEB para: " + input.getPeb());
		}
		if (edicao.getPacotePadrao() != input.getPactPadrao()) {

			edicao.setPacotePadrao(input.getPactPadrao());
			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do Pacote Padrao para: "
							+ input.getPactPadrao());
		}
		if (null != edicao.getPeso() && !edicao.getPeso().equals(input.getPesoUni())) {

			edicao.setPeso(input.getPesoUni());
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do Peso para: " + input.getPesoUni());
		}
		
		this.getSession().merge(edicao);
	}
	
	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}