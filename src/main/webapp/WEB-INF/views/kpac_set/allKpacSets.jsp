<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>K-PAC Set</title>
	<link rel="stylesheet" href="//cdn.dhtmlx.com/suite/edge/suite.css" type="text/css" />
	<link rel="stylesheet" href="https://snippet.dhtmlx.com/codebase/assets/css/auxiliary_controls.css">
	<script src="//cdn.dhtmlx.com/suite/edge/suite.js"></script>
</head>
<body>
<style>
	<%@include file='/WEB-INF/views/css/design.css' %>
</style>
<section class="dhx_sample-container" style="height: 10%; width: 17.5%">
	<div style="height: 70%; width: 73%" id="grid"></div>
</section>
<button class="dhx_demo-button" onclick="addNewItem()">
	<span class="dhx_demo-button__icon dxi dxi-plus"></span>
	<span class="dhx_demo-button__text">Add new K-PAC Set</span>
</button>
<div id="kpac-set-form" style="display: none;">
	<form id="add-kpac-set-form">
		<label for="title-input">Title:</label>
		<input type="text" id="title-input" name="title"><br>
	</form>
</div>
<div id="kpac-table" style="display: none;">
	<table border="1">
		<tr>
			<th>ID</th>
			<th>Title</th>
			<th>Select</th>
		</tr>
		<c:forEach var="kpac" items="${kpacList}">
			<tr>
				<td>
					<c:out value="${kpac.id}"/>
				</td>
				<td>
					<c:out value="${kpac.title}"/>
				</td>
				<td>
					<button onclick="selectKpac(${kpac.id})">Select</button>
				</td>
			</tr>
		</c:forEach>
	</table>
</div>
<div id="kpac-set-submit-btn" style="display: none;">
	<form>
		<input type="submit" value="Save">
	</form>
</div>
<script>
	const selectedKpacs = [];

	function selectKpac(kpacId) {
		selectedKpacs.push(kpacId);
	}

	const kpacSetForm = document.getElementById("kpac-set-form");
	const kpacSetSubmitBtn = document.getElementById("kpac-set-submit-btn");
	const addKpacSetForm = document.getElementById("add-kpac-set-form");
	const kpacTable = document.getElementById("kpac-table");

	function addNewItem() {
		kpacSetForm.style.display = "block";
		kpacTable.style.display = "block";
		kpacSetSubmitBtn.style.display = "block";
	}

	kpacSetSubmitBtn.addEventListener("submit", function(event) {
		event.preventDefault();

		const title = document.getElementById('title-input').value;
		const selectedIds = selectedKpacs.join(',')

		const data = {title: title, kpacsIds: selectedIds}
		const xhr = new XMLHttpRequest();
		xhr.open('POST', '/sets', true);
		xhr.setRequestHeader('Content-Type', 'application/json');
		 xhr.onreadystatechange = function() {
		 	if (xhr.readyState === 4 && xhr.status === 200) {
				kpacSetForm.style.display = "none";
				addKpacSetForm.reset();
				location.reload();
		 	}
		 };
		xhr.send(JSON.stringify(data));
	});
	const grid = new dhx.Grid("grid", {
		css: "dhx_demo-grid",
		columns: [
			{
				width: 60,
				id: "id",
				header: [{ text: "Id" }, { content: "inputFilter" }],
				htmlEnable: true,
				template: function (obj) {
					return "<span class='action-buttons'><a class='redirect'>" + obj + "</a></span>"
				}
			},
			{
				width: 120, id: "title",
				header: [{ text: "Title"}, { content: "inputFilter" }],
				htmlEnable: true,
				template: function (obj) {
					return "<span class='action-buttons'><a class='redirect'>" + obj + "</a></span>"
				}
			},
			{
				id: "delete", gravity: 1.5, header: [{ text: " ", align: "center" }],
				width: 55,
				htmlEnable: true, align: "center",
				template: function () {
					return "<span class='action-buttons'><a class='remove-button'><span class='icon'></span></a></span>"
				}
			}
		],
		selection: "row",
		editable: false,
		eventHandlers: {
			onclick: {
				"remove-button": function (e, data) {
					const xhr = new XMLHttpRequest();
					xhr.open('DELETE', '/sets/' + data.row.id, true);
					grid.data.remove(data.row.id);
					xhr.send();
				},
				"redirect" : function (e,data) {
					window.location.href = "http://localhost:8080/set/" + data.row.id;
				}
			}
		}
	});

	const data = [
		<c:forEach items="${kpacSetList}" var="kpacSet" varStatus="status">
		{
			id: "${kpacSet.id}",
			title: "${kpacSet.title}",
		}<c:if test="${not status.last}">, </c:if>
		</c:forEach>
	]
	grid.data.parse(data);
</script>
</body>
</html>