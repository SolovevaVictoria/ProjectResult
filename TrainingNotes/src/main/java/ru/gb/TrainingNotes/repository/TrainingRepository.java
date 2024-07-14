package ru.gb.TrainingNotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gb.TrainingNotes.model.Training;
import ru.gb.TrainingNotes.model.User;

import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Long> {
    List<Training> findByUser(User user);
}
