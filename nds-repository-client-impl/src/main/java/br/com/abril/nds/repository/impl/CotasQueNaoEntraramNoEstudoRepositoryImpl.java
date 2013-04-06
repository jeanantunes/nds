package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaQueNaoEntrouNoEstudoDTO;
import br.com.abril.nds.dto.CotasQueNaoEntraramNoEstudoQueryDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.repository.CotasQueNaoEntraramNoEstudoRepository;

@Repository
@SuppressWarnings("unchecked")
public class CotasQueNaoEntraramNoEstudoRepositoryImpl implements
		CotasQueNaoEntraramNoEstudoRepository {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<CotaQueNaoEntrouNoEstudoDTO> buscaCotasQuerNaoEntraramNoEstudo(
			CotasQueNaoEntraramNoEstudoQueryDTO queryDTO) {
		List<EstudoCota> estudosCotas = getSession()
				.createQuery("from EstudoCota ec where ec.estudo.id = :id")
				.setParameter("id", queryDTO.getEstudo()).list();

		Map<String, Object> params = new HashMap<>();
		List<Long> ids = new ArrayList<>();
		String hql = "from Cota c ";
		String where = " where c.id not in (:ids)";

		for (EstudoCota estudoCota : estudosCotas) {
			ids.add(estudoCota.getCota().getId());
		}
		params.put("ids", ids);

		if (queryDTO.possuiCota()) {
			hql += " and c.id = :cota";
			params.put("cota", queryDTO.getCota());
		}

		if (queryDTO.possuiNome()) {
			hql += " and c.pessoa.nome= :nome";
			params.put("cota", queryDTO.getNome());
		}

		if (queryDTO.possuiElemento()) {
			if (queryDTO.elementoIsTipoPontoVenda()) {
				hql += " inner join c.pdvs as pdvs";

				where += " and pdvs.tipoPontoPDV.id = :tipoPDVID";
				params.put("tipoPDVID", queryDTO.getValorElemento());
			}
			if (queryDTO.elementoIsGeradoorDeFluxo()) {
				hql += " inner join c.pdvs as pdvs ";

				where += " and pdvs.geradorFluxoPDV.id = :geradorDeFluxoId";
				params.put("geradorDeFluxoId", queryDTO.getValorElemento());
			}
			if (queryDTO.elementoIsBairro()) {
				hql += " inner join c.pdvs as pdvs ";
				hql += " inner join pdvs.enderecos enderecosPDV  ";

				where += " and enderecosPDV.endereco.bairro = :bairro";
				params.put("bairro", queryDTO.getValorElemento());
			}
			if (queryDTO.elementoIsRegiao()) {
				hql += " left join c.registroCotaRegiao as registroRegiao ";

				where += " and registroRegiao.regiao.nome = :regiao";
				params.put("regiao", queryDTO.getValorElemento());
			}
			if (queryDTO.elementoIsAreaDeInfluencia()) {
				hql += " inner join c.pdvs as pdvs ";

				where += " and pdvs.areaInfluenciaPDV.desccricao = :descricao";
				params.put("descricao", queryDTO.getValorElemento());
			}
			if (queryDTO.elementoIsDistrito()) {
				hql += " inner join c.pdvs as pdvs ";
				hql += " inner join pdvs.enderecos enderecosPDV  ";

				where += " and enderecosPDV.endereco.uf = :uf";
				params.put("uf", queryDTO.getValorElemento());
			}
			if (queryDTO.elementoIsCotasAVista()) {

			}
			if (queryDTO.elementoIsCotasNovas()) {

			}
		}
		
		Query query = getSession().createQuery(hql+where);
		populateQuery(query, params);

		List<Cota> cotas= query.list();
		List<CotaQueNaoEntrouNoEstudoDTO> lista = query.list();
		for (Cota cota : cotas) {
			lista.add(new CotaQueNaoEntrouNoEstudoDTO(cota));
		}
		return lista;
	}

	public Session getSession() {
		return sessionFactory.openSession();
	}
	
	private void populateQuery(Query query, Map<String,Object> params) {
		Set<String> keySet = params.keySet();
		for (String key : keySet) {
			Object object = params.get(key);
			query.setParameter(key, object);
		}
		
	}

}
