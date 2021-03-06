package br.com.abril.nds.integracao.ems0197.processor;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import br.com.abril.nds.dto.*;
import br.com.abril.nds.repository.DescontoProdutoEdicaoRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.ftfutil.FTFParser;
import br.com.abril.nds.helper.LancamentoHelper;
import br.com.abril.nds.integracao.ems0197.outbound.EMS0197Detalhe;
import br.com.abril.nds.integracao.ems0197.outbound.EMS0197Header;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.model.cadastro.desconto.DescontoDTO;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.service.ControleCotaService;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.ExporteCouch;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.util.TirarAcento;

import br.com.abril.nds.efacil.ConectionEfacil;

@Component
public class EMS0197MessageProcessor extends AbstractRepository implements MessageProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(EMS0197MessageProcessor.class);

	private static final String REPARTE_FOLDER = "reparte";

	private static final String REPARTE_EXT = ".LCT";

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	@Autowired
	private FixedFormatManager fixedFormatManager;

	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private DescontoService descontoService;

	@Autowired
	private ControleCotaService controleCotaService;

	@Autowired
	private ExporteCouch exporteCouch;

	@Autowired
	private DescontoProdutoEdicaoRepository descontoProdutoEdicaoRepository;

	private Date dataLctoDistrib;

	/** Quantidade de arquivos processados. */
	private int quantidadeArquivosGerados = 0;
	
	@Override
	public void processMessage(Message message) {

		this.quantidadeArquivosGerados = 0;

		List<EMS0197Header> listHeaders = this.criarHeader(dataLctoDistrib);
		if (!listHeaders.isEmpty()) {
			final Map<String, DescontoDTO> descontos = descontoService
					.obterDescontosMapPorLancamentoProdutoEdicao(dataLctoDistrib);

			for (EMS0197Header outheader : listHeaders) {

				try {

					String nomeArquivo = "" + outheader.getCodDistribuidor() + "."
							+ StringUtils.leftPad(outheader.getNumeroCota(), 5, '0') + "."
							+ sdf.format(outheader.getDataLctoDistrib());

					PrintWriter print = new PrintWriter(new FileWriter(
							message.getHeader().get(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO.name())
									+ File.separator + REPARTE_FOLDER + File.separator + nomeArquivo + REPARTE_EXT));

					List<IpvLancamentoDTO> listDetalhes = getDetalhesPickingLancamento(outheader.getIdCota(),
							this.dataLctoDistrib);

					addDescontoProduto(descontos, outheader, print, listDetalhes);

					print.flush();
					print.close();

					this.quantidadeArquivosGerados++;

				} catch (IOException e) {
					LOGGER.error("Falha ao gerar arquivo.", e);
				}

			}
			try {

				Iterator it = FileUtils.iterateFiles(
						new File(message.getHeader().get(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO.name())
								+ File.separator + REPARTE_FOLDER),
						new String[] { "LCT" }, false);

				while (it.hasNext()) {
					File file = (File) it.next();
					String name = file.getName();
					Integer cota = Integer.parseInt(name.split("\\.")[1]);
					Integer cotaMaster = controleCotaService.buscarCotaMaster(cota);
					if (cotaMaster != null && !cotaMaster.equals(cota)) { // tem cota master, appendar no arquivo
						String nomeArquivoMaster = "" + name.split("\\.")[0] + "."
								+ StringUtils.leftPad(cotaMaster.toString(), 5, '0') + "." + (name.split("\\.")[2]);

						File fileMaster = new File(
								message.getHeader().get(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO.name())
										+ File.separator + REPARTE_FOLDER + File.separator + nomeArquivoMaster
										+ REPARTE_EXT);
						if (!fileMaster.exists())
							this.quantidadeArquivosGerados++;
						cat(file, fileMaster);
						file.delete();
					}

				}
			} catch (Exception ee) {
				LOGGER.error("Falha ao gerar arquivo caruso.", ee);
			}

			compactarArquivos(message);

		} else {
			LOGGER.warn("Nenhum registro encontrado para esta data" + dataLctoDistrib);
		}

		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			List<CotaCouchDTO> lista = cotaRepository.getCotaLancamento(dataLctoDistrib);

			for (CotaCouchDTO reparte : lista) {
				List<ProdutoCouchDTO> produtos = cotaRepository.getProdutoLancamento(reparte.getIdCota(), formatter.parse(reparte.getDataMovimento()));

				for(ProdutoCouchDTO produto: produtos){
					DescontoDTO desconto =	descontoService.obterDescontoPor(new Integer(reparte.getCodigoCota()),produto.getCodigoProduto(),new Long(produto.getNumeroEdicao()));

					BigDecimal valorDesconto = new BigDecimal(produto.getPrecoCapa()).multiply((desconto.getValor().divide(new BigDecimal(100))));
					produto.setPrecoCusto(new BigDecimal(produto.getPrecoCapa()).subtract(valorDesconto).toString());
				}
				reparte.setProdutos(produtos);
			}

			if(!lista.isEmpty()) {
		//		exportarLancamentoEfacil(lista);

				exporteCouch.exportarLancamentoRecolhimento(lista, "Lancamento");
			}
		} catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
		}

	}

	private void exportarLancamentoEfacil(List<CotaCouchDTO> lista ){
		ConectionEfacil c = new ConectionEfacil();
				EfacilDTO efacilDTO = new EfacilDTO();
				efacilDTO.setDados(createJsonLancamento(lista).toString());
				efacilDTO.setMetodo("POST");
				efacilDTO.setPrivateKey("jytCqej8a50GlPp0Esio4mk1qrc39g8pBSURCeZepsD5vtgXREr31XMR5kevvtXhSEnCGxyIqLPCLX1dFxH7yMTLoigdZV32LZOL");
				efacilDTO.setToken("fea6261d13e6dd2911ac8aa11f90d3fa");
				efacilDTO.setURL("https://gerenciador.efacil.net/rest/banca/lancamento");
				c.postEfacil(efacilDTO);
	}


	private JsonArray createJsonLancamento(List<CotaCouchDTO>  listaLancamento){

		JsonArray jsonCotaLancamentoArray = new JsonArray();



		for(CotaCouchDTO cotaLancamento: listaLancamento){

			JsonObject jsonCotaLancamento = new JsonObject();
			jsonCotaLancamento.addProperty("codigoCota", cotaLancamento.getCodigoCota());
			jsonCotaLancamento.addProperty("codigoDistribuidor", cotaLancamento.getCodigoDistribuidor());
			jsonCotaLancamento.addProperty("dataMovimento", cotaLancamento.getDataMovimento());


			JsonArray jsonProdutos = new JsonArray();
			for(ProdutoCouchDTO produto: cotaLancamento.getProdutos()){
				JsonObject jsonProduto = new JsonObject();
				jsonProduto.addProperty("codigoPublicacao", produto.getCodigoProduto());
				jsonProduto.addProperty("numeroEdicao", produto.getNumeroEdicao());
				jsonProduto.addProperty("codigoBarras", produto.getCodigoBarrasProduto());
				jsonProduto.addProperty("nomePublicacao", produto.getNomeProduto());
				if(produto.getReparte()!=null){
					jsonProduto.addProperty("quantidadeReparte", produto.getReparte());
				}else{
					jsonProduto.addProperty("quantidadeReparte", 0);

				}
				jsonProduto.addProperty("codigoEditora", produto.getCodigoEditora());
				jsonProduto.addProperty("nomeEditora", produto.getNomeEditora());

				if(produto.getReparte()!=null){
					jsonProduto.addProperty("precoCapa", produto.getPrecoCapa());
				}else{
					jsonProduto.addProperty("precoCapa", 0);

				}

				if(produto.getReparte()!=null){
					jsonProduto.addProperty("precoCusto", produto.getPrecoCusto());
				}else{
					jsonProduto.addProperty("precoCusto", 0);

				}

				jsonProduto.addProperty("chamadaCapa", produto.getChamadaCapa());
				jsonProduto.addProperty("dataLancamento", produto.getDataLancamento());
				jsonProduto.addProperty("dataPrimeiroLancamentoParcial", "");
				jsonProdutos.add(jsonProduto);


			}
			jsonCotaLancamento.add("itens",jsonProdutos);
			jsonCotaLancamentoArray.add(jsonCotaLancamento);
		}



		return jsonCotaLancamentoArray;
	}




	public void cat(File origem, File destino) throws IOException {

		FileInputStream fis = new FileInputStream(origem);
		BufferedReader in = new BufferedReader(new InputStreamReader(fis));

		FileWriter fstream = new FileWriter(destino, true);
		BufferedWriter out = new BufferedWriter(fstream);

		String aLine = null;
		while ((aLine = in.readLine()) != null) {
			out.write(aLine);
			out.newLine();
		}

		in.close();

		out.close();
	}

	private void compactarArquivos(Message message) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dir = message.getHeader().get(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO.name())
				+ File.separator + REPARTE_FOLDER + File.separator;
		File diretorio = new File(dir);
		FileOutputStream fos = null;
		ZipOutputStream zipOut = null;
		FileInputStream fis = null;
		try {

			fos = new FileOutputStream(
					message.getHeader().get(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO.name())
							+ File.separator + REPARTE_FOLDER + File.separator + "zip" + File.separator + "reparte-"
							+ sdf.format(dataLctoDistrib) + ".zip");
			zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
			for (File input : diretorio.listFiles()) {

				if (input.isDirectory()) {
					continue;
				}

				fis = new FileInputStream(input);
				ZipEntry ze = new ZipEntry(input.getName());

				zipOut.putNextEntry(ze);
				byte[] tmp = new byte[4 * 1024];
				int size = 0;
				while ((size = fis.read(tmp)) != -1) {

					zipOut.write(tmp, 0, size);
				}
				zipOut.flush();
				fis.close();
			}

			zipOut.close();

			for (File input : diretorio.listFiles()) {

				if (input.isDirectory()) {
					continue;
				}

				input.delete();
			}

		} catch (FileNotFoundException e) {

			LOGGER.error("Falha ao obter arquivo.", e);
		} catch (IOException e) {

			LOGGER.error("IOException", e);
		} finally {
			try {

				if (fos != null) {
					fos.close();
				}
			} catch (Exception ex) {

				LOGGER.error("Falha ao fechar arquivo.", ex);
			}
		}
	}

	private void addDescontoProduto(final Map<String, DescontoDTO> descontos, EMS0197Header outheader,
			PrintWriter print, List<IpvLancamentoDTO> listDetalhes) {

		for (IpvLancamentoDTO ipvLancamento : listDetalhes) {

			/**
			 * A busca dos descontos é feita diretamente no Map, por chave,
			 * agilizando o retorno do resultado
			 */
			DescontoDTO descontoDTO = null;
			try {

				if (ipvLancamento.getIdFornecedor() == null) {
					throw new Exception("Produto sem Fornecedor cadastrado!");
				}

				if (ipvLancamento.getIdEditor() == null) {
					throw new Exception("Produto sem Editor cadastrado!");
				}

				descontoDTO = descontoService.obterDescontoPor(descontos, outheader.getIdCota(),
						ipvLancamento.getIdFornecedor(), ipvLancamento.getIdEditor(), ipvLancamento.getIdProduto(),
						ipvLancamento.getIdProdutoEdicao());

				if (descontoDTO == null) {
					LOGGER.error("Produto sem desconto: " + ipvLancamento.getCodProduto() + " / "
							+ ipvLancamento.getNumEdicao());
					throw new ValidacaoException();
				}
			} catch (final ValidacaoException e) {
				final String msg = "Produto sem desconto: " + ipvLancamento.getCodProduto() + " / "
						+ ipvLancamento.getNumEdicao();
				LOGGER.error(msg, e);
				throw new ValidacaoException(TipoMensagem.ERROR, msg);
			} catch (final Exception e) {
				final String msg = e.getMessage();
				LOGGER.error(msg, e);
				throw new ValidacaoException(TipoMensagem.ERROR, msg);
			}

			final BigDecimal desconto = descontoDTO != null ? descontoDTO.getValor() : BigDecimal.ZERO;
			BigDecimal precoVenda = new BigDecimal(ipvLancamento.getPrecoCapa());
			final BigDecimal precoComDesconto = precoVenda
					.subtract(MathUtil.calculatePercentageValue(precoVenda, desconto));
			ipvLancamento.setPrecoCapa(precoVenda.multiply(BigDecimal.valueOf(100)).setScale(0).toString());

			ipvLancamento.setPrecoCusto(
					precoComDesconto.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).toString());

			exportarDadosParaArquivo(print, ipvLancamento);

		}
	}

	private void exportarDadosParaArquivo(PrintWriter print, IpvLancamentoDTO ipvLancamento) {

		EMS0197Detalhe outDetalhe = createDetalhes(ipvLancamento);

		print.write(fixedFormatManager.export(outDetalhe), 0, 204);
		print.print("\r\n");
	}

	/**
	 * Cria o trailer do arquivo
	 * 
	 * @param jornaleiro
	 * @return private EMS0197Trailer createTrailer(String numeroCota, Integer
	 *         qtdRegistros) {
	 * 
	 *         EMS0197Trailer outTrailer = new EMS0197Trailer();
	 * 
	 *         outTrailer.setNumeroCota(numeroCota);
	 *         outTrailer.setQtdeRegTipo2(qtdRegistros);
	 * 
	 *         return outTrailer; }
	 */

	/**
	 * Cria os detalhes do arquivo
	 * 
	 */
	private EMS0197Detalhe createDetalhes(IpvLancamentoDTO dto) {

		EMS0197Detalhe outDetalhe = new EMS0197Detalhe();

		String detalhe = "";

		FTFParser ftfParser = new FTFParser();

		try {
			detalhe = ftfParser.parseArquivo(dto);
		} catch (Exception e) {

			LOGGER.error("Erro no parse do Arquivo", e);
		}

		detalhe = TirarAcento.removerAcentuacao(detalhe);
		outDetalhe.setDetalhes(detalhe);

		return outDetalhe;
	}

	@SuppressWarnings("unchecked")
	public List<EMS0197Header> criarHeader(Date data) {

		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT DISTINCT (c.id) AS idCota, ");
		sql.append("                 c.numero_cota AS numeroCota, ");
		sql.append("                 pdv.NOME AS nomePDV, ");
		sql.append("                  lan.DATA_LCTO_DISTRIBUIDOR AS dataLctoDistrib, ");
		sql.append(
				"                 (SELECT if (d.COD_DISTRIBUIDOR_DINAP != 0,d.COD_DISTRIBUIDOR_DINAP,d.COD_DISTRIBUIDOR_FC) FROM distribuidor d LIMIT 1) AS codDistribuidor ");
		sql.append("   FROM cota c ");
		sql.append("        JOIN pdv pdv ");
		sql.append("           ON pdv.cota_id = c.id ");
		sql.append("        JOIN estudo_cota_gerado ecg ");
		sql.append("           ON ecg.COTA_ID = c.ID ");
		sql.append("        JOIN estudo_gerado eg  ");
		sql.append("           ON ecg.ESTUDO_ID = eg.ID ");
		sql.append("       JOIN lancamento lan ON lan.ESTUDO_ID = eg.id       ");
		sql.append("  WHERE  lan.DATA_LCTO_DISTRIBUIDOR = :data and lan.STATUS in ('BALANCEADO', 'EXPEDIDO') ");
		sql.append("           AND pdv.PONTO_PRINCIPAL = true ");
		sql.append("           AND ecg.QTDE_EFETIVA > 0 ");
		sql.append("           AND c.UTILIZA_IPV = true ");
		sql.append("           AND c.tipo_transmissao='TXT' ");

		SQLQuery query = this.getSession().createSQLQuery(sql.toString());

		query.setParameter("data", data);

		query.addScalar("idCota", StandardBasicTypes.LONG);
		query.addScalar("dataLctoDistrib", StandardBasicTypes.DATE);
		query.addScalar("numeroCota", StandardBasicTypes.STRING);
		query.addScalar("nomePDV", StandardBasicTypes.STRING);
		query.addScalar("codDistribuidor", StandardBasicTypes.STRING);

		query.setResultTransformer(new AliasToBeanResultTransformer(EMS0197Header.class));

		return (List<EMS0197Header>) query.list();
	}

	/**
	 * 
	 * @param idCota
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DetalhesPickingDTO> getDetalhesPicking(Long idCota, Date data) {

		StringBuilder sql = new StringBuilder();

		sql.append(" select c.id as idCota ");
		sql.append(" ,c.numero_cota as numeroCota ");
		sql.append(" ,mec.qtde as qtdeMEC ");
		sql.append(" ,CAST(p.CODIGO as UNSIGNED) as codigoProduto ");
		sql.append(" ,p.nome as nomeProduto ");
		sql.append(" ,l.sequencia_matriz as sequenciaMatriz ");
		sql.append(" ,pe.numero_edicao as codigoEdicao ");
		sql.append(" ,pe.preco_custo as precoCustoProdutoEdicao ");
		sql.append(" ,pe.preco_venda as precoVendaProdutoEdicao ");
		sql.append(" ,mec.valor_desconto as valorDescontoMEC ");
		sql.append(" ,pe.codigo_de_barras as codigoDeBarrasProdutoEdicao ");
		sql.append(" from movimento_estoque_cota mec ");
		sql.append(" join lancamento l on l.id = mec.lancamento_id ");
		sql.append(" join cota c on mec.COTA_ID = c.ID  ");
		sql.append(" join produto_edicao pe on pe.id = mec.produto_edicao_id ");
		sql.append(" join produto p on p.id = pe.produto_id ");
		sql.append(" join tipo_movimento tm on tm.id = mec.TIPO_MOVIMENTO_ID ");
		sql.append(" where mec.data = :data ");
		sql.append(" and c.id = :idCota ");
		sql.append(" and tm.GRUPO_MOVIMENTO_ESTOQUE in (:grupos) ");

		SQLQuery query = this.getSession().createSQLQuery(sql.toString());

		query.setParameter("data", data);
		query.setParameter("idCota", idCota);
		query.setParameterList("grupos",
				Arrays.asList(GrupoMovimentoEstoque.RECEBIMENTO_JORNALEIRO_JURAMENTADO.name(),
						GrupoMovimentoEstoque.SOBRA_DE_COTA.name(), GrupoMovimentoEstoque.SOBRA_EM_COTA.name(),
						GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name(),
						GrupoMovimentoEstoque.RATEIO_REPARTE_COTA_AUSENTE.name(),
						GrupoMovimentoEstoque.RESTAURACAO_REPARTE_COTA_AUSENTE.name()));

		query.addScalar("idCota", StandardBasicTypes.LONG);
		query.addScalar("numeroCota", StandardBasicTypes.INTEGER);
		query.addScalar("qtdeMEC", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("codigoProduto", StandardBasicTypes.STRING);
		query.addScalar("codigoEdicao", StandardBasicTypes.LONG);
		query.addScalar("codigoDeBarrasProdutoEdicao", StandardBasicTypes.STRING);
		query.addScalar("nomeProduto", StandardBasicTypes.STRING);
		query.addScalar("sequenciaMatriz", StandardBasicTypes.INTEGER);
		query.addScalar("precoCustoProdutoEdicao", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("precoVendaProdutoEdicao", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("valorDescontoMEC", StandardBasicTypes.BIG_DECIMAL);

		query.setResultTransformer(new AliasToBeanResultTransformer(DetalhesPickingDTO.class));

		return (List<DetalhesPickingDTO>) query.list();
	}

	@SuppressWarnings("unchecked")
	public List<IpvLancamentoDTO> getDetalhesPickingLancamento(Long idCota, Date data) {

		StringBuilder sql = new StringBuilder();

		sql.append("   SELECT ");
		sql.append("       '01' as versao, ");
		sql.append("       'L' as tipoArquivo, ");
		sql.append(
				"       CAST((SELECT if (d.COD_DISTRIBUIDOR_DINAP != 0,d.COD_DISTRIBUIDOR_DINAP,d.COD_DISTRIBUIDOR_FC) FROM distribuidor d LIMIT 1) AS CHAR) AS codDistribuidor, ");
		sql.append("       CAST(coalesce(c.numero_jornaleiro_ipv,c.PESSOA_ID) AS CHAR) AS codJornaleiro, ");
		sql.append("       CAST(c.NUMERO_COTA AS CHAR) AS codCota, ");
		sql.append("       CAST(coalesce(pdvs.numero_pdv,pdvs.ID) AS CHAR )  AS codPDV, ");
		sql.append("       DATE_FORMAT((lct.DATA_LCTO_DISTRIBUIDOR), '%Y%m%d') AS dataMovimento, ");
		sql.append("       CAST(SUBSTRING(p.CODIGO, -8) AS CHAR) AS codProduto, ");
		sql.append("       CAST(pe.NUMERO_EDICAO AS CHAR) AS numEdicao, ");
		sql.append("       CAST(pe.CODIGO_DE_BARRAS AS CHAR) AS codBarras, ");
		sql.append("       p.NOME AS nomeProduto, ");
		sql.append("       CAST(ROUND(ecg.QTDE_EFETIVA, 0) AS CHAR) AS reparte, ");
		sql.append("       pes.RAZAO_SOCIAL AS nomeEditora, ");
		sql.append("       CAST(ROUND(pe.PRECO_VENDA, 2) AS CHAR) AS precoCapa, ");

		sql.append("       pe.CHAMADA_CAPA AS chamadaCapa, ");
		sql.append("       DATE_FORMAT((lct.DATA_LCTO_DISTRIBUIDOR), '%Y%m%d') AS dataLancamento, ");
		sql.append("   '        '  as dataPrimeiroLancamentoParcial,");
		sql.append("       CAST(lct.ID AS CHAR) as idLancamento, ");
		sql.append("       CAST(pe.ID AS CHAR) as idProdutoEdicao, ");
		sql.append("       CAST(p.ID AS CHAR) as idProduto, ");
		sql.append("       CAST(f.ID AS CHAR) as idFornecedor, ");
		sql.append("       CAST(edt.ID AS CHAR) as idEditor ");

		sql.append("   FROM estudo_cota_gerado ecg ");

		sql.append("     JOIN estudo_gerado eg ON ecg.ESTUDO_ID = eg.ID ");
		sql.append("     JOIN lancamento lct ON eg.LANCAMENTO_ID = lct.ID ");
		sql.append("     JOIN produto_edicao pe  ON lct.PRODUTO_EDICAO_ID = pe.ID ");
		sql.append("     JOIN produto p ON pe.PRODUTO_ID = p.ID ");
		sql.append("     JOIN cota c ON ecg.COTA_ID = c.ID ");
		sql.append("     JOIN pdv pdvs ON pdvs.COTA_ID = c.ID and pdvs.ponto_principal is true");
		sql.append("     LEFT JOIN editor edt ON p.EDITOR_ID = edt.ID ");
		sql.append("     JOIN pessoa pes ON edt.JURIDICA_ID = pes.ID ");
		sql.append("     LEFT JOIN produto_fornecedor pf ON pf.PRODUTO_ID = p.ID ");
		sql.append("     LEFT JOIN fornecedor f ON pf.fornecedores_ID = f.ID ");

		sql.append("       WHERE lct.DATA_LCTO_DISTRIBUIDOR = :data ");
		sql.append("       		and lct.STATUS in (:statusLancamento) ");
		sql.append("       		and ecg.REPARTE > 0 ");
		sql.append(" 	 		and c.id = :idCota");
		sql.append(" 	 		and c.UTILIZA_IPV is true ");
		sql.append(" 	 		order by lct.SEQUENCIA_MATRIZ ");

		SQLQuery query = this.getSession().createSQLQuery(sql.toString());

		query.setParameter("data", data);
		query.setParameter("idCota", idCota);
		query.setParameterList("statusLancamento",
				LancamentoHelper.getStatusLancamentosPosBalanceamentoLancamentoString());

		query.addScalar("versao", StandardBasicTypes.STRING);
		query.addScalar("tipoArquivo", StandardBasicTypes.STRING);
		query.addScalar("codDistribuidor", StandardBasicTypes.STRING);
		query.addScalar("codJornaleiro", StandardBasicTypes.STRING);
		query.addScalar("codCota", StandardBasicTypes.STRING);
		query.addScalar("codPDV", StandardBasicTypes.STRING);
		query.addScalar("dataMovimento", StandardBasicTypes.STRING);
		query.addScalar("codProduto", StandardBasicTypes.STRING);
		query.addScalar("numEdicao", StandardBasicTypes.STRING);
		query.addScalar("codBarras", StandardBasicTypes.STRING);
		query.addScalar("nomeProduto", StandardBasicTypes.STRING);
		query.addScalar("reparte", StandardBasicTypes.STRING);
		query.addScalar("nomeEditora", StandardBasicTypes.STRING);
		query.addScalar("precoCapa", StandardBasicTypes.STRING);
		query.addScalar("chamadaCapa", StandardBasicTypes.STRING);
		query.addScalar("dataLancamento", StandardBasicTypes.STRING);
		query.addScalar("dataPrimeiroLancamentoParcial", StandardBasicTypes.STRING);
		query.addScalar("idLancamento", StandardBasicTypes.LONG);
		query.addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
		query.addScalar("idProduto", StandardBasicTypes.LONG);
		query.addScalar("idFornecedor", StandardBasicTypes.LONG);
		query.addScalar("idEditor", StandardBasicTypes.LONG);

		query.setResultTransformer(new AliasToBeanResultTransformer(IpvLancamentoDTO.class));

		return (List<IpvLancamentoDTO>) query.list();
	}
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub

	}

	public void setDataLctoDistrib(Date dataLctoDistrib) {
		this.dataLctoDistrib = dataLctoDistrib;
	}

	/**
	 * Retorna a quantidade de arquivos gerados apos o processamento.
	 * 
	 * @return
	 */
	public int getQuantidadeArquivosGerados() {
		return quantidadeArquivosGerados;
	}

}
