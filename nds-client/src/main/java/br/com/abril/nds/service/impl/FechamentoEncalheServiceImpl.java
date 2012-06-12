package br.com.abril.nds.service.impl;

import java.util.Collections;
import java.util.Comparator;
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
	@Transactional
	public List<FechamentoFisicoLogicoDTO> buscarFechamentoEncalhe(FiltroFechamentoEncalheDTO filtro,
			String sortorder, String sortname, int page, int rp) {
		
		int startSearch = page * rp - rp;
		String sort = sortname;
		if (sortname.equals("total")) {
			sort = null;
		}
		
		List<FechamentoFisicoLogicoDTO> listaConferencia = fechamentoEncalheRepository.buscarConferenciaEncalhe(filtro, sortorder, sort, startSearch, rp);
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
			
			fechamento.setFisico(qtd); // retorna valor pra tela
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
}
