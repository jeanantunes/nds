var negociacaoDividaController = $.extend(true, {

	path : contextPath + '/financeiro/negociacaoDivida/',

	init : function() {
		negociacaoDividaController.initGridNegociacao();
		negociacaoDividaController.initGridNegociacaoDetalhe();
	},

	pesquisarCota : function(numeroCota) {

		$(".grids", this.workspace).hide();
		
		$.postJSON(contextPath + '/cadastro/cota/pesquisarPorNumero',
				"numeroCota=" + numeroCota, 
				function(result) {
					$('#negociacaoDivida_statusCota').html(result.status);
					$('#negociacaoDivida_nomeCota').html(result.nome);
				},
				function() {
					$('#negociacaoDivida_statusCota').html('');
					$('#negociacaoDivida_nomeCota').html('');
				}
				
		);
	},
	
	pesquisar : function() {
		
		$("#totalSelecionado", this.workspace).html('0,00');
		$('#negociacaoDivida_numEnomeCota').html($('#negociacaoDivida_numCota').val() +' - '+ $('#negociacaoDivida_nomeCota').html());
		
		var params = $("#negociacaoDividaForm", this.workspace).serialize();
	
		$(".negociarGrid", this.workspace).flexOptions({
			url : this.path + 'pesquisar.json?' + params, 
			preProcess : negociacaoDividaController.montaColunaDetalhesAcao,
			newp : 1
		});
		
		$(".negociarGrid").flexReload();
		
		$(".grids", this.workspace).show();
				
		
	},
	
	pesquisarDetalhes : function() {
		
		var params = $("#negociacaoDividaForm", this.workspace).serialize();
		
		$(".negociarDetalheGrid", this.workspace).flexOptions({
			url : this.path + 'pesquisarDetalhes.json?' + params, 
			newp : 1
		});
			
		$(".negociarDetalhesGrid").flexReload();
		
		
	},
	
	montaColunaDetalhesAcao : function(data) {
		
		var total = '0,00';
		
		$.each(data.rows, function(index, value) {
			
			var detalhes = '<a href="javascript:;" onclick="negociacaoDividaController.popup_detalhe();" title="Ver Detalhes"><img src="' + contextPath + '/images/ico_detalhes.png" alt="Detalhes" border="0" /></a>    ';
			var acao = '<input type="checkbox" class="negociacaoCheck" onclick="negociacaoDividaController.verificarCheck()"></input> ';
			value.cell.detalhes = detalhes;
			value.cell.acao = acao;
			
			total = sumPrice(value.cell.total, total);
		});

		$("#total", this.workspace).html(total);
		
		return data;
	},
	
	
	
	popup_detalhe : function() {
		negociacaoDividaController.pesquisarDetalhes();
		$(".negociarDetalheGrid").flexReload();
		$("#dialog-detalhe").dialog({
			resizable: false,
			height:420,
			width:700,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	},
	
	verificarCheck : function() {
		
		var todosChecados = true;
		var totalSelecionado = $("#totalSelecionado", this.workspace);
		totalSelecionado.html('0,00');
		$(".negociacaoCheck", this.workspace).each(function(index, element) {	
			var total = $('td[abbr="total"] >div', element.parentNode.parentNode.parentNode);
			if (!element.checked) {
				todosChecados = false;
			}
			if(element.checked){
				totalSelecionado.html(sumPrice(totalSelecionado.html(), total.html()));
			}
			
		});
		
		$("#negociacaoCheckAll").get(0).checked = todosChecados;
	},
	
	
	checkAll : function (check) {
		
		$(".negociacaoCheck").each(function(index, element) {
			element.checked = check.checked;
		});
		negociacaoDividaController.verificarCheck();
	},
	
	initGridNegociacao : function() {
		$(".negociarGrid").flexigrid({
			dataType : 'json',
			colModel : [  {
				display : 'Data Emiss&atilde;o',
				name : 'dtEmissao',
				width : 150,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Vencimento',
				name : 'dtVencimento',
				width : 150,
				sortable : true,
				align : 'center'
			}, {
				display : 'Prazo',
				name : 'prazo',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Valor Divida R$',
				name : 'vlDivida',
				width : 140,
				sortable : true,
				align : 'right'
			}, {
				display : 'Encargos',
				name : 'encargos',
				width : 100,
				sortable : true,
				align : 'center',
			}, {
				display : 'Total R$',
				name : 'total',
				width : 100,
				sortable : true,
				align : 'right',
			}, {
				display : 'Detalhes',
				name : 'detalhes',
				width : 60,
				sortable : true,
				align : 'center',
			}, {
				display : '',
				name : 'acao',
				width : 40,
				sortable : true,
				align : 'center',
			}],
			sortname : "dtEmissao",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});
	},
	
	initGridNegociacaoDetalhe : function() {
		$(".negociarDetalheGrid").flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Data',
				name : 'data',
				width : 90,
				sortable : true,
				align : 'center'
			},{
				display : ' ',
				name : 'tipo',
				width : 80,
				sortable : true,
				align : 'left'
			},{
				display : 'R$',
				name : 'valor',
				width : 60,
				sortable : true,
				align : 'right'
			},  {
				display : 'Observa&ccedil;&atilde;o',
				name : 'observacao',
				width : 320,
				sortable : true,
				align : 'left'
			}],
			width : 620,
			height : 160
		});		
	},

}, BaseController);
