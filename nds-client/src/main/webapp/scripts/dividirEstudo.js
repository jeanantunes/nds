var dividirEstudo = $.extend(true, {

    gerarDivisao : function() {

	var numeroEstudoOriginal = $('#numeroEstudoOriginal');
	var codigoProduto = $('#codigoProduto');
	var nomeProduto = $('#nomeProduto');
	var edicaoProduto = $('#edicaoProduto');
	var dataDistribuicao = $('#dataDistribuicao');
	var percentualDivisaoPrimeiroEstudo = $('#percentualDivisaoPrimeiroEstudo');
	var percentualDivisaoSegundoEstudo = $('#percentualDivisaoSegundoEstudo');
	var quantidadeReparte = $('#quantidadeReparte');
	var numeroPrimeiroEstudo = $('#numeroPrimeiroEstudo');
	var repartePrimeiroEstudo = $('#repartePrimeiroEstudo');
	var numeroSegundoEstudo = $('#numeroSegundoEstudo');
	var reparteSegundoEstudo = $('#reparteSegundoEstudo');
	var dataLancamentoPrimeiroEstudo = $('#dataLancamentoPrimeiroEstudo');
	var dataLancamentoSegundoEstudo = $('#dataLancamentoSegundoEstudo');

	var dados = [];

	dados.push({
	    name : "divisaoEstudo.numeroEstudoOriginal",
	    value : numeroEstudoOriginal.val()
	});

	dados.push({
	    name : "divisaoEstudo.codigoProduto",
	    value : codigoProduto.val()
	});

	dados.push({
	    name : "divisaoEstudo.nomeProduto",
	    value : nomeProduto.val()
	});

	dados.push({
	    name : "divisaoEstudo.edicaoProduto",
	    value : edicaoProduto.val()
	});

	dados.push({
	    name : "divisaoEstudo.dataDistribuicao",
	    value : dataDistribuicao.val()
	});

	dados.push({
	    name : "divisaoEstudo.percentualDivisaoPrimeiroEstudo",
	    value : percentualDivisaoPrimeiroEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.percentualDivisaoSegundoEstudo",
	    value : percentualDivisaoSegundoEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.quantidadeReparte",
	    value : quantidadeReparte.val()
	});

	dados.push({
	    name : "divisaoEstudo.numeroPrimeiroEstudo",
	    value : numeroPrimeiroEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.repartePrimeiroEstudo",
	    value : repartePrimeiroEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.numeroSegundoEstudo",
	    value : numeroSegundoEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.reparteSegundoEstudo",
	    value : reparteSegundoEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.dataLancamentoSegundoEstudo",
	    value : dataLancamentoSegundoEstudo.val()
	});

	$.postJSON('dividirEstudo/gerarDivisao', dados, function(response) {
	    numeroPrimeiroEstudo.val(response.divisaoEstudoDTO.numeroPrimeiroEstudo);
	    repartePrimeiroEstudo.val(response.divisaoEstudoDTO.repartePrimeiroEstudo);
	    dataLancamentoPrimeiroEstudo.val(response.divisaoEstudoDTO.dataLancamentoPrimeiroEstudo);
	    numeroSegundoEstudo.val(response.divisaoEstudoDTO.numeroSegundoEstudo);
	    reparteSegundoEstudo.val(response.divisaoEstudoDTO.reparteSegundoEstudo);
	    $('.corpo').flexReload();
	}, function() {
	});

    },

    confirmar : function() {

	var numeroEstudoOriginal = $('#numeroEstudoOriginal');
	var codigoProduto = $('#codigoProduto');
	var nomeProduto = $('#nomeProduto');
	var edicaoProduto = $('#edicaoProduto');
	var dataDistribuicao = $('#dataDistribuicao');
	var percentualDivisaoPrimeiroEstudo = $('#percentualDivisaoPrimeiroEstudo');
	var percentualDivisaoSegundoEstudo = $('#percentualDivisaoSegundoEstudo');
	var quantidadeReparte = $('#quantidadeReparte');
	var numeroPrimeiroEstudo = $('#numeroPrimeiroEstudo');
	var repartePrimeiroEstudo = $('#repartePrimeiroEstudo');
	var numeroSegundoEstudo = $('#numeroSegundoEstudo');
	var reparteSegundoEstudo = $('#reparteSegundoEstudo');
	var dataLancamentoPrimeiroEstudo = $('#dataLancamentoPrimeiroEstudo');
	var dataLancamentoSegundoEstudo = $('#dataLancamentoSegundoEstudo');

	var dados = [];

	dados.push({
	    name : "divisaoEstudo.numeroEstudoOriginal",
	    value : numeroEstudoOriginal.val()
	});

	dados.push({
	    name : "divisaoEstudo.codigoProduto",
	    value : codigoProduto.val()
	});

	dados.push({
	    name : "divisaoEstudo.nomeProduto",
	    value : nomeProduto.val()
	});

	dados.push({
	    name : "divisaoEstudo.edicaoProduto",
	    value : edicaoProduto.val()
	});

	dados.push({
	    name : "divisaoEstudo.dataDistribuicao",
	    value : dataDistribuicao.val()
	});

	dados.push({
	    name : "divisaoEstudo.percentualDivisaoPrimeiroEstudo",
	    value : percentualDivisaoPrimeiroEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.percentualDivisaoSegundoEstudo",
	    value : percentualDivisaoSegundoEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.quantidadeReparte",
	    value : quantidadeReparte.val()
	});

	dados.push({
	    name : "divisaoEstudo.numeroPrimeiroEstudo",
	    value : numeroPrimeiroEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.repartePrimeiroEstudo",
	    value : repartePrimeiroEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.dataLancamentoPrimeiroEstudo",
	    value : dataLancamentoPrimeiroEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.numeroSegundoEstudo",
	    value : numeroSegundoEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.reparteSegundoEstudo",
	    value : reparteSegundoEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.dataLancamentoSegundoEstudo",
	    value : dataLancamentoSegundoEstudo.val()
	});

	$.postJSON('dividirEstudo/confirmar', dados, function(response) {
	    $('#workspace').tabs("remove", $('#workspace').tabs('option', 'selected'));
	    var pathTela = "/nds-client";
	    var matrizDistribuicao = new MatrizDistribuicao(pathTela, "matrizDistribuicao", BaseController.workspace);
	    matrizDistribuicao.pesquisar();
	}, function() {
	});
    },

    cancelar : function() {

	var numeroEstudoOriginal = $('#numeroEstudoOriginal');
	var codigoProduto = $('#codigoProduto');
	var nomeProduto = $('#nomeProduto');
	var edicaoProduto = $('#edicaoProduto');
	var dataDistribuicao = $('#dataDistribuicao');
	var percentualDivisaoPrimeiroEstudo = $('#percentualDivisaoPrimeiroEstudo');
	var percentualDivisaoSegundoEstudo = $('#percentualDivisaoSegundoEstudo');
	var quantidadeReparte = $('#quantidadeReparte');
	var numeroPrimeiroEstudo = $('#numeroPrimeiroEstudo');
	var repartePrimeiroEstudo = $('#repartePrimeiroEstudo');
	var numeroSegundoEstudo = $('#numeroSegundoEstudo');
	var reparteSegundoEstudo = $('#reparteSegundoEstudo');
	var dataLancamentoPrimeiroEstudo = $('#dataLancamentoPrimeiroEstudo');
	var dataLancamentoSegundoEstudo = $('#dataLancamentoSegundoEstudo');

	var dados = [];

	dados.push({
	    name : "divisaoEstudo.numeroEstudoOriginal",
	    value : numeroEstudoOriginal.val()
	});

	dados.push({
	    name : "divisaoEstudo.codigoProduto",
	    value : codigoProduto.val()
	});

	dados.push({
	    name : "divisaoEstudo.nomeProduto",
	    value : nomeProduto.val()
	});

	dados.push({
	    name : "divisaoEstudo.edicaoProduto",
	    value : edicaoProduto.val()
	});

	dados.push({
	    name : "divisaoEstudo.dataDistribuicao",
	    value : dataDistribuicao.val()
	});

	dados.push({
	    name : "divisaoEstudo.percentualDivisaoPrimeiroEstudo",
	    value : percentualDivisaoPrimeiroEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.percentualDivisaoSegundoEstudo",
	    value : percentualDivisaoSegundoEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.quantidadeReparte",
	    value : quantidadeReparte.val()
	});

	dados.push({
	    name : "divisaoEstudo.numeroPrimeiroEstudo",
	    value : numeroPrimeiroEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.repartePrimeiroEstudo",
	    value : repartePrimeiroEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.dataLancamentoPrimeiroEstudo",
	    value : dataLancamentoPrimeiroEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.numeroSegundoEstudo",
	    value : numeroSegundoEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.reparteSegundoEstudo",
	    value : reparteSegundoEstudo.val()
	});

	dados.push({
	    name : "divisaoEstudo.dataLancamentoSegundoEstudo",
	    value : dataLancamentoSegundoEstudo.val()
	});

	$.postJSON('dividirEstudo/cancelar', dados, function(response) {
	    percentualDivisaoPrimeiroEstudo.val(response.divisaoEstudoDTO.percentualDivisaoPrimeiroEstudo);
	    percentualDivisaoSegundoEstudo.val(response.divisaoEstudoDTO.percentualDivisaoSegundoEstudo);
	    quantidadeReparte.val(response.divisaoEstudoDTO.quantidadeReparte);
	    numeroPrimeiroEstudo.val(response.divisaoEstudoDTO.numeroPrimeiroEstudo);
	    numeroSegundoEstudo.val(response.divisaoEstudoDTO.numeroSegundoEstudo);
	    repartePrimeiroEstudo.val(response.divisaoEstudoDTO.repartePrimeiroEstudo);
	    reparteSegundoEstudo.val(response.divisaoEstudoDTO.reparteSegundoEstudo);
	    dataLancamentoPrimeiroEstudo.val(response.divisaoEstudoDTO.dataLancamentoPrimeiroEstudo);
	    dataLancamentoSegundoEstudo.val(response.divisaoEstudoDTO.dataLancamentoSegundoEstudo);
	}, function() {
	});
    }
}, BaseController);
