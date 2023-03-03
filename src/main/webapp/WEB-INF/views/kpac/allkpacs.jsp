<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>K-PACs</title>
	<link rel="stylesheet" href="//cdn.dhtmlx.com/suite/edge/suite.css" type="text/css" />
	<link rel="stylesheet" href="https://snippet.dhtmlx.com/codebase/assets/css/auxiliary_controls.css">
	<script src="//cdn.dhtmlx.com/suite/edge/suite.js"></script>
</head>
<body>
<style>
	<%@include file='/WEB-INF/views/css/design.css'%>
</style>
<section class="dhx_sample-container" style="height: 90%; width: 45.7%">
	<div style="height: 90%; width: 100%" id="grid"></div>
</section>
<button class="dhx_demo-button" onclick="addNewItem()">
	<span class="dhx_demo-button__icon dxi dxi-plus"></span>
	<span class="dhx_demo-button__text">Add new K-PAC</span>
</button>
<div id="kpac-form" style="display: none;">
	<form id="add-kpac-form">
		<label for="title-input">Title:</label>
		<input type="text" id="title-input" name="title"><br>
		<label for="description-input">Description:</label>
		<textarea id="description-input" name="description"></textarea><br>
		<label for="criationDate-input">Creation Date (dd-mm-yyyy):</label>
		<input type="text" id="criationDate-input" name="creationDate"><br>
		<input type="submit" value="Save">
	</form>
</div>
<script>
	const kpacForm = document.getElementById("kpac-form");
	const addKpacForm = document.getElementById("add-kpac-form");

	function addNewItem() {
		kpacForm.style.display = "block";
	}

	addKpacForm.addEventListener("submit", function(event) {
		event.preventDefault();

		const title = document.getElementById("title-input").value;
		const description = document.getElementById("description-input").value;
		const creationDate = document.getElementById("criationDate-input").value;
		const data = {title: title, description: description, creationDate: creationDate}
		const xhr = new XMLHttpRequest();
		xhr.open('POST', '/kpacs', true);
		xhr.setRequestHeader('Content-Type', 'application/json');
		xhr.onreadystatechange = function() {
			if (xhr.readyState === 4 && xhr.status === 200) {
				kpacForm.style.display = "none";
				addKpacForm.reset();
				location.reload();
			}
		};
		xhr.send(JSON.stringify(data));
	});
	const grid = new dhx.Grid("grid", {
		css: "dhx_demo-grid",
		columns: [
			{ width: 60,
				id: "id",
				header: [{ text: "Id" }, { content: "inputFilter" }]
			},
			{ width: 120,
				id: "title",
				header: [{ text: "Title"}, { content: "inputFilter" }]
			},
			{
				width: 525,
				id: "description",
				header: [{ text: "Description", align: "center"}, { content: "inputFilter" }]
			},
			{
				width: 109,
				id: "creationDate",
				header:[{ text: "Creation Date", align: "center"}, { content: "inputFilter" }],
				type: "date",
				dateFormat: "%d-%m-%Y",
				align: "center"
			},
			{
				id: "delete", gravity: 1.5, header: [{ text: "Del", align: "center" }],
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
					xhr.open('DELETE', '/kpacs/' + data.row.id, true);
					grid.data.remove(data.row.id);
					xhr.send();
				}
			}
		}
	});

	const data = [
		<c:forEach items="${kpacList}" var="kpac" varStatus="status">
		{
			id: "${kpac.id}",
			title: "${kpac.title}",
			description: "${kpac.description}",
			creationDate: "${kpac.creationDate}",
		}<c:if test="${not status.last}">, </c:if>
		</c:forEach>
	]
	grid.data.parse(data);
</script>
</body>
</html>