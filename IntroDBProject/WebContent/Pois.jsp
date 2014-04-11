<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>

<h1>Persons Of Interest</h1>

<hr/>
<%= session.getAttribute("pois") %> 
<hr/>

<h2>Comments</h2>
<hr/>
<%= session.getAttribute("comments") %>
<hr/>
<%@ include file="Footer.jsp" %>