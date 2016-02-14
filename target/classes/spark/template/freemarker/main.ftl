<#macro main title="Home">

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>${title}</title>
        <style>
            body {
                margin: 0;
                padding: 0;
                background: #ccc;
            }

            .nav ul {
                list-style: none;
                background-color: #444;
                text-align: center;
                padding: 0;
                margin: 0;
            }
            .nav li {
                font-family: 'Oswald', sans-serif;
                font-size: 1.2em;
                line-height: 40px;
                height: 40px;
                border-bottom: 1px solid #888;
            }

            .nav a {
                text-decoration: none;
                color: #fff;
                display: block;
                transition: .3s background-color;
            }

            .nav a:hover {
                background-color: #005f5f;
            }

            .nav a.current {
                background-color: #fff;
                color: #444;
                cursor: default;
            }

            @media screen and (min-width: 600px) {
                .nav li {
                    width: 120px;
                    border-bottom: none;
                    height: 50px;
                    line-height: 50px;
                    font-size: 1.4em;
                }

                .nav li {
                    display: inline-block;
                    margin-right: -4px;
                }
            }

            .content {
                width: 750px;
                height: 600px;
                margin: 0 auto;
                padding-top: 10px;
                padding-bottom: 10px;
                font-size: large;
                border-left:1px solid black;
                border-right:1px solid black;
                text-align: center;
            }

            .footer {
                margin: 0 auto;
                width: 750px;
                border: 1px solid black;
            }
        </style>
    </head>
    <body>
        <div class="nav">
            <ul>
                <li><a href="/" <#if title == "Home">class="current"</#if>>Home</a></li>
                <#if user??>
                    <li><a href="/timeline" <#if title == "Timeline">class="current"</#if>>Timeline</a></li>
                    <li><a href="/statistics" <#if title == "Statistics">class="current"</#if>>Statistics</a></li>
                    <li><a href="/logout" <#if title == "Logout">class="current"</#if>>Logout</a></li>
                <#else>
                    <li><a href="/login" <#if title == "Login">class="current"</#if>>Login</a></li>
                    <li><a href="/register" <#if title == "Register">class="current"</#if>>Register</a></li>
                </#if>
            </ul>
        </div>
        <div class="content">
            <#nested />
        </div>
        <div class="footer">
            Footer
        </div>
    </body>
</html>

</#macro>