package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.dto.QuantidadePrecoItemNotaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.nota.ItemNotaFiscal;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.GeracaoNotaEnvioService;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.MathUtil;

@Service
public class GeracaoNotaEnvioServiceImpl implements GeracaoNotaEnvioService {

	@Autowired
	private CotaRepository cotaRepository;

	@Autowired
	private DistribuidorRepository distribuidorRepository;

	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;

	@Autowired
	private DescontoService descontoService;

	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;

	@Override
	@Transactional
	public List<CotaExemplaresDTO> busca(Intervalo<Integer> intervaloBox,
			Intervalo<Integer> intervalorCota,
			Intervalo<Date> intervaloDateMovimento,
			List<Long> listIdFornecedor, String sortname, String sortorder,
			Integer resultsPage, Integer page,
			SituacaoCadastro situacaoCadastro, Long idRoteiro, Long idRota) {

		Set<Long> idsCotasDestinatarias = this.cotaRepository
				.obterIdCotasEntre(intervalorCota, intervaloBox,
						situacaoCadastro, idRoteiro, idRota);

		Map<Long, QuantidadePrecoItemNotaDTO> cotasTotalItens = obterTotalItensNotaEnvioPorCota(
				intervaloDateMovimento, listIdFornecedor, idsCotasDestinatarias);

		Set<Long> idCotas = cotasTotalItens.keySet();

		List<CotaExemplaresDTO> listaCotaExemplares = new ArrayList<CotaExemplaresDTO>();

		for (Long idCota : idCotas) {

			Cota cota = this.cotaRepository.buscarPorId(idCota);

			CotaExemplaresDTO cotaExemplares = new CotaExemplaresDTO();

			cotaExemplares.setIdCota(cota.getId());
			cotaExemplares.setExemplares(cotasTotalItens.get(cota).getQuantidade().longValue());
			cotaExemplares.setNomeCota(cota.getPessoa().getNome());
			cotaExemplares.setNumeroCota(cota.getNumeroCota());
			cotaExemplares.setTotal(cotasTotalItens.get(cota).getPreco());
			cotaExemplares.setTotalDesconto(cotasTotalItens.get(cota).getPrecoComDesconto());

			listaCotaExemplares.add(cotaExemplares);

		}

		return listaCotaExemplares;
	}

	public Map<Long, QuantidadePrecoItemNotaDTO> obterTotalItensNotaEnvioPorCota(
			Intervalo<Date> periodo, List<Long> listaIdFornecedores,
			Set<Long> idsCotasDestinatarias) {

		Map<Long, QuantidadePrecoItemNotaDTO> idCotaTotalItensNota = new HashMap<Long, QuantidadePrecoItemNotaDTO>();

		Distribuidor distribuidor = this.distribuidorRepository.obter();

		for (Long idCota : idsCotasDestinatarias) {

			List<ItemNotaFiscal> itensNotaFiscal = obterItensNotaVenda(
					distribuidor, idCota, periodo, listaIdFornecedores);

			if (itensNotaFiscal != null && !itensNotaFiscal.isEmpty()) {
				idCotaTotalItensNota.put(idCota,
						this.sumarizarTotalItensNota(itensNotaFiscal));
			}
		}

		return idCotaTotalItensNota;
	}

	private List<ItemNotaFiscal> obterItensNotaVenda(Distribuidor distribuidor,
			Long idCota, Intervalo<Date> periodo, List<Long> listaIdFornecedores) {

		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoques = new ArrayList<GrupoMovimentoEstoque>();

		listaGrupoMovimentoEstoques
				.add(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = movimentoEstoqueCotaRepository
				.obterMovimentoEstoqueCotaPor(distribuidor, idCota,
						listaGrupoMovimentoEstoques, periodo,
						listaIdFornecedores);

		return gerarItensNotaEnvio(listaMovimentoEstoqueCota, idCota);

	}

	private List<ItemNotaFiscal> gerarItensNotaEnvio(
			List<MovimentoEstoqueCota> listaMovimentoEstoqueCota, Long idCota) {

		Map<Long, ItemNotaFiscal> mapItemNotaFiscal = new HashMap<Long, ItemNotaFiscal>();

		Cota cota = cotaRepository.buscarPorId(idCota);
		for (MovimentoEstoqueCota movimentoEstoqueCota : listaMovimentoEstoqueCota) {

			ProdutoEdicao produtoEdicao = movimentoEstoqueCota
					.getProdutoEdicao();
			BigDecimal precoVenda = produtoEdicao.getPrecoVenda();
			BigDecimal percentualDesconto = descontoService
					.obterDescontoPorCotaProdutoEdicao(cota, produtoEdicao);
			BigDecimal valorDesconto = MathUtil.calculatePercentageValue(
					precoVenda, percentualDesconto);

			BigDecimal valorUnitario = precoVenda.subtract(valorDesconto);

			BigInteger quantidade = movimentoEstoqueCota.getQtde();

			List<MovimentoEstoqueCota> listaMovimentoEstoqueItem = new ArrayList<MovimentoEstoqueCota>();

			listaMovimentoEstoqueItem.add(movimentoEstoqueCota);

			ItemNotaFiscal itemNotaFiscal = new ItemNotaFiscal();

			itemNotaFiscal.setIdProdutoEdicao(produtoEdicao.getId());

			if (produtoEdicao.getProduto().getTributacaoFiscal() != null) {
				itemNotaFiscal.setCstICMS(produtoEdicao.getProduto()
						.getTributacaoFiscal().getCST());
			}

			itemNotaFiscal.setQuantidade(quantidade);
			itemNotaFiscal.setValorUnitario(valorUnitario);
			itemNotaFiscal
					.setListaMovimentoEstoqueCota(listaMovimentoEstoqueItem);

			mapItemNotaFiscal.put(produtoEdicao.getId(), itemNotaFiscal);
		}

		return new ArrayList<ItemNotaFiscal>(mapItemNotaFiscal.values());
	}

	private QuantidadePrecoItemNotaDTO sumarizarTotalItensNota(
			List<ItemNotaFiscal> listaItemNotaFiscal) {

		QuantidadePrecoItemNotaDTO dto = new QuantidadePrecoItemNotaDTO();

		BigInteger quantidade = BigInteger.ZERO;
		BigDecimal preco = BigDecimal.ZERO;
		BigDecimal precoComDesconto = BigDecimal.ZERO;

		for (ItemNotaFiscal item : listaItemNotaFiscal) {
			quantidade = quantidade.add(item.getQuantidade());
			preco = preco.add(item.getValorUnitario().multiply(
					new BigDecimal(quantidade)));

			BigDecimal desconto = this.descontoService
					.obterDescontoPorCotaProdutoEdicao(item
							.getListaMovimentoEstoqueCota().get(0).getCota(),
							this.produtoEdicaoRepository.buscarPorId(item
									.getIdProdutoEdicao()));

			precoComDesconto = precoComDesconto.add(
					item.getValorUnitario()
							.subtract(desconto, new MathContext(3))
							.multiply(item.getValorUnitario())
							.divide(new BigDecimal(100))).multiply(
					new BigDecimal(item.getQuantidade()));
		}

		dto.setQuantidade(quantidade);
		dto.setPreco(preco);
		dto.setPrecoComDesconto(precoComDesconto);

		return dto;
	}
}