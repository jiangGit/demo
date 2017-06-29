package com.bt.pja.common.mybatis.idgen;


import com.bt.pja.common.exception.SpringInitException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chenjiapeng on 2015/6/24 0024.
 */
public class IdGenerator {
    private IdGeneratorMapper mapper;
    private String tableName;

    private DataSourceTransactionManager transactionManager;

    private ConcurrentHashMap<String, IdGeneratorUnit> counters = new ConcurrentHashMap<String, IdGeneratorUnit>();

    private IdGenerator(IdGeneratorMapper mapper, String tableName, DataSourceTransactionManager transactionManager) {
        this.mapper = mapper;
        this.tableName = tableName;
        this.transactionManager = transactionManager;
        init();
    }

    private void init() {
        List<String> names = mapper.selectAllNames(tableName);
        for (String name : names) {
            this.counters.put(name, new IdGeneratorUnit(name));
        }
    }

    public int nextId(String name) {
        return mapIdInfo(name).nextId();
    }

    public long nextLongId(String name) {
        return mapIdInfo(name).nextLongId();
    }

    public static IdGenerator createIdGenerator(IdGeneratorMapper mapper, String tableName, DataSourceTransactionManager transactionManager) {
        return new IdGenerator(mapper, tableName, transactionManager);
    }

    private IdGeneratorUnit mapIdInfo(String name) {
        IdGeneratorUnit idInfo = counters.get(name);
        if (idInfo != null) {
            return idInfo;
        }
        idInfo = new IdGeneratorUnit(name);
        IdGeneratorUnit oldInfo = counters.put(name, idInfo);
        return oldInfo == null ? idInfo : oldInfo;
    }

    class IdGeneratorUnit {
        public String name;
        public long val;
        public int count;

        public IdGeneratorUnit(String name) {
            this(name, -1, 0);
        }

        public IdGeneratorUnit(String name, long val, int count) {
            this.name = name;
            this.val = val;
            this.count = count;
        }

        public int nextId() {
            long value = nextLongId();
            validateVal(value, 0, Integer.MAX_VALUE);
            return (int) value;
        }

        public synchronized long nextLongId() {
            if (count <= 0) {
                slide();
            }
            long value = val++;
            count--;
            validateVal(value, 0, Long.MAX_VALUE);
            return value;
        }

        private void validateVal(long val, long min, long max) {
            if (val < min || val > max) {
                throw new IllegalArgumentException(String.format("id illegal. [%s]", val));
            }
        }

        public void reset() {
            this.val = -1;
            this.count = 0;
        }

        public void slide() {
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            TransactionStatus status = transactionManager.getTransaction(def);
            try {
                IdGenInfo idGenInfo = mapper.selectByName(tableName, name);
                mapper.updateIdByName(tableName, name);
                this.val = idGenInfo.getCurrent();
                this.count = idGenInfo.getStep();
                transactionManager.commit(status);
            } catch (Exception e) {
                this.reset();
                transactionManager.rollback(status);
                throw new SpringInitException(String.format("cannot slide id. [%s]", name), e);
            }
        }
    }
}
