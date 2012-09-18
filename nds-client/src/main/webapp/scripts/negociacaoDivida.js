var negociacaoDividaController = $.extend(true, {

	path : contextPath + '/financeiro/negociacaoDivida/',

	init : function() {
		negociacaoDividaController.initGridNegociacao();
	},

	pesquisarCota : function(numeroCota) {

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
		
		$('#negociacaoDivida_numEnomeCota').html($('#negociacaoDivida_numCota').val() +' - '+ $('#negociacaoDivida_nomeCota').html());
		
		var params = $("#negociacaoDividaForm", this.workspace).serialize();
		
		$(".grids", this.workspace).flexOptions({
			url : this.path + 'pesquisar.json?' + params, 
			//preProcess : visaoEstoqueController.montaColunaAcao,
			newp : 1
		});
		
		$(".grids", this.workspace).show();
		
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
				name : 'sel',
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

}, BaseController);
