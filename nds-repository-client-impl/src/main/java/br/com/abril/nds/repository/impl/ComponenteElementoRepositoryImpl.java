package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ComponenteElementoDTO;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.pdv.AreaInfluenciaPDV;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.model.cadastro.pdv.GeradorFluxoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.model.distribuicao.Regiao;
import br.com.abril.nds.repository.ComponenteElementoRepository;
import br.com.abril.nds.repository.EnderecoRepository;
import br.com.abril.nds.util.UfEnum;

@Repository
@SuppressWarnings("unchecked")
public class ComponenteElementoRepositoryImpl implements
		ComponenteElementoRepository {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<ComponenteElementoDTO> buscaTiposDePontoDeVena() {

		List<ComponenteElementoDTO> retorno = new ArrayList<>();
		List<TipoPontoPDV> list = getSession().createCriteria(
				TipoPontoPDV.class).list();

		for (TipoPontoPDV tipoPontoPDV : list) {
			retorno.add(new ComponenteElementoDTO("tipo_ponto_venda",
					tipoPontoPDV.getId(), tipoPontoPDV.getDescricao()));
		}

		return retorno;
	}

	@Override
	public List<ComponenteElementoDTO> buscaGeradorDeFluxo() {

		List<ComponenteElementoDTO> retorno = new ArrayList<>();
		List<GeradorFluxoPDV> list = getSession().createCriteria(
				GeradorFluxoPDV.class).list();

		for (GeradorFluxoPDV geradorFluxoPDV : list) {
			retorno.add(new ComponenteElementoDTO("gerador_de_fluxo",
					geradorFluxoPDV.getId(), geradorFluxoPDV.getPrincipal()
							.getDescricao()));
		}

		return retorno;
	}
	
	@Override
	public List<ComponenteElementoDTO> buscaBairros() {
		
		
		List<ComponenteElementoDTO> retorno = new ArrayList<>();
		List<EnderecoCota> list = getSession().createCriteria(EnderecoCota.class).list();
		

		for (EnderecoCota enderecocota : list) {
			retorno.add(new ComponenteElementoDTO(	"bairro",
					enderecocota.getId(), enderecocota.getEndereco().getBairro()));
		}

		return retorno;
	}

	@Override
	public List<ComponenteElementoDTO> buscaRegioes() {
		List<ComponenteElementoDTO> retorno = new ArrayList<>();
		List<Regiao> list = getSession().createCriteria(Regiao.class).list();

		for (Regiao regiao : list) {
			retorno.add(new ComponenteElementoDTO("regiao", regiao.getId(),
					regiao.getNomeRegiao()));
		}

		return retorno;
	}

	@Override
	public List<ComponenteElementoDTO> buscaCotasAVista() {
		List<ComponenteElementoDTO> retorno = new ArrayList<>();
		// TODO
		return retorno;
	}

	@Override
	public List<ComponenteElementoDTO> buscaCotasNovas() {
		List<ComponenteElementoDTO> retorno = new ArrayList<>();
		// TODO
		return retorno;
	}

	@Override
	public List<ComponenteElementoDTO> buscaAreaDeInfluencia() {
		List<ComponenteElementoDTO> retorno = new ArrayList<>();
		List<AreaInfluenciaPDV> list = getSession().createCriteria(
				AreaInfluenciaPDV.class).list();

		for (AreaInfluenciaPDV regiao : list) {
			retorno.add(new ComponenteElementoDTO("area_influencia", regiao
					.getId(), regiao.getDescricao()));
		}

		return retorno;
	}

	@Override
	public List<ComponenteElementoDTO> buscaDistritos() {

		List<ComponenteElementoDTO> retorno = new ArrayList<>();
		UfEnum[] values = UfEnum.values();
		for (UfEnum uf : values) {
			retorno.add(new ComponenteElementoDTO("distrito", uf.getSigla(), uf.getDescricao()));
		}
		
		return retorno;
	}

	public Session getSession() {
		return sessionFactory.openSession();
	}

}
