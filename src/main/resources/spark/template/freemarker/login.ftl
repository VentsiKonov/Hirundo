<#import "main.ftl" as layout>

<@layout.main title="Login">
<form action="/login" method="post">
    <label for="username">Username</label>:
    <input type="text" name="username" title="Username" value="${username!}"/>
    <br/>
    <label for="password">Password</label>:
    <input type="password" name="password" title="Password"/>
    <br/>
    <input type="submit" name="loginBtn" value="Log in"/>
</form>
</@layout.main>
