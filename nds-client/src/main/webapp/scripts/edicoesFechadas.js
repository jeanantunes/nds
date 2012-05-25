var contextPath;

var edicoesFechadasController = {
		init : function(path) {
			this.contextPath=path;
			this.initGridEdicoesFechadasGrid();
			this.initGridDetalheEdicoesFechadas();
			this.bindButtons();
		},
		bindButtons : function() {
			$("#btnPesquisar").click(function() {
				edicoesFechadasController.pesquisar();
				$(".grids").show();
			});
		},
		initGridEdicoesFechadasGrid: function(){
			$(".consultaEdicoesFechadasGrid").flexigrid({
				dataType : 'json',
				colModel : [ {
					display : 'Código',
					name : 'codigo',
					width : 70,
					sortable : true,
					align : 'left'
				}, {
					display : 'Produto',
					name : 'produto',
					width : 190,
					sortable : true,
					align : 'left'
				}, {
					display : 'Edição',
					name : 'edicao',
					width : 60,
					sortable : true,
					align : 'center'
				},{
					display : 'Fornecedor',
					name : 'fornecedor',
					width : 180,
					sortable : true,
					align : 'left'
				},  {
					display : 'Lançamento',
					name : 'lancamento',
					width : 80,
					sortable : true,
					align : 'center'
				}, {
					display : 'Recolhimento',
					name : 'recolhimento',
					width : 80,
					sortable : true,
					align : 'center'
				}, {
					display : 'Parcial',
					name : 'parcial',
					width : 80,
					sortable : true,
					align : 'center'
				}, {
					display : 'Saldo',
					name : 'saldo',
					width : 60,
					sortable : true,
					align : 'right'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 30,
					sortable : true,
					align : 'center'
				}],
				sortname : "fornecedor",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 180
			});
		},
		initGridDetalheEdicoesFechadas: function(){
			$(".detalheEdicoesFechadasGrid").flexigrid({
				dataType : 'xml',
				colModel : [ {
					display : 'Data',
					name : 'data',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Movimento',
					name : 'movimento',
					width : 200,
					sortable : true,
					align : 'left'
				}, {
					display : 'Entrada',
					name : 'entrada',
					width : 100,
					sortable : true,
					align : 'center'
				}, {
					display : 'Saída',
					name : 'saida',
					width : 100,
					sortable : true,
					align : 'center'
				}, {
					display : 'Saldo Parcial',
					name : 'saldoParcial',
					width : 110,
					sortable : true,
					align : 'right'
				}],
				sortname : "data",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 700,
				height : 180
			});
		},
		pesquisar : function() {
			alert("pesquisar");
			$(".consultaEdicoesFechadasGrid").flexOptions({
				url: contextPath + '/estoque/edicoesFechadas/pesquisar',
				params: [],		
			    newp: 1,
			});
			$(".consultaEdicoesFechadasGrid").flexReload();
		},
		popup_detalhes : function() {
			$( "#dialog-detalhes" ).dialog({
				resizable: false,
				height:440,
				width:750,
				modal: true,
				buttons: {
					"Fechar": function() {
						$( this ).dialog( "close" );
						
					},
				}
			});
		}
}

$(function() {
	$( "#filtroEdicoesFechadasDTO.dataDe" ).datepicker({
		showOn: "button",
		buttonImage: contextPath + "/images/calendar.gif",
		buttonImageOnly: true,
		dateFormat: 'dd/mm/yy',
		defaultDate: new Date()
	});

	$("#filtroEdicoesFechadasDTO.dataDe").mask("99/99/9999");

	$( "#filtroEdicoesFechadasDTO.dataAte" ).datepicker({
		showOn: "button",
		buttonImage: contextPath + "/images/calendar.gif",
		buttonImageOnly: true,
		dateFormat: 'dd/mm/yy',
		defaultDate: new Date()
	});

	$("#filtroEdicoesFechadasDTO.dataAte").mask("99/99/9999");
	
});
