package de.heins.vokabeltraineronline.business.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.heins.vokabeltraineronline.business.repository.IndexBoxRepository;
import de.heins.vokabeltraineronline.business.repository.AppUserRepository;
import de.heins.vokabeltraineronline.business.entity.IndexBox;
import de.heins.vokabeltraineronline.business.entity.AppUser;
import de.heins.vokabeltraineronline.web.entities.IndexBoxForm;
import de.heins.vokabeltraineronline.web.entities.SessionAppUserForm;


@Service
public class IndexBoxService {
	@Autowired
	private IndexBoxRepository indexBoxRepository;
	@Autowired
	private AppUserRepository appUserRepository;
	public List<IndexBoxForm> findAllForAppUser(SessionAppUserForm sessionAppUserForm) {
		List<AppUser> appUsers = appUserRepository.findByEmail(sessionAppUserForm.getEmail());
		if (appUsers.size() == 1) {
			AppUser appUser = appUsers.get(0);
			List<IndexBoxForm> indexBoxForms = new ArrayList<IndexBoxForm>();
			try {
				List<IndexBox> indexBoxes = indexBoxRepository.findByAppUser(appUser);
				indexBoxes.iterator().forEachRemaining(indexBox -> {
					IndexBoxForm indexBoxForm = new IndexBoxForm();
					indexBoxForm.setName(indexBox.getName());
					indexBoxForms.add(indexBoxForm);
				});
			} catch (Exception e) {
				// this occurs only when there are no items in the database table,
				// or the table wasn't created yet.
				// nothing to do then.
			}
			return indexBoxForms;
		} else {
			throw new RuntimeException("No AppUser found or AppUser not unique by email");
		}
	}
}
