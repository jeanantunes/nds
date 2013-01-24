package br.com.abril.nds.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.FixacaoReparteDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoProdutoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.distribuicao.FixacaoReparte;
import br.com.abril.nds.repository.FixacaoReparteRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.service.FixacaoReparteService;
@Service
public class FixacaoReparteServiceImpl implements FixacaoReparteService {
	@Autowired
	private FixacaoReparteRepository fixacaoReparteRepository;
	@Autowired
	private ProdutoRepository produtoRepository;

	@Transactional
	@Override
	public void incluirFixacaoReparte(FixacaoReparte fixacaoReparte) {
		
		fixacaoReparteRepository.adicionar(fixacaoReparte);
	}

	@Transactional
	@Override
	public List<FixacaoReparteDTO> obterFixacoesRepartePorProduto(
			FiltroConsultaFixacaoProdutoDTO filtroConsultaFixacaoProdutoDTO) {
		List<FixacaoReparteDTO> listaFixacaoReparteDTO = new ArrayList<FixacaoReparteDTO>();
		Long idProdutoBusca = new Long(filtroConsultaFixacaoProdutoDTO.getCodigoProduto());
		
		Produto produto = produtoRepository.obterProdutoPorID(idProdutoBusca); 
		List<FixacaoReparte> listaFixacaoReparte = fixacaoReparteRepository.obterFixacoesRepartePorProduto(produto);
		
		getListaFixacaoReparteDTO(listaFixacaoReparte, listaFixacaoReparteDTO);
		return listaFixacaoReparteDTO;
	}

	private void getListaFixacaoReparteDTO(List<FixacaoReparte> listaFixacaoReparte, List<FixacaoReparteDTO> listaFixacaoReparteDTO) {
		
		for(FixacaoReparte fixacaoReparte: listaFixacaoReparte){
			FixacaoReparteDTO fixacaoReparteDTO = new FixacaoReparteDTO();
			fixacaoReparteDTO.setId(fixacaoReparte.getId());
			fixacaoReparteDTO.setQtdeEdicoes(fixacaoReparte.getQtdeEdicoes());
			fixacaoReparteDTO.setUsuario(fixacaoReparte.getUsuario().getNome());
			fixacaoReparteDTO.setProdutoFixado(fixacaoReparte.getProdutoFixado().getId());
			fixacaoReparteDTO.setData(getDataFormatada(fixacaoReparte.getDataHora()));
			fixacaoReparteDTO.setHora(getHoraFormatada(fixacaoReparte.getDataHora()));
			listaFixacaoReparteDTO.add(fixacaoReparteDTO);
		}
	}
	
	@Transactional
	@Override
	public List<FixacaoReparteDTO> obterFixacoesRepartePorCota(
			FiltroConsultaFixacaoCotaDTO filtroConsultaFixacaoCotaDTO) {
		// 
		return null;
	}

	
	private String getHoraFormatada(Date date){
		String horaConvertida = "";
		if(date != null){
			DateFormat hf = new SimpleDateFormat("dd/MM/yyyy");
			horaConvertida = hf.format(date);
			return horaConvertida;
		}else{
			return null;	
		}
		
	}
	
	private String getDataFormatada(Date date){
		String dataConvertida = "";
		if(date != null){
			DateFormat df = new SimpleDateFormat("HH:mm");
			dataConvertida = df.format(date);
			return dataConvertida;
		}else{
			return null;	
		}
		
	}
	
	
	public CotaDTO getCotaDTO(Cota cota){
		if(cota != null){	
			CotaDTO cotaDTO = new CotaDTO();
			cotaDTO.setNumeroCota(cota.getNumeroCota());
			cotaDTO.setNomePessoa(cota.getPessoa().getNome());
			return cotaDTO;
		}else{
			return null;
		}
	
	}
	

}
