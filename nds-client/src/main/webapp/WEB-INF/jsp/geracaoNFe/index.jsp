<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.multiselect.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.br.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.fileDownload.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/geracaoNFe.js"></script>
<style type="text/css">
  fieldset label {width: auto; margin-bottom: 0px!important;
}
#dialogCotasSuspensas fieldset{width:570px!important;}
  </style>

<script type="text/javascript">
var geracaoNFeController;
$(function(){
	geracaoNFeController = $.extend(true, new GeracaoNFeController(),  BaseController);
	
});
</script>

<div id="dialogCotasSuspensas" title="Gera&ccedil;&atilde;o NF-e">
	<form>
     <fieldset>
     	<legend>Cotas Suspensas</legend>
     	<table id="gridCotasSuspensas"></table>
        <span class="bt_sellAll" style="float:right;">
	        <label for="checkboxCheckAllCotasSuspensas">Selecionar Todos</label>
	        <input type="checkbox" name="Todos" id="checkboxCheckAllCotasSuspensas" style="float:left;"/>
        </span>
     </fieldset>
     </form>
</div>

<div id="dialogCotasSuspensasConfirmar" title="Transferência para Suplementar">
	<form>
		<fieldset>
			<legend>Transferência para Suplementar</legend>
			<p>O reparte das cotas selecionadas serão transferidos para Suplementar</p>
		</fieldset>
	</form>
</div>
<div class="areaBts">
	<div class="area">
		<span class="bt_novos"><a href="javascript:;" id="btnGerar" rel="tipsy" title="Confirma  Gera&ccedil;&atilde;o de Nf-e?" ><img src="${pageContext.request.contextPath}/images/ico_check.gif" width="16" height="16" border="0" hspace="5" /></a></span>

        <span class="bt_arq"><a href="javascript:;" id="btnImprimirXLS" rel="tipsy" title="Gerar Arquivo"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" /></a></span>

        <span class="bt_arq"><a href="javascript:;" id="btnImprimirPDF" rel="tipsy" title="Imprimir"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" /></a></span>	
	</div>
</div>
<div class="linha_separa_fields">&nbsp;</div>
<fieldset class="fieldFiltro">
	<legend> Pesquisar NF-e</legend>
		<form>
			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<tr>
					<td width="91">Tipo de Nota:</td>
    				<td width="204">
						<select id="selectTipoNotaFiscal" style="width:250px; font-size:11px!important">
							<c:forEach items="${listaTipoNotaFiscal}" var="tipoNotaFiscal">
								<option value="${tipoNotaFiscal.key }">${tipoNotaFiscal.value }</option>
							</c:forEach>
						</select>
					</td>
    				<td width="95">Data Movimento:</td>
    				<td width="237">
    					<input type="text" id="datepickerIntervaloMovimentoDe" style="width:76px;"/>&nbsp;&nbsp;At&eacute;&nbsp;
						<input type="text" id="datepickerIntervaloMovimentoAte" style="width:76px;"/>
					</td>
					<td width="80">Data Emissão:</td>
					<td width="212">
						<input name="datepickerDe" type="text" id="datepickerEmissao" style="width:80px;"/>
					</td>
				</tr>
				<tr>
					<td>Roteiro:</td>
					<td>
						<select id="listRoteiro" style="width:200px; font-size:11px!important">
      						<option value="">Selecione...</option>
      						<c:forEach items="${roteiros}" var="roteiro">
								<option value="${roteiro.key }">${roteiro.value }</option>
							</c:forEach>
    					</select>
    				</td>
					<td>Rota:</td>
    				<td>
    					<select id="listRota" style="width:200px; font-size:11px!important">
    						<option value="">Selecione...</option>
    						<c:forEach items="${rotas}" var="rota">
								<option value="${rota.key }">${rota.value }</option>
							</c:forEach>
    					</select>
    				</td>
    				<td>Cota de:</td>
    				<td>
    					<input type="text" id="inputIntervaloCotaDe" style="width:80px;"/>&nbsp;At&eacute;&nbsp;
						<input type="text" id="inputIntervaloCotaAte" style="width:80px;"/>
					</td>
				</tr>
				<tr>
					<td>Intervalo Box:</td>
    				<td>
    					<input type="text" id="inputIntervaloBoxDe" style="width:76px;"/>&nbsp;At&eacute; &nbsp;
				  		<input type="text" id="inputIntervaloBoxAte" style="width:76px;"/>
					</td>
    				<td>Fornecedor:</td>
    				<td colspan="3">
				     	<select id="selectFornecedores" multiple="multiple" style="width:400px">
							<c:forEach items="${fornecedores}" var="fornecedor">
								<option value="${fornecedor.key }">${fornecedor.value }</option>
							</c:forEach>
						</select>
					</td>
					<td>&nbsp;</td>
    			</tr>
  				<tr>
				    <td>&nbsp;</td>
				    <td>&nbsp;</td>
				    <td>&nbsp;</td>
				    <td>&nbsp;</td>
				    <td>&nbsp;</td>
				    <td>
				    	<span class="bt_pesquisar"><a href="javascript:;" id="btnPesquisar" onclick="mostrar();"></a></span>
				    </td>
  				</tr>
  				<td colspan="3"></td>
  			</table>
		</fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
       <fieldset class="fieldGrid">
       	  <legend>Gera&ccedil;&atilde;o NF-e</legend>
          <div class="grids" style="display:none;">
		  <table id="gridNFe"></table>
		</div>
		</form>
      </fieldset>
