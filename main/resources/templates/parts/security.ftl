<#assign
known = Session.SPRING_SECURITY_CONTEXT??
<#-- if object is defined in context we can work with user session -->
>

<#if known>
<#-- if session exists -->
    <#assign
    user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
    name = user.getUsername()
    isAdmin = user.isAdmin()
    >
<#else>
    <#assign
    name = "unknown"
    isAdmin = false
    >
</#if>