<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags/identime" prefix="local" %>
<local:site-theme title="Claimed Identity Page">
  <p>This is an OpenID Claimed Identity URL for a user. If this is your identity,
  you may use it to log in by giving it to a website that supports OpenID login
  (a Relying Party).</p>
  <p>If you are a new user and want to sign up for an OpenID, <a href="${baseURL}/register">go here</a>.</p> 
</local:site-theme>