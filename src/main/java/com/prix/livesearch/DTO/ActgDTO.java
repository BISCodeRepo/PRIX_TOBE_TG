package com.prix.livesearch.DTO;

public class ActgDTO {

    private boolean failed;
    private boolean finished;
    private String output;
    private String processName;
    private String prixIndex;
    private String rate;
    private String title;
    private String index;
    private String databaseType;
    private String mappingMethod;

    // Getter 및 Setter 메서드

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getPrixIndex() {
        return prixIndex;
    }

    public void setPrixIndex(String prixIndex) {
        this.prixIndex = prixIndex;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getMappingMethod() {
        return mappingMethod;
    }

    public void setMappingMethod(String mappingMethod) {
        this.mappingMethod = mappingMethod;
    }
}

