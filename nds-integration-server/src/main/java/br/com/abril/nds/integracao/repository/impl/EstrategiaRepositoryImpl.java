package br.com.abril.nds.integracao.repository.impl;

import br.com.abril.nds.integracao.model.canonic.EMS2021Input;
import br.com.abril.nds.integracao.model.canonic.EMS2021InputItem;
import br.com.abril.nds.integracao.repository.EstrategiaRepository;
import br.com.abril.nds.model.integracao.icd.IcdEstrategia;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Repository
@Transactional("transactionManagerIcd")
public class EstrategiaRepositoryImpl extends AbstractRepositoryModel<IcdEstrategia, Long> implements EstrategiaRepository {

    public EstrategiaRepositoryImpl() {
        super(IcdEstrategia.class);
    }

    @Override
    public List<EMS2021Input> obterEstrategias(Long codigoDistribuidor) {

        StringBuilder sql = new StringBuilder();
        sql.append("select   DSTR.COD_DISTRIBUIDOR as \"codigoDistribuidor\" ");
        sql.append("        ,PRAC.COD_PRACA as \"codigoPraca\" ");
        sql.append("        ,TO_CHAR(LPAD(LEPU.COD_PUBLICACAO_ADABAS, 8, '0')) as \"codigoProduto\" ");
        sql.append("        ,LEPU.NUM_EDICAO as \"numeroEdicao\" ");
        sql.append("        ,LEPU.NUM_LANCTO_EDICAO as \"periodo\" ");
        sql.append("        ,LEPU.COD_LANCTO_EDICAO as \"codigoLancamentoEdicao\" ");
        sql.append("        ,ESMD.PCT_ABRANGENCIA_DISTBCAO as \"abrangenciaDistribuicao\" ");
        sql.append("        ,ESMD.TXT_OPORTUNIDADE_VENDA as \"oportunidadeVenda\" ");
        sql.append("        ,ESMD.QTD_REPARTE_MINIMO as \"reparteMinimo\" ");
        sql.append("    from DISTRIBUIDOR DSTR ");
        sql.append("    inner join PRACA PRAC on DSTR.COD_DISTRIBUIDOR = PRAC.COD_DISTRIBUIDOR ");
        sql.append("    inner join ESTRATEGIA_LANCTO_PRACA ESLP   on PRAC.COD_PRACA = ESLP.COD_PRACA ");
        sql.append("    inner join ESTRATEGIA_MICRO_DISTBCAO ESMD on ESLP.COD_ESTRATEGIA = ESMD.COD_ESTRATEGIA ");
        sql.append("    inner join LANCTO_EDICAO_PUBLICACAO LEPU on ESMD.COD_LANCTO_EDICAO = LEPU.COD_LANCTO_EDICAO ");
        sql.append("    where  PRAC.IND_PRACA_ATIVA = 'S' ");
        sql.append("    and    DSTR.COD_DISTRIBUIDOR = :P_CODIGO_DISTRIBUIDOR ");
        sql.append("    and    (ESMD.DAT_ALT > (SYSDATE - 450) OR ESMD.DAT_INC > (SYSDATE - 450)) ");

        Query query = getSessionIcd().createSQLQuery(sql.toString());
        query.setResultTransformer(new AliasToBeanResultTransformer(EMS2021Input.class));
        query.setParameter("P_CODIGO_DISTRIBUIDOR", codigoDistribuidor);

        ((SQLQuery) query).addScalar("codigoDistribuidor", StandardBasicTypes.LONG);
        ((SQLQuery) query).addScalar("codigoPraca", StandardBasicTypes.INTEGER);
        ((SQLQuery) query).addScalar("codigoProduto", StandardBasicTypes.STRING);
        ((SQLQuery) query).addScalar("numeroEdicao", StandardBasicTypes.LONG);
        ((SQLQuery) query).addScalar("periodo", StandardBasicTypes.INTEGER);
        ((SQLQuery) query).addScalar("codigoLancamentoEdicao", StandardBasicTypes.BIG_INTEGER);
        ((SQLQuery) query).addScalar("abrangenciaDistribuicao", StandardBasicTypes.LONG);
        ((SQLQuery) query).addScalar("oportunidadeVenda", StandardBasicTypes.STRING);
        ((SQLQuery) query).addScalar("reparteMinimo", StandardBasicTypes.BIG_INTEGER);

        return query.list();
    }

    @Override
    public List<EMS2021InputItem> obterEdicaoBaseEstrategia(Integer codigoPraca, BigInteger codigoLancamentoEdicao) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT   TO_CHAR(LPAD(LEPU.COD_PUBLICACAO_ADABAS, 8, '0')) AS \"codigoProduto\" ");
        sql.append("        ,LEPU.NUM_EDICAO as \"numeroEdicao\" ");
        sql.append("        ,LEPU.NUM_LANCTO_EDICAO AS \"periodo\" ");
        sql.append("        ,CMBC.PCT_BASE_CALCULO AS \"peso\" ");
        sql.append("    FROM ESTRATEGIA_LANCTO_PRACA ELPR ");
        sql.append("    INNER JOIN ESTRATEGIA_MICRO_DISTBCAO EMDI ON ELPR.COD_ESTRATEGIA = EMDI.COD_ESTRATEGIA ");
        sql.append("    LEFT JOIN  COMPOSICAO_BASE_CALCULO CMBC   ON EMDI.COD_ESTRATEGIA = CMBC.COD_ESTRATEGIA ");
        sql.append("    INNER JOIN LANCTO_EDICAO_PUBLICACAO LEPU  ON CMBC.COD_LANCTO_EDICAO = LEPU.COD_LANCTO_EDICAO ");
        sql.append("    WHERE ELPR.COD_PRACA = :P_COD_PRACA ");
        sql.append("    AND   ELPR.COD_LANCTO_EDICAO = :P_COD_LANCTO_EDICAO ");

        Query query = getSessionIcd().createSQLQuery(sql.toString());
        query.setResultTransformer(new AliasToBeanResultTransformer(EMS2021InputItem.class));
        query.setParameter("P_COD_PRACA", codigoPraca);
        query.setParameter("P_COD_LANCTO_EDICAO", codigoLancamentoEdicao);

        ((SQLQuery) query).addScalar("codigoProduto", StandardBasicTypes.STRING);
        ((SQLQuery) query).addScalar("numeroEdicao", StandardBasicTypes.LONG);
        ((SQLQuery) query).addScalar("periodo", StandardBasicTypes.INTEGER);
        ((SQLQuery) query).addScalar("peso", StandardBasicTypes.INTEGER);

        return query.list();
    }
}