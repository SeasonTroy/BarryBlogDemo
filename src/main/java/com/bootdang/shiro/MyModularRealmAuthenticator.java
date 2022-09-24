package com.bootdang.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.ArrayList;
import java.util.Collection;

//自定义认证器
public class MyModularRealmAuthenticator extends ModularRealmAuthenticator {
    @Override
    protected AuthenticationInfo doAuthenticate (AuthenticationToken authenticationToken) throws AuthenticationException {

        assertRealmsConfigured();
        MyUserNamePassWord customizedToken = (MyUserNamePassWord) authenticationToken;
        Collection<Realm> realms = getRealms();
        ArrayList<Realm> myrealms=new ArrayList<>();
        if(customizedToken.getType ()==1){//admin认证
         realms.forEach((a)->{
             if(a instanceof myAdminRealm){
                 myrealms.add(a);
             }
         });
        }else{
            realms.forEach((a)->{
                if(a instanceof myRealm){
                    myrealms.add(a);
                }
            });
        }
        if (myrealms.size() == 1)
            return doSingleRealmAuthentication(myrealms.iterator().next(), customizedToken);
        else
            return doMultiRealmAuthentication(myrealms, customizedToken);
    }

    public static void main (String[] args) {
        System.out.println();
    }

    }

