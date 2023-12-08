package br.com.fiapvideo.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;


public class UniqueValueValidator implements ConstraintValidator<UniqueValue, Object> {
    private static final long NENHUM_USUARIO_ENCONTRADO_COM_EMAIL_INFORMADO = 0;
    private String domainAttribute;
    private Class<?> klass;

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public void initialize(UniqueValue params) {
        domainAttribute = params.fieldName();
        klass = params.domainClass();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Query query = new Query(Criteria.where(domainAttribute).is(value));
        long count = mongoOperations.count(query, klass);

        return count == NENHUM_USUARIO_ENCONTRADO_COM_EMAIL_INFORMADO;
    }
}