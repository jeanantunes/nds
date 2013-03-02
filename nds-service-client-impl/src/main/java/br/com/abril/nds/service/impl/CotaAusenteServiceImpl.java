package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaAusenteDTO;
import br.com.abril.nds.dto.MovimentoEstoqueCotaDTO;
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
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.RateioCotaAusenteRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.service.CotaAusenteService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.exception.TipoMovimentoEstoqueInexistenteException;
 
@Service 
public class CotaAusenteServiceImpl implements CotaAusenteService{
	
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
	
	@Transactional
	public void declararCotaAusenteEnviarSuplementar(List<Integer> numCotas, Date data, Long idUsuario) throws TipoMovimentoEstoqueInexistenteException {
		
		for(Integer numCota : numCotas) {
		
			Cota cota = cotaRepository.obterPorNumerDaCota(numCota);
			
			gerarCotaAusente(numCota, data, idUsuario, cota);

			List<MovimentoEstoqueCota> movimentosCota = 
				movimentoEstoqueCotaRepository.obterMovimentoCotaPorTipoMovimento(data, cota.getId(), GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
					
			movimentoEstoqueService.enviarSuplementarCotaAusente(data, cota.getId(), movimentosCota);
		}
	}
		
	private CotaAusente gerarCotaAusente(Integer numCota, Date data, Long idUsuario, Cota cota) throws TipoMovimentoEstoqueInexistenteException{
				
		if(isCotaAusenteNaData(numCota,data)) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota de número '" + numCota + "' já está declada como ausente.");
		}
				
		CotaAusente cotaAusente = new CotaAusente();
		cotaAusente.setCota(cota);
		cotaAusente.setAtivo(true);
		cotaAusente.setData(data);
		
		cotaAusenteRepository.adicionar(cotaAusente);
		
		return cotaAusente;
	}
	
	@Transactional 
	public void declararCotaAusenteRatearReparte(List<Integer> numCotas, Date data, Long idUsuario,
			List<MovimentoEstoqueCotaDTO> movimentosRateio) throws TipoMovimentoEstoqueInexistenteException{
		
		CotaAusente cotaAusente = null;
		
		for(Integer numCota : numCotas) {
				
			Cota cota = cotaRepository.obterPorNumerDaCota(numCota);
									
			cotaAusente = gerarCotaAusente(numCota, data, idUsuario, cota);
					
			List<MovimentoEstoqueCota> movimentosCota = 
					movimentoEstoqueCotaRepository.obterMovimentoCotaPorTipoMovimento(data, cota.getId(), GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
					
			movimentoEstoqueService.enviarSuplementarCotaAusente(data, cota.getId(), movimentosCota);
					
		}
		
		List<MovimentoEstoqueCotaDTO> movimentosCota = 
				movimentoEstoqueCotaRepository.obterMovimentoCotasPorTipoMovimento(data, numCotas, GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		
		for ( MovimentoEstoqueCotaDTO movimento : movimentosCota ) {
			
			for ( MovimentoEstoqueCotaDTO movimentoRateioDTO : movimentosRateio ) {
				
				if ( movimento.getIdProdEd() == movimentoRateioDTO.getIdProdEd() ) {
					
					ProdutoEdicao edicao = produtoEdicaoRepository.buscarPorId(movimento.getIdProdEd());
					
					gerarRateios(movimento.getQtdeReparte(), movimentoRateioDTO, 
							data, idUsuario, edicao, cotaAusente);					
				}
			}
		}	
	}
	
	private boolean isCotaAusenteNaData(Integer numCota, Date data) {
		
		FiltroCotaAusenteDTO filtro = new FiltroCotaAusenteDTO();
		filtro.setData(data);
		filtro.setNumCota(numCota);
		
		if(cotaAusenteRepository.obterCountCotasAusentes(filtro) > 0) {
			return true;
		}
		return false;
	}



	/**
	 * Método que cancela uma Cota Ausente e reajusta os movimentos
	 * @param idCotaAusente
	 * @throws TipoMovimentoEstoqueInexistenteException 
	 */
	@Transactional
	public void cancelarCotaAusente(Long idCotaAusente, Long idUsuario) throws TipoMovimentoEstoqueInexistenteException {
		
		Date dataAtual = new Date();
		
		CotaAusente cotaAusente = cotaAusenteRepository.buscarPorId(idCotaAusente);
	
		alterarStatusCotaAusente(cotaAusente);
		
		List<MovimentoEstoqueCota> movimentosCota = movimentoEstoqueCotaRepository.obterMovimentoCotaPorTipoMovimento(dataAtual, cotaAusente.getId(), GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
			
		for(MovimentoEstoqueCota movimento : movimentosCota) {
			
			if(movimento.getProdutoEdicao() != null){
				BigInteger qtdeExistente = obterQuantidadeSuplementarExistente(movimento.getProdutoEdicao().getId());
				BigInteger qtdeARetirar;
				
				if(qtdeExistente.compareTo(movimento.getQtde()) > 0) {
					qtdeARetirar = movimento.getQtde();
				} else {
					qtdeARetirar = qtdeExistente;
				}
				
				TipoMovimentoEstoque tipoMovimento = 
						tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE);
					
					TipoMovimentoEstoque tipoMovimentoCota =
						tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.RESTAURACAO_REPARTE_COTA_AUSENTE);
				
					if ( tipoMovimento == null ) {
					 	throw new TipoMovimentoEstoqueInexistenteException(GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE);		
					} 
					
					if ( tipoMovimentoCota == null ) {
						throw new TipoMovimentoEstoqueInexistenteException(GrupoMovimentoEstoque.RESTAURACAO_REPARTE_COTA_AUSENTE);
					}
					
			
				movimentoEstoqueService.gerarMovimentoEstoque(dataAtual,movimento.getProdutoEdicao().getId(),idUsuario,qtdeARetirar,tipoMovimento);
				movimentoEstoqueService.gerarMovimentoCota(dataAtual, movimento.getProdutoEdicao().getId(), idCotaAusente, idUsuario, qtdeARetirar, tipoMovimentoCota); 
			}	
		}
	}
	
	/**
	 * Modificar o Status de Cota para Inativo 
	 * @param cotaAusente
	 */
	private void alterarStatusCotaAusente(CotaAusente cotaAusente) {
		cotaAusente.setAtivo(false);
		cotaAusenteRepository.alterar(cotaAusente);		
	}



	private BigInteger obterQuantidadeSuplementarExistente(Long idProdutoEdicao) {
	
		EstoqueProduto ep = estoqueProdutoRepository.buscarEstoquePorProduto(idProdutoEdicao);
		
		return ep.getQtdeSuplementar();
	}
	
	private void gerarRateios(Integer qtdeDisponivel,
							  MovimentoEstoqueCotaDTO movimentoRateioDTO, 
							  Date data, Long idUsuario, ProdutoEdicao produtoEdicao, 
							  CotaAusente cotaAusente) {
		 
		int total = 0;
	
		for (RateioDTO rateioDTO : movimentoRateioDTO.getRateios()) {
			
			total += rateioDTO.getQtde();
			
			Cota cota = this.cotaRepository.obterPorNumerDaCota(rateioDTO.getNumCota());
			
			RateioCotaAusente rateio = new RateioCotaAusente();
			rateio.setCota(cota);
			rateio.setCotaAusente(cotaAusente);
			rateio.setProdutoEdicao(produtoEdicao);
			rateio.setQtde(BigInteger.valueOf( rateioDTO.getQtde() ));
			
			this.rateioCotaAusenteRepository.adicionar(rateio);
			
			TipoMovimentoEstoque tipoMovimento = 
				this.tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE);
				
			TipoMovimentoEstoque tipoMovimentoCota =
				this.tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		
			this.movimentoEstoqueService.gerarMovimentoEstoque(data, rateio.getProdutoEdicao().getProduto().getId(), idUsuario, rateio.getQtde(), tipoMovimento);
			this.movimentoEstoqueService.gerarMovimentoCota(data, rateio.getProdutoEdicao().getProduto().getId(), cota.getId(), idUsuario, rateio.getQtde(), tipoMovimentoCota);
		}
		
		if (total > qtdeDisponivel) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "A Quantidade Ultrapassou o Reparte.");
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
			this.movimentoEstoqueCotaRepository.obterMovimentoCotaPorTipoMovimento(
				data, cota.getId(), GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		
		if (movimentosEstoqueCota == null 
				|| movimentosEstoqueCota.isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "Cota '" + cota.getNumeroCota() + "' não possui reparte na data.");
		}
	}
	
}
