package br.com.abril.nds.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.id.IdentityGenerator.GetGeneratedKeysDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CaracteristicaDTO;
import br.com.abril.nds.dto.EnderecoPdvDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.filtro.FiltroPdvDTO;
import br.com.abril.nds.model.cadastro.AssociacaoEndereco;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.LicencaMunicipal;
import br.com.abril.nds.model.cadastro.MaterialPromocional;
import br.com.abril.nds.model.cadastro.TipoLicencaMunicipal;
import br.com.abril.nds.model.cadastro.pdv.AreaInfluenciaPDV;
import br.com.abril.nds.model.cadastro.pdv.CaracteristicasPDV;
import br.com.abril.nds.model.cadastro.pdv.ClusterPDV;
import br.com.abril.nds.model.cadastro.pdv.EspecialidadePDV;
import br.com.abril.nds.model.cadastro.pdv.GeradorFluxoPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.PeriodoFuncionamentoPDV;
import br.com.abril.nds.model.cadastro.pdv.SegmentacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoGeradorFluxoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.repository.AreaInfluenciaPDVRepository;
import br.com.abril.nds.repository.ClusterPDVRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EspecialidadePDVRepository;
import br.com.abril.nds.repository.GeradorFluxoPDVRepository;
import br.com.abril.nds.repository.MaterialPromocionalRepository;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.repository.PeriodoFuncionamentoPDVRepository;
import br.com.abril.nds.repository.TipoGeradorFluxoPDVRepsitory;
import br.com.abril.nds.repository.TipoLicencaMunicipalRepository;
import br.com.abril.nds.repository.TipoPontoPDVRepository;
import br.com.abril.nds.service.PdvService;

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
	
	@Override
	public boolean isExcluirPdv(Long idPdv) {
		
		/**
		 * TODO definir regra de exclusão de PDV
		 */
		return Boolean.TRUE;
	}
	
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
	public List<EspecialidadePDV> obterEspecialidadesPDV(){
		
		return especialidadePDVRepository.buscarTodos();
	}
	
	@Transactional(readOnly=true)
	@Override
	public List<TipoGeradorFluxoPDV> obterTiposGeradorFluxo(){
		
		return tipoGeradorFluxoPDVRepsitory.buscarTodos();
	}
	
	@Transactional(readOnly=true)
	@Override
	public List<MaterialPromocional> obterMateriaisPromocionalPDV(){
		
		return materialPromocionalRepository.buscarTodos();
	}
	
	@Transactional(readOnly=true)
	@Override
	public List<PdvDTO> obterPDVsPorCota(FiltroPdvDTO filtro) {
		
		return pdvRepository.obterPDVsPorCota(filtro);
	}
	
	@Transactional
	public void salvar(PdvDTO pdvDTO){
		
		if(pdvDTO == null){
			throw new IllegalArgumentException("Parâmetro PDV inválido");
		}
		
		if(pdvDTO.getNumeroCota() == null){
			throw new IllegalArgumentException("Parâmetro Cota PDV inválido");
		}
		
		Cota cota  = obterCotaPDV(pdvDTO);
		
		if(cota == null){
			throw new IllegalArgumentException("Não foi encontrado Cota para inclusão do PDV");
		}
		
		PDV pdv = null;
		
		if(pdvDTO.getId()!= null){
			pdv = pdvRepository.buscarPorId(pdvDTO.getId());
		}
		
		if(pdv == null){
			pdv = new PDV();
		}
		
		pdv.setCota(cota);
		pdv.setNome(pdvDTO.getNomePDV());
		pdv.setContato(pdvDTO.getContato());
		pdv.setPontoReferencia(pdvDTO.getPontoReferencia());
		pdv.setEmail(pdvDTO.getEmail());
		pdv.setPorcentagemFaturamento(pdvDTO.getPorcentagemFaturamento());
		pdv.setPossuiSistemaIPV(pdvDTO.isSistemaIPV());
		pdv.setPrincipal(pdvDTO.isPrincipal());
		pdv.setQtdeFuncionarios(pdvDTO.getQtdeFuncionarios());
		pdv.setSite(pdvDTO.getSite());
		pdv.setStatus(pdvDTO.getStatusPDV());
		pdv.setTamanhoPDV(pdvDTO.getTamanhoPDV());
		pdv.setDentroOutroEstabelecimento(pdvDTO.isDentroDeOutroEstabelecimento());
	
		pdv.setEspecialidades(obterEspecialidadesPDV(pdvDTO));
		pdv.setLicencaMunicipal(obterLicencaMunicipalPDV(pdvDTO,pdv));
		pdv.setCaracteristicas(obterCaracteristicaPDV(pdvDTO,pdv));
		pdv.setMateriais(obterMateriaisPDV(pdvDTO));
		pdv.setSegmentacao(obterSegmentacaoPDV(pdvDTO,pdv));
		
		if(pdvDTO.isDentroDeOutroEstabelecimento()){
			pdv.setTipoEstabelecimentoPDV(pdvDTO.getTipoEstabelecimentoAssociacaoPDV());
		}
		
		pdv =  pdvRepository.merge(pdv);
		
		fluxoPDVRepository.merge(obterGeradorFluxoPDV(pdvDTO, pdv));
		
		//salvarPeriodoFuncionamentoPDV(pdvDTO, pdv);
		
		//salvarEndereco(pdvDTO, pdv);
		//salvarTelefone(pdvDTO, pdv);
			
	}
	
	private void salvarPeriodoFuncionamentoPDV(PdvDTO pdvDTO,PDV pdv){
		
	}

	private void salvarEndereco(PdvDTO pdvDTO,PDV pdv){
		
	}
	
	private void salvarTelefone(PdvDTO pdvDTO,PDV pdv){
		
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

	private Set<PeriodoFuncionamentoPDV> obterPeriodosPDV(PdvDTO pdvDTO) {
		
		Set<PeriodoFuncionamentoPDV> periodoFuncionamento = new HashSet<PeriodoFuncionamentoPDV>();
		
		if(pdvDTO.getId() == null){
			
			
		}
		
	//	periodoFuncionamento.addAll( periodoFuncionamentoPDVRepository.obterPeriodoFuncionamentoPDV(idPDV));
		
		return periodoFuncionamento ;
		
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
		
		GeradorFluxoPDV fluxoPDV = pdv.getGeradorFluxoPDV();
		
		if(fluxoPDV == null){
			fluxoPDV = new GeradorFluxoPDV();
		}
		
		TipoGeradorFluxoPDV fluxoPrincipal = null;
		
		if(pdvDTO.getGeradorFluxoPrincipal()!= null){
			fluxoPrincipal = tipoGeradorFluxoPDVRepsitory.buscarPorId(pdvDTO.getGeradorFluxoPrincipal());
		}
		
		Set<TipoGeradorFluxoPDV> fluxoSecundario = new HashSet<TipoGeradorFluxoPDV>();
		
		if(pdvDTO.getGeradorFluxoSecundario()!= null){

			fluxoSecundario.addAll(tipoGeradorFluxoPDVRepsitory.obterTiposGeradorFluxo(pdvDTO.getGeradorFluxoSecundario().toArray(new Long[]{})));
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
		
		return cotaRepository.obterPorNumerDaCota(pdvDTO.getNumeroCota());
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
		
		return caracteristicasPDV;
	}

}
