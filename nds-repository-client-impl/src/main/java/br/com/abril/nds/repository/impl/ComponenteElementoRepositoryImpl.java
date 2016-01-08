package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ComponenteElementoDTO;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.cadastro.pdv.AreaInfluenciaPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoGeradorFluxoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.model.distribuicao.Regiao;
import br.com.abril.nds.repository.ComponenteElementoRepository;
import br.com.abril.nds.util.UfEnum;

@Repository
@SuppressWarnings("unchecked")
public class ComponenteElementoRepositoryImpl implements ComponenteElementoRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<ComponenteElementoDTO> buscaTiposDePontoDeVena(Long estudo) {

        StringBuilder sql = new StringBuilder();
        sql.append("select distinct ");
        sql.append("       t.* ");
        sql.append("  from tipo_ponto_pdv t ");
        sql.append("  join pdv p on p.tipo_ponto_pdv_id = t.id ");
        sql.append("  join cota c on c.id = p.cota_id ");
        sql.append("  join estudo_cota_gerado ec on ec.cota_id = c.id and ec.estudo_id = :estudoId ");
        sql.append(" order by t.descricao ");

        Query query = getSession().createSQLQuery(sql.toString()).addEntity(TipoPontoPDV.class);
        query.setParameter("estudoId", estudo);
        List<TipoPontoPDV> list = query.list();

        List<ComponenteElementoDTO> retorno = new ArrayList<>();
        for (TipoPontoPDV tipo : list) {
            retorno.add(new ComponenteElementoDTO("tipo_ponto_venda", tipo.getId(), tipo.getDescricao()));
        }
        return retorno;
    }

    @Override
    public List<ComponenteElementoDTO> buscaGeradorDeFluxo(Long estudo) {

        StringBuilder sql = new StringBuilder();
        sql.append("select distinct ");
        sql.append("       t.* ");
        sql.append("  from tipo_gerador_fluxo_pdv t ");
        sql.append("  join gerador_fluxo_pdv g on g.tipo_gerador_fluxo_id = t.id ");
        sql.append("  join pdv on pdv.id = g.pdv_id ");
        sql.append("  join cota c on c.id = pdv.cota_id ");
        sql.append("  join estudo_cota_gerado ec on ec.cota_id = c.id and ec.estudo_id = :estudoId ");
        sql.append(" order by t.descricao ");

        Query query = getSession().createSQLQuery(sql.toString()).addEntity(TipoGeradorFluxoPDV.class);
        query.setParameter("estudoId", estudo);
        List<TipoGeradorFluxoPDV> list = query.list();

        List<ComponenteElementoDTO> retorno = new ArrayList<>();
        for (TipoGeradorFluxoPDV tipoGerador : list) {
            retorno.add(new ComponenteElementoDTO("gerador_de_fluxo", tipoGerador.getId(), tipoGerador.getDescricao()));
        }
        return retorno;
    }

    @Override
    public List<ComponenteElementoDTO> buscaBairros(Long estudo) {

        StringBuilder sql = new StringBuilder();
        sql.append("select distinct e.bairro ");
        sql.append("  from cota c ");
        sql.append("  join estudo_cota_gerado ec on ec.cota_id = c.id and ec.estudo_id = :estudoId ");
        sql.append("  join pdv p on p.cota_id = c.id ");
        sql.append("  join endereco_pdv ep on ep.pdv_id = p.id ");
        sql.append("  join endereco e on e.id = ep.endereco_id and e.bairro is not null and trim(e.bairro) <> '' ");
        sql.append(" order by 1 ");

        Query query = getSession().createSQLQuery(sql.toString());
        query.setParameter("estudoId", estudo);
        List<String> lista = query.list();

        List<ComponenteElementoDTO> retorno = new ArrayList<>();
        for (String item : lista) {
            retorno.add(new ComponenteElementoDTO("bairro", 0, item));
        }
        return retorno;
    }

    @Override
    public List<ComponenteElementoDTO> buscaRegioes(Long estudo) {

        StringBuilder sql = new StringBuilder();
        sql.append("select distinct ");
        sql.append("       r.id, ");
        sql.append("       r.nome_regiao, ");
        sql.append("       r.regiao_is_fixa, ");
        sql.append("       r.data_regiao, ");
        sql.append("       r.usuario_id ");
        sql.append("  from regiao r ");
        sql.append("  join registro_cota_regiao rcr on rcr.regiao_id = r.id ");
        sql.append("  join estudo_cota_gerado ec on ec.cota_id = rcr.cota_id and ec.estudo_id = :estudoId ");
        sql.append(" order by r.nome_regiao ");

        Query query = getSession().createSQLQuery(sql.toString()).addEntity(Regiao.class);
        query.setParameter("estudoId", estudo);
        List<Regiao> list = query.list();

        List<ComponenteElementoDTO> retorno = new ArrayList<>();
        for (Regiao regiao : list) {
            retorno.add(new ComponenteElementoDTO("regiao", regiao.getId(), regiao.getNomeRegiao()));
        }
        return retorno;
    }

    @Override
    public List<ComponenteElementoDTO> buscaCotasAVista() {
        List<ComponenteElementoDTO> retorno = new ArrayList<>();
        retorno.add(new ComponenteElementoDTO("cotas_a_vista", "A-VISTA", TipoCota.A_VISTA.toString()));
        //retorno.add(new ComponenteElementoDTO("cotas_a_vista", "CONSIGNADO", TipoCota.CONSIGNADO.toString())); removido a pedido do cliente.
        return retorno;
    }

    @Override
    public List<ComponenteElementoDTO> buscaCotasNovas() {
        List<ComponenteElementoDTO> retorno = new ArrayList<>();
        retorno.add(new ComponenteElementoDTO("cotas_novas", 1, "Sim"));
        //retorno.add(new ComponenteElementoDTO("cotas_novas", 0, "Não")); removido a pedido do cliente.
        return retorno;
    }
    
    @Override
	public List<ComponenteElementoDTO> buscaLegendaCotas() {
        List<ComponenteElementoDTO> retorno = new ArrayList<>();
        
        retorno.add(new ComponenteElementoDTO("legenda_cota", "AJ", "AJ - AJUSTE"));
        retorno.add(new ComponenteElementoDTO("legenda_cota", "CP", "CP - COMPLEMENTAR"));
        retorno.add(new ComponenteElementoDTO("legenda_cota", "IN", "IN - INCLUSÃO"));
        retorno.add(new ComponenteElementoDTO("legenda_cota", "FX", "FX - FIXAÇÃO"));
        retorno.add(new ComponenteElementoDTO("legenda_cota", "MX", "MX - MIX"));
        retorno.add(new ComponenteElementoDTO("legenda_cota", "PR", "PR - PROPORCIONAL"));
        retorno.add(new ComponenteElementoDTO("legenda_cota", "RD", "RD - REDUTOR"));
        retorno.add(new ComponenteElementoDTO("legenda_cota", "SN", "SN - SEGMENTO EXCEÇÃO"));
        retorno.add(new ComponenteElementoDTO("legenda_cota", "TR", "TR - AJUSTE DE REPARTE"));
        retorno.add(new ComponenteElementoDTO("legenda_cota", "TD", "TODAS LEGENDAS "));
        
        return retorno;
    }

    @Override
    public List<ComponenteElementoDTO> buscaTipoDistribuicaoCotas() {
        List<ComponenteElementoDTO> retorno = new ArrayList<>();
        retorno.add(new ComponenteElementoDTO("tipo_distribuicao_cota", TipoDistribuicaoCota.CONVENCIONAL, "Convencional"));
        retorno.add(new ComponenteElementoDTO("tipo_distribuicao_cota", TipoDistribuicaoCota.ALTERNATIVO, "Alternativo"));
        return retorno;
    }

    @Override
    public List<ComponenteElementoDTO> buscaAreaDeInfluencia(Long estudo) {

        StringBuilder sql = new StringBuilder();
        sql.append("select distinct ");
        sql.append("       a.* ");
        sql.append("  from area_influencia_pdv a ");
        sql.append("  join pdv p on p.area_influencia_pdv_id = a.id ");
        sql.append("  join cota c on c.id = p.cota_id ");
        sql.append("  join estudo_cota_gerado ec on ec.cota_id = c.id and ec.estudo_id = :estudoId ");
        sql.append(" order by a.descricao ");

        Query query = getSession().createSQLQuery(sql.toString()).addEntity(AreaInfluenciaPDV.class);
        query.setParameter("estudoId", estudo);
        List<AreaInfluenciaPDV> list = query.list();

        List<ComponenteElementoDTO> retorno = new ArrayList<>();
        for (AreaInfluenciaPDV area : list) {
            retorno.add(new ComponenteElementoDTO("area_influencia", area.getId(), area.getDescricao()));
        }
        return retorno;
    }

    @Override
    public List<ComponenteElementoDTO> buscaDistritos(Long estudo) {

        StringBuilder sql = new StringBuilder();
        sql.append("select distinct e.uf ");
        sql.append("  from cota c ");
        sql.append("  join estudo_cota_gerado ec on ec.cota_id = c.id and ec.estudo_id = :estudoId ");
        sql.append("  join pdv p on p.cota_id = c.id ");
        sql.append("  join endereco_pdv ep on ep.pdv_id = p.id ");
        sql.append("  join endereco e on e.id = ep.endereco_id and e.uf is not null and trim(e.uf) <> '' ");
        sql.append(" order by 1 ");

        Query query = getSession().createSQLQuery(sql.toString());
        query.setParameter("estudoId", estudo);
        List<String> lista = query.list();

        List<ComponenteElementoDTO> retorno = new ArrayList<>();
        for (String item : lista) {
            retorno.add(new ComponenteElementoDTO("distrito", UfEnum.valueOf(item), UfEnum.valueOf(item).getDescricao()));
        }
        return retorno;
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }

}
