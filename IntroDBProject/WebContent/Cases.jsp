<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="ch.ethz.inf.dbproject.util.UserManagement"%>
<%@page import="ch.ethz.inf.dbproject.model.User"%>
<%@ include file="Header.jsp" %>
<% final User user = (User) session.getAttribute(UserManagement.SESSION_USER); %>

<h1>Cases</h1>

<hr/>

<%= session.getAttribute("cases") %>

<hr/>

<% if (user != null) { %>
<h2>Add new case:</h2>

<form method="get" action="Cases">
<div>
	<table>
		<tr>
			<td>Title:</td>
			<td><input type="text" name="title" /></td>
		</tr>
		<tr>
			<td>Description:</td>
			<td><input type="text" name="description" /></td>
		</tr>
		<tr>
			<td>Address ( Street StreetNo, PLZ Ort ):</td>
			<td><input type="text" name="address" /></td>
		</tr>
		<tr>
			<td>Time ( yyyy-mm-dd ):</td>
			<td><input type="text" name="time" /></td>
		</tr>
	</table>
	<input type="hidden" name="creator" value="<%=user.getName()%>" />
	
	<input type="hidden" name="action" value="addcase" />
	
	<input type="submit" value="addcase" title="addcase" />
</div>
</form>
<% } %>

<hr/>

<%@ include file="Footer.jsp" %>