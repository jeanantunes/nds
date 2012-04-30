var boxController = {
	urlBusca : contextPath + '/cadastro/box/busca.json',
	intGridBox: function(){
		$(".boxGrid").flexigrid({
			preProcess:function(data){
				if(typeof data.mensagens == "object") {
										
						exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
						
				}else{	
					$.each(data.rows, function(index, value) {						
							var idBox = value.cell.id;								
							var acao = '<a href="javascript:;" onclick="boxController.editar('+idBox+');"><img src="'+contextPath+'/images/ico_editar.gif" border="0" hspace="5" />';
							acao +='</a> <a href="javascript:;" onclick="boxController.excluir('+idBox+');""><img src="'+contextPath+'/images/ico_excluir.gif" hspace="5" border="0" /></a>';
							
							value.cell.acao = acao;	
					});
					
					
					return data;	
				}
				
			},
			dataType : 'json',
			colModel : [ {
				display : 'Box',
				name : 'codigo',
				width : 220,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 350,
				sortable : true,
				align : 'left'
			}, {
				display : 'Tipo de Box',
				name : 'tipoBox',
				width : 250,
				sortable : true,
				align : 'left'
			}, {
				display : 'A&ccedil;&atilde;o',
				name : 'acao',
				width : 60,
				sortable : false,
				align : 'left'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});

	},
	buscar: function(codigoBox, tipoBox, postoAvancado){
		$(".boxGrid").flexOptions({"url": this.urlBusca, params:[{name:"codigoBox",value:codigoBox},{name:"tipoBox",value:tipoBox},{name:"postoAvancado",value:false}]});	
		$(".boxGrid").flexReload();
	},
	bindButtons: function(){
		$("#btnPesquisar").click(function(){
			boxController.buscar($("#pesquisaCodigoBox").val(),$("#pesquisaTipoBox").val(),$("#boxPostoAvancado").val());
			$(".grids").show();
		});
	},
	init:function(){
		this.intGridBox();
		this.bindButtons();
	},
	editar:function(id){
		console.log(id);
	},
	excluir:function(id){
		console.log(id);
	}
	
};

