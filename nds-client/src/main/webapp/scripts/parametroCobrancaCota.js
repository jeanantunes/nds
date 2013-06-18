var parametroCobrancaCotaController = $.extend(true, {

    idCota: "",
    idHistorico: "",
    idFormaPagto: "",
    resultFormaCobranca: [],
    modoTela: null,

    init : function () {


		//GRID DE FORMAS DE COBRANÇA
		$(".boletosUnificadosGrid", this.workspace).flexigrid({
			preProcess: parametroCobrancaCotaController.getDataFromResult,
			dataType : 'json',
			colModel : [  {
				display : 'Fornecedores',
				name : 'fornecedor',
				width : 170,
				sortable : true,
				align : 'left'
			},{
				display : 'Concentração de Pagamento',
				name : 'concentracaoPagto',
				width : 220,
				sortable : true,
				align : 'left'
			}, {
				display : 'Tipo de Pagamento',
				name : 'tipoPagto',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Detalhes - Tipo de Pagamento',
				name : 'detalhesTipoPagto',
				width : 250,
				sortable : true,
				align : 'left'
			}, {
				display : 'Parâmetro Distribuidora',
				name : 'parametroDistribuidor',
				width : 120,
				sortable : true,
				align : 'left'
			},{
				display : 'Ação',
				name : 'acao',
				width : 50,
				sortable : false,
				align : 'center'
			}],
			sortname : "Fornecedores",
			sortorder : "asc",
			showTableToggleBtn : true,
			width : 870,
			height : 150
		});

		$("#fatorVencimento", this.workspace).numeric();
		$("#valorMinimo", this.workspace).priceFormat({
			centsSeparator: ',',
		    thousandsSeparator: '.',
		    centsLimit:2	
		});
		$("#qtdDividasAberto", this.workspace).numeric();
		$("#vrDividasAberto", this.workspace).numeric();
		
		$("#numBanco", this.workspace).numeric();
		$("#nomeBanco", this.workspace).numeric();
		$("#agencia", this.workspace).numeric();
		$("#agenciaDigito", this.workspace).numeric();
		$("#conta", this.workspace).numeric();
	    $("#contaDigito", this.workspace).numeric();
	    $("#diaDoMesCota", this.workspace).numeric();
	    $(".dataInputMask", this.workspace).mask("99/99/9999");
	    $("#parametroCobrancaDateInicio", this.workspace).val(formatDateToString(new Date()));
	    $("#primeiroDiaQuinzenalParametroCobrancaCota", this.workspace).numeric();
	    $("#segundoDiaQuinzenalParametroCobrancaCota", this.workspace).numeric();
	},

    definirModoTela : function(modoTela, idHistorico) {
        parametroCobrancaCotaController.modoTela = modoTela;
        parametroCobrancaCotaController.idHistorico = idHistorico;
        if (parametroCobrancaCotaController.isReadOnly()) {
            $('#FINANCEIRObtnNovaFormaPagamento', this.workspace).hide();
            $('#popupNovaFormaPagamentoIncluirNova', this.workspace).hide();
            $("#dialog-unificacao", this.workspace).find(':input:not(:disabled)').prop('disabled', true);
            $("#botaoContrato", this.workspace).hide();
        } else {
            $('#FINANCEIRObtnNovaFormaPagamento', this.workspace).show();
            $('#popupNovaFormaPagamentoIncluirNova', this.workspace).show();
            $("#dialog-unificacao", this.workspace).find(':input(:disabled)').prop('disabled', false);
            $("#botaoContrato", this.workspace).show();
        }
    },

    carregarComboTipoCobranca : function(selected){

    	carregarCombo(contextPath + "/cota/parametroCobrancaCota/obterTiposCobranca", null,
                $("#tipoCobrancaParametroCobrancaCota", this.workspace), selected, null);
    },

    isModoTelaCadastroCota : function() {
        return parametroCobrancaCotaController.modoTela == ModoTela.CADASTRO_COTA;
    },

    isReadOnly : function() {
        return !parametroCobrancaCotaController.isModoTelaCadastroCota();
    },

	//PRÉ CARREGAMENTO DA PAGINA
	carregaFinanceiro : function(idCota){
        parametroCobrancaCotaController.idCota = idCota;
		$("#_idParametroCobranca", this.workspace).val("");
		$("#_idCota", this.workspace).val("");
		$("#_numCota", this.workspace).val("");
		parametroCobrancaCotaController.obterParametroCobranca(idCota);
		parametroCobrancaCotaController.mostrarGrid(idCota);	
		
		var _this = this;
	    
	    var options = {
				success: _this.tratarRespostaUploadAnexoContrato
		};
	    
	    $('#parametroCobrancaFormUpload', this.workspace).ajaxForm(options);
	    
	    $(".dataInputMask").mask("99/99/9999");
	    
	    $("#parametroCobrancaDateInicio", this.workspace).val(formatDateToString(new Date()));
	    
	    this.calcularDataTermino();
	    
	    this.carregarArquivoContrato();
	    
	    parametroCobrancaCotaController.carregarComboTipoCobranca("");   
	},
	
	montarTrRadioBox : function(result,name,nameItemIdent) {

		var options = "";
		
		$.each(result, function(index, row) {

			options += "<tr> <td width='23'>";
			options += "<input id='" + nameItemIdent + row.key.$ +"' value='" + row.key.$ + "' name='"+name+"' type='checkbox' />";
			options += "</td> <td width='138'>";	
			options += "<label for='" + nameItemIdent + row.key.$ +"' >"+ row.value.$ +"</label>";
		    options += "</td>";
		    options += "</tr>";   

		});
		
		return options;
	},
	
	carregarFornecedoresRelacionados : function(){
		var data = [{name: 'idCota', value: parametroCobrancaCotaController.idCota},
            {name: 'modoTela', value: parametroCobrancaCotaController.modoTela.value},
            {name: 'idFormaPagto', value: parametroCobrancaCotaController.idFormaPagto}];
		$.postJSON(contextPath + "/cota/parametroCobrancaCota/fornecedoresCota",
				   data,
				   parametroCobrancaCotaController.sucessCallbackCarregarFornecedores,
				   null,
				   true);
	},
	
	sucessCallbackCarregarFornecedores : function(result) {
	    var radioBoxes =  parametroCobrancaCotaController.montarTrRadioBox(result,"checkGroupFornecedores","fornecedor_");
		$("#fornecedoresCota", this.workspace).html(radioBoxes);
        //Desabilita os checkboxes gerados dinamicamente, caso seja necessário
        if(parametroCobrancaCotaController.isReadOnly()) {
            $("#fornecedoresCota", this.workspace).find(':input:not(:disabled)').prop('disabled', true);
        }       
	},

    montarComboBox : function(result,name,onChange,selected) {
		
		var options = "";
		
		options += "<select name='"+name+"' id='"+name+"' style='width:220px;' onchange='"+onChange+"'>";
		options += "<option value=''>Selecione</option>";
		$.each(result, function(index, row) {
			if (selected == row.key.$){
			    options += "<option selected='true' value='" + row.key.$ + "'>"+ row.value.$ +"</option>";	
			}
			else{
				options += "<option value='" + row.key.$ + "'>"+ row.value.$ +"</option>";	
			}
		});
		options += "</select>";
		
		return options;
	},
	
	carregarFornecedoresPadrao : function(selected){
		var data = [{name: 'idCota', value: parametroCobrancaCotaController.idCota},
            {name: 'modoTela', value: parametroCobrancaCotaController.modoTela.value},
            {name: 'idFormaPagto', value: parametroCobrancaCotaController.idFormaPagto}];
		$.postJSON(contextPath + "/cota/parametroCobrancaCota/fornecedoresCota",
				   data,
				   function(resultado){
				       
			           parametroCobrancaCotaController.sucessCallbackCarregarFornecedoresPadrao(resultado,selected);
		           },
				   null,
				   true);
	},
	
	sucessCallbackCarregarFornecedoresPadrao : function(result,selected) {
		var comboFornecedoresPadrao =  parametroCobrancaCotaController.montarComboBox(result,"fornecedorPadrao","",selected);
	    $("#fornecedoresPadrao", this.workspace).html(comboFornecedoresPadrao);
	},

    mostrarGrid : function(idCota) {
    	
		/*PASSAGEM DE PARAMETROS*/
		$(".boletosUnificadosGrid", this.workspace).flexOptions({
			/*METODO QUE RECEBERA OS PARAMETROS*/
			url: contextPath + "/cota/parametroCobrancaCota/obterFormasCobranca",
			params: [{name:'idCota', value:idCota},
                     {name: 'modoTela', value: parametroCobrancaCotaController.modoTela.value},
                     {name: 'idHistorico', value: parametroCobrancaCotaController.idHistorico}],
			        newp: 1
		});
		
		/*RECARREGA GRID CONFORME A EXECUCAO DO METODO COM OS PARAMETROS PASSADOS*/
		$(".boletosUnificadosGrid", this.workspace).flexReload();
		
		//$(".grids", this.workspace).show();
	},
	
	getDataFromResult : function(resultado) {
		
		//TRATAMENTO NA FLEXGRID PARA EXIBIR MENSAGENS DE VALIDACAO
		if (resultado.mensagens) {
			exibirMensagemDialog(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			//$(".grids", this.workspace).hide();
			return resultado;
		}	
		
		$.each(resultado.rows, function(index, row) {
			
			if(row.cell.parametroDistribuidor)
				parametroCobrancaCotaController.idFormaCobrancaDistribuidor = row.cell.idFormaCobranca;
			
            var title = parametroCobrancaCotaController.isReadOnly() ? 'Visualizar Forma Pagamento' : 'Editar Forma Pagamento';

            var linkEditar = '<a href="javascript:;" onclick="parametroCobrancaCotaController.popup_editar_unificacao(' + row.cell.idFormaCobranca + ');" style="cursor:pointer">' +
					     	  	'<img title="'+ title +'" src="' + contextPath + '/images/ico_editar.gif" hspace="5" border="0px" />' +
					  		  '</a>';
            var linkExcluir = '';
            if (!parametroCobrancaCotaController.isReadOnly()) {
                linkExcluir = '<a href="javascript:;" onclick="parametroCobrancaCotaController.popup_excluir_unificacao(' + row.cell.idFormaCobranca + ');" style="cursor:pointer">' +
                    '<img title="Excluir Forma Pagamento" src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" />' +
                    '</a>';
            }
            
            row.cell.parametroDistribuidor = row.cell.parametroDistribuidor ? 'Sim' : 'Não';
            
            row.cell.principal = row.cell.principal ? '<img src="/nds-client/images/ico_check.gif" hspace="5" border="0px" title="Forma de cobrança principal">' : '';
            
			row.cell.acao = linkEditar + linkExcluir;
		});
			
		//$(".grids", this.workspace).show();
		
		return resultado;
	},
	
    //MODOS DE EXIBIÇÃO 
	exibe_form_contrato : function(exibir){
		
		if(exibir) {
			$(".form-contrato-hidden-class").css("visibility", "visible");
		}else{
			$(".form-contrato-hidden-class").css("visibility", "hidden");
		}
	},
	
	exibe_form_suspencao : function(exibir) {
		$(".form-suspensao-hidden-class").toggle(exibir);
	},
	
	exibe_form_upload : function(exibir) {
		$(".parametroCobrancaFileField").toggle(exibir);
		if(!exibir) {
			this.removerUpload();
			 $("#parametroCobrancaFormUploadFile").val("");
		} else {
			$("#parametroCobrancaNumeroCota", this.workspace).val(MANTER_COTA.numeroCota);
		}
	},
	
	opcaoPagto : function(op){
		
		if ((op=='BOLETO')||(op=='BOLETO_EM_BRANCO')){
			$('#divComboBanco', this.workspace).show();
			$('#divRecebeEmail', this.workspace).show();
			$('#divDadosBancarios', this.workspace).hide();
	    }
		else if ((op=='CHEQUE')||(op=='TRANSFERENCIA_BANCARIA')){
			$('#divComboBanco', this.workspace).show();
			$('#divDadosBancarios', this.workspace).show();
			$('#divRecebeEmail', this.workspace).hide();
		}    
		else if (op=='DEPOSITO'){
			$('#divDadosBancarios', this.workspace).hide();
			$('#divRecebeEmail', this.workspace).hide();
			$('#divComboBanco', this.workspace).show();
		}    
		else{
			$('#divRecebeEmail', this.workspace).hide();
			$('#divComboBanco', this.workspace).hide();
			$('#divDadosBancarios', this.workspace).hide();
		}
		
	},
	
	mostraDiario : function(){
		$("#tipoFormaCobranca", this.workspace).val('DIARIA');
		$("#semanal", this.workspace).attr("checked", false);
		$("#quinzenal", this.workspace).attr("checked", false);
		$("#mensal", this.workspace).attr("checked", false);
		$( ".semanal", this.workspace ).hide();
		$( ".quinzenal", this.workspace ).hide();
		$( ".mensal", this.workspace ).hide();
		$( ".diario", this.workspace ).show();
	},
	
	mostraQuinzenal : function(){
		$("#tipoFormaCobranca", this.workspace).val('QUINZENAL');
		$("#diario", this.workspace).attr("checked", false);
		$("#semanal", this.workspace).attr("checked", false);
		$("#mensal", this.workspace).attr("checked", false);
		$( ".diario", this.workspace ).hide();
		$( ".semanal", this.workspace ).hide();
		$( ".mensal", this.workspace ).hide();
		$( ".quinzenal", this.workspace ).show();
	},
	
	mostraSemanal : function(){
		$("#tipoFormaCobranca", this.workspace).val('SEMANAL');
		$("#diario", this.workspace).attr("checked", false);
		$("#quinzenal", this.workspace).attr("checked", false);
		$("#mensal", this.workspace).attr("checked", false);
		$( ".diario", this.workspace ).hide();
		$( ".quinzenal", this.workspace ).hide();
		$( ".mensal", this.workspace ).hide();
		$( ".semanal", this.workspace ).show();
	},
		
	mostraMensal : function(){
		$("#tipoFormaCobranca", this.workspace).val('MENSAL');
		$("#diario", this.workspace).attr("checked", false);
		$("#semanal", this.workspace).attr("checked", false);
		$("#quinzenal", this.workspace).attr("checked", false);
		$( ".diario", this.workspace ).hide();
		$( ".semanal", this.workspace ).hide();
		$( ".quinzenal", this.workspace ).hide();
		$( ".mensal", this.workspace ).show();
	},
	
	opcaoTipoFormaCobranca : function(op){
        if (op=='SEMANAL'){
            $("#semanal", this.workspace).attr("checked", true);
			$("#mensal", this.workspace).attr("checked", false);
			$("#diario", this.workspace).attr("checked", false);
			$("#quinzenal", this.workspace).attr("checked", false);
			parametroCobrancaCotaController.mostraSemanal();
	    }
		else if (op=='MENSAL'){
			$("#semanal", this.workspace).attr("checked", false);
			$("#mensal", this.workspace).attr("checked", true);
			$("#diario", this.workspace).attr("checked", false);
			$("#quinzenal", this.workspace).attr("checked", false);
			parametroCobrancaCotaController.mostraMensal();
		}    
		else if (op=='DIARIA'){
			$("#semanal", this.workspace).attr("checked", false);
			$("#mensal", this.workspace).attr("checked", false);
			$("#diario", this.workspace).attr("checked", true);
			$("#quinzenal", this.workspace).attr("checked", false);
			parametroCobrancaCotaController.mostraDiario();
		}    
		else if (op=='QUINZENAL'){
			$("#semanal", this.workspace).attr("checked", false);
			$("#mensal", this.workspace).attr("checked", false);
			$("#diario", this.workspace).attr("checked", false);
			$("#quinzenal", this.workspace).attr("checked", true);
			parametroCobrancaCotaController.mostraQuinzenal();
		}    
	},

	//PARAMETROS DE COBRANÇA
	obterParametroCobranca : function(idCota){
		var data = [{name: 'idCota', value: idCota},
                    {name: 'modoTela', value: parametroCobrancaCotaController.modoTela.value},
                    {name: 'idHistorico', value: parametroCobrancaCotaController.idHistorico}];
		$.postJSON(contextPath + "/cota/parametroCobrancaCota/obterParametroCobranca",
				   data,
				   parametroCobrancaCotaController.sucessCallbackParametroCobranca, 
				   null,
				   true);
	},

	sucessCallbackParametroCobranca : function(resultado) {
		
		//hidden
		$("#_idParametroCobranca", this.workspace).val(resultado.idParametroCobranca);
		$("#_idCota", this.workspace).val(resultado.idCota);
		$("#_numCota", this.workspace).val(resultado.numCota);
	
		$("#fatorVencimento", this.workspace).val(resultado.fatorVencimento);
		
		$("#sugereSuspensao", this.workspace).val(resultado.sugereSuspensao);
		$("#sugereSuspensao").attr("checked", resultado.sugereSuspensao);

		$("#contrato", this.workspace).val(resultado.contrato);
		$("#contrato").attr("checked", resultado.contrato);

		parametroCobrancaCotaController.exibe_form_contrato(resultado.contrato);

		$("#valorMinimo", this.workspace).val( floatToPrice( resultado.valorMinimo ) );
		$("#qtdDividasAberto", this.workspace).val(resultado.qtdDividasAberto);
		$("#vrDividasAberto", this.workspace).val(resultado.vrDividasAberto);
		$("#tipoCota", this.workspace).val(resultado.tipoCota);
		
		$("#unificaCobranca", this.workspace).val(resultado.unificaCobranca?0:1);

        if (resultado.inicioContrato) {
            $("#parametroCobrancaDateInicio", this.workspace).val(resultado.inicioContrato.$);
        }
        if (resultado.terminoContrato) {
            $("#parametroCobrancaDateTermino", this.workspace).val(resultado.terminoContrato.$);
        }

        if (parametroCobrancaCotaController.isModoTelaCadastroCota()) {
            parametroCobrancaCotaController.carregarFornecedoresRelacionados();
        }
        
        parametroCobrancaCotaController.carregarFornecedoresPadrao(resultado.idFornecedor);
	},

	buildParametroCobrancaDto : function() {
		
		// hidden
		var idParametroCobranca = $("#_idParametroCobranca", this.workspace).val();
		var idCota = $("#_idCota", this.workspace).val();
		var numCota = $("#_numCota", this.workspace).val();
		
		var fatorVencimento  = $("#fatorVencimento", this.workspace).val();
		
		$("#sugereSuspensao", this.workspace).val(0);
		//if (document.formFinanceiro.sugereSuspensao.checked){
		if ($("#sugereSuspensao", this.workspace).is(":checked")) {
			$("#sugereSuspensao", this.workspace).val(1);
		}
		var sugereSuspensao = $("#sugereSuspensao", this.workspace).val();
		
		$("#contrato", this.workspace).val(0);
		//if (document.formFinanceiro.contrato.checked){
		if ($("#contrato", this.workspace).is(":checked")) {
			$("#contrato", this.workspace).val(1);
		}
		var contrato = $("#contrato", this.workspace).val();
	 
		var valorMinimo = priceToFloat($("#valorMinimo", this.workspace).val());

		var qtdDividasAberto = $("#qtdDividasAberto", this.workspace).val();
		var vrDividasAberto = $("#vrDividasAberto", this.workspace).val();
		var tipoCota = $("#tipoCota", this.workspace).val();
		var fornecedorPadrao = $("#fornecedorPadrao", this.workspace).val();
		var unificaCobranca = $("#unificaCobranca", this.workspace).val()==0?1:0;
		
		var params = {"parametroCobranca.idParametroCobranca": idParametroCobranca,
				"parametroCobranca.idCota": idCota,
				"parametroCobranca.numCota": numCota,   
				"parametroCobranca.fatorVencimento": fatorVencimento,   
				"parametroCobranca.sugereSuspensao": sugereSuspensao,   
				"parametroCobranca.contrato": contrato,         
				"parametroCobranca.valorMinimo": valorMinimo,   
				"parametroCobranca.qtdDividasAberto": qtdDividasAberto,  
				"parametroCobranca.vrDividasAberto": vrDividasAberto,
				"parametroCobranca.tipoCota": tipoCota,
				"parametroCobranca.idFornecedor": fornecedorPadrao,
				"parametroCobranca.unificaCobranca": unificaCobranca};
		
		return params;
	},
	
	postarParametroCobranca : function() {
		
		var params = parametroCobrancaCotaController.buildParametroCobrancaDto();
		
		$.postJSON(
				   contextPath + "/cota/parametroCobrancaCota/obterQtdFormaCobrancaCota",
				   {"id" : params["parametroCobranca.idCota"]},
				   function(response){
					   if (response == 0) {
						   
						   $("#dialog-confirm-formaCobrancaDistribuidor").dialog({
								resizable : false,
								height:170,
								width:490,
								modal : true,
								buttons : {
									"Confirmar" : function() {
										$(this).dialog("close");
										
										$.postJSON(
											   contextPath + "/cota/parametroCobrancaCota/postarParametroCobranca",
											   params,
											   function(){
												   parametroCobrancaCotaController.mostrarGrid(params["parametroCobranca.idCota"]);
										           return true;
											   },
											   function(){
										           return false;
											   },
											   true
										);	
									},
									"Cancelar" : function() {
										$(this).dialog("close");
									}
								}
							});
						   
					   }
					   else {
						   $.postJSON(
								   contextPath + "/cota/parametroCobrancaCota/postarParametroCobranca",
								   params,
								   function(){
									   parametroCobrancaCotaController.mostrarGrid(params["parametroCobranca.idCota"]);
							           return true;
								   },
								   function(){
							           return false;
								   },
								   true
							);	
					   }
				   }
		);
	},
	calcularDataTermino : function() {
		if(parametroCobrancaCotaController.isModoTelaCadastroCota()) {
            var dataInicio = $("#parametroCobrancaDateInicio").val();

            var params = {"dataInicio":dataInicio};

            $.postJSON(contextPath + "/cota/parametroCobrancaCota/calcularDataTermino.json", params,
                function(result) {

                    $("#parametroCobrancaDateTermino").val(result.dataTermino);

                }
            );
        }
	},
	
	//FORMAS DE COBRANÇA
	preparaNovaFormaCobranca : function(){
		
		$("input[name='checkGroupFornecedores']:checked", this.workspace).each(function(i) {
			//document.getElementById("fornecedor_"+$(this).val()).checked = false;
			$("#fornecedor_"+$(this), this.workspace).attr("checked", false);
		});
		
		$( ".semanal", this.workspace ).hide();
		$( ".mensal", this.workspace ).hide();
		$( ".diario", this.workspace ).hide();
		$( ".quinzenal", this.workspace ).hide();
		
		$("#mensal", this.workspace).attr("checked", false);
		$("#semanal", this.workspace).attr("checked", false);
		/*document.formularioFormaCobranca.mensal.checked = false;
		document.formularioFormaCobranca.semanal.checked = false;*/
		$("#diario", this.workspace).attr("checked", false);
		$("#quinzenal", this.workspace).attr("checked", false);
		
		$("#_idFormaCobranca", this.workspace).val("");
		$("#tipoCobrancaParametroCobrancaCota", this.workspace).val("");
		$("#tipoFormaCobranca", this.workspace).val("");
		$("#banco", this.workspace).val("");
		$("#numBanco", this.workspace).val("");
		$("#nomeBanco", this.workspace).val("");
		$("#agencia", this.workspace).val("");
		$("#agenciaDigito", this.workspace).val("");
		$("#conta", this.workspace).val("");
	    $("#contaDigito", this.workspace).val("");
	    $("#diaDoMesCota", this.workspace).val("");
	    $("#primeiroDiaQuinzenalParametroCobrancaCota", this.workspace).val("");
	    $("#segundoDiaQuinzenalParametroCobrancaCota", this.workspace).val("");

		$("#recebeEmail", this.workspace).attr("checked", false);
		$("#PS", this.workspace).attr("checked", false);
		$("#PT", this.workspace).attr("checked", false);
		$("#PQ", this.workspace).attr("checked", false);
		$("#PQu", this.workspace).attr("checked", false);
		$("#PSex", this.workspace).attr("checked", false);
		$("#PSab", this.workspace).attr("checked", false);
		$("#PDom", this.workspace).attr("checked", false);
		
		this.opcaoPagto();
		
		this.carregarFornecedoresRelacionados();
		
		parametroCobrancaCotaController.carregarComboTipoCobranca("");

		//parametroCobrancaCotaController.obterFormaCobrancaDefault();
	}, 

	
	tratarRespostaUploadAnexoContrato : function(data) {
		
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
			
		} else {
			parametroCobrancaCotaController.showFileName(responseJson.fileName);
		}			
	},
	
	showFileName : function(fileName) {
		$("#parametroCobrancaArquivo > *").remove();
		
		var fileName = '<span id="parametroCobrancaFileName">'+fileName+'</span>';
		
		$("#parametroCobrancaArquivo", this.workspace).append(fileName);
	},
	
	carregarArquivoContrato : function() {

		var idCota = MANTER_COTA.idCota;
		var numeroCota = MANTER_COTA.numeroCota;
		var params = {idCota:idCota, numeroCota:numeroCota};
		
		var _this = this;
		
		$.postJSON(contextPath + "/cota/parametroCobrancaCota/carregarArquivoContrato", params,
				function(data){
					if(data.isRecebido) {
						
						_this.exibe_form_upload(true);
						$("#parametroCobrancaIsRecebidoCheckBox").attr("checked", true);
						_this.showFileName(data.fileName);
					}
		});
	},
	
	removerUpload : function() {
		var fileName =  $("#parametroCobrancaFileName").html();
		
		if (!fileName) return;
		
		$.postJSON(contextPath + "/cota/parametroCobrancaCota/removerUpload.json", null,
			null,  
			function(data) {
			
				var mensagens = data.mensagens? data.mensagens:data;
				var tipoMensagem = mensagens.tipoMensagem;
				var listaMensagens = mensagens.listaMensagens;

				if (tipoMensagem && listaMensagens) {
					exibirMensagemDialog(tipoMensagem, listaMensagens,"dialog-cota");
				}
		},true);
		
		$("#parametroCobrancaArquivo > *").remove();
	},
	
	obterFornecedoresUnificados : function(unificados) {
		$("input[name='checkGroupFornecedores']:checked", parametroCobrancaCotaController.workspace).each(function(i) {
			//$("#fornecedor_"+$(this).val(), this.workspace).attr("checked", false);
			document.getElementById("fornecedor_"+$(this).val(), parametroCobrancaCotaController.workspace).checked = false;
		});
		var i;
		for(i=0;i<unificados.length;i++){
			//$("#fornecedor_"+unificados[i], this.workspace).attr("checked", false);
			if(document.getElementById('fornecedor_'+unificados[i])){
			    document.getElementById("fornecedor_"+unificados[i], parametroCobrancaCotaController.workspace).checked = true;
			}
		}
	},
	
	obterFormaCobranca : function(idFormaCobranca){
		var data = [{name: 'idFormaCobranca', value: idFormaCobranca}, 
		            {name: 'modoTela', value: parametroCobrancaCotaController.modoTela.value }];
		$.postJSON(contextPath + "/cota/parametroCobrancaCota/obterFormaCobranca",
				   data,
				   parametroCobrancaCotaController.sucessCallbackFormaCobranca, 
				   null,
				   true);
	},

	sucessCallbackFormaCobranca : function(resultado) {
		
		//hidden
		$("#_idFormaCobranca", this.workspace).val(resultado.idFormaCobranca);
		
		parametroCobrancaCotaController.carregarComboTipoCobranca(resultado.tipoCobranca);
		
        $("#tipoFormaCobranca", this.workspace).val(resultado.tipoFormaCobranca);
		if (parametroCobrancaCotaController.isModoTelaCadastroCota()) {
            parametroCobrancaCotaController.carregarBancos(resultado.idBanco);
        } else {
            if (resultado.numBanco) {
                var descricaoBanco = resultado.numBanco;
                descricaoBanco += '-'  + resultado.nomeBanco;
                descricaoBanco += '-' + resultado.conta;
                if (resultado.contaDigito) {
                    descricaoBanco+= '-' + resultado.contaDigito;
                }
                montarComboBoxUnicaOpcao("",
                    descricaoBanco,$("#banco", this.workspace));
            }
        }
		$("#numBanco", this.workspace).val(resultado.numBanco);
		$("#nomeBanco", this.workspace).val(resultado.nomeBanco);
		$("#agencia", this.workspace).val(resultado.agencia);
		$("#agenciaDigito", this.workspace).val(resultado.agenciaDigito);
		$("#conta", this.workspace).val(resultado.conta);
	    $("#contaDigito", this.workspace).val(resultado.contaDigito);
	    $("#diaDoMesCota", this.workspace).val(resultado.diaDoMes);
	    $("#primeiroDiaQuinzenalParametroCobrancaCota", this.workspace).val(resultado.primeiroDiaQuinzenal);
	    $("#segundoDiaQuinzenalParametroCobrancaCota", this.workspace).val(resultado.segundoDiaQuinzenal);
	    
		
		$("#recebeEmail", this.workspace).attr("checked", resultado.recebeEmail);

        $("#PCC-PS", this.workspace).attr("checked", resultado.segunda);
		$("#PCC-PT", this.workspace).attr("checked", resultado.terca);
		$("#PCC-PQ", this.workspace).attr("checked", resultado.quarta);
		$("#PCC-PQu", this.workspace).attr("checked", resultado.quinta);
		$("#PCC-PSex", this.workspace).attr("checked", resultado.sexta);
		$("#PCC-PSab", this.workspace).attr("checked", resultado.sabado);
		$("#PCC-PDom", this.workspace).attr("checked", resultado.domingo);

		parametroCobrancaCotaController.opcaoPagto(resultado.tipoCobranca);
		parametroCobrancaCotaController.opcaoTipoFormaCobranca(resultado.tipoFormaCobranca);
		parametroCobrancaCotaController.obterFornecedoresUnificados(resultado.fornecedoresId);
	},

    carregarBancos : function(selected){
        carregarCombo(contextPath + "/cota/parametroCobrancaCota/carregarBancos", null,
            $("#banco", this.workspace), selected, null);
    },

	obterFormaCobrancaDefault : function(){
		$.postJSON(contextPath + "/cota/parametroCobrancaCota/obterFormaCobrancaDefault",
		   null,
		   function(resultado){
	            
	           if (resultado){
	
				   //hidden
				   $("#_idFormaCobranca", this.workspace).val(resultado.idFormaCobranca);
				
				   $("#tipoCobrancaParametroCobrancaCota", this.workspace).val(resultado.tipoCobranca);
				   $("#tipoFormaCobranca", this.workspace).val(resultado.tipoFormaCobranca);
				   if (resultado.diasDoMes.length == 1){
				       $("#diaDoMesCota", this.workspace).val(resultado.diasDoMes[0]);
				   }
				   else if (resultado.diasDoMes.length > 1){
					   $("#primeiroDiaQuinzenalParametroCobrancaCota", this.workspace).val(resultado.diasDoMes[0]);
					   $("#segundoDiaQuinzenalParametroCobrancaCota", this.workspace).val(resultado.diasDoMes[1]);
				   }
				   $("#PCC-PS", this.workspace).attr("checked", resultado.segunda);
				   $("#PCC-PT", this.workspace).attr("checked", resultado.terca);
				   $("#PCC-PQ", this.workspace).attr("checked", resultado.quarta);
				   $("#PCC-PQu", this.workspace).attr("checked", resultado.quinta);
				   $("#PCC-PSex", this.workspace).attr("checked", resultado.sexta);
				   $("#PCC-PSab", this.workspace).attr("checked", resultado.sabado);
				   $("#PCC-PDom", this.workspace).attr("checked", resultado.domingo);
	
				   parametroCobrancaCotaController.opcaoPagto(resultado.tipoCobranca);
				   parametroCobrancaCotaController.opcaoTipoFormaCobranca(resultado.tipoFormaCobranca); 
	           }
	           
           },
           null,
		   true);
	},

	obterFornecedoresMarcados : function() {
		var fornecedorMarcado = new Array();
		$("input[name='checkGroupFornecedores']:checked", this.workspace).each(function(i) {			
			fornecedorMarcado.push($(this).val());
		});
		return fornecedorMarcado;
	},
	
	buildFormaCobrancaDTO : function(){

		//hidden
		var idFormaCobranca = $("#_idFormaCobranca", this.workspace).val();
		var idCota = $("#_idCota", this.workspace).val();
		var idParametroCobranca = $("#_idParametroCobranca", this.workspace).val();
		
		var tipoCobranca        = $("#tipoCobrancaParametroCobrancaCota", this.workspace).val();
		var tipoFormaCobranca   = $("#tipoFormaCobranca", this.workspace).val();
		var idBanco             = $("#banco", this.workspace).val();
		var numBanco            = $("#numBanco", this.workspace).val();
		var nomeBanco           = $("#nomeBanco", this.workspace).val();
		var agencia             = $("#agencia", this.workspace).val();
		var agenciaDigito       = $("#agenciaDigito", this.workspace).val();
		var conta               = $("#conta", this.workspace).val();
		var contaDigito         = $("#contaDigito", this.workspace).val();
		var diaDoMes            = $("#diaDoMesCota", this.workspace).val();
		var primeiroDiaQuinzenal= $("#primeiroDiaQuinzenalParametroCobrancaCota", this.workspace).val();
		var segundoDiaQuinzenal = $("#segundoDiaQuinzenalParametroCobrancaCota", this.workspace).val();
		
		var valorMinimo			= priceToFloat($("#valorMinimo", this.workspace).val());
		var fatorVencimento		= $("#fatorVencimento", this.workspace).val();
		var tipoCota 			= $("#tipoCota", this.workspace).val();
		var fornecedorPadrao 	= $("#fornecedorPadrao", this.workspace).val();
		var unificaCobranca 	= $("#unificaCobranca", this.workspace).val()==0?1:0;

		$("#recebeEmail", this.workspace).val(0);
		//if (document.formularioDadosBoleto.recebeEmail.checked){
		if ($("#recebeEmail", this.workspace).is(":checked")) {
			$("#recebeEmail", this.workspace).val(1);
		}
		var recebeEmail = $("#recebeEmail", this.workspace).val();
		
		$("#PCC-PS", this.workspace).val(0);
		//if (document.formularioFormaCobranca.PS.checked){
		if ($("#PCC-PS", this.workspace).is(":checked")) {
			$("#PCC-PS", this.workspace).val(1);
		}
		var segunda = $("#PCC-PS", this.workspace).val();
		
		$("#PCC-PT", this.workspace).val(0);
		//if (document.formularioFormaCobranca.PT.checked){
		if ($("#PCC-PT", this.workspace).is(":checked")) {
			$("#PCC-PT", this.workspace).val(1);
		}
		var terca = $("#PCC-PT", this.workspace).val();
		
		$("#PCC-PQ", this.workspace).val(0);
		//if (document.formularioFormaCobranca.PQ.checked){
		if ($("#PCC-PQ", this.workspace).is(":checked")) {
			$("#PCC-PQ", this.workspace).val(1);
		}
		var quarta = $("#PCC-PQ", this.workspace).val();
		
		$("#PCC-PQu", this.workspace).val(0);
		//if (document.formularioFormaCobranca.PQu.checked){
		if ($("#PCC-PQu", this.workspace).is(":checked")) {
			$("#PCC-PQu", this.workspace).val(1);
		}
		var quinta = $("#PCC-PQu", this.workspace).val();
		
		$("#PCC-PSex", this.workspace).val(0);
		//if (document.formularioFormaCobranca.PSex.checked){
		if ($("#PCC-PSex", this.workspace).is(":checked")) {
			$("#PCC-PSex", this.workspace).val(1);
		}
		var sexta  = $("#PCC-PSex", this.workspace).val();
		
		$("#PCC-PSab", this.workspace).val(0);
		//if (document.formularioFormaCobranca.PSab.checked){
		if ($("#PCC-PSab", this.workspace).is(":checked")) {
			$("#PCC-PSab", this.workspace).val(1);
		}
		var sabado = $("#PCC-PSab", this.workspace).val();
		
		$("#PCC-PDom", this.workspace).val(0);
		//if (document.formularioFormaCobranca.PDom.checked){
		if ($("#PCC-PDom", this.workspace).is(":checked")) {
			$("#PCC-PDom", this.workspace).val(1);
		}
		var domingo  = $("#PCC-PDom", this.workspace).val();
				
		var params =  {"formaCobranca.idCota": idCota,
						 "formaCobranca.idParametroCobranca": idParametroCobranca,
						 "formaCobranca.tipoCobranca": tipoCobranca, 
						 "formaCobranca.idBanco": idBanco,           
						 "formaCobranca.recebeEmail": recebeEmail,   
						 "formaCobranca.numBanco": numBanco,       
						 "formaCobranca.nomeBanco": nomeBanco,         
						 "formaCobranca.agencia": agencia,           
						 "formaCobranca.agenciaDigito": agenciaDigito,    
						 "formaCobranca.conta": conta,             
						 "formaCobranca.contaDigito": contaDigito,       
						 "formaCobranca.domingo": domingo,   
						 "formaCobranca.segunda": segunda,           
						 "formaCobranca.terca": terca,           
						 "formaCobranca.quarta": quarta,           
						 "formaCobranca.quinta": quinta,           
						 "formaCobranca.sexta": sexta,           
						 "formaCobranca.sabado": sabado,
						 "formaCobranca.diaDoMes": diaDoMes,
						 "formaCobranca.primeiroDiaQuinzenal": primeiroDiaQuinzenal,
						 "formaCobranca.segundoDiaQuinzenal": segundoDiaQuinzenal,
						 "tipoFormaCobranca": tipoFormaCobranca,
						 "parametroCobranca.idCota": idCota,
						 "parametroCobranca.valorMinimo": valorMinimo,
						 "parametroCobranca.fatorVencimento": fatorVencimento,
						 "parametroCobranca.tipoCota": tipoCota,
						 "parametroCobranca.idFornecedor": fornecedorPadrao,
						 "parametroCobranca.unificaCobranca": unificaCobranca};
		
		params = serializeArrayToPost('listaIdsFornecedores',parametroCobrancaCotaController.obterFornecedoresMarcados(), params );
 
		return params;
	},
	
	postarFormaCobranca : function(novo, incluirSemFechar) {
		
		var telaMensagem="idModalUnificacao",
			idFormaCobranca = $("#_idFormaCobranca", this.workspace).val(),
			idCota = $("#_idCota", this.workspace).val(),
			idParametroCobranca = $("#_idParametroCobranca", this.workspace).val(),
			params = {};
		
		params = parametroCobrancaCotaController.buildFormaCobrancaDTO();

		if (novo) {
			$.postJSON(contextPath + "/cota/parametroCobrancaCota/postarFormaCobranca",
					  params,
					   function(mensagens) {
				           $("#dialog-unificacao", this.workspace).dialog("close");
				           if (incluirSemFechar){
				        	   
				        	   parametroCobrancaCotaController.popup_nova_unificacao();
				           }
				           else{
				        	   telaMensagem=null;
				        	   if (mensagens){
								   var tipoMensagem = mensagens.tipoMensagem;
								   var listaMensagens = mensagens.listaMensagens;
								   if (tipoMensagem && listaMensagens) {
								       exibirMensagem(tipoMensagem, listaMensagens);
							       }
				        	   }	   
				           }
				           parametroCobrancaCotaController.mostrarGrid(idCota);
		               },
					   null,
					   true,
					   telaMensagem);
		}
		else{
			params["formaCobranca.idFormaCobranca"] = idFormaCobranca;
			$.postJSON(contextPath + "/cota/parametroCobrancaCota/postarFormaCobranca",params,
					   function(mensagens) {
				           $("#dialog-unificacao", this.workspace).dialog("close");
				           if (incluirSemFechar){
				        	   
				        	   parametroCobrancaCotaController.popup_nova_unificacao();
				           }
				           else{
				        	   telaMensagem=null;
				        	   if (mensagens){
								   var tipoMensagem = mensagens.tipoMensagem;
								   var listaMensagens = mensagens.listaMensagens;
								   if (tipoMensagem && listaMensagens) {
								       exibirMensagem(tipoMensagem, listaMensagens);
							       }
				        	   }
				           }
				           parametroCobrancaCotaController.mostrarGrid(idCota);
		               },
					   null,
					   true,
					   telaMensagem);
	    }
	},
	
	excluirFormaCobranca : function(idFormaCobranca){
		var idCota = $("#_idCota", this.workspace).val();
		var data = [{name: 'idFormaCobranca', value: idFormaCobranca}];
		$.postJSON(contextPath + "/cota/parametroCobrancaCota/excluirFormaCobranca",
				   data,
				   function(){
			parametroCobrancaCotaController.mostrarGrid(idCota);
				   },
				   null,
				   true);
	},

	//POPUPS
	popup_nova_unificacao : function() {
		
		parametroCobrancaCotaController.preparaNovaFormaCobranca();
		
		$( "#dialog-unificacao", this.workspace ).dialog({
			resizable: false,
			height:630,
			width:500,
			modal: true,
			disabled: false,
			buttons: {
				"Confirmar": function() {	
					
					parametroCobrancaCotaController.postarFormaCobranca(true,false);

				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			beforeClose: function() {
				clearMessageDialogTimeout("idModalUnificacao", this.workspace);
		    },
			form: $("#workspaceCota", this.workspace)
		});
    },

    popup_editar_unificacao : function(idFormaCobranca) {
    	if (!parametroCobrancaCotaController.isModoTelaCadastroCota()) {
            parametroCobrancaCotaController.idFormaPagto = idFormaCobranca;
            parametroCobrancaCotaController.carregarFornecedoresRelacionados();
        }

    	parametroCobrancaCotaController.obterFormaCobranca(idFormaCobranca);

        if (parametroCobrancaCotaController.isReadOnly()) {
            $( "#dialog-unificacao", this.workspace ).dialog({
                resizable: false,
                height:630,
                width:500,
                modal: true,
                disabled: false,
                buttons: {
                   "Fechar": function() {
                        $( this ).dialog( "close" );
                    }
                },
                form: $("#workspaceCota", this.workspace)
            });

        } else {
            $( "#dialog-unificacao", this.workspace ).dialog({
                resizable: false,
                height:630,
                width:500,
                modal: true,
                disabled: false,
                buttons: {
                    "Confirmar": function() {

                        parametroCobrancaCotaController.postarFormaCobranca(false,false);

                    },
                    "Cancelar": function() {
                        $( this ).dialog( "close" );
                    }
                },
                beforeClose: function() {
                    clearMessageDialogTimeout("idModalUnificacao");
                },
                form: $("#workspaceCota", this.workspace)
            });
        }
    },
    
    popup_excluir_unificacao : function(idFormaCobranca) {
    	
		$( "#dialog-excluir-unificacao", this.workspace ).dialog({
			resizable: false,
			height:170,
			width:490,
			modal: true,
			disabled: false,
			buttons: {
				"Confirmar": function() {
					
					parametroCobrancaCotaController.excluirFormaCobranca(idFormaCobranca);
					
					$( this ).dialog( "close" );
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			beforeClose: function() {
				clearMessageDialogTimeout("idModalUnificacao");
		    },
			form: $("#workspaceCota", this.workspace)
		});
	},

	//IMPRESSÃO DO CONTRATO
	imprimeContrato : function(){
		
		
		var idCota = $("#_idCota", this.workspace).val();
		var dataInicio = $("#parametroCobrancaDateInicio",this.workspace).val();
		var dataTermino = $("#parametroCobrancaDateTermino",this.workspace).val();
		var isRecebido = $("#parametroCobrancaIsRecebidoCheckBox").is(":checked");
		
		var fileName =  $("#parametroCobrancaFileName",this.workspace).html();
		
		if (!fileName) isRecebido = false;
		
	    document.location.assign(
	    		contextPath + "/cota/parametroCobrancaCota/imprimeContrato?" +
	    				"idCota="+idCota+"&dataInicio="+dataInicio+"&dataTermino="+dataTermino+"&isRecebido="+isRecebido);
	},
	
	uploadContratoAnexo : function() {
		var selectedFile = $("#parametroCobrancaFormUploadFile").val();
		
		if(!selectedFile) return;
		
		$('#parametroCobrancaFormUpload', this.workspace).submit();
	},
	
	
	//INCLUSÃO DE NOVA UNIFICAÇÃO SEM SAIR DO POPUP
	incluirNovaUnificacao : function(){
		parametroCobrancaCotaController.postarFormaCobranca(false,true);
	}
	
}, BaseController);
//@ sourceURL=parametroCobrancaCota.js