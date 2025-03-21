package com.guanmengyuan.spring.ex.mybatis.flex.config;

import cn.dev33.satoken.stp.StpUtil;
import com.mybatisflex.core.dialect.IDialect;
import com.mybatisflex.core.logicdelete.impl.BooleanLogicDeleteProcessor;
import com.mybatisflex.core.table.TableInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

import static com.mybatisflex.core.constant.SqlConsts.EQUALS;

/**
 * 自定义逻辑删除处理器
 */
@Slf4j
public class MyLogicDeleteProcessor extends BooleanLogicDeleteProcessor {
    /**
     * 默认构造
     */
    public MyLogicDeleteProcessor() {
    }

    @Override
    public String buildLogicDeletedSet(String logicColumn, TableInfo tableInfo, IDialect dialect) {
        String[] allColumns = tableInfo.getAllColumns();
        List<String> list = Arrays.asList(allColumns);
        String sql = dialect.wrap(logicColumn) + EQUALS + getLogicDeletedValue();
        if (list.contains("delete_time")) {
            sql += "," + dialect.wrap("delete_time") + EQUALS + "now()";
        }
        if (list.contains("delete_user_id")) {
            try {
                long loginIdAsLong = StpUtil.getLoginIdAsLong();
                sql += "," + dialect.wrap("delete_user_id") + EQUALS + loginIdAsLong;
            } catch (Exception e) {
                log.warn("saToken get loginId unsuccessful");
            }
        }
        return sql;
    }
}
