var excecaoSegmentoParciaisController = $.extend(true, {
	
	init : function() {
	
		excecaoSegmentoParciaisController.excessaoBGrid = {
				
				gridName : "excessaoBGrid",
				comments : "Grid que fica na parte direita da tela que é responsável pela listagem dos produtos para inserção na cota",
				reload : function reload(){
					$(".excessaoBGrid", workspace).flexReload();
				},
				reload : function reload(workspace){
					$(".excessaoBGrid", workspace).flexReload();
				},
				reload : function reloadNovosParametros(options){
					var workspace = options.workspace;
						
					if (workspace === undefined) {
						$(".excessaoBGrid").flexOptions(options);
					}else {
						$(".excessaoBGrid", workspace).flexOptions(options);
					}
					
					$(".excessaoBGrid").flexReload();
				},
				grid : $('.excessaoBGrid').flexigrid({
					//url : '../xml/excessaoB-xml.xml',
					dataType : 'json',
					colModel : [ {
						display : 'Código',
						name : 'codigo',
						width : 40,
						sortable : true,
						align : 'left'
					},{
						display : 'Produto',
						name : 'produto',
						width : 90,
						sortable : true,
						align : 'left'
					},{
						display : 'Segmento',
						name : 'segmento',
						width : 50,
						sortable : true,
						align : 'left'
					},{
						display : 'Fornecedor',
						name : 'fornecedor',
						width : 50,
						sortable : true,
						align : 'left'
					},  {
						display : '',
						name : 'sel',
						width : 20,
						sortable : true,
						align : 'center'
					}],
					width : 330,
					height : 235
				})
				
			}
			
		$(".excessaoNaoRecebidaGrid").flexigrid({
			//url : '../xml/excessaoNaoRecebidaGrid-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 50,
				sortable : true,
				align : 'left'
			},{
				display : 'Status',
				name : 'status',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 115,
				sortable : true,
				align : 'left'
			}, {
				display : 'Usuário',
				name : 'usuario',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data',
				name : 'data',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Hora',
				name : 'hora',
				width : 60,
				sortable : true,
				align : 'center'
			},  {
				display : 'Ação',
				name : 'acao',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 600,
			height : 250
		});
		
	$(".excessaoGrid").flexigrid({
			//url : '../xml/excessaoGridXml-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 50,
				sortable : true,
				align : 'left'
			},{
				display : 'Status',
				name : 'status',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'Nome',
				width : 130,
				sortable : true,
				align : 'left'
			},  {
				display : '',
				name : 'sel',
				width : 20,
				sortable : true,
				align : 'center'
			}],
			width : 330,
			height : 235
		});
	
	$(".excessaoCotaGrid").flexigrid({
			//url : '../xml/excessaoCotaGrid-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 170,
				sortable : true,
				align : 'left'
			}, {
				display : 'Usuário',
				name : 'usuario',
				width : 105,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data',
				name : 'data',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Hora',
				name : 'hora',
				width : 60,
				sortable : true,
				align : 'center'
			},  {
				display : 'Ação',
				name : 'acao',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 600,
			height : 250
		});
		
	},
	
	
	porCota : function (){
		$('.porCota').show();
		$('.porExcessao').hide();
		
		excecaoSegmentoParciaisController.excessaoBGrid.reload({
			url : contextPath + '/distribuicao/excecaoSegmentoParciais/teste',
			dataType : 'json',
			params : [{ name : 'text', value : 'UHUUUUUUUUUUUUUUU!!!'}]
		});
	},
	
	
	
	
}, BaseController);
//@ sourceURL=excecaoSegmentoParciaisController.js
