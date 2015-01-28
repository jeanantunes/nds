var parametroCobrancaCotaController = $.extend(true, {

    idCota: "",
    numeroCota: "",
    idHistorico: "",
    idFormaPagto: "",
    modoTela: null,

    financeiro: { 
    	
    	resultParametroCobranca : null,
    	
    	resultFormaCobranca: null,
    	
    	parametroDistribuidor: false,
    	
    	formaCobrancaAlterada: false,
    	
    	fornecedoresUnificacao: null
    },
    
    init : function () {

		//GRID DE FORMAS DE COBRANÇA
		$(".boletosUnificadosGrid", this.workspace).flexigrid({
			preProcess: parametroCobrancaCotaController.getDataFromResult,
			dataType : 'json',
			colModel : [  {
				display : 'Fornecedores',
				name : 'fornecedor',
				width : 270,
				sortable : true,
				align : 'left'
			},{
				display : 'Concentração',
				name : 'concentracaoPagto',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Tipo Pagamento',
				name : 'tipoPagto',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Banco',
				name : 'detalhesTipoPagto',
				width : 165,
				sortable : true,
				align : 'left'
			}, {
				display : 'P.Dist',
				name : 'parametroDistribuidor',
				width : 40,
				sortable : true,
				align : 'left'
			}, {
				display : 'Unificação',
				name : 'unificacao',
				width : 100,
				sortable : false,
				align : 'center'
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
		
		$("#valorMinimo", this.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});
		
		$("#qtdDividasAbertoCota", this.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:0
		});	
		
		$("#vrDividasAbertoCota", this.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});
		
		$("#numBanco", this.workspace).numeric();
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
	carregaFinanceiro : function(idCota) {
		
		//parametroCobrancaCotaController.financeiro.formaCobrancaAlterada = false;
		
		parametroCobrancaCotaController.financeiro.resultParametroCobranca = null;
    	
		parametroCobrancaCotaController.financeiro.resultFormaCobranca = null;
    	
		parametroCobrancaCotaController.financeiro.parametroDistribuidor = false;
    	
		parametroCobrancaCotaController.financeiro.fornecedoresUnificacao = null;
     
		parametroCobrancaCotaController.idCota = idCota;
		
		$("#parametroCobrancaDateTermino", this.workspace).val("");
        
		$("#_idParametroCobranca", this.workspace).val("");
		
		$("#_idCota", this.workspace).val("");
		
		$("#_numCota", this.workspace).val("");
		
		$("#parametroCobrancaFileName").html('');
		
		$("#parametroCobrancaIsRecebidoCheckBox").attr("checked", false);
		parametroCobrancaCotaController.exibe_form_upload(false);
		
		
		parametroCobrancaCotaController.obterParametroCobranca(idCota);
		
		parametroCobrancaCotaController.mostrarGrid(idCota);	
		
		var _this = this;
	    
	    var options = {
				success: _this.tratarRespostaUploadAnexoContrato
		};
	    
	    $('#parametroCobrancaFormUpload', this.workspace).ajaxForm(options);
	    	    
	    $(".dataInputMask").mask("99/99/9999");
	    
	    $("#parametroCobrancaDateInicio", this.workspace).val(formatDateToString(new Date()));
	    
	    parametroCobrancaCotaController.carregarComboTipoCobranca("");   
	    
	    $('.formaCobrancaValueChangeListener').change(function() {
			parametroCobrancaCotaController.financeiro.formaCobrancaAlterada = true;
		});
	    
	},
	
	montarTrRadioBox : function(result,name,nameItemIdent) {

		var options = "";
		
		$.each(result, function(index, row) {

			options += "<tr> <td width='23'>";
			options += "<input class=\"formaCobrancaValueChangeListener\" id='" + nameItemIdent + row.key.$ +"' value='" + row.key.$ + "' name='"+name+"' type='checkbox' />";
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

    montarComboBox : function(result,name,onChange) {
		
    	var desabilitar = "";
    	if ($("#" + name, parametroCobrancaCotaController.workspace).attr("disabled")){
    		desabilitar = "disabled='disabled'";
    	}
    	
		var options = "";
		
		options += "<select " + desabilitar + " name='"+name+"' id='"+name+"' style='width:220px;' onchange='"+onChange+"'>";
		options += "<option value=''>Selecione</option>";
		$.each(result, function(index, row) {
			options += "<option value='" + row.key.$ + "'>"+ row.value.$ +"</option>";	
		});
		options += "</select>";
		
		return options;
	},
	
	obterSugestaoFornecedorPadrao : function() {
		
		var selected;
		
		var options = $("#fornecedorPadrao", this.workspace).find("option");
		
		$.each(options, function(index, row) {
			
			if ('Dinap' == $(row).html()) {
				
				selected = row.value;
				
				return false;
 		    }
		});
		
		if (!selected) {
		
			$.each(options, function(index, row) {
				
				if (row.value) {
					
					selected = row.value;
					
					return false;
			    }
			});
		}
		
        return selected;
	},
	
	carregarFornecedoresPadrao : function(idFornecedor) {
		
		var data = [{name: 'idCota', value: parametroCobrancaCotaController.idCota},
            {name: 'modoTela', value: parametroCobrancaCotaController.modoTela.value},
            {name: 'idFormaPagto', value: parametroCobrancaCotaController.idFormaPagto}];
		
		$.postJSON(contextPath + "/cota/parametroCobrancaCota/fornecedoresCota",
				   data,
				   function(resultado){
					
			       		parametroCobrancaCotaController.sucessCallbackCarregarFornecedoresPadrao(resultado);
			       		
			        	if (!idFornecedor) {

			        		idFornecedor = parametroCobrancaCotaController.obterSugestaoFornecedorPadrao();
			        	}
			        	
			        	$("#fornecedorPadrao", this.workspace).val(idFornecedor);
		           },
				   null,
				   true);
	},
	
	sucessCallbackCarregarFornecedoresPadrao : function(result) {
		
		var comboFornecedoresPadrao =  parametroCobrancaCotaController.montarComboBox(result,"fornecedorPadrao","");
		
	    $("#fornecedorPadrao", this.workspace).html(comboFornecedoresPadrao);
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

            var linkEditar = '<a href="javascript:;"  onclick="parametroCobrancaCotaController.popup_editar_unificacao(' + row.cell.idFormaCobranca + ');" style="cursor:pointer">' +
					     	  	'<img title="'+ title +'" src="' + contextPath + '/images/ico_editar.gif" hspace="5" border="0px" />' +
					  		  '</a>';
            var linkExcluir = '';
            if (!parametroCobrancaCotaController.isReadOnly()) {
                linkExcluir = '<a href="javascript:;" isEdicao="true" onclick="parametroCobrancaCotaController.popup_excluir_unificacao(' + row.cell.idFormaCobranca + ');" style="cursor:pointer">' +
                    '<img title="Excluir Forma Pagamento" src="'+ contextPath +'/images/ico_excluir.gif" hspace="5" border="0px" />' +
                    '</a>';
            }
            
            row.cell.parametroDistribuidor = row.cell.parametroDistribuidor ? 'Sim' : 'Não';
            
            row.cell.principal = row.cell.principal ? '<img src="'+ contextPath +'/images/ico_check.gif" hspace="5" border="0px" title="Forma de cobrança principal">' : '';
            
            if (!row.cell.unificacao || row.cell.unificacao.toUpperCase() == 'UNIFICADORA'){
            
            	row.cell.acao = linkEditar + linkExcluir;
            } else {
            	
            	$.each($("#formFinanceiro input", parametroCobrancaCotaController.workspace),
            		function(idx, item){
            			$(item).attr("disabled", "disabled");
            		}
            	);
            	
            	$.each($("#formFinanceiro select", parametroCobrancaCotaController.workspace),
            		function(idx, item){
            			$(item).attr("disabled", "disabled");
            		}
            	);
            	
            	row.cell.acao = '';
            }
            
            if (!row.cell.unificacao){
            	
            	row.cell.unificacao = '';
            }
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
		
		if(exibir){
			
			$(".form-suspensao-hidden2-class").show();
		}	
		else{
			
			$(".form-suspensao-hidden2-class").hide();
		}	
	},
	
    exibe_form_suspencao_distribuidor : function(checked) {
		
        if(checked){

        	$.postJSON(contextPath + "/cota/parametroCobrancaCota/obterPoliticaSuspensaoDistribuidor.json", 
    			null,
				function(data){
    		
					if(data) {
			
						$("#qtdDividasAbertoCota", this.workspace).val(data.qtdDividasAberto);

			        	$("#vrDividasAbertoCota", this.workspace).val(data.vrDividasAberto);
			        	
			        	$("#sugereSuspensaoCota", this.workspace).attr("checked", data.sugereSuspensao);
					}
    		});
        	
        	$(".form-suspensao-hidden2-class").show();
        	
        	$("#qtdDividasAbertoCota", this.workspace).prop('disabled', true);

        	$("#vrDividasAbertoCota", this.workspace).prop('disabled', true);
        	
        	$("#sugereSuspensaoCota", this.workspace).prop('disabled', true);
		}	
		else{
			
			$("#qtdDividasAbertoCota", this.workspace).prop('disabled', false);
			
			$("#vrDividasAbertoCota", this.workspace).prop('disabled', false);
			
			$("#sugereSuspensaoCota", this.workspace).prop('disabled', false);
		}	
	},
	
	exibe_form_upload : function(exibir) {
		if(exibir == true)
			$(".parametroCobrancaFileField").show();
		else
			$(".parametroCobrancaFileField").hide();
			
		if(!exibir) {
			this.removerUpload();
			 $("#parametroCobrancaFormUploadFile").val("");
		} else {
			$("#parametroCobrancaNumeroCota", this.workspace).val(parametroCobrancaCotaController.numeroCota);
		}
	},
	
	opcaoPagtoBoletoEmBranco : function(isBoletoEmBranco){
		
		if (isBoletoEmBranco){
		
		    parametroCobrancaCotaController.opcaoTipoFormaCobranca('DIARIA');
		}
		
		$("#semanal", this.workspace).prop('disabled', isBoletoEmBranco);
		
		$("#quinzenal", this.workspace).prop('disabled', isBoletoEmBranco);
		
		$("#mensal", this.workspace).prop('disabled', isBoletoEmBranco);
		
		return isBoletoEmBranco;
	},
	
	opcaoPagto : function(op){
		
		$('#divRecebeEmail', this.workspace).show();
		
		if ((op=='BOLETO')||(op=='BOLETO_EM_BRANCO')){
			$('#divComboBanco', this.workspace).show();
			$('#divDadosBancarios', this.workspace).hide();
	    }
		else if (op=='TRANSFERENCIA_BANCARIA'){
			$('#divComboBanco', this.workspace).show();
			$('#divDadosBancarios', this.workspace).show();
		}    
		else if (op=='DEPOSITO'){
			$('#divDadosBancarios', this.workspace).hide();
			$('#divComboBanco', this.workspace).show();
		}    
		else{
			$('#divComboBanco', this.workspace).hide();
			$('#divDadosBancarios', this.workspace).hide();
		}	
	},
	
	obterParametrosDistribuidor : function (op) {
		
		this.opcaoPagto(op);
		
		var isOpBoletoEmBranco = parametroCobrancaCotaController.opcaoPagtoBoletoEmBranco(op=='BOLETO_EM_BRANCO');
		
		if(isOpBoletoEmBranco){
		    
			return;
		}
		
		var data = [{name: 'op', value: op}];
		
		$.postJSON(contextPath + "/cota/parametroCobrancaCota/obterParametroCobrancaDistribuidor",
				   data,
				   parametroCobrancaCotaController.sucessCallbackParametroCobrancaDistribuidor, 
				   null,
				   true);
	},
	
	sucessCallbackParametroCobrancaDistribuidor : function(result) {
		parametroCobrancaCotaController.sucessCallbackFormaCobranca(result);
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

		parametroCobrancaCotaController.numeroCota = resultado.numCota;
	
		$("#fatorVencimento", this.workspace).val(resultado.fatorVencimento);
		
		$("#sugereSuspensaoCota", this.workspace).val(resultado.sugereSuspensao);
		$("#sugereSuspensaoCota").attr("checked", resultado.sugereSuspensao);
		
		$("#sugereSuspensaoDistribuidor", this.workspace).val(resultado.sugereSuspensaoDistribuidor);
		$("#sugereSuspensaoDistribuidor").attr("checked", resultado.sugereSuspensaoDistribuidor);
		
		parametroCobrancaCotaController.exibe_form_suspencao(resultado.sugereSuspensao);

		parametroCobrancaCotaController.exibe_form_suspencao_distribuidor(resultado.sugereSuspensaoDistribuidor);
		
		$("#contrato", this.workspace).val(resultado.contrato);
		$("#contrato").attr("checked", resultado.contrato);

		parametroCobrancaCotaController.exibe_form_contrato(resultado.contrato);
		
		$("#valorMinimo", this.workspace).val( resultado.valorMinimo );
		$("#qtdDividasAbertoCota", this.workspace).val(resultado.qtdDividasAberto);
		$("#vrDividasAbertoCota", this.workspace).val( resultado.vrDividasAberto );
		
		$("#devolveEncalhe", this.workspace).val(resultado.devolveEncalhe ? 0 : 1);
		if(resultado.situacaoCadastro != 'PENDENTE') {
			$("#devolveEncalhe", this.workspace).attr('disabled', 'disabled');
        }
		
		//$("#unificaCobranca", this.workspace).val(resultado.unificaCobranca?0:1);

        if (resultado.inicioContrato) {
            $("#parametroCobrancaDateInicio", this.workspace).val(resultado.inicioContrato.$);
        }
        if (resultado.terminoContrato) {
            $("#parametroCobrancaDateTermino", this.workspace).val(resultado.terminoContrato.$);
        }

        if($("#parametroCobrancaDateTermino", this.workspace).val() == "")
        	parametroCobrancaCotaController.calcularDataTermino();
        
        if (parametroCobrancaCotaController.isModoTelaCadastroCota()) {
            parametroCobrancaCotaController.carregarFornecedoresRelacionados();
        }
        
        parametroCobrancaCotaController.carregarFornecedoresPadrao(resultado.idFornecedor);
        
        parametroCobrancaCotaController.carregarArquivoContrato();

        parametroCobrancaCotaController.financeiro.parametroDistribuidor = resultado.parametroDistribuidor;
        
        parametroCobrancaCotaController.financeiro.resultParametroCobranca = parametroCobrancaCotaController.buildParametroCobrancaDto();
        
        parametroCobrancaCotaController.financeiro.resultParametroCobranca['parametroCobranca.idFornecedor'] = resultado.idFornecedor;     
        
        parametroCobrancaCotaController.exibeDevolveEncalhe(resultado.tipoCota, resultado.devolveEncalhe);
        
	},

	buildParametroCobrancaDto : function() {
		
		// hidden
		var idParametroCobranca = $("#_idParametroCobranca", this.workspace).val();
		var idCota = $("#_idCota", this.workspace).val();
		var numCota = $("#_numCota", this.workspace).val();
		
		var fatorVencimento  = $("#fatorVencimento", this.workspace).val();
		
		$("#sugereSuspensaoCota", this.workspace).val(0);
		//if (document.formFinanceiro.sugereSuspensaoCota.checked){
		if ($("#sugereSuspensaoCota", this.workspace).is(":checked")) {
			$("#sugereSuspensaoCota", this.workspace).val(1);
		}
		var sugereSuspensao = $("#sugereSuspensaoCota", this.workspace).val();
		
		$("#sugereSuspensaoDistribuidor", this.workspace).val(0);
		//if (document.formFinanceiro.sugereSuspensaoDistribuidor.checked){
		if ($("#sugereSuspensaoDistribuidor", this.workspace).is(":checked")) {
			$("#sugereSuspensaoDistribuidor", this.workspace).val(1);
		}
		var sugereSuspensaoDistribuidor = $("#sugereSuspensaoDistribuidor", this.workspace).val();
		
		$("#contrato", this.workspace).val(0);
		//if (document.formFinanceiro.contrato.checked){
		if ($("#contrato", this.workspace).is(":checked")) {
			$("#contrato", this.workspace).val(1);
		}
		var contrato = $("#contrato", this.workspace).val();
		var inicioContrato = $("#parametroCobrancaDateInicio", this.workspace).val();
        var terminoContrato = $("#parametroCobrancaDateTermino", this.workspace).val();
	 
		var valorMinimo = $("#valorMinimo", this.workspace).val();

		var qtdDividasAberto = $("#qtdDividasAbertoCota", this.workspace).val();
		var vrDividasAberto = $("#vrDividasAbertoCota", this.workspace).val();
		var devolveEncalhe = $("#devolveEncalhe", this.workspace).val() == 0 ? 1 : 0;
		var fornecedorPadrao = $("#fornecedorPadrao", this.workspace).val();
		//var unificaCobranca = $("#unificaCobranca", this.workspace).val() == 0 ? 1 : 0;
		
		var params = {"parametroCobranca.idParametroCobranca": idParametroCobranca,
				"parametroCobranca.idCota": idCota,
				"parametroCobranca.numCota": numCota,   
				"parametroCobranca.fatorVencimento": fatorVencimento,   
				"parametroCobranca.sugereSuspensao": sugereSuspensao,   
				"parametroCobranca.sugereSuspensaoDistribuidor": sugereSuspensaoDistribuidor,
				"parametroCobranca.contrato": contrato,
				"parametroCobranca.inicioContrato": inicioContrato,
				"parametroCobranca.terminoContrato": terminoContrato,
				"parametroCobranca.valorMinimo": valorMinimo,   
				"parametroCobranca.qtdDividasAberto": qtdDividasAberto,  
				"parametroCobranca.vrDividasAberto": vrDividasAberto,
				"parametroCobranca.devolveEncalhe": devolveEncalhe,
				"parametroCobranca.idFornecedor": fornecedorPadrao};
		
		return params;
	},

	isAlterFormaCobranca : function(formaCobrancaTela) {
		
    	var formaCobranca = parametroCobrancaCotaController.financeiro.resultFormaCobranca;
    	
    	if(formaCobrancaTela['formaCobranca.agencia'] != formaCobranca['formaCobranca.agencia']) {return true;}
    	if(formaCobrancaTela['formaCobranca.agenciaDigito'] != formaCobranca['formaCobranca.agenciaDigito']){return true;}
    	if(formaCobrancaTela['formaCobranca.conta'] != formaCobranca['formaCobranca.conta']){return true;}
    	if(formaCobrancaTela['formaCobranca.contaDigito'] != formaCobranca['formaCobranca.contaDigito']){return true;}
    	if(formaCobrancaTela['formaCobranca.diaDoMes'] != formaCobranca['formaCobranca.diaDoMes']){return true;}
		if(formaCobrancaTela['formaCobranca.domingo'] != formaCobranca['formaCobranca.domingo']){return true;}
		if(formaCobrancaTela['formaCobranca.idBanco'] != formaCobranca['formaCobranca.idBanco']){return true;}
		if(formaCobrancaTela['formaCobranca.idCota'] != formaCobranca['formaCobranca.idCota']){return true;}
		if(formaCobrancaTela['formaCobranca.idParametroCobranca'] != formaCobranca['formaCobranca.idParametroCobranca']){return true;}
		if(formaCobrancaTela['formaCobranca.nomeBanco'] != formaCobranca['formaCobranca.nomeBanco']){return true;}
		if(formaCobrancaTela['formaCobranca.numBanco'] != formaCobranca['formaCobranca.numBanco']){return true;}
		if(formaCobrancaTela['formaCobranca.primeiroDiaQuinzenal'] != formaCobranca['formaCobranca.primeiroDiaQuinzenal']){return true;}
		if(formaCobrancaTela['formaCobranca.quarta'] != formaCobranca['formaCobranca.quarta']){return true;}
		if(formaCobrancaTela['formaCobranca.quinta'] != formaCobranca['formaCobranca.quinta']){return true;}
		if(formaCobrancaTela['formaCobranca.recebeEmail'] != formaCobranca['formaCobranca.recebeEmail']){return true;}
		if(formaCobrancaTela['formaCobranca.sabado'] != formaCobranca['formaCobranca.sabado']){return true;}
		if(formaCobrancaTela['formaCobranca.segunda'] != formaCobranca['formaCobranca.segunda']){return true;}
		if(formaCobrancaTela['formaCobranca.segundoDiaQuinzenal'] != formaCobranca['formaCobranca.segundoDiaQuinzenal']){return true;}
		if(formaCobrancaTela['formaCobranca.sexta'] != formaCobranca['formaCobranca.sexta']){return true;}
		if(formaCobrancaTela['formaCobranca.terca'] != formaCobranca['formaCobranca.terca']){return true;}
		if(formaCobrancaTela['formaCobranca.tipoCobranca'] != formaCobranca['formaCobranca.tipoCobranca']){return true;}
    	
    	return false;
    },
	
	isAlterParametroCobranca : function(parametroCobrancaTela) {
		
		var parametroCobranca = parametroCobrancaCotaController.financeiro.resultParametroCobranca;

    	if(parametroCobrancaTela['parametroCobranca.fatorVencimento'] != parametroCobranca['parametroCobranca.fatorVencimento']){return true;}
    	if(parametroCobrancaTela['parametroCobranca.idCota'] != parametroCobranca['parametroCobranca.idCota']){return true;}
    	if(parametroCobrancaTela['parametroCobranca.idFornecedor'] != parametroCobranca['parametroCobranca.idFornecedor']){return true;}
    	if(parametroCobrancaTela['parametroCobranca.idParametroCobranca'] != parametroCobranca['parametroCobranca.idParametroCobranca']){return true;}
	    if(parametroCobrancaTela['parametroCobranca.numCota'] != parametroCobranca['parametroCobranca.numCota']){return true;}

		return false;
	},
	
	isAlterFornecedoresUnificacao : function(fornecedoresUnificacao){
		
		var fornecedores = parametroCobrancaCotaController.financeiro.fornecedoresUnificacao;
		
		if (fornecedores.length != fornecedoresUnificacao.length){
	
			return true;
        }
		
		for (var i=0; i<fornecedores.length;i++){
			
			if (fornecedores[i]!=fornecedoresUnificacao[i]){
				
				return true;
			}
		}
		
		return false;
	},
	
	isAlter : function(parametroCobrancaTela) {
		
		if (parametroCobrancaCotaController.isAlterParametroCobranca(parametroCobrancaTela)
				|| parametroCobrancaCotaController.financeiro.formaCobrancaAlterada) {
			
			return true;
		}

		return false;
	},
	
	salvarContratoECaracteristicasFinanceirasEspecificasCota : function(){
		
		var params = parametroCobrancaCotaController.buildFormaCobrancaDTO();
        
		$.postJSON(contextPath + "/cota/parametroCobrancaCota/salvarContratoECaracteristicasFinanceirasEspecificasCota",
				   params,
				   function(mensagens) {
			
			        	   telaMensagem=null;
			        	   
			        	   if (mensagens){
			        		   
							   var tipoMensagem = mensagens.tipoMensagem;
							   
							   var listaMensagens = mensagens.listaMensagens;
							   
							   if (tipoMensagem && listaMensagens) {
								   
							       exibirMensagem(tipoMensagem, listaMensagens);
						       }
			        	   }	   
	               },
				   null,
				   true,
				   "idModalUnificacao");
	},

	postarParametroCobranca : function() {
		
		var params = parametroCobrancaCotaController.buildParametroCobrancaDto();
		
		var parametroCobrancaAlterado = parametroCobrancaCotaController.isAlterParametroCobranca(params);
		
		params["parametroCobrancaAlterado"] = parametroCobrancaAlterado;
		
	    if (parametroCobrancaCotaController.financeiro.parametroDistribuidor) {

		   if (parametroCobrancaCotaController.isAlter(params)){
			   
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
									   parametroCobrancaCotaController.financeiro.parametroDistribuidor = false;
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
		   else{
				
		       parametroCobrancaCotaController.salvarContratoECaracteristicasFinanceirasEspecificasCota();
		   }
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
		$("#parametro-cobranca-banco", this.workspace).val("");
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

		var idCota = parametroCobrancaCotaController.idCota;
		var numeroCota = parametroCobrancaCotaController.numeroCota;
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
			
			document.getElementById("fornecedor_"+$(this).val(), parametroCobrancaCotaController.workspace).checked = false;
		});
		
		var i;
		
		for(i=0;i<unificados.length;i++){
			
			if(document.getElementById('fornecedor_'+unificados[i])){
				
			    document.getElementById("fornecedor_"+unificados[i], parametroCobrancaCotaController.workspace).checked = true;
			}
		}
	},
	
	obterFormaCobranca : function(idFormaCobranca){
		
		//hidden
		$("#_idFormaCobranca", this.workspace).val(idFormaCobranca);
		
		var data = [{name: 'idFormaCobranca', value: idFormaCobranca}, 
		            {name: 'modoTela', value: parametroCobrancaCotaController.modoTela.value }];
		
		$.postJSON(contextPath + "/cota/parametroCobrancaCota/obterFormaCobranca",
				   data,
				   parametroCobrancaCotaController.sucessCallbackFormaCobranca, 
				   null,
				   true);
	},

	sucessCallbackFormaCobranca : function(resultado) {
		
		parametroCobrancaCotaController.carregarComboTipoCobranca(resultado.tipoCobranca);
		
        $("#tipoFormaCobranca", this.workspace).val(resultado.tipoFormaCobranca);
        
		if (parametroCobrancaCotaController.isModoTelaCadastroCota()) {
			
            parametroCobrancaCotaController.carregarBancos(resultado.idBanco);       
        } 
		else {
        	
            if (resultado.numBanco) {
            	
                var descricaoBanco = resultado.numBanco;
                
                descricaoBanco += '-'  + resultado.nomeBanco;
                
                descricaoBanco += '-' + resultado.conta;
                
                if (resultado.contaDigito) {
                	
                    descricaoBanco+= '-' + resultado.contaDigito;
                }
                
                montarComboBoxUnicaOpcao("",
                    descricaoBanco,$("#parametro-cobranca-banco", this.workspace));
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

		parametroCobrancaCotaController.financeiro.fornecedoresUnificacao = parametroCobrancaCotaController.obterFornecedoresMarcados();
		
		parametroCobrancaCotaController.financeiro.resultFormaCobranca = parametroCobrancaCotaController.buildFormaCobrancaDTO();	
		
		parametroCobrancaCotaController.financeiro.resultFormaCobranca['formaCobranca.tipoCobranca'] = resultado.tipoCobranca;
		
		parametroCobrancaCotaController.financeiro.resultFormaCobranca['formaCobranca.idBanco'] = resultado.idBanco;
	},

    carregarBancos : function(selected){
        carregarCombo(contextPath + "/cota/parametroCobrancaCota/carregarBancos", null,
            $("#parametro-cobranca-banco", this.workspace), selected, null);
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
		var idBanco             = $("#parametro-cobranca-banco", this.workspace).val();
		var numBanco            = $("#numBanco", this.workspace).val();
		var nomeBanco           = $("#nomeBanco", this.workspace).val();
		var agencia             = $("#agencia", this.workspace).val();
		var agenciaDigito       = $("#agenciaDigito", this.workspace).val();
		var conta               = $("#conta", this.workspace).val();
		var contaDigito         = $("#contaDigito", this.workspace).val();
		var diaDoMes            = $("#diaDoMesCota", this.workspace).val();
		var primeiroDiaQuinzenal= $("#primeiroDiaQuinzenalParametroCobrancaCota", this.workspace).val();
		var segundoDiaQuinzenal = $("#segundoDiaQuinzenalParametroCobrancaCota", this.workspace).val();
		
		var valorMinimo			= $("#valorMinimo", this.workspace).val();
		var fatorVencimento		= $("#fatorVencimento", this.workspace).val();
		var devolveEncalhe		= $("#devolveEncalhe", this.workspace).val() == 0 ? 1 : 0;
		var fornecedorPadrao 	= $("#fornecedorPadrao", this.workspace).val();
		//var unificaCobranca 	= $("#unificaCobranca", this.workspace).val() == 0 ? 1 : 0;
		var sugereSuspensao 	= $("#sugereSuspensaoCota", this.workspace).val();
		var sugereSuspensaoDistribuidor	= $("#sugereSuspensaoDistribuidor", this.workspace).val();
		var contrato			= $("#contrato", this.workspace).val();
		var inicioContrato 		= $("#parametroCobrancaDateInicio", this.workspace).val();
        var terminoContrato 	= $("#parametroCobrancaDateTermino", this.workspace).val();
		var qtdDividasAberto	= $("#qtdDividasAbertoCota", this.workspace).val();
		var vrDividasAberto		= $("#vrDividasAbertoCota", this.workspace).val();
			
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
						 "parametroCobranca.devolveEncalhe": devolveEncalhe,
						 "parametroCobranca.idFornecedor": fornecedorPadrao,
						 //"parametroCobranca.unificaCobranca": unificaCobranca,
						 "parametroCobranca.sugereSuspensao": sugereSuspensao,   
						 "parametroCobranca.sugereSuspensaoDistribuidor": sugereSuspensaoDistribuidor,   
						 "parametroCobranca.qtdDividasAberto": qtdDividasAberto,  
						 "parametroCobranca.vrDividasAberto": vrDividasAberto,
						 "parametroCobranca.contrato": contrato,
						 "parametroCobranca.inicioContrato": inicioContrato,
						 "parametroCobranca.terminoContrato": terminoContrato};
		
		params = serializeArrayToPost('listaIdsFornecedores', parametroCobrancaCotaController.obterFornecedoresMarcados(), params );
 
		return params;
	},
	
	postarFormaCobranca : function(novo, incluirSemFechar) {
		
		
		var telaMensagem="idModalUnificacao",
			idFormaCobranca = $("#_idFormaCobranca", this.workspace).val(),
			idCota = $("#_idCota", this.workspace).val(),
			idParametroCobranca = $("#_idParametroCobranca", this.workspace).val(),
			params = {};
		
		params = parametroCobrancaCotaController.buildFormaCobrancaDTO();
		
		
		if ((parametroCobrancaCotaController.financeiro.resultFormaCobranca != null) && (parametroCobrancaCotaController.financeiro.parametroDistribuidor)){
			
			parametroCobrancaCotaController.financeiro.formaCobrancaAlterada = parametroCobrancaCotaController.financeiro.formaCobrancaAlterada
				|| (parametroCobrancaCotaController.isAlterFormaCobranca(params)
	     			   || parametroCobrancaCotaController.isAlterFornecedoresUnificacao(parametroCobrancaCotaController.obterFornecedoresMarcados()) );
			
			if (!parametroCobrancaCotaController.financeiro.formaCobrancaAlterada){

				return;
			}
		}
		

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
				           
				           parametroCobrancaCotaController.financeiro.formaCobrancaAlterada = true;
		               },
					   null,
					   true,
					   telaMensagem);
		} else {
			params["formaCobranca.idFormaCobranca"] = idFormaCobranca;
			$.postJSON(contextPath + "/cota/parametroCobrancaCota/postarFormaCobranca",params,
					   function(mensagens) {
				           $("#dialog-unificacao", this.workspace).dialog("close");
				           if (incluirSemFechar) {
				        	   
				        	   parametroCobrancaCotaController.popup_nova_unificacao();
				        	   
				           } else {
				        	   telaMensagem = null;
				        	   if (mensagens) {
								   var tipoMensagem = mensagens.tipoMensagem;
								   var listaMensagens = mensagens.listaMensagens;
								   if (tipoMensagem && listaMensagens) {
								       exibirMensagem(tipoMensagem, listaMensagens);
							       }
				        	   }
				           }
				           
				           parametroCobrancaCotaController.mostrarGrid(idCota);		
				           
				           parametroCobrancaCotaController.financeiro.formaCobrancaAlterada = true;
		               },
					   null,
					   true,
					   telaMensagem);
	    };
	    
	    MANTER_COTA._indCadastroCotaAlterado = false;
	},
	
	excluirFormaCobranca : function(idFormaCobranca){
		var idCota = $("#_idCota", this.workspace).val();
		
		
		var data = [{name: 'idFormaCobranca', value: idFormaCobranca}];
		$.postJSON(contextPath + "/cota/parametroCobrancaCota/excluirFormaCobranca",
				   data,
				   function(){
					parametroCobrancaCotaController.obterParametroCobranca(idCota);
			           parametroCobrancaCotaController.mostrarGrid(idCota);
				   },
				   null,
				   true);
		
		parametroCobrancaCotaController.obterParametroCobranca(idCota);
		
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
	incluirNovaUnificacao : function() {
		parametroCobrancaCotaController.postarFormaCobranca(false,true);
	},
	
	exibeDevolveEncalhe : function(val, devolveEncalhe) {
		
		if (val == 'A_VISTA') {
		    
			$('#tituloDevolveEncalhe', this.workspace).show();
			
			$('#selectDevolveEncalhe', this.workspace).val(devolveEncalhe ? 1 : 0);
			$('#selectDevolveEncalhe', this.workspace).show();
		} else {
			
			$('#devolveEncalhe', this.workspace).val(0);
			
			$('#tituloDevolveEncalhe', this.workspace).hide();
			
			$('#selectDevolveEncalhe', this.workspace).hide();
		}
	}
	
}, BaseController);
//@ sourceURL=parametroCobrancaCota.js
