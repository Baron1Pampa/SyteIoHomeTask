package com.syte.io.semyonm.spring;

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
            return new ProtobufJsonFormatHttpMessageConverter();
    }

    @Bean
    ToDoListDao toDoListDao (){
        return null;
    }

    @Bean
    ToDoListFacade toDoListFacade() {
        return new ToDoListFacadeManager();
    }
}
