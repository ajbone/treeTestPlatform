package com.dashu.datashow.mapper;

import com.dashu.datashow.domain.BugSeverityObject;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by shenzhaohua on 16/5/17.
 */
public interface BugSeverityMapper {

        @Select("select severity as bugSeverity ,count(bi.severity) as bugNum FROM bf_bug_info bi LEFT JOIN bf_product pr ON pr.id = bi.product_id  where pr.id = #{2}   and bug_status in ('Active') AND bi.updated_at between #{0} and #{1} group by bi.severity")
        List<BugSeverityObject> getList(String startDate, String endDate, String project);
}
