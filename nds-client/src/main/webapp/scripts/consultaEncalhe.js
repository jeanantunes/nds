var ConsultaEncalhe = $.extend(true, {

		init : function() {
			
			var colunas = ConsultaEncalhe.obterColModel();
			
			$("#cota", ConsultaEncalhe.workspace).numeric();
			
			$("#gridConsultaEncalhe", ConsultaEncalhe.workspace).flexigrid({
				
				dataType : 'json',
				preProcess:ConsultaEncalhe.executarPreProcessamento,
				//onSuccess:function(){$('input[id^="valorExemplarNota"]').numeric();},
				colModel : colunas,
				sortname : "codigoProduto",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 180
			});
			
			$('#dataRecolhimento', ConsultaEncalhe.workspace).datepicker({
				showOn: "button",
				buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
				buttonImageOnly: true,
				dateFormat: "dd/mm/yy"
			});
			
			$('#dataRecolhimento', ConsultaEncalhe.workspace).mask("99/99/9999");
			
		},
		
		pesquisar: function() {
			
			var dataRecolhimento 	= $("#dataRecolhimento", ConsultaEncalhe.workspace).val();
			var idFornecedor		= $("#idFornecedor", ConsultaEncalhe.workspace).val();
			var numeroCota			= $("#cota", ConsultaEncalhe.workspace).val();
			
			var formData = [
			        
			        {name:'dataRecolhimento', value: dataRecolhimento},
			        {name:'idFornecedor', value: idFornecedor},
			        {name:'numeroCota', value: numeroCota }
			];
			
			$("#gridConsultaEncalhe", ConsultaEncalhe.workspace).flexOptions({
				url: contextPath + "/devolucao/consultaEncalhe/pesquisar",
				params: formData
			});
			
			$("#gridConsultaEncalhe", ConsultaEncalhe.workspace).flexReload();

		},
	
		executarPreProcessamento: function(resultado) {
			
			//Verifica mensagens de erro do retorno da chamada ao controller.
			if (resultado.mensagens) {

				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				
				$(".grids", ConsultaEncalhe.workspace).hide();

				return resultado.tableModel;
			}
			
			$(".grids", ConsultaEncalhe.workspace).show();
			
			$("#qtdExemplarDemaisRecolhimentos", ConsultaEncalhe.workspace).val(resultado.qtdExemplarDemaisRecolhimentos);
			
			$("#qtdExemplarPrimeiroRecolhimento", ConsultaEncalhe.workspace).val(resultado.qtdExemplarPrimeiroRecolhimento);
			
			$("#qtdProdutoDemaisRecolhimentos", ConsultaEncalhe.workspace).val(resultado.qtdProdutoDemaisRecolhimentos);
			
			$("#qtdProdutoPrimeiroRecolhimento", ConsultaEncalhe.workspace).val(resultado.qtdProdutoPrimeiroRecolhimento);
			
			return resultado.tableModel;
			
		},
		
		obterColModel : function() {


				var colModel = [ {
					display : 'Código',
					name : 'codigoProduto',
					width : 60,
					sortable : true,
					align : 'left'
				}, {
					display : 'Produto',
					name : 'nomeProduto',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Edição',
					name : 'numeroEdicao',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : 'Preço Capa R$',
					name : 'precoVenda',
					width : 80,
					sortable : true,
					align : 'right'
				}, {
					display : 'Preço com Desc. R$',
					name : 'precoComDesconto',
					width : 110,
					sortable : true,
					align : 'right'
				}, {
					display : 'Reparte',
					name : 'reparte',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : 'Encalhe',
					name : 'encalhe',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : 'Fornecedor',
					name : 'fornecedor',
					width : 120,
					sortable : true,
					align : 'left'
				}, {
					display : 'Total R$',
					name : 'total',
					width : 90,
					sortable : true,
					align : 'right'
				}, {
					display : 'Recolhimento',
					name : 'recolhimento',
					width : 80,
					sortable : true,
					align : 'center'
				}];	
				
				return colModel;
		}
		
}, BaseController);
