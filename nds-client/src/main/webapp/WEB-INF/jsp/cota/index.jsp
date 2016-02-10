<head>
<title>NDS - Treelog</title>

<script type="text/javascript" src='<c:url value="/"/>/scripts/jquery.numeric.js'></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/endereco.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselects-0.3.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/parametroCobranca.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/cotaGarantia.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/tabCota.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/manterCota.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pdv.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/autoCompleteCampos.js"></script>

<script language="javascript" type="text/javascript">
	
	var pesquisaCotaCadastroCota = new PesquisaCota(MANTER_COTA.workspace);
	
	$(function() {
		MANTER_COTA.init();	
	});
</script>
<style>

	.diasFunc label, .finceiro label, .materialPromocional label{ vertical-align:super;}
	.complementar label, .distribuicao label{ vertical-align:super; margin-right:5px; margin-left:5px;}
	
	#dialog-pdv fieldset{width:777px!important; margin-bottom:10px;  margin-left: -11px;}
	
	.ui-widget {
    	font-size: 1em;
	}


</style>

<style>
.linkDisabled {
	cursor: default;
	opacity: 0.4;
}
</style>

</head>

<body>
	
	<div id="dialog-excluirCota" title="Aten&ccedil;&atilde;o" style="display:none">
		<p>Confirmar exclus&atilde;o Cota ?</p>
	</div>
	<div id="dialog-confirmarSenha" title="Aten&ccedil;&atilde;o" style="display:none">
		<p>Informe sua senha: 
		<input type="password" id="confirmarSenhaInput" name="confirmarSenhaInput">
		<input type="hidden" name="fileType" id="fileType" />
		</p>
	</div>
	<div class="areaBts">
		<div class="area">
			<span class="bt_novos">
				<a href="javascript:;" onclick="MANTER_COTA.novoPopupCotaCPF();" rel="tipsy" title="Incluir Nova Cota Pessoa F&igrave;sica">
					<img src="${pageContext.request.contextPath}/images/ico_jornaleiro.gif" hspace="5" border="0"/>
				</a>
			</span>
			
			<span class="bt_novos">
				<a href="javascript:;" onclick="MANTER_COTA.novoPopupCotaCNPJ();" rel="tipsy" title="Incluir Nova Cota Pessoa Jur&igrave;dica">
					<img src="${pageContext.request.contextPath}/images/ico_usuarios1.gif" hspace="5" border="0"/>
				</a>
			</span>
			
			<span class="bt_arq">
				<a href="javascript:;" onclick="MANTER_COTA.imprimir('XLS')" rel="tipsy" title="Gerar Arquivo">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
				</a>
			</span>
			
			<span class="bt_arq">
				<a href="javascript:;" onclick="MANTER_COTA.imprimir('PDF')" rel="tipsy" title="Imprimir">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
				</a>
			</span>
		</div>
	</div>
	<div class="linha_separa_fields">&nbsp;</div>
 	<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
  	   <legend> Pesquisar Cotas</legend>
       <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
           <tr>
             <td width="78">Cota:</td>
             <td colspan="3">
             		<input name="numCota" id="numCota" type="text" maxlength="32" style="width:70px; float:left; margin-right:5px;"
             		onchange="pesquisaCotaCadastroCota.pesquisarPorNumeroCota('#numCota', '#descricaoPessoa');" />
          		
             </td>
               <td width="121">Nome / Razão Social:</td>
               <td width="205">
               		<input name="descricaoPessoa" id="descricaoPessoa" type="text" class="nome_jornaleiro" maxlength="255"
				      		 style="width:200px;" onkeyup="pesquisaCotaCadastroCota.autoCompletarPorNome('#descricaoPessoa');" />
               </td>
               <td width="83">CPF / CNPJ:</td>
               <td width="200">
               		<input type="text" name="txtCPF_CNPJ" id="txtCPF_CNPJ" style="width:180px;" maxlength="18"/>
               </td>
               <td width="170">Status:	&nbsp;
                               <select name="selectStatus" id="selectStatus" style="width:120px;">
									<option value="PENDENTE">Pendente </option>
                					<option value="ATIVO">Ativo    </option>
                					<option value="SUSPENSO">Suspenso </option>
                					<option value="INATIVO">Inativo  </option>
                					<option value="TODOS" selected="selected">Todos</option>
              						</select>
               </td>
           </tr>
           </table>
           <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
		   <tr>
				<td width="78">Logradouro:</td>
				<td colspan="3">
					<input type="text" id="logradouroPesquisa" style="width:170px;"/>
				</td>
				<td width="43">Bairro:</td>
				<td width="214">
					<input type="text" id="bairroPesquisa" style="width:200px;"/>
				</td>
				<td width="86">Cidade:</td>
				<td width="193">
					<input type="text" id="municipioPesquisa" style="width:180px;"/>
				</td>
				<td width="126">
	               	<span class="bt_novos">
	               		<a href="javascript:;" onclick="MANTER_COTA.pesquisar();"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a>
	               	</span>
                </td>
		   </tr>
         </table>

     </fieldset>
      
    <div class="linha_separa_fields">&nbsp;</div>
	
	<div class="grids" id="gridsCota" style="display: none;">
		<fieldset class="fieldGrid">
			<legend>Cotas Cadastradas</legend>
				<table class="pessoasGrid"></table>
			
	
		</fieldset>
	</div>
	<div class="linha_separa_fields">&nbsp;</div>

	<jsp:include page="dialogCota.jsp"/>

</body>
