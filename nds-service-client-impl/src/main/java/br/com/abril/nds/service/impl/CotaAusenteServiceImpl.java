package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaAusenteDTO;
import br.com.abril.nds.dto.MovimentoEstoqueCotaDTO;
import br.com.abril.nds.dto.ProdutoEdicaoSuplementarDTO;
import br.com.abril.nds.dto.RateioDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaAusenteDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.movimentacao.CotaAusente;
import br.com.abril.nds.model.movimentacao.RateioCotaAusente;
import br.com.abril.nds.repository.CotaAusenteRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.RateioCotaAusenteRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.service.CotaAusenteService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.exception.TipoMovimentoEstoqueInexistenteException;
 
@Service 
public class CotaAusenteServiceImpl implements CotaAusenteService {
	
	@Autowired
	CotaAusenteRepository cotaAusenteRepository;
	
	@Autowired
	CotaRepository cotaRepository;
	
	@Autowired
	MovimentoEstoqueService movimentoEstoqueService;
	
	@Autowired
	RateioCotaAusenteRepository rateioCotaAusenteRepository;
	
	@Autowired
	MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
	
	@Autowired
	EstoqueProdutoRespository estoqueProdutoRepository;
	
	@Autowired
	ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	DistribuidorRepository distribuidorRepository;
	
	@Transactional
	public void declararCotaAusenteEnviarSuplementar(List<Integer> numCotas, 
													 Date data, 
													 Long idUsuario) 
													 throws TipoMovimentoEstoqueInexistenteException {
		
		for (Integer numCota : numCotas) {
			
			this.validarCotaAusenteNaData(numCota, data);
			
			Cota cota = this.cotaRepository.obterPorNumerDaCota(numCota);

			List<MovimentoEstoqueCota> movimentosCota = 
				this.movimentoEstoqueCotaRepository.obterMovimentoCotaLancamentoPorTipoMovimento(
					data, cota.getId(), Arrays.asList(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE));
			
			List<MovimentoEstoqueCota> movimentosEstoqueCotaEnvio =
				this.movimentoEstoqueService.enviarSuplementarCotaAusente(
					data, cota.getId(), movimentosCota);
			
			CotaAusente cotaAusente = 
				gerarCotaAusente(numCota, data, idUsuario, cota, movimentosEstoqueCotaEnvio);
			
			this.cotaAusenteRepository.adicionar(cotaAusente);
		}
	}
		
	private CotaAusente gerarCotaAusente(Integer numCota, 
										 Date data, 
										 Long idUsuario, 
										 Cota cota,
										 List<MovimentoEstoqueCota> movimentosEstoqueCota) 
										 throws TipoMovimentoEstoqueInexistenteException {

		CotaAusente cotaAusente = new CotaAusente();
		
		cotaAusente.setCota(cota);
		cotaAusente.setData(data);
		cotaAusente.setMovimentosEstoqueCota(movimentosEstoqueCota);
		
		return cotaAusente;
	}
	
	@Transactional 
	public void declararCotaAusenteRatearReparte(List<Integer> numCotas, 
												 Date data, 
												 Long idUsuario,
												 List<MovimentoEstoqueCotaDTO> movimentosRateio) 
												 throws TipoMovimentoEstoqueInexistenteException{
		
		List<CotaAusente> cotasAusentes = new ArrayList<CotaAusente>();
		
		for (Integer numCota : numCotas) {
				
			Cota cota = this.cotaRepository.obterPorNumerDaCota(numCota);

			List<MovimentoEstoqueCota> movimentosCota = 
				this.movimentoEstoqueCotaRepository.obterMovimentoCotaLancamentoPorTipoMovimento(
					data, cota.getId(), Arrays.asList(GrupoMovimentoEstoque.values()));

			List<MovimentoEstoqueCota> movimentosCotaEnvio =
				this.movimentoEstoqueService.enviarSuplementarCotaAusente(
					data, cota.getId(), movimentosCota);
			
			CotaAusente cotaAusente = 
				gerarCotaAusente(numCota, data, idUsuario, cota, movimentosCotaEnvio);
			
			cotasAusentes.add(cotaAusente);
			
			this.cotaAusenteRepository.adicionar(cotaAusente);
		}
		
		List<MovimentoEstoqueCotaDTO> movimentosCota = 
			this.movimentoEstoqueCotaRepository.obterMovimentoCotasPorTipoMovimento(
				data, numCotas, Arrays.asList(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE));
		
		for (MovimentoEstoqueCotaDTO movimentoEstoqueCota : movimentosCota) {
			
			for (MovimentoEstoqueCotaDTO movimentoRateioDTO : movimentosRateio) {
				
				if (movimentoEstoqueCota.getIdProdEd().equals(
						movimentoRateioDTO.getIdProdEd())) {
					
					ProdutoEdicao produtoEdicao = 
						this.produtoEdicaoRepository.buscarPorId(
							movimentoEstoqueCota.getIdProdEd());
					
					gerarRateios(
						movimentoEstoqueCota.getQtdeReparte(), movimentoRateioDTO, 
							data, idUsuario, produtoEdicao, cotasAusentes);					
				}
			}
		}
	}
	
	public void validarCotaAusenteNaData(Integer numCota, Date data) {
		
		FiltroCotaAusenteDTO filtro = new FiltroCotaAusenteDTO();
		
		filtro.setData(data);
		filtro.setNumCota(numCota);
		
		if (this.cotaAusenteRepository.obterCountCotasAusentes(filtro) > 0) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "Cota de número '" 
					+ numCota + "' já está declarada como ausente.");
		}
	}
	
   /**
	* Estorna movimentos de estoque de rateios de cota ausente
	* Atraves de lançamentos inversos
	* @param rateios
	* @param cotaAusente
	* @param idUsuario
	* @param tipoMovimentoCotaEstorno
	* @param dataOperacao
	* @param edicaoXRateio
	* @return List<MovimentoEstoqueCota>
	*/
	private List<MovimentoEstoqueCota> estornaRateios(List<RateioCotaAusente> rateios, 
			                                          CotaAusente cotaAusente, 
									                  Long idUsuario, 
									                  TipoMovimentoEstoque tipoMovimentoCotaEstorno, 
									                  Date dataOperacaoDistribuidor,
									                  Map<Long, BigInteger> edicaoXRateio){
		
        BigInteger quantidadeRateio = BigInteger.ZERO;
		
		List<MovimentoEstoqueCota> movimentosCota = new ArrayList<MovimentoEstoqueCota>();

		//Estorno Rateios
		if (rateios != null) {
			
			for (RateioCotaAusente rateio : rateios) {

				//Estorna movimento da cota rateio
				this.movimentoEstoqueService.gerarMovimentoCota(cotaAusente.getData(), 
						                                        rateio.getProdutoEdicao().getId(),
																rateio.getCota().getId(), 
																idUsuario, 
																rateio.getQtde(), 
																tipoMovimentoCotaEstorno,
																dataOperacaoDistribuidor);

				quantidadeRateio = edicaoXRateio.get(rateio.getProdutoEdicao().getId());
				
				quantidadeRateio = quantidadeRateio!=null?quantidadeRateio:BigInteger.ZERO;
				
				quantidadeRateio = quantidadeRateio.add(rateio.getQtde());
				
				edicaoXRateio.put(rateio.getProdutoEdicao().getId(), quantidadeRateio);

				movimentosCota.addAll(rateio.getCotaAusente().getMovimentosEstoqueCota());	
			}
		}
		
		return movimentosCota;
	}
	
	/**
	 * Restitui Estoque da Cota Ausente e Estoque do Distribuidor
	 * Atraves de lançamentos inversos 
	 * @param movimentosCota
	 * @param cotaAusente
	 * @param idUsuario
	 * @param tipoMovimento
	 * @param tipoMovimentoCota
	 * @param edicaoXRateio
	 */
	private void restituiEstoqueCotaEDistribuidor(List<MovimentoEstoqueCota> movimentosCota, 
			                                      CotaAusente cotaAusente, 
			                                      Long idUsuario, 
			                                      TipoMovimentoEstoque tipoMovimento, 
			                                      TipoMovimentoEstoque tipoMovimentoCota,
			                                      Date dataOperacaoDistribuidor,
			                                      Map<Long, BigInteger> edicaoXRateio){
		
		//Restituição do Estoque da Cota e do Estoque do Distribuidor 
		for (MovimentoEstoqueCota movimento : movimentosCota) {
			
			if (movimento.getProdutoEdicao() != null) {
				
				BigInteger qtdeExistenteSuplementar = 
					obterQuantidadeSuplementarExistente(
						movimento.getProdutoEdicao().getId());
				
				BigInteger qtdeAEstornar;
				
				BigInteger qtdeARetirar;
				
				if (qtdeExistenteSuplementar.compareTo(movimento.getQtde()) > 0) {
					
					qtdeAEstornar = movimento.getQtde();
					
				} else {
					
					qtdeAEstornar = qtdeExistenteSuplementar;
				}
				
				BigInteger qtdeRateioEdicao = edicaoXRateio.get(movimento.getProdutoEdicao().getId()) != null ? 
						                      edicaoXRateio.get(movimento.getProdutoEdicao().getId()): 
						                      BigInteger.ZERO;
				
				qtdeARetirar = qtdeAEstornar.subtract(qtdeRateioEdicao);
				
				if (qtdeARetirar.compareTo(BigInteger.ZERO) > 0){
					
					//Lança movimento para restituir o saldo do distribuidor
					this.movimentoEstoqueService.gerarMovimentoEstoque(cotaAusente.getData(), 
							                                           movimento.getProdutoEdicao().getId(), 
																	   idUsuario, 
																	   qtdeARetirar, 
																	   tipoMovimento);
				}	
				
				//Lança movimento para restituir o saldo da cota ausente
				this.movimentoEstoqueService.gerarMovimentoCota(cotaAusente.getData(), 
						                                        movimento.getProdutoEdicao().getId(),
														        cotaAusente.getCota().getId(), 
														        idUsuario, 
														        qtdeAEstornar, 
														        tipoMovimentoCota,
																dataOperacaoDistribuidor);
			}	
		}
	}

	/**
	 * Método que cancela uma Cota Ausente e reajusta os movimentos
	 * @param idCotaAusente
	 * @throws TipoMovimentoEstoqueInexistenteException 
	 */
	@Transactional
	public void cancelarCotaAusente(Long idCotaAusente, Long idUsuario) throws TipoMovimentoEstoqueInexistenteException {
		
		CotaAusente cotaAusente = this.cotaAusenteRepository.buscarPorId(idCotaAusente);
		
		Date dataOperacaoDistribuidor =
			this.distribuidorRepository.obterDataOperacaoDistribuidor();
		
		if (cotaAusente.getData().compareTo(dataOperacaoDistribuidor) != 0) {
			
			throw new ValidacaoException(
				TipoMensagem.ERROR, 
					"A ausência da cota so pode ser cancelada na data de operação a qual ocorreu a ausência");
		}
		
		TipoMovimentoEstoque tipoMovimento = 
			this.tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
				GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE);
			
		TipoMovimentoEstoque tipoMovimentoCota =
			this.tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
				GrupoMovimentoEstoque.RESTAURACAO_REPARTE_COTA_AUSENTE);
		
		TipoMovimentoEstoque tipoMovimentoCotaEstorno =
			this.tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
				GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE);
		
		if (tipoMovimento == null) {
			
		 	throw new TipoMovimentoEstoqueInexistenteException(
		 		GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE);		
		} 
		
		if (tipoMovimentoCota == null) {
			
			throw new TipoMovimentoEstoqueInexistenteException(
				GrupoMovimentoEstoque.RESTAURACAO_REPARTE_COTA_AUSENTE);
		}
		
		if (tipoMovimentoCotaEstorno == null) {
			
			throw new TipoMovimentoEstoqueInexistenteException(
				GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE);
		}
		
		List<RateioCotaAusente> rateios = cotaAusente.getRateios();
		
		Map<Long, BigInteger> edicaoXRateio = new HashMap<Long, BigInteger>();
		                          
		List<MovimentoEstoqueCota> movimentosCota =  this.estornaRateios(rateios, 
				                                                         cotaAusente, 
				                                                         idUsuario, 
				                                                         tipoMovimentoCotaEstorno, 
				                                                         dataOperacaoDistribuidor,
				                                                         edicaoXRateio);

		this.restituiEstoqueCotaEDistribuidor(movimentosCota,
                                              cotaAusente, 
                                              idUsuario, 
                                              tipoMovimento, 
                                              tipoMovimentoCota, 
                                              dataOperacaoDistribuidor, 
                                              edicaoXRateio);		
		
		List<MovimentoEstoqueCota> backupMovimentosCotaAusente = cotaAusente.getMovimentosEstoqueCota();
		
		this.cotaAusenteRepository.remover(cotaAusente);
		
		this.movimentoEstoqueCotaRepository.mergeAll(backupMovimentosCotaAusente);
	}

	private BigInteger obterQuantidadeSuplementarExistente(Long idProdutoEdicao) {
	
		EstoqueProduto estoqueProduto = 
			this.estoqueProdutoRepository.buscarEstoquePorProduto(idProdutoEdicao);
		
		return (estoqueProduto.getQtdeSuplementar() == null) 
					? BigInteger.ZERO : estoqueProduto.getQtdeSuplementar();
	}
	
	private void gerarRateios(Integer qtdeDisponivel,
							  MovimentoEstoqueCotaDTO movimentoRateioDTO, 
							  Date data, Long idUsuario, 
							  ProdutoEdicao produtoEdicao, 
							  List<CotaAusente> cotasAusentes) {
		 
		if (movimentoRateioDTO == null
				|| movimentoRateioDTO.getRateios() == null
				|| movimentoRateioDTO.getRateios().isEmpty()) {
			
			return;
		}
		
		int total = 0;
	
		for (RateioDTO rateioDTO : movimentoRateioDTO.getRateios()) {
			
			total += rateioDTO.getQtde();
			
			Cota cota = this.cotaRepository.obterPorNumerDaCota(rateioDTO.getNumCota());
			
			BigInteger qtdeRateio = BigInteger.valueOf(rateioDTO.getQtde());
			
			TipoMovimentoEstoque tipoMovimento = 
				this.tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
					GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE);
				
			TipoMovimentoEstoque tipoMovimentoCota =
				this.tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
					GrupoMovimentoEstoque.RATEIO_REPARTE_COTA_AUSENTE);
		
			this.movimentoEstoqueService.gerarMovimentoEstoque(
				data, produtoEdicao.getId(), 
					idUsuario, qtdeRateio, tipoMovimento);
			
			this.movimentoEstoqueService.gerarMovimentoCota(
				data, produtoEdicao.getId(), cota.getId(), 
				idUsuario, qtdeRateio, tipoMovimentoCota, data, null, null, null);
			
			for (CotaAusente cotaAusente : cotasAusentes) {
				
				RateioCotaAusente rateio = new RateioCotaAusente();
				
				rateio.setCota(cota);
				rateio.setCotaAusente(cotaAusente);
				rateio.setProdutoEdicao(produtoEdicao);
				rateio.setQtde(BigInteger.valueOf(rateioDTO.getQtde()));
				
				this.rateioCotaAusenteRepository.adicionar(rateio);
			}
		}
		
		if (total > qtdeDisponivel) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "A quantidade ultrapassou o reparte.");
		}
	}

	@Transactional(readOnly = true)
	public List<CotaAusenteDTO> obterCotasAusentes(FiltroCotaAusenteDTO filtroCotaAusenteDTO){
		return cotaAusenteRepository.obterCotasAusentes(filtroCotaAusenteDTO);
	}

	@Transactional(readOnly = true)
	public Long obterCountCotasAusentes(FiltroCotaAusenteDTO filtro) {
		return cotaAusenteRepository.obterCountCotasAusentes(filtro);
	}
	
	@Transactional(readOnly = true)
	public void verificarExistenciaReparteCota(Date data, Integer numeroCota) {
		
		Cota cota = this.cotaRepository.obterPorNumerDaCota(numeroCota);
		
		Validate.notNull(cota, "Cota inexistente");
		
		List<MovimentoEstoqueCota> movimentosEstoqueCota = 
			this.movimentoEstoqueCotaRepository.obterMovimentoCotaLancamentoPorTipoMovimento(
				data, cota.getId(), Arrays.asList(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE));
		
		if (movimentosEstoqueCota == null 
				|| movimentosEstoqueCota.isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "Cota '" + cota.getNumeroCota() + "' não possui reparte na data.");
		}
	}
	
	@Transactional(readOnly = true)
	public List<ProdutoEdicaoSuplementarDTO> obterDadosExclusaoCotaAusente(Long idCotaAusente) {
		
		return this.cotaAusenteRepository.obterDadosExclusaoCotaAusente(idCotaAusente);
	}	
}
