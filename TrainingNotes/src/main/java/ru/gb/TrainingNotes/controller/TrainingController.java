package ru.gb.TrainingNotes.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gb.TrainingNotes.model.Training;
import ru.gb.TrainingNotes.model.User;
import ru.gb.TrainingNotes.service.TrainingService;
import ru.gb.TrainingNotes.service.UserService;


import java.time.LocalDate;
import java.util.List;
@Slf4j
@AllArgsConstructor
@org.springframework.stereotype.Controller
public class TrainingController {

    private TrainingService trainingService;
    private UserService userService;

    @GetMapping("/home")
    String home() {
        return "home";
    }

    @GetMapping("/createTraining")
    public String createTrainingForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByLogin(authentication.getName());
        Training training = new Training();
        training.setCreatedDate(LocalDate.now());
        training.setTimeStart(System.currentTimeMillis());
        model.addAttribute("training", training);
        model.addAttribute("user", user);
        return "createTraining";
    }

    @PostMapping("/createTraining")
    public String createTrainings(Model model, @ModelAttribute Training training) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByLogin(authentication.getName());
        training.setTimeFinish(System.currentTimeMillis());
        long duration = (training.getTimeFinish() - training.getTimeStart()) / 1000 / 60;
        training.setDuration(duration);
        training.setUser(user);
        trainingService.createTraining(training);
        List<Training> trainingsList = trainingService.getTrainingsByUser(user);
        model.addAttribute("trainings", trainingsList);
        return "storyTraining";
    }

    @GetMapping("/storyTraining")
    public String diaryStory(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByLogin(authentication.getName());
        List<Training> noteList = trainingService.getTrainingsByUser(user);
        model.addAttribute("trainings", noteList);
        return "storyTraining";
    }

    @GetMapping("deleteTraining/{id}")
    public String deleteTraining(@PathVariable Long id) {
        Training training = trainingService.getTrainingById(id);
        User user = training.getUser();
        user.getTrainings().remove(training);
        userService.updateUsers(user.getId(), user);
        trainingService.deleteTraining(id);
        return "redirect:/storyTraining";
    }
    @GetMapping("/storyTrainingUpdate/{id}")
    public String updateTraining(@PathVariable("id") Long id, Model model) {
        Training training = trainingService.getTrainingById(id);
        model.addAttribute("training", training);
        return "storyTrainingUpdate";
    }
    @PostMapping("/storyTrainingUpdate/{id}")
    public String updateNote(@PathVariable Long id, @ModelAttribute Training training) {
        trainingService.getTrainingById(training.getId());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByLogin(authentication.getName());
        trainingService.updateTraining(id, training);
        return "redirect:/storyTraining";
    }
}
