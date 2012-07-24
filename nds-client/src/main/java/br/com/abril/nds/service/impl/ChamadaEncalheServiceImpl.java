package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaEmissaoDTO;
import br.com.abril.nds.dto.filtro.FiltroEmissaoCE;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.repository.ChamadaEncalheRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.service.ChamadaEncalheService;
import br.com.abril.nds.util.DateUtil;

/**
 * Classe de implementação referentes a serviços de chamada encalhe. 
 *   
 * @author Discover Technology
 */

@Service
public class ChamadaEncalheServiceImpl implements ChamadaEncalheService {
	
	
	@Autowired
	private ChamadaEncalheRepository chamadaEncalheRepository;
	
	@Autowired
	private CotaRepository cotaRepository;

	@Override
	@Transactional
	public List<CotaEmissaoDTO> obterDadosEmissaoChamadasEncalhe(FiltroEmissaoCE filtro) {
		
		
		List<CotaEmissaoDTO> chamadasEncalheCota = chamadaEncalheRepository.obterDadosEmissaoChamadasEncalhe(filtro);
		
		return chamadasEncalheCota;
	}


	@Override
	public List<CotaEmissaoDTO> obterDadosImpressaoEmissaoChamadasEncalhe(
			FiltroEmissaoCE filtro) {
		
		List<CotaEmissaoDTO> lista = obterDadosEmissaoChamadasEncalhe(filtro);
		
		for(CotaEmissaoDTO dto:lista) {
			
			Cota cota = cotaRepository.buscarPorId( dto.getIdCota());
			
			Endereco endereco = null;
			
			for(EnderecoCota enderecoCota : cota.getEnderecos()){
				
				if (enderecoCota.isPrincipal()){
					endereco = enderecoCota.getEndereco();
					break;
				}
			}
			
			if( endereco!= null) {
				dto.setEndereco(endereco.getLogradouro().toUpperCase()  + " " + endereco.getNumero());
			}
			
			dto.setNumeroNome(dto.getNumCota()+ " " + dto.getNomeCota().toUpperCase());
			dto.setCnpj(cota.getPessoa().getDocumento());
			
			if(filtro.getDtRecolhimentoDe()!= null && filtro.getDtRecolhimentoDe().equals(filtro.getDtRecolhimentoAte())) {
				dto.setDataRecolhimento(DateUtil.formatarDataPTBR(filtro.getDtRecolhimentoDe()));
			}
			
			dto.setDataEmissao(DateUtil.formatarDataPTBR(new Date()));
		}
		
		return lista;
	}
		
}
