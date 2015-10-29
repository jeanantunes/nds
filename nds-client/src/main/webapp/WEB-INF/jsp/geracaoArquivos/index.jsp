<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.multiselect.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.br.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.fileDownload.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/geracaoArquivos.js"></script>
<style type="text/css">
  fieldset label {width: auto; margin-bottom: 0px!important;
}
#dialogCotasSuspensas fieldset{width:570px!important;}


#dialog-excluir, #dialog-novo, #dialog_cota_unificacao, #dialog_nova_cota_unificacao{display:none;}


  </style>

<script type="text/javascript">
var geracaoNFeController;
$(function(){
	geracaoArquivos = $.extend(true, new GeracaoArquivos(),  BaseController);
	
});

</script>


<form>


	
	 <input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
	
    <div class="areaBts">
		<div class="area">
		    <span class="bt_novos"
			    title="Confirmar"><a id="btnGerar"  isEdicao="true" href="javascript:;" rel="tipsy"><img
					border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif"></a>
			</span>
			
			<!--  
		  	 <span class="bt_novos"
			    title="Unificar arquivos FC/DINAP"><a id="btnUnificar"  isEdicao="true" href="javascript:;" rel="tipsy">
			    <img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_change.png"></a>
			</span>
			-->
			
			
			
			
			 <span class="bt_novos"
			    title="Manutencao de cotas unificadas"><a id="btnUnificarCotasCRUD"  isEdicao="true" href="javascript:;" rel="tipsy">
			    <img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_estudo_complementar.gif"></a>
			</span>
			
			
		
		</div>
	</div>
    <br/>
    <br/>

<fieldset class="classFieldset fieldFiltroItensNaoBloqueados">
	<legend> Geração de Arquivos aos Jornaleiros</legend>
	<table width="950" border="0" cellpadding="2" cellspacing="1"
		class="filtro">
		<tr>
			<td width="199">Tipo de Arquivo:</td>
			<td width="206"><select name="tipoArquivo" id="tipoArquivo"
				style="width: 150px;">
					<option value="">Selecione...</option>
					<option value="REPARTE">Reparte</option>
					<option value="ENCALHE">Encalhe</option>
					<option value="PICKING" selected="selected">Picking</option>
					<option value="VENDA">Venda</option>
					<option value="UNIFICAR">Unificar</option>
			</select></td>
			<td width="205">
				<div id="dtLancto" style="display: none;">Data Lançamento:</div>
				<div id="dtRecolhimento">Data Recolhimento:</div>
				<div id="dtVenda" style="display: none;">Data:</div>
				<div id="arqDinap" style="display: none;">Arquivo Dinap:</div>
			</td>
			<td width="286"><div id="dtEscolha" >
					<input type="text" name="datepickerDe" id="datepickerDe"
						style="width: 70px;" />
					</div>	
				<div id="arquivo" >
				<select name="nomeArquivo" id="nomeArquivo" style="width:200px;" >
			<option value="">Selecione...</option>
		<c:forEach items="${arquivosDinapList}" var="arquivos">
			<option value="${arquivos}">${arquivos}</option>
		</c:forEach>
        </select>
				</div></td>
			<td width="228">
		    </td>
		   
		</tr>
	</table>

</fieldset>
</form>


<form id="cota_unificacao_form" name="cota_unificacao_form">
	   <div id="dialog_cota_unificacao" title="Cotas Centralizadas">
		  <table class="cotasCentralizadas"></table>
	   </div>
    </form>
	
    <form id="nova_cota_unificacao_form" name="nova_cota_unificacao_form">
	   <div id="dialog_nova_cota_unificacao" title="Unifica&ccedil;&atilde;o de Cotas">
		  <fieldset style="width: 98%;">
		  	<legend>Cotas Master</legend>
		  	<table style="width: 100%;">
		  		<tr>
		  			<td>Cota</td>
		  			<td>Nome</td>
		  		</tr>
		  		<tr>
		  			<td style="width: 10%;">
		  				<input type="text" class="numCota" id="parametro-cobranca-numeroCota" style="width: 40px;"
		  					onchange="GeracaoArquivos.prototype.buscarCotaPorNumero('')"/>
		  			</td>
		  			<td>
		  				<input type="text" id="parametro-cobranca-nomeCota" style="width: 495px;" 
		  					onkeyup="GeracaoArquivos.prototype.onkeyupCampoNome('');" 
		  					onblur="GeracaoArquivos.prototype.onblurCampoNome('');"/>
		  			</td>
		  		</tr>
		  	</table>
		  </fieldset>
		  
		  <fieldset style="width: 98%;">
			<legend>Cota</legend>
				<div style="overflow: auto; height: 190px;">
					<table id="cotasCentralizadas">
				  		<tr>
				  			<td>Cota</td>
				  			<td>Nome</td>
				  		</tr>
				  	</table>
				</div>
			  </fieldset>
		  </div>
    </form>
<div class="linha_separa_fields">&nbsp;</div>

<fieldset id="resultado" class="grids classFieldset"
	style="display: none">
	<legend>Arquivos Gerados</legend>
	<h2>Quantidade de Arquivos Gerados:&nbsp;<spam id="qtdArquivosGerados">0</spam></h2>

</fieldset>


<fieldset id="resultado_unificacao" class="grids classFieldset"
	style="display: none">
	<legend>Resultado da Unificação</legend>
	
	<h2>&nbsp;<spam id="qtdArquivosUnificados">Unificado com Sucesso</spam></h2>

</fieldset>


