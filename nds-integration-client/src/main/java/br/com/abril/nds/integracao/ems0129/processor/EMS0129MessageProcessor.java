package br.com.abril.nds.integracao.ems0129.processor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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

import br.com.abril.icd.axis.util.Constantes;
import br.com.abril.icd.axis.util.DateUtil;
import br.com.abril.nds.dto.DetalhesPickingDTO;
import br.com.abril.nds.dto.DetalhesPickingPorCotaModelo03DTO;
import br.com.abril.nds.dto.FooterPickingModelo3DTO;
import br.com.abril.nds.dto.HeaderPickingDTO;
import br.com.abril.nds.dto.PickingLEDFullDTO;
import br.com.abril.nds.dto.SubHeaderPickingDTO;
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
import br.com.abril.nds.integracao.ems0129.outbound.EMS0129Picking3Footer;
import br.com.abril.nds.integracao.ems0129.outbound.EMS0129Picking3Header;
import br.com.abril.nds.integracao.ems0129.outbound.EMS0129Picking3Trailer2;
import br.com.abril.nds.integracao.ems0129.outbound.EMS0129Picking3Trailer3;
import br.com.abril.nds.integracao.ems0129.outbound.EMS0129Picking3_8_Trailer2;
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
import br.com.abril.nds.service.LedModelo4IntegracaoService;
import br.com.abril.nds.util.TirarAcento;

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
	private LedModelo4IntegracaoService ledModelo4IntegracaoService;
	
	private String mensagemValidacao;
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void processMessage(Message message) {

		this.setMensagemValidacao(null);
		
		LOGGER.info("obter o distribuidor");
		Distribuidor distribuidor = distribuidorRepository.obter();
		
		if (distribuidor.getTipoImpressaoInterfaceLED().equals(TipoImpressaoInterfaceLED.MODELO_1)) {
			
			processarArquivoPickingModelo1(message, distribuidor);
			
		} else if (distribuidor.getTipoImpressaoInterfaceLED().equals(TipoImpressaoInterfaceLED.MODELO_2)) {
			
			// processarArquivoPickingModelo2(message, distribuidor);
			processarArquivoPickingModelo3_8(message, distribuidor);

		} else if (distribuidor.getTipoImpressaoInterfaceLED().equals(TipoImpressaoInterfaceLED.MODELO_3)) {
			
			processarArquivoPickingModelo3(message, distribuidor);
			
		} else if (distribuidor.getTipoImpressaoInterfaceLED().equals(TipoImpressaoInterfaceLED.MODELO_4)) {
			
			processarArquivoPickingModelo4(message, distribuidor);
			
		} else {
			
			String mensagemValidacao = "Leiaute Picking Invalido.";
			
			this.lancarMensagemValidacao(mensagemValidacao, message);
		}

	}

	
	            /**
     * Processa geraÃ§Ã£o do arquivo de Picking do Modelo1
     * 
     * @param message
     * @param distribuidor
     */
	private void processarArquivoPickingModelo1(Message message, Distribuidor distribuidor) {
		
		String nomeArquivoPickingInterfaceLED = distribuidor.getArquivoInterfaceLedPicking1();
		
		if (nomeArquivoPickingInterfaceLED == null) {
			
			nomeArquivoPickingInterfaceLED = NOME_ARQUIVO_PICKING_INTERFACE_LED_DEFAULT;
		}
		
		nomeArquivoPickingInterfaceLED = String.format("%1$s-%2$s", nomeArquivoPickingInterfaceLED, dateFormat.format(new Date()) );;
		
		gerarArquivoPickingModelo1(message, nomeArquivoPickingInterfaceLED);
	}

	
	            /**
     * Processa geraÃ§Ã£o do arquivo de Picking do Modelo1
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
	
	
	private void processarArquivoPickingModelo4(Message message, Distribuidor distribuidor) {
		
		String nomeArquivoPickingInterfaceLED = distribuidor.getArquivoInterfaceLedPicking4();
		
		if (nomeArquivoPickingInterfaceLED == null) {
			
			nomeArquivoPickingInterfaceLED = "PICKING4";
		}else{
			nomeArquivoPickingInterfaceLED = nomeArquivoPickingInterfaceLED.substring(0, (nomeArquivoPickingInterfaceLED.length() - 4));
		}
		
		String dataFormatada = DateUtil.formatarData(Calendar.getInstance().getTime(), Constantes.DATE_TIME_PT_BR_FOR_FILE).toString();
		
		nomeArquivoPickingInterfaceLED += "-"+dataFormatada+".TXT";
		
		gerarArquivoPickingModelo4(message, nomeArquivoPickingInterfaceLED);
	}
	
	
	private void gerarArquivoPickingModelo4(Message message, String nomeArquivoPickingInterfaceLED) {

		try {

			FileWriter fileWriter = new FileWriter(message.getHeader().get(TipoParametroSistema.PATH_INTERFACE_PICKING_EXPORTACAO.name())	+ 
					File.separator  + nomeArquivoPickingInterfaceLED);
			
			PrintWriter print = new PrintWriter(fileWriter, true);
			
			Date dataLancamento = getDataLancDistrib(message);
			
			List<PickingLEDFullDTO> registros = new ArrayList<>();
			
			// LINHA 01 - OK
			List<HeaderPickingDTO> listHeaders = getHeadePickingModulo3(dataLancamento);
			
			// LINHA 03 - OK
			List<SubHeaderPickingDTO> listaSubHeadePickingModelo4 = getSubHeaderPickingModulo3(dataLancamento, true);
			
			if (listHeaders.isEmpty()) {

				String mensagemValidacao = "Nenhum registro encontrado!";
				
				this.lancarMensagemValidacao(mensagemValidacao, message);
			}
			
			int cont = 0;
			String cotasSemEnderecoLED = "";
			for (HeaderPickingDTO headerDTO : listHeaders) {
				
				EMS0129Picking3Header linha01Modelo04 = criarHeaderModelo3(headerDTO);
				
				print.println(fixedFormatManager.export(linha01Modelo04) + "\r");
				
				// LINHA 02 - OK
				List<DetalhesPickingPorCotaModelo03DTO> listaLinha02Modelo04 = getLinha02Modelo03(linha01Modelo04.getCodigoCota(), dataLancamento, true);
				
				for (DetalhesPickingPorCotaModelo03DTO detalhesPickingPorCotaModelo03DTO : listaLinha02Modelo04) {
					EMS0129Picking3Trailer2 linha02Modelo03 = criarLinha02Modelo03(detalhesPickingPorCotaModelo03DTO, true);
					print.println(fixedFormatManager.export(linha02Modelo03) + "\r");
				}
				
				SubHeaderPickingDTO subHeaderPickingDTO = listaSubHeadePickingModelo4.get(cont);
				
				long ccotal = Long.parseLong(subHeaderPickingDTO.getCodigoCota().replace(";",""));
				
				
				while(ccotal !=new Long(headerDTO.getCodigoCota()).longValue()){
					cont++;
					try {
					 subHeaderPickingDTO = listaSubHeadePickingModelo4.get(cont);
					 ccotal = Long.parseLong(subHeaderPickingDTO.getCodigoCota().replace(";",""));
					}catch (IndexOutOfBoundsException e) {
						System.out.println("Erro na execução do Led (Interface 129):"+e);
					}catch(Exception exx ){
						System.out.println("Erro na execução do Led (Interface 129):"+exx);
					}
					
				}
				
				subHeaderPickingDTO.setCredito(""+listaSubHeadePickingModelo4.size());
				subHeaderPickingDTO.setDebito(""+listaSubHeadePickingModelo4.size());
				//subHeaderPickingDTO.setConsignado("");
				
				EMS0129Picking3Trailer3 linha03Modelo04 = criarLinha03Modelo03(subHeaderPickingDTO);
				
				print.println(fixedFormatManager.export(linha03Modelo04) + "\r");
				
				cont++;
				if(subHeaderPickingDTO.getConsignado() != null){
					registros.add(this.popularRegistrosLED(headerDTO, listaLinha02Modelo04, subHeaderPickingDTO));
				}else{
					if(cotasSemEnderecoLED.isEmpty()){
						cotasSemEnderecoLED += headerDTO.getCodigoCota();
					}else{
						cotasSemEnderecoLED += ", "+headerDTO.getCodigoCota();
					}
				}
			}
			
			print.flush();
			print.close();
			
			Date data = (Date) message.getHeader().get("DATA_LCTO_DISTRIB");
			
			this.ledModelo4IntegracaoService.exportarPickingLED(registros, data);
			
			if(!cotasSemEnderecoLED.isEmpty()){
				String mensagemValidacao = "As seguintes cotas nÃ£o possuem endereÃ§o LED cadastrado: "+cotasSemEnderecoLED;
				this.lancarMensagemValidacao(mensagemValidacao, message);
			}

		} catch (IOException e) {

			String mensagemValidacao = "Erro ao gerar o arquivo. " + e.getMessage();
			
			this.lancarMensagemValidacao(mensagemValidacao, message);
		}

	}
	
	private PickingLEDFullDTO popularRegistrosLED(HeaderPickingDTO headerDTO, List<DetalhesPickingPorCotaModelo03DTO> listaLinha02Modelo04, SubHeaderPickingDTO subHeaderPickingDTO){
		
		PickingLEDFullDTO dto = new PickingLEDFullDTO();
		
		dto.setIdentificadorLinha1(headerDTO.getIdentificadorLinha());
		dto.setCodigoCotaLinha1(headerDTO.getCodigoCota().toString());
		dto.setNomeDistribuidor(headerDTO.getNomeDistribuidor());
		dto.setNomeCota(headerDTO.getNomeCota());
		dto.setCodigoBox(headerDTO.getCodigoBox());
		dto.setTipoOperacao(headerDTO.getConsignado());
		dto.setCpf(headerDTO.getCpf());
		dto.setCnpj(headerDTO.getCnpj());
		dto.setInscricaoMunicipal(headerDTO.getInscricaoEstadual());
		dto.setDataLancamento(headerDTO.getDataLancamento());
		
		dto.setListTrailer2(listaLinha02Modelo04);
		
		dto.setIdentificadorLinha3(subHeaderPickingDTO.getIdentificadorLinha());
		//dto.setCodigoCotaLinha3(subHeaderPickingDTO.getCodigoCota());
		if(headerDTO.getCodigoCota().toString().equals("125")){
		 dto.setCodigoCotaLinha3(headerDTO.getCodigoCota().toString());
		}
		dto.setPrecoTotal(subHeaderPickingDTO.getPrecoTotal());
		dto.setPrecoTotalDesconto(subHeaderPickingDTO.getPrecoTotalDesconto());
		dto.setObservacoes01(subHeaderPickingDTO.getCredito());
		dto.setObservacoes02(subHeaderPickingDTO.getDebito());
		dto.setDataLancamentoLinha3(subHeaderPickingDTO.getDataLancamento());
		dto.setEnderecoLED(subHeaderPickingDTO.getConsignado());
		
		return dto;
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
     * ObtÃ©m a data de LanÃ§amento do Distribuidor
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
     * ObtÃ©m os dados de detalhes para geraÃ§Ã£o do arquivo
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
     * Cria o cabeÃ§alho para os arquivos do modelo1
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
     * Cria o cabeÃ§alho para os arquivos do modelo 2
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
     * ObtÃ©m dados utilizado no header dos arquivos de LED
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
		
		modelo.setNomePublicacao(TirarAcento.removerAcentuacao(pickingDTO.getNomeProduto()));
		
		modelo.setPrecoCusto(pickingDTO.getPrecoCustoProdutoEdicao());
		
		modelo.setPrecoVenda(pickingDTO.getPrecoVendaProdutoEdicao());
		
		modelo.setDesconto(pickingDTO.getValorDescontoMEC());
		
		modelo.setSequenciaNotaEnvio(String.format("%1$03d",pickingDTO.getSequenciaMatriz()));
		
		return modelo;
	}

	            /**
     * LanÃ§a ExceÃ§Ã£o com Mensagem de ValidaÃ§Ã£o
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
	
	    /**
	* Processa geraÃ§Ã£o do arquivo de Picking do Modelo3
	* 
	* @param message
	* @param distribuidor
	*/
	private void processarArquivoPickingModelo3(Message message, Distribuidor distribuidor) {
	
		String nomeArquivoPickingInterfaceLED = distribuidor.getArquivoInterfaceLedPicking3();
		
		if (nomeArquivoPickingInterfaceLED == null) {
			
			nomeArquivoPickingInterfaceLED = NOME_ARQUIVO_PICKING_INTERFACE_LED_DEFAULT;
		}
		
		gerarArquivoPickingModelo3(message, nomeArquivoPickingInterfaceLED);
	}
	
	
    /**
	* Processa geraÃ§Ã£o do arquivo de Picking do Modelo3 com codigo produto com 8 caracteres
	* 
	* @param message
	* @param distribuidor
	*/
	private void processarArquivoPickingModelo3_8(Message message, Distribuidor distribuidor) {
	
		String nomeArquivoPickingInterfaceLED = distribuidor.getArquivoInterfaceLedPicking2();
		
		if (nomeArquivoPickingInterfaceLED == null) {
			
			nomeArquivoPickingInterfaceLED = NOME_ARQUIVO_PICKING_INTERFACE_LED_DEFAULT;
		}
		
		gerarArquivoPickingModelo3_8(message, nomeArquivoPickingInterfaceLED);
	}
	
	
	/**
	 * Gera o arquivo de acordo com o modelo de picking 3 com codigo de produto com 8 digitos
	 * 
	 * @param message
	 * @param nomeArquivoPickingInterfaceLED
	 */
	private void gerarArquivoPickingModelo3_8(Message message, String nomeArquivoPickingInterfaceLED) {
		
		try {
			
			//StringBuilder stringFinal = new StringBuilder();
			//File file = new File("/Users/lazaroJR/Documents/docsnds/ambiente2/parametros_nds/picking/teste.txt");

			FileWriter fileWriter = new FileWriter(message.getHeader().get(TipoParametroSistema.PATH_INTERFACE_PICKING_EXPORTACAO.name())	+ 
					File.separator  + nomeArquivoPickingInterfaceLED);
			
			PrintWriter print = new PrintWriter(fileWriter, true);
			
			Date dataLancamento = getDataLancDistrib(message);
			
			List<HeaderPickingDTO> listHeaders = getHeadePickingModulo3(dataLancamento);
			
			List<SubHeaderPickingDTO> listaSubHeadePickingModulo3 = getSubHeaderPickingModulo3(dataLancamento, false);
			
			if (listHeaders.isEmpty()) {

				String mensagemValidacao = "Nenhum registro encontrado!";
				
				this.lancarMensagemValidacao(mensagemValidacao, message);
			}
			
			int cont = 0;
			for (HeaderPickingDTO headerDTO : listHeaders) {
				
				EMS0129Picking3Header linha01Modelo03 = criarHeaderModelo3(headerDTO);
				
				print.println(fixedFormatManager.export(linha01Modelo03) + "\r");
				
				List<DetalhesPickingPorCotaModelo03DTO> listaLinha02Modelo03 = getLinha02Modelo03_8(linha01Modelo03.getCodigoCota(), dataLancamento);
				
				for (DetalhesPickingPorCotaModelo03DTO detalhesPickingPorCotaModelo03DTO : listaLinha02Modelo03) {
					EMS0129Picking3_8_Trailer2 linha02Modelo03 = criarLinha02Modelo03_8(detalhesPickingPorCotaModelo03DTO);
					print.println(fixedFormatManager.export(linha02Modelo03) + "\r");
				}
				
				SubHeaderPickingDTO subHeaderPickingDTO = listaSubHeadePickingModulo3.get(cont);
				
				EMS0129Picking3Trailer3 linha03Modelo03 = criarLinha03Modelo03(subHeaderPickingDTO);
				
				print.println(fixedFormatManager.export(linha03Modelo03) + "\r");
				
				cont++;
				
			}
			
			FooterPickingModelo3DTO footerPickingModulo3 = getFooterPickingModulo3(dataLancamento);
			
			EMS0129Picking3Footer linhaFooterModelo03 = criarLinhaFooterModelo03(footerPickingModulo3);
			
			print.println(fixedFormatManager.export(linhaFooterModelo03) + "\r");
			
			print.flush();
			print.close();

		} catch (IOException e) {

			String mensagemValidacao = "Erro ao gerar o arquivo. " + e.getMessage();
			
			this.lancarMensagemValidacao(mensagemValidacao, message);
		}
		
	}
	
	/**
	 * Gera o arquivo de acordo com o modelo de picking 3
	 * 
	 * @param message
	 * @param nomeArquivoPickingInterfaceLED
	 */
	private void gerarArquivoPickingModelo3(Message message, String nomeArquivoPickingInterfaceLED) {
		
		try {
			
			//StringBuilder stringFinal = new StringBuilder();
			//File file = new File("/Users/lazaroJR/Documents/docsnds/ambiente2/parametros_nds/picking/teste.txt");

			FileWriter fileWriter = new FileWriter(message.getHeader().get(TipoParametroSistema.PATH_INTERFACE_PICKING_EXPORTACAO.name())	+ 
					File.separator  + nomeArquivoPickingInterfaceLED);
			
			PrintWriter print = new PrintWriter(fileWriter, true);
			
			Date dataLancamento = getDataLancDistrib(message);
			
			List<HeaderPickingDTO> listHeaders = getHeadePickingModulo3(dataLancamento);
			
			List<SubHeaderPickingDTO> listaSubHeadePickingModulo3 = getSubHeaderPickingModulo3(dataLancamento, false);
			
			if (listHeaders.isEmpty()) {

				String mensagemValidacao = "Nenhum registro encontrado!";
				
				this.lancarMensagemValidacao(mensagemValidacao, message);
			}
			
			int cont = 0;
			for (HeaderPickingDTO headerDTO : listHeaders) {
				
				EMS0129Picking3Header linha01Modelo03 = criarHeaderModelo3(headerDTO);
				
				print.println(fixedFormatManager.export(linha01Modelo03) + "\r");
				
				List<DetalhesPickingPorCotaModelo03DTO> listaLinha02Modelo03 = getLinha02Modelo03(linha01Modelo03.getCodigoCota(), dataLancamento, false);
				
				for (DetalhesPickingPorCotaModelo03DTO detalhesPickingPorCotaModelo03DTO : listaLinha02Modelo03) {
					EMS0129Picking3Trailer2 linha02Modelo03 = criarLinha02Modelo03(detalhesPickingPorCotaModelo03DTO, false);
					print.println(fixedFormatManager.export(linha02Modelo03) + "\r");
				}
				
				SubHeaderPickingDTO subHeaderPickingDTO = listaSubHeadePickingModulo3.get(cont);
				
				EMS0129Picking3Trailer3 linha03Modelo03 = criarLinha03Modelo03(subHeaderPickingDTO);
				
				print.println(fixedFormatManager.export(linha03Modelo03) + "\r");
				
				cont++;
				
			}
			
			FooterPickingModelo3DTO footerPickingModulo3 = getFooterPickingModulo3(dataLancamento);
			
			EMS0129Picking3Footer linhaFooterModelo03 = criarLinhaFooterModelo03(footerPickingModulo3);
			
			print.println(fixedFormatManager.export(linhaFooterModelo03) + "\r");
			
			print.flush();
			print.close();

		} catch (IOException e) {

			String mensagemValidacao = "Erro ao gerar o arquivo. " + e.getMessage();
			
			this.lancarMensagemValidacao(mensagemValidacao, message);
		}
		
	}

	@SuppressWarnings("unchecked")
	private List<HeaderPickingDTO> getHeadePickingModulo3(Date dataLancamento) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select '1;' as identificadorLinha,  ");
		sql.append(" 		concat(lpad(a.numero_cota,4,0), ';') as numeroCota, ");
		sql.append("        a.numero_cota as codigoCota, ");
		sql.append("        concat(rpad((select bm.RAZAO_SOCIAL from distribuidor dt join pessoa bm ON dt.PJ_ID = bm.ID),40,' '), ';') as nomeDistribuidor, ");
		sql.append("        concat(DATE_FORMAT(l.data_lcto_distribuidor,'%d%m%Y'), ';' ) as dataLancamento, ");
		sql.append("        concat(rpad(cn.nome,30,' '), ';') as nomeCota, ");
		sql.append("        concat(lpad(bb.codigo,3,'0'), ';') as codigoBox, ");
		sql.append("        concat(k.FORMA_COMERCIALIZACAO, ';' ) as consignado, ");
		sql.append("        concat(rpad(coalesce(cn.cpf,0),11,0), ';' ) as cpf, ");
		sql.append("        concat(rpad(coalesce(cn.cnpj,0),14,0),';') as cnpj, ");
		sql.append("        concat(lpad(coalesce(trim(cn.INSC_ESTADUAL),0),20,0), ';') as inscricaoEstadual ");
		sql.append("     from cota a ");
		sql.append("     join estudo_cota i ");
		sql.append("       on i.COTA_ID = a.ID ");
		sql.append("     join estudo h ");
		sql.append("       on i.ESTUDO_ID = h.ID ");
		sql.append("     join produto_edicao j ");
		sql.append("       on h.PRODUTO_EDICAO_ID = j.ID ");
		sql.append("     join produto k ");
		sql.append("       on j.PRODUTO_ID = k.ID ");
		sql.append("     join lancamento l  ");
		sql.append("       on l.ESTUDO_ID = h.ID ");
		sql.append("     join pessoa cn ");
		sql.append("       on a.PESSOA_ID = cn.ID ");
		sql.append("     join box bb ");
		sql.append("       on a.BOX_ID = bb.ID ");
		
		sql.append(" where l.status in ('BALANCEADO', 'EXPEDIDO') ");
		sql.append(" and l.data_lcto_distribuidor = :dataLancamento  ");
		sql.append(" group by a.numero_cota ; ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("dataLancamento", dataLancamento);
		
		query.addScalar("identificadorLinha", StandardBasicTypes.STRING);
		query.addScalar("numeroCota", StandardBasicTypes.STRING);
		query.addScalar("codigoCota", StandardBasicTypes.INTEGER);
		query.addScalar("nomeDistribuidor", StandardBasicTypes.STRING);
		query.addScalar("dataLancamento", StandardBasicTypes.STRING);
		query.addScalar("nomeCota", StandardBasicTypes.STRING);
		query.addScalar("codigoBox", StandardBasicTypes.STRING );
		query.addScalar("consignado", StandardBasicTypes.STRING );
		query.addScalar("cpf", StandardBasicTypes.STRING );
		query.addScalar("cnpj", StandardBasicTypes.STRING );
		query.addScalar("inscricaoEstadual", StandardBasicTypes.STRING );
		
		query.setResultTransformer(new AliasToBeanResultTransformer(HeaderPickingDTO.class));
		
		return (List<HeaderPickingDTO>) query.list();
	}
	
	private EMS0129Picking3Header criarHeaderModelo3(HeaderPickingDTO headerDTO) {
		
		EMS0129Picking3Header outheader = new EMS0129Picking3Header();
		
		outheader.setCodigoCota(headerDTO.getNumeroCota());
		outheader.setNomeDistribuidor(headerDTO.getNomeDistribuidor());
		outheader.setNomeCota(headerDTO.getNomeCota());
		outheader.setCodigoBox(headerDTO.getCodigoBox());
		outheader.setCpf(headerDTO.getCpf());
		outheader.setCnpj(headerDTO.getCnpj());
		outheader.setInscricaoMunicipal(headerDTO.getInscricaoEstadual());
		outheader.setDataLancamento(headerDTO.getDataLancamento());
		outheader.setIdentificadorLinha(headerDTO.getIdentificadorLinha());
		outheader.setConsignado(headerDTO.getConsignado());
		
		return outheader;
	}
	
	@SuppressWarnings("unchecked")
	private List<DetalhesPickingPorCotaModelo03DTO> getLinha02Modelo03(String numeroCota, Date dataLancamento, boolean isModelo4) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select '2;' as identificadorLinha ");
		sql.append(" ,concat(lpad(a.numero_cota,4,0), ';') as codigoCota  ");
		sql.append(" ,concat(lpad(l.SEQUENCIA_MATRIZ,3,0), ';') as sequencia  ");
		sql.append(" ,concat(lpad(k.codigo,10,0), ';') as produto ");
		sql.append(" ,concat(lpad(j.numero_edicao,4,0), ';') as edicao ");
		sql.append(" ,concat(rpad(j.nome_comercial,20,' '), ';') as nome  ");
		
		if(isModelo4){
			sql.append(" ,concat(lpad(j.codigo_de_barras,20,0), ';') as codigoDeBarras ");
		}
		
		sql.append(" ,concat(replace(lpad(truncate(j.PRECO_VENDA,2),11,0),'.',''), ';') as preco ");
		sql.append(" ,concat(replace(lpad(truncate(j.PRECO_VENDA,2),11,0),'.',''), ';') as precoDesconto ");
		sql.append(" ,concat('02500', ';' ) as desconto ");
		sql.append(" ,concat(lpad(truncate(i.QTDE_efetiva,0),6,0), ';') as quantidade ");
		
		sql.append(" from cota a, estudo h, estudo_cota i, produto_edicao j, produto k, lancamento l ");
		sql.append(" where i.cota_id = a.id  ");
		sql.append(" and h.id = i.estudo_id ");
		sql.append(" and k.id = j.produto_id ");
		sql.append(" and h.id = l.estudo_id ");
		sql.append(" and l.PRODUTO_EDICAO_ID = j.id ");
		sql.append(" and l.status in ('BALANCEADO', 'EXPEDIDO') ");
		
		sql.append(" and l.data_lcto_distribuidor = :dataLancamento ");
		sql.append(" and a.numero_cota = :codigoCota ;");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("dataLancamento", dataLancamento);
		query.setParameter("codigoCota", numeroCota);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(DetalhesPickingPorCotaModelo03DTO.class));
		
		return (List<DetalhesPickingPorCotaModelo03DTO>) query.list();
		
		
	}
	
	
	@SuppressWarnings("unchecked")
	private List<DetalhesPickingPorCotaModelo03DTO> getLinha02Modelo03_8(String numeroCota, Date dataLancamento) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select '2;' as identificadorLinha ");
		sql.append(" ,concat(lpad(a.numero_cota,4,0), ';') as codigoCota  ");
		sql.append(" ,concat(lpad(l.SEQUENCIA_MATRIZ,3,0), ';') as sequencia  ");
		sql.append(" ,concat(lpad(k.codigo,8,0), ';') as produto ");
		sql.append(" ,concat(lpad(j.numero_edicao,4,0), ';') as edicao ");
		sql.append(" ,concat(rpad(j.nome_comercial,20,' '), ';') as nome  ");
		sql.append(" ,concat(replace(lpad(truncate(j.PRECO_VENDA,2),11,0),'.',''), ';') as preco ");
		sql.append(" ,concat(replace(lpad(truncate(j.PRECO_VENDA,2),11,0),'.',''), ';') as precoDesconto ");
		sql.append(" ,concat('02500', ';' ) as desconto ");
		sql.append(" ,concat(lpad(truncate(i.QTDE_efetiva,0),6,0), ';') as quantidade ");
		
		sql.append(" from cota a, estudo h, estudo_cota i, produto_edicao j, produto k, lancamento l ");
		sql.append(" where i.cota_id = a.id  ");
		sql.append(" and h.id = i.estudo_id ");
		sql.append(" and k.id = j.produto_id ");
		sql.append(" and h.id = l.estudo_id ");
		sql.append(" and l.PRODUTO_EDICAO_ID = j.id ");
		sql.append(" and l.status in ('BALANCEADO', 'EXPEDIDO') ");
		
		sql.append(" and l.data_lcto_distribuidor = :dataLancamento ");
		sql.append(" and a.numero_cota = :codigoCota ;");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("dataLancamento", dataLancamento);
		query.setParameter("codigoCota", numeroCota);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(DetalhesPickingPorCotaModelo03DTO.class));
		
		return (List<DetalhesPickingPorCotaModelo03DTO>) query.list();
		
		
	}
	
	private EMS0129Picking3Trailer2 criarLinha02Modelo03(DetalhesPickingPorCotaModelo03DTO detalhesPickingPorCotaModelo03DTO, boolean isModelo4) {
		
		if(isModelo4){
			return new EMS0129Picking3Trailer2(detalhesPickingPorCotaModelo03DTO.getIdentificadorLinha(), detalhesPickingPorCotaModelo03DTO.getCodigoCota(), 
				detalhesPickingPorCotaModelo03DTO.getSequencia(), detalhesPickingPorCotaModelo03DTO.getProduto(),
				detalhesPickingPorCotaModelo03DTO.getEdicao(), detalhesPickingPorCotaModelo03DTO.getNome(),
				detalhesPickingPorCotaModelo03DTO.getPreco(), detalhesPickingPorCotaModelo03DTO.getPrecoDesconto(),
				detalhesPickingPorCotaModelo03DTO.getDesconto(), detalhesPickingPorCotaModelo03DTO.getQuantidade(), detalhesPickingPorCotaModelo03DTO.getCodigoDeBarras());
		}else{
			return new EMS0129Picking3Trailer2(detalhesPickingPorCotaModelo03DTO.getIdentificadorLinha(), detalhesPickingPorCotaModelo03DTO.getCodigoCota(), 
				detalhesPickingPorCotaModelo03DTO.getSequencia(), detalhesPickingPorCotaModelo03DTO.getProduto(),
				detalhesPickingPorCotaModelo03DTO.getEdicao(), detalhesPickingPorCotaModelo03DTO.getNome(),
				detalhesPickingPorCotaModelo03DTO.getPreco(), detalhesPickingPorCotaModelo03DTO.getPrecoDesconto(),
				detalhesPickingPorCotaModelo03DTO.getDesconto(), detalhesPickingPorCotaModelo03DTO.getQuantidade());
		}
	}
	
	
	private EMS0129Picking3_8_Trailer2 criarLinha02Modelo03_8(DetalhesPickingPorCotaModelo03DTO detalhesPickingPorCotaModelo03DTO) {
		
		return new EMS0129Picking3_8_Trailer2(detalhesPickingPorCotaModelo03DTO.getIdentificadorLinha(), detalhesPickingPorCotaModelo03DTO.getCodigoCota(), 
				detalhesPickingPorCotaModelo03DTO.getSequencia(), detalhesPickingPorCotaModelo03DTO.getProduto(),
				detalhesPickingPorCotaModelo03DTO.getEdicao(), detalhesPickingPorCotaModelo03DTO.getNome(),
				detalhesPickingPorCotaModelo03DTO.getPreco(), detalhesPickingPorCotaModelo03DTO.getPrecoDesconto(),
				detalhesPickingPorCotaModelo03DTO.getDesconto(), detalhesPickingPorCotaModelo03DTO.getQuantidade());
	}
	
	@SuppressWarnings("unchecked")
	private List<SubHeaderPickingDTO> getSubHeaderPickingModulo3(Date dataLancamento, boolean isModelo4) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select '3;' as identificadorLinha ");
		sql.append(" ,concat(lpad(a.numero_cota,4,0), ';') as codigoCota ");
		sql.append(" ,concat(replace(lpad(truncate(sum(j.PRECO_CUSTO * i.QTDE_efetiva),2),11,0),'.',''), ';' ) as precoTotal ");
		sql.append(" ,concat(replace(lpad(truncate(sum(j.PRECO_VENDA * i.QTDE_efetiva),2),11,0),'.',''), ';' ) as precoTotalDesconto ");
		sql.append(" ,concat('0000000000', ';' ) as debito ");
		sql.append(" ,concat('0000000000', ';' ) as credito ");
		sql.append(" ,concat(DATE_FORMAT(l.data_lcto_distribuidor,'%d%m%Y'), ';' ) as dataLancamento ");
		
		if(isModelo4){
			sql.append(" ,concat(a.ENDERECO_LED, ';' ) as consignado ");
		}else{
			sql.append(" ,concat('0000000000', ';' ) as consignado ");
		}
		
		sql.append(" from cota a, estudo h, estudo_cota i, produto_edicao j, produto k, lancamento l  ");
		sql.append(" where i.cota_id = a.id  ");
		sql.append(" and h.id = i.estudo_id ");
		sql.append(" and k.id = j.produto_id ");
		sql.append(" and h.produto_edicao_id = j.id ");
		sql.append(" and l.PRODUTO_EDICAO_ID = j.id ");
		sql.append(" and l.status in ('BALANCEADO', 'EXPEDIDO') ");
		
		sql.append(" and l.data_lcto_distribuidor = :dataLancamento  ");
		sql.append(" group by a.numero_cota ; ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("dataLancamento", dataLancamento);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(SubHeaderPickingDTO.class));
		
		return (List<SubHeaderPickingDTO>) query.list();
	}
	
	private EMS0129Picking3Trailer3 criarLinha03Modelo03(SubHeaderPickingDTO subHeaderPickingDTO) {
		
		return new EMS0129Picking3Trailer3(subHeaderPickingDTO.getIdentificadorLinha(), subHeaderPickingDTO.getCodigoCota(), 
				subHeaderPickingDTO.getPrecoTotal(),
				subHeaderPickingDTO.getPrecoTotalDesconto(), subHeaderPickingDTO.getDebito(), subHeaderPickingDTO.getCredito(),
				subHeaderPickingDTO.getDataLancamento(), subHeaderPickingDTO.getConsignado());
	}
	
	private FooterPickingModelo3DTO getFooterPickingModulo3(Date dataLancamento) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select '9;' as identificadorLinha ");
		sql.append(" ,concat(lpad(truncate(count(distinct numero_cota),0),6,0), ';') as numeroTotalCotas ");
		sql.append(" ,concat(lpad(truncate(coalesce(sum(i.QTDE_efetiva),0),0),6,0), ';') as quantidadeEfetiva ");
		
		sql.append(" from cota a, estudo h, estudo_cota i, produto_edicao j, produto k, lancamento l ");
		sql.append(" where i.cota_id = a.id  ");
		sql.append(" and h.id = i.estudo_id ");
		sql.append(" and k.id = j.produto_id ");
		sql.append(" and h.produto_edicao_id = j.id ");
		sql.append(" and l.PRODUTO_EDICAO_ID = j.id ");
		sql.append(" and l.status in ('BALANCEADO', 'EXPEDIDO') ");
		
		sql.append(" and l.data_lcto_distribuidor = :dataLancamento  ;");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("dataLancamento", dataLancamento);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(FooterPickingModelo3DTO.class));
		
		return (FooterPickingModelo3DTO) query.uniqueResult();
	}
	
	private EMS0129Picking3Footer criarLinhaFooterModelo03(FooterPickingModelo3DTO footerPickingModulo3) {
		return new EMS0129Picking3Footer(footerPickingModulo3.getIdentificadorLinha(), footerPickingModulo3.getNumeroTotalCotas(), footerPickingModulo3.getQuantidadeEfetiva());
		
	}
}