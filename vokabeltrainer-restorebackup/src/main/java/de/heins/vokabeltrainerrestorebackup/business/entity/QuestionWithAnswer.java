package de.heins.vokabeltrainerrestorebackup.business.entity;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.sun.istack.NotNull;
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"appUser", "question"})})
public class QuestionWithAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   
    @ManyToOne()
    @NotNull()
	private IndexBox indexBox;

    @ManyToOne
	private LearningStrategy learningStrategy;

    public LearningStrategy getLearningStrategy() {
		return learningStrategy;
	}
	public void setLearningStrategy(LearningStrategy learningStrategy) {
		this.learningStrategy = learningStrategy;
	}
	private String question;
    
	private String answer;
    
    private Date nextAppearance;
    
    @ManyToOne
    private SuccessStep actualSuccessStep;

	@ManyToOne
	@JoinColumn(name="appUser")
	private AppUser appUser;
    public QuestionWithAnswer() {
    }
    public QuestionWithAnswer(//
    		Long id2//
    		, IndexBox indexBox2//
    		, LearningStrategy learningStrategy//
    		, String question2//
    		, String answer2//
    		, AppUser appUser2//
    	) {
    	this.id=id2;
    	this.indexBox=indexBox2;
    	this.learningStrategy=learningStrategy;
		this.question=question2;
		this.answer=answer2;
		this.appUser=appUser2;
		this.nextAppearance = Calendar.getInstance().getTime();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public IndexBox getIndexBox() {
		return indexBox;
	}
	public void setIndexBox(IndexBox indexBox) {
		this.indexBox = indexBox;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public Date getNextAppearance() {
		return nextAppearance;
	}
	public void setNextAppearance(Date nextAppearance) {
		this.nextAppearance = nextAppearance;
	}
	public SuccessStep getActualSuccessStep() {
		return actualSuccessStep;
	}
	public void setActualSuccessStep(SuccessStep actualSuccessStep) {
		this.actualSuccessStep = actualSuccessStep;
	}
	public AppUser getAppUser() {
		return appUser;
	}
	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

}
