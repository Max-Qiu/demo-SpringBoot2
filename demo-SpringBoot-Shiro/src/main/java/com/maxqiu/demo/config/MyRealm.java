package com.maxqiu.demo.config;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import com.maxqiu.demo.entity.Permission;
import com.maxqiu.demo.entity.Role;
import com.maxqiu.demo.entity.User;
import com.maxqiu.demo.service.PermissionService;
import com.maxqiu.demo.service.RoleService;
import com.maxqiu.demo.service.UserService;

/**
 * @author Max_Qiu
 */
@Component
public class MyRealm extends AuthorizingRealm {
    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private PermissionService permissionService;

    // 自定义授权方法:获取当前登录用户的角色、权限信息，返回给shiro用来进行授权认证
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("自定义授权方法");
        // 1获取用户身份信息
        String principal = principalCollection.getPrimaryPrincipal().toString();
        User user = userService.getByName(principal);
        // 2调用业务层获取用户的角色信息（数据库）
        List<Role> roleList = roleService.getByUserId(user.getId());
        List<String> roleCodes = roleList.stream().map(Role::getCode).collect(Collectors.toList());
        System.out.println("当前用户角色信息 = " + roleCodes);
        // 2.5调用业务层获取用户的权限信息（数据库）
        List<Permission> permissionList = permissionService.getByRoleIds(roleList.stream().map(Role::getId).collect(Collectors.toList()));
        List<String> permissionCodes = permissionList.stream().map(Permission::getCode).collect(Collectors.toList());
        System.out.println("当前用户权限信息 = " + permissionCodes);
        // 3创建对象，封装当前登录用户的角色、权限信息
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRoles(roleCodes);
        info.addStringPermissions(permissionCodes);
        // 4返回信息
        return info;
    }

    // 自定义登录认证方法
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 1获取用户身份信息
        String name = authenticationToken.getPrincipal().toString();
        // 2调用业务层获取用户信息（数据库）
        User user = userService.getByName(name);
        // 3非空判断，将数据封装返回
        if (user != null) {
            return new SimpleAuthenticationInfo(authenticationToken.getPrincipal(), user.getPwd(), ByteSource.Util.bytes("salt"),
                authenticationToken.getPrincipal().toString());
        }
        return null;
    }
}
