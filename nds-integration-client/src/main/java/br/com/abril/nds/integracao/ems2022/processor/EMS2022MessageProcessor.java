package br.com.abril.nds.integracao.ems2022.processor;

import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.LogExecucaoRepository;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class EMS2022MessageProcessor extends AbstractRepository implements MessageProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(EMS2022MessageProcessor.class);

    private static final String driverOracle = "oracle.jdbc.driver.OracleDriver";

    @Autowired
    private ParametroSistemaRepository parametroSistemaRepository;


    @Autowired
    private Environment env;

    private static final String PROC_INTERFACE_NDS_NAW = "{call PROC_INTERFACE_NDS_NAW(?,?,?,?,?)}";

    @Autowired
    private LogExecucaoRepository logExecucaoRepository;

    @Override
    public void preProcess(AtomicReference<Object> tempVar) {
        LOGGER.info("EMS2022 preProcesss Client: {}", tempVar);

        List<Object> objs = new ArrayList<Object>();
        Object dummyObj = new Object();
        objs.add(dummyObj);

        tempVar.set(objs);
    }

    @Override
    public void processMessage(Message message) {

        LOGGER.error(":: Carregando Acessos NA");

        Connection con = null;
        Statement stmt = null;
        PreparedStatement pstmt = null;

        try {

            String nomeInterface = message.getHeader().get(MessageHeaderProperties.URI.getValue()).toString();
            Date dataUltimaExecucao = logExecucaoRepository.buscarDataUltimaExecucao(nomeInterface);

            /**
             * Busca código do distribuidor e cotas que possuem registro na AcessoNA e é foram atualizadas desde a ultima execução
             */

            String codigoDistribuidorDinap = message.getHeader().get(MessageHeaderProperties.CODIGO_DISTRIBUIDOR.getValue()).toString();


            StringBuilder sql = new StringBuilder(" from Cota c inner join fetch c.acessoNA na join fetch c.pessoa where 1 = 1 ");
            if(dataUltimaExecucao != null) {
                sql.append(" and na.dataAlteracao >= :dataUltimaExecucao");
            }

            Query query = super.getSession().createQuery(sql.toString());

            if(dataUltimaExecucao != null) {
                query.setParameter("dataUltimaExecucao", dataUltimaExecucao);
            }

            List<Cota> cotas = query.list();

            if(!cotas.isEmpty()) {
                con = this.getDBConnection();
            }
            /**
             * Validamos se existe registro na tabela PONTO_VENDA no ICD, se não existir criamos o registro. Em seguida, chamamos a PROC que atualizará os acessos
             * ao numeros atrasados
             */
            for (Cota cota: cotas) {

                PreparedStatement preparedStatement = con.prepareStatement("select count(1) from ponto_venda where cod_distribuidor = ? and cod_cota = ?");
                preparedStatement.setString (1, codigoDistribuidorDinap);
                preparedStatement.setLong   (2, cota.getNumeroCota());

                ResultSet rs = preparedStatement.executeQuery();
                rs.next();
                long qtdPontoVenda = rs.getLong(1);


                if(qtdPontoVenda == 0L){
                    LOGGER.info(":: INSERIR REGISTRO NA PONTO_VENDA");

                    con.setAutoCommit(false);

                    StringBuilder insertSQL = new StringBuilder("INSERT INTO ponto_venda (")
                                .append(" cod_ponto_venda_ptvd, ")
                                .append(" cod_distribuidor, ")
                                .append(" cod_cota, ")
                                .append(" cod_situacao_cota_ptvd, ")
                                .append(" cod_jornaleiro_ptvd, ")
                                .append(" nome_jornaleiro_ptvd, ")
                                .append(" cod_ponto_venda_sdc_ptvd, ")
                                .append(" cod_sistema_sipv, ")
                                .append(" tipo_logradouro_ptvd, ")
                                .append(" nome_logradouro_ptvd, ")
                                .append(" num_logradouro_ptvd, ")
                                .append(" compl_logradouro_ptvd, ")
                                .append(" cod_bairro_brro, ")
                                .append(" nome_bairro_ptvd, ")
                                .append(" nome_municipio_ptvd, ")
                                .append(" sigla_uf_ptvd, ")
                                .append(" cod_cep_ptvd, ")
                                .append(" cod_ddd_telefone_ptvd, ")
                                .append(" num_telefone_ptvd, ")
                                .append(" ind_municipio_indefinido, ")
                                .append(" ind_na_web, " )
                                .append(" end_email" )
                                .append(") VALUES (cod_ponto_venda_ptvd.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
                    pstmt = con.prepareStatement(insertSQL.toString());

                    PDV pdvPrincipal = cota.getPDVPrincipal();
                    EnderecoPDV enderecoPDV = pdvPrincipal.getEnderecoPrincipal();
                    String tipoLogradouro = enderecoPDV.getEndereco().getTipoLogradouro();
                    TelefoneCota telefone = cota.getTefefonePrincipal();

                    int paramIndex = 1;
                    pstmt.setString     (paramIndex++, codigoDistribuidorDinap);
                    pstmt.setLong       (paramIndex++, cota.getNumeroCota());
                    pstmt.setString     (paramIndex++, pdvPrincipal.getStatus().getDescricaoIcd());
                    pstmt.setLong       (paramIndex++, cota.getPessoa().getId()); // Alinhado com o Sergio
                    pstmt.setString     (paramIndex++, cota.getPessoa().getNome().length() > 35 ? cota.getPessoa().getNome().substring(0, 35) : cota.getPessoa().getNome() );
                    // cod_ponto_venda_sdc_ptvd || codigo agente devedor
                    pstmt.setLong       (paramIndex++, pdvPrincipal.getId()); // Alinhado com o Sergio
                    pstmt.setNull       (paramIndex++, Types.INTEGER);
                    pstmt.setString     (paramIndex++, tipoLogradouro.length() > 5 ? tipoLogradouro.substring(0,5) : tipoLogradouro);
                    pstmt.setString     (paramIndex++, enderecoPDV.getEndereco().getLogradouro());
                    pstmt.setString     (paramIndex++, enderecoPDV.getEndereco().getNumero());
                    pstmt.setString     (paramIndex++, enderecoPDV.getEndereco().getComplemento());
                    pstmt.setInt        (paramIndex++, enderecoPDV.getEndereco().getCodigoUf()); // TODO - Codigo bairro NÃO ESTÁ MAPEADO NA ENTIDADE
                    pstmt.setString     (paramIndex++, enderecoPDV.getEndereco().getBairro());
                    pstmt.setString     (paramIndex++, enderecoPDV.getEndereco().getCidade());
                    pstmt.setString     (paramIndex++, enderecoPDV.getEndereco().getUf());
                    pstmt.setString     (paramIndex++, enderecoPDV.getEndereco().getCep());
                    pstmt.setString     (paramIndex++, telefone.getTelefone().getDdd());
                    pstmt.setString     (paramIndex++, telefone.getTelefone().getNumeroSemFormatacao());
                    pstmt.setString     (paramIndex++, "S");
                    pstmt.setString     (paramIndex++, "S");
                    pstmt.setString     (paramIndex++, pdvPrincipal.getEmail());

                    int insertCount = pstmt.executeUpdate();
                    LOGGER.error("Commit 1");
                    con.commit();
                    con.setAutoCommit(true);

                }

                CallableStatement callableStatement = con.prepareCall(PROC_INTERFACE_NDS_NAW);

                callableStatement.setInt    (1, Integer.valueOf(codigoDistribuidorDinap));
                callableStatement.setInt    (2, cota.getNumeroCota());
                callableStatement.setString (3, cota.getPessoa().getEmail());
                callableStatement.setString (4, cota.getAcessoNA().isAcessoAtivo() ? "ATV" : "SUS");
                callableStatement.registerOutParameter(5, Types.VARCHAR);
                callableStatement.executeUpdate();
                String p_retorno = callableStatement.getString(5);

                LOGGER.error("Retorno da PROC_INTERFACE_NDS_NAW: {}", p_retorno);

                if(!"SUCESSO".equals(p_retorno)) {
                    message.getHeader().put(MessageHeaderProperties.ERRO_PROCESSAMENTO.getValue(), "Erro ao processar PROC. Retorno" + p_retorno);
                }


            } // Fim do For

            LOGGER.error(":: A Carga da EMS2022 foi finalizada");


        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            message.getHeader().put(MessageHeaderProperties.ERRO_PROCESSAMENTO.getValue(), "Erro ao processar registro. " + e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if(con != null) {
                    con.close();
                }
            } catch (SQLException e){ }
        }

    }

    @Override
    public void posProcess(Object tempVar) {
        LOGGER.info("EMS2022 posProcess Client: {}", tempVar);
    }

    private Connection getDBConnection() {
        Connection dbConnection = null;

        String driverClassName = driverOracle;
        String url =  parametroSistemaRepository.getParametro("ICDDB_URL");
        String username =  parametroSistemaRepository.getParametro("ICDDB_USERNAME");
        String password =  parametroSistemaRepository.getParametro("ICDDB_PASSWORD");

        try {
            Class.forName(driverClassName);
            LOGGER.error("Eu vou me conectar no ICD");
            dbConnection = DriverManager.getConnection(url, username,password);
            LOGGER.error("Eu me conectei: ", dbConnection );
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return dbConnection;
    }

}
