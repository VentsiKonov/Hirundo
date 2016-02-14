<#import "main.ftl" as layout>
<#function rz n>
    <#if n == 0>
        <#return 1>
    <#else>
        <#return n>
    </#if>
</#function>
<@layout.main title="Statistics">
<#if targetUser??>
<h2><a href="/user/${targetUser.username}">${targetUser.username}</a>'s messages time and place distributions</h2>
    <#if timeStats??>
    <table class="timeTable">
        <tr>
            <th>From</th>
            <th>To</th>
            <th>Count</th>
            <th>Percentage</th>
        </tr>
        <#list timeStats as t>
            <#if !t?is_last>
                <tr>
                    <td>${t?index * 4}:00</td>
                    <td>${t?index * 4 + 4 - 1}:59</td>
                    <td>${t}</td>
                    <td>${(t / rz(timeStats?last?int))?string.percent}</td>
                </tr>
            </#if>
        </#list>
    </table>
    </#if>
    <#if placeStats??>
    <table class="placesTable">
        <tr>
            <th>Place</th>
            <th>Messages</th>
            <th>Percentage</th>
        </tr>
        <#list placeStats?keys as p>
            <#if p != "Total">
                <tr>
                    <td>${p}</td>
                    <td>${placeStats[p]}</td>
                    <td>${(placeStats[p] / rz(placeStats["Total"]))?string.percent}</td>
                </tr>
            </#if>
        </#list>
    </table>
    </#if>
    <br/>
    <#if timeStats?? || placeStats??>
        <h3>${targetUser.username}'s total messages:
        <#if timeStats??>
        ${timeStats?last?int}
        <#else>
        ${placeStats["Total"]}
        </#if>
        </h3>
    </#if>
</#if>
<#if topTen??>
<h2>Top 10 users</h2>
<#if word??>
    <h3>Ordered by messages containing "${word}"</h3>
</#if>
<table class="topTen" border="1">
    <tr>
        <th>Position</th>
        <th>Username</th>
        <th>Messages</th>
    </tr>
    <#list topTen as i>
        <tr>
            <td>${i?counter}</td>
            <td><a href="/statistics/${i._id}">${i._id}</a></td>
            <td>${i.count}</td>
        </tr>
    </#list>
</table>
<form class="topTenForm" action="/statistics" method="post">
    Word to search in comments:
    <input type="text" required="required" name="word" title="Word"/>
    <input type="submit" value="Get stats">
</form>
</#if>
</@layout.main>
