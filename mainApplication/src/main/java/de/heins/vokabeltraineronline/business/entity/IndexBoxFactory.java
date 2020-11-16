package de.heins.vokabeltraineronline.business.entity;


import org.springframework.stereotype.Component;

@Component
public class IndexBoxFactory
{
    private Long id;

    private String name;
    
    private String subject;
    
    private AppUser appUser;
    
	public IndexBoxFactory setId(Long id) {
		this.id = id;
		return this;
	}


	public IndexBoxFactory setName(String name) {
		this.name = name;
		return this;
	}
	public IndexBoxFactory setAppUser(AppUser appUser) {
		this.appUser=appUser;
		return this;
	}


	public IndexBoxFactory setSubject(String subject) {
		this.subject = subject;
		return this;
	}
    public IndexBox getNewObject() {
    	return new IndexBox(this.id, this.name, this.subject, this.appUser);
	}

}
