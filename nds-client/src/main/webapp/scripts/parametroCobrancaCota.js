var parametroCobrancaCotaController = $.extend(true, {

    idCota: "",
    idHistorico: "",
    idFormaPagto: "",
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
		$("#valorMinimo", this.workspace).numeric();
		$("#qtdDividasAberto", this.workspace).numeric();
		$("#vrDividasAberto", this.workspace).numeric();
		
		$("#numBanco", this.workspace).numeric();
		$("#nomeBanco", this.workspace).numeric();
		$("#agencia", this.workspace).numeric();
		$("#agenciaDigito", this.workspace).numeric();
		$("#conta", this.workspace).numeric();
	    $("#contaDigito", this.workspace).numeric();
	    $("#diaDoMes", this.workspace).numeric();
	    $(".dataInputMask", this.workspace).mask("99/99/9999");
	    $("#parametroCobrancaDateInicio", this.workspace).val(formatDateToString(new Date()));
	    $("#primeiroDiaQuinzenal", this.workspace).numeric();
	    $("#segundoDiaQuinzenal", this.workspace).numeric();
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

		$("#valorMinimo", this.workspace).val(resultado.valorMinimo);
		$("#qtdDividasAberto", this.workspace).val(resultado.qtdDividasAberto);
		$("#vrDividasAberto", this.workspace).val(resultado.vrDividasAberto);
		$("#tipoCota", this.workspace).val(resultado.tipoCota);

        if (resultado.inicioContrato) {
            $("#parametroCobrancaDateInicio", this.workspace).val(resultado.inicioContrato.$);
        }
        if (resultado.terminoContrato) {
            $("#parametroCobrancaDateTermino", this.workspace).val(resultado.terminoContrato.$);
        }

        if (parametroCobrancaCotaController.isModoTelaCadastroCota()) {
            parametroCobrancaCotaController.carregarFornecedoresRelacionados();
        }
	},

	postarParametroCobranca : function() {
		
		//hidden
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
	 
		var valorMinimo = $("#valorMinimo", this.workspace).val();
		var qtdDividasAberto = $("#qtdDividasAberto", this.workspace).val();
		var vrDividasAberto = $("#vrDividasAberto", this.workspace).val();
		var tipoCota = $("#tipoCota", this.workspace).val();
		
		$.postJSON(contextPath + "/cota/parametroCobrancaCota/postarParametroCobranca",
				{"parametroCobranca.idParametroCobranca":idParametroCobranca,
			"parametroCobranca.idCota":idCota,
			"parametroCobranca.numCota":numCota,   
			"parametroCobranca.fatorVencimento":fatorVencimento,   
			"parametroCobranca.sugereSuspensao":sugereSuspensao,   
			"parametroCobranca.contrato":contrato,         
			"parametroCobranca.valorMinimo":valorMinimo,   
			"parametroCobranca.qtdDividasAberto":qtdDividasAberto,  
			"parametroCobranca.vrDividasAberto":vrDividasAberto+
			"parametroCobranca.tipoCota":tipoCota},
				   function(){
			           return true;
				   },
				   function(){
			           return false;
				   },
				   true);
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
		$("#tipoCobranca", this.workspace).val("");
		$("#tipoFormaCobranca", this.workspace).val("");
		$("#banco", this.workspace).val("");
		$("#numBanco", this.workspace).val("");
		$("#nomeBanco", this.workspace).val("");
		$("#agencia", this.workspace).val("");
		$("#agenciaDigito", this.workspace).val("");
		$("#conta", this.workspace).val("");
	    $("#contaDigito", this.workspace).val("");
	    $("#diaDoMes", this.workspace).val("");
	    $("#primeiroDiaQuinzenal", this.workspace).val("");
	    $("#segundoDiaQuinzenal", this.workspace).val("");

		$("#recebeEmail", this.workspace).attr("checked", false);
		$("#PS", this.workspace).attr("checked", false);
		$("#PT", this.workspace).attr("checked", false);
		$("#PQ", this.workspace).attr("checked", false);
		$("#PQu", this.workspace).attr("checked", false);
		$("#PSex", this.workspace).attr("checked", false);
		$("#PSab", this.workspace).attr("checked", false);
		$("#PDom", this.workspace).attr("checked", false);

	    /*document.formularioDadosBoleto.recebeEmail.checked = false;
		document.formularioFormaCobranca.PS.checked = false;
		document.formularioFormaCobranca.PT.checked = false;
		document.formularioFormaCobranca.PQ.checked = false;
		document.formularioFormaCobranca.PQu.checked = false;
		document.formularioFormaCobranca.PSex.checked = false;
		document.formularioFormaCobranca.PSab.checked = false;
		document.formularioFormaCobranca.PDom.checked = false;*/
		
		
		parametroCobrancaCotaController.obterFormaCobrancaDefault();
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
		
		$("#tipoCobranca", this.workspace).val(resultado.tipoCobranca);
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
	    $("#diaDoMes", this.workspace).val(resultado.diaDoMes);
	    $("#primeiroDiaQuinzenal", this.workspace).val(resultado.primeiroDiaQuinzenal);
	    $("#segundoDiaQuinzenal", this.workspace).val(resultado.segundoDiaQuinzenal);
	    
		
		$("#recebeEmail", this.workspace).attr("checked", resultado.recebeEmail);

        $("#PS", this.workspace).attr("checked", resultado.segunda);
		$("#PT", this.workspace).attr("checked", resultado.terca);
		$("#PQ", this.workspace).attr("checked", resultado.quarta);
		$("#PQu", this.workspace).attr("checked", resultado.quinta);
		$("#PSex", this.workspace).attr("checked", resultado.sexta);
		$("#PSab", this.workspace).attr("checked", resultado.sabado);
		$("#PDom", this.workspace).attr("checked", resultado.domingo);

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
				
				   $("#tipoCobranca", this.workspace).val(resultado.tipoCobranca);
				   $("#tipoFormaCobranca", this.workspace).val(resultado.tipoFormaCobranca);
				   if (resultado.diasDoMes.length == 1){
				       $("#diaDoMes", this.workspace).val(resultado.diasDoMes[0]);
				   }
				   else if (resultado.diasDoMes.length > 1){
					   $("#primeiroDiaQuinzenal", this.workspace).val(resultado.diasDoMes[0]);
					   $("#segundoDiaQuinzenal", this.workspace).val(resultado.diasDoMes[1]);
				   }
				   $("#PS", this.workspace).attr("checked", resultado.segunda);
				   $("#PT", this.workspace).attr("checked", resultado.terca);
				   $("#PQ", this.workspace).attr("checked", resultado.quarta);
				   $("#PQu", this.workspace).attr("checked", resultado.quinta);
				   $("#PSex", this.workspace).attr("checked", resultado.sexta);
				   $("#PSab", this.workspace).attr("checked", resultado.sabado);
				   $("#PDom", this.workspace).attr("checked", resultado.domingo);
	
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
	
	postarFormaCobranca : function(novo, incluirSemFechar) {
		
		var telaMensagem="idModalUnificacao";
		
		//hidden
		var idFormaCobranca = $("#_idFormaCobranca", this.workspace).val();
		var idCota = $("#_idCota", this.workspace).val();
		var idParametroCobranca = $("#_idParametroCobranca", this.workspace).val();
		
		var tipoCobranca        = $("#tipoCobranca", this.workspace).val();
		var tipoFormaCobranca   = $("#tipoFormaCobranca", this.workspace).val();
		var idBanco             = $("#banco", this.workspace).val();
		var numBanco            = $("#numBanco", this.workspace).val();
		var nomeBanco           = $("#nomeBanco", this.workspace).val();
		var agencia             = $("#agencia", this.workspace).val();
		var agenciaDigito       = $("#agenciaDigito", this.workspace).val();
		var conta               = $("#conta", this.workspace).val();
		var contaDigito         = $("#contaDigito", this.workspace).val();
		var diaDoMes            = $("#diaDoMes", this.workspace).val();
		var primeiroDiaQuinzenal= $("#primeiroDiaQuinzenal", this.workspace).val();
		var segundoDiaQuinzenal = $("#segundoDiaQuinzenal", this.workspace).val();

		$("#recebeEmail", this.workspace).val(0);
		//if (document.formularioDadosBoleto.recebeEmail.checked){
		if ($("#recebeEmail", this.workspace).is(":checked")) {
			$("#recebeEmail", this.workspace).val(1);
		}
		var recebeEmail = $("#recebeEmail", this.workspace).val();
		
		$("#PS", this.workspace).val(0);
		//if (document.formularioFormaCobranca.PS.checked){
		if ($("#PS", this.workspace).is(":checked")) {
			$("#PS", this.workspace).val(1);
		}
		var segunda = $("#PS", this.workspace).val();
		
		$("#PT", this.workspace).val(0);
		//if (document.formularioFormaCobranca.PT.checked){
		if ($("#PT", this.workspace).is(":checked")) {
			$("#PT", this.workspace).val(1);
		}
		var terca = $("#PT", this.workspace).val();
		
		$("#PQ", this.workspace).val(0);
		//if (document.formularioFormaCobranca.PQ.checked){
		if ($("#PQ", this.workspace).is(":checked")) {
			$("#PQ", this.workspace).val(1);
		}
		var quarta = $("#PQ", this.workspace).val();
		
		$("#PQu", this.workspace).val(0);
		//if (document.formularioFormaCobranca.PQu.checked){
		if ($("#PQu", this.workspace).is(":checked")) {
			$("#PQu", this.workspace).val(1);
		}
		var quinta = $("#PQu", this.workspace).val();
		
		$("#PSex", this.workspace).val(0);
		//if (document.formularioFormaCobranca.PSex.checked){
		if ($("#PSex", this.workspace).is(":checked")) {
			$("#PSex", this.workspace).val(1);
		}
		var sexta  = $("#PSex", this.workspace).val();
		
		$("#PSab", this.workspace).val(0);
		//if (document.formularioFormaCobranca.PSab.checked){
		if ($("#PSab", this.workspace).is(":checked")) {
			$("#PSab", this.workspace).val(1);
		}
		var sabado = $("#PSab", this.workspace).val();
		
		$("#PDom", this.workspace).val(0);
		//if (document.formularioFormaCobranca.PDom.checked){
		if ($("#PDom", this.workspace).is(":checked")) {
			$("#PDom", this.workspace).val(1);
		}
		var domingo  = $("#PDom", this.workspace).val();
		 var params =  {"formaCobranca.idCota":idCota,
				 "formaCobranca.idParametroCobranca":idParametroCobranca,
				 "formaCobranca.tipoCobranca":tipoCobranca, 
				 "formaCobranca.idBanco":idBanco,           
				 "formaCobranca.recebeEmail":recebeEmail,   
				 "formaCobranca.numBanco":numBanco,       
				 "formaCobranca.nomeBanco":nomeBanco,         
				 "formaCobranca.agencia":agencia,           
				 "formaCobranca.agenciaDigito":agenciaDigito,    
				 "formaCobranca.conta":conta,             
				 "formaCobranca.contaDigito":contaDigito,       
				 "formaCobranca.domingo":domingo,   
				 "formaCobranca.segunda":segunda,           
				 "formaCobranca.terca":terca,           
				 "formaCobranca.quarta":quarta,           
				 "formaCobranca.quinta":quinta,           
				 "formaCobranca.sexta":sexta,           
				 "formaCobranca.sabado":sabado,
				 "formaCobranca.diaDoMes":diaDoMes,
				 "formaCobranca.primeiroDiaQuinzenal":primeiroDiaQuinzenal,
				 "formaCobranca.segundoDiaQuinzenal":segundoDiaQuinzenal,
				 "tipoFormaCobranca":tipoFormaCobranca};
		 params = serializeArrayToPost('listaIdsFornecedores',parametroCobrancaCotaController.obterFornecedoresMarcados(), params );
		 
		 
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
		debugger;
		
		
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
