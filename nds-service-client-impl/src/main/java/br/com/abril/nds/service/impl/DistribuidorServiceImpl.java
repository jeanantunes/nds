package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.DistribuidorDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.ObrigacaoFiscal;
import br.com.abril.nds.model.cadastro.ParametrosRecolhimentoDistribuidor;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.cadastro.TipoContabilizacaoCE;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.TipoImpressaoCE;
import br.com.abril.nds.model.cadastro.TipoImpressaoNENECADANFE;
import br.com.abril.nds.model.cadastro.TipoStatusGarantia;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Service
public class DistribuidorServiceImpl implements DistribuidorService {

	@Autowired
	private DistribuidorRepository distribuidorRepository;

	@Override
	@Transactional(readOnly = true)
	public boolean isDistribuidor(Integer codigo) {
		
		if (this.codigoDistribuidorDinap().equals(codigo.toString())){
			
			return true;
		}
		
		return false;
	}

	@Override
	@Transactional
	public Distribuidor obter() {
		return distribuidorRepository.obter();
	}

	@Override
	@Transactional
	public void alterar(Distribuidor distribuidor) {
		distribuidorRepository.alterar(distribuidor);
	}

	@Override
	@Transactional
	public DistribuidorDTO obterDadosEmissao() {
		
		Distribuidor distribuidor = obter();
		
		DistribuidorDTO dto = new DistribuidorDTO();
		
		dto.setRazaoSocial(distribuidor.getJuridica().getRazaoSocial().toUpperCase());
		Endereco endereco = distribuidor.getEnderecoDistribuidor().getEndereco(); 
		dto.setEndereco(endereco.getLogradouro().toUpperCase()  + " " + endereco.getNumero());
		dto.setCnpj(distribuidor.getJuridica().getCnpj());
		dto.setCidade(endereco.getCidade().toUpperCase());
		dto.setUf(endereco.getUf().toUpperCase());
		dto.setCep(endereco.getCep());
		dto.setInscricaoEstatual(distribuidor.getJuridica().getInscricaoEstadual());
		
		return dto;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<String> obterNomeCNPJDistribuidor(){
		
		return this.distribuidorRepository.obterNomeCNPJDistribuidor();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ItemDTO<TipoGarantia, String>> getComboTiposGarantia() {
		
		List<ItemDTO<TipoGarantia,String>> comboTiposGarantia =  new ArrayList<ItemDTO<TipoGarantia,String>>();
		for (TipoGarantia itemTipoGarantia: TipoGarantia.values()){
			comboTiposGarantia.add(new ItemDTO<TipoGarantia,String>(itemTipoGarantia, itemTipoGarantia.getDescTipoGarantia()));
		}
		return comboTiposGarantia;
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<ItemDTO<TipoStatusGarantia, String>> getComboTiposStatusGarantia() {
		
		List<ItemDTO<TipoStatusGarantia,String>> comboTiposStatusGarantia =  new ArrayList<ItemDTO<TipoStatusGarantia,String>>();
		for (TipoStatusGarantia itemTipoStatusGarantia: TipoStatusGarantia.values()){
			comboTiposStatusGarantia.add(new ItemDTO<TipoStatusGarantia,String>(itemTipoStatusGarantia, itemTipoStatusGarantia.getDescTipoStatusGarantia()));
		}
		return comboTiposStatusGarantia;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Date obterDataOperacaoDistribuidor(){
		
		return this.distribuidorRepository.obterDataOperacaoDistribuidor();
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean utilizaGarantiaPdv(){
		
		return this.distribuidorRepository.utilizaGarantiaPdv();
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean aceitaJuramentado(){
		
		return this.distribuidorRepository.aceitaJuramentado();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int qtdDiasEncalheAtrasadoAceitavel(){
		
		return this.distribuidorRepository.qtdDiasEncalheAtrasadoAceitavel();
	}

	@Override
	@Transactional(readOnly = true)
	public boolean utilizaControleAprovacao() {
		
		return this.distribuidorRepository.utilizaControleAprovacao();
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean utilizaControleAprovacaoFaltaSobra() {
		
		return this.distribuidorRepository.utilizaControleAprovacaoFaltaSobra();
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean utilizaTermoAdesao() {
		
		return this.distribuidorRepository.utilizaTermoAdesao();
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean utilizaProcuracaoEntregadores() {
		
		return this.distribuidorRepository.utilizaProcuracaoEntregadores();
	}

	@Override
	@Transactional(readOnly = true)
	public DiaSemana inicioSemana() {
		
		return this.distribuidorRepository.buscarInicioSemana();
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean utilizaSugestaoIncrementoCodigo() {
		
		return this.distribuidorRepository.utilizaSugestaoIncrementoCodigo();
	}

	@Override
	@Transactional(readOnly = true)
	public String getEmail() {
		
		return this.distribuidorRepository.getEmail();
	}

	@Override
	@Transactional(readOnly = true)
	public ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor() {
		
		return this.distribuidorRepository.parametrosRecolhimentoDistribuidor();
	}

	@Override
	@Transactional(readOnly = true)
	public String obterRazaoSocialDistribuidor() {
		
		return this.distribuidorRepository.obterRazaoSocialDistribuidor();
	}

	@Override
	@Transactional(readOnly = true)
	public TipoImpressaoCE tipoImpressaoCE() {
		
		return this.distribuidorRepository.tipoImpressaoCE();
	}

	@Override
	@Transactional(readOnly = true)
	public Integer qntDiasVencinemtoVendaEncalhe() {
		
		return this.distribuidorRepository.qntDiasVencinemtoVendaEncalhe();
	}

	@Override
	@Transactional(readOnly = true)
	public Integer negociacaoAteParcelas() {
		
		return this.distribuidorRepository.negociacaoAteParcelas();
	}

	@Override
	@Transactional(readOnly = true)
	public Integer qtdDiasLimiteParaReprogLancamento() {
		
		Integer qtd = this.distribuidorRepository.qtdDiasLimiteParaReprogLancamento();
		
		return qtd == null ? 0 : qtd;
	}

	@Override
	@Transactional(readOnly = true)
	public ObrigacaoFiscal obrigacaoFiscal() {
		
		return this.distribuidorRepository.obrigacaoFiscal();
	}

	@Override
	@Transactional(readOnly = true)
	public TipoImpressaoNENECADANFE tipoImpressaoNENECADANFE() {
		
		return this.distribuidorRepository.tipoImpressaoNENECADANFE();
	}

	@Override
	@Transactional(readOnly = true)
	public String cidadeDistribuidor() {
		
		return this.distribuidorRepository.cidadeDistribuidor();
	}

	@Override
	@Transactional(readOnly = true)
	public String codigoDistribuidorDinap() {
		
		return this.distribuidorRepository.codigoDistribuidorDinap();
	}

	@Override
	@Transactional(readOnly = true)
	public String codigoDistribuidorFC() {
		
		return this.distribuidorRepository.codigoDistribuidorFC();
	}

	@Override
	@Transactional(readOnly = true)
	public Integer diasNegociacao() {
		
		return this.distribuidorRepository.diasNegociacao();
	}

	@Override
	@Transactional(readOnly = true)
	public TipoContabilizacaoCE tipoContabilizacaoCE() {
		
		return this.distribuidorRepository.tipoContabilizacaoCE();
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean preenchimentoAutomaticoPDV() {
		
		return this.distribuidorRepository.preenchimentoAutomaticoPDV();
	}

	@Override
	@Transactional(readOnly = true)
	public Long qntDiasReutilizacaoCodigoCota() {
		
		return this.distribuidorRepository.qntDiasReutilizacaoCodigoCota();
	}

	@Override
	@Transactional(readOnly = true)
	public Set<PoliticaCobranca> politicasCobranca() {
		
		return this.distribuidorRepository.politicasCobranca();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Set<PoliticaCobranca> politicasCobrancaAtivas() {
		
		return this.distribuidorRepository.politicasCobrancaAtivas();
	}

	@Override
	@Transactional(readOnly = true)
	public String assuntoEmailCobranca() {
		
		return this.distribuidorRepository.assuntoEmailCobranca();
	}

	@Override
	@Transactional(readOnly = true)
	public String mensagemEmailCobranca() {
		
		return this.distribuidorRepository.mensagemEmailCobranca();
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean regimeEspecial() {
		
		return this.distribuidorRepository.regimeEspecial();
	}

	@Override
	@Transactional(readOnly = true)
	public TipoAtividade tipoAtividade() {
		
		return this.distribuidorRepository.tipoAtividade();
	}

	@Override
	@Transactional(readOnly = true)
	public Integer fatorRelancamentoParcial() {
		
		return this.distribuidorRepository.fatorRelancamentoParcial();
	}

	@Override
	@Transactional(readOnly = true)
	public Long obterId() {
		
		return this.distribuidorRepository.obterId();
	}
	
	@Transactional(readOnly = true)
	public int obterOrdinalUltimoDiaRecolhimento() {
		
		ParametrosRecolhimentoDistribuidor parametroRecolhimento = 
				this.distribuidorRepository.parametrosRecolhimentoDistribuidor();
		
		int ordinal = 0;
		
		ordinal = parametroRecolhimento.isDiaRecolhimentoPrimeiro() ? 1 : ordinal;
		ordinal = parametroRecolhimento.isDiaRecolhimentoSegundo()  ? 2 : ordinal;
		ordinal = parametroRecolhimento.isDiaRecolhimentoTerceiro() ? 3 : ordinal;
		ordinal = parametroRecolhimento.isDiaRecolhimentoQuarto()   ? 4 : ordinal;
		ordinal = parametroRecolhimento.isDiaRecolhimentoQuinto()   ? 5 : ordinal;
		
		return ordinal;
	}
}