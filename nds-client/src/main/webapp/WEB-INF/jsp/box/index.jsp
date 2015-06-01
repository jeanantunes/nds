<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/box.js"></script>
<script type="text/javascript">

$(function(){
	boxController.init();	
});
</script>
<style type="text/css">
	#dialog-box{display:none;}
	#dialog-box fieldset{width:570px!important;}
</style>

</head>

<body>
	<form action="/cadastro/box/" id="cotas_associadas_box_form">
	<div id="dialog-box" title="Detalhes do Box">
		<fieldset>
	    	<legend>Cotas Associadas ao Box</legend>
	        <table class="boxCotaGrid"></table>
	  
  		  		<span class="bt_novos">
					<a href="${pageContext.request.contextPath}/cadastro/box/exportarDetalhes?fileType=XLS">
						<img src="${pageContext.request.contextPath}/images/ico_excel.png" border="0" />
					</a>
				</span>
  		  		<span class="bt_novos">
					<a href="${pageContext.request.contextPath}/cadastro/box/exportarDetalhes?fileType=PDF">
						<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" border="0" />
					</a>
				</span>
		 </fieldset>
	</div>
	</form>

	<form action="/cadastro/box/" id="excluir_box_form">
	<div id="dialog-excluir" title="Excluir Box">		
		<p>Confirma a exclus&atilde;o deste Box?</p>
	</div>
	</form>
	
	<form action="/cadastro/box/" id="novo_box_form">
	<input type="hidden" name="box.id" id="boxId" style="width:80px;"/>
	<div id="dialog-novo" title="Incluir Novo Box">  
		<jsp:include page="../messagesDialog.jsp" />  
	    <table width="356" border="0" cellpadding="2" cellspacing="1" class="filtro">
	            <tr>
	              <td width="112">Box:</td>
	              <td width="233"><input type="text" name="box.codigo" id="boxCodigo" style="width:80px;"/></td>
	            </tr>
	            <tr>
	              <td>Nome:</td>
	              <td><input type="text" name="box.nome" id="boxNome" style="width:220px;"/></td>
	            </tr>
	            <tr>
	              <td>Tipo de Box:</td>
	              <td><select name="box.tipoBox" id="boxTipoBox" style="width:227px;">
	                  	<option selected="selected"> </option>
	                    <option value="ESPECIAL">Especial</option>
	                    <option value="ENCALHE">Encalhe</option>
	                    <option value="LANCAMENTO">Lan&ccedil;amento</option>
	             	 </select>
	             </td>
	            </tr>	           
          </table>
    </div>
	</form>
	
	
	<form action="/cadastro/box/" id="pesquisar_box_form">
	<div class="areaBts">
		<div class="area">
			<span class="bt_novos">
				<a href="javascript:;" id="btnNovo" rel="tipsy" title="Incluir Novo Box">
					<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>
				</a>
			</span>
			<div id="fileExport" style="display: none; float:right;">
				<span class="bt_arq">
					<a href="${pageContext.request.contextPath}/cadastro/box/exportarConsulta?fileType=PDF" rel="tipsy" title="Imprimir">
						<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" border="0" />
					</a>
				</span>
				
				<span class="bt_arq">
					<a href="${pageContext.request.contextPath}/cadastro/box/exportarConsulta?fileType=XLS"  rel="tipsy" title="Gerar Arquivo">
						<img src="${pageContext.request.contextPath}/images/ico_excel.png" border="0" />
					</a>
				</span>
			</div>
		</div>  		  		
	</div>
  	<div class="linha_separa_fields">&nbsp;</div>
	
	
	
	 <fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">
   	    <legend> Pesquisar Box</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="30">Box:</td>
                <td width="152"><input type="text" name="box.codigo" id="pesquisaCodigoBox" style="width:130px;"/></td>
                <td width="77">Tipo de Box:</td>
                <td width="191"><select name="box.tipoBox" id="pesquisaTipoBox" style="width:180px;">
                		<option selected="selected"> </option>
	                    <option value="ENCALHE">Encalhe</option>
	                    <option value="LANCAMENTO">Lan&ccedil;amento</option>
              </select></td>
              <td width="474"><span class="bt_novos"><a href="javascript:;" id="btnPesquisar"><img src="${pageContext.request.contextPath}/images/ico_pesquisar.png" border="0" /></a></span></td>
            </tr>
          </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <div class="grids" style="display:none;">
	      <fieldset class="fieldGrid">
	       	  <legend>Boxes Cadastrados</legend>
	        	<table class="boxGrid"></table>	
	      </fieldset>
      </div>
    </form>
</body>