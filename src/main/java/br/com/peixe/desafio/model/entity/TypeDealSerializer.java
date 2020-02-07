package br.com.peixe.desafio.model.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class TypeDealSerializer extends StdSerializer<TypeDeal> {

    public TypeDealSerializer() {
        super(TypeDeal.class);
    }

    @Override
    public void serialize(TypeDeal typeDeal, JsonGenerator generator, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        generator.writeStartObject();
        generator.writeFieldName("name");
        generator.writeString(typeDeal.name());
        generator.writeFieldName("descricao");
        generator.writeString(typeDeal.getDescricao());
        generator.writeEndObject();
    }
}
