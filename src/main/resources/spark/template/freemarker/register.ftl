<#import "main.ftl" as layout>

<@layout.main title="Register">
<form action="/register" method="post">
    <label for="username">Username</label>:
    <input type="text" name="username" title="Username" value="${username!}"/>
    <br/>
    <label for="password">Password</label>:
    <input type="password" name="password" title="Password"/>
    <br/>
    <label for="email">e-mail</label>:
    <input type="email" name="email" title="e-mail" value="${email!}"/>
    <br/>
    <input type="submit" name="registerBtn" value="Register"/>
</form>
</@layout.main>
