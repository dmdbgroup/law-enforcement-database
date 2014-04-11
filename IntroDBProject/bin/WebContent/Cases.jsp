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
	Title:
	<input type="text" name="title" />
	<br/>
	Description:
	<input type="text" name="description" />
	<br/>
	Address ( Street StreetNo, PLZ Ort ):
	<input type="text" name="address" />
	<br/>
	Time ( yyyy-mm-dd hh:mm:ss ):
	<input type="text" name="time" />
	<br/>
	<input type="hidden" name="creator" value="<%=user.getName()%>" />
	
	<input type="hidden" name="action" value="addcase" />
	
	<input type="submit" value="addcase" title="addcase" />
</div>
</form>
<% } %>

<hr/>

<%@ include file="Footer.jsp" %>