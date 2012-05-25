/**
 * 
 */

function InformeEncalhe(){
	
};


InformeEncalhe.prototype.initGrid =  function(){
	$(".consultaInformeEncalheGrid").flexigrid({
		dataType : 'json',
		colModel :  [ {
			display : 'Seq',
			name : 'sequenciaMatriz',
			width : 20,
			sortable : true,
			align : 'left'
		},{
			display : 'Código',
			name : 'codigoProduto	',
			width : 40,
			sortable : true,
			align : 'left'
		}, {

			display : 'Produto',
			name : 'descricaoProduto',
			width : 80,
			sortable : true,
			align : 'left'
		}, {

			display : 'Edição',
			name : 'numeroEdicao',
			width : 40,
			sortable : true,
			align : 'left'
		},{

			display : 'Chamada de Capa',
			name : 'slogan',
			width : 105,
			sortable : true,
			align : 'left'
		},{

			display : 'Código Barras',
			name : 'codigoDeBarras',
			width : 110,
			sortable : true,
			align : 'left'
		}, {

			display : 'Preço de Capa R$',
			name : 'precoVenda',
			width : 90,
			sortable : true,
			align : 'right'
		}, {

			display : 'Preço Desconto R$',
			name : 'precoDesconto',
			width : 90,
			sortable : true,
			align : 'right'
		}, {

			display : 'Data de Lançamento',
			name : 'dataLancamento',
			width : 110,
			sortable : true,
			align : 'center'
		},{
			display : 'Data Recolhimento',
			name : 'dataRecolhimento	',
			width : 90,
			sortable : true,
			align : 'left'
		}, {

			display : 'Ação',
			name : 'acao',
			width : 30,
			sortable : false,
			align : 'center'
		}],
		sortname : "Nome",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 180
	});

};