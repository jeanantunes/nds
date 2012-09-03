package br.com.abril.nds.integracao.ems0135.processor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0135Input;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.impl.AbstractRepository;

@Component
public class EMS0135MessageProcessor extends AbstractRepository implements MessageProcessor {
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	
	@Override
	public void preProcess() {
		// TODO Auto-generated method stub
	}

	@Override
	public void preProcess(Message message) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void processMessage(Message message) {
		
		EMS0135Input input = (EMS0135Input) message.getBody();
		
		validarDistribuidor(message, input);
		
		ProdutoEdicao produtoEdicao = obterProdutoEdicao(input);
		
		if(produtoEdicao == null){			
			
			this.ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.RELACIONAMENTO, "Produto não encontrado.");

			throw new RuntimeException("Produto não encontrado.");
		}
		
		NotaFiscalEntrada notafiscalEntrada = obterNotaFiscal(input.getNotaFiscal());
		
		if(notafiscalEntrada == null){
			salvarNotaFiscalEntrada(input, produtoEdicao);			
		}else{
			atualizarNotaFiscalEntrada(notafiscalEntrada, input, produtoEdicao);
		}
		
		
	}

	private void salvarNotaFiscalEntrada(EMS0135Input input, ProdutoEdicao produtoEdicao) {
		
		
		NotaFiscalEntradaFornecedor notaFiscalEntradaFornecedor = new NotaFiscalEntradaFornecedor();
		
		notaFiscalEntradaFornecedor.setDataEmissao(input.getDataEmissao());		
		notaFiscalEntradaFornecedor.setNumero(input.getNotaFiscal().longValue());
		notaFiscalEntradaFornecedor.setSerie(input.getSerieNotaFiscal().toString());		
		notaFiscalEntradaFornecedor.setDataExpedicao(input.getDataLancamento());		
		notaFiscalEntradaFornecedor.setChaveAcesso(input.getChaveAcessoNF());		
		notaFiscalEntradaFornecedor.setCfop(obterCFOP());
		notaFiscalEntradaFornecedor.setOrigem(Origem.INTERFACE);
		notaFiscalEntradaFornecedor.setStatusNotaFiscal(StatusNotaFiscalEntrada.NAO_RECEBIDA);
		
		BigDecimal valorBruto = new BigDecimal(input.getPreco() * input.getQtdExemplar());
		notaFiscalEntradaFornecedor.setValorBruto(valorBruto);
		
		double desconto = (input.getDesconto() / 100);
		Double valorLiquido =  (valorBruto.doubleValue()  + (desconto * input.getPreco())) * input.getQtdExemplar();
		
		notaFiscalEntradaFornecedor.setValorLiquido(new BigDecimal(valorLiquido));		
		notaFiscalEntradaFornecedor.setValorDesconto(new BigDecimal(input.getDesconto()));		
		notaFiscalEntradaFornecedor.setEmitente(obterPessoaJuridica("10000000000100"));		
		notaFiscalEntradaFornecedor.setTipoNotaFiscal(obterTipoNotaFiscal(GrupoNotaFiscal.RECEBIMENTO_MERCADORIAS));		
		notaFiscalEntradaFornecedor.setEmitida(true);	
		
		//Implementado pela regra assistida pelo Sr. Eduardo "Punk Rock" Castro em 03/09 devido a informação necessaria na consulta de Notas
		
		notaFiscalEntradaFornecedor.setFornecedor(produtoEdicao.getProduto().getFornecedor());
		
		this.getSession().persist(notaFiscalEntradaFornecedor);		
		salvarItemNota(notaFiscalEntradaFornecedor, input, produtoEdicao);
	}
	
	private void atualizarNotaFiscalEntrada(NotaFiscalEntrada notafiscalEntrada, EMS0135Input input, ProdutoEdicao produtoEdicao) {
		BigDecimal valorBruto = new BigDecimal((input.getPreco() * input.getQtdExemplar()) + notafiscalEntrada.getValorBruto().floatValue() );
		notafiscalEntrada.setValorBruto(valorBruto);
		
		double desconto = (input.getDesconto() / 100);
		Double valorLiquido =  (valorBruto.doubleValue()  + (desconto * input.getPreco())) * input.getQtdExemplar();		
		notafiscalEntrada.setValorLiquido(new BigDecimal(valorLiquido));
		
		this.getSession().update(notafiscalEntrada);
		
		salvarItemNota((NotaFiscalEntradaFornecedor)notafiscalEntrada, input, produtoEdicao);
		
	}

	private void salvarItemNota(NotaFiscalEntradaFornecedor notaFiscalEntradaFornecedor, EMS0135Input input, ProdutoEdicao produtoEdicao) {
		
		ItemNotaFiscalEntrada item = new ItemNotaFiscalEntrada();
		item.setQtde(new BigInteger(input.getQtdExemplar().toString()));
		item.setNotaFiscal(notaFiscalEntradaFornecedor);
		item.setProdutoEdicao(produtoEdicao);
		Lancamento lancamento = obterLancamentoProdutoEdicao(produtoEdicao.getId());
		item.setDataLancamento(lancamento.getDataLancamentoPrevista());
		item.setDataRecolhimento(lancamento.getDataRecolhimentoPrevista());
		
		this.getSession().persist(item);
		
	}
	
	private Lancamento obterLancamentoProdutoEdicao(Long idProdutoEdicao) {
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select lancamento ")
		   .append(" from Lancamento lancamento ")
		   .append(" where lancamento.dataLancamentoDistribuidor = ")
		   .append(" (select max(lancamentoMaxDate.dataLancamentoDistribuidor) ")
		   .append(" from Lancamento lancamentoMaxDate where lancamentoMaxDate.produtoEdicao.id=:idProdutoEdicao ) ")
		   .append(" and lancamento.produtoEdicao.id=:idProdutoEdicao ");
		
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		Object lancamento = query.uniqueResult();
		
		return (lancamento!=null) ? (Lancamento) lancamento : null ;
		
	}

	private NotaFiscalEntrada obterNotaFiscal(Long numero) {
		String hql = "from NotaFiscalEntrada nf where nf.numero = :numero ";
		Query query = super.getSession().createQuery(hql);
		query.setParameter("numero", numero);
		return (NotaFiscalEntrada) query.uniqueResult();
		
	}

	private ProdutoEdicao obterProdutoEdicao(EMS0135Input input) {
		
		String hql = "from ProdutoEdicao produtoEdicao " 
				   + " join fetch produtoEdicao.produto " 
				   + " where produtoEdicao.produto.codigo = :codigoProduto "
				   + " and 	 produtoEdicao.numeroEdicao   = :numeroEdicao";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("codigoProduto", input.getCodigoProduto().toString());
		query.setParameter("numeroEdicao", new Long(input.getEdicao()));
		
		query.setMaxResults(1);
		
		return (ProdutoEdicao) query.uniqueResult();
		
	}

	private TipoNotaFiscal obterTipoNotaFiscal(GrupoNotaFiscal grupoNotaFiscal) {
		
		String hql = " from TipoNotaFiscal tipoNotaFiscal where tipoNotaFiscal.grupoNotaFiscal = :grupoNotaFiscal group by tipoNotaFiscal.id  ";
		
		Query query = getSession().createQuery(hql);
		
		query.setParameter("grupoNotaFiscal", grupoNotaFiscal);
		
		return (TipoNotaFiscal) query.uniqueResult();
	}

	private PessoaJuridica obterPessoaJuridica(String cnpj) {

		String hql = "from PessoaJuridica where cnpj = :cnpj";

		Query query = getSession().createQuery(hql);

		query.setParameter("cnpj", cnpj);

		return (PessoaJuridica) query.uniqueResult();
	}

	private CFOP obterCFOP() {
		 
		Criteria criteria =  getSession().createCriteria(CFOP.class);	

		criteria.add(Restrictions.eq("codigo", "5102"));
	
		return (CFOP) criteria.uniqueResult();
	}

	private void validarDistribuidor(Message message, EMS0135Input input) {
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		if(!distribuidor.getCodigoDistribuidorDinap().equals(input.getDistribuidor().toString())){			
		
			this.ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.RELACIONAMENTO, "Código do distribuidor do arquivo não é o mesmo do arquivo.");

			throw new RuntimeException("Distribuidor incorreto.");
		}
		
	}

	@Override
	public void posProcess() {
		// TODO Auto-generated method stub
	}

	@Override
	public void posProcess(Message message) {
		// TODO Auto-generated method stub
	}
	
}
