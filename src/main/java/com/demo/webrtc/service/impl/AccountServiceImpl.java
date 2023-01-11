package com.demo.webrtc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.webrtc.bean.RolePool;
import com.demo.webrtc.domain.entity.Connection;
import com.demo.webrtc.domain.entity.WrRole;
import com.demo.webrtc.domain.entity.WrUser;
import com.demo.webrtc.domain.entity.WrUserRole;
import com.demo.webrtc.mapper.WrUserMapper;
import com.demo.webrtc.mapper.WrUserRoleMapper;
import com.demo.webrtc.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    WrUserMapper userMapperDao;

    @Autowired
    WrUserRoleMapper UserRoleDao;

    @Autowired
    RolePool rolePool;

    @Override
    public WrUser findUserByUserID(String userID) {
        return userMapperDao.selectById(userID);
    }

    @Override
    public WrUser findUserByUserID(Long userID) {
        return findUserByUserID(String.valueOf(userID));
    }

    @Override
    public WrUser findUserByUserName(String userName) {
        QueryWrapper<WrUser> WrUserQueryWrapper = new QueryWrapper<>();
        WrUserQueryWrapper.eq("account",userName);
        return userMapperDao.selectOne(WrUserQueryWrapper);
    }

    @Override
    public List<WrUserRole> findRolesByUserID(String userID) {
        QueryWrapper<WrUserRole> WrUserRoleQueryWrapper = new QueryWrapper<>();
        WrUserRoleQueryWrapper.eq("user_id", userID);
        return UserRoleDao.selectList(WrUserRoleQueryWrapper);
    }

    @Override
    public List<WrUserRole> findRolesByUserID(Long userID) {
        return findRolesByUserID(String.valueOf(userID));
    }

    @Override
    public List<WrUserRole> findRolesByUserName(String userName) {
        return findRolesByUserID(findUserByUserName(userName).getId());
    }

    @Override
    public WrRole getRoleByID(String RoleID) {
        return rolePool.getRoles().get(RoleID);
    }

    @Override
    public WrRole getRoleByID(Long RoleID) {
        return getRoleByID(String.valueOf(RoleID));
    }

    @Override
    public Connection getConnection(Subject currentUser) {
        // TODO
        StringBuilder role = new StringBuilder();
        for(Object o : currentUser.getPrincipals()){
            WrUser s = (WrUser) o;
            role.append(s.getAccount()).append(",");
        }
        log.info("================登出================");
        log.info("Username = " + role);
        log.info("===================================");
        currentUser.logout();
        return null;
    }
}
