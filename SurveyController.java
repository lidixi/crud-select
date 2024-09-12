package com.dyzc.web.controller.follow.homepage;

import com.dyzc.function.domain.*;
import com.dyzc.function.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/satisfaction")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @PostMapping("/statistics")
    public SurveyStatistics getSurveyStatistics(@RequestBody QueryFilter queryFilter) {
        return surveyService.getSurveyStatistics(queryFilter);
    }
}