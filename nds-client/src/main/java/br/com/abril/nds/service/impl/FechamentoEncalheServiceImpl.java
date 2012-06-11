package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.FechamentoEncalhe;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalhePK;
import br.com.abril.nds.repository.FechamentoEncalheRepository;
import br.com.abril.nds.service.FechamentoEncalheService;

@Service
public class FechamentoEncalheServiceImpl implements FechamentoEncalheService {

	@Autowired
	private FechamentoEncalheRepository fechamentoEncalheRepository;
	
	@Override
	@Transactional(readOnly=true)
	public List<FechamentoFisicoLogicoDTO> buscarFechamentoEncalhe(FiltroFechamentoEncalheDTO filtro,
			String sortorder, String sortname, int page, int rp) {
		
		int startSearch = page * rp - rp;
		
		List<FechamentoFisicoLogicoDTO> listaConferencia = fechamentoEncalheRepository.buscarConferenciaEncalhe(filtro, sortorder, sortname, startSearch, rp);
		List<FechamentoEncalhe> listaFechamento = fechamentoEncalheRepository.buscarFechamentoEncalhe(filtro);
		
		for (FechamentoFisicoLogicoDTO conferencia : listaConferencia) {
			
			conferencia.setTotal(conferencia.getExemplaresDevolucao().multiply(conferencia.getPrecoCapa()));
			
			for (FechamentoEncalhe fechamento : listaFechamento) {
				if (conferencia.getCodigo().equals(fechamento.getFechamentoEncalhePK().getProdutoEdicao().getProduto().getCodigo())) {
					conferencia.setFisico(fechamento.getQuantidade());
					break;
				}
			}
			
		}
		
		return listaConferencia;
	}
	
	@Override
	@Transactional(readOnly=true)
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
		}
		
		fechamentoEncalheRepository.flush();
		
		return listaConferencia;
	}

	@Override
	@Transactional(readOnly=true)
	public List<CotaAusenteEncalheDTO> buscarCotasAusentes(Date dataEncalhe,
			String sortorder, String sortname, int page, int rp) {

		int startSearch = page * rp - rp;
		
		return this.fechamentoEncalheRepository.buscarCotasAusentes(dataEncalhe, sortorder, sortname, startSearch, rp);
	}

	@Override
	@Transactional(readOnly=true)
	public Integer buscarTotalCotasAusentes(Date dataEncalhe) {
		return this.fechamentoEncalheRepository.buscarTotalCotasAusentes(dataEncalhe);
	}

}
