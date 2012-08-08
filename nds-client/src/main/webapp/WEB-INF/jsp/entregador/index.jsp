<head>
<style>
#dialog-pdv{display:none!important;}
.diasFunc label, .finceiro label, .materialPromocional label{ vertical-align:super;}
.complementar label{ vertical-align:super; margin-right:5px; margin-left:5px;}
</style>
<script>
	function obterCota(numeroCota, isPF) {
		
		var sufixo = "PJ";
		
		if(isPF) {
			
			sufixo = "PF";
		}

		var numeroCotaProcuracao = "#numeroCotaProcuracao" + sufixo;
		var nomeJornaleiroProcuracao = "#nomeJornaleiroProcuracao" + sufixo;
		var boxProcuracao = "#boxProcuracao" + sufixo;
		var nacionalidadeProcuracao = "#nacionalidadeProcuracao" + sufixo;
		var estadoCivilProcuracao = "#estadoCivilProcuracao" + sufixo;
		var enderecoPDVPrincipalProcuracao = "#enderecoPDVPrincipalProcuracao" + sufixo;
		var cepProcuracao = "#cepProcuracao" + sufixo;
		var bairroProcuracao = "#bairroProcuracao" + sufixo;
		var cidadeProcuracao = "#cidadeProcuracao" + sufixo;
		var numeroPermissaoProcuracao = "#numeroPermissaoProcuracao" + sufixo;
		var rgProcuracao = "#rgProcuracao" + sufixo;
		var cpfProcuracao = "#cpfProcuracao" + sufixo;
		
		$.postJSON(
			'<c:url value="/cadastro/entregador/obterCotaPorNumero" />',
			{'numeroCota': numeroCota},
			function(result) {
				$(numeroCotaProcuracao).val(result.numeroCota);
				$(nomeJornaleiroProcuracao).val(result.nomeJornaleiro);
				$(boxProcuracao).val(result.box);
				$(nacionalidadeProcuracao).val(result.nacionalidade);
				$(estadoCivilProcuracao).val(result.estadoCivil);
				$(enderecoPDVPrincipalProcuracao).val(result.enderecoPDVPrincipal);
				$(cepProcuracao).val(result.cep);
				$(bairroProcuracao).val(result.bairro);
				$(cidadeProcuracao).val(result.cidade);
				$(numeroPermissaoProcuracao).val(result.numeroPermissao);
				$(rgProcuracao).val(result.rg).mask("99.999.999-9");
				$(cpfProcuracao).val(result.cpf).mask("999.999.999-99");
			}, 
			function(result) {
				
				$(numeroCotaProcuracao).val("");
				$(nomeJornaleiroProcuracao).val("");
				$(boxProcuracao).val("");
				$(nacionalidadeProcuracao).val("");
				$(estadoCivilProcuracao).val("");
				$(enderecoPDVPrincipalProcuracao).val("");
				$(cepProcuracao).val("");
				$(bairroProcuracao).val("");
				$(cidadeProcuracao).val("");
				$(numeroPermissaoProcuracao).val("");
				$(rgProcuracao).val("");
				$(cpfProcuracao).val("");

				exibirMensagemDialog(
					result.mensagens.tipoMensagem, 
					result.mensagens.listaMensagens, ""
				);
			},
			true
		);
	}

	function novoEntregador(isCadastroPF) {
		
		$("#idEntregadorPF").val("");
		$("#idEntregadorPJ").val("");
		
		var idCampoInicioAtividade = "#inicioAtividade";

		if (isCadastroPF) {
			
			idCampoInicioAtividade += "PF";
			
			$("#formDadosEntregadorPF")[0].reset();
			$("#idEntregadorPF").val("");
			$("#naoComissionadoPF").click();
			$("#naoProcuracaoPF").click();

		} else {

			idCampoInicioAtividade += "PJ";
			
			$("#formDadosEntregadorPJ")[0].reset();
			$("#idEntregadorPJ").val("");
			$("#naoComissionadoPJ").click();
			$("#naoProcuracaoPJ").click();
		}
		
		$.postJSON(
			'<c:url value="/cadastro/entregador/novoCadastro" />',
			null,
			function(result) {

				$("#linkDadosCadastrais").click();
				
				$(idCampoInicioAtividade).html(result);

				popup_novoEntregador(isCadastroPF);		
			}
		);
	}	

	function popup_novoEntregador(isCadastroPF) {

		if (isCadastroPF) {

			$("#dadosCadastraisPF").show();
			$("#dadosCadastraisPJ").hide();

		} else {

			$("#dadosCadastraisPJ").show();
			$("#dadosCadastraisPF").hide();
		}

		$( "#dialog-novoEntregador" ).dialog({
			resizable: false,
			height:610,
			width:840,
			modal: true,
			buttons: {
				"Confirmar": function() {

					cadastrarEntregador(isCadastroPF);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	}
	
	function cadastrarEntregador(isCadastroPF, imprimir) {

		var data = new Array();

		var url;

		if (isCadastroPF) {

			data.push({name: 'cpfEntregador', value: $("#cpf").val()});
			data.push({name: 'idEntregador', value: $("#idEntregadorPF").val()});
			data.push({name: 'codigoEntregador', value: $("#codigoEntregadorPF").val()});
			data.push({name: 'isComissionado', value: $("#comissionadoPF").is(":checked")});
			data.push({name: 'percentualComissao', value: $("#percentualComissaoPF").val()});

			data.push({name: 'pessoaFisica.cpf', value: $("#cpf").val()});
			data.push({name: 'pessoaFisica.nome', value: $("#nomeEntregador").val()});
			data.push({name: 'pessoaFisica.apelido', value: $("#apelido").val()});
			data.push({name: 'pessoaFisica.rg', value: $("#rg").val()});
			data.push({name: 'pessoaFisica.dataNascimento', value: $("#dataNascimento").val()});
			data.push({name: 'pessoaFisica.orgaoEmissor', value: $("#orgaoEmissor").val()});
			data.push({name: 'pessoaFisica.ufOrgaoEmissor', value: $("#ufOrgaoEmissor").val()});
			data.push({name: 'pessoaFisica.estadoCivil', value: $("#estadoCivil").val()});
			data.push({name: 'pessoaFisica.sexo', value: $("#sexo").val()});
			data.push({name: 'pessoaFisica.nacionalidade', value: $("#nacionalidade").val()});
			data.push({name: 'pessoaFisica.natural', value: $("#natural").val()});
			data.push({name: 'pessoaFisica.email', value: $("#emailPF").val()});

			url = '<c:url value="/cadastro/entregador/cadastrarEntregadorPessoaFisica" />';

		} else {

			data.push({name: 'cnpjEntregador', value: $("#cnpj").val()});
			data.push({name: 'idEntregador', value: $("#idEntregadorPJ").val()});
			data.push({name: 'codigoEntregador', value: $("#codigoEntregadorPJ").val()});
			data.push({name: 'isComissionado', value: $("#comissionadoPJ").is(":checked")});
			data.push({name: 'percentualComissao', value: $("#percentualComissaoPJ").val()});

			data.push({name: 'pessoaJuridica.razaoSocial', value: $("#razaoSocial").val()});
			data.push({name: 'pessoaJuridica.nomeFantasia', value: $("#nomeFantasia").val()});
			data.push({name: 'pessoaJuridica.cnpj', value: $("#cnpj").val()});
			data.push({name: 'pessoaJuridica.inscricaoEstadual', value: $("#inscricaoEstadual").val()});
			data.push({name: 'pessoaJuridica.email', value: $("#emailPJ").val()});

			url = '<c:url value="/cadastro/entregador/cadastrarEntregadorPessoaJuridica" />';
		}

		$.postJSON(
			url,
			data,
			function(result) {
				
				if (imprimir) {

					realizarImpressao(isCadastroPF);

				} else {

					$("#dialog-novoEntregador").dialog( "close" );

					pesquisarEntregadores();
					
					exibirMensagem(
						result.tipoMensagem, 
						result.listaMensagens
					);
				}
			},
			function(result) {
		
				exibirMensagemDialog(
					result.mensagens.tipoMensagem, 
					result.mensagens.listaMensagens, ""
				);
			},
			true
		);
	}

	function editarEntregador(idEntregador) {

		$.postJSON(
			'<c:url value="/cadastro/entregador/editarEntregador" />',
			{'idEntregador' : idEntregador},
			function(result) {

				if (result.pessoaFisica) {

					$("#formDadosEntregadorPF")[0].reset();

					popup_novoEntregador(true);

					$("#idEntregadorPF").val(result.entregador.id);
					$("#codigoEntregadorPF").val(result.entregador.codigo);
					$("#inicioAtividadePF").html(result.inicioAtividadeFormatada);
					$("#nomeEntregador").val(result.pessoaFisica.nome);
					$("#apelido").val(result.pessoaFisica.apelido);
					$("#cpf").val(result.pessoaFisica.cpf).mask("999.999.999-99");
					$("#rg").val(result.pessoaFisica.rg).mask("99.999.999-9");
					$("#dataNascimento").val(result.dataNascimentoEntregadorFormatada).mask("99/99/9999");
					$("#orgaoEmissor").val(result.pessoaFisica.orgaoEmissor);
					$("#ufOrgaoEmissor").val(result.pessoaFisica.ufOrgaoEmissor);
					$("#estadoCivil").val(result.pessoaFisica.estadoCivil);
					$("#sexo").val(result.pessoaFisica.sexo);
					$("#nacionalidade").val(result.pessoaFisica.nacionalidade);
					$("#natural").val(result.pessoaFisica.natural);
					$("#emailPF").val(result.pessoaFisica.email);
					$("#percentualComissaoPF").val(result.entregador.percentualComissao);

					if (result.entregador.comissionado) {
						$("#comissionadoPF").click();
						$("#naoComissionadoPF").uncheck();	
					} else {
						$("#naoComissionadoPF").click();
						$("#comissionadoPF").uncheck();
					}

					if (result.entregador.procuracao) {

						$("#estadoCivilProcuradorProcuracaoPF").val(result.procuracaoEntregador.estadoCivil);
						$("#nacionalidadeProcuradorProcuracaoPF").val(result.procuracaoEntregador.nacionalidade);
						$("#numeroPermissaoPF").val(result.procuracaoEntregador.numeroPermissao);
						$("#nomeProcuradorProcuracaoPF").val(result.procuracaoEntregador.procurador);
						$("#profissaoProcuradorProcuracaoPF").val(result.procuracaoEntregador.profissao);
						$("#rgProcuradorProcuracaoPF").val(result.procuracaoEntregador.rg).mask("99.999.999-9");
						$("#enderecoProcuradorProcuracaoPF").val(result.procuracaoEntregador.endereco);
						
						obterCota(result.procuracaoEntregador.cota.numeroCota, true); 

						$("#procuracaoPF").click();
						$("#naoProcuracaoPF").uncheck();	
					} else {
						$("#naoProcuracaoPF").click();
						$("#procuracaoPF").uncheck();
					}

				} else {

					$("#formDadosEntregadorPJ")[0].reset();

					popup_novoEntregador(false);

					$("#idEntregadorPJ").val(result.entregador.id);
					$("#codigoEntregadorPJ").val(result.entregador.codigo);
					$("#inicioAtividadePJ").html(result.inicioAtividadeFormatada);
					$("#razaoSocial").val(result.pessoaJuridica.razaoSocial);
					$("#nomeFantasia").val(result.pessoaJuridica.nomeFantasia);
					$("#cnpj").val(result.pessoaJuridica.cnpj).mask("99.999.999/9999-99");
					$("#inscricaoEstadual").val(result.pessoaJuridica.inscricaoEstadual).mask("999.999.999.999");
					$("#emailPJ").val(result.pessoaJuridica.email);
					$("#percentualComissaoPJ").val(result.entregador.percentualComissao);

					if (result.entregador.comissionado) {
						$("#comissionadoPJ").click();
						$("#naoComissionadoPJ").uncheck();	
					} else {
						$("#naoComissionadoPJ").click();
						$("#comissionadoPJ").uncheck();
					}

					if (result.entregador.procuracao) {

						$("#estadoCivilProcuradorProcuracaoPJ").val(result.procuracaoEntregador.estadoCivil);
						$("#nacionalidadeProcuradorProcuracaoPJ").val(result.procuracaoEntregador.nacionalidade);
						$("#numeroPermissaoPJ").val(result.procuracaoEntregador.numeroPermissao);
						$("#nomeProcuradorProcuracaoPJ").val(result.procuracaoEntregador.procurador);
						$("#profissaoProcuradorProcuracaoPJ").val(result.procuracaoEntregador.profissao);
						$("#rgProcuradorProcuracaoPJ").val(result.procuracaoEntregador.rg).mask("99.999.999-9");
						$("#enderecoProcuradorProcuracaoPJ").val(result.procuracaoEntregador.endereco);
						
						obterCota(result.procuracaoEntregador.cota.numeroCota, false);

						$("#procuracaoPJ").click();
						$("#naoProcuracaoPJ").uncheck();

					} else {
						$("#naoProcuracaoPJ").click();
						$("#procuracaoPJ").uncheck();
					}
				}

				ENDERECO_ENTREGADOR.popularGridEnderecos();
				
				ENTREGADOR.carregarTelefones();
				
				$("#linkDadosCadastrais").click();
			},
			function(result) {
				
				exibirMensagemDialog(
					result.mensagens.tipoMensagem, 
					result.mensagens.listaMensagens, ""
				);
			},
			true
		);
	}

	function confirmarExclusaoEntregador(idEntregador) {

		$( "#dialog-excluir-entregador" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$.postJSON(
						'<c:url value="/cadastro/entregador/removerEntregador" />',
						{'idEntregador' : idEntregador},
						function(result) {

							$("#dialog-excluir-entregador").dialog( "close" );
							
							$(".pessoasGrid").flexReload();
							
							exibirMensagem(
								result.tipoMensagem, 
								result.listaMensagens
							);
						},
						function(result) {

							exibirMensagemDialog(
								result.mensagens.tipoMensagem, 
								result.mensagens.listaMensagens, ""
							);
						}
					);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
	function realizarImpressao(isPF) {

		var sufixo = "PJ";
		
		if(isPF) {
			
			sufixo = "PF";
		}

		var nomeJornaleiroProcuracao = "#nomeJornaleiroProcuracao" + sufixo;
		var boxProcuracao = "#boxProcuracao" + sufixo;
		var nacionalidadeProcuracao = "#nacionalidadeProcuracao" + sufixo;
		var estadoCivilProcuracao = "#estadoCivilProcuracao" + sufixo;
		var enderecoPDVPrincipalProcuracao = "#enderecoPDVPrincipalProcuracao" + sufixo;
		var bairroProcuracao = "#bairroProcuracao" + sufixo;
		var cidadeProcuracao = "#cidadeProcuracao" + sufixo;
		var numeroPermissaoProcuracao = "#numeroPermissao" + sufixo;
		var rgProcuracao = "#rgProcuracao" + sufixo;
		var cpfProcuracao = "#cpfProcuracao" + sufixo;

		var nomeProcurador = "#nomeProcuradorProcuracao" + sufixo;		
		var rgProcurador = "#rgProcuradorProcuracao" + sufixo;
		var estadoCivilProcurador = "#estadoCivilProcuradorProcuracao" + sufixo;
		var nacionalidadeProcurador = "#nacionalidadeProcuradorProcuracao" + sufixo;
		var enderecoDoProcurado = "#enderecoProcuradorProcuracao" + sufixo;
		
		var data = new Array();			
		
		data.push({ name:'procuracaoImpressao.nomeJornaleiro' , value: $(nomeJornaleiroProcuracao).val()});
		data.push({ name:'procuracaoImpressao.boxCota' , value: $(boxProcuracao).val()});
		data.push({ name:'procuracaoImpressao.nacionalidade' , value: $(nacionalidadeProcuracao).val()});
		data.push({ name:'procuracaoImpressao.estadoCivil' , value: $(estadoCivilProcuracao).val()});
		data.push({ name:'procuracaoImpressao.enderecoPDV' , value: $(enderecoPDVPrincipalProcuracao).val()});
		data.push({ name:'procuracaoImpressao.bairroPDV' , value: $(bairroProcuracao).val()});
		data.push({ name:'procuracaoImpressao.cidadePDV' , value: $(cidadeProcuracao).val()});
		data.push({ name:'procuracaoImpressao.numeroPermissao' , value: $(numeroPermissaoProcuracao).val()});
		data.push({ name:'procuracaoImpressao.rgJornaleiro' , value: $(rgProcuracao).val()});
		data.push({ name:'procuracaoImpressao.cpfJornaleiro' , value: $(cpfProcuracao).val()});
		
		data.push({ name:'procuracaoImpressao.nomeProcurador' , value: $(nomeProcurador).val()});
		data.push({ name:'procuracaoImpressao.rgProcurador' , value: $(rgProcurador).val()});
		data.push({ name:'procuracaoImpressao.estadoCivilProcurador' , value: $(estadoCivilProcurador).val()});
		data.push({ name:'procuracaoImpressao.nacionalidadeProcurador' , value: $(nacionalidadeProcurador).val()});
		data.push({ name:'procuracaoImpressao.enderecoDoProcurado' , value: $(enderecoDoProcurado).val()});

		var parameters = arrayToURLParameters(data);
		
		window.location = "<c:url value='/cadastro/entregador/imprimirProcuracao"+ parameters +"'/>";
	}

	function imprimirProcuracao(isPF) {

		cadastrarEntregador(isPF, true);
	}

	function arrayToURLParameters(array) {

		var i;
		var params = "?";
		
		for (i = 0; i < array.length; i++) {
			
			params += i == 0 ? "" : "&";
			params += array[i].name + "=" + array[i].value; 
		}
		
		return params;
	}
	
	function processarResultadoEntregadores(data) {

		if (data.mensagens) {

			exibirMensagem(
				data.mensagens.tipoMensagem,
				data.mensagens.listaMensagens
			);

			$(".grids").hide();

			return;
		}

		var i;

		for (i = 0; i < data.rows.length; i++) {

			var lastIndex = data.rows[i].cell.length;

			data.rows[i].cell[lastIndex] = getActionEntregador(data.rows[i].id);
		}

		if (data.rows.length < 0) {

			$(".grids").hide();

			return;
		} 

		$(".grids").show();

		return data;
	}

	function getActionEntregador(idEntregador) {

		return '<a href="javascript:;" onclick="editarEntregador('
				+ idEntregador
				+ ')" '
				+ ' style="cursor:pointer;border:0px;margin:5px" title="Editar entregador">'
				+ '<img src="${pageContext.request.contextPath}/images/ico_editar.gif" border="0px"/>'
				+ '</a>'
				+ '<a href="javascript:;" onclick="confirmarExclusaoEntregador('
				+ idEntregador
				+ ')" '
				+ ' style="cursor:pointer;border:0px;margin:5px" title="Excluir entregador">'
				+ '<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" border="0px"/>'
				+ '</a>';
	}

	$(function() {

		$(".pessoasGrid").flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome / Razão Social',
				name : 'nomeRazaoSocial',
				width : 180,
				sortable : true,
				align : 'left'
			}, {
				display : 'CPF / CNPJ',
				name : 'cpfCnpj',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Apelido/Nome Fantasia',
				name : 'apelidoNomeFantasia',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : 'Telefone',
				name : 'telefone',
				width : 100,
				sortable : false,
				align : 'left'
			}, {
				display : 'E-Mail',
				name : 'email',
				width : 220,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : false,
				align : 'center'
			} ],
			sortname : "nomeRazaoSocial",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255, 
			singleSelect: true
		});

		$( "#tabsNovoEntregador" ).tabs();
	});

	function pesquisarEntregadores() {

		var formData = $("#formularioPesquisaEntregadores").serializeArray();

		$(".pessoasGrid").flexOptions({
			url : '<c:url value="/cadastro/entregador/pesquisarEntregadores" />',
			preProcess : processarResultadoEntregadores,
			params : formData
		});

		$(".pessoasGrid").flexReload();

		$(".grids").show();
	}
	
	function pesquisarCotas(){
		
		var idEntregador = $("#idEntregadorPF").val() == "" ? $("#idEntregadorPJ").val() : $("#idEntregadorPF").val();
		
		$.postJSON(
			'<c:url value="/cadastro/entregador/carregarAbaCota" />',
			{'idEntregador' : idEntregador},
			function(result) {
				
				if (result.tipoMensagem){
					
					exibirMensagem(
						result.tipoMensagem, 
						result.listaMensagens
					);
					
					return;
				}
				
				$("#nomeEntregadorAbaCota").text(result[0]);
				$("#nomeRoteiro").text(result[1]);
				$("#numeroBox").text(result[2]);
				$("#nomeRota").text(result[3]);
				
				$(".entregadoresCotaGrid").flexAddData({
					page: result[4].page, total: result[4].total, rows: result[4].rows
				});
				
				$(".entregadoresCotaGrid").flexOptions({
					url : '<c:url value="/cadastro/entregador/atualizarGridCotas" />',
					params : [
					          {name: "idEntregador", value: idEntregador}
					         ]
				});
			}, null, true
		);
	}
</script>
</head>
<body>

	<div id="dialog-excluir-entregador" title="Excluir Entregador" style="display:none">
		<p>Confirma a exclusão deste Entregador?</p>
	</div>

	<div class="container">

		<div id="effect" style="padding: 0 .7em;"
			class="ui-state-highlight ui-corner-all">
			<p>
				<span style="float: left; margin-right: .3em;"
					class="ui-icon ui-icon-info">
				</span> 
			</p>
		</div>
    
      <fieldset class="classFieldset" title="Capataz, Entregador, Mula, Carreteiro">
   	    <legend> Pesquisar Entregador</legend>
   	    
   	    <form id="formularioPesquisaEntregadores">
   	    
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="117">Nome/Razão Social:</td>
              <td colspan="3"><input type="text" name="filtroEntregador.nomeRazaoSocial" id="textfield2" style="width:160px;"/>
              </td>
                <td width="146">Apelido / Nome Fantasia:</td>
              <td width="145"><input type="text" name="filtroEntregador.apelidoNomeFantasia" id="textfield" style="width:130px;"/></td>
                <td width="79">CPF / CNPJ:</td>
              <td width="152"><input type="text" name="filtroEntregador.cpfCnpj" id="textfield" style="width:150px;"/></td>
              <td width="106">
              	<span class="bt_pesquisar"><a href="javascript:;" onclick="pesquisarEntregadores();">Pesquisar</a></span>
              </td>
            </tr>
          </table>
         </form>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="classFieldset">
       	  <legend title="Capataz, Entregador, Mula, Carreteiro">Entregadores Cadastrados</legend>
        <div class="grids" style="display:none;">
        	<table class="pessoasGrid"></table>
        </div>
        <span class="bt_novos" title="Novo">
           	<a href="javascript:;" onclick="novoEntregador(true);">
           		<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>CPF
           	</a>
        </span>

        <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="novoEntregador(false);">
        	<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>CNPJ</a>
        </span>
           
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
	</div>
	
	<jsp:include page="novoEntregador.jsp" />
</body>