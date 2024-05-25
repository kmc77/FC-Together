package com.project.together.controller;

import com.project.together.domain.*;
import com.project.together.service.ManagementService;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/management")
public class ManagementController {

    private final ManagementService managementService;

    /*@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")*/
    @GetMapping("/management_customer_support")
    public String getRuleList(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "8") int limit, Model model) {

        int start = (page - 1) * limit;

        Map<String, Integer> params = new HashMap<>();
        params.put("start", start);
        params.put("limit", limit);

        List<Rule> ruleList = managementService.findAllRules(params);
        int totalRules = managementService.countRules();
        int totalPages = (int) Math.ceil((double) totalRules / limit);

        Map<Integer, Boolean> filePresenceMap = new HashMap<>();
        for (Rule rule : ruleList) {
            List<File> filesForRule = managementService.findFilesByRuleNum(rule.getRuleNum());
            filePresenceMap.put(rule.getRuleNum(), !filesForRule.isEmpty());
        }

        model.addAttribute("ruleList", ruleList);
        model.addAttribute("filePresenceMap", filePresenceMap);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "layout/management/management_customer_support";
    }

    @Autowired
    public ManagementController(ManagementService managementService) {
        this.managementService = managementService;
    }

    @GetMapping("/get{sectionName}Data")
    public ResponseEntity<?> getSectionData(@PathVariable String sectionName,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> response = new HashMap<>();
        System.out.println("1 ========== sectionName = " + sectionName);
        try {
            int offset = page * size;
            switch (sectionName.toLowerCase()) {
                case "rule":
                    List<Rule> rules = managementService.getRuleData(offset, size);
                    int totalRules = managementService.countRules();
                    Map<Integer, Boolean> filePresenceMapForRules = new HashMap<>();
                    for (Rule rule : rules) {
                        List<File> filesForRule = managementService.findFilesByRuleNum(rule.getRuleNum());
                        filePresenceMapForRules.put(rule.getRuleNum(), !filesForRule.isEmpty());
                    }
                    response.put("rules", rules);
                    response.put("totalRules", totalRules);
                    response.put("filePresenceMap", filePresenceMapForRules);
                    response.put("pageInfo", createPageInfo(page, size, totalRules));
                    break;
                case "operation":
                    List<Operation> operations = managementService.getOperationData(offset, size);
                    int totalOperations = managementService.countOperations();
                    Map<Integer, Boolean> filePresenceMapForOperations = new HashMap<>();
                    for (Operation operation : operations) {
                        List<File> filesForOperation = managementService.findFilesByOperationNum(operation.getOperationNum());
                        filePresenceMapForOperations.put(operation.getOperationNum(), !filesForOperation.isEmpty());
                    }
                    response.put("operations", operations);
                    response.put("totalOperations", totalOperations);
                    response.put("filePresenceMap", filePresenceMapForOperations);
                    response.put("pageInfo", createPageInfo(page, size, totalOperations));
                    break;
                case "faq":
                    List<Faq> faqs = managementService.getAllFaqData();
                    response.put("faqs", faqs);
                    break;
                case "schedule":
                    List<TrainingSchedule> schedules = managementService.getTrainingScheduleData(offset, size);
                    int totalSchedules = managementService.countTrainingSchedules();
                    response.put("schedules", schedules);
                    response.put("totalSchedules", totalSchedules);
                    response.put("pageInfo", createPageInfo(page, size, totalSchedules));
                    break;
                default:
                    return ResponseEntity.badRequest().body("Invalid section name");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching data for section: " + sectionName + ", error: " + e.getMessage());
        }
    }

    private Map<String, Object> createPageInfo(int currentPage, int size, int totalElements) {
        Map<String, Object> pageInfo = new HashMap<>();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        pageInfo.put("page", currentPage);
        pageInfo.put("total", totalElements);
        pageInfo.put("size", size);
        return pageInfo;
    }

    @GetMapping("/management_customer_support_rule_view")
    public String supportViewPage(@RequestParam("no") int ruleNum, Model model) throws NotFoundException {
        managementService.increaseRuleHits(ruleNum);
        Rule rule = managementService.viewRuleSupportPage(ruleNum);
        if (rule == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rule not found with id: " + ruleNum);
        }

        List<File> files = managementService.findFilesByRuleNum(ruleNum);
        model.addAttribute("rule", rule);
        model.addAttribute("files", files);

        LocalDate currentRuleDate = LocalDate.parse(rule.getRuleDate());
        Rule prevRule = managementService.findPrevRuleByCurrentRuleDate(currentRuleDate);
        if (prevRule != null) {
            model.addAttribute("prevRule", prevRule);
        }

        return "layout/management/management_customer_support_rule_view";
    }

    @GetMapping("/management_customer_support_operation_view")
    public String operationViewPage(@RequestParam("no") int operationNum, Model model) throws NotFoundException {
        managementService.increaseOperationHits(operationNum);
        Operation operation = managementService.viewOperationSupportPage(operationNum);
        if (operation == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Operation not found with id: " + operationNum);
        }

        List<File> files = managementService.findFilesByOperationNum(operationNum);
        model.addAttribute("operation", operation);
        model.addAttribute("files", files);

        LocalDate currentOperationDate = LocalDate.parse(operation.getOperationDate());
        Operation prevOperation = managementService.findPrevOperationByCurrentOperationDate(currentOperationDate);
        if (prevOperation != null) {
            model.addAttribute("prevOperation", prevOperation);
        }

        return "layout/management/management_customer_support_operation_view";
    }

    @GetMapping("/management_customer_support_schedule_view")
    public String scheduleViewPage(@RequestParam("no") int scheduleNum, Model model) throws NotFoundException {
        managementService.increaseScheduleHits(scheduleNum);
        TrainingSchedule trainingSchedule = managementService.viewSchedulePage(scheduleNum);
        if (trainingSchedule == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Operation not found with id: " + scheduleNum);
        }

        model.addAttribute("trainingSchedule", trainingSchedule);

        LocalDate currentScheduleDate = LocalDate.parse(trainingSchedule.getScheduleDate());
        TrainingSchedule prevSchedule = managementService.findPrevScheduleByCurrentScheduleDate(currentScheduleDate);
        if (prevSchedule != null) {
            model.addAttribute("prevSchedule", prevSchedule);
        }

        return "layout/management/management_customer_support_schedule_view";
    }
}
