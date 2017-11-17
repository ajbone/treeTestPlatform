package com.dashu.datashow.mapper;

import com.dashu.datashow.domain.BugPlatformObject;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by shenzhaohua on 17/4/17.
 */
public interface BugPlatformMapper {


        @Select("SELECT bi.bug_status as bugResolutionStatus, COUNT(bi.bug_status)  as bugNum FROM bf_bug_info bi LEFT JOIN bf_product pr ON pr.id = bi.product_id  where pr.id = #{2}   AND bi.created_at between #{0} and #{1} group by bi.bug_status")
        List<BugPlatformObject> getList(String startDate, String endDate, String project);
}
