package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.MovimentoEstoqueCotaDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.ProdutoServico;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.service.MovimentoEstoqueCotaService;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.ValidacaoVO;


@Service
public class MovimentoEstoqueCotaServiceImpl implements MovimentoEstoqueCotaService {
	
	@Autowired
	MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
	
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
	public List<MovimentoEstoqueCotaDTO> obterMovimentoDTOCotaPorTipoMovimento(Date data, Integer numCota, GrupoMovimentoEstoque grupoMovimentoEstoque) {
	
		List<MovimentoEstoqueCota> movimentos = this.obterMovimentoCotaPorTipoMovimento(data, numCota, grupoMovimentoEstoque);
		
		
		List<MovimentoEstoqueCotaDTO> movimentosDTO = new ArrayList<MovimentoEstoqueCotaDTO>();
		
		Cota cota = cotaRepository.obterPorNumerDaCota(numCota);
		
		for(MovimentoEstoqueCota movimento : movimentos) {
			
			movimentosDTO.add(new MovimentoEstoqueCotaDTO(
					cota.getId(), 
					movimento.getProdutoEdicao().getId(), 
					movimento.getProdutoEdicao().getProduto().getCodigo(), 
					movimento.getProdutoEdicao().getNumeroEdicao(), 
					movimento.getProdutoEdicao().getProduto().getNome(), 
					movimento.getQtde().intValue(), 
					null));
		}
		
		return movimentosDTO;
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.MovimentoEstoqueCotaService#obterMovimentoEstoqueCotaPor(br.com.abril.nds.model.cadastro.Distribuidor, java.lang.Long, br.com.abril.nds.model.fiscal.TipoNotaFiscal, java.util.List, br.com.abril.nds.util.Intervalo, java.util.List, java.util.List)
	 */
	@Override
	@Transactional
	public List<MovimentoEstoqueCota> obterMovimentoEstoqueCotaPor(Distribuidor distribuidor, Long idCota, 
			TipoNotaFiscal tipoNotaFiscal, List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoques, 
			Intervalo<Date> periodo, List<Long> listaFornecedores, List<Long> listaProdutos) {
		
		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota =
				this.movimentoEstoqueCotaRepository.obterMovimentoEstoqueCotaPor(
						distribuidor, idCota, tipoNotaFiscal.getGrupoNotaFiscal(), listaGrupoMovimentoEstoques, periodo, 
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
	
}
