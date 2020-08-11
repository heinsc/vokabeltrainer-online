package de.heins.vokabeltraineronline.business.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.heins.vokabeltraineronline.business.repository.IndexBoxRepository;
import de.heins.vokabeltraineronline.business.repository.UserRepository;
import de.heins.vokabeltraineronline.business.entity.IndexBox;
import de.heins.vokabeltraineronline.business.entity.User;
import de.heins.vokabeltraineronline.web.entities.IndexBoxForm;
import de.heins.vokabeltraineronline.web.entities.SessionUserForm;


@Service
public class IndexBoxService {
	@Autowired
	private IndexBoxRepository indexBoxRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LearningStrategyService learningStrategyService;
	public List<IndexBoxForm> findAllForUser(SessionUserForm sessionUserForm) {
		List<User> users = userRepository.findByEmail(sessionUserForm.getEmail());
		if (users.size() == 1) {
			User user = users.get(0);
			List<IndexBoxForm> indexBoxForms = new ArrayList<IndexBoxForm>();
			try {
				List<IndexBox> indexBoxes = indexBoxRepository.findByUser(user);
				indexBoxes.iterator().forEachRemaining(indexBox -> {
					IndexBoxForm indexBoxForm = new IndexBoxForm();
					indexBoxForm.setName(indexBox.getName());
					indexBoxForm.setLearningStrategyForm(//
							learningStrategyService.getForLearningStrategy(//
									indexBox.getLearningStrategy()//
							)
					);
					indexBoxForms.add(indexBoxForm);
				});
			} catch (Exception e) {
				// this occurs only when there are no items in the database table,
				// or the table wasn't created yet.
				// nothing to do then.
			}
			return indexBoxForms;
		} else {
			throw new RuntimeException("No User found or User not unique by email");
		}
	}
}
