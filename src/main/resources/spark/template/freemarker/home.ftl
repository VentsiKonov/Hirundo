<#import "main.ftl" as layout>
<#import "timeline.ftl" as timeline>
<@layout.main title="Home">
<#if user??>
    <h3>New Message</h3>
    <div class="newMessage">
        <form action="/message" method="post">
            <p>
                <textarea name="content" title="content" maxlength="140" rows="3"><#if msgContent??>${msgContent}</#if></textarea>
                <br/>
                Location: <input type="text" name="location" title="Location"/>
                <input type="submit" value="Send Hirund"/>
            </p>
        </form>
    </div>
<#else>
    <h1>Welcome to Hirundo!</h1>
</#if>
<#if messages??>
    <h3>My latest messages</h3>
    <@timeline.timeline/>
</#if>
</@layout.main>
