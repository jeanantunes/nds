var MANTER_COTA = {
	
    numeroCota:"",
    idCota:1,
    
    formDataPesquisa: function(){
		
		var formData = [ {name:"numCota",value:$("#numCota").val()},
		                 {name:"nomeCota",value:$("#descricaoPessoa").val()},
			             {name:"numeroCpfCnpj",value:$("#txtCPF_CNPJ").val()}
			            ];
		return formData;
	},
	
    carregarTelefones:function(){
    	
    	COTA.carregarTelefones();
    },
    
    carregaFinanceiroCota:function (){
    	carregaFinanceiro(MANTER_COTA.idCota);
    },
    
    carregarEnderecos: function(){
    	
    	ENDERECO_COTA.popularGridEnderecos();
    },

	limparFormsTabs: function () {
		
		ENDERECO_COTA.limparFormEndereco();
		COTA.limparCamposTelefone();
	},
	
	carregarPDV : function (){
		PDV.idCota = MANTER_COTA.idCota;
		PDV.pesquisarPdvs(MANTER_COTA.idCota);
	},
	
	carregarFornecedores:function(idFornecedor,idFornecedorSelecionado){
		
		$.postJSON(contextPath + "/cadastro/cota/obterFornecedores",
				"idCota="+ MANTER_COTA.idCota, 
				function(result){
					
					if(result){
						MANTER_COTA.montarCombo(result,idFornecedor);
					}
				},null,true
		);
		
		$.postJSON(contextPath + "/cadastro/cota/obterFornecedoresSelecionados",
				"idCota="+ MANTER_COTA.idCota, 
				function(result){
				
					if(result){
						MANTER_COTA.montarCombo(result,idFornecedorSelecionado);
					}
				},null,true
		);
	
	},
	
	executarPreProcessamento:function (resultado){
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$("#grids").hide();

			return resultado.tableModel;
		}
		
		// Monta as colunas com os inputs do grid
		$.each(resultado.rows, function(index, row) {
			
			var linkEdicao = '<a href="javascript:;" onclick="MANTER_COTA.editar('+ row.cell.numero +');" style="cursor:pointer">' +
				 '<img src="'+ contextPath +'/images/ico_editar.gif" hspace="5" border="0px" title="Editar Cota" />' +
				 '</a>';			
			 
			var linkExclusao ='<a href="javascript:;" onclick="MANTER_COTA.exibirDialogExclusao('+ row.cell.numero +' );" style="cursor:pointer">' +
                 '<img src="'+ contextPath +'/images/ico_excluir.gif" hspace="5" border="0px" title="Excluir Cota" />' +
                  '</a>';		 					 
			
             row.cell.acao = linkEdicao + linkExclusao; 
		});
		
		$("#grids").show();
		
		return resultado;
	},
	
	pesquisar:function(){
		
		$(".pessoasGrid").flexOptions({
			url: contextPath + "/cadastro/cota/pesquisarCotas",
			params: MANTER_COTA.formDataPesquisa(),newp: 1
		});
		
		$(".pessoasGrid").flexReload();
	},
	
	exibirDialogExclusao:function (idCota){
		
		$("#dialog-excluirCota" ).dialog({
			resizable: false,
			height:'auto',
			width:250,
			modal: true,
			buttons: {
				"Confirmar": function() {
					MANTER_COTA.excluir(idCota);
					$( this ).dialog( "close" );
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	},
	
	editar:function(numeroCota){
		
		MANTER_COTA.numeroCota = numeroCota;
		
		$.postJSON(contextPath + "/cadastro/cota/editar",
				"numeroCota="+numeroCota, 
				function(result){
					
				if(result == 'true'){
					COTA_CPF.popup_cpf();
				}
				else {
					COTA_CNPJ.popup_cnpj();
				}
				
			}
		);
		
	},
	
	excluir:function(numeroCota){
		
		$.postJSON(contextPath + "/cadastro/cota/excluir",
				"numeroCota="+numeroCota, 
				function(){
					MANTER_COTA.pesquisar();
				}
		);
	},
	
	montarCombo:function(result,idCombo){
	
		var comboClassificacao =  montarComboBox(result, false);
		
		$(idCombo).html(comboClassificacao);
	},
	
	salvarFornecedores: function(fornecedores){
		
		$.postJSON(
				contextPath + "/cadastro/cota/salvarFornecedores",
				fornecedores + 
				"idCota="+ MANTER_COTA.idCota, 
				function(){},
				null,
				true
		);
	}
};

var COTA_CNPJ = {
	
	novoCNPJ:function(){

		$.postJSON(
				contextPath + "/cadastro/cota/incluirNovoCNPJ",
				null, 
				function(result){
					
					var dados = result;
					
					$("#dataInclusaoCPF").val(dados.dataInicioAtividade);
					$("#numeroCotaCPF").val(dados.numeroSugestaoCota);
					$("#statusCPF").val(dados.status);
					
					MANTER_COTA.montarCombo(dados.listaClassificacao,"#classificacaoCNPJ");
					
					COTA_CNPJ.popup_cnpj();
				}
		);
	},
		
	popup_cnpj: function() {
		
		$('input[id^="historico"]').numeric();
		
		$('input[id^="historicoPorcentagem"]').mask("99.99");
		
		$('input[id^="periodoCota"]').mask("99/99/9999");
		
		$('input[id^="periodoCota"]').datepicker({
			showOn: "button",
			buttonImage: contextPath+"/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		
		$( "#dialog-cnpj" ).dialog({
			resizable: false,
			height:590,
			width:950,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	},
	
	salvarDadosBasico:function (){
		
		var formData = $("#formDadosBasicoCnpj").serializeArray();
		
		$.postJSON(contextPath + "/cadastro/cota/salvarCotaCNPJ",
				formData, 
				function(){},true
		);
	},
	
	salvarFornecedores:function(){
		
		var listaFornecedores ="";
		
		 $("#selectFornecedorSelecionado_option_cnpj option").each(function (index) {
			 listaFornecedores = listaFornecedores + "fornecedores["+index+"]="+ $(this).val() +"&";
		 });
		 
		 MANTER_COTA.salvarFornecedores(listaFornecedores);
	},
	
	salvarDesconto:function(){
		
	},
	
	carregarFornecedores:function(){
		
		MANTER_COTA.carregarFornecedores("#selectFornecedor_option_cnpj", "#selectFornecedorSelecionado_option_cnpj");
	}
};

var COTA_CPF = {
	
	novoCPF:function(){
		COTA_CPF.popup_cpf();
	},	
		
	popup_cpf: function () {

		$( "#dialog-cpf" ).dialog({
			resizable: false,
			height:590,
			width:950,
			modal: true,
			buttons: {
				"Confirmar": function() {
					//postarParametroCobranca();
					//salvarCota();
					$( this ).dialog( "close" );
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	}	
}