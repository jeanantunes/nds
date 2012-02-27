function pesquisarPorCodigoProduto() {
	var codigoProduto = $("#codigo").val();
	
<<<<<<< HEAD
	if (!isNumeric(codigoProduto)) {
		//TODO: mostrar msg
		
		$("#codigo").val("");
		$("#produto").val("");
		$("#edicao").val("");
		$("#edicao").attr("disabled", "disabled");
		
		return;
	}
	
	if (codigoProduto && codigoProduto.length > 0) {
		$("#produto").val("");
		$("#edicao").val("");
		
		$.postJSON(contextPath + "/produto/pesquisarPorCodigoProduto",
				   "codigoProduto=" + codigoProduto, exibirNomeProduto, null);
=======
	if (codigoProduto && codigoProduto.length > 0) {
		$.postJSON(contextPath + "/produto/pesquisarPorCodigoProduto",
				   "codigoProduto=" + codigoProduto, exibirNomeProduto);
>>>>>>> EMS0009
	}
}

function exibirNomeProduto(result) {
<<<<<<< HEAD
	$("#edicao").removeAttr("disabled");
=======
>>>>>>> EMS0009
	$("#produto").val(result.nome);
	$("#edicao").focus();
	
	chamarFuncaoCallBack();
}

function pesquisarPorNomeProduto() {
	var produto = $("#produto").val();
	
	if (produto && produto.length > 0) {
		$.postJSON(contextPath + "/produto/pesquisarPorNomeProduto",
				   "nomeProduto=" + produto, exibirAutoComplete);
	}
}

function exibirAutoComplete(result) {
<<<<<<< HEAD
=======
	//TODO tratar retorno pesquisa
>>>>>>> EMS0009
	$("#produto").autocomplete({
		source: result,
		select: function(event, ui) {
			completarPesquisa(ui.item.chave);
		}
	});
}

function completarPesquisa(chave){
<<<<<<< HEAD
	$("#edicao").removeAttr("disabled");
	$("#edicao").val("");
=======
>>>>>>> EMS0009
	$("#codigo").val(chave.codigo);
	$("#edicao").focus();
	
	chamarFuncaoCallBack();
}

function validarNumEdicao() {
<<<<<<< HEAD
	var codigoProduto = $("#codigo").val();
	var numeroEdicao = $("#edicao").val();
	
	if (!isNumeric(numeroEdicao)) {
		//TODO: mostrar msg
		
		$("#edicao").val("");
		
		return;
	}
	
	if (codigoProduto && codigoProduto.length > 0
			&& numeroEdicao && numeroEdicao.length > 0) {
		
		var data = "codigoProduto=" + codigoProduto +
		   		   "&numeroEdicao=" + numeroEdicao;
		
		$.postJSON(contextPath + "/produto/validarNumeroEdicao",
				   data, null, tratarErroValidacao);
	}
=======
	var data = "codigoProduto=" + $("#codigo").val() +
       		   "&numeroEdicao=" + $("#edicao").val();

	$.postJSON(contextPath + "/produto/validarNumeroEdicao",
			   data, null, tratarErroValidacao);
>>>>>>> EMS0009
}

function tratarErroValidacao() {
	$("#edicao").val("");
	$("#edicao").focus();
}

function chamarFuncaoCallBack() {
	if (typeof pesquisarProdutoCallBack == 'function') {
		pesquisarProdutoCallBack();
	}
}
