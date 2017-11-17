package com.dashu.datashow.mapper;

import com.dashu.datashow.domain.BugProjectObject;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by adrian on 16/5/17.
 */
public interface BugProjectMapper {


        @Select("select name as projectName,id as projectKey from bf_product where id >1 ORDER BY id DESC ")
                List<BugProjectObject> getList();

}
