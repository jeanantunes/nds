package br.com.abril.nds.service.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.FiltroValeDescontoVO;
import br.com.abril.nds.client.vo.ValeDescontoVO;
import br.com.abril.nds.client.vo.ValeDescontoVO.PublicacaoCuponada;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.ValeDesconto;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.repository.TipoProdutoRepository;
import br.com.abril.nds.repository.ValeDescontoRepository;
import br.com.abril.nds.service.ValeDescontoService;

@Service
public class ValeDescontoServiceImpl implements ValeDescontoService {
	
	@Autowired
	private ValeDescontoRepository valeDescontoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired 
	private ProdutoEdicaoRepository produtoEdicaoRepository;	
	
	@Autowired 
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private TipoProdutoRepository tipoProdutoRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void salvar(ValeDesconto valeDesconto) {

		valeDesconto.setProduto(this.getProdutoParaValeDesconto(valeDesconto.getProduto()));

		valeDesconto.setProdutosAplicacao(this.getProdutosAplicacaoParaValeDesconto(valeDesconto.getProdutosAplicacao()));

		this.valeDescontoRepository.merge(valeDesconto);

		this.insertLancamentosParaValeDesconto(valeDesconto.getLancamentos(), valeDesconto);
	}

	private Produto getProdutoParaValeDesconto(Produto produto) {
		
		produto.setTipoProduto(this.tipoProdutoRepository.obterPorCodigo(50L));
		
		Produto produtoValeDesconto = this.produtoRepository.merge(produto);

		return produtoValeDesconto;
	}
	
	private Set<ProdutoEdicao> getProdutosAplicacaoParaValeDesconto(Set<ProdutoEdicao> produtosAplicacao) {

		HashSet<ProdutoEdicao> produtosValeDesconto = new HashSet<ProdutoEdicao>();
		
		Iterator<ProdutoEdicao> iterator = produtosAplicacao.iterator();
		
		while (iterator.hasNext()) {
			
			ProdutoEdicao produtoEdicao = iterator.next();
			
			produtosValeDesconto.add(this.produtoEdicaoRepository.buscarPorId(produtoEdicao.getId()));
		}

		return produtosValeDesconto;
	}
	
	private void insertLancamentosParaValeDesconto(Set<Lancamento> lancamentos, ValeDesconto valeDesconto) {
		
		Iterator<Lancamento> iteratorLancamento = lancamentos.iterator();

		while (iteratorLancamento.hasNext()) {

			Lancamento lancamento = iteratorLancamento.next();
			
			lancamento.setProdutoEdicao(valeDesconto);
			
			this.lancamentoRepository.merge(lancamento);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remover(Long id) {
		
		ValeDescontoVO valeDesconto = this.valeDescontoRepository.obterPorId(id);
		
		if (valeDesconto.getSituacao() != null) {
			
			IllegalArgumentException exception = 
					new IllegalArgumentException("Este Vale não pode ser removido, pois se encontra em uma matriz de recolhimento confirmada.");
			
			switch(valeDesconto.getSituacao()) {
			case CONFIRMADO:
				throw exception;
			case PLANEJADO:
				throw exception;
			case EM_BALANCEAMENTO_RECOLHIMENTO:
				throw exception;
			default:
			}
		}

		this.valeDescontoRepository.removerPorId(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly=true)
	public ValeDescontoVO obterPorId(Long id) {

		ValeDescontoVO valeDesconto = this.valeDescontoRepository.obterPorId(id);
		
		valeDesconto.setPublicacoesCuponadas(this.valeDescontoRepository.obterPublicacoesCuponadas(id));
		
		return valeDesconto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValeDescontoVO sugerirProximaEdicao(String codigo) {
		
		Validate.notNull(codigo, "Código do vale desconto deve ser informado.");
		
		ValeDescontoVO valeDesconto = this.valeDescontoRepository.obterUltimaEdicao(codigo);
		
		if (valeDesconto == null) {
			
			valeDesconto = new ValeDescontoVO();
			
			valeDesconto.setNumeroEdicao(0L);
			
			valeDesconto.setCodigo(codigo);
		}
		
		Long proximaEdicao = valeDesconto.getNumeroEdicao() + 1;
		
		valeDesconto.setNumeroEdicao(proximaEdicao);

		return valeDesconto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ValeDescontoVO> obterPorFiltro(FiltroValeDescontoVO filtroValeDesconto) {

		return this.valeDescontoRepository.obterPorFiltro(filtroValeDesconto);
	}
	
	@Override 
	public Long obterCountPesquisaPorFiltro(FiltroValeDescontoVO filtroValeDesconto) {
		
		return this.valeDescontoRepository.obterCountPesquisaPorFiltro(filtroValeDesconto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly=true)
	public ValeDescontoVO obterPorCodigo(String codigo) {

		codigo = StringUtils.leftPad(codigo, 8, '0');
		
		return this.valeDescontoRepository.obterPorCodigo(codigo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly=true)
	public List<ValeDesconto> obterPorNome(String nome) {

		return this.valeDescontoRepository.obterPorNome(nome);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly=true)
	public List<ValeDescontoVO> obterPorCodigoOuNome(String filtro) {

		return this.valeDescontoRepository.obterPorCodigoOuNome(filtro);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly=true)
	public List<ValeDesconto> obterTodos() {

		return this.valeDescontoRepository.buscarTodos();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PublicacaoCuponada obterCuponadasPorCodigoEdicao(String codigo, Long numeroEdicao) {

		return this.valeDescontoRepository.obterCuponadasPorCodigoEdicao(codigo, numeroEdicao);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PublicacaoCuponada> obterCuponadasPorCodigoOuNome(String filtro) {

		return this.valeDescontoRepository.obterCuponadasPorCodigoOuNome(filtro);
	}
}
