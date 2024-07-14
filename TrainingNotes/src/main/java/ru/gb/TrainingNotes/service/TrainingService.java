package ru.gb.TrainingNotes.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.TrainingNotes.model.Training;
import ru.gb.TrainingNotes.model.User;
import ru.gb.TrainingNotes.repository.TrainingRepository;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class TrainingService {
    private final TrainingRepository trainingRepository;
    public Training createTraining(Training training) {
        training.setCreatedDate(LocalDate.now());
        training.setTimeStart(System.currentTimeMillis());
        return trainingRepository.save(training);
    }
    public List<Training> getAllNotes() {
        return trainingRepository.findAll();
    }
    public Training getTrainingById(Long id) {
        return trainingRepository.findById(id).orElseThrow(()-> new RuntimeException("Training not found"));
    }
    @Transactional
    public void deleteTraining(Long id) {
        trainingRepository.deleteById(id);
    }
    public List<Training> getTrainingsByUser(User user) {
        return trainingRepository.findByUser(user);
    }
    @Transactional
    public Training updateTraining(Long id, Training training) {
        Training trainingByID = trainingRepository.findById(training.getId()).orElseThrow(()-> new RuntimeException("Training not found"));
        trainingByID.setTitle(training.getTitle());
        training.setBody(training.getBody());
        return trainingRepository.save(trainingByID);
    }
}


