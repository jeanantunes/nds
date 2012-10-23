relatorioServicosEntregaController = $.extend(true, {
	
	path : contextPath + '/administracao/relatorioServicosEntrega/',


	init : function() {
		
		$("#relatorioServicosEntrega_filtro_dataDe").datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#relatorioServicosEntrega_filtro_dataAte").datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true
		});
		
		this.initGridTransportadores();
		this.initGridDetalhe();
	},
	
	
	pesquisar : function() {
		
		var params = $("#relatorioServicosEntregaForm").serialize();
		
		$(".relatorioServicosEntrega_transportadoresGrid").flexOptions({
			url : this.path + 'pesquisar.json',
			params: serializeParamsToFlexiGridPost(params),
			preProcess : relatorioServicosEntregaController.insereLinks,
			newp : 1
		});
		
		$(".relatorioServicosEntrega_transportadoresGrid").flexReload();
		$(".grids", this.workspace).show();
	},
	
	
	insereLinks : function(data) {
		
		$.each(data.rows, function(index, value) {
			
		});
		
		return data;
	},
	
	
	initGridTransportadores : function() {

		$(".relatorioServicosEntrega_transportadoresGrid").flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Transportador',
				name : 'nomeTransportador',
				width : 200,
				sortable : true,
				align : 'left'
			}, {
				display : 'Roteiro',
				name : 'descricaoRoteiro',
				width : 150,
				sortable : true,
				align : 'left'
			}, {
				display : 'Rota',
				name : 'descricaoRota',
				width : 150,
				sortable : true,
				align : 'left'
			}, {
				display : 'Cota',
				name : 'numeroCota',
				width : 65,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeJornaleiro',
				width : 200,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 100,
				sortable : true,
				align : 'right'
			}],
			sortname : "nome",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
	},
	
	
	initGridDetalhe : function() {
		
		$(".transportadorDetalheGrid").flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Descrição',
				name : 'descricao',
				width : 250,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data',
				name : 'data',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 70,
				sortable : true,
				align : 'right'
			}],
			width : 450,
			height : 155
		});
	}

}, BaseController);