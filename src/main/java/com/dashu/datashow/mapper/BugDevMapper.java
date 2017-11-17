package com.dashu.datashow.mapper;

import com.dashu.datashow.domain.BugDevObject;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by shenzhaohua on 16/5/17.
 */
public interface BugDevMapper {

//         @Select("select realname as dev ,count(bi.assign_to) as bugNum FROM bf_bug_info bi LEFT JOIN bf_product pr ON pr.id = bi.product_id LEFT JOIN bf_test_user bu ON bi.assign_to = bu.id " +
//                 "where pr.id = #{2} AND bi.updated_at between #{0} and #{1} group by bi.assign_to order by bugNum DESC limit 10")
         @Select("select reopen_count as dev ,count(reopen_count) as bugNum FROM bf_bug_info where product_id = #{2} AND updated_at between #{0} and #{1} group by reopen_count order by reopen_count DESC")

         List<BugDevObject> getList(String startDate, String endDate, String project);

}
