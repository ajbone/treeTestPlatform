package com.dashu.datashow.mapper;

import com.dashu.datashow.domain.BugCheckObject;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by shenzhaohua on 16/5/17.
 */
public interface BugCheckMapper {


          @Select("select realname as bugReporter,count(bi.created_by) as bugNum FROM bf_bug_info bi LEFT JOIN bf_product pr ON pr.id = bi.product_id LEFT JOIN bf_test_user bu ON bi.created_by = bu.id" +
                  " where pr.id = #{2} and bug_status in('Resolved') AND bi.updated_at between #{0} and #{1} group by bi.created_by")
          List<BugCheckObject> getList(String startDate, String endDate, String project);

}
