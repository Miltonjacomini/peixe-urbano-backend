package br.com.peixe.desafio.model.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class TypeDealDeserializer extends StdDeserializer<TypeDeal> {

    public TypeDealDeserializer() {
        super(TypeDeal.class);
    }

    @Override
    public TypeDeal deserialize(JsonParser jsonParser, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String name = node.get("name").asText();
        for (TypeDeal typeDeal : TypeDeal.values()) {

            if (typeDeal.name().equals(name)) {
                return typeDeal;
            }
        }

        return null;
    }
}