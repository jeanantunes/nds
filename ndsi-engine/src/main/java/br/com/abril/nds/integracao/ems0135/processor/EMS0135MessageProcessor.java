package br.com.abril.nds.integracao.ems0135.processor;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
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
	public void processMessage(Message message) {
		
		EMS0135Input input = (EMS0135Input) message.getBody();
		if (input == null) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.ERRO_INFRA, "NAO ENCONTROU o Arquivo");
			return;
		}
		
		
		// Validar código do distribuidor:
		Distribuidor distribuidor = this.distribuidorService.obter();
		if(!distribuidor.getCodigoDistribuidorDinap().equals(
				input.getDistribuidor().toString())){			
			this.ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.RELACIONAMENTO, 
					"Código do distribuidor do arquivo não é o mesmo do arquivo.");
			return;
		}
		
		
		// Validar Produto/Edicao
		final Integer codigoProduto = input.getCodigoProduto();
		final Integer edicao = input.getEdicao();
		ProdutoEdicao produtoEdicao = this.obterProdutoEdicao(codigoProduto,
				edicao);
		if (produtoEdicao == null) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"Impossivel realizar Insert/update - Nenhum resultado encontrado para Produto: "
							+ codigoProduto + " e Edicao: " + edicao
							+ " na tabela produto_edicao");
			return;
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

	/**
	 * Obtém o Produto Edição cadastrado previamente.
	 * 
	 * @param codigoPublicacao Código da Publicação.
	 * @param edicao Número da Edição.
	 * 
	 * @return
	 */
	private ProdutoEdicao obterProdutoEdicao(Integer codigoPublicacao,
			Integer edicao) {

		try {

			Criteria criteria = this.getSession().createCriteria(
					ProdutoEdicao.class, "produtoEdicao");

			criteria.createAlias("produtoEdicao.produto", "produto");
			criteria.setFetchMode("produto", FetchMode.JOIN);

			criteria.add(Restrictions.eq("produto.codigo", codigoPublicacao));
			criteria.add(Restrictions.eq("produtoEdicao.numeroEdicao", edicao));

			return (ProdutoEdicao) criteria.uniqueResult();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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


	@Override
	public void posProcess() {
		// TODO Auto-generated method stub
	}
	
}
