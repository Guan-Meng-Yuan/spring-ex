package com.guanmengyuan.spring.ex.data.jpa;

import org.hibernate.dialect.MySQLDialect;
import org.hibernate.type.SqlTypes;

public class DefaultSQLDialect extends MySQLDialect {

    @Override
    protected String columnType(int sqlTypeCode) {
        if (sqlTypeCode == SqlTypes.NUMERIC) {
            return "decimal(65,30)";
        }
        if (sqlTypeCode == SqlTypes.DECIMAL) {
            return "decimal(65,30)";
        }
        return super.columnType(sqlTypeCode);
    }

}
