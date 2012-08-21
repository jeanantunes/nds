<head>
<title>NDS - Novo Distrib</title>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/endereco.js"></script>

<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/cotaGarantia.js"></script>

<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/tabCota.js"></script>

<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/pdv.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pessoa.js"></script>

<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselects-0.3.js"></script>

<script language="javascript" type="text/javascript" src='<c:url value="/"/>/scripts/jquery.numeric.js'></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/manterCota.js"></script>

<script language="javascript" type="text/javascript">
	
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

</head>

<body>
	
	<div id="dialog-excluirCota" title="Atenção" style="display:none">
		<p>Confirmar exclusão Cota ?</p>
	</div>
	
 	<fieldset class="classFieldset">
  	   <legend> Pesquisar Cotas</legend>
       <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
           <tr>
             <td width="31">Cota:</td>
             <td colspan="3">
             		
             		<input name="numCota" 
	              		   id="numCota" 
	              		   type="text"
	              		   maxlength="11"
	              		   style="width:70px; 
	              		   float:left; margin-right:5px;"
	              		    />
          		
             </td>
               <td width="127">Nome / Razão Social:</td>
               <td width="204">
               
               		<input   name="descricaoPessoa" 
				      		 id="descricaoPessoa" 
				      		 type="text" 
				      		 class="nome_jornaleiro" 
				      		 maxlength="255"
				      		 style="width:130px;"
				      		 onkeyup="PESSOA.autoCompletarPorNome('#descricaoPessoa');" />
               </td>
               <td width="72">CPF / CNPJ:</td>
               <td width="253">
               		<input type="text" name="txtCPF_CNPJ" id="txtCPF_CNPJ" style="width:180px;" maxlength="18"/>
               </td>
               <td width="104">&nbsp;</td>
           </tr>
           </table>
           <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
		   <tr>
				<td width="68">Logradouro:</td>
				<td colspan="3">
					<input type="text" id="logradouroPesquisa" style="width:180px;"
						   onkeyup="MANTER_COTA.pesquisarLogradouros('#logradouroPesquisa');"/>
				</td>
				<td width="36">Bairro:</td>
				<td width="195">
					<input type="text" id="bairroPesquisa" style="width:155px;"
						   onkeyup="MANTER_COTA.pesquisarBairros('#bairroPesquisa');"/>
				</td>
				<td width="57">Município:</td>
				<td width="253">
					<input type="text" id="municipioPesquisa" style="width:180px;"
						   onkeyup="MANTER_COTA.pesquisarMunicipios('#municipioPesquisa');"/>
				</td>
				<td width="104">
	               	<span class="bt_pesquisar">
	               		<a href="javascript:;" onclick="MANTER_COTA.pesquisar();">Pesquisar</a>
	               	</span>
                </td>
		   </tr>
         </table>

     </fieldset>
      
    <div class="linha_separa_fields">&nbsp;</div>
	
	<fieldset class="classFieldset">
	
		<legend>Cotas Cadastradas</legend>
	
		<div class="grids" id="grids" style="display: none;">
			<table class="pessoasGrid"></table>
		</div>
		
		<span class="bt_novos" title="Novo">
			<a href="javascript:;" onclick="COTA_CPF.novoCPF();">
				<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />CPF
			</a>
		</span>
		
		<span class="bt_novos" title="Novo">
			<a href="javascript:;" onclick="COTA_CNPJ.novoCNPJ();">
				<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />CNPJ
			</a>
		</span>
		
		<span class="bt_novos" title="Gerar Arquivo">
			<a href="${pageContext.request.contextPath}/cadastro/cota/exportar?fileType=XLS">
				<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo
			</a>
		</span>
		
		<span class="bt_novos" title="Imprimir">
			<a href="${pageContext.request.contextPath}/cadastro/cota/exportar?fileType=PDF">
				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir
			</a>
		</span>

	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>

	<jsp:include page="dialogCota.jsp"/>

</body>
