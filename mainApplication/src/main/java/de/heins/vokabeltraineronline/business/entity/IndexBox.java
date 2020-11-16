package de.heins.vokabeltraineronline.business.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"appUser", "name", "subject"})})
public class IndexBox  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
    private String subject;
    private boolean actualInUse;
    
    @ManyToMany
	private Set<QuestionWithAnswer> questionWithAnswers;

	@JoinColumn(name = "appUser")
	@ManyToOne
	private AppUser appUser;
    
    public IndexBox() {
    }
    public IndexBox(//
    		Long id2//
    		, String name2//
    		, String subject2//
    		, AppUser appUser2//
    	) {
    	this.id=id2;
		this.name=name2;
		this.subject=subject2;
		this.actualInUse=false;
		this.appUser=appUser2;
		
		this.questionWithAnswers=new HashSet<QuestionWithAnswer>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public AppUser getAppUser() {
		return appUser;
	}
	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

	public Set<QuestionWithAnswer> getQuestionsWithAnsers() {
		return this.questionWithAnswers;
	}
	public void addQuestionWithAnswer(QuestionWithAnswer questionWithAnswer) {
		this.questionWithAnswers.add(questionWithAnswer);
	}
	
	public void removeQuestionWithAnswer(QuestionWithAnswer questionWithAnswer) {
		this.questionWithAnswers.remove(questionWithAnswer);
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public boolean isActualInUse() {
		return actualInUse;
	}
	public void setActualInUse(boolean actualInUse) {
		this.actualInUse = actualInUse;
	}

}
