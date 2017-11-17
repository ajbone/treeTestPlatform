package com.dashu.datashow.mapper;

import com.dashu.datashow.domain.BugFixedObject;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by shenzhaohua on 16/5/17.
 */
public interface BugFixedMapper {


        @Select("select datediff as bugFixed,count(datediff) as bugDate from (SELECT bi.id, DATEDIFF(bi.updated_at,bi.created_at) as datediff ,bi.assign_to,bi.created_by  FROM bf_bug_info bi LEFT JOIN bf_product pr ON pr.id = bi.product_id  " +
                "where pr.ID = #{2}  and bug_status in ('Resolved','Closed') AND bi.updated_at between #{0} and #{1}) t group by datediff order by bugDate DESC limit 20")
        List<BugFixedObject> getList(String startDate, String endDate, String project);

}
