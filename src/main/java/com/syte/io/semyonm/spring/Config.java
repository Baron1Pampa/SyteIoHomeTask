package com.syte.io.semyonm.spring;

import com.google.protobuf.util.JsonFormat;
import com.syte.io.semyonm.dao.InMemoryToDoListDao;
import com.syte.io.semyonm.dao.ToDoListDao;
import com.syte.io.semyonm.facade.ToDoListFacade;
import com.syte.io.semyonm.facade.ToDoListFacadeManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter;

@Configuration
public class Config {

    @Bean
    ProtobufHttpMessageConverter protobufHttpMessageConverter() throws Exception {
        JsonFormat.Printer printer = JsonFormat.printer();
        printer.includingDefaultValueFields();
        return new ProtobufJsonFormatHttpMessageConverter(null, printer);
    }

    @Bean
    ToDoListDao toDoListDao() {
        return new InMemoryToDoListDao();
    }

    @Bean
    ToDoListFacade toDoListFacade(ToDoListDao toDoListDao) {
        return new ToDoListFacadeManager(toDoListDao);
    }
}
