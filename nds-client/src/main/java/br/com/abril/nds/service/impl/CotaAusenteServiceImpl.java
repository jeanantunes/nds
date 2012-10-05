package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaAusenteDTO;
import br.com.abril.nds.dto.MovimentoEstoqueCotaDTO;
import br.com.abril.nds.dto.RateioDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaAusenteDTO;
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
import br.com.abril.nds.repository.RateioCotaAusenteRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.service.CotaAusenteService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.exception.TipoMovimentoEstoqueInexistenteException;
import br.com.abril.nds.util.TipoMensagem;
 
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
	
	@Transactional
	public void declararCotaAusenteEnviarSuplementar(List<Integer> numCotas, Date data, Long idUsuario) throws TipoMovimentoEstoqueInexistenteException {
		
		for(Integer numCota : numCotas) {
		
			Cota cota = cotaRepository.obterPorNumerDaCota(numCota);
			
			gerarCotaAusente(numCota, data, idUsuario, cota);
					
			List<MovimentoEstoqueCota> movimentosCota = 
					movimentoEstoqueCotaRepository.obterMovimentoCotaPorTipoMovimento(data, cota.getId(), GrupoMovimentoEstoque.ENVIO_JORNALEIRO);
					
			movimentoEstoqueService.enviarSuplementarCotaAusente(data, cota.getId(), movimentosCota);
		}
	}
		
	private CotaAusente gerarCotaAusente(Integer numCota, Date data, Long idUsuario, Cota cota) throws TipoMovimentoEstoqueInexistenteException{
				
		if(isCotaAusenteNaData(numCota,data)) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota já está declada como ausente.");
		}
				
		CotaAusente cotaAusente = new CotaAusente();
		cotaAusente.setCota(cota);
		cotaAusente.setAtivo(true);
		cotaAusente.setData(data);
		
		cotaAusenteRepository.adicionar(cotaAusente);
		
		return cotaAusente;
	}
	
	@Transactional 
	public void declararCotaAusenteRatearReparte(Integer numCota, Date data, Long idUsuario,
			List<MovimentoEstoqueCotaDTO> movimentosRateio) throws TipoMovimentoEstoqueInexistenteException{
	
		Cota cota = cotaRepository.obterPorNumerDaCota(numCota);
		
		CotaAusente cotaAusente = gerarCotaAusente(numCota, data, idUsuario, cota);
				
		List<MovimentoEstoqueCota> movimentosCota = 
				movimentoEstoqueCotaRepository.obterMovimentoCotaPorTipoMovimento(data, cota.getId(), GrupoMovimentoEstoque.ENVIO_JORNALEIRO);
				
		movimentoEstoqueService.enviarSuplementarCotaAusente(data, cota.getId(), movimentosCota);
				
		for ( MovimentoEstoqueCota movimento : movimentosCota ) {
			
			for ( MovimentoEstoqueCotaDTO movimentoRateioDTO : movimentosRateio ) {
				
				if ( movimento.getProdutoEdicao().getId() == movimentoRateioDTO.getIdProdEd() ) {
					
					gerarRateios(movimento, movimentoRateioDTO, 
							data, idUsuario, movimento.getProdutoEdicao(), cotaAusente);					
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
		
		List<MovimentoEstoqueCota> movimentosCota = movimentoEstoqueCotaRepository.obterMovimentoCotaPorTipoMovimento(dataAtual, cotaAusente.getId(), GrupoMovimentoEstoque.ENVIO_JORNALEIRO);
			
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
	
	private void gerarRateios(MovimentoEstoqueCota movimento,
			MovimentoEstoqueCotaDTO movimentoRateioDTO, Date data,
			Long idUsuario, ProdutoEdicao produtoEdicao, CotaAusente cotaAusente) {
		 		
		
		int total = 0;
	
		for(RateioDTO rateioDTO : movimentoRateioDTO.getRateios()) {
			
				total += rateioDTO.getQtde();
			
				Cota cota = cotaRepository.obterPorNumerDaCota(rateioDTO.getNumCota());
				
				RateioCotaAusente rateio = new RateioCotaAusente();
				rateio.setCota(cota);
				rateio.setCotaAusente(cotaAusente);
				rateio.setProdutoEdicao(produtoEdicao);
				rateio.setQtde(BigInteger.valueOf( rateioDTO.getQtde() ));
				
				rateioCotaAusenteRepository.adicionar(rateio);
				
				TipoMovimentoEstoque tipoMovimento = 
						tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ENVIO_JORNALEIRO);
					
				TipoMovimentoEstoque tipoMovimentoCota =
					tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
			
				movimentoEstoqueService.gerarMovimentoEstoque(data, rateio.getProdutoEdicao().getProduto().getId(), idUsuario, rateio.getQtde(),tipoMovimento);
				movimentoEstoqueService.gerarMovimentoCota(data, rateio.getProdutoEdicao().getProduto().getId(), cota.getId(), idUsuario, rateio.getQtde(), tipoMovimentoCota);
				
		}
		
		if(total > movimento.getQtde().intValue()) {
			throw new ValidacaoException(TipoMensagem.ERROR, "A Quantidade Ultrapassou o Reparte.");				
			
		}	
	}

	@Transactional
	public List<CotaAusenteDTO> obterCotasAusentes(FiltroCotaAusenteDTO filtroCotaAusenteDTO){
		return cotaAusenteRepository.obterCotasAusentes(filtroCotaAusenteDTO);
	}

	@Override
	public Long obterCountCotasAusentes(FiltroCotaAusenteDTO filtro) {
		return cotaAusenteRepository.obterCountCotasAusentes(filtro);
	}
}
