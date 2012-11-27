
var EmissaoCEController = $.extend(true, {
	
	emissoes : [],
	fornecedoresSelecionados : [],
	
	init : function() {
		
		$("#dataDe", this.workspace).mask("99/99/9999");
		
		$( "#dataDe" , this.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#dataAte", this.workspace).mask("99/99/9999");
		
		$( "#dataAte" , this.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});		
		
		$("#cotaDe", this.workspace).numeric();
		
		$("#cotaAte", this.workspace).numeric();
		
		this.inicializarGrids();
	},
	
	cliquePesquisar : function() {
		
		var data = [];
		
		data.push({name:'filtro.dtRecolhimentoDe',		value: this.get("dataDe")});
		data.push({name:'filtro.dtRecolhimentoAte',		value: this.get("dataAte")});
		data.push({name:'filtro.idBoxDe',				value: this.get("boxDe")});
		data.push({name:'filtro.idBoxAte',				value: this.get("boxAte")});
		data.push({name:'filtro.numCotaDe',				value: this.get("cotaDe")});
		data.push({name:'filtro.numCotaAte',			value: this.get("cotaAte")});
		data.push({name:'filtro.idRoteiro',				value: this.get("roteiro")});
		data.push({name:'filtro.idRota',				value: this.get("rota")});
		data.push({name:'filtro.capa',					value: this.get("capa")});
		data.push({name:'filtro.personalizada',			value: this.get("personalizada")});
		
		$.each(this.fornecedoresSelecionados, function(index, row) {
			data.push({name:'filtro.fornecedores[' + index + ']',	value: row.id});
		});
		
		$(".ceEmissaoGrid", this.workspace).flexOptions({			
			url : contextPath + "/emissaoCE/pesquisar",
			dataType : 'json',
			params:data,
			preProcess: this.processaRetornoPesquisa
		});
		
		$(".ceEmissaoGrid", this.workspace).flexReload();
		
	},
	
	processaRetornoPesquisa : function(result) {
		
		if (result.mensagens) {

			exibirMensagem(
					result.mensagens.tipoMensagem, 
					result.mensagens.listaMensagens
			);
			
			$(".grids", this.workspace).hide();

			return result;
		}
		
		$.each(result.rows, function(index, row) {
	
			if(!row.cell.nomeCota){
				row.cell.nomeCota = "";
			}	
		});
		
		$(".grids", this.workspace).show();
		
		return result;
	},
	
	
	/**
	 * Atribui valor a um campo da tela
	 * Obs: Checkboxs devem ser atribuidos com o valor de true ou false
	 * 
	 * @param campo - Campo a ser alterado
	 * @param value - valor
	 */
	set : function(campo,value) {
				
		var elemento = $("#" + campo);
		
		if(elemento.attr('type') == 'checkbox') {
			
			if(value) {
				elemento.attr('checked','checked');
			} else {
				elemento.removeAttr('checked');
			}
						
		} else {
			elemento.val(value);
		}
	},
	
	/**
	 * Obtém valor de elemento da tela
	 * @param campo - de onde o valor será obtido
	 */
	get : function(campo) {
		
		var elemento = $("#" + campo , this.workspace);
		
		if(elemento.attr('type') == 'checkbox') {
			return (elemento.attr('checked') == 'checked') ;
		} else {
			return elemento.val();
		}
		
	},
	
	gerarFornecedoresSelecionados : function() {
		
		var cellFornecedores = '';
				
		$.each(this.fornecedoresSelecionados, function(index,fornecedor){
			
			cellFornecedores += '<div class="forncedoresSel">' +
								   '<label>'+ fornecedor.nome +'</label>' +
								   '<a href="javascript:;" onclick="EmissaoCEController.removerFornecedor('+index+');">' +
								   		'<img src="'+ contextPath +'/images/ico_excluir.gif" width="15" height="15" alt="Excluir" border="0" />' +
								   '</a>' +
							   '</div>';
		});
		
		$('#fornecedoresSelecionados', this.workspace).html(cellFornecedores);
		
	},
	
	getFornecedoresSelecionados : function() {
		
		$.each( $('#selectFornecedores', this.workspace).val(), function(index, row) {
			EmissaoCEController.fornecedoresSelecionados.push({id: row.split('_')[0] , nome: row.split('_')[1]});
		});
	},
	
	removerFornecedor : function(index) {
		this.fornecedoresSelecionados.splice(index,1);
		this.gerarFornecedoresSelecionados();
	},
	
	ativarPersonalizada : function(elemento) {
		if(elemento.checked==true)
			$('.personalizada', this.workspace).show();
		else
			$('.personalizada', this.workspace).hide();
	},
	
	popup_pesq_fornecedor : function() {
		
		 	$("#selectFornecedores", this.workspace).val(this.fornecedoresSelecionados);
		
			$( "#dialog-pesq-fornecedor", this.workspace).dialog({
				resizable: false,
				height:300,
				width:500,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						EmissaoCEController.getFornecedoresSelecionados();
						EmissaoCEController.gerarFornecedoresSelecionados();
						
						$( this ).dialog( "close" );
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				form: $("#dialog-pesq-fornecedor", this.workspace).parents("form")
			});
	},
	
	inicializarGrids : function() {
		
		$(".ceEmissaoGrid", this.workspace).flexigrid({
			colModel : [ {
				display : 'Cota',
				name : 'numCota',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 530,
				sortable : true,
				align : 'left'
			}, {
				display : 'Qtde. Exemplares',
				name : 'qtdeExemplares',
				width : 130,
				sortable : true,
				align : 'center'
			}, {
				display : 'Valor Total CE R$',
				name : 'vlrTotalCe',
				width : 130,
				sortable : true,
				align : 'right'
			}],
				width : 960,
				height : 'auto',
				sortname : "nomeCota",
				sortorder : "asc"
			});
			
	}
	
}, BaseController);

$(function() {
	EmissaoCEController.init();
				
});