var TAB_COTA = new TabCota('tabCota');

var MANTER_COTA = {
	
    numeroCota:"",
    idCota:"",
    tipoCotaSelecionada:"",
    tipoCota_CPF:"FISICA",
    tipoCota_CNPJ:"JURIDICA",
    fecharModalCadastroCota:false,

    formDataPesquisa: function(){
		
		var formData = [ {name:"numCota",value:$("#numCota").val()},
		                 {name:"nomeCota",value:$("#descricaoPessoa").val()},
			             {name:"numeroCpfCnpj",value:$("#txtCPF_CNPJ").val()}
			            ];
		return formData;
	},
	
	 carregarDadosCadastrais:function(){
	    
		 TAB_COTA.funcaoSalvar = MANTER_COTA.salvarDadosCadastrais;
	 },
	
    carregarTelefones:function(){
    	
    	TAB_COTA.funcaoSalvar = MANTER_COTA.salvarTelefone;
    
		$.postJSON(contextPath + "/cadastro/cota/recarregarTelefone",
				   "idCota="+MANTER_COTA.idCota,function(){
			COTA.carregarTelefones();
		},null,true,null);
    },
    
    carregaFinanceiroCota:function (){
    	
    	TAB_COTA.funcaoSalvar = postarParametroCobranca;
    	carregaFinanceiro(MANTER_COTA.idCota);
    },
    
    carregarEnderecos: function(){
    	TAB_COTA.funcaoSalvar = MANTER_COTA.salvarEndereco;
    	
    	$.postJSON(contextPath + "/cadastro/cota/recarregarEndereco",
    			"idCota="+MANTER_COTA.idCota,function(){
    		
    		ENDERECO_COTA.popularGridEnderecos();
    	},null,true,null);
    },
    
    carregarGarantias:function(){
    	
    },
    
    carregarDescontos:function(){
    	
    	TAB_COTA.funcaoSalvar = COTA_DESCONTO.salvarDesconto;
    	COTA_DESCONTO.carregarDescontoCota();
    },
    
    carregarDistribuicao:function(){
    	
    	TAB_COTA.funcaoSalvar = DISTRIB_COTA.salvar;
    	DISTRIB_COTA.carregarDadosDistribuicaoCota(MANTER_COTA.idCota);
    },
    
    carregarDadosSocio:function(){
    	
    	TAB_COTA.funcaoSalvar = SOCIO_COTA.salvarSocios;
    	SOCIO_COTA.carregarSociosCota();
    },
    
	carregarPDV : function (){
		
		PDV.idCota = MANTER_COTA.idCota;
		PDV.pesquisarPdvs(MANTER_COTA.idCota);
	},
	
	carregarFornecedores:function(){
		
		TAB_COTA.funcaoSalvar = COTA_FORNECEDOR.salvarFornecedores;
		COTA_FORNECEDOR.carregarFornecedores();
	},
	
	limparFormsTabs: function () {
		
		ENDERECO_COTA.limparFormEndereco();
		COTA.limparCamposTelefone();
		
		$.postJSON(contextPath + "/cadastro/cota/cancelar",null, null);
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
		
		MANTER_COTA.mudarNomeModalCadastro("Cota- " +  numeroCota);
		
		MANTER_COTA.fecharModalCadastroCota = false;
		
		$.postJSON(contextPath + "/cadastro/cota/editar",
				"idCota="+idCota, 
				function(result){
				
					if(result){
						
						if(result.tipoPessoa == MANTER_COTA.tipoCota_CPF){	
							MANTER_COTA.montarCombo(result.listaClassificacao,"#classificacaoSelecionadaCPF");
							COTA_CPF.editarCPF(result);
						}
						else {
							MANTER_COTA.montarCombo(result.listaClassificacao,"#classificacaoSelecionada");
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

	salvarDadosCadastrais:function(){
		
		if(MANTER_COTA.tipoCotaSelecionada == MANTER_COTA.tipoCota_CNPJ){
			
			COTA_CNPJ.salvarDadosBasico();
		}
		else {
			
			COTA_CPF.salvarDadosBasico();
		}
	},
	
	salvarEndereco:function(){
		
		$.postJSON(
				contextPath + "/cadastro/cota/salvarEnderecos",
				"idCota="+ MANTER_COTA.idCota, 
				null,
				null,
				true
		);
		
	},
	
	salvarTelefone:function(){
		
		$.postJSON(
				contextPath + "/cadastro/cota/salvarTelefones",
				"idCota="+ MANTER_COTA.idCota, 
				MANTER_COTA.carregarTelefones,
				null,
				true
		);
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
		
		$("#numeroCPF").mask("999.999.999-99");
				
		$('input[id^="periodoCota"]').mask("99/99/9999");
		
		$('input[id^="dataNascimento"]').mask("99/99/9999");
		
		$('input[id^="periodoCota"]').datepicker({
			showOn: "button",
			buttonImage: contextPath+"/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		
		$('input[id^="dataNascimento"]').datepicker({
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
			buttons: [
			         {id:"btn_confirmar_cota",text:"Confirmar",
		        	  click: function() {
								if(TAB_COTA.funcaoSalvar)
									TAB_COTA.funcaoSalvar();
		        	  		}
			         },
		        	{id:"btn_cancelar_cota",text:"Cancelar",
			         click:function(){
		        				MANTER_COTA.fecharModalCadastroCota = false;
		        				$( this ).dialog( "close" );
		        		}	  
		        	}  
			],
			beforeClose: function(event, ui) {
				
				clearMessageDialogTimeout();
				
				if (!MANTER_COTA.fecharModalCadastroCota){
					
					MANTER_COTA.cancelarCadastro();
					
					return MANTER_COTA.fecharModalCadastroCota;
				}
				
				MANTER_COTA.limparFormsTabs();
				
				return MANTER_COTA.fecharModalCadastroCota;
				
			}
		});
	},
	
	cancelarCadastro:function(){
		
		$("#dialog-cancelar-cadastro-cota").dialog({
			resizable: false,
			height:150,
			width:600,
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					MANTER_COTA.fecharModalCadastroCota = true;
					
					$("#dialog-close").dialog("close");
					$("#dialog-cancelar-cadastro-cota").dialog("close");
					$("#dialog-cota").dialog("close");
					
				},
				"Cancelar": function() {
					MANTER_COTA.fecharModalCadastroCota = false;
					$(this).dialog("close");
				}
			}
		});
	},
	
	validarCotaHistoricoBase:function(idCampoNumeroCota, idCampoPorcentagem){
		
		if($(idCampoNumeroCota).val().length > 0){
			
			$.postJSON(
					contextPath + "/cadastro/cota/validarNumeroCotaHistoricoBase",
					"&numeroCota="+ $(idCampoNumeroCota).val(), 
					null,
					function(){
						$(idCampoNumeroCota).focus();
						$(idCampoNumeroCota).val("");
						$(idCampoPorcentagem).val("");
					},	
					true
			);
		}
		else {
			$(idCampoPorcentagem).val("");
		}	
	},
	
	mudarNomeModalCadastro:function(value){
		
		$("#ui-dialog-title-dialog-cota").html(value);
	}
};

var COTA_DESCONTO = {
		
		salvarDesconto:function(){
			
			var descontos = "";
			
			 $("#selectDesconto option").each(function (index) {
				 descontos = descontos + "descontos["+index+"]="+ $(this).val() +"&";
			 });
			
			$.postJSON(
					contextPath + "/cadastro/cota/salvarDescontos",
					descontos + 
					"&idCota="+ MANTER_COTA.idCota, 
					null,
					null,
					true
			);
		},
		
		carregarDescontoCota:function(){
			
			$.postJSON(contextPath + "/cadastro/cota/obterDescontos",
					"idCota="+ MANTER_COTA.idCota, 
					function(result){
						
						if(result){
							MANTER_COTA.montarCombo(result,"#selectTipoDesconto");
						}
					},null,true
			);
			
			$.postJSON(contextPath + "/cadastro/cota/obterDescontosSelecionados",
					"idCota="+ MANTER_COTA.idCota, 
					function(result){
					
						if(result){
							MANTER_COTA.montarCombo(result,"#selectDesconto");
						}
					},null,true
			);
		}
		
};

var COTA_FORNECEDOR = {
	
		salvarFornecedores: function(){
			
			var fornecedores ="";
			
			 $("#selectFornecedorSelecionado_option_cnpj option").each(function (index) {
				 fornecedores = fornecedores + "fornecedores["+index+"]="+ $(this).val() +"&";
			 });
			
			$.postJSON(
					contextPath + "/cadastro/cota/salvarFornecedores",
					fornecedores + 
					"idCota="+ MANTER_COTA.idCota, 
					null,
					function(mensagens){
						
						COTA_FORNECEDOR.carregarFornecedores();
					},
					true
			);

		},
		
		carregarFornecedores:function(){
			
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
		}
};

var COTA_CNPJ = {	
	
	tratarExibicaoDadosCadastrais:function(){
		
		$("#dadosCNPJ").show();
		$("#dadosCPF").hide();
		$("#idTabSocio").parent().show();
		$( "#tabCota" ).tabs({ selected: 0 });
	},	
		
	novoCNPJ:function(){
		
		COTA_CNPJ.tratarExibicaoDadosCadastrais();
		
		TAB_COTA.possuiDadosObrigatorios = false;
		MANTER_COTA.fecharModalCadastroCota = false;
		MANTER_COTA.mudarNomeModalCadastro("Nova Cota");
		
		MANTER_COTA.tipoCotaSelecionada = MANTER_COTA.tipoCota_CNPJ;
		MANTER_COTA.idCota="";
		MANTER_COTA.numeroCota="";
		
		COTA_CNPJ.limparCampos();
		
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
		
		COTA_CNPJ.tratarExibicaoDadosCadastrais();
		
		COTA_CNPJ.carregarDadosCadastraisCnpj(result);
		
		MANTER_COTA.tipoCotaSelecionada = MANTER_COTA.tipoCota_CNPJ;
			
		MANTER_COTA.popupCota();
		
		MANTER_COTA.mudarNomeModalCadastro("Cota - " + result.numeroCota);
	},
	
	carregarDadosCadastraisCnpj:function(result){
		
		COTA_CNPJ.limparCampos();
		
		MANTER_COTA.mudarNomeModalCadastro("Cota - " + result.numeroCota);
		
		$( "#tabCota" ).tabs({ selected:0 });
		TAB_COTA.possuiDadosObrigatorios = true;
		
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
	},
		
	salvarDadosBasico:function (){

		var formData = $("#formDadosBasicoCnpj").serializeArray();
		
		formData.push({name:"cotaDTO.idCota",value: MANTER_COTA.idCota});
		
		$.postJSON(contextPath + "/cadastro/cota/salvarCotaCNPJ",
				formData , 
				function(result){
			
					MANTER_COTA.idCota = result.idCota;
					MANTER_COTA.numeroCota = result.numeroCota;
					
					TAB_COTA.possuiDadosObrigatorios = true;
					
					COTA_CNPJ.carregarDadosCadastraisCnpj(result);
					
					exibirMensagemDialog("SUCCESS",["Operação realizada com sucesso."],"");

				},
				null,
				true
		);
	},
	
	limparCampos:function(){
		
		$("#numeroCota").val("");
		$("#email").val("");
		$("#status").val("");
		$("#dataInclusao").html("");
		$("#razaoSocial").val("");
		$("#nomeFantasia").val("");
		$("#numeroCnpj").val("");
		$("#inscricaoEstadual").val("");
		$("#inscricaoMunicipal").val("");
		$("#emailNF").val("");
		$("#emiteNFE").attr("checked", null);
		$("#classificacaoSelecionada").val("");
		$("#historicoPrimeiraCota").val("");
		$("#historicoPrimeiraPorcentagem").val("" );
		$("#historicoSegundaCota").val("");
		$("#historicoSegundaPorcentagem").val("");
		$("#historicoTerceiraCota").val("");
		$("#historicoTerceiraPorcentagem").val("");
		$("#periodoCotaDe").val("");
		$("#periodoCotaAte").val("");
		
		clearMessageDialogTimeout(null);
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
	
	tratarExibicaoDadosCadastrais:function(){
		
		$("#dadosCPF").show();
		$("#dadosCNPJ").hide();
		$("#idTabSocio").parent().hide();
		$( "#tabCota" ).tabs({ selected: 0 });
	},		
		
	novoCPF:function(){
		
		MANTER_COTA.tipoCotaSelecionada = MANTER_COTA.tipoCota_CPF;
		MANTER_COTA.idCota="";
		MANTER_COTA.numeroCota="";
		
		COTA_CPF.tratarExibicaoDadosCadastrais();
		COTA_CPF.limparCampos();
		
		TAB_COTA.possuiDadosObrigatorios = false;
		MANTER_COTA.fecharModalCadastroCota = false;
		
		MANTER_COTA.mudarNomeModalCadastro("Nova Cota");
		
		$.postJSON(
				contextPath + "/cadastro/cota/incluirNovoCPF",
				null, 
				function(result){
					
					var dados = result;
					
					$("#dataInclusaoCPF").html(dados.dataInicioAtividade);
					$("#numeroCotaCPF").val(dados.numeroSugestaoCota);
					$("#statusCPF").val(dados.status);
					
					MANTER_COTA.montarCombo(dados.listaClassificacao,"#classificacaoSelecionadaCPF");
					
					MANTER_COTA.popupCota();
				}
		);
	},

	editarCPF:function(result){
		
		COTA_CPF.tratarExibicaoDadosCadastrais();
		
		COTA_CPF.carregarDadosCpf(result);
		
		MANTER_COTA.tipoCotaSelecionada = MANTER_COTA.tipoCota_CPF;
		
		MANTER_COTA.popupCota();
		
		MANTER_COTA.mudarNomeModalCadastro("Cota - " + result.numeroCota);
	},
	
	carregarDadosCpf:function(result){
		
		COTA_CPF.limparCampos();
		
		MANTER_COTA.mudarNomeModalCadastro("Cota - " + result.numeroCota);
		
		$( "#tabCota" ).tabs({ selected:0 });
		TAB_COTA.possuiDadosObrigatorios = true;
		
		$("#numeroCotaCPF").val(result.numeroCota);
		$("#emailCPF").val(result.email);
		$("#statusCPF").val(result.status);
		$("#dataInclusaoCPF").html(result.dataInclusao.$);
		$("#nomePessoaCPF").val(result.nomePessoa);
		$("#numeroCPF").val(result.numeroCPF);
		$("#numeroRG").val(result.numeroRG);
		$("#orgaoEmissor").val(result.orgaoEmissor);
		$("#estadoSelecionado").val(result.estadoSelecionado);
		$("#estadoCivilSelecionado").val(result.estadoCivilSelecionado);
		$("#sexoSelecionado").val(result.sexoSelecionado);
		$("#nacionalidade").val(result.nacionalidade);
		$("#natural").val(result.natural);
		$("#emailNFCPF").val(result.emailNF);
		$("#emiteNFECPF").attr("checked", (result.emiteNFE == true)?"checked":null);
		$("#classificacaoSelecionadaCPF").val(result.classificacaoSelecionada);
		$("#historicoPrimeiraCotaCPF").val(result.historicoPrimeiraCota);
		$("#historicoPrimeiraPorcentagemCPF").val( eval( result.historicoPrimeiraPorcentagem));
		$("#historicoSegundaCotaCPF").val(result.historicoSegundaCota);
		$("#historicoSegundaPorcentagemCPF").val( eval( result.historicoSegundaPorcentagem));
		$("#historicoTerceiraCotaCPF").val(result.historicoTerceiraCota);
		$("#historicoTerceiraPorcentagemCPF").val( eval( result.historicoTerceiraPorcentagem));
		
		if(result.dataNascimento){
			$("#dataNascimento").val(result.dataNascimento.$);
		}
		
		if(result.inicioPeriodo){
			$("#periodoCotaDeCPF").val(result.inicioPeriodo.$);
		}
		
		if(result.fimPeriodo){
			$("#periodoCotaAteCPF").val(result.fimPeriodo.$);
		}
	},
	
	salvarDadosBasico:function (){

		var formData = $("#formDadosBasicoCpf").serializeArray();
		
		formData.push({name:"cotaDTO.idCota",value: MANTER_COTA.idCota});
		
		$.postJSON(contextPath + "/cadastro/cota/salvarCotaCPF",
				formData , 
				function(result){
			
					MANTER_COTA.idCota = result.idCota;
					MANTER_COTA.numeroCota = result.numeroCota;
					
					TAB_COTA.possuiDadosObrigatorios = true;
					
					COTA_CPF.carregarDadosCpf(result);
					
					exibirMensagemDialog("SUCCESS",["Operação realizada com sucesso."],"");

				},
				null,
				true
		);
		
	},
	carregarDadosCPF: function(idCampo){
		
		$.postJSON(contextPath + "/cadastro/cota/obterDadosCPF",
				"numeroCPF="+$(idCampo).val() , 
				function(result){
					
					if(result.email)$("#emailCPF").val(result.email);
					if(result.nomePessoa)$("#nomePessoaCPF").val(result.nomePessoa);
					if(result.numeroRG)$("#numeroRG").val(result.numeroRG);
					if(result.dataNascimento)$("#dataNascimento").val(result.dataNascimento.$);
					if(result.orgaoEmissor)$("#orgaoEmissor").val(result.orgaoEmissor);
					if(result.estadoSelecionado)$("#estadoSelecionado").val(result.estadoSelecionado);
					if(result.estadoCivilSelecionado)$("#estadoCivilSelecionado").val(result.estadoCivilSelecionado);
					if(result.sexoSelecionado)$("#sexoSelecionado").val(result.sexoSelecionado);
					if(result.nacionalidade)$("#nacionalidade").val(result.nacionalidade);
					if(result.natural)$("#natural").val(result.natural);
				},
				null,
				true
		);
	},
	
	limparCampos:function(){
		
		$("#numeroCotaCPF").val("");
		$("#emailCPF").val("");
		$("#statusCPF").val("");
		$("#dataInclusaoCPF").html("");
		$("#nomePessoaCPF").val("");
		$("#numeroCPF").val("");
		$("#numeroRG").val("");
		$("#dataNascimento").val("");
		$("#orgaoEmissor").val("");
		$("#estadoSelecionado").val("");
		$("#estadoCivilSelecionado").val("");
		$("#sexoSelecionado").val("");
		$("#nacionalidade").val("");
		$("#natural").val("");
		$("#emailNFCPF").val("");
		$("#emiteNFECPF").attr("checked", null);
		$("#classificacaoSelecionadaCPF").val("");
		$("#historicoPrimeiraCotaCPF").val("");
		$("#historicoPrimeiraPorcentagemCPF").val("");
		$("#historicoSegundaCotaCPF").val("");
		$("#historicoSegundaPorcentagemCPF").val("");
		$("#historicoTerceiraCotaCPF").val("");
		$("#historicoTerceiraPorcentagemCPF").val("");
		$("#periodoCotaDeCPF").val("");
		$("#periodoCotaAteCPF").val("");
		
		clearMessageDialogTimeout(null);
	},
	
};

var SOCIO_COTA = {
		
		itemEdicao:null,
		rows:[],
		
		socio:function(){
			
			var socio = {
					nome:$("#idNomeSocio").val(),
					cargo:$("#idCargoSocio").val(),
					principal:($("#idSocioPrincipal").attr("checked"))?true:false,
					id:($("#idSocio").val())
			};
			
			return socio;
		},
		
		salvarSocios:function(){
			
			var list =   serializeArrayToPost("sociosCota",SOCIO_COTA.obterListaSocios());			
			var objPost = concatObjects({idCota:MANTER_COTA.idCota},list);
			
			$.postJSON(contextPath + "/cadastro/cota/salvarSocioCota",
					objPost , 
					null,
					null,
					true
			);
			
			return false;
		},
		
		carregarSociosCota: function() {
			
			SOCIO_COTA.rows = [];
			
			$.postJSON(
				contextPath+'/cadastro/cota/carregarSociosCota',
				"idCota=" + MANTER_COTA.idCota,
				function(result) {
					
					$.each(result, function(index, value) {
						
						var socio = {
								id:value.id,
								nome:value.nome, 
								cargo:value.cargo,
								principal:value.principal,
								endereco:value.endereco.tipoLogradouro + 
										 " " + 
										 value.endereco.logradouro +
										 ", " + 
										 value.endereco.numero + 
										 " - " + 
										 value.endereco.cidade +
										 "/" + 
										 value.endereco.uf +
										 " " +
										 value.endereco.cep,			
								telefone:value.telefone.ddd + 
										 " " +
										 value.telefone.numero};
						
						SOCIO_COTA.rows.push({"id": value.id,"cell":socio});
					});
					
					$(".sociosPjGrid").flexAddData({rows:SOCIO_COTA.rows,page:1,total:1}  );
					
					SOCIO_COTA.limparFormSocios();
				},
				function(result) {
					
					SOCIO_COTA.processarResultadoConsultaSocios(result);
				},
				true
			);
		},		
		
		processarResultadoConsultaSocios:function(data){
			
			if (data.mensagens) {

				exibirMensagemDialog(
					data.mensagens.tipoMensagem, 
					data.mensagens.listaMensagens,
					""
				);
				
				return;
			}
			
			$.each(data.rows, function(index, value) {
				
				var idSocio = value.id;
			
				var acao  = '<a href="javascript:;" onclick="SOCIO_COTA.editarSocio(' + idSocio + ');" ><img src="' + contextPath + '/images/ico_editar.gif" border="0" hspace="5" /></a>';
				    acao += '<a href="javascript:;" onclick="SOCIO_COTA.removerSocio(' + idSocio + ');" ><img src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0" /></a>';

				value.cell.acao = acao;
				
				value.cell.principalFlag =(value.cell.principal == true) 
								?'<img src="' + contextPath + '/images/ico_check.gif" border="0" hspace="5" />'
							  	:'&nbsp';
			});
			
			return data;
			
		},
		
		editarSocio:function(idSocio){
			
			SOCIO_COTA.itemEdicao = idSocio;
			
			$.postJSON(
				contextPath+'/cadastro/cota/carregarSocioPorId',
				"idSocioCota=" + idSocio,
				function(result) {
					$("#nomeSocio").val(result.nome),
					$("#cargoSocio").val(result.cargo),
					$("#socioPrincipal").attr("checked",(result.principal == true)?"checked":null);
					$("#idSocio").val(result.id);	

					$("#idTelefone").val(result.telefone.id);
					$("#ddd").val(result.telefone.ddd);
					$("#numeroTelefone").val(result.telefone.numero);
					
					$("#idEndereco").val(result.endereco.id);
					$("#uf").val(result.endereco.uf);
					$("#cep").val(result.endereco.cep);
					$("#cidade").val(result.endereco.cidade);
					$("#bairro").val(result.endereco.bairro);
					$("#complemento").val(result.endereco.complemento);
					$("#tipoLogradouro").val(result.endereco.tipoLogradouro);
					$("#logradouro").val(result.endereco.logradouro);
					$("#numero").val(result.endereco.numero);
					
					popup_novo_socio();
				},
				null,
				true
			);
		},
		
		limparFormSocios:function(){
			
			$("#idNomeSocio").val(""),
			$("#idCargoSocio").val(""),
			$("#idSocioPrincipal").attr("checked",null);
			$("#idSocio").val("");
			SOCIO_COTA.itemEdicao = null;
			
			$("#btnEditarSocio").hide();
			$("#btnAddSocio").show();
		},
		
		removerSocio:function(idSocio){
			
			$("#dialog-excluir-socio").dialog({
				resizable: false,
				height:'auto',
				width:300,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						SOCIO_COTA.rows.splice(idSocio, 1);
						
						var lista = new Array;
						
						for (var index in SOCIO_COTA.rows) {	
							lista.push({"id":lista.length, "cell":SOCIO_COTA.rows[index].cell});
						}
						
						SOCIO_COTA.rows = lista;
						
						$(".sociosPjGrid").flexAddData({rows:lista,page:1,total:1}  );
						
						$(this).dialog("close");
					},
					"Cancelar": function() {
						$(this).dialog("close");
					}
				}
			});
			
			$("#dialog-excluir-socio").show();
		},
		
		obterListaSocios:function(){
			
			var list = new Array();
			
			for (var index in SOCIO_COTA.rows) {
				var socio = SOCIO_COTA.rows[index].cell;
				socio.principalFlag=null;
				socio.acao = null;
				list.push(socio);
			}
			
			return list;
		},

		incluirSocio:function(){

			var data = $("#formSocioCota").serializeArray();

			data.push({name:'idCota', value:MANTER_COTA.idCota});

			$.postJSON(
					contextPath + "/cadastro/cota/incluirSocioCota",
					data,
					function(result) {

						SOCIO_COTA.carregarSociosCota();

						$( "#dialog-socio" ).dialog( "close" );
					},
					function(result) {
						
						if (data.mensagens) {

							exibirMensagemDialog(
								data.mensagens.tipoMensagem, 
								data.mensagens.listaMensagens,
								"dialog-socio"
							);
							
							return;
						}
					},
					true,
					"dialog-socio"
				);
		}
};
