package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.ControleFechamentoEncalhe;
import br.com.abril.nds.model.estoque.FechamentoEncalhe;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalhePK;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ChamadaEncalheRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.FechamentoEncalheRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.FechamentoEncalheService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class FechamentoEncalheServiceImpl implements FechamentoEncalheService {

	@Autowired
	private FechamentoEncalheRepository fechamentoEncalheRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private GerarCobrancaService gerarCobrancaService;

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;
	
	@Autowired
	private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;

	@Autowired
	private ChamadaEncalheRepository chamadaEncalheRepository;
		
	@Override
	@Transactional
	public List<FechamentoFisicoLogicoDTO> buscarFechamentoEncalhe(FiltroFechamentoEncalheDTO filtro,
			String sortorder, String sortname, int page, int rp) {
		
		int startSearch = page * rp - rp;
		String sort = sortname;
		if (sortname.equals("total")) {
			sort = null;
		}
		
		Boolean fechado = fechamentoEncalheRepository.buscaControleFechamentoEncalhe(filtro.getDataEncalhe());
		List<FechamentoFisicoLogicoDTO> listaConferencia = fechamentoEncalheRepository.buscarConferenciaEncalhe(filtro, sortorder, sort, startSearch, rp);
		List<FechamentoEncalhe> listaFechamento = fechamentoEncalheRepository.buscarFechamentoEncalhe(filtro);
		
		for (FechamentoFisicoLogicoDTO conferencia : listaConferencia) {
			
			conferencia.setTotal(conferencia.getExemplaresDevolucao().multiply(conferencia.getPrecoCapa()));
			conferencia.setFechado(fechado);
			
			for (FechamentoEncalhe fechamento : listaFechamento) {
				if (conferencia.getCodigo().equals(fechamento.getFechamentoEncalhePK().getProdutoEdicao().getProduto().getCodigo())) {
					conferencia.setFisico(fechamento.getQuantidade());
					break;
				}
			}
		}
		
		if (sort == null) {
			if (sortorder.equals("asc")) {
				Collections.sort(listaConferencia, new FechamentoAscComparator());
			} else {
				Collections.sort(listaConferencia, new FechamentoDescComparator());
			}
		}
		
		return listaConferencia;
	}
	
	@Override
	@Transactional
	public List<FechamentoFisicoLogicoDTO> salvarFechamentoEncalhe(FiltroFechamentoEncalheDTO filtro,
			String sortorder, String sortname, int page, int rp) {
		
		int startSearch = page * rp - rp;
		
		List<FechamentoFisicoLogicoDTO> listaConferencia = this.buscarFechamentoEncalhe(filtro, sortorder, sortname, startSearch, rp);
		
		FechamentoFisicoLogicoDTO fechamento;
		Long qtd;
		
		for (int i=0; i < listaConferencia.size(); i++) {
			
			fechamento = listaConferencia.get(i);
			qtd = filtro.getFisico().get(i);
			
			FechamentoEncalhePK id = new FechamentoEncalhePK();
			id.setDataEncalhe(filtro.getDataEncalhe());
			ProdutoEdicao pe = new ProdutoEdicao();
			pe.setId(fechamento.getProdutoEdicao());
			id.setProdutoEdicao(pe);
			
			FechamentoEncalhe fechamentoEncalhe = fechamentoEncalheRepository.buscarPorId(id);
			
			if (fechamentoEncalhe == null) {
				
				fechamentoEncalhe = new FechamentoEncalhe();
				fechamentoEncalhe.setFechamentoEncalhePK(id);
				fechamentoEncalhe.setQuantidade(qtd);
				
				fechamentoEncalheRepository.adicionar(fechamentoEncalhe);
			
			} else {
				
				fechamentoEncalhe.setQuantidade(qtd);
				fechamentoEncalheRepository.alterar(fechamentoEncalhe);
			}
			
			fechamento.setFisico(qtd); // retorna valor pra tela
		}
		
		fechamentoEncalheRepository.flush();
		
		return listaConferencia;
	}

	@Override
	@Transactional(readOnly=true)
	public List<CotaAusenteEncalheDTO> buscarCotasAusentes(Date dataEncalhe,
			String sortorder, String sortname, int page, int rp) {
		
		int startSearch = 0;
		
		if (page >= 0) {
			startSearch = page * rp - rp;	
		} 
		
		List<CotaAusenteEncalheDTO> listaCotaAusenteEncalhe = 
			this.fechamentoEncalheRepository.buscarCotasAusentes(dataEncalhe, sortorder, sortname, startSearch, rp);
		
		for (CotaAusenteEncalheDTO cotaAusenteEncalheDTO : listaCotaAusenteEncalhe) {
			
			if (cotaAusenteEncalheDTO.getFechado()) {
				cotaAusenteEncalheDTO.setAcao(
					"Cobrado, " + DateUtil.formatarData(cotaAusenteEncalheDTO.getDataEncalhe(), "dd/MM/yyyy"));
			}
		}
		
		return listaCotaAusenteEncalhe;
	}

	@Override
	@Transactional(readOnly=true)
	public Integer buscarTotalCotasAusentes(Date dataEncalhe) {
		return this.fechamentoEncalheRepository.buscarTotalCotasAusentes(dataEncalhe);
	}

	private class FechamentoAscComparator implements Comparator<FechamentoFisicoLogicoDTO> {
		@Override
		public int compare(FechamentoFisicoLogicoDTO o1, FechamentoFisicoLogicoDTO o2) {
			return o1.getTotal().compareTo(o2.getTotal());
		}
	}
	
	private class FechamentoDescComparator implements Comparator<FechamentoFisicoLogicoDTO> {
		@Override
		public int compare(FechamentoFisicoLogicoDTO o1, FechamentoFisicoLogicoDTO o2) {
			return o2.getTotal().compareTo(o1.getTotal());
		}
	}


	@Override
	@Transactional
	public void postergarCotas(Date dataEncalhe, List<Long> idsCotas) {
	
		if (idsCotas == null || idsCotas.isEmpty()) {
			throw new IllegalArgumentException("Lista de ids das cotas não pode ser nula e nem vazia.");
		}
		
		if (dataEncalhe == null) {
			throw new IllegalArgumentException("Data de encalhe não pode ser nula.");
		}
		
		List<Cota> listaCotas = 
			this.cotaRepository.obterCotasPorIDS(idsCotas);
		
		for (Cota cota : listaCotas) {
			System.out.println(cota.getId());
		}
	}

	@Override
	@Transactional
	public void cobrarCotas(Date dataOperacao, Usuario usuario, List<Long> idsCotas) {

		if (idsCotas == null || idsCotas.isEmpty()) {
			throw new IllegalArgumentException("Lista de ids das cotas não pode ser nula e nem vazia.");
		}
		
		try {
			
			List<Cota> listaCotas = 
				this.cotaRepository.obterCotasPorIDS(idsCotas);
	
			for (Cota cota : listaCotas) {
	
				Distribuidor distribuidor = this.distribuidorService.obter();
				
				TipoMovimentoFinanceiro tipoMovimentoFinanceiro = 
					this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.ENVIO_ENCALHE);
				
				MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO = new MovimentoFinanceiroCotaDTO();
				
				movimentoFinanceiroCotaDTO.setCota(cota);
				movimentoFinanceiroCotaDTO.setTipoMovimentoFinanceiro(tipoMovimentoFinanceiro);
				movimentoFinanceiroCotaDTO.setUsuario(usuario);
				movimentoFinanceiroCotaDTO.setValor(this.buscarValorTotalEncalhe(dataOperacao, cota.getId()));
				movimentoFinanceiroCotaDTO.setDataOperacao(distribuidor.getDataOperacao());
				movimentoFinanceiroCotaDTO.setBaixaCobranca(null);
				movimentoFinanceiroCotaDTO.setDataVencimento(distribuidor.getDataOperacao());
				movimentoFinanceiroCotaDTO.setDataAprovacao(distribuidor.getDataOperacao());
				movimentoFinanceiroCotaDTO.setDataCriacao(distribuidor.getDataOperacao());
				movimentoFinanceiroCotaDTO.setObservacao(null);
				movimentoFinanceiroCotaDTO.setTipoEdicao(TipoEdicao.INCLUSAO);
				movimentoFinanceiroCotaDTO.setAprovacaoAutomatica(true);
				movimentoFinanceiroCotaDTO.setLancamentoManual(false);
				
				this.movimentoFinanceiroCotaService.gerarMovimentoFinanceiroDebitoCredito(movimentoFinanceiroCotaDTO);
	
				this.gerarCobrancaService.gerarCobranca(cota.getId(), usuario.getId(), true);
				
				List<ChamadaEncalhe> listaChamadaEncalhe = 
					this.chamadaEncalheRepository.obterChamadasEncalhePor(dataOperacao, cota.getId());
			
				for (ChamadaEncalhe chamadaEncalhe : listaChamadaEncalhe) {
					
					chamadaEncalhe.setDataRecolhimento(distribuidor.getDataOperacao());
					
					for (ChamadaEncalheCota chamadaEncalheCota : chamadaEncalhe.getChamadaEncalheCotas()) {
						
						chamadaEncalheCota.setFechado(true);
					}

					this.chamadaEncalheRepository.merge(chamadaEncalhe);
				}
			}
			
		} catch (ValidacaoException e) {
			throw new ValidacaoException(e.getValidacao());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional(readOnly=true)
	public BigDecimal buscarValorTotalEncalhe(Date dataEncalhe, Long idCota) {
		
		List<FechamentoFisicoLogicoDTO> list = fechamentoEncalheRepository.buscarValorTotalEncalhe(dataEncalhe, idCota);
        BigDecimal soma = BigDecimal.ZERO;
        
        for (FechamentoFisicoLogicoDTO dto : list) {
               soma = soma.add(dto.getExemplaresDevolucao().multiply(dto.getPrecoCapa()));
        }
        
        return soma;
	}

	@Override
	@Transactional
	public void encerrarOperacaoEncalhe(Date dataEncalhe) {
	
		try {
			
			Integer totalCotasAusentes = 
				this.buscarTotalCotasAusentes(dataEncalhe);
			
			if (totalCotasAusentes > 0) {
				throw new ValidacaoException(TipoMensagem.ERROR, "Cotas ausentes existentes!");
			}
			
			ControleFechamentoEncalhe controleFechamentoEncalhe = new ControleFechamentoEncalhe();
			
			controleFechamentoEncalhe.setDataEncalhe(dataEncalhe);
			
			this.fechamentoEncalheRepository.salvarControleFechamentoEncalhe(controleFechamentoEncalhe);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
