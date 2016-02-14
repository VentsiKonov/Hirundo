<#import "main.ftl" as layout>
<#import "timeline.ftl" as timeline>

<@layout.main title="Profile">
    <h2>
        User <#if targetUser.verified!false><p class="verified"></#if>${targetUser.username}<#if targetUser.verified!false></p></#if>
            <#if user??>
                [
                <#if following!false>
                <a href="/unfollow/${targetUser.username}">Unfollow</a>
                <#else>
                <a href="/follow/${targetUser.username}">Follow</a>
                </#if>
                ]
            </#if>
            [<a href="/statistics/${targetUser.username}">See statistics</a>]
    </h2>

    <@timeline.timeline/>
</@layout.main>
