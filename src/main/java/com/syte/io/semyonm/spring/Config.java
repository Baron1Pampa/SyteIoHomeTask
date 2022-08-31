package com.syte.io.semyonm.spring;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter;

@Configuration
public class Config {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.ALWAYS)
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    @Bean
    ProtobufHttpMessageConverter protobufHttpMessageConverter() throws Exception {
       /* JsonFormat.TypeRegistry registry = JsonFormat.TypeRegistry.newBuilder()
                .build();
        Set<Descriptors.FieldDescriptor> fieldsToAlwaysOutput = new HashSet<>();
        ListTasksResponse.getDescriptor().getFields()
                .forEach(t -> fieldsToAlwaysOutput.addAll(t.getMessageType().getFields()));
        JsonFormat.Printer printer = JsonFormat.printer().usingTypeRegistry(registry)
                .includingDefaultValueFields(fieldsToAlwaysOutput);

        JsonFormat.Parser parser = JsonFormat.parser().usingTypeRegistry(registry);*/
        return new ProtobufJsonFormatHttpMessageConverter();
    }



}
