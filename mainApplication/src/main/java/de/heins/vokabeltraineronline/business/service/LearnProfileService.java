package de.heins.vokabeltraineronline.business.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.heins.vokabeltraineronline.business.repository.AppUserRepository;
import de.heins.vokabeltraineronline.business.entity.AppUser;
import de.heins.vokabeltraineronline.business.entity.BehaviourIfPoolWithWrongAnswersIsFull;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.attributereference.LearnProfileAttrRef;


@Service
public class LearnProfileService {
	@Autowired
	private AppUserRepository appUserRepository;
	public List<String> getAllBehavioursIfPoolWithWrongAnswersIsFullAsStringArray() {
		return java.util.stream.Stream//
				.of(BehaviourIfPoolWithWrongAnswersIsFull.values())//
				.map(BehaviourIfPoolWithWrongAnswersIsFull::name)//
				.collect(Collectors.toList());
	}
	public void updateLearnProfile(//
			SessionAppUser sessionAppUser//
			, int maxNumberOfWrongAnswersPerSession//
			, String behaviourIfPoolWithWrongAnswersIsFull//
	) {
		AppUser appUser = appUserRepository.findByEmail(sessionAppUser.getEmail()).get(0);
		appUser.setBehaviourIfPoolWithWrongAnswersIsFull(BehaviourIfPoolWithWrongAnswersIsFull.valueOf(behaviourIfPoolWithWrongAnswersIsFull));
		appUser.setMaxNumberOfWrongAnswersPerSession(maxNumberOfWrongAnswersPerSession);
		appUserRepository.save(appUser);
		
	}
	public LearnProfileAttrRef findLearnProfileByUser(SessionAppUser sessionAppUser) {
		AppUser appUser = appUserRepository.findByEmail(sessionAppUser.getEmail()).get(0);
		LearnProfileAttrRef learnProfileAttrRef = new LearnProfileAttrRef();
		learnProfileAttrRef.setMaxNumberOfWrongAnswersPerSession(appUser.getMaxNumberOfWrongAnswersPerSession());
		learnProfileAttrRef.setBehaviourIfPoolWithWrongAnswersIsFull(appUser.getBehaviourIfPoolWithWrongAnswersIsFull().name());
		return learnProfileAttrRef;
		
	}
}
