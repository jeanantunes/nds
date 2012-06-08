<head>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/box.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/tipoProduto.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	<script type="text/javascript">
	var tipoProduto;
		$(function(){
			tipoProduto = new TipoProduto();			
		});
	</script>


	<style type="text/css">
		#dialog-box{display:none;}
		#dialog-box fieldset{width:570px!important;}
	</style>

</head>	

<body>

	<div id="dialog-excluir" title="Excluir Tipo Produto" style="display:none; ">		
		<p>Confirma a exclus&atilde;o deste Tipo Produto?</p>
	</div>
	
	<div id="dialog-novo" title="Incluir Novo Tipo Produto">
		<jsp:include page="../messagesDialog.jsp" />  
		<table width="442" border="0" cellspacing="2" cellpadding="2">
			<tbody>
				<tr>
					<td width="111"><strong>C&oacute;digo NCM:</strong></td>
					<td width="317">
						<input name="tipoProdutoNovoNCM" id="tipoProdutoNovoNCM" type="text" style="width: 100px;">
					</td>
				</tr>
				<tr>
					<td width="111"><strong>C&oacute;digo NBM:</strong></td>
					<td width="317">
						<input name="tipoProdutoNovoNBM" id="tipoProdutoNovoNBM" type="text" style="width: 100px;">
					</td>
				</tr>
				<tr>
					<td width="111"><strong>C&oacute;digo:</strong></td>
					<td width="317">
						<input name="tipoProdutoNovoCodigo" id="tipoProdutoNovoCodigo" type="text" style="width: 100px;">
					</td>
				</tr>
				<tr>
					<td><strong>Tipo de Produto:</strong></td>
					<td>
						<input name="tipoProdutoNovoDescricao" id="tipoProdutoNovoDescricao" type="text" style="width: 300px;">
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<fieldset class="classFieldset">
   	    <legend>Pesquisar Tipos de Produto</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tbody><tr>
              
              <td width="80">C&oacute;digo :</td>
              <td width="133"><input type="text" name="tipoProdutoPesquisaCodigo" id="tipoProdutoPesquisaCodigo" style="width:100px;"></td>
             
                
              <td width="134">Nome Tipo de Produto :</td>
              <td width="350"><input type="text" name="tipoProdutoPesquisaDescricao" id="tipoProdutoPesquisaDescricao" style="width:250px;"></td>
             
              <td width="104">
              	<span class="bt_pesquisar">
              		<a href="javascript:;" id="tipoProdutoPesquisaButton" >
              		Pesquisar
              		</a>
              	</span>
              </td>
              <tr>
              		<td width="80">C&oacute;digo NCM:</td>
              		<td width="133"><input type="text" name="tipoProdutoPesquisaCodigoNCM" id="tipoProdutoPesquisaCodigoNCM" style="width:100px;"></td>
             
             		 <td width="50">C&oacute;digo NBM:</td>
              		 <td width="133"><input type="text" name="tipoProdutoPesquisaCodigoNBM" id="tipoProdutoPesquisaCodigoNBM" style="width:100px;"></td>
              </tr>
            </tr>
          </tbody>
        </table>

      </fieldset>
      
      <div class="linha_separa_fields">&nbsp;</div>
      
      <fieldset class="classFieldset">
	    <legend>Tipos de Produtos Cadastrados</legend>
      
        <div class="grids" style="display: none;">
       		<div class="tipoProdutoGrid" style="width: 960px; "></div>
       	 </div>

         <span class="bt_novos" title="Novo">
         	<a href="javascript:;" id="tipoProdutoNovoButton">
         		<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0">
         		Novo
         	</a>
         </span>
         
         <span class="bt_novos file-export" title="Gerar Arquivo">
		 	<a href="${pageContext.request.contextPath}/administracao/tipoProduto/exportar?fileType=XLS">
				<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
				Arquivo
			</a>
		</span>
		
		<span class="bt_novos file-export" title="Imprimir">
			<a href="${pageContext.request.contextPath}/administracao/tipoProduto/exportar?fileType=PDF">
				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
				Imprimir
			</a>
		</span>  
      </fieldset>
      
      <div class="linha_separa_fields">&nbsp;</div>
</body>