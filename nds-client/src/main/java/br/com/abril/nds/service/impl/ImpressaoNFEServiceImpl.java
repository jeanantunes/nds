package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.NfeVO;
import br.com.abril.nds.dto.CotasImpressaoNfeDTO;
import br.com.abril.nds.dto.NfeDTO;
import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroImpressaoNFEDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.ImpressaoNFeRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.ImpressaoNFEService;
import br.com.abril.nds.service.MatrizLancamentoService;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.MathUtil;

/**
 * @author InfoA2
 */
@Service
public class ImpressaoNFEServiceImpl implements ImpressaoNFEService {

	@Autowired
	private DescontoService descontoService;
	
	@Autowired
	private NotaFiscalRepository notaFiscalRepository;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private MatrizLancamentoService matrizLancamentoService;
	
	@Autowired
	private ImpressaoNFeRepository impressaoNFeRepository;

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.ImpressaoNFEService#obterProdutosExpedicaoConfirmada(java.util.List)
	 */
	@Override
	public List<ProdutoLancamentoDTO> obterProdutosExpedicaoConfirmada(List<Fornecedor> fornecedores, Date data) {
		List<Long> idsFornecedores = new ArrayList<Long>();
		for (Fornecedor fornecedor : fornecedores) {
			idsFornecedores.add(fornecedor.getId());
		}

		FiltroLancamentoDTO filtroLancamento = new FiltroLancamentoDTO(new Date(), idsFornecedores);
		
		// Retorna uma lista de produtos da data apontada no service
		return matrizLancamentoService.obterMatrizLancamento(filtroLancamento, false).getMatrizLancamento().get(data);
	}

	@Override
	@Transactional
	public List<NfeDTO> obterNFesParaImpressao(FiltroImpressaoNFEDTO filtro) {

		List<CotasImpressaoNfeDTO> listaNFeDTO = impressaoNFeRepository.buscarCotasParaImpressaoNFe(filtro);
		
		List<NfeVO> listaNFeVO = new ArrayList<NfeVO>();
		for(CotasImpressaoNfeDTO nfeDTO : listaNFeDTO) {
			/*
			List<NfeDTO> listaNotaFisal = impressaoNFeRepository.pesquisarNotaFiscal(filtro);
			NfeVO nfe = new NfeVO();
			nfe.setIdNotaFiscal(nfeDTO.getIdNotaFiscal());
			listaNFeVO.add(nfe);*/
		}
		
		return null;
		
	}
	@Transactional
	public List<CotasImpressaoNfeDTO> buscarCotasParaImpressaoNFe(FiltroImpressaoNFEDTO filtro) {

		List<CotasImpressaoNfeDTO> cotas = impressaoNFeRepository.buscarCotasParaImpressaoNFe(filtro);
		List<CotasImpressaoNfeDTO> cotasARemover = new ArrayList<CotasImpressaoNfeDTO>();

		Distribuidor distribuidor = this.distribuidorRepository.obter();
		
		for (CotasImpressaoNfeDTO itemCota : cotas) {

			List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = obterItensNotaVenda(
					distribuidor, itemCota.getIdCota(), filtro.getDataMovimentoInicial(), filtro.getDataMovimentoFinal(), filtro.getIdsFornecedores(), filtro.getCodigosProdutos());

			if(listaMovimentoEstoqueCota == null || listaMovimentoEstoqueCota.size() < 1)
				cotasARemover.add(itemCota);
			
			this.sumarizarTotalItensNota(listaMovimentoEstoqueCota, itemCota);

		}

		cotas.removeAll(cotasARemover);
		
		return cotas;
	}

	@Transactional
	private List<MovimentoEstoqueCota> obterItensNotaVenda(Distribuidor distribuidor,
			Long idCota, Date dataMovimentoInicial, Date dataMovimentoFinal, List<Long> listaIdFornecedores, List<Long> listaCodigosProdutos) {

		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoques = new ArrayList<GrupoMovimentoEstoque>();

		Intervalo<Date> dataMovimento = new Intervalo<Date>(dataMovimentoInicial, dataMovimentoFinal);
		
		//TODO: Sérgio - Complementar a lista com os movimentos de estoque possíveis
		listaGrupoMovimentoEstoques.add(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);

		List<MovimentoEstoqueCota> movEstCota = movimentoEstoqueCotaRepository
				.obterMovimentoEstoqueCotaPor(distribuidor, idCota, listaGrupoMovimentoEstoques, dataMovimento, listaIdFornecedores);
		
		
		if(listaCodigosProdutos != null && listaCodigosProdutos.size() > 0) {
			List<MovimentoEstoqueCota> movEstCotaFiltrado = new ArrayList<MovimentoEstoqueCota>();
			
			for(MovimentoEstoqueCota mec : movEstCota) {
				if(listaCodigosProdutos.contains(Long.parseLong(mec.getProdutoEdicao().getProduto().getCodigo()))) {
					movEstCotaFiltrado.add(mec);
				}				
			}
			
			return movEstCotaFiltrado;
		}
		
		return movEstCota;
	}

	@Transactional
	private void sumarizarTotalItensNota(
			List<MovimentoEstoqueCota> listaMovimentoEstoqueCota,
			CotasImpressaoNfeDTO itemCota) {

		BigInteger quantidade = BigInteger.ZERO;
		BigDecimal preco = BigDecimal.ZERO;
		BigDecimal precoComDesconto = BigDecimal.ZERO;
		itemCota.setNotaImpressa(false);

		for (MovimentoEstoqueCota movimento : listaMovimentoEstoqueCota) {
			ProdutoEdicao produtoEdicao = movimento.getProdutoEdicao();
			BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
			BigDecimal percentualDesconto = descontoService
					.obterDescontoPorCotaProdutoEdicao(cotaRepository.buscarPorId(itemCota.getIdCota()), produtoEdicao);
			BigDecimal valorDesconto = MathUtil.calculatePercentageValue(precoVenda, percentualDesconto);			
			quantidade = quantidade.add(movimento.getQtde());
			preco = preco.add(precoVenda.multiply(new BigDecimal(movimento.getQtde())));
			precoComDesconto = precoComDesconto.add(precoVenda.subtract(valorDesconto, new MathContext(3)));	

			if(!movimento.getListaItemNotaEnvio().isEmpty()){
				itemCota.setNotaImpressa(true);
			}

		}

		itemCota.setVlrTotal(preco);
		itemCota.setVlrTotalDesconto(precoComDesconto);
		itemCota.setTotalExemplares(quantidade);

	}

	@Transactional
	public Integer buscarNFeParaImpressaoTotalQtd(FiltroImpressaoNFEDTO filtro) {
		return impressaoNFeRepository.buscarCotasParaImpressaoNFeQtd(filtro);
	}


}
