package ie.dodwyer.model;

import ie.dodwyer.activities.Base;

/**
 * Created by User on 2/4/2017.
 */

public class Challenge {
    private Base activity;
    private int challengeId;
    private String createdBy;
    private int gameId;
    private String nomineeId;
    private String subject;
    private String message;
    private double maxPointValue;
    private String result;
    private double pointsAwarded;
    private double  blankWordDecimal;
    private String creationDate;
    private String contactInReceiverAddressBook;


    public Challenge() {
    }

    public Challenge(int challengeId, String createdBy, int gameId, String nomineeId, String subject, String message, double maxPointValue, String result, double pointsAwarded, double blankWordDecimal,String creationDate, String contactInReceiverAddressBook) {
        this.challengeId = challengeId;
        this.createdBy = createdBy;
        this.gameId = gameId;
        this.nomineeId = nomineeId;
        this.subject = subject;
        this.message = message;
        this.maxPointValue = maxPointValue;
        this.result = result;
        this.pointsAwarded = pointsAwarded;
        this.blankWordDecimal = blankWordDecimal;
        this.creationDate = creationDate;
        this.contactInReceiverAddressBook = contactInReceiverAddressBook;
    }



    public Challenge(String createdBy, String subject, String message, double maxPointValue,String creationDate) {
        this.createdBy = createdBy;
        this.subject = subject;
        this.message = message;
        this.maxPointValue = maxPointValue;
        this.creationDate = creationDate;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getNomineeId() {
        return nomineeId;
    }

    public void setNomineeId(String nomineeId) {
        this.nomineeId = nomineeId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public double getPointsAwarded() {
        return pointsAwarded;
    }

    public void setPointsAwarded(double pointsAwarded) {
        this.pointsAwarded = pointsAwarded;
    }

    public double getBlankWordDecimal() {
        return blankWordDecimal;
    }

    public void setBlankWordDecimal(double blankWordDecimal) {
        this.blankWordDecimal = blankWordDecimal;
    }

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getMaxPointValue() {
        return maxPointValue;
    }

    public void setMaxPointValue(double maxPointValue) {
        this.maxPointValue = maxPointValue;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getContactInReceiverAddressBook() {
        return contactInReceiverAddressBook;
    }

    public void setContactInReceiverAddressBook(String contactInReceiverAddressBook) {
        this.contactInReceiverAddressBook = contactInReceiverAddressBook;
    }
}
