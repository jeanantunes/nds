<%@ page contentType="text/html" pageEncoding="UTF-8" %>  
<!-- fixacaoReparte -->
<script type="text/javascript" src="scripts/pesquisaCota.js"></script>
<script type="text/javascript" src="scripts/pesquisaProduto.js"></script>
<script type="text/javascript" src="scripts/autoCompleteCampos.js"></script>
<script type="text/javascript" src="scripts/fixacaoReparte.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>

<script type="text/javascript">
var pesquisaProduto = new PesquisaProduto();
var pesquisaCota = new PesquisaCota();

$(function(){
	fixacaoReparteController.init();
	mostraQtd();
});

function porCota(){
	$('#fixacaoReparte_porCota').show();
	$('#fixacaoReparte_fixacaoProduto').hide();
}
function porExcessao(){
	$('#fixacaoReparte_porCota').hide();
	$('#fixacaoReparte_fixacaoProduto').show();
}

function filtroPorCotaFixacao(){
	$('#fixaxaoReparte_filtroPorCota').show();
	$('#fixacaoReparte_filtroPorProduto').hide();
	$('#fixacaoReparte_fixacaoProduto').hide();
	$("#codigoProdutoFixacao").val("");
	$("#nomeProdutoFixacao").val("");
	
	$("#fixReparte_divBotoesPorProduto").hide();
	$("#fixReparte_divBotoesPorCota").show();
	
	//$('#historicoXLS').attr('href', contextPath + "/distribuicao/fixacaoReparte/exportar?fileType=XLS&tipoExportacao=historicoCota");
	//$('#historicoPDF').attr('href', contextPath + "/distribuicao/fixacaoReparte/exportar?fileType=PDF&tipoExportacao=historicoCota");
	
}

function filtroPorProdutoFixacao(){
	$('#fixaxaoReparte_filtroPorCota').hide();
	$('#fixacaoReparte_filtroPorProduto').show();
	$('#fixacaoReparte_porCota').hide();
	$("#codigoCotaFixacao").val("");
	$("#nomeCotaFixacao").val("");
	
	$("#fixReparte_divBotoesPorProduto").show();
	$("#fixReparte_divBotoesPorCota").hide();
	
	//$('#historicoXLS').attr('href', contextPath + "/distribuicao/fixacaoReparte/exportar?fileType=XLS&tipoExportacao=historicoProduto");
	//$('#historicoPDF').attr('href', contextPath + "/distribuicao/fixacaoReparte/exportar?fileType=PDF&tipoExportacao=historicoProduto");
}
function mostraQtd(){
	$('#edInicialModal').val("");
	$('#edFinalModal').val("");
	$('.qtdEd').show();
	$('.intervalo').hide();
	}
function mostraIntervalo(){
	$('#qtdeEdicoesModal').val("");
	$('.qtdEd').hide();
	$('.intervalo').show();}



</script>


<body>

<div id="cotaCopiaFixacao-dialog" title="Cópia de Fixação" style="display:none;">
      
      <fieldset >
   		<legend>Cota origem</legend>
   		  <table border="0" cellspacing="1" cellpadding="1">
   		  	<tr>
	   		  <td><label><strong>Cota:</strong></label></td>
	   		  <td>
		      <input type="text" style="width:80px;" id="cotaFixacaoOrigemInput" 
		      	onchange="pesquisaCota.pesquisarPorNumeroCota('#'+this.id,'#nomeCotaFixacaoOrigemInput',false,undefined,undefined)"/>
		      </td>
		      <td>
		      <label><strong>Nome:</strong></label>
		      </td>
		      <td>
		      <input type="text" style="width:200px;" id="nomeCotaFixacaoOrigemInput"/>
   		  	</td>
   		  	</tr>
   		  </table>
   		  
   	  </fieldset>
   	  <fieldset >
   		<legend>Cota Destino</legend>
   		  <table border="0" cellspacing="1" cellpadding="1">
   		  	<tr>
	   		  <td><label><strong>Cota:</strong></label></td>
	   		  <td>
		      <input type="text" style="width:80px;" id="cotaFixacaoDestinoInput"
		      onchange="pesquisaCota.pesquisarPorNumeroCota('#'+this.id,'#nomeCotaFixacaoDestinoInput',false,undefined,undefined)"/>
		      </td>
		      <td>
		      <label><strong>Nome:</strong></label>
		      </td>
		      <td>
		      <input type="text" style="width:200px;" id="nomeCotaFixacaoDestinoInput"/>
   		  	</td>
   		  	</tr>
   		  </table>
   	  </fieldset>
   	
      </div>
      
      <div id="produtoCopiaFixacao-dialog" title="Cópia de Fixação" style="display:none;">
	      
	       <fieldset >
   		<legend>Publica&ccedil;&atilde;o origem</legend>
   		  <table border="0" cellspacing="1" cellpadding="1">
   		  	<tr>
	   		  <td><label><strong>C&oacute;digo:</strong></label></td>
	   		  <td>
		      <input type="text" style="width:80px;" id="codigoProdutoFixacaoOrigemInput" 
		      onchange="pesquisaProduto.pesquisarPorCodigoProduto('#'+this.id,'#nomeProdutoFixacaoOrigemInput',false,undefined,undefined )"/>
		      </td>
		      <td>
		      <label><strong>Produto:</strong></label>
		      </td>
		      <td>
		      <input type="text" style="width:200px;" id="nomeProdutoFixacaoOrigemInput" onkeyup="pesquisaProduto.autoCompletarPorNomeProduto('#'+this.id);"/>
   		  	</td>
   		  	</tr>
   		  </table>
   		  
   	  </fieldset>
   	  <fieldset >
   		<legend>Publica&ccedil;&atilde;o Destino</legend>
   		  <table border="0" cellspacing="1" cellpadding="1">
   		  	<tr>
	   		  <td><label><strong>C&oacute;digo:</strong></label></td>
	   		  <td>
		      <input type="text" style="width:80px;" id="codigoProdutoFixacaoDestinoInput" 
		      	onchange="pesquisaProduto.pesquisarPorCodigoProduto('#'+this.id,'#nomeProdutoFixacaoDestinoInput',false,undefined,undefined )"/>
		      </td>
		      <td>
		      <label><strong>Produto:</strong></label>
		      </td>
		      <td>
		      <input type="text" style="width:200px;" id="nomeProdutoFixacaoDestinoInput" onkeyup="pesquisaProduto.autoCompletarPorNomeProduto('#codigoProdutoDestinoInput');"/>
   		  	</td>
   		  	</tr>
   		  </table>
   	  </fieldset>

          <fieldset >
              <legend>Classificaçao a copiar</legend>
              <table border="0" cellspacing="1" cellpadding="1">
                  <tr>
                      <td><label><strong>Classificaçao:</strong></label></td>
                      <td>
                          <select id="tipoClassificacaoCopia">
                              <option value="-1" selected>TODAS</option>
                              <c:forEach items="${classificacao}" var="tipoClassificacao">
                                  <option value="<c:out value="${tipoClassificacao.id}"/>"><c:out value="${tipoClassificacao.descricao}"/></option>
                              </c:forEach>
                          </select>
                      </td>
                  </tr>
              </table>
          </fieldset>
	   
      </div>

<div class="areaBts">
		<div class="area">
			<div class="porExcessao" id="fixReparte_divBotoesPorProduto" style="display:none">
				<span class="bt_novos" id="btNovoProduto">
					<a href="javascript:;" rel="tipsy" title="Incluir Novo">
						<img src="images/ico_salvar.gif" hspace="5" border="0" />
					</a>
				</span>
	            <span class="bt_novos" id="btAddLoteProduto">
	            	<a href="javascript:;" href="javascript:;" onclick="fixacaoReparteController.add_lote();" rel="tipsy" title="Adicionar em Lote">
	            		<img src="images/ico_integrar.png" hspace="5" border="0" />
	            	</a>
	            </span>
				<span class="bt_novos" id="btCopiaMix">
					<a href="javascript:;" onclick="fixacaoReparteController.abrirCopiaDialog()" rel="tipsy" title="C&oacute;pia de Fixa&ccedil;&atilde;o">
						<img src="images/ico_negociar.png" alt="Cópia de Fixação" hspace="5" border="0" />
					</a>
				</span>
				<span class="bt_arq" id="btGerarArquivoProduto">
     	    		<a href="${pageContext.request.contextPath}/distribuicao/fixacaoReparte/exportar?fileType=XLS&tipoExportacao=produto" rel="tipsy" title="Gerar Arquivo">
     	    			<img src="images/ico_excel.png" hspace="5" border="0" />
     	    		</a>
     	    	</span>
				<span class="bt_arq" id="btImprimirProduto">
					<a  href="${pageContext.request.contextPath}/distribuicao/fixacaoReparte/exportar?fileType=PDF&tipoExportacao=produto" rel="tipsy" title="Imprimir">
						<img src="images/ico_impressora.gif" hspace="5" border="0" />
					</a>
				</span>
			</div>
			<div class="porCota" id="fixReparte_divBotoesPorCota" style="display:none;">
				<span class="bt_novos" id="btNovoCota">
					<a href="javascript:;" rel="tipsy" title="Incluir Novo">
						<img src="images/ico_salvar.gif" hspace="5" border="0" />
					</a>
				</span>
				<span class="bt_novos" id="btAddLoteCota">
					<a href="javascript:;" onclick="fixacaoReparteController.add_lote();" rel="tipsy" title="Adicionar em Lote">
						<img src="images/ico_integrar.png" hspace="5" border="0" />
					</a>
				</span>
				<span class="bt_novos" id="btCopiaMix">
					<a href="javascript:;" onclick="fixacaoReparteController.abrirCopiaDialog()" rel="tipsy" title="C&oacute;pia de Fixa&ccedil;&atilde;o">
						<img src="images/ico_negociar.png" alt="Cópia de Mix" hspace="5" border="0" />
					</a>
				</span>
				<span class="bt_arq" id="btGerarArquivoCota">
					<a href="${pageContext.request.contextPath}/distribuicao/fixacaoReparte/exportar?fileType=XLS&tipoExportacao=cota" rel="tipsy" title="Gerar Arquivo">
						<img src="images/ico_excel.png" hspace="5" border="0" />
					</a>
				</span>
				<span class="bt_arq" id="btImprimirCota">
					<a href="${pageContext.request.contextPath}/distribuicao/fixacaoReparte/exportar?fileType=PDF&tipoExportacao=cota" rel="tipsy" title="Imprimir">
						<img src="images/ico_impressora.gif" hspace="5" border="0" />
					</a>
				</span>
			</div>
		</div>
</div>

     
<br clear="all"/>
    <br />
    
    
    <fieldset class="classFieldset">
   	    <legend> Pesquisar Fixação</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
          <tr>
            <td width="22" align="right"><input type="radio" name="filtroPrincipalRadio" id="radio"  value="Cota" onclick="filtroPorCotaFixacao();" /></td>
            <td width="50"><label >Cota</label></td>
            <td width="22"><input type="radio" name="filtroPrincipalRadio" id="radio2" value="Produto" onclick="filtroPorProdutoFixacao()" /></td>
            <td width="49"><label >Produto</label></td>
            <td width="781"><table width="760" border="0" cellpadding="2" cellspacing="1" class="filtro filtroPorProduto" id="fixacaoReparte_filtroPorProduto" style="display:none;">
          <tr>
            <td width="52">Código:</td>
            <td width="86"><input type="text" name="codigoProdutoFixacao" id="codigoProdutoFixacao"  style="width:80px;" onchange="pesquisaProduto.pesquisarPorCodigoProduto('#codigoProdutoFixacao','#nomeProdutoFixacao',false,undefined,undefined )"/></td>
            <td width="48">Produto:</td>
            <td width="206"><input type="text" name="nomeProdutoFixacao" id="nomeProdutoFixacao" style="width:200px;"/></td>
            <td width="75">Classificação:</td>
            <td width="167">
            	<select name="select" id="filtroClassificacaoFixacao" style="width:160px;">
                    <option value="-1">SELECIONE</option>
                    <c:forEach items="${classificacao}" var="tipoClassificacao">
                        <option value="<c:out value="${tipoClassificacao.id}"/>" ${tipoClassificacao.descricao eq 'NORMAL'? 'selected="selected"' : '' }><c:out value="${tipoClassificacao.descricao}"/></option>
                    </c:forEach>
          		</select>
            
            </td>
            <td width="106"><span class="bt_pesquisar"><a href="javascript:;" onclick="fixacaoReparteController.pesquisarPorProduto();">Pesquisar</a></span></td>
          </tr>
        </table>
        
        <table width="758" border="0" cellpadding="2" cellspacing="1" id="fixaxaoReparte_filtroPorCota" class="filtro filtroPorCota" style="display:none;">
            <tr>
           	  <td width="30" >Cota:</td>
                <td width="91">
                <!-- pesquisaCota.pesquisarPorNumeroCota('#codigoCota','#nomeCota',false,undefined,undefined) -->
                <input type="text" name="codigoCotaFixacao" id="codigoCotaFixacao" style="width:80px;" onchange="fixacaoReparteController.autoCompleteNumeroCota('#codigoCotaFixacao','#nomeCotaFixacao')"/></td>
                <td width="37" >Nome:</td>
                <td width="470"><input type="text" name="nomeCotaFixacao" id="nomeCotaFixacao" style="width:200px;"/></td>
              <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="fixacaoReparteController.pesquisarPorCota();">Pesquisar</a></span></td>
            </tr>
          </table>
            </td>
          </tr>
        </table>
        
        
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>

	 <div class="grids" style="display:block;">
	      <div class="porExcessao" id="fixacaoReparte_fixacaoProduto" style="display:none;">
		      <fieldset class="classFieldset">
		       	  	<legend>Fixação Produto</legend>
		        	<table class="fixacaoProdutoGrid"></table>
		      </fieldset>
	      </div>
	       <div class="porCota" id="fixacaoReparte_porCota" style="display:none;">
		      <fieldset class="classFieldset">
		       	 <legend>Produtos</legend>
		        <table class="fixacaoCotaGrid"></table> 
		      </fieldset>
   		   </div>
   		   
    </div>
    
   <!-- MODAL NOVA FIXAÇÃO --> 
    
 	<div id="dialog-novoFixacao" title="Nova Fixação" style="display:none;">
 		
			<fieldset style="width:760px!important; margin-bottom:5px;">
		    	<legend id="subtitulo1">Produto:</legend>
		        	<span id="spanCodigoProduto"></span>-<span id="spanNomeProduto"></span>
		    </fieldset>

		  <fieldset style="width:760px!important; margin-top:5px;">
		   	<legend id="subtitulo2">Pesquisar Cota</legend>
		    <table width="588" border="0" cellpadding="2" cellspacing="1" class="filtro">
		        <tr>
		          <td width="41" id="label1">Cota:</td>
		          <td width="66"><input type="text" name="codigoModalFixacao" id="codigoModalFixacao" style="width:60px;"/></td>
		          <td width="41" id="label2">Nome:</td>
		          <td width="119">
		          	<input type="text" name="nomeModalFixacao" id="nomeModalFixacao" style="width:110px;" onkeyup="fixacaoReparteController.autoCompleteNomeModal();"/>
			       </td>
			       <td width="119">
			       		<select name="selectModal" id="selectModal" style="width:160px;display:none;">
                            <option value="-1">SELECIONE</option>
				            <c:forEach items="${classificacao}" var="tipoClassificacao">
								<option value="<c:out value="${tipoClassificacao.id}"/>" ${tipoClassificacao.descricao eq 'NORMAL'? 'selected="selected"' : '' }><c:out value="${tipoClassificacao.descricao}"/></option>
							</c:forEach>
	          			</select>

			       </td>
		          </select></td>
		          <td width="73"><span class="bt_pesquisar"><a id="pesquisaModal" href="javascript:;" onclick="fixacaoReparteController.pesquisaHistoricoPorCota();">Pesquisar</a></span></td>
		        </tr>
		      </table>
			</fieldset>
		    <br clear="all" />
		    <fieldset style="width:760px!important; margin-top:5px;">
		      <table width="590" border="0" cellspacing="1" cellpadding="1">
		      <tr>
		        <td width="90"><strong>Edição:</strong></td>
		        <td width="78"><span id="edicaoDestaque"></span></td>
		        <td width="50"><strong>Status:</strong></td>
		        <td width="100"><span id="statusDestaque"></span></td>
		        <td width="140">&nbsp;</td>
		        <td width="113">&nbsp;</td>
		        </tr>
		      </table>
		      <table width="587" border="0" cellspacing="1" cellpadding="1">
		          <tr>
		          	<td width="20"><input type="radio" name="radio" id="radioQtdeEdicoes" value="radio" checked onclick="mostraQtd();" /></td>
		            <td width="74">Qtde Edições:</td>
		            <td width="57"><input type="text" name="textfield" id="qtdeEdicoesModal" style="width:50px;" class="qtdEd"/></td>
		            <td width="20"><input type="radio" name="radio" id="radioIntervalo" value="radio" onclick="mostraIntervalo();" /></td>
		            <td width="93">Intervalo Edições:</td>
		            <td width="29" class="intervalo"> Inicial:</td>
		            <td width="60" class="intervalo"><input type="text" name="edInicialModal" id="edInicialModal" style="width:50px;"/></td>
		            <td width="31" class="intervalo"> Final:</td>
		            <td width="52" class="intervalo"><input type="text" name="edFinalModal" id="edFinalModal" style="width:50px;"/></td>
		            <td width="67">Qtde Fixada:</td>
		            <td width="50"><input type="text" name="textfield2" id="qtdeFixadaModal" style="width:50px;"/></td>
		          </tr>
		  </table>
		  </fieldset>
		    <br clear="all" />

		    <fieldset style="width:760px!important; margin-top:5px;">
		    	<legend>Histórico</legend>

		        <table class="historicoGrid"></table>

		    <br clear="all" />

		     <span class="bt_novos"><a id="historicoXLS" rel="tipsy" title="Gerar Arquivo"><img src="images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

		    <span class="bt_novos"><a id="historicoPDF" rel="tipsy" title="Imprimir"><img src="images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
		   </fieldset>
	</div>

<!-- DIALOG  UPLOAD -->		
	<div id="modalUploadArquivo" title="Adicionar em Lote" style="display:none;">
	 <form id="formUploadLoteFixacao"  action="${pageContext.request.contextPath}/distribuicao/fixacaoReparte/uploadArquivoLoteFixacao"  method="post"  enctype="multipart/form-data">
			<p>Utilize o modelo de exemplo para fazer upload para o sistema: </p>
			<p ><span class="bt_novos"><a href="${pageContext.request.contextPath}/modelos/modelo_fixacao_reparte.xls" rel="tipsy" title="Download Modelo"><img align="center" src="images/ico_excel.png" hspace="5" border="0" /></a></span></p>
			<br><br><br>
			<hr>
			<p>Selecione um arquivo para upload:</p>
			<br>
			<p align="center"><input type="file" id="excelFileFixacao" name="excelFileFixacao" style="width:200px"/></p>
	  </form>
	</div>	
	
<!-- DIALOG MSG UPLOAD -->		
	<div id="dialog-msg-upload" title="Adicionar em Lote" style="display:none;">
	</div>

<!-- DIALOG EXCLUSAO -->	
	<div id="dialog-excluir" title="Excluir Fixação" style="display:none;">
	<p>Confirma a exclusão desta Fixação?</p>
	</div>

<!-- DIALOG REPARTE -->	
	<div id="dialog-confirma-reparte" title="Confirma Reparte PDV" style="display:none;">
	<p>A soma dos valores dos repartes definidos está diferente do valor de reparte total. Deseja prosseguir?</p>
	</div>

<!-- EDITAR FIXAÇÂO -->	
	<div id="dialog-defineReparte" title="Reparte por PDV" style="display:none;">
  <fieldset style="width:605px!important;">
   		<legend>Dados da Cota</legend>
    	<table width="500" border="0" cellspacing="1" cellpadding="1">
          <tr>
            <td width="42"><strong>Cota:</strong></td>
            <td width="92"><span id="codigoCotaModalReparte"></span></td>
            <td width="44"><strong>Nome:</strong></td>
            <td width="400"><span id="nomeCotaModalReparte" ></span></td>
            <td width="151">&nbsp;</td>
          </tr>
        </table>

	</fieldset>
    <br clear="all" />
    <fieldset style="width:605px!important; margin-top:10px;">
   		<legend>Dados do Produto</legend>
    	<table width="500" border="0" cellspacing="1" cellpadding="1">
          <tr>
            <td width="42"><strong>Código:</strong></td>
            <td width="92">&nbsp;<span id="codigoProdutoModalReparte"/></td>
            <td width="44"><strong>Produto:</strong></td>
            <td width="400">&nbsp;<span id="nomeProdutoModalReparte" /></td>
            <td width="44"><strong>Classificação:</strong></td>
            <td width="200">&nbsp;<span id="classificacaoModalReparte"/></td>
          </tr>
        </table>

	</fieldset>
    <br clear="all" />
    <fieldset style="width:605px!important; margin-top:10px;">
   		<legend>PDV da Cota</legend>
    	<table class="pdvCotaGrid" id="pdvCotaGrid"></table>
        <table width="600" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="312">&nbsp;</td>
    <td width="224">&nbsp;</td>
    <td width="71" align="center"></td>
    <td width="23">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="3" align="right">Manter Fixa&nbsp; </td>
    <td><input name="input2" type="checkbox" value="" /></td>
  </tr>
</table>
	</fieldset>

</div>


 </body>     
