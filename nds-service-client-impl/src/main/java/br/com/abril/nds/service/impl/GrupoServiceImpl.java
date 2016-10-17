package br.com.abril.nds.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaTipoDTO;
import br.com.abril.nds.dto.GrupoCotaDTO;
import br.com.abril.nds.dto.MunicipioDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.TipoGrupo;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.GrupoCota;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.cadastro.pdv.TipoCaracteristicaSegmentacaoPDV;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.repository.ChamadaEncalheRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.GrupoRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.GrupoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.SemanaUtil;

@Service
public class GrupoServiceImpl implements GrupoService {
	
	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private CalendarioService calendarioService;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private ChamadaEncalheRepository chamadaEncalheRepository;
	
	private static final DateFormat DATE_FORMAT =  new SimpleDateFormat("dd/MM/yyyy");
	
	@Override
	@Transactional
	public List<GrupoCotaDTO> obterTodosGrupos(String sortname, String sortorder, boolean includeHistory) {
		
		List<GrupoCota> grupos = grupoRepository.obterGruposAtivos(sortname, sortorder, includeHistory);
		
		List<GrupoCotaDTO> gruposDTO = new ArrayList<GrupoCotaDTO>();

		for(GrupoCota grupo: grupos) {
			
			GrupoCotaDTO dto = new GrupoCotaDTO();
			
			dto.setDiasSemana(new ArrayList<DiaSemana>());
			
			StringBuilder dias = montarStringDiasRecolhimento(grupo.getDiasRecolhimento(), dto);
						
			dto.setIdGrupo(grupo.getId());
			dto.setNome(grupo.getNome()==null? "":grupo.getNome());
			dto.setRecolhimento(dias.toString());
			dto.setTipoCota(grupo.getTipoCota());
			dto.setTipoGrupo(grupo.getTipoGrupo());
			
			dto.setDataInicioVigencia(DateUtil.formatarDataPTBR(grupo.getDataInicioVigencia()));
			if(grupo.getDataFimVigencia()!=null){
			    dto.setDataFimVigencia(DateUtil.formatarDataPTBR(grupo.getDataFimVigencia()));
			}
			
			gruposDTO.add(dto);			
		}
		
		return gruposDTO;
	}

	private StringBuilder montarStringDiasRecolhimento(Set<DiaSemana> diasRecolhimento, GrupoCotaDTO dto) {
		
		StringBuilder dias = null;
		
		List<DiaSemana> diasOrdenados = new ArrayList<DiaSemana>(diasRecolhimento);
		
		Collections.sort(diasOrdenados);
		
		for(DiaSemana dia : diasOrdenados){
			if(dias == null)
				dias = new StringBuilder();					
			else
				dias.append(" - ");
			
			dto.getDiasSemana().add(dia);
			dias.append(dia.getDescricaoDiaSemana());
		}
		
		return dias;
	}

	@Transactional(readOnly=true)
	@Override
	public Integer countTodosGrupos(Date dataOperacao) {
	    
		return grupoRepository.countTodosGrupos(dataOperacao);
	}
	
	@Transactional
	@Override
	public void excluirGrupo(Long idGrupo) {
		final Date dataOperacao = this.distribuidorRepository.obterDataOperacaoDistribuidor();
        
        final Date dataInicioProximaSemana = getDataInicioProximaSemanaSemCE();
		GrupoCota grupo = grupoRepository.buscarPorId(idGrupo);
		
		if (grupo.getDataInicioVigencia().after(dataOperacao)){
		    grupoRepository.remover(grupo);
		}else{
		    grupo.setDataFimVigencia(DateUtils.addDays(dataInicioProximaSemana, -1));
	        grupoRepository.saveOrUpdate(grupo);
		}
		
	}

	@Override
	@Transactional
	public List<CotaTipoDTO> obterCotaPorTipo(TipoDistribuicaoCota tipoCota, Integer page, Integer rp, String sortname, String sortorder) {

		return cotaRepository.obterCotaPorTipo(tipoCota, page, rp, sortname, sortorder);
	}

	@Override
	@Transactional
	public int obterCountCotaPorTipo(TipoDistribuicaoCota tipoCota) {
		return cotaRepository.obterCountCotaPorTipo(tipoCota);
	}
	
	@Override
	@Transactional
	public int obterCountQtdeCotaMunicipio() {
		return cotaRepository.obterCountQtdeCotaMunicipio();
	}

	@Override
	@Transactional
	public List<MunicipioDTO> obterQtdeCotaMunicipio(Integer page, Integer rp,
			String sortname, String sortorder) {
		return cotaRepository.obterQtdeCotaMunicipio(page, rp, sortname, sortorder);
	}
	
	@Override
	@Transactional
	public void salvarGrupoCotas(Long idGrupo, Set<Long> idCotas, String nome,
			List<DiaSemana> diasSemana, TipoCaracteristicaSegmentacaoPDV tipoCota) {
	    
		salvarGrupoCota(idGrupo, nome, diasSemana, TipoGrupo.TIPO_COTA, null, tipoCota, idCotas);
	}

	@Override
	@Transactional
	public void salvarGrupoMunicipios(Long idGrupo, Set<String> municipios,
			String nome, List<DiaSemana> diasSemana) {
		
		salvarGrupoCota(idGrupo, nome, diasSemana, TipoGrupo.MUNICIPIO, municipios, null, null);
	}

    /**
     * @param idGrupo
     * @param municipios
     * @param nome
     * @param diasSemana
     */
    private void salvarGrupoCota(Long idGrupo, String nome, List<DiaSemana> diasSemana, TipoGrupo tipoGrupo, Set<String> municipios, TipoCaracteristicaSegmentacaoPDV tipoCota, Set<Long> idCotas) {
       
        final Date dataOperacao = this.distribuidorRepository.obterDataOperacaoDistribuidor();
        
       
        
        final Date dataInicioProximaSemana = getDataInicioProximaSemanaSemCE();
        
        this.validarNomeGrupo(nome, idGrupo, dataInicioProximaSemana);
        
        Set<Cota> cotas = this.validarCotaJaPertenceGrupo(idGrupo, idCotas, null, dataInicioProximaSemana);
        
        
		GrupoCota grupoNovo = null;
		if(idGrupo != null) {
		   final GrupoCota grupoAntigo = grupoRepository.buscarPorId(idGrupo);
			if (grupoAntigo.getDataInicioVigencia().after(dataOperacao)){
                grupoNovo = grupoAntigo;
			} else {
			    grupoAntigo.setDataFimVigencia(DateUtils.addDays(dataInicioProximaSemana, -1));
			    grupoRepository.saveOrUpdate(grupoAntigo);
            }
			
		}
		
		if(grupoNovo == null ){
		    grupoNovo = new GrupoCota();
	        grupoNovo.setDataInicioVigencia( dataInicioProximaSemana);
		}
		grupoNovo.setNome(nome);
		grupoNovo.setDiasRecolhimento(new HashSet<DiaSemana>(diasSemana));
		grupoNovo.setTipoGrupo(tipoGrupo);
		if(TipoGrupo.MUNICIPIO.equals(tipoGrupo)){
		    grupoNovo.setMunicipios(municipios);
		    grupoNovo.setTipoCota(null);
	        grupoNovo.setCotas(null);
		}else{
		    grupoNovo.setMunicipios(null);
		    grupoNovo.setTipoCota(tipoCota);
	        grupoNovo.setCotas(cotas);
		}
		grupoRepository.merge(grupoNovo);
    }

    /**
     * @return
     */
    @Transactional
    @Override
    public Date getDataInicioProximaSemanaSemCE() {
        final int diaInicioSemana = this.distribuidorRepository.buscarInicioSemanaRecolhimento().getCodigoDiaSemana();
        
        final Date maxDataRecolhimento = this.chamadaEncalheRepository.obterMaxDataRecolhimento(TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
        
        final Date dataInicioProximaSemana = SemanaUtil.obterDataInicioProximaSemana(diaInicioSemana, maxDataRecolhimento);
        return dataInicioProximaSemana;
    }

	private void validarNomeGrupo(String nome, Long idGrupo, Date dataOperacao) {
		
		if (nome == null || nome.isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Informe um nome válido para o grupo!");
		}
		
		if (this.grupoRepository.existeGrupoCota(nome, idGrupo, dataOperacao)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Já existe um grupo cadastrado com o nome: " + nome + "!");
		}
	}
	
	private Set<Cota> validarCotaJaPertenceGrupo(Long idGrupo, Set<Long> idCotas, Set<String> municipios, Date dataOperacao){
		
		List<String> msgs = new ArrayList<>();
		
		Set<Cota> cotas = null;
		
		if (idCotas != null && !idCotas.isEmpty()){
			
			cotas = new HashSet<Cota>();
			for(Long id : idCotas) {
				
				Cota cota = cotaRepository.buscarPorId(id);
				
				String nomeGrupo = grupoRepository.obterNomeGrupoPorCota(id, idGrupo, dataOperacao);
				
				if (nomeGrupo == null){
					
					if (cota.getEnderecoPrincipal() != null){
						
						nomeGrupo = this.grupoRepository.obterNomeGrupoPorMunicipio(
							cota.getEnderecoPrincipal().getEndereco().getCidade(), null, dataOperacao);
					}
				}
				
				if(nomeGrupo != null){
					msgs.add("Cota " + cota.getNumeroCota() + " já pertence ao grupo '" + nomeGrupo + "'.");
				}
				cotas.add(cota);
			}
		}
		
		if (municipios != null && !municipios.isEmpty()){
			
			for (String municipio : municipios){
				
				String nomeGrupo = this.grupoRepository.obterNomeGrupoPorMunicipio(municipio, idGrupo, dataOperacao);
				
				if (nomeGrupo == null){
					
					List<Long> cotasNoMunicipio = this.cotaRepository.obterIdsCotasPorMunicipio(municipio);
					
					for (Long idCota : cotasNoMunicipio){
						
						nomeGrupo = this.grupoRepository.obterNomeGrupoPorCota(idCota, idGrupo, dataOperacao);
						
						if(nomeGrupo != null){
							msgs.add("Cota " + this.cotaRepository.buscarNumeroCotaPorId(idCota) + 
							" (municipio de " + municipio + ")" +
							" já pertence ao grupo '" + nomeGrupo + "'.");
						}
					}
				} else {
					msgs.add("Já existe grupo ('"+ nomeGrupo +"') para o municipio " + municipio);
				}
			}
		}
		
		if (!msgs.isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, msgs);
		}
		
		return cotas;
	}

	@Override
	@Transactional
	public Set<String> obterMunicipiosDoGrupo(Long idGrupo) {

		return this.grupoRepository.obterMunicipiosCotasGrupo(idGrupo);
	}

	@Override
	@Transactional
	public Set<Long> obterCotasDoGrupo(Long idGrupo) {
		
		return this.grupoRepository.obterIdsCotasGrupo(idGrupo);
	}
	
	/**
	 * Obtem o dia da semana da data
	 * 
	 * @param data
	 * @return DiaSemana
	 */
	private DiaSemana getDiaSemanaData(Date data){
		
		return DiaSemana.getByDate(data);
	}
	
	/**
	 * Obtem dias da semana de operação diferenciada da cota considerando data de vigência
	 * 
	 * @param numeroCota
	 * @param dataOperacao
	 * @return List<DiaSemana>
	 */
	protected Set<DiaSemana> diasOperacaoDiferenciadaCota(Integer numeroCota, Date dataOperacao){
	    
		List<GrupoCota> gps = this.grupoRepository.obterListaGrupoCotaPorNumeroCota(numeroCota, dataOperacao);
		
		Set<DiaSemana> dss = new HashSet<DiaSemana>();
		
		for (GrupoCota gp : gps){
			
			dss.addAll(gp.getDiasRecolhimento());
		}
	
	    return dss;
	}

	/**
	 * Ordena dias da semana à partir de inicio da semana do distribuidor
	 * 
	 * @param diaSemana
	 * @return DiaSemana[]
	 */
	private DiaSemana[] ordenarDiasSemanaPorDataInicioSemanaDistribuidor(DiaSemana diaSemana){
		
		DiaSemana[] diasSemanaOrdenados = new DiaSemana[7];
		
		DiaSemana[] diasSemanaAnteriores = new DiaSemana[7];
		
		boolean contagem = false;
		
		int x = 0;
		
		for (DiaSemana ds : DiaSemana.values()){
			
			if (!contagem && ds.equals(diaSemana)){
				
				contagem = true;
				
				x = 0;
			}
			
			if (contagem){
				
				diasSemanaOrdenados[x] = ds;
				
				x++;
			}
			else{
				
				diasSemanaAnteriores[x] = ds;
				
				x++;
			}
		}
		
		for (int i = 0; i < (diasSemanaAnteriores.length-1); i++){
			
			if (diasSemanaAnteriores[i]==null){
				
				continue;
			}
			
			diasSemanaOrdenados[x] = diasSemanaAnteriores[i];
			
			x++;
		}
		
		return diasSemanaOrdenados;
	}

	/**
	 * Tratamento da data de operação diferenciada
	 * Quando feriado, adiciona um dia à data de operação Diferenciada
	 * 
	 * @param datas
	 * @return List<Date>
	 */
	private Date tratarFeriadoDataRecolhimentoOperacaoDiferenciada(Date d){
		
		if (!this.calendarioService.isFeriadoMunicipalSemOperacao(d) && 
			!this.calendarioService.isFeriadoSemOperacao(d) &&
			this.calendarioService.isDiaUtil(d)){
			
			return d;
		}
		
		Date data = new Date();
		
		data = DateUtil.adicionarDias(d, 1);
		
		return this.tratarFeriadoDataRecolhimentoOperacaoDiferenciada(data);
	}
	
	/**
	 * Tratamento de feriados da semana de operação diferenciada
	 * Quando feriado, adiciona um dia à data de operação Diferenciada
	 * 
	 * @param datas
	 * @return List<Date>
	 */
	private List<Date> tratarFeriadosDatasRecolhimentoOperacaoDiferenciada(List<Date> datas){
		
		List<Date> datasConsiderandoFeriados = new ArrayList<Date>();
		
		for (Date d : datas){
			
			Date data = this.tratarFeriadoDataRecolhimentoOperacaoDiferenciada(d);

			if (!datasConsiderandoFeriados.contains(data)){
			    
				datasConsiderandoFeriados.add(data);
			}
		}
		
		return datasConsiderandoFeriados;
	}

	/**
	 * Obtém datas de recolhimento da semana da data de operação diferenciada
	 * Se nao houver operação diferenciada, retorna apenas a data de operação na lista
	 * 
	 * @param numeroCota
	 * @param data
	 * @return List<Date>
	 */
	@Override
	@Transactional(readOnly=true)
	public List<Date> obterDatasRecolhimentoOperacaoDiferenciada(final Integer numeroCota, 
			                                                     final Date data) {
		
		final Set<DiaSemana> diasSemanaOperacaoDiferenciadaCota = this.diasOperacaoDiferenciadaCota(numeroCota, data);
		
		final DiaSemana diaSemanaDataOperacao = this.getDiaSemanaData(data);
		
		List<Date> datas = new ArrayList<Date>();
		
		if (diasSemanaOperacaoDiferenciadaCota==null || 
	        diasSemanaOperacaoDiferenciadaCota.isEmpty()){
			
			datas.add(data);
			
			return datas;
		}
		
        if (!diasSemanaOperacaoDiferenciadaCota.contains(diaSemanaDataOperacao)){
        	
			return datas;
		}
		
        datas.add(data);
        
		final DiaSemana diaSemanaInicioDistribuidor = distribuidorService.inicioSemanaRecolhimento();
		
		int numeroDiasSemana = 0;
		
		int count = 0;
		
		DiaSemana[] dss = this.ordenarDiasSemanaPorDataInicioSemanaDistribuidor(diaSemanaInicioDistribuidor);
		
		for (DiaSemana ds : dss){
			
            if (ds.equals(diaSemanaDataOperacao)){
				
				break;
			}

            numeroDiasSemana++;
            
            count++;
            
			if (diasSemanaOperacaoDiferenciadaCota.contains(ds)){
				
				numeroDiasSemana = 0;
			}
		}

		for (int i = 1; i <= numeroDiasSemana; i++){
			
			Date dataOpDif = DateUtil.subtrairDias(data, i);
			
			datas.add(dataOpDif);
		}
		
		List<Date> datasPosteriores = new ArrayList<Date>();
		
		boolean ultimaData = true;
		
		for (int i = (count+1); i < dss.length; i++){
			
			Date dataOpDif = DateUtil.adicionarDias(data, i-count);
			
			DiaSemana ds = this.getDiaSemanaData(dataOpDif);
			
			if (diasSemanaOperacaoDiferenciadaCota.contains(ds)){
				
				ultimaData = false;
				
				break;
			}

			datasPosteriores.add(dataOpDif);
		}
		
		if (ultimaData){
			
			datas.addAll(datasPosteriores);
		}
		
		return datas;
	}

}