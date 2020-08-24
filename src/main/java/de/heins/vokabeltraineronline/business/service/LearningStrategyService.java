package de.heins.vokabeltraineronline.business.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.heins.vokabeltraineronline.business.entity.LearningStrategy;
import de.heins.vokabeltraineronline.business.entity.AppUser;
import de.heins.vokabeltraineronline.business.repository.LearningStrategyRepository;
import de.heins.vokabeltraineronline.business.repository.AppUserRepository;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.attributereference.LearningStrategyModAttr;

@Service
public class LearningStrategyService {
	@Autowired
	private LearningStrategyRepository learningStrategyRepository;
	@Autowired
	private AppUserRepository appUserRepository;
	public LearningStrategyModAttr getForLearningStrategy(LearningStrategy learningStrategy) {
		LearningStrategyModAttr learningStrategyForm = new LearningStrategyModAttr();
		learningStrategyForm.setName(learningStrategy.getName());
		return learningStrategyForm;
	}
	public List<LearningStrategyModAttr> findAllForAppUser(SessionAppUser sessionAppUserForm) {
		List<AppUser> appUsers = appUserRepository.findByEmail(sessionAppUserForm.getEmail());
		if (appUsers.size() == 1) {
			AppUser appUser = appUsers.get(0);
			List<LearningStrategyModAttr> learningStrategyForms = new ArrayList<LearningStrategyModAttr>();
			try {
				List<LearningStrategy> learningStrategies = learningStrategyRepository.findByAppUser(appUser);
				learningStrategies.iterator().forEachRemaining(//
						learningStrategy -> {
							LearningStrategyModAttr learningStrategyForm = new LearningStrategyModAttr();
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
			throw new RuntimeException("No AppUser found or AppUser not unique by email");
		}
	}
}
