function Distribuicao(tela) {
	
	var D = this;

    var modoTela = null;

    var idHistorico = "";


    this.inicializar = function() {
		
		D.verificarTipoConvencional();
		  
	},                        
    
	this.verificarTipoConvencional = function(idCota) {
		
        var param = [{name: 'idCota', value: idCota}];

		$.postJSON(contextPath + "/cadastro/cota/verificarTipoConvencional",
				param,
				function(result) {

					if (result){
						
						if (result.boolean){
							
							D.$('recebeComplementar').removeAttr('disabled');
						} else {
							
							D.$('recebeComplementar').attr('disabled', true);
						}
					}
				},
				
				null, true);
		
	},
	
    /**
     * Define o modo da tela de distribuição conforme os
     * valores ModoTela.CADASTRO_COTA ou ModoTela.HISTORICO_TITULARIDADE
     *
     */
    this.definirModoTela = function(modoTela, idHistorico) {
        D.modoTela = modoTela;
        D.idHistorico = idHistorico;
    },

    /**
     * Verifica se a tela esta em modo de cadastro de cota
     */
    this.isModoTelaCadastroCota = function() {
        return ModoTela.CADASTRO_COTA == D.modoTela;
    },
	
	/**
	 * Envia dados da tela para serem salvos no controller
	 * 
	 * @return false - define que a não sairá da tela, responsabilidade deixada para o método de callback
	 */
	this.salvar = function(callback) {
				
		$.postJSON(contextPath + "/cadastro/cota/salvarDistribuicaoCota",
				D.getDados(),
				callback,
				null,
				true,
				"dialog-cota");
		
		if(MANTER_COTA && MANTER_COTA._indCadastroCotaAlterado) {
    		MANTER_COTA._indCadastroCotaAlterado = false;
    	}
		
		return false;		
	},
	
	/**
	 * Retorna todos os dados da tela no padrão utilizado pelo VRaptor
	 * @return Espelho de DistribuicaoDTO (br.com.abril.nds.dto) 
	 */
	this.getDados = function() {
	
		var data = [];
		
		data.push({name:'distribuicao.numCota',					            value: D.get("numCota")});
		data.push({name:'distribuicao.qtdePDV',					            value: D.get("qtdePDV")});
		data.push({name:'distribuicao.box',						            value: D.get("box")});
		data.push({name:'distribuicao.assistComercial',			            value: D.get("assistComercial")});
		data.push({name:'distribuicao.gerenteComercial',    	            value: D.get("gerenteComercial")});
		data.push({name:'distribuicao.descricaoTipoEntrega',	            value: D.get("tipoEntrega")});
		data.push({name:'distribuicao.observacao',				            value: D.get("observacao")});
		data.push({name:'distribuicao.arrendatario',			            value: D.get("arrendatario")});
		data.push({name:'distribuicao.repPorPontoVenda',		            value: D.get("repPorPontoVenda")});
		data.push({name:'distribuicao.solNumAtras',				            value: D.get("solNumAtras")});
		data.push({name:'distribuicao.recebeRecolhe',			            value: D.get("recebeRecolhe")});
		data.push({name:'distribuicao.recebeComplementar',		            value: D.get("recebeComplementar")});
		data.push({name:'distribuicao.enderecoLED',		           			value: D.get("enderecoLED")});
		data.push({name:'distribuicao.neImpresso',				            value: D.get("neImpresso")});
		data.push({name:'distribuicao.neEmail',					            value: D.get("neEmail")});
		data.push({name:'distribuicao.ceImpresso',				            value: D.get("ceImpresso")});
		data.push({name:'distribuicao.ceEmail',					            value: D.get("ceEmail")});
		data.push({name:'distribuicao.slipImpresso',			            value: D.get("slipImpresso")});
		data.push({name:'distribuicao.slipEmail',				            value: D.get("slipEmail")});
		data.push({name:'distribuicao.boletoImpresso',			            value: D.get("boletoImpresso")});
		data.push({name:'distribuicao.boletoEmail',				            value: D.get("boletoEmail")});
		data.push({name:'distribuicao.boletoSlipImpresso',		            value: D.get("boletoSlipImpresso")});
		data.push({name:'distribuicao.boletoSlipEmail',			            value: D.get("boletoSlipEmail")});
		data.push({name:'distribuicao.reciboImpresso',			            value: D.get("reciboImpresso")});
		data.push({name:'distribuicao.reciboEmail',				            value: D.get("reciboEmail")});
		data.push({name:'distribuicao.utilizaParametrosDocsDistribuidor',	value: D.get("cotaUtilizaParametrosDistrib")});
		
		var tipoEntrega = D.get('tipoEntrega');
		
		if (tipoEntrega == 'ENTREGA_EM_BANCA') {
			
			data.push({name:'distribuicao.utilizaTermoAdesao',		value: D.get("utilizaTermoAdesao")});
			data.push({name:'distribuicao.termoAdesaoRecebido',		value: D.get("termoAdesaoRecebido")});
							
		} else if (tipoEntrega == 'ENTREGADOR') {
			
			data.push({name:'distribuicao.utilizaProcuracao',		value: D.get("utilizaProcuracao")});
			data.push({name:'distribuicao.procuracaoRecebida',		value: D.get("procuracaoRecebida")});
		}
		
		if (tipoEntrega == 'ENTREGADOR' || tipoEntrega == 'ENTREGA_EM_BANCA'){
			
			data.push({name:'distribuicao.baseCalculo',	            value: D.get("baseCalculo")});
			
			if (D.get("modalidadeCobranca") == "TAXA_FIXA"){

				data.push({name:'distribuicao.taxaFixa', value: D.get("valorTaxaFixa")});
				
			} else {

				data.push({name:'distribuicao.percentualFaturamento',	value: D.get("valorPercentualFaturamento")});
			}
			
			data.push({name:'distribuicao.modalidadeCobranca',	    value: D.get("modalidadeCobranca")});
			data.push({name:'distribuicao.porEntrega',	            value: D.get("checkPorEntrega")});
			data.push({name:'distribuicao.inicioPeriodoCarencia',	value: D.get("inicioPeriodoCarencia")});
			data.push({name:'distribuicao.fimPeriodoCarencia',		value: D.get("fimPeriodoCarencia")});
			
			var diaCobranca = '';
			
			var periodicidade = $("[name="+tela+"radioPeriodicidade]:checked", this.workspace).val();
			
			var diaSemanaCobranca = $("[name="+tela+"diaSemanaCob]:checked", this.workspace).val();
			
			if (periodicidade == "QUINZENAL"){
				
				diaCobranca = D.get("inputQuinzenalDiaInicio");
				
			} else if (periodicidade == "MENSAL"){
				
				diaCobranca = D.get("inputCobrancaMensal");
			}
			
			data.push({name:'distribuicao.periodicidadeCobranca',	value: periodicidade});
			data.push({name:'distribuicao.diaCobranca',	            value: diaCobranca});
			data.push({name:'distribuicao.diaSemanaCobranca', 		value:diaSemanaCobranca});
		}

		return data;
	},
	
	/**
	 * Preenche a tela
	 * 
	 * @param dto - objeto Espelho de DistribuicaoDTO esperado (br.com.abril.nds.dto) 
	 */
	this.setDados = function(dto) {
				
		if(dto.tiposEntrega)
			D.montarComboTipoEntrega(dto.tiposEntrega);
		
		if(dto.basesCalculo)
			D.montarComboBaseCalculo(dto.basesCalculo);
		
		$("input[name='numCotaUpload']").val(dto.numCota);
		
		D.set('numCota',				dto.numCota);
		D.set('qtdePDV',				dto.qtdePDV ? dto.qtdePDV.toString() : '' );
		D.set('box',					dto.box);
		D.set('assistComercial',		dto.assistComercial);
		D.set('gerenteComercial',		dto.gerenteComercial);
		D.set('tipoEntrega',			dto.descricaoTipoEntrega);
		D.set('tipoEntregaHidden',		dto.descricaoTipoEntrega);
		D.set('arrendatario',			dto.arrendatario);
		D.set('observacao',				dto.observacao);
		D.set('repPorPontoVenda',	    dto.repPorPontoVenda);
		D.set('solNumAtras',			dto.solNumAtras);
		D.set('recebeRecolhe',			dto.recebeRecolhe);
		
		if(dto.enderecoLED != undefined && dto.enderecoLED > 0){
			var str = "" + dto.enderecoLED
			var pad = "0000"
			var ans = pad.substring(0, pad.length - str.length) + str
			
			D.set('enderecoLED',		ans);
		}else{
			D.set('enderecoLED',		'');
		}
		
		
		if(dto.tipoDistribuicaoCota=='CONVENCIONAL'){
			D.set('recebeComplementar',	dto.recebeComplementar);			
		}else {
			D.set('recebeComplementar',	false);
		}
		
		D.set('neImpresso',				        dto.neImpresso);
		D.set('neEmail',				        dto.neEmail);
		D.set('ceImpresso',				        dto.ceImpresso);
		D.set('ceEmail',				        dto.ceEmail);
		D.set('slipImpresso',			        dto.slipImpresso);
		D.set('slipEmail',				        dto.slipEmail);
		D.set('boletoImpresso',			        dto.boletoImpresso);
		D.set('boletoEmail',			        dto.boletoEmail);
		D.set('boletoSlipImpresso',		        dto.boletoSlipImpresso);
		D.set('boletoSlipEmail',		        dto.boletoSlipEmail);
		D.set('reciboImpresso',			        dto.reciboImpresso);
		D.set('reciboEmail',			        dto.reciboEmail);
		D.set('cotaUtilizaParametrosDistrib',	dto.utilizaParametrosDocsDistribuidor);
		
		var tipoEntrega = D.get('tipoEntrega');
		
		if (tipoEntrega == 'ENTREGA_EM_BANCA') {
			
			D.set('utilizaTermoAdesao',					dto.utilizaTermoAdesao);
			D.set('termoAdesaoRecebido',				dto.termoAdesaoRecebido);
			
			D.setNomeTermoAdesao(dto.nomeTermoAdesao);
			
	
		} else if (tipoEntrega == 'ENTREGADOR') {
		
			D.set('utilizaProcuracao',dto.utilizaProcuracao);
			
			D.setNomeProcuracao(dto.nomeProcuracao);
		}
		
		if(tipoEntrega == 'ENTREGADOR' || tipoEntrega == 'ENTREGA_EM_BANCA'){
			
			D.set("modalidadeCobranca",dto.modalidadeCobranca);
			
			D.set('baseCalculo',dto.baseCalculo);
			
			if (dto.modalidadeCobranca == 'TAXA_FIXA') {
				$(".transpTaxaFixa", this.workspace).show();
				$(".transpPercentual", this.workspace).hide();
				D.set("valorTaxaFixa",dto.taxaFixa ? floatToPrice(dto.taxaFixa) : '');
			}
			if (dto.modalidadeCobranca == 'PERCENTUAL') {
				$(".transpTaxaFixa", this.workspace).hide();
				$(".transpPercentual", this.workspace).show();
				D.set("valorPercentualFaturamento", dto.percentualFaturamento ? floatToPrice(dto.percentualFaturamento) : '');
			}
			
			D.set("checkPorEntrega",dto.porEntrega);
			
			D.set('inicioPeriodoCarencia',	dto.inicioPeriodoCarencia);
			D.set('fimPeriodoCarencia',		dto.fimPeriodoCarencia);
			
			D.alterarPeriodicidadeCobranca(dto.periodicidadeCobranca);
			
			if (dto.periodicidadeCobranca == "QUINZENAL"){
				
				D.set("inputQuinzenalDiaInicio",dto.diaCobranca);
				
				D.calcularDiaFimCobQuinzenal();
				
			} else if (dto.periodicidadeCobranca == "MENSAL"){
				
				D.set("inputCobrancaMensal",dto.diaCobranca);
				
			} else if (dto.periodicidadeCobranca == "SEMANAL"){
				
				$("input:radio[value="+ dto.diaSemanaCobranca +"]", this.workspace).check();
			}
		}
		
		D.carregarConteudoTipoEntrega(tipoEntrega, false, false);
		
		if(D.isModoTelaCadastroCota()){
             if (dto.qtdeAutomatica) {
		    	D.$('qtdePDV').attr('disabled', true);
		    } else {
			    D.$('qtdePDV').removeAttr('disabled');
	    	}
        } else {
            D.$('qtdePDV', this.workspace).prop('disabled', true);
        }
		
		D.$('numCota').attr('disabled', true);
		D.$('box').attr('disabled', true);
	},
	
	/**
	 * Limpa campos da tela
	 */
	this.limparDados = function() {
		
		D.setDados(null);
	},
	
	/**
	 * Busca no banco os dados de Distribuição da Cota e preenche a tela
	 * @param idCota - Código da cota
	 */
	this.carregarDadosDistribuicaoCota = function(idCota) {
        var param = [{name: 'idCota', value: idCota},
                     {name: 'modoTela', value:D.modoTela.value},
                     {name: 'idHistorico', value:D.idHistorico}];

		$.postJSON(contextPath + "/cadastro/cota/carregarDistribuicaoCota",
				param,
				function(result) {
					D.setDados(result);
				},
				null, true);
	},
	
	/**
	 * Gera combo de de Tipo de Entrega
	 * @param itensDTO - objeto espelho de ItemDTO (br.com.abril.nds.dto)
	 */
	this.montarComboTipoEntrega = function(itensDTO) {
		
		itensDTO.splice(0,0,{"key": {"@class": "string","$": ""},"value": {"@class": "string","$": ""}});
		
		var combo =  montarComboBox(itensDTO, false);
		
		D.$("tipoEntrega").html(combo);
		D.$("tipoEntrega").sortOptions();
	},
	
	/**
	 * Gera combo de de Base de Calculo
	 * @param itensDTO - objeto espelho de ItemDTO (br.com.abril.nds.dto)
	 */
	this.montarComboBaseCalculo = function(itensDTO) {
		
		itensDTO.splice(0,0,{"key": {"@class": "string","$": ""},"value": {"@class": "string","$": ""}});
		
		var combo =  montarComboBox(itensDTO, false);
		
		D.$("baseCalculo").html(combo);
		D.$("baseCalculo").sortOptions();
	},
	
	this.submitForm = function(idForm) {
		
		$('#' + idForm).submit();
	},
		
	this.downloadTermoAdesao = function() {
		
		var valorTaxa = floatValue(D.get("valorTaxaFixa"));

		var params = {taxa:valorTaxa, percentual:D.get("valorPercentualFaturamento")};
		
		$.postJSON(contextPath + "/cadastro/cota/validarValoresParaDownload",
				params,
				function() {
					document.location.assign(contextPath + "/cadastro/cota/downloadTermoAdesao?termoAdesaoRecebido="+D.get("termoAdesaoRecebido")+"&numeroCota="+D.get("numCota")+"&taxa="+valorTaxa+"&percentual="+D.get("valorPercentualFaturamento"));
				},
				null,
				true,
				"dialog-cota");
	},
	
	this.downloadProcuracao = function() {
		
		document.location.assign(contextPath + "/cadastro/cota/downloadProcuracao?procuracaoRecebida="+D.get("procuracaoRecebida")+"&numeroCota="+D.get("numCota"));
	},
	
	this.tratarRetornoUpload = function(data) {
		
		data = replaceAll(data, "<pre>", "");
		data = replaceAll(data, "</pre>", "");
		
		data = replaceAll(data, "<PRE>", "");
		data = replaceAll(data, "</PRE>", "");
		
		var responseJson = jQuery.parseJSON(data);
		
		if (responseJson.mensagens) {

			exibirMensagemDialog(
				responseJson.mensagens.tipoMensagem, 
				responseJson.mensagens.listaMensagens, "dialog-cota"
			);
		}
			
		var fileName = responseJson.result;
		
		var tipoEntrega = D.get('tipoEntrega');
		
		if (tipoEntrega == 'ENTREGA_EM_BANCA') {

			D.setNomeTermoAdesao(fileName);
			
		} else if (tipoEntrega == 'ENTREGADOR') {
			
			D.setNomeProcuracao(fileName);
		}
	},
	
	/**
	 * Atribui valor a um campo da tela
	 * Obs: Checkboxs devem ser atribuidos com o valor de true ou false
	 * 
	 * @param campo - Campo a ser alterado
	 * @param value - valor
	 */
	this.set = function(campo,value) {
				
		var elemento = D.$(campo);
		
		if(elemento.attr('type') == 'checkbox') {
			
			if(value) {
				elemento.attr('checked','checked');
			} else {
				elemento.removeAttr('checked');
			}
						
		} else {
			elemento.val(value);
		}
	},
	
	/**
	 * Obtém valor de elemento da tela
	 * @param campo - de onde o valor será obtido
	 */
	this.get = function(campo) {
		
		var elemento = D.$(campo);
		
		if(elemento.attr('type') == 'checkbox') {
			return (elemento.attr('checked') == 'checked') ;
		} else {
			return elemento.val();
		}
		
	},
	
	/**
	 * Obtém elemento da tela
	 */
	this.$ = function(campo) {
		
		 return $("#" + tela + campo);
	},
	
	this.mostrarOpcaoSelecionada = function() {

		if (D.get("modalidadeCobranca") == "TAXA_FIXA") {
			$(".transpTaxaFixa", this.workspace).show();
			$(".transpPercentual", this.workspace).hide();
		} else {
			$(".transpTaxaFixa", this.workspace).hide();
			$(".transpPercentual", this.workspace).show();
		}
		
		D.limparCamposCobranca();
	},
	
	this.mostrarOpcaoTaxaFixa = function(){
		
		$(".transpTaxaFixa", this.workspace).show();
		$(".transpPercentual", this.workspace).hide();
		
		D.limparCamposCobranca();
	}, 
	
	this.mostrarOpcaoPercentual = function(){
		
		$(".transpTaxaFixa", this.workspace).hide();
		$(".transpPercentual", this.workspace).show();
		
		D.limparCamposCobranca();
	},
	
	this.limparCamposCobranca = function(){
		
		D.set('valorPercentualFaturamento',	"");
		D.set('valorTaxaFixa',"");
		D.set('baseCalculo','');
	},
	
	this.alterarPeriodicidadeCobranca = function(tipoCobranca){
		
		$(".perCobrancaSemanal",this.workspace).hide();
		$(".perCobrancaQuinzenal", this.workspace).hide();
		$(".perCobrancaMensal", this.workspace).hide();
		
		if (tipoCobranca == 'SEMANAL'){
			
			$(".perCobrancaSemanal", this.workspace).show();
		} else if (tipoCobranca == 'QUINZENAL'){
			
			$(".perCobrancaQuinzenal", this.workspace).show();
		} else if (tipoCobranca == 'MENSAL'){
			
			$(".perCobrancaMensal", this.workspace).show();
		}
		
		$("input:radio[value="+ tipoCobranca +"]", this.workspace).check();
		
		$("input[id$=inputQuinzenalDiaFim", this.workspace).attr("disabled","disabled");
		
		D.set("inputQuinzenalDiaInicio",'');
		D.set("inputQuinzenalDiaFim",'');
		D.set("inputCobrancaMensal",'');
		D.set("diaSemanaCob",'');
	},
	
	this.calcularDiaFimCobQuinzenal = function(){
		
		var valorInput = parseInt(D.get("inputQuinzenalDiaInicio"));
		
		if (!valorInput || valorInput <= 0){
			D.set("inputQuinzenalDiaFim","");
		} else {
			var diaFim = valorInput + 14 < 31 ? valorInput + 14 : 31;
			D.set("inputQuinzenalDiaFim",diaFim);
		}
	},
	
	this.mostrarEsconderDiv = function(classDiv, exibir) {
		
		$("." + classDiv).toggle(exibir);
	};
	
	this.mostarPopUpAteracaoTipoEntrega = function(value) {
		
		var param = [{name:"numeroCota",value:D.get('numCota')},
		             {name:"tipoEntrega",value:value}];
		
		$.postJSON(contextPath + "/cadastro/cota/validarTipoEntrega",
				param,function (result) {
			
			var tipoEntregaHidden = D.get('tipoEntregaHidden');
			
			if (tipoEntregaHidden == "" || tipoEntregaHidden == 'COTA_RETIRA') {
				
				D.set('tipoEntregaHidden', value);
				
				D.carregarConteudoTipoEntrega(value, true, true);
			}
			else{
				
				D.exibirModalConfirmacao(value);
			}

		},function(result){
			
			D.set("tipoEntrega",  D.get('tipoEntregaHidden'));
			
		},true);
	};
	
	this.exibirModalConfirmacao = function(value){
		
		$("#dialogMudancaTipoEntrega").dialog({
			resizable: false,
			height:'auto',
			width:600,
			modal: true,
			buttons: [
			    {
			    	id: "mudancaTipoEntregaBtnConfirmar",
			    	text: "Confirmar",
			    	click: function() {
					
			    		D.carregarConteudoTipoEntrega(value, true, true);
						
						D.set('tipoEntregaHidden', value);
						
						$(this).dialog("close");
			    	}
			    },
			    {
			    	id: "mudancaTipoEntregaBtnCancelar",
			    	text: "Cancelar",
			    	click: function() {
			    
			    		D.set("tipoEntrega", D.get("tipoEntregaHidden"));
						
						$(this).dialog("close");
			    	}
				}
			]
		});
	},
	
	this.carregarConteudoTipoEntrega = function(value, limparCampos, carregarValores) {
		
		if (value == "COTA_RETIRA") {
			
			D.mostrarEsconderConteudoEntregaBanca(false, limparCampos);
			
			D.mostrarEsconderConteudoEntregador(false, limparCampos);
			
		} else if (value == "ENTREGA_EM_BANCA") {
			
			D.mostrarEsconderConteudoEntregador(false, limparCampos);
			
			D.mostrarEsconderConteudoEntregaBanca(true, limparCampos);
			
		} else if (value == "ENTREGADOR") {
			
			D.mostrarEsconderConteudoEntregaBanca(false, limparCampos);
			
			D.mostrarEsconderConteudoEntregador(true, limparCampos);
			
		} else {
			
			D.mostrarEsconderConteudoEntregaBanca(false, limparCampos);
			
			D.mostrarEsconderConteudoEntregador(false, limparCampos);
		}
	};
	
    this.distribuidorUtilizaTermoAdesao = function() {
		
		$.postJSON(contextPath + "/cadastro/cota/distribuidorUtilizaTermoAdesao",
			null,
			function (result) {
			
				if (result.boolean){
					
					$(".divImpressaoTermoAdesao").show();
					
					if(D.get("utilizaTermoAdesao")==true){
					  $(".divUtilizaTermoAdesao").show();
					  $(".divTermoAdesaoRecebido").show();
					}else{
					  $(".divUtilizaTermoAdesao").hide();
					  $(".divTermoAdesaoRecebido").hide();
					}
					  
					
				}else{
				
				    $(".divImpressaoTermoAdesao").hide();
				    $(".divUtilizaTermoAdesao").hide();
					$(".divTermoAdesaoRecebido").hide();
				}
			},
			null,
			true);
	};
	
    this.distribuidorUtilizaProcuracao = function() {
		
		$.postJSON(contextPath + "/cadastro/cota/distribuidorUtilizaProcuracao",
			null,
			function (result) {
			
				if (result.boolean){
					
					$("#cotaTemEntregador").show();
				}
				else{
				
				    $("#cotaTemEntregador").hide();
				}
			},
			null,
			true);
	};
	
	this.mostrarEsconderConteudoEntregaBanca = function(exibirDiv, limparCampos) {
		
		D.mostrarEsconderConteudoTipoEntrega(exibirDiv, "divConteudoEntregaBanca",
											 "divUtilizaTermoAdesao", "divTermoAdesaoRecebido",
											 "utilizaTermoAdesao",null,null, limparCampos);
		
		D.distribuidorUtilizaTermoAdesao();
		
		D.mostrarEsconderConteudoTipoEntrega(false,
				 "divUtilizaTermoAdesao", "divTermoAdesaoRecebido",
				 "utilizaTermoAdesao",null,null, limparCampos);
		
		D.mostrarEsconderDiv("dadosComuns", exibirDiv);
		
	};
	
	this.mostrarEsconderConteudoEntregador = function(exibirDiv, limparCampos) {
		
		D.mostrarEsconderConteudoTipoEntrega(exibirDiv, "divConteudoEntregador",
											 "divUtilizaProcuracao", "divProcuracaoRecebida",
											 "utilizaProcuracao", "procuracaoRecebida", limparCampos);
		
		D.mostrarEsconderDiv("dadosComuns", exibirDiv);
		
		D.distribuidorUtilizaProcuracao();
	};

	this.mostrarEsconderConteudoTipoEntrega = function(exibirDiv, divConteudoTipoEntrega,
													   divUtilizaArquivo, divArquivoRecebido,
													   campoUtilizaArquivo, campoArquivoRecebido, limparCampos) {
		
		D.mostrarEsconderDiv(divConteudoTipoEntrega, exibirDiv);
		
		if (!exibirDiv && limparCampos) {
			
			D.set(campoUtilizaArquivo, false);
			
			D.limparCampos();
		}
		
		D.mostrarEsconderDivUtilizaArquivo(divUtilizaArquivo, divArquivoRecebido,
										   campoUtilizaArquivo, campoArquivoRecebido);
	};
	
	this.mostrarEsconderDivUtilizaArquivo = function(divUtilizaArquivo, divArquivoRecebido,
													 campoUtilizaArquivo, campoArquivoRecebido) {
		
		var exibirDiv = D.get(campoUtilizaArquivo);
		
		if (D.isModoTelaCadastroCota()) {
            D.mostrarEsconderDiv(divUtilizaArquivo, exibirDiv);
        }

		if (!exibirDiv) {
			
			D.set(campoArquivoRecebido,	false);
		}
		
		D.mostrarEsconderDivArquivoRecebido(divArquivoRecebido, campoArquivoRecebido);
	};
	
	this.mostrarEsconderDivArquivoRecebido = function(divArquivoRecebido, campoArquivoRecebido) {
		
		var exibirDiv = D.get(campoArquivoRecebido);
		
		D.mostrarEsconderDiv(divArquivoRecebido, exibirDiv);
	};
	
	this.uncheckUtilizaParametrosDistrib = function(){
		$(".isCotaUtilizaParametrosDistrib", this.workspace).prop( "checked", false );
	}
	
	this.limparCampos = function() {
		
		var tipoEntrega = D.get('tipoEntregaHidden');
		
		if (tipoEntrega == "ENTREGA_EM_BANCA") {
			
			$("#nomeArquivoTermoAdesao").html("");
			
			$.postJSON(contextPath + "/cadastro/cota/excluirTermoAdesao",
					{numCota:D.get("numCota")} ,
					null,
					null,
					true);
			
		} else if (tipoEntrega == "ENTREGADOR") {
			
			$("#nomeArquivoProcuracao").html("");
			
			$.postJSON(contextPath + "/cadastro/cota/excluirProcuracao",
					{numCota:D.get("numCota")},
					null,
					null,
					true);
		}
		
		D.set('valorPercentualFaturamento',	"");
		D.set('valorTaxaFixa',				"");
		D.set('inicioPeriodoCarencia',	"");
		D.set('fimPeriodoCarencia',		"");
		D.set("inputQuinzenalDiaInicio",'');
		D.set("inputCobrancaMensal",'');
		D.set("diaSemanaCob",'');
		D.set("checkPorEntrega",false);
		D.alterarPeriodicidadeCobranca("DIARIO");
		D.set('baseCalculo','');
		
	};
	
	this.setNomeTermoAdesao = function(nomeTermoAdesao) {
		
		$("#nomeArquivoTermoAdesao").html(nomeTermoAdesao);
	};
	
	this.setNomeProcuracao = function(nomeProcuracao) {
		
		$("#nomeArquivoProcuracao").html(nomeProcuracao);
	};
	
	$(function() {
		D.$("numCota").numeric();
		D.$("qtdePDV").numeric();
		
		$("input[name='inicioPeriodoCarencia']").datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true,
			defaultDate: new Date()
		});
		
		$("input[name='inicioPeriodoCarencia']").mask("99/99/9999");
		
		$("input[name='fimPeriodoCarencia']").datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true,
			defaultDate: new Date()
		});
		
		$("input[name='fimPeriodoCarencia']").mask("99/99/9999");
		
		var options = {
			success: D.tratarRetornoUpload
	    };
		
		$('#formUploadTermoAdesao').ajaxForm(options);
		
		$('#formUploadProcuracao').ajaxForm(options);
		
		$("input[id$=valorTaxaFixa", this.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});
		$("input[id$=valorPercentualFaturamento", this.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});	
		
	
		$("input[id$=inputQuinzenalDiaInicio", this.workspace).numeric();
		
		$("input[id$=inputCobrancaMensal",this.workspace).numeric();
		
		
	});
}

//@ sourceURL=distribuicao.js