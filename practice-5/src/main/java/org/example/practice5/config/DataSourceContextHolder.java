package org.example.practice5.config;

import org.example.practice5.util.DataSourceEnum;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DataSourceContextHolder {
    private static final ThreadLocal<DataSourceEnum> context =
            ThreadLocal.withInitial(() -> DataSourceEnum.PRIMARY);

    public void setBranchContext(DataSourceEnum dataSourceEnum) {
        context.set(dataSourceEnum);
    }

    public DataSourceEnum getBranchContext() {
        return context.get();
    }

    public void clearBranchContext() {
        context.remove();
    }
}