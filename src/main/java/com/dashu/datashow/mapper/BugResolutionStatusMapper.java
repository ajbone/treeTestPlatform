package com.dashu.datashow.mapper;

import com.dashu.datashow.domain.BugResolutionStatusObject;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by shenzhaohua on 16/5/17.
 */
public interface BugResolutionStatusMapper {


           @Select("select Date(bi.created_at) as bugNewDate, COUNT(Date(bi.created_at))  as bugNum from bf_bug_info bi LEFT JOIN bf_product pr ON pr.id = bi.product_id  where pr.id = #{2}  and bi.created_at between #{0} and #{1} group by Date(bi.created_at) ")
         List<BugResolutionStatusObject> getList(String startDate, String endDate,String project);
}
