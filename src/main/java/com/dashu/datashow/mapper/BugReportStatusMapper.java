package com.dashu.datashow.mapper;

import com.dashu.datashow.domain.BugReportStatusObject;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by shenzhaohua on 16/5/17.
 */
public interface BugReportStatusMapper {


        @Select("select realname as bugReportStatus,count(bi.updated_by) as bugNum " +
                "FROM bf_bug_info bi LEFT JOIN bf_product pr ON pr.id = bi.product_id LEFT JOIN bf_test_user bu ON bi.assign_to = bu.id   where pr.id = #{2}  and bug_status in('Active') AND bi.created_at between #{0} and #{1} group by bi.assign_to\n")

        List<BugReportStatusObject> getList(String startDate, String endDate, String project);
}
