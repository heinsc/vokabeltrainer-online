package de.heins.vokabeltraineronline.business.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.heins.vokabeltraineronline.business.entity.LearningStrategy;
import de.heins.vokabeltraineronline.business.entity.User;
import de.heins.vokabeltraineronline.business.repository.LearningStrategyRepository;
import de.heins.vokabeltraineronline.business.repository.UserRepository;
import de.heins.vokabeltraineronline.web.entities.LearningStrategyForm;
import de.heins.vokabeltraineronline.web.entities.SessionUserForm;

@Service
public class LearningStrategyService {
	@Autowired
	private LearningStrategyRepository learningStrategyRepository;
	@Autowired
	private UserRepository userRepository;
	public LearningStrategyForm getForLearningStrategy(LearningStrategy learningStrategy) {
		LearningStrategyForm learningStrategyForm = new LearningStrategyForm();
		learningStrategyForm.setName(learningStrategy.getName());
		return learningStrategyForm;
	}
	public List<LearningStrategyForm> findAllForUser(SessionUserForm sessionUserForm) {
		List<User> users = userRepository.findByEmail(sessionUserForm.getEmail());
		if (users.size() == 1) {
			User user = users.get(0);
			List<LearningStrategyForm> learningStrategyForms = new ArrayList<LearningStrategyForm>();
			try {
				List<LearningStrategy> learningStrategies = learningStrategyRepository.findByUser(user);
				learningStrategies.iterator().forEachRemaining(//
						learningStrategy -> {
							LearningStrategyForm learningStrategyForm = new LearningStrategyForm();
							learningStrategyForm.setName(learningStrategy.getName());
							learningStrategyForms.add(learningStrategyForm);
						}
				);
			} catch (Exception e) {
				// this occurs only when there are no items in the database table,
				// or the table wasn't created yet.
				// nothing to do then.
			}
			return learningStrategyForms;
		} else {
			throw new RuntimeException("No User found or User not unique by email");
		}
	}
}
