var romaneiosController = $.extend(true, {
	
	init : function () {
		$(".romaneiosGrid", romaneiosController.workspace).flexigrid({
			preProcess: romaneiosController.executarPreProcessamento,
			onSuccess: romaneiosController.carregarQuantidadeCotas,
			dataType : 'json',
			colModel : [ {
				display : 'Nº. NE',
				name : 'numeroNotaEnvio',
				width : 80,
				sortable : true,
				align : 'left'
			},{
				display : 'Cota',
				name : 'numeroCota',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 310,
				sortable : true,
				align : 'left'
			}, {
				display : 'Endereço',
				name : 'endereco',
				width : 450,
				sortable : false,
				align : 'left'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 150
		});
		
		$("#idRoteiro", romaneiosController.workspace).multiselect({
			selectedList : 6
		})
		
		$( "#romaneio-dataLancamento", romaneiosController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath +"/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat : 'dd/mm/yy'
		});
		
		$("#romaneio-dataLancamento", romaneiosController.workspace).mask("99/99/9999");
		
		$("#selectProdutos", romaneiosController.workspace).multiselect({
			selectedList : 6
		});
	},
	
	mostrar : function(){
		$(".grids", romaneiosController.workspace).show();
	},

	confirmar : function(){
			$(".dados", romaneiosController.workspace).show();
	},
	
	pesqEncalhe : function(){
			$(".dadosFiltro", romaneiosController.workspace).show();		
	},

	pesquisar : function(){
		
		var params = [];
		
		params.push({name: 'filtro.data',      	value: $("#romaneio-dataLancamento", romaneiosController.workspace).val()});
		params.push({name: 'filtro.idBox', 	value:$('#codigoBox', romaneiosController.workspace).val()});
		//params.push({name: 'filtro.idRoteiro', 	value:$('#idRoteiro', romaneiosController.workspace).val()});
		params.push({name: 'filtro.idRota',    	value:$('#idRota', romaneiosController.workspace).val()});
		params.push({name: 'filtro.nomeRota',	value: $('#idRota option:selected', romaneiosController.workspace).text()});
		params.push({name: 'filtro.nomeRoteiro',value: $('#idRoteiro option:selected', romaneiosController.workspace).text()});
		params.push({name: 'filtro.nomeBox',  	value: $('#codigoBox option:selected', romaneiosController.workspace).text()});
		
		var idRoteiros = $("#idRoteiro", romaneiosController.workspace).val();
		
		if (idRoteiros){
			$.each(idRoteiros, function(index, value){
				if(value == null || value == "") {
					console.log('olá')
				} else {
					
					params.push({name: 'filtro.idRoteiro['+index+']', value: value});
				}
			});
		}
		
		var produtos = $("#selectProdutos", romaneiosController.workspace).val();
		
		if (produtos){
			$.each(produtos, function(index, value){
				params.push({name: 'filtro.produtos['+index+']', value: value});
			});
		}
		
		$(".romaneiosGrid", romaneiosController.workspace).flexOptions({
			url: contextPath + "/romaneio/pesquisarRomaneio",
			dataType : 'json',
			params: params
		});
		
		$(".romaneiosGrid", romaneiosController.workspace).flexReload();
	},

	executarPreProcessamento : function(resultado) {
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids", romaneiosController.workspace).hide();
			
			romaneiosController.esconderBotoes();

			return resultado;
		}
		
		$.each(resultado.rows, function(index, value){
			
			if (!value.cell.numeroNotaEnvio){
				
				value.cell.numeroNotaEnvio = '';
			}
		});
		
		$(".grids", romaneiosController.workspace).show();
		
		romaneiosController.mostrarBotoes();
		
		return resultado;
	},
	
	carregarQuantidadeCotas : function(){
		
		$.postJSON(contextPath + "/romaneio/pesquisarQuantidadeCotasEntrega",
			null,
			function(result) {
				
				if (result.mensagens) {
	
					exibirMensagem(
						result.mensagens.tipoMensagem, 
						result.mensagens.listaMensagens
					);
				}
				
				$("#totalCotas", romaneiosController.workspace).text(result ? result.int : "");
			}
		);
	},
	
	pesquisarProdutos : function(){
		
		var data = $("#romaneio-dataLancamento", romaneiosController.workspace).val();
		
		if (data){
		
			$.postJSON(contextPath + "/romaneio/carregarProdutosDataLancamento",
				{data:data},
				function(result) {
					
					if (result.mensagens) {
		
						exibirMensagem(
							result.mensagens.tipoMensagem, 
							result.mensagens.listaMensagens
						);
						
						$(".grids", romaneiosController.workspace).hide();
					}
					
					var itens = '';
					
					if (result){
						$.each(result, function(index, result) {
							itens += '<option value="'+ result.key.$ +'">'+ (result.value ? result.value.$ : '' ) +'</option>';
						});
					}
					
					$("#selectProdutos", romaneiosController.workspace).multiselect("destroy");
					$("#selectProdutos", romaneiosController.workspace).html("");
					$("#selectProdutos", romaneiosController.workspace).append(itens).multiselect();
				}
			);
		} else {
			
			$("#selectProdutos", romaneiosController.workspace).multiselect("destroy");
			$("#selectProdutos", romaneiosController.workspace).html("");
			$("#selectProdutos", romaneiosController.workspace).multiselect();
		}
	},
	
	recarregarComboRotas:function(idRoteiro){
		
		$.postJSON(contextPath + "/cadastro/roteirizacao/recarregarListaRotas",
				[{name:"roteiro",value:idRoteiro},
				 {name:"idBox",value:$("#codigoBox", romaneiosController.workspace).val()}], function(result){
			
			var comboRotas =  montarComboBox(result, true);
			
			$("#idRota", romaneiosController.workspace).html(comboRotas);	
		});
	},
	
	recarregarComboRoteiroRotas:function(idBox){
		
		$.postJSON(contextPath + "/cadastro/roteirizacao/recarregarRoteiroRota",
				[{name:"idBox",value:idBox}], function(result){
			
			var comboRotas =  montarComboBoxCustomJson(result.rotas, true);
			var comboRoteiros = montarComboBoxCustomJson(result.roteiros, true);
			
			$("#idRota", romaneiosController.workspace).html(comboRotas);
			$("#idRoteiro", romaneiosController.workspace).html(comboRoteiros);
		});
	},
	
	mostrarBotoes : function() {
		
		$(".areaBts", romaneiosController.workspace).find("span").show();
	},
	
	esconderBotoes : function() {
		
		$(".areaBts", romaneiosController.workspace).find("span").hide();
	},
	
	gerarArquivo : function (fileType) {
    	
    	var path = contextPath + "/romaneio/gerarArquivoRot";
    	
    	var params = [];
		
    	var produtos = $("#selectProdutos", romaneiosController.workspace).val();
    	
    	var idRoteiros = $("#idRoteiro", romaneiosController.workspace).val();
    	
    	if(!idRoteiros || !produtos) {
			exibirMensagem("WARNING", ["Favor selecionar 'ROTEIROS / PRODUTO' ."], "");
			return false;
    	}
    	 	
    	
		params.push({name: 'filtro.data',      	value: $("#romaneio-dataLancamento", romaneiosController.workspace).val()});
		params.push({name: 'filtro.idBox', 	value:$('#codigoBox', romaneiosController.workspace).val()});
		//params.push({name: 'filtro.idRoteiro', 	value:$('#idRoteiro', romaneiosController.workspace).val()});
		params.push({name: 'filtro.idRota',    	value:$('#idRota', romaneiosController.workspace).val()});
		params.push({name: 'filtro.nomeRota',	value: $('#idRota option:selected', romaneiosController.workspace).text()});
		params.push({name: 'filtro.nomeRoteiro',value: $('#idRoteiro option:selected', romaneiosController.workspace).text()});
		params.push({name: 'filtro.nomeBox',  	value: $('#codigoBox option:selected', romaneiosController.workspace).text()});
		
		if (idRoteiros){
			$.each(idRoteiros, function(index, value){
				if(value == null || value == "") {
					console.log('olá')
				} else {
					
					params.push({name: 'filtro.idRoteiro['+index+']', value: value});
				}
			});
		}
		
		
		
		if (produtos){
			$.each(produtos, function(index, value){
				params.push({name: 'filtro.produtos['+index+']', value: value});
			});
		}
    	
    	
    	$.fileDownload(path, {
			httpMethod : "POST",
			data : params,
			successCallback: function(result) {
				if (result.mensagens) {
					exibirMensagem(
						result.mensagens.tipoMensagem, 
						result.mensagens.listaMensagens
					);
				}			
			},
            failCallback: function(result) {
	
        		res = $.parseJSON($(result).text());
        		if ((typeof res != "undefined") && (typeof res.mensagens != "undefined")) {
        			
					exibirMensagem(
							res.mensagens.tipoMensagem, 
							res.mensagens.listaMensagens
					);
				}	
			}
		});
    	
    },
	
	
}, BaseController);

//@ sourceURL=romaneios.js
