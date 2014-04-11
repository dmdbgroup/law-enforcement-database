<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>

<h1>Case Details</h1>

<hr /> 
<%= session.getAttribute("includedCases") %>
<hr />
<%@ include file="Footer.jsp"%>