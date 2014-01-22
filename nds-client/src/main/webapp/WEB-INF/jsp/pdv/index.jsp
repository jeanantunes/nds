
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/pdv.js"></script>
<fieldset style="width:880px!important; margin:5px;">
 	<legend>PDVs Cadastrados</legend>	
	<table class="PDVsGrid"></table>
	<br />
	<span class="bt_novos" id="PDVbtnNovo"><a href="javascript:;" onclick="PDV.poupNovoPDV();" rel="tipsy" title="Incluir Novo PDV"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" border="0" /></a></span>
</fieldset>
<br clear="all" />

<div id="dialog-excluirPdv" title="Aten&ccedil;&atilde;o" style="display:none">
	<p>Confirmar exclus&atilde;o PDV ?</p>
</div>

<div id="dialog-cancelar-cadastro-pdv" title="PDV" style="display: none;">
	<p>Dados n&atilde;o salvos ser&atilde;o perdidos. Confirma o cancelamento?</p>
</div>
<div id="dialog-confirmaGeradorFluxo" title="Ponto Principal" style="display: none;">
	<p>Gerador de fluxo  &eacute; obrigat&oacute;rio para o ponto principal.</p>
</div>

<div id="dialog-pdv" title="PDV Cota">
		
		<jsp:include page="../messagesDialog.jsp">
			
			<jsp:param value="idModalPDV" name="messageDialog"/>
		
		</jsp:include>
		
		<input type="hidden" name="idPDV" id="idPDV" value=""/>
		
		<div id="tabpdv">
		    <ul>
		        <li><a href="#tabpdv-1">Dados B&aacute;sicos</a></li>
		        <li><a href="#tabpdv-2" onclick="ENDERECO_PDV.popularGridEnderecos();">Endere&ccedil;os</a></li>
		        <li><a href="#tabpdv-3" onclick="TELEFONE_PDV.carregarTelefones();">Telefones</a></li>
		        <li><a href="#tabpdv-4">Caract. / Segmenta&ccedil;&atilde;o</a></li>
		        <li><a href="#tabpdv-6">Gerador de Fluxo</a></li>
		        <li><a href="#tabpdv-7">MAP</a></li>
		       
		  </ul>
		 	   <div id="tabpdv-1"> <jsp:include page="dadosBasico.jsp"/> </div>
			   
			   <div id="tabpdv-2"> <jsp:include page="endereco.jsp"/> </div>
			   
			   <div id="tabpdv-3"> 	<jsp:include page="telefone.jsp"/> 	</div>
			   
			   <div id="tabpdv-4"> <jsp:include page="caracteristica.jsp"/> </div>
			   
			   <div id="tabpdv-6"> <jsp:include page="geradorFluxo.jsp"/> </div>
			   
			   <div id="tabpdv-7"> <jsp:include page="map.jsp"/> </div>				
					 
			  <br clear="all" />
		</div>
</div>

<div id= "dialog-confirmaPontoPrincipal" title="Ponto Principal" style="display: none;">
	<p>J&aacute; existe um ponto principal, deseja substitui-lo por este?</p>
</div>

