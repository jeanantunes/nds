var furoProdutoController = $.extend(true, {
	init : function() {
		$("#dataLancamento", furoProdutoController.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		$("#novaData", furoProdutoController.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		$("#produto", furoProdutoController.workspace).autocomplete({source: ""});
		
		$("#dataLancamento", furoProdutoController.workspace).mask("99/99/9999");
		$("#novaData", furoProdutoController.workspace).mask("99/99/9999");
		$("#edicao", furoProdutoController.workspace).mask("?99999999999999999999", {placeholder:""});
	},
	
	popup : function() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$("#dialog-novo", furoProdutoController.workspace).dialog({
			resizable: false,
			height:'auto',
			width:250,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$(this).dialog("close");
					furoProdutoController.confirmar();
				},
				"Cancelar": function() {
					$(this).dialog("close");
				}
			},
			close : function(){
					$("#linkConfirmar", furoProdutoController.workspace).focus();
				},
			form: $("#dialog-novo", this.workspace).parents("form")
		});
	},
	
	pesquisar : function(){
		
		$("#resultado", furoProdutoController.workspace).hide();
		
		var data = {codigo: $("#codigo", furoProdutoController.workspace).val(),
				produto: $("#produto", furoProdutoController.workspace).val(),
				edicao: $("#edicao", furoProdutoController.workspace).val(),
				dataLancamento: $("#dataLancamento", furoProdutoController.workspace).val()};
		$.postJSON(contextPath + "/lancamento/furoProduto/pesquisar", data, furoProdutoController.exibirProduto);
	},
	
	exibirProduto : function(result){
		$("#txtProduto", furoProdutoController.workspace).text(result.nomeProduto);
		$("#txtEdicao", furoProdutoController.workspace).text(result.edicao);
		$("#txtQtdExemplares", furoProdutoController.workspace).text(result.quantidadeExemplares);
		if (result.pathImagem){
			$("#imagem", furoProdutoController.workspace).attr("src", result.pathImagem);
		} else {
			$("#imagem", furoProdutoController.workspace).attr("src", contextPath + "/images/logo_sistema.png");
		}
		$("#imagem", furoProdutoController.workspace).attr("alt", result.nomeProduto);
		$("#novaData", furoProdutoController.workspace).val(result.novaData);
		
		$("#codigoProdutoHidden", furoProdutoController.workspace).val(result.codigoProduto);
		$("#lancamentoHidden", furoProdutoController.workspace).val(result.idLancamento);
		$("#produtoEdicaoHidden", furoProdutoController.workspace).val(result.idProdutoEdicao);
					
		$("#resultado", furoProdutoController.workspace).show();
		$("#novaData", furoProdutoController.workspace).focus();
	},
	
	pesquisarPorNomeProduto : function(){
		var produto = $("#produto", furoProdutoController.workspace).val();
		
		if (produto && produto.length > 0){
			$.postJSON(contextPath + "/lancamento/furoProduto/pesquisarPorNomeProduto", {nomeProduto:produto}, furoProdutoController.exibirAutoComplete);
		}
	},
	
	exibirAutoComplete : function(result){
		$("#produto", furoProdutoController.workspace).autocomplete({
			source: result,
			select: function(event, ui){
				furoProdutoController.completarPesquisa(ui.item.chave);
			}
		});
	},
	
	completarPesquisa : function(chave){
		$("#codigo", furoProdutoController.workspace).val(chave.codigoProduto);
		$("#edicao", furoProdutoController.workspace).focus();
	},
	
	confirmar : function(){
		var data = {codigoProduto:$("#codigoProdutoHidden", furoProdutoController.workspace).val(),
				idProdutoEdicao:$("#produtoEdicaoHidden", furoProdutoController.workspace).val(),
				novaData:$("#novaData", furoProdutoController.workspace).val(),
				idLancamento:$("#lancamentoHidden", furoProdutoController.workspace).val()};
		$.postJSON(contextPath + "/lancamento/furoProduto/validarFuro", data, function(result) { furoProdutoController.posProcessarConfirmacao(result, data) } );
	},
	
	posProcessarConfirmacao : function(result, data) {
	
		// typeof result.mensagens != "object"
		if (result.boolean == true) {
			furoProdutoController.popUpConfirmarFuroProduto(data);
		}
	},
	
	popUpConfirmarFuroProduto : function(data) {
		
		$("#dialog-confirmar-furo-produto", furoProdutoController.workspace).dialog({
			resizable: false,
			height:'auto',
			width:250,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$(this).dialog("close");
					$.postJSON(contextPath + "/lancamento/furoProduto/confirmarFuro", data, furoProdutoController.limparCampos());
				},
				"Cancelar": function() {
					$(this).dialog("close");
				}
			},
			close : function(){
					$("#linkConfirmar", furoProdutoController.workspace).focus();
				},
			form: $("#dialog-confirmar-furo-produto", this.workspace).parents("form")
		});

	},
	
	limparCampos : function(){
		$("#resultado", furoProdutoController.workspace).hide();
		$("#codigo", furoProdutoController.workspace).val("");
		$("#produto", furoProdutoController.workspace).val("");
		$("#edicao", furoProdutoController.workspace).mask("?99999999999999999999", {placeholder:""}).val("");
		$("#dataLancamento", furoProdutoController.workspace).val("");
		$("#novaData", furoProdutoController.workspace).val("");
		$("#codigo", furoProdutoController.workspace).focus();
	},
	
	buscarNomeProduto : function(){
		if ($("#codigo", furoProdutoController.workspace).val().length > 0){
			var data = {codigoProduto: $("#codigo", furoProdutoController.workspace).val()};
			$.postJSON(contextPath + "/lancamento/furoProduto/buscarNomeProduto", data,
				function(result){
					if (result && result.string != ""){
						$("#produto", furoProdutoController.workspace).val(result);
						$("#edicao", furoProdutoController.workspace).focus();
					} else {
						$("#produto", furoProdutoController.workspace).val("");
						$("#produto", furoProdutoController.workspace).focus();
					}
				}
			);
		}
	}
	
}, BaseController);
//@ sourceURL=furoProduto.js
