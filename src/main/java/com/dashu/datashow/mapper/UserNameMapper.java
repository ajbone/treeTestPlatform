package com.dashu.datashow.mapper;

import com.dashu.datashow.domain.UserNameObject;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by shenzhaohua on 16/5/17.
 */
public interface UserNameMapper {


        @Select("SELECT realname as userName , email as engName from bf_test_user")
        List<UserNameObject> getList();

}
