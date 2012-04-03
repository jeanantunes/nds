package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.CotaAusenteDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaAusenteDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.movimentacao.CotaAusente;
import br.com.abril.nds.model.movimentacao.RateioCotaAusente;
import br.com.abril.nds.repository.CotaAusenteRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.RateioCotaAusenteRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.service.CotaAusenteService;
import br.com.abril.nds.service.MovimentoEstoqueService;
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
	RateioCotaAusenteRepository rateioCotaAusente;
	
	@Autowired
	MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
	
	@Transactional
	public void declararCotaAusente(Integer numCota, Date data, List<RateioCotaAusente> listaDeRateio, Long idUsuario){

		Cota cota = cotaRepository.obterPorNumerDaCota(numCota);
		
		CotaAusente cotaAusente = new CotaAusente();
		cotaAusente.setCota(cota);
		cotaAusente.setAtivo(true);
		cotaAusente.setData(data);
		
		cotaAusenteRepository.adicionar(cotaAusente);
		
		 List<MovimentoEstoqueCota> movimentosCota = movimentoEstoqueCotaRepository.obterMovimentoCotaPorTipoMovimento(data, cota.getId(), GrupoMovimentoEstoque.ENVIO_JORNALEIRO);
		
		 movimentoEstoqueService.enviarSuplementarCotaAusente(data, cota.getId(), movimentosCota);
		 
		 gerarRateios(listaDeRateio, movimentosCota, data, idUsuario, cota.getId());
	}
	
	private void gerarRateios(List<RateioCotaAusente> listaDeRateio, List<MovimentoEstoqueCota> movimentosCota, Date data, Long idUsuario, Long idCota) {
		
		if(listaDeRateio == null || listaDeRateio.size()==0) {
			return;
		}
				
		for(MovimentoEstoqueCota movimentoEstoqueCota : movimentosCota) {
		
			BigDecimal total = new BigDecimal(0);
		
			for(RateioCotaAusente rateio : listaDeRateio) {
				
				if(movimentoEstoqueCota.getProdutoEdicao() != null && rateio.getProdutoEdicao() != null
						&& movimentoEstoqueCota.getProdutoEdicao().getProduto() != null 
						&&  rateio.getProdutoEdicao().getProduto() != null ){
				
					if(movimentoEstoqueCota.getProdutoEdicao().getProduto().getId() == rateio.getProdutoEdicao().getProduto().getId()) {
						total = total.add(rateio.getQtde());
						rateioCotaAusente.adicionar(rateio);
						
						TipoMovimentoEstoque tipoMovimento = 
								tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ENVIO_JORNALEIRO);
							
							TipoMovimentoEstoque tipoMovimentoCota =
								tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
						
						movimentoEstoqueService.gerarMovimentoEstoque(data, rateio.getProdutoEdicao().getProduto().getId(), idUsuario, rateio.getQtde(),tipoMovimento);
						movimentoEstoqueService.gerarMovimentoCota(data, rateio.getProdutoEdicao().getProduto().getId(), idCota, idUsuario, rateio.getQtde(), tipoMovimentoCota);
						
					}
				}
			}
			
			if(total.compareTo(movimentoEstoqueCota.getQtde()) == 1) {
				throw new ValidacaoException(TipoMensagem.ERROR, "A Quantidade Ultrapassou o Reparte.");				
			}
		}		
		
		
	}

	@Transactional
	public List<CotaAusenteDTO> obterCotasAusentes(FiltroCotaAusenteDTO filtroCotaAusenteDTO){
		return cotaAusenterepository.obterCotasAusentes(filtroCotaAusenteDTO);
	}

	@Override
	public Long obterCountCotasAusentes(FiltroCotaAusenteDTO filtro) {
		return cotaAusenteRepository.obterCountCotasAusentes(filtro);
	}

}
