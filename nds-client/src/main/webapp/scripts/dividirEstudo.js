var dividirEstudo = $.extend(true, {

    confirmar : function() {

	var numeroEstudoOriginal = $('#numeroEstudoOriginal').val();
	var codigoProduto = $('#codigoProduto').val();
	var nomeProduto = $('#nomeProduto').val();
	var edicaoProduto = $('#edicaoProduto').val();
	var dataDistribuicao = $('#dataDistribuicao').val();
	var percentualDivisaoPrimeiroEstudo = $('#percentualDivisaoPrimeiroEstudo').val();
	var percentualDivisaoSegundoEstudo = $('#percentualDivisaoSegundoEstudo').val();
	var quantidadeReparte = $('#quantidadeReparte').val();
	var numeroPrimeiroEstudo = $('#numeroPrimeiroEstudo').val();
	var repartePrimeiroEstudo = $('#repartePrimeiroEstudo').val();
	var numeroSegundoEstudo = $('#numeroSegundoEstudo').val();
	var reparteSegundoEstudo = $('#reparteSegundoEstudo').val();
	var dataLancamentoSegundoEstudo = $('#dataLancamentoSegundoEstudo').val();

	var dados = [];

	dados.push({
	    name : "divisaoEstudo.numeroEstudoOriginal",
	    value : numeroEstudoOriginal
	});

	dados.push({
	    name : "divisaoEstudo.codigoProduto",
	    value : codigoProduto
	});

	dados.push({
	    name : "divisaoEstudo.nomeProduto",
	    value : nomeProduto
	});

	dados.push({
	    name : "divisaoEstudo.edicaoProduto",
	    value : edicaoProduto
	});

	dados.push({
	    name : "divisaoEstudo.dataDistribuicao",
	    value : dataDistribuicao
	});

	dados.push({
	    name : "divisaoEstudo.percentualDivisaoPrimeiroEstudo",
	    value : percentualDivisaoPrimeiroEstudo
	});

	dados.push({
	    name : "divisaoEstudo.percentualDivisaoSegundoEstudo",
	    value : percentualDivisaoSegundoEstudo
	});

	dados.push({
	    name : "divisaoEstudo.quantidadeReparte",
	    value : quantidadeReparte
	});

	dados.push({
	    name : "divisaoEstudo.numeroPrimeiroEstudo",
	    value : numeroPrimeiroEstudo
	});

	dados.push({
	    name : "divisaoEstudo.repartePrimeiroEstudo",
	    value : repartePrimeiroEstudo
	});

	dados.push({
	    name : "divisaoEstudo.numeroSegundoEstudo",
	    value : numeroSegundoEstudo
	});

	dados.push({
	    name : "divisaoEstudo.reparteSegundoEstudo",
	    value : reparteSegundoEstudo
	});

	dados.push({
	    name : "divisaoEstudo.dataLancamentoSegundoEstudo",
	    value : dataLancamentoSegundoEstudo
	});

	$.ajax({
	    url : 'dividirEstudo/confirmar',
	    data : dados,
	    type : "POST",

	    success : function(data) {
		if (data.mensagens.tipoMensagem == "ERROR") {
		    alert("Ocorreu um erro durante a divisão!");
		} else {
		    alert("Divisão executada com sucesso!");
		}

	    }
	});
    },
}, BaseController);
