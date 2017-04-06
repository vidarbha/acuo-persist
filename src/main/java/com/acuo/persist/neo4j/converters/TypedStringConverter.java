package com.acuo.persist.neo4j.converters;

import com.acuo.common.type.TypedString;
import com.acuo.persist.ids.ClientId;
import com.acuo.persist.ids.MarginStatementId;
import com.acuo.persist.ids.PortfolioId;
import com.acuo.persist.ids.TradeId;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.typeconversion.AttributeConverter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Slf4j
public abstract class TypedStringConverter<T extends TypedString> implements AttributeConverter<T, String> {

    public static class PortfolioIdConverter extends TypedStringConverter<PortfolioId> {}
    public static class ClientIdConverter extends TypedStringConverter<ClientId> {}
    public static class TradeIdConverter extends TypedStringConverter<TradeId> {}
    public static class MarginStatementIdConverter extends TypedStringConverter<MarginStatementId> {}

    @Override
    public String toGraphProperty(T value) {
        return value.toString();
    }

    @Override
    public T toEntityAttribute(String value) {
        Class<T> clazz = getReturnTypeFor();
        try {
            return (T) clazz.getMethod("fromString", new Class[]{String.class}).invoke(null, value);
        } catch (IllegalAccessException |
                InvocationTargetException |
                NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Class getReturnTypeFor() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType t = (ParameterizedType) type;
            if (t.getRawType().equals(TypedStringConverter.class)) {
                Type argument = t.getActualTypeArguments()[0];
                if (argument instanceof ParameterizedType) {
                    return (Class) ((ParameterizedType) argument).getRawType();
                }
                return (Class) argument;
            }
        }
        throw new RuntimeException("Your converter does not define its return type? ");
    }
}
