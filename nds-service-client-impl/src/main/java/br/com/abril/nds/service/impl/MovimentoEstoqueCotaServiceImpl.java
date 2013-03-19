package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.MovimentoEstoqueCotaDTO;
import br.com.abril.nds.dto.TransferenciaReparteSuplementarDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ParametrosRecolhimentoDistribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.ProdutoServico;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.repository.LancamentoParcialRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.service.ControleAprovacaoService;
import br.com.abril.nds.service.MovimentoEstoqueCotaService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.vo.ValidacaoVO;


@Service
public class MovimentoEstoqueCotaServiceImpl implements MovimentoEstoqueCotaService {
	
	@Autowired
	MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
	
	@Autowired
	private ControleAprovacaoService controleAprovacaoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private EstoqueProdutoRespository estoqueProdutoRespository;
	
	@Autowired
	private MovimentoEstoqueRepository movimentoEstoqueRepository;
	
	@Autowired
	private LancamentoParcialRepository lancamentoParcialRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private MovimentoEstoqueService movimentoEstoqueService;
	
	@Transactional
	public List<MovimentoEstoqueCota> obterMovimentoCotaPorTipoMovimento(Date data, Long idCota, GrupoMovimentoEstoque grupoMovimentoEstoque){
		return movimentoEstoqueCotaRepository.obterMovimentoCotaPorTipoMovimento(data, idCota, grupoMovimentoEstoque);
		
	}

	@Override
	@Transactional
	public List<MovimentoEstoqueCota> obterMovimentoCotaPorTipoMovimento(Date data, Integer numCota,GrupoMovimentoEstoque grupoMovimentoEstoque) {
		
		Cota cota = cotaRepository.obterPorNumerDaCota(numCota);
		
		return this.obterMovimentoCotaPorTipoMovimento(data, cota.getId(), grupoMovimentoEstoque);
	}

	@Override
	@Transactional
	public List<MovimentoEstoqueCotaDTO> obterMovimentoDTOCotaPorTipoMovimento(Date data, List<Integer> numCotas, GrupoMovimentoEstoque grupoMovimentoEstoque) {
	
		return movimentoEstoqueCotaRepository.obterMovimentoCotasPorTipoMovimento(data, numCotas, grupoMovimentoEstoque);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.MovimentoEstoqueCotaService#obterMovimentoEstoqueCotaPor(br.com.abril.nds.model.cadastro.Distribuidor, java.lang.Long, br.com.abril.nds.model.fiscal.TipoNotaFiscal, java.util.List, br.com.abril.nds.util.Intervalo, java.util.List, java.util.List)
	 */
	@Override
	@Transactional
	public List<MovimentoEstoqueCota> obterMovimentoEstoqueCotaPor(ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor, Long idCota, 
			TipoNotaFiscal tipoNotaFiscal, List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoques, 
			Intervalo<Date> periodo, List<Long> listaFornecedores, List<Long> listaProdutos) {
		
		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota =
				this.movimentoEstoqueCotaRepository.obterMovimentoEstoqueCotaPor(
						parametrosRecolhimentoDistribuidor, idCota, tipoNotaFiscal.getGrupoNotaFiscal(), listaGrupoMovimentoEstoques, periodo, 
						listaFornecedores, listaProdutos);
		
		listaMovimentoEstoqueCota = filtrarMovimentosQueJaPossuemNotas(listaMovimentoEstoqueCota,tipoNotaFiscal);
		
		return listaMovimentoEstoqueCota;
	}

	/**
	 * Filtra Movimentos que ja possuem nota fiscal do tipo de nota parametrizado.
	 * 
	 * @param listaMovimentoEstoqueCota
	 * @param tipoNotaFiscal
	 * @return movimentos que não possuem nota
	 */
	private List<MovimentoEstoqueCota> filtrarMovimentosQueJaPossuemNotas(
			List<MovimentoEstoqueCota> listaMovimentoEstoqueCota, TipoNotaFiscal tipoNotaFiscal) {
		
		List<MovimentoEstoqueCota> listaMovimentosFiltrados = new ArrayList<MovimentoEstoqueCota>();
		
		if (listaMovimentoEstoqueCota != null) {
			
			for (MovimentoEstoqueCota movimentoEstoqueCota : listaMovimentoEstoqueCota) {
				
				List<ProdutoServico> listaProdutoServico = movimentoEstoqueCota.getListaProdutoServicos();
				
				if (listaProdutoServico != null && !listaProdutoServico.isEmpty()) {
					
					boolean possuiNota = false;
					
					for (ProdutoServico produtoServico : listaProdutoServico) {
					
						NotaFiscal notaFiscal = produtoServico.getProdutoServicoPK().getNotaFiscal();
					
						TipoNotaFiscal tipoNota = notaFiscal.getIdentificacao().getTipoNotaFiscal();
					
						if (tipoNota.equals(tipoNotaFiscal)) {
							possuiNota = true;
						}
					}
					
					if(!possuiNota) {
						listaMovimentosFiltrados.add(movimentoEstoqueCota);
					}
					
				} else {
					listaMovimentosFiltrados.add(movimentoEstoqueCota);
				}
			}
		}
		
		return listaMovimentosFiltrados;
	}

	@Override
	@Transactional
	public Long obterQuantidadeReparteProdutoCota(Long idProdutoEdicao,
			Integer numeroCota) {		
		
		Cota cota = cotaRepository.obterPorNumerDaCota(numeroCota);
		
		TipoMovimentoEstoque tipoMovimentoCota =
				tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		
		if(tipoMovimentoCota == null)
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Tipo de Movimento de Reparte não encontrado."));
		
		Long idCota = cota == null ? null : cota.getId();
		
		return movimentoEstoqueCotaRepository.obterQuantidadeProdutoEdicaoMovimentadoPorCota(idCota, idProdutoEdicao, tipoMovimentoCota.getId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void transferirReparteParaSuplementar(ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor, 
												 List<Long> idsCota, Intervalo<Date> periodo,
												 List<Long> listaIdFornecedores, List<Long> listaIdProduto, 
												 TipoNotaFiscal tipoNotaFiscal) {

		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoque = new ArrayList<GrupoMovimentoEstoque>();
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE);
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.RESTAURACAO_REPARTE_COTA_AUSENTE);
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.COMPRA_SUPLEMENTAR);
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.ESTORNO_COMPRA_SUPLEMENTAR);
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.ENTRADA_SUPLEMENTAR_ENVIO_REPARTE);
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.ESTORNO_ENVIO_REPARTE);

		Usuario usuario = this.usuarioService.getUsuarioLogado();
		
		HashMap<ProdutoEdicao, TransferenciaReparteSuplementarDTO> mapaSuplementar = 
				new HashMap<ProdutoEdicao, TransferenciaReparteSuplementarDTO>();

		HashMap<ProdutoEdicao, TransferenciaReparteSuplementarDTO> mapaEstornoEnvioCota = null;

		for (Long idCota : idsCota) {

			Cota cota = this.cotaRepository.buscarPorId(idCota);

			mapaEstornoEnvioCota = new HashMap<ProdutoEdicao, TransferenciaReparteSuplementarDTO>();

			List<MovimentoEstoqueCota> listaMovimentoEstoqueCota =
					this.obterMovimentoEstoqueCotaPor(parametrosRecolhimentoDistribuidor, idCota, tipoNotaFiscal, 
							listaGrupoMovimentoEstoque, periodo, listaIdFornecedores, listaIdProduto);

			for (MovimentoEstoqueCota movimentoEstoqueCota : listaMovimentoEstoqueCota) {

				ajustarQuantidadeMovimentoPorProdutoEdicao(mapaSuplementar, movimentoEstoqueCota);

				ajustarQuantidadeMovimentoPorProdutoEdicao(mapaEstornoEnvioCota, movimentoEstoqueCota);
			}

			this.gerarMovimentoEstorno(mapaEstornoEnvioCota, cota, usuario);
		}

		this.gerarSuplementares(mapaSuplementar, usuario);
	}

	/*
	 * Método que realiza ajuste na quantidade do movimento por produto edição.
	 */
	private void ajustarQuantidadeMovimentoPorProdutoEdicao(
								HashMap<ProdutoEdicao, TransferenciaReparteSuplementarDTO> mapaProdutoEdicaoQuantidade,
								MovimentoEstoqueCota movimentoEstoqueCota) {

		ProdutoEdicao produtoEdicao = movimentoEstoqueCota.getProdutoEdicao();

		TransferenciaReparteSuplementarDTO transferencia = mapaProdutoEdicaoQuantidade.get(produtoEdicao);

		if (transferencia == null) {

			transferencia = new TransferenciaReparteSuplementarDTO();
			
			transferencia.setQuantidadeTransferir(BigInteger.ZERO);
		}

		BigInteger quantidadeTransferir = getQuantidadeParaEstornoPorGrupoMovimento(
			((TipoMovimentoEstoque) movimentoEstoqueCota.getTipoMovimento()).getGrupoMovimentoEstoque(), 
			transferencia.getQuantidadeTransferir(), movimentoEstoqueCota.getQtde()
		);

		transferencia.setQuantidadeTransferir(quantidadeTransferir);
		transferencia.setEstoqueProdutoCota(movimentoEstoqueCota.getEstoqueProdutoCota());
		
		mapaProdutoEdicaoQuantidade.put(produtoEdicao, transferencia);
	}

	/*
	 * Método que gera os movimentos de estorno para a cota.
	 */
	private void gerarMovimentoEstorno(HashMap<ProdutoEdicao, TransferenciaReparteSuplementarDTO> mapaEstornoEnvioCota, 
									   Cota cota, Usuario usuario) {

		TipoMovimentoEstoque tipoMovimento = 
			this.tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
				GrupoMovimentoEstoque.ESTORNO_ENVIO_REPARTE);

		for (Map.Entry<ProdutoEdicao, TransferenciaReparteSuplementarDTO> entry : mapaEstornoEnvioCota.entrySet()) {

			TransferenciaReparteSuplementarDTO transferencia = entry.getValue();
			
			ProdutoEdicao produtoEdicao = entry.getKey();
			
			this.movimentoEstoqueService.gerarMovimentoCota(
				null, produtoEdicao.getId(), cota.getId(), usuario.getId(), 
					transferencia.getQuantidadeTransferir(), tipoMovimento);
		}
	}

	/*
	 * Contabiliza a quantidade para o movimento, de acordo com o grupo de movimento do estoque. 
	 */
	private BigInteger getQuantidadeParaEstornoPorGrupoMovimento(GrupoMovimentoEstoque grupoMovimentoEstoque, 
							   									 BigInteger quantidadeParaEstorno, 
							   									 BigInteger quantidadeAtual) {

		switch(grupoMovimentoEstoque.getOperacaoEstoque()) {

		case ENTRADA:

			return quantidadeParaEstorno.add(quantidadeAtual);

		case SAIDA:

			return quantidadeParaEstorno.subtract(quantidadeAtual);

		default:

			return BigInteger.ZERO;
		}
	}

	/*
	 * Método que gera os movimentos de entrada do distribuidor. 
	 */
	private void gerarSuplementares(HashMap<ProdutoEdicao, TransferenciaReparteSuplementarDTO> mapaSuplementar,
									Usuario usuario) {

		TipoMovimentoEstoque tipoMovimento = this.tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
			GrupoMovimentoEstoque.ENTRADA_SUPLEMENTAR_ENVIO_REPARTE
		);

		Iterator<ProdutoEdicao> iterator = mapaSuplementar.keySet().iterator();

		while (iterator.hasNext()) {

			ProdutoEdicao produtoEdicao = iterator.next();

			TransferenciaReparteSuplementarDTO transferencia = mapaSuplementar.get(iterator);

			this.movimentoEstoqueService.gerarMovimentoEstoque(
				produtoEdicao.getId(), usuario.getId(), 
					transferencia.getQuantidadeTransferir(), tipoMovimento);
		}
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.MovimentoEstoqueCotaService#envioConsignadoNotaCancelada(br.com.abril.nds.model.fiscal.nota.NotaFiscal)
	 */
	@Override
	public void envioConsignadoNotaCancelada(NotaFiscal notaFiscalCancelada) {
		
		TipoMovimentoEstoque tipoMovimento = 
			this.tipoMovimentoEstoqueRepository.
				buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.CANCELAMENTO_NOTA_FISCAL_ENVIO_CONSIGNADO);
		
		List<ProdutoServico> listaProdutosServicosNotaCancelada = notaFiscalCancelada.getProdutosServicos();
		
		for (ProdutoServico produtoServico : listaProdutosServicosNotaCancelada) {
			
			for (MovimentoEstoqueCota movimentoEstoqueCota : produtoServico.getListaMovimentoEstoqueCota()) {
				
				ProdutoEdicao produtoEdicao = movimentoEstoqueCota.getProdutoEdicao();
				
				Usuario usuario = this.usuarioService.getUsuarioLogado();
				
				this.movimentoEstoqueService.gerarMovimentoEstoque(
					produtoEdicao.getId(), usuario.getId(), 
						movimentoEstoqueCota.getQtde(), tipoMovimento);
				
			}
			
		}
		
	}
	
	
}
