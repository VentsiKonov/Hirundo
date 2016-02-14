<#macro timeline>

    <#if messages??>
    <div class="timeline">
        <#list messages as message>
            <div class="message">
                <p>
                    [${message.datePublished?string["dd.MM.yyyy, HH:mm"]}]&nbsp;
                    <#if message.place?? && message.place != ""> at ${message.place}</#if>
                </p>
                <a href="/user/${message.authorName}"><strong>${message.authorName}</strong></a>:
            ${message.content}
            </div>
            <#else>
            This user has no messages!
        </#list>
    </div>
    </#if>

</#macro>