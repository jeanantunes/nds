//#workspace div.ui-tabs-panel:not(.ui-tabs-hide)
var produtoController = $.extend(true, {
	
	pesquisaProduto:null,
	
	inicializar : function (pesquisaProduto) {
		
		var valorComboGerAuto = null;
		
		produtoController.aplicarMascaras();
		
		produtoController.pesquisaProduto = pesquisaProduto;
		
		this.iniciarGrid();
		$( "#tabProduto", this.workspace).tabs();
		
		$(".bt_arq", this.workspace).hide();

		$("#produto-comboGeracaoAutomatica", produtoController.workspace).val(-1).enable();
		this.tamanhoInicial = 3;
		
		$('#produto-codigoProduto', produtoController.workspace).change(function (){
			produtoController.pesquisarPorCodigoProduto('#produto-codigoProduto', '#produto-produto', '#produto-comboGeracaoAutomatica', false);
		});
		
		$('#nomeProduto', produtoController.workspace).bind({
			keyup: function(){
				this.value = this.value.toUpperCase();
			},
			focus: function(){
				produtoController.atualizaICD();
			}
		});
		
		$("#produto-produto",produtoController.workspace).autocomplete({
			source:function(param ,callback) {
				$.postJSON(contextPath + "/produto/autoCompletarPorNomeProduto", { 'nomeProduto': param.term }, callback);
			},
			select : function(event, ui) {
				$('#produto-codigoProduto',produtoController.workspace).val(ui.item.chave.codigo);
				
			},
			minLength: 2,
			delay : 0,
		}).keyup(function(){
			this.value = this.value.toUpperCase();
		});
	},

	aplicarMascaras : function () {
		$("#produto-peb", this.workspace).numeric();
		$("#produto-pacotePadrao", this.workspace).numeric(
		    {
		    	decimal : false,
		    	negative : false
		    }
		);
		
		$("#produto-percentualDesconto", produtoController.workspace).mask("999,99");
		$("#codigoProdutoCadastro", produtoController.workspace).mask("?99999999");
	},

	buscarValueRadio:function(radioName) {

		var valueRadio = new Array();

		$("input[type=radio][name='"+radioName+"']:checked", this.workspace).each(function() {
			valueRadio.push($(this).val());
		});

		return valueRadio;
	},
	
	buscarValueCheckBox:function(checkName) {
		return $("#"+checkName).is(":checked", this.workspace);
	},
	
	
	//Pesquisa por código de produto
	pesquisarPorCodigoProduto : function(idCodigo, idProduto, idGeracao, isFromModal) {
		
		var codigoProduto = $(idCodigo, produtoController.workspace).attr("value");
		
		if (codigoProduto != ""){

			codigoProduto = $.trim(codigoProduto);
			
			$(idCodigo, produtoController.workspace).val(codigoProduto);
			
			$(idProduto, produtoController.workspace).val("");
			$(idGeracao, produtoController.workspace).val(-1);
			
			if (codigoProduto && codigoProduto.length > 0) {
				
				$.postJSON(contextPath + "/produto/pesquisarPorCodigoProduto",
						{codigoProduto:codigoProduto},
						function(result) { produtoController.successCallBack(result, idProduto, idGeracao); },
						function() { produtoController.errorCallBack(idCodigo); }, isFromModal);
				
			} 
			
		}else{
			produtoController.limparCamposFiltro();
		}
	},
	
	//Auto complete por nome do produto
	autoCompletarPorNomeProduto : function(idProduto, idCodProduto, isFromModal, successCallBack) {
		
		var nomeProduto = $(idProduto, produtoController.workspace).attr("value");
		
		var idGeracao = "#produto-comboGeracaoAutomatica";
		
		if (nomeProduto != ""){
			if (nomeProduto && nomeProduto.length > 1) {
				$.postJSON(contextPath + "/produto/autoCompletarPorNomeProduto", {nomeProduto:nomeProduto},
						   function(result) { produtoController.exibirAutoCompletePorNome(result, idCodProduto, idProduto, idGeracao, successCallBack); },
						   null, isFromModal);
			}
		}else{
			produtoController.limparCamposFiltro();
		}
	},
	
	limparCamposFiltro : function (){
		$("#produto-produto", produtoController.workspace).val("");
		$("#produto-codigoProduto", produtoController.workspace).val("");
		$("#produto-comboGeracaoAutomatica", produtoController.workspace).val(-1).enable();
	},
	
	
	//Exibe o auto complete no campo
	exibirAutoCompletePorNome : function(result, idCampoCodigo, idCampoNome, idGeracao, successCallBack) {
	
			$(idCampoNome, produtoController.workspace).autocomplete({
				source: result,
				
				
				select : function(event, ui) {
					
					$(idCampoCodigo, produtoController.workspace).val(ui.item.chave.codigo);
					$(idCampoNome, produtoController.workspace).val(ui.item.value);
					$(idGeracao, produtoController.workspace).val(produtoController.formatarValorParaPopularCombo(result.isGeracaoAutomatica)).disable();
					
					produtoController.pesquisarPorCodigoProduto(idCampoCodigo, idCampoNome, idGeracao, false);
				},
				
				minLength: produtoController.tamanhoInicial,
				delay : 0,
			});
	},
	
	
	successCallBack : function(result, idProduto, idGeracao, idCodigo, isFromModal) {
		
		$(idProduto, produtoController.workspace).val(result.nome);
		$(idGeracao, produtoController.workspace).val(produtoController.formatarValorParaPopularCombo(result.isGeracaoAutomatica)).disable();
		$(idCodigo, produtoController.workspace).val(result.id);
		
		$("#produto-fornecedor", produtoController.workspace).focus();
		
	},
	
	formatarValorParaPopularCombo : function (valor){
		
		var valorFormatado;
		
		if(valor == true){
			valorFormatado = 0;
		}else{
			valorFormatado = 1;
		}
		return valorFormatado;
	},
	
	
	errorCallBack : function(idCodigo) {
		produtoController.limparCamposFiltro();
		$(idCodigo, produtoController.workspace).focus();
	},
	
	//Pesquisar Fornecedor
	pesquisarProdutosSuccessCallBack:function() {
		
		produtoController.pesquisarFornecedor(produtoController.getCodigoProdutoPesquisa());

	},
	
	getCodigoProdutoPesquisa: function () {
		return  {codigoProduto:$("#produto-codigoProduto", this.workspace).val()};
	},
	
	pesquisarFornecedor:function(data){
	
		$.postJSON(contextPath + "/produto/pesquisarFornecedorProduto",
				   data, this.montarComboFornecedores);
	},

	//Mostrar auto complete por nome do fornecedor
	autoCompletarPorNomeFornecedor : function(idFornecedor, isFromModal) {
		
		produtoController.pesquisaProduto.pesquisaRealizada = false;
		
		var nomeFornecedor = $(idFornecedor, this.workspace).val();
		
		if (nomeFornecedor && nomeFornecedor.length > 2) {
			$.postJSON(contextPath + "/produto/autoCompletarPorNomeFornecedor", {nomeFornecedor:nomeFornecedor},
					   function(result) { produtoController.pesquisaProduto.exibirAutoComplete(result, idFornecedor); },
					   null, isFromModal);
		}
	},

	montarComboFornecedores:function(result) {
		var comboFornecedores =  montarComboBox(result, true);
		
		$("#produto-fornecedor", this.workspace).html(comboFornecedores);
	},

	iniciarGrid : function() {
		$(".produtosGrid", this.workspace).flexigrid({
			preProcess: produtoController.executarPreProcessamento,
			dataType : 'json',
			colModel : [ {
				display : 'C&oacute;digo',
				name : 'codigo',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produtoDescricao',
				width : 170,
				sortable : true,
				align : 'left'
			}, {
				display : 'Tipo Produto',
				name : 'tipoProdutoDescricao',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Editor',
				name : 'nomeEditor',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : 'Fornecedor',
				name : 'tipoContratoFornecedor',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'PEB',
				name : 'peb',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Pcte. Padr&atilde;o',
				name : 'pacotePadrao',
				width : 65,
				sortable : true,
				align : 'center'
			}, {
				display : 'Desconto %',
				name : 'percentualDesconto',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Periodicidade',
				name : 'periodicidade',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 52,
				sortable : false,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 'auto',
			singleSelect : true
		});
	},			

	popularCombo : function(data, combo) {
		opcoes = "";
		selecionado = "-1";
		$.each(data, function(i,n){
			if (n["checked"]) {
				opcoes+="<option value="+n["value"]+" selected='selected'>"+n["label"]+"</option>";
				selecionado = n["value"];
			} else {
				opcoes+="<option value="+n["value"]+">"+n["label"]+"</option>";
			}
		});
		$(combo).clear().append(opcoes);
		$(combo).val(selecionado);
	},
	
	pesquisar : function() {
		
		var codigo = $("#produto-codigoProduto", this.workspace).val();
		var produto = $("#produto-produto", this.workspace).val();
		var fornecedor = $("#produto-fornecedor", this.workspace).val();
		var editor = $("#produto-edicao", this.workspace).val();
		var codigoTipoProduto = $("#produto-comboTipoProduto", this.workspace).val();
		var isGeracaoAutomatica = produtoController.formatarCampoComboGeracaoAutomatica("#produto-comboGeracaoAutomatica");
		
		
		$(".produtosGrid", this.workspace).flexOptions({
			url: contextPath + "/produto/pesquisarProdutos",
			params: [{name:'codigo', value: codigo },
				     {name:'produto', value: produto },
				     {name:'fornecedor', value: fornecedor },
				     {name:'editor', value: editor },
				     {name:'codigoTipoProduto', value : codigoTipoProduto},
				     {name:'isGeracaoAutomatica', value : isGeracaoAutomatica}],
			newp: 1,
		});
		
		$(".produtosGrid", this.workspace).flexReload();
	},
	
	editarProduto : function(id) {
		
		$("td[name='tdCodigoProdutoICDCadastro']", produtoController.workspace).show();
		

		$("#dialog-novo", this.workspace).dialog({
			resizable: false,
			height:550,
			width:850,
			modal: true,
			title:"Edição de Produto",
			buttons: {
				"Confirmar": function() {
					produtoController.salvarProduto();
					
					produtoController.atualizarValorComboGeraAutomatica();
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			beforeClose: function() {
				produtoController.limparModalCadastro();
				clearMessageDialogTimeout('dialogMensagemNovo');
			},
			form: $("#dialog-novo", this.workspace).parents("form")
		});
		
		this.carregarNovoProduto(
			function() {
				produtoController.limparModalCadastro();
				produtoController.carregarProdutoEditado(id);		
			}
		);
		
		$("#codigoProdutoCadastro", this.workspace).disable();
	},
	
	atualizarValorComboGeraAutomatica : function(){
	
		var valorAtual;
		
		if(($("#produto-produto").val() != "") && ($("#produto-codigoProduto").val() != "")){
			if(produtoController.valorComboGerAuto != null){
				
				if(produtoController.valorComboGerAuto == true){
					valorAtual = 0;
				}else{
					valorAtual = 1;
				}
				$("#produto-comboGeracaoAutomatica").val(valorAtual).disable();
			}
		}
		
		
		
	},
	
habilitarDesabilitarCamposInterfaceEditor : function(habilitar) {
		
		$(".habilitarCampoInterfaceEditor", produtoController.workspace).attr('disabled',!habilitar);
		
		
	},
	
	
	habilitarDesabilitarCamposInterface : function(habilitar) {
		
		$(".habilitarCampoInterface", produtoController.workspace).attr('disabled',!habilitar);
		
		$(".habilitarCampoInterfaceSegmentacao", produtoController.workspace).attr('disabled',!habilitar);
	},
	
	carregarProdutoEditado : function(id) {

		$.postJSON(contextPath + "/produto/carregarProdutoParaEdicao", 
				   	{id:id},
				   	function(result) {
			   
						$("#idProduto", produtoController.workspace).val(result.id);
						$("#codigoProdutoCadastro", produtoController.workspace).val(result.codigo);
						$("#codigoProdutoICDCadastro", produtoController.workspace).val(result.codigoICD);
						$("#nomeProduto", produtoController.workspace).val(result.nome);
						$("#sloganProduto", produtoController.workspace).val(result.slogan);
						$("#produto-peb", produtoController.workspace).val(result.peb);
						$("#produto-pacotePadrao", produtoController.workspace).val(result.pacotePadrao);
						$("#comboPeriodicidade", produtoController.workspace).val(result.periodicidade);
						$("#comboEditor", produtoController.workspace).val(result.codigoEditor);
						$("#selGeracaoAuto", produtoController.workspace).attr('checked', result.isGeracaoAutomatica);
						$("#selSemCeIntegracao", produtoController.workspace).attr('checked', result.isSemCeIntegracao);
						$("#comboTipoSegmento", produtoController.workspace).val(result.idTipoSegmentoProduto);
						$("#comboClassifProd", produtoController.workspace).val(result.idTipoClassifProduto);
						$("#selNotaFiscal", produtoController.workspace).attr('checked', result.notaFiscal);
						
						produtoController.habilitarDesabilitarCamposInterface(!(result.origem == "INTERFACE"));
						
						produtoController.habilitarDesabilitarCamposInterfaceEditor(!(result.origem == "INTERFACE") || result.codigoEditor == 0 );
						
						$("#comboFornecedoresCadastro", produtoController.workspace).val(result.codigoFornecedor).disable();
						$("#comboTipoProdutoCadastro", produtoController.workspace).val(result.codigoTipoProduto);
						$("#segmentacaoClasseSocial", produtoController.workspace).val(result.classeSocial);
						$("#segmentacaoSexo", produtoController.workspace).val(result.sexo);
						$("#segmentacaoFaixaEtaria", produtoController.workspace).val(result.faixaEtaria);
						$("#segmentacaoFormato", produtoController.workspace).val(result.formatoProduto);
						$("#segmentacaoFormaFisica", produtoController.workspace).val(result.formaFisica);//ainda nao carrega
						
						$("#produto-percentualDesconto", produtoController.workspace).val($.formatNumber(result.desconto, {format:"###,##000.00", locale:"br"}));

						if (result.formaComercializacao == 'CONTA_FIRME') {
							$("#formaComercializacaoContaFirme", this.workspace).attr('checked', true);
						} else if (result.formaComercializacao == 'CONSIGNADO') {
							$("#formaComercializacaoConsignado", this.workspace).attr('checked', true);
						}
						
						if (result.tributacaoFiscal == 'TRIBUTADO') {
							$("#radioTributado", this.workspace).attr('checked', true);
						} else if (result.tributacaoFiscal == 'ISENTO') {
							$("#radioIsento", this.workspace).attr('checked', true);
						} else if (result.tributacaoFiscal == 'OUTROS') {
							$("#radioTributacaoOutros", this.workspace).attr('checked', true);
						}
						
						if (!(result.origem == "INTERFACE")){
							
							produtoController.carregarComboDesconto("MANUAL",result.idDesconto);
							$("#comboTipoDesconto", produtoController.workspace).hide();
							$("#tipoDescontoManual", produtoController.workspace).show();
							$("#tipoDescontoManual", produtoController.workspace).val(result.descricaoDescontoManual);
							$("#produto-percentualDesconto", produtoController.workspace).removeAttr('disabled','disabled');
						}
						else{
							
							produtoController.carregarComboDesconto("INTERFACE",result.idDesconto);
							$("#produto-percentualDesconto", produtoController.workspace).attr('disabled','disabled');
							$("#comboTipoDesconto", produtoController.workspace).show();
							$("#tipoDescontoManual", produtoController.workspace).hide();
						}	
					},
					null,
					true
				);
	},
	
	carregarComboDesconto : function(origemProduto, idDesconto){
		
		$.postJSON(contextPath + "/produto/carregarDadosDesconto",
					{origemProduto:origemProduto},
					function (result) {
						produtoController.popularCombo(result, $("#comboTipoDesconto", this.workspace));
						$("#comboTipoDesconto", this.workspace).val(idDesconto);
					},
				  	null,
				   	true
			);
	},
	
	removerProduto : function(id) {

		$("#dialog-excluir", this.workspace).dialog( {
			resizable : false,
			height : 'auto',
			width : 450,
			modal : true,
			buttons : {
				"Confirmar" : function() {

					$("#dialog-excluir", this.workspace).dialog("close");
					
					$.postJSON(contextPath + "/produto/removerProduto", 
							   {id:id},
							   function(result) {
							   		
									var tipoMensagem = result.tipoMensagem;
									var listaMensagens = result.listaMensagens;
									
									if (tipoMensagem && listaMensagens) {
										
										exibirMensagem(tipoMensagem, listaMensagens);
									}

									$(".filtro", this.workspace).each(function() {  
										$("input[type='text'], select", this.workspace).val(""); 
									});
									
									$(".produtosGrid", this.workspace).flexOptions({
										params: null,
										newp: 1
									});
									
									$(".produtosGrid", this.workspace).flexReload();
							   },
							   null,
							   true
					);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			},
			beforeClose: function() {
				clearMessageDialogTimeout('dialogMensagemNovo');
				$("#produto-comboGeracaoAutomatica", produtoController.workspace).val(-1).enable();
			},
			form: $("#dialog-excluir", this.workspace).parents("form")
		});
	},
	
	atualizaICD : function(){
		var value = $("#codigoProdutoCadastro").val();
		console.log(value);
//		var l = value.length;

		$("#codigoProdutoICDCadastro").val(value);
		
//		if(l>=6){
//			$("#codigoProdutoICDCadastro").val(value.substring(0,6));
//		}else{
//			$("#codigoProdutoICDCadastro").val(value.substring(0,(value.length)));
//		}
	},
	
	novoProduto : function () {
		
		produtoController.limparModalCadastro();
		
		$("td[name='tdCodigoProdutoICDCadastro']", produtoController.workspace).hide();
		
		produtoController.trocarOrdemCampos(true);
		
		$("#dialog-novo", this.workspace).dialog({
			resizable: false,
			height:550,
			width:850,
			modal: true,
			title:"Novo Produto",
			buttons: {
				"Confirmar": function() {

					produtoController.salvarProduto();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			beforeClose: function() {
				produtoController.limparModalCadastro();
				clearMessageDialogTimeout('dialogMensagemNovo', this.workspace);
			},
			form: $("#dialog-novo", this.workspace).parents("form")
		});

		produtoController.habilitarDesabilitarCamposInterface(true);
		produtoController.habilitarDesabilitarCamposInterfaceEditor(true);
		
		this.carregarNovoProduto(this.limparModalCadastro);
		
		$("#codigoProdutoCadastro", this.workspace).enable();
		$("#comboTipoDesconto", produtoController.workspace).hide();
		
		$("#tipoDescontoManual", produtoController.workspace).show();
		$("#produto-percentualDesconto", produtoController.workspace).removeAttr('disabled');		
	},

	carregarNovoProduto : function(callback) {

		$.postJSON(contextPath + "/produto/carregarDadosProduto",
					null,
					function (result) {

						produtoController.popularCombo(result[0], $("#comboTipoProdutoCadastro", this.workspace));
						produtoController.popularCombo(result[1], $("#comboFornecedoresCadastro", this.workspace));
						produtoController.popularCombo(result[2], $("#comboEditor", this.workspace));
					
						produtoController.trocarOrdemCampos(false);
						
						if (callback) {
							callback();
						}
					},
				  	null,
				   	true
			);
	},

	limparModalCadastro : function() {

		$("#idProduto", this.workspace).val("");
		$("#codigoProdutoCadastro", this.workspace).val("");
		$("#nomeProduto", this.workspace).val("");
		$("#sloganProduto", this.workspace).val("");
		$("#produto-peb", this.workspace).val("");
		$("#produto-pacotePadrao", this.workspace).val("");
		$("#comboPeriodicidade", this.workspace).val("");
		$("#tipoDescontoManual", this.workspace).val("");
		$("#comboFornecedoresCadastro", produtoController.workspace).val(0).enable();

		$("#formaComercializacaoContaFirme", this.workspace).attr('checked', false);
		$("#formaComercializacaoConsignado", this.workspace).attr('checked', false);
					
		$("#radioTributado", this.workspace).attr('checked', false);
		$("#radioIsento", this.workspace).attr('checked', false);
		$("#radioTributacaoOutros", this.workspace).attr('checked', false);
		
		$("#produto-percentualDesconto", this.workspace).val("");
		
		//Field Público Alvo
		$("#segmentacaoClasseSocial", this.workspace).val("");
		$("#segmentacaoSexo", this.workspace).val("");
		$("#segmentacaoFaixaEtaria", this.workspace).val("");
		$("#segmentacaoFormaFisica", this.workspace).val("");
		$("#segmentacaoFormato", this.workspace).val("");
		$("#comboClassifProd", this.workspace).val("");
		$("#comboTipoSegmento", this.workspace).val("");
		
		$("#selGeracaoAuto", produtoController.workspace).attr('checked', false);
		$("#selSemCeIntegracao", produtoController.workspace).attr('checked', false);
		$("#selNotaFiscal", produtoController.workspace).attr('checked', false);
	},
	
	salvarProduto : function() {

		produtoController.valorComboGerAuto = produtoController.formatarCampoGeracaoAutomatica("#selGeracaoAuto");
		
		var idDesconto = $("#comboTipoDesconto", produtoController.workspace).val();
		
		 var params = [{name:"produto.id",value:$("#idProduto", produtoController.workspace).val()},
        			   {name:"produto.codigo",value:$("#codigoProdutoCadastro", produtoController.workspace).val()},
        			   {name:"produto.codigoICD",value:$("#codigoProdutoICDCadastro", produtoController.workspace).val()},
        			   {name:"produto.nome",value:$("#nomeProduto", produtoController.workspace).val()},
        			   {name:"produto.nomeComercial",value:$("#nomeProduto", produtoController.workspace).val()},
        			   {name:"produto.peb",value:$("#produto-peb", produtoController.workspace).val()},
        			   {name:"produto.pacotePadrao",value:$("#produto-pacotePadrao", produtoController.workspace).val()},
        			   {name:"produto.slogan",value:$("#sloganProduto", produtoController.workspace).val()},
        			   {name:"produto.periodicidade",value:$("#comboPeriodicidade", produtoController.workspace).val()},
        			   {name:"produto.formaComercializacao",value:this.buscarValueRadio('formaComercializacao', produtoController.workspace)},
        			   {name:"produto.tributacaoFiscal",value:this.buscarValueRadio('radioTributacaoFiscal', produtoController.workspace)},
        			   {name:"produto.segmentacao.classeSocial",value:$("#segmentacaoClasseSocial", produtoController.workspace).val()},
        			   {name:"produto.segmentacao.sexo",value:$("#segmentacaoSexo", produtoController.workspace).val()},
        			   {name:"produto.segmentacao.faixaEtaria",value:$("#segmentacaoFaixaEtaria", produtoController.workspace).val()},
        			   {name:"produto.segmentacao.formatoProduto",value:$("#segmentacaoFormato", produtoController.workspace).val()},
        			   {name:"produto.segmentacao.formaFisica",value:$("#segmentacaoFormaFisica", produtoController.workspace).val()},
        			   {name:"codigoEditor",value:$("#comboEditor", produtoController.workspace).val()},
        			   {name:"codigoFornecedor",value:$("#comboFornecedoresCadastro", produtoController.workspace).val()},
        			   {name:"idDesconto",value:idDesconto ? idDesconto : ''},
        			   {name:"codigoTipoProduto",value:$("#comboTipoProdutoCadastro", produtoController.workspace).val()},
        			   {name:"produto.desconto",value:$("#produto-percentualDesconto", produtoController.workspace).val()},
        			   {name:"produto.isGeracaoAutomatica",value:(produtoController.formatarCampoGeracaoAutomatica("#selGeracaoAuto"))},
        			   
        			   {name:"produto.isSemCeIntegracao",value:(produtoController.formatarCampoGeracaoAutomatica("#selSemCeIntegracao"))},
        			   {name:"produto.notaFiscal",value:(produtoController.formatarCampoGeracaoAutomatica("#selNotaFiscal"))},
        			   
        			   {name:"produto.tipoSegmentoProduto.id",value:$("#comboTipoSegmento", produtoController.workspace).val()},
        			   {name:"produto.tipoSegmentoProduto.descricao",value:$("#comboTipoSegmento :checked", produtoController.workspace).text()},
        			 
//        			   {name:"produto.tipoClassificacaoProduto.id",value:$("#comboClassifProd", produtoController.workspace).val()},
//        			   {name:"produto.tipoClassificacaoProduto.descricao",value:$("#comboClassifProd :checked", produtoController.workspace).text()},
        			   
        			   {name:"produto.descricaoDesconto",value:$("#tipoDescontoManual", produtoController.workspace).val()}];
 
		$.postJSON(contextPath + "/produto/salvarProduto",  
			   	params,
			   	function (result) {

			var tipoMensagem = result.tipoMensagem;
					
					var listaMensagens = result.listaMensagens;
					
					if (tipoMensagem && listaMensagens) {
						
						exibirMensagem(tipoMensagem, listaMensagens);
					} 

					if (tipoMensagem == 'SUCCESS') {

						$("#dialog-novo", this.workspace).dialog( "close" );

						//$(".produtosGrid", this.workspace).flexReload();
						produtoController.pesquisar();
					}
					
				},
			  	null,
			   	true,
			   	'dialogMensagemNovo'
		);
	},
	
	executarPreProcessamento : function(resultado) {
		
		if (resultado.mensagens) {

			$(".bt_arq", this.workspace).hide();
			$(".grids", this.workspace).hide();
			
			exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
			);
			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {
			
			if(!row.cell.tipoContratoFornecedor){
				row.cell.tipoContratoFornecedor = '';
			}
			
			var linkAprovar = '<a href="javascript:;"  onclick="produtoController.editarProduto(' + row.cell.id + ');" style="cursor:pointer">' +
					     	  	'<img title="Editar" src="' + contextPath +'/images/ico_editar.gif" style="margin-right:10px" border="0px" />' +
					  		  '</a>';
			
			var linkExcluir = '<a href="javascript:;" isEdicao="true" onclick="produtoController.removerProduto(' + row.cell.id + ');" style="cursor:pointer">' +
							   	 '<img title="Excluir" src="' + contextPath +'/images/ico_excluir.gif" hspace="5" border="0px" />' +
							   '</a>';
			
			row.cell.acao = linkAprovar + linkExcluir;
		});
			
		$(".grids", this.workspace).show();
		
		$(".bt_arq", this.workspace).show();
		
		return resultado;
	},	
	
	carregarTipoDescontoProduto : function(callback) {
		if (callback) {
			callback();
		}
	},
	
	
	formatarCampoGeracaoAutomatica : function (campo){
		
		var valFormatado = '';
		
		if($(campo).attr('checked') == "checked"){
			valFormatado = true;
		}else{
			valFormatado = false;
		}
		return valFormatado;
	},
	
	formatarCampoComboGeracaoAutomatica : function (campo){
		
		var valFormatado = '';
		
		if($(campo).val() == 0){
			valFormatado = true;
		}
		if($(campo).val() == 1){
			valFormatado = false;
		}
		return valFormatado;
	},
	
	proximoCodigoDisponivel : function(comboFornecedor){
		
		if (comboFornecedor.value && comboFornecedor.value != '0'){

			var _this = this;
	       
	       $.postJSON(contextPath + "/produto/obterCodigoDisponivel",  
	    		[{name:"idFornecedor", value:comboFornecedor.value}],
	            function (result) {
	   
	           var tipoMensagem = result.tipoMensagem;
	           
	           var listaMensagens = result.listaMensagens;
	           
	           if (tipoMensagem && listaMensagens) {
	             
	             exibirMensagem(tipoMensagem, listaMensagens);
	             return;

	           }
	                     
             if (result[0]){
               
               $("#codigoProdutoCadastro", _this.workspace).val("");
               $("#codigoProdutoCadastro", _this.workspace).mask("?99999999");
               
               $('#comboEditor').find('option[value="-1"]').remove();
             } else {
               
               $("#codigoProdutoCadastro", _this.workspace).val(result[1]);
               $("#codigoProdutoCadastro", _this.workspace).mask("?9999999999");
               
               if(result[2] === false) {
	               var texto = result[3];
	               $('#comboEditor', _this.workspace).prepend( $('<option></option>').val(-1).html(texto) );
	               $('#comboEditor', _this.workspace).val($("#comboEditor option:first").val());
               }
             }
           });
	       
	     } else {
	         
	         $("#codigoProdutoCadastro", this.workspace).mask("?99999999");
	         $("#codigoProdutoCadastro", this.workspace).val("");
	       }
		
		produtoController.exibir_formatarCodICD();
		
	     },
	     
	     exibir_formatarCodICD : function(){
	    	
	 			var fornecedores = [1,2,16];
	 			var idFornecedor = parseInt($('#comboFornecedoresCadastro').val());
	 			
	 			if(idFornecedor==0){
	 				$("td[name='tdCodigoProdutoICDCadastro']", produtoController.workspace).hide();
	 				$("#codigoProdutoICDCadastro").val('');
	 				return;
	 			}
	 			
	 			$("td[name='tdCodigoProdutoICDCadastro']", produtoController.workspace).show();
	 			
	 			var disabled=(fornecedores.indexOf(idFornecedor)==-1);
	 			
	 			$("#codigoProdutoICDCadastro").prop('disabled', disabled);
	 			
	 			if(disabled == false){ 
	 				$("#codigoProdutoICDCadastro").val('');
	 			}else{
	 				produtoController.atualizaICD();
	 			}
	     },
	             
         validarCodigoProduto : function(){
        	     
    	     var idForn = $("#comboFornecedoresCadastro", this.workspace).val();
    	     var inpCodigo = $("#codigoProdutoCadastro", this.workspace).val();
    	     
    	     if (idForn && idForn != "0" && inpCodigo){
    	       
    	       var _this = this;
    	       
    	       $.postJSON(contextPath + "/produto/validarCodigoProdutoInput",  
    	         [{name:"codigoFornecedor", value:idForn}, {name:"codigoProduto", value:inpCodigo}],
    	            null,
    	         function (result){
    	           $("#codigoProdutoCadastro", _this.workspace).focus();
    	       });
    	     }
         },
	     
	     trocarOrdemCampos : function(p){
	    	     
    	     if (p){	    	       
    	       var trForn = $("#trForn", this.workspace);
    	       $("#trForn", this.workspace).remove();
    	       $("#trCodigo", this.workspace).after(trForn);
    	     } else {
    	       
    	       var trCodigo = $("#trCodigo", this.workspace);
    	       $("#trCodigo", this.workspace).remove();
    	       $("#trForn", this.workspace).after(trCodigo);
    	     }
	     }   
}, BaseController);
//@ sourceURL=produto.js