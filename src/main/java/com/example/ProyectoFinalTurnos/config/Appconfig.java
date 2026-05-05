package com.example.ProyectoFinalTurnos.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;

public class Appconfig {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
