package com.project.tinkoff.repository;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.util.Properties;

public class IdGenerator extends SequenceStyleGenerator implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return session.getEntityPersister(null, object).getClassMetadata().getIdentifier(object, session);
    }

    @Override
    public void configure(Type type, Properties parameters, ServiceRegistry serviceRegistry) throws MappingException {
        parameters.setProperty("sequence", "hibernate_sequence");
        parameters.setProperty("sequence_name", "hibernate_sequence");
        super.configure(type, parameters, serviceRegistry);
    }
}
