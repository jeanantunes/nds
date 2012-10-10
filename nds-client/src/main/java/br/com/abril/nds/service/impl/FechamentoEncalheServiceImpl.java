package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.AnaliticoEncalheDTO;
import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.exception.GerarCobrancaValidacaoException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.ControleFechamentoEncalhe;
import br.com.abril.nds.model.estoque.FechamentoEncalhe;
import br.com.abril.nds.model.estoque.FechamentoEncalheBox;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalheBoxPK;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalhePK;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.ChamadaEncalheRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.FechamentoEncalheBoxRepository;
import br.com.abril.nds.repository.FechamentoEncalheRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
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
	
	@Autowired
	private ChamadaEncalheCotaRepository chamadaEncalheCotaRepository;
	
	@Autowired
	private FechamentoEncalheBoxRepository fechamentoEncalheBoxRepository;
	
	@Override
	@Transactional
	public List<FechamentoFisicoLogicoDTO> buscarFechamentoEncalhe(FiltroFechamentoEncalheDTO filtro,
			String sortorder, String sortname, Integer page, Integer rp) {
		
		Integer startSearch = null;
		if ( page != null || rp != null ){
			startSearch = page * rp - rp;
		}
		String sort = sortname;
		if (sortname.equals("total")) {
			sort = null;
		}
		
		Boolean fechado = fechamentoEncalheRepository.buscaControleFechamentoEncalhe(filtro.getDataEncalhe());
		List<FechamentoFisicoLogicoDTO> listaConferencia = fechamentoEncalheRepository.buscarConferenciaEncalhe(filtro, sortorder, sort, startSearch, rp);
		if (filtro.getBoxId() == null ){ 
			List<FechamentoEncalhe> listaFechamento = fechamentoEncalheRepository.buscarFechamentoEncalhe(filtro.getDataEncalhe());
			for (FechamentoFisicoLogicoDTO conferencia : listaConferencia) {
				
				conferencia.setTotal(conferencia.getPrecoCapa().multiply(new BigDecimal(conferencia.getExemplaresDevolucao())));
				conferencia.setFechado(fechado);
				
				for (FechamentoEncalhe fechamento : listaFechamento) {
					if (conferencia.getCodigo().equals(fechamento.getFechamentoEncalhePK().getProdutoEdicao().getProduto().getCodigo())) {
						conferencia.setFisico(fechamento.getQuantidade());
						conferencia.setDiferenca(conferencia.getExemplaresDevolucao().longValue() - conferencia.getFisico().longValue());
						break;
					}
				}
			}
			
		} else {
			List<FechamentoEncalheBox> listaFechamentoBox = fechamentoEncalheBoxRepository.buscarFechamentoEncalheBox(filtro);
			for (FechamentoFisicoLogicoDTO conferencia : listaConferencia) {
				
				conferencia.setTotal(new BigDecimal(conferencia.getExemplaresDevolucao()).multiply(conferencia.getPrecoCapa()));
				conferencia.setFechado(fechado);
				
				for (FechamentoEncalheBox fechamento : listaFechamentoBox) {
					if (conferencia.getCodigo().equals(fechamento.getFechamentoEncalheBoxPK().getFechamentoEncalhe().getFechamentoEncalhePK().getProdutoEdicao().getProduto().getCodigo())) {
						conferencia.setFisico(fechamento.getQuantidade());
						conferencia.setDiferenca(conferencia.getExemplaresDevolucao().longValue() - conferencia.getFisico().longValue());
						break;
					}
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
	public void salvarFechamentoEncalhe(FiltroFechamentoEncalheDTO filtro, List<FechamentoFisicoLogicoDTO> listaFechamento) {
		
		
		FechamentoFisicoLogicoDTO fechamento;
		Long qtd;
		
		for (int i=0; i < listaFechamento.size(); i++) {
			
			fechamento = listaFechamento.get(i);
			qtd = fechamento.getFisico();
			
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
				cotaAusenteEncalheDTO.setAcao("Cobrado");
				
			} else if (cotaAusenteEncalheDTO.getPostergado()) {

				Date dataPostergacao = 
					this.fechamentoEncalheRepository.obterChamdasEncalhePostergadas(
						cotaAusenteEncalheDTO.getIdCota(), cotaAusenteEncalheDTO.getDataEncalhe());
				
				cotaAusenteEncalheDTO.setAcao(
					"Postergado, " + DateUtil.formatarData(dataPostergacao, "dd/MM/yyyy"));
			}
		}
		
		return listaCotaAusenteEncalhe;
	}

	@Override
	@Transactional(readOnly=true)
	public Integer buscarTotalCotasAusentes(Date dataEncalhe) {
		
		return this.fechamentoEncalheRepository.buscarTotalCotasAusentes(dataEncalhe);
	}

	@Override
	@Transactional(readOnly=true)
	public int buscarQuantidadeCotasAusentes(Date dataEncalhe) {
		
		List<CotaAusenteEncalheDTO> listaCotaAusenteEncalhe = 
			this.fechamentoEncalheRepository.buscarCotasAusentes(dataEncalhe, "asc", "numeroCota", 0, 0);
		
		int total = 0;
		
		for (CotaAusenteEncalheDTO cotaAusenteEncalheDTO : listaCotaAusenteEncalhe) {
			
			if (cotaAusenteEncalheDTO.getFechado() || cotaAusenteEncalheDTO.getPostergado()) {
				total++;
			}
		}
		
		return total;
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
	public void postergarCotas(Date dataEncalhe, Date dataPostergacao, List<Long> idsCotas) {
	
		if (idsCotas == null || idsCotas.isEmpty()) {
			throw new IllegalArgumentException("Lista de ids das cotas não pode ser nula e nem vazia.");
		}
		
		if (dataEncalhe == null) {
			throw new IllegalArgumentException("Data de encalhe não pode ser nula.");
		}
		
		this.postergar(dataEncalhe, dataPostergacao, idsCotas);
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
				
				this.movimentoFinanceiroCotaService.gerarMovimentosFinanceirosDebitoCredito(movimentoFinanceiroCotaDTO);
	
				this.gerarCobrancaService.gerarCobrancaCota(cota.getId(), usuario.getId(), new HashSet<String>());
				
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
		} catch (GerarCobrancaValidacaoException e) {
			throw e.getValidacaoException();
		} 
	}

	@Override
	@Transactional(readOnly=true)
	public BigDecimal buscarValorTotalEncalhe(Date dataEncalhe, Long idCota) {
		
		List<FechamentoFisicoLogicoDTO> list = fechamentoEncalheRepository.buscarValorTotalEncalhe(dataEncalhe, idCota);
        BigDecimal soma = BigDecimal.ZERO;
        
        for (FechamentoFisicoLogicoDTO dto : list) {
               soma = soma.add(new BigDecimal(dto.getExemplaresDevolucao()).multiply(dto.getPrecoCapa()));
        }
        
        return soma;
	}
	

	@Override
	@Transactional
	public void encerrarOperacaoEncalhe(Date dataEncalhe) {

		Integer totalCotasAusentes = this.buscarQuantidadeCotasAusentes(dataEncalhe);
		
		if (totalCotasAusentes > 0) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Cotas ausentes existentes!");
		}
		
		if (!this.validarEncerramentoOperacao(dataEncalhe)) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Encalhe não totalmente fechado");
		}
		
		try {
			
			ControleFechamentoEncalhe controleFechamentoEncalhe = new ControleFechamentoEncalhe();
			controleFechamentoEncalhe.setDataEncalhe(dataEncalhe);
			
			this.fechamentoEncalheRepository.salvarControleFechamentoEncalhe(controleFechamentoEncalhe);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Transactional
	private boolean validarEncerramentoOperacao(Date dataEncalhe) {
		
		int countFechamento = 0;
		int countConferencia = 0;
		boolean porBox;
		
		List<FechamentoEncalhe> listFechamentoEncalhe = this.fechamentoEncalheRepository.buscarFechamentoEncalhe(dataEncalhe);
		
		if (listFechamentoEncalhe.isEmpty()) {
			// Não tem nada salvo
			return false;
		}
		
		if (listFechamentoEncalhe.get(0).getListFechamentoEncalheBox() == null || listFechamentoEncalhe.get(0).getListFechamentoEncalheBox().isEmpty()) {
			// consolidado
			porBox = false;
			countFechamento = listFechamentoEncalhe.size();
		} else {
			// por box
			porBox = true;
			for (FechamentoEncalhe fechamentoEncalhe : listFechamentoEncalhe) {
				countFechamento += fechamentoEncalhe.getListFechamentoEncalheBox().size();
			}
		}
		
		countConferencia = this.fechamentoEncalheRepository.buscaQuantidadeConferencia(dataEncalhe, porBox);
		
		return countFechamento == countConferencia;
	}
	
	@Transactional
	private void postergar(Date dataEncalhe, Date dataPostergacao, List<Long> cotas) {
		
		ChamadaEncalhe chamadaEncalhe = null;
	
		for (Long idCota : cotas) {
			
			List<ChamadaEncalheCota> listChamadaEncalheCota = this.fechamentoEncalheRepository.buscarChamadaEncalheCota(dataEncalhe, idCota);
			
			for (ChamadaEncalheCota chamadaEncalheCota : listChamadaEncalheCota) {
				
				// Atualizando para postergado
				chamadaEncalheCota.setPostergado(true);
				this.chamadaEncalheCotaRepository.merge(chamadaEncalheCota);
				
				// Criando chamada de encalhe
				chamadaEncalhe = this.chamadaEncalheRepository.obterPorNumeroEdicaoEDataRecolhimento(
						chamadaEncalheCota.getChamadaEncalhe().getProdutoEdicao(), 
						dataPostergacao, 
						chamadaEncalheCota.getChamadaEncalhe().getTipoChamadaEncalhe());
				
				if (chamadaEncalhe == null) {
					
					chamadaEncalhe = new ChamadaEncalhe();
					chamadaEncalhe.setDataRecolhimento(dataPostergacao);
					chamadaEncalhe.setProdutoEdicao(chamadaEncalheCota.getChamadaEncalhe().getProdutoEdicao());
					chamadaEncalhe.setTipoChamadaEncalhe(chamadaEncalheCota.getChamadaEncalhe().getTipoChamadaEncalhe());
					this.chamadaEncalheRepository.adicionar(chamadaEncalhe);
				} 
				
				// Criando novo chamadaEncalheCota
				ChamadaEncalheCota cce = new ChamadaEncalheCota();
				cce.setChamadaEncalhe(chamadaEncalhe);
				cce.setCota(chamadaEncalheCota.getCota());
				cce.setQtdePrevista(chamadaEncalheCota.getQtdePrevista());
				this.chamadaEncalheCotaRepository.adicionar(cce);
			}
		}
	}

	@Override
	@Transactional
	public void salvarFechamentoEncalheBox(FiltroFechamentoEncalheDTO filtro, List<FechamentoFisicoLogicoDTO> listaFechamento) {
		
		
		FechamentoFisicoLogicoDTO fechamento;
		Long qtd;
		for (int i=0; i < listaFechamento.size(); i++) {
			fechamento = listaFechamento.get(i);
			qtd = fechamento.getFisico();
			FechamentoEncalhePK id = new FechamentoEncalhePK();
			id.setDataEncalhe(filtro.getDataEncalhe());
			ProdutoEdicao pe = new ProdutoEdicao();
			pe.setId(fechamento.getProdutoEdicao());
			id.setProdutoEdicao(pe);
			FechamentoEncalheBox fechamentoEncalheBox = new FechamentoEncalheBox();
			
			FechamentoEncalheBoxPK fechamentoEncalheBoxPK =  new  FechamentoEncalheBoxPK();
			Box box = new Box();
			box.setId(filtro.getBoxId());
			fechamentoEncalheBoxPK.setBox(box);
			
			
			FechamentoEncalhe fechamentoEncalhe = fechamentoEncalheRepository.buscarPorId(id);
			if (fechamentoEncalhe == null) {
				fechamentoEncalhe = new FechamentoEncalhe();
				fechamentoEncalhe.setFechamentoEncalhePK(id);
				fechamentoEncalheRepository.adicionar(fechamentoEncalhe);
				fechamentoEncalheBox.setQuantidade(qtd);
				fechamentoEncalheBoxPK.setFechamentoEncalhe(fechamentoEncalhe);
				fechamentoEncalheBox.setFechamentoEncalheBoxPK(fechamentoEncalheBoxPK);
				fechamentoEncalheBoxRepository.adicionar(fechamentoEncalheBox);
				
			} else {
		
				fechamentoEncalheBox =	fechamentoEncalheBoxRepository.buscarPorId(fechamentoEncalheBoxPK);
				if ( fechamentoEncalheBox == null  ) {
					fechamentoEncalheBoxPK.setFechamentoEncalhe(fechamentoEncalhe);
					fechamentoEncalheBox = new FechamentoEncalheBox();
					fechamentoEncalheBox.setFechamentoEncalheBoxPK(fechamentoEncalheBoxPK);
					fechamentoEncalheBox.setQuantidade(qtd);
					
				} else {
					fechamentoEncalheBox.setQuantidade(qtd);
				}
				fechamentoEncalheBoxRepository.merge(fechamentoEncalheBox);
				
			}
			
			fechamento.setFisico(qtd); // retorna valor pra tela
		}
		
		
	}

	@Override
	@Transactional(readOnly=true)
	public Boolean existeFechamentoEncalheDetalhado(FiltroFechamentoEncalheDTO filtro) {
		List<FechamentoEncalhe> listaFechamento = fechamentoEncalheRepository.buscarFechamentoEncalhe(filtro.getDataEncalhe());
		if (listaFechamento == null || listaFechamento.isEmpty() ){
			return Boolean.FALSE;
		} else if (listaFechamento.get(0).getQuantidade() == null ){
			return Boolean.TRUE;
		}
		
		return Boolean.FALSE;
	 
	}

	@Override
	@Transactional(readOnly=true)
	public Boolean existeFechamentoEncalheConsolidado(FiltroFechamentoEncalheDTO filtro) {
		List<FechamentoEncalhe> listaFechamento = fechamentoEncalheRepository.buscarFechamentoEncalhe(filtro.getDataEncalhe());
		if (listaFechamento == null || listaFechamento.isEmpty() ){
			return Boolean.FALSE;
		} else if (listaFechamento.get(0).getQuantidade() != null ){
			return Boolean.TRUE;
		}
		
		return Boolean.FALSE;
	}
	
	
	
	@Transactional
	public void converteFechamentoDetalhadoEmConsolidado(FiltroFechamentoEncalheDTO filtro) {
		List<FechamentoFisicoLogicoDTO> listaConferencia = this.buscarFechamentoEncalhe(filtro, null, "codigo", null, null);
		FechamentoFisicoLogicoDTO fechamento;
		for (int i=0; i < listaConferencia.size(); i++) {
			fechamento = listaConferencia.get(i);
			FechamentoEncalhePK id = new FechamentoEncalhePK();
			id.setDataEncalhe(filtro.getDataEncalhe());
			ProdutoEdicao pe = new ProdutoEdicao();
			pe.setId(fechamento.getProdutoEdicao());
			id.setProdutoEdicao(pe);
			FechamentoEncalhe fechamentoEncalhe = fechamentoEncalheRepository.buscarPorId(id);
			if ( fechamentoEncalhe == null){
				continue;
			}
			
			for(FechamentoEncalheBox encalheBox: fechamentoEncalhe.getListFechamentoEncalheBox() ) {
				if (fechamentoEncalhe.getQuantidade() == null  ){
					fechamentoEncalhe.setQuantidade(encalheBox.getQuantidade());
				} else {
					fechamentoEncalhe.setQuantidade(fechamentoEncalhe.getQuantidade()+encalheBox.getQuantidade());
				}
				fechamentoEncalheBoxRepository.remover(encalheBox);
			}
			
			fechamentoEncalhe.setListFechamentoEncalheBox(null);
			fechamentoEncalheRepository.alterar(fechamentoEncalhe);
		
		}
		
	}
	
	@Transactional
	public void removeFechamentoDetalhado(FiltroFechamentoEncalheDTO filtro) {
		filtro.setBoxId(null);
		filtro.setFornecedorId(null);
		List<FechamentoFisicoLogicoDTO> listaConferencia = this.buscarFechamentoEncalhe(filtro, null, "codigo", null, null);
		FechamentoFisicoLogicoDTO fechamento;
		for (int i=0; i < listaConferencia.size(); i++) {
			fechamento = listaConferencia.get(i);
			FechamentoEncalhePK id = new FechamentoEncalhePK();
			id.setDataEncalhe(filtro.getDataEncalhe());
			ProdutoEdicao pe = new ProdutoEdicao();
			pe.setId(fechamento.getProdutoEdicao());
			id.setProdutoEdicao(pe);
			FechamentoEncalhe fechamentoEncalhe = fechamentoEncalheRepository.buscarPorId(id);
			if ( fechamentoEncalhe == null){
				continue;
			}
			fechamentoEncalheRepository.remover(fechamentoEncalhe);
			
		}
		
	}

	@Override
	@Transactional
	public ControleFechamentoEncalhe buscaControleFechamentoEncalhePorData(Date dataFechamentoEncalhe) {
		return fechamentoEncalheRepository.buscaControleFechamentoEncalhePorData(dataFechamentoEncalhe);
	}

	@Override
	@Transactional
	public Date buscaDataUltimoControleFechamentoEncalhe() {
		return fechamentoEncalheRepository.buscaDataUltimoControleFechamentoEncalhe();
	}

	@Override
	@Transactional
	public Date buscarUltimoFechamentoEncalheDia(Date dataFechamentoEncalhe) {
		return fechamentoEncalheRepository.buscarUltimoFechamentoEncalheDia(dataFechamentoEncalhe);
	}

	@Override
	@Transactional
	public List<AnaliticoEncalheDTO> buscarAnaliticoEncalhe(FiltroFechamentoEncalheDTO filtro, String sortorder, String sortname, Integer page, Integer rp) {
		
		Integer startSearch = null;
		if ( page != null || rp != null ){
			startSearch = page * rp - rp;
		}
		
		return fechamentoEncalheRepository.buscarAnaliticoEncalhe(filtro,  sortorder,  sortname,  startSearch,  rp);
	}
	
	@Override
	@Transactional
	public Integer buscarTotalAnaliticoEncalhe(FiltroFechamentoEncalheDTO filtro) {

		return fechamentoEncalheRepository.buscarTotalAnaliticoEncalhe(filtro);
	}

	@Override
	@Transactional(readOnly=true)
	public Date buscarUtimoDiaDaSemanaRecolhimento() {
		Distribuidor  distribuidor =  distribuidorService.obter();
		
		Integer numeroSemana = DateUtil.obterNumeroSemanaNoAno(new Date());
		Date dataInicioSemana = 
				DateUtil.obterDataDaSemanaNoAno(
					numeroSemana, distribuidor.getInicioSemana().getCodigoDiaSemana(), null);
			
			Date dataFimSemana = DateUtil.adicionarDias(dataInicioSemana, 6);

		return dataFimSemana;
	}

}
