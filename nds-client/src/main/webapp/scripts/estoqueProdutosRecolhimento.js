var EstoqueProdutosRecolhimentoController = $.extend(true, {
	
	init : function() {
		
		$("#dataRecolhimento", this.workspace).mask("99/99/9999");
		
		$( "#dataRecolhimento" , this.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		this.inicializarGrid();
	},
	
	inicializarGrid : function() {
		
		$("#gridResultado", this.workspace).flexigrid({
			preProcess: EstoqueProdutosRecolhimentoController.getDataFromResult,
			dataType : 'json',
			colModel : [ {
				display : 'Seq',
				name : 'sequencia',
				width : 15,
				sortable : true,
				align : 'center'
			}, {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 130,
				sortable : true,
				align : 'center'
			}, {
				display : 'Edicao',
				name : 'numeroEdicao',
				width : 40,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preco Capa',
				name : 'precoCapa',
				width : 70,
				sortable : true,
				align : 'center'
			} , {
				display : 'Desc Logistica',
				name : 'descontoLogistica',
				width : 50,
				sortable : true,
				align : 'center'
			},{
				display: 'Lançamento.',
				name: 'lancamento',
				width: 60,
				sortable: true,
				align: 'center'
			}, {
				display: 'Suplementar',
				name: 'suplementar',
				width: 60,
				sortable: true,
				align: 'center'
			}, {
				display: 'Recolhimento',
				name: 'recolhimento',
				width: 80,
				sortable: true,
				align: 'center'
			}, {
				display: 'Recolhimento PDV',
				name: 'recolhimentoPDV',
				width: 100,
				sortable: true,
				align: 'center'
			}, {
				display: 'Danificado',
				name: 'danificado',
				width: 70,
				sortable: true,
				align: 'center'
			}, {
				display: 'Total',
				name: 'total',
				width: 60,
				sortable: true,
				align: 'center'
			}],
				width : 960,
				height : 300,
				sortname : "sequencia",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true
			}
		);
	},
	
	getDataFromResult: function(data){
		
		if (typeof data.mensagens == "object"){
			
			$(".bt_arq", EstoqueProdutosRecolhimentoController.workspace).hide();
			
			$(".fieldGrid", EstoqueProdutosRecolhimentoController.workspace).hide();
			
			exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
			
		} else {
			
			$.each(data.rows, function(index, value){
				
				if (!value.cell.sequencia){
					value.cell.sequencia = '-';
				}
				
				if (!value.cell.lancamento){
					value.cell.lancamento = '-';
				}
				
				if (!value.cell.suplementar){
					value.cell.suplementar = '-';
				}
				
				if (!value.cell.recolhimento){
					value.cell.recolhimento = '-';
				}
				
				if (!value.cell.recolhimentoPDV){
					value.cell.recolhimento = '-';
				}
				
				if (!value.cell.danificado){
					value.cell.danificado = '-';
				}
				
				value.cell.produto = '<p align="left">' + value.cell.produto + '</p>';
			});
			
			$(".fieldGrid", EstoqueProdutosRecolhimentoController.workspace).show();
			$(".bt_arq", EstoqueProdutosRecolhimentoController.workspace).show();
			
			return data;
		}
	},
	
	pesquisar: function(){
		
		var parametroPesquisa = [{name : 'dataRecolhimento', value : $("#dataRecolhimento").val()}];
		
		$("#gridResultado", EstoqueProdutosRecolhimentoController.workspace).flexOptions({
			url : contextPath + '/estoqueProdutosRecolhimento/pesquisar', 
			params: parametroPesquisa,
			newp: 1
		});
		
		$("#gridResultado", EstoqueProdutosRecolhimentoController.workspace).flexReload();
	}
	
}, BaseController);

$(function() {
	EstoqueProdutosRecolhimentoController.init();
});

//@ sourceURL=estoqueProdutosRecolhimento.js