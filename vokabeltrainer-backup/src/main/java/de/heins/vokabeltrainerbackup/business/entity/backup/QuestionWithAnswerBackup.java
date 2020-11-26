package de.heins.vokabeltrainerbackup.business.entity.backup;

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
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"appUserBackup", "question"})})
public class QuestionWithAnswerBackup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   
    @ManyToOne()
    @NotNull()
	private IndexBoxBackup indexBoxBackup;

    @ManyToOne
	private LearningStrategyBackup learningStrategyBackup;

    public LearningStrategyBackup getLearningStrategyBackup() {
		return learningStrategyBackup;
	}
	public void setLearningStrategyBackup(LearningStrategyBackup learningStrategyBackup) {
		this.learningStrategyBackup = learningStrategyBackup;
	}
	private String question;
    
	private String answer;
    
    private Date nextAppearance;
    
    @ManyToOne
    private SuccessStepBackup actualSuccessStepBackup;

	@ManyToOne
	@JoinColumn(name="appUserBackup")
	private AppUserBackup appUserBackup;
    public QuestionWithAnswerBackup() {
    }
    public QuestionWithAnswerBackup(//
    		Long id2//
    		, IndexBoxBackup indexBoxBackup2//
    		, LearningStrategyBackup learningStrategyBackup//
    		, String question2//
    		, String answer2//
    		, AppUserBackup appUserBackup2//
    	) {
    	this.id=id2;
    	this.indexBoxBackup=indexBoxBackup2;
    	this.learningStrategyBackup=learningStrategyBackup;
		this.question=question2;
		this.answer=answer2;
		this.appUserBackup=appUserBackup2;
		this.nextAppearance = Calendar.getInstance().getTime();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public IndexBoxBackup getIndexBoxBackup() {
		return indexBoxBackup;
	}
	public void setIndexBoxBackup(IndexBoxBackup indexBoxBackup) {
		this.indexBoxBackup = indexBoxBackup;
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
	public SuccessStepBackup getActualSuccessStepBackup() {
		return actualSuccessStepBackup;
	}
	public void setActualSuccessStepBackup(SuccessStepBackup actualSuccessStepBackup) {
		this.actualSuccessStepBackup = actualSuccessStepBackup;
	}
	public AppUserBackup getAppUserBackup() {
		return appUserBackup;
	}
	public void setAppUserBackup(AppUserBackup appUserBackup) {
		this.appUserBackup = appUserBackup;
	}

}
