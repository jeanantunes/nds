package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CapaDTO;
import br.com.abril.nds.dto.CotaEmissaoDTO;
import br.com.abril.nds.dto.ProdutoEmissaoDTO;
import br.com.abril.nds.dto.filtro.FiltroEmissaoCE;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.repository.ChamadaEncalheRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.service.ChamadaEncalheService;
import br.com.abril.nds.util.CurrencyUtil;
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
	@Transactional
	public List<CotaEmissaoDTO> obterDadosImpressaoEmissaoChamadasEncalhe(
			FiltroEmissaoCE filtro) {
		
		List<CotaEmissaoDTO> lista = chamadaEncalheRepository.obterDadosEmissaoImpressaoChamadasEncalhe(filtro);
		
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
				dto.setUf(endereco.getUf());
				dto.setCidade(endereco.getCidade());
				dto.setUf(endereco.getUf());
				dto.setCep(endereco.getCep());
			}
			
			if(cota.getPessoa() instanceof PessoaJuridica)
				dto.setInscricaoEstadual(((PessoaJuridica)cota.getPessoa()).getInscricaoEstadual());
			
			dto.setNumeroNome(dto.getNumCota()+ " " + dto.getNomeCota().toUpperCase());
			dto.setCnpj(cota.getPessoa().getDocumento());
									
			dto.setDataEmissao(DateUtil.formatarDataPTBR(new Date()));
			
			dto.setProdutos(chamadaEncalheRepository.obterProdutosEmissaoCE(filtro,dto.getIdCota()));
			
			Double vlrReparte = 0.0;	
			Double vlrDesconto = 0.0;
			Double vlrEncalhe = 0.0;	
			
			for(ProdutoEmissaoDTO produtoDTO : dto.getProdutos()) {
				produtoDTO.setVendido(produtoDTO.getReparte() - produtoDTO.getQuantidadeDevolvida());
				produtoDTO.setVlrVendido(CurrencyUtil.formatarValor(produtoDTO.getVendido() * produtoDTO.getVlrPrecoComDesconto()));
				vlrReparte += produtoDTO.getPrecoVenda() * produtoDTO.getReparte();
				vlrDesconto +=  produtoDTO.getVlrDesconto() * produtoDTO.getReparte();
				vlrEncalhe += produtoDTO.getQuantidadeDevolvida() * (produtoDTO.getPrecoVenda() - produtoDTO.getVlrDesconto());
			}
			
			Double vlrReparteLiquido = vlrReparte - vlrDesconto;
			Double totalLiquido = vlrReparteLiquido - vlrEncalhe;
			
			dto.setVlrReparte(CurrencyUtil.formatarValor(vlrReparte));
			dto.setVlrComDesconto(CurrencyUtil.formatarValor(vlrDesconto));
			dto.setVlrReparteLiquido(CurrencyUtil.formatarValor(vlrReparteLiquido));
			dto.setVlrEncalhe(CurrencyUtil.formatarValor(vlrEncalhe));
			dto.setVlrTotalLiquido(CurrencyUtil.formatarValor(totalLiquido));
			
			
			
		}
		
		return lista;
	}


	@Override
	@Transactional
	public List<CapaDTO> obterIdsCapasChamadaEncalhe(Date dtRecolhimentoDe,
			Date dtRecolhimentoAte) {
		
		return chamadaEncalheRepository.obterIdsCapasChamadaEncalhe(dtRecolhimentoDe, dtRecolhimentoAte);
	}
		
}
