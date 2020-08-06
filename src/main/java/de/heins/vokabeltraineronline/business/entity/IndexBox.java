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
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"user", "name"})})
public class IndexBox  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
    
    @ManyToOne
	private LearningStrategy learningStrategy;
	
	@ManyToMany
	private Set<QuestionWithAnswer> questionWithAnswers;

	@JoinColumn(name = "user")
	@ManyToOne
	private User user;
    
    public IndexBox() {
    }
    public IndexBox(//
    		Long id2//
    		, String name2//
    		, User user2//
    	) {
    	this.id=id2;
		this.name=name2;
		this.user=user2;
		
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
	public LearningStrategy getLearningStrategy() {
		return learningStrategy;
	}
	public void setLearningStrategy(LearningStrategy learningStrategy) {
		this.learningStrategy = learningStrategy;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public void addQuestionWithAnswer(QuestionWithAnswer questionWithAnswer) {
		this.questionWithAnswers.add(questionWithAnswer);
	}
	
	public void removeQuestionWithAnswer(QuestionWithAnswer questionWithAnswer) {
		this.questionWithAnswers.remove(questionWithAnswer);
	}

}
