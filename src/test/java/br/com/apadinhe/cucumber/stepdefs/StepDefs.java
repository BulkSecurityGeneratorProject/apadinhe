package br.com.apadinhe.cucumber.stepdefs;

import br.com.apadinhe.ApadinheApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = ApadinheApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
