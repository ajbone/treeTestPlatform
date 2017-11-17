package com.dashu.datashow.mapper;

import com.dashu.datashow.domain.TickNumTrendObject;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by shenzhaohua on 4/12/17.
 */
public interface TickNumMapper {
    @Select("SELECT \t DATE(bi.created_at) AS yearMonth,\n" +
            "\t\tCOUNT(bi.created_at) AS createdTicketNo,\n" +
            "\t\tT.CreatedTicketNo AS resolvedTciektNo\n" +
            "FROM bf_bug_info bi LEFT JOIN bf_product pr ON pr.id = bi.product_id " +
            "LEFT JOIN (\n" +
            "\tSELECT\n" +
            "\t\tDATE(bi.updated_at) AS YearMonth,\n" +
            "\t\tCOUNT(bi.updated_at) AS CreatedTicketNo\n" +
            "\tFROM bf_bug_info bi LEFT JOIN bf_product pr ON pr.id = bi.product_id " +
            "\tWHERE\n" +
            "\t\tpr.id = #{2}\n" +
            "\tAND bi.updated_at between #{0} and #{1}" +
            "\t and bug_status  in('Resolved','Closed') GROUP BY\n" +
            "\t\tDATE(bi.updated_at)\n" +
            "\t) T ON T.YearMonth = DATE(bi.created_at)\n" +
            "WHERE pr.id = #{2}\n" +
            "\tAND bi.created_at between #{0} and #{1} \n" +
            "GROUP BY DATE(bi.created_at);")
    List<TickNumTrendObject> getList(String startDate,String endDate,String project);
}
