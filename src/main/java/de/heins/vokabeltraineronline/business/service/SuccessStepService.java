package de.heins.vokabeltraineronline.business.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.heins.vokabeltraineronline.business.repository.SuccessStepRepository;
import de.heins.vokabeltraineronline.business.repository.AppUserRepository;
import de.heins.vokabeltraineronline.business.entity.BehaviourIfWrong;
import de.heins.vokabeltraineronline.business.entity.SuccessStep;
import de.heins.vokabeltraineronline.business.entity.SuccessStepFactory;
import de.heins.vokabeltraineronline.business.entity.AppUser;
import de.heins.vokabeltraineronline.web.entities.SessionAppUser;
import de.heins.vokabeltraineronline.web.entities.attributereference.SuccessStepAttrRef;


@Service
public class SuccessStepService {
	@Autowired
	private SuccessStepRepository successStepRepository;
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private SuccessStepFactory successStepFactory;
	
	public static final SuccessStepAttrRef EMPTY_SUCCESS_STEP = new SuccessStepAttrRef();
	
	public List<SuccessStepAttrRef> findAllForAppUser(SessionAppUser sessionAppUserForm) {
		List<AppUser> appUsers = appUserRepository.findByEmail(sessionAppUserForm.getEmail());
		if (appUsers.size() == 1) {
			AppUser appUser = appUsers.get(0);
			List<SuccessStepAttrRef> successStepAttrRefs = new ArrayList<SuccessStepAttrRef>();
			try {
				List<SuccessStep> successSteps = successStepRepository.findByAppUser(appUser);
				successSteps.forEach(successStep -> {
					SuccessStepAttrRef successStepAttrRef = new SuccessStepAttrRef();
					successStepAttrRef.setName(successStep.getName());
					successStepAttrRef.setNextAppearanceInDays(successStep.getNextAppearanceInDays());
					successStepAttrRef.setBehaviourIfWrong(successStep.getBehaviourIfWrong().name());
					successStepAttrRefs.add(successStepAttrRef);
				});
			} catch (Exception e) {
				// this occurs only if there are no items in the database table,
				// or the table wasn't created yet.
				// nothing to do then.
			}
			return successStepAttrRefs;
		} else {
			throw new RuntimeException("No AppUser found or AppUser not unique by email");
		}
	}
	public SuccessStepAttrRef findForAppUserAndName(SessionAppUser sessionAppUser, String name) {
		AppUser appUser = appUserRepository.findByEmail(sessionAppUser.getEmail()).get(0);
		
		try {
			List<SuccessStep> resultByAppUserAndName = successStepRepository.findByAppUserAndName(appUser, name);
			if (!resultByAppUserAndName.isEmpty()) {
				SuccessStep successStep = resultByAppUserAndName.get(0);
				SuccessStepAttrRef successStepAttrRef = new SuccessStepAttrRef();
				successStepAttrRef.setName(successStep.getName());
				successStepAttrRef.setNextAppearanceInDays(successStep.getNextAppearanceInDays());
				successStepAttrRef.setBehaviourIfWrong(successStep.getBehaviourIfWrong().name());
				return successStepAttrRef;
			}
		} catch (Exception e) {
			// this occurs only if there are no items in the database table,
			// or the table wasn't created yet.
			// nothing to do then.
		}
		return EMPTY_SUCCESS_STEP;
	}
	public void update(SessionAppUser appUserForm, SuccessStepAttrRef successStepAttrRef, String oldName) {
		AppUser appUser = appUserRepository.findByEmail(appUserForm.getEmail()).get(0);
		List<SuccessStep> findByAppUserAndNameList = new ArrayList<SuccessStep>();
		try {
			findByAppUserAndNameList = successStepRepository.findByAppUserAndName(appUser, oldName);
		} catch (Exception e) {
			// this occurs only if there are no items in the database table,
			// or the table wasn't created yet.
			// nothing to do then.
		}
		SuccessStep successStep;
		if (findByAppUserAndNameList.isEmpty()) {//
				successStep = successStepFactory//
					.setName(successStepAttrRef.getName())//
					.setNextAppearanceInDays(successStepAttrRef.getNextAppearanceInDays())//
					.setBehaviourIfWrong(BehaviourIfWrong.valueOf(successStepAttrRef.getBehaviourIfWrong()))//
					.setAppUser(appUser)//
					.getNewObject();
		} else {
			successStep = findByAppUserAndNameList.get(0); //
			successStep.setName(successStepAttrRef.getName());//
			successStep.setNextAppearanceInDays(successStepAttrRef.getNextAppearanceInDays());//
			successStep.setBehaviourIfWrong(BehaviourIfWrong.valueOf(successStepAttrRef.getBehaviourIfWrong()));
		}
		successStepRepository.save(successStep);
	}
	public List<String> getAllBehavioursIfWrongAsStringArray() {
		// TODO Auto-generated method stub
		return java.util.stream.Stream//
				.of(BehaviourIfWrong.values())//
				.map(BehaviourIfWrong::name)//
				.collect(Collectors.toList());
	}
}
