<div id="tabDistribuicao" style="width: 1030px">
	<br clear="all" />
	<fieldset style="width: 260px;">
		<legend>Distribuição</legend>
		<table width="253" border="0" cellspacing="1" cellpadding="2">
			<tr>
				<td>Geração Automática de Estudo:</td>
				<td align="center">
					<input name="parametrosDistribuidor.geracaoAutomaticaEstudo" 
					id="geracaoAutomaticaEstudo"
					type="checkbox" ${parametrosDistribuidor.geracaoAutomaticaEstudo ? "checked" : ""} />
				</td>
			</tr>
			<tr>
				<td>Exibir informa&ccedil;&otilde;es reparte complementar:</td>
				<td align="center">
					<input name="parametrosDistribuidor.infoReparteComplementar" 
					id="infoReparteComplementar"
					type="checkbox" ${parametrosDistribuidor.infoReparteComplementar ? "checked" : ""} />
				</td>
			</tr>
			<tr>
				<td width="161">Venda Média +:</td>
				<td width="81" align="center">
					<input name="parametrosDistribuidor.vendaMediaMais" type="text" 
					id="vendaMediaMais" value="${parametrosDistribuidor.vendaMediaMais}" 
					style="width: 50px; text-align: center;" maxlength="2"/>
				</td>
			</tr>
			<tr>
				<td>Praça de Veraneio:</td>
				<td align="center">
					<input name="parametrosDistribuidor.pracaVeraneio" 
					id="pracaVeraneio"
					type="checkbox" ${parametrosDistribuidor.pracaVeraneio ? "checked" : ""} />
				</td>
			</tr>
			<tr>
				<td>Complementar Automático:</td>
				<td align="center">
					<input name="parametrosDistribuidor.complementarAutomatico" 
					id="complementarAutomatico"
					type="checkbox" value="" ${parametrosDistribuidor.complementarAutomatico ? "checked" : ""} />
				</td>
			</tr>
			<tr>
				<td>% Máximo de Fixação</td>
				<td align="center">
					<input name="parametrosDistribuidor.percentualMaximoFixacao"
					id="percentualMaximoFixacao"
					value="${parametrosDistribuidor.percentualMaximoFixacao}" 
					type="text" style="width: 50px; text-align: center;" maxlength="2"/>
				</td>
			</tr>
		</table>
	</fieldset>

	<fieldset style="width: 350px;">
		<legend>Tabela de Classificação da Cota</legend>

		<table width="345" border="0" cellpadding="0" cellspacing="1">
			<tr class="header_table">
				<td width="115">Classificação Cota</td>
				<td align="center">De</td>
				<td align="center">Até</td>
			</tr>
			<tr class="class_linha_1">
				<td width="115">AA ( + de 1 PDV )
					<input type="hidden" name="parametrosDistribuidor.listClassificacaoCota[0].id" id="listClassificacaoCota0.id" value="${parametrosDistribuidor.listClassificacaoCota[0].id}" />
					<input type="hidden" name="parametrosDistribuidor.listClassificacaoCota[0].codigoClassificacaoCota" id="listClassificacaoCota0.codigoClassificacaoCota" value="AA" />
				</td>
				<td width="110" align="center">
					<input name="parametrosDistribuidor.listClassificacaoCota[0].valorDe" 
					id="listClassificacaoCota0.valorDe" 
					value="${parametrosDistribuidor.listClassificacaoCota[0].valorDe}"
					type="text" disabled="disabled" style="width: 105px; text-align: right;" />
				</td>
				<td width="110" align="center">
					<input name="parametrosDistribuidor.listClassificacaoCota[0].valorAte"
					id="listClassificacaoCota0.valorAte"
					value="${parametrosDistribuidor.listClassificacaoCota[0].valorAte}"
					type="text" disabled="disabled" style="width: 105px; text-align: right;" maxlength="10"/>
				</td>
			</tr>
			<tr class="class_linha_2">
				<td>A
					<input type="hidden" name="parametrosDistribuidor.listClassificacaoCota[1].id" id="listClassificacaoCota1.id" value="${parametrosDistribuidor.listClassificacaoCota[1].id}" />
					<input type="hidden" name="parametrosDistribuidor.listClassificacaoCota[1].codigoClassificacaoCota" id="listClassificacaoCota1.codigoClassificacaoCota" value="A" />
				</td>
				<td align="center">
					<input name="parametrosDistribuidor.listClassificacaoCota[1].valorDe"
					id="listClassificacaoCota1.valorDe"
					value="${parametrosDistribuidor.listClassificacaoCota[1].valorDe}"
					type="text" disabled="disabled" style="width: 105px; text-align: right;" />
				</td>
				<td width="110" align="center">
					<input name="parametrosDistribuidor.listClassificacaoCota[1].valorAte"
					id="listClassificacaoCota1.valorAte"
					value="${parametrosDistribuidor.listClassificacaoCota[1].valorAte}"
					type="text" style="width: 105px; text-align: right;" maxlength="10"/>
				</td>
			</tr>
			<tr class="class_linha_1">
				<td>B
					<input type="hidden" name="parametrosDistribuidor.listClassificacaoCota[2].id" id="listClassificacaoCota2.id" value="${parametrosDistribuidor.listClassificacaoCota[2].id}" />
					<input type="hidden" name="parametrosDistribuidor.listClassificacaoCota[2].codigoClassificacaoCota" id="listClassificacaoCota2.codigoClassificacaoCota" value="B" />
				</td>
				<td align="center">
					<input name="parametrosDistribuidor.listClassificacaoCota[2].valorDe"
					id="listClassificacaoCota2.valorDe"
					value="${parametrosDistribuidor.listClassificacaoCota[2].valorDe}"
					type="text" disabled="disabled" style="width: 105px; text-align: right;" />
				</td>
				<td align="center">
					<input name="parametrosDistribuidor.listClassificacaoCota[2].valorAte"
					id="listClassificacaoCota2.valorAte"
					value="${parametrosDistribuidor.listClassificacaoCota[2].valorAte}"
					type="text" style="width: 105px; text-align: right;" maxlength="10"/>
				</td>
			</tr>
			<tr class="class_linha_2">
				<td>C
					<input type="hidden" name="parametrosDistribuidor.listClassificacaoCota[3].id" id="listClassificacaoCota3.id" value="${parametrosDistribuidor.listClassificacaoCota[3].id}" />
					<input type="hidden" name="parametrosDistribuidor.listClassificacaoCota[3].codigoClassificacaoCota" id="listClassificacaoCota3.codigoClassificacaoCota" value="C" />
				</td>
				<td align="center">
					<input name="parametrosDistribuidor.listClassificacaoCota[3].valorDe"
					id="listClassificacaoCota3.valorDe"
					value="${parametrosDistribuidor.listClassificacaoCota[3].valorDe}"
					type="text" disabled="disabled" style="width: 105px; text-align: right;" />
				</td>
				<td align="center">
					<input name="parametrosDistribuidor.listClassificacaoCota[3].valorAte"
					id="listClassificacaoCota3.valorAte"
					value="${parametrosDistribuidor.listClassificacaoCota[3].valorAte}"
					type="text" style="width: 105px; text-align: right;" maxlength="10"/>
				</td>
			</tr>
			<tr class="class_linha_1">
				<td>D
					<input type="hidden" name="parametrosDistribuidor.listClassificacaoCota[4].id" id="listClassificacaoCota4.id" value="${parametrosDistribuidor.listClassificacaoCota[4].id}" />
					<input type="hidden" name="parametrosDistribuidor.listClassificacaoCota[4].codigoClassificacaoCota" id="listClassificacaoCota4.codigoClassificacaoCota" value="D" />
				</td>
				<td align="center">
					<input name="parametrosDistribuidor.listClassificacaoCota[4].valorDe"
					id="listClassificacaoCota4.valorDe"
					value="${parametrosDistribuidor.listClassificacaoCota[4].valorDe}"
					type="text" disabled="disabled" style="width: 105px; text-align: right;" />
				</td>
				<td align="center">
					<input name="parametrosDistribuidor.listClassificacaoCota[4].valorAte"
					id="listClassificacaoCota4.valorAte"
					value="${parametrosDistribuidor.listClassificacaoCota[4].valorAte}"
					type="text" style="width: 105px; text-align: right;" maxlength="10"/>
				</td>
			</tr>
		</table>
	</fieldset>

	<fieldset style="width: 290px;">
		<legend>% Percentual de Excedente</legend>

		<table width="284" border="0" cellpadding="0" cellspacing="1">
			<tr class="header_table">
				<td>&nbsp;</td>
				<td colspan="2" align="center">Excedente</td>
			</tr>
			<tr class="header_table">
				<td>Eficiência</td>
				<td align="center">Venda</td>
				<td align="center">PDV</td>
			</tr>
			<tr class="class_linha_1">
				<td width="90">&gt; 60 %
					<input type="hidden" name="parametrosDistribuidor.listPercentualExcedente[0].id" id="listPercentualExcedente0.id" value="${parametrosDistribuidor.listPercentualExcedente[0].id}" />
					<input type="hidden" name="parametrosDistribuidor.listPercentualExcedente[0].eficiencia" id="listPercentualExcedente0.eficiencia" value="DE_60_100" />
				</td>
				<td width="75" align="center">
					<input name="parametrosDistribuidor.listPercentualExcedente[0].venda"
					id="listPercentualExcedente0.venda"
					value="${parametrosDistribuidor.listPercentualExcedente[0].venda}"
					type="text" style="width: 70px; text-align: center;" maxlength="2" />
				</td>
				<td width="75" align="center">
					<input name="parametrosDistribuidor.listPercentualExcedente[0].pdv"
					id="listPercentualExcedente0.pdv"
					value="${parametrosDistribuidor.listPercentualExcedente[0].pdv}"
					type="text" style="width: 70px; text-align: center;" maxlength="2" />
				</td>
			</tr>
			<tr class="class_linha_2">
				<td>&gt; 30% a 60%
					<input type="hidden" name="parametrosDistribuidor.listPercentualExcedente[1].id" id="listPercentualExcedente1.id" value="${parametrosDistribuidor.listPercentualExcedente[1].id}" />
					<input type="hidden" name="parametrosDistribuidor.listPercentualExcedente[1].eficiencia" id="listPercentualExcedente1.eficiencia" value="DE_30_60" />
				</td>
				<td align="center">
					<input name="parametrosDistribuidor.listPercentualExcedente[1].venda"
					id="listPercentualExcedente1.venda"
					value="${parametrosDistribuidor.listPercentualExcedente[1].venda}"
					type="text" style="width: 70px; text-align: center;" maxlength="2" />
				</td>
				<td width="75" align="center">
					<input name="parametrosDistribuidor.listPercentualExcedente[1].pdv"
					id="listPercentualExcedente1.pdv"
					value="${parametrosDistribuidor.listPercentualExcedente[1].pdv}"
					type="text" style="width: 70px; text-align: center;" maxlength="2" />
				</td>
			</tr>
			<tr class="class_linha_1">
				<td>0% a 30%
					<input type="hidden" name="parametrosDistribuidor.listPercentualExcedente[2].id" id="listPercentualExcedente2.id" value="${parametrosDistribuidor.listPercentualExcedente[2].id}" />
					<input type="hidden" name="parametrosDistribuidor.listPercentualExcedente[2].eficiencia" id="listPercentualExcedente2.eficiencia" value="DE_0_30" />
				</td>
				<td align="center">
					<input name="parametrosDistribuidor.listPercentualExcedente[2].venda"
					id="listPercentualExcedente2.venda"
					value="${parametrosDistribuidor.listPercentualExcedente[2].venda}"
					type="text" style="width: 70px; text-align: center;" maxlength="2" />
				</td>
				<td align="center">
					<input name="parametrosDistribuidor.listPercentualExcedente[2].pdv"
					id="listPercentualExcedente2.pdv"
					value="${parametrosDistribuidor.listPercentualExcedente[2].pdv}"
					type="text" style="width: 70px; text-align: center;" maxlength="2" />
				</td>
			</tr>
		</table>
	</fieldset>
	<br clear="all" />
</div>
