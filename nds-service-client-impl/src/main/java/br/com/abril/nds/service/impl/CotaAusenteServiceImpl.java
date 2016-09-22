package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.tools.ant.util.DateUtils;
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
import br.com.abril.nds.model.cadastro.FormaComercializacao;
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
		
		this.enviarParaSuplementar(numCotas, data, idUsuario);
	}
	
	@Transactional 
	public void declararCotaAusenteRatearReparte(List<Integer> numCotas, 
												 Date data, 
												 Long idUsuario,
												 List<MovimentoEstoqueCotaDTO> movimentosRateio) 
												 throws TipoMovimentoEstoqueInexistenteException{
		
		List<CotaAusente> cotasAusentes = this.enviarParaSuplementar(numCotas, data, idUsuario);
		
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
	
	private List<CotaAusente> enviarParaSuplementar(List<Integer> numerosCotasAusentes, 
													Date data, 
													Long idUsuario)
													throws TipoMovimentoEstoqueInexistenteException {

		List<CotaAusente> cotasAusentes = new ArrayList<CotaAusente>();

		for (Integer numeroCotaAusente : numerosCotasAusentes) {

			validarCotaAusenteNaData(numeroCotaAusente, data);
			
			Cota cota = this.cotaRepository.obterPorNumeroDaCota(numeroCotaAusente);

			List<MovimentoEstoqueCota> movimentosCota = this.movimentoEstoqueCotaRepository.obterMovimentoCotaLancamentoPorTipoMovimento(
					data, cota.getId(), Arrays.asList(GrupoMovimentoEstoque.values()));

			List<MovimentoEstoqueCota> movimentosCotaEnvio = this.movimentoEstoqueService.enviarSuplementarCotaAusente(data, cota.getId(), movimentosCota, idUsuario);
			
			CotaAusente cotaAusente = this.gerarCotaAusente(
				numeroCotaAusente, data, idUsuario, cota, movimentosCotaEnvio);

			cotasAusentes.add(cotaAusente);

			cotaAusente.setId(this.cotaAusenteRepository.adicionar(cotaAusente));			
		}

		return cotasAusentes;
	}
	
	private CotaAusente gerarCotaAusente(Integer numCota, Date data, Long idUsuario, Cota cota,
										 List<MovimentoEstoqueCota> movimentosEstoqueCota)
										 throws TipoMovimentoEstoqueInexistenteException {

		CotaAusente cotaAusente = new CotaAusente();

		cotaAusente.setCota(cota);
		cotaAusente.setData(data);
		cotaAusente.setMovimentosEstoqueCota(movimentosEstoqueCota);

		return cotaAusente;
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
	 * Obtem quantidade à restaurar da Cota ausente, considerando quantidade disponivel no estoque suplementar
	 * @param movimento
	 * @param qtdeExistenteSuplementar
	 * @param quantidadeEstoqueSuplementarEdicao
	 * @return BigInteger
	 */
	private BigInteger obterQuantidadeRestaurarCotaAusente(MovimentoEstoqueCota movimento, 
		                                                   BigInteger qtdeExistenteSuplementar,
		                                                   Map<Long,BigInteger> quantidadeEstoqueSuplementarEdicao){

        BigInteger qtdeARestaurarCotaAusente = BigInteger.ZERO;
		
      //Se quantidade em estoque da distribuidora for maior que a qtd a retornar p/ cota ausente, retorna apenas a qtd inicial da cota ausente
		if (qtdeExistenteSuplementar.compareTo(movimento.getQtde()) > 0) {
			
			qtdeARestaurarCotaAusente = movimento.getQtde();
			
			quantidadeEstoqueSuplementarEdicao.put(movimento.getProdutoEdicao().getId(), (qtdeExistenteSuplementar.subtract(qtdeARestaurarCotaAusente)));
			
		} else {
			
			//Retona apenas a quantidade disponível em estoque
			qtdeARestaurarCotaAusente = qtdeExistenteSuplementar;
			
			quantidadeEstoqueSuplementarEdicao.put(movimento.getProdutoEdicao().getId(), BigInteger.ZERO);
		}
		
		return qtdeARestaurarCotaAusente;
	}
	
	/**
	 * Obtem quantidade existente no suplementar de uma edição
	 * @param movimento
	 * @param quantidadeEstoqueSuplementarEdicao
	 * @return BigInteger
	 */
	private BigInteger obterQuantidadeExistenteSuplementarEdicao(MovimentoEstoqueCota movimento,  
			                                                     Map<Long,BigInteger> quantidadeEstoqueSuplementarEdicao){
		
		BigInteger qtdeExistenteSuplementar = BigInteger.ZERO;
		
		if (quantidadeEstoqueSuplementarEdicao.get(movimento.getProdutoEdicao())!=null){
			
			qtdeExistenteSuplementar = quantidadeEstoqueSuplementarEdicao.get(movimento.getProdutoEdicao());
		}
		else{
		
		    qtdeExistenteSuplementar = obterQuantidadeSuplementarExistente(movimento.getProdutoEdicao().getId());
		    
		    quantidadeEstoqueSuplementarEdicao.put(movimento.getProdutoEdicao().getId(), qtdeExistenteSuplementar);
		}
		
		return qtdeExistenteSuplementar;
	}
	
   /**
	* Obtém movimentos de estoque de rateios de cota ausente e fornece quantidade de cada produto por referência
	* Atraves de lançamentos inversos
	* @param rateios
	* @param edicaoXRateio
	* @return List<MovimentoEstoqueCota>
	*/
	private Map<Long, BigInteger> obterMovimentosDeRateios(List<RateioCotaAusente> rateios) {
		
		Map<Long, BigInteger> edicaoXRateio = new HashMap<Long, BigInteger>();
		
        BigInteger quantidadeRateio = BigInteger.ZERO;

		if (rateios != null) {
			
			for (RateioCotaAusente rateio : rateios) {

				quantidadeRateio = edicaoXRateio.get(rateio.getProdutoEdicao().getId());
				
				quantidadeRateio = quantidadeRateio!=null?quantidadeRateio:BigInteger.ZERO;
				
				quantidadeRateio = quantidadeRateio.add(rateio.getQtde());
				
				edicaoXRateio.put(rateio.getProdutoEdicao().getId(), quantidadeRateio);
			}
		}
		
		return edicaoXRateio;
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
		
		Map<Long,BigInteger> quantidadeEstoqueSuplementarEdicao = new HashMap<Long, BigInteger>();
		
		//Restituição do Estoque da Cota e do Estoque do Distribuidor 
		for (MovimentoEstoqueCota movimento : movimentosCota) {
			
			if (movimento.getProdutoEdicao() != null) {
				
				BigInteger qtdeExistenteSuplementar =  this.obterQuantidadeExistenteSuplementarEdicao(movimento, 
						                                                                              quantidadeEstoqueSuplementarEdicao);

				BigInteger qtdeARestaurarCotaAusenteSuplementar = this.obterQuantidadeRestaurarCotaAusente(movimento, 
						                                                                        qtdeExistenteSuplementar,
						                                                                        quantidadeEstoqueSuplementarEdicao);

				if (qtdeARestaurarCotaAusenteSuplementar.compareTo(BigInteger.ZERO) > 0){

					//Retira do estoque da distribuidora a quantidade diponível a ser enviada de volta p/ cota ausente
					
					//Lança movimento para restituir o saldo do distribuidor
					this.movimentoEstoqueService.gerarMovimentoEstoque(null, 
							                                           movimento.getProdutoEdicao().getId(), 
																	   idUsuario, 
																	   qtdeARestaurarCotaAusenteSuplementar, 
																	   tipoMovimento);
					
					//Lança movimento para restituir o saldo da cota ausente
					this.movimentoEstoqueService.gerarMovimentoCota(cotaAusente.getData(), 
							                                        movimento.getProdutoEdicao(),
															        cotaAusente.getCota().getId(), 
															        idUsuario, 
															        qtdeARestaurarCotaAusenteSuplementar, 
															        tipoMovimentoCota,
																	dataOperacaoDistribuidor, null, FormaComercializacao.CONSIGNADO, false, false);
				}	
			}	
		}
	}

	/**
	 * Cancela uma Cota Ausente e reajusta os movimentos
	 * @param idCotaAusente
	 * @param idUsuario
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
		
		if (tipoMovimento == null) {
			
		 	throw new TipoMovimentoEstoqueInexistenteException(
		 		GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE);		
		} 
		
		if (tipoMovimentoCota == null) {
			
			throw new TipoMovimentoEstoqueInexistenteException(
				GrupoMovimentoEstoque.RESTAURACAO_REPARTE_COTA_AUSENTE);
		}
		
		List<RateioCotaAusente> rateios = cotaAusente.getRateios();
		                          
		List<MovimentoEstoqueCota> movimentosCota = cotaAusente.getMovimentosEstoqueCota();
				
		Map<Long, BigInteger> edicaoXRateio = this.obterMovimentosDeRateios(rateios);

		this.restituiEstoqueCotaEDistribuidor(movimentosCota,
                                              cotaAusente, 
                                              idUsuario, 
                                              tipoMovimento, 
                                              tipoMovimentoCota, 
                                              dataOperacaoDistribuidor, 
                                              edicaoXRateio);		
		
		this.cotaAusenteRepository.remover(cotaAusente);
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
			
			Cota cota = this.cotaRepository.obterPorNumeroDaCota(rateioDTO.getNumCota());
			
			BigInteger qtdeRateio = BigInteger.valueOf(rateioDTO.getQtde());
			
			TipoMovimentoEstoque tipoMovimento = 
				this.tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
					GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE);
				
			TipoMovimentoEstoque tipoMovimentoCota =
				this.tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
					GrupoMovimentoEstoque.RATEIO_REPARTE_COTA_AUSENTE);
		
			this.movimentoEstoqueService.gerarMovimentoEstoque(
				null, produtoEdicao.getId(), 
					idUsuario, qtdeRateio, tipoMovimento);
			
			this.movimentoEstoqueService.gerarMovimentoCota(
				data, produtoEdicao, cota.getId(), 
				idUsuario, qtdeRateio, tipoMovimentoCota, data, null, null, null, FormaComercializacao.CONSIGNADO);
			
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
		
		Cota cota = this.cotaRepository.obterPorNumeroDaCota(numeroCota);
		
		Validate.notNull(cota, "Cota inexistente");
		
		List<MovimentoEstoqueCota> movimentosEstoqueCota = 
			this.movimentoEstoqueCotaRepository.obterMovimentoCotaLancamentoPorTipoMovimento(
				data, cota.getId(), Arrays.asList(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE));
		
		if (movimentosEstoqueCota == null 
				|| movimentosEstoqueCota.isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "Cota '" + cota.getNumeroCota() + "' não possui reparte na data de lancamento "+DateUtils.format(data, "dd/MM/yyyy"));
		}
	}
	
	@Transactional(readOnly = true)
	public List<ProdutoEdicaoSuplementarDTO> obterDadosExclusaoCotaAusente(Long idCotaAusente) {
		
		return this.cotaAusenteRepository.obterDadosExclusaoCotaAusente(idCotaAusente);
	}	
}
