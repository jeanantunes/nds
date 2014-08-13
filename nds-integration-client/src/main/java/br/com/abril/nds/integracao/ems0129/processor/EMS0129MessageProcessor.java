package br.com.abril.nds.integracao.ems0129.processor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.dto.DetalhesPickingDTO;
import br.com.abril.nds.dto.HeaderPickingDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.ems0129.data.SomaRegistro;
import br.com.abril.nds.integracao.ems0129.outbound.EMS0129Picking1Detalhe;
import br.com.abril.nds.integracao.ems0129.outbound.EMS0129Picking1Header;
import br.com.abril.nds.integracao.ems0129.outbound.EMS0129Picking1Trailer;
import br.com.abril.nds.integracao.ems0129.outbound.EMS0129Picking2Detalhe;
import br.com.abril.nds.integracao.ems0129.outbound.EMS0129Picking2Header;
import br.com.abril.nds.integracao.ems0129.outbound.EMS0129Picking2Trailer;
import br.com.abril.nds.integracao.ems0129.outbound.InterfaceDetalhesPicking;
import br.com.abril.nds.integracao.ems0129.route.EMS0129Route;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.TipoImpressaoInterfaceLED;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.DescontoService;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

@Component

public class EMS0129MessageProcessor extends AbstractRepository implements MessageProcessor  {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EMS0129Route.class);

	private static final String NOME_ARQUIVO_PICKING_INTERFACE_LED_DEFAULT = "PICKING.NEP";
	
	@Autowired
	private FixedFormatManager fixedFormatManager;

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private DescontoService descontoService;

	private String mensagemValidacao;
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void processMessage(Message message) {
		
		this.setMensagemValidacao(null);
		
		Distribuidor distribuidor = distribuidorRepository.obter();
		
		if (distribuidor.getTipoImpressaoInterfaceLED().equals(TipoImpressaoInterfaceLED.MODELO_1)) {
			
			processarArquivoPickingModelo1(message, distribuidor);
			
		} else if (distribuidor.getTipoImpressaoInterfaceLED().equals(TipoImpressaoInterfaceLED.MODELO_2)) {
			
			processarArquivoPickingModelo2(message, distribuidor);

		} else if (distribuidor.getTipoImpressaoInterfaceLED().equals(TipoImpressaoInterfaceLED.MODELO_3)) {



				LOGGER.warn(String.format("Leiaute Picking Distribuidor %s nao implementado !",
						distribuidor.getTipoImpressaoInterfaceLED()));

			
		} else {
			
			String mensagemValidacao = "Leiaute Picking Invalido.";
			
			this.lancarMensagemValidacao(mensagemValidacao, message);
		}

	}

	
	            /**
     * Processa geração do arquivo de Picking do Modelo1
     * 
     * @param message
     * @param distribuidor
     */
	private void processarArquivoPickingModelo1(Message message,
			Distribuidor distribuidor) {
		
		String nomeArquivoPickingInterfaceLED = distribuidor.getArquivoInterfaceLedPicking1();
		
		if (nomeArquivoPickingInterfaceLED == null) {
			
			nomeArquivoPickingInterfaceLED = NOME_ARQUIVO_PICKING_INTERFACE_LED_DEFAULT;
		}
		
		nomeArquivoPickingInterfaceLED = String.format("%1$s-%2$s", nomeArquivoPickingInterfaceLED, dateFormat.format(new Date()) );;
		
		gerarArquivoPickingModelo1(message, nomeArquivoPickingInterfaceLED);
	}

	
	            /**
     * Processa geração do arquivo de Picking do Modelo1
     * 
     * @param message
     * @param distribuidor
     */
	private void processarArquivoPickingModelo2(Message message,
			Distribuidor distribuidor) {
		
		String nomeArquivoPickingInterfaceLED = distribuidor.getArquivoInterfaceLedPicking2();
		
		if (nomeArquivoPickingInterfaceLED == null) {
			
			nomeArquivoPickingInterfaceLED = NOME_ARQUIVO_PICKING_INTERFACE_LED_DEFAULT;
		}
		
		nomeArquivoPickingInterfaceLED = String.format("%1$s-%2$s", nomeArquivoPickingInterfaceLED, dateFormat.format(new Date()));
		
		gerarArquivoPickingModelo2(message, nomeArquivoPickingInterfaceLED);
	}

	
	/**
	 * Gera o arquivo de acordo com o modelo de pickin 1
	 * 
	 * @param message
	 * @param nomeArquivoPickingInterfaceLED
	 */
	private void gerarArquivoPickingModelo1(Message message, String nomeArquivoPickingInterfaceLED) {

		try {

			PrintWriter print = new PrintWriter(
					new FileWriter(
							message.getHeader().get(TipoParametroSistema.PATH_INTERFACE_PICKING_EXPORTACAO.name()) 
									+ File.separator + nomeArquivoPickingInterfaceLED)); 
			
			Date data = getDataLancDistrib(message);
			
			List<HeaderPickingDTO> listHeaders = getHeadePicking(data);
			
			if (listHeaders.isEmpty()) {

				String mensagemValidacao = "Nenhum registro encontrado!";
				
				this.lancarMensagemValidacao(mensagemValidacao, message);
			}
			
			for (HeaderPickingDTO headerDTO : listHeaders) {
				
				EMS0129Picking1Header headerModelo1 = criarHeaderModelo1(headerDTO);
				
				print.println(fixedFormatManager.export(headerModelo1));

				List<DetalhesPickingDTO> listDataPickingDTO = getDetalhesPicking(headerModelo1.getIdCota(), data);
				
				for (DetalhesPickingDTO pickingDTO : listDataPickingDTO) {

					EMS0129Picking1Detalhe outdetalhe = (EMS0129Picking1Detalhe) getDetalhesFromDTO(pickingDTO, new EMS0129Picking1Detalhe());
					
					print.println(fixedFormatManager.export(outdetalhe));

				}
				
				EMS0129Picking1Trailer outtrailer = criaTrailer(print, headerDTO.getCodigoCota(), listDataPickingDTO.size());
				
				print.println(fixedFormatManager.export(outtrailer));
			}

			print.flush();
			print.close();

		} catch (IOException e) {
			
			String mensagemValidacao = "Erro ao gerar o arquivo. " + e.getMessage();
			
			this.lancarMensagemValidacao(mensagemValidacao, message);
		}

	}

	
	/**
	 * Gera o arquivo de acordo com o modelo de picking 2
	 * 
	 * @param message
	 * @param nomeArquivoPickingInterfaceLED
	 */
	private void gerarArquivoPickingModelo2(Message message, String nomeArquivoPickingInterfaceLED) {

		try {

			PrintWriter print = new PrintWriter(
					new FileWriter(message.getHeader().get(TipoParametroSistema.PATH_INTERFACE_PICKING_EXPORTACAO.name())	+ 
							File.separator  + nomeArquivoPickingInterfaceLED));
			
			Date data = getDataLancDistrib(message);
			
			List<HeaderPickingDTO> listHeaders = getHeadePicking(data);
			
			if (listHeaders.isEmpty()) {

				String mensagemValidacao = "Nenhum registro encontrado!";
				
				this.lancarMensagemValidacao(mensagemValidacao, message);
			}	
	
			for (HeaderPickingDTO headerDTO : listHeaders) {
				
				EMS0129Picking2Header headerModelo2 = criarHeaderModelo2(headerDTO);
				
				print.println(fixedFormatManager.export(headerModelo2));
				
				int numeroCota = headerDTO.getCodigoCota();
				
				List<DetalhesPickingDTO> listDataPickingDTO = getDetalhesPicking(headerModelo2.getIdCota(), data);
				
				SomaRegistro somaRegistros = criarDetalhesModelo2(print, listDataPickingDTO);
				
				criaTrailer(print, numeroCota, somaRegistros.getValorTotalBruto(), somaRegistros.getValorTotalDesconto());
			}

			print.flush();
			print.close();

		} catch (IOException e) {

			String mensagemValidacao = "Erro ao gerar o arquivo. " + e.getMessage();
			
			this.lancarMensagemValidacao(mensagemValidacao, message);
		}

	}

	
	            /**
     * Obtém a data de Lançamento do Distribuidor
     * 
     * @param message
     * @return
     */
	private Date getDataLancDistrib(Message message) {
		
		Date data = (Date) message.getHeader().get("DATA_LCTO_DISTRIB");
		
		if (data == null) {
			
			String mensagemValidacao = "Data nao informada. Execute a interface EMS0129 passando a dataLancamentoDistribuidor!";
			
			this.lancarMensagemValidacao(mensagemValidacao, message);
		}
		
		return data;
	}
	
	
	            /**
     * Obtém os dados de detalhes para geração do arquivo
     * 
     * @param message
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<DetalhesPickingDTO> getDetalhesPicking(Long idCota, Date data) {
		
		StringBuilder sql = new StringBuilder();

		sql.append(" select c.id as idCota ");
		sql.append(" ,c.numero_cota as numeroCota ");
		sql.append(" ,mec.qtde as qtdeMEC ");
		sql.append(" ,p.codigo as codigoProduto ");
		sql.append(" ,p.nome as nomeProduto ");
		sql.append(" ,l.sequencia_matriz as sequenciaMatriz ");
		sql.append(" ,pe.numero_edicao as codigoEdicao ");
		sql.append(" ,pe.preco_custo as precoCustoProdutoEdicao ");
		sql.append(" ,pe.preco_venda as precoVendaProdutoEdicao ");
		sql.append(" ,mec.valor_desconto as valorDescontoMEC ");
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
				Arrays.asList( 
						GrupoMovimentoEstoque.RECEBIMENTO_JORNALEIRO_JURAMENTADO.name(),
						GrupoMovimentoEstoque.SOBRA_DE_COTA.name(), 
						GrupoMovimentoEstoque.SOBRA_EM_COTA.name(),
						GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name(), 
						GrupoMovimentoEstoque.RATEIO_REPARTE_COTA_AUSENTE.name(),
						GrupoMovimentoEstoque.RESTAURACAO_REPARTE_COTA_AUSENTE.name()
				));
		
		query.addScalar("idCota", StandardBasicTypes.LONG);
		query.addScalar("numeroCota", StandardBasicTypes.INTEGER);
		query.addScalar("qtdeMEC", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("codigoProduto", StandardBasicTypes.STRING);
		query.addScalar("codigoEdicao", StandardBasicTypes.LONG);
		query.addScalar("nomeProduto", StandardBasicTypes.STRING);
		query.addScalar("sequenciaMatriz", StandardBasicTypes.INTEGER);
		query.addScalar("precoCustoProdutoEdicao", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("precoVendaProdutoEdicao", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("valorDescontoMEC", StandardBasicTypes.BIG_DECIMAL);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(DetalhesPickingDTO.class));

		return (List<DetalhesPickingDTO>) query.list();
	}
	
	
	
	            /**
     * Cria o cabeçalho para os arquivos do modelo1
     * 
     * @param headerDTO
     * @return
     */
	public EMS0129Picking1Header criarHeaderModelo1(HeaderPickingDTO headerDTO) {
		EMS0129Picking1Header outheader = new EMS0129Picking1Header();
		
		outheader.setIdCota(headerDTO.getIdCota());
		outheader.setCodigoCota(String.format("%1$05d", headerDTO.getCodigoCota()));
		outheader.setNomeCota(headerDTO.getNomeCota());
		
		return outheader;
	}

	
	            /**
     * Cria o cabeçalho para os arquivos do modelo 2
     * 
     * @param headerDTO
     * @return
     */
	public EMS0129Picking2Header criarHeaderModelo2(HeaderPickingDTO headerDTO) {
		
		EMS0129Picking2Header outheader = new EMS0129Picking2Header();
		
		outheader.setIdCota(headerDTO.getIdCota());
		outheader.setData(headerDTO.getData());
		outheader.setCodigoCota(String.format("%1$05d", headerDTO.getCodigoCota()));
		outheader.setNomeCotaComCpfCnpj(String.format("%1$s||%2$s", headerDTO.getNomeCota(), headerDTO.getDocumento()));
		outheader.setNomeFantasia(headerDTO.getNomeCota());
		
		return outheader;
	}

	            /**
     * Obtém dados utilizado no header dos arquivos de LED
     * 
     * @param data
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<HeaderPickingDTO> getHeadePicking(Date data) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct(c.id) as idCota");
		sql.append(" ,c.numero_cota as codigoCota ");
		sql.append(" ,(case when p.CPF then p.cpf else p.cnpj end) as documento");
		sql.append(" ,pdv.NOME as nomeCota ");
		sql.append(" ,mec.data as data");
		sql.append(" from cota c ");
		sql.append(" join pdv pdv on pdv.cota_id = c.id  ");
		sql.append(" join pessoa p on p.id = c.pessoa_id ");
		sql.append(" join movimento_estoque_cota mec on mec.cota_id = c.ID ");
		sql.append(" join tipo_movimento tm on tm.id = mec.TIPO_MOVIMENTO_ID ");
		sql.append(" where mec.DATA = :data ");
		sql.append(" and pdv.PONTO_PRINCIPAL = true ");
		sql.append(" and tm.GRUPO_MOVIMENTO_ESTOQUE in (:grupos) ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("data", data);
		query.setParameterList("grupos", 
				Arrays.asList( 
						GrupoMovimentoEstoque.RECEBIMENTO_JORNALEIRO_JURAMENTADO.name(),
						GrupoMovimentoEstoque.SOBRA_DE_COTA.name(), 
						GrupoMovimentoEstoque.SOBRA_EM_COTA.name(),
						GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name(), 
						GrupoMovimentoEstoque.RATEIO_REPARTE_COTA_AUSENTE.name(),
						GrupoMovimentoEstoque.RESTAURACAO_REPARTE_COTA_AUSENTE.name()
				));
		
		query.addScalar("idCota", StandardBasicTypes.LONG);
		query.addScalar("codigoCota", StandardBasicTypes.INTEGER );
		query.addScalar("documento", StandardBasicTypes.STRING );
		query.addScalar("nomeCota", StandardBasicTypes.STRING);
		query.addScalar("data", StandardBasicTypes.DATE);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(HeaderPickingDTO.class));
		
		return (List<HeaderPickingDTO>) query.list();
	}
	
	/**
	 * Cria trailer do arquivo a ser gerado
	 * @param print
	 * @param numeroCota
	 * @param quantidadeRegistros
	 */
	private EMS0129Picking1Trailer criaTrailer(PrintWriter print, Integer numeroCota,
			int quantidadeRegistros) {

		EMS0129Picking1Trailer outtrailer = new EMS0129Picking1Trailer();

		outtrailer.setCodigoCota(numeroCota);
		outtrailer.setQuantidadeRegistros(quantidadeRegistros);
		
		return outtrailer;
	}

	
	/**
	 * Cria Trailer do arquivo a ser gerado
	 * 
	 * @param print
	 * @param numeroCota
	 * @param valorTotalBruto
	 * @param valorTotalDesconto
	 */
	private void criaTrailer(PrintWriter print, Integer numeroCota,	BigDecimal valorTotalBruto, BigDecimal valorTotalDesconto) {

		EMS0129Picking2Trailer outtrailer = new EMS0129Picking2Trailer();

		outtrailer.setCodigoCota(numeroCota);
		outtrailer.setValorTotalBruto(valorTotalBruto);
		outtrailer.setValorTotalDesconto(valorTotalDesconto);

		print.println(fixedFormatManager.export(outtrailer));
	}

	/**
	 * Cria os detalhes do arquivo a ser gerado
	 * 
	 * @param print
	 * @param numeroCota
	 * @param movimentoEstoqueCotas
	 * @param somaRegistros
	 * @return
	 */
	private SomaRegistro criarDetalhesModelo2(PrintWriter print, 
			List<DetalhesPickingDTO> listPickingDTO) {

		EMS0129Picking2Detalhe outdetalhe;
		
		int qtdeRegistros = 0;
		
		SomaRegistro somaRegistros = new SomaRegistro();
		
		for (DetalhesPickingDTO pickingDTO : listPickingDTO) {
			
			outdetalhe = (EMS0129Picking2Detalhe) getDetalhesFromDTO(pickingDTO, new EMS0129Picking2Detalhe());
			
			print.println(fixedFormatManager.export(outdetalhe));
			
			BigDecimal valorTotalBruto =
				pickingDTO.getPrecoVendaProdutoEdicao().multiply(
						new BigDecimal(pickingDTO.getQtdeMEC()));
			
			BigDecimal valorTotalVenda =
				pickingDTO.getPrecoCustoProdutoEdicao().multiply(
						new BigDecimal(pickingDTO.getQtdeMEC()));
			
			somaRegistros.addValorTotalBruto(valorTotalBruto);
			somaRegistros.addValorTotalDesconto(valorTotalVenda);
		}
		
		somaRegistros.addQtdeRegistros(qtdeRegistros);
	
		return somaRegistros;

	}
	
	
	/**
	 * Gera o modelo base dos detalhes
	 * 
	 * @param pickingDTO
	 * @param modelo
	 * @return
	 */
	public InterfaceDetalhesPicking getDetalhesFromDTO(DetalhesPickingDTO pickingDTO, InterfaceDetalhesPicking modelo) {
		
		modelo.setCodigoCota(String.format("%1$05d", pickingDTO.getNumeroCota()));

		modelo.setQuantidade(pickingDTO.getQtdeMEC().longValue());
        
		modelo.setCodigoProduto(String.format("%1$08d", Integer.parseInt(pickingDTO.getCodigoProduto())));
		
		modelo.setEdicao(String.format("%1$04d",pickingDTO.getCodigoEdicao()));
		
		modelo.setNomePublicacao(pickingDTO.getNomeProduto());
		
		modelo.setPrecoCusto(pickingDTO.getPrecoCustoProdutoEdicao());
		
		modelo.setPrecoVenda(pickingDTO.getPrecoVendaProdutoEdicao());
		
		modelo.setDesconto(pickingDTO.getValorDescontoMEC());
		
		modelo.setSequenciaNotaEnvio(String.format("%1$03d",pickingDTO.getSequenciaMatriz()));
		
		return modelo;
	}

	            /**
     * Lança Exceção com Mensagem de Validação
     * 
     * @param mensagemValidacao
     * @param message
     * @throws ValidacaoException
     */
	private void lancarMensagemValidacao(String mensagemValidacao, Message message) throws ValidacaoException {
		
        this.ndsiLoggerFactory.getLogger().logWarning(message,
				EventoExecucaoEnum.GERACAO_DE_ARQUIVO, mensagemValidacao);
		
		this.setMensagemValidacao(mensagemValidacao);
		
		throw new ValidacaoException(TipoMensagem.WARNING, mensagemValidacao);
	}
	
	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}

	/**
	 * @return the mensagemValidacao
	 */
	public String getMensagemValidacao() {
		return mensagemValidacao;
	}

	/**
	 * @param mensagemValidacao the mensagemValidacao to set
	 */
	public void setMensagemValidacao(String mensagemValidacao) {
		this.mensagemValidacao = mensagemValidacao;
	}
}