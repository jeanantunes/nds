<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.multiselect.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.br.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.fileDownload.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/geracaoArquivos.js"></script>
<style type="text/css">
  fieldset label {width: auto; margin-bottom: 0px!important;
}
#dialogCotasSuspensas fieldset{width:570px!important;}
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
			
		  	 <span class="bt_novos"
			    title="Unificar arquivos FC/DINAP"><a id="btnUnificar"  isEdicao="true" href="javascript:;" rel="tipsy">
			    <img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_change.png"></a>
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
			<td width="99">Tipo de Arquivo:</td>
			<td width="206"><select name="tipoArquivo" id="tipoArquivo"
				style="width: 150px;">
					<option value="">Selecione...</option>
					<option value="REPARTE">Reparte</option>
					<option value="ENCALHE">Encalhe</option>
					<option value="PICKING" selected="selected">Picking</option>
			</select></td>
			<td width="105">
				<div id="dtLancto" style="display: none;">Data Lançamento:</div>
				<div id="dtRecolhimento">Data Recolhimento:</div>
			</td>
			<td width="386"><div id="dtEscolha">
					<input type="text" name="datepickerDe" id="datepickerDe"
						style="width: 70px;" />
				</div></td>
			<td width="128">
		    </td>
		</tr>
	</table>

</fieldset>
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