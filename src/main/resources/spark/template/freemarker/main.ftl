<#macro main title="Home">

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>${title}</title>
        <link rel="stylesheet" type="text/css" href="/style.css">
    </head>
    <body>
        <div class="nav">
            <ul>
                <li><a href="/" <#if title == "Home">class="current"</#if>>Home</a></li>
                <li><a href="/users" <#if title == "Users">class="current"</#if>>Users</a></li>
                    <li><a href="/statistics" <#if title == "Statistics">class="current"</#if>>Statistics</a></li>
                <#if user??>
                    <li><a href="/following" <#if title == "Timeline">class="current"</#if>>Timeline</a></li>
                    <li><a href="/logout" <#if title == "Logout">class="current"</#if>>Logout</a></li>
                <#else>
                    <li><a href="/login" <#if title == "Login">class="current"</#if>>Login</a></li>
                    <li><a href="/register" <#if title == "Register">class="current"</#if>>Register</a></li>
                </#if>
            </ul>
        </div>
        <div class="content">
            <#if error??>
                <h2 class="error">${error}</h2>
            </#if>
            <#if success??>
                <h2 class="success">${success}</h2>
            </#if>
            <#nested />
        </div>
        <div class="footer">
            Hirundo | NoSQL databases basics with MongoDB | Ventsislav Konov | 2016
        </div>
    </body>
</html>

</#macro>