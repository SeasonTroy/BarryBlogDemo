package com.bootdang.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bootdang.system.controller.loginController;
import com.bootdang.system.entity.Power;
import com.bootdang.system.entity.Role;
import com.bootdang.system.entity.User;
import com.bootdang.system.service.IPowerService;
import com.bootdang.system.service.IRoleService;
import com.bootdang.system.service.IRoleUserService;
import com.bootdang.system.service.IUserService;
import com.bootdang.system.service.impl.PowerServiceImpl;
import com.bootdang.system.service.impl.RoleServiceImpl;
import com.bootdang.system.service.impl.UserServiceImpl;
import com.bootdang.util.MyHashPassWordUtile;
import com.bootdang.util.MyWebApplictionUtil;
import com.bootdang.util.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sun.nio.cs.US_ASCII;

import javax.sound.midi.Soundbank;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class myRealm extends AuthorizingRealm {

    @Autowired
   IRoleService roleService;

    @Autowired
    IPowerService powerService;

    static final Logger log= LoggerFactory.getLogger(loginController.class);

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo (PrincipalCollection principalCollection) {
        Object primaryPrincipal = principalCollection.getPrimaryPrincipal();
        User of = User.of();
        BeanUtils.copyProperties(primaryPrincipal,of);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        /*角色*/
        List<Role> roles = roleService.selectRoleUserid(of.getUserId());
        List<String> collect = roles.stream().map((a) -> a.getSign()).collect(Collectors.toList());
        simpleAuthorizationInfo.addRoles(collect);

        List<Power> powers = powerService.selectByUserId(of.getUserId());
        HashSet<String> objects = new HashSet<>();
        powers.stream().forEach((power)->{
            objects.add(power.getPerms());
        });
        simpleAuthorizationInfo.addStringPermissions(objects);

        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo (AuthenticationToken authenticationToken)  {
        String principal = (String)authenticationToken.getPrincipal();
        UserServiceImpl bean = MyWebApplictionUtil.getBean(UserServiceImpl.class);
        User userByName = bean.isUserByName(principal);
        boolean present = Optional.ofNullable(userByName).isPresent();
        if(!present){
            return null;
        }else if(userByName.getState().equals("0")){
            throw new AuthenticationException("此账号状态异常");
        }

        ByteSource bytes = ByteSource.Util.bytes(userByName.getUsername());//盐
       // new MySimpleByteSource().
        //
        AuthenticationInfo simp= new SimpleAuthenticationInfo(userByName,userByName.getPassword(),bytes,getName());

        return simp;
    }


    public void clearCache() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }

    /**
     * 重写方法,清除当前用户的的 授权缓存
     * @param principals
     */
    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    /**
     * 重写方法，清除当前用户的 认证缓存
     * @param principals
     */
    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    /**
     * 自定义方法：清除所有 授权缓存
     */
    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    /**
     * 自定义方法：清除所有 认证缓存
     */
    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    /**
     * 自定义方法：清除所有的  认证缓存  和 授权缓存
     */
    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }


    public static void main (String[] args) {
        SimpleHash simpleHash=new SimpleHash("MD5","sysadmin","sysadmin",20);
        System.out.println(simpleHash.toString());
        String pass = MyHashPassWordUtile.encode("MD5", "sysadmin","sysadmin", 20);
        System.out.println(pass);
}
}
