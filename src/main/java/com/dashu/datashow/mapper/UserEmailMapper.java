package com.dashu.datashow.mapper;

import com.dashu.datashow.domain.UserEmailObject;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by adrian on 16/5/17.
 */
public interface UserEmailMapper {


        @Select("SELECT realname as userName , email as userEmail from bf_test_user ")
        List<UserEmailObject> getList();

}
