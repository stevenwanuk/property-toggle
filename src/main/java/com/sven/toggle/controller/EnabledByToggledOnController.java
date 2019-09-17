package com.sven.toggle.controller;

import com.sven.toggle.condition.ConditionalOnToggle;
import com.sven.toggle.condition.PropertyToggle;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@ConditionalOnToggle(whenToggledOn = {PropertyToggle.BOOLEAN_TOGGLE}, whenToggledOff = {PropertyToggle.STRING_TOGGLE})
@RestController
public class EnabledByToggledOnController {

    @GetMapping
    public String test() {
        return "aaa";
    }
}
