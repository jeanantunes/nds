package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.AnaliticoEncalheDTO;
import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTO;
import br.com.abril.nds.dto.MovimentoEstoqueCotaGenericoDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.GerarCobrancaValidacaoException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Processo;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.ControleFechamentoEncalhe;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.FechamentoEncalhe;
import br.com.abril.nds.model.estoque.FechamentoEncalheBox;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalheBoxPK;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalhePK;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.InformacaoTransporte;
import br.com.abril.nds.model.fiscal.nota.ItemNotaFiscalSaida;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscalReferenciada;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.ChamadaEncalheRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.FechamentoEncalheBoxRepository;
import br.com.abril.nds.repository.FechamentoEncalheRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.ProdutoServicoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.repository.TipoNotaFiscalRepository;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.FechamentoEncalheService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;

@Service
public class FechamentoEncalheServiceImpl implements FechamentoEncalheService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FechamentoEncalheServiceImpl.class);
	
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
	
	@Autowired
	private TipoNotaFiscalRepository tipoNotaFiscalRepository;
	
	@Autowired
	private NotaFiscalService notaFiscalService;
	
	@Autowired
	private NotaFiscalRepository notaFiscalRepository;
	
	@Autowired
	private ProdutoServicoRepository produtoServicoRepository;
	
	@Autowired
	private MovimentoEstoqueService movimentoEstoqueService;
	
	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private DiferencaEstoqueService diferencaEstoqueService;
	
	@Autowired 
	private ProdutoEdicaoRepository edicaoRepository;
	
	@Override
	@Transactional
	public List<FechamentoFisicoLogicoDTO> buscarFechamentoEncalhe(FiltroFechamentoEncalheDTO filtro,
			String sortorder, String sortname, Integer page, Integer rp) {
		
		Integer startSearch = null;
		if ( page != null || rp != null ){
			startSearch = page * rp - rp;
		}
		String sort = sortname;
		if ("total".equals(sortname)) {
			sort = null;
		}
		
		Boolean fechado = fechamentoEncalheRepository.buscaControleFechamentoEncalhe(filtro.getDataEncalhe());
		
		List<FechamentoFisicoLogicoDTO> listaConferencia = 
				fechamentoEncalheRepository.buscarConferenciaEncalhe(filtro, sortorder, sort, startSearch, rp);
		
		Date dataAtual = DateUtil.removerTimestamp(new Date());
		
		int inicioDiaSemana = 
				distribuidorRepository.buscarInicioSemana().getCodigoDiaSemana();
		
		int numeroSemana = 
				DateUtil.obterNumeroSemanaNoAno(dataAtual, inicioDiaSemana);
		
		Date dataFimSemana = 
				DateUtil.adicionarDias(DateUtil.obterDataDaSemanaNoAno(
						numeroSemana, inicioDiaSemana, dataAtual),
						6);
		
		if (filtro.getBoxId() == null ){ 
			List<FechamentoEncalhe> listaFechamento = fechamentoEncalheRepository.buscarFechamentoEncalhe(filtro.getDataEncalhe());
			for (FechamentoFisicoLogicoDTO conferencia : listaConferencia) {
				
				this.setarInfoComumFechamentoFisicoLogicoDTO(conferencia, fechado, dataAtual, dataFimSemana);
				
				for (FechamentoEncalhe fechamento : listaFechamento) {
					if (conferencia.getCodigo().equals(fechamento.getFechamentoEncalhePK().getProdutoEdicao().getProduto().getCodigo())) {
						conferencia.setFisico(fechamento.getQuantidade());
						conferencia.setDiferenca(calcularDiferencao(conferencia));
						break;
					}
				}
			}
			
		} else {
			List<FechamentoEncalheBox> listaFechamentoBox = fechamentoEncalheBoxRepository.buscarFechamentoEncalheBox(filtro);
			for (FechamentoFisicoLogicoDTO conferencia : listaConferencia) {
				
				this.setarInfoComumFechamentoFisicoLogicoDTO(conferencia, fechado, dataAtual, dataFimSemana);
				
				for (FechamentoEncalheBox fechamento : listaFechamentoBox) {
					if (conferencia.getCodigo().equals(fechamento.getFechamentoEncalheBoxPK().getFechamentoEncalhe().getFechamentoEncalhePK().getProdutoEdicao().getProduto().getCodigo())) {
												
						conferencia.setFisico(fechamento.getQuantidade());
												
						conferencia.setDiferenca(calcularDiferencao(conferencia));
						
						break;
					}
				}
			}
		}
		
		if (sort == null) {
			if ("asc".equals(sortorder) ) {
				Collections.sort(listaConferencia, new FechamentoAscComparator());
			} else {
				Collections.sort(listaConferencia, new FechamentoDescComparator());
			}
		}
		
		return listaConferencia;
	}

	private Long calcularDiferencao(FechamentoFisicoLogicoDTO conferencia) {
		 
		if (conferencia.getFisico() != null && conferencia.getExemplaresDevolucao() != null) {	
			return conferencia.getFisico().longValue() - conferencia.getExemplaresDevolucao().longValue() ;
		}
		
		return null;
	}
	
	private void setarInfoComumFechamentoFisicoLogicoDTO(
			FechamentoFisicoLogicoDTO conferencia, 
			boolean fechado,
			Date dataAtual,
			Date dataFimSemana){
		
		conferencia.setTotal(new BigDecimal(conferencia.getExemplaresDevolucao()).multiply(conferencia.getPrecoCapa()));
		conferencia.setFechado(fechado);
		
		  if ("P".equals(conferencia.getTipo())){
			  
			  conferencia.setEstoque(TipoEstoque.LANCAMENTO.getDescricao());
		  } else if (dataAtual.after(DateUtil.removerTimestamp(dataFimSemana)) ){
			  
			  conferencia.setEstoque(TipoEstoque.SUPLEMENTAR.getDescricao());
		  } else {
			  
			  conferencia.setEstoque("Encalhe");
		  }
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
			boolean isSomenteCotasSemAcao, String sortorder, String sortname, int page, int rp) {
		
		int startSearch = 0;
		
		if (page >= 0) {
			startSearch = page * rp - rp;	
		} 
		
		List<CotaAusenteEncalheDTO> listaCotaAusenteEncalhe = 
			this.fechamentoEncalheRepository.buscarCotasAusentes(dataEncalhe, isSomenteCotasSemAcao, sortorder, sortname, startSearch, rp);
		
		if (isSomenteCotasSemAcao) {
			
			return listaCotaAusenteEncalhe;
		}
		
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
	public Integer buscarTotalCotasAusentes(Date dataEncalhe, boolean isSomenteCotasSemAcao) {
		
		return this.fechamentoEncalheRepository.buscarTotalCotasAusentes(dataEncalhe, isSomenteCotasSemAcao);
	}

	@Override
	@Transactional(readOnly=true)
	public int buscarQuantidadeCotasAusentes(Date dataEncalhe) {
		
		List<CotaAusenteEncalheDTO> listaCotaAusenteEncalhe = 
			this.fechamentoEncalheRepository.buscarCotasAusentes(dataEncalhe, false, "asc", "numeroCota", 0, 0);
		
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
		
		for (Long idCota : idsCotas) {
		
			this.postergar(dataEncalhe, dataPostergacao, idCota);
		}
	}
	
	@Override
	@Transactional
	public void postergarTodasCotas(Date dataEncalhe, Date dataPostergacao) {
	
		if (dataEncalhe == null) {
			throw new IllegalArgumentException("Data de encalhe não pode ser nula.");
		}
		
		List<CotaAusenteEncalheDTO> listaCotaAusenteEncalhe = 
				this.fechamentoEncalheRepository.buscarCotasAusentes(dataEncalhe, true, null, null, 0, 0);
		
		for (CotaAusenteEncalheDTO cotaAusente : listaCotaAusenteEncalhe) {
		
			this.postergar(dataEncalhe, dataPostergacao, cotaAusente.getIdCota());
		}
	}

	@Override
	@Transactional
	public void cobrarCotas(Date dataOperacao, Usuario usuario, List<Long> idsCotas) {

		if (idsCotas == null || idsCotas.isEmpty()) {
			throw new IllegalArgumentException("Lista de ids das cotas não pode ser nula e nem vazia.");
		}
		
		List<Cota> listaCotas = 
			this.cotaRepository.obterCotasPorIDS(idsCotas);

		for (Cota cota : listaCotas) {

			realizarCobrancaCotas(dataOperacao, usuario, cota);
		}
	}
	
	@Override
	@Transactional
	public void cobrarTodasCotas(Date dataOperacao, Usuario usuario) {

		List<CotaAusenteEncalheDTO> listaCotaAusenteEncalhe = 
				this.fechamentoEncalheRepository.buscarCotasAusentes(dataOperacao, true, null, null, 0, 0);

		for (CotaAusenteEncalheDTO cotaAusente : listaCotaAusenteEncalhe) {

			Cota cota = this.cotaRepository.buscarPorId(cotaAusente.getIdCota());

			realizarCobrancaCotas(dataOperacao, usuario, cota);
		}
	}
	
	private void realizarCobrancaCotas(Date dataOperacao, Usuario usuario, Cota cota) {

		try {
			
			Date dataOperacaoDistribuidor = this.distribuidorService.obterDataOperacaoDistribuidor();
		
			TipoMovimentoFinanceiro tipoMovimentoFinanceiro = 
				this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.VENDA_TOTAL);
			
			MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO = new MovimentoFinanceiroCotaDTO();
			
			movimentoFinanceiroCotaDTO.setCota(cota);
			movimentoFinanceiroCotaDTO.setTipoMovimentoFinanceiro(tipoMovimentoFinanceiro);
			movimentoFinanceiroCotaDTO.setUsuario(usuario);
			movimentoFinanceiroCotaDTO.setValor(this.buscarValorTotalEncalhe(dataOperacao, cota.getId()));
			movimentoFinanceiroCotaDTO.setDataOperacao(dataOperacaoDistribuidor);
			movimentoFinanceiroCotaDTO.setBaixaCobranca(null);
			movimentoFinanceiroCotaDTO.setDataVencimento(dataOperacaoDistribuidor);
			movimentoFinanceiroCotaDTO.setDataAprovacao(dataOperacaoDistribuidor);
			movimentoFinanceiroCotaDTO.setDataCriacao(dataOperacaoDistribuidor);
			movimentoFinanceiroCotaDTO.setObservacao(null);
			movimentoFinanceiroCotaDTO.setTipoEdicao(TipoEdicao.INCLUSAO);
			movimentoFinanceiroCotaDTO.setAprovacaoAutomatica(true);
			movimentoFinanceiroCotaDTO.setLancamentoManual(false);
			
			this.movimentoFinanceiroCotaService.gerarMovimentosFinanceirosDebitoCredito(movimentoFinanceiroCotaDTO);
	
			this.gerarCobrancaService.gerarCobranca(cota.getId(), usuario.getId(), new HashSet<String>());
			
			List<ChamadaEncalhe> listaChamadaEncalhe = 
				this.chamadaEncalheRepository.obterChamadasEncalhePor(dataOperacao, cota.getId());
			
			TipoMovimentoEstoque tipoMovimentoEstoque =
					this.tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE);
			
			for (ChamadaEncalhe chamadaEncalhe : listaChamadaEncalhe) {
				
				chamadaEncalhe.setDataRecolhimento(dataOperacaoDistribuidor);
				
				List<ChamadaEncalheCota> listaChamadaEncalheCota = 
						this.chamadaEncalheCotaRepository.obterListChamadaEncalheCota(chamadaEncalhe.getId(), cota.getId());
				
				for (ChamadaEncalheCota chamadaEncalheCota : listaChamadaEncalheCota) {
					
					chamadaEncalheCota.setFechado(true);
				}
				
				this.movimentoEstoqueService.gerarMovimentoCota(
						new Date(), 
						chamadaEncalhe.getProdutoEdicao().getId(), 
						cota.getId(), 
						usuario.getId(), 
						BigInteger.ZERO, 
						tipoMovimentoEstoque);
	
				this.chamadaEncalheRepository.merge(chamadaEncalhe);
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
	

	/**
	 * Ao realizar o encerramento do encalhe serão gerados
	 * registros em MovimentoEstoqueCota e MovimentoEstoque
	 * que correspondem ao produtos que as cotas devolveram
	 * ao distribuidor de forma juramentada.
	 * 
	 */
	private void gerarMovimentosDeEstoqueProdutosJuramentados(Date dataEncalhe, Usuario usuario){
		
		List<MovimentoEstoqueCotaGenericoDTO> listaMovimentoEstoqueCota = 
				movimentoEstoqueCotaRepository.obterListaMovimentoEstoqueCotaDevolucaoJuramentada(dataEncalhe);
		
		
		TipoMovimentoEstoque tipoMovEstoqueRecebJornaleiroJuramentado = tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_JORNALEIRO_JURAMENTADO);
		
		TipoMovimentoEstoque tipoMovEstoqueEnvioJornaleiroJuramentado = tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ENVIO_JORNALEIRO_JURAMENTADO);
		
		for(MovimentoEstoqueCotaGenericoDTO movimentoEstoqueCota : listaMovimentoEstoqueCota) {
			
			movimentoEstoqueService.gerarMovimentoCota(
					null, 
					movimentoEstoqueCota.getIdProdutoEdicao(), 
					movimentoEstoqueCota.getIdCota(), 
					usuario.getId(), 
					movimentoEstoqueCota.getQtde(), 
					tipoMovEstoqueRecebJornaleiroJuramentado);
			
			movimentoEstoqueService.gerarMovimentoEstoque(
					null, 
					movimentoEstoqueCota.getIdProdutoEdicao(), 
					usuario.getId(), 
					movimentoEstoqueCota.getQtde(), 
					tipoMovEstoqueEnvioJornaleiroJuramentado);
			
		}
		
	}
	
	@Override
	@Transactional
	public void encerrarOperacaoEncalhe(Date dataEncalhe, Usuario usuario,FiltroFechamentoEncalheDTO filtroSessao)  {

		Integer totalCotasAusentes = this.buscarTotalCotasAusentes(dataEncalhe, true);
		
		if (totalCotasAusentes > 0) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Cotas ausentes existentes!");
		}
		
		ControleFechamentoEncalhe controleFechamentoEncalhe = new ControleFechamentoEncalhe();
		controleFechamentoEncalhe.setDataEncalhe(dataEncalhe);
		
		this.fechamentoEncalheRepository.salvarControleFechamentoEncalhe(controleFechamentoEncalhe);
		
		List<FechamentoFisicoLogicoDTO> listaEncalhe = this.buscarFechamentoEncalhe(filtroSessao, null,null, null, null);
		
		if(!listaEncalhe.isEmpty()){
			
			for(FechamentoFisicoLogicoDTO item : listaEncalhe){
				
				gerarMovimentoFaltasSobras(item,usuario);
				
				//TODO hoje está sendo atualizado na conferencia de encalhe, ver com cesar onde ficara a geração desses movimentos
				//gerarMovimentoEstoqueEntradaDistribuidor(item,usuario);
			}
		}
		
		gerarMovimentosDeEstoqueProdutosJuramentados(dataEncalhe, usuario);
		
		this.gerarNotaFiscal(dataEncalhe);
	}
	
	private void gerarMovimentoEstoqueEntradaDistribuidor(FechamentoFisicoLogicoDTO item,Usuario usuario) {
		
		TipoMovimentoEstoque tipoMovEstoque = tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE);
		tipoMovEstoque.setAprovacaoAutomatica(true);
				
		movimentoEstoqueService.gerarMovimentoEstoque(null, item.getProdutoEdicao(), usuario.getId(), item.getExemplaresDevolucao(), tipoMovEstoque);
	}

	private void gerarMovimentoFaltasSobras(FechamentoFisicoLogicoDTO item, Usuario usuarioLogado) {
		
		BigInteger qntDiferenca = new BigInteger(item.getDiferenca().toString());
		
		if(qntDiferenca.compareTo(BigInteger.ZERO) == 0){
			return;
		}
		
		ProdutoEdicao produtoEdicao = edicaoRepository.buscarPorId(item.getProdutoEdicao());
		
		Diferenca diferenca = new Diferenca();
	
		diferenca.setQtde(qntDiferenca.abs());
		diferenca.setResponsavel(usuarioLogado);
		diferenca.setProdutoEdicao(produtoEdicao);
		
		if( qntDiferenca.compareTo(BigInteger.ZERO ) < 0 ){
		
			diferenca.setTipoDiferenca(TipoDiferenca.FALTA_EM);
			
		} else if(qntDiferenca.compareTo(BigInteger.ZERO) > 0){						
			
			diferenca.setTipoDiferenca(TipoDiferenca.SOBRA_EM);
			
		}
		
		diferencaEstoqueService.lancarDiferenca(diferenca,TipoEstoque.RECOLHIMENTO);
	}

	@Transactional(rollbackFor=Exception.class)
	public void gerarNotaFiscal(Date dataEncalhe)  {
		
		List<TipoNotaFiscal> listaTipoNotaFiscal = this.tipoNotaFiscalRepository.obterTiposNotaFiscal(GrupoNotaFiscal.NF_DEVOLUCAO_REMESSA_CONSIGNACAO);
			
		Distribuidor distribuidor = this.distribuidorService.obter();
		List<Cota> cotas = fechamentoEncalheRepository.buscarCotaFechamentoChamadaEncalhe(dataEncalhe);
		for (Cota cota : cotas) {
		
			try {

				TipoNotaFiscal tipoNotaFiscal = obterTipoNotaFiscal(listaTipoNotaFiscal, cota);
				
				List<ItemNotaFiscalSaida> listItemNotaFiscal = 
						this.notaFiscalService.obterItensNotaFiscalPor(distribuidor,cota, null, null, null, tipoNotaFiscal);
				
				if (listItemNotaFiscal == null || listItemNotaFiscal.isEmpty()) 
					continue;
				
				List<NotaFiscalReferenciada> listaNotasFiscaisReferenciadas = this.notaFiscalService.obterNotasReferenciadas(listItemNotaFiscal);
				
				InformacaoTransporte transporte = this.notaFiscalService.obterTransporte(cota.getId());
				
				Set<Processo> processos = new HashSet<Processo>();
				processos.add(Processo.GERACAO_NF_E);
				
				Long idNotaFiscal = this.notaFiscalService.emitiNotaFiscal(tipoNotaFiscal.getId(), dataEncalhe, cota, 
						listItemNotaFiscal, transporte, null, listaNotasFiscaisReferenciadas, processos, null);
				
				NotaFiscal notaFiscal = this.notaFiscalRepository.buscarPorId(idNotaFiscal);
				
				this.produtoServicoRepository.atualizarProdutosQuePossuemNota(notaFiscal.getProdutosServicos(), listItemNotaFiscal);
				
			} catch (Exception exception) {
				LOGGER.warn(exception.getLocalizedMessage(), exception);
				continue;
			}
		}
	}

	private TipoNotaFiscal obterTipoNotaFiscal(
			List<TipoNotaFiscal> listaTipoNotaFiscal, Cota cota) {
		TipoNotaFiscal  tipoNotaFiscal = null;
		
		Boolean contribuinte = Boolean.FALSE;
		
		if ( cota.getParametrosCotaNotaFiscalEletronica() != null && cota.getParametrosCotaNotaFiscalEletronica().getEmiteNotaFiscalEletronica() != null  ){
			
			contribuinte = cota.getParametrosCotaNotaFiscalEletronica().getEmiteNotaFiscalEletronica();
		}
		
		for (TipoNotaFiscal tipo : listaTipoNotaFiscal){
			if (tipo.isContribuinte() == contribuinte){
				tipoNotaFiscal = tipo;
				break;
			}
		}
		return tipoNotaFiscal;
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
	private void postergar(Date dataEncalhe, Date dataPostergacao, Long idCota) {
		
		List<ChamadaEncalheCota> listChamadaEncalheCota = this.fechamentoEncalheRepository.buscarChamadaEncalheCota(dataEncalhe, idCota);

		for (ChamadaEncalheCota chamadaEncalheCota : listChamadaEncalheCota) {
			
			// Atualizando para postergado
			chamadaEncalheCota.setPostergado(true);
			this.chamadaEncalheCotaRepository.merge(chamadaEncalheCota);
			
			// Criando chamada de encalhe
			ChamadaEncalhe chamadaEncalhe = this.chamadaEncalheRepository.obterPorNumeroEdicaoEDataRecolhimento(
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
