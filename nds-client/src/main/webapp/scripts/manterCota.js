var TAB_COTA = new TabCota('tabCota');

var MANTER_COTA = {
	
    numeroCota:"",
    idCota:"",
    tipoCotaSelecionada:"",
    tipoCota_CPF:"FISICA",
    tipoCota_CNPJ:"JURIDICA",

    formDataPesquisa: function(){
		
		var formData = [ {name:"numCota",value:$("#numCota").val()},
		                 {name:"nomeCota",value:$("#descricaoPessoa").val()},
			             {name:"numeroCpfCnpj",value:$("#txtCPF_CNPJ").val()}
			            ];
		return formData;
	},
	
	 carregarDadosCadastrais:function(){
	    
		 TAB_COTA.salvar = MANTER_COTA.salvarDadosCadastrais;
	 },
	
    carregarTelefones:function(){
    	
    	TAB_COTA.funcaoSalvar = MANTER_COTA.salvarTelefone;
    	COTA.carregarTelefones();
    },
    
    carregaFinanceiroCota:function (){
    	
    	carregaFinanceiro(MANTER_COTA.idCota);
    },
    
    carregarEnderecos: function(){
    	TAB_COTA.funcaoSalvar = MANTER_COTA.salvarEndereco;
    	ENDERECO_COTA.popularGridEnderecos();
    },
    
    carregarGarantias:function(){
    	
    },
    
    carregarDescontos:function(){
    	
    },
    
    carregarDistribuicao:function(){
    	TAB_COTA.funcaoSalvar = DISTRIB_COTA.salvar;
    	DISTRIB_COTA.carregarDadosDistribuicaoCota(MANTER_COTA.idCota);
    },
    
    carregarDadosSocio:function(){
    	
    },
    
	carregarPDV : function (){
		PDV.idCota = MANTER_COTA.idCota;
		PDV.pesquisarPdvs(MANTER_COTA.idCota);
	},
	
	carregarFornecedores:function(){
		
		TAB_COTA.funcaoSalvar = MANTER_COTA.salvarFornecedores;
		
		$.postJSON(contextPath + "/cadastro/cota/obterFornecedores",
				"idCota="+ MANTER_COTA.idCota, 
				function(result){
					
					if(result){
						MANTER_COTA.montarCombo(result,"#selectFornecedor_option_cnpj");
					}
				},null,true
		);
		
		$.postJSON(contextPath + "/cadastro/cota/obterFornecedoresSelecionados",
				"idCota="+ MANTER_COTA.idCota, 
				function(result){
				
					if(result){
						MANTER_COTA.montarCombo(result,"#selectFornecedorSelecionado_option_cnpj");
					}
				},null,true
		);
	
	},
	
	limparFormsTabs: function () {
		
		ENDERECO_COTA.limparFormEndereco();
		COTA.limparCamposTelefone();
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
			
			var paramCota =  "'" +row.cell.numero +"'," + "'"+ row.cell.idCota + "'" ;
			
			var linkEdicao = '<a href="javascript:;" onclick="MANTER_COTA.editar('+ paramCota +');" style="cursor:pointer">' +
				 '<img src="'+ contextPath +'/images/ico_editar.gif" hspace="5" border="0px" title="Editar Cota" />' +
				 '</a>';			
			 
			var linkExclusao ='<a href="javascript:;" onclick="MANTER_COTA.exibirDialogExclusao('+ row.cell.idCota +' );" style="cursor:pointer">' +
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
	
	editar:function(numeroCota,idCota){
		
		MANTER_COTA.numeroCota = numeroCota;
		MANTER_COTA.idCota = idCota;
		
		$.postJSON(contextPath + "/cadastro/cota/editar",
				"idCota="+idCota, 
				function(result){
				
					if(result){
						
						MANTER_COTA.montarCombo(result.listaClassificacao,"#classificacaoSelecionada");
						
						if(result.tipoPessoa == MANTER_COTA.tipoCota_CPF){
							COTA_CPF.editarCPF();
						}
						else {
							COTA_CNPJ.editarCNPJ(result);
						}
					}
			}
		);
		
	},
	
	excluir:function(idCota){
		
		$.postJSON(contextPath + "/cadastro/cota/excluir",
				"idCota="+idCota, 
				function(){
					MANTER_COTA.pesquisar();
				}
		);
	},
	
	montarCombo:function(result,idCombo){
	
		var comboClassificacao =  montarComboBox(result, false);
		
		$(idCombo).html(comboClassificacao);
	},
	
	salvarFornecedores: function(callback){
	
		var fornecedores ="";
		
		 $("#selectFornecedorSelecionado_option_cnpj option").each(function (index) {
			 fornecedores = fornecedores + "fornecedores["+index+"]="+ $(this).val() +"&";
		 });
		
		$.postJSON(
				contextPath + "/cadastro/cota/salvarFornecedores",
				fornecedores + 
				"idCota="+ MANTER_COTA.idCota, 
				callback,
				null,
				true
		);
		
		return false;
	},
	
	salvarDadosCadastrais:function(callback){
		
		if(MANTER_COTA.tipoCotaSelecionada == MANTER_COTA.tipoCota_CNPJ){
			
			COTA_CNPJ.salvarDadosBasico(callback);
		}
		else {
			
		}
		
		return false;
	},
	
	salvarEndereco:function(callback){
		
		$.postJSON(
				contextPath + "/cadastro/cota/salvarEnderecos",
				"idCota="+ MANTER_COTA.idCota, 
				callback,
				null,
				true
		);
		
		return false;
	},
	
	salvarTelefone:function(callback){
		
		$.postJSON(
				contextPath + "/cadastro/cota/salvarTelefones",
				"idCota="+ MANTER_COTA.idCota, 
				callback,
				null,
				true
		);
		
		return false;
	},
	
	salvarDesconto:function(callback){
		
		var descontos = [];
		
		$.postJSON(
				contextPath + "/cadastro/cota/salvarDescontos",
				descontos + 
				"&idCota="+ MANTER_COTA.idCota, 
				callback,
				null,
				true
		);
		
		return false;
	},
	
	validarEmail : function (idInput)	{
		er = /^[a-zA-Z0-9][a-zA-Z0-9\._-]+@([a-zA-Z0-9\._-]+\.)[a-zA-Z-0-9]{2}/;
		
		if($(idInput).val().length == 0){
			return;
		}
		
		if(!er.exec($(idInput).val())) {
			$(idInput).focus();
			exibirMensagemDialog("WARNING",["E-mail inv&aacutelido."],"");
		}
	},
	
	popupCota: function() {
		
		//Define a função salvar inicial ao abrir o dialog de cadastro de cota 
		TAB_COTA.funcaoSalvar = MANTER_COTA.salvarDadosCadastrais;
		
		$('input[id^="historico"]').numeric();
		
		$("#numeroCnpj").mask("99.999.999/9999-99");
		
		$('input[id^="periodoCota"]').mask("99/99/9999");
		
		$('input[id^="periodoCota"]').datepicker({
			showOn: "button",
			buttonImage: contextPath+"/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		
		$( "#dialog-cota" ).dialog({
			resizable: false,
			height:590,
			width:950,
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					if(TAB_COTA.funcaoSalvar){						
						if(TAB_COTA.funcaoSalvar(TAB_COTA.confirmar) == false) {
							return;
						}
					}
					
					$( this ).dialog( "close" );
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	}
};

var COTA_CNPJ = {	
		
	novoCNPJ:function(){
		
		$("#tabCota" ).tabs( "option", "enabled", [] );
		
		MANTER_COTA.tipoCotaSelecionada = MANTER_COTA.tipoCota_CNPJ;
		MANTER_COTA.idCota="";
		MANTER_COTA.numeroCota="";
		
		$.postJSON(
				contextPath + "/cadastro/cota/incluirNovoCNPJ",
				null, 
				function(result){
					
					var dados = result;
					
					$("#dataInclusao").html(dados.dataInicioAtividade);
					$("#numeroCota").val(dados.numeroSugestaoCota);
					$("#status").val(dados.status);
					
					MANTER_COTA.montarCombo(dados.listaClassificacao,"#classificacaoSelecionada");
					
					MANTER_COTA.popupCota();
				}
		);
	},
	
	editarCNPJ:function(result){
		
		$("#tabCota" ).tabs( "option", "enabled", [9] );
		
		$("#numeroCota").val(result.numeroCota);
		$("#email").val(result.email);
		$("#status").val(result.status);
		$("#dataInclusao").html(result.dataInclusao.$);
		$("#razaoSocial").val(result.razaoSocial);
		$("#nomeFantasia").val(result.nomeFantasia);
		$("#numeroCnpj").val(result.numeroCnpj);
		$("#inscricaoEstadual").val(result.inscricaoEstadual);
		$("#inscricaoMunicipal").val(result.inscricaoMunicipal);
		$("#emailNF").val(result.emailNF);
		$("#emiteNFE").attr("checked", (result.emiteNFE == true)?"checked":null);
		$("#classificacaoSelecionada").val(result.classificacaoSelecionada);
		$("#historicoPrimeiraCota").val(result.historicoPrimeiraCota);
		$("#historicoPrimeiraPorcentagem").val( eval( result.historicoPrimeiraPorcentagem));
		$("#historicoSegundaCota").val(result.historicoSegundaCota);
		$("#historicoSegundaPorcentagem").val( eval( result.historicoSegundaPorcentagem));
		$("#historicoTerceiraCota").val(result.historicoTerceiraCota);
		$("#historicoTerceiraPorcentagem").val( eval( result.historicoTerceiraPorcentagem));
		
		if(result.inicioPeriodo){
			$("#periodoCotaDe").val(result.inicioPeriodo.$);
		}
		
		if(result.fimPeriodo){
			$("#periodoCotaAte").val(result.fimPeriodo.$);
		}
		
		MANTER_COTA.tipoCotaSelecionada = MANTER_COTA.tipoCota_CNPJ;
		
		MANTER_COTA.popupCota();
	},
		
	salvarDadosBasico:function (callback){

		var formData = $("#formDadosBasicoCnpj").serializeArray();
		
		formData.push({name:"cotaDTO.idCota",value: MANTER_COTA.idCota});
		
		$.postJSON(contextPath + "/cadastro/cota/salvarCotaCNPJ",
				formData , 
				function(result){
			
					MANTER_COTA.idCota = result.idCota;
					MANTER_COTA.numeroCota = result.numeroCota;
					callback();
				},
				null,
				true
		);
	},
	
	carregarDadosCNPJ: function(idCampo){
		
		$.postJSON(contextPath + "/cadastro/cota/obterDadosCNPJ",
				"numeroCnpj="+$(idCampo).val() , 
				function(result){

					if(result.email){$("#email").val(result.email);}
					
					if(result.razaoSocial){$("#razaoSocial").val(result.razaoSocial);}
					
					if(result.nomeFantasia){$("#nomeFantasia").val(result.nomeFantasia);}
					
					if(result.inscricaoEstadual){$("#inscricaoEstadual").val(result.inscricaoEstadual);}
					
					if(result.inscricaoMunicipal){$("#inscricaoMunicipal").val(result.inscricaoMunicipal);}

				},
				null,
				true
		);
	}
};

var COTA_CPF = {
	
	novoCPF:function(){
		
		alert("Operação em desenvolvimento!!");
		/*
		$("#tabCota" ).tabs( "option", "disabled", [9] );
		
		MANTER_COTA.tipoCotaSelecionada = MANTER_COTA.tipoCota_CPF;
		MANTER_COTA.popupCota();*/
	},

	editarCPF:function(){
		
		$("#tabCota" ).tabs( "option", "disabled", [9] );
		
		MANTER_COTA.tipoCotaSelecionada = MANTER_COTA.tipoCota_CPF;
		MANTER_COTA.popupCota();
	}
		
};