<script type="text/javascript">
	$(function(){
		$(".sociosGrid").flexigrid({
			preProcess: processarResultadoSocios,
			dataType : 'json',
			colModel : [  {
				display : 'Nome',
				name : 'nome',
				width : 580,
				sortable : true,
				align : 'left'
			}, {
				display : 'Principal',
				name : 'principal',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : true,
				align : 'center'
			}],
			width : 770,
			height : 160
		});
	});
	
	function carregarSocios(){
		$.postJSON("<c:url value='/cadastro/fiador/pesquisarSociosFiador' />", null, 
			function(result) {
				$(".sociosGrid").flexAddData({
					page: 1, total: 1, rows: result.rows
				});	
				
				$("#btnAddEditarSocio").text("Incluir Novo");
			},
			null,
			true
		);
	}
	
	function processarResultadoSocios(data){
		if (data.mensagens) {

			exibirMensagemDialog(
				data.mensagens.tipoMensagem, 
				data.mensagens.listaMensagens
			);
			
			return;
		}
		
		if (data.result){
			data.rows = data.result.rows;
		}
		
		var i;

		for (i = 0 ; i < data.rows.length; i++) {

			var lastIndex = data.rows[i].cell.length;

			data.rows[i].cell[lastIndex] = getActionsSocios(data.rows[i].id);
			
			data.rows[i].cell[1] = 
				data.rows[i].cell[1] == "true" ? '<img src="/nds-client/images/ico_check.gif" border="0px"/>'
					                           : '&nbsp;';
		}

		$('.sociosGrid').show();
		
		if (data.result){
			return data.result;
		}
		return data;
	}
	
	function getActionsSocios(idSocio){
		return '<a href="javascript:;" onclick="editarSocio(' + idSocio + ')" ' +
				' style="cursor:pointer;border:0px;margin:5px" title="Editar Socio">' +
				'<img src="/nds-client/images/ico_editar.gif" border="0px"/>' +
				'</a>' +
				'<a href="javascript:;" onclick="removerSocio(' + idSocio + ')" ' +
				' style="cursor:pointer;border:0px;margin:5px" title="Excluir Socio">' +
				'<img src="/nds-client/images/ico_excluir.gif" border="0px"/>' +
				'</a>';
	}
	
	function adicionarSocio(){
		var data = "pessoa.nome=" + $('[name="nomeFiadorCpf"]:eq(1)').val() + "&" +
			"pessoa.email=" + $('[name="emailFiadorCpf"]:eq(1)').val() + "&" +
	        "pessoa.cpf=" + $('[name="cpfFiador"]:eq(1)').val() + "&" +
	        "pessoa.rg=" + $('[name="rgFiador"]:eq(1)').val() + "&" +
	        "pessoa.dataNascimento=" + $('[name="dataNascimentoFiadorCpf"]:eq(1)').val() + "&" +
	        "pessoa.orgaoEmissor=" + $('[name="orgaoEmissorFiadorCpf"]:eq(1)').val() + "&" +
	        "pessoa.ufOrgaoEmissor=" + $('[name="selectUfOrgaoEmiCpf"]:eq(1)').val() + "&" +
	        "pessoa.estadoCivil=" + $('[name="estadoCivilFiadorCpf"]:eq(1)').val() + "&" +
	        "pessoa.sexo=" + $('[name="selectSexoFiador"]:eq(1)').val() + "&" +
	        "pessoa.nacionalidade=" + $('[name="nacionalidadeFiadorCpf"]:eq(1)').val() + "&" +
	        "pessoa.natural=" + $('[name="naturalFiadorCpf"]:eq(1)').val() + "&" +
	        "pessoa.socioPrincipal=" + ("" + $('[name="checkboxSocioPrincipal"]:eq(1)').attr("checked") == 'checked');
	    
	    if (addConjuge){ 
	        data = data + "&pessoa.conjuge.nome=" + $('[name="nomeConjugeCpf"]:eq(1)').val() + "&" +
	        "pessoa.conjuge.email=" + $('[name="emailConjugeCpf"]:eq(1)').val() + "&" +
	        "pessoa.conjuge.cpf=" + $('[name="cpfConjuge"]:eq(1)').val() + "&" +
	        "pessoa.conjuge.rg=" + $('[name="rgConjuge"]:eq(1)').val() + "&" +
	        "pessoa.conjuge.dataNascimento=" + $('[name="dataNascimentoConjugeCpf"]:eq(1)').val() + "&" +
	        "pessoa.conjuge.orgaoEmissor=" + $('[name="orgaoEmissorConjugeCpf"]:eq(1)').val() + "&" +
	        "pessoa.conjuge.ufOrgaoEmissor=" + $('[name="selectUfOrgaoEmiConjugeCpf"]:eq(1)').val() + "&" +
	        "pessoa.conjuge.sexo=" + $('[name="selectSexoConjuge"]:eq(1)').val() + "&" +
	        "pessoa.conjuge.nacionalidade=" + $('[name="nacionalidadeConjugeCpf"]:eq(1)').val() + "&" +
	        "pessoa.conjuge.natural=" + $('[name="naturalConjugeCpf"]:eq(1)').val();
	    }

		$.postJSON("<c:url value='/cadastro/fiador/adicionarSocio'/>", data, 
			function(result){
		
				//if (result[0].tipoMensagem){
				//	exibirMensagemDialog(result[0].tipoMensagem, result[0].listaMensagens);
				//}
				
				if (result != ""){
					$(".sociosGrid").flexAddData({
						page: result.page, total: result.total, rows: result.rows
					});
					
					//$("#gridFiadoresCadastrados").show();
				} //else {
					//$("#gridFiadoresCadastrados").hide();
				//}
				
				//if (result[0].tipoMensagem == "SUCCESS"){
				//	$(janela).dialog("close");
				//}
			},
			null,
			true
		);
	}
</script>

<jsp:include page="dadosCadastraisCpf.jsp"></jsp:include>

<br />
<span class="bt_add"><a href="javascript:adicionarSocio();" id="btnAddEditarSocio">Incluir Novo</a></span>
<br />
<br />
<br clear="all" />
<strong>Sócios Cadastrados</strong>
<br />
<table class="sociosGrid"></table>