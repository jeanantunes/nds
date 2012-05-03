package br.com.abril.nds.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CaracteristicaDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.PeriodoFuncionamentoDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroPdvDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.LicencaMunicipal;
import br.com.abril.nds.model.cadastro.MaterialPromocional;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TipoLicencaMunicipal;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.cadastro.pdv.AreaInfluenciaPDV;
import br.com.abril.nds.model.cadastro.pdv.CaracteristicasPDV;
import br.com.abril.nds.model.cadastro.pdv.ClusterPDV;
import br.com.abril.nds.model.cadastro.pdv.EspecialidadePDV;
import br.com.abril.nds.model.cadastro.pdv.GeradorFluxoPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.PeriodoFuncionamentoPDV;
import br.com.abril.nds.model.cadastro.pdv.SegmentacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.TelefonePDV;
import br.com.abril.nds.model.cadastro.pdv.TipoEstabelecimentoAssociacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoGeradorFluxoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPeriodoFuncionamentoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.repository.AreaInfluenciaPDVRepository;
import br.com.abril.nds.repository.ClusterPDVRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EspecialidadePDVRepository;
import br.com.abril.nds.repository.GeradorFluxoPDVRepository;
import br.com.abril.nds.repository.MaterialPromocionalRepository;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.repository.PeriodoFuncionamentoPDVRepository;
import br.com.abril.nds.repository.TelefonePdvRepository;
import br.com.abril.nds.repository.TipoGeradorFluxoPDVRepsitory;
import br.com.abril.nds.repository.TipoLicencaMunicipalRepository;
import br.com.abril.nds.repository.TipoPontoPDVRepository;
import br.com.abril.nds.repository.TiposEstabelecimentoRepository;
import br.com.abril.nds.service.PdvService;
import br.com.abril.nds.service.TelefoneService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class PdvServiceImpl implements PdvService {

	@Autowired
	private PdvRepository pdvRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private EspecialidadePDVRepository especialidadePDVRepository;
	
	@Autowired
	private GeradorFluxoPDVRepository fluxoPDVRepository;
	
	@Autowired
	private TipoGeradorFluxoPDVRepsitory tipoGeradorFluxoPDVRepsitory;
	
	@Autowired
	private MaterialPromocionalRepository materialPromocionalRepository;
	
	@Autowired
	private PeriodoFuncionamentoPDVRepository periodoFuncionamentoPDVRepository;
	
	@Autowired
	private AreaInfluenciaPDVRepository areaInfluenciaPDVRepository;
	
	@Autowired
	private ClusterPDVRepository clusterPDVRepository;
	
	@Autowired
	private TipoPontoPDVRepository tipoPontoPDVRepository;
	
	@Autowired
	private TipoLicencaMunicipalRepository tipoLicencaMunicipalRepository;
	
	@Autowired
	private TiposEstabelecimentoRepository tiposEstabelecimentoRepository;
	
	@Autowired
	private ParametroSistemaRepository parametroSistemaRepository;
	
	@Autowired
	private TelefonePdvRepository telefonePdvRepository;
	
	@Autowired
	private TelefoneService telefoneService;
	
	@Transactional(readOnly=true)
	@Override
	public List<TipoPontoPDV> obterTiposPontoPDV(){
		
		return tipoPontoPDVRepository.buscarTodos();
	}
	
	@Transactional(readOnly=true)
	@Override
	public List<AreaInfluenciaPDV> obterAreasInfluenciaPDV(){
		
		return areaInfluenciaPDVRepository.buscarTodos(); 
	}
	
	@Transactional(readOnly=true)
	@Override
	public List<ClusterPDV> obterClustersPDV(){
		
		return clusterPDVRepository.buscarTodos();
	}
	
	@Transactional(readOnly=true)
	@Override
	public List<EspecialidadePDV> obterEspecialidadesPDV(Long... codigos){
		
		if(codigos.length == 0){
			
			return especialidadePDVRepository.buscarTodos();
		}
		
		return especialidadePDVRepository.obterEspecialidades(codigos);
	}
	
	@Transactional(readOnly=true)
	@Override
	public List<EspecialidadePDV> obterEspecialidadesPDVNotIn(Long... codigos){
		
		if(codigos.length > 0){
			
			return especialidadePDVRepository.obterEspecialidadesNotIn(codigos);
		}
		
		return new ArrayList<EspecialidadePDV>();
	}
	
	@Transactional(readOnly=true)
	@Override
	public List<TipoGeradorFluxoPDV> obterTiposGeradorFluxo(Long... codigos){
		
		if(codigos.length == 0){
			
			return tipoGeradorFluxoPDVRepsitory.buscarTodos();
		}
		
		return tipoGeradorFluxoPDVRepsitory.obterTiposGeradorFluxo(codigos);
	}
	
	@Transactional(readOnly=true)
	@Override
	public List<TipoGeradorFluxoPDV> obterTiposGeradorFluxoNotIn(Long... codigos) {
		
		if(codigos.length > 0){
			
			return tipoGeradorFluxoPDVRepsitory.obterTiposGeradorFluxoNotIn(codigos);
		}
		
		return new ArrayList<TipoGeradorFluxoPDV>();
	}
	
	@Transactional(readOnly=true)
	@Override
	public List<MaterialPromocional> obterMateriaisPromocionalPDV(Long...codigos){
		
		if(codigos.length == 0){
			
			return materialPromocionalRepository.buscarTodos();
		}
		
		return materialPromocionalRepository.obterMateriaisPromocional(codigos);
	}
	
	@Transactional(readOnly=true)
	@Override
	public List<MaterialPromocional> obterMateriaisPromocionalPDVNotIn(Long... codigos){
		
		if(codigos.length > 0){
			return materialPromocionalRepository.obterMateriaisPromocionalNotIn(codigos);
		}
		
		return new ArrayList<MaterialPromocional>();
	}
	
	@Transactional(readOnly=true)
	@Override
	public List<TipoEstabelecimentoAssociacaoPDV> obterTipoEstabelecimentoAssociacaoPDV() {
		
		return tiposEstabelecimentoRepository.buscarTodos();
	}
	
	@Transactional(readOnly=true)
	@Override
	public List<TipoLicencaMunicipal> obterTipoLicencaMunicipal() {
		
		return tipoLicencaMunicipalRepository.buscarTodos();
	}
	
	@Transactional(readOnly=true)
	@Override
	public List<PdvDTO> obterPDVsPorCota(FiltroPdvDTO filtro) {
		
		return pdvRepository.obterPDVsPorCota(filtro);
	}
	
	@Transactional(readOnly=true)
	@Override
	public PdvDTO obterPDV(Long idCota, Long idPdv){
		
		PDV pdv = pdvRepository.obterPDV(idCota, idPdv);
		
		PdvDTO pdvDTO = new PdvDTO();
		
		if(pdv!= null){
			
			atribuirValorDadosBasico(pdv, pdvDTO);
			atribuirValorCaracteristica(pdv, pdvDTO);
			atribuirValorEspecialidade(pdv, pdvDTO);
			atribuirValorGeradorFluxo(pdv, pdvDTO);
			atribuirValorMaterialPromocional(pdv, pdvDTO);
		}
		
		return pdvDTO;
	}
	
	
	
	@Transactional
	@Override
	public void excluirPDV(Long idPdv){
		
		PDV pdv = pdvRepository.buscarPorId(idPdv);
		
		//TODO verificar se o PDV esta associado a uma Roterização
		
		if(pdvRepository.obterQntPDV() == 1 ){
			throw new ValidacaoException(TipoMensagem.WARNING,"PDV não pode ser excluido!");
		}
		
		if( pdv.getCaracteristicas()!= null &&  pdv.getCaracteristicas().isPontoPrincipal()){
			throw new ValidacaoException(TipoMensagem.WARNING,"PDV não pode ser excluido! Pelomenos um PDV deve ser um Ponto Principal");
		}
		
		if(pdv!= null){
			pdvRepository.remover(pdv);
		}
	}
	
	@Transactional
	@Override
	public void salvar(PdvDTO pdvDTO){
		
		if(pdvDTO == null){
			throw new ValidacaoException(TipoMensagem.ERROR,"Parâmetro PDV inválido");
		}
		
		if(pdvDTO.getNomePDV() == null || pdvDTO.getNomePDV().isEmpty() ){
			throw new ValidacaoException(TipoMensagem.WARNING,"Nome do PDV deve ser informado!");
		}

		if(pdvDTO.getIdCota() == null){
			throw new ValidacaoException(TipoMensagem.ERROR,"Parâmetro Cota PDV inválido");
		}
	
		Cota cota  = obterCotaPDV(pdvDTO);
		
		if(cota == null){
			throw new ValidacaoException(TipoMensagem.ERROR,"Não foi encontrado Cota para inclusão do PDV.");
		}
	
		salvarPDV(pdvDTO, cota);	
	}
	
	private void salvarPDV(PdvDTO pdvDTO, Cota cota){
		
		PDV pdv = null;
		
		if(pdvDTO.getId()!= null){
			pdv = pdvRepository.buscarPorId(pdvDTO.getId());
		}
		
		if(pdv == null){
			pdv = new PDV();
			pdv.setDataInclusao(new Date());
			
			if( pdvDTO.getCaracteristicaDTO()!= null && pdvDTO.getCaracteristicaDTO().isPontoPrincipal()){
				
				if(pdvRepository.existePDVPrincipal()){
					throw new ValidacaoException(TipoMensagem.WARNING,"PDV não pode ser incluído! Já existe PDV incluído como principal.");
				}
			}
		}
		
		if( pdvDTO.getCaracteristicaDTO().isPontoPrincipal()){
		
			if(pdv.getCaracteristicas()!= null && !pdv.getCaracteristicas().isPontoPrincipal()){
				
				if(pdvRepository.existePDVPrincipal()){
					throw new ValidacaoException(TipoMensagem.WARNING,"PDV não pode der incluído! Já existe PDV incluído como principal.");
				}
			}
		}
	
		pdv.setCota(cota);
		pdv.setNome(pdvDTO.getNomePDV());
		pdv.setContato(pdvDTO.getContato());
		pdv.setPontoReferencia(pdvDTO.getPontoReferencia());
		pdv.setEmail(pdvDTO.getEmail());
		pdv.setPorcentagemFaturamento(pdvDTO.getPorcentagemFaturamento());
		pdv.setPossuiSistemaIPV(pdvDTO.isSistemaIPV());
		pdv.setQtdeFuncionarios(pdvDTO.getQtdeFuncionarios());
		pdv.setSite(pdvDTO.getSite());
		pdv.setStatus(pdvDTO.getStatusPDV());
		pdv.setTamanhoPDV(pdvDTO.getTamanhoPDV());
		pdv.setDentroOutroEstabelecimento(pdvDTO.isDentroOutroEstabelecimento());
		pdv.setEspecialidades(obterEspecialidadesPDV(pdvDTO));
		pdv.setLicencaMunicipal(obterLicencaMunicipalPDV(pdvDTO,pdv));
		pdv.setCaracteristicas(obterCaracteristicaPDV(pdvDTO,pdv));
		pdv.setMateriais(obterMateriaisPDV(pdvDTO));
		pdv.setSegmentacao(obterSegmentacaoPDV(pdvDTO,pdv));
		pdv.setExpositor(pdvDTO.isExpositor());
		pdv.setTipoExpositor(pdvDTO.getTipoExpositor());
		
		if(pdvDTO.isDentroOutroEstabelecimento() && pdvDTO.getTipoEstabelecimentoAssociacaoPDV()!= null){
			
			Long codigo = pdvDTO.getTipoEstabelecimentoAssociacaoPDV().getCodigo();
			
			TipoEstabelecimentoAssociacaoPDV tipoEstabelecimentoAssociacaoPDV = tiposEstabelecimentoRepository.obterTipoEstabelecimentoAssociacaoPDV(codigo);
			
			if(tipoEstabelecimentoAssociacaoPDV!=null){
				pdv.setTipoEstabelecimentoPDV(tipoEstabelecimentoAssociacaoPDV);
			}

		}
		
		pdv =  pdvRepository.merge(pdv);
		
		if(pdvDTO.getImagem() != null) 
			atualizaImagemPDV(pdvDTO.getImagem(),pdv.getId());
		
		salvarPeriodoFuncionamentoPDV(pdvDTO, pdv);
		
		salvarGeradorFluxo(pdvDTO, pdv);
	
		//salvarEndereco(pdvDTO, pdv);
		processarTelefones(pdvDTO, pdv);
	}
	
		
	public void atualizaImagemPDV(InputStream foto, Long idPdv) {
		
		//TODO validar código
		
		ParametroSistema path = 
				this.parametroSistemaRepository.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_IMAGENS_PDV);
		
		
		String dirFile = path.getValor().replace("\\", "/") + "pdv_" + idPdv + ".jpeg"; 
		
		File fileArquivo = new File(dirFile);
		   		
		if(fileArquivo.exists())
			fileArquivo.delete();
		
		FileOutputStream fos = null;
		
		fileArquivo = new File(dirFile);
		
		try {
						
			fos = new FileOutputStream(fileArquivo);
			
			((FileInputStream)foto).getChannel().size();
			
			IOUtils.copyLarge(foto, fos);
			
		} catch (Exception e) {
			
			throw new ValidacaoException(TipoMensagem.ERROR,
				"Falha ao gravar o arquivo em disco!");
		
		} finally {
			try { 
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new ValidacaoException(TipoMensagem.ERROR,
					"Falha ao gravar o arquivo em disco!");
			}
		}
		
	}

	private void salvarEndereco(PdvDTO pdvDTO,PDV pdv){
		
	}
	
	@Transactional
	private void processarTelefones(PdvDTO pdvDTO,PDV pdv){
		
		List<Telefone> listaTelefones = new ArrayList<Telefone>();
		if (pdvDTO.getTelefonesAdicionar() != null){
			for (TelefoneAssociacaoDTO telefoneAssociacaoDTO : pdvDTO.getTelefonesAdicionar()){
				listaTelefones.add(telefoneAssociacaoDTO.getTelefone());
			}
		}
		
		if (!listaTelefones.isEmpty()){
			pdv.getCota().getPessoa().setTelefones(listaTelefones);
		}	
		
		this.salvarTelefonesPdv(pdv, pdvDTO.getTelefonesAdicionar());
		
		this.removerTelefonesPdv(pdvDTO.getTelefonesRemover());
	}

	private void salvarTelefonesPdv(PDV pdv, List<TelefoneAssociacaoDTO> listaTelefones) {
		
		this.telefoneService.cadastrarTelefone(listaTelefones);
		
		if (listaTelefones != null){
			
			for (TelefoneAssociacaoDTO dto : listaTelefones){
				
				TelefonePDV telefonePdv = this.telefonePdvRepository.obterTelefonePorTelefonePdv(dto.getTelefone().getId(), pdv.getId());
				
				if (telefonePdv == null){
					telefonePdv = new TelefonePDV();
					
					telefonePdv.setPdv(pdv);
					telefonePdv.setPrincipal(dto.isPrincipal());
					telefonePdv.setTelefone(dto.getTelefone());
					telefonePdv.setTipoTelefone(dto.getTipoTelefone());
					
					this.telefonePdvRepository.adicionar(telefonePdv);
				} else {
					
					telefonePdv.setPrincipal(dto.isPrincipal());
					telefonePdv.setTipoTelefone(dto.getTipoTelefone());
					
					this.telefonePdvRepository.alterar(telefonePdv);
				}
			}
		}
	}

	private void removerTelefonesPdv(Collection<Long> listaTelefones) {
		
		if (listaTelefones != null && !listaTelefones.isEmpty()){
			this.telefonePdvRepository.removerTelefonesPdv(listaTelefones);
			
			this.telefoneService.removerTelefones(listaTelefones);
		}
	}
	
	private void salvarGeradorFluxo(PdvDTO pdvDTO, PDV pdv){
		
		GeradorFluxoPDV geradorFluxoPDV = obterGeradorFluxoPDV(pdvDTO, pdv);
		
		if (geradorFluxoPDV!= null){
			fluxoPDVRepository.merge(geradorFluxoPDV);
		}
	}
	
	private void salvarPeriodoFuncionamentoPDV(PdvDTO pdvDTO,PDV pdv){
		
		if(pdv.getPeriodos()!= null && !pdv.getPeriodos().isEmpty()){
			
			for(PeriodoFuncionamentoPDV periodo : pdv.getPeriodos() ){
				periodoFuncionamentoPDVRepository.remover(periodo);
			}
		}
		
		if( pdvDTO.getPeriodosFuncionamentoDTO()!= null){
			
			PeriodoFuncionamentoPDV periodo = null;
			
			for(PeriodoFuncionamentoDTO periodoDTO : pdvDTO.getPeriodosFuncionamentoDTO()){
				
				periodo = new PeriodoFuncionamentoPDV();
				periodo.setHorarioFim(DateUtil.parseData(periodoDTO.getFim(),"HH:mm"));
				periodo.setHorarioInicio(DateUtil.parseData(periodoDTO.getInicio(),"HH:mm"));
				periodo.setTipoPeriodoFuncionamentoPDV(periodoDTO.getTipoPeriodoFuncionamentoPDV());
				periodo.setPdv(pdv);
				
				periodoFuncionamentoPDVRepository.adicionar(periodo);
			}
		}
	}

	private SegmentacaoPDV obterSegmentacaoPDV(PdvDTO pdvDTO,PDV pdv) {
		
		SegmentacaoPDV segmaSegmentacaoPDV = pdv.getSegmentacao();
		
		if(segmaSegmentacaoPDV == null){
			segmaSegmentacaoPDV = new SegmentacaoPDV();
		}
		
		CaracteristicaDTO caracteristicaDTO = pdvDTO.getCaracteristicaDTO();
		AreaInfluenciaPDV areaInfluenciaPDV = null;
		ClusterPDV clusterPDV = null;
		TipoPontoPDV tipoPontoPDV = null;
		
		if(pdvDTO.getCaracteristicaDTO()!= null){
			
			if(caracteristicaDTO.getAreaInfluencia()!= null){
				areaInfluenciaPDV = areaInfluenciaPDVRepository.buscarPorId(caracteristicaDTO.getAreaInfluencia());
			}
			
			if(caracteristicaDTO.getCluster()!= null){
				clusterPDV = clusterPDVRepository.buscarPorId(caracteristicaDTO.getCluster());
			}
			
			if(caracteristicaDTO.getTipoPonto()!= null){
				tipoPontoPDV = tipoPontoPDVRepository.buscarPorId(caracteristicaDTO.getTipoPonto());
			}
		}
			
		segmaSegmentacaoPDV.setAreaInfluenciaPDV(areaInfluenciaPDV);
		segmaSegmentacaoPDV.setClusterPDV(clusterPDV);
		segmaSegmentacaoPDV.setTipoCaracteristica(caracteristicaDTO.getTipoCaracteristicaSegmentacaoPDV());
		segmaSegmentacaoPDV.setTipoPontoPDV(tipoPontoPDV);
		
		return segmaSegmentacaoPDV;
	}

	
	private Set<MaterialPromocional> obterMateriaisPDV(PdvDTO pdvDTO) {
		
		Set<MaterialPromocional> materialPromocional = new HashSet<MaterialPromocional>();
		
		if(pdvDTO.getMaps()!= null){

			materialPromocional.addAll( materialPromocionalRepository.obterMateriaisPromocional((pdvDTO.getMaps().toArray(new Long[]{}))));
			
		}
		
		return materialPromocional;
	}

	private LicencaMunicipal obterLicencaMunicipalPDV(PdvDTO pdvDTO,PDV pdv) {
		
		LicencaMunicipal licencaMunicipal = pdv.getLicencaMunicipal();
		
		if(licencaMunicipal == null){
			licencaMunicipal = new LicencaMunicipal();
		}
		
		TipoLicencaMunicipal tipoLicencaMunicipal = null;
		
		if(pdvDTO.getTipoLicencaMunicipal()!= null){
			tipoLicencaMunicipal = tipoLicencaMunicipalRepository.obterTipoLicencaMunicipal(pdvDTO.getTipoLicencaMunicipal().getId());
		}
		
		licencaMunicipal.setNomeLicenca(pdvDTO.getNomeLicenca());
		licencaMunicipal.setNumeroLicenca(pdvDTO.getNumeroLicenca());
		licencaMunicipal.setTipoLicencaMunicipal(tipoLicencaMunicipal);
		
		return licencaMunicipal;
	}
	
	private GeradorFluxoPDV obterGeradorFluxoPDV(PdvDTO pdvDTO, PDV pdv){
		
		TipoGeradorFluxoPDV fluxoPrincipal = null;
		
		if(pdvDTO.getGeradorFluxoPrincipal()!= null){
			fluxoPrincipal = tipoGeradorFluxoPDVRepsitory.buscarPorId(pdvDTO.getGeradorFluxoPrincipal());
		}
		
		Set<TipoGeradorFluxoPDV> fluxoSecundario = null;
		
		if(pdvDTO.getGeradorFluxoSecundario()!= null){
			
			fluxoSecundario = new HashSet<TipoGeradorFluxoPDV>();
			fluxoSecundario.addAll(tipoGeradorFluxoPDVRepsitory.obterTiposGeradorFluxo(pdvDTO.getGeradorFluxoSecundario().toArray(new Long[]{})));
		}
		
		if(fluxoPrincipal == null && fluxoSecundario == null ){
			return null;
		}
		
		GeradorFluxoPDV fluxoPDV = pdv.getGeradorFluxoPDV();
		
		if(fluxoPDV == null){
			fluxoPDV = new GeradorFluxoPDV();
		}
		
		fluxoPDV.setPrincipal(fluxoPrincipal);
		fluxoPDV.setSecundarios(fluxoSecundario);
		fluxoPDV.setPdv(pdv);
	
		return fluxoPDV;
	}
	
	private Set<EspecialidadePDV> obterEspecialidadesPDV(PdvDTO pdvDTO) {
		
		Set<EspecialidadePDV> especialidadePDVs = new HashSet<EspecialidadePDV>();
		
		if(pdvDTO.getEspecialidades() != null){
			especialidadePDVs.addAll(especialidadePDVRepository.obterEspecialidades((pdvDTO.getEspecialidades().toArray(new Long[]{}))));
		}
		
		return especialidadePDVs;
	}

	private Cota obterCotaPDV(PdvDTO pdvDTO) {
		
		return cotaRepository.buscarPorId(pdvDTO.getIdCota());
	}

	private CaracteristicasPDV obterCaracteristicaPDV(PdvDTO pdvDTO,PDV pdv) {
		
		CaracteristicaDTO carct = pdvDTO.getCaracteristicaDTO();
		
		CaracteristicasPDV caracteristicasPDV = pdv.getCaracteristicas(); 
		
		if(caracteristicasPDV == null){
			caracteristicasPDV = new CaracteristicasPDV();
		}
		
		caracteristicasPDV.setBalcaoCentral(carct.isBalcaoCentral());
		caracteristicasPDV.setPontoPrincipal(carct.isPontoPrincipal());
		caracteristicasPDV.setPossuiComputador(carct.isTemComputador());
		caracteristicasPDV.setPossuiLuminoso(carct.isLuminoso());
		
		if(carct.isLuminoso()){
			caracteristicasPDV.setTextoLuminoso(carct.getTextoLuminoso());
		}
		else{
			caracteristicasPDV.setTextoLuminoso(null);
		}
		
		return caracteristicasPDV;
	}
	
	private void atribuirValorDadosBasico(PDV pdv, PdvDTO pdvDTO){
		
		pdvDTO.setId(pdv.getId());
		pdvDTO.setStatusPDV(pdv.getStatus());
		pdvDTO.setNomePDV(pdv.getNome());
		pdvDTO.setContato(pdv.getContato());
		pdvDTO.setDataInicio(pdv.getDataInclusao());
		pdvDTO.setSite(pdv.getSite());
		pdvDTO.setEmail(pdv.getEmail());
		pdvDTO.setPontoReferencia(pdv.getPontoReferencia());
		pdvDTO.setDentroOutroEstabelecimento(pdv.isDentroOutroEstabelecimento());
		pdvDTO.setTipoEstabelecimentoAssociacaoPDV(pdv.getTipoEstabelecimentoPDV());
		pdvDTO.setTamanhoPDV(pdv.getTamanhoPDV());
		pdvDTO.setSistemaIPV(pdv.isPossuiSistemaIPV());
		pdvDTO.setQtdeFuncionarios(pdv.getQtdeFuncionarios());
		pdvDTO.setPorcentagemFaturamento(pdv.getPorcentagemFaturamento());
	
		LicencaMunicipal licencaMunicipal = pdv.getLicencaMunicipal();
		
		if(licencaMunicipal!= null){
			pdvDTO.setNumeroLicenca(licencaMunicipal.getNumeroLicenca());
			pdvDTO.setNomeLicenca(licencaMunicipal.getNomeLicenca());
			pdvDTO.setTipoLicencaMunicipal(licencaMunicipal.getTipoLicencaMunicipal());
		}
		
		Set<PeriodoFuncionamentoPDV>periodos = pdv.getPeriodos();
		
		List<PeriodoFuncionamentoDTO> listaPeriodos = new ArrayList<PeriodoFuncionamentoDTO>();
		
		if(periodos!= null && !periodos.isEmpty()){

			for(PeriodoFuncionamentoPDV periodo : periodos  ){
				
				listaPeriodos.add(new PeriodoFuncionamentoDTO(periodo.getTipoPeriodoFuncionamentoPDV(),
						DateUtil.formatarData(periodo.getHorarioInicio(),"HH:mm"),
						DateUtil.formatarData(periodo.getHorarioFim(),"HH:mm")));
			}				
		}
		
		pdvDTO.setPeriodosFuncionamentoDTO(listaPeriodos);
	}
	
	private void atribuirValorCaracteristica(PDV pdv, PdvDTO pdvDTO){
		
		CaracteristicaDTO caracteristicaDTO = new CaracteristicaDTO();
		
		if(pdv.getSegmentacao()!= null){

			caracteristicaDTO.setAreaInfluencia(pdv.getSegmentacao().getAreaInfluenciaPDV().getCodigo());
			caracteristicaDTO.setCluster(pdv.getSegmentacao().getClusterPDV().getCodigo());
			caracteristicaDTO.setTipoCaracteristicaSegmentacaoPDV(pdv.getSegmentacao().getTipoCaracteristica());
			caracteristicaDTO.setTipoPonto(pdv.getSegmentacao().getTipoPontoPDV().getCodigo());
		}
		
		if(pdv.getCaracteristicas()!= null){
			
			caracteristicaDTO.setBalcaoCentral(pdv.getCaracteristicas().isBalcaoCentral());
			caracteristicaDTO.setLuminoso(pdv.getCaracteristicas().isPossuiLuminoso());
			caracteristicaDTO.setPontoPrincipal(pdv.getCaracteristicas().isPontoPrincipal());
			caracteristicaDTO.setTemComputador(pdv.getCaracteristicas().isPossuiComputador());
			caracteristicaDTO.setTextoLuminoso(pdv.getCaracteristicas().getTextoLuminoso());
		}
		
		pdvDTO.setCaracteristicaDTO(caracteristicaDTO);
	}
	
	private void atribuirValorEspecialidade(PDV pdv, PdvDTO pdvDTO){
		
		pdvDTO.setEspecialidades(new ArrayList<Long>());
		
		if(pdv.getEspecialidades()!= null && !pdv.getEspecialidades().isEmpty()){
			
			for(EspecialidadePDV esp: pdv.getEspecialidades()){
				pdvDTO.getEspecialidades().add(esp.getCodigo());
			}
		}
	}
	
	private void atribuirValorGeradorFluxo(PDV pdv, PdvDTO pdvDTO){
		
		pdvDTO.setGeradorFluxoSecundario(new ArrayList<Long>());
		
		if(pdv.getGeradorFluxoPDV()!= null){
			
			if(pdv.getGeradorFluxoPDV().getPrincipal()!= null){
				pdvDTO.setGeradorFluxoPrincipal(pdv.getGeradorFluxoPDV().getPrincipal().getCodigo());
			}
			
			if(pdv.getGeradorFluxoPDV().getSecundarios()!= null && !pdv.getGeradorFluxoPDV().getSecundarios().isEmpty() ){
				
				for(TipoGeradorFluxoPDV flx: pdv.getGeradorFluxoPDV().getSecundarios()){
					pdvDTO.getGeradorFluxoSecundario().add(flx.getCodigo());
				}
			}
		}
	}
	
	private void atribuirValorMaterialPromocional(PDV pdv, PdvDTO pdvDTO){
		
		pdvDTO.setMaps(new ArrayList<Long>());
		
		if(pdv.getMateriais()!= null && !pdv.getMateriais().isEmpty() ){
			
			for(MaterialPromocional mat: pdv.getMateriais()){
				pdvDTO.getMaps().add(mat.getCodigo());
			}
		}
		
		pdvDTO.setExpositor(pdv.isExpositor());
		pdvDTO.setTipoExpositor(pdv.getTipoExpositor());
	}
	
	/**
	 * Obtém lista com os possíveis peridos a serem selecionados
	 * 
	 * @param selecionados - Periodos já selecionados
	 * @return - períodos que ainda podem ser selecionados
	 */
	public List<TipoPeriodoFuncionamentoPDV> getPeriodosPossiveis(List<PeriodoFuncionamentoDTO> selecionados) {
		
		List<TipoPeriodoFuncionamentoPDV> possiveis = new ArrayList<TipoPeriodoFuncionamentoPDV>();
		
		for(TipoPeriodoFuncionamentoPDV periodo: TipoPeriodoFuncionamentoPDV.values()) {
			
			try{
				selecionados.add(new PeriodoFuncionamentoDTO(periodo, null,null));
				validarPeriodos(selecionados);
				selecionados.remove(selecionados.size() - 1);
				
				possiveis.add(periodo);
			} catch (Exception e) {
				selecionados.remove(selecionados.size() - 1);
			}
		}
		return possiveis;
	}
	
	/**
	 * Valida se uma lista de períodos é valida, de acordo com as regras definidas na EMS 0159
	 * 
	 * @param listaTipos
	 * @throws Exception
	 */
	public void validarPeriodos(List<PeriodoFuncionamentoDTO> periodos) throws Exception {
		
		List<TipoPeriodoFuncionamentoPDV> listaTipos = new ArrayList<TipoPeriodoFuncionamentoPDV>();
		
		for(PeriodoFuncionamentoDTO p : periodos) {
			listaTipos.add(p.getTipoPeriodoFuncionamentoPDV());
		}
		
		validarDuplicidadeDePeriodo(listaTipos);
		
		if (listaTipos.contains(TipoPeriodoFuncionamentoPDV.DIARIA)) {
			
			if(listaTipos.size()>1) {
				
				throw new Exception("Ao selecionar " + TipoPeriodoFuncionamentoPDV.DIARIA.getDescricao() + ", nenhum outro item deve ser incluido.");
			} 
		
		}
		
		if (listaTipos.contains(TipoPeriodoFuncionamentoPDV.VINTE_QUATRO_HORAS)) {
			
			if(listaTipos.size() > 1) {
				
				throw new Exception("Ao selecionar " + TipoPeriodoFuncionamentoPDV.VINTE_QUATRO_HORAS.getDescricao() + ", nenhum outro item deve ser incluido.");
			} 
		
		} 
		
		if (listaTipos.contains(TipoPeriodoFuncionamentoPDV.SEGUNDA_SEXTA)) {
			
			if (listaTipos.contains(TipoPeriodoFuncionamentoPDV.SEGUNDA_FEIRA)
				|| listaTipos.contains(TipoPeriodoFuncionamentoPDV.TERCA_FEIRA)
				|| listaTipos.contains(TipoPeriodoFuncionamentoPDV.QUARTA_FEIRA)
				|| listaTipos.contains(TipoPeriodoFuncionamentoPDV.QUINTA_FEIRA)
				|| listaTipos.contains(TipoPeriodoFuncionamentoPDV.SEXTA_FEIRA)) {
				
				throw new Exception("Ao selecionar o período de '"+TipoPeriodoFuncionamentoPDV.SEGUNDA_SEXTA.getDescricao()+"', não é permitido a selecao específica de um dia da semana.");				
			}
		} 
		
		if (listaTipos.contains(TipoPeriodoFuncionamentoPDV.FINAIS_SEMANA)) {
			
			if (listaTipos.contains(TipoPeriodoFuncionamentoPDV.SABADO)
				|| listaTipos.contains(TipoPeriodoFuncionamentoPDV.DOMINGO)) {
				
				throw new Exception("Ao selecionar o período de '"+TipoPeriodoFuncionamentoPDV.FINAIS_SEMANA.getDescricao()+"', não é permitido a definição específíca para sábado ou domingo.");				
			}
		}		
	}

	/**
	 * Valida duplicidade de período
	 * 
	 * @param periodos - periodos
	 * @throws Exception - Exceção ao encontrar registro duplicado.
	 */
	private void validarDuplicidadeDePeriodo(List<TipoPeriodoFuncionamentoPDV> periodos) throws Exception {
		
		for(TipoPeriodoFuncionamentoPDV item : periodos) {
			int count=0;
			for(TipoPeriodoFuncionamentoPDV itemComparado : periodos) {
				if(item.equals(itemComparado)) {
					count++;
					if(count>1) {
						throw new Exception("O período " + 
								item.getDescricao() + 
								" foi incluido a lista mais de uma vez.");
					}
				}
			}			
		}
		
	}


}
