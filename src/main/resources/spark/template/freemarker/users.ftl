<#import "main.ftl" as layout>

<@layout.main title="Users">
    <#if user??>
        <h2>Users you follow</h2>
        <div class="following">
        <#list following as i>
            <a href="/user/${i.username}" <#if i.verified!false>class="verified"</#if>>${i.username}</a>
        </#list>
        </div>
        <h2>All users</h2>
    </#if>
    <div class="users">
        <#list users as j>
            <a href="/user/${j.username}" <#if j.verified!false>class="verified"</#if>>${j.username}</a>
        </#list>
    </div>
</@layout.main>