package com.dashu.datashow.service;

import com.dashu.datashow.domain.UserEmailObject;
import com.dashu.datashow.mapper.UserEmailMapper;
import com.dashu.datashow.mapper.UserNameMapper;
import com.dashu.datashow.domain.BugProjectObject;
import com.dashu.datashow.mapper.BugProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * Created by shenzhaohua on 16/5/17.
 */
@Service
public class BugProjectService {

    @Autowired
    private BugProjectMapper bugProjectMapper;
    @Autowired
    private UserNameMapper userNameMapper;
    @Autowired
    private UserEmailMapper userEmailMapper;

    List<BugProjectObject> getList(){
        return bugProjectMapper.getList();
    };
//    List<UserNameObject> getUserList(){
//        return userNameMapper.getList();
//    };
    List<UserEmailObject> getEmailList(){
        return userEmailMapper.getList();
    };



    public HashMap selectBarPicture()  {
        List<BugProjectObject> ProjectList;

        ProjectList=this.getList();

        HashMap map=new HashMap<String, String>();
        for (BugProjectObject bugStatus:ProjectList){
            map.put(bugStatus.getProjectKey(),bugStatus.getProjectName());
        }

        return map;

    }

//    public HashMap selectUserName()  {
//        List<UserNameObject> UserList;
//
//        UserList=this.getUserList();
//        HashMap map=new HashMap<String, String>();
//        for (UserNameObject Users:UserList){
//            map.put(Users.getUserName(),Users.getEngName());
//        }
//
//        return map;
//
//    }

    public HashMap selectUserEmail()  {
        List<UserEmailObject> EmailList;

        EmailList=this.getEmailList();
        HashMap map=new HashMap<String, String>();
        for (UserEmailObject Emails:EmailList){
            map.put(Emails.getUserName(),Emails.getUserEmail());
        }

        return map;

    }
}
